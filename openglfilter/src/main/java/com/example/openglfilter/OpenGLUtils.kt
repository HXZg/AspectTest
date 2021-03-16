package com.example.openglfilter

import android.content.Context
import android.opengl.GLES20
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

object OpenGLUtils {

    fun readRawTxtFile(context: Context,rawId: Int) : String {
        val inputStream = context.resources.openRawResource(rawId)
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line : String? = bufferReader.readLine()
        val sb = StringBuilder()
        while (line != null) {
            sb.append(line)
            sb.append("\n")
            line = bufferReader.readLine()
        }
        bufferReader.close()
        return sb.toString()
    }

    fun loadProgram(vSource: String,fSource: String) : Int{
        // 顶点着色器
        val vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER)
        GLES20.glShaderSource(vertexShader,vSource)
        GLES20.glCompileShader(vertexShader)

        val status = intArrayOf(0)
        GLES20.glGetShaderiv(vertexShader,GLES20.GL_COMPILE_STATUS,status,0)  // 检查是否加载成功
        if (status[0] != GLES20.GL_TRUE) {
            throw IllegalStateException("load vertex shader ${GLES20.glGetShaderInfoLog(vertexShader)}")
        }

        // 片元着色器
        val fragShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER)
        GLES20.glShaderSource(fragShader,fSource)
        GLES20.glCompileShader(fragShader)

        GLES20.glGetShaderiv(fragShader,GLES20.GL_COMPILE_STATUS,status,0)  // 检查是否加载成功
        if (status[0] != GLES20.GL_TRUE) {
            throw IllegalStateException("load fragment shader ${GLES20.glGetShaderInfoLog(fragShader)}")
        }

        // 创建着色器程序
        val program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program,vertexShader)
        GLES20.glAttachShader(program,fragShader)
        GLES20.glLinkProgram(program)

        GLES20.glGetShaderiv(program,GLES20.GL_LINK_STATUS,status,0)  // 检查是否加载成功
        if (status[0] != GLES20.GL_TRUE) {
            throw IllegalStateException("link program ${GLES20.glGetProgramInfoLog(program)}")
        }

        GLES20.glDeleteShader(vertexShader)
        GLES20.glDeleteShader(fragShader)
        return program
    }
}