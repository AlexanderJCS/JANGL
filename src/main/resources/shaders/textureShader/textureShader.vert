#version 460
#include "janglCamera.glsl"

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 textures;

layout (location = 0) out vec2 tex_coords;

void main() {
    tex_coords = textures;
    gl_Position = applyCamera(vec4(vertices, 1.0));
}
