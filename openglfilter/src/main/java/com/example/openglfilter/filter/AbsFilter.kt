package com.example.openglfilter.filter

import android.content.Context
import android.opengl.GLES20
import com.example.openglfilter.OpenGLUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * 滤镜 封装顶点与片元程序得使用
 * 顶点程序 对应坐标 每层滤镜都不会改变
 *
 */
abstract class AbsFilter(private val context: Context,private val vertRes: Int,private val fragRes: Int) {

    protected val mProgram: Int
    private val vPosition: Int
    private val vCoord: Int
    private val vTextures: Int
//    private val vMatrix: Int

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

    protected var mWidth = 0
    protected var mHeight = 0

//    private lateinit var mtx : FloatArray

    init {
        // float 四个字节  4个点坐标 每个坐标下x，y两点
        vertextBuffer.clear()
        vertextBuffer.put(VERTEXT)

        textureBuffer.clear()
        textureBuffer.put(TEXTURES)

        val vertexShader = OpenGLUtils.readRawTxtFile(context, vertRes)
        val fragShader = OpenGLUtils.readRawTxtFile(context,fragRes)

        mProgram = OpenGLUtils.loadProgram(vertexShader,fragShader)

        vPosition = GLES20.glGetAttribLocation(mProgram,"vPosition")  // GPU 坐标
        vCoord = GLES20.glGetAttribLocation(mProgram,"vCoord")  // 纹理坐标
        vTextures = GLES20.glGetUniformLocation(mProgram,"vTexture")  // 采样点坐标
//        vMatrix = GLES20.glGetUniformLocation(mProgram,"vMatrix")  // 摄像头转换矩阵

    }

    open fun setSize(width: Int,height: Int) {
        mWidth = width
        mHeight = height
    }

    /*fun setTransformMatrix(matrix: FloatArray) {
        mtx = matrix
    }*/

    open fun onDraw(texture: Int): Int {
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
//        GLES20.glUniformMatrix4fv(vMatrix,1,false,mtx,0)
        beforeDraw(texture)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0,4)

        return texture
    }

    open fun beforeDraw(texture: Int) {}

    fun release() {
        GLES20.glDeleteProgram(mProgram)
    }
}