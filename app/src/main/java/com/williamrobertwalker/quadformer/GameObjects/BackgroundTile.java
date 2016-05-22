package com.williamrobertwalker.quadformer.GameObjects;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import com.williamrobertwalker.quadformer.GameView;

/**
 * Created by TheWo_000 on 2/7/2016.
 */
public class BackgroundTile {
    public int width;
    public int height;
    protected PointF location;
    protected Paint paint = new Paint();

    public BackgroundTile(int colorID, PointF location, int width, int height, boolean getColorFromId) {
        this.width = width;
        this.height = height;
        this.location = new PointF(location.x * width, location.y * height);

        if(getColorFromId) {
            paint.setColor(GameView.getColorFromID(colorID, GameView.backgroundColorMap));
        }
        else
        {
            paint.setColor(colorID);
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(location.x - GameView.viewOffset.x * 0.25f, location.y - GameView.viewOffset.y * 0.25f, (location.x + width) - GameView.viewOffset.x * 0.25f, (location.y + height) - GameView.viewOffset.y * 0.25f, paint);
    }

//    private void drawGrid(Canvas canvas, int size) {
//        for (int j = 0; j < size; j++) {
//            for (int i = 0; i < size; i++) {
//                canvas.drawRect((i * width) - GameView.viewOffset.x, (j * height) - GameView.viewOffset.y, location.x + width ,location.y + height, paint); //Draw background color to canvas
//            }
//        }
//    }
}
