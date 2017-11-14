package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Calendar;

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.services.DeckNotificationService;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class AwardTurboActivity extends Activity implements OnClickListener {
	int SiteID,UserID;
	String UserName="";
	String sendinfo="";
	boolean server_response = false;
	boolean getturbo = false;
	boolean getinstructor =false;
	boolean getsitelist = false;
	boolean sended = false;
	TextView mtv_name,mtv_day,mtv_date,mtv_time;
	ImageView iv_turbo;
	String time;
	public static String TAG="AwardTurboActivity";
	String am_pm;
	java.util.Date noteTS;
	SharedPreferences mPreferences;
	Boolean isInternetPresent = false;
	String imageurl = "http://office.waterworksswimonline.com/newcode/images/smallturbo.jpg";
	Bitmap image_bitmap =null;
	ArrayList<String> Siteid,Sitename,Userid,Username;
	ListPopupWindow sitenameList,towhomList;
	Button btn_award_site,btn_award_towhom;
	EditText et_message_body;
	Titanic titanic;
	TitanicTextView tv ;
	FrameLayout fl_award_turbo_loading;
	Thread t;
	String et_message_bodyStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_award_turbo);
		mPreferences = PreferenceManager
				.getDefaultSharedPreferences(AwardTurboActivity.this);

		isInternetPresent = Utility
				.isNetworkConnected(AwardTurboActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR);
			int min = c.get(Calendar.MINUTE);
			int Day_Name = c.get(Calendar.DAY_OF_WEEK);
			int Date =  c.get(Calendar.DATE);
			int Month = c.get(Calendar.MONTH);
			String day_name = null;
			if(Day_Name == 1){
				day_name = "SUN";
			}
			else if(Day_Name == 2){
				day_name = "MON";
			}
			else if(Day_Name == 3){
				day_name = "TUES";
			}
			else if(Day_Name == 4){
				day_name = "WED";
			}
			else if(Day_Name == 5){
				day_name = "THUR";
			}
			else if(Day_Name == 6){
				day_name = "FRI";
			}
			else if(Day_Name == 7){
				day_name = "SAT";
			}
			Log.i("Time", "Time = " + hour + ":" + min + " "  + " " + day_name + " " + Date + "/" + Month);
			//////////////
			initialize();		
			WW_StaticClass.duration1 = 300;
			WW_StaticClass.duration2 = 1000;
			titanic = new Titanic();
			titanic.start(tv);
			///////////////////////

			t = new Thread() {

				@Override
				public void run() {
					try {
						while (!isInterrupted()) {
							Thread.sleep(10);
							runOnUiThread(new Runnable() {

								public void run() {
									updateTextView();
									Calendar cc = Calendar.getInstance();
									int AM_PM = cc.get(Calendar.AM_PM);

									if(AM_PM == 0){
										am_pm = "AM";
									}
									else{
										am_pm = "PM";
									} 
								}

								private void updateTextView() {
									// TODO Auto-generated method stub
									noteTS = Calendar.getInstance().getTime();

									time = "hh:mm"; // 12:00
									mtv_time.setText(DateFormat.format(time, noteTS)+" "+am_pm);
								}
							});
						}
					} catch (InterruptedException e) {
					}
				}
			};

			t.start();


			mtv_name.setText(WW_StaticClass.UserName);
			mtv_day.setText(day_name);
			mtv_date.setText(Month+1 + "/" + Date);
			new GetTurbo().execute();
			new GetSite().execute();
			btn_award_towhom.setEnabled(false);
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
				t.interrupt();
				AwardTurboActivity.this.finish();
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

	private void initialize() {
		// TODO Auto-generated method stub
		tv = (TitanicTextView) findViewById(R.id.my_text_view);
		tv.setTypeface(Typefaces.get(AwardTurboActivity.this, "Satisfy-Regular.ttf"));
		fl_award_turbo_loading = (FrameLayout)findViewById(R.id.award_turbo_loading);
		fl_award_turbo_loading.setVisibility(View.GONE);
		fl_award_turbo_loading.bringToFront();
		mtv_date = (TextView)findViewById(R.id.tv_app_date);
		mtv_day = (TextView)findViewById(R.id.tv_app_day);
		mtv_name = (TextView)findViewById(R.id.tv_app_inst_name);
		mtv_time = (TextView)findViewById(R.id.tv_app_time);
		iv_turbo = (ImageView)findViewById(R.id.iv_turbo);
		btn_award_site = (Button)findViewById(R.id.btn_award_site);
		btn_award_towhom = (Button)findViewById(R.id.btn_award_to_whom);
		sitenameList = new ListPopupWindow(getApplicationContext());
		towhomList = new ListPopupWindow(getApplicationContext());
		et_message_body = (EditText)findViewById(R.id.et_award_msg);
	}
	private class GetTurbo extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(isInternetPresent){
				try{
					image_bitmap = Utility.getBitmapFromURL(imageurl, getApplicationContext());
					getturbo = true;
				}
				catch(Exception e ){
					e.printStackTrace();
					server_response = true;
				}

			}
			else{
				onDetectNetworkState().show();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(server_response){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						AwardTurboActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
				server_response = false;
			}
			else{
				if(getturbo){
					iv_turbo.setImageBitmap(image_bitmap);
					getturbo = false;
				}
			}

		}

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (isInternetPresent) {
			switch (v.getId()) {
			case R.id.btn_app_logoff:
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						AwardTurboActivity.this);
				// set title
				alertDialogBuilder.setTitle("WaterWorks");
				alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

				// set dialog message
				alertDialogBuilder
				.setMessage("Are you sure you want to logout ?")
				.setCancelable(false)
				.setPositiveButton("Logout",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mPreferences.edit().clear();
						mPreferences.edit().commit();
						WW_StaticClass.InstructorID="";
						t.interrupt();
						finish();
						stopService(new Intent(AwardTurboActivity.this, DeckNotificationService.class));
						Intent loginIntent= new Intent(AwardTurboActivity.this, LoginActivity.class);
						loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

						startActivity(loginIntent);
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				break;

			case R.id.btn_award_site:
				sitenameList.show();
				isClick=true;
				btn_award_towhom.setText("");
				btn_award_towhom.setHint("To Whom");
				break;

			case R.id.btn_award_to_whom:
				if(isClick){
					if(Username.size()>0){
						towhomList.show();
					}
				}
				break;
			case R.id.btn_back:
				t.interrupt();
				finish();
				break;
			case R.id.btn_award_send:
				et_message_bodyStr=et_message_body.getText().toString();
				if(btn_award_site.getText().toString().isEmpty()||btn_award_site.getText().toString().equalsIgnoreCase("")){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(AwardTurboActivity.this, "WaterWorks", "Please select site.", "Ok");
				}
				else if(btn_award_towhom.getText().toString().isEmpty()||btn_award_towhom.getText().toString().equalsIgnoreCase("")){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(AwardTurboActivity.this, "WaterWorks", "Please select user.", "Ok");
				}
				else if(et_message_body.getText().toString().isEmpty()||et_message_body.getText().toString().equalsIgnoreCase("")){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(AwardTurboActivity.this, "WaterWorks", "Please enter message.", "Ok");
				}
				else{
					new SendMessage().execute();
				}
				break;
			default:
				break;
			}
		}else {
			// Internet connection is not present
			onDetectNetworkState().show();


		}
	}
	boolean isClick =false;
	ProgressDialog pDialog,pDialog2,pDialog3;
	private class GetSite extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//			pDialog = new ProgressDialog(AwardTurboActivity.this);
			//			pDialog.setMessage(Html.fromHtml("Loading wait..."));
			//			pDialog.setIndeterminate(true);
			//			pDialog.setCancelable(false);
			//			pDialog.show();
			fl_award_turbo_loading.setVisibility(View.VISIBLE);
			fl_award_turbo_loading.bringToFront();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetSiteList_1);
			request.addProperty("token", WW_StaticClass.UserToken);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetSiteList_1,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
				//				SoapPrimitive response =  (SoapPrimitive) envelope.getResponse();
				Log.i(TAG,"" + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				Log.i(TAG, "mSoapObject1="+mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				Log.i(TAG, "mSoapObject2="+mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if(code.equalsIgnoreCase("000")){
					getsitelist = true;
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3="+mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("Sites");
					Log.i(TAG,"jArray : " + jArray.toString());
					Siteid = new ArrayList<String>();
					Sitename = new ArrayList<String>();
					JSONObject jsonObject ;
					for(int i=0;i<jArray.length();i++){
						jsonObject = jArray.getJSONObject(i);
						Sitename.add(jsonObject.getString("SiteName"));
						Siteid.add(jsonObject.getString("SiteID"));
					}
					Log.i(TAG, "id = "+Siteid);
					Log.i(TAG, "name = "+Sitename);
					sitenameList.setAdapter(new ArrayAdapter<String>(
							getApplicationContext(),
							R.layout.edittextpopup,Sitename ));
					sitenameList.setAnchorView(btn_award_site);
					sitenameList.setHeight(300);
					sitenameList.setModal(true);
					sitenameList.setOnItemClickListener(
							new OnItemClickListener() {

								public void onItemClick(AdapterView<?> parent, View view,
                                                        int pos, long id) {
									// TODO Auto-generated method stub
									btn_award_site.setText(Sitename.get(pos));
									SiteID = Integer.parseInt(Siteid.get(pos));
									sitenameList.dismiss();
									btn_award_towhom.setEnabled(false);
									new GetToWhom().execute();
								}
							});

				}
				else{
					getsitelist =false;
				}
			}
			catch(JSONException e){
				e.printStackTrace();
				server_response =true;
			}
			catch(Exception e){
				server_response =true;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//			pDialog.dismiss();
			fl_award_turbo_loading.setVisibility(View.GONE);
			if(server_response){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						AwardTurboActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_response = false;
			}
			else{
				if(!getsitelist){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							AwardTurboActivity.this,"WaterWorks",
							"No site found.", "OK");
				}
				else{
					getsitelist= false;
				}
			}
		}

	}

	private class GetToWhom extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fl_award_turbo_loading.setVisibility(View.VISIBLE);
			fl_award_turbo_loading.bringToFront();
			btn_award_towhom.setEnabled(false);
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_Get_InstrctListBySite);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("siteid", SiteID);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Get_InstrctListBySite,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
				Log.i(TAG,"" + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				Log.i(TAG, "mSoapObject1="+mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				Log.i(TAG, "mSoapObject2="+mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					getinstructor = true;
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3="+mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("InstrListBySiteid");
					Log.i(TAG,"jArray : " + jArray.toString());
					Userid = new ArrayList<String>();
					Username = new ArrayList<String>();
					JSONObject jsonObject ;
					for(int i=0;i<jArray.length();i++){
						jsonObject = jArray.getJSONObject(i);
						Username.add(jsonObject.getString("UserName"));
						Userid.add(jsonObject.getString("Userid"));
					}
					Log.i(TAG, "id = "+Userid);
					Log.i(TAG, "name = "+Username);
					towhomList.setAdapter(new ArrayAdapter<String>(
							getApplicationContext(),
							R.layout.edittextpopup,Username));
					towhomList.setAnchorView(btn_award_towhom);
					towhomList.setHeight(300);
					towhomList.setModal(true);
					towhomList.setOnItemClickListener(
							new OnItemClickListener() {


								public void onItemClick(AdapterView<?> parent, View view,
                                                        int pos, long id) {
									// TODO Auto-generated method stub
									btn_award_towhom.setText(Username.get(pos));
									UserName = Username.get(pos).toString();
									UserID = Integer.parseInt(Userid.get(pos));
									towhomList.dismiss();
								}
							});

				}
				else{
					getinstructor = false;
				}

			}
			catch(JSONException e){
				e.printStackTrace();
				server_response =true;
			}
			catch(Exception e){
				server_response =true;
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//			pDialog2.dismiss();
			btn_award_towhom.setEnabled(true);
			fl_award_turbo_loading.setVisibility(View.GONE);
			if(server_response){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						AwardTurboActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_response = false;
			}
			else{
				if(getinstructor){
					getinstructor = false;
				}
				else{
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							AwardTurboActivity.this,"WaterWorks",
							"Invalid selection.Please try again", "OK");
				}
			}
		}

	}


	private class SendMessage extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fl_award_turbo_loading.setVisibility(View.VISIBLE);
			fl_award_turbo_loading.bringToFront();
			btn_award_towhom.setEnabled(false);
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_Insert_TerificTurbo);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("siteid", SiteID);
			request.addProperty("tolist", UserID);
			request.addProperty("uname", UserName);
			request.addProperty("message",et_message_bodyStr);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);

			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_ACTION_Insert_TerificTurbo,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.bodyIn;
				Log.i(TAG,"" + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				Log.i(TAG, "mSoapObject1="+mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				Log.i(TAG, "mSoapObject2="+mSoapObject2);
				SoapObject mSoapObject3 = (SoapObject) mSoapObject2.getProperty(0);
				//				 /anyType{Code=000; Flag=true; Description=Success; }
				Log.i("Code", ""+mSoapObject3);
				String code = mSoapObject3.getPropertyAsString(0).toString();
				Log.i("Code", code);

				if (code.equals("000")) {
					sended= true;
					Object mSoapObject4 =  mSoapObject2.getProperty(1);
					Log.i(TAG, "mSoapObject4="+mSoapObject4);
					String resp = mSoapObject4.toString();
					JSONObject jo = new JSONObject(resp);
					sendinfo = jo.getString("InstTerificTurbo");
				}
				else{
					sended= false;
				}


			}
			catch(Exception e){
				e.printStackTrace();
				server_response = true;
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//			pDialog3.dismiss();
			btn_award_towhom.setEnabled(true);
			fl_award_turbo_loading.setVisibility(View.GONE);

			if(server_response){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						AwardTurboActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
			}
			else{
				if (sended) {
					AlertDialog.Builder alertdialogbuilder2 = new AlertDialog.Builder(
							AwardTurboActivity.this);
					alertdialogbuilder2.setTitle("Waterworks")
					.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
					alertdialogbuilder2
					.setMessage("Message sent successfully...")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

						public void onClick(
                                DialogInterface dialog, int id) {
							dialog.cancel();
							Intent newIntent = new Intent(AwardTurboActivity.this, MenuActivity.class);
							startActivity(newIntent);
							t.interrupt();
							finish();
						}

					});
					AlertDialog alertDialog2 = alertdialogbuilder2.create();
					alertDialog2.show();
					sended = false;

				} else {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							AwardTurboActivity.this,"WaterWorks",
							"Please send valid data.", "OK");
				}
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
		isInternetPresent = Utility
				.isNetworkConnected(AwardTurboActivity.this); 
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		t.interrupt();
		finish();
		Intent it = new Intent(AwardTurboActivity.this, MenuActivity.class);
		startActivity(it);
	}

}
