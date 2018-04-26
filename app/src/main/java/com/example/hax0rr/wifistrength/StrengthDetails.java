package com.example.hax0rr.wifistrength;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class StrengthDetails extends AppCompatActivity {
    File dir, details;
    OutputStream out;
    WifiManager wifiManager;
    SimpleDateFormat date;

    ArrayList<String> apList;
    ArrayAdapter adapter;
    WifiInfo wifiInfo;
    InputStream in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_details);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        final TextView detailsBox=(TextView)findViewById(R.id.detailsBox) ;
        wifiManager=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiInfo=wifiManager.getConnectionInfo();

        date=new SimpleDateFormat("EEE MMM dd yyyy hh:mm a");
        dir=new File(getExternalFilesDir(null),"data");
        details=new File(dir,"WiFiDetails.txt");

        if(!dir.exists()){
            dir.mkdir();
            try {
                details.createNewFile();
                Log.v("Hua","huaaaa");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out = new FileOutputStream(details, true);
            in = new FileInputStream(details);

        }catch(IOException e){
            e.printStackTrace();
        }
        /////
        try{
            out.write("_______________________________________\n".getBytes());}
        catch(Exception e){
            e.printStackTrace();
        }
//        Log.v("hee","hee");
        apList = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, apList);

        /////

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        Log.v("hee","hello");
        apList.clear();

        registerReceiver(new BroadcastReceiver() {
            List<ScanResult> scanResults;

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("Aya hau","andar aya he");
                scanResults = wifiManager.getScanResults();
                int size = scanResults.size();

                try {

                    String currDate = date.format(Calendar.getInstance().getTime());
                    Log.v("date",currDate);

                    String text= currDate +"\n";
                    text = text +"----WiFi Strengths----";

                    for(int i=0;i<size;i++) {

                        String res = "WiFi SSID: " + scanResults.get(i).SSID + " | WiFi Strength: " + scanResults.get(i).level;
                        apList.add(res);
                        text = text + "\n" + res;
                        adapter.notifyDataSetChanged();
                    }
                    text += "\n";
                    Log.v("Show Wifi",text);
                    detailsBox.setText(text);
                    out.write(text.getBytes());
                    unregisterReceiver(this);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();


    }
}
