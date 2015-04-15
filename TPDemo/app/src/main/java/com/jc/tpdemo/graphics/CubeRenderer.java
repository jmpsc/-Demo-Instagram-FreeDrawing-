package com.jc.tpdemo.graphics;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.jc.tpdemo.graphics.objects.Cube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jorge on 15-04-2015.
 *
 * Renders and controls the rotation of a cube
 */
public class CubeRenderer implements GLSurfaceView.Renderer {
    private static final float DEFAULT_ROTATION_STEP = 0.02f;
    Cube mCube;
    float mCubeRotationStep;
    float mCubeRotationX, mCubeRotationY;
    private boolean xRotationEnabled;
    private boolean yRotationEnabled;
    private float mRotationDirection;

    public CubeRenderer(){
        this(DEFAULT_ROTATION_STEP);
    }

    public CubeRenderer(float rotationStep){
        mCube = new Cube();
        mCubeRotationStep = rotationStep;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    /**
     * Draw the cube and update it's rotation
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear the view and transformation axis
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // Apply translation, horizontal rotation and vertical rotation
        // (the rotation is dependant on the object rotation, not the camera's position)
        gl.glTranslatef(0.0f, 0.0f, -10.0f);
        gl.glRotatef(mCubeRotationX, 0, 1, 0.0f);
        gl.glRotatef(mCubeRotationY, 1, 0, 0.0f);

        mCube.draw(gl);

        gl.glLoadIdentity();

        if(xRotationEnabled)
            mCubeRotationX += mRotationDirection * mCubeRotationStep;
        if(yRotationEnabled)
            mCubeRotationY += mRotationDirection * mCubeRotationStep;
    }

    /**
     *
     * @param rotationAcceleration The speed of the acceleration to apply (only regards the sign)
     * @param isHorizontalRotation if true, a horizontal rotation is applied, else, it is a vertical
     *                             rotation
     */
    public void setCubeRotation(float rotationAcceleration, boolean isHorizontalRotation){
        mRotationDirection = (rotationAcceleration > 0) ? 1 : -1;

        if(isHorizontalRotation) {
            xRotationEnabled = true;
            yRotationEnabled = false;
        } else {
            xRotationEnabled = false;
            yRotationEnabled = true;
        }
    }

    public void resetCubeRotation() {
        mCubeRotationX = mCubeRotationY = 0.0f;
        yRotationEnabled = xRotationEnabled = false;
    }

    public void setCubeRotationSpeed(float v) {
        mCubeRotationStep = v;
    }

    public float getCubeRotationSpeed() {
        return mCubeRotationStep;
    }
}
