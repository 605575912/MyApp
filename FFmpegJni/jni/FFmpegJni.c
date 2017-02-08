#include "FFmpegJni.h"

#include <stdlib.h>
#include <stdio.h>

int startdecode(int argc, char **argv, unsigned char *old, jsize len);

char *readFromFile();

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

    int result = startdecode(argc, argv, NULL, 0);
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
        jsize len = (*env)->GetArrayLength(env, jbyteArray);
        jbyte *jbarray = (jbyte *) malloc(len * sizeof(jbyte));
        (*env)->GetByteArrayRegion(env, jbyteArray, 0, len, jbarray);
        unsigned char *byBuf = NULL;
        byBuf = (char *) malloc(len + 1);
        memcpy(byBuf, jbarray, len);
//        byBuf[len] = '\0';
        remove(argv[5]);
        int result = startdecode(argc, argv, byBuf, len);
        free(argv);
        (*env)->ReleaseByteArrayElements(env, jbyteArray, jbarray, 0);
        free(byBuf);
        return result;
    }
    return -1;
}

char *readFromFile() {
    FILE *pFile;   //文件指针
    long lSize;   // 用于文件长度
    char *buffer; // 文件缓冲区指针
    size_t result;  // 返回值是读取的内容数量

    pFile = fopen("/storage/emulated/0/ffmpeg/image1.jpg", "rb+");
    if (pFile == NULL) {
        exit(1);
    }    // 如果文件错误，退出1

    // obtain file size:  获得文件大小
    fseek(pFile, 0, SEEK_END); // 指针移到文件末位
    lSize = ftell(pFile);  // 获得文件长度
    rewind(pFile);  // 函数rewind()把文件指针移到由stream(流)指定的开始处, 同时清除和流相关的错误和EOF标记

    // allocate memory to contain the whole file: 为整个文件分配内存缓冲区
    buffer = (char *) malloc(sizeof(char) * (lSize)); // 分配缓冲区，按前面的 lSize
    if (buffer == NULL) {
        fputs("Memory error", stderr);
        exit(2);
    }  // 内存分配错误，退出2


    // copy the file into the buffer:  该文件复制到缓冲区
    result = fread(buffer, 1, lSize, pFile); // 返回值是读取的内容数量
    if (result != lSize) {
        fputs("Reading error", stderr);
        exit(3);
    } // 返回值如果不和文件大小，读错误

    /* the whole file is now loaded in the memory buffer. */ //现在整个文件载入内存缓冲区

    // 读到内存，看自己怎么使用了...............
    // ...........


    // terminate // 文件终止
    fclose(pFile);
//    free(buffer);
    return buffer;
}
//static int readFromFile(const char *path, char *buf, size_t size) {
//    FILE *fp_open = fopen("/storage/emulated/0/ffmpeg/image1.jpg", "rb+");
//    //
//        int true_size = fread(buf, 1, 32768, fp_open);
//    size_t count = read(fd, buf, size);
//    if (count > 0) {
//        count = (count < size) ? count : size - 1;
//        while (count > 0 && buf[count - 1] == '\n') count--;
//        buf[count] = '\0';
//    } else {
//        buf[0] = '\0';
//    }
//
//    close(fd);
//    return count;
//}
