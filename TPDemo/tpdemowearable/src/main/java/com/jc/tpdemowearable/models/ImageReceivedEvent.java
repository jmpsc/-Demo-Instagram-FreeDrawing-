package com.jc.tpdemowearable.models;

/**
 * Created by Jorge on 14-04-2015.
 */
public class ImageReceivedEvent {
    public final String text;
    public final String imageURL;

    public ImageReceivedEvent(String text, String imageURL) {
        this.text = text;
        this.imageURL = imageURL;
    }
}
