package shaderdemo;

import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.io.Window;
import jangl.graphics.shaders.ColorShader;
import jangl.graphics.shaders.Shader;
import jangl.shapes.Circle;
import jangl.shapes.Rect;

import static org.lwjgl.opengl.GL20.glUniform4f;

public class ShaderDemo {
    public ShaderDemo() {
        JANGL.init(1600, 900);
        Window.setVsync(true);
    }

    public void run() {
        try (
                // Create the shader by passing in the filepaths of the vertex and fragment shader
                // This needs to be inside a try-with-resources statement to prevent memory leaks
                Shader shader = new Shader(
                        "src/demo/java/shaderdemo/colorShader.vert",
                        "src/demo/java/shaderdemo/colorShader.frag"
                );

                // You can also use color shaders to do this automatically
                ColorShader colorShader = new ColorShader(1, 0.8f, 0, 1);

                // Create a rectangle. To see more on shapes, and how to make a program, see ShapeDemo.
                Rect rect = new Rect(new NDCoords(0.25f, 0f), 0.5f, 0.5f);
                Circle circle = new Circle(new NDCoords(0f, -0.5f), 0.25f, 50)
            ) {

            while (Window.shouldRun()) {
                JANGL.update();
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
                Shader.unbind();

                // You can also use color shaders to do this automatically
                circle.draw(colorShader);
            }
        }

        Window.close();
    }

    public static void main(String[] args) {
        new ShaderDemo().run();
    }
}
