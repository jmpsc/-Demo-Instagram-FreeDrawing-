package com.jc.tpdemowearable.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.jc.tpdemowearable.R;
import com.jc.tpdemowearable.TPWearableApplication;
import com.jc.tpdemowearable.models.ImageReceivedEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

public class MainActivity extends Activity {
    private TextView mTextView;
    private ImageView mImageView;

    @Inject
    public Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TPWearableApplication) getApplication()).getApplicationGraph().inject(this);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mImageView = (ImageView) stub.findViewById(R.id.image);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onStart();
        bus.register(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Subscribe public void imageAnswer(ImageReceivedEvent event) {
        mTextView.setText(event.text);
        Picasso.with(this).load(event.imageURL).into(mImageView);
    }
}
