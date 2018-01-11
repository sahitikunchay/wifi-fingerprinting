package com.example.acer.wififingerprinting;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ClipData;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    WifiManager myWifiManager;
    boolean wasEnabled;
    ArrayList<FingerPrintBean> mFingerPrintBeanArrayList;
    ArrayList<FingerPrintBean> pFingerPrintBeanArrayList;
    ArrayAdapter<FingerPrintBean> mArrayAdapter;
    Intent mIntent;
    EditText locLabelET;
    CheckBox crowdedCB;
    dbInstanceBean mdbInstaceBean;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        myWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mFingerPrintBeanArrayList = new ArrayList<FingerPrintBean>();
        mIntent = new Intent(this, Main2Activity.class);

        ListView mListView = (ListView) findViewById(R.id.lv1);
        Button mButton = (Button) findViewById(R.id.button);
        final Button submit = (Button) findViewById(R.id.button3);
        locLabelET = (EditText) findViewById(R.id.editText3);
        crowdedCB = (CheckBox) findViewById(R.id.checkBox3);
        mArrayAdapter = new ArrayAdapter<FingerPrintBean>(this, android.R.layout.simple_list_item_1, mFingerPrintBeanArrayList);
        mListView.setAdapter(mArrayAdapter);

        submit.setEnabled(false);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                refresh();
                submit.setEnabled(true);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                submit();
            }
        });



/*        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pAdapterView, View pView, int pI, long pL) {
                //writeNewFPBean(mFingerPrintBeanArrayList.get(pI));
                mIntent.putExtra("macAddress", mFingerPrintBeanArrayList.get(pI).getMacAddress());
                mIntent.putExtra("SSID", mFingerPrintBeanArrayList.get(pI).getSSID());
                mIntent.putExtra("RSSI", mFingerPrintBeanArrayList.get(pI).getLevel());
                startActivity(mIntent);
            }
        });*/


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

//        if (myWifiManager.isWifiEnabled()) {
//            if (myWifiManager.startScan()) {
//                List<ScanResult> scans = myWifiManager.getScanResults();
//
//                //int topThree = 3;
//                if (scans != null && !scans.isEmpty()) {
//                    int i=0;
//                    for (ScanResult scan : scans) {
//                        Double level = (double)scan.level;
//                        mFingerPrintBean = new FingerPrintBean(scan.BSSID, scan.SSID, scan.level);
//                        mFingerPrintBeanArrayList.add(mFingerPrintBean);
//                    }
//                } else {
//                    Log.d("pras", "inside else");
//                }
//            }
//        }
        if (myWifiManager.isWifiEnabled()) {
            if (myWifiManager.startScan()) {
                int i=0;
                pFingerPrintBeanArrayList = new ArrayList<FingerPrintBean>();
                for(i=0; i<10; i++){
                    List<ScanResult> scans = myWifiManager.getScanResults();

                    if (scans != null && !scans.isEmpty()) {
                        for (ScanResult scan : scans) {
                            Double level = (double)scan.level;
                            mFingerPrintBean = new FingerPrintBean(scan.BSSID, scan.SSID, scan.level);
                            pFingerPrintBeanArrayList.add(mFingerPrintBean);
                        }
                    } else {
                        Log.d("pras", "inside else");
                    }
                }
            }
        }
        int i=0, k=0;

        for(i=0; i<pFingerPrintBeanArrayList.size(); i++){
            FingerPrintBean someBean = pFingerPrintBeanArrayList.get(i);
            int count=0;
            for(k=0; k<pFingerPrintBeanArrayList.size(); k++){
                if(pFingerPrintBeanArrayList.get(k).equals(someBean)){
                    count++;
                    int newlevel = someBean.getLevel()+pFingerPrintBeanArrayList.get(k).getLevel();
                    someBean.setLevel(newlevel);
                    pFingerPrintBeanArrayList.remove(k);
                    Log.d("sam", ""+pFingerPrintBeanArrayList.size());
                }
            }
            someBean.setLevel(someBean.getLevel()/count);
            if(!mFingerPrintBeanArrayList.contains(someBean)) {
                mFingerPrintBeanArrayList.add(someBean);
            }
        }


        mArrayAdapter.notifyDataSetChanged();
        locLabelET.setText("");
        crowdedCB.setChecked(false);
    }

    private void submit() {
        String locationLabel = locLabelET.getText().toString();
        int p = 0;
        for(p=0; p<mFingerPrintBeanArrayList.size(); p++) {
            FingerPrintBean mFingerPrintBean = mFingerPrintBeanArrayList.get(p);
            if (crowdedCB.isChecked()) {
                mdbInstaceBean = new dbInstanceBean(mFingerPrintBean.getMacAddress(), mFingerPrintBean.getSSID(), mFingerPrintBean.getLevel(), 1, locationLabel, String.valueOf(System.currentTimeMillis()));
            } else {
                mdbInstaceBean = new dbInstanceBean(mFingerPrintBean.getMacAddress(), mFingerPrintBean.getSSID(), mFingerPrintBean.getLevel(), 0, locationLabel, String.valueOf(System.currentTimeMillis()));
            }
            mDatabase.push().setValue(mdbInstaceBean);
        }
        Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
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

