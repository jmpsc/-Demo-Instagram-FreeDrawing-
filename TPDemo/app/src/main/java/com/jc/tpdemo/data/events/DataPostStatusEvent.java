package com.jc.tpdemo.data.events;

/**
 * Created by Jorge on 15-04-2015.
 */
public class DataPostStatusEvent {
    public boolean dataDelivered;

    public DataPostStatusEvent(boolean b) {
        dataDelivered = b;
    }
}
