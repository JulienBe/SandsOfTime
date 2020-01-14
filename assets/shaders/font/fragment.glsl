#version 120

#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

const vec2 resolution = vec2(256.0, 256.0);

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_palette;
uniform sampler2D u_background_texture;

void main() {
    vec3 texture = texture2D(u_texture, v_texCoords).rgb;
    vec3 original = texture2D(u_background_texture, gl_FragCoord.xy / resolution).rgb;
    vec3 new_color = 1.0 - original;
    new_color = clamp(new_color, 0.1, 0.9);
    float luminosity = (new_color.r + new_color.g + new_color.b) / 3.0;
    new_color = vec3((luminosity + (1.0 - original)) / 2.0);

    new_color = (original * (1.0 - luminosity)) / 2.0;
    new_color *= 3.0;
    new_color += texture;
    new_color /= 4.0;

    gl_FragColor = vec4(clamp(new_color, 0.1, 0.9), 1.0);
    //gl_FragColor = vec4(texture, 1.0);
}