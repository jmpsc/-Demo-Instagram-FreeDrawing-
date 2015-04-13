package com.jc.tpdemo.asynctasks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.jc.tpdemo.TPApplication;
import com.jc.tpdemo.data.events.ImageSavedEvent;
import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Created by Jorge on 12-04-2015.
 */
public class SaveImageAsyncTask extends AsyncTask<Bitmap, Void, Boolean> {

    @Inject public Bus bus;
    private final Activity mActivity;

    public SaveImageAsyncTask(Activity activity){
        mActivity = activity;
        ((TPApplication)activity.getApplication()).getApplicationGraph().inject(this);
    }

    @Override
    protected Boolean doInBackground(Bitmap... params) {
        String url = MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), params[0], "DATE GOES HERE", "Hand drawn masterpiece");
        //If the url is null, the image was not successfully exported
        return url != null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        //Notify the interested components
        bus.post(new ImageSavedEvent(aBoolean.booleanValue()));
    }
}
