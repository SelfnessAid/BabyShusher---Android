package babyshusher.babyshusher.com.babyshusher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button optionBtn;
    private Button startBtn;

    private SoundMeter mSensor;
    private Handler mHandler = new Handler();
    private Handler stopeHandler = new Handler();
    private Handler stopePlayHandler = new Handler();
    private Handler stopLHandler = new Handler();
    private PowerManager.WakeLock mWakeLock;
    private MediaPlayer mPlayer = null;
    float volume;
    AudioManager mAudioManager ;

    Timer livelTimer, volumeIncreser, volumeDecreaser;
    SharedPreferences myPrefs;
    boolean oneTimeRun = false;
    boolean startApp = false;

    ArrayList<String> soundLimits ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        optionBtn = (Button) findViewById(R.id.option_btn);
        startBtn = (Button) findViewById(R.id.start_btn);

        setGetPrefs();
        init();

        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent optionIntent = new Intent(getApplicationContext(), OptionActivity.class);
                startActivity(optionIntent);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startApp){
                    stopApplication();
                    startBtn.setBackgroundResource(R.drawable.btn_start);
                }else {
                    statApplication();
                    startBtn.setBackgroundResource(R.drawable.btn_end);
                }
            }
        });
    }

    private Runnable stopTask = new Runnable() {
        public void run() {
            stopApplication();
        }
    };

    private Runnable stopPlayTask = new Runnable() {
        public void run() {
            mPlayer.stop();
            startRecord();

            livelTimer = new Timer();
            livelTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    double amp = mSensor.getAmplitude();
                    soundLimits.add("" + amp);
                }
            }, 0, 1000);

            stopLHandler.postDelayed(stopLTask, 10*1000);
        }
    };

    private Runnable stopLTask = new Runnable() {
        public void run() {
            mSensor.stop();
            livelTimer.cancel();
            checkMicSound();
        }
    };

    private void init() {
        mSensor = new SoundMeter();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "NoiseAlert");
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        soundLimits = new ArrayList<String>();
    }

    private void setGetPrefs(){
        myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        String runTime = myPrefs.getString("runTime", "nothing");
        if(runTime.equalsIgnoreCase("nothing")){
            SharedPreferences.Editor editor = myPrefs.edit();
            editor.putString("runTime", "3");
            editor.putBoolean("soundEqualizer", false);
            editor.putString("fileName", "Application Default Sound");
            editor.commit();
        }
    }

    private void stopApplication() {
        Log.d("stop", "stop");

        volumeDecreaser = new Timer();
        volumeDecreaser.schedule(new TimerTask() {
            float i=10;
            @Override

            public void run() {
                i--;
                mPlayer.setVolume(i/10, i/10);
                if(i<=0){
                    volumeDecreaser.cancel();
                    if(livelTimer != null)  livelTimer.cancel();
                    if(mPlayer != null)  mPlayer.stop();
                    if(mSensor != null)  mSensor.stop();
                    if(stopePlayHandler != null) stopePlayHandler.removeCallbacks(stopPlayTask);
                    if(stopeHandler != null) stopeHandler.removeCallbacks(stopTask);
                    if(stopLHandler != null) stopLHandler.removeCallbacks(stopLTask);
                    startApp = false;
                }
            }
        }, 10, 50);
    }

    private void statApplication() {
        startApp = true;
        playSound(0);

        int stopT = 0;
        if(Integer.parseInt(myPrefs.getString("runTime", "3")) < 3){
            switch (Integer.parseInt(myPrefs.getString("runTime", "3"))){
                case 0:{
                    stopT = 60 * 15;
                    break;
                }case 1:{
                    stopT = 60 * 30;
                    break;
                }case 2:{
                    stopT = 60 * 45;
                    break;
                }
            }
        }else {
            stopT = (Integer.parseInt(myPrefs.getString("runTime", "3")) - 2) * 60 * 60 ;
        }
        stopT = stopT * 1000;
        stopeHandler.postDelayed(stopTask, stopT);

        volumeIncreser = new Timer();
        volumeIncreser.schedule(new TimerTask() {
            float i=0;
            @Override

            public void run() {
                i++;
                Log.d("ddd", "DDD" + i);
                mPlayer.setVolume(i/10, i/10);
                if(i>10){
                    volumeIncreser.cancel();
                }
            }
        }, 100, 1000);

        oneTimeRun = true;
    }

    private void playSound(int volume){
        soundLimits.clear();

        String str = myPrefs.getString("fileName", "nothing");

        if(str.equalsIgnoreCase("Application Default Sound")){
            mPlayer = MediaPlayer.create(this, R.raw.shush_v2);
        } else{
            File mediaDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/bb");
            if (!mediaDir.exists()){
                mediaDir.mkdir();
            }
            String mFileName = mediaDir.getAbsolutePath();
            mFileName += "/" + str;
            File newfile =  new File(mFileName);
            Uri mp3Uri = Uri.fromFile(newfile);
            mPlayer =MediaPlayer.create(this, mp3Uri);
        }
        mPlayer.setLooping(true);
        mPlayer.start();
        mPlayer.setVolume(volume, volume);

        if(myPrefs.getBoolean("soundEqualizer", false)){
            stopePlayHandler.postDelayed(stopPlayTask, 300 * 1000);
        }
    }

    private void checkMicSound(){
        double micLevel = 2;
        int count = 0;
        for (int i=0; i< soundLimits.size(); i++) {
            double micPower = Double.parseDouble(soundLimits.get(i));
            if (micPower > micLevel) {
                count ++;
            }
        }
        if((soundLimits.size() / 2) < count ){
            playSound(15);
        }else {
            playSound(3);
        }
    }

    private void startRecord() {
        mSensor.start();
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
    }
}
