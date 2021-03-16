package com.example.openglfilter.filter

import android.content.Context
import android.opengl.GLES20

abstract class AbsFboFilter(context: Context,vertRes: Int,fragRes: Int) : AbsFilter(context, vertRes, fragRes) {


    private val frameBuffer = intArrayOf(0)
    private val frameTextures = intArrayOf(0)

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        releaseFrame()
        GLES20.glGenFramebuffers(1,frameBuffer,0)  // 生成FBO  frame buffer object 离屏渲染

        GLES20.glGenTextures(frameTextures.size,frameTextures,0)  // 生成纹理

        frameTextures.forEach {
            // bind it/0  配套使用 后续原子性操作 配置绑定纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,it)

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST)  //放大过滤
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR)   // 缩小过滤

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,frameTextures[0])

        /**
         * 指定一个二维的纹理图片
         * level
         *     指定细节级别，0级表示基本图像，n级则表示Mipmap缩小n级之后的图像（缩小2^n）
         * internalformat
         *     指定纹理内部格式，必须是下列符号常量之一：GL_ALPHA，GL_LUMINANCE，GL_LUMINANCE_ALPHA，GL_RGB，GL_RGBA。
         * width height
         *     指定纹理图像的宽高，所有实现都支持宽高至少为64 纹素的2D纹理图像和宽高至少为16 纹素的立方体贴图纹理图像 。
         * border
         *     指定边框的宽度。必须为0。
         * format
         *     指定纹理数据的格式。必须匹配internalformat。下面的符号值被接受：GL_ALPHA，GL_RGB，GL_RGBA，GL_LUMINANCE，和GL_LUMINANCE_ALPHA。
         * type
         *     指定纹理数据的数据类型。下面的符号值被接受：GL_UNSIGNED_BYTE，GL_UNSIGNED_SHORT_5_6_5，GL_UNSIGNED_SHORT_4_4_4_4，和GL_UNSIGNED_SHORT_5_5_5_1。
         * data
         *     指定一个指向内存中图像数据的指针。
         */
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGBA,width,height,0,GLES20.GL_RGBA,GLES20.GL_UNSIGNED_BYTE,null)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,frameBuffer[0])

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER,GLES20.GL_COLOR_ATTACHMENT0,GLES20.GL_TEXTURE_2D,frameTextures[0],0)

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)
    }

    fun releaseFrame() {
        GLES20.glDeleteTextures(1,frameTextures,0)

        GLES20.glDeleteFramebuffers(1,frameBuffer,0)
    }

    override fun onDraw(texture: Int): Int {
        //数据输出倒FBO中
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,frameTextures[0])
        super.onDraw(texture)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0)
        return frameTextures[0]
    }
}