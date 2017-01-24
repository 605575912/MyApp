from __future__ import with_statement
import sys
sys.path.insert(0, "/Users/apple/Documents/MyApp/hongbao/.buckd/resources/b7a7b9a85fa5c5dd2ff9ef062bb96b45e92b4448")
sys.path.insert(0, "/Users/apple/Documents/MyApp/hongbao/.buckd/resources/b7a7b9a85fa5c5dd2ff9ef062bb96b45e92b4448/path_to_pywatchman")
# ProjectBuildFileParser adds:
#
# from __future__ import with_statement
# import sys
# sys.path.insert(0, "/path/to/build/pywatchman")
# sys.path.insert(0, "/path/to/pathlib")

import __builtin__
import __future__
from collections import namedtuple
import functools
import imp
import inspect
from pathlib import Path, PureWindowsPath, PurePath
import optparse
import os
import os.path
from pywatchman import bser
import subprocess
import sys
import traceback

# When build files are executed, the functions in this file tagged with
# @provide_for_build will be provided in the build file's local symbol table.
#
# When these functions are called from a build file, they will be passed
# a keyword parameter, build_env, which is a object with information about
# the environment of the build file which is currently being processed.
# It contains the following attributes:
#
# "dirname" - The directory containing the build file.
#
# "base_path" - The base path of the build file.

BUILD_FUNCTIONS = []


class SyncCookieState(object):
    """
    Process-wide state used to enable Watchman sync cookies only on
    the first query issued.
    """

    def __init__(self):
        self.use_sync_cookies = True


class BuildContextType(object):

    """
    Identifies the type of input file to the processor.
    """

    BUILD_FILE = 'build_file'
    INCLUDE = 'include'


class BuildFileContext(object):
    """
    The build context used when processing a build file.
    """

    type = BuildContextType.BUILD_FILE

    def __init__(self, base_path, dirname, allow_empty_globs, watchman_client,
                 watchman_watch_root, watchman_project_prefix, sync_cookie_state,
                 watchman_error):
        self.globals = {}
        self.includes = set()
        self.used_configs = {}
        self.base_path = base_path
        self.dirname = dirname
        self.allow_empty_globs = allow_empty_globs
        self.watchman_client = watchman_client
        self.watchman_watch_root = watchman_watch_root
        self.watchman_project_prefix = watchman_project_prefix
        self.sync_cookie_state = sync_cookie_state
        self.watchman_error = watchman_error
        self.diagnostics = set()
        self.rules = {}


class IncludeContext(object):
    """
    The build context used when processing an include.
    """

    type = BuildContextType.INCLUDE

    def __init__(self):
        self.globals = {}
        self.includes = set()
        self.used_configs = {}
        self.diagnostics = set()


class LazyBuildEnvPartial(object):
    """Pairs a function with a build environment in which it will be executed.

    Note that while the function is specified via the constructor, the build
    environment must be assigned after construction, for the build environment
    currently being used.

    To call the function with its build environment, use the invoke() method of
    this class, which will forward the arguments from invoke() to the
    underlying function.
    """

    def __init__(self, func):
        self.func = func
        self.build_env = None

    def invoke(self, *args, **kwargs):
        """Invokes the bound function injecting 'build_env' into **kwargs."""
        updated_kwargs = kwargs.copy()
        updated_kwargs.update({'build_env': self.build_env})
        return self.func(*args, **updated_kwargs)


DiagnosticMessageAndLevel = namedtuple('DiagnosticMessageAndLevel', ['message', 'level'])


def provide_for_build(func):
    BUILD_FUNCTIONS.append(func)
    return func


def add_rule(rule, build_env):
    assert build_env.type == BuildContextType.BUILD_FILE, (
        "Cannot use `{}()` at the top-level of an included file."
        .format(rule['buck.type']))

    # Include the base path of the BUILD file so the reader consuming this
    # output will know which BUILD file the rule came from.
    if 'name' not in rule:
        raise ValueError(
            'rules must contain the field \'name\'.  Found %s.' % rule)
    rule_name = rule['name']
    if rule_name in build_env.rules:
        raise ValueError('Duplicate rule definition found.  Found %s and %s' %
                         (rule, build_env.rules[rule_name]))
    rule['buck.base_path'] = build_env.base_path
    build_env.rules[rule_name] = rule


class memoized(object):
    '''Decorator. Caches a function's return value each time it is called.
    If called later with the same arguments, the cached value is returned
    (not reevaluated).
    '''
    def __init__(self, func):
        self.func = func
        self.cache = {}

    def __call__(self, *args):
        args_key = repr(args)
        if args_key in self.cache:
            return self.cache[args_key]
        else:
            value = self.func(*args)
            self.cache[args_key] = value
            return value

    def __repr__(self):
        '''Return the function's docstring.'''
        return self.func.__doc__

    def __get__(self, obj, objtype):
        '''Support instance methods.'''
        return functools.partial(self.__call__, obj)


@provide_for_build
def glob(includes, excludes=[], include_dotfiles=False, build_env=None, search_base=None):
    assert build_env.type == BuildContextType.BUILD_FILE, (
        "Cannot use `glob()` at the top-level of an included file.")
    # Ensure the user passes lists of strings rather than just a string.
    assert not isinstance(includes, basestring), \
        "The first argument to glob() must be a list of strings."
    assert not isinstance(excludes, basestring), \
        "The excludes argument must be a list of strings."

    results = None
    if not includes:
        results = []
    elif build_env.watchman_client:
        try:
            results = glob_watchman(
                includes,
                excludes,
                include_dotfiles,
                build_env.base_path,
                build_env.watchman_watch_root,
                build_env.watchman_project_prefix,
                build_env.sync_cookie_state,
                build_env.watchman_client,
                build_env.diagnostics)
        except build_env.watchman_error as e:
            build_env.diagnostics.add(
                DiagnosticMessageAndLevel(
                    message='Watchman error, falling back to slow glob: {0}'.format(e),
                    level='error'))
            try:
                build_env.watchman_client.close()
            except:
                pass
            build_env.watchman_client = None

    if results is None:
        if search_base is None:
            search_base = Path(build_env.dirname)

        results = glob_internal(
            includes,
            excludes,
            include_dotfiles,
            search_base)
    assert build_env.allow_empty_globs or results, (
        "glob(includes={includes}, excludes={excludes}, include_dotfiles={include_dotfiles}) " +
        "returned no results.  (allow_empty_globs is set to false in the Buck " +
        "configuration)").format(
            includes=includes,
            excludes=excludes,
            include_dotfiles=include_dotfiles)

    return results


def merge_maps(*header_maps):
    result = {}
    for header_map in header_maps:
        for key in header_map:
            if key in result and result[key] != header_map[key]:
                assert False, 'Conflicting header files in header search paths. ' + \
                              '"%s" maps to both "%s" and "%s".' \
                              % (key, result[key], header_map[key])

            result[key] = header_map[key]

    return result


def single_subdir_glob(dirpath, glob_pattern, excludes=[], prefix=None, build_env=None,
                       search_base=None):
    results = {}
    files = glob([os.path.join(dirpath, glob_pattern)],
                 excludes=excludes,
                 build_env=build_env,
                 search_base=search_base)
    for f in files:
        if dirpath:
            key = f[len(dirpath) + 1:]
        else:
            key = f
        if prefix:
            # `f` is a string, but we need to create correct platform-specific Path.
            # Using Path straight away won't work because it will use host's class, which is Posix
            # on OS X. For the same reason we can't use os.path.join.
            # Here we try to understand if we're running Windows test, then use WindowsPath
            # to build up the key with prefix, allowing test to pass.
            cls = PureWindowsPath if "\\" in f else PurePath
            key = str(cls(prefix) / cls(key))
        results[key] = f

    return results


@provide_for_build
def subdir_glob(glob_specs, excludes=[], prefix=None, build_env=None, search_base=None):
    """
    Given a list of tuples, the form of (relative-sub-directory, glob-pattern),
    return a dict of sub-directory relative paths to full paths.  Useful for
    defining header maps for C/C++ libraries which should be relative the given
    sub-directory.

    If prefix is not None, prepends it it to each key in the dictionary.
    """

    results = []

    for dirpath, glob_pattern in glob_specs:
        results.append(
            single_subdir_glob(dirpath, glob_pattern, excludes, prefix, build_env, search_base))

    return merge_maps(*results)


def format_watchman_query_params(includes, excludes, include_dotfiles, relative_root):
    match_exprs = ["allof", "exists", ["anyof", ["type", "f"], ["type", "l"]]]
    match_flags = {}
    if include_dotfiles:
        match_flags["includedotfiles"] = True
    if includes:
        match_exprs.append(
            ["anyof"] + [["match", i, "wholename", match_flags] for i in includes])
    if excludes:
        match_exprs.append(
            ["not",
                ["anyof"] + [["match", x, "wholename", match_flags] for x in excludes]])

    return {
        "relative_root": relative_root,
        # Explicitly pass an empty path so Watchman queries only the tree of files
        # starting at base_path.
        "path": [''],
        "fields": ["name"],
        "expression": match_exprs,
    }


