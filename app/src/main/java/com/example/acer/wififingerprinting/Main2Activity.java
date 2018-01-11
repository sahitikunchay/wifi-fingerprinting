package com.example.acer.wififingerprinting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {

    FingerPrintBean mFingerPrintBean;
    private DatabaseReference mDatabase;
    dbInstanceBean mdbInstaceBean;
    Intent mIntent;
    EditText locLabelET;
    CheckBox crowdedCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        int crowded = 0;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFingerPrintBean = new FingerPrintBean(getIntent().getExtras().getString("macAddress"), getIntent().getExtras().getString("SSID"), getIntent().getExtras().getInt("RSSI"));

        TextView macAddressTV = (TextView) findViewById(R.id.textView);
        TextView SSIDTV = (TextView) findViewById(R.id.textView2);
        TextView RSSITV = (TextView) findViewById(R.id.textView3);
        locLabelET = (EditText) findViewById(R.id.editText);
        crowdedCB = (CheckBox) findViewById(R.id.checkBox);
        Button submitButton = (Button) findViewById(R.id.button2);

        macAddressTV.setText("MAC Address: " + mFingerPrintBean.getMacAddress());
        SSIDTV.setText("SSID: " + mFingerPrintBean.getSSID());
        RSSITV.setText("RSSI: "+ String.valueOf(mFingerPrintBean.getLevel()));

        mIntent = new Intent(this, MainActivity.class);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                writeNewDBBean();
            }
        });

    }

    private void writeNewDBBean() {
        String locationLabel = locLabelET.getText().toString();
        if(crowdedCB.isChecked()) {
            mdbInstaceBean = new dbInstanceBean(mFingerPrintBean.getMacAddress(), mFingerPrintBean.getSSID(), mFingerPrintBean.getLevel(), 1, locationLabel, String.valueOf(System.currentTimeMillis()));
        } else {
            mdbInstaceBean = new dbInstanceBean(mFingerPrintBean.getMacAddress(), mFingerPrintBean.getSSID(), mFingerPrintBean.getLevel(), 0, locationLabel, String.valueOf(System.currentTimeMillis()));
        }
        mDatabase.push().setValue(mdbInstaceBean);
        startActivity(mIntent);
    }
}
