package com.example.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by uvionics on 5/7/16.
 */
public class HttpConnection {

    public String Send(String url) {

        String serverResponse = "";
        HttpClient httpclient = new DefaultHttpClient();
        try {
            //HttpGet httpGet = new HttpGet();
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(new URI(url));
            HttpResponse httpResponse = httpclient.execute(httpPost);

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

}
