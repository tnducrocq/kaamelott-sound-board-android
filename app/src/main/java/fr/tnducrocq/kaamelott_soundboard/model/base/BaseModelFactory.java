package fr.tnducrocq.kaamelott_soundboard.model.base;

/**
 * Created by tony on 30/05/2017.
 */

public abstract class BaseModelFactory<T extends BaseModel> {
    public abstract T create();
}
