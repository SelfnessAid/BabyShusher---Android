package babyshusher.babyshusher.com.babyshusher;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class TimerActivity extends AppCompatActivity {

    private Spinner timer;

    SharedPreferences myPrefs;
    int stopValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timer = (Spinner) findViewById(R.id.time_selector);

        init();
    }

    private void init() {
        myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        stopValue = Integer.parseInt(myPrefs.getString("runTime", "3"));
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.planets_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        timer.setAdapter(staticAdapter);
        timer.setSelection(stopValue);
        timer.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    public void update(){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("runTime", "" + stopValue);
        editor.commit();
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            stopValue = pos;
            update();
        }

        public void onNothingSelected(AdapterView parent) {

        }
    }
}