@memoized
def glob_watchman(includes, excludes, include_dotfiles, base_path, watchman_watch_root,
                  watchman_project_prefix, sync_cookie_state, watchman_client, diagnostics):
    assert includes, "The includes argument must be a non-empty list of strings."

    if watchman_project_prefix:
        relative_root = os.path.join(watchman_project_prefix, base_path)
    else:
        relative_root = base_path
    query_params = format_watchman_query_params(
        includes, excludes, include_dotfiles, relative_root)

    # Sync cookies cause a massive overhead when issuing thousands of
    # glob queries.  Only enable them (by not setting sync_timeout to 0)
    # for the very first request issued by this process.
    if sync_cookie_state.use_sync_cookies:
        sync_cookie_state.use_sync_cookies = False
    else:
        query_params["sync_timeout"] = 0

    query = ["query", watchman_watch_root, query_params]
    res = watchman_client.query(*query)
    if res.get('warning'):
        diagnostics.add(
            DiagnosticMessageAndLevel(
                message='Watchman warning: {0}'.format(res.get('warning')),
                level='warning'))
    result = res.get('files', [])
    return sorted(result)


def glob_internal(includes, excludes, include_dotfiles, search_base):

    def includes_iterator():
        for pattern in includes:
            for path in search_base.glob(pattern):
                # TODO(bhamiltoncx): Handle hidden files on Windows.
                if path.is_file() and (include_dotfiles or not path.name.startswith('.')):
                    yield path.relative_to(search_base)

    def is_special(pat):
        return "*" in pat or "?" in pat or "[" in pat

    non_special_excludes = set()
    match_excludes = set()
    for pattern in excludes:
        if is_special(pattern):
            match_excludes.add(pattern)
        else:
            non_special_excludes.add(pattern)

    def exclusion(path):
        if path.as_posix() in non_special_excludes:
            return True
        for pattern in match_excludes:
            result = path.match(pattern, match_entire=True)
            if result:
                return True
        return False

    return sorted(set([str(p) for p in includes_iterator() if not exclusion(p)]))


@provide_for_build
def get_base_path(build_env=None):
    """Get the base path to the build file that was initially evaluated.

    This function is intended to be used from within a build defs file that
    likely contains macros that could be called from any build file.
    Such macros may need to know the base path of the file in which they
    are defining new build rules.

    Returns: a string, such as "java/com/facebook". Note there is no
             trailing slash. The return value will be "" if called from
             the build file in the root of the project.
    """
    assert build_env.type == BuildContextType.BUILD_FILE, (
        "Cannot use `get_base_path()` at the top-level of an included file.")
    return build_env.base_path


@provide_for_build
def add_deps(name, deps=[], build_env=None):
    assert build_env.type == BuildContextType.BUILD_FILE, (
        "Cannot use `add_deps()` at the top-level of an included file.")

    if name not in build_env.rules:
        raise ValueError(
            'Invoked \'add_deps\' on non-existent rule %s.' % name)

    rule = build_env.rules[name]
    if 'deps' not in rule:
        raise ValueError(
            'Invoked \'add_deps\' on rule %s that has no \'deps\' field'
            % name)
    rule['deps'] = rule['deps'] + deps


class BuildFileProcessor(object):

    def __init__(self, project_root, watchman_watch_root, watchman_project_prefix, build_file_name,
                 allow_empty_globs, watchman_client, watchman_error, implicit_includes=[],
                 extra_funcs=[], configs={}):
        self._cache = {}
        self._build_env_stack = []
        self._sync_cookie_state = SyncCookieState()

        self._project_root = project_root
        self._watchman_watch_root = watchman_watch_root
        self._watchman_project_prefix = watchman_project_prefix
        self._build_file_name = build_file_name
        self._implicit_includes = implicit_includes
        self._allow_empty_globs = allow_empty_globs
        self._watchman_client = watchman_client
        self._watchman_error = watchman_error
        self._configs = configs

        lazy_functions = {}
        for func in BUILD_FUNCTIONS + extra_funcs:
            func_with_env = LazyBuildEnvPartial(func)
            lazy_functions[func.__name__] = func_with_env
        self._functions = lazy_functions

    def _merge_globals(self, mod, dst):
        """
        Copy the global definitions from one globals dict to another.

        Ignores special attributes and attributes starting with '_', which
        typically denote module-level private attributes.
        """

        hidden = set([
            'include_defs',
        ])

        keys = getattr(mod, '__all__', mod.__dict__.keys())

        for key in keys:
            if not key.startswith('_') and key not in hidden:
                dst[key] = mod.__dict__[key]

    def _update_functions(self, build_env):
        """
        Updates the build functions to use the given build context when called.
        """

        for function in self._functions.itervalues():
            function.build_env = build_env

    def install_builtins(self, namespace):
        """
        Installs the build functions, by their name, into the given namespace.
        """

        for name, function in self._functions.iteritems():
            namespace[name] = function.invoke

    def _get_include_path(self, name):
        """
        Resolve the given include def name to a full path.
        """

        # Find the path from the include def name.
        if not name.startswith('//'):
            raise ValueError(
                'include_defs argument "%s" must begin with //' % name)
        relative_path = name[2:]
        return os.path.join(self._project_root, relative_path)

    def _read_config(self, section, field, default=None):
        """
        Lookup a setting from `.buckconfig`.

        This method is meant to be installed into the globals of any files or
        includes that we process.
        """

        # Grab the current build context from the top of the stack.
        build_env = self._build_env_stack[-1]

        # Lookup the value and record it in this build file's context.
        value = self._configs.get((section, field))
        build_env.used_configs[(section, field)] = value

        # If no config setting was found, return the default.
        if value is None:
            return default

        return value

    def _include_defs(self, name, implicit_includes=[]):
        """
        Pull the named include into the current caller's context.

        This method is meant to be installed into the globals of any files or
        includes that we process.
        """

        # Grab the current build context from the top of the stack.
        build_env = self._build_env_stack[-1]

        # Resolve the named include to its path and process it to get its
        # build context and module.
        path = self._get_include_path(name)
        inner_env, mod = self._process_include(
            path,
            implicit_includes=implicit_includes)

        # Look up the caller's stack frame and merge the include's globals
        # into it's symbol table.
        frame = inspect.currentframe()
        while frame.f_globals['__name__'] in (__name__, '_functools'):
            frame = frame.f_back
        self._merge_globals(mod, frame.f_globals)

        # Pull in the include's accounting of its own referenced includes
        # into the current build context.
        build_env.includes.add(path)
        build_env.includes.update(inner_env.includes)

        # Pull in any diagnostics issued by the include.
        build_env.diagnostics.update(inner_env.diagnostics)

        # Pull in any config settings used by the include.
        build_env.used_configs.update(inner_env.used_configs)

    def _push_build_env(self, build_env):
        """
        Set the given build context as the current context.
        """

        self._build_env_stack.append(build_env)
        self._update_functions(build_env)

    def _pop_build_env(self):
        """
        Restore the previous build context as the current context.
        """

        self._build_env_stack.pop()
        if self._build_env_stack:
            self._update_functions(self._build_env_stack[-1])

    def _process(self, build_env, path, implicit_includes=[]):
        """
        Process a build file or include at the given path.
        """

        # First check the cache.
        cached = self._cache.get(path)
        if cached is not None:
            return cached

        # Install the build context for this input as the current context.
        self._push_build_env(build_env)

        # The globals dict that this file will be executed under.
        default_globals = {}

        # Install the 'include_defs' function into our global object.
        default_globals['include_defs'] = functools.partial(
            self._include_defs,
            implicit_includes=implicit_includes)

        # Install the 'read_config' function into our global object.
        default_globals['read_config'] = self._read_config

        # If any implicit includes were specified, process them first.
        for include in implicit_includes:
            include_path = self._get_include_path(include)
            inner_env, mod = self._process_include(include_path)
            self._merge_globals(mod, default_globals)
            build_env.includes.add(include_path)
            build_env.includes.update(inner_env.includes)
            build_env.diagnostics.update(inner_env.diagnostics)

        # Build a new module for the given file, using the default globals
        # created above.
        module = imp.new_module(path)
        module.__file__ = path
        module.__dict__.update(default_globals)

        with open(path) as f:
            contents = f.read()

        # Enable absolute imports.  This prevents the compiler from trying to
        # do a relative import first, and warning that this module doesn't
        # exist in sys.modules.
        future_features = __future__.absolute_import.compiler_flag
        code = compile(contents, path, 'exec', future_features, 1)
        exec(code, module.__dict__)

        # Restore the previous build context.
        self._pop_build_env()

        self._cache[path] = build_env, module
        return build_env, module

    def _process_include(self, path, implicit_includes=[]):
        """
        Process the include file at the given path.
        """

        build_env = IncludeContext()
        return self._process(
            build_env,
            path,
            implicit_includes=implicit_includes)

    def _process_build_file(self, path, implicit_includes=[]):
        """
        Process the build file at the given path.
        """

        # Create the build file context, including the base path and directory
        # name of the given path.
        relative_path_to_build_file = os.path.relpath(
            path, self._project_root).replace('\\', '/')
        len_suffix = -len('/' + self._build_file_name)
        base_path = relative_path_to_build_file[:len_suffix]
        dirname = os.path.dirname(path)
        build_env = BuildFileContext(
            base_path,
            dirname,
            self._allow_empty_globs,
            self._watchman_client,
            self._watchman_watch_root,
            self._watchman_project_prefix,
            self._sync_cookie_state,
            self._watchman_error)

        return self._process(
            build_env,
            path,
            implicit_includes=implicit_includes)

    def process(self, path, diagnostics):
        """
        Process a build file returning a dict of it's rules and includes.
        """
        build_env, mod = self._process_build_file(
            os.path.join(self._project_root, path),
            implicit_includes=self._implicit_includes)

        # Initialize the output object to a map of the parsed rules.
        values = build_env.rules.values()

        # Add in tracked included files as a special meta rule.
        values.append({"__includes": [path] + sorted(build_env.includes)})

        # Add in tracked used config settings as a special meta rule.
        configs = {}
        for (section, field), value in build_env.used_configs.iteritems():
            configs.setdefault(section, {})
            configs[section][field] = value
        values.append({"__configs": configs})

        diagnostics.update(build_env.diagnostics)

        return values


