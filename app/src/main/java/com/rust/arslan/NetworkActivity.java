package com.rust.arslan;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.*;
import android.webkit.WebView;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

public class NetworkActivity extends Activity {
    public static String TAG = "rust";
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL =
            "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    public static boolean refreshDisplay = true;

    public static String sPref = null;

    private NetworkReceiver receiver = new NetworkReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        sPref = sharedPreferences.getString("listPref", "Wi-Fi");
        updateConnectedFlags();
        if (refreshDisplay) {
            loadPage();
        }

    }

    public void updateConnectedFlags() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activieInfo = connMgr.getActiveNetworkInfo();
        if (activieInfo != null && activieInfo.isConnected()) {
            wifiConnected = activieInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activieInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }
    }

    public void loadPage() {
        if ((sPref.equals(ANY) && (wifiConnected || mobileConnected))
                || (sPref.equals(WIFI) && wifiConnected)) {
//            new DownloadXmlTask().execute(URL);
        } else {
//            new DownloadXmlTask().execute(URL);
        }
    }

}

