package humaneer.org.wearablerunning.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import humaneer.org.wearablerunning.BLE.BlunoLibrary;
import humaneer.org.wearablerunning.CustomPreferenceManager;
import humaneer.org.wearablerunning.Fragment.DataFragment;
import humaneer.org.wearablerunning.Fragment.MainFragment;
import humaneer.org.wearablerunning.Model.UserVO;
import humaneer.org.wearablerunning.R;
import humaneer.org.wearablerunning.Service.ServiceGPS;
import humaneer.org.wearablerunning.Service.ServiceTimer;
import humaneer.org.wearablerunning.SlidingTabLayout;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends BlunoLibrary {

    private EditText serialSendText;

    private static boolean isLocationRunning = false;   // GPS 서비스 시작했는지 안했는지.(=버튼이 눌렸는지 안눌렸는지)
    public static boolean isLocationRunning() {
        return isLocationRunning;
    }
    public static void setLocationRunning(boolean locationRunning) { isLocationRunning = locationRunning; }

    private static Realm mRealm;
    public static Realm GetRealmObject() {return mRealm;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ImageView imageViewUser = (ImageView) findViewById(R.id.image_user);
        imageViewUser.setImageResource(R.drawable.profile_icon);

        setSupportActionBar(toolbar);

        if(CustomPreferenceManager.isAlreadyRun(this))  // 첫 실행 판단 - true이면 이전에 한번 수행한 적 있는것임.
            realmInit();
        else {

        }


        // View Pager
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        // 탭
        SlidingTabLayout mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);


        onCreateProcess();														//onCreate Process by BlunoLibrary


        serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200

        serialSendText = (EditText) findViewById(R.id.serialSendText);			//initial the EditText of the sending data

        Button button = (Button) findViewById(R.id.btn_test_ble);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serialSend(serialSendText.getText().toString());				//send the data to the BLUNO
            }
        });
    }

    public void realmInit() {

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("finalet.realm")
                .schemaVersion(42)
                .build();
        mRealm = Realm.getInstance(config);

        // TODO: 앱 실행시 User Info 초기화
        // 1. Today 확인
        long id = getTodayId();
        if(id == 0 || id != getToday()) {   // id값이 없으면.(0일경우는 아예 데이터 자체가 없을 때, id != getToday()는 데이터는 있지만 현재 날짜가 없을 때)
            // 2. default value 0으로 - insert
            mRealm.beginTransaction();
            UserVO user = mRealm.createObject(UserVO.class, getDate(new SimpleDateFormat("yyyy-MM-dd EEE", Locale.ENGLISH).format(Calendar.getInstance().getTime())));

            user.setDate(new SimpleDateFormat("yyyy-MM-dd EEE", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
            user.setDistance(0);
            user.setPercentage(0);
            user.setSpeed(0);
            user.setTimeSeconds(0);

            mRealm.commitTransaction();
        } else {
            // 2. default value를 기준에 사용했던 데이터 불러와서 저장 - update
            mRealm.beginTransaction();
            UserVO user = mRealm.where(UserVO.class)
                    .equalTo("_id", getDate(new SimpleDateFormat("yyyy-MM-dd EEE", Locale.ENGLISH).format(Calendar.getInstance().getTime())))
                    .findFirst();

            Log.d("### user id", user.get_Id() + "");
            Log.d("### user distance", user.getDistance() + "");
            Log.d("### user date", user.getDate() + "");
            Log.d("### user percentage", user.getPercentage() + "");
            mRealm.commitTransaction();
        }

        Log.d("### Test DB", ""+ mRealm.where(UserVO.class).findAll());
    }

    public long getDate(String date) {  // yyyy-MM-dd EEE
        String[] d = date.split("-");
        String id = d[0]+d[1]+d[2].split(" ")[0];
        return Long.parseLong(id);
    }

    private long getTodayId() {
        Number id = mRealm.where(UserVO.class).max("_id");
        if(id == null)
            return 0;
        else
            return id.longValue();
    }

    private long getToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        return Long.parseLong(dateFormat.format(calendar.getTime()));
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
            // Show 2 total pages.

            // First : Main Page.
            // Second : Data Page.
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
