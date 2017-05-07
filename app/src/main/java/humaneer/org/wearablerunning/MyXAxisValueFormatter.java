package humaneer.org.wearablerunning;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Minki on 2017-05-05.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public MyXAxisValueFormatter() {}
    public MyXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Log.d("### MyXAxis... value:", value+"");
        return mValues[(int) value];
    }
}
