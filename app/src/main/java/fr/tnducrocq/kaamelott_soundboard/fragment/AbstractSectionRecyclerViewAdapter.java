package fr.tnducrocq.kaamelott_soundboard.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.sectionedrecyclerview.ItemCoord;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;

import java.util.ArrayList;
import java.util.List;

import fr.tnducrocq.kaamelott_soundboard.R;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;

/**
 * Created by tony on 29/09/2017.
 */

public abstract class AbstractSectionRecyclerViewAdapter extends SectionedRecyclerViewAdapter<AbstractSectionRecyclerViewAdapter.SectionHolder> implements ISoundRecyclerViewAdapter {

    public static final String TAG = Sound.class.getSimpleName();
    private static final String PACKAGE_NAME = "fr.tnducrocq.kaamelott_soundboard";
    List<SectionContainer> sections = new ArrayList<>();

    Context context;
    SoundViewAdapterListener listener;

    public AbstractSectionRecyclerViewAdapter(SoundViewAdapterListener listener) {
        super();
        this.listener = listener;
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
    public void onBindHeaderViewHolder(SectionHolder holder, int section, boolean expanded) {
        SectionViewHolder sholder = (SectionViewHolder) holder;
        sholder.bind(context, sections.get(section).person);
    }

    @Override
    public void onBindViewHolder(SectionHolder holder, int section, int relativePosition, int absolutePosition) {
        SoundViewHolder sholder = (SoundViewHolder) holder;
        sholder.bind(context, sections.get(section).soundList.get(relativePosition));
    }

    @Override
    public SectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Change inflated layout based on 'header'.
        int layoutRes = viewType == VIEW_TYPE_HEADER ? R.layout.item_section_card : R.layout.item_sound_card;
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        if (viewType == VIEW_TYPE_HEADER) {
            return new SectionViewHolder(v);
        } else {
            return new SoundViewHolder(v, listener);
        }
    }

    public static class SectionContainer {
        public String person;
        public List<Sound> soundList;

        public SectionContainer(String person, List<Sound> soundList) {
            this.person = person;
            this.soundList = soundList;
        }
    }

    public static class SectionHolder extends SectionedViewHolder implements View.OnClickListener {

        public SectionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            boolean isHeader = isHeader();
            ItemCoord position = getRelativePosition();
            int section = position.section();
            int relativePos = position.relativePos();
        }
    }

}
