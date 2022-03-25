package com.example.wellibe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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

    public enum Job {
        Doctor,
        Patient
    }

    static Job job;
    ToolBarMode toolBarMode;
    public static FirebaseAuth mAuth;
    FirebaseFirestore db;
    public static boolean connectivityFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
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
                    Fragment myFragment = getSupportFragmentManager().findFragmentById(R.id.drawer_layout_fragment_container);
                    assert myFragment != null;
                    if (myFragment.getClass() != HomeFragment.class && myFragment.isVisible()) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout_fragment_container,
                                new HomeFragment(), "start activity fragment").commit();
                    }
                    break;
                case R.id.add_new_visit:
                    getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout_fragment_container,
                                                                           new NewVisitFragment()).commit();
                    break;
                case R.id.view_my_visits:
                    getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout_fragment_container,
                                                                           new MyVisitsFragment()).commit();
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
                final DrawerLayout drawerLeft = findViewById(R.id.drawer_layout);
                drawerLeft.closeDrawers();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

}