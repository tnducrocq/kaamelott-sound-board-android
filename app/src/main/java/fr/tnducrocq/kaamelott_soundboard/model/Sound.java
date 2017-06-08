package fr.tnducrocq.kaamelott_soundboard.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.tnducrocq.kaamelott_soundboard.model.base.BaseModel;

/**
 * Created by tony on 29/05/2017.
 */
public class Sound extends BaseModel implements Parcelable {

    public static final String TAG = Sound.class.getSimpleName();

    public String character;
    public String episode;
    public String title;
    public String fileName;

    public Sound() {
    }

    private Sound(Parcel in) {
        title = in.readString();
        character = in.readString();
        fileName = in.readString();
        episode = in.readString();
    }

    public String getCharacter() {
        return character;
    }

    public String getEpisode() {
        return episode;
    }

    public String getTitle() {
        return title;
    }

    public String getFileName() {
        return fileName;
    }

    public void parse(JSONObject object) throws JSONException {
        title = object.getString("title");
        character = object.getString("character");
        fileName = object.getString("file");
        episode = object.getString("episode");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(character);
        out.writeString(fileName);
        out.writeString(episode);
    }

    public static final Parcelable.Creator<Sound> CREATOR = new Parcelable.Creator<Sound>() {
        public Sound createFromParcel(Parcel in) {
            return new Sound(in);
        }

        public Sound[] newArray(int size) {
            return new Sound[size];
        }
    };

    public static class SoundCollection {

        public static List<Sound> filterByCharacter(List<Sound> sounds, @NonNull String character) {
            List<Sound> result = new ArrayList<>();
            for (Sound sound : sounds) {
                if (character.equals(sound.getCharacter())) {
                    result.add(sound);
                }
            }
            return result;
        }

    }
}