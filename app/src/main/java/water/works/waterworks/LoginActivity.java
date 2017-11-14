package water.works.waterworks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;


import water.works.waterworks.chat.Chat_Room;
import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;

public class LoginActivity extends Activity implements OnClickListener {
	TextView mTv_passion,mTv_approachability,mTv_adaptability,mTV_selfresponsibility,tv_our_mission;
	EditText mEt_login_password;
	AutoCompleteTextView mEt_login_username;
	//ab.dream.customlibrary.ShownEdittext mEt_login_password;
	String name,token,level,userid,sites;
	String Tag = "LoginActivity";
	boolean login_status = false;
	boolean server_status = false;
	Boolean isInternetPresent = false,connectiontimeout = false;
	SharedPreferences mPreferences;
	Titanic titanic;
	TitanicTextView tv ;
	FrameLayout fl_login_loading;
	//RelativeLayout rl_login_activity;
	private View decorView;
	private int uiOptions;
	Button login_button;

	//GCM

	public static String SENDER_ID="468292255530";
	public static String REG_ID;
	private static final String APP_VERSION = "3.6.1";
	GoogleCloudMessaging gcm;
	Context mContext=this;
	String regId="RegistrationID";
	static final String TAG = "Login Activity";

	//Double click 
	private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
	private long lastPressTime;
	private boolean mHasDoubleClicked = false;

	private static final String[] USERS = new String[] {
			"Christine w", "Alan", "todd", "jon"
	};

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//		decorView = getWindow().getDecorView();
		//		uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		//                | View.SYSTEM_UI_FLAG_FULLSCREEN
		//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

		//		createUiChangeListener();
 		setContentView(R.layout.newlogin);
//		regId = registerGCM();
		mPreferences = PreferenceManager
				.getDefaultSharedPreferences(LoginActivity.this);
		//		Crittercism.initialize(getApplicationContext(), "536f2b3107229a773f000001");

		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		String version = pInfo.versionName;
		TextView versionText = (TextView) findViewById(R.id.tv_versionno_name);
		versionText.setText("Version: " + version);
		isInternetPresent = Utility
				.isNetworkConnected(LoginActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}else{
			//			new AsyscWhichServer().execute();
			if(regId.contains("RegistrationID")){
//				regId=getRegistrationId(mContext);
			}
			Initialization();
			WW_StaticClass.duration1 = 300;
			WW_StaticClass.duration2 = 1000;
//			titanic = new Titanic();
//			titanic.start(tv);
		}

		versionText.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				long pressTime = System.currentTimeMillis();


