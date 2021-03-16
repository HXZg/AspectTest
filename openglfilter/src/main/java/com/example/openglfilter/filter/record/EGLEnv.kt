package com.example.openglfilter.filter.record

import android.content.Context
import android.opengl.*
import android.view.Surface
import com.example.openglfilter.filter.EGLScreenFilter

class EGLEnv(val context: Context, glContext: EGLContext, val surface: Surface, val width: Int, val height: Int) {

    private lateinit var mDisplay: EGLDisplay
    private lateinit var eglConfig: EGLConfig
    private lateinit var eglContext: android.opengl.EGLContext
    private var eglSurface: EGLSurface?
    private lateinit var screenFilter: EGLScreenFilter

    init {
        mDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)

        if (mDisplay == EGL14.EGL_NO_DISPLAY) {
            throw RuntimeException("")
        }

        val version = intArrayOf(0,0)
        if (!EGL14.eglInitialize(mDisplay,version,0,version,1)) {
            throw RuntimeException("")
        }

        val configAttributes = intArrayOf(
            EGL14.EGL_RED_SIZE,8,
            EGL14.EGL_GREEN_SIZE,8,
            EGL14.EGL_BLUE_SIZE,8,
            EGL14.EGL_ALPHA_SIZE,8,
            EGL14.EGL_RENDERABLE_TYPE,EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_NONE
        )
        val numConfigs = intArrayOf(0)
        val configs = Array<EGLConfig?>(1){null}
        if (!EGL14.eglChooseConfig(mDisplay,configAttributes,0,configs,0,configs.size,numConfigs,0)) {
            throw RuntimeException()
        }

        eglConfig = configs[0]!!
        val contextAttribList = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION,2,
            EGL14.EGL_NONE
        )
        eglContext = EGL14.eglCreateContext(mDisplay,eglConfig,glContext,contextAttribList,0)

        if (eglContext == EGL14.EGL_NO_CONTEXT) {
            throw RuntimeException()
        }

        val surfaceAttributeList = intArrayOf(
            EGL14.EGL_NONE
        )

        eglSurface = EGL14.eglCreateWindowSurface(mDisplay,eglConfig,surface,surfaceAttributeList,0)

        if (eglSurface == null) {
            throw RuntimeException()
        }

        if (!EGL14.eglMakeCurrent(mDisplay,eglSurface,eglSurface,eglContext)) {
            throw RuntimeException()
        }
        screenFilter = EGLScreenFilter(context)
        screenFilter.setSize(width,height)
    }

    fun draw(textureId: Int,timestamp: Long) {
        screenFilter.onDraw(textureId)
        EGLExt.eglPresentationTimeANDROID(mDisplay,eglSurface,timestamp)
        EGL14.eglSwapBuffers(mDisplay,eglSurface) // 交换缓冲区  数据缓冲至surface
    }

    fun release() {
        EGL14.eglDestroySurface(mDisplay,eglSurface)
        EGL14.eglMakeCurrent(mDisplay,EGL14.EGL_NO_SURFACE,EGL14.EGL_NO_SURFACE,EGL14.EGL_NO_CONTEXT)
        EGL14.eglDestroyContext(mDisplay,eglContext)
        EGL14.eglReleaseThread()
        EGL14.eglTerminate(mDisplay)
        screenFilter.release()
    }

}