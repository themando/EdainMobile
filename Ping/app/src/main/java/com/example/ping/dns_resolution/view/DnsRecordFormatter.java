package com.example.ping.dns_resolution.view;

import android.content.Context;

import com.example.ping.dns_resolution.model.DnsModel;
import com.example.ping.dns_resolution.model.DnsRecordData;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

// Class Contain Methods for formatting the data for different records
public class DnsRecordFormatter {
    Context context;
    public DnsRecordFormatter(Context context) {
        this.context = context;
    }

    public String aRecord(DnsModel model) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < (model != null ? model.getRecordData().size() : 0); i++) {
            DnsRecordData recordData = model.getRecordData().get(i);
            stringBuilder.append(recordData.getName()).append("/").append(recordData.getData()).append("\n");
            stringBuilder.append(getIPAddress(recordData.getName())).append("\n\n");
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public String soaRecord(DnsModel model) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < (model != null ? model.getRecordData().size() : 0); i++) {
            DnsRecordData recordData = model.getRecordData().get(i);
            String[] val = recordData.getData().split(" ");
            stringBuilder.append("Primary Server       ").append(val[0]).append("\n");
            stringBuilder.append("Administrator        ").append(val[1]).append("\n");
            stringBuilder.append("Serial Number        ").append(val[2]).append("\n");
            stringBuilder.append("Refresh              ").append(val[3]).append("\n");
            stringBuilder.append("Retry                ").append(val[4]).append("\n");
            stringBuilder.append("Expire               ").append(val[5]).append("\n");
            stringBuilder.append("TTL                  ").append(recordData.getTTL()).append("\n\n");
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public String mxRecord(DnsModel model) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < (model != null ? model.getRecordData().size() : 0); i++) {
            DnsRecordData recordData = model.getRecordData().get(i);
            String[] val = recordData.getData().split(" ");
            stringBuilder.append(val[1]).append("\n");
            stringBuilder.append(getIPAddress(val[1])).append("          ").append("Preferences:").append(val[0]).append("\n\n");
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public String nsRecord(DnsModel model) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < (model != null ? model.getRecordData().size() : 0); i++) {
            DnsRecordData recordData = model.getRecordData().get(i);
            String val = recordData.getData();
            stringBuilder.append(val).append("\n");
            stringBuilder.append(getIPAddress(val)).append("\n\n");
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public String txtRecord(DnsModel model) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < (model != null & model.getRecordData() != null ? model.getRecordData().size() : 0); i++) {
            DnsRecordData recordData = model.getRecordData().get(i);
            String val = recordData.getData();
            stringBuilder.append(val).append("\n\n");
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public ArrayList<String> exportARecord(DnsModel model) {
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < (model != null & model.getRecordData() != null ? model.getRecordData().size() : 0); i++) {
            DnsRecordData recordData = model.getRecordData().get(i);
            String val = recordData.getData();
            val = val.replaceAll("\\s", ",");
            val += "\n";
            out.add(val);
        }
        return out;
    }

    public ArrayList<String> exportSOARecord(DnsModel model) {
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < (model != null ? model.getRecordData().size() : 0); i++) {
            DnsRecordData recordData = model.getRecordData().get(i);
            String[] val = recordData.getData().split(" ");
            String val1 = val[0] + "," + val[1] + "," + val[2] + "," + val[3] + "," + val[4] + "," + val[5];
            val1 += "," + recordData.getTTL() + "\n";
            out.add(val1);
        }
        return out;
    }

    public ArrayList<String> exportMXRecord(DnsModel model) {
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < (model != null ? model.getRecordData().size() : 0); i++) {
            DnsRecordData recordData = model.getRecordData().get(i);
            String[] val = recordData.getData().split(" ");
            String val1 = getIPAddress(val[1]) + " " + recordData.getData();
            val1 = val1.replaceAll("\\s", ",");
            val1 += "\n";
            out.add(val1);
        }
        return out;
    }

    public ArrayList<String> exportNSRecord(DnsModel model) {
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < (model != null ? model.getRecordData().size() : 0); i++) {
            DnsRecordData recordData = model.getRecordData().get(i);
            String val = recordData.getData();
            val = val + " " + getIPAddress(val);
            val = val.replaceAll("\\s", ",");
            val += "\n";
            out.add(val);
        }
        return out;
    }

    public ArrayList<String> exportTXTRecord(DnsModel model) {
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < (model != null ? model.getRecordData().size() : 0); i++) {
            DnsRecordData recordData = model.getRecordData().get(i);
            String val = recordData.getData();
            val = val.replaceAll("\\s", ",");
            val += "\n";
            out.add(val);
        }
        return out;
    }

    public String getIPAddress(String url) {
        StringBuilder stringBuilder = new StringBuilder();

        GetIpAddress getIpAddress = new GetIpAddress(context);
        getIpAddress.getDnsServer();
        try {
            getIpAddress.getDnsStuff(url);
            Thread.sleep(1000);
            final HashMap<String, Object> hashMap = getIpAddress.getHashMap(url);
            ArrayList<String> ipv4 = (ArrayList<String>) hashMap.get("resolved_ipv4");
            ArrayList<String> ipv6 = (ArrayList<String>) hashMap.get("resolved_ipv6");

        } catch (SocketException | InterruptedException | UnknownHostException e) {
            e.printStackTrace();
            stringBuilder.append("NaN");
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
