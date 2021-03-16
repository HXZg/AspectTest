precision mediump float;
varying  vec2 aCoord;
uniform sampler2D vTexture;

void main() {
    float y = aCoord.y;
    float a = 1.0/3.0;

    if (y < a) {  // 分三屏   只绘制中间部分
        y +=a;  // 第一屏
    } else if (y > 2.0*a) {
        y -= 1.0/3.0;  // 第三屏
    }

    gl_FragColor = texture2D(vTexture,vec2(aCoord.x,y));
}
