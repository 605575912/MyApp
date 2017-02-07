#include "FFmpegJni.h"
#include "logjni.h"

#include <stdlib.h>
#include <stdio.h>

int mains(int argc, char **argv, jbyte *old);

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

    int result = mains(argc, argv, NULL);
    for (i = 0; i < argc; ++i) {
        (*env)->ReleaseStringUTFChars(env, strr[i], argv[i]);
    }
    free(argv);
    free(strr);

    return result;
}

jint Java_org_mqstack_ffmpegjni_FFmpegJni_ImageToVideo(JNIEnv *env, jobject obj,
                                                       jstring outfile, jstring infile,
                                                       jarray jbyteArray) {
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
        jsize len  = (*env)->GetArrayLength(env,jbyteArray);


        jbyte *jbarray = (jbyte *)malloc(len * sizeof(jbyte));

        (*env)->GetByteArrayRegion(env,jbyteArray,0,len,jbarray);

LOGI("===%d==%d==%d",  len,strlen(jbarray),jbarray[0]);
//        uint8_t *data = (uint8_t *)jbarray;//uint8_t 就是byte

//        LOGI("===%d==%d==%d",  len,strlen(data),data[0]);
//        jbyte *bytedata = (*env)->GetByteArrayElements(env, jbyteArray, 0);
//        unsigned char *old = (unsigned char *) bytedata;
        remove(argv[5]);
        int result = mains(argc, argv, jbarray);
        free(argv);
//        (*env)->ReleaseByteArrayElements(env, jbyteArray, jbarray, 0);
        return result;
    }
    return -1;
}
