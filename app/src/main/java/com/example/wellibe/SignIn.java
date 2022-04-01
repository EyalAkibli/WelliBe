package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.ToolBarMode.NO_BUTTONS;

import androidx.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.wellibe.databinding.ActivitySigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseUser;


public class SignIn extends WelliBeActivity {

    public ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private static ActivitySigninBinding binding;

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
                tab.setText(ViewPagerFragmentAdapter.tabTitles[position]);
            }
        }).attach();
    }

    @Override
    protected void onResume() {
        connectivityFlag = checkInternetConnectivity();
        if (connectivityFlag) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            CheckUserSignedIn();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        super.onResume();
    }

    public static boolean isValidLoginInput(Context context, String email, String password) {
        if (email.isEmpty()) {
            Toast.makeText(context, "Email is required. Please try again",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(context, "Password is required. Please try again",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Invalid email. Please try again",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(context, "Minimum length of password is 6 chars", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void CheckUserSignedIn() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        //User has been disabled, deleted or login credentials are no longer valid,
                        //send to Login screen
                        if (!getLocalClassName().equals("SignIn")) {
                            Intent i = new Intent(getApplicationContext(), SignIn.class);
                            finish();
                            startActivity(i);
                        }
                    } else { //task was successful
                        Intent i = new Intent(getApplicationContext(), Home.class);
                        finish();
                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                }
            });
        }
    }

    public static void transitionFrags(int i) {
        binding.viewPager.setCurrentItem(i);
    }
}

