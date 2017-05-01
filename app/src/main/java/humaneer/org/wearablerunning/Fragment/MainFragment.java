package humaneer.org.wearablerunning.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import humaneer.org.wearablerunning.Activity.MainActivity;
import humaneer.org.wearablerunning.OnTextEventListener;
import humaneer.org.wearablerunning.R;
import humaneer.org.wearablerunning.Service.ServiceGPS;
import humaneer.org.wearablerunning.Service.ServiceTimer;


public class MainFragment extends Fragment {

    private final static String TAG = MainFragment.class.getSimpleName();
    private final String GPS_TAG = "humaneer.org.wearablerunning.Services.GPS";

    public static OnTextEventListener OnTextEventListenerObject;
//    private Intent gpsServiceIntent = null;


    /**
     * Widget variables
     */
    ImageView buttonRunning;
    ConstraintLayout mRelativeLayout;
    TextView textViewAim;
    TextView textViewTime;
    TextView textViewSpeed;

    boolean isGranted = false;

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
    static MainFragment fragment;
    public static MainFragment newInstance() {
        fragment = new MainFragment();
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

        textViewAim = (TextView) view.findViewById(R.id.textview_aim);
        textViewTime = (TextView) view.findViewById(R.id.textview_time);
        textViewSpeed = (TextView) view.findViewById(R.id.textView_speed);

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


                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        isGranted = true;
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> arrayList) {
                        isGranted = false;
                    }
                };

                new TedPermission(getActivity())
                        .setPermissionListener(permissionListener)
                        .setDeniedMessage("GPS를 동작시키세요.")
                        .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                        .check();

//                if(isGranted) // 이거 넣으면 위에 check()하는데 시간이 걸려서 isGranted가 true로 되기전에 요기 문장이 수행되어서 if문이 false인 상태로 지나침..
//                              // 그래서 일단 모든 사용자가 허용을 누른다고 생각하고 그냥 주석막아놓음! - devming
                    startButtonClickedEventHandler();

            }
        });

        setOnTextEventListener(new OnTextEventListener() {
            @Override
            public void onTextEvent(String s) {
                final String speed = s;
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            textViewSpeed.setText(speed);
                        }
                    });
                }catch (NullPointerException e) {
                    Log.e("Go to background..", e.getMessage());
                }
            }

            @Override
            public void onTextEvent(String t, String p) {
                final String time = t;
                final String percentage = p;
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        textViewTime.setText(time);
                        textViewAim.setText(percentage);
                        }
                    });
                }catch (NullPointerException e) {
                    Log.e("Go to background..", e.getMessage());
                }
            }
        });

        return mRelativeLayout;
    }

    public void setOnTextEventListener(OnTextEventListener listener) {
        OnTextEventListenerObject = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.isLocationRunning()) {
            buttonRunning.setImageResource(R.drawable.btn_stop);
        } else {
            buttonRunning.setImageResource(R.drawable.btn_start);
        }

    }

    public void startButtonClickedEventHandler() {

        Animation anim = AnimationUtils.loadAnimation(mMainActivity, R.anim.toalpha1);

        if(MainActivity.isLocationRunning()) {   // 시작 중인 상태(STOP을 누를 경우)

            buttonRunning.setImageResource(R.drawable.btn_start);
            MainActivity.setLocationRunning(false);
            stopServiceGPS();
            stopServiceTimer();
        } else {    // 아직 시작하지 않은 상태(Start를 누를 경우)
            // GPS 서비스 실행

            buttonRunning.setImageResource(R.drawable.btn_stop);
            MainActivity.setLocationRunning(true);
            startServiceGPS();
            startServiceTimer();
        }
        // button 애니메이션 실행
        buttonRunning.startAnimation(anim);
    }

//    FinishRunning finishRunning = new FinishRunning();
    public void startServiceGPS() {

        Intent intent = new Intent(fragment.getContext(), ServiceGPS.class);

        getActivity().startService(intent);
//        getActivity().registerReceiver(finishRunning, makeUpdateIntentFilter());
    }

    public void stopServiceGPS() {

        Intent intent = new Intent(fragment.getContext(), ServiceGPS.class);
        getActivity().stopService(intent);
//        getActivity().unregisterReceiver(finishRunning);
    }

//    ServiceTimer serviceTimer;

    public void startServiceTimer() {

        // Tiemr 서비스 실행
//        serviceTimer = new ServiceTimer();
//        serviceTimer.setContext(getContext());

        Intent intent = new Intent(fragment.getContext(), ServiceTimer.class);
        getActivity().startService(intent);
    }

    public void stopServiceTimer() {

        Intent intent = new Intent(fragment.getContext(), ServiceTimer.class);
        getActivity().stopService(intent);
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