#include "FFmpegJni.h"

#include <stdlib.h>
#include <stdio.h>

int main(int argc, char **argv);

jint Java_org_mqstack_ffmpegjni_FFmpegJni_run(JNIEnv *env, jobject obj, jint argc,
                                              jobjectArray args) {
    int i = 0;
    char **argv = NULL;
    jstring *strr = NULL;

    if (args != NULL) {
        argv = (char **) malloc(sizeof(char *) * argc);
        strr = (jstring *) malloc(sizeof(jstring) * argc);

        for (i = 0; i < argc; ++i) {
            strr[i] = (jstring) (*env)->GetObjectArrayElement(env, args, i);
            argv[i] = (char *) (*env)->GetStringUTFChars(env, strr[i], 0);
        }
    }

    int result = main(argc, argv);
    for (i = 0; i < argc; ++i) {
        (*env)->ReleaseStringUTFChars(env, strr[i], argv[i]);
    }
    free(argv);
    free(strr);

    return result;
}

jint Java_org_mqstack_ffmpegjni_FFmpegJni_ImageToVideo(JNIEnv *env, jobject obj,
                                                       jstring outfile, jstring infile) {
    char **argv = NULL;
    jint argc = 6;
    if (outfile != NULL && infile != NULL) {
        argv = (char **) malloc(sizeof(char *) * argc);
        argv[0] = "ffmpeg";
        argv[1] = "-f";
        argv[2] = "image2";
        argv[3] = "-i";
        argv[4] = (char *) (*env)->GetStringUTFChars(env, infile, 0);
        argv[5] = (char *) (*env)->GetStringUTFChars(env, outfile, 0);
        remove(argv[5]);
        int result = main(argc, argv);
        free(argv);
        return result;
    }
    return -1;
}
