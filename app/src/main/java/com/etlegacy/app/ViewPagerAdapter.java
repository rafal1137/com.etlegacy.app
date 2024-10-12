package com.etlegacy.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.etlegacy.app.fragments.ControlsFragment;
import com.etlegacy.app.fragments.GeneralFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new GeneralFragment();
            case 1:
                return new ControlsFragment();
            default:
                return new GeneralFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
