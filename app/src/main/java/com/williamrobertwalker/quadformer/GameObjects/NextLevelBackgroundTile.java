package com.williamrobertwalker.quadformer.GameObjects;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;
import com.williamrobertwalker.quadformer.GameView;
import com.williamrobertwalker.quadformer.MainActivity;


public class NextLevelBackgroundTile extends BackgroundTile {

    public NextLevelBackgroundTile(PointF location) {
        super(9, location, 128, 128, true);
    }

    public NextLevelBackgroundTile(PointF location, int color) {

        super(color, location, 128, 128, false);
    }

    public void update() {
        if (GameView.player.location.x > location.x && GameView.player.location.x < location.x + this.width
                && GameView.player.location.y > location.y && GameView.player.location.y < location.y + this.height) {
            //Go to next Level!
            Log.i("LEVEL", "You have just gone to the NEXT LEVEL!");
            GameView.level++;
            MainActivity.gameView.generateLevel();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(location.x - GameView.viewOffset.x, location.y - GameView.viewOffset.y, (location.x + width) - GameView.viewOffset.x, (location.y + height) - GameView.viewOffset.y, paint);
    }
}