package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.ToolBarMode.NO_BUTTONS;
import static com.example.wellibe.WelliBeActivity.ToolBarMode.ONLY_BACK;
import static com.example.wellibe.WelliBeActivity.ToolBarMode.ONLY_HAMBURGER;

import static java.util.Objects.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.example.wellibe.databinding.ActivitySigninBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class SignIn extends WelliBeActivity {

    ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private ActivitySigninBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolBarMode = NO_BUTTONS;
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initToolbar();

        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(this);
        binding.viewPager.setAdapter(viewPagerFragmentAdapter);

        new TabLayoutMediator(binding.tabsSignInSignOut, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(ViewPagerFragmentAdapter.tabTitles[1]);
                //binding.tabsSignInSignOut.selectTab(tab);
                //mainBinding.pager.setCurrentItem(tab.position, true)
            }
        }).attach();

        /*binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabsSignInSignOut.selectTab(binding.tabsSignInSignOut.getTabAt(position));
            }
        });*/


    }
}