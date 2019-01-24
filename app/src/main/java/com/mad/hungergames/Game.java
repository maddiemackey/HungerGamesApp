package com.mad.hungergames;

import java.util.ArrayList;
import java.util.Date;

public class Game {
    private String id;
    private final String name;
    private final ArrayList<Competitor> competitors;
    private final Object kills;
    private final Boolean registrationActive;
    private final Date startTime;
    private final Date endTime;
    private final String winnerId;

    public Game(String id, String name, ArrayList<Competitor> competitors,
                Object kills, Boolean registrationActive,
                Date startTime, Date endTime, String winnerId){
        this.id = id;
        this.name = name;
        this.competitors = competitors;
        this.kills = kills;
        this.registrationActive = registrationActive;
        this.startTime = startTime;
        this.endTime = endTime;
        this.winnerId = winnerId;
    }

    public String getId() { return this.id; }
    public String getName(){
        return this.name;
    }
    public ArrayList<Competitor> getCompetitors(){
        return this.competitors;
    }
    public Object getKills(){
        return this.kills;
    }
    public Boolean isRegistrationActive(){
        return this.registrationActive;
    }
    public Date getStartTime(){
        return this.startTime;
    }
    public Date getEndTime(){
        return this.endTime;
    }
    public String getWinnerId(){
        return this.winnerId;
    }

    public String getWinnerName(){
        return this.winnerId;
    }
}
