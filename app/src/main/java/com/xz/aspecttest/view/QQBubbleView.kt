package com.xz.aspecttest.view

import android.animation.PointFEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator

/**
 * @title com.xz.aspecttest.view  AspectTest
 * @author xian_zhong  admin
 * @version 1.0
 * @Des QQBubbleView
 * @DATE 2020/8/12  10:10 星期三
 * 仿照 QQ气泡
 */
class QQBubbleView @JvmOverloads constructor(context: Context,attributeSet: AttributeSet? = null,defStyle: Int = 0) :
    View(context,attributeSet,defStyle) {


    private val BUBBLE_DEFAULT = 0  // 默认
    private val BUBBLE_CONNECT = 1  // 连接
    private val BUBBLE_DISMISS = 2  // 消失
    private val BUBBLE_RESET = 4    // 开始回弹动画
    private val BUBBLE_APART = 3    // 爆炸  此处 游离

    private var bubbleState = BUBBLE_DEFAULT

    // 气泡半径
    private var mBubbleRadius = 50f
    // 气泡中间文字颜色
    private var mTxtColor = Color.WHITE
    // 文字大小
    private var mTxtSize = 40f
    private var mTxtContent = "12"
    private val mTxtPaint = Paint()
    private var mBubbleStillRadius = 50f

    // 绘制气泡的画笔
    private val mBubblePaint= Paint()
    private var mBubbleColor = Color.RED

    // 贝赛尔 曲线 路径
    private val mBubblePath = Path()

    // view  宽高
    private lateinit var mBubbleStillCenter : PointF

    private lateinit var mBubbleMovedCenter : PointF

    // 连接状态时  两园圆心的距离
    private var mDist = 0.0

    private val mTxtRect = Rect()

    init {
        mTxtPaint.isAntiAlias = false
        mTxtPaint.color = mTxtColor
        mTxtPaint.textSize = mTxtSize

        mBubblePaint.isAntiAlias = false
        mBubblePaint.color = mBubbleColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (::mBubbleStillCenter.isInitialized) {
            mBubbleStillCenter.set(w/2f,h/2f)
        } else {
            mBubbleStillCenter = PointF(w/2f,h/2f)
        }

        if (::mBubbleMovedCenter.isInitialized) {
            mBubbleMovedCenter.set(w/2f,h/2f)
        } else {
            mBubbleMovedCenter = PointF(w/2f,h/2f)
        }
    }


    override fun onDraw(canvas: Canvas?) {

        if (bubbleState == BUBBLE_CONNECT) {
            canvas?.drawCircle(mBubbleStillCenter.x,mBubbleStillCenter.y,mBubbleStillRadius,mBubblePaint)

            // 贝赛尔 曲线
            // 两圆心之间的连线与X轴的夹角 cos sin 值  与 要求的 A,B,C,D 点 与 Y轴夹角相同
            val cosTra = (mBubbleMovedCenter.x - mBubbleStillCenter.x) / mDist
            val sinTra = (mBubbleMovedCenter.y - mBubbleStillCenter.y) / mDist

            //A点坐标  每个象限  四点指示的位置 不一样  但是 只要 A依旧与C相连 则不影响结果
            val pointAX = mBubbleStillCenter.x - sinTra * mBubbleStillRadius
            val pointAY = mBubbleStillCenter.y + cosTra * mBubbleStillRadius

            // B点 坐标
            val pointBX = mBubbleStillCenter.x + sinTra * mBubbleStillRadius
            val pointBY = mBubbleStillCenter.y - cosTra * mBubbleStillRadius

            // C点 坐标
            val pointCX = mBubbleMovedCenter.x - sinTra * mBubbleRadius
            val pointCY = mBubbleMovedCenter.y + cosTra * mBubbleRadius

            // D点坐标
            val pointDX = mBubbleMovedCenter.x + sinTra * mBubbleRadius
            val pointDY = mBubbleMovedCenter.y - cosTra * mBubbleRadius

            // G点坐标  控制点
            val pointGX = (mBubbleMovedCenter.x + mBubbleStillCenter.x) / 2
            val pointGY = (mBubbleMovedCenter.y + mBubbleStillCenter.y) / 2

            mBubblePath.reset()
            mBubblePath.moveTo(pointAX.toFloat(),pointAY.toFloat())
            mBubblePath.quadTo(pointGX,pointGY, pointCX.toFloat(), pointCY.toFloat())
            mBubblePath.lineTo(pointDX.toFloat(), pointDY.toFloat())
            mBubblePath.quadTo(pointGX,pointGY, pointBX.toFloat(), pointBY.toFloat())
            mBubblePath.close()
            canvas?.drawPath(mBubblePath,mBubblePaint)
        }

        if (bubbleState != BUBBLE_DISMISS) {
            canvas?.drawCircle(mBubbleMovedCenter.x,mBubbleMovedCenter.y,mBubbleRadius,mBubblePaint)
            mTxtPaint.getTextBounds(mTxtContent,0,mTxtContent.length,mTxtRect)
            canvas?.drawText(mTxtContent,mBubbleMovedCenter.x - mTxtRect.width()/2f,
                mBubbleMovedCenter.y + mTxtRect.height()/2f,mTxtPaint)
        }

        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 气泡消失  则 没有处理的必要
                if (bubbleState != BUBBLE_DISMISS) {
                    // 两圆心之间的距离
                    mDist = Math.hypot((event.x - mBubbleStillCenter.x).toDouble(),
                        (event.y - mBubbleStillCenter.y).toDouble()
                    )

                    // 大于默认园的半径  则不再触摸的范围之内
                    bubbleState = if (mDist > mBubbleRadius) {
                        BUBBLE_DEFAULT
                    } else {
                        BUBBLE_CONNECT
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (bubbleState != BUBBLE_CONNECT) return true
                mBubbleMovedCenter.x = event.x
                mBubbleMovedCenter.y = event.y

                mDist = Math.hypot((event.x - mBubbleStillCenter.x).toDouble(),
                    (event.y - mBubbleStillCenter.y).toDouble()
                )
                if (bubbleState == BUBBLE_CONNECT) {
                    if (mDist > 8*mBubbleRadius) {
                        bubbleState = BUBBLE_APART  // 游离状态 无需绘制 path
                    } else {
                        mBubbleStillRadius = (mBubbleRadius - mDist / 8).toFloat()
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                // 根据状态 做动画效果
                if (bubbleState == BUBBLE_CONNECT) {
                    // 回弹动画
                    startBubbleRestAnim()
                } else if (bubbleState == BUBBLE_APART) {
                    if (mDist < 8* mBubbleRadius) {  // 8倍 更容易触发
                        // 回弹动画
                        startBubbleRestAnim()
                    } else {
                        // 爆炸动画
                        startBubbleApartAnim()
                    }
                }
            }
        }
        return true
    }
    
    private fun startBubbleRestAnim() {
        bubbleState = BUBBLE_RESET
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val anim = ValueAnimator.ofObject(PointFEvaluator(),
                                    PointF(mBubbleMovedCenter.x,mBubbleMovedCenter.y),
                PointF(mBubbleStillCenter.x,mBubbleStillCenter.y))
            anim.duration = 400
            anim.interpolator = OvershootInterpolator(5f)
            anim.addUpdateListener {
                mBubbleMovedCenter = it.animatedValue as PointF
                invalidate()
            }
            anim.start()
        }
    }

    private fun startBubbleApartAnim() {
        // 爆炸动画
        bubbleState = BUBBLE_DISMISS
        invalidate()
    }
}