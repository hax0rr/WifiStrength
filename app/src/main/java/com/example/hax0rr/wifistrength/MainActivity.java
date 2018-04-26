package com.example.hax0rr.wifistrength;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn=(Button) findViewById(R.id.button);
        Button btn2=(Button) findViewById(R.id.button2);
//        btn2.setVisibility(View.INVISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickbutton();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDetails();
            }
        });

    }

    public void onClickbutton(){
        Button btn2=(Button) findViewById(R.id.button2);

        TextView connectionStatus=(TextView)findViewById(R.id.connectionStatus) ;
        TextView ssid=(TextView)findViewById(R.id.ssid);
        WifiManager wifiManager=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        if(wifiInfo.getSupplicantState()== SupplicantState.COMPLETED) {
            connectionStatus.setText("You Are Connected");

            ssid.setText(wifiInfo.getSSID());
        }else{
            ssid.setText("You aren't connected!");
        }

    }

    public void onClickDetails(){
            Intent intent = new Intent(this, StrengthDetails.class);
            startActivity(intent);
    }
}
