package sounddemo;

import jangl.JANGL;
import jangl.coords.NDCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.parser.Font;
import jangl.io.Window;
import jangl.io.keyboard.KeyEvent;
import jangl.io.keyboard.Keyboard;
import jangl.sound.Sound;

import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundDemo {
    private final Sound sound;
    private final Text instructions;
    private char lastInput;

    public SoundDemo() {
        this.sound = new Sound("src/demo/demoResources/soundDemo/cMajScale.ogg");
        this.sound.setLooping(true);

        this.instructions = new Text(
                new NDCoords(-0.9f, 0.9f),
                new Font("src/demo/demoResources/font/arial.fnt", "src/demo/demoResources/font/arial.png"),
                0.2f,
                "Controls:\nSpace: Play\nP: Pause\nS: Stop\nR: Rewind"
        );
    }

    public void draw() {
        this.instructions.draw();
    }

    private void handleInput(char key) {
        key = Character.toUpperCase(key);

        // Do not repeat the same function multiple times
        if (key == this.lastInput) {
            return;
        }

        if (key == ' ') {
            this.sound.play();
        } else if (key == 'P') {
            this.sound.pause();
        } else if (key == 'S') {
            this.sound.stop();
        } else if (key == 'R') {
            this.sound.rewind();
        }

        this.lastInput = key;
    }

    public void update() {
        for (KeyEvent event : Keyboard.getEvents()) {
            this.handleInput(event.key);
        }
    }

    public void run() {
        while (Window.shouldRun()) {
            this.draw();
            this.update();

            JANGL.update();
        }

        this.sound.close();
    }

    public static void main(String[] args) {
        JANGL.init(1600, 900);

        SoundDemo sd = new SoundDemo();
        sd.run();

        Window.close();
    }
}
