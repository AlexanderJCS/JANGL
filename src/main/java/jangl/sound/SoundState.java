package jangl.sound;


import org.lwjgl.openal.AL11;

/**
 * These are the OpenAL source states, but more easily accessible for use with JANGL.
 */
public enum SoundState {
    INITIAL, PAUSED, PLAYING, STOPPED;

    /**
     * Convert an OpenAL source state to a SoundState enum.
     * @return The SoundState from the OpenAL source state
     */
    public static SoundState ALStateToSoundState(int sourceState) {
        SoundState returnValue = null;

        switch (sourceState) {
            case AL11.AL_INITIAL -> returnValue = SoundState.INITIAL;
            case AL11.AL_PAUSED -> returnValue = SoundState.PAUSED;
            case AL11.AL_PLAYING -> returnValue = SoundState.PLAYING;
            case AL11.AL_STOPPED -> returnValue = SoundState.STOPPED;
        }

        return returnValue;
    }
}
