package com.example.khajanpandey.myapplication;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.khajanpandey.myapplication.interceptors.NetworkInterceptor;
import com.example.khajanpandey.myapplication.interceptors.OfflineInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by khajanpandey on 7/27/18.
 */

public class RestClient {

    Context mContext;


    public RestClient(Context context)
    {
        mContext = context ;

    }

    public OkHttpClient getClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        File cacheDirectory = new File(mContext.getCacheDir().getAbsolutePath(), "MyHttpCache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(cacheDirectory, cacheSize);


        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
               .addNetworkInterceptor(new NetworkInterceptor())
               .addInterceptor(new OfflineInterceptor(mContext))
                .addInterceptor(logging)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1,TimeUnit.MINUTES)
                .build();
        return client;
    }


    public static Request getRequest(){

        Request request = new Request.Builder()
                .url(SecondActivity.BASE_URL)
                .build();
        return request;
    }

    private static final OkHttpClient client = new OkHttpClient();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void run() throws Exception {
        Request request = new Request.Builder()
                .url("https://api.github.com/repos/square/okhttp/issues")
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json; q=0.5")
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println("Server: " + response.header("Server"));
            System.out.println("Date: " + response.header("Date"));
            System.out.println("Vary: " + response.headers("Vary"));
        }
    }


}
