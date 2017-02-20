package com.williamrobertwalker.quadformer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public static GameView gameView;
//    public static boolean alreadyStarted = false;
    private static boolean screenIsOff = false;
    public Button resetButton;
//    public static final Object syncLock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameView = (GameView) findViewById(R.id.gameView);
        resetButton = (Button) findViewById(R.id.resetButton);


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseGame();
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    public static void pauseGame() {

        if(GameView.updateThread != null && GameView.drawThread != null && !screenIsOff) {

            try {
                GameView.updateThread.setRunning(false);
                GameView.updateThread.join();
                GameView.updateThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                try {
                    throw e;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            try {
                GameView.drawThread.setRunning(false);
                GameView.drawThread.join();
                GameView.drawThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                try {
                    throw e;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
//            alreadyStarted = false;
        }
    }

    public static void autoPauseGame() {

        if(GameView.updateThread != null && GameView.drawThread != null) {

            try {
                GameView.updateThread.setRunning(false);
                GameView.updateThread.join();
                GameView.updateThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                try {
                    throw e;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            try {
                GameView.drawThread.setRunning(false);
                GameView.drawThread.join();
                GameView.drawThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
                try {
                    throw e;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
//            alreadyStarted = false;
        }
    }

    public static void resumeGame() {

        if((GameView.updateThread == null) && (GameView.drawThread == null) /*&& alreadyStarted*/) {

            GameView.updateThread = new UpdateThread(gameView);
            GameView.updateThread.setRunning(true); //start game loop
            GameView.updateThread.start();

            GameView.drawThread = new DrawThread(gameView.getHolder(), gameView);
            GameView.drawThread.setRunning(true); //start game loop
            GameView.drawThread.start();
        }
//        alreadyStarted = true;
    }

    @Override
    protected void onPause() {

        super.onPause();
    }
}
