#version 120
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord;
attribute vec2 a_light;

uniform mat4 u_projTrans;

void main() {
    gl_Position = u_projTrans * a_position;
}