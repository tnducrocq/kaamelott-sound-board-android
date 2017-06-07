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
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import fr.tnducrocq.kaamelott_soundboard.R;
import fr.tnducrocq.kaamelott_soundboard.SoundRecyclerViewAdapter;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;

public class RecyclerViewFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private String sortMode;
    private List<Sound> soundList;

    private Unbinder unbinder;

    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        unbinder = ButterKnife.bind(this, view);

        sortMode = getArguments().getString("sortMode");
        soundList = new ArrayList<>();
        Collections.addAll(soundList, (Sound[]) getArguments().getParcelableArray("soundArray"));
        Collections.sort(soundList, new Comparator<Sound>() {
            @Override
            public int compare(Sound o1, Sound o2) {
                if ("alpha".equals(sortMode)) {
                    return o1.title.toLowerCase().compareTo(o2.title.toLowerCase());
                } else if ("personnage".equals(sortMode)) {
                    return o1.character.toLowerCase().compareTo(o2.character.toLowerCase());
                } else {
                    return o1.title.toLowerCase().compareTo(o2.title.toLowerCase());
                }
            }
        });
        mRecyclerView.setAdapter(new SoundRecyclerViewAdapter(soundList));
        return view;
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

        mRecyclerView.setAdapter(new SoundRecyclerViewAdapter(filtered));
    }
}