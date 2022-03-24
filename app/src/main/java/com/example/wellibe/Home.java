package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.ToolBarMode.NO_BUTTONS;
import static com.example.wellibe.WelliBeActivity.ToolBarMode.ONLY_BACK;
import static com.example.wellibe.WelliBeActivity.ToolBarMode.ONLY_HAMBURGER;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.wellibe.databinding.ActivityHomeBinding;
import com.example.wellibe.databinding.ActivitySigninBinding;
import com.google.android.material.navigation.NavigationView;

public class Home extends WelliBeActivity {

    NavigationView navigationView;
    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolBarMode = ONLY_HAMBURGER;
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.drawer_layout_fragment_container, new HomeFragment()).commit();
            //binding.navigationView.setCheckedItem(R.id.home);
        }
        initToolbar();
        setupDrawerContentActions(findViewById(R.id.navigation_view));

        setDoctorPatientUI(WelliBeActivity.job);
    }


    protected void setDoctorPatientUI(WelliBeActivity.Job j) {
        //if (j == Job.Patient) {

        //} else { //j == Job.Doctor
            //binding.navigationView.getMenu().findItem(R.id.add_new_visit).setVisible(false);
            //binding.navigationView.getMenu().findItem(R.id.view_my_visits).setVisible(false);
        //}
    }


}