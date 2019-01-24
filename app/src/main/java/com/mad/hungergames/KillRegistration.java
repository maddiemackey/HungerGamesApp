package com.mad.hungergames;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
* Class for handling kill registration.
* Takes in the username and killcode inputted, as well as context and activity.
* Handles logic of kills. Handles connection to database.
* Returns feedback through Toasts that indicate why/why not the kill was entered to the database.
 */
public class KillRegistration extends AppCompatActivity {
    private String killerName;
    private String victimKillcode;

    private Activity activity;
    private Context context;

    private ProgressDialog registerKillProgress;
    private String gameId;

    private String GAMES = "games";
    private String START_TIME = "start_time";
    private String END_TIME = "end_time";
    private String USERNAME_ERR = "Please enter a valid username.";
    private String KILLCODE_ERR = "Please enter a valid killcode.";
    private String KILL_CONFIRMED = "Kill confirmed";
    private String VICTIM_ERR = "Victim must be alive in current game.";
    private String KILLER_ERR = "Killer must be alive in current game.";
    private String GAME_ERR = "No games in progress.";
    private String ERR = "ERR";
    private String DB_FAIL = "Database failure.";
    private String PLAYERS = "players";
    private String NAME = "name";
    private String KILLCODE = "killcode";
    private String GAMES_PATH = "games/";
    private String COMP_PATH = "/competitors";
    private String COMP_PATH_DOUBLE = "/competitors/";
    private String ALIVE = "alive";
    private String KILLS_PATH = "/kills";
    private String ALIVE_PATH = "/alive";
    private String KILLER_ID_PATH = "/killer_id";
    private String VICTIM_ID_PATH = "/victim_id";
    private String TIME_OF_DEATH_PATH = "/time_of_death";


    public KillRegistration(String killerName, String victimKillcode, Activity activity,
                            Context context) {
        this.killerName = killerName;
        this.victimKillcode = victimKillcode;

        this.activity = activity;
        this.context = context;
    }

    /*
    * Handles the logic behind registering a kill.
     */
    public void registerKill(){
        displayProgressDialog();

        // Setting up link to Firebase.
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("");

        gameId = null;

        // Gets snapshot of database
        getCurrentGameData(databaseRef.getRoot(), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot game : dataSnapshot.child(GAMES).getChildren()) {
                    // Checks if game has started.
                    if (game.child(START_TIME).getValue(Long.class) != null) {
                        // ...and hasn't ended (therefore, current).
                        if (game.child(END_TIME).getValue(Long.class) == null) {

                            // Get ID of current game.
                            gameId = game.getKey();

                            // Get killer and victim IDs.
                            String killerId = getPlayerIdFromName(killerName, dataSnapshot);
                            if(killerId == null){
                                Toast.makeText(context, USERNAME_ERR, Toast.LENGTH_LONG).show();
                            }
                            String victimId = getPlayerIdFromKillcode(victimKillcode, dataSnapshot);
                            if(killerId == null){
                                Toast.makeText(context, KILLCODE_ERR, Toast.LENGTH_LONG).show();
                            }

                            // If the killer and victim are both alive in the current game.
                            if(isCompetitorAlive(killerId, gameId, dataSnapshot)){
                                if(isCompetitorAlive(victimId, gameId, dataSnapshot)){
                                    // Write kill to the database
                                    writeKillToDatabase(gameId, killerId, victimId);
                                    Toast.makeText(context, KILL_CONFIRMED,
                                            Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(context, VICTIM_ERR, Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(context, KILLER_ERR, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                if(gameId == null){
                    Toast.makeText(context, GAME_ERR, Toast.LENGTH_LONG).show();
                }
                // Hides progress dialog once data is loaded.
                if (registerKillProgress.isShowing()) {
                    registerKillProgress.dismiss();
                }
            }
            @Override
            public void onStart() {
            }

            @Override
            public void onFailure() {
                Log.e(ERR, DB_FAIL);
            }
        });
    }


    /*
     * Creates and shows the progress dialog on the current activity.
     */
    public void displayProgressDialog(){
        // Displays progress dialog while connecting to Firebase and loading data.
        registerKillProgress = new ProgressDialog(activity);
        registerKillProgress.setMessage(context.getResources().getString(R.string.downloading));
        registerKillProgress.show();
    }

    /*
    * Sets listener and handles retrieving the current data from the database asynchronously.
     */
    public void getCurrentGameData(DatabaseReference ref, final OnGetDataListener listener) {
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                listener.onFailure();
            }
        });
    }

    /*
    * Interface for connecting to the database.
     */
    public interface OnGetDataListener {
        void onSuccess(DataSnapshot dataSnapshot);
        void onStart();
        void onFailure();
    }

    /*
    * Returns the player ID from the player name
     */
    private String getPlayerIdFromName(String name, DataSnapshot dataSnapshot){
        String playerId = null;
        for (DataSnapshot player : dataSnapshot.child(PLAYERS).getChildren()) {
            String checkName = player.child(NAME).getValue(String.class);
            if(checkName.equals(name)){
                playerId = player.getKey();
            }
        }
        return  playerId;
    }

    /*
     * Returns the player ID from the player killcode.
     */
    private String getPlayerIdFromKillcode(String killcode, DataSnapshot dataSnapshot){
        String playerId = null;
        for (DataSnapshot player : dataSnapshot.child(PLAYERS).getChildren()) {
            if(player.child(KILLCODE).getValue(String.class) != null){
                String checkKillcode = player.child(KILLCODE).getValue(String.class);
                if(checkKillcode.equals(killcode)){
                    playerId = player.getKey();
                }
            }
        }
        return  playerId;
    }

    /*
     * Checks if player is an alive competitor in game by ID.
     * Returns a Boolean indicating this.
     */
    private Boolean isCompetitorAlive(String playerId, String gameId, DataSnapshot dataSnapshot){
        Boolean alive = false;
        for (DataSnapshot competitor : dataSnapshot.child(GAMES_PATH+gameId+COMP_PATH)
                .getChildren()) {
            if(competitor.getKey().equals(playerId)){
                if(competitor.child(ALIVE).getValue(Boolean.class)){
                    alive = true;
                }

            }

        }
        return alive;
    }

    /*
     * Writes the kill into the database for the game, and changes the competitor to be dead.
     */
    private void writeKillToDatabase(String currentGameId, String killerId, String victimId){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference killsRef = database.getReference(GAMES_PATH+currentGameId+KILLS_PATH);
        DatabaseReference newKill = killsRef.push();
        // Write new killer.
        DatabaseReference newKillKillerRef = newKill.child(KILLER_ID_PATH);
        newKillKillerRef.setValue(killerId);
        // Write new victim.
        DatabaseReference newKillVictimRef = newKill.child(VICTIM_ID_PATH);
        newKillVictimRef.setValue(victimId);
        // Write new time of death (UNIX).
        DatabaseReference newKillTimeRef = newKill.child(TIME_OF_DEATH_PATH);
        newKillTimeRef.setValue(System.currentTimeMillis() / 1000L);

        // Set victim to be dead in competitor list
        DatabaseReference victimCompRef = database.getReference(
                GAMES_PATH+currentGameId+COMP_PATH_DOUBLE+victimId+ALIVE_PATH);
        victimCompRef.setValue(false);
    }

}
