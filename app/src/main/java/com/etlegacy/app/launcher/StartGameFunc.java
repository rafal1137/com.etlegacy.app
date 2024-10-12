package com.etlegacy.app.launcher;

import android.content.Intent;
import android.os.Bundle;

import com.etlegacy.app.ETLMain;
import com.etlegacy.app.GameLauncher;
import com.etlegacy.app.R;
import com.etlegacy.app.lib.ContextUtility;
import com.etlegacy.app.q3e.Q3ELang;

public final class StartGameFunc extends GameLauncherFunc {
    private final int m_code;
    private String m_path;

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

        m_path = data.getString("path");

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
        /*
		String dir=m_path;
		if ((new File(dir+"/base").exists())&&(!new File(dir+"/base/gl2progs").exists()))
		getgl2progs(dir+"/base/");
        */

        m_gameLauncher.finish();
        m_gameLauncher.startActivity(new Intent(m_gameLauncher, ETLMain.class));
    }
}
