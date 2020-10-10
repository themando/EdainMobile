package com.example.ping.dns_resolution.service;

import com.example.ping.dns_resolution.model.DnsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface DnsServiceApiInterface {

    @Headers("Content-Type: application/json")
    @GET("/resolve?do=1")
    Call<DnsModel> getModel(
            @Query("name") String name,
            @Query("type") String type
    );
}
