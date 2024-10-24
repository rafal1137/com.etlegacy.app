package com.etlegacy.app.q3e;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public final class Q3EPreference {
    public static final String pref_hideonscr     = "q3e_hideonscr";
    public static final String pref_analog        = "q3e_analog";
    public static final String pref_2fingerlmb    = "q3e_2fingerlmb";
    public static final String pref_harm_view_motion_control_gyro     = "q3e_harm_mouse_move_control_gyro";
    public static final String pref_harm_joystick_unfixed             = "harm_joystick_unfixed";
    public static final String pref_harm_using_mouse                  = "harm_using_mouse";
    public static final String pref_harm_joystick_visible             = "harm_joystick_visible";
    public static final String pref_harm_view_motion_gyro_x_axis_sens = "q3e_harm_view_motion_gyro_x_axis_sens";
    public static final String pref_harm_view_motion_gyro_y_axis_sens = "q3e_harm_view_motion_gyro_y_axis_sens";
    public static final String fs_game_user                           = "fs_game_user";
    public static final String LANG                          = "harm_lang";
    public static final String THEME                         = "harm_theme";
    public static final String CONTROLS_THEME                = "harm_controls_theme";
    public static final String ONSCREEN_BUTTON               = "harm_onscreen_key"; // old = "harm_onscreen_button"
    public static final String WEAPON_PANEL_KEYS             = "harm_weapon_panel_keys";
    public static final String pref_harm_etw_command_record   = "q3e_harm_etw_command_record";
    public static final String pref_harm_etw_fs_game          = "q3e_harm_etw_fs_game";
    public static final String pref_params_etw                = "q3e_params_etw";
    public static final String pref_datapath      = "q3e_datapath";
    public static final String COVER_EDGES                   = "harm_cover_edges";
    public static final String HIDE_NAVIGATION_BAR           = "harm_hide_nav";
    public static final String AUTOSAVE_BUTTON_SETTINGS      = "harm_autosave_button_settings";


    private Q3EPreference() {

    }
}
