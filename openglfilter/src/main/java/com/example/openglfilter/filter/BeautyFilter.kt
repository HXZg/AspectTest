package com.example.openglfilter.filter

import android.content.Context
import android.opengl.GLES20
import com.example.openglfilter.R

class BeautyFilter(context: Context) : AbsFboFilter(context, R.raw.base_vert,R.raw.beauty_frag) {

    private var widthHandler = 0
    private var heightHandler = 0

    init {
        widthHandler = GLES20.glGetUniformLocation(mProgram,"width")
        heightHandler = GLES20.glGetUniformLocation(mProgram,"height")
    }

    override fun beforeDraw(texture: Int) {
        super.beforeDraw(texture)
        GLES20.glUniform1i(widthHandler,mWidth)
        GLES20.glUniform1i(heightHandler,mHeight)
    }
}