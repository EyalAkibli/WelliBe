package com.example.wellibe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wellibe.databinding.FragmentHomeBinding;
import com.example.wellibe.databinding.FragmentSignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        setDoctorPatientUI();

        return binding.getRoot();
    }

    protected void setDoctorPatientUI() {
        final String[] welcomeUser = {"Hello "};
        Home.docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        if (doc.getString("Job").equals(WelliBeActivity.Job.PATIENT.name())) {
                            welcomeUser[0] = welcomeUser[0] + doc.getString("Full name");
                        } else {
                            welcomeUser[0] = welcomeUser[0] + "Dr. " + doc.getString("Full name");
                        }
                        binding.tvUserName.setText(welcomeUser[0]);
                    }
                }
            }
        });
    }

}