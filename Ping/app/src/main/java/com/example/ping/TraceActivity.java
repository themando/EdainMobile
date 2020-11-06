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
	public static final String[] TRANCO_TOP_10 = new String[]{"google.com", "facebook.com", "youtube.com", "microsoft.com", "twitter.com", "tmall.com", "instagram.com", "qq.com", "linkedin.com", "apple.com", "baidu.com", "wikipedia.org", "live.com", "netflix.com", "sohu.com", "yahoo.com", "amazon.com", "doubleclick.net", "taobao.com", "googletagmanager.com", "360.cn", "pinterest.com", "adobe.com", "youtu.be", "vimeo.com", "jd.com", "reddit.com", "wordpress.com", "zoom.us", "office.com", "weibo.com", "bing.com", "sina.com.cn", "goo.gl", "microsoftonline.com", "github.com", "bit.ly", "amazonaws.com", "blogspot.com", "vk.com", "wordpress.org", "xinhuanet.com", "fbcdn.net", "tumblr.com", "mozilla.org", "google-analytics.com", "msn.com", "nytimes.com", "whatsapp.com", "flickr.com", "okezone.com", "dropbox.com", "gravatar.com", "soundcloud.com", "europa.eu", "alipay.com", "skype.com", "godaddy.com", "nih.gov", "myshopify.com", "csdn.net", "yahoo.co.jp", "cnn.com", "medium.com", "t.co", "ebay.com", "apache.org", "twitch.tv", "w3.org", "theguardian.com", "google.com.hk", "office365.com", "spotify.com", "macromedia.com", "naver.com", "forbes.com", "imdb.com", "bongacams.com", "googlevideo.com", "bbc.co.uk", "sourceforge.net", "zhanqi.tv", "paypal.com", "cloudflare.com", "stackoverflow.com", "bbc.com", "aliexpress.com", "archive.org", "amazon.in", "googleadservices.com", "github.io", "googlesyndication.com", "yandex.ru", "google.co.in", "china.com.cn", "virginmedia.com", "weebly.com", "digicert.com", "amazon.co.jp", "tianya.cn", "who.int", "creativecommons.org", "issuu.com", "washingtonpost.com", "windows.net", "imgur.com", "etsy.com", "livejasmin.com", "icloud.com", "slideshare.net", "tribunnews.com", "oracle.com", "amazon.co.uk", "chaturbate.com", "php.net", "cdc.gov", "mail.ru", "reuters.com", "instructure.com", "wix.com", "google.de", "tinyurl.com", "wikimedia.org", "app-measurement.com", "huanqiu.com", "aparat.com", "pornhub.com", "wsj.com", "wp.com", "1688.com", "yy.com", "google.co.jp", "bloomberg.com", "cnet.com", "google.cn", "sciencedirect.com", "ok.ru", "youtube-nocookie.com", "businessinsider.com", "opera.com", "163.com", "harvard.edu", "mit.edu", "gnu.org", "google.com.br", "windows.com", "espn.com", "so.com", "outlook.com", "researchgate.net", "dailymail.co.uk", "sogou.com", "17ok.com", "alibaba.com", "ibm.com", "forms.gle", "rakuten.co.jp", "amazon.de", "go.com", "samsung.com", "stanford.edu", "wiley.com", "google.co.uk", "booking.com", "usatoday.com", "bitly.com", "hp.com", "flipkart.com", "list-manage.com", "blogger.com", "aol.com", "jrj.com.cn", "canva.com", "telegraph.co.uk", "indeed.com", "mama.cn", "cnbc.com", "surveymonkey.com", "fb.com", "msedge.net", "cpanel.net", "nginx.org", "ntp.org", "opendns.com", "fandom.com", "nature.com", "eventbrite.com", "dailymotion.com", "nasa.gov", "myspace.com", "kompas.com", "huffingtonpost.com", "behance.net", "indiatimes.com", "google.ru", "google.fr", "aaplimg.com", "google.es", "t.me", "walmart.com", "hicloud.com", "addthis.com", "udemy.com", "npr.org", "time.com", "foxnews.com", "un.org", "xvideos.com", "hao123.com", "detik.com", "freepik.com", "aliyun.com", "springer.com", "youku.com", "slack.com", "ted.com", "pixnet.net", "google.it", "sharepoint.com", "cpanel.com", "scribd.com", "grid.id", "www.gov.uk", "yelp.com", "ca.gov", "ettoday.net", "mysql.com", "wired.com", "salesforce.com", "nginx.com", "hugedomains.com", "daum.net", "roblox.com", "bilibili.com", "doi.org", "wetransfer.com", "scorecardresearch.com", "cnblogs.com", "babytree.com", "goodreads.com", "independent.co.uk", "thestartmagazine.com", "zillow.com", "google.com.sg", "gmail.com", "stackexchange.com", "debian.org", "intel.com", "themeforest.net", "wikihow.com", "squarespace.com", "tripadvisor.com", "techcrunch.com", "tiktok.com", "mailchimp.com", "free.fr", "force.com", "android.com", "addtoany.com", "zendesk.com", "livejournal.com", "berkeley.edu", "google.ca", "hotstar.com", "okta.com", "grammarly.com", "soso.com", "speedtest.net", "onlinesbi.com", "taboola.com", "mediafire.com", "xhamster.com", "healthline.com", "latimes.com", "6.cn", "line.me", "shutterstock.com", "unsplash.com", "quora.com", "statcounter.com", "duckduckgo.com", "amzn.to", "webmd.com", "google.com.tw", "chase.com", "amazon.ca", "webex.com", "w3schools.com", "craigslist.org", "ikea.com", "cisco.com", "kickstarter.com", "discord.com", "theverge.com", "digg.com", "ft.com", "nationalgeographic.com", "giphy.com", "jimdo.com", "cornell.edu", "wa.me", "deviantart.com", "ietf.org", "tradingview.com", "sindonews.com", "theatlantic.com", "loc.gov", "telegram.org", "adsrvr.org", "eastday.com", "tandfonline.com", "weather.com", "cbsnews.com", "washington.edu", "tokopedia.com", "academia.edu", "rubiconproject.com", "crashlytics.com", "shopify.com", "sitemaps.org", "google.com.mx", "padlet.com", "about.com", "buzzfeed.com", "investopedia.com", "dell.com", "rednet.cn", "akismet.com", "box.com", "criteo.com", "arnebrachhold.de", "pixabay.com", "ilovepdf.com", "cambridge.org", "savefrom.net", "stumbleupon.com", "zhihu.com", "quizlet.com", "pubmatic.com", "marriott.com", "metropoles.com", "coursera.org", "symantec.com", "appsflyer.com", "whatsapp.net", "iqiyi.com", "tiktokv.com", "primevideo.com", "launchpad.net", "typepad.com", "zoho.com", "disqus.com", "uol.com.br", "google.com.vn", "fiverr.com", "google.com.tr", "huffpost.com", "marketwatch.com", "bestbuy.com", "princeton.edu", "pki.goog", "state.gov", "pinimg.com", "usnews.com", "haosou.com", "pikiran-rakyat.com", "prnewswire.com", "youm7.com", "liputan6.com", "nbcnews.com", "steampowered.com", "bet9ja.com", "mailchi.mp", "trello.com", "feedburner.com", "fc2.com", "digikala.com", "ampproject.org", "economist.com", "msftconnecttest.com", "bandcamp.com", "mashable.com", "webs.com", "wendyssubway.com", "sciencemag.org", "globo.com", "avito.ru", "hubspot.com", "mozilla.com", "change.org", "pbs.org", "setn.com", "tistory.com", "advertising.com", "51.la", "youronlinechoices.com", "oup.com", "constantcontact.com", "casalemedia.com", "whitehouse.gov", "ebay.de", "worldometers.info", "ups.com", "ebay.co.uk", "target.com", "wellsfargo.com", "usda.gov", "avast.com", "airbnb.com", "alicdn.com", "columbia.edu", "jquery.com", "unesco.org", "moatads.com", "evernote.com", "hulu.com", "nypost.com", "eepurl.com", "umich.edu", "noaa.gov", "investing.com", "homedepot.com", "aboutads.info", "plesk.com", "patreon.com", "outbrain.com", "huawei.com", "rt.com", "teamviewer.com", "varzesh3.com", "amazon.fr", "britannica.com", "arcgis.com", "engadget.com", "google.co.th", "sagepub.com", "statista.com", "hola.org", "trustpilot.com", "psu.edu", "tripod.com", "hbr.org", "amazon.it", "lazada.sg", "accuweather.com", "vice.com", "geocities.com", "steamcommunity.com", "suara.com", "bukalapak.com", "zdnet.com", "google.com.eg", "cbc.ca", "gofundme.com", "mayoclinic.org", "google.com.sa", "fedex.com", "google.com.ar", "epa.gov", "abc.net.au", "yale.edu", "breitbart.com", "gotowebinar.com", "allaboutcookies.org", "getpocket.com", "guardian.co.uk", "elsevier.com", "sun.com", "nvidia.com", "amazon.es", "hdfcbank.com", "wpengine.com", "irs.gov", "meetup.com", "vox.com", "nike.com", "upenn.edu", "google.co.id", "sciencedaily.com", "redhat.com", "usps.com", "networkadvertising.org", "51sole.com", "patch.com", "smallpdf.com", "ask.com", "intuit.com", "talktalk.co.uk", "google.pl", "hootsuite.com", "bidswitch.net", "photobucket.com", "blackboard.com", "khanacademy.org", "newyorker.com", "business.site", "getadblock.com", "worldbank.org", "vkontakte.ru", "psychologytoday.com", "gizmodo.com", "ieee.org", "dribbble.com", "example.com", "aliexpress.ru", "businesswire.com", "google.com.au", "iso.org", "everesttech.net", "softonic.com", "heavy.com", "fastcompany.com", "merriam-webster.com", "umn.edu", "dw.com", "xnxx.com", "ox.ac.uk", "redd.it", "inc.com", "fortune.com", "gosuslugi.ru", "wiktionary.org", "chinadaily.com.cn", "chouftv.ma", "oreilly.com", "mathtag.com", "nflximg.com", "bestaryua.com", "bluekai.com", "wisc.edu", "ed.gov", "namnak.com", "momoshop.com.tw", "snapchat.com", "nist.gov", "att.com", "iqbroker.com", "feedly.com", "plos.org", "amazon.cn", "uci.edu", "ndtv.com", "ameblo.jp", "www.gov.cn", "ucla.edu", "jhu.edu", "wikia.com", "typeform.com", "entrepreneur.com", "messenger.com", "wayfair.com", "scientificamerican.com", "deepl.com", "canada.ca", "bet365.com", "elegantthemes.com", "letsencrypt.org", "chegg.com", "theconversation.com", "afternic.com", "newrelic.com", "y2mate.com", "chicagotribune.com"};
	public static long doc_ser;
	private Button buttonLaunch, buttonTranco;
	private EditText editTextPing, editTrancoNum;
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
//		this.buttonExport = (Button) findViewById(R.id.buttonExport);
		this.buttonTranco = (Button) findViewById(R.id.buttonTranco);
		this.editTextPing = (EditText) this.findViewById(R.id.editTextPing);
