package com.mad.hungergames;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotificationsFragment extends Fragment {
    private View view;
    private Context context;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceBundle) {
        view = inflater.inflate(R.layout.fragment_notifications, null);

        if (getActivity() != null) {
            activity = getActivity();
            activity.setTitle(R.string.title_notifications);
            context = getActivity().getApplicationContext();
        }

        return view;
    }
}
