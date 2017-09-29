package fr.tnducrocq.kaamelott_soundboard.fragment;

import fr.tnducrocq.kaamelott_soundboard.model.Sound;

/**
 * Created by tony on 29/09/2017.
 */

public interface SoundViewAdapterListener {
    public void favoritesHasChange();
    public void share(Sound sound);
}