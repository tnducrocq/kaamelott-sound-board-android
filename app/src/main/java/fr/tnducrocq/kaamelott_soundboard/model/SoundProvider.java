package fr.tnducrocq.kaamelott_soundboard.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by tony on 29/05/2017.
 */

public class SoundProvider {

    public static ArrayList<Sound> getSounds(Context context){
        final ArrayList<Sound> soundList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset("sounds.json", context);
            JSONArray sounds = new JSONArray(jsonString);
            //JSONArray sounds = json.getJSONArray("sound");

            // Get Recipe objects from data
            for(int i = 0; i < sounds.length(); i++){
                JSONObject object = sounds.getJSONObject(i);

                Sound sound = new Sound();
                sound.title = object.getString("title");
                sound.character = object.getString("character");
                sound.fileName = object.getString("file");
                sound.episode = object.getString("episode");
                soundList.add(sound);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return soundList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}
