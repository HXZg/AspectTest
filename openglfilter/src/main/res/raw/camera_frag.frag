#extension GL_OES_EGL_image_external : require
//必须 写的 固定的  意思   用采样器
//所有float类型数据的精度是lowp
varying vec2 aCoord;
//采样器  uniform static
uniform samplerExternalOES vTexture;
void main(){
    //Opengl 自带函数
    vec4 rgba = texture2D(vTexture,vec2(aCoord.x,aCoord.y));

//    float color = (rgba.r + rgba.g + rgba.b) / 3.0f;  灰色滤镜
//    vec4 tempColor = vec4(color,color,color,1);

//    gl_FragColor= rgba + vec4(0.1,0.1,0,0);  // 黄色
//    gl_FragColor= rgba + vec4(0,0,0.2,0); // 蓝色

    gl_FragColor = rgba;
}