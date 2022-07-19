# Edain: Comprehensive QoE and QoE monitoring suite

Edain provides a set of tools to measure network performance, network speed and web page performance from an Android device. The primary modules are throughput, latency, page load time and YouTube QoE.

If you implement Edain in your project, please use the following citation:

`
Adarsh, Vivek, Michael Nekrasov, Udit Paul, and Elizabeth M. Belding. "Estimation of congestion from cellular walled gardens using passive measurements." IEEE Transactions on Mobile Computing (2021).
`

```
@article{adarsh2021TMC,
  title={Estimation of congestion from cellular walled gardens using passive measurements},
  author={Adarsh, Vivek and Nekrasov, Michael and Paul, Udit and Belding, Elizabeth M},
  journal={IEEE Transactions on Mobile Computing},
  year={2021},
  publisher={IEEE}
}
```

## Latency

The Latency feature iterates through TRANCO Top *N* (b/w 1-1000) sites, pinging each site and returning their latency measurements.
Results are aggregated in a file (.csv), which may be cleared and re-written using the Clear button. The resulting .csv file (latency.csv) may be exported from the app via Google Drive using the corresponding Export button. 

We're collecting the following statistics: *Timestamp, Server, Packets, Loss, Min, Avg, Max, Stddev*
These results are also being stored in Firestore.

## Ping

The Ping module sends a specified amount of packets to a chosen network host. Results are aggregated in a file (.csv), which may be cleared and re-written using the Clear button. The resulting .csv file (ping.csv) may be exported from the app via Google Drive using the corresponding Export button. 

The following statistics are collected in ping.csv: *Timestamp, Server, Packets, Loss, Min, Avg, Max, Stddev*

## Traceroute

We store the **IP addresses, hostnames and latencies** of the servers where the packets are sent and received successfully for the top *N* (b/w 1-1000) *Tranco Sites*. These are also saved to a csv file which can be exported using the export button. 

## DNS Resolution

The DNS Resolution module is based on DNS Protocol http://www.tcpipguide.com/free/t_DNSMessageHeaderandQuestionSectionFormat.htm. The module sends Datagram Packets to the DNS server and retrieves the `Ipv4Addresses and Ipv6Addresses` associated with a particular url. The results are stored in firestore.

The module has a space to enter the number of sites to be fetched. The sites are stored in an array based on Top tranco sites. The number of sites to be fetched has a limit of 1000.

## Speed Test

The Speed Test module runs and collects results from speedtest.net, testing the speed and performance of the device's internet connection. Tests are run at user-defined intervals (minutes), where class duration is usually 75 minutes and testing interval is typically every 15 minutes. Results are aggregated in a common .csv file (speedtest.csv) and may be cleared or exported via Google Drive using the corresponding Clear and Export buttons.
The results from first iteration are stored in Firestore.
https://www.speedtest.net/

The Speed Test module uses an unlicensed version of Chaquopy (Python SDK for Android) to run the speedtest.py and run_speedtest.py scripts. The unlicensed SDK is fully functional, but apps built with it will display a notification on startup, and will only run for 5 minutes at a time. To remove these restrictions, a license is required.

https://chaquo.com/chaquopy/license/

## Page Load

The Page Load module is based on the webview component (based on the chromium framework) in android. The webview loads different urls and runs javascript in the console of webview to get various parameters of page load timings to measure and analyze the performance of web pages. The results are stored in firetore.

The module has a space to enter the number of urls against which the measurements are to be run. The sites are stored in an array based on Top Tranco Sites. The number of sites to be fetched has a limit of 1000.

## Youtube Feature

The Youtube Feature module is based on the Iframe API https://developers.google.com/youtube/iframe_api_reference. It is aimed at measuring the quality of network by measuring `downloaded_video, video_played, buffer_size, curr_resolution, playback_mode` of the embedded youtube video at different time instants. We have used the android library based on the Iframe API https://github.com/PierfrancescoSoffritti/android-youtube-player.

We have embedded 10 videos in the module each of which has a button for selecting the time interval during which we need to run the measurements. There is also a button to export a text file containing the parameters: `video_id,timestamp,downloaded_video, video_played, buffer_size, curr_resolution, playback_mode`.


