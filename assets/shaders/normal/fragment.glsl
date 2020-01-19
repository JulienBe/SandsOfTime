#version 120

#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float u_angle;


void main() {
    vec4 color = texture2D(u_texture, v_texCoords);

    float cos_angle = cos(u_angle);
    float sin_angle = sin(u_angle);
    mat3 rotation = mat3(
        cos_angle, -sin_angle, 0.0,
        sin_angle, cos_angle, 0.0,
        0.0, 0.0, 1.0
    );

    color.rgb = normalize((color.rgb * 2.0 - 1.0) * rotation);
    gl_FragColor = vec4((color.rgb + 1.0) / 2.0, color.a);
}