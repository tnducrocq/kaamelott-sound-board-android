package fr.tnducrocq.kaamelott_soundboard.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.kaamelott_soundboard.R;
import fr.tnducrocq.kaamelott_soundboard.model.Person;

/**
 * Created by tony on 29/09/2017.
 */

public class SectionViewHolder extends AbstractSectionRecyclerViewAdapter.SectionHolder {

    private static final String PACKAGE_NAME = "fr.tnducrocq.kaamelott_soundboard";

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
