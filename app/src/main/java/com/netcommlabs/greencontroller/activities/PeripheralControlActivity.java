package com.netcommlabs.greencontroller.activities;

/**
 * Created by blitz on 11/07/17.
 */

import android.app.Activity;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.netcommlabs.greencontroller.Constants;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.services.BleAdapterService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Timer;

public class PeripheralControlActivity extends Activity {
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_ID = "id";

    private BleAdapterService bluetooth_le_adapter;

    private String device_name;
    private String device_address;
    private Timer mTimer;
    private boolean sound_alarm_on_disconnect = false;
    private int alert_level;
    private boolean back_requested = false;
    private boolean share_with_server = false;
    private Switch share_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peripheral_control);

        //read intent data
        final Intent intent = getIntent();
        device_name = intent.getStringExtra(EXTRA_NAME);
        device_address = intent.getStringExtra(EXTRA_ID);

        //show the device name
        ((TextView) this.findViewById(R.id.nameTextView)).setText("Device : " + device_name + "[" + device_address + "]"); //hid the coloured rectangle used to show green/amber/red rssi distance
        ((LinearLayout) this.findViewById(R.id.rectangle)).setVisibility(View.INVISIBLE);

        //disable the noise button
        ((Button) PeripheralControlActivity.this.findViewById(R.id.noiseButton)).setEnabled(false);

        //disable the LOW/MID/HIGH alert level selection buttons
        ((Button) this.findViewById(R.id.lowButton)).setEnabled(false);
        ((Button) this.findViewById(R.id.midButton)).setEnabled(false);
        ((Button) this.findViewById(R.id.highButton)).setEnabled(false);

        share_switch = (Switch) this.findViewById(R.id.switch1);
        share_switch.setEnabled(false);
        share_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // we'll complete this later
            }
        });

        // connect to the Bluetooth adapter service
        Intent gattServiceIntent = new Intent(this, BleAdapterService.class);
        bindService(gattServiceIntent, service_connection, BIND_AUTO_CREATE);
        showMsg("READY");
    }

    private void showMsg(final String msg) {
        Log.d(Constants.TAG, msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.msgTextView)).setText(msg);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(service_connection);
        bluetooth_le_adapter = null;
    }

    private final ServiceConnection service_connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bluetooth_le_adapter = ((BleAdapterService.LocalBinder) service).getService();
            bluetooth_le_adapter.setActivityHandler(message_handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bluetooth_le_adapter = null;
        }
    };

    private Handler message_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle;
            String service_uuid = "";
            String characteristic_uuid = "";
            byte[] b = null;
            //message handling logic
            switch (msg.what) {
                case BleAdapterService.MESSAGE:
                    bundle = msg.getData();
                    String text = bundle.getString(BleAdapterService.PARCEL_TEXT);
                    showMsg(text);
                    break;

                case BleAdapterService.GATT_CONNECTED:
                    ((Button) PeripheralControlActivity.this.findViewById(R.id.connectButton)).setEnabled(false);
                    //we're connected
                    showMsg("CONNECTED");
                    // enable the LOW/MID/HIGH alert level selection buttons
                    ((Button) PeripheralControlActivity.this.findViewById(R.id.lowButton)).setEnabled(true);
                    ((Button) PeripheralControlActivity.this.findViewById(R.id.midButton)).setEnabled(true);
                    ((Button) PeripheralControlActivity.this.findViewById(R.id.highButton)).setEnabled(true);
                    bluetooth_le_adapter.discoverServices();
                    break;

                case BleAdapterService.GATT_DISCONNECT:
                    ((Button) PeripheralControlActivity.this.findViewById(R.id.connectButton)).setEnabled(true);
                    //we're disconnected
                    showMsg("DISCONNECTED");
                    // hide the rssi distance colored rectangle
                    ((LinearLayout) PeripheralControlActivity.this.findViewById(R.id.rectangle)).setVisibility(View.INVISIBLE);
                    // disable the LOW/MID/HIGH alert level selection buttons
                    ((Button) PeripheralControlActivity.this.findViewById(R.id.lowButton)).setEnabled(false);
                    ((Button) PeripheralControlActivity.this.findViewById(R.id.midButton)).setEnabled(false);
                    ((Button) PeripheralControlActivity.this.findViewById(R.id.highButton)).setEnabled(false);
                    if (back_requested) {
                        PeripheralControlActivity.this.finish();
                    }
                    break;

                case BleAdapterService.GATT_SERVICES_DISCOVERED:
                    //validate services and if ok...
                    List<BluetoothGattService> slist = bluetooth_le_adapter.getSupportedGattServices();
                    boolean link_loss_present = false;
                    boolean immediate_alert_present = false;
                    boolean tx_power_present = false;
                    boolean proximity_monitoring_present = false;

                    for (BluetoothGattService svc : slist) {
                        Log.d(Constants.TAG, "UUID=" + svc.getUuid().toString().toUpperCase() + "INSTANCE=" + svc.getInstanceId());
                        String serviceUuid = svc.getUuid().toString().toUpperCase();
                        if (svc.getUuid().toString().equalsIgnoreCase(BleAdapterService.LINK_LOSS_SERVICE_UUID)) {
                            link_loss_present = true;
                            continue;
                        }
                        if (svc.getUuid().toString().equalsIgnoreCase(BleAdapterService.IMMEDIATE_ALERT_SERVICE_UUID)) {
                            immediate_alert_present = true;
                            continue;
                        }
                        if (svc.getUuid().toString().equalsIgnoreCase(BleAdapterService.TX_POWER_SERVICE_UUID)) {
                            tx_power_present = true;
                            continue;
                        }
                    }

                    if (link_loss_present && immediate_alert_present && tx_power_present) {
                        showMsg("Device has expected services");


                        //enable the LOW/MID/HIGH alert level selection buttons
                        ((Button) PeripheralControlActivity.this.findViewById(R.id.lowButton)).setEnabled(true);
                        ((Button) PeripheralControlActivity.this.findViewById(R.id.midButton)).setEnabled(true);
                        ((Button) PeripheralControlActivity.this.findViewById(R.id.highButton)).setEnabled(true);

                        bluetooth_le_adapter.readCharacteristic(BleAdapterService.LINK_LOSS_SERVICE_UUID, BleAdapterService.ALERT_LEVEL_CHARACTERISTIC);
                    } else {
                        showMsg("Device does not have expected GATT services");
                    }
                    break;

                case BleAdapterService.GATT_CHARACTERISTIC_READ:
                    bundle = msg.getData();
                    Log.d(Constants.TAG, "Service=" + bundle.get(BleAdapterService.PARCEL_SERVICE_UUID).toString().toUpperCase() + " Characteristic=" + bundle.get(BleAdapterService.PARCEL_CHARACTERISTIC_UUID).toString().toUpperCase());
                    if (bundle.get(BleAdapterService.PARCEL_CHARACTERISTIC_UUID).toString()
                            .toUpperCase().equals(BleAdapterService.ALERT_LEVEL_CHARACTERISTIC)
                            && bundle.get(BleAdapterService.PARCEL_SERVICE_UUID).toString().toUpperCase().equals(BleAdapterService.BATTERY_LEVEL_CHARACTERISTIC_UUID)) {
                        b = bundle.getByteArray(BleAdapterService.PARCEL_VALUE);
                        if (b.length > 0) {
                            PeripheralControlActivity.this.setAlertLevel((int) b[0]);
                            showMsg("Received " + b.toString() + "from Pebble.");
                        }
                    }

                    break;

                case BleAdapterService.GATT_CHARACTERISTIC_WRITTEN:
                    bundle = msg.getData();
                    if (bundle.get(BleAdapterService.PARCEL_CHARACTERISTIC_UUID).toString()
                            .toUpperCase().equals(BleAdapterService.ALERT_LEVEL_CHARACTERISTIC)
                            && bundle.get(BleAdapterService.PARCEL_SERVICE_UUID).toString().toUpperCase().equals(BleAdapterService.LINK_LOSS_SERVICE_UUID)) {
                        b = bundle.getByteArray(BleAdapterService.PARCEL_VALUE);
                        if (b.length > 0) {
                            PeripheralControlActivity.this.setAlertLevel((int) b[0]);
                        }
                    }
                    break;
            }
        }
    };

    public void onLow(View view) {
        bluetooth_le_adapter.writeCharacteristic(
                BleAdapterService.LINK_LOSS_SERVICE_UUID,
                BleAdapterService.ALERT_LEVEL_CHARACTERISTIC, Constants.ALERT_LEVEL_LOW
        );
    }

    public void onMid(View view) {
        bluetooth_le_adapter.writeCharacteristic(
                BleAdapterService.LINK_LOSS_SERVICE_UUID,
                BleAdapterService.ALERT_LEVEL_CHARACTERISTIC, Constants.ALERT_LEVEL_MID
        );
    }

    public void onHigh(View view) {
        bluetooth_le_adapter.writeCharacteristic(
                BleAdapterService.LINK_LOSS_SERVICE_UUID,
                BleAdapterService.ALERT_LEVEL_CHARACTERISTIC, Constants.ALERT_LEVEL_HIGH
        );
    }

    public void onSetTime(View view) {
        String[] ids = TimeZone.getAvailableIDs(+5 * 60 * 60 * 1000);
        SimpleTimeZone pdt = new SimpleTimeZone(+5 * 60 * 60 * 1000, ids[0]);

        Calendar calendar = new GregorianCalendar(pdt);
        Date trialTime = new Date();
        calendar.setTime(trialTime);

        /*byte hours = (byte) calendar.get(Calendar.HOUR);
        byte minutes = (byte) calendar.get(Calendar.MINUTE);
        byte seconds = (byte) calendar.get(Calendar.SECOND);
        byte DATE = (byte) calendar.get(Calendar.DAY_OF_MONTH);
        byte MONTH = (byte) (calendar.get(Calendar.MONTH)+1);
        int iYEARMSB = (calendar.get(Calendar.YEAR) / 256);
        int iYEARLSB = (calendar.get(Calendar.YEAR) % 256);
        //byte bYEARMSB = (byte) iYEARMSB;
        //byte bYEARLSB = (byte) iYEARLSB;
        byte bYEARMSB = (byte) iYEARMSB;
        byte bYEARLSB = (byte) iYEARLSB;*/

        byte hours = (byte) 1;
        byte minutes = (byte) 2;
        byte seconds = (byte) 3;
        byte DATE = (byte) 4;
        byte MONTH = (byte) 5;
        //int iYEARMSB = (calendar.get(Calendar.YEAR) / 256);
        //int iYEARLSB = (calendar.get(Calendar.YEAR) % 256);
        //byte bYEARMSB = (byte) iYEARMSB;
        //byte bYEARLSB = (byte) iYEARLSB;
        byte bYEARMSB = (byte) 6;
        byte bYEARLSB = (byte) 7;

        byte[] currentTime = {hours, minutes, seconds, DATE, MONTH, bYEARMSB, bYEARLSB};

        bluetooth_le_adapter.writeCharacteristic(
                BleAdapterService.CURRENT_TIME_SERVICE_SERVICE_UUID,
                BleAdapterService.CURRENT_TIME_CHARACTERISTIC_UUID, currentTime
        );
    }

    public void onSetPots(View view) {
        byte numberOfPots = (byte) 5;
        byte[] pots = {numberOfPots};
        bluetooth_le_adapter.writeCharacteristic(
                BleAdapterService.POTS_SERVICE_SERVICE_UUID,
                BleAdapterService.POTS_CHARACTERISTIC_UUID, pots
        );
    }

    public void onSendTP(View view) {
        byte hours = (byte) 1;
        byte minutes = (byte) 2;
        byte seconds = (byte) 3;
        byte dayOfTheWeek = (byte) 4;
        byte durationMsb = (byte) 5;
        byte durationLsb = (byte) 6;
        byte volumeMsb = (byte) 7;
        byte volumeLsb = (byte) 8;

        byte[] timePoint = {hours, minutes, seconds, dayOfTheWeek, durationMsb, durationLsb, volumeMsb, volumeLsb};
        bluetooth_le_adapter.writeCharacteristic(
                BleAdapterService.TIME_POINT_SERVICE_SERVICE_UUID,
                BleAdapterService.NEW_WATERING_TIME_POINT_CHARACTERISTIC_UUID, timePoint
        );
    }

    public void onBattery(View view) {


        bluetooth_le_adapter.readCharacteristic(
                BleAdapterService.BATTERY_SERVICE_SERVICE_UUID,
                BleAdapterService.BATTERY_LEVEL_CHARACTERISTIC_UUID
        );
    }

    public void onNoise(View view) {

    }

    public void onConnect(View view) {
        showMsg("onConnect");
        if (bluetooth_le_adapter != null) {
            if (bluetooth_le_adapter.connect(device_address)) {
                ((Button) PeripheralControlActivity.this
                        .findViewById(R.id.connectButton)).setEnabled(false);
            } else {
                showMsg("onConnect: failed to connect");
            }
        } else {
            showMsg("onConnect: bluetooth_le_adapter=null");
        }
    }

    public void onBackPressed() {
        Log.d(Constants.TAG, "onBackPressed");
        back_requested = true;
        if (bluetooth_le_adapter.isConnected()) {
            try {
                bluetooth_le_adapter.disconnect();
            } catch (Exception e) {

            }
        } else {
            finish();
        }
    }

    private void setAlertLevel(int alert_level) {
        this.alert_level = alert_level;
        ((Button) this.findViewById(R.id.lowButton)).setTextColor(Color.parseColor("#000000"));
        ;
        ((Button) this.findViewById(R.id.midButton)).setTextColor(Color.parseColor("#000000"));
        ;
        ((Button) this.findViewById(R.id.highButton)).setTextColor(Color.parseColor("#000000"));
        ;

        switch (alert_level) {
            case 0:
                ((Button) this.findViewById(R.id.lowButton)).setTextColor(Color.parseColor("#FF0000"));
                ;
                break;

            case 1:
                ((Button) this.findViewById(R.id.midButton)).setTextColor(Color.parseColor("#FF0000"));
                break;

            case 2:
                ((Button) this.findViewById(R.id.highButton)).setTextColor(Color.parseColor("#FF0000"));
                break;
        }
    }

}
