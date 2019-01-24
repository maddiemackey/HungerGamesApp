package com.mad.hungergames;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class HomeFragment extends Fragment {
    private Context context;
    private Activity activity;

    private ProgressDialog postProgress;
    private PostAdapter adapter;
    private RecyclerView postsRc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceBundle){
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (getActivity() != null){
            activity = getActivity();
            activity.setTitle(R.string.title_home);
            context = getActivity().getApplicationContext();
        }

        // Setting up Players Recycler View.
        postsRc = view.findViewById(R.id.posts);
        postsRc.setHasFixedSize(true);
        // Sets amount of player items per row based on screen size
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        postsRc.setLayoutManager(new LinearLayoutManager
                (postsRc.getContext(),LinearLayoutManager.VERTICAL, false));

        PostFromDatabase postVm = new PostFromDatabase();
        final ObservablePosts postsObserved = postVm.getPosts();

        // Handles updating the player list to match current observable players.
        Observer updatedPostsObserved = new Observer() {
            @Override
            public void update(Observable o, Object newValue) {
                if(!postProgress.isShowing()){
                    displayProgressDialog();
                }
                // Sets the adapter to use the observed data.
                List<Post> posts = postsObserved.getPosts();
                if(posts != null){
                    if(postsObserved.getPosts().size() > 0){
                        adapter = new PostAdapter(posts, context);
                        postsRc.setAdapter(adapter);
                    }
                }
                // Hides progress dialog once data is loaded.
                if (postProgress.isShowing()) {
                    postProgress.dismiss();
                }
            }
        };

        // Adds observer to watch players list
        postsObserved.addObserver(updatedPostsObserved);
        displayProgressDialog();

        return view;
    }

    public void displayProgressDialog(){
        // Displays progress dialog while connecting to Firebase and loading data.
        postProgress = new ProgressDialog(activity);
        postProgress.setMessage(context.getResources().getString(R.string.downloading));
        postProgress.show();
    }
}
