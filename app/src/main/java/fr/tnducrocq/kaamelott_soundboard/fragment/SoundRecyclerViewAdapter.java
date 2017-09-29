package fr.tnducrocq.kaamelott_soundboard.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.tnducrocq.kaamelott_soundboard.R;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;

public class SoundRecyclerViewAdapter extends RecyclerView.Adapter<SoundViewHolder> implements ISoundRecyclerViewAdapter {

    private static final String PACKAGE_NAME = "fr.tnducrocq.kaamelott_soundboard";
    List<Sound> soundList;
    Context context;
    SoundViewAdapterListener listener;

    public SoundRecyclerViewAdapter(SoundViewAdapterListener listener) {
        this.listener = listener;
    }

    public void init(List<Sound> contents) {
        soundList = contents;
        Collections.sort(soundList, new Comparator<Sound>() {
            @Override
            public int compare(Sound o1, Sound o2) {
                return o1.title.toLowerCase().compareTo(o2.title.toLowerCase());
            }
        });
    }

    @Override
    public int getItemCount() {
        return soundList.size();
    }

    @Override
    public SoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sound_card, parent, false);
        context = parent.getContext();

        return new SoundViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(SoundViewHolder holder, int position) {
        Sound sound = soundList.get(position);
        holder.bind(context, sound);
    }

}