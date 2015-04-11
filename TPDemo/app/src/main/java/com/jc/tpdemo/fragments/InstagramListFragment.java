package com.jc.tpdemo.fragments;

import android.app.Activity;

import com.jc.tpdemo.activities.IDrawerManager;

/**
 * Created by Jorge on 11-04-2015.
 */
public class InstagramListFragment extends android.app.Fragment {

    private IDrawerManager mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (IDrawerManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IDrawerManager");
        }

    }
}
