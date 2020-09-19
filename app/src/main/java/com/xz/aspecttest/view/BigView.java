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
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * 大图加载  长宽 同时缩小
 */
public class BigView extends View implements View.OnTouchListener,
        GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {

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
    private float originalScale;

    private ScaleGestureDetector mScaleGestureDetector;

    public BigView(Context context) {
        this(context,null);
    }

    public BigView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BigView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRect = new Rect();  // 图片加载 显示到屏幕上的部分
        mOptions = new BitmapFactory.Options();  // 获取图片参数
        mGestureDetector = new GestureDetector(context,this);  // 手势处理
        mScroller = new Scroller(context);  // 滚动处理
        setOnTouchListener(this);  // 事件处理
        // 添加缩放手势识别
        mScaleGestureDetector = new ScaleGestureDetector(context,new ScaleGesture());

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

        // 确定图片加载区域  没有放大缩小 初始的时候
//        mRect.left = 0;
//        mRect.top = 0;
//        mRect.right = mImageWidth;
//        mScale = mViewWidth / (float)mImageWidth;
//        mRect.bottom = (int)(mViewHeight / mScale);  // 第一块 显示的图片 再 原始区域的位置
        // 显示时经过缩放后，应该正好 为 view 的 高度  所以 图片区域高度 为 mViewHeight / mScale

        // 加入缩放因子之后 的 逻辑
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = Math.min(mImageWidth,mViewWidth);  // 图片放大了  看到的区域不变
        mRect.bottom = Math.min(mImageHeight,mViewHeight);

        // 在定义一个缩放因子
        originalScale = mViewWidth/(float)mImageWidth;
        mScale = originalScale;
    }

    private float tempScale;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDecoder == null) return;

        mOptions.inBitmap = mBitmap;
        mBitmap = mDecoder.decodeRegion(mRect,mOptions);
        Matrix matrix = new Matrix();
        tempScale = mViewWidth / (float) mRect.width();  // mscale  实时变化
        matrix.setScale(tempScale,tempScale);  //为什么使用 tempScale
        canvas.drawBitmap(mBitmap,matrix,null);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
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
        mRect.offset((int)distanceX,(int)distanceY);
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

        if (mRect.right > mImageWidth) {
            mRect.right = mImageWidth;
            mRect.left = mImageWidth - (int)(mViewWidth / mScale);
        }
        if (mRect.left < 0) {
            mRect.left = 0;
            mRect.right = (int)(mViewWidth / mScale);
        }
        invalidate();
        return false;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
//        mScroller.fling(0,mRect.top,0,(int)-velocityY,0,0,0,
//                mImageHeight - (int)(mViewHeight/mScale));
        // 会调用 computeScroll 计算滚动
        mScroller.fling(mRect.left,mRect.top,(int)-velocityX,(int)-velocityY,0,
                mImageWidth - (int)(mViewWidth / mScale),0,
                mImageHeight - (int)(mViewHeight/mScale));
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

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        // 双击放大图片
        if (mScale < originalScale * 2) {  // 放大2倍
            mScale = originalScale * 2;
        } else {
            mScale = originalScale;
        }
        int centerX = (int) motionEvent.getX();
        int centerY = (int) motionEvent.getY();
        int width = (int)(mViewWidth / mScale);
        int height = (int)(mViewHeight / mScale);

//        mRect.left += (centerX - mViewWidth / 2);
//        mRect.top = centerY - height;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;

        // 移动时 处理到达顶部和底部的情况
        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight;
            mRect.top = mImageHeight - height;  // 根部底部值计算出需要显示的高度
            //显示时  会 缩放 mScale   最终 bottom-top  ==  viewHeight
        }
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = height;
        }
        if (mRect.right > mImageWidth) {
            mRect.right = mImageWidth;
            mRect.left = mImageWidth - width;
        }
        if (mRect.left < 0) {
            mRect.left = 0;
            mRect.right = width;
        }
        invalidate();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = mScale;
            scale += detector.getScaleFactor() - 1;  //跟上一次事件相比 得到 的 事件因子
            if (scale < originalScale){
                scale = originalScale;
            }else if (scale > originalScale * 2) { // 最大放大两倍
                scale = originalScale * 2;
            }
            mRect.right = mRect.left + (int)(mViewWidth / scale);
            mRect.bottom = mRect.top + (int)(mViewHeight / scale);
            mScale = scale;
            invalidate();
            return super.onScale(detector);
        }
    }
}
