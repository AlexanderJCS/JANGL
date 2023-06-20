#version 460
#include "janglCamera.glsl"

in vec3 position;
uniform vec2 offset;

void main() {
    gl_Position = applyCamera(
        vec4(position.x + offset.x, position.y + offset.y, position.z, 1.0)
    );
}