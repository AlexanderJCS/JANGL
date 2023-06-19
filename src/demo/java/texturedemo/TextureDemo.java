package texturedemo;

import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.graphics.Texture;
import jangl.graphics.shaders.AttribLocation;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.TextureShaderFrag;
import jangl.graphics.shaders.premade.TextureShaderVert;
import jangl.io.Window;
import jangl.shapes.Rect;

import java.util.ArrayList;
import java.util.List;

public class TextureDemo implements AutoCloseable {
    private final Rect rect;
    private final Texture texture;
    private final ShaderProgram shaderProgram;

    public TextureDemo() {
        this.rect = new Rect(new NDCoords(-0.5f, 0.5f), 1, 1);
        this.texture = new Texture("src/demo/demoResources/playerDemo/player.png");

        List<AttribLocation> attribLocations = new ArrayList<>();
        attribLocations.add(new AttribLocation(0, "vertices"));
        attribLocations.add(new AttribLocation(1, "textures"));

        this.shaderProgram = new ShaderProgram(new TextureShaderVert(), new TextureShaderFrag(), attribLocations);
    }

    public void draw() {
        Window.clear();

        this.shaderProgram.bind();
        this.texture.bind();
        this.rect.draw();
        Texture.unbind();
        ShaderProgram.unbind();
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();

            JANGL.update();
        }
    }

    @Override
    public void close() {
        this.rect.close();
        this.texture.close();
        this.shaderProgram.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);

        TextureDemo textureDemo = new TextureDemo();
        textureDemo.run();
        textureDemo.close();

        Window.close();
    }
}
