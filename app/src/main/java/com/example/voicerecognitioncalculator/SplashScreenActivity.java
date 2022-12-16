package com.example.voicerecognitioncalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreenActivity extends AppCompatActivity {



    TextView splash_name;
    LottieAnimationView splash_lottie;

    private final int SPLASH_DISPLAY_LENGTH = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        splash_name=findViewById(R.id.splash_name);
        splash_lottie=findViewById(R.id.splash_lottie);

        splash_name.animate().translationY(-1600).setDuration(10000).setStartDelay(40000);
        splash_name.animate().translationY(1400).setDuration(10000).setStartDelay(40000);
        splash_lottie.animate().translationY(1600).setDuration(10000).setStartDelay(40000);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreenActivity.this, SampleCalcActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}