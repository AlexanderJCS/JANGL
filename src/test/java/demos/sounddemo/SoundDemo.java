package demos.sounddemo;

import jangl.JANGL;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Text;
import jangl.graphics.font.Font;
import jangl.io.Window;
import jangl.io.keyboard.KeyEvent;
import jangl.io.keyboard.Keyboard;
import jangl.sound.Sound;
import jangl.sound.SoundState;
import jangl.time.Clock;

public class SoundDemo {
    private final Sound sound;
    private final Text instructions;

    public SoundDemo() {
        this.sound = new Sound("src/test/resources/demo/soundDemo/cMajScale.ogg");
        this.sound.setLooping(true);
        this.sound.setMaxVolume(2);
        this.sound.setVolume(2);

        this.instructions = new Text(
                new WorldCoords(0.1f, 0.9f),
                new Font("src/test/resources/demo/font/arial.fnt", "src/test/resources/demo/font/arial.png"),
                0.1f,
                "Controls:\nSpace: Play\nP: Pause\nS: Stop\nR: Rewind\nW: Pitch and speed up\nQ: Pitch and speed down"
        );
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
        } else if (key == 'W') {
            this.sound.setPitchAndSpeed(this.sound.getPitchAndSpeed() + Clock.getTimeDeltaf());
        } else if (key == 'Q') {
            this.sound.setPitchAndSpeed(this.sound.getPitchAndSpeed() - Clock.getTimeDeltaf());
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
