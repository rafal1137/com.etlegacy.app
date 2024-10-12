package com.etlegacy.app.lib;

import android.text.InputType;
import android.widget.EditText;

public class UIUtility {
    public static void EditText__SetReadOnly(EditText editText, boolean readonly, int inputTypeIfEditable)
    {
        // if(false) editText.setEnabled(!readonly); else
        {
            int inputType;
            if(readonly)
                inputType = InputType.TYPE_NULL;
            else
                inputType = inputTypeIfEditable;
            editText.setInputType(inputType);
            editText.setFocusable(!readonly);
            editText.setFocusableInTouchMode(!readonly);
        }

        editText.setSingleLine(readonly);
    }
}
