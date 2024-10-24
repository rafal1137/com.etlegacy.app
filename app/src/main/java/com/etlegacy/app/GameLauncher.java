package com.etlegacy.app;

import static com.etlegacy.app.fragments.GeneralFragment.default_gamedata;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.etlegacy.app.q3e.Q3EInterface;
import com.etlegacy.app.q3e.Q3ELang;
import com.etlegacy.app.q3e.Q3EUtils;
import com.etlegacy.app.sys.Theme;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;


public class GameLauncher extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        Q3ELang.Locale(this);
        setContentView(R.layout.activity_main);

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
}