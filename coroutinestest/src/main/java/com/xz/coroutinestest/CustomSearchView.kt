package com.xz.coroutinestest

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.StaticLayout
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import kotlin.math.min

/**
 * @title com.xz.coroutinestest  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des CustomSearchView
 * @DATE 2020/10/19  16:09 星期一
 */
class CustomSearchView(context: Context,attributeSet: AttributeSet) : View(context,attributeSet) {


    // 图标以及背景画笔  文字画笔
    private val imgPaint by lazy { Paint() }
    private val txtPaint by lazy { TextPaint() }

    // 左右两边图标
    private val leftBmp by lazy {
        BitmapFactory.decodeResource(resources,android.R.drawable.ic_menu_search)
    }

    private val rightBmp by lazy {
        BitmapFactory.decodeResource(resources,android.R.drawable.ic_menu_camera)
    }

    // view 宽高
    private var mWidth = 0
    private var mHeight = 0

    // 左右两边图标 位置
    private var leftTop = 0f
    private var leftLeft = 0f
    private var rightTop = 0f
    private var rightLeft = 0f

    private val txtRect = Rect()
    private val txtList = arrayListOf("测试一|测试二|测试三","测试四|测试五|测试六","测试七|测试八")

    var mDrawWidth = 0f
    var mSpace = 0f

    private var isNext = false
    private var mIndex = 0

    private val mHandler = android.os.Handler(Looper.getMainLooper())
    private val animRun = Runnable {
        isNext = true
        startAnimator()
    }

    private val views = ArrayList<Rect>()  // 绘制view 用于判断点击事件

    init {
        //imgPaint
//        val drawable = GradientDrawable()
//        drawable.setStroke(2,Color.BLUE)
//
//        background = drawable

        imgPaint.color = Color.BLUE
        txtPaint.color = Color.BLUE
        txtPaint.textSize = 30f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            mHeight = Math.max(leftBmp.height,rightBmp.height) + 40
            setMeasuredDimension(mWidth,mHeight)
        }

        leftTop = (mHeight - leftBmp.height) / 2f
        leftLeft = 20f
        rightTop = (mHeight - rightBmp.height) / 2f
        rightLeft = (mWidth - rightBmp.width - 20f)

        views.add(Rect(0,0,leftBmp.width,mHeight))
        views.add(Rect(rightLeft.toInt(),0,mWidth,mHeight))
        views.add(Rect(0,0,0,mHeight))
        views.add(Rect(0,0,0,mHeight))
        views.add(Rect(0,0,0,mHeight))

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        imgPaint.strokeWidth = 2f
        imgPaint.isAntiAlias = false
        imgPaint.style = Paint.Style.STROKE
        canvas?.drawRect(0f,0f,mWidth.toFloat(),mHeight.toFloat(),imgPaint)

        canvas?.drawBitmap(leftBmp,leftLeft,leftTop,imgPaint)

        canvas?.drawBitmap(rightBmp,rightLeft,rightTop,imgPaint)

        if (isNext) {
            drawTxtLine(txtList[mIndex],canvas)
            drawTxtLine(txtList[nextIndex()],canvas,true)
        } else {
            drawTxtLine(txtList[mIndex],canvas,true)
        }

//        drawSpannable(txtList[mIndex],canvas)
    }

    private fun drawTxtLine(txt: String,canvas: Canvas?,isNext : Boolean = false) {
        mDrawWidth = leftLeft + leftBmp.width
        val split = txt.split("|")
        split.forEachIndexed{index: Int,s: String ->
            if (isNext) {
                views[index + 2].left = mDrawWidth.toInt()
                drawNextTxt(s,canvas)
                drawNextTxt("|",canvas)
                views[index + 2].right = mDrawWidth.toInt()
            } else {
                drawTxt(s,canvas)
                drawTxt("|",canvas)
            }
        }
    }

    private fun drawNextTxt(txt: String,canvas: Canvas?) {
        txtPaint.getTextBounds(txt,0,txt.length,txtRect)
        txtPaint.alpha = (255 * (1 - mSpace)).toInt()
        val top = (mHeight + txtRect.height()) / 2f
        val newHeight = top + (mHeight - top) * mSpace
        canvas?.drawText(txt,mDrawWidth,newHeight,txtPaint)
        mDrawWidth += (txtRect.width() + 20f)
    }

    private fun drawTxt(txt: String,canvas: Canvas?) {
        txtPaint.getTextBounds(txt,0,txt.length,txtRect)
        txtPaint.alpha = (255 * mSpace).toInt()
        val top = (mHeight + txtRect.height()) / 2f
        canvas?.drawText(txt,mDrawWidth,top * mSpace,txtPaint)
        mDrawWidth += (txtRect.width() + 20f)
    }

    private fun drawSpannable(text: String,canvas: Canvas?) {
        mDrawWidth = leftLeft + leftBmp.width
        val txt = getSpannableString(text)
//        val desiredWidth = StaticLayout.getDesiredWidth(txt, 0, txt.length, txtPaint)
//        txtPaint.getTextBounds(txt,0,txt.length,txtRect)
        canvas?.drawText(txt,0,txt.length - 1,mDrawWidth,mHeight/2f,txtPaint)
    }

    private fun getSpannableString(text: String) : CharSequence{
        val spannable = SpannableStringBuilder()
        val txts = text.split("|")
        txts.forEachIndexed { index, s ->
            val start = spannable.length
            spannable.append(s)
            val clickSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Log.i("zzzzzzzzzzz",s)
                }
            }
            spannable.setSpan(clickSpan,start,start + s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.append("|")
        }
        return spannable
    }

    private fun startAnimator() {
        val anim = ValueAnimator.ofFloat(1f,0f)
        anim.duration = 500
        anim.addUpdateListener {
            mSpace = 1 - it.animatedFraction
            invalidate()
            if (mSpace == 0f) {
                isNext = false
                mIndex = nextIndex()
                mHandler.postDelayed(animRun,3000)
            }
        }
        anim.setInterpolator {
            return@setInterpolator it
        }

        anim.setEvaluator { fraction, startValue, endValue ->

        }
        anim.start()
    }

    private fun nextIndex() : Int {
        return (mIndex + 1) % txtList.size
    }

    fun startIndexAnim() {
        mHandler.postDelayed(animRun,3000)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            views.forEachIndexed {index: Int,it:Rect ->
                if (it.contains(event.x.toInt(),event.y.toInt())) {
                    if (index > 1) {
                        val newIndex = index - 2
                        val split = txtList[mIndex].split("|")
                        if (split.size <= newIndex) {
                            Toast.makeText(context,"搜索",Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context,"搜索${split[newIndex]}",Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        if (index == 0) {
                            Toast.makeText(context,"搜索图标",Toast.LENGTH_SHORT).show()
                        } else if (index == 1) {
                            Toast.makeText(context,"扫描图标",Toast.LENGTH_SHORT).show()
                        }
                    }
                    Log.i("zzzzzzzzzzzzz","$index")
                    return true
                }
            }
            Toast.makeText(context,"搜索",Toast.LENGTH_SHORT).show()
            Log.i("zzzzzzzzzz","last back")
        }
        return true
    }


}