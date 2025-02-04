package com.etlegacy.app.q3e;

import android.content.Context;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;

import androidx.preference.PreferenceManager;

import com.etlegacy.app.q3e.karin.KKeyToolBar;

import org.libsdl.app.SDLActivity;

public class Q3EControlView extends SurfaceView {
    public static final float GYROSCOPE_X_AXIS_SENS = 18;
    public static final float GYROSCOPE_Y_AXIS_SENS = 18;

    // toolbar function
    private boolean m_toolbarActive = true;
    private View m_keyToolbar = null;
    private boolean m_enableGyro = false;
    private int m_mapBack = Q3EGlobals.ENUM_BACK_ALL;

    private float m_xAxisGyroSens = GYROSCOPE_X_AXIS_SENS;
    private float m_yAxisGyroSens = GYROSCOPE_Y_AXIS_SENS;

    public Q3EControlView(Context context) {
        super(context);
    }

    public View CreateToolbar()
    {
        if (Q3EUtils.q3ei.function_key_toolbar)
        {
            Context context = getContext();
            m_keyToolbar = new KKeyToolBar(context);
            m_keyToolbar.setVisibility(View.GONE);
            try
            {
                String str = PreferenceManager.getDefaultSharedPreferences(context).getString(Q3EPreference.pref_harm_function_key_toolbar_y, "0");
                if(null == str)
                    str = "0";
                int y = Integer.parseInt(str);
                if (y > 0)
                    m_keyToolbar.setY(y);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return m_keyToolbar;
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

    public boolean EnableGyroscopeControl(boolean... b)
    {
        if (null != b && b.length > 0)
            m_enableGyro = b[0];
        return m_enableGyro;
    }

    public void SetGyroscopeSens(float x, float y)
    {
        XAxisSens(x);
        yAxisSens(y);
    }

    public float XAxisSens(float... f)
    {
        if (null != f && f.length > 0)
            m_xAxisGyroSens = f[0];
        return m_xAxisGyroSens;
    }

    public float yAxisSens(float... f)
    {
        if (null != f && f.length > 0)
            m_yAxisGyroSens = f[0];
        return m_yAxisGyroSens;
    }

    public int getCharacter(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_DEL) return '\b';
        return event.getUnicodeChar();
    }

    //@Override
    public boolean OnKeyDown(int keyCode, KeyEvent event)
    {
        if (((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)))
            return false;
        if (keyCode == KeyEvent.KEYCODE_BACK && (m_mapBack & Q3EGlobals.ENUM_BACK_ESCAPE) == 0)
        {
            return true;
        }
        int qKeyCode;
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_UP:
                qKeyCode = Q3EUtils.q3ei.VOLUME_UP_KEY_CODE;
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                qKeyCode = Q3EUtils.q3ei.VOLUME_DOWN_KEY_CODE;
                break;
            default:
                qKeyCode = Q3EKeyCodes.convertKeyCode(keyCode, event);
                break;
        }
        int t = getCharacter(keyCode, event);
        SDLActivity.onNativeKeyDown(qKeyCode);
        //Q3EUtils.q3ei.callbackObj.sendKeyEvent(true, qKeyCode, t);
        return true;
    }

    //@Override
    public boolean OnKeyUp(int keyCode, KeyEvent event)
    {
        if (((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) || (keyCode == KeyEvent.KEYCODE_VOLUME_UP)))
            return false;
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (m_mapBack == Q3EGlobals.ENUM_BACK_NONE)
                return true;
            if ((m_mapBack & Q3EGlobals.ENUM_BACK_EXIT) != 0)
                return true;
            Q3EUtils.ToggleToolbar(false);
        }
        int qKeyCode;
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_UP:
                qKeyCode = Q3EUtils.q3ei.VOLUME_UP_KEY_CODE;
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                qKeyCode = Q3EUtils.q3ei.VOLUME_DOWN_KEY_CODE;
                break;
            default:
                qKeyCode = Q3EKeyCodes.convertKeyCode(keyCode, event);
                break;
        }
        SDLActivity.onNativeKeyUp(qKeyCode);
        //Q3EUtils.q3ei.callbackObj.sendKeyEvent(false, qKeyCode, getCharacter(keyCode, event));
        return true;
    }
}
