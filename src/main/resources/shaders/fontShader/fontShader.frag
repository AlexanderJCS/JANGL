/*
 * This file is designed for the Font class, and will allow text to be set to any color.
 */

#version 460

uniform sampler2D texSampler;
uniform vec4 color;
in vec2 tex_coords;
out vec4 fragColor;

void main() {
    fragColor = vec4(color.x, color.y, color.z, texture(texSampler, tex_coords).w * color.w);
}
