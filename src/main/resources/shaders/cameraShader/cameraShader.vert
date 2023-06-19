#version 460
#include "janglCamera.glsl"

in vec3 position;

void main() {
    gl_Position = applyCamera(vec4(position, 1.0));
}