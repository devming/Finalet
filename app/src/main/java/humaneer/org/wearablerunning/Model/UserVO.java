package humaneer.org.wearablerunning.Model;

/**
 * Created by Minki on 2017-03-07.
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserVO extends RealmObject {
    @PrimaryKey
    private long _id;
    private double distance;
    private int timeSeconds;
    private double speed;
    private String date;
    private double percentage;

    public static String ID = "_id";
    public static String DISTANCE = "distance";
    public static String TIMESECONDS = "timeSeconds";
    public static String SPEED = "speed";
    public static String DATE = "date";
    public static String PERCENTAGE = "percentage";

    public long get_Id() {
        return _id;
    }

    private void set_Id(long id) {
        this._id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(int timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {  // yyyy-MM-dd EEE
        this.date = date;
        String[] d = date.split("-");
        String id = d[0]+d[1]+d[2].split(" ")[0];
//        set_Id(Long.parseLong(id));
    }

    public String getDayOfWeek() {
        return date.split(" ")[1];
    }

    /**
     * 특정 날짜에 대하여 요일을 구함(일 ~ 토)
     * @param date
     * @param dateType
     * @return
     * @throws Exception
     */
    public String getDateString(String date, String dateType) throws Exception {

        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "MON";
                break;
            case 2:
                day = "TUE";
                break;
            case 3:
                day = "WED";
                break;
            case 4:
                day = "THU";
                break;
            case 5:
                day = "FRI";
                break;
            case 6:
                day = "SAT";
                break;
            case 7:
                day = "SUN";
                break;
        }

        return day;
    }

    public double getPercentage() { return percentage; }

    public void setPercentage(double percentage) { this.percentage = percentage; }
}