package com.example.keith.pregopizza.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.keith.pregopizza.R;

import gr.net.maroulis.library.EasySplashScreen;

/**
 * Created by Keith on 15/04/2018.
 */

public class SplashScreen extends AppCompatActivity

    {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(CategoryMenu.class)
                .withSplashTimeOut(1500)
                .withBackgroundColor(Color.parseColor("#ffffff"))
                .withLogo(R.drawable.prego);

        //Set to view
        View view = config.create();

        //Set view to content view
        setContentView(view);
    }

    }
