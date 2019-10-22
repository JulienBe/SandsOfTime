//A uniform stays the same for every single vertex.  Attributes on the other hand can vary from vertex to vertex.
//A varying value on the other hand can be thought of as the return value, these values will be passed on down the rendering pipeline
//As you can see from the use of gl_Position, OpenGL also has some built in values.  For vertex shaders there are gl_Position and gl_PointSize.
//Think of these as uniform variables provided by OpenGL itself.  gl_Position is ultimately the position of your vertex in the world.

attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
attribute vec2 a_light;

// given by spritebatch, projection matrix
uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoords;

void main() {
    v_color = a_color;
    v_texCoords = a_texCoord0;

    vec2 relativePosition = ((a_position.xy - vec2(100.0, 100.0)) / 200.0);
    // So I get a vec2, it describes how far it is from 100 100. It's between -1.0 and 1.0 in each direction
    float len = length(relativePosition);
    // then I get the distance, between 0 and sqrt(2) since it's not norm
    // So now I want something like : below 0.33 > +3 light, below 0.66 +2 and below 1 +1
    len = 1.4142135 - len;
    // now 0 means on the point, 1,4142135 means very far
    len = round(len * 3.0) / 3.0;
    // now I get values that are discrete: 0.0, 0.333, 0.666, 1.0, 1.333
    v_color.rgb = v_color.rgb * (vec3(1.5, 1.5, 1.5) * len);

    gl_Position = u_projTrans * a_position;
}