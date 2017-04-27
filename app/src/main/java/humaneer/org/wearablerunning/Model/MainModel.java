package humaneer.org.wearablerunning.Model;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

import humaneer.org.wearablerunning.MyItem;
import humaneer.org.wearablerunning.R;

/**
 * Created by Minki on 2017-04-25.
 */

public class MainModel {

    LineDataSet dataSetGoal;
    LineDataSet dataSetCurrent;
    List<MyItem> items;

    public MainModel() {

        setDefaultChartData();
        setDefaultDateData();
    }

    private void setDefaultChartData() {

        float []dataObjects = new float[3];
        dataObjects[0] = 1.5f;
        dataObjects[1] = 3.3f;
        dataObjects[2] = 2.6f;


        List<Entry> entries = new ArrayList<Entry>();
        for(float data : dataObjects) {
            entries.add(new Entry(data, data));
        }

        dataSetGoal = new LineDataSet(entries, "Goal");
        dataSetGoal.setColor(R.color.colorAccent);
        dataSetGoal.setValueTextColor(R.color.colorAccent);

        dataSetCurrent = new LineDataSet(entries, "My Status");
        dataSetCurrent.setColor(R.color.colorPrimary);
        dataSetCurrent.setValueTextColor(R.color.colorPrimary);
    }

    public LineDataSet getDataSetGoal() {

        return dataSetGoal;
    }

    public LineDataSet getDataSetCurrent() {

        return dataSetCurrent;
    }

    public List<MyItem> getDefaultDateData() {
        return items;
    }

    private void setDefaultDateData() {
        items = new ArrayList<MyItem>();

        setDateList(0);

//        switch (Calendar.getInstance().DAY_OF_WEEK){
//            case Calendar.MONDAY: // MON
//                setDateList(0);
//                break;
//            case Calendar.TUESDAY: // TUE
//                setDateList(1);
//                break;
//            case Calendar.WEDNESDAY: // WED
//                setDateList(2);
//                break;
//            case Calendar.THURSDAY: // THU
//                setDateList(3);
//                break;
//            case Calendar.FRIDAY: // FRI
//                setDateList(4);
//                break;
//            case Calendar.SATURDAY: // SAT
//                setDateList(5);
//                break;
//            case Calendar.SUNDAY: // SUN
//                setDateList(6);
//                break;
//        }


//        // TODO: DB에서 날짜 데이터 불러오기
//        items = new ArrayList<MyItem>();
//        MyItem itemData = new MyItem();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-EEE", Locale.ENGLISH);
//        itemData.setDate(dateFormat.format(new Date()));
//        items.add(itemData);
//
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd-EEE", Locale.ENGLISH);
//        itemData.setDate(dateFormat.format(new Date()));
//        items.add(itemData);
//
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd-EEE", Locale.ENGLISH);
//        itemData.setDate(dateFormat.format(new Date()));
//        items.add(itemData);
//
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd-EEE", Locale.ENGLISH);
//        itemData.setDate(dateFormat.format(new Date()));
//        items.add(itemData);
//
//        dateFormat = new SimpleDateFormat("yyyy-MM-dd-EEE", Locale.ENGLISH);
//        itemData.setDate(dateFormat.format(new Date()));
//        items.add(itemData);
    }

    private void setDateList(int num) {

        MyItem itemData;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd EEE", Locale.ENGLISH);

        for(int i=3;i<10;i++) {
            int weekNum = i-num;
            if(i == 9)
                weekNum = 2-num;
            itemData = new MyItem();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, weekNum);
            itemData.setDate(dateFormat.format(calendar.getTime()));
            items.add(itemData);
        }
    }

}
