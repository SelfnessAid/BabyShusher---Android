package babyshusher.babyshusher.com.babyshusher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EqualizerActivity extends AppCompatActivity {

    private Button equalizerBtn;

    SharedPreferences myPrefs;
    boolean soundEqualizer = false;
    int stopValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        equalizerBtn = (Button) findViewById(R.id.equalizer_btn);
        equalizerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundEqualizer){
                    soundEqualizer = false;
                    equalizerBtn.setBackgroundResource(R.drawable.switch_off);
                }else {
                    equalizerBtn.setBackgroundResource(R.drawable.switch_on);
                    soundEqualizer = true;
                }
                update();
            }
        });

        init();
    }

    private void init() {
        myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        stopValue = Integer.parseInt(myPrefs.getString("runTime", "3"));
        soundEqualizer = myPrefs.getBoolean("soundEqualizer", false);

        if(soundEqualizer){
            equalizerBtn.setBackgroundResource(R.drawable.switch_on);
        }else {
            equalizerBtn.setBackgroundResource(R.drawable.switch_off);
        }
    }

    public void update(){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("runTime", "" + stopValue);
        editor.putBoolean("soundEqualizer", soundEqualizer);
        editor.commit();
    }
}
