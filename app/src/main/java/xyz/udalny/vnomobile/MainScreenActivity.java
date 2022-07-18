package xyz.udalny.vnomobile;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import xyz.udalny.vnolib.client.OnCommand;
import xyz.udalny.vnolib.command.servercommands.MCCommand;
import xyz.udalny.vnomobile.adapter.pager.MainScreenPagerAdapter;
import xyz.udalny.vnomobile.resource.SoundHandler;

import com.example.vnomobile.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainScreenActivity extends AppCompatActivity {

    private ViewPager2 pager;
    private TabLayout tabLayout;
    private SoundHandler soundHandler;
    private int[] tabTextId = {R.string.scene, R.string.log, R.string.ooc, R.string.music, R.string.areas};

    private static final int OOC_TAB_POSITION = 2;

    @OnCommand(MCCommand.class)
    public void onMusicCued(MCCommand command) {
        soundHandler.playMusicTrack(command.getTrackName(), command.getLoopingStatus().isLooping());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        this.pager = findViewById(R.id.main_screen_pager);
        FragmentStateAdapter adapter = new MainScreenPagerAdapter(this);
        this.pager.setAdapter(adapter);
        this.pager.setUserInputEnabled(false);
        this.pager.setOffscreenPageLimit(4);
        this.tabLayout = findViewById(R.id.main_screen_tabs);
        new TabLayoutMediator(tabLayout, pager, ((tab, position) -> {
            if(position == OOC_TAB_POSITION) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            } else {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }
            tab.setText(tabTextId[position]);
        })).attach();
        this.soundHandler = SoundHandler.getInstance();
        ClientHandler.getClient().subscribeToCommand(MCCommand.class, this);
    }
}