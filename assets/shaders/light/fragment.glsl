#version 120

#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

const vec2 resolution = vec2(256.0, 256.0);
const float light_default_z = 0.5;
const vec3 falloff = vec3(0.2, 6.0, 20.0);

const int nb_steps = 7;
const float dither_width = 3.0;
const float lvl1 = 0.65;
const float lvl2 = lvl1 * 0.65;
const float lvl3 = lvl2 * 0.65;
const float lvl4 = lvl3 * 0.65;
const float lvl5 = lvl4 * 0.65;
const float lvl6 = lvl5 * 0.65;
const float lvl7 = lvl6 * 0.65;
const vec2[nb_steps] light_steps = vec2[](
    vec2(lvl1 - 0.05 * dither_width,   lvl1 + 0.05 * dither_width),
    vec2(lvl2 - 0.025 * dither_width,  lvl2 + 0.025 * dither_width),
    vec2(lvl3 - 0.0125 * dither_width, lvl3 + 0.0125 * dither_width),
    vec2(lvl4 - 0.0065 * dither_width, lvl4 + 0.0065 * dither_width),
    vec2(lvl5 - 0.0032 * dither_width, lvl5 + 0.0032 * dither_width),
    vec2(lvl6 - 0.0016 * dither_width, lvl6 + 0.0016 * dither_width),
    vec2(lvl7 - 0.0008 * dither_width, lvl7 + 0.0008 * dither_width)
);
const float[9] dither_pattern = float[](
    -0.1, 0.0, +0.1,
    -0.02, 0.0, +0.02,
    -0.05, 0.0, +0.05);
const float pixel_size_x = 1.0 / resolution.x;
const float pixel_size_y = 1.0 / resolution.y;

uniform sampler2D u_texture;
uniform sampler2D u_palette;
uniform sampler2D u_normal;
uniform sampler2D u_occlusion;

uniform vec4 u_light_pos_angle_tilt[600];
uniform float u_light_intensity[400];
uniform int u_light_count;

void main() {
    vec4 color_text = texture2D(u_texture, v_texCoords);
    vec3 normal = texture2D(u_normal, v_texCoords).rgb * 2.0 - 1.0;
    float total_light = 0.0;
    vec2 pixel_pos = gl_FragCoord.xy / resolution.xy;

    for (int i = 0; i <= u_light_count; i++) {
        vec2 light_pos = u_light_pos_angle_tilt[i].xy / resolution.xy;
        vec3 delta_pixel_pos = vec3(light_pos - pixel_pos, light_default_z);

        float len = length(delta_pixel_pos.xy);

        vec3 nor_delta = normalize(delta_pixel_pos);
        vec3 nor_normal = normalize(normal);

        float angle_fitness = dot(vec2(cos(u_light_pos_angle_tilt[i].z) * u_light_pos_angle_tilt[i].w, sin(u_light_pos_angle_tilt[i].z) * u_light_pos_angle_tilt[i].w), vec2(delta_pixel_pos.xy)) + 1.0;
        float normal = max(dot(nor_normal, nor_delta), 0.0);
        float attenuation = 1.0 / (falloff.x + (falloff.y * len) + (falloff.z * len * len));

        float l = u_light_intensity[i] * (1.0 - len) * attenuation * normal * angle_fitness * angle_fitness * angle_fitness;

        for (int i = 0; i <= nb_steps; i++) {
            float lower = step(light_steps[i].x, l);
            float higher = step(l, light_steps[i].y);
            float mul = lower * higher;
            l += mul * dither_pattern[int(mod(gl_FragCoord.x * gl_FragCoord.y, 9))] * 0.1;
        }

        // can be used to adjust the fact that a light could go over an obstacle. The closest you get, the less steps it does, to more likely it is to go 'over' the blocker

        //**
        float shadow_total_steps = len * 96.0;
        vec2 shadow_sample_step = delta_pixel_pos.xy / shadow_total_steps;
        float min_light = 1.0;
        for (float f = 0.0; f < shadow_total_steps; f += 1.0) {
            vec2 coord_to_sample = pixel_pos + vec2(shadow_sample_step.x * f, shadow_sample_step.y * f);
            vec4 occluder_color = texture2D(u_occlusion, coord_to_sample);
            min_light = min(min_light, 1.0 - occluder_color.r);
        }
        l *= min_light;
        //*/

        // doing this to avoid having 'invisible' interactions betweens the lights
        total_light += l;

        total_light += step(lvl1, l) + step(lvl2, l) + step(lvl3, l) + step(lvl4, l) + step(lvl5, l) + step(lvl6, l) + step(lvl7, l);
    }
    total_light /= 6.0;


    int color = int((color_text.r + color_text.g) * 255.0);
    int palette_index =
        int(0 > color) + int(72 > color) + int(135 > color) + int(163 > color) +
        int(182 > color) + int(214 > color) + int(228 > color) + int(249 > color) +
        int(253 > color) + int(255 > color) + int(374 > color) + int(389 > color) +
        int(418 > color) + int(459 > color) + int(491 > color) + int(496 > color);

    vec4 awesome_paletted_color = texture2D(u_palette, vec2(1.0 - total_light, palette_index / 15.0));
    gl_FragColor = vec4(awesome_paletted_color.rgb, 1.0);
    //gl_FragColor = texture2D(u_normal, v_texCoords);
}