package com.etlegacy.app;

import static com.etlegacy.app.fragments.GeneralFragment.default_gamedata;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.content.pm.ActivityInfo;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.etlegacy.app.launcher.FragmentToActivity;
import com.etlegacy.app.launcher.StartGameFunc;
import com.etlegacy.app.q3e.Q3EInterface;
import com.etlegacy.app.q3e.Q3ELang;
import com.etlegacy.app.q3e.Q3EUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;


public class GameLauncher extends AppCompatActivity implements FragmentToActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    String m_data;
    String m_command;
    String m_fs_game;

    private static final int CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_START               = 1;
    private StartGameFunc             m_startGameFunc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        Q3ELang.Locale(this);
        setContentView(R.layout.activity_main);

        if(isDirectToTV())
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        Q3EInterface q3ei = new Q3EInterface();

        q3ei.InitWET();
        q3ei.default_path = default_gamedata;
        q3ei.LoadTypeAndArgTablePreference(this);

        Q3EUtils.q3ei = q3ei;

        tabLayout = findViewById(R.id.tablayout);
        viewPager2 = findViewById(R.id.viewpager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });
    }

    public String GetDefaultGameDirectory()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
            return Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName();
        else
            return default_gamedata;
    }

    private boolean isDirectToTV() {
        return(getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK));
    }

    public void start(View vw)
    {
        if (null == m_startGameFunc)
            m_startGameFunc = new StartGameFunc(this, CONST_RESULT_CODE_REQUEST_EXTERNAL_STORAGE_FOR_START);
        Bundle bundle = new Bundle();
        bundle.putString("data", getStartupGameData());
        bundle.putString("command", m_fs_game + " " + (m_command == null ? "" : m_command));
        m_startGameFunc.Start(bundle);
    }

    public String getStartupGameData()
    {
        return m_data;
    }

    @Override
    public void default_path(String comm) {
       m_data = comm;
    }

    @Override
    public void new_path(String comm) {
        if(comm == null)
            m_fs_game = "";
        else
            m_fs_game = comm;
    }

    @Override
    public void additional_commands(String comm) {
        if(comm == null)
            m_command = "";
        else
            m_command = comm;
    }

    @Override
    public void fs_game(String comm) {
        if(comm == null)
            m_fs_game = "";
        else
            m_fs_game = comm;
    }

}