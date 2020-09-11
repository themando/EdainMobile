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

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class TraceActivity extends Activity {

    public static final String tag = "TraceroutePing";
    public static final String[] TRANCO_TOP_10 = {
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
            "www.youtu.be",
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
            "www.goo.gl",
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
            "www.ytimg.com",
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
            "www.t.co",
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
            "www.panda.tv",
            "www.naver.com",
            "www.paypal.com",
            "www.googleadservices.com",
            "www.aliexpress.com",
            "www.archive.org",
            "www.bbc.com",
            "www.cloudflare.com",
            "www.googlesyndication.com",
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
            "www.google.cn",
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
            "www.t.me",
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
            "www.6.cn",
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

    private Button buttonLaunch, buttonExport, buttonTranco;
    private EditText editTextPing, editTextFile, editTrancoNum;
    private ProgressBar progressBarPing;
    private ListView listViewTraceroute;
    private TraceListAdapter traceListAdapter;
    private String fileName;

    private TracerouteWithPing tracerouteWithPing;
    private final int maxTtl = 255;
    // Storage Permissions

    private List<TracerouteContainer> traces;
    private static final String FILE_NAME = "trace_ping.csv";

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
                    Toast.makeText(TraceActivity.this, "Trace routes will be saved at: \n" + getFilesDir() + "/" + fileName, Toast.LENGTH_SHORT).show();
                    traces.clear();
                    traceListAdapter.notifyDataSetChanged();
                    startProgressBar();
                    hideSoftwareKeyboard(editTextPing);
                    tracerouteWithPing.executeTraceroute(editTextPing.getText().toString(), maxTtl);
                }
            }
        });

        buttonTranco.setOnClickListener(new OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (editTrancoNum.getText().length() == 0) {
                    Toast.makeText(TraceActivity.this, "Please Enter a Number", Toast.LENGTH_SHORT).show();
                } else {
                    if (editTextFile.getText().length() != 0) {
                        fileName = String.valueOf(editTextFile.getText());
                    }
                    Toast.makeText(TraceActivity.this, "Trace routes will be saved to trace_ping.csv", Toast.LENGTH_SHORT).show();
                    traces.clear();
                    traceListAdapter.notifyDataSetChanged();
                    startProgressBar();
                    editTextPing.setFocusable(false);
                    editTextPing.setText("TRANCO " + editTrancoNum.getText());
                    ArrayList<String> sites = new ArrayList<>();
                    sites.addAll(Arrays.asList(TRANCO_TOP_10).subList(0, Integer.parseInt(String.valueOf(editTrancoNum.getText()))));
                    tracerouteWithPing.executeTrancoTraceroute(maxTtl, sites);
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
