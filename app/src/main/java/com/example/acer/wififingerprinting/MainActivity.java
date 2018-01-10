package com.example.acer.wififingerprinting;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    WifiManager myWifiManager;
    boolean wasEnabled;
    ArrayList<FingerPrintBean> mFingerPrintBeanArrayList;
    ArrayAdapter<FingerPrintBean> mArrayAdapter;
    Intent mIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mFingerPrintBeanArrayList = new ArrayList<FingerPrintBean>();
        mIntent = new Intent(this, Main2Activity.class);

        ListView mListView = (ListView) findViewById(R.id.lv1);
        Button mButton = (Button) findViewById(R.id.button);
        mArrayAdapter = new ArrayAdapter<FingerPrintBean>(this, android.R.layout.simple_list_item_1, mFingerPrintBeanArrayList);
        mListView.setAdapter(mArrayAdapter);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                refresh();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                //writeNewFPBean(mFingerPrintBeanArrayList.get(pI));
                mIntent.putExtra("macAddress", mFingerPrintBeanArrayList.get(pI).getMacAddress());
                mIntent.putExtra("SSID", mFingerPrintBeanArrayList.get(pI).getSSID());
                mIntent.putExtra("RSSI", mFingerPrintBeanArrayList.get(pI).getLevel());
                startActivity(mIntent);
            }
        });
    }


    public void refresh(){
        //mFingerPrintBeanArrayList = new ArrayList<FingerPrintBean>();
        mFingerPrintBeanArrayList.clear();
        wasEnabled = myWifiManager.isWifiEnabled();
        if (!wasEnabled)
            myWifiManager.setWifiEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("pras","inside permission");
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0x12345);
        }

        FingerPrintBean mFingerPrintBean;

        if (myWifiManager.isWifiEnabled()) {
            if (myWifiManager.startScan()) {
                List<ScanResult> scans = myWifiManager.getScanResults();

                //int topThree = 3;
                if (scans != null && !scans.isEmpty()) {
                    int i=0;
                    for (ScanResult scan : scans) {
                        Double level = (double)scan.level;
                        mFingerPrintBean = new FingerPrintBean(scan.BSSID, scan.SSID, scan.level);
                        mFingerPrintBeanArrayList.add(mFingerPrintBean);
                    }
                } else {
                    Log.d("pras", "inside else");
                }
            }
        }

        mArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (requestCode == 0x12345) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        WifiManager myWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        boolean wasEnabled = myWifiManager.isWifiEnabled();
        if (!wasEnabled)
            myWifiManager.setWifiEnabled(true);
        if (myWifiManager.isWifiEnabled()) {
            if (myWifiManager.startScan()) {
                List<ScanResult> scans = myWifiManager.getScanResults();
                if (scans != null && !scans.isEmpty()) {
                    for (ScanResult scan : scans) {
                        int level = WifiManager.calculateSignalLevel(scan.level, 20);
                        Log.d("sam", level + "this is the signal level");
                    }
                } else {
                    Log.d("sam", "Scans are either null or empty");
                }
            }
        }

    }

}

}

