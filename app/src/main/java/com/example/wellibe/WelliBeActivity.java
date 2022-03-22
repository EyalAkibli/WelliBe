package com.example.wellibe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.protobuf.NullValue;

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

    ToolBarMode toolBarMode;
    public static FirebaseAuth mAuth;
    public static boolean connectivityFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }

    @Override
    protected void onResume() {
        super.onResume();
        connectivityFlag = checkInternetConnectivity();
        if (!connectivityFlag && !this.getLocalClassName().equals("SignIn")) {
            Toast.makeText(getApplicationContext(), "Internet Connectivity is lost. Returning to Sign-In screen.", Toast.LENGTH_SHORT).show();
            Intent backToSignIn = new Intent(this, SignIn.class);
            finish();
            startActivity(backToSignIn);
        }
    }

    public void initToolbar() {
        Toolbar toolbar = findViewById(R.id.tool_bar_back_menu);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        switch (this.toolBarMode) {
            case NO_BUTTONS:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            case ONLY_BACK:
                if (drawerLayout != null) {
                    drawerLayout.setDrawerLockMode(DrawerLayout. LOCK_MODE_LOCKED_CLOSED);
                }
                break;
            case ONLY_HAMBURGER:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                        R.string.open_drawer,R.string.close_drawer);

                drawer.addDrawerListener(drawerToggle);
                drawerToggle.syncState();
                break;
            default: //ONLY_BACK
                break;
        }
    }
    protected void setupDrawerContentActions(@NonNull NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:

                    break;
                case R.id.add_new_visit:

                    break;
                case R.id.view_my_visits:

                    break;
                case R.id.help:
                    Toast.makeText(getApplicationContext(), "Not yet Supported.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.logout:
                    Toast.makeText(getApplicationContext(), "Logging out..", Toast.LENGTH_SHORT).show();
                    if (mAuth.getCurrentUser() != null) {
                        mAuth.signOut();
                    }
                    Intent backToSignIn = new Intent(getApplicationContext(), SignIn.class);
                    finish();
                    startActivity(backToSignIn);
                    break;
            }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawerLeft = findViewById(R.id.drawer_layout);
        if (drawerLeft != null && drawerLeft.isDrawerOpen(GravityCompat.START)) {
            drawerLeft.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean checkInternetConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}