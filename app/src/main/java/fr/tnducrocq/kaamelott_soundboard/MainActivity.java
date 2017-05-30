package fr.tnducrocq.kaamelott_soundboard;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.SoundProvider;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        // Get data to display
        final ArrayList<Sound> recipeList = SoundProvider.getSounds(this);

        // Create adapter
        SoundAdapter adapter = new SoundAdapter(this, recipeList);

        // Create list view
        mListView = (ListView) findViewById(R.id.sound_list_view);
        mListView.setAdapter(adapter);

        // Set what happens when a list view item is clicked
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sound sound = recipeList.get(position);
                SoundPoolPlayer.getInstance().start(context, sound.fileName);

                /*Intent detailIntent = new Intent(context, SoundDetailActivity.class);
                detailIntent.putExtra("title", sound.title);
                detailIntent.putExtra("url", sound.fileName);
                startActivity(detailIntent);*/
            }

        });
    }
}

class SoundPoolPlayer {

    public static final String TAG = MainActivity.class.getSimpleName();

    private SoundPoolPlayer() {}
    private static class SingletonHolder {
        private final static SoundPoolPlayer instance = new SoundPoolPlayer();
    }
    public static SoundPoolPlayer getInstance() {
        return SingletonHolder.instance;
    }

    private MediaPlayer mediaPlayer;
/*
    public void start(Context context, Sound sound) {
        start(context, sound.fileName);
    }*/

    public void start(Context context, String fileName) {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try(AssetFileDescriptor afd = context.getAssets().openFd(fileName) ) {
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();

        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
    }
}
