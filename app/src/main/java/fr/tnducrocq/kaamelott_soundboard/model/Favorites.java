package fr.tnducrocq.kaamelott_soundboard.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by tony on 28/09/2017.
 */

public class Favorites implements Parcelable {

    private List<String> fileNames;

    public List<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.fileNames);
    }

    public Favorites() {
    }

    protected Favorites(Parcel in) {
        this.fileNames = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Favorites> CREATOR = new Parcelable.Creator<Favorites>() {
        @Override
        public Favorites createFromParcel(Parcel source) {
            return new Favorites(source);
        }

        @Override
        public Favorites[] newArray(int size) {
            return new Favorites[size];
        }
    };
}
