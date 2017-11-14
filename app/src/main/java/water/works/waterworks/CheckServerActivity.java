package water.works.waterworks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.UserManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;

import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.Utility;


public class CheckServerActivity extends Activity {
	static String Tag = "Server activity";
	String result = "";
	Boolean isInternetPresent = false, server_status = false,
			version_status = false;
	SharedPreferences mPreferences;
	String version, web_version, web_path;
	public static final String MyPREFERENCES = "WaterWorks_Version";
	private ProgressDialog pDialog;
	public static final int progress_bar_type = 0;
	public static final String CUSTOM_INTENT = "Aquatics";
	// ///////////////
	private NotificationManager mNotifyManager;
	private Builder mBuilder;
	int id = 1,version_code;
	private boolean mRun;
	CharSequence title;
	Context mContext=this;
	// ///////////////
	boolean installed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_server);
		isInternetPresent = Utility
				.isNetworkConnected(CheckServerActivity.this);
		mPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		if (mPreferences.contains("Version")) {
			version = mPreferences.getString("Version", "1");
			Log.i(Tag, "Version = " + version);
		}
		
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version_code = pInfo.versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		getcount();
//		getAccount();
		
		boolean root_access = mPreferences.getBoolean("root", false);
		installed = mPreferences.getBoolean("installed", false);
		if(root_access==true){
			//			Auto_Launch();
			if(!isAppInstalled("com.updateraqq")){
				copyAssets();
			}
			//			else{
			//				launch_it(mContext);
			//			}
		}else{
			boolean root__access = root_checker();
			if(root__access==true){
				//			Auto_Launch();
				if(!isAppInstalled("com.updateraqq")){
					copyAssets();
				}
				//				else{
				//					launch_it(mContext);
				//				}
			}
		}

		if(installed==false){
			copyAssets();	
		}

		if (isInternetPresent) {
			new AsyscWhichServer().execute();
		} else {
			onDetectNetworkState().show();
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public int getcount(){
		UserManager um = (UserManager) mContext.getSystemService(USER_SERVICE);
		int count = um.getUserCount();
		um.isUserAGoat();
		Log.i("count",""+count);
		System.out.println("Users Count : "+count);
		return count;
	}
	
	public void getAccount(){
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(mContext).getAccounts();
		for (Account account : accounts) {
		    if (emailPattern.matcher(account.name).matches()) {
		        String possibleEmail = account.name;
		        System.out.println("Account : "+possibleEmail);
		    }
		}
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				finish();
			}
		})
		.setPositiveButton("Οk", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		return builder1.create();
	}

	ProgressDialog pd;

	private class AsyscWhichServer extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(CheckServerActivity.this);
			pd.setTitle("Please wait...");
			pd.setMessage("Initializing Server...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					"CheckAccessibility");
			request.addProperty("str", 1);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(
						"http://tempuri.org/CheckAccessibility", envelope); // Calling
				// Web
				// service
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				result = response.toString();
				Log.i("here", "Result : " + response.toString());
			} catch (SocketException e) {
				server_status = true;
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				server_status = true;
				e.printStackTrace();
			} catch (Exception e) {
				server_status = true;
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			/*
			 * Intent intent = new Intent(Intent.ACTION_VIEW);
			 * intent.setDataAndType(Uri.fromFile(new
			 * File(Environment.getExternalStorageDirectory() + "/Download/" +
			 * "Aquatics-1.apk")),"application/vnd.android.package-archive");
			 * startActivity(intent);
			 */
			if (server_status) {
				server_status = false;
				SOAP_CONSTANTS.SOAP_ADDRESS = "http://office.waterworksswimonline.com/WWWebService/Service.asmx?WSDL";
				SOAP_CONSTANTS.Report_Url = "http://office.waterworksswimonline.com/newcode/";
				new CheckVerion().execute();
			} else {
				new CheckVerion().execute();
			}
		}
	}

	private class CheckVerion extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(CheckServerActivity.this);
			pd.setTitle("Please wait...");
			pd.setMessage("Initializing Server...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					"GetLatestVersion");
			// request.addProperty("str", 1);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(
						"http://tempuri.org/GetLatestVersion", envelope); // Calling
				// Web
				// service
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i("here", "Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					version_status = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i(Tag, "mSoapObject3=" + mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jsonArray = jo.getJSONArray("APK");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo1 = jsonArray.getJSONObject(i);
						web_version = jo1.getString("Version");
						web_path = jo1.getString("Path");
					}
					//
				} else {
					version_status = false;
				}
			} catch (SocketException e) {
				server_status = true;
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				server_status = true;
				e.printStackTrace();
			} catch (Exception e) {
				server_status = true;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			if (server_status) {
				onDetectNetworkState().show();
				server_status = false;
			} else {
				if (version_status) {
					version_status = false;
					Editor editor = mPreferences.edit();
					editor.putString("Version", web_version);
					editor.commit();

					if(Integer.parseInt(web_version)>version_code){
						Log.i(Tag, "New version");
						new Thread(new Runnable() {
							public void run() {
								int mCount = 0;
								mRun = true;
								while (mRun) {
									++mCount;
									SystemClock.sleep(1000);
									title = "Downloading: " + mCount % 100
											+ "%";
								}
							}
						}).start();
						mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

						mBuilder = new Builder(
								CheckServerActivity.this);
						mBuilder.setContentTitle("Aquatics")
						.setContentText(title)
						.setSmallIcon(R.drawable.ic_download);

						new DownloadFileFromURL().execute(web_version);
					}else{
						Log.i(Tag, "Same version");
						Intent i = new Intent(getApplicationContext(),
								LoginActivity.class);
						startActivity(i);
						finish();
					}

					//					if (web_version.toString().equalsIgnoreCase(version)) {
					//						Log.i(Tag, "Same version");
					//						Intent i = new Intent(getApplicationContext(),
					//								LoginActivity.class);
					//						startActivity(i);
					//						finish();
					//					} else {
					//						Log.i(Tag, "New version");
					//						new Thread(new Runnable() {
					//							public void run() {
					//								int mCount = 0;
					//								mRun = true;
					//								while (mRun) {
					//									++mCount;
					//									SystemClock.sleep(1000);
					//									title = "Downloading: " + mCount % 100
					//											+ "%";
					//								}
					//							}
					//						}).start();
					//						mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					//
					//						mBuilder = new NotificationCompat.Builder(
					//								CheckServerActivity.this);
					//						mBuilder.setContentTitle("Aquatics")
					//						.setContentText(title)
					//						.setSmallIcon(R.drawable.ic_download);
					//
					//						new DownloadFileFromURL().execute(web_version);
					//					}
				}
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type: // we set this to 0
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Downloading file. Check status in Notification Bar...");
			pDialog.setIndeterminate(false);
			pDialog.setIcon(R.drawable.ic_download);
			/*
			 * pDialog.setMax(100); pDialog.setProgressNumberFormat(null);
			 * pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			 * pDialog.setProgressDrawable(getResources().getDrawable(
			 * R.drawable.custom_progress_bar_horizontal));
			 */
			pDialog.setCancelable(false);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}

	class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//			showDialog(progress_bar_type);
			onProgress().show();
			mBuilder.setProgress(100, 0, false);
			mNotifyManager.notify(id, mBuilder.build());
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(web_path);
				URLConnection conection = url.openConnection();
				conection.connect();

				// this will be useful so that you can show a tipical 0-100%
				// progress bar
				int lenghtOfFile = conection.getContentLength();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				// Output stream
				OutputStream output = new FileOutputStream(
						"sdcard/Download/Aquatics.apk");

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress((int) ((total * 100) / lenghtOfFile));
					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(Integer... progress) {
			// setting progress percentage
			// pDialog.setProgress(Integer.parseInt(progress[0]));
			mBuilder.setProgress(100, progress[0], false);
			mBuilder.setContentText(title);
			mNotifyManager.notify(id, mBuilder.build());
			super.onProgressUpdate(progress);
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			//			dismissDialog(progress_bar_type);
			onProgress().dismiss();
			mBuilder.setContentText("Download complete");
			// Removes the progress bar
			mBuilder.setProgress(0, 0, false);
			mNotifyManager.notify(id, mBuilder.build());
			Toast.makeText(getApplicationContext(), "File downloaded", Toast.LENGTH_LONG)
			.show();

			launch_it(mContext);

			//			boolean root_access = root_checker();
			//			if(root_access==true){
			//				String file = Environment
			//						.getExternalStorageDirectory()
			//						+ "/Download/"
			//						+ "Aquatics.apk";
			//				InstallAPK(file,null,mContext);
			//
			//				//				installNewApk();
			//			}else{
			//				Intent intent = new Intent(Intent.ACTION_VIEW);
			//				intent.setDataAndType(Uri.fromFile(new File(Environment
			//						.getExternalStorageDirectory()
			//						+ "/Download/"
			//						+ "Aquatics.apk")),
			//						"application/vnd.android.package-archive");
			//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			//				startActivity(intent);
			//				finish();
			//
			//			}
		}
	}

	public static void InstallAPK(String filename, File direct_file, Context context){
		File file = new File(filename);
		int installResult = -1337;


		if(file.exists()){
			try {   
				String command;

				if(filename.toString().contains("Aquatics")){
					App_Installed(context);
				}

				Process proc = Runtime.getRuntime().exec("su -c pm install -r " + filename);
				proc.waitFor();


				if (proc != null) {
					try {
						installResult = proc.waitFor();
					} catch(InterruptedException e) {
						// Handle InterruptedException the way you like.
					}
					if (installResult == 0) {
						// Success!
						if(filename.toString().contains("Updater")){
							launch_it(context);
						}else if(filename.toString().contains("Aquatics")){
							App_Installed(context);
						}
						System.out.println("Installed");
					} else {
						// Failure. :-/
						System.out.println(" Not Installed");
					}
				} else {
					// Failure 2. :-(
					System.out.println(" Not Installed");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("Not Present");
		}
	}

	public boolean root_checker(){
		Process p;
		boolean root = false;
		try {   
			// Perform su to get root privledges  
			p = Runtime.getRuntime().exec("su");

			// Attempt to write a file to a root-only   
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");  

			// Close the terminal  
			os.writeBytes("exit\n");   
			os.flush();   
			try {   
				p.waitFor();   
				if (p.exitValue() != 255) {   
					// TODO Code to run on success 
					root=true;
					Editor edit = mPreferences.edit();
					edit.putBoolean("root", root);
					edit.commit();
				}   
				else {   
					// TODO Code to run on unsuccessful  
				}   
			} catch (InterruptedException e) {
				// TODO Code to run in interrupted exception  
			}   
		} catch (IOException e) {
			// TODO Code to run in input/output exception  
		}
		return root;  
	}

	public AlertDialog onProgress(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_download));
		builder1.setMessage("Downloading file. Check status in Notification Bar...")
		.setTitle("Downloading")
		.setCancelable(false);

		return builder1.create();
	}

	private boolean isAppInstalled(String packageName) {
		PackageManager pm = getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	private void copyAssets() {

		installed=true;
		mPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		Editor edit = mPreferences.edit();
		edit.putBoolean("installed", installed);
		edit.apply();
		edit.commit();

		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		for(String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(filename);
				File outFile = new File(getExternalFilesDir(null), filename);
				out = new FileOutputStream(outFile);
				//	          outFile.getPath();
				if(copyFile(in, out)==true){
					InstallAPK("/sdcard/Android/data/water.works.waterworks/files/UpdaterAQ.apk", outFile,mContext);
				}
			} catch(IOException e) {
				Log.e("tag", "Failed to copy asset file: " + filename, e);
			}     
			finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// NOOP
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// NOOP
					}
				}
			}  
		}
	}

	private boolean copyFile(InputStream in, OutputStream out) throws IOException {
		boolean success=false;
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
			success=true;
		}
		return success;
	}

	public static void launch_it(Context context){
		//		Intent i = new Intent();
		//		i.setClassName("com.updateraq", "com.updateraq.MainActivity");
		//		context.startService(i);
		//		Toast.makeText(context, "Service", Toast.LENGTH_SHORT).show();

		Intent i = new Intent();
		i.setClassName("com.updateraqq", "com.updateraq.MainActivity");
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

	public static void App_Installed(Context context)
	{
		Intent intent = new Intent();
		intent.setAction(CUSTOM_INTENT);
		context.sendBroadcast(intent);
	}
}
