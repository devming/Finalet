package humaneer.org.wearablerunning.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import humaneer.org.wearablerunning.Activity.MainActivity;
import humaneer.org.wearablerunning.Fragment.MainFragment;
import humaneer.org.wearablerunning.R;
import humaneer.org.wearablerunning.Service.ServiceTimer;

public class FinishRunning extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Log.d("#### LOCATION_CHANGED" , ""+action);
        if(action.equals(MainFragment.ACTION_GPS_LOCATION_CHANGED)) {

        }
        else if(action.equals(MainFragment.ACTION_TIMER_CHANGED)) {


            int num = intent.getIntExtra("timeCount", -1);
            Toast.makeText(context, num+"", Toast.LENGTH_SHORT).show();
        }
        else if(action.equals(MainFragment.ACTION_GPS_GOAL)) {

            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentTitle("1500m 완주 성공")
                    .setContentText(ServiceTimer.getTimeStr())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_ALL);


            NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            nm.notify(777, builder.build());
        }
    }

}