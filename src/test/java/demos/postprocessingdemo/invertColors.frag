#version 410

uniform sampler2D texSampler;
in vec2 texCoords;
out vec4 fragColor;

void main() {
    vec4 color = texture(texSampler, texCoords);
    vec4 inverted = vec4(vec3(1) - color.xyz, color.w);

    fragColor = inverted;
}
