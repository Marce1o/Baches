package com.example.demo2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity implements  SensorEventListener {
    private static final String TAG = "MainActivity";
    private Array bufferAccelX;
    private Array bufferAccelY;
    private Array bufferAccelZ;
    private SensorManager sensorManager;
    Sensor accelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Iniciando");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        //giroscopio
        //gps
        Log.d(TAG, "Sensor iniciado");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "Accel X: "+ sensorEvent.values[0] + " Accel Y: "+ sensorEvent.values[1] + " Accel Z: "+ sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}