package fr.tnducrocq.kaamelott_soundboard.model.source.remote;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import fr.tnducrocq.kaamelott_soundboard.KaamelottApplication;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import io.reactivex.Observable;

/**
 * Created by tony on 29/09/2017.
 */

public class SoundRemoteSource {
    private static final String SOUNDS_KEY = "sounds.json";
    private static final String FAVORITES_KEY = "favorites.json";

    private static final String TAG = Sound.class.getSimpleName();
    private static final String BASE_URL = "https://raw.githubusercontent.com/2ec0b4/kaamelott-soundboard/master/sounds";

    private final Gson gson;

    public SoundRemoteSource() {
        gson = new GsonBuilder().create();
    }

    public Observable<ProgressHandler> downloadSounds() {
        return Observable.create(emitter -> {
            try {
                List<Sound> sounds = requestSounds();
                ProgressHandler jsonParsed = new SoundRemoteSource.ProgressHandler();
                jsonParsed.type = ProgressHandlerType.JSON;
                emitter.onNext(jsonParsed);

                for (int i = 0; i < sounds.size(); i++) {
                    Sound sound = sounds.get(i);
                    if (!containsSound(sound.fileName)) {
                        downloadSound(sound);
                    }

                    ProgressHandler handler = new SoundRemoteSource.ProgressHandler();
                    handler.type = ProgressHandlerType.SOUND;
                    handler.index = i;
                    handler.count = sounds.size();
                    handler.sound = sound;
                    emitter.onNext(handler);
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
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
            Log.d(TAG, String.format("%s is downloaded", sound.fileName));
        } finally {
            connection.disconnect();
        }
    }

    private List<Sound> requestSounds() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(BASE_URL + "/sounds.json").build();
        Response response = client.newCall(request).execute();

        String json = response.body().string();
        return Arrays.asList(gson.fromJson(json, Sound[].class));
    }

    private boolean containsSound(String fileName) {
        try {
            KaamelottApplication.getInstance().getAssets().openFd(fileName);
            return true;
        } catch (IOException e) {
            return containsSoundFile(fileName);
        }
    }

    private boolean containsSoundFile(String fileName) {
        File directory = KaamelottApplication.getInstance().getFilesDir();
        File file = new File(directory, fileName);
        return file.exists();
    }

    public enum ProgressHandlerType {
        JSON, SOUND;
    }

    public class ProgressHandler {
        public ProgressHandlerType type;
        public int index = -1;
        public int count = -1;
        public Sound sound;
    }
}
