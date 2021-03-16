package com.xz.mmapfile.httpcache;

/**
 * @author xian_zhong  admin
 * @version 1.0
 * @title com.xz.mmapfile.httpcache  AspectTest
 * @Des CacheCallback
 * @DATE 2020/10/17  9:26 星期六
 */
public interface CacheCallback<T> {

    void onFail();

    void onSuccess(T t);
}
