// 灵魂出窍 滤镜

varying highp vec2 aCoord;
uniform sampler2D  vTexture;
uniform highp float scalePercent;   // 缩放取点
uniform lowp float mixturePercent; // 混合 透明度 变化


void main() {

    highp vec2 center = vec2(0.5,0.5);  // 中心点
    highp vec2 textureCoordinateToUse = aCoord;

    textureCoordinateToUse -= center;

    textureCoordinateToUse = textureCoordinateToUse / scalePercent;

    textureCoordinateToUse += center;  // 大于中心点 则减小取值点     小于中心点 则增大取值点

    // 原像素点 颜色
    lowp vec4 textureColor = texture2D(vTexture,aCoord);

    lowp vec4 textureColor2 = texture2D(vTexture,textureCoordinateToUse);  // 新得像素点 颜色

    gl_FragColor = mix(textureColor,textureColor2,mixturePercent);  // 线性混合
}
