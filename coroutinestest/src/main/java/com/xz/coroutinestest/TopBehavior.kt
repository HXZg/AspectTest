package com.xz.coroutinestest

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des TopBehavior
 * @DATE 2020/10/17  18:15 星期六
 */
class TopBehavior(context : Context, attrs : AttributeSet) : CoordinatorLayout.Behavior<View>(context, attrs) {


    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: View,
        layoutDirection: Int
    ): Boolean {
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        child.translationY = -dependency.top.toFloat()
        return true
    }

}