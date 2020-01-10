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
const float light_default_z = 1.0;
const vec3 falloff = vec3(0.4, 3.0, 20.0);

uniform sampler2D u_texture;
uniform sampler2D u_palette;
uniform sampler2D u_normal;

uniform vec2 u_light_pos[400];
uniform float u_light_intensity[400];
uniform int u_light_count;

void main() {
    vec4 color_text = texture2D(u_texture, v_texCoords);
    vec3 normal = texture2D(u_normal, v_texCoords).rgb * 2.0 - 1.0;
    int color = int((color_text.r + color_text.g) * 255.0);
    float total_light = 0.0;

    for (int i = 0; i < u_light_count; i++) {
        vec3 delta_pixel_pos = vec3((u_light_pos[i] - gl_FragCoord.xy) / resolution.xy, light_default_z);
        float len = length(delta_pixel_pos.xy);

        vec3 nor_delta = normalize(delta_pixel_pos);
        vec3 nor_normal = normalize(normal);

        float df = max(dot(nor_normal, nor_delta), 0.0);
        float attenuation = 1.0 / (falloff.x + (falloff.y * len) + (falloff.z * len * len));

        total_light += df * u_light_intensity[i] * (1.0 - len) * attenuation;
    }

    total_light = step(0.79, total_light) + step(0.53, total_light) + step(0.354, total_light) + step(0.236, total_light) + step(0.157, total_light) + step(0.105, total_light) + step(0.07, total_light);
    total_light /= 7.0;
    int palette_index =
        int(1 > color) + int(73 > color) + int(136 > color) + int(164 > color) +
        int(183 > color) + int(215 > color) + int(229 > color) + int(250 > color) +
        int(254 > color) + int(256 > color) + int(375 > color) + int(390 > color) +
        int(419 > color) + int(460 > color) + int(492 > color);

    vec4 awesome_paletted_color = texture2D(u_palette, vec2(1.0 - total_light, palette_index / 15.0));
    gl_FragColor = vec4(awesome_paletted_color.rgb, 1.0);
}