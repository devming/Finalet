package humaneer.org.wearablerunning.Activity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import humaneer.org.wearablerunning.BLE.BluetoothLeService;
import humaneer.org.wearablerunning.BLE.SampleGattAttributes;
import humaneer.org.wearablerunning.DBHelper;
import humaneer.org.wearablerunning.Fragment.DataFragment;
import humaneer.org.wearablerunning.Fragment.FriendFragment;
import humaneer.org.wearablerunning.Fragment.MainFragment;
import humaneer.org.wearablerunning.R;
import humaneer.org.wearablerunning.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    public static DBHelper dbHelper;

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;


    Button button;

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


        button = (Button) findViewById(R.id.btn_test_ble);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mGattCharacteristics != null) {
                    final BluetoothGattCharacteristic characteristic =
                            mGattCharacteristics.get(3).get(0);
                    final int charaProp = characteristic.getProperties();
                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                        // If there is an active notification on a characteristic, clear
                        // it first so it doesn't update the data field on the user interface.
                        if (mNotifyCharacteristic != null) {
                            Log.d("### Main", "mNotifyCharacteristic");
                            mBluetoothLeService.setCharacteristicNotification(
                                    mNotifyCharacteristic, false);
                            mNotifyCharacteristic = null;
                        }
                        String data = "a1000";
                        characteristic.setValue(data);  // data setting! - commented by devming

                        mBluetoothLeService.readCharacteristic(characteristic);
                    }
                    if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                        Log.d("### Main", "characteristic");
                        mNotifyCharacteristic = characteristic;
                        mBluetoothLeService.setCharacteristicNotification(characteristic, true);
                    }
                }
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
                if(isConnected) {
//                    Intent intent = new Intent(this, DeviceControlActivity.class);
//                    intent.putExtra(MainActivity.EXTRAS_DEVICE_NAME, mDeviceName);
//                    intent.putExtra(MainActivity.EXTRAS_DEVICE_ADDRESS, mDeviceAddress);
//                    startActivity(intent);
                    isConnected = false;//TODO: Disconnected 호출 안됨 체크할것.
                    unregisterReceiver(mGattUpdateReceiver);
                    unbindService(mServiceConnection);
                    Toast.makeText(getApplicationContext(), "Bluetooth is disconnected", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, SettingsActivity.class);
                    startActivity(intent);
                }
                return true;
            default:
                return false;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//                                      BLE
//
////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "### onResume");

        final Intent intent = getIntent();

        if(intent.getBooleanExtra(EXTRAS_FROM_SETTINGS_ACTIVITY, false)) {   // SettingsActivity에서 아이템 클릭해서 넘어 온경우
            Log.d(TAG, "### getIntent in onResume.");
            mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
            mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
            Log.d(TAG, "mDeviceName=" + mDeviceName);
            Log.d(TAG, "mDeviceAddress=" + mDeviceAddress);

            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            if (mBluetoothLeService != null) {
                final boolean result = mBluetoothLeService.connect(mDeviceAddress);
                Log.d(TAG, "Connect request result=" + result);
            }
        }
    }


    public static final String EXTRAS_FROM_SETTINGS_ACTIVITY =           "FROM_SETTINGS_ACTIVITY";
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
//    public static final String EXTRAS_SERVICE_INTENT = "BLE_SERVICE_INTENT";

    private String mDeviceName;
    private String mDeviceAddress;

    private boolean isConnected = false;

    private BluetoothLeService mBluetoothLeService;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();


    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                isConnected = true;
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                Log.d("### receiver conn ###", "Connected");
//                updateConnectionState(R.string.connected);
//                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                isConnected = false;
                Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
                Log.d("### receiver dis ###", "Disconnected");
//                updateConnectionState(R.string.disconnected);
//                invalidateOptionsMenu();
//                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                Log.d("### discover ###", "discover");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                String receivedData = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);     // receivedData : 받은 데이터   - commented by devming

                Toast.makeText(getApplicationContext(), receivedData, Toast.LENGTH_SHORT).show();
                Log.d("### ReceivedData ###", receivedData);

            }
        }
    };


    private final static String TAG = MainActivity.class.getSimpleName();
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            Log.e(TAG, "Initializing Bluetooth is successful.");
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
