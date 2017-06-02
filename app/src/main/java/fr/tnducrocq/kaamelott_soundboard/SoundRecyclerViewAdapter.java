package fr.tnducrocq.kaamelott_soundboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.player.SoundPoolPlayer;

public class SoundRecyclerViewAdapter extends RecyclerView.Adapter<SoundRecyclerViewAdapter.SoundViewHolder> {

    List<Sound> contents;

    public SoundRecyclerViewAdapter(List<Sound> contents) {
        this.contents = contents;
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public SoundRecyclerViewAdapter.SoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sound_card, parent, false);
        return new SoundRecyclerViewAdapter.SoundViewHolder(view) {
        };
    }

    @Override
    public void onBindViewHolder(SoundRecyclerViewAdapter.SoundViewHolder holder, int position) {
        Sound sound = contents.get(position);
        holder.bind(sound);
    }

    public static class SoundViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView characterTextView;
        public TextView episodeTextView;

        public SoundViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            characterTextView = (TextView) itemView.findViewById(R.id.characterTextView);
            episodeTextView = (TextView) itemView.findViewById(R.id.episodeTextView);

        }

        public void bind(final Sound sound) {
            titleTextView.setText(sound.title);
            characterTextView.setText(sound.character);
            episodeTextView.setText(sound.episode);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundPoolPlayer.getInstance().start(v.getContext(), sound.fileName);


                }
            });
        }
    }
}