def cygwin_adjusted_path(path):
    if sys.platform == 'cygwin':
        return subprocess.check_output(['cygpath', path]).rstrip()
    else:
        return path


def encode_result(values, diagnostics):
    result = {'values': values}
    if diagnostics:
        encoded_diagnostics = []
        for d in diagnostics:
            encoded_diagnostics.append({
                'message': d.message,
                'level': d.level,
            })
        result['diagnostics'] = encoded_diagnostics
    return bser.dumps(result)


def filter_tb(entries):
    for i in range(len(entries)):
        # Filter out the beginning of the stack trace (any entries including the buck.py file)
        if entries[i][0] != sys.argv[0]:
            return entries[i:]
    return []


def format_traceback_and_exception():
    exc_type, exc_value, exc_traceback = sys.exc_info()
    filtered_traceback = filter_tb(traceback.extract_tb(exc_traceback))
    formatted_traceback = ''.join(traceback.format_list(filtered_traceback))
    formatted_exception = ''.join(traceback.format_exception_only(exc_type, exc_value))
    return formatted_traceback + formatted_exception


def process_with_diagnostics(build_file, build_file_processor, to_parent):
    build_file = cygwin_adjusted_path(build_file)
    diagnostics = set()
    values = []
    try:
        values = build_file_processor.process(build_file.rstrip(), diagnostics=diagnostics)
    except Exception as e:
        # Control-C and sys.exit() don't emit diagnostics.
        if not (e is KeyboardInterrupt or e is SystemExit):
            diagnostics.add(
                DiagnosticMessageAndLevel(
                    message=format_traceback_and_exception(),
                    level='fatal'))
        raise e
    finally:
        to_parent.write(encode_result(values, diagnostics))
        to_parent.flush()


def silent_excepthook(exctype, value, tb):
    # We already handle all exceptions by writing them to the parent, so
    # no need to dump them again to stderr.
    pass

# Inexplicably, this script appears to run faster when the arguments passed
# into it are absolute paths. However, we want the "buck.base_path" property
# of each rule to be printed out to be the base path of the build target that
# identifies the rule. That means that when parsing a BUILD file, we must know
# its path relative to the root of the project to produce the base path.
#
# To that end, the first argument to this script must be an absolute path to
# the project root.  It must be followed by one or more absolute paths to
# BUILD files under the project root.  If no paths to BUILD files are
# specified, then it will traverse the project root for BUILD files, excluding
# directories of generated files produced by Buck.
#
# All of the build rules that are parsed from the BUILD files will be printed
# to stdout encoded in BSER. That means that printing out other information
# for debugging purposes will break the BSER encoding, so be careful!


def main():
    # Our parent expects to read BSER from our stdout, so if anyone
    # uses print, buck will complain with a helpful "but I wanted an
    # array!" message and quit.  Redirect stdout to stderr so that
    # doesn't happen.  Actually dup2 the file handle so that writing
    # to file descriptor 1, os.system, and so on work as expected too.

    to_parent = os.fdopen(os.dup(sys.stdout.fileno()), 'a')
    os.dup2(sys.stderr.fileno(), sys.stdout.fileno())

    parser = optparse.OptionParser()
    parser.add_option(
        '--project_root',
        action='store',
        type='string',
        dest='project_root')
    parser.add_option(
        '--build_file_name',
        action='store',
        type='string',
        dest="build_file_name")
    parser.add_option(
        '--allow_empty_globs',
        action='store_true',
        dest='allow_empty_globs',
        help='Tells the parser not to raise an error when glob returns no results.')
    parser.add_option(
        '--use_watchman_glob',
        action='store_true',
        dest='use_watchman_glob',
        help='Invokes `watchman query` to get lists of files instead of globbing in-process.')
    parser.add_option(
        '--watchman_watch_root',
        action='store',
        type='string',
        dest='watchman_watch_root',
        help='Path to root of watchman watch as returned by `watchman watch-project`.')
    parser.add_option(
        '--watchman_socket_path',
        action='store',
        type='string',
        dest='watchman_socket_path',
        help='Path to Unix domain socket/named pipe as returned by `watchman get-sockname`.')
    parser.add_option(
        '--watchman_project_prefix',
        action='store',
        type='string',
        dest='watchman_project_prefix',
        help='Relative project prefix as returned by `watchman watch-project`.')
    parser.add_option(
        '--watchman_query_timeout_ms',
        action='store',
        type='int',
        dest='watchman_query_timeout_ms',
        help='Maximum time in milliseconds to wait for watchman query to respond.')
    parser.add_option(
        '--include',
        action='append',
        dest='include')
    parser.add_option(
        '--config',
        help='BuckConfig settings available at parse time.')
    parser.add_option(
        '--quiet',
        action='store_true',
        dest='quiet',
        help='Stifles exception backtraces printed to stderr during parsing.')
    (options, args) = parser.parse_args()

    # Even though project_root is absolute path, it may not be concise. For
    # example, it might be like "C:\project\.\rule".
    #
    # Under cygwin, the project root will be invoked from buck as C:\path, but
    # the cygwin python uses UNIX-style paths. They can be converted using
    # cygpath, which is necessary because abspath will treat C:\path as a
    # relative path.
    options.project_root = cygwin_adjusted_path(options.project_root)
    project_root = os.path.abspath(options.project_root)

    watchman_client = None
    watchman_error = None
    if options.use_watchman_glob:
        import pywatchman
        client_args = {}
        if options.watchman_query_timeout_ms is not None:
            # pywatchman expects a timeout as a nonnegative floating-point
            # value in seconds.
            client_args['timeout'] = max(0.0, options.watchman_query_timeout_ms / 1000.0)
        if options.watchman_socket_path is not None:
            client_args['sockpath'] = options.watchman_socket_path
            client_args['transport'] = 'local'
        watchman_client = pywatchman.client(**client_args)
        watchman_error = pywatchman.WatchmanError

    configs = {}
    if options.config is not None:
        with open(options.config) as f:
            for section, contents in bser.loads(f.read()).iteritems():
                for field, value in contents.iteritems():
                    configs[(section, field)] = value

    buildFileProcessor = BuildFileProcessor(
        project_root,
        options.watchman_watch_root,
        options.watchman_project_prefix,
        options.build_file_name,
        options.allow_empty_globs,
        watchman_client,
        watchman_error,
        implicit_includes=options.include or [],
        configs=configs)

    buildFileProcessor.install_builtins(__builtin__.__dict__)

    # While processing, we'll write exceptions as diagnostic messages
    # to the parent then re-raise them to crash the process. While
    # doing so, we don't want Python's default unhandled exception
    # behavior of writing to stderr.
    orig_excepthook = None
    if options.quiet:
        orig_excepthook = sys.excepthook
        sys.excepthook = silent_excepthook

    for build_file in args:
        process_with_diagnostics(build_file, buildFileProcessor, to_parent)

    # "for ... in sys.stdin" in Python 2.x hangs until stdin is closed.
    for build_file in iter(sys.stdin.readline, ''):
        process_with_diagnostics(build_file, buildFileProcessor, to_parent)

    if options.quiet:
        sys.excepthook = orig_excepthook

    # Python tries to flush/close stdout when it quits, and if there's a dead
    # pipe on the other end, it will spit some warnings to stderr. This breaks
    # tests sometimes. Prevent that by explicitly catching the error.
    try:
        to_parent.close()
    except IOError:
        pass


