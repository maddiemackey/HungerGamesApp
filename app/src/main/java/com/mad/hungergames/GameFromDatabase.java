package com.mad.hungergames;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameFromDatabase extends ViewModel {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRef;
    private List<Game> gamesList;
    private ObservableGames gamesObserved;
    private Player playerObj;

    private String GAMES = "games";
    private String NAME = "name";
    private String COMPETITORS = "competitors";
    private String ALIVE = "alive";
    private String KILLS = "kills";
    private String KILLER_ID = "killer_id";
    private String VICTIM_ID = "victim_id";
    private String TIME_OF_DEATH = "time_of_death";
    private String REG_ACTIVE = "registration_active";
    private String START_TIME = "start_time";
    private String END_TIME = "end_time";
    private String WINNER_ID = "winner_id";
    private String ERR = "ERR";
    private String DATABASE_FAIL_MESSAGE = "Failed to read value.";
    private String PLAYERS_PATH = "players/";


    /*
    * Gets game data from Firebase database and returns ObservableGames object with list of games
     */
    public ObservableGames getGames(){

        // Setting up link to Firebase.
        databaseRef = database.getReference(GAMES);

        gamesList = new ArrayList<>();
        gamesObserved = new ObservableGames();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot game : dataSnapshot.getChildren()) {
                    String id = game.getKey();
                    String name = game.child(NAME).getValue(String.class);

                    // Make list of Competitors
                    ArrayList<Competitor> competitors = new ArrayList<Competitor>();
                    if(game.child(COMPETITORS).getValue(Object.class) != null){
                        for(DataSnapshot competitor : (game.child(COMPETITORS)).getChildren()){
                            String playerId = competitor.getKey();
                            Boolean status = competitor.child(ALIVE).getValue(Boolean.class);
                            competitors.add(makeCompetitorFromId(playerId, status));
                        }
                    }

                    // Make list of Kills
                    ArrayList<Kill> kills = new ArrayList<Kill>();
                    if(game.child(KILLS).getValue(Object.class) != null){
                        for(DataSnapshot kill : (game.child(KILLS)).getChildren()){
                            String killerId = kill.child(KILLER_ID).getValue(String.class);
                            String victimId = kill.child(VICTIM_ID).getValue(String.class);
                            Long unixTimeOfDeath = kill.child("time_of_death").getValue(Long.class);
                            Date timeOfDeath = new Date();
                            timeOfDeath.setTime(unixTimeOfDeath*1000);
                            kills.add(new Kill(killerId, victimId, timeOfDeath));
                        }
                    }

                    Boolean registrationActive = game.child(REG_ACTIVE)
                            .getValue(Boolean.class);

                    // Gets UNIX start time from Firebase and converts to Date objects
                    Long unixStartTime;
                    Date startTime = new Date();
                    if(game.child(START_TIME).getValue(Long.class) != null){
                        unixStartTime = game.child(START_TIME).getValue(Long.class);
                        startTime.setTime(unixStartTime*1000);
                    } else{startTime = null;}
                    // Gets UNIX end time from Firebase and converts to Date objects
                    Long unixEndTime;
                    Date endTime = new Date();
                    if(game.child(END_TIME).getValue(Long.class) != null){
                        unixEndTime = game.child(END_TIME).getValue(Long.class);
                        endTime.setTime(unixEndTime*1000);
                    } else{endTime = null;}
                    String winnerId;
                    if(game.child(WINNER_ID).getValue(String.class) != null){
                        winnerId = game.child(WINNER_ID).getValue(String.class);
                    } else{winnerId = null;}

                    Game gameObj = new Game(id, name, competitors, kills, registrationActive,
                            startTime, endTime, winnerId);
                    gamesList.add(gameObj);
                }
                // Updates games in observable.
                if(gamesObserved.getGames() != null){
                    gamesObserved.clear();
                }
                gamesObserved.setValue(gamesList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value.
                Log.w(ERR, DATABASE_FAIL_MESSAGE, databaseError.toException());
            }
        });
        return gamesObserved;
    }

    private Competitor makeCompetitorFromId(String playerId, Boolean status){
        final Boolean alive = status;
        final String id = playerId;
        DatabaseReference playerRef = database.getReference(PLAYERS_PATH+playerId);
        Competitor competitor;
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot player : dataSnapshot.getChildren()) {
                    String key = player.getKey();
                    String name = null;
                    String avatar = null;
                    String role = null;
                    String password = null;
                    String killcode = null;

                    if(key != null){
                        switch (key) {
                            case "avatar":
                                avatar = player.getValue(String.class);
                                break;
                            case "killcode":
                                killcode = player.getValue(String.class);
                                break;
                            case "name":
                                name = player.getValue(String.class);
                                break;
                            case "password":
                                password = player.getValue(String.class);
                                break;
                            case "role":
                                role = player.getValue(String.class);
                                break;
                        }
                    }
                    playerObj = new Player(id, name, avatar, password, role, killcode);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value.
                Log.w(ERR, DATABASE_FAIL_MESSAGE, databaseError.toException());
            }
        });
        competitor = new Competitor(playerId, playerObj, alive);
        return competitor;
    }
}
