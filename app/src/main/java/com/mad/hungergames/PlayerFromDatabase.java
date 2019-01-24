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

public class PlayerFromDatabase extends ViewModel {
    private DatabaseReference databaseRef;
    private List<Player> playersList;

    private String PLAYERS = "players";
    private String NAME = "name";
    private String AVATAR = "avatar";
    private String KILLCODE = "killcode";
    private String PASSWORD = "password";
    private String ROLE = "role";
    private String ERR = "ERR";
    private String DB_FAIL = "Failed to read value from database.";



    /*
     * Gets player data from Firebase and returns ObservablePlayers object with list of players
     */
    public ObservablePlayers getPlayers(){

        // Setting up link to Firebase.
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference(PLAYERS);

        playersList = new ArrayList<>();
        final ObservablePlayers playersObserved = new ObservablePlayers();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot player : dataSnapshot.getChildren()) {
                    String id = player.getKey();
                    String name = player.child(NAME).getValue(String.class);
                    String avatar = player.child(AVATAR).getValue(String.class);
                    String role = player.child(ROLE).getValue(String.class);
                    String password = player.child(PASSWORD).getValue(String.class);
                    String killcode;
                    if(player.child(KILLCODE).getValue(String.class) != null){
                        killcode = player.child(KILLCODE).getValue(String.class);
                    }
                    else{killcode = null;}
                    Player playerObj = new Player(id, name, avatar, password, role, killcode);
                    playersList.add(playerObj);
                }
                // Updates players in observable.
                if(playersObserved.getPlayers() != null){
                    playersObserved.clear();
                }
                playersObserved.setValue(playersList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value.
                Log.w(ERR, DB_FAIL, databaseError.toException());
            }
        });
        return playersObserved;
    }

}
