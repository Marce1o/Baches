package com.example.demo2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final String TAG = "MainActivity";
    SensorManager sensorManager;
    Sensor accelerometer;
    Context context;
    Sensor giro;
    String sensorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    context = this;
        TextView letrero = findViewById((R.id.letrero));

        LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION);
            @Override
            public void onLocationChanged(Location location) {
                String cords = "Coordenadas => Longitud: " + location.getLongitude() + "Latitud: " + location.getLatitude();
                Log.d(TAG, cords);
            }
            public void onProviderEnabled(String provider) {}
            public void onStatusChanged(String provider, int status, Bundle extras) {}
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Log.d(TAG, "Iniciando acelerometro");
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        Log.d(TAG, "Acelerometro iniciado");

        Log.d(TAG, "Iniciando giroscopio");
        giro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, giro, SensorManager.SENSOR_DELAY_FASTEST);
        Log.d(TAG, "Giroscopio iniciado");

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
    }

    boolean isAccelData = false;
    boolean isGyroData = false;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            isAccelData = true;
            sensorName = sensorEvent.sensor.getName();
            Log.d(TAG,sensorName + " Accel X: "+ sensorEvent.values[0] + " Accel Y: "+ sensorEvent.values[1] + " Accel Z: "+ sensorEvent.values[2]);
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            isGyroData = true;
            sensorName = sensorEvent.sensor.getName();
            Log.d(TAG,sensorName + " Accel X: "+ sensorEvent.values[0] + " Accel Y: "+ sensorEvent.values[1] + " Accel Z: "+ sensorEvent.values[2]);
        }
        if (isAccelData & isGyroData) {
            // DO something here
            isAccelData = false;
            isGyroData = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}