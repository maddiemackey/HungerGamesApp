package com.mad.hungergames;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class PlayersFragment extends Fragment {
    private View view;
    private Context context;
    private Activity activity;

    private PlayerFromDatabase playerVm;
    private RecyclerView playersRc;
    private RecyclerView.Adapter adapter;
    private ObservablePlayers playersObserved;
    private ProgressDialog playerProgress;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.fragment_players, container, false);

        if (getActivity() != null){
            activity = getActivity();
            activity.setTitle(R.string.title_players);
            context = getActivity().getApplicationContext();
        }

        // Setting up Players Recycler View.
        playersRc = view.findViewById(R.id.players);
        playersRc.setHasFixedSize(true);
        // Sets amount of player items per row based on screen size
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        playersRc.setLayoutManager(new GridLayoutManager(context, Math.round(screenWidth/350)));

        playerVm = new PlayerFromDatabase();
        playersObserved = playerVm.getPlayers();

        // Handles updating the player list to match current observable players.
        Observer updatedPlayersObserved = new Observer() {
            @Override
            public void update(Observable o, Object newValue) {
                if(!playerProgress.isShowing()){
                    displayProgressDialog();
                }
                // Sets the adapter to use the observed data.
                List<Player> playerList = playersObserved.getPlayers();
                if(playerList != null){
                    if(playersObserved.getPlayers().size() > 0){
                        adapter = new PlayerAdapter(playerList, context);
                        playersRc.setAdapter(adapter);
                    }
                }
                // Hides progress dialog once data is loaded.
                if (playerProgress.isShowing()) {
                    playerProgress.dismiss();
                }
            }
        };

        // Adds observer to watch players list
        playersObserved.addObserver(updatedPlayersObserved);
        displayProgressDialog();

        return view;
    }

    /*
    * Creates and shows a dialog progress on the current activity.
     */
    public void displayProgressDialog(){
        // Displays progress dialog while connecting to Firebase and loading data.
        playerProgress = new ProgressDialog(activity);
        playerProgress.setMessage(context.getResources().getString(R.string.downloading));
        playerProgress.show();
    }
}
