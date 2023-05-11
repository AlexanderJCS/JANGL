#version 400 core

// Use the GLSL programming language to program shaders

in vec3 position;

void main(void) {
    /*
    * The position inputted is a vec3 (x, y, z). The output position needs to be a vec4.
    * "Cast" the vec3 to a vec4 by setting the 4th value to a 1
    * Then define gl_Position as that vec4 that you created
    */

    gl_Position = vec4(position, 1);
}
