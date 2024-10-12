package com.etlegacy.app.q3e;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.etlegacy.app.q3e.karin.KStr;
import com.etlegacy.app.q3e.karin.KidTech4Command;
import com.etlegacy.app.q3e.karin.KidTechCommand;

import java.util.Arrays;
import java.util.Set;

public class Q3EInterface {
    static {
        Q3EKeyCodes.InitQ3Keycodes();
        InitDefaultTypeTable();
        InitDefaultArgTable();
    }

    public boolean isETW = false;

    public int UI_SIZE;
    private static int[] _defaultArgs;
    private static int[] _defaultType;
    public boolean joystick_unfixed = false;
    public String config_name;
    public String subdatadir;
    public String game;
    public String game_base;
    public String game_name;
    public int[] type_table;
    public int[] arg_table;
    public String[] defaults_table;
    public String[] texture_table;
    public String start_temporary_extra_command = "";
    public String libname;
    public String datadir;
    public boolean standalone = false;

    public float joystick_release_range = 0.0f;
    public float joystick_inner_dead_zone = 0.0f;
    public String app_storage_path = "/sdcard/etlegacy";

    public String default_path = Environment.getExternalStorageDirectory() + "/etlegacy";

    public String GetEnableModPreferenceKey()
    {
        return Q3EPreference.pref_harm_etw_user_mod;
    }

