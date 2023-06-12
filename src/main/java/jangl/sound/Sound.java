package jangl.sound;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.openal.AL11.*;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Sound implements AutoCloseable {
    private final int soundBuffer;
    private final int soundSource;
    private static boolean initialized = false;

    public static void init() {
        if (initialized) {
            return;
        }

        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        long device = alcOpenDevice(defaultDeviceName);
        ALCCapabilities deviceCapabilities = ALC.createCapabilities(device);
        long context = alcCreateContext(device, (IntBuffer) null);

        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCapabilities);

        initialized = true;
    }

    /**
     * @param soundFile The sound file, in the .wav format, to load.
     * @throws UnsupportedAudioFileException Throws if the file format is not .wav
     * @throws IllegalStateException Throws if Sound.init() has not been called. Since JANGL.init() initializes sound under the hood, you usually should not encounter this issue.
     */
    public Sound(File soundFile) throws UnsupportedAudioFileException, IllegalStateException {
        if (!initialized) {
            throw new IllegalStateException("Sound.init() must be called before creating a sound object.");
        }

        this.soundSource = alGenSources();

        this.soundBuffer = this.loadSound(soundFile);
        alSourcei(soundSource, AL_BUFFER, this.soundBuffer);
    }

    private int determineFormat(int channels, int sampleSizeBits) {
        if (channels == 1) {
            if (sampleSizeBits == 8) {
                return AL_FORMAT_MONO8;
            } else {
                return AL_FORMAT_MONO16;
            }
        } else {
            if (sampleSizeBits == 8) {
                return AL_FORMAT_STEREO8;
            } else {
                return AL_FORMAT_STEREO16;
            }
        }
    }

    /**
     * @param soundFile The sound file to load
     * @return The sound buffer ID
     * @throws UncheckedIOException If the soundFile could not be found
     * @throws UnsupportedAudioFileException If the audio file is unsupported
     */
    private int loadSound(File soundFile) throws UncheckedIOException, UnsupportedAudioFileException {
        int soundBuffer = alGenBuffers();

        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile)) {
            int bufferSize = audioStream.available();

            ByteBuffer audioData = BufferUtils.createByteBuffer(bufferSize);
            byte[] tempBuffer = new byte[bufferSize];

            // Read all the data from audioStream into tempBuffer
            while (audioStream.read(tempBuffer) != -1);

            audioData.put(tempBuffer);
            audioData.flip();

            // Get the format
            AudioFormat audioFormat = audioStream.getFormat();
            int format = this.determineFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());

            // Put the data into the sound buffer
            alBufferData(soundBuffer, format, audioData, (int) audioFormat.getSampleRate());

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new UnsupportedAudioFileException("Could not load file " + soundFile.getName() + " since its file format is not .wav");
        }

        return soundBuffer;
    }

    /**
     * Plays the sound, with a few conditions:<br>
     * 1. If the sound is currently playing, start the sound from the beginning<br>
     * 2. If the sound is rewound or stopped, start the sound from the beginning<br>
     * 3. If the sound is paused, start the sound where it left off<br>
     */
    public void play() {
        alSourcePlay(this.soundSource);
    }

    /**
     * Pauses the sound.
     */
    public void pause() {
        alSourcePause(this.soundSource);
    }

    /**
     * Stops the sound.
     */
    public void stop() {
        alSourceStop(this.soundSource);
    }

    /**
     * Stops the sound and sets its state to the initial state.
     */
    public void rewind() {
        alSourceRewind(this.soundSource);
    }

    /**
     * @param volume The volume of the sound, where 0 = 0% volume and 1 = 100% volume.
     * @throws IllegalArgumentException Throws if the given volume is outside the range of [0, 1].
     */
    public void setVolume(float volume) throws IllegalArgumentException {
        if (volume > 1 || volume < 0) {
            throw new IllegalArgumentException("The volume of a sound must be a float within the range [0, 1]");
        }

        alSourcef(this.soundSource, AL_GAIN, volume);
    }

    @Override
    public void close() {
        alDeleteSources(this.soundSource);
        alDeleteBuffers(this.soundBuffer);
    }
}
