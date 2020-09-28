import time, datetime, csv, sys
from datetime import datetime
from selenium import webdriver
import os.path

top1m_path = "/data/user/0/com.example.ping/files/top-1m.csv"
save_path = "/data/user/0/com.example.ping/files/"
save_csvfile = os.path.join(save_path, 'pageload.csv')

driver = None
log = None
PATH_TO_CHROME_DRIVER = "/data/user/0/com.example.ping/files/chromedriver"
PAGE_TIMEOUT_SEC = None


def initChrome():
    global driver
    deviceId = None
    chrome_options = webdriver.ChromeOptions()
    chrome_options.add_argument("--incognito")
    chrome_options.add_experimental_option('androidPackage', 'com.android.chrome')
    driver = webdriver.Chrome(PATH_TO_CHROME_DRIVER, options=chrome_options)
    driver.set_page_load_timeout(PAGE_TIMEOUT_SEC)


def loadPage(url, utc, county, location, provider, iteration):
    global driver, log
    initChrome()

    try:
        sys_time = datetime.now().timestamp()
        driver.get(url)
        timing = driver.execute_script("return window.performance.timing")
    except:
        print("Timeout")
        timing = {"timeout": True}
    driver.quit()

    line = "%s,%s,%s,%s,%s,%f,%s,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%i,%s\n" % (
        utc,
        county,
        location,
        provider,
        iteration,
        sys_time,
        url,
        timing.get('connectEnd', 0),
        timing.get('connectStart', 0),
        timing.get('domComplete', 0),
        timing.get('domContentLoadedEventEnd', 0),
        timing.get('domContentLoadedEventStart', 0),
        timing.get('domInteractive', 0),
        timing.get('domLoading', 0),
        timing.get('domainLookupEnd', 0),
        timing.get('domainLookupStart', 0),
        timing.get('fetchStart', 0),
        timing.get('loadEventEnd', 0),
        timing.get('loadEventStart', 0),
        timing.get('navigationStart', 0),
        timing.get('requestStart', 0),
        timing.get('responseEnd', 0),
        timing.get('responseStart', 0),
        timing.get('secureConnectionStart', 0),
        timing.get('redirectEnd', 0),
        timing.get('redirectStart', 0),
        timing.get('unloadEventEnd', 0),
        timing.get('unloadEventStart', 0),
        timing.get('timeout', False)
    )

    log.write(line)
    log.flush()


def main(timeout, num_urls, utc, county, location, provider, iteration):
    global PAGE_TIMEOUT_SEC
    PAGE_TIMEOUT_SEC = timeout

    log = open(save_csvfile, 'a')
    log.write('utc,county,location,provider,iteration,systemTime,url,connectEnd,connectStart,domComplete,domContentLoadedEventEnd,domContentLoadedEventStart,domInteractive,domLoading,domainLookupEnd,domainLookupStart,fetchStart,loadEventEnd,loadEventStart,navigationStart,requestStart,responseEnd,responseStart,secureConnectionStart,redirectEnd,redirectStart,unloadEventEnd,unloadEventStart,timeout\n')

    testNum = 0
    with open(top1m_path, 'r') as csvfile:
        readCSV = csv.reader(csvfile, delimiter=',')
        for row in readCSV:
            url = "http://"+row[1]
            loadPage(url, utc, county, location, provider, iteration)
            testNum += 1
            if testNum >= num_urls :
                break

    log.close()