package humaneer.org.wearablerunning.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import humaneer.org.wearablerunning.Activity.MainActivity;
import humaneer.org.wearablerunning.Fragment.MainFragment;
import humaneer.org.wearablerunning.OnTextEventListener;

public class ServiceTimer extends Service {

    public static int getTimerCount() {
        return timerCount;
    }

    private static int timerCount = 0;

    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private String secondsStr = "";
    private String minutesStr = "";
    private String hoursStr = "";

    public static String getTimeStr() {
        return timeStr;
    }
    private static String timeStr = "";

    private OnTextEventListener mOnTextEventListener;
    public ServiceTimer(){
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }


    public void setOnTextEventListener(OnTextEventListener listener) {
        mOnTextEventListener = listener;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        mTimer = new Timer();
        Log.d("## ServiceTimer", "onStartCommand");

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(MainActivity.isLocationRunning()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d("### timer3 ### zz", "" + timerCount);

                    seconds = timerCount % 60;
                    minutes = (timerCount / 60) % 3600;
                    hours = timerCount / 3600;

                    secondsStr = seconds + "";
                    minutesStr = minutes + "";
                    hoursStr = hours + "";

                    if (seconds < 10)
                        secondsStr = "0" + seconds;
                    if (minutes < 10)
                        minutesStr = "0" + minutes;
                    if (hours < 10)
                        hoursStr = "0" + hours;

                    timeStr = hoursStr + ":" + minutesStr + ":" + secondsStr;

//            Log.d("ServiceTimer", mOnTextEventListener.toString());

                    if (mOnTextEventListener != null) {
                        mOnTextEventListener.onTextEvent(timeStr, seconds / 30.0 + "");
                    }
//            Toast.makeText(mContext,timeStr, Toast.LENGTH_SHORT).show();
                    if(timerCount % 4 == 0)
                        broadcastUpdate(MainFragment.ACTION_TIMER_CHANGED);

                    timerCount++;
                }
            }
        }).run();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mTimer.cancel();
        timerCount = 0;
//        mTimer = null;
    }


//    public void setTimer(Context context) {
//
//        mContext = context;
//        mTimer = new Timer();
//        mTimer.schedule(new TimerTask(){
//            public void run() {
//                Log.d("### timer3 ### zz" , ""+timerCount);
//
//                seconds = timerCount % 60;
//                minutes = timerCount / 60;
//                hours = timerCount / 3600;
//
//                secondsStr = seconds+"";
//                minutesStr = minutes+"";
//                hoursStr = hours+"";
//
//                if (seconds < 10)
//                    secondsStr = "0" + seconds;
//                if (minutes < 10)
//                    minutesStr = "0" + minutes;
//                if (hours < 10)
//                    hoursStr = "0" + hours;
//
//                timeStr = hoursStr + ":" + minutesStr + ":" + secondsStr;
//
//                Log.d("ServiceTimer", mOnTextEventListener.toString());
//
//                if (mOnTextEventListener != null) {
//                    mOnTextEventListener.onTextEvent(timeStr);
//                }
//                timerCount++;
//
//
//            }
//        }, 0, 1000);
//    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);

        getApplicationContext().sendBroadcast(intent);
    }
}