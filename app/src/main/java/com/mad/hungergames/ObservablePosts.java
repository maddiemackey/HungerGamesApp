package com.mad.hungergames;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

public class ObservablePosts extends Observable implements Serializable {

    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    /*
    * Sets the value of the list of posts according to an inputted list.
     */
    public void setValue(List<Post> posts) {
        this.posts = posts;
        this.setChanged();
        this.notifyObservers(posts);
    }

    /*
    * Clears the list of posts.
     */
    public void clear() {
        this.posts.clear();
    }
}