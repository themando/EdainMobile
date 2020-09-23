package com.example.ping.Wifi_Network_Info.model;

import com.google.gson.annotations.SerializedName;

// Model for Parsing the Company from Json Response

public class Company {
    @SerializedName("domain")
    String domain;

    public String getDomain() {
        return domain;
    }
}
