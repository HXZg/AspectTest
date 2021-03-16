package com.example.openglfilter

import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.GLSurfaceView
import androidx.camera.core.Preview
import androidx.lifecycle.LifecycleOwner
import com.example.openglfilter.filter.BeautyFilter
import com.example.openglfilter.filter.CameraFilter
import com.example.openglfilter.filter.record.MediaRecorder
import com.example.openglfilter.filter.record.RecordFilter
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraRender(private val view: CameraView) : GLSurfaceView.Renderer,
    Preview.OnPreviewOutputUpdateListener, SurfaceTexture.OnFrameAvailableListener {

    private var mCameraTexture : SurfaceTexture? = null

    private val textures = intArrayOf(0)
    private val mtx = FloatArray(16)

    private lateinit var cameraFilter: CameraFilter
    private lateinit var recordFilter: RecordFilter
    private lateinit var beautyFilter: BeautyFilter

    private lateinit var mRecorder : MediaRecorder

    init {

        CameraHelper(view.context as LifecycleOwner,this)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        mCameraTexture?.attachToGLContext(textures[0])
        mCameraTexture?.setOnFrameAvailableListener(this)
        // 滤镜
        cameraFilter = CameraFilter(view.context)
        recordFilter = RecordFilter(view.context)
        beautyFilter = BeautyFilter(view.context)

        mRecorder = MediaRecorder(view.context,"",EGL14.eglGetCurrentContext(),400,640)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        cameraFilter.setSize(width,height)
        recordFilter.setSize(width, height)
        beautyFilter.setSize(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        mCameraTexture?.updateTexImage()
        mCameraTexture?.getTransformMatrix(mtx)
        // 纹理坐标转换矩阵  设置给GPU
        cameraFilter.setTransformMatrix(mtx)
        var id = cameraFilter.onDraw(textures[0])  // 将数据输出到FBO中

        id = beautyFilter.onDraw(id)   // 美颜滤镜    数据输出FBO中

        id = recordFilter.onDraw(id)   // 渲染数据至屏幕

        mRecorder.fireFrame(id,mCameraTexture?.timestamp ?: 0L)  // 将每帧数据进行编码
    }

    override fun onUpdated(output: Preview.PreviewOutput?) {
        mCameraTexture = output?.surfaceTexture
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        view.requestRender()
    }

    fun startRecord(speed: Float) {
        mRecorder.start(speed)
    }

    fun stopRecord() {
        mRecorder.stop()
    }
}