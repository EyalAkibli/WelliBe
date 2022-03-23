package com.example.wellibe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wellibe.databinding.FragmentNewVisitBinding;
import com.example.wellibe.databinding.FragmentSignInBinding;
import com.google.android.material.navigation.NavigationView;


public class NewVisitFragment extends WelliBeFragment {

    FragmentNewVisitBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewVisitBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


}