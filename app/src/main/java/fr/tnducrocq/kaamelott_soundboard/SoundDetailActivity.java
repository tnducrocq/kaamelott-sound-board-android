package fr.tnducrocq.kaamelott_soundboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class SoundDetailActivity extends AppCompatActivity {

  public static final String TAG = SoundDetailActivity.class.getSimpleName();
    
  private WebView mWebView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sound_detail);

    // Get recipe data passed from previous activity
    String title = this.getIntent().getExtras().getString("title");
    String url = this.getIntent().getExtras().getString("url");


    SoundPoolPlayer.getInstance().start(this, url);

    // Set title on action bar of this activity
    setTitle(title);

    // Create WebView and load web page
    mWebView = (WebView) findViewById(R.id.detail_web_view);
    mWebView.loadUrl(url);
  }

}
