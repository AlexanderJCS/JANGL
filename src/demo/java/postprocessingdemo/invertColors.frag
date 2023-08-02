#version 460

uniform sampler2D texSampler;
in vec2 tex_coords;
out vec4 fragColor;

void main() {
    vec4 color = texture(texSampler, tex_coords);
    vec4 inverted = vec4(vec3(1) - color.xyz, color.w);

    fragColor = inverted;
}
