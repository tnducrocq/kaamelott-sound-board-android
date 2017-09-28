package fr.tnducrocq.kaamelott_soundboard.fragment;

import android.app.Fragment;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import fr.tnducrocq.kaamelott_soundboard.FavoriteRecyclerViewAdapter;
import fr.tnducrocq.kaamelott_soundboard.PersonRecyclerViewAdapter;
import fr.tnducrocq.kaamelott_soundboard.R;
import fr.tnducrocq.kaamelott_soundboard.SoundRecyclerViewAdapter;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;

public class SoundRecyclerViewFragment extends Fragment implements FavoriteRecyclerViewAdapter.FavoriteRecyclerViewAdapterListener {

    private static final boolean GRID_LAYOUT = false;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private String sortMode;
    private List<Sound> soundList;

    private Unbinder unbinder;

    public static SoundRecyclerViewFragment newInstance() {
        return new SoundRecyclerViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sounds, container, false);
        unbinder = ButterKnife.bind(this, view);
        sortMode = getArguments().getString("sortMode");

        soundList = new ArrayList<>();
        Collections.addAll(soundList, (Sound[]) getArguments().getParcelableArray("soundArray"));
        setAdapter(soundList);
        return view;
    }

    private void setAdapter(final List<Sound> soundList) {
        if ("alpha".equals(sortMode)) {
            SoundRecyclerViewAdapter adapter = new SoundRecyclerViewAdapter(soundList);
            mRecyclerView.setAdapter(adapter);
        } else if ("person".equals(sortMode)) {
            PersonRecyclerViewAdapter adapter = new PersonRecyclerViewAdapter();
            adapter.init(soundList);
            mRecyclerView.setAdapter(adapter);
        } else if ("favorite".equals(sortMode)) {
            FavoriteRecyclerViewAdapter adapter = new FavoriteRecyclerViewAdapter(this);
            adapter.init(soundList);
            mRecyclerView.setAdapter(adapter);
        } else {
            mRecyclerView.setAdapter(new SoundRecyclerViewAdapter(soundList));
        }
    }

    @Override
    public void favoritesHasChange() {
        FavoriteRecyclerViewAdapter adapter = new FavoriteRecyclerViewAdapter(this);
        adapter.init(soundList);
        mRecyclerView.setAdapter(adapter);
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    public void onEvent(BusEvent.QueryEvent event) {
        String query = event.getQuery();
        List<Sound> filtered = new ArrayList<>();
        for (Sound sound : soundList) {
            if (sound.character.toLowerCase().contains(query.toLowerCase()) || sound.title.toLowerCase().contains(query.toLowerCase())) {
                filtered.add(sound);
            }
        }
        setAdapter(filtered);
    }
}