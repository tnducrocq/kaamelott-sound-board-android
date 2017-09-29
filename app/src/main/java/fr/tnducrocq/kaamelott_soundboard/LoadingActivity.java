package fr.tnducrocq.kaamelott_soundboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.tnducrocq.kaamelott_soundboard.model.Sound;
import fr.tnducrocq.kaamelott_soundboard.model.source.SoundRepository;
import fr.tnducrocq.kaamelott_soundboard.model.source.remote.SoundRemoteSource;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tony on 29/09/2017.
 */

public class LoadingActivity extends AppCompatActivity {

    private static final String TAG = Sound.class.getSimpleName();

    @BindView(R.id.waiting_text)
    TextView mWaitingTextView;

    private Disposable soundSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);

        Observable<SoundRemoteSource.ProgressHandler> downloadObservable = SoundRepository.getInstance().downloadSounds();
        soundSubscription = downloadObservable.
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).
                subscribe(handler -> {
                    Log.d(TAG, "subscribe -> handler");
                    if (handler.type == SoundRemoteSource.ProgressHandlerType.JSON) {
                        mWaitingTextView.setText("Actualisation de la liste des sons");
                    } else if (handler.type == SoundRemoteSource.ProgressHandlerType.SOUND) {
                        int arround = (int) Math.round(100.0 / handler.count * (handler.index + 1));
                        mWaitingTextView.setText(arround + "%");
                    }
                }, error -> {
                    Log.e(TAG, "subscribe -> onError", error);

                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                }, () -> {
                    Log.d(TAG, "subscribe -> onCompleted");

                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundSubscription != null && !soundSubscription.isDisposed()) {
            soundSubscription.dispose();
        }
    }
}
