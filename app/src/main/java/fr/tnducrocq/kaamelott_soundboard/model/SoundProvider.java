package fr.tnducrocq.kaamelott_soundboard.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import fr.tnducrocq.kaamelott_soundboard.KaamelottApplication;
import fr.tnducrocq.kaamelott_soundboard.model.base.BaseModelList;

/**
 * Created by tony on 29/05/2017.
 */
public class SoundProvider {

    public static final String TAG = Sound.class.getSimpleName();

    private static final String BASE_URL = "https://raw.githubusercontent.com/2ec0b4/kaamelott-soundboard/master/sounds";
    private static final String SOUNDS_KEY = "sounds.json";
    private static final String FAVORITES_KEY = "favorites.json";
    private static final List<Sound> sounds = new ArrayList<>();
    private static final Set<String> favorites = new HashSet<>();

    public static void initSounds(@NonNull Delegate delegate) throws IOException, JSONException {
        if (Hawk.contains(FAVORITES_KEY)) {
            Set<String> tmp = Hawk.get(FAVORITES_KEY);
            favorites.clear();
            favorites.addAll(tmp);
        }

        sounds.clear();
        try {
            sounds.addAll(requestSounds(delegate));
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
            final BaseModelList<Sound> soundList = new BaseModelList<>();

            String jsonString = null;
            if (Hawk.contains(SOUNDS_KEY)) {
                jsonString = Hawk.get(SOUNDS_KEY);
            } else {
                try (InputStream in = KaamelottApplication.getInstance().getAssets().open(SOUNDS_KEY)) {
                    jsonString = IOUtils.toString(in);
                }
            }

            JSONArray array = new JSONArray(jsonString);
            soundList.parse(new SoundFactory(), array);
            sounds.addAll(soundList);
        }
    }

    public static List<Sound> requestSounds(@NonNull Delegate delegate) throws IOException, JSONException {

        URL soundsURL = new URL(BASE_URL + "/" + SOUNDS_KEY);
        HttpsURLConnection soundsConnection = (HttpsURLConnection) soundsURL.openConnection();
        soundsConnection.setDoInput(true);
        soundsConnection.setDoOutput(false);
        soundsConnection.setConnectTimeout(5000);
        soundsConnection.setRequestProperty("Content-Type", "application/json");
        soundsConnection.setRequestMethod("GET");

        final BaseModelList<Sound> soundList = new BaseModelList<>();
        try (InputStream in = new BufferedInputStream(soundsConnection.getInputStream())) {
            String jsonString = IOUtils.toString(in);
            JSONArray array = new JSONArray(jsonString);
            soundList.parse(new SoundFactory(), array);

            Hawk.put(SOUNDS_KEY, jsonString);
            delegate.jsonParsed();
        } finally {
            soundsConnection.disconnect();
        }

        List<Sound> downloadList = new ArrayList<>();
        for (Sound sound : soundList) {
            try {
                if (downloadSoundIsNeeded(sound)) {
                    downloadList.add(sound);
                }
            } catch (IOException e) {
                Log.e(TAG, "ERROR", e);
            }
        }

        for (int i = 0; i < downloadList.size(); i++) {
            Sound sound = downloadList.get(i);
            try {
                downloadSound(sound);
                delegate.soundDownloaded(sound.fileName, i + 1, downloadList.size());
            } catch (IOException e) {
                Log.e(TAG, "ERROR", e);
            }
        }
        return soundList;
    }

    private static boolean downloadSoundIsNeeded(Sound sound) throws IOException {
        try {
            KaamelottApplication.getInstance().getAssets().openFd(sound.fileName);
            return false;
        } catch (FileNotFoundException e) {
            Log.d(TAG, sound.fileName + " is missing in assets directories");
            return !isSoundContains(sound.fileName);
        }
    }

    private static void downloadSound(Sound sound) throws IOException {
        URL url = new URL(BASE_URL + "/" + sound.fileName);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setRequestMethod("GET");

        File directory = KaamelottApplication.getInstance().getFilesDir();
        File localFile = new File(directory, sound.fileName);
        localFile.createNewFile();
        try (InputStream in = new BufferedInputStream(connection.getInputStream()); OutputStream out = new FileOutputStream(localFile)) {
            IOUtils.copy(in, out);
            Log.i(TAG, String.format("%s is downloaded", sound.fileName));
        } finally {
            connection.disconnect();
        }
    }

    public static List<Sound> getSounds() {
        return sounds;
    }

    public static interface Delegate {

        public void jsonParsed();

        public void soundDownloaded(String fileName, int current, int total);

    }

    public static boolean isSoundContains(String fileName) {
        File directory = KaamelottApplication.getInstance().getFilesDir();
        File file = new File(directory, fileName);
        return file.exists();
    }

    public static void addFavorite(String fileName) {
        favorites.add(fileName);
        Hawk.put(FAVORITES_KEY, favorites);
    }

    public static void removeFavorite(String fileName) {
        favorites.remove(fileName);
        Hawk.put(FAVORITES_KEY, favorites);
    }

    public static boolean isFavorite(String fileName) {
        return favorites.contains(fileName);
    }
}
