# EdainMobile

EdainMobile provides a set of tools to measure network performance, network speed and web page performance from an Android device. The three primary modules are Ping, Speed Test and Page Load.

## Ping

The Ping module sends a specified amount of packets to a chosen network host. Results are aggregated in a file (.csv), which may be cleared and re-written using the Clear button. The resulting .csv file (ping.csv) may be exported from the app via Google Drive using the corresponding Export button.

## Speed Test

The Speed Test module runs and collects results from speedtest.net, testing the speed and performance of the device's internet connection. Tests are ran at user-defined intervals (minutes), where class duration is usually 75 mins and testing interval is
typically every 15 minutes. Results are aggregated in a common .csv file (speedtest.csv) and may be cleared or exported via Google Drive using the corresponding Clear and Export buttons.

https://www.speedtest.net/

## Page Load

The Page Load module utilizes WebPageTest.org to measure and analyze the performance of web pages. Tests are performed one domain at a time, where test time is limited to a maximum user-defined Timeout Seconds. County, Location and Provider inputs are also required and are stored in the file name of the test results file (.csv). Test result files are stored in the devices Download folder.

https://sites.google.com/a/webpagetest.org/docs/