				// If double click...
				if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
					//			            Toast.makeText(getApplicationContext(), "Double Click Event", Toast.LENGTH_SHORT).show();
					if(SOAP_CONSTANTS.Report_Url.contains("office")){
						Toast.makeText(mContext, "Switched from live to local", Toast.LENGTH_SHORT).show();
						SOAP_CONSTANTS.SOAP_ADDRESS = "http://192.168.1.100:8081/WWWebServices/Service.asmx?WSDL"; //local
						SOAP_CONSTANTS.SOAP_ADDRESS_1 = "http://192.168.1.100:8081/WWWebServices/Service.asmx/"; //local
						SOAP_CONSTANTS.Report_Url="http://192.168.1.100:8081/newcode/";
					}else{
						Toast.makeText(mContext, "Switched from local to live", Toast.LENGTH_SHORT).show();
						SOAP_CONSTANTS.SOAP_ADDRESS_1 = "http://office.waterworksswimonline.com/WWWebServices/Service.asmx/";
						SOAP_CONSTANTS.SOAP_ADDRESS = "http://office.waterworksswimonline.com/WWWebService/Service.asmx?WSDL";
						SOAP_CONSTANTS.Report_Url = "http://office.waterworksswimonline.com/newcode/";
					}
					mHasDoubleClicked = true;
				}
				else {     // If not double click....
					mHasDoubleClicked = false;
					Handler myHandler = new Handler() {
						public void handleMessage(Message m) {
							if (!mHasDoubleClicked) {
								//								if(SOAP_CONSTANTS.Report_Url.contains("office")){
								//									mEt_login_username.setText("shivy");
								//									mEt_login_password.setText("test");
								//								}else{
								//									mEt_login_username.setText("Christine W");
								//									mEt_login_password.setText("wegenerwater");
								//								}
							}
						}
					};
					Message m = new Message();
					myHandler.sendMessageDelayed(m,DOUBLE_PRESS_INTERVAL);
				}
				// record the last time the menu button was pressed.
				lastPressTime = pressTime;
			}
		});
	}
	private void createUiChangeListener() {

		decorView.setOnSystemUiVisibilityChangeListener (
				new View.OnSystemUiVisibilityChangeListener() {


					public void onSystemUiVisibilityChange(int pVisibility) {

						if ((pVisibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
							decorView.setSystemUiVisibility(uiOptions);
						}

					}

				});

	}

	ImageView img_login_header;
	LinearLayout ll_login_body;
	private void Initialization() {
		// TODO Auto-generated method stub
		//		rl_login_activity = (RelativeLayout)findViewById(R.id.rl_login_activity);
		ll_login_body = (LinearLayout)findViewById(R.id.ll_login_body);
//		tv = (TitanicTextView) findViewById(R.id.my_text_view);
//		tv.setTypeface(Typefaces.get(LoginActivity.this, "Satisfy-Regular.ttf"));
		fl_login_loading = (FrameLayout)findViewById(R.id.login_loading);
		fl_login_loading.setVisibility(View.GONE);
		login_button = (Button)findViewById(R.id.btn_login);
		mTv_passion=(TextView)findViewById(R.id.tv_passion);
		String passion = "PASSION";
		SpannableString ss=  new SpannableString(passion);
		ss.setSpan(new RelativeSizeSpan(1.5f),0,1,0); // set size
		ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bigs)), 0, 1, 0);// set color
		mTv_passion.setText(ss);
		tv_our_mission = (TextView)findViewById(R.id.tv_our_mission);

		mTv_approachability=(TextView)findViewById(R.id.tv_approachability);
		String approachability= "APPROACHABILITY";
		SpannableString ss1=  new SpannableString(approachability);
		ss1.setSpan(new RelativeSizeSpan(1.5f),0,1,0); // set size
		ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bigs)), 0, 1, 0);// set color
		mTv_approachability.setText(ss1);

		mTv_adaptability=(TextView)findViewById(R.id.tv_adaptability);
		String adaptability= "ADAPTABILITY";
		SpannableString ss2=  new SpannableString(adaptability);
		ss2.setSpan(new RelativeSizeSpan(1.5f),0,1,0); // set size
		ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bigs)), 0, 1, 0);// set color
		mTv_adaptability.setText(ss2);

		mTV_selfresponsibility=(TextView)findViewById(R.id.tv_selfresponsibility);
		String selfresponsibility= "SELF RESPONSIBILITY";
		SpannableString ss3=  new SpannableString(selfresponsibility);
		ss3.setSpan(new RelativeSizeSpan(1.5f),0,1,0); // set size
		ss3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bigs)), 0, 1, 0);// set color
		mTV_selfresponsibility.setText(ss3);
		mEt_login_username=(AutoCompleteTextView)findViewById(R.id.et_login_username);
		mEt_login_password = (EditText)findViewById(R.id.et_login_password);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, USERS);
		mEt_login_username.setAdapter(adapter);

		//		mEt_login_password=(ab.dream.customlibrary.ShownEdittext)findViewById(R.id.et_login_password);
		//		mEt_login_username.setText("Christine W");
		//		mEt_login_password.setText("wegenerwater");
		//		mEt_login_username.setText("Jeremy");
		//		mEt_login_password.setText("3658water");
		//		mEt_login_username.setText("victor");
		//		mEt_login_password.setText("5267water");
		final EditText focusbox = (EditText)findViewById(R.id.focusbox);
		focusbox.setEnabled(false);
		final ScrollView scrollview = (ScrollView)findViewById(R.id.loginscroll);
		scrollview.fullScroll(View.FOCUS_DOWN);
		focusbox.requestFocus();
		new Handler().postDelayed(new Runnable() {

			public void run() {
				mEt_login_username.requestFocus();
			}
		}, 2000);
		mEt_login_username.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				// TODO Auto-generated method stub
				scrollview.fullScroll(View.FOCUS_DOWN);
				focusbox.requestFocus();
				new Handler().postDelayed(new Runnable() {

					public void run() {
						mEt_login_username.requestFocus();
					}
				}, 1000);
			}
		});

		mEt_login_password.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				scrollview.fullScroll(View.FOCUS_DOWN);
				focusbox.requestFocus();
				new Handler().postDelayed(new Runnable() {

					public void run() {
						mEt_login_password.requestFocus();
					}
				}, 1000);
			}
		});

		tv_our_mission.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (SOAP_CONSTANTS.SOAP_ADDRESS.contains("office")) {
					Toast.makeText(mContext, "Live", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, "Local", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}





	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (isInternetPresent) {
			switch (v.getId()) {
				case R.id.btn_login:
					if(Utility.isNetworkConnected(this)){
						if(mEt_login_username.getText().length() > 2
								&& mEt_login_password.getText().length() > 2){

							new LoginAsyn().execute();
						}
						else{
							SingleOptionAlertWithoutTitle.ShowAlertDialog(
									LoginActivity.this,"WaterWorks", "Username/Password Is Invalid",
									"OK");
						}
					}
					else{
						onDetectNetworkState().show();
					}
					break;

				default:
					break;
			}
		}else {
			// Internet connection is not present
			//			SingleOptionAlertWithoutTitle.ShowAlertDialog(LoginActivity.this,
			//					"No Internet Connection",
			//					"Please check your internet connection", "OK");
			onDetectNetworkState().show();


		}

	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						LoginActivity.this.finish();
					}
				})
				.setPositiveButton("Οk",new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
		return builder1.create();
	}

	//	ProgressDialog pDialog;
	public class LoginAsyn extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			//			pDialog = new ProgressDialog(LoginActivity.this);
			//			pDialog.setMessage(Html.fromHtml("Loading wait..."));
			//			pDialog.setIndeterminate(true);
			//			pDialog.setCancelable(false);
			//			pDialog.show();
			//			rl_login_activity.setEnabled(false);
			//			rl_login_activity.setClickable(false);
			mEt_login_password.setClickable(false);
			mEt_login_username.setClickable(false);
			login_button.setEnabled(false);
			login_button.setClickable(false);
			fl_login_loading.setVisibility(View.VISIBLE);
			fl_login_loading.bringToFront();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			LoginMethod();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//			pDialog.dismiss();
			fl_login_loading.setVisibility(View.GONE);
			//			rl_login_activity.setEnabled(true);
			//			rl_login_activity.setClickable(true);
			mEt_login_password.setClickable(true);
			mEt_login_username.setClickable(true);
			login_button.setEnabled(true);
			login_button.setClickable(true);
			if(server_status){
				onDetectNetworkState().show();
				server_status=false;
			}
			else if(connectiontimeout){
				connectiontimeout = false;
				new LoginAsyn().execute();
			}
			else{
				if (login_status) {
					login_status=false;
					Intent newIntent = new Intent(LoginActivity.this, MenuActivity.class);
					startActivity(newIntent);
					LoginActivity.this.finish();

				} else {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							LoginActivity.this,"WaterWorks",
							"Please Enter Valid Username and Password", "OK");
				}
			}
		}
		public void LoginMethod() {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_LOGIN);
			// Adding Username and Password for Login Invok
			request.addProperty("username", mEt_login_username.getText()
					.toString());
			request.addProperty("password", mEt_login_password.getText()
					.toString());
			request.addProperty("deviceid", regId);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_ACTION_LOGIN,
						envelope); // Calling Web service
				SoapObject response =  (SoapObject) envelope.getResponse();
				//			 Log.i("here","Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				//			 Log.i(Tag, "mSoapObject1="+mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				//			 Log.i(Tag, "mSoapObject2="+mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				//			response.toString();
				if (code.equals("000")) {
					login_status = true;
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i(Tag, "mSoapObject3="+mSoapObject3);
					//				String UserID = mSoapObject3.getPropertyAsString(0).toString();
					//				String Uname = mSoapObject3.getPropertyAsString(1).toString();
					//				String user_Type = mSoapObject3.getPropertyAsString(2)
					//						.toString();
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					WW_StaticClass.siteid.clear();
					WW_StaticClass.sitename.clear();
					name = jo.getString("UserName");
					token = jo.getString("UserToken");
					level = jo.getString("UserLevel");
					userid = jo.getString("UserId");
					sites = jo.getString("sitelist");
					WW_StaticClass.InstructorID = userid;
					WW_StaticClass.DeckSuperID = userid;
					WW_StaticClass.UserName = name;
					WW_StaticClass.InstructorName = name;
					WW_StaticClass.UserLevel = level;
					WW_StaticClass.UserToken = token;
					String temp[] = sites.toString().split("\\,");
					String temp2[];
					for (int i = 0; i < temp.length; i++) {
						temp2 = temp[i].toString().split("\\:");
						WW_StaticClass.siteid.add(temp2[0].toString());
						WW_StaticClass.sitename.add(temp2[1].toString());
					}

					SharedPreferences imp_details= getPreferences(Context.MODE_PRIVATE);
					SharedPreferences.Editor edit = imp_details.edit();
					edit.putString("UserName", name);
					edit.putString("UserToken", token);
					edit.putString("UserLevel", level);
					edit.putString("UserId", userid);
					edit.putString("sitelist", sites);
					edit.commit();

					Log.i("Siteid",""+ WW_StaticClass.siteid);
					Log.i("Sitename",""+ WW_StaticClass.sitename);
					Log.i("Name", WW_StaticClass.UserName);
					Log.i("Token", WW_StaticClass.UserToken);
					Log.i("Level", WW_StaticClass.UserLevel);
					Log.i("UserID", WW_StaticClass.InstructorID);
				}
				else{
					login_status = false;
				}
			}
			catch(SocketTimeoutException e){
				connectiontimeout=true;
				e.printStackTrace();
			}
			catch(SocketException e){
				connectiontimeout=true;
				e.printStackTrace();
			}
			catch(JSONException e){
				server_status=true;
				e.printStackTrace();
			}
			catch (Exception e) {
				server_status=true;
				e.printStackTrace();
			}
		}
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//			decorView.setSystemUiVisibility(uiOptions);
		isInternetPresent = Utility
				.isNetworkConnected(LoginActivity.this);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}


	private class AsyscWhichServer extends AsyncTask<Void, Void, Void> {
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
				androidHttpTransport.call("http://tempuri.org/CheckAccessibility",
						envelope); // Calling Web service
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				Log.i("here","Result : " + response.toString());
			}
			catch(SocketException e){
				server_status=true;
				e.printStackTrace();
			}
			catch(SocketTimeoutException e){
				server_status=true;
				e.printStackTrace();
			}
			catch (Exception e) {
				server_status=true;
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			/*Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/" + "Aquatics-1.apk")),"application/vnd.android.package-archive");
			startActivity(intent);*/
			if(server_status){
				server_status = false;
				SOAP_CONSTANTS.SOAP_ADDRESS="http://office.waterworksswimonline.com/WWWebService/Service.asmx?WSDL";
				SOAP_CONSTANTS.Report_Url="http://office.waterworksswimonline.com/newcode/";
			}
		}
	}
	public AlertDialog ConnectionTimeOut(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Connection timeout.")
				.setTitle("WaterWorks.")

				.setPositiveButton("Retry",new DialogInterface.OnClickListener() {


					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						new LoginAsyn().execute();
					}
				});
		return builder1.create();
	}


	//RegistrationID For GCM
	public String registerGCM() {

		gcm = GoogleCloudMessaging.getInstance(this);
//		regId = getRegistrationId(mContext);

		if (TextUtils.isEmpty(regId)) {

			registerInBackground();

			Log.d("RegisterActivity",
					"registerGCM - successfully registered with GCM server - regId: "
							+ regId);
		} else {
			//			Toast.makeText(getApplicationContext(),
			//					"RegId already available. RegId: " + regId,
			//					Toast.LENGTH_LONG).show();
		}
		return regId;
	}

	@SuppressWarnings("unchecked")
	private void registerInBackground() {
		new AsyncTask() {
			String msg = "";
			@Override
			protected String doInBackground(Object... arg0) {
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(mContext);
					}
					regId = gcm.register(SENDER_ID);
					Log.d("RegisterActivity", "registerInBackground - regId: "
							+ regId);
					msg = "Device registered, registration ID=" + regId;

					storeRegistrationId(mContext, regId);
				} catch (Exception ex) {
					msg = "Error :" + ex.getMessage();
					Log.d("RegisterActivity", "Error: " + msg);
				}
				Log.d("RegisterActivity", "AsyncTask completed: " + msg);
				return msg;
			}

			protected void onPostExecute(Object result) {
				//				Toast.makeText(getApplicationContext(),
				//						"Registered with GCM Server." + msg, Toast.LENGTH_LONG)
				//						.show();
			};
		}.execute(null, null, null);
	}

	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getSharedPreferences(
				Chat_Room.class.getSimpleName(), Context.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(REG_ID, regId);
		editor.putInt(APP_VERSION, appVersion);
		editor.commit();
	}

	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(Chat_Room.class.getSimpleName(), Context.MODE_PRIVATE);
		String registrationId = prefs.getString(REG_ID, "");
		if (registrationId.isEmpty()) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			Log.d("RegisterActivity",
					"I never expected this! Going down, going down!" + e);
			throw new RuntimeException(e);
		}
	}


}