package com.etlegacy.app.ui.cvar;

import android.content.Context;

import com.etlegacy.app.lib.KCVar;
import com.etlegacy.app.q3e.karin.KidTech4Command;

public class CVarSetting_checkbox extends androidx.appcompat.widget.AppCompatCheckBox implements CVarSettingInterface {
    private KCVar m_cvar;

    public CVarSetting_checkbox(Context context)
    {
        super(context);
    }

    @Override
    public void SetCVar(KCVar cvar)
    {
        this.m_cvar = cvar;
        setChecked(KidTech4Command.strtob(cvar.defaultValue));
        setText(cvar.description);
    }

    @Override
    public void RestoreCommand(String cmd)
    {
        boolean def = KidTech4Command.strtob(m_cvar.defaultValue);
        Boolean value = KidTech4Command.GetBoolProp(cmd, m_cvar.name, def);
        setChecked(null != value ? value : def);
    }

    @Override
    public String DumpCommand(String cmd)
    {
        return KidTech4Command.SetBoolProp(cmd, m_cvar.name, isChecked());
    }

    @Override
    public String RemoveCommand(String cmd)
    {
        return KidTech4Command.RemoveProp(cmd, m_cvar.name);
    }

    @Override
    public String ResetCommand(String cmd)
    {
        return KidTech4Command.SetBoolProp(cmd, m_cvar.name, KidTech4Command.strtob(m_cvar.defaultValue));
    }

    @Override
    public void SetEnabled(boolean enabled)
    {
        setEnabled(enabled);
    }
}