package com.xz.skinmanager

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.xz.skinmanager.bean.SkinItem
import com.xz.skinmanager.bean.SkinView
import java.lang.Exception
import java.lang.reflect.Constructor
import java.util.*

/**
 * @title com.xz.skinmanager  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des SkinFactory
 * @DATE 2020/8/27  9:05 星期四
 */
class SkinFactory(private val appCompat: AppCompatDelegate?) : LayoutInflater.Factory2 {

    // 存储 当前 界面 所有需要换肤的view
    private val views = arrayListOf<SkinView>()

    // 缓存 view 的 构造方法 避免重复反射获取
    private val sConstructorMap =
        HashMap<String, Constructor<out View?>>()

    // 系统 view 可能 存在的包名
    private val sClassPrefixList = arrayOf(
        "android.widget.",
        "android.webkit.",
        "android.app.",
        "android.view."
    )

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {

        var currentView : View? = null
        if (appCompat != null) {  // 兼容 AppCompatActivity
            currentView = appCompat.createView(parent, name, context, attrs)
        }
        if (currentView == null) {
            // 反射获取 view 实例
            // 判断 name 是否包含包名
            if (name.contains(".")) {  // 包含 包名
                currentView = onCreateView(name, context, attrs)
            } else {  // 为 name 添加 包名 前缀
                sClassPrefixList.forEach {
                    currentView = onCreateView("$it$name", context, attrs)
                    if (currentView != null) {  // 获取到 view 的实例 退出for循环
                        return@forEach
                    }
                }
            }
        }

        if (currentView != null) parseAttribute(context,attrs,currentView!!)
        return currentView
    }

    private fun parseAttribute(context: Context,attrs: AttributeSet,view: View) {
        val ty = context.obtainStyledAttributes(attrs,R.styleable.Skin)
        val isSkin = ty.getBoolean(R.styleable.Skin_is_skin, false)
        if (isSkin) {
            val items = arrayListOf<SkinItem>()
            for (i in 0 until attrs.attributeCount) {
                val name = attrs.getAttributeName(i)
                // 判断是否为 需要换肤的属性 默认  src / background / textColor
                if (name.contains("src") || name.contains("background") || name.contains("textColor")) {
                    val value = attrs.getAttributeValue(i)
                    val resId = Integer.parseInt(value.substring(1))  // 需要换肤的资源ID  截取第一个字符@

                    // resId  是 color or drawable
                    val typeName = context.resources.getResourceTypeName(resId)

                    // 根据 typeName 重新设置 view 对应的属性
                    items.add(SkinItem(name,resId,typeName))
                }
            }
            // 存储 需要换肤的view
            if (items.isNotEmpty()) {
                views.add(SkinView(view,items))
            }
        }
        ty.recycle()
    }

    // 外部调用 此方法 换肤
    fun apply() {
        Log.i("zzzzzzzzzzz views",views.size.toString())
        views.forEach {
            it.apply()
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        var view : View? = null
        // 反射 获取 view的 实例
        try {
            val clazz = context.classLoader.loadClass(name).asSubclass(View::class.java)
            var constructor = sConstructorMap[name]
            if (constructor == null) {
                constructor = clazz.getConstructor(Context::class.java, AttributeSet::class.java) // 获取 view 两个参数的构造方法
//                constructor.isAccessible = true
                sConstructorMap[name] = constructor
            }
            view = constructor.newInstance(context,attrs)
        }catch (e: Exception) {

        }
        return view
    }
}