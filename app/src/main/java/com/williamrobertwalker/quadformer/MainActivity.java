package com.williamrobertwalker.quadformer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public static GameView gameView;
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
                gameView.generateLevel();
            }
        });
    }
}
