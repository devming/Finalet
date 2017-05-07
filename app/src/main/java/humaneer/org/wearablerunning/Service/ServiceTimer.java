package humaneer.org.wearablerunning.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import humaneer.org.wearablerunning.Activity.MainActivity;
import humaneer.org.wearablerunning.Fragment.MainFragment;
import humaneer.org.wearablerunning.R;

public class ServiceTimer extends Service {

    public static int getTimerCount() {
        return timerCount;
    }

    public static String getPercentage() {
        return percentage;
    }

    private static String percentage;
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


    public ServiceTimer(){
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        new Thread(new Runnable() {

            @Override
            public void run() {
//                broadcastUpdate(MainFragment.ACTION_TIMER_CHANGED);
                while(MainActivity.isLocationRunning()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

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

                    if (hours == 0)
                        timeStr = minutesStr + ":" + secondsStr;
                    else
                        timeStr = hoursStr + ":" + minutesStr;

                    if (MainFragment.OnTextEventListenerObject != null) {
                        //                    handler.sendEmptyMessage(1);
                        percentage = String.format("%.2f", timerCount / 30.0);
                        if(timerCount/30.0 >=10.0) {
                            percentage = String.format("%.1f", timerCount / 30.0);
                        } else if(timerCount/30.0 == 100.0) {
                            percentage = String.format("%.0f", timerCount / 30.0);
                            doNotification();

//                            Intent notificationIntent = new Intent(mContext, MainActivity.class);
//                            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
//                            builder.setContentTitle("1500m 완주 성공")
//                                    .setContentText(ServiceTimer.getTimeStr())
//                                    .setSmallIcon(R.mipmap.ic_launcher)
//                                    .setContentIntent(contentIntent)
//                                    .setAutoCancel(true)
//                                    .setWhen(System.currentTimeMillis())
//                                    .setDefaults(Notification.DEFAULT_ALL);
//
//
//                            NotificationManager nm = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
//                            nm.notify(777, builder.build());


                        }
                        MainFragment.OnTextEventListenerObject.onTextEvent(timeStr,percentage);
                    }
                    timerCount++;
                }
            }
        }).start();
        return START_NOT_STICKY;
    }

    private void doNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ServiceTimer.this);
        builder.setContentTitle("Mission Complete! #Running 50 minutes")
                .setContentText(ServiceTimer.getTimeStr())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ServiceTimer.this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(777, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerCount = 0;
        seconds = 0;
        minutes = 0;
        hours = 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

//    private void broadcastUpdate(final String action) {
//        final Intent intent = new Intent(action);
//
//        mContext.sendBroadcast(intent);
//    }
}