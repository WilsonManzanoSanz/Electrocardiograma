package com.wilsonmanzano.electrocardiograma;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class GraphActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Integer> arrayList;
    private TextView textBPM;
    LineDataSet lineDataSet;
    LineChart lineChart;
    LineData lineData;
    List<Entry> signal;
    String host;
    URL url;
    StringBuilder stringBuilder;
    private Handler mUpdateHandler;
    int BPM = 0;
    private int array_length;
    private TextView pulsaciones;
    private ImageButton stopButton;
    private TimerTask timerTask;
    private boolean startRefresh = false;
    private int Edad;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        pulsaciones = (TextView) findViewById(R.id.text_pulsaciones);
        stopButton = (ImageButton) findViewById(R.id.stopButton);
        textBPM = (TextView) findViewById(R.id.textEstado);

        stopButton.setOnClickListener(this);

        arrayList = new ArrayList<>();
        lineChart = (LineChart) findViewById(R.id.graph);
        signal = new ArrayList<>();
        //getGraph = new GetGraph();
         Edad = getIntent().getIntExtra("EDAD",0);
         host = getIntent().getStringExtra("IP");
         if (Objects.equals(host, "nada")){
             host = "http://35.194.40.149/json.php";
             array_length = 1001;
         }
         else {
             host = "http://" + host;
             array_length = 1599;
         }


        //lineChart.setData(lineData);
        Log.i("update", "0");

        arrayList.clear();
        for (int i = 0; i < array_length; i++) {
            arrayList.add(0);
            signal.add(new Entry(i,arrayList.get(i)));

        }

        refreshGraph();



         mUpdateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                if (Objects.equals(message, "UpdateGraph")){
                    lineDataSet = new LineDataSet(signal,"V");
                    lineDataSet.setDrawCircles(false);
                    lineDataSet.setCircleColor(Color.CYAN);
                    lineDataSet.setDrawValues(true);

                    lineData = new LineData();
                    lineData.addDataSet(lineDataSet);
                    lineChart.setData(lineData);
                    lineChart.invalidate(); // refresh
                    pulsaciones.setText(String.valueOf(BPM));
                    if (Edad < 20){
                        if (BPM > 85){
                            textBPM.setText("Acelerado");
                            Utils.showNotification(getApplicationContext(),"ALERTA TATICARDIA","BPM: "+ String.valueOf(BPM),R.drawable.alert,21);
                        }
                        else if (BPM <86 && BPM > 70){
                            textBPM.setText("Normal");

                        }
                        else if (BPM <= 70 && BPM >59){
                            textBPM.setText("Buena");
                        }
                        else if (BPM <= 59){
                            textBPM.setText("Bradicardia ");
                        }
                    }

                    else if (Edad >= 20 && Edad <40){
                        if (BPM > 85){
                            textBPM.setText("Acelerado");
                            Utils.showNotification(getApplicationContext(),"ALERTA TATICARDIA","BPM: "+ String.valueOf(BPM),R.drawable.alert,21);
                        }
                        else if (BPM <86 && BPM > 70){
                            textBPM.setText("Normal");

                        }
                        else if (BPM <= 70){
                            textBPM.setText("Buena");
                        }
                        else if (BPM <= 59){
                            textBPM.setText("Bradicardia ");
                        }


                    }
                    else if (Edad >= 40 && Edad <60){
                        if (BPM > 88){
                            textBPM.setText("Acelerado");
                            Utils.showNotification(getApplicationContext(),"ALERTA TATICARDIA","BPM: "+ String.valueOf(BPM),R.drawable.alert,21);
                        }
                        else if (BPM <=88 && BPM > 72){
                            textBPM.setText("Normal");

                        }
                        else if (BPM <= 72){
                            textBPM.setText("Buena");
                        }
                        else if (BPM <= 59){
                            textBPM.setText("Bradicardia ");
                        }
                    }
                    else if (Edad >= 60){
                        if (BPM > 90){
                            textBPM.setText("Acelerado");
                            Utils.showNotification(getApplicationContext(),"ALERTA TATICARDIA","BPM: "+ String.valueOf(BPM),R.drawable.alert,21);
                        }
                        else if (BPM <=90 && BPM > 74){
                            textBPM.setText("Normal");

                        }
                        else if (BPM <= 74){
                            textBPM.setText("Buena");
                        }
                        else if (BPM <= 59){
                            textBPM.setText("Bradicardia ");
                        }

                    }

                }

            }
        };



    }

    private void refreshGraph() {
        startRefresh = true;
        timer = new Timer();

         timerTask = new TimerTask() {
            @Override
            public void run() {

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
                    int picos = 0;
                    int inicio = 0;
                    int fin = 0;
                    while (count<jsonArray.length()-1){

                        JSONObject JO = jsonArray.getJSONObject(count);
                        if (jsonArray.getJSONObject(count).getInt("data")>550){

                            if ( (jsonArray.getJSONObject(count).getInt("data"))>(jsonArray.getJSONObject(count-1).getInt("data")) &&
                                    ((jsonArray.getJSONObject(count).getInt("data"))>(jsonArray.getJSONObject(count+1).getInt("data")))){
                                if (inicio==0) {
                                    inicio = count;
                                }
                                else {
                                    fin = count;
                                }
                                picos++;
                            }
                        }

                        arrayList.set(count,JO.getInt("data"));
                        signal.set(count,new Entry(count,arrayList.get(count)));
                        count++;


                    }
                    Log.d("picos", String.valueOf(picos));
                    if (picos!=0) {
                        BPM = (picos) * 12;
                    }


                } catch (JSONException e){
                    Log.e("JSONException", e.toString());
                }
                Bundle messageBundle = new Bundle();
                messageBundle.putString("msg", "UpdateGraph");
                Message message = new Message();
                message.setData(messageBundle);
                mUpdateHandler.sendMessage(message);


            }
        };
        timer.schedule(timerTask,0,1000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stopButton:
                if (startRefresh) {
                    timer.cancel();
                    timer.purge();
                    startRefresh = false;
                    stopButton.setImageResource(R.drawable.play);
                }
                else  {
                    refreshGraph();
                    startRefresh = true;
                    stopButton.setImageResource(R.drawable.stop64);
                }
                break;

        }
    }
}
