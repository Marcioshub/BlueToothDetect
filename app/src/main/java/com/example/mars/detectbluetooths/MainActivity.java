package com.example.mars.detectbluetooths;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    BluetoothAdapter BTAdapter;
    Button btn;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btnOnOff);
        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableDisableBT();
            }
        });

        Context context = getApplicationContext();
        Toast.makeText(context, "App is on", Toast.LENGTH_SHORT).show();
    }

    public void enableDisableBT(){
        Context context = getApplicationContext();

        if(BTAdapter == null){
            Toast.makeText(context, "This device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        }
        else{
            if(!BTAdapter.isEnabled()){
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(enableBTIntent);

                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mReceiver, BTIntent);
            }
            else{
                BTAdapter.disable();

                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mReceiver, BTIntent);
            }
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BTAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BTAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(context, "STATE OFF", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Toast.makeText(context, "STATE TURNING OFF", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "STATE TURING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(context, "STATE ON", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Toast.makeText(context, "STATE TURNING ON", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "STATE TURNING ON");
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
