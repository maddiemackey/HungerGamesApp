package com.mad.hungergames;

public class Competitor {
    private String id;
    private final Player player;
    private final Boolean status;

    public Competitor(String id, Player player, Boolean status) {
        this.id = id;
        this.player = player;
        this.status = status;
    }

    public String getId() { return this.id; }
    public Player getPlayer(){ return this.player; }
    public Boolean isAlive(){ return this.status; }
}
