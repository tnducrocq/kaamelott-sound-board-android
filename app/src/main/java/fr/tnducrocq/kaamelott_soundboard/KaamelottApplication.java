package fr.tnducrocq.kaamelott_soundboard;

import android.app.Application;

import java.io.File;
import java.io.IOException;

import fr.tnducrocq.kaamelott_soundboard.disklrucache.SimpleDiskCache;

/**
 * Created by tony on 30/05/2017.
 */

public class KaamelottApplication extends Application {


    public static SimpleDiskCache soundCache;
    public static SimpleDiskCache jsonCache;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            soundCache = SimpleDiskCache.open(new File(getCacheDir() + "/sounds"), 1, Long.MAX_VALUE);
            jsonCache = SimpleDiskCache.open(new File(getCacheDir() + "/jsons"), 1, Long.MAX_VALUE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
