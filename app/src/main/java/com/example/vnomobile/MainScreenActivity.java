package com.example.vnomobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.vnomobile.adapter.pager.ListOfServersAndFavouritesAdapter;
import com.example.vnomobile.adapter.pager.MainScreenPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainScreenActivity extends AppCompatActivity {

    private ViewPager2 pager;
    private TabLayout tabLayout;
    private int[] tabTextId = {R.string.scene, R.string.log, R.string.ooc, R.string.music, R.string.areas};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        this.pager = findViewById(R.id.main_screen_pager);
        FragmentStateAdapter adapter = new MainScreenPagerAdapter(this);
        this.pager.setAdapter(adapter);
        this.tabLayout = findViewById(R.id.main_screen_tabs);
        new TabLayoutMediator(tabLayout, pager, ((tab, position) -> {tab.setText(tabTextId[position]);})).attach();
    }
}