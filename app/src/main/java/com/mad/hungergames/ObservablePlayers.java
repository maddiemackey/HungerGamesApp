package com.mad.hungergames;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

public class ObservablePlayers extends Observable implements Serializable {

    private List<Player> players;

    public List<Player> getPlayers() {
        return players;
    }

    /*
     * Sets the value of the list of players according to an inputted list.
     */
    public void setValue(List<Player> players) {
        this.players = players;
        this.setChanged();
        this.notifyObservers(players);
    }

    /*
    * Clears the list of players.
     */
    public void clear() {
        this.players.clear();
    }
}