package com.williamrobertwalker.quadformer;

import android.content.Context;
import android.graphics.PointF;
import android.util.Log;

import com.williamrobertwalker.quadformer.GameObjects.BackgroundTile;
import com.williamrobertwalker.quadformer.GameObjects.GroundTile;
import com.williamrobertwalker.quadformer.GameObjects.Light;
import com.williamrobertwalker.quadformer.GameObjects.MidgroundTile;
import com.williamrobertwalker.quadformer.GameObjects.NextLevelBackgroundTile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by TheWo_000 on 2/7/2016.
 */
public class Load {

    //Original was 12.75%.
    //TODO: Fix the fact that at 8.5% size, the character will stick to the ceiling and walk around as if it's Spiderman.
//    public static final float PERCENT_SIZE_OF_OBJECTS_RELATIVE_TO_SCREEN = (12.75f / 100);
    static final float PERCENT_SIZE_OF_OBJECTS_RELATIVE_TO_SCREEN = (8.75f / 100);
    final int blockHeight = 3;

    /**
     * Loads the background from files using the current level
     * @param level Needs the current level to load the correct files.
     * @param context Needs a context to load from the file in assets.
     * @throws IOException Incase !$#@ goes south!
     */
    static void background(int level, Context context) throws IOException {

        final int OBJECT_SIZE = (int) (PERCENT_SIZE_OF_OBJECTS_RELATIVE_TO_SCREEN * GameView.diagonalScreenSize);

        ArrayList<String> lines = new ArrayList<>();
        int width = 0;
        int height;

        InputStream is = context.getAssets().open("Levels/background_level_" + level);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            if (!line.startsWith("!")) { //If the line isn't a comment
                lines.add(line);
                width = Math.max(width, line.length());

            }
        }
        height = lines.size();

        for (int j = 0; j < height; j++) {
            String line = lines.get(j);
            for (int i = 0; i < width; i++) {

                if (i < line.length()) {
                    char character = line.charAt(i);
                    try {
                        if(Character.getNumericValue(character) == 1) {
                            BackgroundTile backgroundTile = new BackgroundTile( GameView.getColorFromID(Character.getNumericValue(character),  GameView.backgroundColorMap), new PointF(i, j), OBJECT_SIZE, OBJECT_SIZE, false);
                            GameView.backgroundTileList.add(backgroundTile);
                        }
                        else if(Character.getNumericValue(character) == 2) {
                            MidgroundTile midgroundTile = new MidgroundTile( GameView.getColorFromID(1,  GameView.midgroundColorMap), new PointF(i, j), OBJECT_SIZE, OBJECT_SIZE, false);
                            GameView.midgroundTileList.add(midgroundTile);
                        }
                        else if(Character.getNumericValue(character) == 3) {
                            GameView.nextLevelBackgroundTile = new NextLevelBackgroundTile(new PointF(i, j), OBJECT_SIZE, OBJECT_SIZE, 0xffffffff);
                        }
//                        if (Character.getNumericValue(character) == 9) {
//                            GameView.nextLevelBackgroundTile = new NextLevelBackgroundTile(new PointF(i, j));
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Error", "Background Tile did not load correctly.");
                    }
                }
            }
        }
    }

    //DONE: make it so that the width of the ground tile isn't based on the color. Make it based on a static number that can be changed.
    //DONE: this will make sure that when you move from a phone to a tablet you don't get more viewing distance.
    /**
     * Loads all of the ground tiles from a filename
     * @param level The level of the game to load the file for.
     * @param context Needs a context to load from the file.
     * @throws IOException Incase it explodes.
     */
    static void ground(int level, Context context) throws IOException {

        final int OBJECT_SIZE = (int) (PERCENT_SIZE_OF_OBJECTS_RELATIVE_TO_SCREEN * GameView.diagonalScreenSize);

        ArrayList<String> lines = new ArrayList<>();
        int widthOfGrid = 0;
        int heightOfGrid;
        boolean loadedSuccessfully = true;

        InputStream is = context.getAssets().open("Levels/ground_level_" + level);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            if (!line.startsWith("!")) { //If the line isn't a comment
                lines.add(line);

                //Continually checks to see if the current line is larger than the widthOfGrid.
                widthOfGrid = Math.max(widthOfGrid, line.length());

            }
        }
        heightOfGrid = lines.size();

        for (int j = 0; j < heightOfGrid; j++) {
            String line = lines.get(j);
            for (int i = 0; i < widthOfGrid; i++) {
                if (i < line.length()) {
                    char character = line.charAt(i);
                    try {
                        if (Character.getNumericValue(character) > 0) {
                            int color = GameView.getColorFromID(Character.getNumericValue(character), GameView.groundTileColorMap);
                            new GroundTile(color, new PointF(i, j), OBJECT_SIZE, OBJECT_SIZE);
//                            GameView.wallList.add(wall);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("DEBUG", "Ground Tile did not load correctly.");
                        loadedSuccessfully = false;
                    }
                    //DO THE THING
                }
            }
        }
        if (!loadedSuccessfully)
//            Log.i("MAP", "Wall map has been loaded successfully");
            Log.e("MAP", "Ground Tile map did not load correctly.");


    }

    static void lights(int level, Context context) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        int width = 0;
        int height;
        boolean loadedSuccessfully = true;

        InputStream is = context.getAssets().open("Levels/lights_level_" + level);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            if (!line.startsWith("!")) { //If the line isn't a comment
                lines.add(line);
                width = Math.max(width, line.length());

            }
        }
        height = lines.size();

        for (int j = 0; j < height; j++) {
            String line = lines.get(j);
            for (int i = 0; i < width; i++) {
                if (i < line.length()) {
                    char character = line.charAt(i);
                    try {
                        if (Character.getNumericValue(character) > 0) {
                            new Light(new PointF(i, j));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("DEBUG", "Light did not load correctly.");
                        loadedSuccessfully = false;
                    }
                    //DO THE THING
                }
            }
        }
        if (!loadedSuccessfully)
//            Log.i("MAP", "Wall map has been loaded successfully");
            Log.e("MAP", "Wall map did not load correctly.");


    }
}
