package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.mAuth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.wellibe.databinding.FragmentSignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends WelliBeFragment {

    private FragmentSignInBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        binding.btnLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnLogin.setEnabled(false);
                //hide keyboard
                if (getActivity().getCurrentFocus() != null) {
                    InputMethodManager inputManager =
                            (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                signInEmailPassword();
            }
        });
    }

    private void signInEmailPassword() {
        final String password, email;
        email = binding.etEmail.getText().toString();
        password = binding.etPassword.getText().toString();
        if(!SignIn.isValidLoginInput(getContext(), email.trim(), password.trim())){
            binding.btnLogin.setEnabled(true);
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Login successful. Welcome!",
                                               Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getContext(), Home.class);
                                binding.btnLogin.setEnabled(true);
                                getActivity().finish();
                                startActivity(i);
                            } else {
                                Toast.makeText(getActivity(), "Authentication failed. Please try again",
                                               Toast.LENGTH_LONG).show();
                            }
                            binding.btnLogin.setEnabled(true);
                        }
                    });
        }
    }

}