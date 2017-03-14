package com.example.bluecaptureled;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

    private ListView mMenuList = null;

    private ArrayAdapter<String> mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMenuList = (ListView) findViewById(R.id.menu_list);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mAdapter.add("Find Device");
        mAdapter.add("LED");

        mMenuList.setAdapter(mAdapter);
        mMenuList.setOnItemClickListener(itemClick);
    }

    private OnItemClickListener itemClick = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent _i = null;
            switch (position) {
                case 0:
                    _i = new Intent(MainActivity.this, FIndDeviceActivity.class);
                    startActivity(_i);
                    break;
                case 1:
                    _i = new Intent(MainActivity.this, RGBActivity.class);
                    startActivity(_i);
                    break;

                default:
                    break;
            }
        }

    };
}
