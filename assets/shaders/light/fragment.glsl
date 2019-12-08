#version 120

#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

const vec2 resolution = vec2(256.0, 256.0);

uniform sampler2D u_texture;
uniform vec2 u_light_pos[400];
uniform vec3 u_light_color[400];
uniform float u_light_intensity[400];
uniform int u_light_count;

void main() {
    float total_r = 0.0;
    float total_g = 0.0;
    float total_b = 0.0;
    float total_light = 0.0;
    for (int i = 0; i < u_light_count; i++) {
        vec2 pos = (gl_FragCoord.xy - u_light_pos[i]) / resolution.xy;
        float len = min(length(pos), 1.0);
        len *= 1 / u_light_intensity[i];
        float level = 1.0 - ((step(0.45f, len) + step(0.15f, len) + step(0.05f, len)) / 3.0);
        total_light += level;
        total_r += level * u_light_color[i].r;
        total_g += level * u_light_color[i].g;
        total_b += level * u_light_color[i].b;
    }
    gl_FragColor = vec4(total_r, total_g, total_b, sqrt(total_light));
}