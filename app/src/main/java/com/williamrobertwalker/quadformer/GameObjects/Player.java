package com.williamrobertwalker.quadformer.GameObjects;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.williamrobertwalker.quadformer.GameView;
import com.williamrobertwalker.quadformer.MainActivity;
import com.williamrobertwalker.quadformer.VectorMethods;

/**
 * Created by TheWo_000 on 2/7/2016.
 */
public class Player extends Entity{



    public Player(PointF location, int width, int height, int color)
    {
        super(location, width, height, color);
        vertices[0] = new PointF(MainActivity.gameView.getWidth() / 2, MainActivity.gameView.getHeight() / 2);
        vertices[1] = new PointF(MainActivity.gameView.getWidth() / 2, MainActivity.gameView.getHeight() / 2 + this.height);
        vertices[2] = new PointF(MainActivity.gameView.getWidth() / 2 + this.width, MainActivity.gameView.getHeight() / 2 + this.height);
        vertices[3] = new PointF(MainActivity.gameView.getWidth() / 2 + this.width, MainActivity.gameView.getHeight() / 2);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(canvas.getWidth() / 2, canvas.getHeight() / 2, canvas.getWidth() / 2 + this.width, canvas.getHeight() / 2 + this.height, paint);
//        canvas.drawRect(this.location.x, this.location.y, this.location.x + width, this.location.y + height, paint);
    }

    //V1 Working, but memory intensive.
    /*@Override
    public void drawShadow(Canvas canvas)
    {
        vertices = this.getVertices();
        for(Light light : GameView.lights)
        {
            for(int i = 0; i < vertices.length; i++)
            {
                currentVertex = vertices[i];
                nextVertex = vertices[(i + 1) % vertices.length];
                edge = VectorMethods.sub(nextVertex, currentVertex);
                normal.set(edge.y, -edge.x);
                lightToCurrent = VectorMethods.sub(currentVertex, light.location);

                if(VectorMethods.dot(normal, lightToCurrent) > 0)
                {
                    point1 = VectorMethods.scale(VectorMethods.add(currentVertex, VectorMethods.sub(currentVertex, VectorMethods.scale((light.location), 2))), 80);
                    point2 = VectorMethods.scale(VectorMethods.add(nextVertex, VectorMethods.sub(nextVertex, VectorMethods.scale((light.location), 2))), 80);

                    //Drawing the shadow quads.
                    path.reset();
                    path.moveTo(currentVertex.x - this.location.x + MainActivity.gameView.getWidth()/2, currentVertex.y - this.location.y + MainActivity.gameView.getHeight()/2);
                    path.lineTo(point1.x - this.location.x + MainActivity.gameView.getWidth()/2, point1.y - this.location.y + MainActivity.gameView.getHeight()/2);
                    path.lineTo(point2.x - this.location.x + MainActivity.gameView.getWidth()/2, point2.y - this.location.y + MainActivity.gameView.getHeight()/2);
                    path.lineTo(nextVertex.x -this.location.x + MainActivity.gameView.getWidth()/2, nextVertex.y - this.location.y + MainActivity.gameView.getHeight()/2);
                    canvas.drawPath(path, shadowPaint);
                }
            }
        }
    }*/


    //Works (Except for one side of the shadow is broken a little bit...)
    @Override
    public void drawShadow(Canvas canvas)
    {
        vertices = this.getVertices();
        for(Light light : GameView.lights)
        {
            for(int i = 0; i < vertices.length; i++)
            {
                currentVertex = vertices[i];
                nextVertex = vertices[(i + 1) % vertices.length];
                edge = VectorMethods.sub(tempVector, nextVertex, currentVertex);
                normal.set(edge.y, -edge.x);
                lightToCurrent = VectorMethods.sub(tempVector, currentVertex, light.location);

                if(VectorMethods.dot(normal, lightToCurrent) > 0)
                {
                    //TODO: Make me more efficient
                    point1 = VectorMethods.scale(tempVector2, VectorMethods.add(tempVector, currentVertex, VectorMethods.sub(tempVector, currentVertex, VectorMethods.scale(tempVector, (light.location), 2))), 100);
                    point2 = VectorMethods.scale(tempVector3, VectorMethods.add(tempVector, nextVertex, VectorMethods.sub(tempVector, nextVertex, VectorMethods.scale(tempVector, (light.location), 2))), 100);

                    //Drawing the shadow quads.
                    path.reset();
                    path.moveTo(currentVertex.x - this.location.x + MainActivity.gameView.getWidth()/2, currentVertex.y - this.location.y + MainActivity.gameView.getHeight()/2);
                    path.lineTo(point1.x - this.location.x + MainActivity.gameView.getWidth()/2, point1.y - this.location.y + MainActivity.gameView.getHeight()/2);
                    path.lineTo(point2.x - this.location.x + MainActivity.gameView.getWidth()/2, point2.y - this.location.y + MainActivity.gameView.getHeight()/2);
                    path.lineTo(nextVertex.x -this.location.x + MainActivity.gameView.getWidth()/2, nextVertex.y - this.location.y + MainActivity.gameView.getHeight()/2);
                    canvas.drawPath(path, shadowPaint);
                }
            }
        }
    }
}
