#version 460

uniform sampler2D texSampler;
in vec2 tex_coords;
out vec4 fragColor;

void main() {
    fragColor = texture(texSampler, tex_coords);
}
