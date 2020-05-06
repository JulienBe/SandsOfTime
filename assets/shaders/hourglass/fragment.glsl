#version 120

#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

const vec2 resolution = vec2(320.0, 206.0);
const float ratio = 320.0 / 200.0;
const float level_diff = (sqrt((resolution.x * resolution.x) + (resolution.y * resolution.y))) / 4.0;
const float[12] i_mods = float[](
    1.4, 1.1, 0.8,
    1.62, 1.12, 0.72,
    1.84, 1.14, 0.64,
    1.06, 1.16, 0.56
);
const float[18] dither_pattern = float[](
    0.013, 0.2, +0.075,
    0.023, 0.1, +0.175,
    0.033, 0.0, +0.15,
    0.1, 0.1, +0.1,
    0.02, 0.2, +0.02,
    0.05, 0.0, +0.05);
uniform float u_centers_x[120];
uniform float u_centers_y[120];
uniform float u_transition_time;
uniform int u_phase;
uniform float u_time;

void main() {
    vec2 pixel_pos = gl_FragCoord.xy;
    vec2 delta = vec2(228.0, 128.0) - pixel_pos;
    float level = 0.0;
    for (int i = 0; i <= 120 * u_phase; i++) {
        float l = length(vec2(u_centers_x[i], u_centers_y[i]) - pixel_pos) * u_transition_time * i_mods[int(i / 12)];
        float floor = min(l, 127.0);
        // discard l that are too high
        l *= step(l, 127.0);
        // make it wavy
        float mod = mod(l, 64.0);
        l = min(l, 64.0);
        l -= mod;
        level += (l + floor);
    }
    level /= level_diff;
    level /= 120;
    level = 1.0 - level;
    level *= u_phase;
    gl_FragColor = vec4(0.0, level, level, clamp(level / 2.0, 0.0, 0.2));
}