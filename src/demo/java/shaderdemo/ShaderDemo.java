package shaderdemo;

import jglt.JGLT;
import jglt.coords.ScreenCoords;
import jglt.io.Window;
import jglt.shaders.Shader;
import jglt.shapes.Rect;
import jglt.time.Clock;

import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL20.glUniform4f;

public class ShaderDemo {
    public ShaderDemo() {
        JGLT.init(1600, 900);
    }

    public void run() {
        try (
                // Create the shader by passing in the filepaths of the vertex and fragment shader
                // This needs to be inside a try-with-resources statement to prevent memory leaks
                Shader shader = new Shader(
                        "src/demo/java/shaderdemo/colorVertexShader.glsl",
                        "src/demo/java/shaderdemo/colorFragmentShader.glsl"
                );

                // Create a rectangle. To see more on shapes, and how to make a program, see ShapeDemo.
                Rect rect = new Rect(new ScreenCoords(-0.25f, -0.25f), 0.5f, 0.5f)
            ) {

            while (Window.shouldRun()) {
                JGLT.update();
                Window.clear();

                // Bind the shader. Anything drawn when they shader is bound will be drawn with the shader.
                shader.bind();

                // Pass the vector4 color information to the variable named "color" in the shader.
                // The first float value is red, the second is blue, the third is green, and the last is alpha (transparency).
                // Try modifying the four arguments to get a different color!
                glUniform4f(shader.getUniformLocation("color"), 1, 0, 0, 1);

                // Draw the rectangle with the given shader
                rect.draw();

                // When done with the shader, make sure to unbind it
                shader.unbind();

                Clock.busyTick(60);
            }
        }

        glfwTerminate();
    }

    public static void main(String[] args) {
        new ShaderDemo().run();
    }
}
