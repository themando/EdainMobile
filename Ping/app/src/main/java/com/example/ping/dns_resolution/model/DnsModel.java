package com.example.ping.dns_resolution.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// Model for Parsing the JSON Response

public class DnsModel {
    @SerializedName(value = "Answer", alternate={"Authority"})
    private List<DnsRecordData> recordData;


    public DnsModel(List<DnsRecordData> recordData) {
        this.recordData = recordData;
    }

    public List<DnsRecordData> getRecordData() {
        return recordData;
    }
}

