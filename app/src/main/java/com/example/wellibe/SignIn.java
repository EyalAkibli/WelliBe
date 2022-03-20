package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.ToolBarMode.NO_BUTTONS;
import static com.example.wellibe.WelliBeActivity.ToolBarMode.ONLY_BACK;
import static com.example.wellibe.WelliBeActivity.ToolBarMode.ONLY_HAMBURGER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class SignIn extends WelliBeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolBarMode = NO_BUTTONS;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initToolbar();
    }
}