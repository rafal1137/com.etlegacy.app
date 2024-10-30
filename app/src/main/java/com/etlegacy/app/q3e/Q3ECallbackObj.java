package com.etlegacy.app.q3e;

public class Q3ECallbackObj {
    public Q3EControlView vw;

    public boolean notinmenu = true; // inGaming

    public void ToggleToolbar(boolean on)
    {
        vw.ToggleToolbar(on);
    }
}
