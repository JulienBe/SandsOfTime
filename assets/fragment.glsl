#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
//    gl_FragColor = vec4(1.0, 1.0, 0.0, v_color.a) * texture2D(u_texture, v_texCoords);
}