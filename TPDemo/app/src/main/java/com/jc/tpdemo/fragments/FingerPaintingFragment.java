package com.jc.tpdemo.fragments;

import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jc.tpdemo.R;
import com.jc.tpdemo.TPApplication;
import com.jc.tpdemo.asynctasks.SaveImageAsyncTask;
import com.jc.tpdemo.data.events.ImageSavedEvent;
import com.jc.tpdemo.widgets.DrawingView;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

/**
 * Created by Jorge on 11-04-2015.
 */
public class FingerPaintingFragment extends android.app.Fragment {
    private DrawingView mDrawingView;
    private FloatingActionButton saveButton;

    @Inject Bus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //satisfy dependencies
        ((TPApplication)getActivity().getApplication()).getApplicationGraph().inject(this);
        bus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_fingerpaint, container, false);

        mDrawingView = (DrawingView) layout.findViewById(R.id.drawing_view);
        saveButton = (FloatingActionButton) layout.findViewById(R.id.button_save);

        saveButton.setOnClickListener(saveImageClickListener());

        return layout;
    }

    private View.OnClickListener saveImageClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageToGallery(mDrawingView.getBitmap());
            }
        };
    }

    public void addImageToGallery(Bitmap bitmap) {
        new SaveImageAsyncTask(getActivity()).execute(bitmap);
    }

    @Subscribe public void imageSavedAnswer(ImageSavedEvent event) {
        if(event.imageSaved)
            Toast.makeText(getActivity(),R.string.bitmap_exported, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(),R.string.bitmap_not_exported, Toast.LENGTH_SHORT).show();
    }

}
