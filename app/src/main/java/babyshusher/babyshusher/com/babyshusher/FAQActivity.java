package babyshusher.babyshusher.com.babyshusher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class FAQActivity extends AppCompatActivity {

    private WebView faqView;

    private final String WEBPAGE_URL = "file:///android_res/raw/faq.htm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        faqView = (WebView) findViewById(R.id.faq_webview);
        faqView.loadUrl(WEBPAGE_URL);
    }
}
