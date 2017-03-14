package humaneer.org.wearablerunning.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import humaneer.org.wearablerunning.Fragment.MainFragment;
import humaneer.org.wearablerunning.OnTextEventListener;

public class ServiceTimer extends Service implements Runnable {

    Context mContext;
    // 타이머
//    Timer mTimer;
//    TimerTask task;

    public static int getTimerCount() {
        return timerCount;
    }

    private static int timerCount = 0;

    public static int getPrevTimerCount() {
        return prevTimerCount;
    }

    public static void setPrevTimerCount(int prevTimerCount) {
        ServiceTimer.prevTimerCount = prevTimerCount;
    }

    private static int prevTimerCount = 0;
    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;
    private String secondsStr = "";
    private String minutesStr = "";
    private String hoursStr = "";

    public static String getTimeStr() {
        return timeStr;
    }

    public static void setTimeStr(String timeStr) {
        ServiceTimer.timeStr = timeStr;
    }

    private static String timeStr = "";


    private OnTextEventListener mOnTextEventListener;
    public ServiceTimer(){
        Log.d("## ServiceTimer", "ServiceTimer Constructor");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("## ServiceTimer", "onCreate");
    }

    public void setOnTextEventListener(OnTextEventListener listener) {
        mOnTextEventListener = listener;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        mTimer = new Timer();
        Log.d("## ServiceTimer", "onStartCommand");

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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void run() {

        while(ServiceGPS.IsServiceRunning) {
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
                mOnTextEventListener.onTextEvent(timeStr);
            }
//            Toast.makeText(mContext,timeStr, Toast.LENGTH_SHORT).show();
            if(timerCount % 4 == 0)
                broadcastUpdate(MainFragment.ACTION_TIMER_CHANGED);

            timerCount++;
        }
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);

        mContext.sendBroadcast(intent);
    }
}