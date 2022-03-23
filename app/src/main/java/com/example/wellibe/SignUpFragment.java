package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.connectivityFlag;
import static com.example.wellibe.WelliBeActivity.mAuth;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wellibe.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;


public class SignUpFragment extends WelliBeFragment {

    private FragmentSignUpBinding binding;
    protected String job;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.spinnerDocPat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    ((TextView) adapterView.getChildAt(0)).setTextColor(getResources().getColor(R.color.colorSecondaryLight));
                } else {
                    job = (String) binding.spinnerDocPat.getItemAtPosition(i);
                    Toast.makeText(getActivity(), job, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectivityFlag) {
                    Toast.makeText(getActivity(), "Make sure you have an Internet Connection and then restart PubLeague.", Toast.LENGTH_LONG).show();
                } else {
                    String userName = binding.etUserName.getText().toString();
                    String email = binding.etEmail.getText().toString();
                    String password = binding.etPass.getText().toString();
                    String confirmPass = binding.etConfirmPass.getText().toString();
                    if (email.trim().isEmpty() || password.trim().isEmpty() || userName.trim().isEmpty()
                            || confirmPass.trim().isEmpty()) {
                        Toast.makeText(getActivity(),
                                "Sign up has failed. Some fields are empty", Toast.LENGTH_LONG).show();
                    } else if (!password.equals(confirmPass)) {
                        Toast.makeText(getActivity(),
                                "'Password Confirmation' Doesn't Match 'Password'.", Toast.LENGTH_LONG).show();
                    } else {
                        createAccount(userName, email, password);
                    }
                }
            }
        });
    }

    private void createAccount(final String userName, final String email, final String password) {
        if (!SignIn.isValidLoginInput(getContext(), email, password)) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Registration successful. Please sign-in.",
                            Toast.LENGTH_LONG).show();
                    //addData(userName, email);
                    ((SignIn) getActivity()).onResume();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getContext(), "The Email address is already in use by another account.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Authentication failed. Please try again",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}