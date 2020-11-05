#!/usr/bin/env python3
# Use this script to run speed test at user-defined intervals.
# Class duration is usually 75 mins and testing interval is
# typically every 15 minutes (can be changed through arguments).
# Please note: Both class duration and interval are in *minutes*.

import os.path
import speedtest as speed_script

import csv
import json
import time
from datetime import datetime
from subprocess import PIPE, run
from sys import argv, exit, executable


save_path = "/data/user/0/com.example.ping/files/"
save_csvfile = os.path.join(save_path, 'speedtest.csv')
#save_csvfile = os.path.join(save_path, datetime.utcnow().strftime('%y_%m_%d_%H-%M-%S') + '_speedtest.csv')

# CSV output headers
headers = ['timestamp', 'download', 'upload', 'ping', 'bytes_sent', 'bytes_received',
           'server_url', 'server_lat', 'server_lon', 'server_name', 'server_country',
           'server_sponsor', 'server_id', 'server_host', 'server_latency', 'client_ip',
           'client_lat', 'client_lon', 'client_isp', 'client_isprating', 'client_country']


# Speedtest function (requires 'speedtest.py' in the same directory)
def speedtest():
    res = speed_script.main()
    json_data = json.loads(res)
    return json_data


# Parsing the JSON object to write to output
def json_parser(data):
    log_metrics = dict.fromkeys(headers)
    for key in log_metrics:
        if 'server' in key or 'client' in key:
            arg = key.split("_")
            log_metrics[key] = data[arg[0]][arg[1]]
        else:
            log_metrics[key] = data[key]

    return list(log_metrics.values())


# Append to output CSV
def write_csv(write_data):
    with open(save_csvfile, 'a') as outfile:
        writer = csv.writer(outfile)
        writer.writerow(write_data)


# Minutes to seconds converter
def sleep_minutes(minutes):
    time.sleep(minutes * 60)


def main(class_duration, interval):
    # New bare file with headers
    with open(save_csvfile, 'w') as f:
        writer = csv.writer(f)
        writer.writerow(headers)
        f.close()

    json_data = speedtest()
    row_metrics = json_parser(json_data)
    write_csv(row_metrics)

    return json_data