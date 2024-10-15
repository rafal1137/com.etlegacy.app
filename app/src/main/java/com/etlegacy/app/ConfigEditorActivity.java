package com.etlegacy.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.etlegacy.app.lib.ContextUtility;
import com.etlegacy.app.q3e.Q3ELang;
import com.etlegacy.app.q3e.Q3EUtils;
import com.etlegacy.app.sys.PreferenceKey;
import com.etlegacy.app.sys.Theme;

import java.io.File;

public class ConfigEditorActivity extends AppCompatActivity {
    private final ViewHolder V = new ViewHolder();
    private String m_filePath;
    private File m_file;
    private boolean m_edited = false;

    private final TextWatcher m_textWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(m_edited)
                return;
            m_edited = true;
            V.titleText.setText(m_filePath + "*");
            V.titleText.setTextColor(Color.RED);
            if(V.saveBtn != null)
                V.saveBtn.setEnabled(true);
            if(V.reloadBtn != null)
                V.reloadBtn.setEnabled(true);
        }
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
        public void afterTextChanged(Editable s) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Q3ELang.Locale(this);

        boolean o = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PreferenceKey.LAUNCHER_ORIENTATION, false);
        ContextUtility.SetScreenOrientation(this, o ? 0 : 1);

        Theme.SetTheme(this, false);
        setContentView(R.layout.editor_page);

        V.SetupUI();

        SetupUI();
    }

    private void SetupUI()
    {
        Intent intent = getIntent();
        String path = intent.getStringExtra("CONST_FILE_PATH");
        if (path != null)
        {
            LoadFile(path);
        }
    }

    private void Reset()
    {
        V.editText.removeTextChangedListener(m_textWatcher);
        m_edited = false;
        m_filePath = null;
        m_file = null;

        if(V.saveBtn != null)
            V.saveBtn.setEnabled(false);
        if(V.reloadBtn != null)
            V.reloadBtn.setEnabled(false);
        V.editText.setText("");
        V.titleText.setText("");
        V.editText.setFocusableInTouchMode(false);
        V.titleText.setTextColor(Theme.BlackColor(this));
    }

    private boolean LoadFile(String path)
    {
        Reset();

        File file = new File(path);
        String text = Q3EUtils.file_get_contents(file);
        if (text != null)
        {
            V.editText.setText(text);
            //V.saveBtn.setClickable(true);
            //V.reloadBtn.setClickable(true);
            V.editText.setFocusableInTouchMode(true);
            V.editText.addTextChangedListener(m_textWatcher);
            V.titleText.setText(path);
            m_file = file;
            m_filePath = path;
            return true;
        }
        else
        {
            Reset();
            return false;
        }
    }

    private class ViewHolder
    {
        private TextView titleText;
        private EditText editText;
        private MenuItem saveBtn;
        private MenuItem reloadBtn;

        public void SetupUI()
        {
            titleText = findViewById(R.id.editor_page_title);
            editText = findViewById(R.id.editor_page_editor);
        }

        public void SetupMenu(Menu menu)
        {
            saveBtn = menu.findItem(R.id.config_editor_menu_save);
            reloadBtn = menu.findItem(R.id.config_editor_menu_reload);
        }
    }
}
