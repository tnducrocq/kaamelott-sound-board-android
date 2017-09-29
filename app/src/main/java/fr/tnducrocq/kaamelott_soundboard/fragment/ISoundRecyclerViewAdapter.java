package fr.tnducrocq.kaamelott_soundboard.fragment;

import java.util.List;

import fr.tnducrocq.kaamelott_soundboard.model.Sound;

/**
 * Created by tony on 29/09/2017.
 */

public interface ISoundRecyclerViewAdapter {
    public abstract void init(List<Sound> contents);
}
