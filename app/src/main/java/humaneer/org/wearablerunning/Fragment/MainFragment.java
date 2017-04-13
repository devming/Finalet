package humaneer.org.wearablerunning.Fragment;


import android.content.IntentFilter;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import humaneer.org.wearablerunning.Activity.MainActivity;
import humaneer.org.wearablerunning.R;
import humaneer.org.wearablerunning.Service.ServiceTimer;


public class MainFragment extends Fragment {

    private final static String TAG = MainFragment.class.getSimpleName();
    private final String GPS_TAG = "humaneer.org.wearablerunning.Services.GPS";

//    private Intent gpsServiceIntent = null;

    /**
     * Widget variables
     */
    ImageView buttonRunning;
    ConstraintLayout mRelativeLayout;


    public MainFragment() {
    }

    MainActivity mMainActivity;
    public void setMainActivity(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Fragment 세팅 참고
        http://stackoverflow.com/questions/29331879/android-widget-relativelayout-cannot-be-cast-to-android-support-v7-widget-recycl
        * */
        Log.d(TAG, "### MainFragment onCreateView! ###");
        View view = inflater.inflate(R.layout.fragment_main, container, true);
        mRelativeLayout = (ConstraintLayout) view.findViewById(R.id.main_layout);

        buttonRunning = (ImageView) view.findViewById(R.id.imagebutton_running);
        buttonRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ### Start 버튼 누르면 ###
                // Logic
                // 1. 타이머 서비스 시작
                // 2. GPS 서비스 시작
                // # 추후
                // 3. 각종 애니메이션 추가

                Log.d("### Start Clicked ###", "Start Clicked" );

                startButtonClickedEventHandler();
                // Test Logic
//                PermissionListener permissionListener = new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted() {
////                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
//                        startButtonClickedEventHandler();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(ArrayList<String> arrayList) {
//
////                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
//                    }
//                };
//
//                new TedPermission(getActivity())
//                        .setPermissionListener(permissionListener)
//                        .setDeniedMessage("GPS를 동작시키세요.")
//                        .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
//                        .check();
            }
        });

/////////////////////////////////////////////////////////////////////////////////////////////////
//            ServiceTimerInstance.setOnTextEventListener(new OnTextEventListener() {
//                @Override
//                public void onTextEvent(String text) {
//                    final String data = text;
//                    try {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                textviewTime.setText(data);
//                            }
//                        });
//                    }catch (NullPointerException e) {
//                        Log.e("Go to background..", e.getMessage());
//                    }
//
//                }

//                @Override
//                public void onTextEvent(String distance, String velocity) {}
//            });
//
//            ServiceGpsInstance.setOnTextEventListener(new OnTextEventListener() {
//                @Override
//                public void onTextEvent(String text) {}
//
//                @Override
//                public void onTextEvent(String distance, String velocity) {
//                    final String d = distance;
//                    final String v = velocity;
//                    try {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                textviewDistance.setText(d + "m / 1500m");
//                                textviewAvgSpeed.setText(v + "km/h");
//                            }
//                        });
//                    }catch (NullPointerException e) {
//                        Log.e("Go to background..", e.getMessage());
//                    }
//                }
//            });
/////////////////////////////////////////////////////////////////////////////////////////////////
        return mRelativeLayout;
    }

    @Override
    public void onResume() {
        super.onResume();

//        Toast.makeText(getActivity(), ServiceGPS.IsServiceRunning+"", Toast.LENGTH_SHORT).show();
//        if(ServiceGPS.IsServiceRunning) {   // 서비스 가동중.
////            ServiceTimerInstance.setTimer(getApplicationContext());
//            textviewStartbutton.setText("Stop");
//            textviewDistance.setText(String.format("%.0f", ServiceGPS.getDistance()) + "m / 1500m");
//            textviewTime.setText(ServiceTimer.getTimeStr());
//            textviewAvgSpeed.setText(String.format("%.2f", ServiceGPS.getSpeed()) + "km/h");
//        } else {
//            textviewStartbutton.setText("Start");
//            textviewDistance.setText("0m / 1500m");
//            textviewAvgSpeed.setText("0.00km/h");
//        }
    }

    // Code to manage Service lifecycle.
//    private final ServiceConnection mGpsServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            serviceGPS = ((ServiceGPS.LocalBinder) service).getService();
//            if (!serviceGPS.initialize()) {
//                Log.e(TAG, "Unable to initialize GPS");
//                return;
//            }
//            Log.e(TAG, "Initializing GPS is successful.");
//            // Automatically connects to the device upon successful start-up initialization.
////            serviceGPS.connect(mDeviceAddress);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            serviceGPS = null;
//        }
//    };

