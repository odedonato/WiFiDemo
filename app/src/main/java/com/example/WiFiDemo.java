package com.example;

import java.text.DateFormat;
//import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
//import java.io.OutputStream;
import java.lang.StringBuilder;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
//import android.net.wifi.WifiConfiguration;
//import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class WiFiDemo extends Activity implements OnClickListener {
	private static final String TAG = "WiFiDemo";
	WifiManager wifi;
	BroadcastReceiver receiver;

	TextView textStatus;
	EditText appendText;
	Button buttonSave;
	Handler h=new Handler();
	
	//ArrayList listArray = new ArrayList();
	ArrayList<String> listArray = new ArrayList<String>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Setup UI
		textStatus = (TextView) findViewById(R.id.textStatus);
		buttonSave = (Button) findViewById(R.id.buttonSave);
		appendText = (EditText) findViewById(R.id.entry);
		buttonSave.setOnClickListener(this);
		
		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		// Register Broadcast Receiver
		registerReceiver(receiver, new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Log.d(TAG, "onCreate()");
		
		// List available networks
		//textStatus.setText("\n\n" + wifi.getScanResults());
		
		h.post(new Runnable(){
			@Override
				public void run() {
					// call your function
					h.postDelayed(this,10000);
					listArray.clear();
					String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
					textStatus.setText("List available networks @ " + currentDateTimeString + ":");
					listArray.add("List available networks @ " + currentDateTimeString + ":");
					List<ScanResult> results = wifi.getScanResults();
					for (ScanResult result : results) {
						textStatus.append("\n\n" + result.toString());
				        listArray.add(result.toString());
						}
					//System.out.println(listArray);
					}
			});
	}

	//@Override
	//public void onStop() {
	//	unregisterReceiver(receiver);
	//}

	public void onClick(View view) {
		if (view.getId() == R.id.buttonSave) {
			//SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy_HHmm");
			String format = "ddMMyyyy_HHmm";
			SimpleDateFormat df = new SimpleDateFormat(format, Locale.ITALIAN);
			String saveDateTimeString = df.format(new Date());
			String editTextStr = appendText.getText().toString();
			Log.d(TAG, "onClick() save to file: " + Environment.getExternalStorageDirectory() + File.separator + editTextStr + saveDateTimeString + ".txt");
			File file = new File(Environment.getExternalStorageDirectory() + File.separator + editTextStr + saveDateTimeString + ".txt");
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			StringBuilder out = new StringBuilder();
			for (Object o : listArray)
			{
			  out.append(o.toString());
			  out.append("\n");
			}
				try {
				 FileOutputStream fo = new FileOutputStream(file);
				 ObjectOutputStream oos = new ObjectOutputStream(fo);
			     oos.writeObject(out.toString());
			     oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			Toast.makeText(this, "File Saved into:\n" + Environment.getExternalStorageDirectory() + File.separator + editTextStr + saveDateTimeString + ".txt " , Toast.LENGTH_LONG).show();
			//return out.toString();
			System.out.println(out.toString());
		}
	}
}	