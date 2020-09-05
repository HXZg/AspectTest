package com.xz.mmapfile;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile  AspectTest
 * @Des MmapFileUtils
 * @DATE 2020/9/5  9:52 星期六
 */
public class MmapFileUtils {

    static {
        System.loadLibrary("native_mmap");
    }

    /**
     *   往  "/data/data/com.xz.mmapfile/cache/test.txt"  目录下  写文件 test.txt  内容：fagasghh
     */
    public native void write();
}