    private static void InitDefaultTypeTable()
    {
        int[] type_table = new int[Q3EGlobals.UI_SIZE];

        type_table[Q3EGlobals.UI_JOYSTICK] = Q3EGlobals.TYPE_JOYSTICK;
        type_table[Q3EGlobals.UI_SHOOT] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_JUMP] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_CROUCH] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_RELOADBAR] = Q3EGlobals.TYPE_SLIDER;
        type_table[Q3EGlobals.UI_PDA] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_FLASHLIGHT] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_SAVE] = Q3EGlobals.TYPE_SLIDER;
        type_table[Q3EGlobals.UI_1] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_2] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_3] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_KBD] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_CONSOLE] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_RUN] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_ZOOM] = Q3EGlobals.TYPE_BUTTON;
        type_table[Q3EGlobals.UI_INTERACT] = Q3EGlobals.TYPE_BUTTON;

        type_table[Q3EGlobals.UI_WEAPON_PANEL] = Q3EGlobals.TYPE_DISC;

        type_table[Q3EGlobals.UI_SCORE] = Q3EGlobals.TYPE_BUTTON;

        for(int i = Q3EGlobals.UI_0; i <= Q3EGlobals.UI_9; i++)
            type_table[i] = Q3EGlobals.TYPE_BUTTON;

        _defaultType = Arrays.copyOf(type_table, type_table.length);
    }

    private static void InitDefaultArgTable()
    {
        int[] arg_table = new int[Q3EGlobals.UI_SIZE * 4];

        arg_table[Q3EGlobals.UI_SHOOT * 4] = Q3EKeyCodes.KeyCodesGeneric.K_MOUSE1;
        arg_table[Q3EGlobals.UI_SHOOT * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_SHOOT * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_SHOOT * 4 + 3] = 0;


        arg_table[Q3EGlobals.UI_JUMP * 4] = Q3EKeyCodes.KeyCodesGeneric.K_SPACE;
        arg_table[Q3EGlobals.UI_JUMP * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_JUMP * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_JUMP * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_CROUCH * 4] = Q3EKeyCodes.KeyCodesGeneric.K_C; // BFG
        arg_table[Q3EGlobals.UI_CROUCH * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_CAN_HOLD;
        arg_table[Q3EGlobals.UI_CROUCH * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_RIGHT_BOTTOM;
        arg_table[Q3EGlobals.UI_CROUCH * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_RELOADBAR * 4] = Q3EKeyCodes.KeyCodesGeneric.K_RBRACKET; // 93
        arg_table[Q3EGlobals.UI_RELOADBAR * 4 + 1] = Q3EKeyCodes.KeyCodesGeneric.K_R; // 114
        arg_table[Q3EGlobals.UI_RELOADBAR * 4 + 2] = Q3EKeyCodes.KeyCodesGeneric.K_LBRACKET; // 91
        arg_table[Q3EGlobals.UI_RELOADBAR * 4 + 3] = Q3EGlobals.ONSCRREN_SLIDER_STYLE_LEFT_RIGHT;

        arg_table[Q3EGlobals.UI_PDA * 4] = Q3EKeyCodes.KeyCodesGeneric.K_TAB;
        arg_table[Q3EGlobals.UI_PDA * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_PDA * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_PDA * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_FLASHLIGHT * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F; // BFG
        arg_table[Q3EGlobals.UI_FLASHLIGHT * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_FLASHLIGHT * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_FLASHLIGHT * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_SAVE * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F5;
        arg_table[Q3EGlobals.UI_SAVE * 4 + 1] = Q3EKeyCodes.KeyCodesGeneric.K_ESCAPE;
        arg_table[Q3EGlobals.UI_SAVE * 4 + 2] = Q3EKeyCodes.KeyCodesGeneric.K_F9;
        arg_table[Q3EGlobals.UI_SAVE * 4 + 3] = Q3EGlobals.ONSCRREN_SLIDER_STYLE_DOWN_RIGHT;

        arg_table[Q3EGlobals.UI_1 * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F1;
        arg_table[Q3EGlobals.UI_1 * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_1 * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_1 * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_2 * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F2;
        arg_table[Q3EGlobals.UI_2 * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_2 * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_2 * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_3 * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F3;
        arg_table[Q3EGlobals.UI_3 * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_3 * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_3 * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_KBD * 4] = Q3EKeyCodes.K_VKBD;
        arg_table[Q3EGlobals.UI_KBD * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_KBD * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_KBD * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_CONSOLE * 4] = Q3EKeyCodes.KeyCodesGeneric.K_GRAVE;
        arg_table[Q3EGlobals.UI_CONSOLE * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_CONSOLE * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_CONSOLE * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_RUN * 4] = Q3EKeyCodes.KeyCodesGeneric.K_SHIFT;
        arg_table[Q3EGlobals.UI_RUN * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_CAN_HOLD;
        arg_table[Q3EGlobals.UI_RUN * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_RUN * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_ZOOM * 4] = Q3EKeyCodes.KeyCodesGeneric.K_Z;
        arg_table[Q3EGlobals.UI_ZOOM * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_CAN_HOLD;
        arg_table[Q3EGlobals.UI_ZOOM * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_ZOOM * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_INTERACT * 4] = Q3EKeyCodes.KeyCodesGeneric.K_MOUSE2;
        arg_table[Q3EGlobals.UI_INTERACT * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_INTERACT * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_INTERACT * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_SCORE * 4] = Q3EKeyCodes.KeyCodesGeneric.K_M;
        arg_table[Q3EGlobals.UI_SCORE * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_SCORE * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_SCORE * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_0 * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F10;
        arg_table[Q3EGlobals.UI_0 * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_0 * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_0 * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_4 * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F4;
        arg_table[Q3EGlobals.UI_4 * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_4 * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_4 * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_5 * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F5;
        arg_table[Q3EGlobals.UI_5 * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_5 * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_5 * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_6 * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F6;
        arg_table[Q3EGlobals.UI_6 * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_6 * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_6 * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_7 * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F7;
        arg_table[Q3EGlobals.UI_7 * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_7 * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_7 * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_8 * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F8;
        arg_table[Q3EGlobals.UI_8 * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_8 * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_8 * 4 + 3] = 0;

        arg_table[Q3EGlobals.UI_9 * 4] = Q3EKeyCodes.KeyCodesGeneric.K_F9;
        arg_table[Q3EGlobals.UI_9 * 4 + 1] = Q3EGlobals.ONSCRREN_BUTTON_NOT_HOLD;
        arg_table[Q3EGlobals.UI_9 * 4 + 2] = Q3EGlobals.ONSCREEN_BUTTON_TYPE_FULL;
        arg_table[Q3EGlobals.UI_9 * 4 + 3] = 0;

        _defaultArgs = Arrays.copyOf(arg_table, arg_table.length);
    }

    public String GetGameModPreferenceKey()
    {
        if(Q3EUtils.q3ei.isETW)
            return Q3EPreference.pref_harm_etw_fs_game;
        else
            return null;
    }

    public String GetGameCommandParm()
    {
        if(isETW)
            return "fs_game";
        else
            return null;
    }

    public KidTechCommand GetGameCommandEngine(String cmd)
    {
        return new KidTech4Command(cmd);
    }

    public String GetGameHomeDirectoryPath()
    {
        if(Q3EUtils.q3ei.isETW)
            return ".etlegacy/legacy";
        else
            return null;
    }

    public static void RestoreDefaultOnScreenConfig(int[] args, int[] type)
    {
        System.arraycopy(_defaultArgs, 0, args, 0, args.length);
        System.arraycopy(_defaultType, 0, type, 0, type.length);
    }

    public boolean IsIdTech3()
    {
        return isETW;
    }

    public String GetGameModSubDirectory()
    {
        return null;
    }

    public String GetGameCommandRecordPreferenceKey()
    {
        if(Q3EUtils.q3ei.isETW)
            return Q3EPreference.pref_harm_etw_command_record;
        else
            return null;
    }

    public boolean IsStandaloneGame()
    {
        return false;
    }

    public String GetGameCommandPreferenceKey()
    {
        if(Q3EUtils.q3ei.isETW)
            return Q3EPreference.pref_params_etw;
        else
            return null;
    }

    public void InitTextureTable()
    {
        texture_table = new String[Q3EGlobals.UI_SIZE];

        texture_table[Q3EGlobals.UI_JOYSTICK] = "joystick_bg.png;joystick_center.png";
        texture_table[Q3EGlobals.UI_SHOOT] = "btn_sht.png";
        texture_table[Q3EGlobals.UI_JUMP] = "btn_jump.png";
        texture_table[Q3EGlobals.UI_CROUCH] = "btn_crouch.png";
        texture_table[Q3EGlobals.UI_RELOADBAR] = "btn_reload.png;btn_prevweapon.png;btn_ammo.png;btn_nextweapon.png";
        texture_table[Q3EGlobals.UI_PDA] = "btn_pda.png";
        texture_table[Q3EGlobals.UI_FLASHLIGHT] = "btn_flashlight.png";
        texture_table[Q3EGlobals.UI_SAVE] = "btn_pause.png;btn_savegame.png;btn_escape.png;btn_loadgame.png";
        texture_table[Q3EGlobals.UI_1] = "btn_1.png";
        texture_table[Q3EGlobals.UI_2] = "btn_2.png";
        texture_table[Q3EGlobals.UI_3] = "btn_3.png";
        texture_table[Q3EGlobals.UI_KBD] = "btn_keyboard.png";
        texture_table[Q3EGlobals.UI_CONSOLE] = "btn_notepad.png";
        texture_table[Q3EGlobals.UI_INTERACT] = "btn_activate.png";
        texture_table[Q3EGlobals.UI_ZOOM] = "btn_binocular.png";
        texture_table[Q3EGlobals.UI_RUN] = "btn_kick.png";

        texture_table[Q3EGlobals.UI_WEAPON_PANEL] = "disc_weapon.png";

        texture_table[Q3EGlobals.UI_SCORE] = "btn_score.png";

        texture_table[Q3EGlobals.UI_0] = "btn_0.png";
        texture_table[Q3EGlobals.UI_4] = "btn_4.png";
        texture_table[Q3EGlobals.UI_5] = "btn_5.png";
        texture_table[Q3EGlobals.UI_6] = "btn_6.png";
        texture_table[Q3EGlobals.UI_7] = "btn_7.png";
        texture_table[Q3EGlobals.UI_8] = "btn_8.png";
        texture_table[Q3EGlobals.UI_9] = "btn_9.png";
    }

    public void InitTypeTable()
    {
        type_table = Arrays.copyOf(_defaultType, _defaultType.length);
    }

    public void InitArgTable()
    {
        arg_table = Arrays.copyOf(_defaultArgs, _defaultArgs.length);
    }

    public void InitTable()
    {
        UI_SIZE = Q3EGlobals.UI_SIZE;

        InitTypeTable();
        InitArgTable();
        InitTextureTable();
    }

    public String GameName()
    {
        if(isETW)
            return Q3EGlobals.GAME_NAME_ETW;
        else
            return null;
    }

    public String GameType()
    {
        if(isETW)
            return Q3EGlobals.GAME_ETW;
        else
            return null;
    }

    public String GameBase()
    {
        if(isETW)
            return Q3EGlobals.GAME_BASE_ETW;
        else
            return null;
    }

    private void InitGameState()
    {
        isETW = false;
    }

    private void SetupGameTypeAndName()
    {
        game = GameType();
        game_name = GameName();
        game_base = GameBase();
    }

    public String ConfigFileName()
    {
        if(isETW)
            return Q3EGlobals.CONFIG_FILE_ETW;
        else
            return null;
    }

    public void SetupKeycodes()
    {
        if(isETW)
        {
            Q3EKeyCodes.InitQ3Keycodes();
        }

    }

    public void LoadTypeAndArgTablePreference(Context context)
    {
        // index:type;23,1,2,0|......
        try
        {
            Set<String> configs = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(Q3EPreference.ONSCREEN_BUTTON, null);
            if (null != configs && !configs.isEmpty())
            {
                for (String str : configs)
                {
                    String[] subArr = str.split(":", 2);
                    int index = Integer.parseInt(subArr[0]);
                    subArr = subArr[1].split(";");
                    type_table[index] = Integer.parseInt(subArr[0]);
                    String[] argArr = subArr[1].split(",");
                    arg_table[index * 4] = Integer.parseInt(argArr[0]);
                    arg_table[index * 4 + 1] = Integer.parseInt(argArr[1]);
                    arg_table[index * 4 + 2] = Integer.parseInt(argArr[2]);
                    arg_table[index * 4 + 3] = Integer.parseInt(argArr[3]);
                }
            }
        }
        catch (Exception e)
        {
            //UncaughtExceptionHandler.DumpException(this, Thread.currentThread(), e);
            e.printStackTrace();
            RestoreDefaultOnScreenConfig(arg_table, type_table);
        }
    }

    private void SetupSubDir()
    {
        subdatadir = null;
    }

    public String EngineLibName()
    {
        if(isETW)
        {
            return Q3EGlobals.LIB_ENGINE3_ETW;
        }
        else
            return null;
    }

    public void SetupGame(String name)
    {
        Log.i(Q3EGlobals.CONST_Q3E_LOG_TAG, "SetupGame: " + name);
        if(Q3EGlobals.GAME_ETW.equalsIgnoreCase(name))
        {
            SetupWET();
        }
    }

    public void SetupEngineLib()
    {
        libname = EngineLibName();
    }

    private void SetupConfigFile()
    {
        config_name = ConfigFileName();
    }

    public void SetupGameConfig()
    {
        SetupGameTypeAndName();
        // TODO MAKE SURE WE LOAD IT FROM SDL
        //SetupEngineLib();
        SetupConfigFile();
        SetupKeycodes();
        SetupSubDir();
    }

    public void InitWET()
    {
        InitTable();
    }

    public void SetupWET()
    {
        InitGameState();
        isETW = true;
        SetupGameConfig();
    }

    public String GetGameDataDirectoryPath(String file)
    {
        return KStr.AppendPath(datadir, subdatadir, file);
    }

    public static String GetGameStandaloneDirectory(String name)
    {
        if(Q3EGlobals.GAME_ETW.equalsIgnoreCase(name))
            return Q3EGlobals.GAME_SUBDIR_ETW;
        else
            return null;
    }

    public void SetAppStoragePath(Context context)
    {
        Q3EUtils.q3ei.app_storage_path = Q3EUtils.GetAppStoragePath(context, null);
    }

}
