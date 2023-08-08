#version 410

in vec3 vertices;
in vec2 textures;

out vec2 texCoords;

void main() {
    texCoords = textures;

    gl_Position = vec4(vertices, 1.0);
}
