package xyz.udalny.vnomobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import xyz.udalny.vnomobile.adapter.pager.ListOfServersAndFavouritesAdapter;

import xyz.udalny.vnomobile.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServersListActivity extends AppCompatActivity {

    private ViewPager2 pager;
    private TabLayout tabLayout;
    private int[] tabTextId = {R.string.list_of_servers, R.string.favourites};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers_list);
        this.pager = findViewById(R.id.server_list_pager);
        FragmentStateAdapter adapter = new ListOfServersAndFavouritesAdapter(this);
        this.pager.setAdapter(adapter);
        this.tabLayout = findViewById(R.id.list_of_servers_tabs);
        new TabLayoutMediator(tabLayout, pager, ((tab, position) -> {tab.setText(tabTextId[position]);})).attach();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}