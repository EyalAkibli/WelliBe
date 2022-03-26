package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.mAuth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.wellibe.databinding.FragmentNewVisitBinding;
import com.example.wellibe.databinding.FragmentSignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firestore.v1.DocumentTransform;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class NewVisitFragment extends WelliBeFragment {

    FragmentNewVisitBinding binding;
    Map<String, Object> visit = new HashMap<>();
    Long miliSecTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewVisitBinding.inflate(inflater, container, false);

        //Toast.makeText(getActivity(), , Toast.LENGTH_LONG).show();
        miliSecTime = System.currentTimeMillis();
        binding.tvDataNTime.setText("" + DateFormat.format("dd/MM/yyyy kk:mm", System.currentTimeMillis()));
        //binding.tvDataNTime.setText("" + DateFormat.format("dd/MM/yyyy kk:mm", Calendar.getInstance(Locale.getDefault())));

        binding.fabNewVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.fabNewVisit.setEnabled(false);
                addNewVisitToDB();
            }
        });
        return binding.getRoot();
    }

    private void addNewVisitToDB() {
        visit.put("time_stamp", miliSecTime);
        visit.put("doc_name", binding.etDocName.getText().toString());
        visit.put("summary", binding.etVisitSummary.getText().toString());
        WelliBeActivity.db.collection("Users").document(mAuth.getCurrentUser().getUid()).
                collection("Visits").add(visit).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                binding.fabNewVisit.setEnabled(true);
                //reload fragment
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.drawer_layout_fragment_container,
                        new NewVisitFragment()).commit();
            }
        });
    }

}