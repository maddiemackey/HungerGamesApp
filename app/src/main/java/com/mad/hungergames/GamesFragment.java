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

public class GamesFragment extends Fragment {
    private View view;
    private Context context;
    private Activity activity;

    private RecyclerView gamesRc;
    private GameFromDatabase gameVm;
    private GameAdapter adapter;
    private ObservableGames gamesObserved;
    private ProgressDialog gameProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceBundle){
        view = inflater.inflate(R.layout.fragment_games, container, false);

        if (getActivity() != null){
            activity = getActivity();
            activity.setTitle(R.string.title_games);
            context = getActivity().getApplicationContext();
        }

        // Setting up Players Recycler View.
        gamesRc = view.findViewById(R.id.games);
        gamesRc.setHasFixedSize(true);
        // Sets amount of player items per row based on screen size
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        gamesRc.setLayoutManager(new GridLayoutManager(context, Math.round(screenWidth/350)));

        gameVm = new GameFromDatabase();
        gamesObserved = gameVm.getGames();

        // Handles updating the player list to match current observable players.
        Observer updatedGamesObserved = new Observer() {
            @Override
            public void update(Observable o, Object newValue) {
                if(!gameProgress.isShowing()){
                    displayProgressDialog();
                }
                // Sets the adapter to use the observed data.
                List<Game> gameList = gamesObserved.getGames();
                if(gameList != null){
                    if(gamesObserved.getGames().size() > 0){
                        adapter = new GameAdapter(gameList, context);
                        gamesRc.setAdapter(adapter);
                    }
                }
                // Hides progress dialog once data is loaded.
                if (gameProgress.isShowing()) {
                    gameProgress.dismiss();
                }
            }
        };

        // Adds observer to watch players list
        gamesObserved.addObserver(updatedGamesObserved);
        displayProgressDialog();

        return view;
    }

    /*
    * Creates and shows the progress dialog on the current activity.
     */
    public void displayProgressDialog(){
        // Displays progress dialog while connecting to Firebase and loading data.
        gameProgress = new ProgressDialog(activity);
        gameProgress.setMessage(context.getResources().getString(R.string.downloading));
        gameProgress.show();
    }
}
