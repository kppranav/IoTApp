package com.example.arduinoesp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.utils.HttpConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by uvionics on 1/2/16.
 */
public class HomeActivity extends AppCompatActivity {

    ToggleButton button;
    SendTask task = new SendTask();
    WifiManager manager;
    WifiConfiguration config;
    ImageView image;
    Button onoff;
    String light = "0";

    MediaPlayer player;

    DrawerLayout Drawer;
    ActionBarDrawerToggle drawerToggle;
    Toolbar bar;

    ProgressDialog dialog;

    TextView wattageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        wattageText = (TextView) findViewById(R.id.wattageTextView);
        wattageText.setVisibility(View.VISIBLE);

        bar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(bar);

        setupNavDrawer();

        button = (ToggleButton) findViewById(R.id.toggleButton);
        onoff = (Button) findViewById(R.id.onoffButton);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Connecting....!");


        button.setChecked(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = button.getText().toString();

                String url = "";

                switch (s) {
                    case "ON":
                        url = Appconstants.URL + "0";
                        image.setImageResource(R.drawable.light_bulb_off);
                        break;
                    case "OFF":
                        url = Appconstants.URL + "1";
                        image.setImageResource(R.drawable.light_bulb);
                        break;
                }

                Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();


            }
        });

        player = MediaPlayer.create(this, R.raw.switch_sound);

        image = (ImageView) findViewById(R.id.bulbImageView);

        onoff.setOnClickListener(new View.OnClickListener() {
            String url = "";
            @Override
            public void onClick(View v) {
                if (light == "0") {
                    light = "1";
                    player.start();
                    onoff.setBackgroundResource(R.drawable.switch_1);
                    url = Appconstants.URL + "?status=on";
                    image.setImageResource(R.drawable.light_bulb);
                    new SendTask().execute(url);

                } else {
                    light = "0";
                    player.start();
                    onoff.setBackgroundResource(R.drawable.switch_0);
                    url = Appconstants.URL + "?status=off";
                    image.setImageResource(R.drawable.light_bulb_off);
                    new SendTask().execute(url);
                }
            }
        });

        manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        List<ScanResult> results = manager.getScanResults();

        for (ScanResult result: results) {
            int level = WifiManager.calculateSignalLevel(result.level, 5);
            Log.d("TAG", " From : " + result.SSID + " level : " + level + " strength : " + result.level);
        }

        new StatusTask().execute(Appconstants.FEEDBACK_URL);


    }

    private void setupNavDrawer() {

            Drawer = (DrawerLayout) findViewById(R.id.myDrawer);
            drawerToggle = new ActionBarDrawerToggle(this, Drawer, bar, R.string.opendrawer, R.string.closedrawer){

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };

            Drawer.setDrawerListener(drawerToggle);
            drawerToggle.syncState();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //checkWifi();
        //connectWifi();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: Toast.makeText(getApplicationContext(), "Settings",
                    Toast.LENGTH_SHORT).show();
                break;
            case R.id.test: startActivity(new Intent(HomeActivity.this, DevicesActivity.class));
                            finish();
                            break;
            default:
        }
        return true;
    }

    private void checkWifi() {
        if (manager.isWifiEnabled()) {

            WifiInfo info = manager.getConnectionInfo();
            switch (info.getSSID()) {
                case "\"floodlight\"": Log.d("TAG", "Connected to floodlight");
                    break;
                default: Log.d("TAG", "Connected to " + info.getSSID());
                           // alert1(info);
                            manager.disableNetwork(info.getNetworkId());
                           // connectWifi();

            }

        } else {
            manager.setWifiEnabled(true);
        }
    }

    private void alert1(final WifiInfo info) {

        String ssid = info.getSSID();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your device is currently connected to " + ssid +
                " Change the connection to floodlight....?");

        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                manager.disableNetwork(info.getNetworkId());
               // connectWifi();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.setTitle("Chenge Wifi..");
        alert.show();


    }

    private void connectWifi() {

        dialog.show();
        manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // Setup Wifi configuration
        config = new WifiConfiguration();
        config.SSID = "floodlight";
        config.preSharedKey = "\"12345678\"";
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        // Connect and Enable connection
        int netId = manager.addNetwork(config);
        if (netId < 0) {
            Toast.makeText(getApplicationContext(), "Could not able to connnect to the network" +
                    " floodlight", Toast.LENGTH_SHORT).show();
        } else {

            boolean b = manager.enableNetwork(netId, true);
            manager.setWifiEnabled(true);
            if (b) {
                dialog.dismiss();
                Log.d("TAG", "Connected to floodlight");
                String url = Appconstants.URL + "read";
                new SendTask().execute(url);
            } else {
                Log.d("TAG", "Network NOT Enabled!!!!");
            }

        }

    }

    private class SendTask extends AsyncTask<String, Void, String> {

        String url;

        @Override
        protected String doInBackground(String... params) {

            String p = params[0];
            Log.d("TAG", p);
            String resp = new HttpConnection().Send(p);

            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("TAG", s);
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            new StatusTask().execute(Appconstants.FEEDBACK_URL);
        }
    }

    private class StatusTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String resp = new HttpConnection().Send(params[0]);
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("TAG", "feedback : " + s);
            wattageText.setText(s);

            /*float val = Float.parseFloat(s);

            if (val == 0.0f) {

                onoff.setBackgroundResource(R.drawable.switch_1);
                image.setImageResource(R.drawable.light_bulb);

            } else {

                onoff.setBackgroundResource(R.drawable.switch_0);
                image.setImageResource(R.drawable.light_bulb_off);
            }*/
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
