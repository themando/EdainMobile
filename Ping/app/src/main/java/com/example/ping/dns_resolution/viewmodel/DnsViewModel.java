package com.example.ping.dns_resolution.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ping.dns_resolution.model.DnsModel;
import com.example.ping.dns_resolution.service.DnsService;

// Class for interacting between the view and service

public class DnsViewModel extends ViewModel {
    private DnsService dnsService;
    private LiveData<DnsModel> liveData;

    // Method for instantiating the variables

    public void init() {
        dnsService = new DnsService();
        liveData = dnsService.getLiveDataValue();
    }

    // Method invoked by view for fetching the data

    public void fetchData(String name, String type) {
        dnsService.fetchLiveData(name, type);
    }

    // Method for getting the data

    public LiveData<DnsModel> getData() {
        return liveData;
    }
}


