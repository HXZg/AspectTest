package com.xz.coroutinestest

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.view.SurfaceHolder

/**
 * @title com.kjd.cameramanager  VisitorMachine
 * @author xian_zhong  admin
 * @version 1.0
 * @Des CameraManager
 */
object CameraManager {

    private var mCamera: Camera? = null

    /**
     * 检查是否有相机
     */
    fun checkCameraHardware(context: Context) : Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    fun open() {
        val count = Camera.getNumberOfCameras()
        for (i in 0 until count) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i,info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera = Camera.open(i)
                break
            }
        }
        if (count == 1 && mCamera == null) {
            mCamera = Camera.open(0)
            return
        }
    }

    fun setParam(width: Int, height: Int) {
        val parameters = mCamera?.parameters
        val previewSize = getBestSupportedSize(parameters?.supportedPreviewSizes!!, width, height)

        parameters.setPreviewSize(previewSize.width, previewSize.height)
        mCamera?.parameters = parameters
    }

    fun getParam() = mCamera?.parameters

    private fun getBestSupportedSize(sizes: List<Camera.Size>, widthPixels: Int, heightPixels: Int): Camera.Size {
        var bestSize: Camera.Size = sizes[0]
        var screenRatio = widthPixels.toFloat() / heightPixels
        if (screenRatio > 1) {
            screenRatio = 1 / screenRatio
        }

        for (s in sizes) {
            if (Math.abs(s.height / s.width.toFloat() - screenRatio) < Math.abs(bestSize.height / bestSize.width.toFloat() - screenRatio)) {
                bestSize = s
            }
        }
        return bestSize
    }

    fun startPriview(holder: SurfaceHolder, previewAction: (data: ByteArray) -> Unit) {
        mCamera?.setDisplayOrientation(90)
        mCamera?.setPreviewDisplay(holder)
        mCamera?.startPreview()

        mCamera?.setPreviewCallback { data, _ ->
            previewAction.invoke(data)
        }
    }

    fun takePic(pic: (data: ByteArray,camera: Camera) -> Unit) {  // 拍照
        mCamera?.takePicture(null,null,pic)
    }

    fun getPreviewSize() : Camera.Size? {
        return mCamera?.parameters?.previewSize
    }

    fun stopCamera() {
        if (mCamera != null) {
            mCamera?.setPreviewCallback(null)
            mCamera?.stopPreview()
            mCamera?.release()
            mCamera = null
        }
    }


}