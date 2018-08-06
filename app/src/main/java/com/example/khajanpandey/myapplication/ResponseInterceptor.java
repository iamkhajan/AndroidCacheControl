package com.example.khajanpandey.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.khajanpandey.myapplication.interceptors.NetworkInterceptor.CACHE_CONTROL;

/**
 * Created by khajanpandey on 7/27/18.
 */

public class ResponseInterceptor implements Interceptor {
    Context mContext;
    private static final String TAG = "ResponseInterceptor";

    public  ResponseInterceptor(Context context) {
        mContext=context;

    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse = chain.proceed(chain.request());
        Request newRequest;

        Request.Builder builder = chain.request().newBuilder();

        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge( 60, TimeUnit.SECONDS )
                .build();
        return originalResponse.newBuilder()
                .addHeader(CACHE_CONTROL, cacheControl.toString())
                .build();
    }


}
