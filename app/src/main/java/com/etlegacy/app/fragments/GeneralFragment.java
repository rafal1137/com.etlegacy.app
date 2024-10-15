package com.etlegacy.app.fragments;

import static com.etlegacy.app.GameLauncher.default_gamedata;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.etlegacy.app.GameLauncher;
import com.etlegacy.app.R;
import com.etlegacy.app.launcher.EditConfigFileFunc;
import com.etlegacy.app.lib.ContextUtility;
import com.etlegacy.app.q3e.Q3EGlobals;
import com.etlegacy.app.q3e.Q3EPreference;
import com.etlegacy.app.q3e.Q3EUtils;
import com.etlegacy.app.q3e.karin.KStr;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class GeneralFragment extends Fragment {
    private static final int CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_EDIT_CONFIG_FILE = 2;

    public View view;
    public EditText edt_cmdline;
    public EditText edt_path;
    public CheckBox fs_game_user;
    public EditText edt_fs_game;
    public Button launcher_tab1_game_data_chooser_button;
    public Button launcher_tab1_command_record;
    public Button launcher_tab1_edit_cvar;
    public Button launcher_tab1_edit_autoexec;
    public Button launcher_tab1_edit_doomconfig;

    private String m_edtPathFocused = "";
    private EditConfigFileFunc m_editConfigFileFunc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_general, container, false);
        edt_cmdline = (EditText) view.findViewById(R.id.edt_cmdline);
        edt_path = (EditText) view.findViewById(R.id.edt_path);
        fs_game_user = (CheckBox) view.findViewById(R.id.fs_game_user);
        edt_fs_game = (EditText) view.findViewById(R.id.edt_fs_game);
        launcher_tab1_game_data_chooser_button = (Button) view.findViewById(R.id.launcher_tab1_game_data_chooser_button);
        launcher_tab1_command_record = (Button) view.findViewById(R.id.launcher_tab1_command_record);
        launcher_tab1_edit_cvar = (Button) view.findViewById(R.id.launcher_tab1_edit_cvar);
        launcher_tab1_edit_autoexec = (Button) view.findViewById(R.id.launcher_tab1_edit_autoexec);
        launcher_tab1_edit_doomconfig = (Button) view.findViewById(R.id.launcher_tab1_edit_doomconfig);

        final CompoundButton.OnCheckedChangeListener m_checkboxChangeListener = new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int id = buttonView.getId();
                if (id == R.id.fs_game_user)
                {
                    UpdateUserGame(isChecked);
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                            .putBoolean(Q3EPreference.fs_game_user, isChecked)
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
                if (id == R.id.launcher_tab1_edit_autoexec)
                {
                    EditFile("autoexec.cfg");
                }
                else if (id == R.id.launcher_tab1_edit_doomconfig)
                {
                    EditFile("etconfig.cfg");
                }
                else if (id == R.id.launcher_tab1_game_data_chooser_button)
                {
                    //OpenFolderChooser();
                }
                else if (id == R.id.launcher_tab1_edit_cvar)
                {
                    //EditCVar();
                }
                else if (id == R.id.launcher_tab1_command_record)
                {
                    //OpenCommandChooser();
                }
            }
        };


        edt_cmdline.setText(Q3EGlobals.GAME_EXECUABLE);
        edt_path.setText(default_gamedata);
        m_edtPathFocused = edt_path.getText().toString();
        if(ContextUtility.InScopedStorage())
        {
            edt_path.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    String curPath = edt_path.getText().toString();
                    if(curPath.equals(m_edtPathFocused))
                        return;
                    if(!hasFocus)
                    {
                        //OpenSuggestGameWorkingDirectory(curPath);
                    }
                }
            });
        }
        fs_game_user.setChecked(fs_game_user.isChecked());
        UpdateUserGame(fs_game_user.isChecked());
        fs_game_user.setOnCheckedChangeListener(m_checkboxChangeListener);
        edt_fs_game.addTextChangedListener(new TextWatcher()
        {
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (fs_game_user.isChecked())
                    SetGameModToCommand(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            public void afterTextChanged(Editable s)
            {
            }
        });
        launcher_tab1_game_data_chooser_button.setOnClickListener(m_buttonClickListener);
        launcher_tab1_command_record.setOnClickListener(m_buttonClickListener);
        launcher_tab1_edit_cvar.setOnClickListener(m_buttonClickListener);
        launcher_tab1_edit_autoexec.setOnClickListener(m_buttonClickListener);
        launcher_tab1_edit_doomconfig.setOnClickListener(m_buttonClickListener);

        return view;
    }

    private void SetCmdText(String text)
    {
        if(null == text || text.isEmpty())
            text = Q3EGlobals.GAME_EXECUABLE;
        EditText edit = edt_cmdline;
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
        String s = edt_cmdline.getText().toString();
        if(s.isEmpty())
            s = Q3EGlobals.GAME_EXECUABLE;
        return s;
    }

    private void SetProp(String name, Object val)
    {
        SetCmdText(Q3EUtils.q3ei.GetGameCommandEngine(GetCmdText()).SetProp(name, val).toString());
        //SetCmdText(KidTech4Command.SetProp(GetCmdText(), name, val));
    }

    private void SetGameModToCommand(String mod)
    {
        String arg = Q3EUtils.q3ei.GetGameCommandParm();
        SetProp(arg, mod);
    }

    private void EditFile(String file)
    {
        if (null == m_editConfigFileFunc)
            m_editConfigFileFunc = new EditConfigFileFunc((GameLauncher) getContext(), CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_EDIT_CONFIG_FILE);

        Bundle bundle = new Bundle();
        String path = KStr.AppendPath(edt_path.getText().toString(), Q3EUtils.GetGameHomeDirectoryPath());
        bundle.putString("path", path);
        bundle.putString("file", file);
        m_editConfigFileFunc.Start(bundle);
    }

    private void UpdateUserGame(boolean on)
    {
        fs_game_user.setText(R.string.user_mod);
        edt_fs_game.setEnabled(on);
    }

    public void updatehacktings(boolean all)
    {
        String str;

        if(all) // only for idTech4 games
        {
            return;
        }

        // game mods for every games
        str = GetGameModFromCommand();
        if (str != null)
        {
            if (fs_game_user.isChecked())
            {
                String cur = edt_fs_game.getText().toString();
                if (!str.equals(cur))
                    edt_fs_game.setText(str);
            }
        }

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

}