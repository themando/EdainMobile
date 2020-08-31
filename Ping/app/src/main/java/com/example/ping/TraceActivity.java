package com.example.ping;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
					"www.windowsupdate.com",
					"www.linkedin.com"};

	private Button buttonLaunch, buttonExport,buttonTranco;
	private EditText editTextPing,editTextFile;
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
				editTextPing.setFocusable(false);
				if (editTextPing.getText().length() == 0) {
					Toast.makeText(TraceActivity.this, getString(R.string.no_text), Toast.LENGTH_SHORT).show();
				} else {
					if (editTextFile.getText().length() != 0) {
						fileName = String.valueOf(editTextFile.getText());
					}
					Toast.makeText(TraceActivity.this, "Trace routes will be saved at: \n" + getFilesDir()+"/"+fileName, Toast.LENGTH_SHORT).show();
					traces.clear();
					traceListAdapter.notifyDataSetChanged();
					startProgressBar();
					hideSoftwareKeyboard(editTextPing);
					tracerouteWithPing.executeTraceroute(editTextPing.getText().toString(), maxTtl);
				}
				editTextPing.setFocusable(false);
			}
		});

		buttonTranco.setOnClickListener(new OnClickListener() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onClick(View v) {
				Toast.makeText(TraceActivity.this, "Trace routes will be saved to trace_ping.csv", Toast.LENGTH_SHORT).show();
				traces.clear();
				traceListAdapter.notifyDataSetChanged();
				startProgressBar();
				editTextPing.setFocusable(false);
				editTextPing.setText("TRANCO TOP 10");
				tracerouteWithPing.executeTrancoTraceroute(maxTtl,TRANCO_TOP_10);
				editTextPing.setFocusable(true);
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
		String headers = "Timestamp,Server,IP,Time\n";
		boolean initCsv = false;

		// Initialize csv file if one does not exist
		File file = new File(getFilesDir()+"/"+fileName);
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

				TextView textViewNumber = (TextView) convertView.findViewById(R.id.textViewNumber);
				TextView textViewIp = (TextView) convertView.findViewById(R.id.textViewIp);
				TextView textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);
				ImageView imageViewStatusPing = (ImageView) convertView.findViewById(R.id.imageViewStatusPing);

				// Set up the ViewHolder.
				holder = new ViewHolder();
				holder.textViewNumber = textViewNumber;
				holder.textViewIp = textViewIp;
				holder.textViewTime = textViewTime;
				holder.imageViewStatusPing = imageViewStatusPing;

				// Store the holder with the view.
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TracerouteContainer currentTrace = getItem(position);
			@SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss \n dd/MM/yy");
			String format = simpleDateFormat.format(new Date());
			String trace = format + ','  + currentTrace.getURL() + ',' + currentTrace.getHostname() + " (" + currentTrace.getIp() + ")" + ',' + currentTrace.getMs() + "ms" + "\n";
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

			holder.textViewNumber.setText((position+1) + "");
			holder.textViewIp.setText("[" + currentTrace.getURL() + "]" + "\n" + currentTrace.getHostname() + " (" + currentTrace.getIp() + ")");
			holder.textViewTime.setText(currentTrace.getMs() + "ms");
			return convertView;
		}

		// ViewHolder pattern
		class ViewHolder {
			TextView textViewNumber;
			TextView textViewIp;
			TextView textViewTime;
			ImageView imageViewStatusPing;
		}
	}

	/**
	 * Hides the keyboard
	 * 
	 * @param currentEditText
	 *            The current selected edittext
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
