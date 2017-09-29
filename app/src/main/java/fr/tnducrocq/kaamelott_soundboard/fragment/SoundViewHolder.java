package fr.tnducrocq.kaamelott_soundboard.fragment;

/**
 * Created by tony on 29/09/2017.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.kaamelott_soundboard.R;
import fr.tnducrocq.kaamelott_soundboard.model.Person;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.source.SoundRepository;
import fr.tnducrocq.kaamelott_soundboard.player.SoundPoolPlayer;

public class SoundViewHolder extends AbstractSectionRecyclerViewAdapter.SectionHolder {

    private static final String PACKAGE_NAME = "fr.tnducrocq.kaamelott_soundboard";

    @BindView(R.id.titleTextView)
    public TextView titleTextView;

    @BindView(R.id.characterTextView)
    public TextView characterTextView;

    @BindView(R.id.episodeTextView)
    public TextView episodeTextView;

    @BindView(R.id.characterImageView)
    public ImageView characterImageView;

    @BindView(R.id.favoriteImageButton)
    public View favoriteLayout;

    @BindView(R.id.shareImageButton)
    public View shareLayout;

    @BindView(R.id.favoriteImage)
    public ImageView favoriteImage;

    SoundViewAdapterListener listener;

    public SoundViewHolder(View itemView, SoundViewAdapterListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.listener = listener;
    }

    public void bind(Context context, final Sound sound) {
        Person person = Person.get(sound.character);

        titleTextView.setText(sound.title);
        characterTextView.setText(sound.character);
        episodeTextView.setText(sound.episode);

        int imageResource = context.getResources().getIdentifier(person.getAssetShort(), "drawable", PACKAGE_NAME);
        Drawable res = context.getResources().getDrawable(imageResource, null);
        characterImageView.setImageDrawable(res);

        itemView.setOnClickListener((View view) -> {
            SoundPoolPlayer.getInstance().start(view.getContext(), sound.fileName);
        });

        shareLayout.setOnClickListener((View view) -> {
            if (listener != null) {
                listener.share(sound);
            }
        });

        favoriteLayout.setOnClickListener((View view) -> {
            if (SoundRepository.getInstance().isFavorite(sound.fileName)) {
                SoundRepository.getInstance().removeFavorite(sound.fileName);
            } else {
                SoundRepository.getInstance().addFavorite(sound.fileName);
            }
            if (listener != null) {
                listener.favoritesHasChange();
            }
            initFavoriteImageButton(sound.fileName);
        });

        initFavoriteImageButton(sound.fileName);

    }

    private void initFavoriteImageButton(String fileName) {
        if (SoundRepository.getInstance().isFavorite(fileName)) {
            favoriteImage.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            favoriteImage.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }
}
