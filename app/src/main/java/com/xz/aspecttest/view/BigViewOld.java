package com.xz.aspecttest.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * 大图加载  长宽 同时缩小
 */
public class BigViewOld extends View implements View.OnTouchListener,GestureDetector.OnGestureListener {

    private Rect mRect;
    private BitmapFactory.Options mOptions;
    private GestureDetector mGestureDetector;
    private Scroller mScroller;
    // 原始图片的宽高
    private int mImageWidth;
    private int mImageHeight;
    // view 宽高
    private int mViewWidth;
    private int mViewHeight;
    // 缩放系数
    private float mScale;

    // 区域节码器
    private BitmapRegionDecoder mDecoder;
    // 复用内存的图片
    private Bitmap mBitmap;

    public BigViewOld(Context context) {
        this(context,null);
    }

    public BigViewOld(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BigViewOld(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRect = new Rect();  // 图片加载 显示到屏幕上的部分
        mOptions = new BitmapFactory.Options();  // 获取图片参数
        mGestureDetector = new GestureDetector(context,this);  // 手势处理
        mScroller = new Scroller(context);  // 滚动处理
        setOnTouchListener(this);  // 事件处理
    }

    public void setImage(InputStream is) {
        // 获取图片的宽和高
        mOptions.inJustDecodeBounds = true;  // 只加载计算原始图片的宽高  不加载进内存
        BitmapFactory.decodeStream(is,null,mOptions);
        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;
        mOptions.inJustDecodeBounds = false;

        // 开启复用
        mOptions.inMutable = true;
        // 设置图片格式
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;  // 16 位 2字节 ; ARGB_8888  占32位 4字节

        try {
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();

        // 确定图片加载区域
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mImageWidth;
        mScale = mViewWidth / (float)mImageWidth;
        mRect.bottom = (int)(mViewHeight / mScale);  // 第一块 显示的图片 再 原始区域的位置
        // 显示时经过缩放后，应该正好 为 view 的 高度  所以 图片区域高度 为 mViewHeight / mScale
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDecoder == null) return;

        mOptions.inBitmap = mBitmap;
        mBitmap = mDecoder.decodeRegion(mRect,mOptions);
        Matrix matrix = new Matrix();
        matrix.setScale(mScale,mScale);
        canvas.drawBitmap(mBitmap,matrix,null);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);  // 再次进行触摸时 结束上一次的滚动
        }
        return true;  // 返回true 才能处理后续事件
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
        mRect.offset(0,(int)distanceY);
        // 移动时 处理到达顶部和底部的情况
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight;
            mRect.top = mImageHeight - (int)(mViewHeight / mScale);  // 根部底部值计算出需要显示的高度
            //显示时  会 缩放 mScale   最终 bottom-top  ==  viewHeight
        }
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = (int)(mViewHeight/mScale);
        }
        invalidate();
        return false;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
        mScroller.fling(0,mRect.top,0,(int)-velocityY,0,0,0,
                mImageHeight - (int)(mViewHeight/mScale));
        // 会调用 computeScroll 计算滚动
        return false;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.isFinished()) {
            return;
        }
        if (mScroller.computeScrollOffset()) {  // 再滚动中
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top + (int)(mViewHeight / mScale);
            invalidate();
        }
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }
}
