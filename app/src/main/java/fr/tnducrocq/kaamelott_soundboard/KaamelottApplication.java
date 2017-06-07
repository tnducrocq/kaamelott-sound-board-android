package fr.tnducrocq.kaamelott_soundboard;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.IOException;

import fr.tnducrocq.kaamelott_soundboard.disklrucache.SimpleDiskCache;

/**
 * Created by tony on 30/05/2017.
 */

public class KaamelottApplication extends Application {

    //public static Bus bus;
    public static Context applicationContext;
    public static SimpleDiskCache soundCache;
    public static SimpleDiskCache jsonCache;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;

        try {
            //bus = new Bus();
            soundCache = SimpleDiskCache.open(new File(getCacheDir() + "/sounds"), 1, Long.MAX_VALUE);
            jsonCache = SimpleDiskCache.open(new File(getCacheDir() + "/jsons"), 1, Long.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
