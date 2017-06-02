package fr.tnducrocq.kaamelott_soundboard.model.base;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tony on 30/05/2017.
 */
public abstract class BaseModel {
    public abstract void parse(JSONObject object) throws JSONException;
}