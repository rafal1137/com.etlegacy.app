package com.etlegacy.app.launcher;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import com.etlegacy.app.GameLauncher;
import com.etlegacy.app.R;
import com.etlegacy.app.lib.ContextUtility;
import com.etlegacy.app.misc.FileBrowser;
import com.etlegacy.app.q3e.Q3EGlobals;
import com.etlegacy.app.q3e.Q3ELang;
import com.etlegacy.app.q3e.Q3EPreference;
import com.etlegacy.app.q3e.Q3EUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ChooseGameModFunc extends GameLauncherFunc {
    private final int m_code;
    private String m_path;
    private String m_mod;
    private String m_file;

    public ChooseGameModFunc(GameLauncher gameLauncher, int code)
    {
        super(gameLauncher);
        m_code = code;
    }

    public void Reset()
    {
    }

    public void Start(Bundle data)
    {
        super.Start(data);
        Reset();

        m_path = data.getString("path");
        m_mod = data.getString("mod");
        m_file = data.getString("file");

        int res = ContextUtility.CheckFilePermission(m_gameLauncher, m_code);
        if(res == ContextUtility.CHECK_PERMISSION_RESULT_REJECT)
            Toast_long(Q3ELang.tr(m_gameLauncher, R.string.can_t_s_read_write_external_storage_permission_is_not_granted, Q3ELang.tr(m_gameLauncher, R.string.load_game_mod_list)));
        if(res != ContextUtility.CHECK_PERMISSION_RESULT_GRANTED)
            return;

        run();
    }

    public void run()
    {
        FileBrowser fileBrowser = new FileBrowser();
        fileBrowser.SetFilter(FileBrowser.ID_FILTER_DIRECTORY);
        fileBrowser.SetIgnoreDotDot(true);
        fileBrowser.SetDirNameWithSeparator(false);
        fileBrowser.SetShowHidden(true);
        fileBrowser.SetCurrentPath(m_path);

        final List<CharSequence> items = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        final List<String> values = new ArrayList<>();
        final List<String> TotalList = new ArrayList<>(Arrays.asList(
                Q3EGlobals.GAME_BASE_ETW
        ));
        List<String> blackList = new ArrayList<>();
        boolean standalone = PreferenceManager.getDefaultSharedPreferences(m_gameLauncher).getBoolean(Q3EPreference.GAME_STANDALONE_DIRECTORY, true);
        if(!standalone)
        {
            blackList.addAll(TotalList);
            blackList.addAll(Arrays.asList(
                    Q3EGlobals.GAME_SUBDIR_ETW
            ));
        }

        if(Q3EUtils.q3ei.isETW)
        {
            if(standalone)
                blackList.add(Q3EGlobals.GAME_BASE_ETW);
            else
                blackList.remove(Q3EGlobals.GAME_BASE_ETW);
        }

        String gameHomePath = Q3EUtils.q3ei.GetGameHomeDirectoryPath();
        if(null != gameHomePath)
        {
            int i = gameHomePath.indexOf("/");
            if(i > 0)
                blackList.add(gameHomePath.substring(0, i));
            else
                blackList.add(gameHomePath);
        }

        List<FileBrowser.FileModel> fileModels;
        fileModels = fileBrowser.FileList();

        for (FileBrowser.FileModel fileModel : fileModels)
        {
            String name = "";
            if(blackList.contains(fileModel.name))
                continue;
            if(Q3EUtils.q3ei.isETW)
            {
                if(Q3EGlobals.GAME_BASE_ETW.equals(fileModel.name))
                    name = Q3EGlobals.GAME_NAME_ETW;
            }

            String guessGame = m_gameLauncher.GetGameManager().GetGameOfMod(fileModel.name);
            if(null != guessGame)
            {
                switch (guessGame)
                {
                    case Q3EGlobals.GAME_ETW:
                        if(!Q3EUtils.q3ei.isETW)
                            continue;
                        break;
                }
            }

            if(name.isEmpty())
                name = fileModel.name;

            /*
            File dir = new File(fileModel.path);
            name += "\n " + FileUtility.FormatSize(FileUtility.du(fileModel.path, new Function() {
                @Override
                public Object Invoke(Object... args)
                {
                    File f = (File)args[0];
                    String relativePath = FileUtility.RelativePath(dir, f);
                    if(f.isDirectory())
                    {
                        return !"/savegames".equalsIgnoreCase(relativePath);
                    }
                    else
                    {
                        return !"/.console_history.dat".equalsIgnoreCase(relativePath);
                    }
                }
            }));
*/
            map.put(fileModel.name, name);
            values.add(fileModel.name);
        }

        Collections.sort(values, new Comparator<String>() {
            @Override
            public int compare(String a, String b)
            {
                if(TotalList.contains(a))
                    return -1;
                if(TotalList.contains(b))
                    return 1;
                return a.compareTo(b);
            }
        });

        for (String value : values)
        {
            items.add(map.get(value));
        }

        int selected = -1;
        if(null != m_mod && !m_mod.isEmpty())
        {
            for (int i = 0; i < values.size(); i++)
            {
                if(values.get(i).equals(m_mod))
                {
                    selected = i;
                    break;
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(m_gameLauncher);
        builder.setTitle(Q3EUtils.q3ei.game_name + " " + Q3ELang.tr(m_gameLauncher, R.string.mod));
        builder.setSingleChoiceItems(items.toArray(new CharSequence[0]), selected, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int p)
            {
                String lib = values.get(p);
                Callback(lib);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.unset, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int p)
            {
                Callback("");
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
