package humaneer.org.wearablerunning.Service;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import humaneer.org.wearablerunning.Activity.MainActivity;
import humaneer.org.wearablerunning.BLE.BluetoothLeService;
import humaneer.org.wearablerunning.Fragment.MainFragment;
import humaneer.org.wearablerunning.OnTextEventListener;

public class ServiceGPS extends Service {

    public static int LocationChangeedCallCounter = 0;
    static double prevVelocity = 0.0;

    GPSListener gpsListener;

    ArrayList<LatLng> listLatLng;
    public static boolean IsServiceRunning = false;
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
    static double prevDistance = 0.0;

    public static double getSpeed() {
        return speed;
    }

    static double speed = 0.0;

    // 최소 GPS 정보 업데이트 거리 20미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 5초
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 10 * 1;

    protected LocationManager locationManager;


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

        if(!init()) {

            Toast.makeText(getApplicationContext(), "Service can't Started.", Toast.LENGTH_SHORT).show();
            Log.d("### ServiceGPS", "Service can't Started.");
        }

        Log.d("### ServiceGPS", "onStartCommand");
        return START_STICKY;
    }

    public boolean init() {

        gpsListener = new GPSListener();
        listLatLng = new ArrayList<>();


        try {
            if (locationManager == null) {
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager == null) {
                    Log.e(TAG, "Unable to initialize BluetoothManager.");
                    return false;
                }
            }

            // GPS 정보 가져오기
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // 현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("## Alert ##", "GPS 와 네트워크 사용이 가능하지 않음.");
                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현

                return false;
            } else {
                Log.d("## Alert ##", "GPS 와 네트워크 사용 가능.");
                this.isGetLocation = true;

                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsListener);

                    if (locationManager != null) {
                        mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (mLocation != null) {
                            // 위도 경도 저장
                            latitude = mLocation.getLatitude();
                            longitude = mLocation.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    Log.d("## Alert ##", "isGPSEnabled in getmLocation()");
                    if (mLocation == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsListener);
                        if (locationManager != null) {
                            mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (mLocation != null) {
                                latitude = mLocation.getLatitude();
                                longitude = mLocation.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopUsingGPS(); // 일단 시도해봄.
    }

//    public void setmContext(Context context) {
//        mContext = context;
//    }
//
//    public void setGoogleMap(GoogleMap gMap) {
//        googleMap = gMap;
//    }
//
//    public void startMap() {
//        gpsListener = new GPSListener();
//        listLatLng = new ArrayList<>();
//        mLocation = getmLocation();
//
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        close();
        return super.onUnbind(intent);
    }

    public void close() {

        if (locationManager == null) {
            return;
        }
        locationManager.removeUpdates(gpsListener);
        locationManager = null;
    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public ServiceGPS getService() {
            return ServiceGPS.this;
        }
    }


    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    private final static String TAG = ServiceGPS.class.getSimpleName();
//    public boolean initialize() {
//
//        gpsListener = new GPSListener();
//        listLatLng = new ArrayList<>();
//
//
//        try {
//            if (locationManager == null) {
//                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
//
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (locationManager == null) {
//                    Log.e(TAG, "Unable to initialize BluetoothManager.");
//                    return false;
//                }
//            }
//
//            // GPS 정보 가져오기
//            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            // 현재 네트워크 상태 값 알아오기
//            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//            if (!isGPSEnabled && !isNetworkEnabled) {
//                Log.d("## Alert ##", "GPS 와 네트워크 사용이 가능하지 않음.");
//                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
//
//                return false;
//            } else {
//                Log.d("## Alert ##", "GPS 와 네트워크 사용 가능.");
//                this.isGetLocation = true;
//
//                // 네트워크 정보로 부터 위치값 가져오기
//                if (isNetworkEnabled) {
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsListener);
//
//                    if (locationManager != null) {
//                        mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (mLocation != null) {
//                            // 위도 경도 저장
//                            latitude = mLocation.getLatitude();
//                            longitude = mLocation.getLongitude();
//                        }
//                    }
//                }
//
//                if (isGPSEnabled) {
//                    Log.d("## Alert ##", "isGPSEnabled in getmLocation()");
//                    if (mLocation == null) {
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsListener);
//                        if (locationManager != null) {
//                            mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            if (mLocation != null) {
//                                latitude = mLocation.getLatitude();
//                                longitude = mLocation.getLongitude();
//                            }
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        return true;
//    }

// getmLocation()
// getmLocation()
//    public Location getmLocation() {
//
//        try {
//
////            int gpsCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
////
////            if(gpsCheck == PackageManager.PERMISSION_DENIED) {
////                Toast.makeText(getApplicationContext(), "GPS를 동작시키세요.", Toast.LENGTH_SHORT).show();
////                return null;
////            }
////            ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
//            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
//
//            // GPS 정보 가져오기
//            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            // 현재 네트워크 상태 값 알아오기
//            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//            if (!isGPSEnabled && !isNetworkEnabled) {
//                Log.d("## Alert ##", "GPS 와 네트워크 사용이 가능하지 않음.");
//                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
//            } else {
//                Log.d("## Alert ##", "GPS 와 네트워크 사용 가능.");
//                this.isGetLocation = true;
//
//                // 네트워크 정보로 부터 위치값 가져오기
//                if (isNetworkEnabled) {
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsListener);
//
//                    if (locationManager != null) {
//                        mLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (mLocation != null) {
//                            // 위도 경도 저장
//                            latitude = mLocation.getLatitude();
//                            longitude = mLocation.getLongitude();
//                        }
//                    }
//                }
//
//                if (isGPSEnabled) {
//                    Log.d("## Alert ##", "isGPSEnabled in getmLocation()");
//                    if (mLocation == null) {
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, gpsListener);
//                        if (locationManager != null) {
//                            mLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            if (mLocation != null) {
//                                latitude = mLocation.getLatitude();
//                                longitude = mLocation.getLongitude();
//                            }
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return mLocation;
//    } //

    /**
     * GPS 종료
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            Log.d("## Alert ##", "stopUsingGPS");
            isGetLocation = false;
            locationManager.removeUpdates(gpsListener);
            distance = 0;
//            IsServiceRunning = false;
            listLatLng.clear();
        }
    }

    /**
     * 위도값을 가져옵니다.
     * */
    public double getLatitude(){
        if(mLocation != null){
            latitude = mLocation.getLatitude();
            Log.d("## Alert ##", "latitude:" + latitude);
        }
        return latitude;
    }

    /**
     * 경도값을 가져옵니다.
     * */
    public double getLongitude(){
        if(mLocation != null){
            longitude = mLocation.getLongitude();
            Log.d("## Alert ##", "longitude:" + longitude);
        }
        return longitude;
    }

    /**
     * GPS 나 wife 정보가 켜져있는지 확인합니다.
     * */
    public boolean isGetLocation() {
        return this.isGetLocation;
    }

    /** http://mainia.tistory.com/1153
     * GPS 정보를 가져오지 못했을때
     * 설정값으로 갈지 물어보는 alert 창
     * */
