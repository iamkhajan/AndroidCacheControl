package com.example.khajanpandey.myapplication.interceptors;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * to make sure we are caching data irrespective of response
 * make sure server does appropriate changes
 */

public class NetworkInterceptor implements Interceptor {

    public static String CACHE_CONTROL = "Cache-Control";
    public static String ONLY_IF_CACHED = "only-if-cached";
    public static String NO_STORE = "no-store";
    public static String NO_CACHE = "no-cache";
    public static String MUST_REVALIDATE = "must-revalidate";
    public static String DEFAULT_MAX_AGE = "max-age=0";
    public static String PRAGMA = "";


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        CacheControl cache = new CacheControl.Builder()
                .maxAge(60, TimeUnit.SECONDS)
                .build();

        String cacheControl = originalResponse.header(CACHE_CONTROL);
        if (cacheControl == null || cacheControl.contains(NO_STORE) || cacheControl.contains(NO_CACHE) ||
                cacheControl.contains(MUST_REVALIDATE) || cacheControl.contains(DEFAULT_MAX_AGE)) {
            return originalResponse.newBuilder()
                    .removeHeader(PRAGMA)
//                    .header(CACHE_CONTROL, "public, max-age=" + CACHE_AGE)
                    .header(CACHE_CONTROL, cache.toString())
                    .build();
        } else {
            return originalResponse;
        }
    }
}
