# <p align="center">AspectTest</p>

***
### 编译时技术


##### 注解处理器
[annotation](../annotaion) : 需要处理的注解

[compile](../annotation_compile) : 注解处理器

用来动态生成 json 文件 ， 动态 配置 navigation。


[navtest](../navtest) : 应用注解处理器的demo

[navigation动态添加fragment的工具类](../navtest/src/main/java/com/xz/navtest/utils/NavGraphBuilder.kt)

[实现hide 方式替换fragment](../navtest/src/main/java/com/xz/navtest/utils/FixFragmentNavigator)

##### plugin插件 编译时 javassist 修改字节码
[自定义plugin](../CustomPlugin)
发布至仓库
``` java
uploadArchives {
    repositories.mavenDeployer {
        repository(url: uri('../repo')) //仓库的路径，此处是项目根目录下的 repo 的文件夹
        pom.groupId = 'com.example.customplugin'  //groupId ，自行定义，一般是包名
        pom.artifactId = 'custom' //artifactId ，自行定义
        pom.version = '1.0.1' //version 版本号
    }
}
```
在项目build.gradle里引用：
``` groovy
maven{
            url uri("repo")
        }
```
``` groovy
classpath 'com.example.customplugin:custom:1.0.1'
```

在module 的 build.gradle里引用：
```
apply plugin: 'com.example.customplugin'
```
##### AOP 方式
依赖：
```
classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.4'
```
```
apply plugin: 'android-aspectjx'
```
```
implementation 'org.aspectj:aspectjrt:1.8.14'
```
[demo](../aspectlib)
##### IOC 注入 控制反转
运行时根据注解生成代码，运用动态代理设置view的点击事件

[demo](../IOCLib)

##### 动态换肤
解析apk 获取包名，手动生成resources  替换应用中用到的资源

[lib](../skinmanager)

##### 通过PMS解析apk
获取apk中在Manifest中注册的广播然后动态注册广播
[demo](../plugintest/src/main/java/com/xz/plugintest/receiver/MyPackageParse)

##### hook AMS 实现集中式登录
运用动态代理
[demo](../plugintest/src/main/java/com/xz/plugintest/ams/AmsHook)

##### app 模块
仿QQ的气泡view 自定义实现
navigation hide show方式替换fragment 实现
workmanager 测试

##### mmapfile 模块
sharedPreferences 与 mmkv 测试  [demo](../plugintest)
c++ 实现 mmap函数  做到内存与文件的映射，无需拷贝，内核控件切换。
okhttp 实现websocket 长链接。
实现断点下载，断点上传。
SSL证书信任验证

[服务端代码](https://github.com/clickListenerData/JavaTest)



