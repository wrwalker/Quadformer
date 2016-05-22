package com.williamrobertwalker.quadformer.GameObjects;

/**
 * Created by TheWo_000 on 3/13/2016.
 */

import android.graphics.Canvas;
import android.graphics.PointF;

import com.williamrobertwalker.quadformer.GameView;

/**
 * Created by TheWo_000 on 2/7/2016.
 */
//public class MidgroundTile {
//    public int width;
//    public int height;
//    protected PointF location;
//    Paint paint = new Paint();
//
//    public MidgroundTile(int colorID, PointF location, int width, int height, boolean getColorFromId) {
//        this.width = width;
//        this.height = height;
//        this.location = new PointF(location.x * width, location.y * height);
//
//        if(getColorFromId) {
//            paint.setColor(GameView.getColorFromID(colorID, GameView.midgroundColorMap));
//        }
//        else
//        {
//            paint.setColor(colorID);
//        }
//    }
//
//    public void draw(Canvas canvas) {
//        canvas.drawRect(location.x - GameView.viewOffset.x * 0.66f, location.y - GameView.viewOffset.y * 0.66f, (location.x + width) - GameView.viewOffset.x * 0.66f, (location.y + height) - GameView.viewOffset.y * 0.66f, paint);
//    }
//}
public class MidgroundTile extends BackgroundTile {

    public MidgroundTile(int colorID, PointF location, int width, int height, boolean getColorFromId) {

        super(colorID, location, width, height, getColorFromId);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(location.x - GameView.viewOffset.x * 0.66f, location.y - GameView.viewOffset.y * 0.66f, (location.x + width) - GameView.viewOffset.x * 0.66f, (location.y + height) - GameView.viewOffset.y * 0.66f, paint);
    }
}
