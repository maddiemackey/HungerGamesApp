package com.mad.hungergames;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;

public class KillRegistrationNFCFragment extends Fragment {
    private Context context;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_kill_registration_nfc, container, false);

        // Set up buttons and messages for NFC code register.
        if (getActivity() != null){
            activity = getActivity();
            context = getActivity().getApplicationContext();
        }

        return view;
    }
}
