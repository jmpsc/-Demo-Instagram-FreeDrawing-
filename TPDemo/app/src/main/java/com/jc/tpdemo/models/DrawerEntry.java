package com.jc.tpdemo.models;

/**
 * Created by Jorge on 11-04-2015.
 */
public class DrawerEntry {
    public int iconResourceId;
    public String title;

    public DrawerEntry(String title, int resourceId) {
        this.title = title;
        this.iconResourceId = resourceId;
    }
}
