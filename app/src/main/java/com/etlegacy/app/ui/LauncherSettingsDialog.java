package com.etlegacy.app.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.etlegacy.app.R;

public class LauncherSettingsDialog extends DialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.launcher_settings_dialog, container);

        FragmentManager manager;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
            manager = getChildFragmentManager();
        else
            manager = getFragmentManager();
        manager.beginTransaction()
                .add(R.id.launcher_settings_dialog_main_layout, new LauncherSettingPreference(), "_Settings_preference")
                .commit()
        ;
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(R.string.settings);
        return dialog;
    }

    public static LauncherSettingsDialog newInstance()
    {
        LauncherSettingsDialog dialog = new LauncherSettingsDialog();
        return dialog;
    }
}