//    boolean isSetting = false;
//    public boolean showSettingsAlert(){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
//
//        alertDialog.setTitle("GPS 사용유무셋팅");
//        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다.\n 설정창으로 가시겠습니까?");
//        // OK 를 누르게 되면 설정창으로 이동합니다.
//        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog,int which) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                getApplicationContext().startActivity(intent);
//                isSetting = true;
//            }
//        });
//        // Cancle 하면 종료 합니다.
//        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                isSetting = false;
//                dialog.cancel();
//            }
//        });
//
//        alertDialog.show();
//
//
//        return isSetting;
//    }


    private class GPSListener implements LocationListener {


        private void broadcastUpdate(final String action) {
            final Intent intent = new Intent(action);

            sendBroadcast(intent);
        }

        @Override
        public void onLocationChanged(Location location) {

            if(ServiceGPS.IsServiceRunning) {
                Log.d("## Alert ##", "onLocationChanged");
                LocationChangeedCallCounter++;

                double tempDistance = ServiceGPS.this.mLocation.distanceTo(location);

                if(Math.abs(tempDistance - prevDistance) > 100)
                    return;

                distance += tempDistance;

                ServiceGPS.this.mLocation = location;

//                listLatLng.add(new LatLng(latitude, longitude));
//                googleMap.addPolyline(polylineOptions.add(listLatLng.get(listLatLng.size() - 1)));


//                googleMap.addPolyline(polylineOptions.add(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));

//                speed = ( prevVelocity * (LocationChangeedCallCounter-1) + (distance/ServiceTimer.getTimerCount()) ) / LocationChangeedCallCounter;
//                speed *= 3.6;
//                if( ServiceTimer.getPrevTimerCount() != 0) {
//                    speed = ((distance - prevDistance) / (ServiceTimer.getTimerCount() - ServiceTimer.getPrevTimerCount())) * 3.6;
//                } else
//                    speed = 0.0;

                if(location.hasSpeed()) {
                    speed = filter(speed, location.getSpeed() * 3.6, 3);
                    Log.d("## Velocity", location.getSpeed() + " || " + location.getSpeed() * 3.6);
                }


//                ServiceTimer.setPrevTimerCount(ServiceTimer.getTimerCount());
                prevDistance = distance;



                // 타이머와 거리 변경지점 저장
//                prevVelocity = speed;
//                speed = (distance / ServiceTimer.getTimerCount()) * 3.6;

//                String distanceAndVelocity = String.format("%.0f", distance) + "|" + String.format("%.2f", speed);
//                Log.d("distanceAndVelocity", distanceAndVelocity);
                if (mOnTextEventListener != null) {
                    mOnTextEventListener.onTextEvent(String.format("%.0f", distance), String.format("%.2f", speed));
                }
                Toast.makeText(getApplicationContext(), String.format("%.0f", distance) + " || " + String.format("%.2f", speed), Toast.LENGTH_SHORT).show();

                if(distance >= 1500) {
                    Intent sendIntent = new Intent("humaneer.org.wearablerunning.GPS");
                    getApplicationContext().sendBroadcast(sendIntent);
                    broadcastUpdate(MainFragment.ACTION_GPS_LOCATION_CHANGED);

                    // TODO: 목표 달성 시 처리 조건
                    // 1. 데이터 내장 DB에 저장하기. - SQLLite
                    // 2. ModalBox로 상중하 난이도 띄워주기
                    // 3. Data들 초기화(시간, 속도, 거리, [Location Marker, 이동경로 -> clear() 메소드] )
                }


            }
//            Intent myFilteredResponse = new Intent("org.human.runningapp.Receiver.GPS");
//            myFilteredResponse.putExtra("dataDistance", distance);
//            Log.d("### distance13 ### zz" , ""+distance);
//            mContext.sendBroadcast(myFilteredResponse);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            Log.d("## Alert ##", "onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {

            Log.d("## Alert ##", "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {

            Log.d("## Alert ##", "onProviderDisabled");
        }

        private double filter(final double prev, final double curr, final int ratio) {
            // If first time through, initialise digital filter with current values
            if (Double.isNaN(prev))
                return curr;
            // If current value is invalid, return previous filtered value
            if (Double.isNaN(curr))
                return prev;
            // Calculate new filtered value
            return (double) (curr / ratio + prev * (1.0 - 1.0 / ratio));
        }
    }
}

// https://brunch.co.kr/@babosamo/50  권한 따기