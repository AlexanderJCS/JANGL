package jangl.util;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class ArrayUtils {
    private ArrayUtils() {

    }

    /**
     * Converts an array of x, y coordinate pairs into a vector array.
     *
     * @param arr The array of x and y pairs
     * @return The new vector array
     * @throws IndexOutOfBoundsException Throws if the array length is odd
     */
    public static Vector2f[] toVector2fArray(float[] arr) throws IndexOutOfBoundsException {
        Vector2f[] vecArr = new Vector2f[arr.length / 2];

        for (int i = 0; i < vecArr.length; i++) {
            vecArr[i] = new Vector2f(arr[i * 2], arr[i * 2 + 1]);
        }

        return vecArr;
    }

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
     * Used for collision optimization.
     *
     * @param points    The points to find the farthest point from.
     * @param distPoint The point to compare all the other points to.
     * @return The farthest point from distPoint.
     */
    public static Vector2f getFarthestPointFrom(Vector2f[] points, Vector2f distPoint) {
        Vector2f farthest = points[0];
        float farthestDistanceSquared = -1;  // this is valid since distance cannot be negative

        for (Vector2f vertex : points) {
            float distSquared = vertex.distanceSquared(distPoint);

            if (distSquared > farthestDistanceSquared) {
                farthest = vertex;
                farthestDistanceSquared = distSquared;
            }
        }

        return farthest;
    }

    /**
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
     * Returns the value of all even indices. This is only tested for arrays of even size, so an index out of bounds
     * error may occur if an odd-length array is entered.
     *
     * @return The values of all even indices
     */
    public static float[] getEvenIndices(float[] arr) {
        return getEvenOrOdd(arr, 0);
    }

    /**
     * Returns the value of all odd indices. This is only tested for arrays of even size, so an index out of bounds
     * error may occur if an odd-length array is entered.
     *
     * @return The values of all odd indices
     */
    public static float[] getOddIndices(float[] arr) {
        return getEvenOrOdd(arr, 1);
    }

    public static byte[] repeatSequence(byte[] sequence, int times) {
        byte[] repeated = new byte[sequence.length * times];

        for (int i = 0; i < times; i++) {
            System.arraycopy(sequence, 0, repeated, sequence.length * i, sequence.length);
        }

        return repeated;
    }

    /**
     * Down casts an int array to a byte array. If an int value is over 127, it will overflow to -128.
     *
     * @param ints The int array to downcast.
     * @return The byte array.
     */
    public static byte[] intsToBytes(int[] ints) {
        byte[] bytes = new byte[ints.length];

        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }

        return bytes;
    }

    /**
     * Converts a 4x4 float matrix to a float array.
     * @param matrix The matrix to convert
     * @return The float array
     */
    public static float[] matrixToArray(Matrix4f matrix) {
        float[] array = new float[16];
        matrix.get(array);

        return array;
    }
}
