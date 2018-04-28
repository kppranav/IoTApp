package com.example.fragments;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.arduinoesp.Appconstants;
import com.example.arduinoesp.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by uvionics on 4/2/16.
 */
public class LightFragment extends Fragment {

    Button lightButton;
    ImageView bulbImageView;
    MediaPlayer player;
    String light = "0";
    EditText ipText;
    String ipVal;

    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        setupUi(view);
        return view ;
    }

    private void setupUi(View view) {
        lightButton = (Button) view.findViewById(R.id.onoffButton);
        bulbImageView = (ImageView) view.findViewById(R.id.bulbImageView);
        player = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.switch_sound);

        ipText = (EditText) view.findViewById(R.id.ipText);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());



       // ipText.setText(preferences.getString("bulb", "000.000.000.000"));

        lightButton.setOnClickListener(new View.OnClickListener() {
            String url = "";
            @Override
            public void onClick(View v) {
                ipVal = ipText.getText().toString();
                if (light == "0") {
                    light = "1";
                    player.start();
                    lightButton.setBackgroundResource(R.drawable.switch_1);
                    url = "http://" + ipVal + "/lamp/" + "1";
                    Log.d("TAG", url);
                    bulbImageView.setImageResource(R.drawable.light_bulb);
                    new SendTask().execute(url);

                } else {
                    light = "0";
                    player.start();
                    lightButton.setBackgroundResource(R.drawable.switch_0);
                    url = "http://" + ipVal + "/lamp/" + "0";
                    Log.d("TAG", url);
                    bulbImageView.setImageResource(R.drawable.light_bulb_off);
                    new SendTask().execute(url);
                }

            }
        });

    }

    private class SendTask extends AsyncTask<String, Void, String> {

        String url;

        @Override
        protected String doInBackground(String... params) {

            String p = params[0];
            String serverResponse = "";
            HttpClient httpclient = new DefaultHttpClient();
            try {
                HttpGet httpGet = new HttpGet();
                httpGet.setURI(new URI(p));
                HttpResponse httpResponse = httpclient.execute(httpGet);

                InputStream inputStream = null;
                inputStream = httpResponse.getEntity().getContent();
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream));
                serverResponse = bufferedReader.readLine();
                inputStream.close();

            } catch (URISyntaxException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();

            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("TAG", s);
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ipText.setText(preferences.getString("bulb", "000.000.000.000"));
    }
}
