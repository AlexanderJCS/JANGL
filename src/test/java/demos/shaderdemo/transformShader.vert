#version 410

in vec3 position;
uniform vec2 offset;

void main() {
    gl_Position = vec4(position.x + offset.x, position.y + offset.y, position.z, 1.0);
}