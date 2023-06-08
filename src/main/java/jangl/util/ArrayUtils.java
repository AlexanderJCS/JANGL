package jangl.util;

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

    /**
     * Used for collision.
     *
     * @param arr    The array to get the even or odd indices of
     * @param offset 0 to get even indices, 1 to get odd indices
     * @return a float arr of even or odd indices
     */
    private static float[] getEvenOrOdd(float[] arr, int offset) {
        float[] returnArr = new float[arr.length / 2];

        for (int i = 0; i < returnArr.length; i++) {
            returnArr[i] = arr[i * 2 + offset];
        }

        return returnArr;
    }

    /**
     * Get the odd indices of an array. WARNING: may not work with odd array lengths (it might, I haven't tested it)
     *
     * @param arr The array to get the even indices of.
     * @return The values of the even indices of the array.
     */
    public static float[] getEven(float[] arr) {
        return getEvenOrOdd(arr, 0);
    }

    /**
     * Get the even indices of an array.
     *
     * @param arr The array to get the even indices of.
     * @return The values of the even indices of the array.
     */
    public static float[] getOdd(float[] arr) {
        return getEvenOrOdd(arr, 1);
    }

    public static int[] repeatSequence(int[] sequence, int times) {
        int[] repeated = new int[sequence.length * times];

        for (int i = 0; i < times; i++) {
            System.arraycopy(sequence, 0, repeated, i * times, sequence.length);
        }

        return repeated;
    }
}
