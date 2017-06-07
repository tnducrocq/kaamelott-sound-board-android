package fr.tnducrocq.kaamelott_soundboard.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import fr.tnducrocq.kaamelott_soundboard.R;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.SoundProvider;

public class UpdateDataFragment extends Fragment {

    public static UpdateDataFragment newInstance() {
        return new UpdateDataFragment();
    }

    @BindView(R.id.waiting_text)
    TextView mWaitingTextView;

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waiting_view, container, false);
        unbinder = ButterKnife.bind(this, view);

        new KaamelottAsyncTask().execute();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class KaamelottAsyncTask extends AsyncTask<Void, BusEvent.Event, List<Sound>> {

        @Override
        protected List<Sound> doInBackground(Void... params) {
            try {
                SoundProvider.initSounds(new SoundProvider.Delegate() {

                    @Override
                    public void jsonParsed() {
                        publishProgress(new BusEvent.JSONParsed());
                    }

                    @Override
                    public void soundDownloaded(String fileName, int current, int total) {
                        publishProgress(new BusEvent.Progress(fileName, current, total));
                    }
                });
                return SoundProvider.getSounds();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(BusEvent.Event... values) {
            super.onProgressUpdate(values);
            if (values.length == 0) {
                return;
            }

            if (values[0] instanceof BusEvent.JSONParsed) {
                EventBus.getDefault().post((BusEvent.JSONParsed) values[0]);
                mWaitingTextView.setText("Actualisation de la liste des sons");
            } else if (values[0] instanceof BusEvent.Progress) {
                BusEvent.Progress event = (BusEvent.Progress) values[0];
                int arround = (int) Math.round(100.0 / event.total * event.current);
                mWaitingTextView.setText(arround + "%");
            }
        }

        @Override
        protected void onPostExecute(final List<Sound> soundList) {
            super.onPostExecute(soundList);
            EventBus.getDefault().post(new BusEvent.Finish(soundList));
        }
    }

}
