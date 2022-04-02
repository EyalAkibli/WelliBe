package com.example.wellibe;

import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.regex.Pattern;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

public class QRcodeScanner extends WelliBeActivity {

    private ScannerLiveView camera;
    private TextView scannedTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        toolBarMode = ToolBarMode.ONLY_BACK;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);
        initToolbar();
//        if (!checkPermission()) {
//            requestPermission();
//        }

        scannedTV = findViewById(R.id.idTVscanned);
        camera = (ScannerLiveView) findViewById(R.id.camview);

        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                // method is called when scanner is started
                Toast.makeText(QRcodeScanner.this, "Scanner Started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                // method is called when scanner is stopped.

            }

            @Override
            public void onScannerError(Throwable err) {
                // method is called when scanner gives some error.
                Toast.makeText(QRcodeScanner.this, "Scanner Error: " + err.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeScanned(String data) {
                String patternStr = "^[a-z0-9A-Z]{28}$";
                Pattern uidPattern = Pattern.compile(patternStr);
                if (uidPattern.matcher(data).matches()) {
                    WelliBeActivity.db.collection("Users").document(data).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    //consider adding a check that the user is actually a doctor
                                    if (task.isSuccessful()) {
                                        final DocumentSnapshot doc = task.getResult();
                                        if (doc.exists()) {
                                            String dr_name = "Dr. " + doc.getString("Full name");
                                            scannedTV.setText(dr_name);
                                            NewVisitFragment.binding.tvDoctorName.setText(dr_name);
                                            NewVisitFragment.doctor_id = data;
                                            finish();
                                        } else {
                                            NewVisitFragment.doctor_id = "";
                                            scannedTV.setText("QR code is not a known doctor's code");
                                        }
                                    }
                                }
                            });
                } else {
                    NewVisitFragment.doctor_id = "";
                    scannedTV.setText("QR code is not a doctor's code");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ZXDecoder decoder = new ZXDecoder();
        decoder.setScanAreaPercent(0.8);

        camera.setDecoder(decoder);
        camera.startScanner();
    }

    @Override
    protected void onPause() {
        //camera.stopScanner();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}