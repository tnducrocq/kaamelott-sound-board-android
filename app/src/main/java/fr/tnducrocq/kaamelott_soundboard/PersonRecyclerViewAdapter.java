package fr.tnducrocq.kaamelott_soundboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.ItemCoord;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.kaamelott_soundboard.model.Person;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.player.SoundPoolPlayer;

/**
 * Created by tony on 08/06/2017.
 */

public class PersonRecyclerViewAdapter extends SectionedRecyclerViewAdapter<PersonRecyclerViewAdapter.MainVH> {

    public static final String TAG = Sound.class.getSimpleName();
    private static final String PACKAGE_NAME = "fr.tnducrocq.kaamelott_soundboard";
    List<SectionContainer> sections = new ArrayList<>();
    Context context;

    public PersonRecyclerViewAdapter() {
        super();
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
            sections.add(new SectionContainer(person, sounds));
        }
    }

    @Override
    public int getSectionCount() {
        if (sections == null) {
            return 0;
        }
        return sections.size();
    }

    @Override
    public int getItemCount(int section) {
        if (sections == null) {
            return 0;
        }
        return sections.get(section).soundList.size();
    }

    @Override
    public void onBindHeaderViewHolder(MainVH holder, int section, boolean expanded) {
        SectionViewHolder sholder = (SectionViewHolder) holder;
        sholder.bind(context, sections.get(section).person);
    }

    @Override
    public void onBindViewHolder(MainVH holder, int section, int relativePosition, int absolutePosition) {
        SoundViewHolder sholder = (SoundViewHolder) holder;
        sholder.bind(context, sections.get(section).soundList.get(relativePosition));
    }

    @Override
    public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
        // Change inflated layout based on 'header'.
        int layoutRes = viewType == VIEW_TYPE_HEADER ? R.layout.item_section_card : R.layout.item_sound_min;
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        if (viewType == VIEW_TYPE_HEADER) {
            return new SectionViewHolder(v);
        } else {
            return new SoundViewHolder(v);
        }
    }

    private static class SectionContainer {
        public String person;
        public List<Sound> soundList;

        public SectionContainer(String person, List<Sound> soundList) {
            this.person = person;
            this.soundList = soundList;
        }
    }

    public static class MainVH extends SectionedViewHolder implements View.OnClickListener {

        public MainVH(View itemView) {
            super(itemView);
            // Setup view holder.
            // You'd want some views to be optional, e.g. for header vs. normal.
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // SectionedViewHolder exposes methods such as:
            boolean isHeader = isHeader();
            ItemCoord position = getRelativePosition();
            int section = position.section();
            int relativePos = position.relativePos();
        }
    }


    public static class SectionViewHolder extends MainVH {


        @BindView(R.id.characterTextView)
        public TextView characterTextView;

        @BindView(R.id.characterImageView)
        public ImageView characterImageView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, final String character) {
            Person person = Person.get(character);
            characterTextView.setText(character);
            int imageResource = context.getResources().getIdentifier(person.getAssetLong(), "drawable", PACKAGE_NAME);
            Drawable res = null;
            if (imageResource != 0) {
                res = context.getResources().getDrawable(imageResource, null);
            }
            characterImageView.setImageDrawable(res);
        }
    }


    public static class SoundViewHolder extends MainVH {

        @BindView(R.id.titleTextView)
        public TextView titleTextView;

        @BindView(R.id.episodeTextView)
        public TextView episodeTextView;

        public SoundViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, final Sound sound) {

            Person person = Person.get(sound.character);

            titleTextView.setText(sound.title);
            episodeTextView.setText(sound.episode);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SoundPoolPlayer.getInstance().start(v.getContext(), sound.fileName);
                }
            });
        }
    }
}
