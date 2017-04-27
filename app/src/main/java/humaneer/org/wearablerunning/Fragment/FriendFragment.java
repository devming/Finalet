package humaneer.org.wearablerunning.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import humaneer.org.wearablerunning.R;


public class FriendFragment extends Fragment {

    Button buttonFindBLE;

    ArrayList bleList;
    public FriendFragment() {
        // Required empty public constructor
        bleList = new ArrayList();
        bleList.add("a");
        bleList.add("b");
        bleList.add("c");
        bleList.add("d");
    }

    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_friend, container, false);

        // Listview 세팅
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, bleList);
//
//        ListView listView = (ListView) view.findViewById(R.id.listview);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), view.getId()+"", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // 버튼
//        buttonFindBLE = (Button) view.findViewById(R.id.btn_find_ble);
//        buttonFindBLE.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return view;
    }

}
