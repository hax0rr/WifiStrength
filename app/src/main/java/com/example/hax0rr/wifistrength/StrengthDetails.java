package com.example.hax0rr.wifistrength;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StrengthDetails extends AppCompatActivity {
    File dir, details;
    OutputStream out;
    WifiManager wifiManager;
    SimpleDateFormat simpleDateFormat,date;
    String time;
    WifiInfo wifiInfo;
    InputStream in;
    int counter=0;
    String currTime,currDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_details);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(StrengthDetails.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        final TextView detailsBox=(TextView)findViewById(R.id.detailsBox) ;
        wifiManager=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiInfo=wifiManager.getConnectionInfo();
        simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");
        date=new SimpleDateFormat("EEE MMM dd yyyy");
        dir=new File(getExternalFilesDir(null),"data");
        details=new File(dir,"WiFiDetails.txt");
        if(!dir.exists()){
            dir.mkdir();
            try {
                details.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!details.exists()){
            try {
                details.createNewFile();
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
        new Thread() {
            public void run() {
                Calendar time = Calendar.getInstance();
                long millis = time.getTimeInMillis();

                try {

                    counter = 0;
                    Log.v("TEST","counter "+counter);

                    currDate=date.format(time.getTime());
                    int ipAddress=wifiInfo.getIpAddress();
                    String ipaddress=String.format("%d.%d.%d.%d", (ipAddress & 0xff),(ipAddress >> 8 & 0xff),(ipAddress >> 16 & 0xff),(ipAddress >> 24 & 0xff));
                    String text = "On " +currDate + "\nIP address:" +ipaddress+ "\n----------------------\n";
                    Log.v("TEST","String "+text);
                    out.write(text.getBytes());
                    detailsBox.setText(text);

                    while (counter < 20) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                                try {

                                    currTime = simpleDateFormat.format(Calendar.getInstance().getTime());
                                    String linkSpeed=currTime+" :"+wifiInfo.getLinkSpeed()+"mbps"+ "\n";
                                    out.write(linkSpeed.getBytes());
                                    detailsBox.append(linkSpeed);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                counter++;
                            }
                        });
                    }

                }catch(Exception e){
                    e.printStackTrace();
                    Log.v("EXCEPTION","In exception"+e.getMessage());
                }
            }
        }.start();



        /////
    }
}
