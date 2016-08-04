package com.williamrobertwalker.quadformer;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class IntroActivity extends Activity {

    ImageView titleImageView;
    TextView tapToContinueTextView;

    TextView newGameTextView;
    TextView continueTextView;

    TextView newGameImageTextView;
    TextView continueImageTextView;
    ObjectAnimator tapToContinueTextViewAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        titleImageView = (ImageView) findViewById(R.id.titleImageView);
        tapToContinueTextView = (TextView) findViewById(R.id.tapToContinueTextView);
        newGameImageTextView = (TextView) findViewById(R.id.newGameImageView);
        continueImageTextView = (TextView) findViewById(R.id.continueImageView);
        newGameTextView = (TextView) findViewById(R.id.newGameTextView);
        continueTextView = (TextView) findViewById(R.id.continueTextView);

        tapToContinueTextViewAnimator = ObjectAnimator.ofFloat(tapToContinueTextView, "alpha", 1, 0);
        tapToContinueTextViewAnimator.setDuration(1500);
        tapToContinueTextViewAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        tapToContinueTextViewAnimator.setRepeatCount(ValueAnimator.INFINITE);
        tapToContinueTextViewAnimator.setRepeatMode(ValueAnimator.REVERSE);
        tapToContinueTextViewAnimator.start();

        tapToContinueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapToContinueTextViewAnimator.end();

                tapToContinueTextView.setClickable(false);

                //Title Animation

                ObjectAnimator titleImageViewYAnimator;

                titleImageViewYAnimator = ObjectAnimator.ofFloat(titleImageView, "Y", titleImageView.getY(), (titleImageView.getY() - titleImageView.getHeight() / 2) + 30);
                titleImageViewYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                titleImageViewYAnimator.setEvaluator(new FloatEvaluator());
                titleImageViewYAnimator.setDuration(1000);
                titleImageViewYAnimator.start();

                ObjectAnimator titleImageViewScaleXAnimator;

                titleImageViewScaleXAnimator = ObjectAnimator.ofFloat(titleImageView, "ScaleX", 1f, 0.5f);
                titleImageViewScaleXAnimator.setDuration(1000);
                titleImageViewScaleXAnimator.start();

                ObjectAnimator titleImageViewScaleYAnimator;

                titleImageViewScaleYAnimator = ObjectAnimator.ofFloat(titleImageView, "ScaleY", 1f, 0.5f);
                titleImageViewScaleYAnimator.setDuration(1000);
                titleImageViewScaleYAnimator.start();


                //Tap To Continue Animation
                ObjectAnimator tapToContinueTextViewAnimator;

                tapToContinueTextViewAnimator = ObjectAnimator.ofFloat(tapToContinueTextView, "alpha", 1f, 0f);
                tapToContinueTextViewAnimator.setDuration(1000);
                tapToContinueTextViewAnimator.start();


                //New Game Animation
                ObjectAnimator newGameTextViewAnimator;

                newGameTextViewAnimator = ObjectAnimator.ofFloat(newGameTextView, "alpha", 0f, 1f);
                newGameTextViewAnimator.setDuration(1000);
                newGameTextViewAnimator.start();

                ObjectAnimator newGameImageTextViewAnimator;

                newGameImageTextViewAnimator = ObjectAnimator.ofFloat(newGameImageTextView, "alpha", 0f, 1f);
                newGameImageTextViewAnimator.setDuration(1000);
                newGameImageTextViewAnimator.start();


                //Continue Game Animation
                ObjectAnimator continueTextViewAnimator;

                continueTextViewAnimator = ObjectAnimator.ofFloat(continueTextView, "alpha", 0f, 1f);
                continueTextViewAnimator.setDuration(1000);
                continueTextViewAnimator.start();

                ObjectAnimator continueImageTextViewAnimator;

                continueImageTextViewAnimator = ObjectAnimator.ofFloat(continueImageTextView, "alpha", 0f, 1f);
                continueImageTextViewAnimator.setDuration(1000);
                continueImageTextViewAnimator.start();


                //This is used to remove instant game starting after clicking "Tap to Continue"
                tapToContinueTextViewAnimator.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        newGameImageTextView.setEnabled(true);
                    }

                    @Override public void onAnimationStart(Animator animation) {}
                    @Override public void onAnimationCancel(Animator animation) {}
                    @Override public void onAnimationRepeat(Animator animation) {}
                });

            }
        });

        newGameImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
