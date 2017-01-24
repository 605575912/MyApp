package com.lzxmy.demo.DynamicLoad;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lzxmy.demo.R;
import com.ryg.dynamicload.internal.DLIntent;
import com.ryg.dynamicload.internal.DLPluginManager;
import com.ryg.utils.DLUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DynamicLoadMainActivity extends Activity implements OnItemClickListener {

//    public static final String FROM = "extra.from";
//    public static final int FROM_INTERNAL = 0;
//    public static final int FROM_EXTERNAL = 1;

    private ArrayList<PluginItem> mPluginItems = new ArrayList<PluginItem>();
    private PluginAdapter mPluginAdapter;

    private ListView mListView;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<PluginItem> pluginItems = (ArrayList<PluginItem>) msg.obj;
            mPluginItems.addAll(pluginItems);
            mPluginAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamicload_layout);
        initView();
        initData(getApplicationContext());
    }

    private void initView() {
        mPluginAdapter = new PluginAdapter();
        mListView = (ListView) findViewById(R.id.plugin_list);
        mListView.setAdapter(mPluginAdapter);
        mListView.setOnItemClickListener(this);
    }

    private void initData(Context context) {

        ArrayList<PluginItem> mPluginItems = new ArrayList<PluginItem>();
        String path = "/sdcard/XSW/crash/";
        // 判断是否挂载了SD卡
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

//            if (Environment.getExternalStorageState().equals(
//                    Environment.MEDIA_MOUNTED)) {
//
//                File dir = new File(path);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                } else {
//                    for (File file : dir.listFiles()) {
//                        if (file.isDirectory()) {
//                            continue;
//                        }
//                        file.delete();
//                    }
//                }
//            }
//            path = Environment.getExternalStorageDirectory()
//                    .getAbsolutePath();
//            path = path + "/APP/image/";
//            File file = new File(path);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//            }
        } else {
            path = context.getCacheDir().getPath();
        }

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.i("TAG", "===" + path);
            AssetManager manager = context.getAssets();
            String[] listfile;
            try {
                listfile = manager.list("apk");
                String fileString = "";
                for (int i = 0; i < listfile.length; i++) {
                    fileString = listfile[i];
                    String apkpath = path + fileString;
                    File file = new File(apkpath);
                    if (!file.exists()) {
                        InputStream inputStream = manager.open("apk/" + fileString);
                        FileOutputStream outStream = new FileOutputStream(apkpath);
                        byte buffer[] = new byte[4 * 1024];
                        while ((inputStream.read(buffer)) != -1) {
                            outStream.write(buffer);
                        }
                        outStream.flush();
                        outStream.close();
                        inputStream.close();
                    }
                    if (file.getName().indexOf(".apk") > -1) {
                        PluginItem item = new PluginItem();
                        item.pluginPath = file.getAbsolutePath();
                        item.packageInfo = com.ryg.utils.DLUtils.getPackageInfo(this, item.pluginPath);
                        if (item.packageInfo.activities != null && item.packageInfo.activities.length > 0) {
                            item.launcherActivityName = item.packageInfo.activities[0].name;
                        }
                        if (item.packageInfo.services != null && item.packageInfo.services.length > 0) {
                            item.launcherServiceName = item.packageInfo.services[0].name;
                        }
                        mPluginItems.add(item);
                        DLPluginManager.getInstance(this).loadApk(item.pluginPath);
                    }
                }
                File file_path = new File(path);
                if (!file_path.exists()){
                    file_path.mkdir();
                }
                File[] files = file_path.listFiles();

                for (File s :files){
                getItem(s.getAbsolutePath());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        String pluginFolder = Environment.getExternalStorageDirectory()
//                .getPath() + "/Tencent/QQfile_recv/";
//        File file = new File(pluginFolder);
//        if (file != null && file.exists()) {
//            File[] plugins = file.listFiles();
//            for (File plugin : plugins) {
//                if (plugin.getName().indexOf(".apk") == -1) {
//                    continue;
//                }
//                PluginItem item = new PluginItem();
//                item.pluginPath = plugin.getAbsolutePath();
//                item.packageInfo = com.ryg.utils.DLUtils.getPackageInfo(this, item.pluginPath);
//                if (item.packageInfo.activities != null && item.packageInfo.activities.length > 0) {
//                    item.launcherActivityName = item.packageInfo.activities[0].name;
//                }
//                if (item.packageInfo.services != null && item.packageInfo.services.length > 0) {
//                    item.launcherServiceName = item.packageInfo.services[0].name;
//                }
//                mPluginItems.add(item);
//                DLPluginManager.getInstance(this).loadApk(item.pluginPath);
//            }
//
//
//        }
        Message message = handler.obtainMessage(1);
        message.obj = mPluginItems;
        handler.sendMessage(message);

    }

    void getItem(String path) {
        PackageInfo pkgInfo = com.ryg.utils.DLUtils.getPackageInfo(this, path);
        if (pkgInfo != null) {
            PluginItem item = new PluginItem();
            item.packageInfo = pkgInfo;
            item.pluginPath = path;
            if (item.packageInfo.activities != null && item.packageInfo.activities.length > 0) {
                item.launcherActivityName = item.packageInfo.activities[0].name;
            }
            if (item.packageInfo.services != null && item.packageInfo.services.length > 0) {
                item.launcherServiceName = item.packageInfo.services[0].name;
            }
            mPluginItems.add(item);
            DLPluginManager.getInstance(this).loadApk(item.pluginPath);
        }
    }

    private class PluginAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public PluginAdapter() {
            mInflater = DynamicLoadMainActivity.this.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mPluginItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mPluginItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.plugin_item, parent, false);
                holder = new ViewHolder();
                holder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
                holder.appName = (TextView) convertView.findViewById(R.id.app_name);
                holder.apkName = (TextView) convertView.findViewById(R.id.apk_name);
                holder.packageName = (TextView) convertView.findViewById(R.id.package_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PluginItem item = mPluginItems.get(position);
            PackageInfo packageInfo = item.packageInfo;
            holder.appIcon.setImageDrawable(DLUtils.getAppIcon(DynamicLoadMainActivity.this, item.pluginPath));
            holder.appName.setText(DLUtils.getAppLabel(DynamicLoadMainActivity.this, item.pluginPath));
            holder.apkName.setText(item.pluginPath.substring(item.pluginPath.lastIndexOf(File.separatorChar) + 1));
            holder.packageName.setText(packageInfo.applicationInfo.packageName);
            return convertView;
        }
    }

    private static class ViewHolder {
        public ImageView appIcon;
        public TextView appName;
        public TextView apkName;
        public TextView packageName;
    }

    public static class PluginItem {
        public PackageInfo packageInfo;
        public String pluginPath;
        public String launcherActivityName;
        public String launcherServiceName;

        public PluginItem() {
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(this, ProxyActivity.class);
//        intent.putExtra(ProxyActivity.EXTRA_DEX_PATH, mPluginItems.get(position).pluginPath);
//        startActivity(intent);
        PluginItem item = mPluginItems.get(position);
        DLPluginManager pluginManager = DLPluginManager.getInstance(this);
        pluginManager.startPluginActivity(this, new DLIntent(item.packageInfo.packageName, item.launcherActivityName));

        //如果存在Service则调用起Service
        if (item.launcherServiceName != null) {
            //startService
            DLIntent intent = new DLIntent(item.packageInfo.packageName, item.launcherServiceName);
            //startService
//	        pluginManager.startPluginService(this, intent);

            //bindService
//	        pluginManager.bindPluginService(this, intent, mConnection = new ServiceConnection() {
//                public void onServiceDisconnected(ComponentName name) {
//                }
//
//                public void onServiceConnected(ComponentName name, IBinder binder) {
//                    int sum = ((ITestServiceInterface)binder).sum(5, 5);
//                    Log.e("MainActivity", "onServiceConnected sum(5 + 5) = " + sum);
//                }
//            }, Context.BIND_AUTO_CREATE);
        }
    }

}
