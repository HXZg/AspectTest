package com.xz.skin;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TriangleRender implements GLSurfaceView.Renderer {


    private FloatBuffer vertexBuffer;
    private int mProgram;

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "gl_Position = vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
                    "void main() {" +
                    "gl_FragColor = vColor;" +
                    "}";

    static float triangleCoords[] = {  // 三角形 三点坐标 以中心点为原点
            0.5f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    float[] color = {1f,0f,0f,1f};

    private int loadShader(int type,String shaderCode) {
        int shader = GLES20.glCreateShader(type); // 创建着色器
        // 设置并编译着色器代码
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1f,1f,1f,1f);

        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(triangleCoords);
        vertexBuffer.position(0);

        // 顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);
        // 片元着色器
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);
        // 创建空得gl程序
        mProgram = GLES20.glCreateProgram();
        // 将着色器加入程序
        GLES20.glAttachShader(mProgram,vertexShader);
        GLES20.glAttachShader(mProgram,fragmentShader);
        GLES20.glLinkProgram(mProgram); // 链接着色器程序
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    private int mPositionHandler;
    private int mColorHandler;

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);

        mPositionHandler = GLES20.glGetAttribLocation(mProgram,"vPosition");  // 获取成员
        GLES20.glEnableVertexAttribArray(mPositionHandler); // 启用三角形顶点得句柄
        // 设置 坐标
        GLES20.glVertexAttribPointer(mPositionHandler,3,GLES20.GL_FLOAT,false,12,vertexBuffer);
        mColorHandler = GLES20.glGetUniformLocation(mProgram,"vColor");   // 获取片元着色器成员
        GLES20.glUniform4fv(mColorHandler,1,color,0); // 设置颜色
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);  // 绘制三角形
        GLES20.glDisableVertexAttribArray(mPositionHandler);  // 禁止顶点的句柄
    }
}
