package fr.tnducrocq.kaamelott_soundboard.model.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tony on 30/05/2017.
 */

public class BaseModelList<T extends BaseModel> extends ArrayList<T> {

    public void parse(BaseModelFactory<T> factory, JSONArray array) throws JSONException {
        // Get Recipe objects from data
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            T obj = factory.create();
            obj.parse(object);
            add(obj);
        }
    }
}