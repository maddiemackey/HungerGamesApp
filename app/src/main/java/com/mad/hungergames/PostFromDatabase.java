package com.mad.hungergames;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostFromDatabase extends ViewModel {
    private DatabaseReference databaseRef;
    private List<Post> posts;

    private String POSTS = "posts";
    private String TEXT = "text";
    private String IMAGE = "image";
    private String ERR = "ERR";
    private String DB_FAIL = "Failed to read value.";

    /*
     * Gets post data from Firebase and returns ObservablePost object with list of posts
     */
    public ObservablePosts getPosts(){

        // Setting up link to Firebase.
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(POSTS);

        posts = new ArrayList<>();
        final ObservablePosts postsObserved = new ObservablePosts();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post : dataSnapshot.getChildren()) {
                    String text = post.child(TEXT).getValue(String.class);
                    String image;
                    if(post.child(IMAGE).getValue(String.class) != null){
                        image = post.child(IMAGE).getValue(String.class);
                    }
                    else{image = null;}
                    Post postObj = new Post(text, image);
                    posts.add(postObj);
                }
                // Updates players in observable.
                if(postsObserved.getPosts() != null){
                    postsObserved.clear();
                }
                postsObserved.setValue(posts);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value.
                Log.w(ERR, DB_FAIL, databaseError.toException());
            }
        });
        return postsObserved;
    }

}
