package fr.tnducrocq.kaamelott_soundboard.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
            try (InputStream in = KaamelottApplication.soundCache.getInputStream(fileName).getInputStream()) {
                File file = getFile(context, in);
                if (file != null) {
                    mediaPlayer.setDataSource(file.getAbsolutePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        }
    }

    @Nullable
    private File getFile(@NonNull Context context, @NonNull InputStream in) throws IOException {
        File temp;
        try {
            temp = File.createTempFile("sound", ".mp3", context.getCacheDir());
        } catch (Exception e) {
            Log.e(TAG, "ERROR", e);
            return null;
        }
        try (FileOutputStream fos = new FileOutputStream(temp)) {
            IOUtils.copy(in, fos);
            return temp;
        }
    }
}
