package com.xz.skinmanager.bean

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.xz.skinmanager.SkinManager

/**
 * @title com.xz.skinmanager.bean  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des SkinView
 * @DATE 2020/8/27  9:07 星期四
 *
 * 装载 需要换肤 的view  以及 需要替换资源的属性集合
 */
data class SkinView(val view: View,val items: List<SkinItem>) {

    fun apply() {
        items.forEach {
            if (it.attributeName == "textColor") {
                (view as TextView).setTextColor(SkinManager.getColor(it.attributeValue))
            } else if (it.attributeName == "background") {
                if (it.typeName == "color") {
                    view.setBackgroundColor(SkinManager.getColor(it.attributeValue))
                } else if (it.typeName == "drawable" || it.typeName == "mipmap") {
                    view.background = SkinManager.getDrawable(it.attributeValue)
                }
            } else if (it.attributeName == "src") {
                if (it.typeName == "drawable" || it.typeName == "mipmap") {
                    (view as ImageView).setImageDrawable(SkinManager.getDrawable(it.attributeValue))
                } else if (it.typeName == "color") {
                    (view as ImageView).setImageResource(SkinManager.getResourceId(it.attributeValue))
                }
            }
        }
    }
}