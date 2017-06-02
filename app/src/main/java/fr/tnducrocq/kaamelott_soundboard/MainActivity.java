package fr.tnducrocq.kaamelott_soundboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.kaamelott_soundboard.fragment.AlphaRecyclerViewFragment;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        ButterKnife.bind(this);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % getCount()) {
                    case 0:
                        return AlphaRecyclerViewFragment.newInstance();
                    case 1:
                        return AlphaRecyclerViewFragment.newInstance();
                    default:
                        return AlphaRecyclerViewFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % getCount()) {
                    case 0:
                        return "Alphabétique";
                    case 1:
                        return "Personnage";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(R.color.colorPrimary, "http://www.demotivateur.fr/images-buzz/4771/7d70499bb048568c5dzd6d12d5b89aafbb6.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(R.color.colorPrimary, "http://savemybrain.net/v2/wp-content/uploads/2012/04/arthur.jpg");
                }
                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }
}
