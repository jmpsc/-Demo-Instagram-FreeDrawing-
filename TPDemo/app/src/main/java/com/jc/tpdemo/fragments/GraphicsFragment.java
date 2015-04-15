package com.jc.tpdemo.fragments;

import android.app.Fragment;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jc.tpdemo.R;
import com.jc.tpdemo.graphics.CubeRenderer;

/**
 * Created by Jorge on 11-04-2015.
 *
 * This fragment uses OpenGL ES 2.0 to draw a cube and GDC to detect gestures to promote interaction
 * with the 3D graphics.
 */
public class GraphicsFragment extends Fragment implements GestureDetector.OnGestureListener {
    private static final float REGULAR_CUBE_SPEED = 0.3f;
    private static final float TURBO_CUBE_SPEED = 5.0f;
    private GLSurfaceView mGLSurface;
    private GestureDetectorCompat mDetector;
    private CubeRenderer mCubeRenderer;
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCubeRenderer = new CubeRenderer(0.3f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_graphics, container, false);

        mTextView = (TextView) layout.findViewById(R.id.text);
        mGLSurface = (GLSurfaceView) layout.findViewById(R.id.gl_surface);

        // Set the renderer for the SurfaceView
        mGLSurface.setRenderer(mCubeRenderer);

        // Instantiate GDC and intercept TouchEvents
        mDetector = new GestureDetectorCompat(getActivity(), this);
        layout.setOnTouchListener(getGDCListener());

        return layout;
    }

    private View.OnTouchListener getGDCListener() {
        return new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTextView.setVisibility(View.INVISIBLE);
                return mDetector.onTouchEvent(event);
            }
        };
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mCubeRenderer.resetCubeRotation();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        mCubeRenderer.setCubeRotationSpeed(
                mCubeRenderer.getCubeRotationSpeed() == REGULAR_CUBE_SPEED ?
                TURBO_CUBE_SPEED : REGULAR_CUBE_SPEED
        );
    }

    /**
     * On fling, starts rotating the cube drawn bu the cube renderer.
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // The axis with the greater velocity indicates the orientation of the fling
        boolean isHorizintalFling = Math.abs(velocityX) > Math.abs(velocityY);
        mCubeRenderer.setCubeRotation(isHorizintalFling ? velocityX : velocityY, isHorizintalFling);

        return false;
    }
}
