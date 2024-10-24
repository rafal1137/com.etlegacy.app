package com.etlegacy.app.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.etlegacy.app.GameLauncher;
import com.etlegacy.app.OnScreenButtonConfigActivity;
import com.etlegacy.app.R;
import com.etlegacy.app.launcher.SetupControlsThemeFunc;
import com.etlegacy.app.lib.Utility;
import com.etlegacy.app.q3e.Q3EControlView;
import com.etlegacy.app.q3e.Q3EGlobals;
import com.etlegacy.app.q3e.Q3EPreference;
import com.etlegacy.app.q3e.Q3EUiConfig;
import com.etlegacy.app.q3e.Q3EUtils;

import org.libsdl.app.SDLActivity;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ControlsFragment extends Fragment {
    public View view;
    public Button configure_onscreen_controls_btn;
    public Button onscreen_button_setting;
    public Button setup_onscreen_button_theme;
    public CheckBox smoothjoy;
    public CheckBox launcher_tab2_joystick_unfixed;
    public Spinner launcher_tab2_joystick_visible;
    public CheckBox secfinglmb;
    public CheckBox launcher_tab2_enable_gyro;
    public EditText launcher_tab2_gyro_x_axis_sens;
    public EditText launcher_tab2_gyro_y_axis_sens;
    public CheckBox hideonscr;
    public CheckBox using_mouse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_controls, container, false);
        configure_onscreen_controls_btn = (Button) view.findViewById(R.id.configure_onscreen_controls_btn);
        onscreen_button_setting = (Button) view.findViewById(R.id.onscreen_button_setting);
        setup_onscreen_button_theme = (Button) view.findViewById(R.id.setup_onscreen_button_theme);
        smoothjoy = (CheckBox) view.findViewById(R.id.smoothjoy);
        launcher_tab2_joystick_unfixed = (CheckBox) view.findViewById(R.id.launcher_tab2_joystick_unfixed);
        launcher_tab2_joystick_visible = (Spinner) view.findViewById(R.id.launcher_tab2_joystick_visible);
        secfinglmb = (CheckBox) view.findViewById(R.id.secfinglmb);
        launcher_tab2_enable_gyro = (CheckBox) view.findViewById(R.id.launcher_tab2_enable_gyro);
        launcher_tab2_gyro_x_axis_sens = (EditText) view.findViewById(R.id.launcher_tab2_gyro_x_axis_sens);
        launcher_tab2_gyro_y_axis_sens = (EditText) view.findViewById(R.id.launcher_tab2_gyro_y_axis_sens);
        hideonscr = (CheckBox) view.findViewById(R.id.hideonscr);
        using_mouse = (CheckBox) view.findViewById(R.id.using_mouse);

        final CompoundButton.OnCheckedChangeListener m_checkboxChangeListener = new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int id = buttonView.getId();
                if (id == R.id.hideonscr)
                {
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                            .putBoolean(Q3EPreference.pref_hideonscr, isChecked)
                            .commit();
                }
                else if (id == R.id.smoothjoy)
                {
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                            .putBoolean(Q3EPreference.pref_analog, isChecked)
                            .commit();
                }
                else if (id == R.id.secfinglmb)
                {
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                            .putBoolean(Q3EPreference.pref_2fingerlmb, isChecked)
                            .commit();
                }
                else if (id == R.id.launcher_tab2_enable_gyro)
                {
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                            .putBoolean(Q3EPreference.pref_harm_view_motion_control_gyro, isChecked)
                            .commit();
                    UpdateEnableGyro(isChecked);
                }
                else if (id == R.id.launcher_tab2_joystick_unfixed)
                {
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                            .putBoolean(Q3EPreference.pref_harm_joystick_unfixed, isChecked)
                            .commit();
                }
                else if (id == R.id.using_mouse)
                {
                    UpdateMouseMenu(isChecked);
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                            .putBoolean(Q3EPreference.pref_harm_using_mouse, isChecked)
                            .commit();
                }

            }
        };

        final View.OnClickListener m_buttonClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int id = view.getId();
                if (id == R.id.configure_onscreen_controls_btn)
                {

                }
                else if (id == R.id.onscreen_button_setting)
                {
                    OpenOnScreenButtonSetting();
                }
                else if (id == R.id.setup_onscreen_button_theme)
                {
                    OpenOnScreenButtonThemeSetting();
                }
            }
        };

        final AdapterView.OnItemSelectedListener m_itemSelectedListener = new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int viewId = parent.getId();
                if(viewId == R.id.launcher_tab2_joystick_visible)
                {
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                            .putInt(Q3EPreference.pref_harm_joystick_visible, getResources().getIntArray(R.array.joystick_visible_mode_values)[position])
                            .commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                int viewId = parent.getId();
                if(viewId == R.id.launcher_tab2_joystick_visible)
                {
                    int position = Utility.ArrayIndexOf(getResources().getIntArray(R.array.joystick_visible_mode_values), Q3EGlobals.ONSCRREN_JOYSTICK_VISIBLE_ALWAYS);
                    parent.setSelection(position);
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                            .putInt(Q3EPreference.pref_harm_joystick_visible, Q3EGlobals.ONSCRREN_JOYSTICK_VISIBLE_ALWAYS)
                            .commit();
                }
            }
        };

        configure_onscreen_controls_btn.setOnClickListener(m_buttonClickListener);
        onscreen_button_setting.setOnClickListener(m_buttonClickListener);
        setup_onscreen_button_theme.setOnClickListener(m_buttonClickListener);
        smoothjoy.setChecked(smoothjoy.isChecked());
        smoothjoy.setOnCheckedChangeListener(m_checkboxChangeListener);
        launcher_tab2_joystick_unfixed.setChecked(launcher_tab2_joystick_unfixed.isChecked());
        launcher_tab2_joystick_unfixed.setOnCheckedChangeListener(m_checkboxChangeListener);
        launcher_tab2_joystick_visible.setSelection(Utility.ArrayIndexOf(getResources().getIntArray(R.array.joystick_visible_mode_values), Q3EGlobals.ONSCRREN_JOYSTICK_VISIBLE_ALWAYS));
        launcher_tab2_joystick_visible.setOnItemSelectedListener(m_itemSelectedListener);
        secfinglmb.setChecked(secfinglmb.isChecked());
        secfinglmb.setOnCheckedChangeListener(m_checkboxChangeListener);
        launcher_tab2_enable_gyro.setChecked(launcher_tab2_enable_gyro.isChecked());
        launcher_tab2_enable_gyro.setOnCheckedChangeListener(m_checkboxChangeListener);
        launcher_tab2_gyro_x_axis_sens.setText(String.valueOf(Q3EControlView.GYROSCOPE_X_AXIS_SENS));
        launcher_tab2_gyro_y_axis_sens.setText(String.valueOf(Q3EControlView.GYROSCOPE_Y_AXIS_SENS));
        UpdateEnableGyro(launcher_tab2_enable_gyro.isChecked());
        hideonscr.setOnCheckedChangeListener(m_checkboxChangeListener);
        hideonscr.setChecked(hideonscr.isChecked());
        using_mouse.setChecked(using_mouse.isChecked());
        using_mouse.setOnCheckedChangeListener(m_checkboxChangeListener);

        UpdateMouseMenu(using_mouse.isChecked());
        return view;
    }

    public void UpdateMouseMenu(boolean show)
    {
        final boolean supportGrabMouse = Q3EUtils.SupportMouse() == Q3EGlobals.MOUSE_EVENT;
        //final boolean sdlHintEvent = SDLActivity.nativeGetHintBoolean("SDL_HINT_MOUSE_TOUCH_EVENTS", true);
        if (supportGrabMouse /*|| sdlHintEvent*/)
        {
            using_mouse.setChecked(true);
        }
        else
        {
            using_mouse.setChecked(false);
        }
    }

    private void UpdateEnableGyro(boolean on)
    {
        launcher_tab2_gyro_x_axis_sens.setText(String.valueOf(Q3EControlView.GYROSCOPE_X_AXIS_SENS));
        launcher_tab2_gyro_y_axis_sens.setText(String.valueOf(Q3EControlView.GYROSCOPE_Y_AXIS_SENS));
        // only disable
        launcher_tab2_gyro_x_axis_sens.setEnabled(on);
        launcher_tab2_gyro_y_axis_sens.setEnabled(on);
    }

    private void OpenOnScreenButtonSetting()
    {
        Intent intent = new Intent(getContext(), OnScreenButtonConfigActivity.class);
        startActivity(intent);
    }

    private void OpenOnScreenButtonThemeSetting()
    {
        new SetupControlsThemeFunc((GameLauncher) getContext()).Start(new Bundle());
    }

    public void controls(View view)
    {
        startActivity(new Intent(getContext(), Q3EUiConfig.class));
    }
}