package com.rust.arslan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import static com.rust.arslan.NetworkActivity.WIFI;
import static com.rust.arslan.NetworkActivity.ANY;
import static com.rust.arslan.NetworkActivity.refreshDisplay;
import static com.rust.arslan.NetworkActivity.sPref;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (WIFI.equals(sPref) && networkInfo != null &&
                networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            refreshDisplay = true;
            Toast.makeText(context, "WIFI connected", Toast.LENGTH_SHORT).show();
        } else if (ANY.equals(sPref) && networkInfo != null) {
            refreshDisplay = true;
        } else {
            refreshDisplay = false;
            Toast.makeText(context, "lost connection", Toast.LENGTH_SHORT).show();
        }


    }
}
