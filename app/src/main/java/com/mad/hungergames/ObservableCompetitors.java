package com.mad.hungergames;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

public class ObservableCompetitors extends Observable implements Serializable {

    private List<Competitor> competitors;

    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setValue(List<Competitor> competitors) {
        this.competitors = competitors;
        this.setChanged();
        this.notifyObservers(competitors);
    }

    public void clear() {
        this.competitors.clear();
    }
}