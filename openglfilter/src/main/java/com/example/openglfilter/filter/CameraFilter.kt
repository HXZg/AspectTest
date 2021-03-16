package com.example.openglfilter.filter

import android.content.Context
import android.opengl.GLES20
import com.example.openglfilter.R

class CameraFilter(context: Context) : AbsFboFilter(context, R.raw.camera_vert, R.raw.camera_frag) {

    private val vMatrix: Int = GLES20.glGetUniformLocation(mProgram, "vMatrix")
    private lateinit var mtx: FloatArray

    override fun beforeDraw(texture: Int) {
        super.beforeDraw(texture)
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mtx,0)
    }

    fun setTransformMatrix(mtx: FloatArray?) {
        this.mtx = mtx!!
    }

}