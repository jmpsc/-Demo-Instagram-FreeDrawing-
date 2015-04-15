package com.jc.tpdemo.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
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
 *
 * Displays a drawable view, allowing it's content to be saved.
 */
public class FingerPaintingFragment extends android.app.Fragment {
    private DrawingView mDrawingView;
    private FloatingActionButton saveButton;

    @Inject
    Bus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //satisfy dependencies
        ((TPApplication) getActivity().getApplication()).getApplicationGraph().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_fingerpaint, container, false);

        mDrawingView = (DrawingView) layout.findViewById(R.id.drawing_view);
        saveButton = (FloatingActionButton) layout.findViewById(R.id.button_save);

        saveButton.setOnClickListener(saveImageClickListener());

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
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
        //Saving the image to the gallery is a long running operation
        new SaveImageAsyncTask(getActivity()).execute(bitmap);
    }

    /**
     * Method responsible to alert the user of the success or insuccess of the Image Save Event.
     * Here could also be added a ProgressBar or something similar, as the operation might take
     * a while in some devices.
     *
     * @param event
     */
    @Subscribe
    public void imageSavedAnswer(ImageSavedEvent event) {
        if (event.imageSaved)
            Toast.makeText(getActivity(), R.string.bitmap_exported, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getActivity(), R.string.bitmap_not_exported, Toast.LENGTH_SHORT).show();
    }

}
