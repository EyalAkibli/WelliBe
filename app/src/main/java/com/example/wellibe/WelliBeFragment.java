package com.example.wellibe;

import static com.example.wellibe.WelliBeActivity.mAuth;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class WelliBeFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        WelliBeActivity.connectivityFlag = checkInternetConnectivity();
        if (!WelliBeActivity.connectivityFlag && !getActivity().getLocalClassName().equals("SignIn")) {
            Toast.makeText(getActivity(), "Internet Connectivity is lost. Returning to Sign-In screen.", Toast.LENGTH_SHORT).show();
            Intent backToSignIn = new Intent(getActivity(), SignIn.class);
            getActivity().finish();
            startActivity(backToSignIn);
        }
    }

    protected boolean checkInternetConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}
