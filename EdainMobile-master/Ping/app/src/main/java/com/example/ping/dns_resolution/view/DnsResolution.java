package com.example.ping.dns_resolution.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.example.ping.R;
import com.example.ping.Wifi_Network_Info.model.WifiDataModel;
import com.example.ping.Wifi_Network_Info.viewmodel.WifiViewModel;
import com.example.ping.dns_resolution.model.DnsModel;
import com.example.ping.dns_resolution.viewmodel.DnsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class DnsResolution extends Fragment {

    // init GUI
    static final String FILE_A_RECORDS = "a_record.csv";
    static final String FILE_SOA_RECORDS = "soa_record.csv";
    static final String FILE_MX_RECORDS = "mx_record.csv";
    static final String FILE_NS_RECORDS = "ns_record.csv";
    static final String FILE_TXT_RECORDS = "txt_record.csv";
    ArrayList<String>[] output = new ArrayList[5];

    // Top 1000 Tranco Sites
    String[] top1000TrancoSites = new String[]{"google.com", "facebook.com", "youtube.com", "microsoft.com", "twitter.com", "tmall.com", "instagram.com", "netflix.com", "linkedin.com", "baidu.com", "windowsupdate.com", "qq.com", "wikipedia.org", "apple.com", "live.com", "sohu.com", "yahoo.com", "amazon.com", "doubleclick.net", "googletagmanager.com", "taobao.com", "adobe.com", "pinterest.com", "youtu.be", "360.cn", "vimeo.com", "jd.com", "reddit.com", "office.com", "wordpress.com", "weibo.com", "bing.com", "zoom.us", "sina.com.cn", "goo.gl", "github.com", "microsoftonline.com", "googleusercontent.com", "amazonaws.com", "bit.ly", "blogspot.com", "vk.com", "wordpress.org", "xinhuanet.com", "fbcdn.net", "tumblr.com", "mozilla.org", "godaddy.com", "msn.com", "google-analytics.com", "nytimes.com", "skype.com", "flickr.com", "okezone.com", "whatsapp.com", "gravatar.com", "soundcloud.com", "dropbox.com", "ytimg.com", "europa.eu", "alipay.com", "myshopify.com", "nih.gov", "csdn.net", "yahoo.co.jp", "cnn.com", "t.co", "ebay.com", "apache.org", "twitch.tv", "office365.com", "w3.org", "macromedia.com", "theguardian.com", "googlevideo.com", "medium.com", "spotify.com", "google.com.hk", "bongacams.com", "naver.com", "imdb.com", "sourceforge.net", "bbc.co.uk", "forbes.com", "zhanqi.tv", "paypal.com", "panda.tv", "aliexpress.com", "archive.org", "cloudflare.com", "bbc.com", "googleadservices.com", "googlesyndication.com", "github.io", "google.co.in", "yandex.ru", "amazon.in", "weebly.com", "stackoverflow.com", "china.com.cn", "digicert.com", "virginmedia.com", "tianya.cn", "amazon.co.jp", "creativecommons.org", "who.int", "wixsite.com", "issuu.com", "windows.net", "washingtonpost.com", "imgur.com", "tribunnews.com", "etsy.com", "livejasmin.com", "ggpht.com", "chaturbate.com", "oracle.com", "slideshare.net", "mail.ru", "amazon.co.uk", "reuters.com", "php.net", "gvt2.com", "wix.com", "cdc.gov", "wsj.com", "icloud.com", "app-measurement.com", "akadns.net", "akamaiedge.net", "pornhub.com", "google.de", "tinyurl.com", "huanqiu.com", "wikimedia.org", "1688.com", "google.cn", "alibaba.com", "aparat.com", "wp.com", "bloomberg.com", "google.co.jp", "businessinsider.com", "instructure.com", "cnet.com", "yy.com", "sciencedirect.com", "sogou.com", "opera.com", "youtube-nocookie.com", "163.com", "google.com.br", "harvard.edu", "gnu.org", "ok.ru", "mit.edu", "outlook.com", "dailymail.co.uk", "so.com", "ibm.com", "booking.com", "espn.com", "17ok.com", "amazon.de", "researchgate.net", "go.com", "stanford.edu", "forms.gle", "samsung.com", "google.co.uk", "bitly.com", "rakuten.co.jp", "list-manage.com", "hp.com", "blogger.com", "windows.com", "wiley.com", "telegraph.co.uk", "aol.com", "usatoday.com", "facebook.net", "msedge.net", "ntp.org", "jrj.com.cn", "mama.cn", "fb.com", "cnbc.com", "surveymonkey.com", "nginx.org", "cpanel.net", "fandom.com", "eventbrite.com", "kompas.com", "indiatimes.com", "myspace.com", "nasa.gov", "dailymotion.com", "canva.com", "huffingtonpost.com", "behance.net", "nature.com", "google.fr", "google.es", "cloudfront.net", "aliyun.com", "apple-dns.net", "youku.com", "xvideos.com", "ettoday.net", "office.net", "hao123.com", "gvt1.com", "addthis.com", "nflxso.net", "udemy.com", "time.com", "pixnet.net", "walmart.com", "npr.org", "indeed.com", "un.org", "roblox.com", "hicloud.com", "google.it", "freepik.com", "springer.com", "t.me", "foxnews.com", "google.ru", "babytree.com", "wendyssubway.com", "www.gov.uk", "ted.com", "cpanel.com", "grid.id", "sharepoint.com", "ca.gov", "flipkart.com", "aaplimg.com", "wired.com", "bilibili.com", "mysql.com", "akamai.net", "cnblogs.com", "yelp.com", "nginx.com", "hugedomains.com", "scribd.com", "detik.com", "salesforce.com", "scorecardresearch.com", "doi.org", "thestartmagazine.com", "zendesk.com", "opendns.com", "goodreads.com", "force.com", "soso.com", "adnxs.com", "gmail.com", "debian.org", "independent.co.uk", "intel.com", "wikihow.com", "free.fr", "tripadvisor.com", "shutterstock.com", "wetransfer.com", "themeforest.net", "beian.gov.cn", "akamaized.net", "google.ca", "android.com", "techcrunch.com", "squarespace.com", "amazon-adsystem.com", "daum.net", "google.com.sg", "mailchimp.com", "speedtest.net", "berkeley.edu", "mediafire.com", "stackexchange.com", "googletagservices.com", "msftncsi.com", "chase.com", "addtoany.com", "okta.com", "livejournal.com", "line.me", "unsplash.com", "zillow.com", "tokopedia.com", "taboola.com", "latimes.com", "craigslist.org", "weather.com", "ikea.com", "tiktok.com", "statcounter.com", "google.com.tw", "crashlytics.com", "grammarly.com", "healthline.com", "zoho.com", "xhamster.com", "twimg.com", "duckduckgo.com", "webmd.com", "onlinesbi.com", "quora.com", "gome.com.cn", "amzn.to", "cisco.com", "kickstarter.com", "theverge.com", "adsrvr.org", "webex.com", "6.cn", "jimdo.com", "digg.com", "nationalgeographic.com", "haosou.com", "deviantart.com", "akamaihd.net", "ft.com", "tradingview.com", "amazon.ca", "ietf.org", "sitemaps.org", "cornell.edu", "pixabay.com", "w3schools.com", "loc.gov", "theatlantic.com", "zhihu.com", "buzzfeed.com", "shopify.com", "washington.edu", "google.com.mx", "appsflyer.com", "dell.com", "youm7.com", "giphy.com", "eastday.com", "cbsnews.com", "about.com", "rubiconproject.com", "arnebrachhold.de", "2mdn.net", "slack.com", "wa.me", "telegram.org", "tandfonline.com", "akismet.com", "liputan6.com", "stumbleupon.com", "savefrom.net", "google.com.vn", "criteo.com", "academia.edu", "coursera.org", "symantec.com", "cambridge.org", "marriott.com", "rednet.cn", "marketwatch.com", "digikala.com", "disqus.com", "whatsapp.net", "padlet.com", "investopedia.com", "launchpad.net", "primevideo.com", "typepad.com", "bestbuy.com", "pubmatic.com", "iqiyi.com", "demdex.net", "uol.com.br", "box.com", "princeton.edu", "usnews.com", "avito.ru", "tiktokv.com", "google.com.tr", "azureedge.net", "tiktokcdn.com", "ampproject.org", "ilovepdf.com", "discord.com", "pki.goog", "huffpost.com", "feedburner.com", "msftconnecttest.com", "fc2.com", "mailchi.mp", "webs.com", "prnewswire.com", "advertising.com", "mashable.com", "hola.org", "economist.com", "pinimg.com", "evernote.com", "fda.gov", "alicdn.com", "noaa.gov", "bandcamp.com", "investing.com", "steampowered.com", "steamcommunity.com", "globo.com", "sciencemag.org", "homedepot.com", "worldometers.info", "airbnb.com", "hubspot.com", "bet9ja.com", "change.org", "nbcnews.com", "avast.com", "wellsfargo.com", "youronlinechoices.com", "ebay.de", "moatads.com", "teamviewer.com", "pbs.org", "plesk.com", "nflximg.com", "trello.com", "casalemedia.com", "arcgis.com", "whitehouse.gov", "mozilla.com", "usda.gov", "lazada.sg", "constantcontact.com", "jquery.com", "accuweather.com", "hulu.com", "target.com", "oup.com", "columbia.edu", "eepurl.com", "51.la", "engadget.com", "tistory.com", "umich.edu", "ebay.co.uk", "nypost.com", "outbrain.com", "state.gov", "aboutads.info", "unesco.org", "openx.net", "rt.com", "amazon.fr", "ups.com", "tripod.com", "edgekey.net", "setn.com", "google.co.th", "rlcdn.com", "huawei.com", "breitbart.com", "google.com.sa", "vice.com", "patreon.com", "psu.edu", "varzesh3.com", "geocities.com", "zdnet.com", "gofundme.com", "sagepub.com", "yale.edu", "bukalapak.com", "hbr.org", "meetup.com", "epa.gov", "nvidia.com", "guardian.co.uk", "google.com.ar", "abc.net.au", "trustpilot.com", "mayoclinic.org", "sun.com", "britannica.com", "google.com.eg", "metropoles.com", "allaboutcookies.org", "patch.com", "statista.com", "cbc.ca", "upenn.edu", "smallpdf.com", "bidswitch.net", "nike.com", "aliexpress.ru", "gotowebinar.com", "sciencedaily.com", "elsevier.com", "fiverr.com", "redhat.com", "photobucket.com", "networkadvertising.org", "google.pl", "vox.com", "sindonews.com", "getpocket.com", "google.co.id", "amazon.it", "amazon.es", "quizlet.com", "momoshop.com.tw", "ask.com", "www.gov.cn", "business.site", "irs.gov", "dribbble.com", "suara.com", "redd.it", "usps.com", "wayfair.com", "vkontakte.ru", "hdfcbank.com", "xnxx.com", "hootsuite.com", "worldbank.org", "gizmodo.com", "newyorker.com", "psychologytoday.com", "example.com", "everesttech.net", "talktalk.co.uk", "live.net", "google.com.au", "chinadaily.com.cn", "iso.org", "ieee.org", "fedex.com", "namnak.com", "fastcompany.com", "businesswire.com", "merriam-webster.com", "51sole.com", "khanacademy.org", "inc.com", "mathtag.com", "umn.edu", "oreilly.com", "ox.ac.uk", "wpengine.com", "dw.com", "blackboard.com", "bluekai.com", "softonic.com", "amazon.cn", "fortune.com", "cnzz.com", "cdninstagram.com", "wisc.edu", "chouftv.ma", "ltn.com.tw", "messenger.com", "snapchat.com", "letsencrypt.org", "ndtv.com", "wikia.com", "plos.org", "azure.com", "att.com", "yimg.com", "typeform.com", "entrepreneur.com", "wiktionary.org", "quantserve.com", "jhu.edu", "nist.gov", "agkn.com", "uci.edu", "jianshu.com", "us.com", "iqoption.com", "ucla.edu", "gmw.cn", "scientificamerican.com", "chicagotribune.com", "ameblo.jp", "theconversation.com", "pikiran-rakyat.com", "elegantthemes.com", "deloitte.com", "utexas.edu", "feedly.com", "playstation.com", "heavy.com", "spiegel.de", "sfgate.com", "python.org", "comodoca.com", "rambler.ru", "cmu.edu", "asos.com", "zaloapp.com", "adsafeprotected.com", "deepl.com", "canada.ca", "y2mate.com", "cam.ac.uk", "slate.com", "epicgames.com", "realtor.com", "uk.com", "krxd.net", "newrelic.com", "ubuntu.com", "pexels.com", "newsweek.com", "360.com", "arxiv.org", "indiegogo.com", "lenovo.com", "discordapp.com", "elpais.com", "xfinity.com", "zerodha.com", "qualtrics.com", "ed.gov", "telewebion.com", "intuit.com", "bmj.com", "timeanddate.com", "verisign.com", "eikegolehem.com", "hotstar.com", "manoramaonline.com", "hilton.com", "gosuslugi.ru", "nps.gov", "uchicago.edu", "over-blog.com", "afternic.com", "adform.net", "bootstrapcdn.com", "hotjar.com", "oecd.org", "bbb.org", "mirror.co.uk", "asus.com", "aboutcookies.org", "howstuffworks.com", "zol.com.cn", "fb.me", "biomedcentral.com", "uiuc.edu", "qz.com", "weforum.org", "shaparak.ir", "arstechnica.com", "nicovideo.jp", "mgid.com", "tencent.com", "sfx.ms", "byteoversea.com", "thesun.co.uk", "sberbank.ru", "purdue.edu", "criteo.net", "bet365.com", "medicalnewstoday.com", "cbslocal.com", "chron.com", "pcmag.com", "turn.com", "visualstudio.com", "ladbible.com", "unity3d.com", "tapad.com", "norton.com", "parallels.com", "mercadolivre.com.br", "nba.com", "douban.com", "altervista.org", "icicibank.com", "glassdoor.com", "op.gg", "merdeka.com", "appcenter.ms", "bizjournals.com", "capitalone.com", "ny.gov", "kakao.com", "dictionary.com", "gamepedia.com", "ign.com", "kumparan.com", "idntimes.com", "toutiao.com", "apa.org", "crwdcntrl.net", "americanexpress.com", "si.edu", "ftc.gov", "techradar.com", "autodesk.com", "blogspot.co.uk", "thesaurus.com", "mercadolibre.com.ar", "fao.org", "gstatic.com", "digitaltrends.com", "zend.com", "mlb.com", "geeksforgeeks.org", "namu.wiki", "chess.com", "netscape.com", "bankofamerica.com", "umeng.com", "fbsbx.com", "thepiratebay.org", "politico.com", "cctv.com", "patria.org.ve", "nyu.edu", "sahibinden.com", "icio.us", "news.com.au", "fastly.net", "smh.com.au", "istockphoto.com", "orange.fr", "abs-cbn.com", "ow.ly", "reverso.net", "nydailynews.com", "sectigo.com", "inquirer.net", "usc.edu", "trafficmanager.net", "openstreetmap.org", "cnnic.cn", "appspot.com", "bitnami.com", "spotxchange.com", "exelator.com", "barnesandnoble.com", "genius.com", "google.co.kr", "telegram.me", "ning.com", "joomla.org", "moneycontrol.com", "upwork.com", "prezi.com", "livescience.com", "fontawesome.com", "dropcatch.com", "uber.com", "house.gov", "xing.com", "pnas.org", "sakura.ne.jp", "mercadolibre.com.mx", "googleblog.com", "dedecms.com", "allegro.pl", "duke.edu", "google.gr", "foursquare.com", "instructables.com", "3lift.com", "apnews.com", "ebay-kleinanzeigen.de", "venturebeat.com", "zalo.me", "kapanlagi.com", "census.gov", "marca.com", "aliyuncs.com", "enable-javascript.com", "acs.org", "vmware.com", "variety.com", "lifehacker.com", "verizon.com", "jiameng.com", "brilio.net", "themeisle.com", "umd.edu", "chinanews.com", "youdao.com", "jstor.org", "fidelity.com", "ria.ru", "usgs.gov", "angelfire.com", "madrasati.sa", "jpnn.com", "ea.com", "mckinsey.com", "pewresearch.org", "secureserver.net", "miit.gov.cn", "earthlink.net", "google.com.ua", "ufl.edu", "imageshack.us", "teads.tv", "adp.com", "express.co.uk", "aljazeera.com", "g.page", "msu.edu", "proiezionidiborsa.it", "mookie1.com", "fast.com", "schoology.com", "thefreedictionary.com", "about.me", "ucsd.edu", "81.cn", "urbandictionary.com", "jotform.com", "lijit.com", "mi.com", "wildberries.ru", "linktr.ee", "senate.gov", "duolingo.com", "doubleverify.com", "thehill.com", "adjust.com", "hm.com", "alexa.com", "postgresql.org", "mercari.com", "isnssdk.com", "jsdelivr.net", "branch.io", "thedailybeast.com", "mixpanel.com", "9gag.com", "thelancet.com", "unc.edu", "rollingstone.com", "gartner.com", "northwestern.edu", "atlassian.net", "gmx.net", "ebc.net.tw", "hatena.ne.jp", "ssl-images-amazon.com", "ouedkniss.com", "redbubble.com", "nikkei.com", "kaspersky.com", "hurriyet.com.tr", "discord.gg", "in.gr", "hespress.com", "illinois.edu", "focus.cn", "tutorialspoint.com", "shopee.co.id", "bbcollab.com", "leboncoin.fr", "wattpad.com", "namecheap.com", "vnexpress.net", "proofpoint.com", "stripe.com", "crunchyroll.com", "xiaomi.com", "sky.com", "chegg.com", "utoronto.ca", "xbox.com", "lemonde.fr", "slashdot.org", "thetimes.co.uk", "theglobeandmail.com", "bls.gov", "lowes.com", "gamespot.com", "ensonhaber.com", "nfl.com", "goo.ne.jp", "ebay.com.au", "xe.com", "java.com", "apachefriends.org", "albawabhnews.com", "withgoogle.com", "asu.edu", "smartadserver.com", "jamanetwork.com", "google.nl", "edgesuite.net", "scmp.com", "jiathis.com", "privacyshield.gov", "yts.mx", "gismeteo.ru", "media.net", "gitlab.com", "hhs.gov", "sonhoo.com", "newscientist.com", "itu.int", "gsmarena.com", "wp.me", "repubblica.it", "today.com", "trendyol.com", "siemens.com", "google.ro", "atlassian.com", "fool.com", "e2ro.com", "elbalad.news", "creativecdn.com", "qoo10.sg", "arizona.edu", "wunderground.com", "zippyshare.com", "nhk.or.jp", "wufoo.com", "automattic.com", "onenote.net", "getbootstrap.com", "sec.gov", "td.com", "dotomi.com", "divar.ir", "hatenablog.com", "w.org", "globalsign.com", "1rx.io", "boston.com", "hollywoodreporter.com", "oppomobile.com", "moodle.org", "mitre.org", "people.com", "docker.com", "dcard.tw", "shopee.vn", "mzstatic.com", "livestream.com", "w55c.net", "mixcloud.com", "uspto.gov", "m.me", "mheducation.com", "history.com", "mapquest.com", "openssl.org", "dhl.com", "indiamart.com", "nejm.org", "googleapis.com", "adsymptotic.com", "drupal.org", "rediff.com", "newegg.com", "ustream.tv", "imrworldwide.com", "emofid.com", "colorado.edu", "dbs.com.sg", "hindustantimes.com", "efu.com.cn", "techtarget.com", "youporn.com", "freebsd.org", "eastmoney.com", "center4family.com", "zemanta.com", "biblegateway.com", "www.nhs.uk", "google.be", "uptodown.com", "disneyplus.com", "naver.jp", "mega.nz", "icann.org", "a1sewcraft.com", "web.de", "azurewebsites.net", "metro.co.uk", "eff.org", "edx.org", "woocommerce.com", "howtogeek.com", "hexun.com", "haofang.net", "infobae.com"};

    Button pingButton, addButton, clearButton, exportButton, trancoButton, firestoreButton;
    EditText editText, editText1;
    TextView aRecord, soaRecord, mxRecord, nsRecord, txtRecord;
    DnsViewModel viewModelARecord, viewModelSOARecord, viewModelMXRecord, viewModelNSRecord, viewModelTXTRecord;
    ScrollView scrollView;
    ProgressBar progressBar;
    String wifiNetworkName = "Time Limit Exceeded to get Wifi Network from API";
    WifiViewModel model;

    // val for deciding whether we want (Top N Tranco Site Data) or (Data for URL)
    boolean val = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dns_resolution, container, false);
        scrollView = view.findViewById(R.id.scrollView);
        progressBar = view.findViewById(R.id.progress_circular);
        editText = view.findViewById(R.id.edit_text);
        editText1 = view.findViewById(R.id.number_text);
        aRecord = view.findViewById(R.id.aRecord);
        soaRecord = view.findViewById(R.id.soaRecord);
        mxRecord = view.findViewById(R.id.mxRecord);
        nsRecord = view.findViewById(R.id.nsRecord);
        txtRecord = view.findViewById(R.id.txtRecord);
        pingButton = view.findViewById(R.id.dnsPingButton);
        exportButton = view.findViewById(R.id.dnsExportButton);
        clearButton = view.findViewById(R.id.dnsClearButton);
        trancoButton = view.findViewById(R.id.dnsTrancoButton);
        firestoreButton = view.findViewById(R.id.firestoreButton);

        // CLick Ping Button
        pingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = editText.getText().toString();
                if (hasConnectivity() & !url.equals("")) {
                    val = false;
                    // launch Progress Bar while retrieving Data
                    scrollView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    final Handler handler = new Handler();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // get DNS Records
                                Task1 task = new Task1();
                                task.execute(url).get();

                                Thread.sleep(600);
                                // Reset Progress Bar
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        scrollView.setVisibility(View.VISIBLE);
                                    }
                                });
                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else if (!url.equals("")) {
                    // show no internet connection in the UI thread
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // show field can't be empty
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Click Export Button
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportCsv();
            }
        });

        // Click Clear Button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCsv();
            }
        });

        // Click Tranco Button
        trancoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = editText1.getText().toString();
                if (hasConnectivity() & !s.equals("")) {
                    val = true;
                    // launch Progress Bar while retrieving Data
                    scrollView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    //  number of Tranco Sites taken as input
                    final int n = Integer.parseInt(s);

                    final Handler handler = new Handler();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            // get DNS Records for Top N Tranco Sites
                            BackGroundTask task = new BackGroundTask();
                            task.execute(n);

                            try {
                                Thread.sleep((n * 1000));
                                exportCsv();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Reset Progress Bar
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "DNS Data for Top " + n + " Tranco Sites stored in csv files", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                } else if (!s.equals("")) {
                    // show no internet connection in the UI thread
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // show field can't be empty
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Click Firestore Button
        firestoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = editText1.getText().toString();
                if (hasConnectivity() & !s.equals("")) {

                    // launch Progress Bar while retrieving Data
                    scrollView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    final int n = Integer.parseInt(s);

                    // number of Tranco Sites taken as input
                    final Handler handler = new Handler();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // store the data retrieved to firestore
                                storeToFireStore(n);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Reset Progress Bar
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "DNS Data for Top " + n + " Tranco Sites stored in csv files", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                } else if (!s.equals("")) {
                    // show no internet connection in the UI thread
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // show field can't be empty
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Field can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init ViewModels for fetching different records
        viewModelARecord = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(DnsViewModel.class);
        viewModelSOARecord = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(DnsViewModel.class);
        viewModelMXRecord = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(DnsViewModel.class);
        viewModelNSRecord = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(DnsViewModel.class);
        viewModelTXTRecord = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(DnsViewModel.class);

        // ViewModels Observe the Data Changes and store the values in output Array<String>

        // init model for getting wifi data
        model = new ViewModelProvider(new ViewModelStore(), new ViewModelProvider.NewInstanceFactory()).get(WifiViewModel.class);

        model.init();

        // observer for getting live wifi data
        model.getLiveData().observe(this, new Observer<WifiDataModel>() {
            @Override
            public void onChanged(WifiDataModel wifiDataModel) {
                if (wifiDataModel.getCompany() != null) {
                    wifiNetworkName = wifiDataModel.getOrg() + " " + wifiDataModel.getCompany().getDomain();
                } else {
                    wifiNetworkName = wifiDataModel.getOrg();
                }
            }
        });

        final Context context = this.getContext();

        viewModelARecord.init();

        // observer for getting live data
        viewModelARecord.getData().observe(this, new Observer<DnsModel>() {
            @Override
            public void onChanged(DnsModel dnsModel) {

                // init DnsRecordFormatter
                DnsRecordFormatter dnsRecordFetcher = new DnsRecordFormatter(context);

                // Checking if we have Clicked on Ping Button
                if (!val) {
                    // Setting the Record TexView when we click on PING
                    aRecord.setText(dnsRecordFetcher.aRecord(dnsModel));
                }

                // Storing the formatted data in output Array<String>
                output[0] = dnsRecordFetcher.exportARecord(dnsModel);
            }
        });


        viewModelSOARecord.init();

        // observer for getting live data
        viewModelSOARecord.getData().observe(this, new Observer<DnsModel>() {
            @Override
            public void onChanged(DnsModel dnsModel) {

                // init DnsRecordFormatter
                DnsRecordFormatter dnsRecordFetcher = new DnsRecordFormatter(context);

                // Checking if we have Clicked on Ping Button
                if (!val) {
                    // Setting the Record TexView when we click on PING
                    soaRecord.setText(dnsRecordFetcher.soaRecord(dnsModel));
                }

                // Storing the formatted data in output Array<String>
                output[1] = dnsRecordFetcher.exportSOARecord(dnsModel);
            }
        });


        viewModelMXRecord.init();

        // observer for getting live data
        viewModelMXRecord.getData().observe(this, new Observer<DnsModel>() {
            @Override
            public void onChanged(DnsModel dnsModel) {

                // init DnsRecordFormatter
                DnsRecordFormatter dnsRecordFetcher = new DnsRecordFormatter(context);

                // Checking if we have Clicked on Ping Button
                if (!val) {
                    // Setting the Record TexView when we click on PING
                    mxRecord.setText(dnsRecordFetcher.mxRecord(dnsModel));
                }

                // Storing the formatted data in output Array<String>
                output[2] = dnsRecordFetcher.exportMXRecord(dnsModel);
            }
        });

        // init ViewModel
        viewModelNSRecord.init();

        // observer for getting live data
        viewModelNSRecord.getData().observe(this, new Observer<DnsModel>() {
            @Override
            public void onChanged(DnsModel dnsModel) {

                // init DnsRecordFormatter
                DnsRecordFormatter dnsRecordFetcher = new DnsRecordFormatter(context);

                // Checking if we have Clicked on Ping Button
                if (!val) {
                    // Setting the Record TexView when we click on PING
                    nsRecord.setText(dnsRecordFetcher.nsRecord(dnsModel));
                }

                // Storing the formatted data in output Array<String>
                output[3] = dnsRecordFetcher.exportNSRecord(dnsModel);
            }
        });

        // init ViewModel
        viewModelTXTRecord.init();

        // observer for getting live data
        viewModelTXTRecord.getData().observe(this, new Observer<DnsModel>() {
            @Override
            public void onChanged(DnsModel dnsModel) {
                // init DnsRecordFormatter
                DnsRecordFormatter dnsRecordFetcher = new DnsRecordFormatter(context);

                // Checking if we have Clicked on Ping Button
                if (!val) {
                    // Setting the Record TexView when we click on PING
                    txtRecord.setText(dnsRecordFetcher.txtRecord(dnsModel));
                }

                // Storing the formatted data in output Array<String>
                output[4] = dnsRecordFetcher.exportTXTRecord(dnsModel);
            }
        });
    }

    // Method for getting DNS Records
    public void getDnsRecords(String url) {
        String[] types = new String[]{"a", "soa", "mx", "ns", "txt"};

        // ViewModels fetching data for different types of records

        viewModelARecord.fetchData(url, types[0]);
        viewModelSOARecord.fetchData(url, types[1]);
        viewModelMXRecord.fetchData(url, types[2]);
        viewModelNSRecord.fetchData(url, types[3]);
        viewModelTXTRecord.fetchData(url, types[4]);
    }

    // delete CSV
    public void deleteCsv() {
        // file names for various files containing dns records

        String[] fileNames = new String[]{FILE_A_RECORDS, FILE_SOA_RECORDS, FILE_MX_RECORDS, FILE_NS_RECORDS, FILE_TXT_RECORDS};
        for (int i = 0; i < 5; i++) {
            requireContext().deleteFile(fileNames[i]);
        }

        // checking if we click on Ping Button

        if (!val) {
            // Toast for showing that files have been deleted
            Toast.makeText(getContext(), "dns_records cleared", Toast.LENGTH_SHORT).show();
        }
    }

    // Export CSV
    public void exportCsv() {
        try {
            Context context = requireActivity().getApplicationContext();

            // file names for various files containing dns records
            String[] fileNames = new String[]{FILE_A_RECORDS, FILE_SOA_RECORDS, FILE_MX_RECORDS, FILE_NS_RECORDS, FILE_TXT_RECORDS};

            // init file locations and paths
            File[] fileLocations = new File[5];
            Uri[] paths = new Uri[5];

            ArrayList<Uri> exportFiles = new ArrayList<>();
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "DNS Resolution Data");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            for (int i = 0; i < 5; i++) {
                fileLocations[i] = new File(requireContext().getFilesDir() + "/" + fileNames[i]);
                paths[i] = FileProvider.getUriForFile(context, "com.example.Ping.fileprovider", fileLocations[i]);
                exportFiles.add(paths[i]);
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, exportFiles);
            startActivity(Intent.createChooser(intent, "Export dns_records"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Write command output to csv
    // Initialize new csv file if one does not exist
    // String output: output from environ command
    public void writeCsv(ArrayList<String>[] output) {
        FileOutputStream fileOutputStream = null;
        String[] headers = new String[5];
        headers[0] = "IPAddress\n";
        headers[1] = "PrimaryServer,Administrator,SerialNumber,Refresh,Retry,Expire,TTL\n";
        headers[2] = "IPAddress,Preferences,DomainName\n";
        headers[3] = "DomainName,IPAddress\n";
        headers[4] = "Data\n";
        String[] fileNames = new String[]{FILE_A_RECORDS, FILE_SOA_RECORDS, FILE_MX_RECORDS, FILE_NS_RECORDS, FILE_TXT_RECORDS};
        boolean[] initRecords = new boolean[]{false, false, false, false, false};

        // init file locations
        File[] fileLocations = new File[5];

        // Initialize csv file if one does not exist
        for (int i = 0; i < 5; i++) {
            fileLocations[i] = new File(requireContext().getFilesDir() + "/" + fileNames[i]);
            if (!fileLocations[i].exists()) {
                initRecords[i] = true;
            }
        }
        for (int i = 0; i < 5; i++) {
            try {
                fileOutputStream = new FileOutputStream(fileLocations[i], true);
                if (initRecords[i]) {
                    fileOutputStream.write(headers[i].getBytes());
                }
                for (int j = 0; j < output[i].size(); j++) {
                    fileOutputStream.write(output[i].get(j).getBytes());
                }
                fileOutputStream.write("\n".getBytes());
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("WRITE", "Error Writing csv");
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // get DNS Records for Top N Tranco Sites
    public void Top10TrancoSiteDatatoCsv(final int n) {
        deleteCsv();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < n; i++) {
                    Task1 task = new Task1();
                    task.execute(top1000TrancoSites[i]);
                }
            }
        }).start();
    }

    // Async Task for getting DNS Records in the Background
    @SuppressLint("StaticFieldLeak")
    private class Task1 extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            getDnsRecords(strings[0]);
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            writeCsv(output);
        }
    }

    // Async Task for getting DNS Record for Top N Sites in Background
    @SuppressLint("StaticFieldLeak")
    private class BackGroundTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            Top10TrancoSiteDatatoCsv(integers[0]);
            return null;
        }
    }

    // Storing the resolved parameters in firestore
    public void storeToFireStore(int n) throws InterruptedException {
        // get firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        long docId = (long) Math.floor(Math.random() * Long.MAX_VALUE);
        // create a map for storing data to firestore
        HashMap<String, Object> data = new HashMap<>();

        // setting up serial value = random value
        data.put("serial", docId);

        // setting timestamp as the time in UTC
        data.put("timestamp", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("dd/MM/uuuu h:mm:ss a xxx")));

        // setting total url range
        data.put("totol_url_range", n);

        // setting the network parameter values
        HashMap<String, String> network_data = getNetworkName();
        data.put("LTE Network", network_data.get("LTE Network"));
        data.put("Wifi Network", network_data.get("Wifi Network"));

        // instantiate getIpAddress for doing DNS Resolution
        GetIpAddress getIpAddress = new GetIpAddress(this.getContext());
        getIpAddress.getDnsServer();

        // checks whether the dns server associated with our network connection is public or not
        if (!getIpAddress.checkPublicDns()) {
            // setting public dns no if the dns server for the network is a public dns server
            data.put("pubic_dns", "no");
            DocumentReference documentReference = db.collection("dns_resolve").document(String.valueOf(docId));
            documentReference.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    for (int i = 0; i < n; i++) {
                        // get the dns resolution for url = top1000TrancoSites[i];
                        getIpAddress.getDnsStuff(top1000TrancoSites[i], documentReference);
                    }
                }
            });
        } else {
            // setting public dns yes if the dns server for the network is a public dns server
            data.put("pubic_dns", "yes");
            db.collection("dns_resolve").add(data);
        }

    }

    // method for getting the network name
    public HashMap<String, String> getNetworkName() throws InterruptedException {
        HashMap<String, String> network_data = new HashMap<>();
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        assert networkInfo != null;
        if (networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiTask wifiTask = new WifiTask();
                wifiTask.execute();
                Thread.sleep(5000);
                network_data.put("Wifi Network", wifiNetworkName);
                network_data.put("LTE Network", "NaN");
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager manager = (TelephonyManager) requireContext().getSystemService(Context.TELEPHONY_SERVICE);
                network_data.put("Wifi Network", "NaN");
                network_data.put("LTE Network", manager.getNetworkOperatorName());
            }
        } else {
            network_data.put("Wifi Network", "No Internet Connection");
            network_data.put("LTE Network", "No Internet Connection");
        }

        return network_data;
    }

    // Async Task for fetching the wifi data
    private class WifiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            model.fetchLiveData();
            return null;
        }
    }

    // method for checking whether the system has a valid internet connection or not.
    public boolean hasConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        assert networkInfo != null;
        return networkInfo.isConnected();
    }
}