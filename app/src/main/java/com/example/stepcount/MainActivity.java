package com.example.stepcount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private static final int PHYISCAL_ACTIVITY = 0;
    TextView txtstep , txtdistance , txtcalories , txtcoins , txtdate;

    SensorManager sensorManager;
    Sensor countSensor;

    boolean runinng = false  ;

    float calories;
    float distance;
    int stepcount = 0;
    String[] splitdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PHYISCAL_ACTIVITY);
            }
        }

        txtstep = findViewById(R.id.steps);
        txtdistance = findViewById(R.id.distance);
        txtcalories = findViewById(R.id.calories);
        txtcoins = findViewById(R.id.coins);
        txtdate = findViewById(R.id.date);

        sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);


        assert sensorManager != null;
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null){
            Toast.makeText(this, "Sensor is present", Toast.LENGTH_SHORT).show();
            countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            runinng = true;
        }else {
            txtstep.setText("Counter sensor is not present");
            runinng = false;
        }

        Date currenttime = Calendar.getInstance().getTime();
        String formatteddate = DateFormat.getDateInstance(DateFormat.LONG).format(currenttime);



        String strDate = "";
        if (DateUtils.isToday(currenttime.getTime()))
            strDate = "Today";
//        else if (DateUtils.isToday(currenttime.getTime() + DateUtils.DAY_IN_MILLIS))
//            strDate = "Yesterday";
        else {
            txtdate.setText(formatteddate);
        }

        txtdate.setText(strDate);





    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor  == countSensor){


            stepcount = (int) sensorEvent.values[0];
            txtstep.setText(String.valueOf(stepcount));

            calories =  Math.round((float)0.04 * stepcount);
            distance = Math.round((float) 0.80 * stepcount);

            txtcalories.setText(String.valueOf(calories));
            txtdistance.setText(String.valueOf(distance));

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null);
        sensorManager.registerListener(this , countSensor , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null);
        sensorManager.unregisterListener(this , countSensor);
    }
}