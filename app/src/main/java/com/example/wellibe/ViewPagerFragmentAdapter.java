package com.example.wellibe;

import android.content.res.loader.ResourcesProvider;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    String str1;
    String str2;
    public static String[] tabTitles;

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        str1 = fragmentActivity.getResources().getString(R.string.sign_in);
        str2 = fragmentActivity.getResources().getString(R.string.sign_up);
        tabTitles = new String[]{str1, str2};
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new SignInFragment();
            case 1:
                return new SignUpFragment();
        }
        return new SignInFragment();
    }

    @Override
    public int getItemCount() {
        return tabTitles.length;
    }
}
