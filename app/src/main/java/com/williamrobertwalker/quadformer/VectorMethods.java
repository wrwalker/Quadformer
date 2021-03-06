package com.williamrobertwalker.quadformer;

import android.graphics.PointF;

/**
 * Created by TheWo_000 on 3/7/2016.
 */
public class VectorMethods {


    /**
     * Performs dot product operation on 2 vectors
     * @param vector1 First vector
     * @param vector2 Second vector
     * @return The dot product value of the two vectors
     */
    public static float dot(PointF vector1, PointF vector2)
    {
        return (vector1.x * vector2.x) + (vector1.y * vector2.y);
    }

    /**
     * Subtract vectors
     * @param temporaryVector To reuse allocated memory.
     * @param vector1 The vector to be subtracted by the other.
     * @param vector2 The vector to subtract the first by.
     * @return The difference.
     */
    public static PointF sub(PointF temporaryVector, PointF vector1, PointF vector2)
    {
        temporaryVector.x = vector1.x - vector2.x;
        temporaryVector.y = vector1.y - vector2.y;
        return temporaryVector;
    }

    /**
     * Add vectors
     * @param temporaryVector To reuse allocated memory.
     * @param vector1 The vector to be added by the second.
     * @param vector2 The vector to add to the first.
     * @return The sum.
     */
    public static PointF add(PointF temporaryVector, PointF vector1, PointF vector2)
    {
        temporaryVector.x = vector1.x + vector2.x;
        temporaryVector.y = vector1.y + vector2.y;
        return temporaryVector;
    }

    /**
     * Scale a vector
     * @param temporaryVector To reuse allocated memory.
     * @param vector The vector to be scaled.
     * @param scale The amount to scale the vector by.
     * @return The scaled vector.
     */
    public static PointF scale(PointF temporaryVector, PointF vector, float scale)
    {
        temporaryVector.x = vector.x * scale;
        temporaryVector.y = vector.y * scale;
        return temporaryVector;
    }





//These ones are kinda terrible because they use up way too much memory with garbage.


    /**
     *
     * @param vector1 The vector to be subtracted by the other.
     * @param vector2 The vector to subtract the first by.
     * @return The difference.
     */
    public static PointF sub(PointF vector1, PointF vector2)
    {
        return new PointF(vector1.x - vector2.x, vector1.y - vector2.y);
    }

    /**
     *
     * @param vector1 The vector to be added by the second.
     * @param vector2 The vector to add to the first.
     * @return The sum.
     */
    public static PointF add(PointF vector1, PointF vector2)
    {
        return new PointF(vector1.x + vector2.x, vector1.y + vector2.y);
    }

    /**
     *
     * @param vector The vector to be scaled.
     * @param scale The amount to scale the vector by.
     * @return The scaled vector.
     */
    public static PointF scale(PointF vector, float scale)
    {
        return new PointF(vector.x * scale, vector.y * scale);
    }
}
