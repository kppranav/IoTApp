package com.example.fragments;

import android.content.SharedPreferences;
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
 * Created by uvionics on 8/12/16.
 */
public class OthersFragment extends Fragment {

    Button button;
    String status = "0";
    String ipVal;
    String url = "";
    SharedPreferences preferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view) {

        button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("0")) {
                    status = "1";
                    button.setBackgroundResource(R.drawable.switch_1);
                    url = "http://" + ipVal + "/plug/" + "1";
                    Log.d("TAG", url);
                    new SendTask().execute(url);
                } else if (status.equals("1")) {
                    status = "0";
                    button.setBackgroundResource(R.drawable.switch_0);
                    url = "http://" + ipVal + "/plug/" + "0";
                    Log.d("TAG", url);
                    new SendTask().execute(url);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        ipVal = preferences.getString("bulb", "");
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

}
