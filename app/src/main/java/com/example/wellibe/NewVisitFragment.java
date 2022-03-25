package com.example.wellibe;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.wellibe.databinding.FragmentNewVisitBinding;
import com.example.wellibe.databinding.FragmentSignInBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Date;


public class NewVisitFragment extends WelliBeFragment {

    FragmentNewVisitBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewVisitBinding.inflate(inflater, container, false);
        binding.tvDataNTime.setText("" + DateFormat.format("dd/MM/yyyy kk:mm", System.currentTimeMillis()));

        return binding.getRoot();
    }

}