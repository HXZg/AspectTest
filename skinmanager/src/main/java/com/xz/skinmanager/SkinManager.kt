package com.xz.skinmanager

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log

/**
 * @title com.xz.skinmanager  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des SkinManager
 * @DATE 2020/8/26  10:56 星期三
 * 资源替换  主要是替换 color and drawable
 */
object SkinManager {
    private lateinit var context: Context
    private lateinit var resources : Resources
    private lateinit var packageName: String

    fun initContext(context: Context) {
        this.context = context
    }

    fun loadPath(path: String) {
        // 获取到 资源包 的 包名
        packageName =
            context.packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES).packageName
        Log.i("zzzzzzzzzz","$path ,,  $packageName")
        // 构建 资源包的 resource
        val assetManager = AssetManager::class.java.newInstance()
        val method = AssetManager::class.java.getDeclaredMethod("addAssetPath",String::class.java)
        method.invoke(assetManager,path)
        resources = Resources(assetManager,context.resources.displayMetrics,context.resources.configuration)
    }

    fun getColor(resId: Int) : Int {
        if (!resourceIsInit()) {
            return context.resources.getColor(resId)
        }
        val identifier = getResId(resId)
        if (identifier == 0) {  // 没有找到对应的资源
            return context.resources.getColor(resId)
        }
        return resources.getColor(identifier)
    }

    fun getResourceId(resId: Int) : Int {
        if (!resourceIsInit()) {
            return resId
        }
        val identifier = getResId(resId)
        if (identifier == 0) {  // 没有找到对应的资源
            return resId
        }
        return identifier
    }

    fun getDrawable(resId: Int) : Drawable {
        if (!resourceIsInit()) {
            return context.resources.getDrawable(resId)
        }
        val identifier = getResId(resId)
        if (identifier == 0) {  // 没有找到对应的资源
            return context.resources.getDrawable(resId)
        }
        return resources.getDrawable(identifier)
    }

    private fun getResId(resId: Int) : Int {
        val resourceEntryName = context.resources.getResourceEntryName(resId)
        val resourceTypeName = context.resources.getResourceTypeName(resId)
        return resources.getIdentifier(resourceEntryName, resourceTypeName, packageName)
    }

    private fun resourceIsInit() = ::resources.isInitialized

}