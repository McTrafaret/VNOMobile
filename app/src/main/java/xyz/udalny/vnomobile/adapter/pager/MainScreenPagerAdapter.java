package xyz.udalny.vnomobile.adapter.pager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import xyz.udalny.vnomobile.fragment.AreaFragment;
import xyz.udalny.vnomobile.fragment.LogFragment;
import xyz.udalny.vnomobile.fragment.MusicFragment;
import xyz.udalny.vnomobile.fragment.OOCFragment;
import xyz.udalny.vnomobile.fragment.SceneFragment;

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
