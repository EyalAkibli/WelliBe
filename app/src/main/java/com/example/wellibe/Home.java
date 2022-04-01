package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.ToolBarMode.ONLY_HAMBURGER;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.wellibe.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class Home extends WelliBeActivity {

    static ActivityHomeBinding binding;

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
        setDoctorPatientDrawerUI();

    }

    protected void setDoctorPatientDrawerUI() {
        DocumentReference docRef = WelliBeActivity.db.collection("Users").document(WelliBeActivity.mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        if (doc.getString("Job").equals(Job.DOCTOR.name())) {
                            binding.navigationView.getMenu().findItem(R.id.add_new_visit).setVisible(false);
                            binding.navigationView.getMenu().findItem(R.id.view_my_visits).setVisible(false);
                        }
                    }
                }
            }
        });
    }


}