package fr.tnducrocq.kaamelott_soundboard.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.IOException;

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

    public void start(@NonNull Context context, @NonNull String fileName) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try (AssetFileDescriptor afd = context.getAssets().openFd(fileName)) {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ioe) {
            Log.w(TAG, "WARNING", ioe);

            try {

                File directory = KaamelottApplication.getInstance().getFilesDir();
                File localFile = new File(directory, fileName);
                if (localFile != null) {
                    mediaPlayer.setDataSource(localFile.getAbsolutePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }
}
