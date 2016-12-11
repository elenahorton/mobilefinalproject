package com.example.elenahorton.mobilefinalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;

import android.animation.Animator;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends AppCompatActivity {

    public static final int PERIOD = 3000;
    private boolean timerEnabled = false;
    private Timer timer;
    private int shoppingActivity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupSplash();
    }

    private void setupSplash() {
        ImageView logo = (ImageView) findViewById(R.id.imgLogo);
        logo.setImageResource(R.drawable.app_logo);
        setupTimer();
        setupAnimation(logo);
    }

    private void setupAnimation(ImageView logo) {
        final Animation logoAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.logoanim);
        logo.startAnimation(logoAnim);
    }

    private void setupTimer() {
        timerEnabled = true;
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTaskShowTime(), 0, PERIOD);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timerEnabled = false;
        if (timer != null) {
            timer.cancel();
        }
    }

    private class TimerTaskShowTime extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // jump to List Activity after 3 seconds
                    launchListActivity();
                }
            });
        }
    }

    private void launchListActivity() {
        if (shoppingActivity == 1) {
            startShoppingActivity();
        }
        shoppingActivity++;
    }

    private void startShoppingActivity() {
        Intent intentShowList = new Intent();
        intentShowList.setClass(SplashActivity.this, LoginActivity.class);
        startActivity(intentShowList);
    }
}