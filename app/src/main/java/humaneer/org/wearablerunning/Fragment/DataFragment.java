package humaneer.org.wearablerunning.Fragment;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import humaneer.org.wearablerunning.R;

public class DataFragment extends Fragment {

    TextView textviewDistance;
    TextView textviewTime;
    TextView textviewAvgSpeed;

    double distance = 0.0;
    int time = 0;
    double speed = 0.0;


    public DataFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_data, container, false);


        textviewDistance = (TextView) view.findViewById(R.id.textview_distance);
        textviewTime = (TextView) view.findViewById(R.id.textview_time);
        textviewAvgSpeed = (TextView) view.findViewById(R.id.textview_speed);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
