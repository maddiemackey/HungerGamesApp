package com.mad.hungergames;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

public class ObservableGames extends Observable implements Serializable {

    private List<Game> games;

    public List<Game> getGames() {
        return games;
    }

    /*
     * Sets the value of the list of posts according to an inputted list.
     */
    public void setValue(List<Game> games) {
        this.games = games;
        this.setChanged();
        this.notifyObservers(games);
    }

    /*
     * Clears the list of games.
     */
    public void clear() {
        this.games.clear();
    }
}