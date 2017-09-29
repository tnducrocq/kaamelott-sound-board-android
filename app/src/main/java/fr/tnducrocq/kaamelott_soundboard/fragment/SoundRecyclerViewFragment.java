package fr.tnducrocq.kaamelott_soundboard.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import fr.tnducrocq.kaamelott_soundboard.R;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.source.SoundRepository;

public class SoundRecyclerViewFragment extends Fragment implements SoundViewAdapterListener {

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

        soundList = SoundRepository.getInstance().getSounds();
        setAdapter(soundList);
        return view;
    }

    private ISoundRecyclerViewAdapter adapter;

    private void setAdapter(final List<Sound> soundList) {
        if ("person".equals(sortMode)) {
            adapter = new PersonRecyclerViewAdapter(this);
            adapter.init(soundList);
            mRecyclerView.setAdapter((RecyclerView.Adapter) adapter);
        } else if ("favorite".equals(sortMode)) {
            adapter = new FavoriteRecyclerViewAdapter(this);
            adapter.init(soundList);
            mRecyclerView.setAdapter((RecyclerView.Adapter) adapter);
        } else {
            adapter = new SoundRecyclerViewAdapter(this);
            adapter.init(soundList);
            mRecyclerView.setAdapter((RecyclerView.Adapter) adapter);
        }
    }

    @Override
    public void favoritesHasChange() {
        if (adapter instanceof FavoriteRecyclerViewAdapter) {
            adapter.init(soundList);
            mRecyclerView.setAdapter((RecyclerView.Adapter) adapter);
        }
    }

    @Override
    public void share(Sound sound) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://kaamelott-soundboard.2ec0b4.fr/#son/" + sound.fileName);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
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