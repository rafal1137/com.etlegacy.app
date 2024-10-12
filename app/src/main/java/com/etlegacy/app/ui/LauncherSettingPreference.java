package com.etlegacy.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.etlegacy.app.R;
import com.etlegacy.app.lib.ContextUtility;
import com.etlegacy.app.q3e.Q3EPreference;
import com.etlegacy.app.sys.PreferenceKey;
import com.etlegacy.app.sys.Theme;


public class LauncherSettingPreference extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.launcher_settings_preference);

        findPreference(PreferenceKey.LAUNCHER_ORIENTATION).setOnPreferenceChangeListener(this);
        findPreference(PreferenceKey.HIDE_AD_BAR).setOnPreferenceChangeListener(this);
        findPreference(Q3EPreference.LANG).setOnPreferenceChangeListener(this);
        findPreference(Q3EPreference.THEME).setOnPreferenceChangeListener(this);
    }

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {

    }

    @Override
    public boolean onPreferenceTreeClick(@NonNull Preference preference)
    {
        String key = preference.getKey();
        Context context = ContextUtility.GetContext(this);

        return super.onPreferenceTreeClick(preference);
    }

    /*@Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
    {
        String key = preference.getKey();
        Context context = ContextUtility.GetContext(this);

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }*/

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        String key = preference.getKey();
        Context context = ContextUtility.GetContext(this);
        switch (key)
        {
            case PreferenceKey.LAUNCHER_ORIENTATION:
                int o = (boolean) newValue ? 0 : 1;
                ContextUtility.SetScreenOrientation((AppCompatActivity) getActivity(), o);
                return true;
            case PreferenceKey.HIDE_AD_BAR:
                boolean b = (boolean) newValue;
                View view = getActivity().findViewById(R.id.main_ad_layout);
                if (null != view)
                    view.setVisibility(b ? View.GONE : View.VISIBLE);
                return true;
            case Q3EPreference.LANG:
            {
                Toast.makeText(context, R.string.be_available_on_reboot_the_next_time, Toast.LENGTH_SHORT).show();
                return true;
            }
            case Q3EPreference.THEME:
            {
                Toast.makeText(context, R.string.app_is_rebooting, Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Theme.Reset(null);
                        ContextUtility.RestartApp((AppCompatActivity) LauncherSettingPreference.this.getActivity());
                    }
                }, 1000);
                return true;
            }
            default:
                return false;
        }
    }
}
