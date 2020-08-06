package com.example.indiabudgetgov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
ImageView imageview;
private static int SPLASH_TIMEOUT=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageview=findViewById(R.id.logo);
        imageview.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);
               //fINISH THE SPLASH SCREEN ACTIVITY TO AVOID BACK BUTTON
                finish();
            }
        },SPLASH_TIMEOUT);
    }


}
