package com.williamrobertwalker.quadformer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.williamrobertwalker.quadformer.GameObjects.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * TODO: document your custom view class.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{



    public static   int level = 1;
//    public static   boolean doLevelChange = false; //TODO: Make this into a method doLevelChange that calls generateLevel.

    public static   UpdateThread updateThread;
    public static   DrawThread drawThread;
    public static   PointF viewOffset;

    public static   Player player;

//    public static   ArrayList<Character> entityList;
    public static   ArrayList<BackgroundTile> backgroundTileList;
    public static   ArrayList<MidgroundTile> midgroundTileList;
    public static   ArrayList<GroundTile> groundTileList;
    public static   ArrayList<Light> lights;

//    public static final int[] backgroundColorRGB = {40, 40, 40};
    public static final int[] backgroundColorRGB = {70, 70, 70};

    final static int backgroundColor = Color.rgb(backgroundColorRGB[0], backgroundColorRGB[1], backgroundColorRGB[2]);
    public static final int shadowColor = Color.rgb(backgroundColorRGB[0] - (int)(backgroundColorRGB[0] * 0.1f), backgroundColorRGB[1] - (int)(backgroundColorRGB[1] * 0.1f), backgroundColorRGB[2] - (int)(backgroundColorRGB[2] * 0.1f));

    public static   ArrayMap<String, Integer> backgroundColorMap;
    public static   ArrayMap<String, Integer> midgroundColorMap;
    public static   ArrayMap<String, Integer> groundTileColorMap;

    public static final Object syncLock = new Object();
    public static NextLevelBackgroundTile nextLevelBackgroundTile;

    public GameView(Context context) {
        super(context);
        //Add callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);
        //Make gameView focusable so it can handle events
        this.setFocusable(true);


    }

    public GameView(Context context, AttributeSet attributeSet) {

        super(context, attributeSet);
        //Add callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);
        //Make gameView focusable so it can handle events
        this.setFocusable(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { //Only whether the screen has been touched or not and where is sensed, because all you do with
        //the main screen is touch to fire and aim at where it was touched.

        if(event.getAction() == MotionEvent.ACTION_DOWN) //FIRE
        {
            player.jump();
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP) // Finger Leaves screen
        {
            return true;
        }


        return super.onTouchEvent(event);
    }



    public void update() {

        viewOffset.x = player.location.x - this.getWidth() / 2;
        viewOffset.y = player.location.y - this.getHeight() / 2;
        player.update();
        if(nextLevelBackgroundTile != null) {
            nextLevelBackgroundTile.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
//        canvas.drawColor(0xff182022);
        canvas.drawColor(backgroundColor);


        //Draw shadows
        for(int i = 0; i < groundTileList.size() ; i++) {
            groundTileList.get(i).drawShadow(canvas);
        }
        player.drawShadow(canvas);

        //The background and midground are drawn after the shadows because
        //the shadows have to be opaque to not overlap. Consequently the background blobs in the background
        //and the midground have to be translucent and drawn over top of the shadows. This is a stupid
        //way of doing this.
        //Draw background
        for(int i = 0; i < backgroundTileList.size() ; i++) {
            backgroundTileList.get(i).draw(canvas);
        }
        //Draw midground
        for(int i = 0; i < midgroundTileList.size() ; i++) {
            midgroundTileList.get(i).draw(canvas);
        }
        if(nextLevelBackgroundTile != null) {
            nextLevelBackgroundTile.draw(canvas);
        }


        //Draw blocks, entities and player
        player.draw(canvas);
        for(int i = 0; i < groundTileList.size() ; i++) {
            groundTileList.get(i).draw(canvas);
        }

    }

    public void generateLevel() {

        synchronized(syncLock) {
            initializeColors();
            createBackground(level);
            createGround(level);
            createLights(level);

            viewOffset = new PointF();
            player = new Player(new PointF(129, 0), 23, 68, 0xffC9C9A7);
        }
    }

    private void createLights(int level) {

        lights = new ArrayList<>();
        try {
            Load.lights(level, this.getContext());
        } catch(IOException e) {
            e.printStackTrace();
            Log.i("ERROR", "Error loading lights file. Wrong Filename?");
        }

    }

    private void createGround(int level) {

        groundTileList = new ArrayList<>();
        try {
            Load.ground(level, this.getContext());
        } catch(IOException e) {
            e.printStackTrace();
            Log.i("ERROR", "Error loading ground file. Wrong Filename?");
        }

//        new GroundTile(0xff000000, new PointF(0, getHeight()-50), 700, 50);
    }

    private void createBackground(int level) {

        backgroundTileList = new ArrayList<>();
        midgroundTileList = new ArrayList<>();
        try {
            Load.background(level, this.getContext());
        } catch(IOException e) {
            e.printStackTrace();
            Log.i("ERROR", "Error loading background file. Wrong Filename?");
        }
    }

    private void initializeColors() {

        backgroundColorMap  = new ArrayMap<>();
        midgroundColorMap = new ArrayMap<>();
        groundTileColorMap  = new ArrayMap<>();

        backgroundColorMap.put("1", 0x20444444);
//        midgroundColorMap .put("1", Color.rgb(GameView.backgroundColorRGB[0] - 10, GameView.backgroundColorRGB[1] - 10, GameView.backgroundColorRGB[2] - 10));
        midgroundColorMap .put("1", 0x05000000);
        groundTileColorMap.put("1", 0xff000000);
    }

    public static int getColorFromID(int imageID, Map<String, Integer> map) {
        int color = 0xff888888;
        try {
            color = map.get("" + imageID);
        } catch(Exception e) {
            Log.i("ERROR", "No Color in colorMap with ID " + imageID);
//            e.printStackTrace();
        }

        return color;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        synchronized(syncLock) {

//            imageMap = new ArrayMap<>();
//            backgroundColorMap = new ArrayMap<>();
//            wallImageMap = new ArrayMap<>();
//
//            decodeImages();

            generateLevel();


//            if(!MainActivity.alreadyStarted) {
                updateThread = new UpdateThread(this);
                updateThread.setRunning(true); //start game loop
                updateThread.start();

                drawThread = new DrawThread(getHolder(), this);
                drawThread.setRunning(true); //start game loop
                drawThread.start();
//                MainActivity.alreadyStarted = true;
//            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
