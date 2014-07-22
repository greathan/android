package com.example.android;

import android.widget.TextView;
import android.app.Activity;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.content.Context;

import java.text.DecimalFormat;

/**
 * Created by greathan on 13-11-29.
 */
public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mPressure;
    private Sensor mLight;
    private Sensor mTemp;

    private TextView tv;
    private TextView tvLight;
    private TextView tvTemp;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor);

        tv = (TextView) findViewById(R.id.pressure);
        tvLight = (TextView) findViewById(R.id.light);
        tvTemp = (TextView) findViewById(R.id.temp);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mTemp = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        int type = sensor.getType();
        float v1 = event.values[0];
        if (type == Sensor.TYPE_PRESSURE) {
            // Do something with this sensor data.
            tv.setText(String.valueOf(v1) + "mPa");
        } else if (type == Sensor.TYPE_LIGHT) {
            tvLight.setText(new DecimalFormat("#.0").format(v1) + "lux");
        } else if (type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            tvTemp.setText(String.valueOf(v1) + "°C");
        }
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mTemp, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
