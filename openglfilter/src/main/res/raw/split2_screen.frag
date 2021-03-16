precision mediump float;  // float 为中精度
varying vec2 aCoord;
uniform sampler2D vTexture;

void main() {

    float y = aCoord.y;

    if (y < 0.5) {
        y += 0.25;  // 分两屏  只绘制中间部分  0.25-0.75
    } else {
        y -= 0.25;
    }

    gl_FragColor = texture2D(vTexture,vec2(aCoord.x,y));  // 采样新得点  绘制
}
