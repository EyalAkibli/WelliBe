package com.example.wellibe;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;
import static com.example.wellibe.WelliBeActivity.mAuth;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wellibe.databinding.FragmentNewVisitBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NewVisitFragment extends WelliBeFragment {

    public static FragmentNewVisitBinding binding;
    Map<String, Object> visit = new HashMap<>();
    Long milliSecTime;
    boolean clicked_scanner_yet = false;
    String doctor_name = "";
    static String doctor_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewVisitBinding.inflate(inflater, container, false);
        milliSecTime = System.currentTimeMillis();
        binding.tvDataNTime.setText("" + DateFormat.format("dd/MM/yyyy kk:mm", System.currentTimeMillis()));

        binding.fabNewVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.fabNewVisit.setEnabled(false);
                if (binding.etVisitSummary.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Visit summary is empty, please fill in the text box", Toast.LENGTH_SHORT).show();
                    binding.fabNewVisit.setEnabled(true);
                } else {
                    addNewVisitToDB();
                }
            }
        });
        binding.ivCodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    // You can use the API that requires the permission.
                    clicked_scanner_yet = false;
                    binding.tvDoctorName.setText(getResources().getString(R.string.scan_dr_qr_code));
                    Intent i = new Intent(getContext(), QRcodeScanner.class);
                    startActivity(i);
                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    String[] permissionsNeeded = {CAMERA, VIBRATE};
                    requestPermissionLauncher.launch(permissionsNeeded);
                }
                if (!shouldShowRequestPermissionRationale(CAMERA) && clicked_scanner_yet) {
                    // In an educational UI, explain to the user why your app requires this
                    // permission for a specific feature to behave as expected. In this UI,
                    // include a "cancel" or "no thanks" button that allows the user to
                    // continue using your app without granting the permission.
                    Snackbar snackbar = Snackbar
                            .make(binding.newVisitLayout, "Can't scan without camera permission", Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.action_settings), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            });
                    snackbar.show();
                }
                clicked_scanner_yet = true;
            }
        });
        return binding.getRoot();
    }

    private void addNewVisitToDB() {
        visit.put("time_stamp", milliSecTime);
        visit.put("summary", binding.etVisitSummary.getText().toString());
        visit.put("loved", false);
        visit.put("doc_id", doctor_id);
        String doc_name = binding.tvDoctorName.getText().toString();
        if (doc_name.equals(getResources().getString(R.string.scan_dr_qr_code)) || doc_name.equals("")) {
            visit.put("doc_name", "Unknown");
        } else {
            visit.put("doc_name", binding.tvDoctorName.getText().toString());
        }
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

    private boolean checkPermission() {
        int camera_permission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA);
        int vibrate_permission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), VIBRATE);
        return camera_permission == PackageManager.PERMISSION_GRANTED && vibrate_permission == PackageManager.PERMISSION_GRANTED;
    }


    private ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    if (result.get(CAMERA)) {
                        binding.tvDoctorName.setText(getResources().getString(R.string.scan_dr_qr_code));
                        Intent i = new Intent(getContext(), QRcodeScanner.class);
                        startActivity(i);
                    } else if (shouldShowRequestPermissionRationale(CAMERA)) {
                        Toast.makeText(getActivity(), "Scan is possible only with camera permission", Toast.LENGTH_SHORT).show();
                    }
                }
            });




}