//		this.editTextFile = (EditText) this.findViewById(R.id.editTextFile);
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
//					if (editTextFile.getText().length() != 0) {
//						fileName = String.valueOf(editTextFile.getText());
//					}
					String datetime = new SimpleDateFormat("yyMMddHHmm").format(new Date());
					 doc_ser = Long.parseLong(datetime);
//					Toast.makeText(TraceActivity.this, "Trace routes will be saved at: \n" + getFilesDir() + "/" + fileName, Toast.LENGTH_SHORT).show();
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
//				Toast.makeText(TraceActivity.this, "Running Traceroutes", Toast.LENGTH_SHORT).show();
				editTextPing.setText("TRANCO " + editTrancoNum.getText());
				if ((editTrancoNum.getText().length() == 0)) {
					Toast.makeText(TraceActivity.this, "Please Enter a Number", Toast.LENGTH_SHORT).show();
				} else {
//					if (editTextFile.getText().length() != 0) {
//						fileName = String.valueOf(editTextFile.getText());
//					}
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
//					Toast.makeText(TraceActivity.this, "Trace routes will be saved to trace_ping.csv", Toast.LENGTH_SHORT).show();
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

//		buttonExport.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				export();
//			}
//		});

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

//			if (position % 2 == 1) {
//				convertView.setBackgroundResource(R.drawable.table_odd_lines);
//			} else {
//				convertView.setBackgroundResource(R.drawable.table_pair_lines);
//			}

			if (currentTrace.isSuccessful()) {
				holder.imageViewStatusPing.setImageResource(R.drawable.check);
			} else {
				holder.imageViewStatusPing.setImageResource(R.drawable.cross);
			}

			holder.textViewNumber.setText((position + 1) + "");
			holder.textViewIp.setText("[" + currentTrace.getURL() + "]" + "\n" + currentTrace.getHostname());
			holder.textViewLatency.setText(currentTrace.latency);
			if(currentTrace.latency.contains("Oops")){
				holder.textViewLatency.setTextColor(Color.parseColor("#ff0000"));
			}else{
				holder.textViewLatency.setTextColor(Color.parseColor("#00ff00"));
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