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

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wellibe.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class SignUpFragment extends WelliBeFragment {

    private FragmentSignUpBinding binding;
    Map<String, Object> user = new HashMap<>();

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
                    if (i == 1) {
                        //user.put("Job", ((TextView) adapterView.getChildAt(0)).getText());
                        WelliBeActivity.job = WelliBeActivity.Job.PATIENT;
                    } else { // i == 2
                        WelliBeActivity.job = WelliBeActivity.Job.DOCTOR;
                    }
                    Toast.makeText(getActivity(), ((TextView) adapterView.getChildAt(0)).getText(), Toast.LENGTH_SHORT).show();
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
                    String fullName = binding.etFullName.getText().toString();
                    String email = binding.etEmail.getText().toString();
                    String password = binding.etPass.getText().toString();
                    String confirmPass = binding.etConfirmPass.getText().toString();
                    if (WelliBeActivity.job == WelliBeActivity.Job.NOT_SELECTED) {
                        Toast.makeText(getActivity(),
                                "Please choose between patient and doctor", Toast.LENGTH_SHORT).show();
                    } else if (email.trim().isEmpty() || password.trim().isEmpty() || fullName.trim().isEmpty()
                            || confirmPass.trim().isEmpty()) {
                        Toast.makeText(getActivity(),
                                "Sign up has failed. Some fields are empty", Toast.LENGTH_LONG).show();
                    } else if (!password.equals(confirmPass)) {
                        Toast.makeText(getActivity(),
                                "'Password Confirmation' Doesn't Match 'Password'.", Toast.LENGTH_LONG).show();
                    } else {
                        createAccount(fullName, email, password);
                        binding.btnRegister.setEnabled(false);
                    }
                }
            }
        });

        String signInOffer = getResources().getString(R.string.sign_in_offer);
        int start = signInOffer.indexOf("S");
        int end = signInOffer.indexOf(".");
        binding.tvSignInOffer.setMovementMethod(LinkMovementMethod.getInstance());
        //binding.tvSignInOffer.setText(signInOffer, TextView.BufferType.SPANNABLE);
        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                SignIn.transitionFrags(0);
            }
        };
        ((Spannable) binding.tvSignInOffer.getText()).setSpan(myClickableSpan, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void createAccount(final String fullName, final String email, final String password) {
        if (!SignIn.isValidLoginInput(getContext(), email, password)) {
            binding.btnRegister.setEnabled(true);
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user.put("Full name", fullName);
                    user.put("Email", email);
                    user.put("Job", WelliBeActivity.job.name());
                    user.put("Hearts received", 0);
                    String userId = mAuth.getCurrentUser().getUid();
                    WelliBeActivity.db.collection("Users").document(userId).set(user).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Registration successful. Welcome!",
                                            Toast.LENGTH_LONG).show();
                                    binding.btnRegister.setEnabled(true);
                                    ((SignIn) getActivity()).onResume();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mAuth.getCurrentUser().reauthenticate(EmailAuthProvider.getCredential(email, password)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mAuth.getCurrentUser().delete();
                                            Toast.makeText(getContext(), "Unexpected error has occurred. Please restart the application.",
                                                    Toast.LENGTH_LONG).show();
                                            getActivity().finishAffinity();
                                        }
                                    });
                                }
                            });
                    //addData(userName, email);

                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getContext(), "This Email address already Exists.",
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