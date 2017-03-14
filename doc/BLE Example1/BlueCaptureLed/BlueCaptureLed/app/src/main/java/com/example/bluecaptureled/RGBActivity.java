package com.example.bluecaptureled;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class RGBActivity extends ActionBarActivity {
    private static final int RED = 0;
    private static final int GREEN = 0;
    private static final int BLUE = 0;

    private SeekBar rBar = null;
    private SeekBar gBar = null;
    private SeekBar bBar = null;

    private TextView rText = null;
    private TextView gText = null;
    private TextView bText = null;

    private ColorPickerView colorPickerView;

    private Button autoButton;
    private Button offButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rgb);

        rBar = (SeekBar) findViewById(R.id.r_bar);
        gBar = (SeekBar) findViewById(R.id.g_bar);
        bBar = (SeekBar) findViewById(R.id.b_bar);

        rText = (TextView) findViewById(R.id.r_label);
        gText = (TextView) findViewById(R.id.g_label);
        bText = (TextView) findViewById(R.id.b_label);

        rText.setText("0");
        gText.setText("0");
        bText.setText("0");

        rBar.setOnSeekBarChangeListener(rChangeListener);
        gBar.setOnSeekBarChangeListener(gChangeListener);
        bBar.setOnSeekBarChangeListener(bChangeListener);

        colorPickerView = new ColorPickerView(this);
        colorPickerView.setListener(new ColorPickerView.OnColorSelectedListener() {
            @Override
            public void colorSelected(Integer color) {
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);

                rBar.setProgress(r);
                gBar.setProgress(g);
                bBar.setProgress(b);

                rText.setText(Integer.toString(r));
                gText.setText(Integer.toString(g));
                bText.setText(Integer.toString(b));

                sendRGB(r, g, b);
            }
        });
        colorPickerView.setColor(Color.rgb(0, 0, 0));
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.color_picker_framelayout);
        frameLayout.addView(colorPickerView);

        autoButton = (Button) findViewById(R.id.auto_button);
        autoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAuto();
            }
        });

        offButton = (Button) findViewById(R.id.off_button);
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rBar.setProgress(0);
                gBar.setProgress(0);
                bBar.setProgress(0);

                rText.setText("0");
                gText.setText("0");
                bText.setText("0");

                colorPickerView.setColor(Color.rgb(0, 0, 0));

                sendRGB(0, 0, 0);
            }
        });
    }

    private SeekBar.OnSeekBarChangeListener rChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            rText.setText(Integer.toString(rBar.getProgress()));

            int r = rBar.getProgress();
            int g = gBar.getProgress();
            int b = bBar.getProgress();

            colorPickerView.setColor(Color.rgb(r, g, b));

            sendRGB(r, g, b);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }
    };

    private SeekBar.OnSeekBarChangeListener gChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            gText.setText(Integer.toString(gBar.getProgress()));

            int r = rBar.getProgress();
            int g = gBar.getProgress();
            int b = bBar.getProgress();

            colorPickerView.setColor(Color.rgb(r, g, b));

            sendRGB(r, g, b);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }
    };

    private SeekBar.OnSeekBarChangeListener bChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            bText.setText(Integer.toString(bBar.getProgress()));

            int r = rBar.getProgress();
            int g = gBar.getProgress();
            int b = bBar.getProgress();

            colorPickerView.setColor(Color.rgb(r, g, b));

            sendRGB(r, g, b);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }
    };

    private void sendRGB(int r, int g, int b) {
        byte[] data = new byte[]{(byte) r, (byte) g, (byte) b, (byte) 0};

        if (FIndDeviceActivity.fIndDeviceActivity != null) {
            FIndDeviceActivity.fIndDeviceActivity.OnDataChangeListener(data);
        }
    }

    private void sendAuto() {
        byte[] data = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 255};

        if (FIndDeviceActivity.fIndDeviceActivity != null) {
            FIndDeviceActivity.fIndDeviceActivity.OnDataChangeListener(data);
        }
    }
}
