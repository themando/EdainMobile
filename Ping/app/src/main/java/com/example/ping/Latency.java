package com.example.ping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
//import com.example.ping.PingTrancoSites;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import static java.lang.Integer.parseInt;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.example.ping.Wifi_Network_Info.model.WifiDataModel;
import com.example.ping.Wifi_Network_Info.viewmodel.WifiViewModel;

public class Latency extends AppCompatActivity {
    private static final String FILE_NAME = "ping.csv";
    public String Header;
    static WifiViewModel model;
   // static int doc_ser;
    static String wifi_name = "Time Limit Exceeded to get Wifi Network from API";
    String countryCode = "No Country Code";
    TableLayout tl;
    RelativeLayout tableLayout;
    TableRow tr;
    PingTrancoSites asyncPing = new PingTrancoSites();
    TextView newRow,anotherRow;
    ProgressBar progressBar;


    /**
     * Adding Firestore services:
     */ FirebaseFirestore db = FirebaseFirestore.getInstance();


   static String[] sites = {
            "google.com",
            "facebook.com",
            "youtube.com",
            "microsoft.com",
            "twitter.com",
            "tmall.com",
            "instagram.com",
            "netflix.com",
            "linkedin.com",
            "baidu.com",
            "qq.com",
            "wikipedia.org",
            "apple.com",
            "live.com",
            "sohu.com",
            "yahoo.com",
            "amazon.com",
            "doubleclick.net",
            "googletagmanager.com",
            "taobao.com",
            "adobe.com",
            "pinterest.com",
            "youtu.be",
            "360.cn",
            "vimeo.com",
            "jd.com",
            "reddit.com",
            "office.com",
            "wordpress.com",
            "weibo.com",
            "bing.com",
            "zoom.us",
            "sina.com.cn",
            "goo.gl",
            "github.com",
            "microsoftonline.com",
            "amazonaws.com",
            "bit.ly",
            "blogspot.com",
            "vk.com",
            "wordpress.org",
            "xinhuanet.com",
            "fbcdn.net",
            "tumblr.com",
            "mozilla.org",
            "godaddy.com",
            "msn.com",
            "google-analytics.com",
            "nytimes.com",
            "skype.com",
            "flickr.com",
            "okezone.com",
            "whatsapp.com",
            "gravatar.com",
            "soundcloud.com",
            "dropbox.com",
            "europa.eu",
            "alipay.com",
            "myshopify.com",
            "nih.gov",
            "csdn.net",
            "yahoo.co.jp",
            "cnn.com",
            "t.co",
            "ebay.com",
            "apache.org",
            "twitch.tv",
            "office365.com",
            "w3.org",
            "macromedia.com",
            "theguardian.com",
            "googlevideo.com",
            "medium.com",
            "spotify.com",
            "google.com.hk",
            "bongacams.com",
            "naver.com",
            "imdb.com",
            "sourceforge.net",
            "bbc.co.uk",
            "forbes.com",
            "zhanqi.tv",
            "paypal.com",
            "aliexpress.com",
            "archive.org",
            "cloudflare.com",
            "bbc.com",
            "googleadservices.com",
            "googlesyndication.com",
            "github.io",
            "google.co.in",
            "yandex.ru",
            "amazon.in",
            "weebly.com",
            "stackoverflow.com",
            "china.com.cn",
            "digicert.com",
            "virginmedia.com",
            "tianya.cn",
            "amazon.co.jp",
            "creativecommons.org",
            "who.int",
            "issuu.com",
            "windows.net",
            "washingtonpost.com",
            "imgur.com",
            "tribunnews.com",
            "etsy.com",
            "livejasmin.com",
            "chaturbate.com",
            "oracle.com",
            "slideshare.net",
            "mail.ru",
            "amazon.co.uk",
            "reuters.com",
            "php.net",
            "wix.com",
            "cdc.gov",
            "wsj.com",
            "icloud.com",
            "app-measurement.com",
            "pornhub.com",
            "google.de",
            "tinyurl.com",
            "wikimedia.org",
            "1688.com",
            "google.cn",
            "alibaba.com",
            "aparat.com",
            "wp.com",
            "bloomberg.com",
            "google.co.jp",
            "businessinsider.com",
            "instructure.com",
            "cnet.com",
            "yy.com",
            "sciencedirect.com",
            "sogou.com",
            "opera.com",
            "youtube-nocookie.com",
            "163.com",
            "google.com.br",
            "harvard.edu",
            "gnu.org",
            "ok.ru",
            "mit.edu",
            "outlook.com",
            "dailymail.co.uk",
            "ibm.com",
            "booking.com",
            "espn.com",
            "17ok.com",
            "amazon.de",
            "researchgate.net",
            "go.com",
            "stanford.edu",
            "forms.gle",
            "samsung.com",
            "google.co.uk",
            "bitly.com",
            "rakuten.co.jp",
            "list-manage.com",
            "hp.com",
            "blogger.com",
            "windows.com",
            "wiley.com",
            "telegraph.co.uk",
            "aol.com",
            "usatoday.com",
            "msedge.net",
            "ntp.org",
            "jrj.com.cn",
            "mama.cn",
            "fb.com",
            "cnbc.com",
            "surveymonkey.com",
            "nginx.org",
            "cpanel.net",
            "fandom.com",
            "eventbrite.com",
            "kompas.com",
            "indiatimes.com",
            "myspace.com",
            "nasa.gov",
            "dailymotion.com",
            "canva.com",
            "huffingtonpost.com",
            "behance.net",
            "nature.com",
            "google.fr",
            "google.es",
            "aliyun.com",
            "youku.com",
            "xvideos.com",
            "ettoday.net",
            "hao123.com",
            "addthis.com",
            "udemy.com",
            "time.com",
            "pixnet.net",
            "walmart.com",
            "npr.org",
            "indeed.com",
            "un.org",
            "roblox.com",
            "hicloud.com",
            "google.it",
            "freepik.com",
            "springer.com",
            "t.me",
            "foxnews.com",
            "google.ru",
            "wendyssubway.com",
            "ted.com",
            "cpanel.com",
            "grid.id",
            "sharepoint.com",
            "ca.gov",
            "flipkart.com",
            "aaplimg.com",
            "wired.com",
            "bilibili.com",
            "mysql.com",
            "cnblogs.com",
            "yelp.com",
            "nginx.com",
            "hugedomains.com",
            "scribd.com",
            "detik.com",
            "salesforce.com",
            "scorecardresearch.com",
            "doi.org",
            "zendesk.com",
            "opendns.com",
            "goodreads.com",
            "force.com",
            "gmail.com",
            "debian.org",
            "independent.co.uk",
            "intel.com",
            "wikihow.com",
            "free.fr",
            "tripadvisor.com",
            "shutterstock.com",
            "wetransfer.com",
            "themeforest.net",
            "beian.gov.cn",
            "google.ca",
            "android.com",
            "techcrunch.com",
            "squarespace.com",
            "daum.net",
            "google.com.sg",
            "mailchimp.com",
            "speedtest.net",
            "berkeley.edu",
            "mediafire.com",
            "stackexchange.com",
            "chase.com",
            "addtoany.com",
            "okta.com",
            "livejournal.com",
            "line.me",
            "unsplash.com",
            "zillow.com",
            "tokopedia.com",
            "taboola.com",
            "latimes.com",
            "craigslist.org",
            "weather.com",
            "ikea.com",
            "tiktok.com",
            "statcounter.com",
            "google.com.tw",
            "crashlytics.com",
            "grammarly.com",
            "healthline.com",
            "zoho.com",
            "xhamster.com",
            "duckduckgo.com",
            "webmd.com",
            "onlinesbi.com",
            "quora.com",
            "amzn.to",
            "cisco.com",
            "kickstarter.com",
            "theverge.com",
            "adsrvr.org",
            "webex.com",
            "6.cn",
            "jimdo.com",
            "digg.com",
            "nationalgeographic.com",
            "deviantart.com",
            "ft.com",
            "tradingview.com",
            "amazon.ca",
            "ietf.org",
            "sitemaps.org",
            "cornell.edu",
            "pixabay.com",
            "w3schools.com",
            "loc.gov",
            "theatlantic.com",
            "zhihu.com",
            "buzzfeed.com",
            "shopify.com",
            "washington.edu",
            "google.com.mx",
            "appsflyer.com",
            "dell.com",
            "youm7.com",
            "giphy.com",
            "eastday.com",
            "cbsnews.com",
            "about.com",
            "rubiconproject.com",
            "arnebrachhold.de",
            "slack.com",
            "wa.me",
            "telegram.org",
            "tandfonline.com",
            "akismet.com",
            "liputan6.com",
            "stumbleupon.com",
            "savefrom.net",
            "google.com.vn",
            "criteo.com",
            "academia.edu",
            "coursera.org",
            "symantec.com",
            "cambridge.org",
            "marriott.com",
            "rednet.cn",
            "marketwatch.com",
            "digikala.com",
            "disqus.com",
            "whatsapp.net",
            "padlet.com",
            "investopedia.com",
            "launchpad.net",
            "primevideo.com",
            "typepad.com",
            "bestbuy.com",
            "pubmatic.com",
            "iqiyi.com",
            "uol.com.br",
            "box.com",
            "princeton.edu",
            "usnews.com",
            "avito.ru",
            "tiktokv.com",
            "google.com.tr",
            "ampproject.org",
            "ilovepdf.com",
            "discord.com",
            "pki.goog",
            "huffpost.com",
            "feedburner.com",
            "msftconnecttest.com",
            "fc2.com",
            "mailchi.mp",
            "webs.com",
            "prnewswire.com",
            "advertising.com",
            "mashable.com",
            "hola.org",
            "economist.com",
            "pinimg.com",
            "evernote.com",
            "alicdn.com",
            "noaa.gov",
            "bandcamp.com",
            "investing.com",
            "steampowered.com",
            "steamcommunity.com",
            "globo.com",
            "sciencemag.org",
            "homedepot.com",
            "worldometers.info",
            "airbnb.com",
            "hubspot.com",
            "bet9ja.com",
            "change.org",
            "nbcnews.com",
            "avast.com",
            "wellsfargo.com",
            "youronlinechoices.com",
            "ebay.de",
            "moatads.com",
            "teamviewer.com",
            "pbs.org",
            "plesk.com",
            "nflximg.com",
            "trello.com",
            "casalemedia.com",
            "arcgis.com",
            "whitehouse.gov",
            "mozilla.com",
            "usda.gov",
            "lazada.sg",
            "constantcontact.com",
            "jquery.com",
            "accuweather.com",
            "hulu.com",
            "target.com",
            "oup.com",
            "columbia.edu",
            "eepurl.com",
            "51.la",
            "engadget.com",
            "tistory.com",
            "umich.edu",
            "ebay.co.uk",
            "nypost.com",
            "outbrain.com",
            "state.gov",
            "aboutads.info",
            "unesco.org",
            "rt.com",
            "amazon.fr",
            "ups.com",
            "tripod.com",
            "setn.com",
            "google.co.th",
            "huawei.com",
            "breitbart.com",
            "google.com.sa",
            "vice.com",
            "patreon.com",
            "psu.edu",
            "varzesh3.com",
            "geocities.com",
            "zdnet.com",
            "gofundme.com",
            "sagepub.com",
            "yale.edu",
            "bukalapak.com",
            "hbr.org",
            "meetup.com",
            "epa.gov",
            "nvidia.com",
            "guardian.co.uk",
            "google.com.ar",
            "abc.net.au",
            "trustpilot.com",
            "mayoclinic.org",
            "sun.com",
            "britannica.com",
            "google.com.eg",
            "metropoles.com",
            "allaboutcookies.org",
            "patch.com",
            "statista.com",
            "cbc.ca",
            "upenn.edu",
            "smallpdf.com",
            "bidswitch.net",
            "nike.com",
            "aliexpress.ru",
            "gotowebinar.com",
            "sciencedaily.com",
            "elsevier.com",
            "fiverr.com",
            "redhat.com",
            "photobucket.com",
            "networkadvertising.org",
            "google.pl",
            "vox.com",
            "sindonews.com",
            "getpocket.com",
            "google.co.id",
            "amazon.it",
            "amazon.es",
            "quizlet.com",
            "momoshop.com.tw",
            "ask.com",
            "business.site",
            "irs.gov",
            "dribbble.com",
            "suara.com",
            "redd.it",
            "usps.com",
            "wayfair.com",
            "vkontakte.ru",
            "hdfcbank.com",
            "xnxx.com",
            "hootsuite.com",
            "worldbank.org",
            "gizmodo.com",
            "newyorker.com",
            "psychologytoday.com",
            "example.com",
            "everesttech.net",
            "talktalk.co.uk",
            "google.com.au",
            "chinadaily.com.cn",
            "iso.org",
            "ieee.org",
            "fedex.com",
            "namnak.com",
            "fastcompany.com",
            "businesswire.com",
            "merriam-webster.com",
            "51sole.com",
            "khanacademy.org",
            "inc.com",
            "mathtag.com",
            "umn.edu",
            "oreilly.com",
            "ox.ac.uk",
            "wpengine.com",
            "dw.com",
            "blackboard.com",
            "bluekai.com",
            "softonic.com",
            "amazon.cn",
            "fortune.com",
            "cnzz.com",
            "wisc.edu",
            "chouftv.ma",
            "ltn.com.tw",
            "messenger.com",
            "snapchat.com",
            "letsencrypt.org",
            "ndtv.com",
            "wikia.com",
            "plos.org",
            "azure.com",
            "att.com",
            "yimg.com",
            "typeform.com",
            "entrepreneur.com",
            "wiktionary.org",
            "jhu.edu",
            "nist.gov",
            "uci.edu",
            "jianshu.com",
            "us.com",
            "iqoption.com",
            "ucla.edu",
            "gmw.cn",
            "scientificamerican.com",
            "chicagotribune.com",
            "ameblo.jp",
            "theconversation.com",
            "pikiran-rakyat.com",
            "elegantthemes.com",
            "deloitte.com",
            "utexas.edu",
            "feedly.com",
            "playstation.com",
            "heavy.com",
            "spiegel.de",
            "sfgate.com",
            "python.org",
            "comodoca.com",
            "rambler.ru",
            "cmu.edu",
            "asos.com",
            "zaloapp.com",
            "adsafeprotected.com",
            "deepl.com",
            "canada.ca",
            "y2mate.com",
            "cam.ac.uk",
            "slate.com",
            "epicgames.com",
            "realtor.com",
            "uk.com",
            "newrelic.com",
            "ubuntu.com",
            "pexels.com",
            "newsweek.com",
            "360.com",
            "arxiv.org",
            "indiegogo.com",
            "lenovo.com",
            "discordapp.com",
            "elpais.com",
            "xfinity.com",
            "zerodha.com",
            "qualtrics.com",
            "ed.gov",
            "telewebion.com",
            "intuit.com",
            "bmj.com",
            "timeanddate.com",
            "verisign.com",
            "eikegolehem.com",
            "hotstar.com",
            "manoramaonline.com",
            "hilton.com",
            "gosuslugi.ru",
            "nps.gov",
            "uchicago.edu",
            "over-blog.com",
            "afternic.com",
            "bootstrapcdn.com",
            "hotjar.com",
            "oecd.org",
            "bbb.org",
            "mirror.co.uk",
            "asus.com",
            "aboutcookies.org",
            "howstuffworks.com",
            "zol.com.cn",
            "fb.me",
            "biomedcentral.com",
            "uiuc.edu",
            "qz.com",
            "weforum.org",
            "shaparak.ir",
            "arstechnica.com",
            "nicovideo.jp",
            "mgid.com",
            "tencent.com",
            "thesun.co.uk",
            "sberbank.ru",
            "purdue.edu",
            "criteo.net",
            "bet365.com",
            "medicalnewstoday.com",
            "cbslocal.com",
            "chron.com",
            "pcmag.com",
            "turn.com",
            "visualstudio.com",
            "ladbible.com",
            "unity3d.com",
            "tapad.com",
            "norton.com",
            "parallels.com",
            "mercadolivre.com.br",
            "nba.com",
            "douban.com",
            "altervista.org",
            "icicibank.com",
            "glassdoor.com",
            "op.gg",
            "merdeka.com",
            "appcenter.ms",
            "bizjournals.com",
            "capitalone.com",
            "ny.gov",
            "kakao.com",
            "dictionary.com",
            "gamepedia.com",
            "ign.com",
            "kumparan.com",
            "idntimes.com",
            "toutiao.com",
            "apa.org",
            "crwdcntrl.net",
            "americanexpress.com",
            "si.edu",
            "ftc.gov",
            "techradar.com",
            "autodesk.com",
            "blogspot.co.uk",
            "thesaurus.com",
            "mercadolibre.com.ar",
            "fao.org",
            "gstatic.com",
            "digitaltrends.com",
            "zend.com",
            "mlb.com",
            "geeksforgeeks.org",
            "namu.wiki",
            "chess.com",
            "netscape.com",
            "bankofamerica.com",
            "umeng.com",
            "fbsbx.com",
            "thepiratebay.org",
            "politico.com",
            "cctv.com",
            "patria.org.ve",
            "nyu.edu",
            "sahibinden.com",
            "icio.us",
            "news.com.au",
            "fastly.net",
            "smh.com.au",
            "istockphoto.com",
            "orange.fr",
            "abs-cbn.com",
            "ow.ly",
            "reverso.net",
            "nydailynews.com",
            "sectigo.com",
            "inquirer.net",
            "usc.edu",
            "openstreetmap.org",
            "cnnic.cn",
            "appspot.com",
            "bitnami.com",
            "spotxchange.com",
            "exelator.com",
            "barnesandnoble.com",
            "genius.com",
            "google.co.kr",
            "telegram.me",
            "ning.com",
            "joomla.org",
            "moneycontrol.com",
            "upwork.com",
            "prezi.com",
            "livescience.com",
            "fontawesome.com",
            "dropcatch.com",
            "uber.com",
            "house.gov",
            "xing.com",
            "pnas.org",
            "sakura.ne.jp",
            "mercadolibre.com.mx",
            "googleblog.com",
            "dedecms.com",
            "allegro.pl",
            "duke.edu",
            "google.gr",
            "foursquare.com",
            "instructables.com",
            "3lift.com",
            "apnews.com",
            "ebay-kleinanzeigen.de",
            "venturebeat.com",
            "zalo.me",
            "kapanlagi.com",
            "census.gov",
            "marca.com",
            "aliyuncs.com",
            "enable-javascript.com",
            "acs.org",
            "vmware.com",
            "variety.com",
            "lifehacker.com",
            "verizon.com",
            "jiameng.com",
            "brilio.net",
            "themeisle.com",
            "umd.edu",
            "youdao.com",
            "jstor.org",
            "fidelity.com",
            "ria.ru",
            "usgs.gov",
            "angelfire.com",
            "madrasati.sa",
            "jpnn.com",
            "ea.com",
            "mckinsey.com",
            "pewresearch.org",
            "secureserver.net",
            "miit.gov.cn",
            "earthlink.net",
            "google.com.ua",
            "ufl.edu",
            "imageshack.us",
            "teads.tv",
            "adp.com",
            "express.co.uk",
            "aljazeera.com",
            "g.page",
            "msu.edu",
            "proiezionidiborsa.it",
            "fast.com",
            "schoology.com",
            "thefreedictionary.com",
            "about.me",
            "ucsd.edu",
            "urbandictionary.com",
            "jotform.com",
            "lijit.com",
            "mi.com",
            "wildberries.ru",
            "linktr.ee",
            "senate.gov",
            "duolingo.com",
            "doubleverify.com",
            "thehill.com",
            "adjust.com",
            "hm.com",
            "alexa.com",
            "postgresql.org",
            "mercari.com",
            "branch.io",
            "thedailybeast.com",
            "mixpanel.com",
            "9gag.com",
            "thelancet.com",
            "unc.edu",
            "rollingstone.com",
            "gartner.com",
            "northwestern.edu",
            "atlassian.net",
            "gmx.net",
            "ebc.net.tw",
            "hatena.ne.jp",
            "ouedkniss.com",
            "redbubble.com",
            "nikkei.com",
            "kaspersky.com",
            "hurriyet.com.tr",
            "discord.gg",
            "in.gr",
            "hespress.com",
            "illinois.edu",
            "focus.cn",
            "tutorialspoint.com",
            "shopee.co.id",
            "bbcollab.com",
            "leboncoin.fr",
            "wattpad.com",
            "namecheap.com",
            "vnexpress.net",
            "proofpoint.com",
            "stripe.com",
            "crunchyroll.com",
            "xiaomi.com",
            "sky.com",
            "chegg.com",
            "utoronto.ca",
            "xbox.com",
            "lemonde.fr",
            "slashdot.org",
            "thetimes.co.uk",
            "theglobeandmail.com",
            "bls.gov",
            "lowes.com",
            "gamespot.com",
            "ensonhaber.com",
            "nfl.com",
            "goo.ne.jp",
            "ebay.com.au",
            "xe.com",
            "java.com",
            "apachefriends.org",
            "albawabhnews.com",
            "withgoogle.com",
            "asu.edu",
            "smartadserver.com",
            "jamanetwork.com",
            "google.nl",
            "scmp.com",
            "privacyshield.gov",
            "yts.mx",
            "gismeteo.ru",
            "media.net",
            "gitlab.com",
            "hhs.gov",
            "newscientist.com",
            "itu.int",
            "gsmarena.com",
            "wp.me",
            "repubblica.it",
            "today.com",
            "trendyol.com",
            "siemens.com",
            "google.ro",
            "atlassian.com",
            "fool.com",
            "elbalad.news",
            "creativecdn.com",
            "qoo10.sg",
            "arizona.edu",
            "wunderground.com",
            "zippyshare.com",
            "wufoo.com",
            "automattic.com",
            "onenote.net",
            "getbootstrap.com",
            "sec.gov",
            "td.com",
            "dotomi.com",
            "divar.ir",
            "hatenablog.com",
            "w.org",
            "globalsign.com",
            "boston.com",
            "hollywoodreporter.com",
            "oppomobile.com",
            "moodle.org",
            "mitre.org",
            "people.com",
            "docker.com",
            "dcard.tw",
            "shopee.vn",
            "mzstatic.com",
            "livestream.com",
            "w55c.net",
            "mixcloud.com",
            "uspto.gov",
            "m.me",
            "mheducation.com",
            "history.com",
            "mapquest.com",
            "openssl.org",
            "dhl.com",
            "indiamart.com",
            "nejm.org",
            "googleapis.com",
            "adsymptotic.com",
            "drupal.org",
            "rediff.com",
            "newegg.com",
            "ustream.tv",
            "emofid.com",
            "colorado.edu",
            "dbs.com.sg",
            "hindustantimes.com",
            "efu.com.cn",
            "techtarget.com",
            "youporn.com",
            "freebsd.org",
            "eastmoney.com",
            "center4family.com",
            "zemanta.com",
            "biblegateway.com",
            "google.be",
            "uptodown.com",
            "disneyplus.com",
            "naver.jp",
            "mega.nz",
            "icann.org",
            "a1sewcraft.com",
            "web.de",
            "metro.co.uk",
            "eff.org",
            "edx.org",
            "woocommerce.com",
            "howtogeek.com",
            "hexun.com",
            "haofang.net",
            "infobae.com",
            "sony.com",
            "lenta.ru",
            "pearson.com",
            "dmoz.org",
            "flaticon.com",
            "biobiochile.cl",
            "bitbucket.org",
            "ecosia.org",
            "kernel.org",
            "indianexpress.com",
            "google.co.ve",
            "adobe.io",
            "eset.com",
            "ucdavis.edu",
            "beytoote.com",
            "zadn.vn",
            "technologyreview.com",
            "shopee.tw",
            "last.fm",
            "atdmt.com",
            "thenextweb.com",
            "iana.org",
            "citi.com",
            "rottentomatoes.com",
            "sap.com",
            "nymag.com",
            "livedoor.jp",
            "t-online.de",
            "cbssports.com",
            "taleo.net",
            "unicef.org",
            "wa.gov",
            "104.com.tw",
            "rutgers.edu",
            "amap.com",
            "opensource.org",
            "as.com",
            "gotomeeting.com",
            "alodokter.com",
            "costco.com",
            "thoughtco.com",
            "borna.news",
            "businessinsider.de",
            "wish.com",
            "phys.org",
            "samsungapps.com",
            "filimo.com",
            "wordreference.com",
            "yumpu.com",
            "videocampaign.co",
            "chinaz.com",
            "libsyn.com",
            "onet.pl",
            "wellnowuc.com",
            "businessweek.com",
            "gdanstum.net",
            "virginia.edu",
            "broadcom.com",
            "1337x.to",
            "iyiou.com",
            "zing.vn",
            "naukri.com",
            "moz.com",
            "nokia.com",};

