package com.etlegacy.app.sys;

import com.etlegacy.app.q3e.Q3EGlobals;
import com.etlegacy.app.q3e.Q3EUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GameManager {
    private final Map<String, List<GameProp>> GameProps = new LinkedHashMap<>();

    public final static String[] Games = {
            Q3EGlobals.GAME_ETW,
    };

    public GameManager()
    {
        InitGameProps();
    }

    public static class GameProp
    {
        public final int     index;
        public final String  game;
        public final String  fs_game;
        public final String  fs_game_base;
        public final boolean is_user;
        public final String  lib;

        public GameProp(int index, String game, String fs_game, String fs_game_base, boolean is_user, String lib)
        {
            this.index = index;
            this.game = game;
            this.fs_game = fs_game;
            this.fs_game_base = fs_game_base;
            this.is_user = is_user;
            this.lib = lib;
        }

        public boolean IsGame(String game)
        {
            if (null == game)
                game = "";
            if(game.equals(this.game))
                return true;
            if(index == 0 && game.isEmpty())
                return true;
            return false;
        }

        public boolean IsValid()
        {
            return index >= 0 && !game.isEmpty();
        }
    }

    private void InitGameProps()
    {
        List<GameProp> props;
        GameProp prop;

        for(String game : Games)
            GameProps.put(game, new ArrayList<>());
        Game[] values = Game.values();

        for (Game value : values)
        {
            props = GameProps.get(value.type);
            prop = new GameProp(props.size(), value.game, value.fs_game, value.fs_game_base, false, value.lib);
            props.add(prop);
        }
    }

    public GameProp ChangeGameMod(String game, boolean userMod)
    {
        if (null == game)
            game = "";

        List<GameProp> list;
        list = GameProps.get(Q3EGlobals.GAME_ETW);

        GameProp res = null;
        for (GameProp prop : list)
        {
            if(prop.IsGame(game))
            {
                res = prop;
                break;
            }
        }
        if(null == res)
            res = new GameProp(0, "", game, "", userMod, "");
        return res;
    }

    public String GetGameOfMod(String game)
    {
        for (String key : GameProps.keySet())
        {
            List<GameProp> props = GameProps.get(key);
            for (GameProp prop : props)
            {
                if(prop.game.equals(game))
                    return key;
            }
        }
        return null;
    }
}
