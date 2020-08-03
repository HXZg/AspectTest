package com.example.customplugin

import com.android.SdkConstants
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import org.gradle.api.Project

class CustomTransform extends Transform {

    // 字节码 池  很耗内存
    def pool = ClassPool.default

    def project

    CustomTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "huaiXZ"
    }

    // 对class 文件 进行处理
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    // 作用域： 整个项目
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    // false  每次运行时  都进行编译
    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        project.android.bootClasspath.each {
            pool.appendClassPath(it.absolutePath)
        }
        println("huaixz,,,,,,,,,,,,,,,,,,,,,,,,,,,")
        transformInvocation.inputs.each {
            it.jarInputs.each {
                pool.insertClassPath(it.file.absolutePath)
                // jar  中的文件不进行处理 原样输出到目标文件
                def dest = transformInvocation.outputProvider.getContentLocation(it.name,it.contentTypes,it.scopes, Format.JAR) // 获取输出的位置
                FileUtils.copyFile(it.file,dest)
            }

            it.directoryInputs.each {
                println("${it.file.absolutePath}")
                // 获取到 整个文件的目录
                def preName = it.file.absolutePath
                pool.insertClassPath(preName)
                findTarget(it.file,preName)
                def dest = transformInvocation.outputProvider.getContentLocation(it.name,it.contentTypes,it.scopes, Format.DIRECTORY) // 获取输出的位置
                FileUtils.copyDirectory(it.file,dest)
            }
        }
    }

    private void findTarget(File file,String fileName) {
        if (file.isDirectory()) {
            // 是文件夹 递归遍历
            file.listFiles().each {
                findTarget(it,fileName)
            }
        }else {
            modifyFile(file,fileName)
        }
    }

    private void modifyFile(File file,String fileName) {
        def filePath = file.absolutePath
        if (!filePath.endsWith(SdkConstants.DOT_CLASS)) {
            // 不是以 .class 结尾 不做处理
            return
        }
        if (filePath.contains('R$') || filePath.contains('R.class') || filePath.contains('BuildConfig.class')) {
            // 不处理 系统生成的文件
            return
        }

        // 获取全类名  去掉 文件夹目录  替换 \ 为 .
        def className = filePath.replace(fileName,"")
        .replace("/",".").replace("\\",".")
        // 去掉 后缀名  去掉 第一个字符.
        def name = className.replace(SdkConstants.DOT_CLASS,"").substring(1)
        println("huaixz::::$name")
        CtClass ctClass = pool.get(name)  // 从 池中获取到对应的类
        if (name.contains("com.xz.plugintest")) {
            // 再 这个包名 下的文件 才进行处理
            def code = "android.util.Log.i(\"huaixz\",\"zzzzzzzzzzzzzzzzzzzzzzzz\");"
            addCode(ctClass,code,fileName)
        }
        ctClass.detach()
    }

    private void addCode(CtClass ctClass,String code,String fileName) {
        def methods = ctClass.getDeclaredMethods()
        methods.each {
            it.insertBefore(code)
        }
        ctClass.writeFile(fileName)
    }
}