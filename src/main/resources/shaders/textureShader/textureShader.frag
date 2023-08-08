#version 410

uniform sampler2D texSampler;
in vec2 texCoords;
out vec4 fragColor;

void main() {
    fragColor = texture(texSampler, texCoords);
}