@provide_for_build
def prebuilt_jar(name, binary_jar, deps=[], gwt_jar=None, javadoc_url=None, maven_coords=None, source_jar=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'prebuilt_jar',
    'name' : name,
    'binaryJar' : binary_jar,
    'deps' : deps,
    'gwtJar' : gwt_jar,
    'javadocUrl' : javadoc_url,
    'mavenCoords' : maven_coords,
    'sourceJar' : source_jar,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def android_instrumentation_apk(name, apk, manifest, deps=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'android_instrumentation_apk',
    'name' : name,
    'apk' : apk,
    'manifest' : manifest,
    'deps' : deps,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def gwt_binary(name, deps=[], draft_compile=None, experimental_args=[], local_workers=None, module_deps=[], modules=[], optimize=None, strict=None, style=None, vm_args=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'gwt_binary',
    'name' : name,
    'deps' : deps,
    'draftCompile' : draft_compile,
    'experimentalArgs' : experimental_args,
    'localWorkers' : local_workers,
    'moduleDeps' : module_deps,
    'modules' : modules,
    'optimize' : optimize,
    'strict' : strict,
    'style' : style,
    'vmArgs' : vm_args,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def ios_react_native_library(name, bundle_name, entry_path, deps=[], packager_flags=None, srcs=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'ios_react_native_library',
    'name' : name,
    'bundleName' : bundle_name,
    'entryPath' : entry_path,
    'deps' : deps,
    'packagerFlags' : packager_flags,
    'srcs' : srcs,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def d_binary(name, srcs, deps=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'd_binary',
    'name' : name,
    'srcs' : srcs,
    'deps' : deps,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def apple_asset_catalog(name, dirs, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'apple_asset_catalog',
    'name' : name,
    'dirs' : dirs,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def android_build_config(name, package, values=None, values_file=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'android_build_config',
    'name' : name,
    'javaPackage' : package,
    'values' : values,
    'valuesFile' : values_file,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def groovy_test(name, annotation_processor_deps=[], annotation_processor_only=None, annotation_processor_params=[], annotation_processors=[], compiler=None, contacts=[], deps=[], exported_deps=[], extra_arguments=[], extra_groovyc_arguments=[], java_version=None, javac=None, javac_jar=None, labels=[], provided_deps=[], resources=[], run_test_separately=None, source=None, srcs=[], std_err_log_level=None, std_out_log_level=None, target=None, test_rule_timeout_ms=None, test_type=None, vm_args=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'groovy_test',
    'name' : name,
    'annotationProcessorDeps' : annotation_processor_deps,
    'annotationProcessorOnly' : annotation_processor_only,
    'annotationProcessorParams' : annotation_processor_params,
    'annotationProcessors' : annotation_processors,
    'compiler' : compiler,
    'contacts' : contacts,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'extraArguments' : extra_arguments,
    'extraGroovycArguments' : extra_groovyc_arguments,
    'javaVersion' : java_version,
    'javac' : javac,
    'javacJar' : javac_jar,
    'labels' : labels,
    'providedDeps' : provided_deps,
    'resources' : resources,
    'runTestSeparately' : run_test_separately,
    'source' : source,
    'srcs' : srcs,
    'stdErrLogLevel' : std_err_log_level,
    'stdOutLogLevel' : std_out_log_level,
    'target' : target,
    'testRuleTimeoutMs' : test_rule_timeout_ms,
    'testType' : test_type,
    'vmArgs' : vm_args,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def java_library(name, annotation_processor_deps=[], annotation_processor_only=None, annotation_processor_params=[], annotation_processors=[], compiler=None, deps=[], exported_deps=[], extra_arguments=[], java_version=None, javac=None, javac_jar=None, maven_coords=None, postprocess_classes_commands=[], proguard_config=None, provided_deps=[], resources=[], resources_root=None, source=None, srcs=[], target=None, tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'java_library',
    'name' : name,
    'annotationProcessorDeps' : annotation_processor_deps,
    'annotationProcessorOnly' : annotation_processor_only,
    'annotationProcessorParams' : annotation_processor_params,
    'annotationProcessors' : annotation_processors,
    'compiler' : compiler,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'extraArguments' : extra_arguments,
    'javaVersion' : java_version,
    'javac' : javac,
    'javacJar' : javac_jar,
    'mavenCoords' : maven_coords,
    'postprocessClassesCommands' : postprocess_classes_commands,
    'proguardConfig' : proguard_config,
    'providedDeps' : provided_deps,
    'resources' : resources,
    'resourcesRoot' : resources_root,
    'source' : source,
    'srcs' : srcs,
    'target' : target,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def apple_bundle(name, binary, extension, info_plist, deps=[], info_plist_substitutions={}, product_name=None, tests=[], xcode_product_type=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'apple_bundle',
    'name' : name,
    'binary' : binary,
    'extension' : extension,
    'infoPlist' : info_plist,
    'deps' : deps,
    'infoPlistSubstitutions' : info_plist_substitutions,
    'productName' : product_name,
    'tests' : tests,
    'xcodeProductType' : xcode_product_type,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def export_file(name, out=None, src=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'export_file',
    'name' : name,
    'out' : out,
    'src' : src,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def apple_resource(name, dirs, files, variants=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'apple_resource',
    'name' : name,
    'dirs' : dirs,
    'files' : files,
    'variants' : variants,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def groovy_library(name, annotation_processor_deps=[], annotation_processor_only=None, annotation_processor_params=[], annotation_processors=[], compiler=None, deps=[], exported_deps=[], extra_arguments=[], extra_groovyc_arguments=[], java_version=None, javac=None, javac_jar=None, provided_deps=[], resources=[], source=None, srcs=[], target=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'groovy_library',
    'name' : name,
    'annotationProcessorDeps' : annotation_processor_deps,
    'annotationProcessorOnly' : annotation_processor_only,
    'annotationProcessorParams' : annotation_processor_params,
    'annotationProcessors' : annotation_processors,
    'compiler' : compiler,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'extraArguments' : extra_arguments,
    'extraGroovycArguments' : extra_groovyc_arguments,
    'javaVersion' : java_version,
    'javac' : javac,
    'javacJar' : javac_jar,
    'providedDeps' : provided_deps,
    'resources' : resources,
    'source' : source,
    'srcs' : srcs,
    'target' : target,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def ocaml_library(name, compiler_flags=[], deps=[], linker_flags=[], srcs=[], warnings_flags=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'ocaml_library',
    'name' : name,
    'compilerFlags' : compiler_flags,
    'deps' : deps,
    'linkerFlags' : linker_flags,
    'srcs' : srcs,
    'warningsFlags' : warnings_flags,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def lua_library(name, base_module=None, deps=[], srcs=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'lua_library',
    'name' : name,
    'baseModule' : base_module,
    'deps' : deps,
    'srcs' : srcs,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def project_config(is_intellij_plugin=None, src_roots=[], src_target=None, test_roots=[], test_target=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'project_config',
    'name' : 'project_config',
    'isIntellijPlugin' : is_intellij_plugin,
    'srcRoots' : src_roots,
    'srcTarget' : src_target,
    'testRoots' : test_roots,
    'testTarget' : test_target,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def rust_library(name, srcs, deps=[], features=[], rustc_flags=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'rust_library',
    'name' : name,
    'srcs' : srcs,
    'deps' : deps,
    'features' : features,
    'rustcFlags' : rustc_flags,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def ocaml_binary(name, compiler_flags=[], deps=[], linker_flags=[], srcs=[], warnings_flags=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'ocaml_binary',
    'name' : name,
    'compilerFlags' : compiler_flags,
    'deps' : deps,
    'linkerFlags' : linker_flags,
    'srcs' : srcs,
    'warningsFlags' : warnings_flags,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def lua_binary(name, main_module, deps=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'lua_binary',
    'name' : name,
    'mainModule' : main_module,
    'deps' : deps,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def prebuilt_ocaml_library(name, bytecode_lib=None, c_libs=[], deps=[], include_dir=None, lib_dir=None, lib_name=None, native_lib=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'prebuilt_ocaml_library',
    'name' : name,
    'bytecodeLib' : bytecode_lib,
    'cLibs' : c_libs,
    'deps' : deps,
    'includeDir' : include_dir,
    'libDir' : lib_dir,
    'libName' : lib_name,
    'nativeLib' : native_lib,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def scala_test(name, contacts=[], deps=[], extra_arguments=[], labels=[], maven_coords=None, provided_deps=[], resources=[], resources_root=None, run_test_separately=None, srcs=[], std_err_log_level=None, std_out_log_level=None, test_rule_timeout_ms=None, test_type=None, tests=[], use_cxx_libraries=None, vm_args=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'scala_test',
    'name' : name,
    'contacts' : contacts,
    'deps' : deps,
    'extraArguments' : extra_arguments,
    'labels' : labels,
    'mavenCoords' : maven_coords,
    'providedDeps' : provided_deps,
    'resources' : resources,
    'resourcesRoot' : resources_root,
    'runTestSeparately' : run_test_separately,
    'srcs' : srcs,
    'stdErrLogLevel' : std_err_log_level,
    'stdOutLogLevel' : std_out_log_level,
    'testRuleTimeoutMs' : test_rule_timeout_ms,
    'testType' : test_type,
    'tests' : tests,
    'useCxxLibraries' : use_cxx_libraries,
    'vmArgs' : vm_args,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def android_binary(name, keystore, manifest, android_sdk_proguard_config=None, build_config_values=None, build_config_values_file=None, build_string_source_map=None, compress_asset_libraries=None, cpu_filters=[], deps=[], dex_compression=None, dex_reorder_data_dump_file=None, dex_reorder_tool_file=None, disable_pre_dex=None, exopackage=None, exopackage_modes=[], linear_alloc_hard_limit=None, locales=[], minimize_primary_dex_size=None, no_dx=[], optimization_passes=None, package_asset_libraries=None, package_type=None, preprocess_java_classes_bash=None, preprocess_java_classes_deps=[], primary_dex_classes_file=None, primary_dex_patterns=[], primary_dex_scenario_file=None, primary_dex_scenario_overflow_allowed=None, proguard_config=None, reorder_classes_intra_dex=None, resource_compression=None, resource_filter=[], resource_union_package=None, secondary_dex_head_classes_file=None, secondary_dex_tail_classes_file=None, skip_crunch_pngs=None, use_android_proguard_config_with_optimizations=None, use_linear_alloc_split_dex=None, use_split_dex=None, xz_compression_level=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'android_binary',
    'name' : name,
    'keystore' : keystore,
    'manifest' : manifest,
    'androidSdkProguardConfig' : android_sdk_proguard_config,
    'buildConfigValues' : build_config_values,
    'buildConfigValuesFile' : build_config_values_file,
    'buildStringSourceMap' : build_string_source_map,
    'compressAssetLibraries' : compress_asset_libraries,
    'cpuFilters' : cpu_filters,
    'deps' : deps,
    'dexCompression' : dex_compression,
    'dexReorderDataDumpFile' : dex_reorder_data_dump_file,
    'dexReorderToolFile' : dex_reorder_tool_file,
    'disablePreDex' : disable_pre_dex,
    'exopackage' : exopackage,
    'exopackageModes' : exopackage_modes,
    'linearAllocHardLimit' : linear_alloc_hard_limit,
    'locales' : locales,
    'minimizePrimaryDexSize' : minimize_primary_dex_size,
    'noDx' : no_dx,
    'optimizationPasses' : optimization_passes,
    'packageAssetLibraries' : package_asset_libraries,
    'packageType' : package_type,
    'preprocessJavaClassesBash' : preprocess_java_classes_bash,
    'preprocessJavaClassesDeps' : preprocess_java_classes_deps,
    'primaryDexClassesFile' : primary_dex_classes_file,
    'primaryDexPatterns' : primary_dex_patterns,
    'primaryDexScenarioFile' : primary_dex_scenario_file,
    'primaryDexScenarioOverflowAllowed' : primary_dex_scenario_overflow_allowed,
    'proguardConfig' : proguard_config,
    'reorderClassesIntraDex' : reorder_classes_intra_dex,
    'resourceCompression' : resource_compression,
    'resourceFilter' : resource_filter,
    'resourceUnionPackage' : resource_union_package,
    'secondaryDexHeadClassesFile' : secondary_dex_head_classes_file,
    'secondaryDexTailClassesFile' : secondary_dex_tail_classes_file,
    'skipCrunchPngs' : skip_crunch_pngs,
    'useAndroidProguardConfigWithOptimizations' : use_android_proguard_config_with_optimizations,
    'useLinearAllocSplitDex' : use_linear_alloc_split_dex,
    'useSplitDex' : use_split_dex,
    'xzCompressionLevel' : xz_compression_level,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def android_manifest(name, skeleton, deps=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'android_manifest',
    'name' : name,
    'skeleton' : skeleton,
    'deps' : deps,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def android_instrumentation_test(name, apk, contacts=[], labels=[], source_under_test=[], test_rule_timeout_ms=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'android_instrumentation_test',
    'name' : name,
    'apk' : apk,
    'contacts' : contacts,
    'labels' : labels,
    'sourceUnderTest' : source_under_test,
    'testRuleTimeoutMs' : test_rule_timeout_ms,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def prebuilt_python_library(name, binary_src, deps=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'prebuilt_python_library',
    'name' : name,
    'binarySrc' : binary_src,
    'deps' : deps,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def scala_library(name, deps=[], extra_arguments=[], maven_coords=None, provided_deps=[], resources=[], resources_root=None, srcs=[], tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'scala_library',
    'name' : name,
    'deps' : deps,
    'extraArguments' : extra_arguments,
    'mavenCoords' : maven_coords,
    'providedDeps' : provided_deps,
    'resources' : resources,
    'resourcesRoot' : resources_root,
    'srcs' : srcs,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def go_test(name, srcs, compiler_flags=[], contacts=[], deps=[], labels=[], linker_flags=[], package_name=None, run_test_separately=None, test_rule_timeout_ms=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'go_test',
    'name' : name,
    'srcs' : srcs,
    'compilerFlags' : compiler_flags,
    'contacts' : contacts,
    'deps' : deps,
    'labels' : labels,
    'linkerFlags' : linker_flags,
    'packageName' : package_name,
    'runTestSeparately' : run_test_separately,
    'testRuleTimeoutMs' : test_rule_timeout_ms,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def ndk_library(name, deps=[], flags=[], is_asset=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'ndk_library',
    'name' : name,
    'deps' : deps,
    'flags' : flags,
    'isAsset' : is_asset,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def android_aar(name, manifest_skeleton, annotation_processor_deps=[], annotation_processor_only=None, annotation_processor_params=[], annotation_processors=[], compiler=None, deps=[], exported_deps=[], extra_arguments=[], java_version=None, javac=None, javac_jar=None, manifest=None, maven_coords=None, postprocess_classes_commands=[], proguard_config=None, provided_deps=[], resource_union_package=None, resources=[], resources_root=None, source=None, srcs=[], target=None, tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'android_aar',
    'name' : name,
    'manifestSkeleton' : manifest_skeleton,
    'annotationProcessorDeps' : annotation_processor_deps,
    'annotationProcessorOnly' : annotation_processor_only,
    'annotationProcessorParams' : annotation_processor_params,
    'annotationProcessors' : annotation_processors,
    'compiler' : compiler,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'extraArguments' : extra_arguments,
    'javaVersion' : java_version,
    'javac' : javac,
    'javacJar' : javac_jar,
    'manifest' : manifest,
    'mavenCoords' : maven_coords,
    'postprocessClassesCommands' : postprocess_classes_commands,
    'proguardConfig' : proguard_config,
    'providedDeps' : provided_deps,
    'resourceUnionPackage' : resource_union_package,
    'resources' : resources,
    'resourcesRoot' : resources_root,
    'source' : source,
    'srcs' : srcs,
    'target' : target,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def apple_binary(name, can_be_asset=None, compiler_flags=[], configs={}, cxx_runtime_type=None, deps=[], exported_deps=[], exported_headers=None, exported_lang_preprocessor_flags={}, exported_linker_flags=[], exported_platform_headers=None, exported_platform_linker_flags=None, exported_platform_preprocessor_flags=None, exported_preprocessor_flags=[], extra_xcode_sources=[], force_static=None, frameworks=[], header_namespace=None, header_path_prefix=None, headers=None, info_plist=None, info_plist_substitutions={}, lang_preprocessor_flags={}, libraries=[], link_style=None, link_whole=None, linker_flags=[], platform_compiler_flags=None, platform_headers=None, platform_linker_flags=None, platform_preprocessor_flags=None, platform_srcs=None, prefix_header=None, preprocessor_flags=[], soname=None, srcs=[], supported_platforms_regex=None, tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'apple_binary',
    'name' : name,
    'canBeAsset' : can_be_asset,
    'compilerFlags' : compiler_flags,
    'configs' : configs,
    'cxxRuntimeType' : cxx_runtime_type,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'exportedHeaders' : exported_headers,
    'exportedLangPreprocessorFlags' : exported_lang_preprocessor_flags,
    'exportedLinkerFlags' : exported_linker_flags,
    'exportedPlatformHeaders' : exported_platform_headers,
    'exportedPlatformLinkerFlags' : exported_platform_linker_flags,
    'exportedPlatformPreprocessorFlags' : exported_platform_preprocessor_flags,
    'exportedPreprocessorFlags' : exported_preprocessor_flags,
    'extraXcodeSources' : extra_xcode_sources,
    'forceStatic' : force_static,
    'frameworks' : frameworks,
    'headerNamespace' : header_namespace,
    'headerPathPrefix' : header_path_prefix,
    'headers' : headers,
    'infoPlist' : info_plist,
    'infoPlistSubstitutions' : info_plist_substitutions,
    'langPreprocessorFlags' : lang_preprocessor_flags,
    'libraries' : libraries,
    'linkStyle' : link_style,
    'linkWhole' : link_whole,
    'linkerFlags' : linker_flags,
    'platformCompilerFlags' : platform_compiler_flags,
    'platformHeaders' : platform_headers,
    'platformLinkerFlags' : platform_linker_flags,
    'platformPreprocessorFlags' : platform_preprocessor_flags,
    'platformSrcs' : platform_srcs,
    'prefixHeader' : prefix_header,
    'preprocessorFlags' : preprocessor_flags,
    'soname' : soname,
    'srcs' : srcs,
    'supportedPlatformsRegex' : supported_platforms_regex,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def cxx_python_extension(name, base_module=None, compiler_flags=[], cxx_runtime_type=None, deps=[], frameworks=[], header_namespace=None, headers=None, lang_preprocessor_flags={}, libraries=[], linker_flags=[], platform_compiler_flags=None, platform_deps=None, platform_headers=None, platform_linker_flags=None, platform_preprocessor_flags=None, platform_srcs=None, prefix_header=None, preprocessor_flags=[], srcs=[], tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'cxx_python_extension',
    'name' : name,
    'baseModule' : base_module,
    'compilerFlags' : compiler_flags,
    'cxxRuntimeType' : cxx_runtime_type,
    'deps' : deps,
    'frameworks' : frameworks,
    'headerNamespace' : header_namespace,
    'headers' : headers,
    'langPreprocessorFlags' : lang_preprocessor_flags,
    'libraries' : libraries,
    'linkerFlags' : linker_flags,
    'platformCompilerFlags' : platform_compiler_flags,
    'platformDeps' : platform_deps,
    'platformHeaders' : platform_headers,
    'platformLinkerFlags' : platform_linker_flags,
    'platformPreprocessorFlags' : platform_preprocessor_flags,
    'platformSrcs' : platform_srcs,
    'prefixHeader' : prefix_header,
    'preprocessorFlags' : preprocessor_flags,
    'srcs' : srcs,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def xcode_prebuild_script(name, cmd, outputs=[], srcs=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'xcode_prebuild_script',
    'name' : name,
    'cmd' : cmd,
    'outputs' : outputs,
    'srcs' : srcs,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def robolectric_test(name, annotation_processor_deps=[], annotation_processor_only=None, annotation_processor_params=[], annotation_processors=[], compiler=None, contacts=[], deps=[], exported_deps=[], extra_arguments=[], java_version=None, javac=None, javac_jar=None, labels=[], maven_coords=None, path_to_java_agent=None, postprocess_classes_commands=[], proguard_config=None, provided_deps=[], resources=[], resources_root=None, run_test_separately=None, source=None, source_under_test=[], srcs=[], std_err_log_level=None, std_out_log_level=None, target=None, test_rule_timeout_ms=None, test_type=None, tests=[], use_cxx_libraries=None, vm_args=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'robolectric_test',
    'name' : name,
    'annotationProcessorDeps' : annotation_processor_deps,
    'annotationProcessorOnly' : annotation_processor_only,
    'annotationProcessorParams' : annotation_processor_params,
    'annotationProcessors' : annotation_processors,
    'compiler' : compiler,
    'contacts' : contacts,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'extraArguments' : extra_arguments,
    'javaVersion' : java_version,
    'javac' : javac,
    'javacJar' : javac_jar,
    'labels' : labels,
    'mavenCoords' : maven_coords,
    'pathToJavaAgent' : path_to_java_agent,
    'postprocessClassesCommands' : postprocess_classes_commands,
    'proguardConfig' : proguard_config,
    'providedDeps' : provided_deps,
    'resources' : resources,
    'resourcesRoot' : resources_root,
    'runTestSeparately' : run_test_separately,
    'source' : source,
    'sourceUnderTest' : source_under_test,
    'srcs' : srcs,
    'stdErrLogLevel' : std_err_log_level,
    'stdOutLogLevel' : std_out_log_level,
    'target' : target,
    'testRuleTimeoutMs' : test_rule_timeout_ms,
    'testType' : test_type,
    'tests' : tests,
    'useCxxLibraries' : use_cxx_libraries,
    'vmArgs' : vm_args,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def thrift_library(name, srcs, cpp2_deps=[], cpp2_options=[], cpp_deps=[], cpp_exported_headers=None, cpp_header_namespace=None, cpp_options=[], cpp_srcs=None, deps=[], flags=[], java_options=[], py_asyncio_base_module=None, py_base_module=None, py_options=[], py_twisted_base_module=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'thrift_library',
    'name' : name,
    'srcs' : srcs,
    'cpp2Deps' : cpp2_deps,
    'cpp2Options' : cpp2_options,
    'cppDeps' : cpp_deps,
    'cppExportedHeaders' : cpp_exported_headers,
    'cppHeaderNamespace' : cpp_header_namespace,
    'cppOptions' : cpp_options,
    'cppSrcs' : cpp_srcs,
    'deps' : deps,
    'flags' : flags,
    'javaOptions' : java_options,
    'pyAsyncioBaseModule' : py_asyncio_base_module,
    'pyBaseModule' : py_base_module,
    'pyOptions' : py_options,
    'pyTwistedBaseModule' : py_twisted_base_module,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def halide_library(name, compiler_deps=[], compiler_flags=[], configs={}, cxx_runtime_type=None, deps=[], frameworks=[], header_namespace=None, headers=None, lang_preprocessor_flags={}, libraries=[], link_style=None, linker_flags=[], platform_compiler_flags=None, platform_headers=None, platform_linker_flags=None, platform_preprocessor_flags=None, platform_srcs=None, prefix_header=None, preprocessor_flags=[], srcs=[], tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'halide_library',
    'name' : name,
    'compilerDeps' : compiler_deps,
    'compilerFlags' : compiler_flags,
    'configs' : configs,
    'cxxRuntimeType' : cxx_runtime_type,
    'deps' : deps,
    'frameworks' : frameworks,
    'headerNamespace' : header_namespace,
    'headers' : headers,
    'langPreprocessorFlags' : lang_preprocessor_flags,
    'libraries' : libraries,
    'linkStyle' : link_style,
    'linkerFlags' : linker_flags,
    'platformCompilerFlags' : platform_compiler_flags,
    'platformHeaders' : platform_headers,
    'platformLinkerFlags' : platform_linker_flags,
    'platformPreprocessorFlags' : platform_preprocessor_flags,
    'platformSrcs' : platform_srcs,
    'prefixHeader' : prefix_header,
    'preprocessorFlags' : preprocessor_flags,
    'srcs' : srcs,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def python_binary(name, base_module=None, build_args=[], deps=[], main=None, main_module=None, package_style=None, platform=None, zip_safe=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'python_binary',
    'name' : name,
    'baseModule' : base_module,
    'buildArgs' : build_args,
    'deps' : deps,
    'main' : main,
    'mainModule' : main_module,
    'packageStyle' : package_style,
    'platform' : platform,
    'zipSafe' : zip_safe,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def d_test(name, deps, srcs, contacts=[], labels=[], source_under_test=[], test_rule_timeout_ms=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'd_test',
    'name' : name,
    'deps' : deps,
    'srcs' : srcs,
    'contacts' : contacts,
    'labels' : labels,
    'sourceUnderTest' : source_under_test,
    'testRuleTimeoutMs' : test_rule_timeout_ms,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def android_prebuilt_aar(name, aar, deps=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'android_prebuilt_aar',
    'name' : name,
    'aar' : aar,
    'deps' : deps,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def python_library(name, base_module=None, deps=[], platform_resources=None, platform_srcs=None, resources=None, srcs=None, zip_safe=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'python_library',
    'name' : name,
    'baseModule' : base_module,
    'deps' : deps,
    'platformResources' : platform_resources,
    'platformSrcs' : platform_srcs,
    'resources' : resources,
    'srcs' : srcs,
    'zipSafe' : zip_safe,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def gen_aidl(name, aidl, import_path, deps=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'gen_aidl',
    'name' : name,
    'aidl' : aidl,
    'importPath' : import_path,
    'deps' : deps,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def cxx_binary(name, compiler_flags=[], cxx_runtime_type=None, deps=[], frameworks=[], header_namespace=None, headers=None, lang_preprocessor_flags={}, libraries=[], link_style=None, linker_flags=[], platform_compiler_flags=None, platform_headers=None, platform_linker_flags=None, platform_preprocessor_flags=None, platform_srcs=None, prefix_header=None, preprocessor_flags=[], srcs=[], tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'cxx_binary',
    'name' : name,
    'compilerFlags' : compiler_flags,
    'cxxRuntimeType' : cxx_runtime_type,
    'deps' : deps,
    'frameworks' : frameworks,
    'headerNamespace' : header_namespace,
    'headers' : headers,
    'langPreprocessorFlags' : lang_preprocessor_flags,
    'libraries' : libraries,
    'linkStyle' : link_style,
    'linkerFlags' : linker_flags,
    'platformCompilerFlags' : platform_compiler_flags,
    'platformHeaders' : platform_headers,
    'platformLinkerFlags' : platform_linker_flags,
    'platformPreprocessorFlags' : platform_preprocessor_flags,
    'platformSrcs' : platform_srcs,
    'prefixHeader' : prefix_header,
    'preprocessorFlags' : preprocessor_flags,
    'srcs' : srcs,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def rust_binary(name, srcs, deps=[], features=[], rustc_flags=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'rust_binary',
    'name' : name,
    'srcs' : srcs,
    'deps' : deps,
    'features' : features,
    'rustcFlags' : rustc_flags,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def genrule(name, out, bash=None, cmd=None, cmd_exe=None, srcs=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'genrule',
    'name' : name,
    'out' : out,
    'bash' : bash,
    'cmd' : cmd,
    'cmdExe' : cmd_exe,
    'srcs' : srcs,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def go_library(name, srcs, compiler_flags=[], deps=[], package_name=None, tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'go_library',
    'name' : name,
    'srcs' : srcs,
    'compilerFlags' : compiler_flags,
    'deps' : deps,
    'packageName' : package_name,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def prebuilt_cxx_library(name, deps=[], exported_deps=[], exported_headers=None, exported_lang_preprocessor_flags={}, exported_linker_flags=[], exported_platform_headers=None, exported_platform_linker_flags=None, exported_platform_preprocessor_flags=None, exported_preprocessor_flags=[], force_static=None, header_namespace=None, header_only=None, include_dirs=[], lib_dir=None, lib_name=None, link_whole=None, link_without_soname=None, provided=None, soname=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'prebuilt_cxx_library',
    'name' : name,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'exportedHeaders' : exported_headers,
    'exportedLangPreprocessorFlags' : exported_lang_preprocessor_flags,
    'exportedLinkerFlags' : exported_linker_flags,
    'exportedPlatformHeaders' : exported_platform_headers,
    'exportedPlatformLinkerFlags' : exported_platform_linker_flags,
    'exportedPlatformPreprocessorFlags' : exported_platform_preprocessor_flags,
    'exportedPreprocessorFlags' : exported_preprocessor_flags,
    'forceStatic' : force_static,
    'headerNamespace' : header_namespace,
    'headerOnly' : header_only,
    'includeDirs' : include_dirs,
    'libDir' : lib_dir,
    'libName' : lib_name,
    'linkWhole' : link_whole,
    'linkWithoutSoname' : link_without_soname,
    'provided' : provided,
    'soname' : soname,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def d_library(name, srcs, deps=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'd_library',
    'name' : name,
    'srcs' : srcs,
    'deps' : deps,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def core_data_model(name, path, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'core_data_model',
    'name' : name,
    'path' : path,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def keystore(name, properties, store, deps=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'keystore',
    'name' : name,
    'properties' : properties,
    'store' : store,
    'deps' : deps,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def prebuilt_native_library(name, native_libs, deps=[], is_asset=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'prebuilt_native_library',
    'name' : name,
    'nativeLibs' : native_libs,
    'deps' : deps,
    'isAsset' : is_asset,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def android_react_native_library(name, bundle_name, entry_path, deps=[], packager_flags=None, package=None, srcs=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'android_react_native_library',
    'name' : name,
    'bundleName' : bundle_name,
    'entryPath' : entry_path,
    'deps' : deps,
    'packagerFlags' : packager_flags,
    'rDotJavaPackage' : package,
    'srcs' : srcs,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def python_test(name, base_module=None, build_args=[], contacts=[], deps=[], env={}, labels=[], package_style=None, platform=None, platform_resources=None, platform_srcs=None, resources=None, source_under_test=[], srcs=None, test_rule_timeout_ms=None, zip_safe=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'python_test',
    'name' : name,
    'baseModule' : base_module,
    'buildArgs' : build_args,
    'contacts' : contacts,
    'deps' : deps,
    'env' : env,
    'labels' : labels,
    'packageStyle' : package_style,
    'platform' : platform,
    'platformResources' : platform_resources,
    'platformSrcs' : platform_srcs,
    'resources' : resources,
    'sourceUnderTest' : source_under_test,
    'srcs' : srcs,
    'testRuleTimeoutMs' : test_rule_timeout_ms,
    'zipSafe' : zip_safe,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def xcode_workspace_config(name, action_config_names={}, extra_schemes={}, extra_targets=[], extra_tests=[], is_remote_runnable=None, src_target=None, workspace_name=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'xcode_workspace_config',
    'name' : name,
    'actionConfigNames' : action_config_names,
    'extraSchemes' : extra_schemes,
    'extraTargets' : extra_targets,
    'extraTests' : extra_tests,
    'isRemoteRunnable' : is_remote_runnable,
    'srcTarget' : src_target,
    'workspaceName' : workspace_name,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def apk_genrule(name, apk, out, bash=None, cmd=None, cmd_exe=None, srcs=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'apk_genrule',
    'name' : name,
    'apk' : apk,
    'out' : out,
    'bash' : bash,
    'cmd' : cmd,
    'cmdExe' : cmd_exe,
    'srcs' : srcs,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def apple_library(name, can_be_asset=None, compiler_flags=[], configs={}, cxx_runtime_type=None, deps=[], exported_deps=[], exported_headers=None, exported_lang_preprocessor_flags={}, exported_linker_flags=[], exported_platform_headers=None, exported_platform_linker_flags=None, exported_platform_preprocessor_flags=None, exported_preprocessor_flags=[], extra_xcode_sources=[], force_static=None, frameworks=[], header_namespace=None, header_path_prefix=None, headers=None, info_plist=None, info_plist_substitutions={}, lang_preprocessor_flags={}, libraries=[], link_style=None, link_whole=None, linker_flags=[], platform_compiler_flags=None, platform_headers=None, platform_linker_flags=None, platform_preprocessor_flags=None, platform_srcs=None, prefix_header=None, preprocessor_flags=[], soname=None, srcs=[], supported_platforms_regex=None, tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'apple_library',
    'name' : name,
    'canBeAsset' : can_be_asset,
    'compilerFlags' : compiler_flags,
    'configs' : configs,
    'cxxRuntimeType' : cxx_runtime_type,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'exportedHeaders' : exported_headers,
    'exportedLangPreprocessorFlags' : exported_lang_preprocessor_flags,
    'exportedLinkerFlags' : exported_linker_flags,
    'exportedPlatformHeaders' : exported_platform_headers,
    'exportedPlatformLinkerFlags' : exported_platform_linker_flags,
    'exportedPlatformPreprocessorFlags' : exported_platform_preprocessor_flags,
    'exportedPreprocessorFlags' : exported_preprocessor_flags,
    'extraXcodeSources' : extra_xcode_sources,
    'forceStatic' : force_static,
    'frameworks' : frameworks,
    'headerNamespace' : header_namespace,
    'headerPathPrefix' : header_path_prefix,
    'headers' : headers,
    'infoPlist' : info_plist,
    'infoPlistSubstitutions' : info_plist_substitutions,
    'langPreprocessorFlags' : lang_preprocessor_flags,
    'libraries' : libraries,
    'linkStyle' : link_style,
    'linkWhole' : link_whole,
    'linkerFlags' : linker_flags,
    'platformCompilerFlags' : platform_compiler_flags,
    'platformHeaders' : platform_headers,
    'platformLinkerFlags' : platform_linker_flags,
    'platformPreprocessorFlags' : platform_preprocessor_flags,
    'platformSrcs' : platform_srcs,
    'prefixHeader' : prefix_header,
    'preprocessorFlags' : preprocessor_flags,
    'soname' : soname,
    'srcs' : srcs,
    'supportedPlatformsRegex' : supported_platforms_regex,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def android_resource(name, assets=None, deps=[], has_whitelisted_strings=None, manifest=None, package=None, res=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'android_resource',
    'name' : name,
    'assets' : assets,
    'deps' : deps,
    'hasWhitelistedStrings' : has_whitelisted_strings,
    'manifest' : manifest,
    'rDotJavaPackage' : package,
    'res' : res,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def sh_test(name, test, args=[], deps=[], labels=[], test_rule_timeout_ms=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'sh_test',
    'name' : name,
    'test' : test,
    'args' : args,
    'deps' : deps,
    'labels' : labels,
    'testRuleTimeoutMs' : test_rule_timeout_ms,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def apple_package(name, bundle, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'apple_package',
    'name' : name,
    'bundle' : bundle,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def java_binary(name, blacklist=[], deps=[], main_class=None, manifest_file=None, merge_manifests=None, meta_inf_directory=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'java_binary',
    'name' : name,
    'blacklist' : blacklist,
    'deps' : deps,
    'mainClass' : main_class,
    'manifestFile' : manifest_file,
    'mergeManifests' : merge_manifests,
    'metaInfDirectory' : meta_inf_directory,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def java_test(name, annotation_processor_deps=[], annotation_processor_only=None, annotation_processor_params=[], annotation_processors=[], compiler=None, contacts=[], deps=[], exported_deps=[], extra_arguments=[], java_version=None, javac=None, javac_jar=None, labels=[], maven_coords=None, path_to_java_agent=None, postprocess_classes_commands=[], proguard_config=None, provided_deps=[], resources=[], resources_root=None, run_test_separately=None, source=None, source_under_test=[], srcs=[], std_err_log_level=None, std_out_log_level=None, target=None, test_rule_timeout_ms=None, test_type=None, tests=[], use_cxx_libraries=None, vm_args=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'java_test',
    'name' : name,
    'annotationProcessorDeps' : annotation_processor_deps,
    'annotationProcessorOnly' : annotation_processor_only,
    'annotationProcessorParams' : annotation_processor_params,
    'annotationProcessors' : annotation_processors,
    'compiler' : compiler,
    'contacts' : contacts,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'extraArguments' : extra_arguments,
    'javaVersion' : java_version,
    'javac' : javac,
    'javacJar' : javac_jar,
    'labels' : labels,
    'mavenCoords' : maven_coords,
    'pathToJavaAgent' : path_to_java_agent,
    'postprocessClassesCommands' : postprocess_classes_commands,
    'proguardConfig' : proguard_config,
    'providedDeps' : provided_deps,
    'resources' : resources,
    'resourcesRoot' : resources_root,
    'runTestSeparately' : run_test_separately,
    'source' : source,
    'sourceUnderTest' : source_under_test,
    'srcs' : srcs,
    'stdErrLogLevel' : std_err_log_level,
    'stdOutLogLevel' : std_out_log_level,
    'target' : target,
    'testRuleTimeoutMs' : test_rule_timeout_ms,
    'testType' : test_type,
    'tests' : tests,
    'useCxxLibraries' : use_cxx_libraries,
    'vmArgs' : vm_args,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def xcode_postbuild_script(name, cmd, outputs=[], srcs=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'xcode_postbuild_script',
    'name' : name,
    'cmd' : cmd,
    'outputs' : outputs,
    'srcs' : srcs,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def cxx_library(name, can_be_asset=None, compiler_flags=[], cxx_runtime_type=None, deps=[], exported_deps=[], exported_headers=None, exported_lang_preprocessor_flags={}, exported_linker_flags=[], exported_platform_headers=None, exported_platform_linker_flags=None, exported_platform_preprocessor_flags=None, exported_preprocessor_flags=[], force_static=None, frameworks=[], header_namespace=None, headers=None, lang_preprocessor_flags={}, libraries=[], link_style=None, link_whole=None, linker_flags=[], platform_compiler_flags=None, platform_headers=None, platform_linker_flags=None, platform_preprocessor_flags=None, platform_srcs=None, prefix_header=None, preprocessor_flags=[], soname=None, srcs=[], supported_platforms_regex=None, tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'cxx_library',
    'name' : name,
    'canBeAsset' : can_be_asset,
    'compilerFlags' : compiler_flags,
    'cxxRuntimeType' : cxx_runtime_type,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'exportedHeaders' : exported_headers,
    'exportedLangPreprocessorFlags' : exported_lang_preprocessor_flags,
    'exportedLinkerFlags' : exported_linker_flags,
    'exportedPlatformHeaders' : exported_platform_headers,
    'exportedPlatformLinkerFlags' : exported_platform_linker_flags,
    'exportedPlatformPreprocessorFlags' : exported_platform_preprocessor_flags,
    'exportedPreprocessorFlags' : exported_preprocessor_flags,
    'forceStatic' : force_static,
    'frameworks' : frameworks,
    'headerNamespace' : header_namespace,
    'headers' : headers,
    'langPreprocessorFlags' : lang_preprocessor_flags,
    'libraries' : libraries,
    'linkStyle' : link_style,
    'linkWhole' : link_whole,
    'linkerFlags' : linker_flags,
    'platformCompilerFlags' : platform_compiler_flags,
    'platformHeaders' : platform_headers,
    'platformLinkerFlags' : platform_linker_flags,
    'platformPreprocessorFlags' : platform_preprocessor_flags,
    'platformSrcs' : platform_srcs,
    'prefixHeader' : prefix_header,
    'preprocessorFlags' : preprocessor_flags,
    'soname' : soname,
    'srcs' : srcs,
    'supportedPlatformsRegex' : supported_platforms_regex,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def go_binary(name, srcs, compiler_flags=[], deps=[], linker_flags=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'go_binary',
    'name' : name,
    'srcs' : srcs,
    'compilerFlags' : compiler_flags,
    'deps' : deps,
    'linkerFlags' : linker_flags,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def sh_binary(name, main, deps=[], resources=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'sh_binary',
    'name' : name,
    'main' : main,
    'deps' : deps,
    'resources' : resources,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def prebuilt_dotnet_library(name, assembly, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'prebuilt_dotnet_library',
    'name' : name,
    'assembly' : assembly,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def remote_file(name, sha1, url, out=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'remote_file',
    'name' : name,
    'sha1' : sha1,
    'url' : url,
    'out' : out,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def android_library(name, annotation_processor_deps=[], annotation_processor_only=None, annotation_processor_params=[], annotation_processors=[], compiler=None, deps=[], exported_deps=[], extra_arguments=[], java_version=None, javac=None, javac_jar=None, manifest=None, maven_coords=None, postprocess_classes_commands=[], proguard_config=None, provided_deps=[], resource_union_package=None, resources=[], resources_root=None, source=None, srcs=[], target=None, tests=[], visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'android_library',
    'name' : name,
    'annotationProcessorDeps' : annotation_processor_deps,
    'annotationProcessorOnly' : annotation_processor_only,
    'annotationProcessorParams' : annotation_processor_params,
    'annotationProcessors' : annotation_processors,
    'compiler' : compiler,
    'deps' : deps,
    'exportedDeps' : exported_deps,
    'extraArguments' : extra_arguments,
    'javaVersion' : java_version,
    'javac' : javac,
    'javacJar' : javac_jar,
    'manifest' : manifest,
    'mavenCoords' : maven_coords,
    'postprocessClassesCommands' : postprocess_classes_commands,
    'proguardConfig' : proguard_config,
    'providedDeps' : provided_deps,
    'resourceUnionPackage' : resource_union_package,
    'resources' : resources,
    'resourcesRoot' : resources_root,
    'source' : source,
    'srcs' : srcs,
    'target' : target,
    'tests' : tests,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def zip_file(name, srcs, deps=[], out=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'zip_file',
    'name' : name,
    'srcs' : srcs,
    'deps' : deps,
    'out' : out,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def cxx_test(name, args=[], compiler_flags=[], contacts=[], cxx_runtime_type=None, deps=[], env={}, framework=None, frameworks=[], header_namespace=None, headers=None, labels=[], lang_preprocessor_flags={}, libraries=[], link_style=None, linker_flags=[], platform_compiler_flags=None, platform_headers=None, platform_linker_flags=None, platform_preprocessor_flags=None, platform_srcs=None, prefix_header=None, preprocessor_flags=[], run_test_separately=None, source_under_test=[], srcs=[], test_rule_timeout_ms=None, tests=[], use_default_test_main=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'cxx_test',
    'name' : name,
    'args' : args,
    'compilerFlags' : compiler_flags,
    'contacts' : contacts,
    'cxxRuntimeType' : cxx_runtime_type,
    'deps' : deps,
    'env' : env,
    'framework' : framework,
    'frameworks' : frameworks,
    'headerNamespace' : header_namespace,
    'headers' : headers,
    'labels' : labels,
    'langPreprocessorFlags' : lang_preprocessor_flags,
    'libraries' : libraries,
    'linkStyle' : link_style,
    'linkerFlags' : linker_flags,
    'platformCompilerFlags' : platform_compiler_flags,
    'platformHeaders' : platform_headers,
    'platformLinkerFlags' : platform_linker_flags,
    'platformPreprocessorFlags' : platform_preprocessor_flags,
    'platformSrcs' : platform_srcs,
    'prefixHeader' : prefix_header,
    'preprocessorFlags' : preprocessor_flags,
    'runTestSeparately' : run_test_separately,
    'sourceUnderTest' : source_under_test,
    'srcs' : srcs,
    'testRuleTimeoutMs' : test_rule_timeout_ms,
    'tests' : tests,
    'useDefaultTestMain' : use_default_test_main,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def apple_test(name, extension, info_plist, can_be_asset=None, can_group=None, compiler_flags=[], configs={}, contacts=[], cxx_runtime_type=None, deps=[], destination_specifier={}, exported_deps=[], exported_headers=None, exported_lang_preprocessor_flags={}, exported_linker_flags=[], exported_platform_headers=None, exported_platform_linker_flags=None, exported_platform_preprocessor_flags=None, exported_preprocessor_flags=[], extra_xcode_sources=[], force_static=None, frameworks=[], header_namespace=None, header_path_prefix=None, headers=None, info_plist_substitutions={}, labels=[], lang_preprocessor_flags={}, libraries=[], link_style=None, link_whole=None, linker_flags=[], platform_compiler_flags=None, platform_headers=None, platform_linker_flags=None, platform_preprocessor_flags=None, platform_srcs=None, prefix_header=None, preprocessor_flags=[], product_name=None, run_test_separately=None, soname=None, srcs=[], supported_platforms_regex=None, test_host_app=None, tests=[], xcode_product_type=None, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'apple_test',
    'name' : name,
    'extension' : extension,
    'infoPlist' : info_plist,
    'canBeAsset' : can_be_asset,
    'canGroup' : can_group,
    'compilerFlags' : compiler_flags,
    'configs' : configs,
    'contacts' : contacts,
    'cxxRuntimeType' : cxx_runtime_type,
    'deps' : deps,
    'destinationSpecifier' : destination_specifier,
    'exportedDeps' : exported_deps,
    'exportedHeaders' : exported_headers,
    'exportedLangPreprocessorFlags' : exported_lang_preprocessor_flags,
    'exportedLinkerFlags' : exported_linker_flags,
    'exportedPlatformHeaders' : exported_platform_headers,
    'exportedPlatformLinkerFlags' : exported_platform_linker_flags,
    'exportedPlatformPreprocessorFlags' : exported_platform_preprocessor_flags,
    'exportedPreprocessorFlags' : exported_preprocessor_flags,
    'extraXcodeSources' : extra_xcode_sources,
    'forceStatic' : force_static,
    'frameworks' : frameworks,
    'headerNamespace' : header_namespace,
    'headerPathPrefix' : header_path_prefix,
    'headers' : headers,
    'infoPlistSubstitutions' : info_plist_substitutions,
    'labels' : labels,
    'langPreprocessorFlags' : lang_preprocessor_flags,
    'libraries' : libraries,
    'linkStyle' : link_style,
    'linkWhole' : link_whole,
    'linkerFlags' : linker_flags,
    'platformCompilerFlags' : platform_compiler_flags,
    'platformHeaders' : platform_headers,
    'platformLinkerFlags' : platform_linker_flags,
    'platformPreprocessorFlags' : platform_preprocessor_flags,
    'platformSrcs' : platform_srcs,
    'prefixHeader' : prefix_header,
    'preprocessorFlags' : preprocessor_flags,
    'productName' : product_name,
    'runTestSeparately' : run_test_separately,
    'soname' : soname,
    'srcs' : srcs,
    'supportedPlatformsRegex' : supported_platforms_regex,
    'testHostApp' : test_host_app,
    'tests' : tests,
    'xcodeProductType' : xcode_product_type,
    'visibility' : visibility,
  }, build_env)


@provide_for_build
def csharp_library(name, framework_ver, srcs, deps=[], dll_name=None, resources={}, visibility=[], build_env=None):
  add_rule({
    'buck.type' : 'csharp_library',
    'name' : name,
    'frameworkVer' : framework_ver,
    'srcs' : srcs,
    'deps' : deps,
    'dllName' : dll_name,
    'resources' : resources,
    'visibility' : visibility,
  }, build_env)


if __name__ == '__main__':
  try:
    main()
  except KeyboardInterrupt:
    print >> sys.stderr, 'Killed by User'
