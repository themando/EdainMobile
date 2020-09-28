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
    static WifiViewModel model;
   // static int doc_ser;
    static String wifi_name = "Time Limit Exceeded to get Wifi Network from API";
    TableLayout tl;
    RelativeLayout tableLayout;
    TableRow tr;
    PingTrancoSites asyncPing = new PingTrancoSites();
    TextView newRow,anotherRow;
    ProgressBar progressBar;


    /**
     * Adding Firestore services:
     */ FirebaseFirestore db = FirebaseFirestore.getInstance();


    String[] sites = {
            "www.google.com",
            "www.facebook.com",
            "www.youtube.com",
            "www.microsoft.com",
            "www.twitter.com",
            "www.tmall.com",
            "www.instagram.com",
            "www.netflix.com",
            "www.linkedin.com",
            "www.qq.com",
            "www.baidu.com",
            "www.windowsupdate.com",
            "www.wikipedia.org",
            "www.apple.com",
            "www.live.com",
            "www.sohu.com",
            "www.yahoo.com",
            "www.amazon.com",
            "www.googletagmanager.com",
            "www.doubleclick.net",
            "www.taobao.com",
            "www.adobe.com",
            "www.pinterest.com",
            "www.360.cn",
            "www.vimeo.com",
            "www.jd.com",
            "www.reddit.com",
            "www.wordpress.com",
            "www.weibo.com",
            "www.office.com",
            "www.bing.com",
            "www.sina.com.cn",
            "www.zoom.us",
            "www.github.com",
            "www.googleusercontent.com",
            "www.blogspot.com",
            "www.amazonaws.com",
            "www.bit.ly",
            "www.vk.com",
            "www.microsoftonline.com",
            "www.wordpress.org",
            "www.xinhuanet.com",
            "www.fbcdn.net",
            "www.tumblr.com",
            "www.mozilla.org",
            "www.godaddy.com",
            "www.msn.com",
            "www.skype.com",
            "www.google-analytics.com",
            "www.nytimes.com",
            "www.flickr.com",
            "www.okezone.com",
            "www.whatsapp.com",
            "www.gravatar.com",
            "www.dropbox.com",
            "www.soundcloud.com",
            "www.europa.eu",
            "www.alipay.com",
            "www.myshopify.com",
            "www.nih.gov",
            "www.cnn.com",
            "www.csdn.net",
            "www.apache.org",
            "www.ebay.com",
            "www.yahoo.co.jp",
            "www.twitch.tv",
            "www.w3.org",
            "www.office365.com",
            "www.googlevideo.com",
            "www.macromedia.com",
            "www.theguardian.com",
            "www.medium.com",
            "www.bongacams.com",
            "www.spotify.com",
            "www.sourceforge.net",
            "www.zhanqi.tv",
            "www.imdb.com",
            "www.forbes.com",
            "www.bbc.co.uk",
            "www.google.com.hk",
            "www.naver.com",
            "www.paypal.com",
            "www.googleadservices.com",
            "www.aliexpress.com",
            "www.archive.org",
            "www.bbc.com",
            "www.cloudflare.com",
            "www.github.io",
            "www.stackoverflow.com",
            "www.yandex.ru",
            "www.google.co.in",
            "www.amazon.in",
            "www.weebly.com",
            "www.china.com.cn",
            "www.creativecommons.org",
            "www.who.int",
            "www.digicert.com",
            "www.wixsite.com",
            "www.amazon.co.jp",
            "www.tianya.cn",
            "www.issuu.com",
            "www.virginmedia.com",
            "www.washingtonpost.com",
            "www.windows.net",
            "www.imgur.com",
            "www.tribunnews.com",
            "www.ggpht.com",
            "www.etsy.com",
            "www.oracle.com",
            "www.chaturbate.com",
            "www.gvt2.com",
            "www.php.net",
            "www.reuters.com",
            "www.livejasmin.com",
            "www.slideshare.net",
            "www.amazon.co.uk",
            "www.app-measurement.com",
            "www.wix.com",
            "www.mail.ru",
            "www.akadns.net",
            "www.akamaiedge.net",
            "www.cdc.gov",
            "www.icloud.com",
            "www.wsj.com",
            "www.pornhub.com",
            "www.tinyurl.com",
            "www.wikimedia.org",
            "www.huanqiu.com",
            "www.google.de",
            "www.aparat.com",
            "www.1688.com",
            "www.wp.com",
            "www.bloomberg.com",
            "www.yy.com",
            "www.alibaba.com",
            "www.cnet.com",
            "www.businessinsider.com",
            "www.google.co.jp",
            "www.opera.com",
            "www.sciencedirect.com",
            "www.youtube-nocookie.com",
            "www.163.com",
            "www.gnu.org",
            "www.harvard.edu",
            "www.sogou.com",
            "www.mit.edu",
            "www.google.com.br",
            "www.booking.com",
            "www.outlook.com",
            "www.dailymail.co.uk",
            "www.ibm.com",
            "www.17ok.com",
            "www.ok.ru",
            "www.so.com",
            "www.samsung.com",
            "www.amazon.de",
            "www.stanford.edu",
            "www.go.com",
            "www.researchgate.net",
            "www.bitly.com",
            "www.google.co.uk",
            "www.forms.gle",
            "www.facebook.net",
            "www.hp.com",
            "www.list-manage.com",
            "www.blogger.com",
            "www.telegraph.co.uk",
            "www.aol.com",
            "www.rakuten.co.jp",
            "www.wiley.com",
            "www.ntp.org",
            "www.instructure.com",
            "www.mama.cn",
            "www.jrj.com.cn",
            "www.usatoday.com",
            "www.msedge.net",
            "www.espn.com",
            "www.windows.com",
            "www.fb.com",
            "www.cnbc.com",
            "www.nginx.org",
            "www.fandom.com",
            "www.surveymonkey.com",
            "www.cpanel.net",
            "www.eventbrite.com",
            "www.canva.com",
            "www.indiatimes.com",
            "www.huffingtonpost.com",
            "www.myspace.com",
            "www.nasa.gov",
            "www.dailymotion.com",
            "www.nature.com",
            "www.nflxso.net",
            "www.apple-dns.net",
            "www.youku.com",
            "www.behance.net",
            "www.cloudfront.net",
            "www.roblox.com",
            "www.kompas.com",
            "www.google.es",
            "www.babytree.com",
            "www.google.fr",
            "www.time.com",
            "www.addthis.com",
            "www.walmart.com",
            "www.aliyun.com",
            "www.hicloud.com",
            "www.xvideos.com",
            "www.gvt1.com",
            "www.npr.org",
            "www.un.org",
            "www.grid.id",
            "www.haosou.com",
            "www.pixnet.net",
            "www.freepik.com",
            "www.akamai.net",
            "www.foxnews.com",
            "www.office.net",
            "www.springer.com",
            "www.bilibili.com",
            "www.indeed.com",
            "www.www.gov.uk",
            "www.hao123.com",
            "www.wendyssubway.com",
            "www.cpanel.com",
            "www.ted.com",
            "www.google.it",
            "www.ettoday.net",
            "www.aaplimg.com",
            "www.wired.com",
            "www.yelp.com",
            "www.mysql.com",
            "www.ca.gov",
            "www.google.ru",
            "www.nginx.com",
            "www.sharepoint.com",
            "www.flipkart.com",
            "www.hugedomains.com",
            "www.scribd.com",
            "www.salesforce.com",
            "www.opendns.com",
            "www.scorecardresearch.com",
            "www.cnblogs.com",
            "www.force.com",
            "www.udemy.com",
            "www.doi.org",
            "www.gmail.com",
            "www.debian.org",
            "www.adnxs.com",
            "www.goodreads.com",
            "www.independent.co.uk",
            "www.wikihow.com",
            "www.intel.com",
            "www.free.fr",
            "www.zendesk.com",
            "www.zillow.com",
            "www.tripadvisor.com",
            "www.detik.com",
            "www.android.com",
            "www.themeforest.net",
            "www.beian.gov.cn",
            "www.akamaized.net",
            "www.googletagservices.com",
            "www.chase.com",
            "www.techcrunch.com",
            "www.amazon-adsystem.com",
            "www.wetransfer.com",
            "www.google.ca",
            "www.mailchimp.com",
            "www.thestartmagazine.com",
            "www.crashlytics.com",
            "www.squarespace.com",
            "www.soso.com",
            "www.msftncsi.com",
            "www.berkeley.edu",
            "www.stackexchange.com",
            "www.addtoany.com",
            "www.unsplash.com",
            "www.google.com.sg",
            "www.speedtest.net",
            "www.livejournal.com",
            "www.mediafire.com",
            "www.ikea.com",
            "www.shutterstock.com",
            "www.latimes.com",
            "www.craigslist.org",
            "www.daum.net",
            "www.weather.com",
            "www.statcounter.com",
            "www.xhamster.com",
            "www.gome.com.cn",
            "www.line.me",
            "www.okta.com",
            "www.healthline.com",
            "www.twimg.com",
            "www.taboola.com",
            "www.pixabay.com",
            "www.google.com.tw",
            "www.kickstarter.com",
            "www.tiktok.com",
            "www.adsrvr.org",
            "www.grammarly.com",
            "www.webmd.com",
            "www.amzn.to",
            "www.duckduckgo.com",
            "www.onlinesbi.com",
            "www.quora.com",
            "www.tokopedia.com",
            "www.cisco.com",
            "www.jimdo.com",
            "www.digg.com",
            "www.tradingview.com",
            "www.deviantart.com",
            "www.theverge.com",
            "www.nationalgeographic.com",
            "www.akamaihd.net",
            "www.appsflyer.com",
            "www.ietf.org",
            "www.ft.com",
            "www.sitemaps.org",
            "www.youm7.com",
            "www.cornell.edu",
            "www.theatlantic.com",
            "www.loc.gov",
            "www.webex.com",
            "www.amazon.ca",
            "www.buzzfeed.com",
            "www.washington.edu",
            "www.dell.com",
            "www.eastday.com",
            "www.w3schools.com",
            "www.about.com",
            "www.rubiconproject.com",
            "www.zhihu.com",
            "www.google.com.vn",
            "www.shopify.com",
            "www.telegram.org",
            "www.arnebrachhold.de",
            "www.giphy.com",
            "www.2mdn.net",
            "www.cbsnews.com",
            "www.akismet.com",
            "www.wa.me",
            "www.savefrom.net",
            "www.stumbleupon.com",
            "www.criteo.com",
            "www.trello.com",
            "www.marketwatch.com",
            "www.google.com.mx",
            "www.coursera.org",
            "www.marriott.com",
            "www.liputan6.com",
            "www.whatsapp.net",
            "www.symantec.com",
            "www.digikala.com",
            "www.tandfonline.com",
            "www.zoho.com",
            "www.cambridge.org",
            "www.rednet.cn",
            "www.disqus.com",
            "www.launchpad.net",
            "www.academia.edu",
            "www.hola.org",
            "www.tiktokcdn.com",
            "www.pubmatic.com",
            "www.avito.ru",
            "www.azureedge.net",
            "www.typepad.com",
            "www.ampproject.org",
            "www.tiktokv.com",
            "www.demdex.net",
            "www.slack.com",
            "www.iqiyi.com",
            "www.bestbuy.com",
            "www.princeton.edu",
            "www.primevideo.com",
            "www.uol.com.br",
            "www.investopedia.com",
            "www.usnews.com",
            "www.feedburner.com",
            "www.msftconnecttest.com",
            "www.webs.com",
            "www.google.com.tr",
            "www.huffpost.com",
            "www.alicdn.com",
            "www.prnewswire.com",
            "www.mailchi.mp",
            "www.steamcommunity.com",
            "www.worldometers.info",
            "www.economist.com",
            "www.mashable.com",
            "www.globo.com",
            "www.fc2.com",
            "www.advertising.com",
            "www.steampowered.com",
            "www.fda.gov",
            "www.box.com",
            "www.evernote.com",
            "www.wellsfargo.com",
            "www.airbnb.com",
            "www.pinimg.com",
            "www.avast.com",
            "www.nbcnews.com",
            "www.bandcamp.com",
            "www.homedepot.com",
            "www.sciencemag.org",
            "www.discord.com",
            "www.sindonews.com",
            "www.pki.goog",
            "www.ilovepdf.com",
            "www.change.org",
            "www.nflximg.com",
            "www.moatads.com",
            "www.noaa.gov",
            "www.youronlinechoices.com",
            "www.51.la",
            "www.investing.com",
            "www.hubspot.com",
            "www.ups.com",
            "www.teamviewer.com",
            "www.cnzz.com",
            "www.pbs.org",
            "www.casalemedia.com",
            "www.usda.gov",
            "www.whitehouse.gov",
            "www.mozilla.com",
            "www.jquery.com",
            "www.accuweather.com",
            "www.edgekey.net",
            "www.bet9ja.com",
            "www.target.com",
            "www.nypost.com",
            "www.lazada.sg",
            "www.eepurl.com",
            "www.constantcontact.com",
            "www.plesk.com",
            "www.columbia.edu",
            "www.umich.edu",
            "www.rt.com",
            "www.ebay.de",
            "www.ebay.co.uk",
            "www.engadget.com",
            "www.hulu.com",
            "www.arcgis.com",
            "www.oup.com",
            "www.state.gov",
            "www.tripod.com",
            "www.unesco.org",
            "www.openx.net",
            "www.vice.com",
            "www.aboutads.info",
            "www.outbrain.com",
            "www.rlcdn.com",
            "www.geocities.com",
            "www.amazon.fr",
            "www.psu.edu",
            "www.zdnet.com",
            "www.yale.edu",
            "www.huawei.com",
            "www.usps.com",
            "www.gofundme.com",
            "www.patreon.com",
            "www.varzesh3.com",
            "www.guardian.co.uk",
            "www.setn.com",
            "www.fiverr.com",
            "www.google.co.th",
            "www.sagepub.com",
            "www.hbr.org",
            "www.tistory.com",
            "www.sun.com",
            "www.meetup.com",
            "www.abc.net.au",
            "www.google.com.sa",
            "www.breitbart.com",
            "www.www.gov.cn",
            "www.mayoclinic.org",
            "www.statista.com",
            "www.epa.gov",
            "www.padlet.com",
            "www.aliexpress.ru",
            "www.cbc.ca",
            "www.allaboutcookies.org",
            "www.nike.com",
            "www.upenn.edu",
            "www.bukalapak.com",
            "www.bidswitch.net",
            "www.patch.com",
            "www.google.com.eg",
            "www.google.com.ar",
            "www.nvidia.com",
            "www.sciencedaily.com",
            "www.britannica.com",
            "www.photobucket.com",
            "www.redhat.com",
            "www.vox.com",
            "www.wayfair.com",
            "www.networkadvertising.org",
            "www.elsevier.com",
            "www.irs.gov",
            "www.business.site",
            "www.getpocket.com",
            "www.amazon.es",
            "www.smallpdf.com",
            "www.gotowebinar.com",
            "www.ask.com",
            "www.redd.it",
            "www.vkontakte.ru",
            "www.google.pl",
            "www.example.com",
            "www.newyorker.com",
            "www.gizmodo.com",
            "www.amazon.it",
            "www.metropoles.com",
            "www.51sole.com",
            "www.worldbank.org",
            "www.chinadaily.com.cn",
            "www.google.co.id",
            "www.xnxx.com",
            "www.everesttech.net",
            "www.iso.org",
            "www.trustpilot.com",
            "www.dribbble.com",
            "www.momoshop.com.tw",
            "www.psychologytoday.com",
            "www.businesswire.com",
            "www.hootsuite.com",
            "www.namnak.com",
            "www.ieee.org",
            "www.fastcompany.com",
            "www.suara.com",
            "www.live.net",
            "www.talktalk.co.uk",
            "www.eikegolehem.com",
            "www.google.com.au",
            "www.hdfcbank.com",
            "www.iqoption.com",
            "www.mathtag.com",
            "www.inc.com",
            "www.ox.ac.uk",
            "www.oreilly.com",
            "www.cdninstagram.com",
            "www.ameblo.jp",
            "www.fortune.com",
            "www.bluekai.com",
            "www.dw.com",
            "www.letsencrypt.org",
            "www.umn.edu",
            "www.fedex.com",
            "www.wikia.com",
            "www.wisc.edu",
            "www.snapchat.com",
            "www.plos.org",
            "www.typeform.com",
            "www.merriam-webster.com",
            "www.messenger.com",
            "www.zaloapp.com",
            "www.amazon.cn",
            "www.us.com",
            "www.att.com",
            "www.softonic.com",
            "www.ltn.com.tw",
            "www.jhu.edu",
            "www.uci.edu",
            "www.wpengine.com",
            "www.entrepreneur.com",
            "www.yimg.com",
            "www.nist.gov",
            "www.quantserve.com",
            "www.ladbible.com",
            "www.khanacademy.org",
            "www.360.com",
            "www.chicagotribune.com",
            "www.ucla.edu",
            "www.scientificamerican.com",
            "www.zol.com.cn",
            "www.agkn.com",
            "www.wiktionary.org",
            "www.jianshu.com",
            "www.gmw.cn",
            "www.pexels.com",
            "www.theconversation.com",
            "www.azure.com",
            "www.sfgate.com",
            "www.chouftv.ma",
            "www.y2mate.com",
            "www.deloitte.com",
            "www.utexas.edu",
            "www.spiegel.de",
            "www.comodoca.com",
            "www.epicgames.com",
            "www.elegantthemes.com",
            "www.slate.com",
            "www.uk.com",
            "www.python.org",
            "www.cam.ac.uk",
            "www.cmu.edu",
            "www.krxd.net",
            "www.elpais.com",
            "www.rambler.ru",
            "www.canada.ca",
            "www.ed.gov",
            "www.ubuntu.com",
            "www.arxiv.org",
            "www.over-blog.com",
            "www.newsweek.com",
            "www.lenovo.com",
            "www.discordapp.com",
            "www.ndtv.com",
            "www.realtor.com",
            "www.telewebion.com",
            "www.intuit.com",
            "www.indiegogo.com",
            "www.xfinity.com",
            "www.feedly.com",
            "www.bmj.com",
            "www.adsafeprotected.com",
            "www.timeanddate.com",
            "www.uchicago.edu",
            "www.hilton.com",
            "www.nps.gov",
            "www.adform.net",
            "www.zerodha.com",
            "www.playstation.com",
            "www.nicovideo.jp",
            "www.qualtrics.com",
            "www.verisign.com",
            "www.newrelic.com",
            "www.oecd.org",
            "www.bbb.org",
            "www.unity3d.com",
            "www.bootstrapcdn.com",
            "www.mirror.co.uk",
            "www.byteoversea.com",
            "www.norton.com",
            "www.uiuc.edu",
            "www.hotjar.com",
            "www.howstuffworks.com",
            "www.shaparak.ir",
            "www.aboutcookies.org",
            "www.asus.com",
            "www.arstechnica.com",
            "www.nba.com",
            "www.douban.com",
            "www.qz.com",
            "www.asos.com",
            "www.sfx.ms",
            "www.thesun.co.uk",
            "www.heavy.com",
            "www.criteo.net",
            "www.biomedcentral.com",
            "www.op.gg",
            "www.fb.me",
            "www.weforum.org",
            "www.mgid.com",
            "www.sberbank.ru",
            "www.visualstudio.com",
            "www.parallels.com",
            "www.purdue.edu",
            "www.cbslocal.com",
            "www.deepl.com",
            "www.tapad.com",
            "www.medicalnewstoday.com",
            "www.fbsbx.com",
            "www.toutiao.com",
            "www.turn.com",
            "www.chron.com",
            "www.altervista.org",
            "www.gamepedia.com",
            "www.glassdoor.com",
            "www.pcmag.com",
            "www.gosuslugi.ru",
            "www.blackboard.com",
            "www.kumparan.com",
            "www.mercadolivre.com.br",
            "www.kakao.com",
            "www.bizjournals.com",
            "www.digitaltrends.com",
            "www.capitalone.com",
            "www.ny.gov",
            "www.hotstar.com",
            "www.afternic.com",
            "www.fidelity.com",
            "www.isnssdk.com",
            "www.trafficmanager.net",
            "www.sahibinden.com",
            "www.mlb.com",
            "www.center4family.com",
            "www.bankofamerica.com",
            "www.bet365.com",
            "www.si.edu",
            "www.ftc.gov",
            "www.blogspot.co.uk",
            "www.apa.org",
            "www.merdeka.com",
            "www.tencent.com",
            "www.ign.com",
            "www.chess.com",
            "www.icicibank.com",
            "www.idntimes.com",
            "www.cnnic.cn",
            "www.zend.com",
            "www.orange.fr",
            "www.a1sewcraft.com",
            "www.netscape.com",
            "www.politico.com",
            "www.fao.org",
            "www.zalo.me",
            "www.crwdcntrl.net",
            "www.istockphoto.com",
            "www.umeng.com",
            "www.patria.org.ve",
            "www.cctv.com",
            "www.icio.us",
            "www.dictionary.com",
            "www.sectigo.com",
            "www.fastly.net",
            "www.techradar.com",
            "www.nyu.edu",
            "www.appcenter.ms",
            "www.mercadolibre.com.ar",
            "www.dedecms.com",
            "www.smh.com.au",
            "www.ow.ly",
            "www.nydailynews.com",
            "www.quizlet.com",
            "www.dropcatch.com",
            "www.thepiratebay.org",
            "www.autodesk.com",
            "www.usc.edu",
            "www.moneycontrol.com",
            "www.news.com.au",
            "www.manoramaonline.com",
            "www.gstatic.com",
            "www.barnesandnoble.com",
            "www.americanexpress.com",
            "www.bitnami.com",
            "www.mercadolibre.com.mx",
            "www.ning.com",
            "www.sakura.ne.jp",
            "www.openstreetmap.org",
            "www.appspot.com",
            "www.jiameng.com",
            "www.googleblog.com",
            "www.joomla.org",
            "www.spotxchange.com",
            "www.geeksforgeeks.org",
            "www.house.gov",
            "www.xing.com",
            "www.livescience.com",
            "www.foursquare.com",
            "www.oliveogrill.com",
            "www.pnas.org",
            "www.genius.com",
            "www.duke.edu",
            "www.telegram.me",
            "www.fontawesome.com",
            "www.inquirer.net",
            "www.reverso.net",
            "www.instructables.com",
            "www.allegro.pl",
            "www.venturebeat.com",
            "www.marca.com",
            "www.google.co.kr",
            "www.apnews.com",
            "www.81.cn",
            "www.prezi.com",
            "www.3lift.com",
            "www.vmware.com",
            "www.ea.com",
            "www.google.gr",
            "www.ebay-kleinanzeigen.de",
            "www.lifehacker.com",
            "www.enable-javascript.com",
            "www.secureserver.net",
            "www.verizon.com",
            "www.census.gov",
            "www.variety.com",
            "www.chinanews.com",
            "www.wellnowuc.com",
            "www.umd.edu",
            "www.acs.org",
            "www.themeisle.com",
            "www.usgs.gov",
            "www.exelator.com",
            "www.uber.com",
            "www.ria.ru",
            "www.adjust.com",
            "www.namu.wiki",
            "www.hespress.com",
            "www.mckinsey.com",
            "www.imageshack.us",
            "www.angelfire.com",
            "www.aliyuncs.com",
            "www.aljazeera.com",
            "www.pikiran-rakyat.com",
            "www.mi.com",
            "www.earthlink.net",
            "www.thesaurus.com",
            "www.ufl.edu",
            "www.abs-cbn.com",
            "www.panamacityjuniors.com",
            "www.msu.edu",
            "www.urbandictionary.com",
            "www.jstor.org",
            "www.thefreedictionary.com",
            "www.express.co.uk",
            "www.youdao.com",
            "www.pewresearch.org",
            "www.emofid.com",
            "www.ucsd.edu",
            "www.teads.tv",
            "www.wildberries.ru",
            "www.about.me",
            "www.jpnn.com",
            "www.lijit.com",
            "www.mookie1.com",
            "www.adp.com",
            "www.fast.com",
            "www.senate.gov",
            "www.brilio.net",
            "www.hm.com",
            "www.duolingo.com",
            "www.rollingstone.com",
            "www.thehill.com",
            "www.g.page",
            "www.google.com.ua",
            "www.hurriyet.com.tr",
            "www.doubleverify.com",
            "www.postgresql.org",
            "www.alexa.com",
            "www.upwork.com",
            "www.atlassian.com",
            "www.thedailybeast.com",
            "www.tudou.com",
            "www.crunchyroll.com",
            "www.mixpanel.com",
            "www.jotform.com",
            "www.unc.edu",
            "www.jsdelivr.net",
            "www.xiaomi.com",
            "www.goo.ne.jp",
            "www.branch.io",
            "www.9gag.com",
            "www.albawabhnews.com",
            "www.kaspersky.com",
            "www.vnexpress.net",
            "www.northwestern.edu",
            "www.thelancet.com",
            "www.hatena.ne.jp",
            "www.linktr.ee",
            "www.pharmacy-canadian-order.com",
            "www.ouedkniss.com",
            "www.edgesuite.net",
            "www.nikkei.com",
            "www.in.gr",
            "www.focus.cn",
            "www.leboncoin.fr",
            "www.lowes.com",
            "www.gartner.com",
            "www.wattpad.com",
            "www.discord.gg",
            "www.sky.com",
            "www.creativecdn.com",
            "www.thetimes.co.uk",
            "www.illinois.edu",
            "www.gitlab.com",
            "www.ssl-images-amazon.com",
            "www.proofpoint.com",
            "www.miit.gov.cn",
            "www.sonhoo.com",
            "www.slashdot.org",
            "www.theglobeandmail.com",
            "www.e2ro.com",
            "www.redbubble.com",
            "www.namecheap.com",
            "www.java.com",
            "www.gismeteo.ru",
            "www.media.net",
            "www.apachefriends.org",
            "www.zippyshare.com",
            "www.jiathis.com",
            "www.bls.gov",
            "www.gmx.net",
            "www.ebay.com.au",
            "www.scmp.com",
            "www.playinguphockey.com",
            "www.ensonhaber.com",
            "www.stripe.com",
            "www.kapanlagi.com",
            "www.hatenablog.com",
            "www.withgoogle.com",
            "www.smartadserver.com",
            "www.oppomobile.com",
            "www.gamespot.com",
            "www.xe.com",
            "www.today.com",
            "www.jamanetwork.com",
            "www.itu.int",
            "www.utoronto.ca",
            "www.sec.gov",
            "www.rozariatrust.net",
            "www.newscientist.com",
            "www.shopee.co.id",
            "www.hindustantimes.com",
            "www.shopee.vn",
            "www.arizona.edu",
            "www.privacyshield.gov",
            "www.mercari.com",
            "www.wp.me",
            "www.yts.mx",
            "www.wufoo.com",
            "www.lemonde.fr",
            "www.hhs.gov",
            "www.zadn.vn",
            "www.alwafd.news",
            "www.asu.edu",
            "www.siemens.com",
            "www.nhk.or.jp",
            "www.mzstatic.com",
            "www.trendyol.com",
            "www.wunderground.com",
            "www.ebc.net.tw",
            "www.xbox.com",
            "www.qoo10.sg",
            "www.tutorialspoint.com",
            "www.hollywoodreporter.com",
            "www.globalsign.com",
            "www.automattic.com",
            "www.costco.com",
            "www.livestream.com",
            "www.dotomi.com",
            "www.fool.com",
            "www.google.nl",
            "www.getbootstrap.com",
            "www.onenote.net",
            "www.efu.com.cn",
            "www.infobae.com",
            "www.mitre.org",
            "www.haofang.net",
            "www.chesscoachcentral.com",
            "www.boston.com",
            "www.openssl.org",
            "www.tsetmc.com",
            "www.td.com",
            "www.divar.ir",
            "www.mixcloud.com",
            "www.people.com",
            "www.w.org",
            "www.drupal.org",
            "www.google.ro",
            "www.scoverage.org",
            "www.mapquest.com",
            "www.1rx.io",
            "www.w55c.net",
            "www.metropolitanbaptistchurch.org",
            "www.ustream.tv",
            "www.repubblica.it",
            "www.nejm.org",
            "www.m.me",
            "www.lenta.ru",
            "www.docker.com",
            "www.naver.jp",
            "www.googleapis.com",
            "www.dcard.tw",
            "www.freebsd.org",
            "www.youporn.com",
            "www.moodle.org",
            "www.adsymptotic.com",
            "www.eastmoney.com",
            "www.dbs.com.sg",
            "www.rediff.com",
            "www.indiamart.com",
            "www.uspto.gov",
            "www.zemanta.com",
            "www.citi.com",
            "www.borna.news",
            "www.newegg.com",
            "www.sony.com",
            "www.elbalad.news",
            "www.techtarget.com",
            "www.filimo.com",
            "www.icann.org",
            "www.dhl.com",
            "www.imrworldwide.com",
            "www.edx.org",
            "www.metro.co.uk",
            "www.amap.com",
            "www.hexun.com",
            "www.eff.org",
            "www.uptodown.com"};

    ArrayList<String> Sites;
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
                wifi_name = wifiDataModel.getOrg() + " " + wifiDataModel.getCompany().getDomain();
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
                    Sites = new ArrayList<>(Arrays.asList(sites).subList(0, Integer.parseInt(String.valueOf(n.getText()))));
                    Toast.makeText(Latency.this, "Latency results will be saved to ping.csv", Toast.LENGTH_SHORT).show();

                    //Remove pre-existing table, and cancel aysnc task if previously running:
                    if (asyncPing != null && asyncPing.getStatus() != AsyncTask.Status.FINISHED) {
                        Log.i("asyncPing!=null-", "set to true");
                        asyncPing.cancel(true);
                    }
                    tl.removeAllViews();
                    /**Start Pinging, call async function**/
                    addHeaders();
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
             Random random = new Random();
             int randomNumber = random.nextInt(999999999);
             int doc_ser = randomNumber;

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
                        writeCsv(results, site.get(i), "1");

                        String[] resList = results.split(",");
                        m.put("min",resList[4]);
                        m.put("avg",resList[5]);
                        m.put("max",resList[6]);

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
    public void addHeaders(){

        /** Create a TableRow dynamically **/

        tr = new TableRow(this);

        tr.setLayoutParams(new LayoutParams(

                LayoutParams.MATCH_PARENT,

                LayoutParams.WRAP_CONTENT));

        /** Creating a TextView to add to the newRow **/

        TextView newRow = new TextView(this);

        newRow.setText("Tranco Top 100:");

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

    private void saveData(int doc_ser) {
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
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:     // api< 8: replace by 11
                case TelephonyManager.NETWORK_TYPE_GSM:      // api<25: replace by 16
                    format = String.format("2G / %s", manager.getNetworkOperatorName());
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
                    format = String.format("3G / %s", manager.getNetworkOperatorName());
                    return format;
                case TelephonyManager.NETWORK_TYPE_LTE:      // api<11: replace by 13
                case TelephonyManager.NETWORK_TYPE_IWLAN:    // api<25: replace by 18
                case 19: // LTE_CA
                    format = String.format("4G / %s", manager.getNetworkOperatorName());
                    return format;
                case TelephonyManager.NETWORK_TYPE_NR:       // api<29: replace by 20
                    format = String.format("5G / %s", manager.getNetworkOperatorName());
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
