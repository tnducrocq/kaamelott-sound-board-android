package fr.tnducrocq.kaamelott_soundboard.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.tnducrocq.kaamelott_soundboard.KaamelottApplication;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;

/**
 * Created by tony on 08/06/2017.
 */

public class PersonRecyclerViewAdapter extends AbstractSectionRecyclerViewAdapter {

    public static final String TAG = Sound.class.getSimpleName();

    public PersonRecyclerViewAdapter(SoundViewAdapterListener listener) {
        super(listener);
    }

    public void init(List<Sound> contents) {
        this.sections = new ArrayList<>();
        this.context = KaamelottApplication.getInstance();

        Set<String> uniqPersons = new HashSet<>();
        for (Sound sound : contents) {
            uniqPersons.add(sound.getCharacter());
        }

        List<String> persons = new ArrayList<>(uniqPersons);
        Collections.sort(persons, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });

        for (String person : persons) {
            List<Sound> sounds = Sound.SoundCollection.filterByCharacter(contents, person);
            Collections.sort(sounds, new Comparator<Sound>() {
                @Override
                public int compare(Sound o1, Sound o2) {
                    return o1.title.toLowerCase().compareTo(o2.title.toLowerCase());
                }
            });
            sections.add(new AbstractSectionRecyclerViewAdapter.SectionContainer(person, sounds));
        }
    }


}
