package com.example.khajanpandey.myapplication.interceptors;

import android.content.Context;

import com.example.khajanpandey.myapplication.NetworkUtils;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.khajanpandey.myapplication.interceptors.NetworkInterceptor.CACHE_CONTROL;
import static com.example.khajanpandey.myapplication.interceptors.NetworkInterceptor.NO_CACHE;
import static com.example.khajanpandey.myapplication.interceptors.NetworkInterceptor.ONLY_IF_CACHED;
import static com.example.khajanpandey.myapplication.interceptors.NetworkInterceptor.PRAGMA;

/**
 */

public class OfflineInterceptor implements Interceptor {
    private Context mContext;
    boolean isDataLoaded;

    public OfflineInterceptor(Context context) {
        this.mContext = context;
        this.isDataLoaded = true;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            if (isDataLoaded) {
                request = request.newBuilder()
                        .removeHeader(PRAGMA)
                        .header(CACHE_CONTROL, ONLY_IF_CACHED)
                        .build();
            } else {
                request = request.newBuilder()
                        .removeHeader(PRAGMA)
                        .header(CACHE_CONTROL, NO_CACHE)
                        .build();
            }
        }
        return chain.proceed(request);
    }
}
