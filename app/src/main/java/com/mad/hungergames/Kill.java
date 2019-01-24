package com.mad.hungergames;

import java.util.Date;

public class Kill {
    private final String killerId;
    private final String victimId;
    private final Date timeOfDeath;

    public Kill(String killerId, String victimId, Date timeOfDeath) {
        this.killerId = killerId;
        this.victimId = victimId;
        this.timeOfDeath = timeOfDeath;
    }

    public String getKillerId() { return this.killerId; }
    public String getVictimId(){ return this.victimId; }
    public Date getTimeOfDeath(){ return this.timeOfDeath; }
}
