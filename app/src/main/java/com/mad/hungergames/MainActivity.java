package com.mad.hungergames;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener{
    private String FRAGMENT_INSTANCE_KEY = "FRAGMENT";
    private int storedFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Restores the correct fragment based on the saved instance state.
        // Loads the main menu if there is no saved instance state.
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
        else{
            int restoredFragmentId = savedInstanceState.getInt(FRAGMENT_INSTANCE_KEY);
            switch (restoredFragmentId) {
                case 1:
                    loadFragment(new HomeFragment());
                    break;
                case 2:
                    loadFragment(new KillRegistrationFragment());
                    break;
                case 3:
                    loadFragment(new PlayersFragment());
                    break;
                case 4:
                    loadFragment(new GamesFragment());
                    break;
                case 5:
                    loadFragment(new NotificationsFragment());
                    break;
            }
        }

        // Loads bottom navigation menu.
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
    }

    /*
    * loads the correct fragment into the main activity screen
     */
    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        else{return false;}
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                storedFragmentId = 1;
                fragment = new HomeFragment();
                break;
            case R.id.navigation_kill_registration:
                storedFragmentId = 2;
                fragment = new KillRegistrationFragment();
                break;
            case R.id.navigation_players:
                storedFragmentId = 3;
                fragment = new PlayersFragment();
                break;
            case R.id.navigation_games:
                storedFragmentId = 4;
                fragment = new GamesFragment();
                break;
            case R.id.navigation_notifications:
                storedFragmentId = 5;
                fragment = new NotificationsFragment();
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        storedFragmentId = savedInstanceState.getInt(FRAGMENT_INSTANCE_KEY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_INSTANCE_KEY, storedFragmentId);
    }
}
