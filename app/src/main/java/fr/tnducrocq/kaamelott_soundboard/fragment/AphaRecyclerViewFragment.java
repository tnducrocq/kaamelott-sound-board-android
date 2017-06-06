package fr.tnducrocq.kaamelott_soundboard.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import fr.tnducrocq.kaamelott_soundboard.R;
import fr.tnducrocq.kaamelott_soundboard.SoundRecyclerViewAdapter;
import fr.tnducrocq.kaamelott_soundboard.model.QueryEvent;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.SoundProvider;

public class AphaRecyclerViewFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private String sortMode;

    public static AphaRecyclerViewFragment newInstance() {
        return new AphaRecyclerViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sortMode = getArguments().getString("sortMode");
        return inflater.inflate(R.layout.fragment_recycler, container, false);
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

    public void onEvent(QueryEvent event) {

        new KaamelottAsyncTask().execute(event.getQuery());

    }

    private class KaamelottAsyncTask extends AsyncTask<String, String, List<Sound>> {

        @Override
        protected List<Sound> doInBackground(String... params) {
            try {
                List<Sound> sounds = SoundProvider.getSounds();
                if (params.length > 0) {
                    String query = params[0];

                    List<Sound> origin = sounds;
                    sounds = new ArrayList<>();
                    for (Sound sound : origin) {
                        if (sound.character.toLowerCase().contains(query.toLowerCase()) || sound.title.toLowerCase().contains(query.toLowerCase())) {
                            sounds.add(sound);
                        }
                    }
                }


                Collections.sort(sounds, new Comparator<Sound>() {
                    @Override
                    public int compare(Sound o1, Sound o2) {
                        if ("alpha".equals(sortMode)) {
                            return o1.title.compareTo(o2.title);
                        } else if ("personnage".equals(sortMode)) {
                            return o1.character.compareTo(o2.character);
                        } else {
                            return o1.title.compareTo(o2.title);
                        }
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
            mRecyclerView.setAdapter(new SoundRecyclerViewAdapter(soundList));
        }
    }

}