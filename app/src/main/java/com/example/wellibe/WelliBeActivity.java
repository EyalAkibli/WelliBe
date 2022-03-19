package com.example.wellibe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class WelliBeActivity extends AppCompatActivity {
    /**
     * For Initializing toolbar layout:
     * toolBarMode value options:
     * 0- no back ,no hamburger
     * 1 - only back on left
     * 2 - only hamburger on left
     */
    protected enum ToolBarMode {
        NO_BUTTONS,
        ONLY_BACK,
        ONLY_HAMBURGER
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

}