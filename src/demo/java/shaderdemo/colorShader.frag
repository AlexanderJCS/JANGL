#version 120

// This vec4 will be inputted to the shader via our Java program
// The vec4 is: (red, green, blue, alpha).
uniform vec4 color;

void main(void) {
    // Set the color of the pixel equal to the color passed into the shader
    gl_FragColor = color;
}
