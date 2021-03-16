package com.example.openglfilter.filter

import android.content.Context
import android.opengl.GLES20
import com.example.openglfilter.R

class SoulFilter(context: Context) : AbsFboFilter(context, R.raw.base_vert,R.raw.soul_frag) {

    private var scalePercent = 0
    private var mixPercent = 0

    private var scale = 0f
    private var mix = 0f

    init {
        scalePercent = GLES20.glGetUniformLocation(mProgram,"scalePercent")
        mixPercent = GLES20.glGetUniformLocation(mProgram,"mixturePercent")
    }

    override fun beforeDraw(texture: Int) {
        super.beforeDraw(texture)
        GLES20.glUniform1f(scalePercent,scale + 1f)
        GLES20.glUniform1f(mixPercent,1f - mix)

        scale += 0.08f
        mix += 0.08f

        if (scale >= 1f) {   // 1-2
            scale = 0f
        }
        if (mix >= 1f) {  // 0-1
            mix = 0f
        }
    }
}