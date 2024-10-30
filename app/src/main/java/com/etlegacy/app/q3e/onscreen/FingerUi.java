package com.etlegacy.app.q3e.onscreen;

public class FingerUi extends Finger {
    public int lastx;
    public int lasty;
    public boolean movd;

    public FingerUi(TouchListener tgt, int myid)
    {
        super(tgt, myid);
    }
}
