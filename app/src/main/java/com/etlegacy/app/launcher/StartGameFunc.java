package com.etlegacy.app.launcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.etlegacy.app.ETLMain;
import com.etlegacy.app.GameLauncher;
import com.etlegacy.app.R;
import com.etlegacy.app.lib.ContextUtility;
import com.etlegacy.app.q3e.Q3ELang;

public final class StartGameFunc extends GameLauncherFunc {
    private final int m_code;
    private String m_data;
    private String m_command;

    public StartGameFunc(GameLauncher gameLauncher, int code)
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

        m_data = data.getString("data");
        m_command = data.getString("command");

        //k check external storage permission
        int res = ContextUtility.CheckFilePermission(m_gameLauncher, m_code);
        if (res == ContextUtility.CHECK_PERMISSION_RESULT_REJECT)
            Toast_long(Q3ELang.tr(m_gameLauncher, R.string.can_t_s_read_write_external_storage_permission_is_not_granted, Q3ELang.tr(m_gameLauncher, R.string.startgame)));
        if (res != ContextUtility.CHECK_PERMISSION_RESULT_GRANTED)
            return;
        run();
    }

    @Override
    public void run()
    {
        Intent m_intent = new Intent(m_gameLauncher, ETLMain.class);
        m_intent.putExtra("data", m_data);
        m_intent.putExtra("command", m_command);
        m_gameLauncher.finish();
        m_gameLauncher.startActivity(m_intent);
    }
}