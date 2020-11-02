package com.example.ping;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

// WebPageTest is used for measuring and analyzing the performance of web pages.
// https://sites.google.com/a/webpagetest.org/docs/
//
// The API key is limited to 200 page loads per day.
// If need to do more testing then should consider running a private instance:
// https://sites.google.com/a/webpagetest.org/docs/private-instances

public class Page extends Activity {
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    EditText editText;
    private WebView webView;
    int largest = 0;
    long docId;
    HashMap<String, Object> hashMap;
    public static final String[] TRANCO_TOP_10 = new String[]{
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
            "nokia.com"
    };
    String[] commandList = new String[]{
            "window.performance.timing.connectEnd",
            "window.performance.timing.connectStart",
            "window.performance.timing.domComplete",
            "window.performance.timing.domContentLoadedEventEnd",
            "window.performance.timing.domContentLoadedEventStart",
            "window.performance.timing.domInteractive",
            "window.performance.timing.domLoading",
            "window.performance.timing.domainLookupEnd",
            "window.performance.timing.domainLookupStart",
            "window.performance.timing.fetchStart",
            "window.performance.timing.loadEventEnd",
            "window.performance.timing.loadEventStart",
            "window.performance.timing.navigationStart",
            "window.performance.timing.redirectEnd",
            "window.performance.timing.redirectStart",
            "window.performance.timing.requestStart",
            "window.performance.timing.responseEnd",
            "window.performance.timing.responseStart",
            "window.performance.timing.secureConnectionStart",
            "window.performance.timing.unloadEventEnd",
            "window.performance.timing.unloadEventStart",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        // init GUI
        Button loadButton = (Button) findViewById(R.id.loadButton);
        webView = (WebView) findViewById(R.id.webView);
        editText = findViewById(R.id.page_load_editText);
        hashMap = new HashMap<>();

        String datetime = new SimpleDateFormat("yyMMddHHmm").format(new Date());
        docId = Long.parseLong(datetime);
        // Load Button
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                largest = Integer.parseInt(editText.getText().toString());
                webView.getSettings().setJavaScriptEnabled(true);
                BackGroundTask task = new BackGroundTask();
                task.doInBackground();
            }
        });
    }

    private class Task1 {
        int i;
        HashMap<String, Object> data = new HashMap<>();

        Task1(int i) {
            this.i = i;
        }

        protected void doInBackground() {
            webView.setWebViewClient(new MyWebViewClient(data, TRANCO_TOP_10[i], i));
            webView.loadUrl("http://" + TRANCO_TOP_10[i]);
        }
    }

    private class BackGroundTask {
        protected void doInBackground() {
            Task1 task = new Task1(0);
            task.doInBackground();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        HashMap<String, Object> data;
        String url;
        int i;
        private boolean done = false;

        MyWebViewClient(HashMap<String, Object> data, String url, int i) {
            this.data = data;
            this.url = url;
            this.i = i;
        }

        @Override
        public void onPageFinished(WebView view, String url1) {
            if (!done) {
                done = true;
                for (int i = 0; i < commandList.length; i++) {
                    String[] list = commandList[i].split("\\.");
                    webView.evaluateJavascript("javascript:" + commandList[i], new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            data.put(list[3], value);
                            System.out.println(value);
                        }
                    });
                }
            }
            if (done) {
                done = false;
                System.out.println("Hello");
                hashMap.put(url, data);
                webView.clearCache(true);
                webView.clearHistory();
                webView.clearSslPreferences();
                webView.clearFormData();
                if (i < largest-1) {
                    Task1 task = new Task1(i + 1);
                    task.doInBackground();
                } else {
                    firebaseFirestore.collection("page_load").document(String.valueOf(docId)).set(hashMap);
                }
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}
