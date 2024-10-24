package com.etlegacy.app.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.etlegacy.app.ConfigEditorActivity;
import com.etlegacy.app.GameLauncher;
import com.etlegacy.app.R;
import com.etlegacy.app.lib.ContextUtility;
import com.etlegacy.app.q3e.Q3ELang;
import com.etlegacy.app.q3e.karin.KStr;

import java.io.File;

public final class EditConfigFileFunc extends GameLauncherFunc {
    private final int m_code;
    private String m_path;
    private String m_file;

    public EditConfigFileFunc(GameLauncher gameLauncher, int code)
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
        m_file = data.getString("file");

        int res = ContextUtility.CheckFilePermission(m_gameLauncher, m_code);
        if(res == ContextUtility.CHECK_PERMISSION_RESULT_REJECT)
            Toast_long(Q3ELang.tr(m_gameLauncher, R.string.can_t_s_read_write_external_storage_permission_is_not_granted, Q3ELang.tr(m_gameLauncher, R.string.access_file)));
        if(res != ContextUtility.CHECK_PERMISSION_RESULT_GRANTED)
            return;
        run();
    }

    public void run()
    {
        String basePath = m_path;
        basePath = KStr.AppendPath(basePath, m_file);
        File f = new File(basePath);
        if(!f.isFile() || !f.canWrite() || !f.canRead())
        {
            Toast_long(Q3ELang.tr(m_gameLauncher, R.string.file_can_not_access) + basePath);
            return;
        }

        Intent intent = new Intent(m_gameLauncher, ConfigEditorActivity.class);
        intent.putExtra("CONST_FILE_PATH", basePath);
        m_gameLauncher.startActivity(intent);
    }
}
