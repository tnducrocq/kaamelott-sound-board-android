package fr.tnducrocq.kaamelott_soundboard.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.kaamelott_soundboard.R;
import fr.tnducrocq.kaamelott_soundboard.SoundRecyclerViewAdapter;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.SoundProvider;

public class AlphaRecyclerViewFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    public static AlphaRecyclerViewFragment newInstance() {
        return new AlphaRecyclerViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alpha, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);
        new KaamelottAsyncTask().execute();
    }

    private class KaamelottAsyncTask extends AsyncTask<Void, String, List<Sound>> {

        @Override
        protected List<Sound> doInBackground(Void... params) {
            try {
                List<Sound> sounds = SoundProvider.getSounds();
                Collections.sort(sounds, new Comparator<Sound>() {
                    @Override
                    public int compare(Sound o1, Sound o2) {
                        return o1.title.compareTo(o2.title);
                    }
                });
                return sounds;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final List<Sound> soundList) {
            super.onPostExecute(soundList);
            if (soundList == null) {
                return;
            }
            mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
            mRecyclerView.setAdapter(new SoundRecyclerViewAdapter(soundList));
        }
    }

}