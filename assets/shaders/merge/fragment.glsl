#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_lights;

void main() {
    vec4 light = texture2D(u_lights, v_texCoords);
    vec4 game = texture2D(u_texture, v_texCoords);
    vec4 color = vec4(((0.75 + light.rgb) * game.rgb), min(light.a, game.a));
    gl_FragColor = v_color * light * game;
}