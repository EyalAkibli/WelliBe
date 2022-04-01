package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.db;
import static com.example.wellibe.WelliBeActivity.mAuth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wellibe.databinding.FragmentMyVisitsBinding;
import com.example.wellibe.databinding.FragmentNewVisitBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyVisitsFragment extends WelliBeFragment {

    FragmentMyVisitsBinding binding;
    private CollectionReference visitsRef = db.collection("Users").document(mAuth.getUid()).collection("Visits");
    protected VisitsRecyclerAdapter visitsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyVisitsBinding.inflate(inflater, container, false);
        Query query = visitsRef.orderBy("time_stamp", Query.Direction.DESCENDING);
        setUpRecyclerView(binding.recyclerVisits, query);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        visitsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (visitsAdapter != null) {
            visitsAdapter.stopListening();
        }
    }

    protected void setUpRecyclerView(RecyclerView recView, Query query) {
        FirestoreRecyclerOptions<Visit> options = new FirestoreRecyclerOptions.Builder<Visit>()
                .setQuery(query, Visit.class).build();

        visitsAdapter = new VisitsRecyclerAdapter(getContext(), options);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        recView.setAdapter(visitsAdapter);
    }
}