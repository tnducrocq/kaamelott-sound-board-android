package fr.tnducrocq.kaamelott_soundboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.kaamelott_soundboard.model.Person;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.SoundProvider;
import fr.tnducrocq.kaamelott_soundboard.player.SoundPoolPlayer;

public class SoundRecyclerViewAdapter extends RecyclerView.Adapter<SoundRecyclerViewAdapter.SoundViewHolder> {

    private static final String PACKAGE_NAME = "fr.tnducrocq.kaamelott_soundboard";
    List<Sound> soundList;
    Context context;

    public SoundRecyclerViewAdapter(List<Sound> contents) {
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
    public SoundRecyclerViewAdapter.SoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sound_card, parent, false);
        context = parent.getContext();

        return new SoundRecyclerViewAdapter.SoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SoundRecyclerViewAdapter.SoundViewHolder holder, int position) {
        Sound sound = soundList.get(position);
        holder.bind(context, sound);
    }

    public static class SoundViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleTextView)
        public TextView titleTextView;

        @BindView(R.id.characterTextView)
        public TextView characterTextView;

        @BindView(R.id.episodeTextView)
        public TextView episodeTextView;

        @BindView(R.id.characterImageView)
        public ImageView characterImageView;

        @BindView(R.id.favoriteImageButton)
        public ImageButton favoriteImageButton;

        public SoundViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, final Sound sound) {

            Person person = Person.get(sound.character);

            titleTextView.setText(sound.title);
            characterTextView.setText(person == Person.Other ? sound.character : "");
            episodeTextView.setText(sound.episode);

            int imageResource = context.getResources().getIdentifier(person.getAssetShort(), "drawable", PACKAGE_NAME);
            Drawable res = context.getResources().getDrawable(imageResource, null);
            characterImageView.setImageDrawable(res);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundPoolPlayer.getInstance().start(v.getContext(), sound.fileName);
                }
            });

            initFavoriteImageButton(sound.fileName);

            favoriteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SoundProvider.isFavorite(sound.fileName)) {
                        SoundProvider.removeFavorite(sound.fileName);
                    } else {
                        SoundProvider.addFavorite(sound.fileName);
                    }
                    initFavoriteImageButton(sound.fileName);
                }
            });
        }

        private void initFavoriteImageButton(String fileName) {
            if (SoundProvider.isFavorite(fileName)) {
                favoriteImageButton.setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                favoriteImageButton.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
        }
    }

}