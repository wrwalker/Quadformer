package com.williamrobertwalker.quadformer.GameObjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.williamrobertwalker.quadformer.GameView;
import com.williamrobertwalker.quadformer.VectorMethods;

/**
 * Created by William Walker on 2/7/2016.
 */


public class GroundTile {
    public PointF location;
    private final int width;
    private final int height;
    Paint paint = new Paint();

    public PointF max, min, topRight, bottomLeft;
    PointF[] vertices = new PointF[4];
    Paint shadowPaint = new Paint();


    public GroundTile(int color, PointF location, int width, int height) {

        this.location = new PointF();
        this.location.x = location.x * width;
        this.location.y = location.y * height;
        this.width = width;
        this.height = height;
        this.paint.setColor(color);
        shadowPaint.setColor(GameView.shadowColor);

        min = new PointF();
        bottomLeft = new PointF();
        max = new PointF();
        topRight = new PointF();

        vertices = new PointF[]{
                min,
                bottomLeft,
                max,
                topRight
        };

        GameView.groundTileList.add(this);
    }

    public void draw(Canvas canvas) {


        min = location;
//        bottomLeft.set(this.location.x, this.location.y + height);
        bottomLeft.x = this.location.x;
        bottomLeft.y = this.location.y + height;
//        max.set(this.location.x + width, this.location.y + height);
        max.x = this.location.x + width;
        max.y = this.location.y + height;
//        topRight.set(this.location.x + width, this.location.y);
        topRight.x = this.location.x + width;
        topRight.y = this.location.y;

        vertices[0] = min;
        vertices[1] = bottomLeft;
        vertices[2] = max;
        vertices[3] = topRight;

        canvas.drawRect(location.x - GameView.viewOffset.x, location.y - GameView.viewOffset.y, (location.x + width) - GameView.viewOffset.x, (location.y + height) - GameView.viewOffset.y, paint);
    }

    //Variables for the drawShadow Method.

    PointF currentVertex;
    PointF nextVertex;
    PointF edge;
    PointF normal = new PointF();
    PointF lightToCurrent;

    PointF point1;
    PointF point2;

    /**
     * Temporary vector to reuse pre-allocated memory instead of creating (literally) hundreds of thousands of single-use instances of PointFs.
     */
    PointF tempVector = new PointF();
    PointF tempVector2 = new PointF();
    PointF tempVector3 = new PointF();

    Path path = new Path();

//    public void drawShadow(Canvas canvas)
//    {
//
//        for(int j = 0; j < GameView.lights.size(); j++)
//        {
//            for(int i = 0; i < vertices.length; i++)
//            {
//                currentVertex = vertices[i];
//                nextVertex = vertices[(i + 1) % vertices.length];
//                edge = VectorMethods.sub(tempVector, nextVertex, currentVertex);
//                normal.set(edge.y, -edge.x);
//                lightToCurrent = VectorMethods.sub(tempVector, currentVertex, GameView.lights.get(j).location);
//
//                if(VectorMethods.dot(normal, lightToCurrent) > 0)
//                {
////                    point1 = VectorMethods.scale(tempVector, VectorMethods.add(tempVector2, currentVertex, VectorMethods.sub(tempVector, currentVertex, VectorMethods.scale(tempVector3, (GameView.lights.get(j).location), 2))), 10);
////                    point2 = VectorMethods.scale(tempVector, VectorMethods.add(tempVector2, nextVertex, VectorMethods.sub(tempVector, nextVertex, VectorMethods.scale(tempVector3, (GameView.lights.get(j).location), 2))), 10);
//                    point1 = VectorMethods.scale(tempVector, VectorMethods.add(tempVector2, currentVertex, VectorMethods.sub(tempVector, currentVertex, VectorMethods.scale(tempVector3, (GameView.lights.get(j).location), 2))), 80);
//                    point2 = VectorMethods.scale(tempVector, VectorMethods.add(tempVector2, nextVertex, VectorMethods.sub(tempVector, nextVertex, VectorMethods.scale(tempVector3, (GameView.lights.get(j).location), 2))), 80);
//
//                    //Drawing the shadow quads.
//                    path.reset();
//                    path.moveTo(currentVertex.x - GameView.viewOffset.x, currentVertex.y - GameView.viewOffset.y);
//                    path.lineTo(point1.x - GameView.viewOffset.x, point1.y - GameView.viewOffset.y);
//                    path.lineTo(point2.x - GameView.viewOffset.x, point2.y - GameView.viewOffset.y);
//                    path.lineTo(nextVertex.x - GameView.viewOffset.x, nextVertex.y - GameView.viewOffset.y);
//                    canvas.drawPath(path, shadowPaint);
//                }
//            }
//        }
//
//    }
    public void drawShadow(Canvas canvas)
    {

        for(int j = 0; j < GameView.lights.size(); j++)
        {
            for(int i = 0; i < vertices.length; i++)
            {
                currentVertex = vertices[i];
                nextVertex = vertices[(i + 1) % vertices.length];
                edge = VectorMethods.sub(tempVector, nextVertex, currentVertex);
//                normal.set(edge.y, -edge.x);
                normal.x = edge.y;
                normal.y = -edge.x;
                lightToCurrent = VectorMethods.sub(tempVector, currentVertex, GameView.lights.get(j).location);

                if(VectorMethods.dot(normal, lightToCurrent) > 0)
                {
//                    point1 = VectorMethods.scale(tempVector, VectorMethods.add(tempVector2, currentVertex, VectorMethods.sub(tempVector, currentVertex, VectorMethods.scale(tempVector3, (GameView.lights.get(j).location), 2))), 10);
//                    point2 = VectorMethods.scale(tempVector, VectorMethods.add(tempVector2, nextVertex, VectorMethods.sub(tempVector, nextVertex, VectorMethods.scale(tempVector3, (GameView.lights.get(j).location), 2))), 10);
                    //TODO: Make me more efficient
                    point1 = VectorMethods.scale(tempVector2, VectorMethods.add(tempVector, currentVertex, VectorMethods.sub(tempVector, currentVertex, VectorMethods.scale(tempVector, (GameView.lights.get(j).location), 2))), 100);
                    point2 = VectorMethods.scale(tempVector3, VectorMethods.add(tempVector, nextVertex, VectorMethods.sub(tempVector, nextVertex, VectorMethods.scale(tempVector, (GameView.lights.get(j).location), 2))), 100);

                    //Drawing the shadow quads.
                    path.rewind();
                    path.moveTo(currentVertex.x - GameView.viewOffset.x, currentVertex.y - GameView.viewOffset.y);
                    path.lineTo(point1.x - GameView.viewOffset.x, point1.y - GameView.viewOffset.y);
                    path.lineTo(point2.x - GameView.viewOffset.x, point2.y - GameView.viewOffset.y);
                    path.lineTo(nextVertex.x - GameView.viewOffset.x, nextVertex.y - GameView.viewOffset.y);
                    canvas.drawPath(path, shadowPaint);
                }
            }
        }

    }


    //region Getters & Setters:
    public int getWidth() {

        return width;
    }

    public int getHeight() {

        return height;
    }

    public PointF getLocation() {

        return location;
    }

//    public PointF[] getVertices() {
//        return new PointF[] {
//                new PointF(this.location.x, this.location.y),
//                new PointF(this.location.x, this.location.y + height),
//                new PointF(this.location.x + width, this.location.y + height),
//                new PointF(this.location.x + width, this.location.y)
//        };
//    }



    public PointF[] getVertices() {
        return vertices;
    }
    //endregion

}
