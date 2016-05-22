package com.williamrobertwalker.quadformer;

public interface JoystickMovedListener {

    public void OnMoved(int pan, int tilt);

    public void OnReleased();

}