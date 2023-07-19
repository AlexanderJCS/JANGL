/*
 * I used this resource when programming the Sound class:
 * https://www.youtube.com/watch?v=dLrqBTeipwg&ab_channel=GamesWithGabe
 */

package jangl.sound;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;

public class Sound implements AutoCloseable {
    private static boolean initialized = false;
    private final int bufferID;
    private final int sourceID;

    /**
     * @param soundFilepath The sound file, in the .wav format, to load.
     * @throws IllegalStateException Throws if Sound.init() has not been called. Since JANGL.init() initializes sound
     *                               under the hood, you usually do not encounter this issue.
     */
    public Sound(String soundFilepath) throws IllegalStateException {
        if (!initialized) {
            throw new IllegalStateException("Sound.init() must be called before creating a sound object.");
        }

        this.sourceID = alGenSources();

        this.bufferID = this.loadSound(soundFilepath);
        alSourcei(sourceID, AL_BUFFER, this.bufferID);
    }

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

    private int determineFormat(int channels) {
        if (channels == 1) {
            return AL_FORMAT_MONO16;
        } else {
            return AL_FORMAT_STEREO16;
        }
    }

    /**
     * @param soundFilepath The sound file to load
     * @return The sound buffer ID
     * @throws UncheckedIOException If the soundFile could not be found
     */
    private int loadSound(String soundFilepath) throws UncheckedIOException {
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer sampleRateBuffer = BufferUtils.createIntBuffer(1);

        ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(soundFilepath, channelsBuffer, sampleRateBuffer);

        if (rawAudioBuffer == null) {
            throw new UncheckedIOException(new IOException(
                    "Could not load from file: " + soundFilepath + ". Make sure that:\n"
                            + "1. The file exists.\n2. It has a .ogg file format. JANGL does not support other file formats."
            ));
        }

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        int format = this.determineFormat(channels);

        int bufferID = alGenBuffers();
        alBufferData(bufferID, format, rawAudioBuffer, sampleRate);

        return bufferID;
    }

    /**
     * Plays the sound.
     */
    public void play() {
        alSourcePlay(this.sourceID);
    }

    /**
     * Pauses the sound.
     */
    public void pause() {
        alSourcePause(this.sourceID);
    }

    /**
     * Stops the sound.
     */
    public void stop() {
        alSourceStop(this.sourceID);
    }

    /**
     * Stops the sound and sets its state to the initial state.
     */
    public void rewind() {
        alSourceRewind(this.sourceID);
    }

    /**
     * @param volume The volume of the sound, where 0 = 0% volume and 1 = 100% volume.
     * @throws IllegalArgumentException Throws if the given volume is outside the range of [0, 1].
     */
    public void setVolume(float volume) throws IllegalArgumentException {
        if (volume > 1 || volume < 0) {
            throw new IllegalArgumentException("The volume of a sound must be a float within the range [0, 1]");
        }

        alSourcef(this.sourceID, AL_GAIN, volume);
    }

    /**
     * Set the audio to loop or not loop. Looping is off by default.
     *
     * @param loop true to make the audio loop, false to make the audio not loop.
     */
    public void setLooping(boolean loop) {
        if (loop) {
            alSourcei(this.sourceID, AL_LOOPING, AL_TRUE);
        } else {
            alSourcei(this.sourceID, AL_LOOPING, AL_FALSE);
        }
    }

    public SoundState getState() {
        return SoundState.ALStateToSoundState(alGetSourcei(this.sourceID, AL_SOURCE_STATE));
    }

    @Override
    public void close() {
        alDeleteSources(this.sourceID);
        alDeleteBuffers(this.bufferID);
    }
}
