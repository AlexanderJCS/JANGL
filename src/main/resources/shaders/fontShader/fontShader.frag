/*
 * This file is designed for the Font class, and will allow text to be set to any color.
 */

#version 410

uniform sampler2D texSampler;
uniform vec4 color;
uniform bool keepDefaultColors;
in vec2 texCoords;
out vec4 fragColor;

void main() {
    vec4 originalColor = texture(texSampler, texCoords);

    if (!keepDefaultColors) {
        originalColor = vec4(color.x, color.y, color.z, originalColor.w * color.w);
    }
    
    fragColor = originalColor;
}
