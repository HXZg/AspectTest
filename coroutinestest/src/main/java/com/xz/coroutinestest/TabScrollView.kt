package com.xz.coroutinestest

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des TabScorllView
 * @DATE 2020/10/20  11:28 星期二
 */
class TabScrollView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyle: Int = 0) :
    NestedScrollView(context,attributeSet,defStyle) {


    private lateinit var headerView : ViewGroup
    private lateinit var contentView: ViewGroup


    override fun onFinishInflate() {
        super.onFinishInflate()
        val vg = (getChildAt(0) as ViewGroup)
        headerView = vg.getChildAt(0) as ViewGroup
        contentView = vg.getChildAt(1) as ViewGroup
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val layoutParams = contentView.layoutParams.apply { height = measuredHeight }
        contentView.layoutParams = layoutParams
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
//        super.onNestedPreScroll(target, dx, dy, consumed, type)

        //向上滚动并且滚动的距离小于头部控件的高度，则此时父控件先滚动并记录消费的滚动距离

        //super.onNestedPreScroll(target, dx, dy, consumed, type);
        /*
          第二个要点：先让NestedScrollingParent3滑动到顶部后，NestedScrollingChild3才可以滑动
          解决方法:由于NestedScrollView即实现了NestedScrollingParent3又实现了NestedScrollingChild3，
                  所以super.onNestedPreScroll(target, dx, dy, consumed, type)内部实现又会去调用父控件
                  的onNestedPreScroll方法，就会出现NestedScrollView无法滑动到顶部的想象，所以此处
                  注释掉super.onNestedPreScroll(target, dx, dy, consumed, type)，实现滑动逻辑
         */

        //向上滚动并且滚动的距离小于头部控件的高度，则此时父控件先滚动并记录消费的滚动距离
        val hideTop = dy > 0 && scrollY < headerView.measuredHeight

        if (hideTop) {
            //滚动到相应的滑动距离
            scrollBy(0, dy)
            //记录父控件消费的滚动记录，防止子控件重复滚动
            consumed[1] = dy
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed, type)
        }
    }





}