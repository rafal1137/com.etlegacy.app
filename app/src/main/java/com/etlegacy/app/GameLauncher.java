package com.etlegacy.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.preference.PreferenceManager;
import androidx.viewpager2.widget.ViewPager2;

import com.etlegacy.app.launcher.CVarEditorFunc;
import com.etlegacy.app.launcher.ChooseCommandRecordFunc;
import com.etlegacy.app.launcher.ChooseGameFolderFunc;
import com.etlegacy.app.launcher.ChooseGameModFunc;
import com.etlegacy.app.launcher.EditConfigFileFunc;
import com.etlegacy.app.launcher.SetupControlsThemeFunc;
import com.etlegacy.app.launcher.StartGameFunc;
import com.etlegacy.app.launcher.SupportDeveloperFunc;
import com.etlegacy.app.lib.ContextUtility;
import com.etlegacy.app.lib.UIUtility;
import com.etlegacy.app.lib.Utility;
import com.etlegacy.app.misc.TextHelper;
import com.etlegacy.app.q3e.Q3EAd;
import com.etlegacy.app.q3e.Q3EControlView;
import com.etlegacy.app.q3e.Q3EGlobals;
import com.etlegacy.app.q3e.Q3EInterface;
import com.etlegacy.app.q3e.Q3ELang;
import com.etlegacy.app.q3e.Q3EPreference;
import com.etlegacy.app.q3e.Q3EUtils;
import com.etlegacy.app.q3e.karin.KStr;
import com.etlegacy.app.q3e.karin.KidTechCommand;
import com.etlegacy.app.q3e.onscreen.Q3EControls;
import com.etlegacy.app.sys.Game;
import com.etlegacy.app.sys.PreferenceKey;
import com.etlegacy.app.sys.GameManager;
import com.etlegacy.app.ui.LauncherSettingsDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameLauncher extends AppCompatActivity {
    private static final int CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_START = 1;
    private static final int CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_EDIT_CONFIG_FILE = 2;
    private static final int CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_FOLDER = 3;
    private static final int CONST_RESULT_CODE_REQUEST_EXTRACT_PATCH_RESOURCE = 4;
    private static final int CONST_RESULT_CODE_REQUEST_BACKUP_PREFERENCES_CHOOSE_FILE = 5;
    private static final int CONST_RESULT_CODE_REQUEST_RESTORE_PREFERENCES_CHOOSE_FILE = 6;
    private static final int CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_GAME_LIBRARY = 7;
    private static final int CONST_RESULT_CODE_REQUEST_ADD_EXTERNAL_GAME_LIBRARY = 8;
    private static final int CONST_RESULT_CODE_REQUEST_EDIT_EXTERNAL_GAME_LIBRARY = 9;
    private static final int CONST_RESULT_CODE_REQUEST_EXTRACT_SOURCE = 10;
    private static final int CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_GAME_MOD = 11;
    private static final int CONST_RESULT_CODE_ACCESS_ANDROID_DATA = 12;

    private final GameManager m_gameManager = new GameManager();
    // GameLauncher function
    private EditConfigFileFunc m_editConfigFileFunc;
    private ChooseGameFolderFunc m_chooseGameFolderFunc;
    private StartGameFunc m_startGameFunc;
    private ChooseGameModFunc m_chooseGameModFunc;
    private ChooseCommandRecordFunc m_chooseCommandRecordFunc;

    public static final String default_gamedata = Environment.getExternalStorageDirectory() + "/etlegacy";
    private final ViewHolder V = new ViewHolder();
    private boolean m_cmdUpdateLock = false;
    private String m_edtPathFocused = "";

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;

    private final CompoundButton.OnCheckedChangeListener m_checkboxChangeListener = new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            int id = buttonView.getId();
            if (id == R.id.hideonscr)
            {
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                        .putBoolean(Q3EPreference.pref_hideonscr, isChecked)
                        .commit();
            }
            else if (id == R.id.smoothjoy)
            {
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                        .putBoolean(Q3EPreference.pref_analog, isChecked)
                        .commit();
            }
            else if (id == R.id.secfinglmb)
            {
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                        .putBoolean(Q3EPreference.pref_2fingerlmb, isChecked)
                        .commit();
            }
            else if (id == R.id.fs_game_user)
            {
                UpdateUserGame(isChecked);
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                        .putBoolean(Q3EUtils.q3ei.GetEnableModPreferenceKey(), isChecked)
                        .commit();
            }
            else if (id == R.id.launcher_tab2_enable_gyro)
            {
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                        .putBoolean(Q3EPreference.pref_harm_view_motion_control_gyro, isChecked)
                        .commit();
                UpdateEnableGyro(isChecked);
            }
            else if (id == R.id.launcher_tab2_joystick_unfixed)
            {
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                        .putBoolean(Q3EPreference.pref_harm_joystick_unfixed, isChecked)
                        .commit();
                Q3EUtils.q3ei.joystick_unfixed = isChecked;
            }
            else if (id == R.id.using_mouse)
            {
                UpdateMouseMenu(isChecked);
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                        .putBoolean(Q3EPreference.pref_harm_using_mouse, isChecked)
                        .commit();
            }
            else if (id == R.id.readonly_command)
            {
                SetupCommandLine(isChecked);
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                        .putBoolean(PreferenceKey.READONLY_COMMAND, isChecked)
                        .commit();
            }
            else if (id == R.id.editable_temp_command)
            {
                SetupTempCommandLine(isChecked);
            }
            else if (id == R.id.collapse_mods)
            {
                CollapseMods(isChecked);
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                        .putBoolean(PreferenceKey.COLLAPSE_MODS, isChecked)
                        .commit();
            }
        }
    };

    private final RadioGroup.OnCheckedChangeListener m_groupCheckChangeListener = new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id)
        {
            int index;
            int rgId = radioGroup.getId();
            if (rgId == R.id.rg_fs_game ||  rgId == R.id.rg_fs_etwgame)
            {
                RadioButton checked = radioGroup.findViewById(id);
                SetGameDLL((String)checked.getTag());
            }

        }
    };

    private final View.OnClickListener m_buttonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            int id = view.getId();
            if (id == R.id.launcher_tab1_edit_autoexec)
            {
                EditFile("autoexec.cfg");
            }
            else if (id == R.id.launcher_tab1_edit_doomconfig)
            {
                EditFile(Q3EUtils.q3ei.config_name);
            }
            else if (id == R.id.launcher_tab1_game_data_chooser_button)
            {
                OpenFolderChooser();
            }
            else if (id == R.id.onscreen_button_setting)
            {
                OpenOnScreenButtonSetting();
            }
            else if (id == R.id.setup_onscreen_button_theme)
            {
                OpenOnScreenButtonThemeSetting();
            }
            else if (id == R.id.launcher_tab1_edit_cvar)
            {
                EditCVar();
            }
            else if (id == R.id.launcher_tab1_game_mod_button)
            {
                OpenGameModChooser();
            }
            else if (id == R.id.launcher_tab1_command_record)
            {
                OpenCommandChooser();
            }
        }
    };

    private final AdapterView.OnItemSelectedListener m_itemSelectedListener = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
        {
            int viewId = parent.getId();
            if(viewId == R.id.launcher_tab2_joystick_visible)
            {
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
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
                PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                        .putInt(Q3EPreference.pref_harm_joystick_visible, Q3EGlobals.ONSCRREN_JOYSTICK_VISIBLE_ALWAYS)
                        .commit();
            }
        }
    };

    private class SavePreferenceTextWatcher implements TextWatcher
    {
        private final String name;
        private final String defValue;
        private final Runnable runnable;

        public SavePreferenceTextWatcher(String name, String defValue)
        {
            this(name, defValue, null);
        }

        public SavePreferenceTextWatcher(String name, String defValue, Runnable runnable)
        {
            this.name = name;
            this.defValue = defValue;
            this.runnable = runnable;
        }

        public SavePreferenceTextWatcher(String name)
        {
            this(name, null);
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        public void afterTextChanged(Editable s)
        {
            String value = s.length() == 0 && null != defValue ? defValue : s.toString();
            PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                    .putString(name, value)
                    .commit();
            if(null != runnable)
                runnable.run();
        }
    }

    private class SaveFloatPreferenceTextWatcher implements TextWatcher
    {
        private final String name;
        private final String preference;
        private final float defValue;

        public SaveFloatPreferenceTextWatcher(String name, String preference, float defValue)
        {
            this.name = name;
            this.preference = preference;
            this.defValue = defValue;
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            SetProp(name, s);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        public void afterTextChanged(Editable s)
        {
            String value = s.length() == 0 ? "" + defValue : s.toString();
            Q3EPreference.SetFloatFromString(GameLauncher.this, preference, value, defValue);
        }
    }

    private class CommandTextWatcher implements TextWatcher
    {
        private boolean enabled;

        public boolean IsEnabled()
        {
            return enabled;
        }

        public void Install(boolean e)
        {
            enabled = e;
        }

        public void Uninstall()
        {
            enabled = false;
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            boolean cond = enabled && V.edt_cmdline.isInputMethodTarget() && !IsCmdUpdateLocked();
            if (cond)
                updatehacktings(false);
        }

        public void afterTextChanged(Editable s)
        {
            String value = s.length() == 0 ? Q3EGlobals.GAME_EXECUABLE : s.toString();
            PreferenceManager.getDefaultSharedPreferences(GameLauncher.this).edit()
                    .putString(Q3EUtils.q3ei.GetGameCommandPreferenceKey(), value)
                    .commit();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    }

    private final CommandTextWatcher m_commandTextWatcher = new CommandTextWatcher();

    private void InitUIDefaultLayout(Q3EInterface q3ei)
    {
        q3ei.defaults_table = Q3EControls.GetDefaultLayout(this, Q3EControls.CONST_DEFAULT_ON_SCREEN_BUTTON_FRIENDLY_EDGE, Q3EControls.CONST_DEFAULT_ON_SCREEN_BUTTON_SIZE_SCALE, Q3EControls.CONST_DEFAULT_ON_SCREEN_BUTTON_OPACITY, false);
    }

    public void InitGameProfile(String game)
    {
        // Q3EKeyCodes.InitD3Keycodes();
        Q3EInterface q3ei = new Q3EInterface();
        q3ei.standalone = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Q3EPreference.GAME_STANDALONE_DIRECTORY, true);

        q3ei.InitWET();

        InitUIDefaultLayout(q3ei);

        q3ei.default_path = default_gamedata;

        q3ei.SetupWET();

        // Q3EInterface.DumpDefaultOnScreenConfig(q3ei.arg_table, q3ei.type_table);

        q3ei.LoadTypeAndArgTablePreference(this);

        if(null != game && !game.isEmpty())
            q3ei.SetupGame(game);

        Q3EUtils.q3ei = q3ei;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
            CollapseMods(V.collapse_mods.isChecked());
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        InitUIDefaultLayout(Q3EUtils.q3ei);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        Q3EAd.LoadAds(this);
        super.onConfigurationChanged(newConfig);
    }

    public void support(View vw)
    {
        new SupportDeveloperFunc(this).Start(new Bundle());
    }

    public int GetCheckboxIndex(RadioGroup rg)
    {
        return GetCheckboxIndex(rg, rg.getCheckedRadioButtonId());
    }

    public int GetCheckboxIndex(RadioGroup rg, int id)
    {
        for (int i = 0, j = 0; i < rg.getChildCount(); i++)
        {
            View item = rg.getChildAt(i);
            if (item instanceof RadioButton)
            {
                if (item.getId() == id)
                {
                    return j;
                }
                j++;
            }
        }
        return -1;
        //return rg.indexOfChild(findViewById(rg.getCheckedRadioButtonId()));
    }

    public int GetCheckboxId(RadioGroup rg, int index)
    {
        for (int i = 0, j = 0; i < rg.getChildCount(); i++)
        {
            View item = rg.getChildAt(i);
            if (item instanceof RadioButton)
            {
                if (j == index)
                {
                    return item.getId();
                }
                j++;
            }
        }
        return -1;
        //return rg.getChildAt(index).getId();
    }

    public boolean getProp(String name)
    {
        Boolean b = Q3EUtils.q3ei.GetGameCommandEngine(GetCmdText()).GetBoolProp(name, false);
        // Boolean b = KidTech4Command.GetBoolProp(GetCmdText(), name, false);
        return null != b ? b : false;
    }

    public void setProp(String name, boolean val)
    {
        SetProp(name, KidTechCommand.btostr(val));
    }

    private void ThrowException()
    {
        ((String) null).toString();
    }

    private void UpdateUserGame(boolean on)
    {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(GameLauncher.this);
        String game = preference.getString(Q3EUtils.q3ei.GetGameModPreferenceKey(), "");
        if (null == game)
            game = "";

        GameManager.GameProp prop = m_gameManager.ChangeGameMod(game, true);
        if(prop.IsValid())
            SelectCheckbox(GetGameModRadioGroup(), prop.index);
        game = prop.fs_game;

        preference.edit().putString(Q3EPreference.pref_harm_game_lib, "").commit();
        if (on)
        {
            SetGameModToCommand(game);
            //RemoveProp("fs_game_base");
        }
        else
        {
            if (!prop.is_user && !game.isEmpty())
                SetGameModToCommand(game);
            else
                RemoveGameModFromCommand();
            //RemoveProp("fs_game_base");
        }
        V.edt_fs_game.setText(game);
        V.rg_fs_game.setEnabled(!on);
        V.rg_fs_etwgame.setEnabled(!on);
        V.fs_game_user.setText(on ? R.string.mod_ : R.string.user_mod);
        //V.launcher_tab1_game_lib_button.setEnabled(on);
        V.edt_fs_game.setEnabled(on);
        //V.launcher_tab1_user_game_layout.setVisibility(on ? View.VISIBLE : View.GONE);
    }

    private void UpdateEnableGyro(boolean on)
    {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        V.launcher_tab2_gyro_x_axis_sens.setText(Q3EPreference.GetStringFromFloat(preference, Q3EPreference.pref_harm_view_motion_gyro_x_axis_sens, Q3EControlView.GYROSCOPE_X_AXIS_SENS));
        V.launcher_tab2_gyro_y_axis_sens.setText(Q3EPreference.GetStringFromFloat(preference, Q3EPreference.pref_harm_view_motion_gyro_y_axis_sens, Q3EControlView.GYROSCOPE_Y_AXIS_SENS));
        // only disable
        V.launcher_tab2_gyro_x_axis_sens.setEnabled(on);
        V.launcher_tab2_gyro_y_axis_sens.setEnabled(on);
    }

    public void UpdateMouseMenu(boolean show)
    {
        final boolean supportGrabMouse = Q3EUtils.SupportMouse() == Q3EGlobals.MOUSE_EVENT;

    }

    private void SetupCommandLine(boolean readonly)
    {
        UIUtility.EditText__SetReadOnly(V.edt_cmdline, readonly, InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    private void SetupTempCommandLine(boolean editable)
    {
        UIUtility.EditText__SetReadOnly(V.edt_cmdline_temp, !editable, InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    private void CollapseMods(boolean on)
    {
        final int VisibleRadioCount = 4;
        RadioGroup radioGroup = null;
        int childCount = V.mods_container_layout.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            View view = V.mods_container_layout.getChildAt(i);
            if(view.getVisibility() == View.VISIBLE)
            {
                int radioCount = ((RadioGroup)view).getChildCount();
                if(radioCount > VisibleRadioCount)
                {
                    radioGroup = (RadioGroup)view;
                }
                break;
            }
        }

        ViewGroup.LayoutParams layoutParams = V.mods_container.getLayoutParams();
        if(null == radioGroup)
        {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            V.mods_container.setLayoutParams(layoutParams);
            V.mods_container.setNestedScrollingEnabled(false);
            return;
        }

        if(on)
        {
            int height = 0;
            for (int m = 0; m < VisibleRadioCount; m++)
            {
                View radio = radioGroup.getChildAt(m);
                height += Math.max(radio.getHeight(), radio.getMeasuredHeight());
            }
            layoutParams.height = height;
        }
        else
        {
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        V.mods_container.setLayoutParams(layoutParams);
        V.mods_container.setNestedScrollingEnabled(on);
    }

    public void SelectCheckbox(RadioGroup rg, int index)
    {
        for (int i = 0, j = 0; i < rg.getChildCount(); i++)
        {
            View item = rg.getChildAt(i);
            if (item instanceof RadioButton)
            {
                if (j == index)
                {
                    rg.check(item.getId());
                    return;
                }
                j++;
            }
        }
        //rg.check(rg.getChildAt(index).getId());
    }

    private RadioGroup GetGameModRadioGroup()
    {
        if(Q3EUtils.q3ei.isETW)
            return V.rg_fs_etwgame;
        else
            return null;
    }

    private void SetGameModToCommand(String mod)
    {
        String arg = Q3EUtils.q3ei.GetGameCommandParm();
        SetProp(arg, mod);
    }

    private void SetProp(String name, Object val)
    {
        if(!LockCmdUpdate())
            return;
        SetCmdText(Q3EUtils.q3ei.GetGameCommandEngine(GetCmdText()).SetProp(name, val).toString());
        //SetCmdText(KidTech4Command.SetProp(GetCmdText(), name, val));
        UnlockCmdUpdate();
    }

    private boolean LockCmdUpdate()
    {
        if(m_cmdUpdateLock)
            return false;
        m_cmdUpdateLock = true;
        return true;
    }

    private void SetCmdText(String text)
    {
        if(null == text || text.isEmpty())
            text = Q3EGlobals.GAME_EXECUABLE;
        EditText edit = V.edt_cmdline;
        if (edit.getText().toString().equals(text))
            return;
        int pos = edit.getSelectionStart();
        edit.setText(text);
        pos = Math.max(0, Math.min(pos, text.length()));
        try
        {
            edit.setSelection(pos);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String GetCmdText()
    {
        String s = V.edt_cmdline.getText().toString();
        if(s.isEmpty())
            s = Q3EGlobals.GAME_EXECUABLE;
        return s;
    }

    private void UnlockCmdUpdate()
    {
        m_cmdUpdateLock = false;
    }

    private void RemoveGameModFromCommand()
    {
        String arg = Q3EUtils.q3ei.GetGameCommandParm();
        RemoveProp(arg);
    }

    private void RemoveProp(String name)
    {
        if(!LockCmdUpdate())
            return;
        String orig = GetCmdText();
        String str = Q3EUtils.q3ei.GetGameCommandEngine(orig).RemoveProp(name).toString();
        if (!orig.equals(str))
            SetCmdText(str);
        UnlockCmdUpdate();
    }

    private void SetGameDLL(String val)
    {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean userMod = V.fs_game_user.isChecked(); //preference.getBoolean(Q3EUtils.q3ei.GetEnableModPreferenceKey(), false);
        String game = null != val ? val : "";
        GameManager.GameProp prop = m_gameManager.ChangeGameMod(game, userMod);
        HandleGameProp(prop);

        if (userMod)
        {
            V.edt_fs_game.setText(prop.fs_game);
        }
        preference.edit().putString(Q3EUtils.q3ei.GetGameModPreferenceKey(), game).commit();
    }

    private void HandleGameProp(GameManager.GameProp prop)
    {
        if(prop.is_user)
        {
            if (!prop.fs_game.isEmpty())
                SetGameModToCommand(prop.fs_game);
            else
                RemoveGameModFromCommand();
            RemoveProp("fs_game_base");
            RemoveProp("harm_fs_gameLibPath");
        }
        else
        {
            if(null == prop.fs_game || prop.fs_game.isEmpty() || !prop.IsValid())
                RemoveGameModFromCommand();
            else
                SetGameModToCommand(prop.fs_game);
            if(null == prop.fs_game_base || prop.fs_game_base.isEmpty() || !prop.IsValid())
                RemoveProp("fs_game_base");
            else
                SetProp("fs_game_base", prop.fs_game_base);
            RemoveProp("harm_fs_gameLibPath");
        }
    }

    private void EditFile(String file)
    {
        if (null == m_editConfigFileFunc)
            m_editConfigFileFunc = new EditConfigFileFunc(this, CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_EDIT_CONFIG_FILE);

        Bundle bundle = new Bundle();
        String game = GetGameModFromCommand();
        if (game == null || game.isEmpty())
            game = Q3EUtils.q3ei.game_base;
        String path = KStr.AppendPath(V.edt_path.getText().toString(), Q3EUtils.q3ei.subdatadir, Q3EUtils.q3ei.GetGameHomeDirectoryPath());
        bundle.putString("game", game);
        bundle.putString("path", path);
        bundle.putString("file", file);
        m_editConfigFileFunc.Start(bundle);
    }

    private String GetGameModFromCommand()
    {
        String arg = Q3EUtils.q3ei.GetGameCommandParm();
        return GetProp(arg);
    }

    private String GetProp(String name)
    {
        return Q3EUtils.q3ei.GetGameCommandEngine(GetCmdText()).Prop(name);
        // return KidTech4Command.GetProp(GetCmdText(), name);
    }

    private void OpenFolderChooser()
    {
        if (null == m_chooseGameFolderFunc)
            m_chooseGameFolderFunc = new ChooseGameFolderFunc(this, CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_FOLDER, CONST_RESULT_CODE_ACCESS_ANDROID_DATA, new Runnable()
            {
                @Override
                public void run()
                {
                    V.edt_path.setText(m_chooseGameFolderFunc.GetResult());
                    OpenSuggestGameWorkingDirectory(V.edt_path.getText().toString());
                }
            });
        Bundle bundle = new Bundle();
        bundle.putString("path", V.edt_path.getText().toString());
        m_chooseGameFolderFunc.Start(bundle);
    }

    public void OpenSuggestGameWorkingDirectory(String curPath)
    {
        if(ContextUtility.InScopedStorage() && !ContextUtility.IsInAppPrivateDirectory(GameLauncher.this, curPath))
        {
            String path = Q3EUtils.GetAppStoragePath(GameLauncher.this);
            Toast.makeText(GameLauncher.this, Q3ELang.tr(this, R.string.suggest_game_woring_directory_tips, path), Toast.LENGTH_LONG).show();
        }
        m_edtPathFocused = curPath;
    }

    private void OpenOnScreenButtonSetting()
    {
        Intent intent = new Intent(this, OnScreenButtonConfigActivity.class);
        startActivity(intent);
    }

    private void OpenOnScreenButtonThemeSetting()
    {
        new SetupControlsThemeFunc(this).Start(new Bundle());
    }

    private void EditCVar()
    {
        Bundle bundle = new Bundle();
        bundle.putString("game", GetGameModFromCommand());
        bundle.putString("command", Q3EUtils.q3ei.start_temporary_extra_command);
        bundle.putString("baseCommand", GetTempBaseCommand());
        CVarEditorFunc cVarEditorFunc = new CVarEditorFunc(this, new Runnable()
        {
            @Override
            public void run()
            {
                Q3EUtils.q3ei.start_temporary_extra_command = CVarEditorFunc.GetResultFromBundle(bundle);
                UpdateTempCommand();
            }
        });
        cVarEditorFunc.Start(bundle);
    }

    private String GetTempBaseCommand()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GameLauncher.this);
        String tempCmd = "";
        boolean skipIntro = preferences.getBoolean(Q3EPreference.pref_harm_skip_intro, false);
        if (skipIntro && (Q3EUtils.q3ei.IsIdTech3()))
            tempCmd += " +disconnect";
        return tempCmd.trim();
    }

    private void UpdateTempCommand()
    {
        if(Q3EUtils.q3ei.start_temporary_extra_command.equals(GetTempCmdText()))
            return;

        V.edt_cmdline_temp.setText(Q3EUtils.q3ei.start_temporary_extra_command);
        V.temp_cmdline.setVisibility(Q3EUtils.q3ei.start_temporary_extra_command.length() > 0 ? View.VISIBLE : View.GONE);
    }

    private String GetTempCmdText()
    {
        return V.edt_cmdline_temp.getText().toString();
    }

    private void OpenGameModChooser()
    {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        String preferenceKey = Q3EUtils.q3ei.GetGameModPreferenceKey();
        if (null == m_chooseGameModFunc)
        {
            m_chooseGameModFunc = new ChooseGameModFunc(this, CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_GAME_MOD);
        }

        m_chooseGameModFunc.SetCallback(new Runnable()
        {
            @Override
            public void run()
            {
                String mod = m_chooseGameModFunc.GetResult();
                if(mod.startsWith(":"))
                {
                    if(":".equals(mod))
                    {
                        RemoveParam("file");
                        RemoveParam("deh");
                        RemoveParam("bex");
                    }
                    else
                    {
                        String files = mod.substring(1);
                        String[] split = files.split("\\s+");
                        StringBuilder file = new StringBuilder();
                        StringBuilder deh = new StringBuilder();
                        StringBuilder bex = new StringBuilder();

                        for (String s : split)
                        {
                            if(s.toLowerCase().endsWith(".deh"))
                                deh.append(" ").append(s);
                            else if(s.toLowerCase().endsWith(".bex"))
                                bex.append(" ").append(s);
                            else
                                file.append(" ").append(s);
                        }

                        RemoveParam("file");
                        RemoveParam("deh");
                        RemoveParam("bex");

                        if(file.length() > 0)
                            SetParam("file", file.toString().trim());
                        if(deh.length() > 0)
                            SetParam("deh", deh.toString().trim());
                        if(bex.length() > 0)
                            SetParam("bex", bex.toString().trim());
                    }
                }
                else
                    V.edt_fs_game.setText(mod);
            }
        });
        Bundle bundle = new Bundle();
        String path = KStr.AppendPath(preference.getString(Q3EPreference.pref_datapath, default_gamedata), Q3EUtils.q3ei.subdatadir, Q3EUtils.q3ei.GetGameModSubDirectory());
        bundle.putString("mod", preference.getString(preferenceKey, ""));
        bundle.putString("path", path);
        bundle.putString("file", GetParam("file") + " " + GetParam("deh") + " " + GetParam("bex"));
        m_chooseGameModFunc.Start(bundle);
    }

    private void RemoveParam(String name)
    {
        if(!LockCmdUpdate())
            return;
        String orig = GetCmdText();
        String str = Q3EUtils.q3ei.GetGameCommandEngine(orig).RemoveParam(name).toString();
        if (!orig.equals(str))
            SetCmdText(str);
        UnlockCmdUpdate();
    }

    private void SetParam(String name, Object val)
    {
        if(!LockCmdUpdate())
            return;
        SetCmdText(Q3EUtils.q3ei.GetGameCommandEngine(GetCmdText()).SetParam(name, val).toString());
        // SetCmdText(KidTech4Command.SetParam(GetCmdText(), name, val));
        UnlockCmdUpdate();
    }

    private String GetParam(String name)
    {
        return Q3EUtils.q3ei.GetGameCommandEngine(GetCmdText()).Param(name);
        // return KidTech4Command.GetParam(GetCmdText(), name);
    }

    private void OpenCommandChooser()
    {
        final String PreferenceKey = Q3EUtils.q3ei.GetGameCommandRecordPreferenceKey();
        String cmd = GetCmdText();
        if (null == m_chooseCommandRecordFunc)
        {
            m_chooseCommandRecordFunc = new ChooseCommandRecordFunc(this, new Runnable()
            {
                @Override
                public void run()
                {
                    String cmdResult = m_chooseCommandRecordFunc.GetResult();
                    if(null != cmdResult && !cmd.equals(cmdResult))
                        SetCmdText(cmdResult);
                }
            });
        }
        Bundle bundle = new Bundle();
        bundle.putString("command", cmd);
        bundle.putString("key", PreferenceKey);
        m_chooseCommandRecordFunc.Start(bundle);
    }

    public String GetDefaultGameDirectory()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
            return Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName();
        else
            return default_gamedata;
    }

    public GameManager GetGameManager()
    {
        return m_gameManager;
    }

    private boolean IsCmdUpdateLocked()
    {
        return m_cmdUpdateLock;
    }

    public void updatehacktings(boolean all)
    {
        LockCmdUpdate();
        String str;

        if(all) // only for idTech4 games
        {
            return;
        }

        // game mods for every games
        str = GetGameModFromCommand();
        if (str != null)
        {
            if (!V.fs_game_user.isChecked())
            {
                GameManager.GameProp prop = m_gameManager.ChangeGameMod(str, false);
                if(prop.IsValid())
                {
                    SelectCheckbox(GetGameModRadioGroup(), prop.index);
                }
            }
            else
            {
                String cur = V.edt_fs_game.getText().toString();
                if (!str.equals(cur))
                    V.edt_fs_game.setText(str);
            }
        }
        else
        {
            SelectCheckbox(GetGameModRadioGroup(), -1);
        }

        UnlockCmdUpdate();
    }

    private void AfterCreated()
    {
        try
        {
            Q3EAd.LoadAds(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public void start(View vw)
    {
        WritePreferences();

        if (null == m_startGameFunc)
            m_startGameFunc = new StartGameFunc(this, CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_START);
        Bundle bundle = new Bundle();
        bundle.putString("path", V.edt_path.getText().toString());
        m_startGameFunc.Start(bundle);
    }

    public void controls(View vw)
    {
        //startActivity(new Intent(this, Q3EUiConfig.class));
    }

    private void WritePreferences()
    {
        SharedPreferences.Editor mEdtr = PreferenceManager.getDefaultSharedPreferences(this).edit();
        mEdtr.putString(Q3EUtils.q3ei.GetGameCommandPreferenceKey(), GetCmdText());
        mEdtr.putString(Q3EPreference.pref_datapath, V.edt_path.getText().toString());
        mEdtr.putBoolean(Q3EPreference.pref_hideonscr, V.hideonscr.isChecked());
        mEdtr.putBoolean(Q3EPreference.pref_harm_using_mouse, V.using_mouse.isChecked());

        mEdtr.putBoolean(Q3EPreference.pref_analog, V.smoothjoy.isChecked());
        mEdtr.putBoolean(Q3EPreference.pref_2fingerlmb, V.secfinglmb.isChecked());
        mEdtr.putBoolean(Q3EUtils.q3ei.GetEnableModPreferenceKey(), V.fs_game_user.isChecked());
        mEdtr.putBoolean(Q3EPreference.pref_harm_view_motion_control_gyro, V.launcher_tab2_enable_gyro.isChecked());
        mEdtr.putFloat(Q3EPreference.pref_harm_view_motion_gyro_x_axis_sens, Q3EUtils.parseFloat_s(V.launcher_tab2_gyro_x_axis_sens.getText().toString(), Q3EControlView.GYROSCOPE_X_AXIS_SENS));
        mEdtr.putFloat(Q3EPreference.pref_harm_view_motion_gyro_y_axis_sens, Q3EUtils.parseFloat_s(V.launcher_tab2_gyro_y_axis_sens.getText().toString(), Q3EControlView.GYROSCOPE_Y_AXIS_SENS));
        mEdtr.putBoolean(Q3EPreference.pref_harm_joystick_unfixed, V.launcher_tab2_joystick_unfixed.isChecked());
        mEdtr.putInt(Q3EPreference.pref_harm_joystick_visible, getResources().getIntArray(R.array.joystick_visible_mode_values)[V.launcher_tab2_joystick_visible.getSelectedItemPosition()]);
        mEdtr.putBoolean(PreferenceKey.READONLY_COMMAND, V.readonly_command.isChecked());
        mEdtr.putBoolean(PreferenceKey.COLLAPSE_MODS, V.collapse_mods.isChecked());

        // mEdtr.putString(Q3EUtils.q3ei.GetGameModPreferenceKey(), V.edt_fs_game.getText().toString());
        mEdtr.commit();
    }

    public boolean getProp(String name, boolean defaultValueIfNotExists)
    {
        String val = GetProp(name);
        if (val != null && !val.trim().isEmpty())
            return "1".equals(val);
        return defaultValueIfNotExists;
    }

    private boolean IsProp(String name)
    {
        return Q3EUtils.q3ei.GetGameCommandEngine(GetCmdText()).IsProp(name);
        // return KidTech4Command.IsProp(GetCmdText(), name );
    }

    private void SetupCommandTextWatcher(boolean b)
    {
        if(b)
        {
            V.edt_cmdline.addTextChangedListener(m_commandTextWatcher);
            m_commandTextWatcher.Install(true);
        }
        else
        {
            m_commandTextWatcher.Uninstall();
            V.edt_cmdline.removeTextChangedListener(m_commandTextWatcher);
        }
    }

    private void SetGame(String game)
    {
        Q3EUtils.q3ei.SetupGame(game);
        V.launcher_tab1_edit_doomconfig.setText(getString(R.string.edit_) + Q3EUtils.q3ei.config_name);

        boolean etwVisible = false;

        boolean modVisible = true;

        if (Q3EUtils.q3ei.isETW)
        {
            etwVisible = true;
        }

        V.rg_fs_etwgame.setVisibility(etwVisible ? View.VISIBLE : View.GONE);

        V.mod_section.setVisibility(modVisible ? View.VISIBLE : View.GONE);

        String subdir = Q3EUtils.q3ei.subdatadir;
        if(null == subdir)
            subdir = "";
        else
            subdir += "/";
        V.launcher_fs_game_cvar.setText("(" + Q3EUtils.q3ei.GetGameCommandParm() + ")");
        V.launcher_fs_game_subdir.setText(Q3ELang.tr(this, R.string.sub_directory) + subdir);
        V.launcher_fs_game_subdir.setVisibility(subdir.isEmpty() ? View.GONE : View.VISIBLE);

        CollapseMods(V.collapse_mods.isChecked());
    }

    private void OpenSettings()
    {
        LauncherSettingsDialog dialog = LauncherSettingsDialog.newInstance();
        dialog.show(getSupportFragmentManager(), "LauncherSettingsDialog");
    }

    private void ChangeGame(String... games)
    {
        LockCmdUpdate();
        SetupCommandTextWatcher(false);
        String newGame = games.length > 0 ? games[0] : null;
        if (null == newGame || newGame.isEmpty())
        {
            int i;
            for (i = 0; i < GameManager.Games.length; i++)
            {
                if (GameManager.Games[i].equalsIgnoreCase(Q3EUtils.q3ei.game))
                    break;
            }
            if (i >= GameManager.Games.length)
                i = GameManager.Games.length - 1;
            newGame = GameManager.Games[(i + 1) % GameManager.Games.length];
        }
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        preference.edit().putString(Q3EPreference.pref_harm_game, newGame).commit();
        SetGame(newGame);
        preference.edit().putString(Q3EPreference.pref_harm_game_lib, "").commit();

        String cmd = preference.getString(Q3EUtils.q3ei.GetGameCommandPreferenceKey(), Q3EGlobals.GAME_EXECUABLE);
        V.edt_cmdline.setText(cmd);
        SetupCommandTextWatcher(true);
        UnlockCmdUpdate();

        // if is DOOM3/Quake4/Prey, update launcher
        updatehacktings(false);

        // put last
        String game = preference.getString(Q3EUtils.q3ei.GetGameModPreferenceKey(), "");
        if (null == game)
            game = "";
        V.edt_fs_game.setText(game);
        boolean userMod = preference.getBoolean(Q3EUtils.q3ei.GetEnableModPreferenceKey(), false);
        V.fs_game_user.setChecked(userMod);

        GameManager.GameProp prop = m_gameManager.ChangeGameMod(game, userMod);
        HandleGameProp(prop);
        SelectCheckbox(GetGameModRadioGroup(), prop.index);

        Q3EUtils.q3ei.start_temporary_extra_command = GetTempBaseCommand();
        UpdateTempCommand();
    }

    private void RemoveParam_temp(String name)
    {
        String orig = GetTempCmdText();
        String str = Q3EUtils.q3ei.GetGameCommandEngine(orig).RemoveParam(name).toString();
        // String str = KidTech4Command.RemoveParam(GetTempCmdText(), name, res);
        if (!orig.equals(str))
            Q3EUtils.q3ei.start_temporary_extra_command = str;
        UpdateTempCommand();
    }

    private void SetParam_temp(String name, Object val)
    {
        String str = Q3EUtils.q3ei.GetGameCommandEngine(GetTempCmdText()).SetParam(name, val).toString();
        // String str = KidTech4Command.SetParam(GetTempCmdText(), name, val);
        Q3EUtils.q3ei.start_temporary_extra_command = str;
        UpdateTempCommand();
    }

    private void SetCommand_temp(String name, boolean prepend)
    {
        String str = Q3EUtils.q3ei.GetGameCommandEngine(GetTempCmdText()).SetCommand(name, prepend).toString();
        // String str = KidTech4Command.SetCommand(GetTempCmdText(), name, prepend);
        Q3EUtils.q3ei.start_temporary_extra_command = str;
        UpdateTempCommand();
    }

    private void RemoveCommand_temp(String name)
    {
        String orig = GetTempCmdText();
        String str = Q3EUtils.q3ei.GetGameCommandEngine(orig).RemoveCommand(name).toString();
        // String str = KidTech4Command.RemoveCommand(GetTempCmdText(), name, res);
        if (!orig.equals(str))
            Q3EUtils.q3ei.start_temporary_extra_command = str;
        UpdateTempCommand();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void InitGameList()
    {
        Map<String, RadioGroup> groups = new HashMap<>();
        RadioButton radio;
        RadioGroup group;
        RadioGroup.LayoutParams layoutParams;

        groups.put(Q3EGlobals.GAME_ETW, V.rg_fs_etwgame);
        Game[] values = Game.values();

        for (Game value : values)
        {
            String subdir = "";

            if(Q3EUtils.q3ei.standalone)
            {
                subdir = Q3EInterface.GetGameStandaloneDirectory(value.type);
                if(!subdir.isEmpty())
                    subdir += "/";
            }

            group = groups.get(value.type);
            layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            radio = new RadioButton(group.getContext());
            String name;
            if(value.name instanceof Integer)
                name = Q3ELang.tr(this, (Integer)value.name);
            else if(value.name instanceof String)
                name = (String)value.name;
            else
                name = "";
            name += "(" + subdir + value.game + ")";
            radio.setText(name);
            radio.setTag(value.game);
            group.addView(radio, layoutParams);
            radio.setChecked(!value.is_mod);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        if (itemId == R.id.main_menu_settings)
        {
            OpenSettings();
            return true;
        }
        else if (itemId == android.R.id.home)
        {
            ChangeGame(Q3EGlobals.GAME_ETW);
            return true;
        }
		/*else if (itemId == R.id.main_menu_move_game_data)
		{
			MoveGameDataToAppPrivateDirectory();
			return true;
		}*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tablayout);
        viewPager2 = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case CONST_RESULT_CODE_ACCESS_ANDROID_DATA:
                    ContextUtility.PersistableUriPermission(this, data.getData());
                    break;
            }
        }
        else
        {
            // Android SDK > 28: resultCode is RESULT_CANCELED
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) // Android 11 FS permission
            {
                if(requestCode == CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_START
                        || requestCode == CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_EDIT_CONFIG_FILE
                        || requestCode == CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_FOLDER
                        || requestCode == CONST_RESULT_CODE_REQUEST_EXTRACT_PATCH_RESOURCE
                        || requestCode == CONST_RESULT_CODE_REQUEST_BACKUP_PREFERENCES_CHOOSE_FILE
                        || requestCode == CONST_RESULT_CODE_REQUEST_RESTORE_PREFERENCES_CHOOSE_FILE
                        || requestCode == CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_GAME_LIBRARY
                        || requestCode == CONST_RESULT_CODE_REQUEST_ADD_EXTERNAL_GAME_LIBRARY
                        || requestCode == CONST_RESULT_CODE_REQUEST_EDIT_EXTERNAL_GAME_LIBRARY
                        || requestCode == CONST_RESULT_CODE_REQUEST_EXTRACT_SOURCE
                        || requestCode == CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_GAME_MOD
                )
                {
                    List<String> list = null;
                    if (!Environment.isExternalStorageManager()) // reject
                    {
                        list = Collections.singletonList(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    }
                    HandleGrantPermissionResult(requestCode, list);
                }
            }
        }
    }

    private void HandleGrantPermissionResult(int requestCode, List<String> list)
    {
        if (null == list || list.isEmpty())
        {
            switch (requestCode)
            {
                case CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_EDIT_CONFIG_FILE:
                    if (null != m_editConfigFileFunc)
                        m_editConfigFileFunc.run();
                    break;
                case CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_FOLDER:
                    if (null != m_chooseGameFolderFunc)
                        m_chooseGameFolderFunc.run();
                    break;
                case CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_START:
                    if (null != m_startGameFunc)
                        m_startGameFunc.run();
                    break;
                case CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_GAME_MOD:
                    if (null != m_chooseGameModFunc)
                        m_chooseGameModFunc.run();
                    break;
                default:
                    break;
            }
            return;
        }
        // not grant
        String opt;
        switch (requestCode)
        {
            case CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_START:
                opt = Q3ELang.tr(this, R.string.start_game);
                break;
            case CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_EDIT_CONFIG_FILE:
                opt = Q3ELang.tr(this, R.string.edit_config_file);
                break;
            case CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_CHOOSE_FOLDER:
                opt = Q3ELang.tr(this, R.string.choose_data_folder);
                break;
            default:
                opt = Q3ELang.tr(this, R.string.operation);
                break;
        }
        StringBuilder sb = new StringBuilder();
        String endl = TextHelper.GetDialogMessageEndl();
        for (String str : list)
        {
            if (str != null)
                sb.append("  * ").append(str);
            sb.append(endl);
        }
        AlertDialog.Builder builder = ContextUtility.CreateMessageDialogBuilder(this, opt + " " + Q3ELang.tr(this, R.string.request_necessary_permissions), TextHelper.GetDialogMessage(sb.toString()));
        builder.setNeutralButton(R.string.grant, new AlertDialog.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ContextUtility.OpenAppSetting(GameLauncher.this);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> list = null;
        for (int i = 0; i < permissions.length; i++)
        {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
            {
                if (list == null)
                    list = new ArrayList<>();
                list.add(permissions[i]);
            }
        }

        HandleGrantPermissionResult(requestCode, list);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private class ViewHolder {
        public EditText edt_cmdline;
        public RadioGroup rg_fs_game;
        public EditText edt_fs_game;
        public EditText edt_path;
        public CheckBox hideonscr;
        public CheckBox secfinglmb;
        public CheckBox smoothjoy;
        public Button launcher_tab1_edit_autoexec;
        public Button launcher_tab1_edit_doomconfig;
        public Button launcher_tab1_game_data_chooser_button;
        public Button onscreen_button_setting;
        public LinearLayout launcher_tab1_user_game_layout;
        public CheckBox fs_game_user;
        public View main_ad_layout;
        public CheckBox launcher_tab2_enable_gyro;
        public LinearLayout launcher_tab2_enable_gyro_layout;
        public EditText launcher_tab2_gyro_x_axis_sens;
        public EditText launcher_tab2_gyro_y_axis_sens;
        public CheckBox launcher_tab2_joystick_unfixed;
        public Button setup_onscreen_button_theme;
        public CheckBox using_mouse;
        public Button launcher_tab1_edit_cvar;
        public EditText edt_cmdline_temp;
        public Button launcher_tab1_game_mod_button;
        public Switch readonly_command;
        public Switch editable_temp_command;
        public LinearLayout temp_cmdline;
        public LinearLayout mod_section;
        public RadioGroup rg_fs_etwgame;
        public Spinner launcher_tab2_joystick_visible;
        public TextView launcher_fs_game_subdir;
        public TextView launcher_fs_game_cvar;
        public Button launcher_tab1_command_record;
        public Switch collapse_mods;
        public NestedScrollView mods_container;
        public LinearLayout mods_container_layout;

        public void Setup()
        {
            edt_cmdline = findViewById(R.id.edt_cmdline);
            rg_fs_game = findViewById(R.id.rg_fs_game);
            edt_fs_game = findViewById(R.id.edt_fs_game);
            edt_path = findViewById(R.id.edt_path);
            hideonscr = findViewById(R.id.hideonscr);
            secfinglmb = findViewById(R.id.secfinglmb);
            smoothjoy = findViewById(R.id.smoothjoy);
            launcher_tab1_edit_autoexec = findViewById(R.id.launcher_tab1_edit_autoexec);
            launcher_tab1_edit_doomconfig = findViewById(R.id.launcher_tab1_edit_doomconfig);
            launcher_tab1_game_data_chooser_button = findViewById(R.id.launcher_tab1_game_data_chooser_button);
            onscreen_button_setting = findViewById(R.id.onscreen_button_setting);
            launcher_tab1_user_game_layout = findViewById(R.id.launcher_tab1_user_game_layout);
            fs_game_user = findViewById(R.id.fs_game_user);
            main_ad_layout = findViewById(R.id.main_ad_layout);
            launcher_tab2_enable_gyro = findViewById(R.id.launcher_tab2_enable_gyro);
            launcher_tab2_enable_gyro_layout = findViewById(R.id.launcher_tab2_enable_gyro_layout);
            launcher_tab2_gyro_x_axis_sens = findViewById(R.id.launcher_tab2_gyro_x_axis_sens);
            launcher_tab2_gyro_y_axis_sens = findViewById(R.id.launcher_tab2_gyro_y_axis_sens);
            launcher_tab2_joystick_unfixed = findViewById(R.id.launcher_tab2_joystick_unfixed);
            setup_onscreen_button_theme = findViewById(R.id.setup_onscreen_button_theme);
            using_mouse = findViewById(R.id.using_mouse);
            launcher_tab1_edit_cvar = findViewById(R.id.launcher_tab1_edit_cvar);
            edt_cmdline_temp = findViewById(R.id.edt_cmdline_temp);
            launcher_tab1_game_mod_button = findViewById(R.id.launcher_tab1_game_mod_button);
			readonly_command = findViewById(R.id.readonly_command);
            editable_temp_command = findViewById(R.id.editable_temp_command);
            temp_cmdline = findViewById(R.id.temp_cmdline);
            mod_section = findViewById(R.id.mod_section);
            launcher_tab2_joystick_visible = findViewById(R.id.launcher_tab2_joystick_visible);
            launcher_fs_game_subdir = findViewById(R.id.launcher_fs_game_subdir);
            launcher_fs_game_cvar = findViewById(R.id.launcher_fs_game_cvar);
            launcher_tab1_command_record = findViewById(R.id.launcher_tab1_command_record);
            collapse_mods = findViewById(R.id.collapse_mods);
            mods_container = findViewById(R.id.mods_container);
            mods_container_layout = findViewById(R.id.mods_container_layout);
            rg_fs_etwgame = findViewById(R.id.rg_fs_etwgame);
        }
    }
}