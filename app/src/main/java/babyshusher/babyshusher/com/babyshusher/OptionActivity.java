package babyshusher.babyshusher.com.babyshusher;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class OptionActivity extends AppCompatActivity {

    private ImageButton quickstartBtn;
    private ImageButton faqsBtn;
    private ImageButton timerBtn;
    private ImageButton equalizerBtn;
    private ImageButton videoBtn;
    private ImageButton shareBtn;
    private ImageButton feedbackBtn;
    private ImageButton rateBtn;
    private ImageButton websiteBtn;

    private final String RATE_APP_URL = "market://details?id=com.babyshusher.android";
    private final String WEBSITE_URL = "http://www.babyshusher.com/product/baby-shusher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        quickstartBtn = (ImageButton) findViewById(R.id.quickstart_btn);
        faqsBtn = (ImageButton) findViewById(R.id.fag_btn);
        timerBtn = (ImageButton) findViewById(R.id.timer_btn);
        equalizerBtn = (ImageButton) findViewById(R.id.eualizer_btn);
        videoBtn = (ImageButton) findViewById(R.id.video_btn);
        shareBtn = (ImageButton) findViewById(R.id.share_btn);
        feedbackBtn = (ImageButton) findViewById(R.id.feedback_btn);
        rateBtn = (ImageButton) findViewById(R.id.rate_btn);
        websiteBtn = (ImageButton) findViewById(R.id.website_btn);

        quickstartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quickstartIntent = new Intent(getApplicationContext(), QuickStartActivity.class);
                startActivity(quickstartIntent);
            }
        });

        faqsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent faqIntent = new Intent(getApplicationContext(), FAQActivity.class);
                startActivity(faqIntent);
            }
        });

        timerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timerIntent = new Intent(getApplicationContext(), TimerActivity.class);
                startActivity(timerIntent);
            }
        });

        equalizerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent equalizerIntent = new Intent(getApplicationContext(), EqualizerActivity.class);
                startActivity(equalizerIntent);
            }
        });

        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(getApplicationContext(), VideoAcitivity.class);
                startActivity(videoIntent);
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I thought you’d be interested in the Baby Shusher App. It is truly a Sleep Miracle for babies, has a lot of cool features, and it even has a place to connect, share secrets, and interact with other Moms" +
                        "\n\n Check out how it works at www.BabyShusher.com");
                startActivity(Intent.createChooser(shareIntent, "Check out this cool Baby app"));
            }
        });

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"shusher@pneo.org"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Baby Shusher");

                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email,"Choose your Email Client"));
            }
        });

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rateIntent = new Intent(Intent.ACTION_VIEW);
                rateIntent.setData(Uri.parse(RATE_APP_URL));
                startActivity(rateIntent);
            }
        });

        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
                websiteIntent.setData(Uri.parse(WEBSITE_URL));
                startActivity(websiteIntent);
            }
        });
    }
}
