package fr.tnducrocq.kaamelott_soundboard;

import android.app.Application;
import android.content.Context;

import com.orhanobut.hawk.Hawk;

/**
 * Created by tony on 26/09/2017.
 */

public class KaamelottApplication extends Application {

    private static KaamelottApplication instance;

    public static KaamelottApplication getInstance() {
        return instance;
    }

    Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        Hawk.init(this).build();
        instance = this;
    }


}
