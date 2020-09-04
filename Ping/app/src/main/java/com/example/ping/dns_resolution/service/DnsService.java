package com.example.ping.dns_resolution.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ping.dns_resolution.model.DnsModel;
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
import retrofit2.http.Query;

// Interface for extracting the JSON Response

interface DnsServiceApiInterface {

    @Headers("Content-Type: application/json")
    @GET("/resolve?do=1")
    Call<DnsModel> getModel(
            @Query("name") String name,
            @Query("type") String type
    );
}

// Class for getting the data using Interface

public class DnsService extends LiveData<DnsModel> {
    private DnsServiceApiInterface dnsServiceApiInterface;
    protected MutableLiveData<DnsModel> dnsModelLiveData;

    // Constructor for Instantiating the Service

    public DnsService() {
        dnsModelLiveData = new MutableLiveData<>();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().
                readTimeout(500, TimeUnit.SECONDS).
                connectTimeout(500, TimeUnit.SECONDS).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        String baseURL = "https://dns.google/";
        dnsServiceApiInterface = new Retrofit.Builder().
                baseUrl(baseURL).
                client(okHttpClient).
                addConverterFactory(GsonConverterFactory.create(gson)).build().create(DnsServiceApiInterface.class);
    }

    // Method for fetching the Live Data from API

    public void fetchLiveData(String name, String type) {
        dnsServiceApiInterface.getModel(name, type).
                enqueue(new Callback<DnsModel>() {
                    @Override
                    public void onResponse(@NotNull Call<DnsModel> call, @NotNull Response<DnsModel> response) {
                        if (response.body() != null) {
                            System.out.println(response.body());
                            dnsModelLiveData.setValue(response.body());
                        } else {
                            dnsModelLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<DnsModel> call, @NotNull Throwable t) {
                        System.out.println(t.getMessage());
                        dnsModelLiveData.setValue(null);
                    }
                });
    }

    // Method for getting the Live from the Service

    public LiveData<DnsModel> getLiveDataValue() {
        return dnsModelLiveData;
    }
}
