# EdainMobile

EdainMobile provides a set of tools to measure network performance, network speed and web page performance from an Android device. The three primary modules are Ping, Speed Test and Page Load.

## Ping

The Ping module sends a specified amount of packets to a chosen network host. Results are aggregated in a file (.csv), which may be cleared and re-written using the Clear button. The resulting .csv file (ping.csv) may be exported from the app via Google Drive using the corresponding Export button. 

The following statistics are collected in ping.csv: *Timestamp, Server, Packets, Loss, Min, Avg, Max, Stddev*

## Traceroute

We store the **IP addresses, hostnames and latencies** of the servers where the packets are sent and received successfully for the top *N* (b/w 1-1000) *Tranco Sites*. These are also saved to a csv file which can be exported using the export button. 

## Speed Test

The Speed Test module runs and collects results from speedtest.net, testing the speed and performance of the device's internet connection. Tests are run at user-defined intervals (minutes), where class duration is usually 75 minutes and testing interval is typically every 15 minutes. Results are aggregated in a common .csv file (speedtest.csv) and may be cleared or exported via Google Drive using the corresponding Clear and Export buttons.

https://www.speedtest.net/

The Speed Test module uses an unlicensed version of Chaquopy (Python SDK for Android) to run the speedtest.py and run_speedtest.py scripts. The unlicensed SDK is fully functional, but apps built with it will display a notification on startup, and will only run for 5 minutes at a time. To remove these restrictions, a license is required.

https://chaquo.com/chaquopy/license/

## Page Load

The Page Load module utilizes WebPageTest.org to measure and analyze the performance of web pages. Tests are performed one domain at a time, where test time is limited to a maximum user-defined Timeout Seconds. County, Location and Provider inputs are also required and are stored in the file name of the test results file (.csv). Test result files are stored in the device's Download folder.

https://sites.google.com/a/webpagetest.org/docs/

The Page Load module utilizes a WebPageTest API key to perform tests. The API key is limited to 200 page loads per day. If more testing is necessary, running a private instance of WebPageTest may be considered:

https://sites.google.com/a/webpagetest.org/docs/private-instances
