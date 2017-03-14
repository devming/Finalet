package com.example.bluecaptureled;
//*****************************************************
//file name : FIndDeviceActivity.java   date      : 2015/4/7   writer    : sunwang song
//desc      : 장치 탐색 화면
//
//
//*********************************************
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.service.BleService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**http://unikys.tistory.com/283 GPS!!!!
 * @author mtrust
 * 블루트스 장비를 탐색하고 연결을 시도 한다.
 */
public class FIndDeviceActivity extends Activity {
	private static final String[] KEYS = { "KEY_MAC_ADDRESS" };
	private static final int[] IDS = { android.R.id.text1 };
	public static final String TAG = "FIndDeviceActivity";
	private ListView mMenuList = null;
	private ArrayAdapter<String> mAdapter = null;
	private final int ENABLE_BT = 1;
	private final Messenger mMessenger;
	private Intent mServiceIntent;
	private Messenger mService = null;
	private BleService.State mState = BleService.State.UNKNOWN;
	private ProgressDialog mProgressDialog;
	Thread progressThread;
	public static FIndDeviceActivity fIndDeviceActivity = null;

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = new Messenger(service);
			try {
				Message msg = Message.obtain(null, BleService.MSG_REGISTER);
				if (msg != null) {
					msg.replyTo = mMessenger;
					mService.send(msg);
					// 장치스켄 시작
					startScan();
				} else {
					mService = null;
				}
			} catch (Exception e) {
				Log.w(TAG, "Error connecting to BleService", e);
				mService = null;
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
	};

	private static class IncomingHandler extends Handler {
		private final WeakReference<FIndDeviceActivity> mActivity;

		public IncomingHandler(FIndDeviceActivity activity) {
			mActivity = new WeakReference<FIndDeviceActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			FIndDeviceActivity activity = mActivity.get();
			// 블루투스 서비스에서 메세지를 받음
			if (activity != null) {
				switch (msg.what) {
				case BleService.MSG_STATE_CHANGED:
					activity.stateChanged(BleService.State.values()[msg.arg1]);
					break;
				case BleService.MSG_DEVICE_FOUND:
					Bundle data = msg.getData();
					if (data != null
							&& data.containsKey(BleService.KEY_MAC_ADDRESSES)) {
						activity.setDevices(activity, data
								.getStringArray(BleService.KEY_MAC_ADDRESSES));
					}
					break;
				case BleService.MSG_DEVICE_DATA:
					break;
				}
			}
			super.handleMessage(msg);
		}
	}

	public FIndDeviceActivity() {
		super();
		mMessenger = new Messenger(new IncomingHandler(this));
	}

	@Override
	protected void onStart() {
		super.onStart();
		bindService(mServiceIntent, mConnection, BIND_AUTO_CREATE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_device);

		mServiceIntent = new Intent(this, BleService.class);
		mMenuList = (ListView) findViewById(R.id.device_list);

		if (FIndDeviceActivity.fIndDeviceActivity != null) {
			FIndDeviceActivity.fIndDeviceActivity = null;
		}
		FIndDeviceActivity.fIndDeviceActivity = this;

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void openProgress() {
		mProgressDialog = new ProgressDialog(FIndDeviceActivity.this);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.show();
	}

	private void stateChanged(BleService.State newState) {
		boolean disconnected = mState == BleService.State.CONNECTED;
		mState = newState;
		switch (mState) {
		case SCANNING:
			if (mProgressDialog != null) {
				// 장치 탐색
				Log.i(TAG, "SCANNING");
				mProgressDialog.setMessage("SCANNING");
			}
			break;
		case BLUETOOTH_OFF:
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, ENABLE_BT);
			if (mProgressDialog != null) {
				mProgressDialog.setMessage("BLUETOOTH_OFF");
				mProgressDialog.dismiss();
			}
			break;
		case IDLE:
			// if (disconnected) {
			// if( mProgressDialog != null ){
			// mProgressDialog.setMessage("IDLE");
			// mProgressDialog.dismiss();
			// }
			// }
			// 탬색 실패
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
			Toast.makeText(FIndDeviceActivity.this, "장치를 찾을수 없습니다.",
					Toast.LENGTH_SHORT).show();
			finish();
			break;
		case CONNECTED:
			// 연결
			if (mProgressDialog != null) {
				mProgressDialog.setMessage("CONNECTED");
			}
			Toast.makeText(FIndDeviceActivity.this, "연결되었습니다.",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				startScan();
			} else {
				finish();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * 탐색 시작
	 * */
	private void startScan() {
		Message msg = Message.obtain(null, BleService.MSG_START_SCAN);
		if (msg != null) {
			try {
				openProgress();
				mService.send(msg);
			} catch (RemoteException e) {
				Log.w(TAG, "Lost connection to service", e);
				unbindService(mConnection);
			}
		}
	}

	/**
	 * 장비의 맥어드레스를 리스트로 보여줌
	 * */
	public void setDevices(Context context, String[] devices) {
		mProgressDialog.setMessage("setDevices");
		mProgressDialog.dismiss();
		mAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1);

		List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		if (devices != null) {
			for (String device : devices) {
				mAdapter.add(device.toString());
			}
		}
		mMenuList.setAdapter(mAdapter);
		mMenuList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onDeviceListFragmentInteraction(mAdapter.getItem(position)
						.toString());
			}
		});
	}

	/**
	 * RGB값을보내는 함수 넘겨 받은 val 값을 기기로 전송함
	 * */
	public void OnDataChangeListener(byte[] val) {
		Log.w(TAG, "-------------------- " + val.length);

		Message msg = Message.obtain(null, BleService.MSG_DEVICE_WRITE);
		if (msg != null) {
			msg.obj = val;
			try {
				mService.send(msg);
			} catch (RemoteException e) {
				Log.w(TAG, "Lost connection to service", e);
				unbindService(mConnection);
			}
		}
	}

	/**
	 * 넘겨받은 맥어드레스의 장치에 연결을 시도 함
	 * */
	public void onDeviceListFragmentInteraction(String macAddress) {
		Message msg = Message.obtain(null, BleService.MSG_DEVICE_CONNECT);
		if (msg != null) {
			msg.obj = macAddress;
			try {
				mService.send(msg);
			} catch (RemoteException e) {
				Log.w(TAG, "Lost connection to service", e);
				unbindService(mConnection);
			}
		}
	}

}
