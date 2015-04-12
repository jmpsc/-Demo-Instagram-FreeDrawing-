package com.jc.tpdemo.models;

/**
 * Created by Jorge on 11-04-2015.
 */
public class InstagramListItem {
    public String username;
    public String imageURL;
    public String uploadDate;

    public InstagramListItem(String imageURL, String username, String uploadDate) {
        this.imageURL = imageURL;
        this.username = username;
        this.uploadDate = uploadDate;
    }
}
