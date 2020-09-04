package com.example.ping.dns_resolution.model;

import com.google.gson.annotations.SerializedName;

// Model for Parsing the Record Data from JSON Response

public class DnsRecordData {
    @SerializedName("name")
    private String name;

    @SerializedName("TTL")
    private int TTL;

    @SerializedName("data")
    private String data;

    public DnsRecordData(String name, int ttl, String data) {
        this.name = name;
        TTL = ttl;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public int getTTL() {
        return TTL;
    }

    public String getData() {
        return data;
    }
}
