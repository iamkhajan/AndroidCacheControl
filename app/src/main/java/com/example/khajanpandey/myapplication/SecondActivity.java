package com.example.khajanpandey.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by khajanpandey on 7/27/18.
 */

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    public static final String BASE_URL = "https://staging.boschnext.com/dev/RB.Bnext.Configuration/v1.0/api/configuration/ios/version?currentVersion=2.4.0";
    public static final int TIME_INTERVAL = 10000;
    TextView textView,textView1,textView2;
     RestClient restClient;
     Request request;
    private boolean isCallToNetwork;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.hello_world);
        textView1 = (TextView) findViewById(R.id.app_update_required);
        textView2 = (TextView) findViewById(R.id.app_version);

        restClient = new RestClient(getApplicationContext());
        request = RestClient.getRequest();
        isCallToNetwork = true;
    }

    //starting thread for periodic call
    private void periodicNetworkRequest()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isCallToNetwork)
                {
                    try{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView1.setText("");
                                textView2.setText("");
                                textView.setText("Loading data");
                            }
                        });

                        networkRequestWithCache(restClient,request);
                        Thread.sleep(TIME_INTERVAL);
                    }
                    catch (Exception e){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("Exception occurred");
                            }
                        });
                    }

                }

            }
        }).start();
    }


    //network call with cache
    private void networkRequestWithCache(RestClient restClient, Request request){

                restClient.getClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d(TAG, "onFailure: "+e.getMessage());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText("Failed");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            final String ss = response.body().string().toString();
                            Log.d(TAG, "onResponse: "+ss);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        JSONObject js = new JSONObject(ss);
                                        boolean forceUpdate = js.getBoolean("forceUpdate");
                                        String versionName = js.getString("latestVersion");
                                        textView1.setText(""+forceUpdate);
                                        textView2.setText(versionName);
                                        textView.setText("");
                                    }
                                    catch (Exception e){
                                        Log.e(TAG, "run: Exception" );
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                textView.setText("Exception occurred");
                                            }
                                        });
                                    }

                                }
                            });
                        }
                    });



    }


    public void serverCall(View view) {

        if(textView1 !=null && textView2 !=null ){
            textView1.setText("");
            textView2.setText("");
        }
        networkRequestWithCache(restClient,request);
    }

    public void stopServerCall(View view) {

        isCallToNetwork = false;
    }

    public void periodicCall(View view) {
        isCallToNetwork = true;
        periodicNetworkRequest();
    }
}
