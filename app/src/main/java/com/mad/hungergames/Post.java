package com.mad.hungergames;

public class Post {
    private String text;
    private final String picture;

    public Post(String text, String picture){
        this.text = text;
        this.picture = picture;
    }

    public String getText() { return this.text; }
    public String getPicture(){
        return this.picture;
    }
}
