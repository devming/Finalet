package humaneer.org.wearablerunning;

/**
 * Created by Minki on 2017-03-07.
 */
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyItem {
    private int _id;
    private double distance;
    private int timeSeconds;
    private double speed;
    private String date;
    private int percentage;

    public int get_Id() {
        return _id;
    }

    public void set_Id(int id) {
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
    public String getDateDay(String date, String dateType) throws Exception {

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

    public int getPercentage() { return percentage; }

    public void setPercentage(int percentage) { this.percentage = percentage; }
}