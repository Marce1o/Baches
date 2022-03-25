package com.example.demo2;

import static java.lang.Math.abs;

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
import java.lang.Math;


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

    int numSamples = 119;
    float [][] accBuff = new float[3][238];
    int take = 0;
    int samplesRead = numSamples;
    float accelerationThreshold = 12;
    long s = 0;
    int temp = 0;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            isAccelData = true;
            sensorName = sensorEvent.sensor.getName();
            //Log.d(TAG,sensorName + " Accel X: "+ sensorEvent.values[0] + " Accel Y: "+ sensorEvent.values[1] + " Accel Z: "+ sensorEvent.values[2]);
        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            isGyroData = true;
            sensorName = sensorEvent.sensor.getName();
            //Log.d(TAG,sensorName + " Accel X: "+ sensorEvent.values[0] + " Accel Y: "+ sensorEvent.values[1] + " Accel Z: "+ sensorEvent.values[2]);

            //buffer
            while(samplesRead == numSamples) {
                    Log.d(TAG,"aun no");
                    float aSum = abs(sensorEvent.values[0]) + abs(sensorEvent.values[1]) + abs(sensorEvent.values[2]);

                    for (int i = 0; i < 119; i++) {
                        for (int j = 0; j < 3; j++) {
                            accBuff[j][i] = accBuff[j][i + 1];
                        }
                    }

                    accBuff[0][118] = sensorEvent.values[0];
                    accBuff[1][118] = sensorEvent.values[1];
                    accBuff[2][118] = sensorEvent.values[2];

                    Log.d(TAG, "Suma " + Float.toString(aSum));

                    if (aSum >= accelerationThreshold){
                        samplesRead = 0;
                        break;
                    } else {
                        break;
                    }
            }

            while (samplesRead < numSamples){
//                Log.d(TAG,"ya");
                samplesRead++;
                accBuff[0][samplesRead+118]=sensorEvent.values[0];
                accBuff[1][samplesRead+118]=sensorEvent.values[1];
                accBuff[2][samplesRead+118]=sensorEvent.values[2];

                if (samplesRead == numSamples) {
//                    for(int i=0; i<238; i++){
//                        for(int j=0; j<3; j++){
//                            if(j!=2){
//                                Log.d(TAG,Float.toString(accBuff[j][i]) + ',');
//                            }
//                            if(j==2)
//                                Log.d(TAG, Double.toString(((accBuff[j][i])/1000.0f)*(75/19.41)));
//                            //tempmem[j][take*238+i]=((((buff[j][i])/1000.0f)*(75/19.41)),3);
//                        }
//                        // \n
//                    }
//                    // \n
//                    take++;
                    samplesRead = 118;
                    break;
                }

            }
        }

        if (isAccelData & isGyroData) {
            isAccelData = false;
            isGyroData = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


}