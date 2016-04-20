package edu.uw.notsetdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by class on 4/18/16.
 */
public class MyReceiver extends BroadcastReceiver {

    private static String TAG = "Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Received: "+intent);

        if(intent.getAction() == Intent.ACTION_BATTERY_LOW){
            Toast.makeText(context, "Battery is low!", Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction() == Intent.ACTION_POWER_DISCONNECTED){
            Toast.makeText(context, "Power disconnected", Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction() == Intent.ACTION_POWER_CONNECTED){
            Toast.makeText(context, "Power connected", Toast.LENGTH_SHORT).show();
        }
        else if(intent.getAction() == MainActivity.ACTION_SMS_SENT){
            Toast.makeText(context, "Message sent!", Toast.LENGTH_SHORT).show();
        }
    }
}
