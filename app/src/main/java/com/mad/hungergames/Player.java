package com.mad.hungergames;

public class Player {
    private String id;
    private final String name;
    private final String avatar;
    private final String password;
    private final String role;
    private final String killcode;

    public Player(String id, String name, String avatar, String password, String role, String killcode){
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.password = password;
        this.role = role;
        this.killcode = killcode;
    }

    public String getId() { return this.id; }
    public String getName(){
        return this.name;
    }
    public String getAvatar(){
        return this.avatar;
    }
    public String getPassword(){
        return this.password;
    }
    public String getRole(){
        return this.role;
    }
    public String getKillcode(){
        return this.killcode;
    }
}
