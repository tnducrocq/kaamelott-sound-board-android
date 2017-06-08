package fr.tnducrocq.kaamelott_soundboard;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import fr.tnducrocq.kaamelott_soundboard.fragment.BusEvent;
import fr.tnducrocq.kaamelott_soundboard.fragment.LoadDataFragment;
import fr.tnducrocq.kaamelott_soundboard.fragment.SoundRecyclerViewFragment;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.SoundProvider;
import fr.tnducrocq.kaamelott_soundboard.player.SoundPoolPlayer;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mToolbar.setVisibility(View.GONE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSupportActionBar(mToolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.alpha);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                if (id == R.id.character) {
                    Fragment fragment = SoundRecyclerViewFragment.newInstance();
                    Bundle args = new Bundle();
                    args.putString("sortMode", "person");
                    args.putParcelableArray("soundArray", SoundProvider.getSounds().toArray(new Sound[0]));
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.container_fragment, fragment).commit();
                    mDrawerLayout.closeDrawers();
                } else if (id == R.id.alpha) {
                    Fragment fragment = SoundRecyclerViewFragment.newInstance();
                    Bundle args = new Bundle();
                    args.putString("sortMode", "alpha");
                    args.putParcelableArray("soundArray", SoundProvider.getSounds().toArray(new Sound[0]));
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.container_fragment, fragment).commit();
                    mDrawerLayout.closeDrawers();
                } else if (id == R.id.random) {
                    List<Sound> soundList = SoundProvider.getSounds();
                    int index = new Random().nextInt(soundList.size());
                    SoundPoolPlayer.getInstance().start(getApplicationContext(), soundList.get(index).fileName);
                } else if (id == R.id.credit) {
                    Intent intent = new Intent(MainActivity.this, CreditActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });
        //View header = navigationView.getHeaderView(0);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Fragment waitingFragment = LoadDataFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.container_fragment, waitingFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                EventBus.getDefault().post(new BusEvent.QueryEvent(query));
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(BusEvent.Finish event) {
        Sound[] soundArray = event.soundList.toArray(new Sound[0]);
        Fragment fragment = SoundRecyclerViewFragment.newInstance();
        Bundle args = new Bundle();
        args.putString("sortMode", "alpha");
        args.putParcelableArray("soundArray", soundArray);
        fragment.setArguments(args);
        getFragmentManager().beginTransaction().replace(R.id.container_fragment, fragment).commit();

        mToolbar.setVisibility(View.VISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
