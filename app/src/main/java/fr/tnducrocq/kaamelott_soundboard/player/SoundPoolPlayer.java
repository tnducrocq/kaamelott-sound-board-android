package fr.tnducrocq.kaamelott_soundboard.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import fr.tnducrocq.kaamelott_soundboard.KaamelottApplication;

/**
 * Created by tony on 31/05/2017.
 */

public class SoundPoolPlayer {

    public static final String TAG = SoundPoolPlayer.class.getSimpleName();

    private SoundPoolPlayer() {
    }

    private static class SingletonHolder {
        private final static SoundPoolPlayer instance = new SoundPoolPlayer();
    }

    public static SoundPoolPlayer getInstance() {
        return SingletonHolder.instance;
    }

    private MediaPlayer mediaPlayer;

    public void start(Context context, String fileName) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        File temp;
        try {
            temp = File.createTempFile("sound", ".mp3", context.getCacheDir());
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return;
        }

        try (InputStream in = KaamelottApplication.soundCache.getInputStream(fileName).getInputStream()) {
            try (FileOutputStream fos = new FileOutputStream(temp)) {
                IOUtils.copy(in, fos);
            }
            mediaPlayer.setDataSource(temp.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }
}
