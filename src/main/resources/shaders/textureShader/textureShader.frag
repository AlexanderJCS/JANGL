#version 120

uniform sampler2D texSampler;
varying vec2 tex_coords;

void main() {
    gl_FragColor = texture2D(texSampler, tex_coords);
}