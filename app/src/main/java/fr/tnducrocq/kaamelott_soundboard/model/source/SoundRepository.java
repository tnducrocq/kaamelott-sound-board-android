package fr.tnducrocq.kaamelott_soundboard.model.source;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.hawk.Hawk;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.source.remote.SoundRemoteSource;
import io.reactivex.Observable;

/**
 * Created by tony on 29/09/2017.
 */

public class SoundRepository {

    private static final String TAG = Sound.class.getSimpleName();

    public static final String SOUNDS_KEY = "sounds.json";
    public static final String FAVORITES_KEY = "favorites.json";

    protected List<Sound> sounds;
    protected Set<String> favorites;

    protected final Gson gson;
    protected SoundRemoteSource remoteSource;

    private SoundRepository() {
        gson = new GsonBuilder().create();
        remoteSource = new SoundRemoteSource();
    }

    private static class SingletonHolder {
        private final static SoundRepository instance = new SoundRepository();
    }

    public static SoundRepository getInstance() {
        return SingletonHolder.instance;
    }

    public Observable<SoundRemoteSource.ProgressHandler> downloadSounds() {
        return remoteSource.downloadSounds().doOnNext(progressHandler -> progressHandler(progressHandler)).doOnComplete(() -> {
            Log.d(TAG, "downloadSounds -> doOnComplete");
        });
    }

    private void progressHandler(SoundRemoteSource.ProgressHandler handler) {
        if (handler.type == SoundRemoteSource.ProgressHandlerType.JSON) {
            sounds.clear();
        } else {
            sounds.add(handler.sound);
        }
    }

    public void initCache(Context context) {
        String json = null;
        if (!Hawk.contains(SoundRepository.SOUNDS_KEY)) {
            try (InputStream in = context.getAssets().open(SoundRepository.SOUNDS_KEY)) {
                json = IOUtils.toString(in);
                Hawk.put(json, SoundRepository.SOUNDS_KEY);
            } catch (IOException e) {
                Log.e(TAG, "FATAL ERROR", e);
                return;
            }
        } else {
            json = Hawk.get(SOUNDS_KEY);
        }
        sounds = new ArrayList<>();
        sounds.addAll(Arrays.asList(gson.fromJson(json, Sound[].class)));

        favorites = new HashSet<>();
        if (Hawk.contains(FAVORITES_KEY)) {
            Set<String> tmp = Hawk.get(FAVORITES_KEY);
            favorites.clear();
            favorites.addAll(tmp);
        }
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public void addFavorite(String fileName) {
        favorites.add(fileName);
        Hawk.put(FAVORITES_KEY, favorites);
    }

    public void removeFavorite(String fileName) {
        favorites.remove(fileName);
        Hawk.put(FAVORITES_KEY, favorites);
    }

    public boolean isFavorite(String fileName) {
        return favorites.contains(fileName);
    }


}
