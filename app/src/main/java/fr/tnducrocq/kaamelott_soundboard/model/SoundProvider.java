package fr.tnducrocq.kaamelott_soundboard.model;

import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import fr.tnducrocq.kaamelott_soundboard.KaamelottApplication;
import fr.tnducrocq.kaamelott_soundboard.disklrucache.SimpleDiskCache;
import fr.tnducrocq.kaamelott_soundboard.model.base.BaseModelList;

/**
 * Created by tony on 29/05/2017.
 */
public class SoundProvider {

    public static final String TAG = Sound.class.getSimpleName();

    private static final String BASE_URL = "https://raw.githubusercontent.com/tnducrocq/kaamelott-soundboard/descripteur/sounds";
    private static final String SOUNDS_KEY = "sounds.json";
    private static final List<Sound> sounds = new ArrayList<>();

    public static void initSounds(@NonNull Delegate delegate) throws IOException, JSONException {
        sounds.clear();
        try {
            sounds.addAll(requestSounds(delegate));
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
            final BaseModelList<Sound> soundList = new BaseModelList<>();
            if (KaamelottApplication.jsonCache.contains(SOUNDS_KEY)) {
                SimpleDiskCache.StringEntry entry = KaamelottApplication.jsonCache.getString(SOUNDS_KEY);
                String jsonString = entry.getString();
                JSONArray array = new JSONArray(jsonString);
                soundList.parse(new SoundFactory(), array);
                sounds.addAll(soundList);
            }
        }
    }

    public static List<Sound> requestSounds(@NonNull Delegate delegate) throws IOException, JSONException {

        URL soundsURL = new URL(BASE_URL + "/" + SOUNDS_KEY);
        HttpsURLConnection soundsConnection = (HttpsURLConnection) soundsURL.openConnection();
        soundsConnection.setDoInput(true);
        soundsConnection.setDoOutput(false);
        soundsConnection.setRequestProperty("Content-Type", "application/json");
        soundsConnection.setRequestMethod("GET");

        final BaseModelList<Sound> soundList = new BaseModelList<>();
        try (InputStream in = new BufferedInputStream(soundsConnection.getInputStream())) {
            String jsonString = IOUtils.toString(in);
            JSONArray array = new JSONArray(jsonString);
            soundList.parse(new SoundFactory(), array);

            KaamelottApplication.jsonCache.put(SOUNDS_KEY, jsonString);
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
            KaamelottApplication.applicationContext.getAssets().openFd(sound.fileName);
            return false;
        } catch (FileNotFoundException e) {
            Log.d(TAG, sound.fileName + " is missing in assets directories");
            return !KaamelottApplication.soundCache.contains(sound.fileName);
        }
    }

    private static void downloadSound(Sound sound) throws IOException {
        URL url = new URL(BASE_URL + "/" + sound.fileName);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setRequestMethod("GET");
        try (InputStream in = new BufferedInputStream(connection.getInputStream())) {
            KaamelottApplication.soundCache.put(sound.fileName, in);
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
}
