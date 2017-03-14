package humaneer.org.wearablerunning.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.app.Fragment;
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

import humaneer.org.wearablerunning.DBHelper;
import humaneer.org.wearablerunning.Fragment.DataFragment;
import humaneer.org.wearablerunning.Fragment.FriendFragment;
import humaneer.org.wearablerunning.Fragment.MainFragment;
import humaneer.org.wearablerunning.R;
import humaneer.org.wearablerunning.SlidingTabLayout;
import humaneer.org.wearablerunning.BLE.BlunoLibrary;

public class MainActivity extends BlunoLibrary {

    public static DBHelper dbHelper;

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;


    Button button;
    EditText serialSendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB 생성.
        dbHelper = new DBHelper(getApplicationContext(), "HealthData.db", null, 1);

        // 서비스 생성자 생성.
//        ServiceTimerInstance = new ServiceTimer();
//        ServiceGpsInstance = new ServiceGPS();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ImageView imageViewLogo = (ImageView) findViewById(R.id.image_logo);
//        Bitmap bmp1 = BitmapFactory.decodeResource(getResources(), R.drawable.font_logo_top);
//        int width=(int)(getWindowManager().getDefaultDisplay().getWidth()); // 가로 사이즈 지정
//        int height=(int)(getWindowManager().getDefaultDisplay().getHeight() * 0.8); // 세로 사이즈 지정
//        Bitmap resizedbitmap1=Bitmap.createScaledBitmap(bmp1, width, height, true); // 이미지 사이즈 조정
//        imageViewLogo.setImageBitmap(resizedbitmap1); // 이미지뷰에 조정한 이미지 넣기
        imageViewLogo.setImageResource(R.drawable.font_logo_top);

        ImageView imageViewUser = (ImageView) findViewById(R.id.image_user);
//        Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon);
//        width=(int)(getWindowManager().getDefaultDisplay().getWidth()); // 가로 사이즈 지정
//        height=(int)(getWindowManager().getDefaultDisplay().getHeight() * 0.8); // 세로 사이즈 지정
//        Bitmap resizedbitmap2=Bitmap.createScaledBitmap(bmp2, width, height, true); // 이미지 사이즈 조정
//        imageViewUser.setImageBitmap(resizedbitmap2); // 이미지뷰에 조정한 이미지 넣기
        imageViewUser.setImageResource(R.drawable.profile_icon);

        setSupportActionBar(toolbar);


        // View Pager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        // 탭
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        // 초기 설정 페이지
        mViewPager.setCurrentItem(1);

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
                    return new FriendFragment();
                case 1:
                    return new MainFragment();
                case 2:
                    return new DataFragment();
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
            return 3;
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
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), theString, Toast.LENGTH_SHORT).show();
        button.setText(theString);
    }
}
