package com.rust.aboutview.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.rust.aboutview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends Activity {

    private static final String TAG = "BluetoothActivity";
    public static final int REQUEST_ENABLE_BLUETOOTH = 11;
    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Switch bluetoothSwitch;
    ListView pairedDevicesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        bluetoothSwitch = (Switch) findViewById(R.id.switch_bt);
        pairedDevicesListView = (ListView) findViewById(R.id.paired_device_list);

        bluetoothSwitch.setTextOn(getResources().getString(R.string.enable_bt));
        bluetoothSwitch.setTextOff(getResources().getString(R.string.close_bt));
        if (mBluetoothAdapter.isEnabled()) {
            bluetoothSwitch.setChecked(true);
        } else {// bluetooth is down
            bluetoothSwitch.setChecked(false);
        }

        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBluetoothAdapter.enable();
                } else {
                    mBluetoothAdapter.disable();
                }
            }
        });

        findViewById(R.id.find_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPairedDevices();
            }
        });
        findPairedDevices();

    }

    private void findPairedDevices() {
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1);
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                arrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        pairedDevicesListView.setAdapter(arrayAdapter);
    }
}
