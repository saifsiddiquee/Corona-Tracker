package com.saif.coronatracker.restService;

import android.os.Build;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClients {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://disease.sh/";

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();
        OkHttpClient client;
        int connection_timeout = 30;
        int read_and_write_timeout = 20;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N || Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(interceptor)
//                    .connectionSpecs(Collections.singletonList(spec))
                    .connectTimeout(connection_timeout, TimeUnit.SECONDS) // connect timeout
                    .writeTimeout(read_and_write_timeout, TimeUnit.SECONDS) // write timeout
                    .readTimeout(read_and_write_timeout, TimeUnit.SECONDS)
                    .build();
        } else {
            client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(interceptor)
                    .connectTimeout(connection_timeout, TimeUnit.SECONDS) // connect timeout
                    .writeTimeout(read_and_write_timeout, TimeUnit.SECONDS) // write timeout
                    .readTimeout(read_and_write_timeout, TimeUnit.SECONDS)
                    .build();
        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
