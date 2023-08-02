/*
 * This file is designed for the Font class, and will allow text to be set to any color.
 */

#version 460

uniform sampler2D texSampler;
uniform vec4 color;
uniform bool keepDefaultColors;
in vec2 tex_coords;
out vec4 fragColor;

void main() {
    vec4 originalColor = texture(texSampler, tex_coords);

    if (!keepDefaultColors) {
        originalColor = vec4(color.x, color.y, color.z, originalColor.w * color.w);
    }
    
    fragColor = originalColor;
}
