package com.example.openglfilter

import android.util.Size
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.lifecycle.LifecycleOwner

class CameraHelper(owner: LifecycleOwner,private val listener: Preview.OnPreviewOutputUpdateListener) {

    init {
        CameraX.bindToLifecycle(owner,getPreview())
    }

    private fun getPreview() : Preview {
        val config = PreviewConfig.Builder()
            .setLensFacing(CameraX.LensFacing.BACK)
            .build()

        val preview = Preview(config)
        preview.onPreviewOutputUpdateListener = listener
        return preview
    }
}