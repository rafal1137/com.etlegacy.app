package com.etlegacy.app.q3e;

public class Q3EGlobals {
    public static final String CONST_Q3E_LOG_TAG = "Q3E";

    public static final String CONST_PACKAGE_NAME = "com.etlegacy.app";
    public static final String CONST_APP_NAME     = "ET Legacy"; // "DIII4A++";

    // on-screen buttons index
    public static final int UI_JOYSTICK     = 0;
    public static final int UI_SHOOT        = 1;
    public static final int UI_JUMP         = 2;
    public static final int UI_CROUCH       = 3;
    public static final int UI_RELOADBAR    = 4;
    public static final int UI_PDA          = 5;
    public static final int UI_FLASHLIGHT   = 6;
    public static final int UI_SAVE         = 7;
    public static final int UI_1            = 8;
    public static final int UI_2            = 9;
    public static final int UI_3            = 10;
    public static final int UI_KBD          = 11;
    public static final int UI_CONSOLE      = 12;
    public static final int UI_RUN          = 13;
    public static final int UI_ZOOM         = 14;
    public static final int UI_INTERACT     = 15;
    public static final int UI_WEAPON_PANEL = 16;
    public static final int UI_SCORE        = 17;
    public static final int UI_0            = 18;
    public static final int UI_4            = 19;
    public static final int UI_5            = 20;
    public static final int UI_6            = 21;
    public static final int UI_7            = 22;
    public static final int UI_8            = 23;
    public static final int UI_9            = 24;
    public static final int UI_SIZE         = UI_9 + 1;

    // on-screen item type
    public static final int TYPE_BUTTON   = 0;
    public static final int TYPE_SLIDER   = 1;
    public static final int TYPE_JOYSTICK = 2;
    public static final int TYPE_DISC     = 3;
    public static final int TYPE_MOUSE    = -1;

    // mouse
    public static final int MOUSE_EVENT  = 1;
    public static final int MOUSE_DEVICE = 2;

    // default size
    public static final int SCREEN_WIDTH  = 640;
    public static final int SCREEN_HEIGHT = 480;

    // on-screen button type
    public static final int ONSCREEN_BUTTON_TYPE_FULL         = 0;
    public static final int ONSCREEN_BUTTON_TYPE_RIGHT_BOTTOM = 1;
    public static final int ONSCREEN_BUTTON_TYPE_CENTER       = 2;
    public static final int ONSCREEN_BUTTON_TYPE_LEFT_TOP     = 3;

    // on-screen button can hold
    public static final int ONSCRREN_BUTTON_NOT_HOLD = 0;
    public static final int ONSCRREN_BUTTON_CAN_HOLD = 1;

    // on-screen slider type
    public static final int ONSCRREN_SLIDER_STYLE_LEFT_RIGHT             = 0;
    public static final int ONSCRREN_SLIDER_STYLE_DOWN_RIGHT             = 1;
    public static final int ONSCRREN_SLIDER_STYLE_LEFT_RIGHT_SPLIT_CLICK = 2;
    public static final int ONSCRREN_SLIDER_STYLE_DOWN_RIGHT_SPLIT_CLICK = 3;

    // on-screen joystick visible
    public static final int ONSCRREN_JOYSTICK_VISIBLE_ALWAYS       = 0;
    public static final int ONSCRREN_JOYSTICK_VISIBLE_HIDDEN       = 1;
    public static final int ONSCRREN_JOYSTICK_VISIBLE_ONLY_PRESSED = 2;

    public static final String GAME_EXECUABLE = "game.arm";
    public static final String GAME_ETW      = "etw";

    public static final String[] CONTROLS_NAMES = {
            "Joystick",
            "Shoot",
            "Jump",
            "Crouch",
            "Reload",
            "PDA",
            "Flashlight",
            "Pause",
            "Extra 1",
            "Extra 2",
            "Extra 3",
            "Keyboard",
            "Console",
            "Run",
            "Zoom",
            "Interact",
            "Weapon",
            "Score",
            "Extra 0",
            "Extra 4",
            "Extra 5",
            "Extra 6",
            "Extra 7",
            "Extra 8",
            "Extra 9",
    };

    public static final String IDTECH4AMM_PAK_SUFFIX = ".zipak";
    public static final String GAME_BASE_ETW        = "legacy";
    public static final String GAME_SUBDIR_ETW     = "etw";
    public static final String GAME_NAME_ETW      = "ETW"; // "Wolfenstein: Enemy Territory";
    public static final String CONFIG_FILE_ETW      = "etconfig.cfg"; // ETW
    public static final String LIB_ENGINE3_ETW      = "libetl.so";
    public static final int ENUM_BACK_ALL    = 0xFF;// ETW
}
