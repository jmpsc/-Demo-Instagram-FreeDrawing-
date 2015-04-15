package com.jc.tpdemowearable.services;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.jc.tpdemowearable.TPWearableApplication;
import com.jc.tpdemowearable.activities.MainActivity;
import com.jc.tpdemowearable.models.ImageReceivedEvent;
import com.squareup.otto.Bus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by Jorge on 14-04-2015.
 */
public class DataLayerListenerService extends WearableListenerService {
    private static final String TAG = "DLLService";
    private static final String IMAGE_URL_KEY = "key_url";
    private static final String TEXT_KEY = "key_text";
    public static final String LOAD_IMAGE_URI = "/show";
    public static final String START_ACTIVITY_PATH = "/start/MainActivity";

    @Inject
    public Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        ((TPWearableApplication) getApplication()).getApplicationGraph().inject(this);
    }

    /**
     * Starts the app's MainActivity if the start activity directive is received
     * @param messageEvent
     */
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals(START_ACTIVITY_PATH)) {
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startIntent);
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onDataChanged: " + dataEvents);
        }
        final List<DataEvent> events = FreezableUtils
                .freezeIterable(dataEvents);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult =
                googleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.e(TAG, "Failed to connect to GoogleApiClient.");
            return;
        }

        // Loop through the events and pass notify the existence of new images to be displayed
        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem provides information about an image/text pair
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo(LOAD_IMAGE_URI) == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    //Post the event in the event-bus. If the activity is visible, the image will be
                    //updated, if not, nothing happens.
                    bus.post(new ImageReceivedEvent(dataMap.getString(TEXT_KEY),
                                                    dataMap.getString(IMAGE_URL_KEY)));
                }
            }
        }
    }
}
