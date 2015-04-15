package com.jc.tpdemo.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.jc.tpdemo.TPApplication;
import com.jc.tpdemo.data.events.DataPostStatusEvent;
import com.squareup.otto.Bus;

import java.util.Collection;
import java.util.HashSet;

import javax.inject.Inject;

/**
 * Created by Jorge on 15-04-2015.
 */
public abstract class GoogleAPIClientFragment<T> extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener{

    private static final String TAG = "APIClientFragment";
    public static final String START_ACTIVITY_PATH = "/start/MainActivity";

    GoogleApiClient mGoogleApiClient;

    @Inject
    Bus bus;

    /*
    Build the Google API Client and add self as the listener for all the connectivity changes
    with other devices.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TPApplication) getActivity().getApplication()).getApplicationGraph().inject(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onResume() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    /**
     * When there is a connection, send a startActivity request
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    protected void sendDataToWearable(T t, String requestPath) {
        if(mGoogleApiClient.isConnected())
            new PutDataAsyncTask(t, requestPath).execute(); //There is a device connected
        else
            bus.post(new DataPostStatusEvent(false)); //No device connected...

    }

    /**
     * Hook method responsible for placing {@code t} data into the provided dataMap,
     * which data is then sent through the GoogleAPIClient
     * @param t
     * @param dataMap
     */
    abstract void translateToDataMap(T t, DataMap dataMap);

    /**
     * Get all the available nodes connected to this device
     * @return
     */
    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }
        return results;
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        //There is no need to listen to data changes coming from wearables.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }

    @Override
    public void onConnectionSuspended(int i) { }

    /**
     * AsynTask to post the data in the DataApi.
     */
    class PutDataAsyncTask extends AsyncTask<Void, Void, Boolean>{
        private String mRequestPath;
        private T mrT;
        public PutDataAsyncTask(T t, String requestPath) {
            mrT = t;
            mRequestPath = requestPath;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            PutDataMapRequest putDataMapReq = PutDataMapRequest.create(mRequestPath);
            translateToDataMap(mrT, putDataMapReq.getDataMap());
            PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
            PendingResult<DataApi.DataItemResult> res = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);

            //Wait synchronously for a response... or for a timeout
            DataApi.DataItemResult result = res.await();

            return result.getStatus().isSuccess();
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            bus.post(new DataPostStatusEvent(b));
        }
    }
}