//    private final ServiceConnection mTimerServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            serviceTimer = ((serviceTimer.LocalBinder) service).getService();
//            if (!serviceTimer.initialize()) {
//                Log.e(TAG, "Unable to initialize Timer");
//                return;
//            }
//            Log.e(TAG, "Initializing Timer is successful.");
//            // Automatically connects to the device upon successful start-up initialization.
////            serviceGPS.connect(mDeviceAddress);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            serviceTimer = null;
//        }
//    };

    public void startButtonClickedEventHandler() {
//        Toast.makeText(getActivity(), ServiceGPS.IsServiceRunning + "", Toast.LENGTH_SHORT).show();
//        Log.d("### servicerunning ###", ServiceGPS.IsServiceRunning + "" );

//        if(gpsServiceIntent == null)
//            gpsServiceIntent = new Intent(getContext(), ServiceGPS.class);

        MainActivity.setLocationRunning(false);

        if(MainActivity.isLocationRunning()) {   // 시작 중인 상태(STOP을 누를 경우)
//            ServiceGPS.IsServiceRunning = false;
            mMainActivity.removeLocationManager();
            Log.d("## FALSE", "Stop!");

            buttonRunning.setImageResource(R.drawable.btn_start);
//            getActivity().stopService(gpsServiceIntent);

//                double latitude = ServiceGpsInstance.getLatitude();
//                double longitude = ServiceGpsInstance.getLongitude();
//                LatLng myPosition = new LatLng(latitude, longitude);
//                MapFragment.GlobalGoogleMap.addMarker(new MarkerOptions().position(myPosition).title("Finish Point"));

//                    ServiceGpsInstance.stopService(gpsIntent);
//                    ServiceTimerInstance.stopService(timerIntent);

//                ServiceGpsInstance.onDestroy();
//                ServiceTimerInstance.onDestroy();
        } else {    // 아직 시작하지 않은 상태(Start를 누를 경우)
            // 1. 타이머를 돌린다. (ServiceTimer)
            // 2. 거리 측정을 시작한다. (ServiceGPS)

//                int gpsCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
//
//                if(gpsCheck == PackageManager.PERMISSION_DENIED) {
//                    Toast.makeText(getActivity(), "GPS를 동작시키세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//            if(timerIntent == null) {
////                    timerIntent = new Intent(TIMER_TAG);
//
//                timerIntent = new Intent(getContext(), ServiceTimer.class);
//            }
//
//
//                    ServiceTimerInstance.startService(timerIntent);
//                    ServiceTimerInstance.setTimer(getApplicationContext());
//
//                Thread timerThread = new Thread(ServiceTimerInstance);
//                timerThread.start();
//
//
//            if(gpsIntent == null){
////                    gpsIntent = new Intent(GPS_TAG);
//
//                gpsIntent = new Intent(getContext(), ServiceGPS.class);
//            }
//
//            Intent timerServiceIntent = new Intent(getActivity(), ServiceTimer.class);
//            getContext().bindService(timerServiceIntent, mTimerServiceConnection, BIND_AUTO_CREATE);


            // GPS 서비스 실행

            startServiceGPS();
            startServiceTimer();

            MainActivity.setLocationRunning(true);

            Log.d("## True", "Start!");
//            ServiceGPS.IsServiceRunning = true;
            buttonRunning.setImageResource(R.drawable.btn_stop);

//                    ServiceGpsInstance.startService(gpsIntent);
//                ServiceGpsInstance.setmContext(getActivity());
//                ServiceGpsInstance.setGoogleMap(MapFragment.GlobalGoogleMap);
//                ServiceGpsInstance.startMap();



//                    Intent serviceGPSIntent = new Intent(getApplicationContext(), ServiceGPS.class);
//                    ServiceGpsInstance.startService(serviceGPSIntent);  // service의 Override된 run 메소드 실행
//                    ServiceGpsInstance.bindService)
//
//                double latitude = ServiceGpsInstance.getLatitude();
//                double longitude = ServiceGpsInstance.getLongitude();

            // Add a marker in Sydney and move the camera
//                LatLng myPosition = new LatLng(latitude, longitude);

//                MapFragment.GlobalGoogleMap.setMyLocationEnabled(true);
//                MapFragment.GlobalGoogleMap.addMarker(new MarkerOptions().position(myPosition).title("Starting Point"));
//                MapFragment.GlobalGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
//                MapFragment.GlobalGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(myPosition));

        }
    }
    public void startServiceGPS() {

        mMainActivity.initLocation();

//        gpsServiceIntent.setPackage(GPS_TAG);
//        getActivity().startService(gpsServiceIntent);
//        getActivity().registerReceiver(new FinishRunning(), makeUpdateIntentFilter());
    }

    public void startServiceTimer() {

        // Tiemr 서비스 실행
        ServiceTimer serviceTimer = new ServiceTimer();
        serviceTimer.setContext(getActivity(), mMainActivity);
        serviceTimer.run();
    }



    public final static String ACTION_GPS_CONNECTED =
            "humaneer.org.wearablerunning.GPS_CONNECTED";
    public final static String ACTION_TIMER_CHANGED =
            "humaneer.org.wearablerunning.ACTION_GPS_TIMER_CHANGED";
    public final static String ACTION_GPS_LOCATION_CHANGED =
            "humaneer.org.wearablerunning.ACTION_GPS_LOCATION_CHANGED";
    public final static String ACTION_GPS_GOAL =
            "humaneer.org.wearablerunning.ACTION_GPS_GOAL";

    private static IntentFilter makeUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GPS_CONNECTED);
        intentFilter.addAction(ACTION_TIMER_CHANGED);
        intentFilter.addAction(ACTION_GPS_LOCATION_CHANGED);
        intentFilter.addAction(ACTION_GPS_GOAL);
        return intentFilter;
    }
}