    static ArrayList<String> Sites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latency);

        // init GUI
        Button pingButton = (Button) findViewById(R.id.pingButton);
        Button exportButton = (Button) findViewById(R.id.exportPingButton);
        Button clearButton = (Button) findViewById(R.id.clearPingButton);
        EditText n = (EditText) findViewById(R.id.N);
        progressBar = (ProgressBar) this.findViewById(R.id.progressbar);

        //init instance of WifiViewModel
        model = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(WifiViewModel.class);
        model.init();
        final double[] value = {SystemClock.elapsedRealtime()};
        model.getLiveData().observe(this, new Observer<WifiDataModel>() {
            @Override
            public void onChanged(WifiDataModel wifiDataModel) {
                if(wifiDataModel.getCompany() != null) {
                    wifi_name = wifiDataModel.getOrg() + " " + wifiDataModel.getCompany().getDomain();
                    countryCode = wifiDataModel.getCountry().toLowerCase();
                }
                else{
                    wifi_name = wifiDataModel.getOrg();
                    countryCode = wifiDataModel.getCountry().toLowerCase();
                }
            }
        });


        /** Click Ping Button **/
        pingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tableLayout = (RelativeLayout) findViewById(R.id.tablelayout);
                tl = (TableLayout) findViewById(R.id.table);

                if (n.getText().length() == 0) {
                    Toast.makeText(Latency.this, "Please enter some number!", Toast.LENGTH_SHORT).show();
                } else {
                    //pick top n sites for pinging

                  //  ArrayList<String> Sites = new ArrayList<>();

                    for (int i = 0; i < Integer.parseInt(String.valueOf(n.getText())); i++){
                        String[] list = sites[i].split("\\.");

                        String locale = getResources().getConfiguration().getLocales().get(0).toString().toLowerCase();
                        if (locale.charAt(locale.length() - 1) == '_') {
                            locale = locale.substring(0, locale.length() - 1);
                        } else {
                            locale = locale.replace('_', '-');
                        }

                        if (list[list.length - 1].length() == 2) {
                            if (list[list.length - 1].equals(countryCode)) {
                                Sites.add(sites[i]);
                            }
                        } else if (list[0].length() == 5 && list[0].charAt(2) == '-') {
                            if (list[0].equals(locale)) {
                                Sites.add(sites[i]);
                            }
                        } else {
                            Sites.add(sites[i]);
                        }
                    }
                    Header = "Tranco Top" + Integer.toString(Sites.size());

                    Toast.makeText(Latency.this, "Latency results will be saved to ping.csv", Toast.LENGTH_SHORT).show();

                    //Remove pre-existing table, and cancel aysnc task if previously running:
                    if (asyncPing != null && asyncPing.getStatus() != AsyncTask.Status.FINISHED) {
                        Log.i("asyncPing!=null-", "set to true");
                        asyncPing.cancel(true);
                    }
                    tl.removeAllViews();
                    /**Start Pinging, call async function**/
                    addHeaders(Header);
                    asyncPing = new PingTrancoSites();
                    asyncPing.execute(Sites);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Click Clear Button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCsv();
            }
        });

        // Click Export Button
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export();
            }
        });
    }

         // Aysnc function for pinging through all the sites
    public class PingTrancoSites extends AsyncTask< ArrayList<String>, String, Void> {
        Map<String, Object> m = new HashMap<>();
        Map<String,Object> M = new HashMap<>();
             String datetime = new SimpleDateFormat("yyMMddHHmm").format(new Date());
             long doc_ser = Long.parseLong(datetime);

             @Override
             protected void onPreExecute(){
            // This runs on the UI thread before the background thread executes.
            super.onPreExecute();
            saveData(doc_ser);
            // Do pre-thread tasks such as initializing variables.

            progressBar.setVisibility(View.VISIBLE);
            Log.v("myBackgroundTask", "Starting Background Task");
        }

        @Override
        protected Void doInBackground(ArrayList<String>... sites) {

            if(isCancelled())
            { Log.i("iscancelled","set to true"); return null;}
            String results;
            ArrayList<String > site = sites[0];
            for (int i = 0; i < site.size() ; i++) {
               // site[i]="www."+ site[i];
                if(isCancelled())
                { Log.i("iscancelled","set to true"); return null;}

                InetAddress ip_host;
                String url_host = "https://www." + site.get(i).trim() + "/";
                try {
                    ip_host = InetAddress.getByName(new URL(url_host).getHost());
                } catch (UnknownHostException | MalformedURLException e) {
                    ip_host = null;
                }
                
                String resolved_ip = String.valueOf(ip_host);
                Map<String, Object> m1 = new HashMap<>();
                m1.put("resolved_ip", resolved_ip);

                db.collection("latency").document(String.valueOf(doc_ser))
                        .collection("metric").document(site.get(i)).set(m1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("FIRESTORE", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FIRESTORE", "Error writing document", e);
                            }
                        });


                String cmd = "/system/bin/ping -c " + "1" + " " + site.get(i);
                    Log.i("site:", site.get(i));
                    Process p = executeCmd(cmd);
                    String pingStats = getPingStats(p);
                    pingStats = formatStats(pingStats, site.get(i), "1");
                    if (pingStats != null) {
                        results = pingStats;
                        Log.i("results after pinging Tranco site", results);
                        writeCsv(results, site.get(i), "7");

                        String[] resList = results.split(",");
                        m.put("min",resList[4]);
                        m.put("avg",resList[5]);
                        m.put("max",resList[6]);
                        m.put("std dev.", resList[7]);

                        M.put("latency",m);
                        db.collection("latency").document(String.valueOf(doc_ser))
                                .collection("metric").document(site.get(i)).set(M)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("FIRESTORE", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("FIRESTORE", "Error writing document", e);
                                    }
                                });
                        if (parseInt(results.split(",")[3]) == 100) {
                            results = "100% Loss :(";
                        }
                    } else {
                        Log.i("null situation", site.get(i));
                        results = "Oops, site unreachable";
                    }

                    publishProgress("We're still pinging .. Please wait!", results, site.get(i), Integer.toString(i));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String...p) {
            // Runs on the UI thread after publishProgress is invoked
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(Latency.this, p[0], Toast.LENGTH_SHORT).show();
            addData(p[2], p[1]);
            Log.i("After adding row", p[1]);

        }

        @Override
        protected void onPostExecute(Void results) {
            // This runs on the UI thread after complete execution of the doInBackground() method
            // This function receives result(String results) returned from the doInBackground() method.
            // Update UI with the found string.
            super.onPostExecute(results);
            if(asyncPing!=null && asyncPing.getStatus() != AsyncTask.Status.FINISHED){Log.i("asyncPing!=null-","set to true");
                asyncPing.cancel(true); }
            progressBar.setVisibility(View.GONE);

            //Toast.makeText(this, "All Done!", Toast.LENGTH_LONG).show();
        }
    }


    // Export csv file
    public void export() {

        try {
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), FILE_NAME);
            Uri path = FileProvider.getUriForFile(context, "com.example.Ping.fileprovider", fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Ping Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Export ping.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Read and return data from file_name
    // Return data as a string
    // Return empty string if file_name does not exist
    public String load(String file_name) {
        String data = "";
        FileInputStream fis = null;


        // Ignore if no export file exists
        File file = new File(getFilesDir()+"/"+file_name);
        if (!file.exists()) {
            Toast.makeText(this, "Export file not found", Toast.LENGTH_LONG).show();
            return data;
        }

        try {
            fis = openFileInput(file_name);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            data = sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }

    // Remove csv file
    public void deleteCsv() {
        boolean res = deleteFile(FILE_NAME);
        Toast.makeText(this, "ping.csv cleared", Toast.LENGTH_LONG).show();
    }

    /** Write command output to csv
     // Initialize new csv file if one does not exist
     // String output: output from environ command
     **/
    public void writeCsv(String output, String server, String pckts) {
        FileOutputStream fos = null;
        String headers = "Timestamp,Server,Packets[default = 1],Loss[%],Min[in ms],Avg[in ms],Max[in ms],Stddev\n";
        String res = "ping -c "+pckts+" "+server+"\n";
        String[] stats = output.split(",");
        String loss = stats[3];
        res += loss+"% packet loss";
        boolean initCsv = false;

        // Initialize csv file if one does not exist
        File file = new File(getFilesDir()+"/"+FILE_NAME);
        if (!file.exists()) {
            initCsv = true;
        }

        try {
            fos = new FileOutputStream(file, true);
            if (initCsv) {
                fos.write(headers.getBytes());
            }
            fos.write(output.getBytes());
            Log.i("toast res: ",res);
            //  Toast.makeText(this, res, Toast.LENGTH_LONG).show();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("WRITE", "Error writing to csv.");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    // Format ping statistics from ping command output
    // Return string in the following field order:
    // "Timestamp,Server,Packets,Loss,Min,Avg,Max,Stddev"

    public static String formatStats(String stats, String server, String packets) {
        String res, time, loss, min, avg, max, sd;
        res = time = loss = min = avg = max = sd = "";

        String[] lines = stats.split("\n");
        if(lines.length==1){
            res = null;
            return res;
        }
        String[] line1 = lines[0].split(", ");

        time = lines[2];
        loss = line1[2].substring(0,line1[2].indexOf("%"));

        // Grab rtt stats from ping server success cmd
        if (lines[1].contains("rtt")) {
            String[] line2 = lines[1].split(" = ");
            String[] rtt = line2[1].split("/");
            min = rtt[0];
            avg = rtt[1];
            max = rtt[2];
            sd = rtt[3].substring(0,rtt[3].length()-3);
        }

        res += time+","+server+","+packets+","+loss+",";
        res += min+","+avg+","+max+","+sd+"\n";
        Log.i("res",res);
        return res;
    }

    // Read and return output stats from ping command
    // Append Timestamp to end of output
    // Process p: returned after executing command
    // Return 3 lines: Ping Server Success
    // Return 2 lines: Ping Server Fail
    public static String getPingStats(Process p) {

        // 1 packet: Server Success
        /**
         PING 8.8.8.8 (8.8.8.8) 56(84) bytes of data.
         64 bytes from 8.8.8.8: icmp_seq=1 ttl=254 time=38.5 ms
         --- 8.8.8.8 ping statistics ---
         1 packets transmitted, 1 received, 0% packet loss, time 0ms
         rtt min/avg/max/mdev = 38.591/38.591/38.591/0.000 ms
         */

        // 1 packet: Server Fail
        /*
            PING asdf (92.242.140.2) 56(84) bytes of data.
            --- asdf ping statistics ---
            1 packets transmitted, 0 received, 100% packet loss, time 0ms
         */

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utc = f.format(new Date());

        try {
            String s;
            String res = "";
            boolean foundStats = false;
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = stdInput.readLine()) != null) {
                if (foundStats) {
                    res += s + "\n";
                } else if (s.contains("statistics")) {
                    foundStats = true;
                }
            }
            res += utc;
            p.destroy();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("READ", "Error reading process from command.");
        }
        return "";
    }

    // Execute the environ command and return the resulting Process object
    // Process obj is null if command does not execute properly
    public static Process executeCmd(String cmd){
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("EXECUTE", "Error executing command.");
        }
        return p;
    }

    //Add Headers to the tableLayout
    public void addHeaders(String header){

        /** Create a TableRow dynamically **/

        tr = new TableRow(this);

        tr.setLayoutParams(new LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the newRow **/

        TextView newRow = new TextView(this);

        newRow.setText(header);

        newRow.setTextColor(Color.BLACK);
        newRow.setAllCaps(true);
        newRow.setTextSize(18);

        newRow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        newRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        newRow.setPadding(50, 100, 5, 0);

        tr.addView(newRow);  // Adding textView to TableRow.

        /** Creating another textview **/

        TextView anotherRow = new TextView(this);

        anotherRow.setText("Avg.Time(in ms):");

        anotherRow.setTextColor(Color.BLACK);
        anotherRow.setAllCaps(true);
        anotherRow.setTextSize(18);

        anotherRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        anotherRow.setPadding(1, 100, 5, 0);

        anotherRow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        tr.addView(anotherRow); // Adding textView to TableRow.

        /** Add the TableRow to the TableLayout **/

        tl.addView(tr, new TableLayout.LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

        // we are adding two textviews for the divider because we have two columns

        tr = new TableRow(this);

        tr.setLayoutParams(new LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

        /** Creating another textview **/

        TextView divider = new TextView(this);

        divider.setText("-----------------------------");

        divider.setTextColor(Color.BLACK);

        divider.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        divider.setPadding(50, 0, 0, 0);

        divider.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        tr.addView(divider); // Adding textView to TableRow.

        TextView divider2 = new TextView(this);

        divider2.setText("-------------------------");

        divider2.setTextColor(Color.BLACK);

        divider2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));

        divider2.setPadding(0, 0, 0, 0);

        divider2.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        tr.addView(divider2); // Adding textView to TableRow.

        // Add the TableRow to the TableLayout

        tl.addView(tr, new TableLayout.LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

    }

    // Adds rows dynamically as the results from Ping come through
    public void addData(String site, String result) {

        /** Create a TableRow dynamically **/

        tr = new TableRow(this);

        tr.setLayoutParams(new LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the newRow **/

        newRow = new TextView(this);

        newRow.setText(site);

        newRow.setTextColor(Color.DKGRAY);

        newRow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        newRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        newRow.setPadding(50, 5, 5, 5);

        tr.addView(newRow);  // Adding textView to TableRow.


        /** Creating another textview **/

        anotherRow = new TextView(this);
        String[] tmp = result.split(",");

        if(result.split(",").length > 2) {
            anotherRow.setText(tmp[5]); anotherRow.setTextColor(Color.DKGRAY);
        }
        else if (tmp.length<3){
            Log.i("site unreachable/loss 100%", Arrays.toString(tmp));
            if(tmp.length==2){anotherRow.setText(result); anotherRow.setTextColor(Color.BLUE);}
            else{
                anotherRow.setText(result); anotherRow.setTextColor(Color.RED);}
        }

        anotherRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        anotherRow.setPadding(5, 5, 5, 5);

        anotherRow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        tr.addView(anotherRow); // Adding textView to TableRow.

        // Add the TableRow to the TableLayout

        tl.addView(tr, new TableLayout.LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

    }

    private void saveData(long doc_ser) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy | hh:mm:ss");
        final String[] lte = new String[1];
        final String[] wifi = new String[1];
        String totalURLrange;
        if (this.Sites!= null) {
            totalURLrange = String.valueOf(this.Sites.size());
        } else {
            totalURLrange = String.valueOf(1);
        }
        String timestamp = simpleDateFormat.format(new Date());

        if (isWifi(this)) {
            new Thread(new Runnable() {
               @Override
                public void run() {
                    WifiTask task = new WifiTask();
                    task.execute();
                    try {
                        Thread.sleep(5000);
                        wifi[0] = Latency.wifi_name;
//                        System.out.println(wifi[0]);
//                        System.out.println(SystemClock.elapsedRealtime());
                        lte[0] = "NN";
                        Map<String, Object> latency = new HashMap<>();
                        latency.put("lteNetwork", lte[0]);
                        latency.put("wifiNetwork", wifi[0]);
                        latency.put("timestamp", timestamp);
                        latency.put("totalURLRange", totalURLrange);
                        latency.put("serial", String.valueOf(doc_ser));

                        db.collection("latency").document(String.valueOf(doc_ser))
                                .set(latency)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("FIRESTORE", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("FIRESTORE", "Error writing document", e);
                                    }
                                });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            wifi[0] = "NN";
            lte[0] = getNetworkClass(getApplicationContext());
            Map<String, Object> latency = new HashMap<>();
            latency.put("lteNetwork", lte[0]);
            latency.put("wifiNetwork", wifi[0]);
            latency.put("timestamp", timestamp);
            latency.put("totalURLRange", totalURLrange);
            latency.put("serial", String.valueOf(doc_ser));

            db.collection("latency").document(String.valueOf(doc_ser))
                    .set(latency)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FIRESTORE", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FIRESTORE", "Error writing document", e);
                        }
                    });
        }

    }

    public boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String format;
        if (info == null || !info.isConnected())
            return "-"; // not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            countryCode = manager.getNetworkCountryIso();
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:     // api< 8: replace by 11
                case TelephonyManager.NETWORK_TYPE_GSM:      // api<25: replace by 16
                    format = String.format("%s | 2G / %s",countryCode,manager.getNetworkOperatorName());
                    return format;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:   // api< 9: replace by 12
                case TelephonyManager.NETWORK_TYPE_EHRPD:    // api<11: replace by 14
                case TelephonyManager.NETWORK_TYPE_HSPAP:    // api<13: replace by 15
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA: // api<25: replace by 17
                    format = String.format("%s | 3G / %s",countryCode, manager.getNetworkOperatorName());
                    return format;
                case TelephonyManager.NETWORK_TYPE_LTE:      // api<11: replace by 13
                case TelephonyManager.NETWORK_TYPE_IWLAN:    // api<25: replace by 18
                case 19: // LTE_CA
                    format = String.format("%s | 4G / %s",countryCode, manager.getNetworkOperatorName());
                    return format;
                case TelephonyManager.NETWORK_TYPE_NR:       // api<29: replace by 20
                    format = String.format("%s | 5G / %s",countryCode, manager.getNetworkOperatorName());
                    return format;
                default:
                    format = String.format("%s", manager.getNetworkOperatorName());
                    return format;
            }
        }
        format = String.format("%s", manager.getNetworkOperatorName());
        return format;
    }

    public String getWifiName(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return "NN";
    }

    private class WifiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Latency.model.fetchLiveData();
            return null;
        }
    }



}
