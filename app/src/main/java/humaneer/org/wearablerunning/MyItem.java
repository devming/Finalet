package humaneer.org.wearablerunning;

/**
 * Created by Minki on 2017-03-07.
 */
import java.sql.Date;

public class MyItem {
    private int _id;
    private double distance;
    private int timeSeconds;
    private double speed;
    private Date date;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}