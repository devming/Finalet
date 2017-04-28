package humaneer.org.wearablerunning.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.TimerTask;

import humaneer.org.wearablerunning.DBHelper;
import humaneer.org.wearablerunning.Fragment.DataFragment;
import humaneer.org.wearablerunning.Fragment.FriendFragment;
import humaneer.org.wearablerunning.Fragment.MainFragment;
import humaneer.org.wearablerunning.MyItem;
import humaneer.org.wearablerunning.R;
import humaneer.org.wearablerunning.Service.ServiceGPS;
import humaneer.org.wearablerunning.Service.ServiceTimer;
import humaneer.org.wearablerunning.SlidingTabLayout;
import humaneer.org.wearablerunning.BLE.BlunoLibrary;

public class MainActivity extends BlunoLibrary {

    private boolean isFirstRunning = true;

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private Button button;
    private EditText serialSendText;

    private static boolean isLocationRunning = false;   // GPS 서비스 시작했는지 안했는지.(=버튼이 눌렸는지 안눌렸는지)

    public static boolean isLocationRunning() {
        return isLocationRunning;
    }

    public static void setLocationRunning(boolean locationRunning) {
        isLocationRunning = locationRunning;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ImageView imageViewUser = (ImageView) findViewById(R.id.image_user);
        imageViewUser.setImageResource(R.drawable.profile_icon);

        setSupportActionBar(toolbar);


        // View Pager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        // 탭
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);


        onCreateProcess();														//onCreate Process by BlunoLibrary


        serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200

        serialSendText = (EditText) findViewById(R.id.serialSendText);			//initial the EditText of the sending data

        button = (Button) findViewById(R.id.btn_test_ble);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serialSend(serialSendText.getText().toString());				//send the data to the BLUNO
            }
        });
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    * SectionsPagerAdapter 클래스
    * Fragment를 담을 수 있는 Adapter
    * */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MainFragment mainFragment = new MainFragment().newInstance();
                    mainFragment.setMainActivity(MainActivity.this);
                    return mainFragment;
                case 1:
                    return new DataFragment().newInstance();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            // First : Friend Page
            // Second : Main Page.
            // Third : Data Page.
            return 2;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.ble_setting:
                buttonScanOnClickProcess();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();														//onPause Process by BlunoLibrary
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();														//onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "### onResume");
        onResumeProcess();														//onResume Process by BlunoLibrary
    }


    public static final String EXTRAS_FROM_SETTINGS_ACTIVITY =           "FROM_SETTINGS_ACTIVITY";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
//    public static final String EXTRAS_SERVICE_INTENT = "BLE_SERVICE_INTENT";

    private boolean isConnected = false;
    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {											//Four connection state
            case isConnected:

//                buttonScan.setText("Connected");
                break;
            case isConnecting:
//                buttonScan.setText("Connecting");
                break;
            case isToScan:
//                buttonScan.setText("Scan");
                break;
            case isScanning:
//                buttonScan.setText("Scanning");
                break;
            case isDisconnecting:
//                buttonScan.setText("isDisconnecting");
                break;
            default:
                break;
        }
    }

    @Override
    public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
        // Data received

        if(ServiceTimer.getTimerCount() < 3000) {
            serialSend(makeData(1500 - (int)ServiceGPS.getDistance(), MODE_MORE));
        } else {
            serialSend(makeData(0, MODE_STOP));
        }

    }

    private final int MODE_MORE = 0;
    private final int MODE_STOP = 1;
    private final int MODE_SLOWER = 2;
    private final int MODE_FASTER = 3;
    public String makeData(int data, int mode) {
        switch (mode) {
            case MODE_MORE:
                return "a" + data + "q";
            case MODE_STOP:
                return "c" + data + "q";
            case MODE_SLOWER:
                return "b" + data + "q";
            case MODE_FASTER:
                return "d" + data + "q";
            default:
                return "f";
        }

    }



//    boolean exitFlag = false;
//    @Override
//    public void onBackPressed() {
//
//        if(exitFlag) {
//            exitFlag = false;
//            super.onBackPressed();
//            finish();
//        }else {
//            TimerTask timerTask = new TimerTask() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(), "다시 한 번 뒤로가기를 누르시면 앱이 종료됩니다.", Toast.LENGTH_LONG).show();
//
//                    exitFlag = true;
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    exitFlag = false;
//                }
//            };
//            timerTask.run();
//        }
//    }

}
