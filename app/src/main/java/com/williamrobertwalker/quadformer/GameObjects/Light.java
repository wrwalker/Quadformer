package com.williamrobertwalker.quadformer.GameObjects;

import android.graphics.PointF;

import com.williamrobertwalker.quadformer.GameView;

/**
 * Created by TheWo_000 on 3/7/2016.
 */
public class Light {
    public PointF location;

    public Light(PointF location) {
        this.location = new PointF(location.x * 128, location.y * 128);
        GameView.lights.add(this);
    }
}
