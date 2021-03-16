package com.example.openglfilter

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class CameraView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : GLSurfaceView(context,attributeSet) {


    init {
        setEGLContextClientVersion(2)
        setRenderer(CameraRender(this))
        renderMode = RENDERMODE_WHEN_DIRTY
    }
}