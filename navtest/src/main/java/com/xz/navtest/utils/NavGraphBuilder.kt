package com.xz.navtest.utils

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.lang.reflect.Type

/**
 * @title com.xz.navtest.utils  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des NavGraphBuilder
 * @DATE 2020/8/7  16:27 星期五
 */
typealias Intercept = (intercept: String) -> Boolean

object NavGraphBuilder {

    private lateinit var map : Map<String,List<CustomDestination>>
    private val controllerTagMap = HashMap<NavController,String>()
    var intercept: Intercept? = null

    // 往 navController  里 加入 自定义的navigator
    fun addNavigator(activity: FragmentActivity,control: NavController,containerId: Int) {
        val provider = control.navigatorProvider

        val fixNavigator = FixFragmentNavigator(activity,activity.supportFragmentManager,containerId)
        provider.addNavigator(fixNavigator)
    }

    fun setNavGraph(context: Context,control: NavController,controlTag: String = "") {
        val provider = control.navigatorProvider

        // NavController 创建的时候 会添加 四种 导航器
        // NavGraphNavigator  页面导航的一种  xml 中根节点 必须是它
        val graph = NavGraph(NavGraphNavigator(provider))
//        val listType = object : TypeToken<List<CustomDestination>>() {}.type
        val type = object : TypeToken<Map<String,List<CustomDestination>>>() {}.type
        if (!::map.isInitialized) {
            map = Gson().fromJson(readJson(context),type)
        }
        val list = map[controlTag] ?: return
        list.forEach {
            when (it.type) {
                0 -> {
                    // fragment
                    val navigator = provider.getNavigator(FragmentNavigator::class.java)
                    val destination = navigator.createDestination()
                    destination.id = it.id
                    destination.addDeepLink(it.pageUrl)
                    destination.className = it.className
                    graph.addDestination(destination)
                }
                1 -> {
                    Log.i("zzzzzzz","activity id:::" + it.id)
                    val navigator = provider.getNavigator(ActivityNavigator::class.java)
                    val destination = navigator.createDestination()
                    destination.id = it.id
                    destination.addDeepLink(it.pageUrl)
                    destination.setComponentName(ComponentName(context.packageName,it.className))
                    graph.addDestination(destination)
                }
                2 -> {  // fix fragment
                    val fixNavigator = provider.getNavigator(FixFragmentNavigator::class.java)
                    val destination = fixNavigator.createDestination()
                    destination.id = it.id
                    destination.addDeepLink(it.pageUrl)
                    destination.className = it.className
                    graph.addDestination(destination)
                }
            }
            if (it.isStart) {
                graph.startDestination = it.id
            }
        }
        control.graph = graph
        controllerTagMap[control] = controlTag
    }

    fun build(context: FragmentActivity,control: NavController,containerId: Int,controlTag : String = "") {
        addNavigator(context,control,containerId)
        setNavGraph(context,control,controlTag)
    }

    fun navigate(controller:NavController,id: Int) {
        val tag = controllerTagMap[controller] ?: return  // tag 为null  则 未加入graph
        val list = map[tag] ?: return
        list.forEach{
            if (it.id == id) {
                navigate(controller,id,it.intercept)
                return@forEach
            }
        }
    }

    fun navigate(controller: NavController,id: Int,intercept: String) {
        if (this.intercept?.invoke(intercept) != true) {  // 返回true 则拦截事件
            controller.navigate(id)
        }
    }

    private fun readJson(context: Context) :String {
        val stream = context.applicationContext.assets.open("destination.json")
        val reader = BufferedReader(InputStreamReader(stream))

        val builder = StringBuilder()
        var line = reader.readLine()
        while (line != null) {
            builder.append(line)
            line = reader.readLine()
        }
        return builder.toString()
    }
}