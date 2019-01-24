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
import android.widget.Button;
import android.widget.EditText;

public class KillRegistrationCodesFragment extends Fragment {
    private Context context;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_kill_registration_codes, container, false);

        // Set up buttons and messages for QR code register.
        final EditText usernameEt = view.findViewById(R.id.name);
        final EditText killcodeEt = view.findViewById(R.id.killcode);
        final Button confirmKillBtn = view.findViewById(R.id.button_confirm_kill);
        if (getActivity() != null){
            activity = getActivity();
            context = getActivity().getApplicationContext();
        }
        confirmKillBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String username = usernameEt.getText().toString();
                // check that username is a player name of a competitor in current game
                String killcode = killcodeEt.getText().toString();
                // check that killcode is a player killcode of a competitor in current game

                KillRegistration killRegistration =
                        new KillRegistration(username, killcode, activity, context);
                killRegistration.registerKill();
            }
        });

        return view;
    }
}
