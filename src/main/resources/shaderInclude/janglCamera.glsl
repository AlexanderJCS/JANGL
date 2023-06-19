layout(binding = 83) uniform CameraPos {
    vec2 cameraPos;
};

vec4 applyCamera(vec4 coords) {
    coords.x -= cameraPos.x;
    coords.y -= cameraPos.y;

    return coords;
}