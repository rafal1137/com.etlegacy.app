package com.etlegacy.app.q3e;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.opengl.GLSurfaceView;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Q3EControlView extends GLSurfaceView implements GLSurfaceView.Renderer, SensorEventListener {
    public static final float GYROSCOPE_X_AXIS_SENS = 18;
    public static final float GYROSCOPE_Y_AXIS_SENS = 18;

    // toolbar function
    private boolean m_toolbarActive = true;
    private View m_keyToolbar = null;

    public Q3EControlView(Context context) {
        super(context);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    public void ToggleToolbar()
    {
        ToggleToolbar(!m_toolbarActive);
    }

    public void ToggleToolbar(boolean b)
    {
        if (null != m_keyToolbar && Q3EUtils.q3ei.function_key_toolbar)
        {
            m_toolbarActive = b;
            if (m_toolbarActive)
                m_keyToolbar.setVisibility(View.VISIBLE);
            else
                m_keyToolbar.setVisibility(View.GONE);
        }
    }
}
