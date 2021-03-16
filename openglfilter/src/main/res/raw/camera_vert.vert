attribute vec4 vPosition;

attribute vec4 vCoord;

uniform mat4 vMatrix;

// 传递给片元着色器
varying vec2 aCoord;

void main() {
    gl_Position=vPosition;
    aCoord= (vMatrix * vCoord).xy;
}