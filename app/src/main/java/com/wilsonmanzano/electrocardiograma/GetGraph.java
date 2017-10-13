package com.wilsonmanzano.electrocardiograma;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ${User} on 31/07/2017.
 */

public class GetGraph extends AsyncTask<Object, Object, ArrayList<Integer>> {

    private StringBuilder stringBuilder;
    private ArrayList<Integer> arrayList;


    @Override
    protected ArrayList<Integer> doInBackground(Object... params) {
        arrayList = new ArrayList<>();
        String host = "http://192.168.0.9/ConnectionDataBase.php";
        URL url;
        try {
            url = new URL(host);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            stringBuilder = new StringBuilder();

            String line = "";

            while ((line = bufferedReader.readLine())!= null){

                stringBuilder.append(line).append("\n");

            }

            httpURLConnection.disconnect();

        } catch (MalformedURLException e){
            Log.e("Creating URLConnection", e.toString());

        } catch (IOException e) {
            Log.e("IOException", e.toString());
        }

        try {
            String json_string = stringBuilder.toString().trim();
            Log.d("JSON_STRING", json_string);
            JSONObject jsonObject = new JSONObject(json_string);
            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
            int count = 0;
            while (count<jsonArray.length()){

                JSONObject JO = jsonArray.getJSONObject(count);
                arrayList.add(count,JO.getInt("data"));
                count++;


            }

        } catch (JSONException e){
            Log.e("JSONException", e.toString());
        }

        return arrayList;


    }
}