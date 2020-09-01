/*
The code given at https://github.com/olivierg13/TraceroutePing was used as a reference
 */

package com.example.ping;

import java.io.Serializable;

public class TracerouteContainer implements Serializable {

	private String hostname;
	private String URL;
	private String ip;
	private boolean isSuccessful;

	public TracerouteContainer(String hostname, String URL, String ip, boolean isSuccessful) {
		this.hostname = hostname;
		this.URL = URL;
		this.ip = ip;
		this.isSuccessful = isSuccessful;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	@Override
	public String toString() {
		return "Traceroute : \nHostname : " + hostname + "\nip : " + ip;
	}

}
