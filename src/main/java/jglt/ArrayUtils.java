package jglt;

public class ArrayUtils {
    public static float getMax(float[] arr) {
        float max = arr[0];

        for (float value : arr) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    public static float getMin(float[] arr) {
        float min = arr[0];

        for (float value : arr) {
            if (value < min) {
                min = value;
            }
        }

        return min;
    }
}
