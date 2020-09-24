package com.example.ping.Wifi_Network_Info.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ping.Wifi_Network_Info.model.WifiDataModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;

// Interface for extracting the JSON Response

interface WifiServiceInterface {

    @Headers("Content-Type: application/json")
    @GET("/?token=88e9dd262b3a3f")
    Call<WifiDataModel> getData();
}

// Class for getting the data using Interface

public class WifiService extends LiveData<WifiDataModel> {
    private WifiServiceInterface wifiServiceInterface;
    protected MutableLiveData<WifiDataModel> wifiLiveData;

    // Constructor for Instantiating the Service

    public WifiService() {
        wifiLiveData = new MutableLiveData<>();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().
                readTimeout(500, TimeUnit.SECONDS).
                connectTimeout(500, TimeUnit.SECONDS).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        String baseURL = "https://ipinfo.io/";
        wifiServiceInterface = new Retrofit.Builder().
                baseUrl(baseURL).
                client(okHttpClient).
                addConverterFactory(GsonConverterFactory.create(gson)).build().create(WifiServiceInterface.class);
    }

    // Method for fetching the Live Data from API

    public void fetchData() {
        wifiServiceInterface.getData().enqueue(new Callback<WifiDataModel>() {
            @Override
            public void onResponse(@NotNull Call<WifiDataModel> call, @NotNull Response<WifiDataModel> response) {
                if (response.body() != null) {
                    wifiLiveData.setValue(response.body());
                    System.out.println(response.body());
                } else {
                    wifiLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull Call<WifiDataModel> call, @NotNull Throwable t) {
                System.out.println(t.getMessage());
                wifiLiveData.setValue(null);
            }
        });
    }

    // Method for getting the Live from the Service

    public MutableLiveData<WifiDataModel> getWifiLiveData() {
        return wifiLiveData;
    }
}
