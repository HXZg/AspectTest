//
// Created by admin on 2020/9/5.
//
#include <jni.h>
#include <string>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <android/bitmap.h>
#include <android/log.h>
#include <malloc.h>

extern "C"
{
#include "jpeglib.h"
}
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define LOG_TAG "hxz"
#define true 1
typedef uint8_t BYTE;

void writeImage(BYTE *data, const char *path,int w,int h) {

    struct jpeg_compress_struct jpeg_struct;
    //    设置错误处理信息
    jpeg_error_mgr err;
    jpeg_struct.err = jpeg_std_error(&err);
//    给结构体分配内存
    jpeg_create_compress(&jpeg_struct);

    FILE *file = fopen(path, "wb");
//    设置输出路径
    jpeg_stdio_dest(&jpeg_struct, file);

    jpeg_struct.image_width = w;
    jpeg_struct.image_height = h;
//    初始化  初始化
//改成FALSE   ---》 开启hufuman算法
    jpeg_struct.arith_code = FALSE;
    jpeg_struct.optimize_coding = TRUE;
    jpeg_struct.in_color_space = JCS_RGB;

    jpeg_struct.input_components = 3;
//    其他的设置默认
    jpeg_set_defaults(&jpeg_struct);
    jpeg_set_quality(&jpeg_struct, 20, true);
    jpeg_start_compress(&jpeg_struct, TRUE);
    JSAMPROW row_pointer[1];
//    一行的rgb
    int row_stride = w * 3;
    while (jpeg_struct.next_scanline < h) {
        row_pointer[0] = &data[jpeg_struct.next_scanline * w * 3];
        jpeg_write_scanlines(&jpeg_struct, row_pointer, 1);
    }
    jpeg_finish_compress(&jpeg_struct);

    jpeg_destroy_compress(&jpeg_struct);
    fclose(file);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_xz_mmapfile_MmapFileUtils_write(JNIEnv *env,jobject thiz) {

    std::string file = "/data/data/com.xz.mmapfile/cache/test.txt";
    int m_fd=open(file.c_str(), O_RDWR | O_CREAT, S_IRWXU);
    int32_t  m_size = getpagesize();

    ftruncate(m_fd,m_size);
    int8_t  *m_ptr = static_cast<int8_t *>(mmap(0, m_size, PROT_READ | PROT_WRITE, MAP_SHARED, m_fd,
                                                0));
    std::string data("fagasghh");
    memcpy(m_ptr,data.data(),data.size());
    close(m_fd);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_xz_mmapfile_MmapFileUtils_compress(JNIEnv *env, jobject thiz, jobject bitmap,
                                            jstring path_) {


    const char *path = env->GetStringUTFChars(path_, 0);

    AndroidBitmapInfo bitmapInfo;
    AndroidBitmap_getInfo(env,bitmap,&bitmapInfo);
    BYTE *pixels;
    AndroidBitmap_lockPixels(env,bitmap,(void **) &pixels);

    int h = bitmapInfo.height;
    int w = bitmapInfo.width;
    BYTE *data,*tmpData;
    data = (BYTE *)malloc(w * h * 3);
    tmpData = data;

    BYTE r,g,b;
    int color;

    for (int i = 0; i < h; ++i) {
        for (int j = 0; j < w; ++j) {
            color = *((int *)pixels);
            r = ((color & 0x00FF0000) >> 16);
            g = ((color & 0x0000FF00) >> 8);
            b = ((color & 0x000000FF));
            *data = b;
            *(data + 1) = g;
            *(data + 2) = r;
            data += 3;
            pixels += 4;
        }
    }

    AndroidBitmap_unlockPixels(env,bitmap);
    writeImage(tmpData,path,w,h);
    env->ReleaseStringUTFChars(path_,path);
}

