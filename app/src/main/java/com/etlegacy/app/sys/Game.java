package com.etlegacy.app.sys;

import android.content.Context;

import com.etlegacy.app.R;
import com.etlegacy.app.q3e.Q3EGlobals;
import com.etlegacy.app.q3e.Q3ELang;

public enum Game {
    // Wolfenstein: Enemy Territory
    ETW_BASE(Q3EGlobals.GAME_ETW, "etmain", "", "etwgame", "", false, R.string.etw_base),
    ;

    public final String  type; // game type: doom3/quake4/prey2006/......
    public final String  game; // game id: unique
    public final String  fs_game; // game data folder name
    public final String  lib; // game library file name, only for DOOM3/Quake4/Prey
    public final String  fs_game_base; // game mod data base folder name, e.g. d3xp
    public final boolean is_mod; // is a mod
    public final Object  name; // game name string resource's id or game name string

    Game(String type, String game, String fs_game, String lib, String fs_game_base, boolean is_mod, Object name)
    {
        this.type = type;
        this.game = game;
        this.fs_game = fs_game;
        this.lib = lib;
        this.fs_game_base = fs_game_base;
        this.is_mod = is_mod;
        this.name = name;
    }

    public String GetName(Context context)
    {
        if(name instanceof Integer)
            return Q3ELang.tr(context, (Integer)name);
        else if(name instanceof String)
            return (String)name;
        else
            return "";
    }
}
