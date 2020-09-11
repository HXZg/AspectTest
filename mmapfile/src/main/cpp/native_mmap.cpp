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