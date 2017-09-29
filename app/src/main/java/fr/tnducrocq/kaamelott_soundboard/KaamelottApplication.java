package fr.tnducrocq.kaamelott_soundboard;

import android.app.Application;
import android.content.Context;

import com.orhanobut.hawk.Hawk;

import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.source.SoundRepository;

/**
 * Created by tony on 26/09/2017.
 */

public class KaamelottApplication extends Application {
    public static final String TAG = Sound.class.getSimpleName();

    private static KaamelottApplication instance;

    public static KaamelottApplication getInstance() {
        return instance;
    }

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        SoundRepository.getInstance().initCache(this);
        instance = this;
    }


}
