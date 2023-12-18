package jangl.sound;


import org.lwjgl.openal.AL11;

/**
 * These are the OpenAL source states, but more easily accessible for use with Jangl.
 */
public enum SoundState {
    INITIAL, PAUSED, PLAYING, STOPPED;

    /**
     * Convert an OpenAL source state to a SoundState enum.
     *
     * @return The SoundState from the OpenAL source state. Null if the source state does not correspond to any SoundState.
     */
    public static SoundState ALStateToSoundState(int sourceState) {
        return switch (sourceState) {
            case AL11.AL_INITIAL -> INITIAL;
            case AL11.AL_PAUSED -> PAUSED;
            case AL11.AL_PLAYING -> PLAYING;
            case AL11.AL_STOPPED -> STOPPED;
            default -> null;
        };
    }
}
