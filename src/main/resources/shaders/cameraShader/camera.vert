#version 460

in vec3 position;

layout(binding = 10) uniform CameraPos {
    vec2 cameraPos;
};

void main() {
    gl_Position = vec4(position.x - cameraPos.x, position.y - cameraPos.y, 0, 1);
}