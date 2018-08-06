package com.example.khajanpandey.myapplication;

import android.content.Context;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by khajanpandey on 7/30/18.
 */

public class ForcedCacheInterceptor implements Interceptor {
    Context mContext;

    public ForcedCacheInterceptor(Context context)
    {
        mContext = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (!NetworkUtils.isNetworkAvailable(mContext)) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        }

        return chain.proceed(builder.build());
    }
}
