package humaneer.org.wearablerunning.Service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import humaneer.org.wearablerunning.OnTextEventListener;

public class ServiceGPS extends Service {

//    GPSListener gpsListener;

    ArrayList<LatLng> listLatLng;
    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;
    // 네트워크 사용유무
    boolean isNetworkEnabled = false;
    // GPS 상태값
    boolean isGetLocation = false;
    Location mLocation;
    double latitude; // 위도
    double longitude; // 경도

    static double distance = 0.0;

    public static double getSpeed() {
        return speed;
    }

    static double speed = 0.0;

    // 최소 GPS 정보 업데이트 거리 20미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 5초
    private static final long MIN_TIME_BW_UPDATES = 1000*5;//1000 * 10 * 1;

    public static double getDistance() {
        return distance;
    }

    private OnTextEventListener mOnTextEventListener;

    public void setOnTextEventListener(OnTextEventListener listener) {
        mOnTextEventListener = listener;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        new Thread(new Runnable() {
            @Override
            public void run() {

                running();
            }
        }).run();

        Log.d("### GPS Service", "onStartCommand");
        return START_NOT_STICKY;
    }

    public void running() {

        Log.d("### GPS Service", "running");
        Toast.makeText(getApplicationContext(), "running", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new GpsListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_DISTANCE_CHANGE_FOR_UPDATES, MIN_TIME_BW_UPDATES, locationListener);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        removeLocationManager();
//        stopUsingGPS(); // 일단 시도해봄.
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private final static String TAG = ServiceGPS.class.getSimpleName();


    /**
     * Location variables
     */
    LocationManager locationManager;
    LocationListener locationListener;

    public void removeLocationManager() {

        locationManager.removeUpdates(locationListener);
    }

    private class GpsListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                speed = Double.parseDouble(String.format("%.3f", location.getSpeed()));

                Log.d("### GpsListener", "speed:"+speed);
                Toast.makeText(getApplicationContext(), "speed:"+speed, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
}

// https://brunch.co.kr/@babosamo/50  권한 따기