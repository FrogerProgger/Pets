package com.example.pets;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private View rootView;
    private Animation fadeIn;
    private  Animation fadeOut;

    private void goToNextActivity(){
        rootView.setAlpha(0f);
        if (isPetExists()) {
            Intent intent = new Intent(MainActivity.this, MainScreen.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, ChooseAnimal.class);
            startActivity(intent);
        }
    }

    private boolean isPetExists() {
        File file = new File(getFilesDir(), "pet.json");
        return file.exists();
    }
    private void setAnimationsListeners() {
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        rootView.startAnimation(fadeOut);
                    }
                }.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goToNextActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void startAnimation() {
        rootView.startAnimation(fadeIn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        setAnimationsListeners();
        startAnimation();
    }
}