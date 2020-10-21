/*
The code given at https://github.com/olivierg13/TraceroutePing was used as a reference
 */

package com.example.ping;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.example.ping.Wifi_Network_Info.model.WifiDataModel;
import com.example.ping.Wifi_Network_Info.viewmodel.WifiViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class TraceActivity extends AppCompatActivity {

	public static final String tag = "TraceroutePing";
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

	public static long doc_ser;
	private Button buttonLaunch, buttonExport, buttonTranco;
	private EditText editTextPing, editTextFile, editTrancoNum;
	private ProgressBar progressBarPing;
	private ListView listViewTraceroute;
	private TraceListAdapter traceListAdapter;
	private String fileName;

	private TracerouteWithPing tracerouteWithPing;
	private final int maxTtl = 250;
	static String wifi_name = "Time Limit Exceeded to get Wifi Network from API";
	String countryCode = "No Country Code";
	// Storage Permissions

	private List<TracerouteContainer> traces;
	private static final String FILE_NAME = "trace_ping.csv";
	static WifiViewModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trace);

		this.tracerouteWithPing = new TracerouteWithPing(this);
		this.traces = new ArrayList<TracerouteContainer>();

		this.buttonLaunch = (Button) this.findViewById(R.id.buttonLaunch);
		this.buttonExport = (Button) findViewById(R.id.buttonExport);
		this.buttonTranco = (Button) findViewById(R.id.buttonTranco);
		this.editTextPing = (EditText) this.findViewById(R.id.editTextPing);
		this.editTextFile = (EditText) this.findViewById(R.id.editTextFile);
		this.editTrancoNum = (EditText) this.findViewById(R.id.editTrancoNum);
		this.fileName = FILE_NAME;
		this.listViewTraceroute = (ListView) this.findViewById(R.id.listViewTraceroute);
		this.progressBarPing = (ProgressBar) this.findViewById(R.id.progressBarPing);

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

		initView();
	}

	/**
	 * initView, init the main view components (action, adapter...)
	 */
	private void initView() {
		traceListAdapter = new TraceListAdapter(getApplicationContext());
		listViewTraceroute.setAdapter(traceListAdapter);

		buttonLaunch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editTextPing.getText().length() == 0) {
					Toast.makeText(TraceActivity.this, getString(R.string.no_text), Toast.LENGTH_SHORT).show();
				} else {
					if (editTextFile.getText().length() != 0) {
						fileName = String.valueOf(editTextFile.getText());
					}
					String datetime = new SimpleDateFormat("yyMMddHHmm").format(new Date());
					 doc_ser = Long.parseLong(datetime);
					Toast.makeText(TraceActivity.this, "Trace routes will be saved at: \n" + getFilesDir() + "/" + fileName, Toast.LENGTH_SHORT).show();
					traces.clear();
					traceListAdapter.notifyDataSetChanged();
					startProgressBar();
					hideSoftwareKeyboard(editTextPing);
					try {
						tracerouteWithPing.executeTraceroute(editTextPing.getText().toString(), maxTtl);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		buttonTranco.setOnClickListener(new OnClickListener() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onClick(View v) {
				Toast.makeText(TraceActivity.this, "Running Traceroutes", Toast.LENGTH_SHORT).show();
				editTextPing.setText("TRANCO " + editTrancoNum.getText());
				if ((editTrancoNum.getText().length() == 0)) {
					Toast.makeText(TraceActivity.this, "Please Enter a Number", Toast.LENGTH_SHORT).show();
				} else {
					if (editTextFile.getText().length() != 0) {
						fileName = String.valueOf(editTextFile.getText());
					}
					String datetime = new SimpleDateFormat("yyMMddHHmm").format(new Date());
					doc_ser = Long.parseLong(datetime);
					ArrayList<String> sites = new ArrayList<>();
					for (int i = 0; i < Integer.parseInt(String.valueOf(editTrancoNum.getText())); i++){
						String[] list = TRANCO_TOP_10[i].split("\\.");

						String locale = getResources().getConfiguration().getLocales().get(0).toString().toLowerCase();
						if (locale.charAt(locale.length() - 1) == '_') {
							locale = locale.substring(0, locale.length() - 1);
						} else {
							locale = locale.replace('_', '-');
						}

						if (list[list.length - 1].length() == 2) {
							if (list[list.length - 1].equals(countryCode)) {
								sites.add(TRANCO_TOP_10[i]);
							}
						} else if (list[0].length() == 5 && list[0].charAt(2) == '-') {
							if (list[0].equals(locale)) {
								sites.add(TRANCO_TOP_10[i]);
							}
						} else {
							sites.add(TRANCO_TOP_10[i]);
						}
					}
					Toast.makeText(TraceActivity.this, "Trace routes will be saved to trace_ping.csv", Toast.LENGTH_SHORT).show();
					traces.clear();
					traceListAdapter.notifyDataSetChanged();
					try {
						tracerouteWithPing.executeTrancoTraceroute(maxTtl, sites);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		buttonExport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				export();
			}
		});

	}


	// Export csv file
	public void export() {

		try {
			Context context = getApplicationContext();
			File fileLocation = new File(getFilesDir(), fileName);
			Uri path = FileProvider.getUriForFile(context, "com.example.Ping.fileprovider", fileLocation);
			Intent fileIntent = new Intent(Intent.ACTION_SEND);
			fileIntent.setType("text/csv");
			fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Ping Data");
			fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			fileIntent.putExtra(Intent.EXTRA_STREAM, path);
			startActivity(Intent.createChooser(fileIntent, "Export trace_ping.csv"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void writeCsv(String trace) {
		FileOutputStream fos = null;
		String headers = "Timestamp,URL,IP,Latency\n";
		boolean initCsv = false;

		// Initialize csv file if one does not exist
		File file = new File(getFilesDir() + "/" + fileName);
		if (!file.exists()) {
			initCsv = true;
		}

		try {
			fos = new FileOutputStream(file, true);
			if (initCsv) {
				fos.write(headers.getBytes());
			}
			fos.write(trace.getBytes());
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

	public void refreshList(TracerouteContainer trace) {
		final TracerouteContainer fTrace = trace;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				traces.add(fTrace);
				traceListAdapter.notifyDataSetChanged();
			}
		});
	}

	public class TraceListAdapter extends BaseAdapter {

		private Context context;

		public TraceListAdapter(Context c) {
			context = c;
		}

		public int getCount() {
			return traces.size();
		}

		public TracerouteContainer getItem(int position) {
			return traces.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("SetTextI18n")
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			// first init
			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.item_list_trace, null);

				TextView textViewLatency = (TextView) convertView.findViewById(R.id.textViewLatency);
				TextView textViewNumber = (TextView) convertView.findViewById(R.id.textViewNumber);
				TextView textViewIp = (TextView) convertView.findViewById(R.id.textViewIp);
				ImageView imageViewStatusPing = (ImageView) convertView.findViewById(R.id.imageViewStatusPing);

				// Set up the ViewHolder.
				holder = new ViewHolder();
				holder.textViewLatency = textViewLatency;
				holder.textViewNumber = textViewNumber;
				holder.textViewIp = textViewIp;
				holder.imageViewStatusPing = imageViewStatusPing;

				// Store the holder with the view.
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TracerouteContainer currentTrace = getItem(position);
			@SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss \n dd/MM/yy");
			String format = simpleDateFormat.format(new Date());
			String trace = format + ',' + currentTrace.getURL() + ',' + currentTrace.getHostname() + " (" + currentTrace.getIp() + ")" + ',' + currentTrace.getLatency() + "\n";
			writeCsv(trace);

			if (position % 2 == 1) {
				convertView.setBackgroundResource(R.drawable.table_odd_lines);
			} else {
				convertView.setBackgroundResource(R.drawable.table_pair_lines);
			}

			if (currentTrace.isSuccessful()) {
				holder.imageViewStatusPing.setImageResource(R.drawable.check);
			} else {
				holder.imageViewStatusPing.setImageResource(R.drawable.cross);
			}

			holder.textViewNumber.setText((position + 1) + "");
			holder.textViewNumber.setTextColor(Color.parseColor("#ff0000"));
			holder.textViewIp.setText("[" + currentTrace.getURL() + "]" + "\n" + currentTrace.getHostname());
			holder.textViewLatency.setText(currentTrace.latency);
			if(currentTrace.latency.contains("Oops")){
				holder.textViewLatency.setTextColor(Color.parseColor("#ff0000"));
			}else{
				holder.textViewLatency.setTextColor(Color.parseColor("#ff00ff"));
			}
			return convertView;
		}

		// ViewHolder pattern
		class ViewHolder {
			TextView textViewNumber;
			TextView textViewIp;
			TextView textViewLatency;
			ImageView imageViewStatusPing;
		}
	}

	/**
	 * Hides the keyboard
	 *
	 * @param currentEditText The current selected edittext
	 */
	public void hideSoftwareKeyboard(EditText currentEditText) {
		InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(currentEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void startProgressBar() {
		progressBarPing.setVisibility(View.VISIBLE);
	}

	public void stopProgressBar() {
		progressBarPing.setVisibility(View.GONE);
	}

}