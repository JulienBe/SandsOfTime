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

uniform sampler2D u_texture;
uniform sampler2D u_palette;

uniform vec2 u_light_pos[400];
uniform vec3 u_light_color[400];
uniform float u_light_intensity[400];
uniform int u_light_count;

void main() {
    vec4 color_text = texture2D(u_texture, v_texCoords);
    int color = int((color_text.r + color_text.g) * 255.0);
    float total_light = 0.0;
    for (int i = 0; i < u_light_count; i++) {
        vec2 pos = (gl_FragCoord.xy - u_light_pos[i]) / resolution.xy;
        float len = min(length(pos), 1.0);
        len *= 1 / u_light_intensity[i];
        float level = step(0.79f, len) + step(0.53f, len) + step(0.354f, len) + step(0.236f, len) + step(0.157f, len) + step(0.105f, len) + step(0.07f, len);
        total_light += level;
    }
    int palette_index =
        int(1 > color) + int(73 > color) + int(136 > color) + int(164 > color) +
        int(183 > color) + int(215 > color) + int(229 > color) + int(250 > color) +
        int(254 > color) + int(256 > color) + int(375 > color) + int(390 > color) +
        int(419 > color) + int(460 > color) + int(492 > color);


    vec4 awesome_paletted_color = texture2D(u_palette, vec2(total_light / 6.0, palette_index / 15.0));
    gl_FragColor = vec4(awesome_paletted_color.rgb, 1.0);
}