package jangl.sound;

import jangl.resourcemanager.Resource;
import jangl.resourcemanager.ResourceManager;
import jangl.resourcemanager.ResourceQueuer;
import jangl.resourcemanager.ResourceType;
import org.lwjgl.openal.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.openal.AL11.*;
import static org.lwjgl.openal.ALC11.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;


public class Sound implements AutoCloseable {
    private static boolean initialized = false;
    private final int bufferID;
    private final int sourceID;
    private final AtomicBoolean closed;

    /**
     * @param soundFilepath The sound file, in the .wav format, to load.
     * @throws IllegalStateException Throws if Sound.init() has not been called. Since Jangl.init() initializes sound
     *                               under the hood, you usually do not encounter this issue.
     */
    public Sound(String soundFilepath) throws IllegalStateException {
        if (!initialized) {
            throw new IllegalStateException("Sound.init() must be called before creating a sound object.");
        }

        this.sourceID = alGenSources();

        this.bufferID = this.loadSound(soundFilepath);
        alSourcei(sourceID, AL_BUFFER, this.bufferID);

        this.closed = new AtomicBoolean(false);
        ResourceManager.add(this, new ResourceQueuer(this.closed, new Resource(this.bufferID, ResourceType.AL_BUFFER)));
        ResourceManager.add(this, new ResourceQueuer(this.closed, new Resource(this.sourceID, ResourceType.AL_SOURCE)));
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
        try (MemoryStack stack = MemoryStack.stackPush()){
            IntBuffer channelsBuffer = stack.mallocInt(1);
            IntBuffer sampleRateBuffer = stack.mallocInt(1);

            ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename(soundFilepath, channelsBuffer, sampleRateBuffer);

            if (rawAudioBuffer == null) {
                throw new UncheckedIOException(new IOException(
                        "Could not load from file: " + soundFilepath + ". Make sure that:\n"
                                + "1. The file exists.\n2. It has a .ogg file format. Jangl does not support other file formats."
                ));
            }

            int channels = channelsBuffer.get();
            int sampleRate = sampleRateBuffer.get();

            int format = this.determineFormat(channels);

            int bufferID = alGenBuffers();
            alBufferData(bufferID, format, rawAudioBuffer, sampleRate);

            MemoryUtil.memFree(rawAudioBuffer);

            return bufferID;
        }
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
        if (volume > this.getMaxVolume() || volume < 0) {
            throw new IllegalArgumentException(
                    "The volume of a sound must be a float within the range [0, this.getMaxVolume()]. If you want to " +
                            "go above 100% volume, set this.setMaxVolume() to your maximum volume, then use this.setVolume()" +
                            " to set the volume to the desired value."
            );
        }

        alSourcef(this.sourceID, AL_GAIN, volume);
    }

    /**
     * Get the volume of the sound, where 0 = 0% volume and 1 = 100% volume.
     * @return The volume of the sound.
     */
    public float getVolume() {
        return alGetSourcef(this.sourceID, AL_GAIN);
    }

    /**
     * Set the pitch of the sound, which changes the speed as a side-effect. The default pitch is 1.0f.
     * For example, a pitch of 1.2f would make the sound 20% higher pitch, and 20% faster.
     * @param pitchShift The pitch shift.
     */
    public void setPitchAndSpeed(float pitchShift) {
        alSourcef(this.sourceID, AL_PITCH, pitchShift);
    }

    /**
     * Get the pitch of the sound.
     * @return The pitch of the sound.
     */
    public float getPitchAndSpeed() {
        return alGetSourcef(this.sourceID, AL_PITCH);
    }

    /**
     * Set the max volume. Used to set the max volume beyond 100%.<br>
     * WARNING: 0 = 0% volume and 1 = 100% volume. Using this.setMaxVolume() to set the volume
     *          higher than 100% may make the sound too loud and (in extreme cases) cause hearing damage.
     *
     * @param volume The new maximum volume.
     */
    public void setMaxVolume(float volume) throws IllegalArgumentException {
        if (volume < 0) {
            throw new IllegalArgumentException("The max volume of a sound must be greater than or equal to 0");
        }

        alSourcef(this.sourceID, AL_MAX_GAIN, volume);
    }

    public float getMaxVolume() {
        return alGetSourcef(this.sourceID, AL_MAX_GAIN);
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
        if (this.closed.getAndSet(true)) {
            return;
        }

        alDeleteSources(this.sourceID);
        alDeleteBuffers(this.bufferID);
    }
}
