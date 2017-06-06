package fr.tnducrocq.kaamelott_soundboard.model;

import fr.tnducrocq.kaamelott_soundboard.model.base.BaseModelFactory;

/**
 * Created by tony on 06/06/2017.
 */

public class SoundFactory extends BaseModelFactory<Sound> {
    @Override
    public Sound create() {
        return new Sound();
    }
}
