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
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class KillRegistrationFragment extends Fragment {
    private Context context;
    private Activity activity;

    private RadioGroup killRegisterTypeRd;

    private String KILL_REGISTER_TYPE_KEY = "KILL_REGISTER_TYPE";
    private int storedKillRegisterTypeId;
    private int restoredKillRegisterId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(
                R.layout.fragment_kill_registration, container, false);

        if (getActivity() != null) {
            activity = getActivity();
            activity.setTitle(R.string.title_kill_registration);
            context = getActivity().getApplicationContext();
        }

        // Restores the correct fragment based on the saved instance state.
        // Loads the main menu if there is no saved instance state.
        if (savedInstanceState == null) {
            loadFragment(new KillRegistrationCodesFragment());
            RadioButton codesRb = view.findViewById(R.id.radio_codes);
            codesRb.setChecked(true);
        }
        else{
            switch (restoredKillRegisterId) {
                case 1:
                    loadFragment(new KillRegistrationCodesFragment());
                    RadioButton codesRb = view.findViewById(R.id.radio_codes);
                    codesRb.setChecked(true);
                    break;
                case 2:
                    loadFragment(new KillRegistrationQRFragment());
                    RadioButton qrRb = view.findViewById(R.id.radio_codes);
                    qrRb.setChecked(true);
                    break;
                case 3:
                    loadFragment(new KillRegistrationNFCFragment());
                    RadioButton nfcRb = view.findViewById(R.id.radio_codes);
                    nfcRb.setChecked(true);
                    break;
            }
        }

        // Buttons to control which type of kill registration.
        killRegisterTypeRd = view.findViewById(R.id.kill_register_type_radio_group);
        killRegisterTypeRd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment = null;

                switch(checkedId){
                    case R.id.radio_codes:
                        storedKillRegisterTypeId = 1;
                        fragment = new KillRegistrationCodesFragment();
                        break;
                    case R.id.radio_QR:
                        storedKillRegisterTypeId = 2;
                        fragment = new KillRegistrationQRFragment();
                        break;
                    case R.id.radio_NFC:
                        storedKillRegisterTypeId = 3;
                        fragment = new KillRegistrationNFCFragment();
                        break;
                }
                loadFragment(fragment);
            }
        });

        return view;
    }

    /*
     * loads the correct fragment into the main activity screen
     */
    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.kill_registration_fragment_container, fragment).commit();
            return true;
        }
        else{return false;}
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            restoredKillRegisterId = savedInstanceState.getInt(KILL_REGISTER_TYPE_KEY);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KILL_REGISTER_TYPE_KEY, storedKillRegisterTypeId);
    }
}
