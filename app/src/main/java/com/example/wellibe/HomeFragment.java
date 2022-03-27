package com.example.wellibe;

import static android.content.Context.WINDOW_SERVICE;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.wellibe.databinding.FragmentHomeBinding;
import com.example.wellibe.databinding.FragmentSignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class HomeFragment extends WelliBeFragment {

    FragmentHomeBinding binding;
    QRGEncoder qrgEncoder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        setDoctorPatientUI();
    }

    protected void setDoctorPatientUI() {
        DocumentReference docRef = WelliBeActivity.db.collection("Users").document(WelliBeActivity.mAuth.getCurrentUser().getUid());
        final String[] welcomeUser = {"Hello "};
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        if (doc.getString("Job").equals(WelliBeActivity.Job.PATIENT.name())) {
                            binding.ivQrCode.setEnabled(false);
                            welcomeUser[0] = welcomeUser[0] + doc.getString("Full name");
                        } else {
                            welcomeUser[0] = welcomeUser[0] + "Dr. " + doc.getString("Full name");
                            binding.ivQrCode.setEnabled(true);
                            WindowManager manager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);

                            // initializing a variable for default display.
                            Display display = manager.getDefaultDisplay();

                            // creating a variable for point which
                            // is to be displayed in QR Code.
                            Point point = new Point();
                            display.getSize(point);

                            // getting width and
                            // height of a point
                            int width = point.x;
                            int height = point.y;

                            // generating dimension from width and height.
                            int dimen = width < height ? width : height;
                            dimen = dimen * 3 / 4;
                            qrgEncoder = new QRGEncoder(WelliBeActivity.mAuth.getCurrentUser().getUid(),
                                    null, QRGContents.Type.TEXT, dimen);
                            qrgEncoder.setColorWhite(getResources().getColor(R.color.gray));
                            binding.ivQrCode.setImageBitmap(qrgEncoder.getBitmap());
                        }
                        binding.tvUserName.setText(welcomeUser[0]);
                    }
                }
            }
        });
    }

}