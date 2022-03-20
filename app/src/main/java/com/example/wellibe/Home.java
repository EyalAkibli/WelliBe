package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.ToolBarMode.NO_BUTTONS;
import static com.example.wellibe.WelliBeActivity.ToolBarMode.ONLY_BACK;
import static com.example.wellibe.WelliBeActivity.ToolBarMode.ONLY_HAMBURGER;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class Home extends WelliBeActivity {

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolBarMode = ONLY_HAMBURGER;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolbar();

        setupDrawerContentActions(findViewById(R.id.navigation_view));

    }
}