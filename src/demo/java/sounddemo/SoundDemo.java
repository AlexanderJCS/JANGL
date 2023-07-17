package sounddemo;

import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.parser.Font;
import jangl.io.Window;
import jangl.io.keyboard.KeyEvent;
import jangl.io.keyboard.Keyboard;
import jangl.sound.Sound;
import jangl.sound.SoundState;

public class SoundDemo {
    private final Sound sound;
    private final Text instructions;

    public SoundDemo() {
        this.sound = new Sound("src/demo/demoResources/soundDemo/cMajScale.ogg");
        this.sound.setLooping(true);

        this.instructions = new Text(
                new WorldCoords(-0.9f, 0.9f),
                new Font("src/demo/demoResources/font/arial.fnt", "src/demo/demoResources/font/arial.png"),
                0.2f,
                "Controls:\nSpace: Play\nP: Pause\nS: Stop\nR: Rewind"
        );

        System.out.println(new WorldCoords(0.5f, 0.5f).toPixelCoords().toWorldCoords());
    }

    public void draw() {
        Window.clear();
        this.instructions.draw();
    }

    private void handleInput(char key) {
        key = Character.toUpperCase(key);
        SoundState state = sound.getState();

        if (key == ' ' && state != SoundState.PLAYING) {
            this.sound.play();
        } else if (key == 'P' && state != SoundState.PAUSED) {
            this.sound.pause();
        } else if (key == 'S' && state != SoundState.STOPPED) {
            this.sound.stop();
        } else if (key == 'R' && state != SoundState.INITIAL) {
            this.sound.rewind();
        }
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
        Window.setVsync(true);

        SoundDemo sd = new SoundDemo();
        sd.run();

        Window.close();
    }
}
