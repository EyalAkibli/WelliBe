package com.example.wellibe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class SignIn extends WelliBeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolBarMode = ToolBarMode.NO_BUTTONS;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }
}