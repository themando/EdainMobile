package com.example.ping.Wifi_Network_Info.model;

import com.google.gson.annotations.SerializedName;

// Model for Parsing the JSON Response

public class WifiDataModel {
    @SerializedName("org")
    String org;

    @SerializedName("company")
    Company company;

    @SerializedName("country")
    String country;

    public String getOrg() {
        return org;
    }

    public Company getCompany() {
        return company;
    }

    public String getCountry() {
        return country;
    }
}
