package fr.tnducrocq.kaamelott_soundboard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import de.greenrobot.event.EventBus;
import fr.tnducrocq.kaamelott_soundboard.fragment.AphaRecyclerViewFragment;
import fr.tnducrocq.kaamelott_soundboard.model.QueryEvent;

public class MainActivity extends AppCompatActivity {


    public static final String FRAGMENT_TAG = "single";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  setTitle("");
        //ButterKnife.bind(this);

        Fragment fragment = AphaRecyclerViewFragment.newInstance();

        Bundle args = new Bundle();
        args.putString("sortMode", "alpha");
        fragment.setArguments(args);

        getFragmentManager().beginTransaction().add(R.id.container_fragment, fragment).commit();

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
                EventBus.getDefault().post(new QueryEvent(query));

                //if you want to collapse the searchview
                invalidateOptionsMenu();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                EventBus.getDefault().post(new QueryEvent(query));
                return false;
            }
        });

        return true;
    }
}
