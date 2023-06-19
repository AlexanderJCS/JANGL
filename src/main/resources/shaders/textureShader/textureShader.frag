#version 460

layout (binding = 0) uniform sampler2D texSampler;
layout (location = 0) in vec2 tex_coords;
layout (location = 0) out vec4 fragColor;

void main() {
    fragColor = texture(texSampler, tex_coords);
}
