package com.example.openglfilter

import android.content.Context
import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class ScreenFilter(private val context: Context) {

    private val mProgram: Int
    private val vPosition: Int
    private val vCoord: Int
    private val vTextures: Int
    private val vMatrix: Int

    private val VERTEXT = floatArrayOf(-1f,-1f,
        1f,-1f,
        -1f,1f,
        1f,1f)  // 顶点坐标

    private val TEXTURES = floatArrayOf(0f,0f,
        1f,0f,
        0f,1f,
        1f,1f)  // 纹理坐标

    private val vertextBuffer : FloatBuffer =
        ByteBuffer.allocateDirect(4*4*2).order(ByteOrder.nativeOrder()).asFloatBuffer()
    private val textureBuffer: FloatBuffer = ByteBuffer.allocateDirect(4*4*2).order(ByteOrder.nativeOrder()).asFloatBuffer()

    private var mWidth = 0
    private var mHeight = 0

    private lateinit var mtx : FloatArray

    init {
        // float 四个字节  4个点坐标 每个坐标下x，y两点
        vertextBuffer.clear()
        vertextBuffer.put(VERTEXT)

        textureBuffer.clear()
        textureBuffer.put(TEXTURES)

        val vertexShader = OpenGLUtils.readRawTxtFile(context,R.raw.camera_vert)
        val fragShader = OpenGLUtils.readRawTxtFile(context,R.raw.camera_frag)

        mProgram = OpenGLUtils.loadProgram(vertexShader,fragShader)

        vPosition = GLES20.glGetAttribLocation(mProgram,"vPosition")  // GPU 坐标
        vCoord = GLES20.glGetAttribLocation(mProgram,"vCoord")  // 纹理坐标
        vTextures = GLES20.glGetUniformLocation(mProgram,"vTexture")  // 采样点坐标
        vMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix")  // 摄像头转换矩阵

    }

    fun setSize(width: Int,height: Int) {
        mWidth = width
        mHeight = height
    }

    fun setTransformMatrix(matrix: FloatArray) {
        mtx = matrix
    }

    fun onDraw(texture: Int) {
        GLES20.glViewport(0,0,mWidth,mHeight)
        GLES20.glUseProgram(mProgram)

        vertextBuffer.position(0)
        GLES20.glVertexAttribPointer(vPosition,2,GLES20.GL_FLOAT,false,0,vertextBuffer)
        GLES20.glEnableVertexAttribArray(vPosition)

        textureBuffer.position(0)
        GLES20.glVertexAttribPointer(vCoord,2,GLES20.GL_FLOAT,false,0,textureBuffer)
        GLES20.glEnableVertexAttribArray(vCoord)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture)
        GLES20.glUniform1i(vTextures,0)
        GLES20.glUniformMatrix4fv(vMatrix,1,false,mtx,0)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4)
    }



}