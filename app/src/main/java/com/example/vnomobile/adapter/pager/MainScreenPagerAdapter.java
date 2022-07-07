package com.example.vnomobile.adapter.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.vnomobile.fragment.AreaFragment;
import com.example.vnomobile.fragment.LogFragment;
import com.example.vnomobile.fragment.MusicFragment;
import com.example.vnomobile.fragment.OOCFragment;
import com.example.vnomobile.fragment.SceneFragment;

public class MainScreenPagerAdapter extends FragmentStateAdapter {

    private final Fragment[] fragments = {
            new SceneFragment(),
            new LogFragment(),
            new OOCFragment(),
            new MusicFragment(),
            new AreaFragment()
    };

    public MainScreenPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
