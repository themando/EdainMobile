package com.example.ping.Wifi_Network_Info.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ping.Wifi_Network_Info.model.WifiDataModel;
import com.example.ping.Wifi_Network_Info.service.WifiService;

// Class for interacting between the view and service

public class WifiViewModel extends ViewModel {
    WifiService wifiService;
    LiveData<WifiDataModel> liveData;

    // Method for instantiating the variables

    public void init() {
        wifiService = new WifiService();
        liveData = wifiService.getWifiLiveData();
    }

    // Method invoked by view for fetching the data

    public void fetchLiveData() {
        wifiService.fetchData();
    }

    // Method for getting the data

    public LiveData<WifiDataModel> getLiveData() {
        return liveData;
    }
}
