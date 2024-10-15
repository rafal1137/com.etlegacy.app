package com.etlegacy.app.q3e;

import com.etlegacy.app.q3e.karin.KidTech4Command;
import com.etlegacy.app.q3e.karin.KidTechCommand;

public class Q3EInterface {
    public String GetGameCommandParm()
    {
        return "fs_game";
    }

    public KidTechCommand GetGameCommandEngine(String cmd)
    {
        return new KidTech4Command(cmd);
    }
}
