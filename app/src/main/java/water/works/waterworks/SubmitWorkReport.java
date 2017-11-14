package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import water.works.waterworks.services.DeckNotificationService;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.WW_StaticClass;


public class SubmitWorkReport extends Activity {

	TextView mtv_name,mtv_day,mtv_date,mtv_time;
	ImageButton ib_back;
	EditText date_view,reason;
	Calendar myCalendar = Calendar.getInstance();
	TextView success_message;
	Button date_button,save,btn_app_logoff;
	//Start spinner
	Spinner hour_1,hour_2,min_1,min_2,am_pm_1,am_pm_2;
	//End spinner
	Spinner hour_1_end,hour_2_end,min_1_end,min_2_end,am_pm_1_end,am_pm_2_end;
	public static String TAG="SubmitWorkReport";
	ArrayList<String> Siteid,Sitename,UserName_Arr = new ArrayList<String>(),wu_mgrid_Arr=new ArrayList<String>();
	HashMap<String, String> sites_list,manager_list =  new HashMap<String, String>();
	Spinner sp_site,sp_manager;
	Context mContext=this;
	String selected_item="",final_date="";
	Thread t;
	String am_pm,time;
	java.util.Date noteTS;
	
	
	OnDateSetListener date = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
			// TODO Auto-generated method stub
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.submit_work_report);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		init();	



		Calendar c = Calendar.getInstance();
//		int hour = c.get(Calendar.HOUR);
//		int min = c.get(Calendar.MINUTE);
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
	}

	public void init(){
		success_message = (TextView)findViewById(R.id.success_message);

		date_view = (EditText)findViewById(R.id.date_view);
		date_button = (Button)findViewById(R.id.date_button);
		reason = (EditText)findViewById(R.id.reason);
		btn_app_logoff = (Button)findViewById(R.id.btn_app_logoff);
		
		hour_1 = (Spinner)findViewById(R.id.hour_1);
		hour_1_end = (Spinner)findViewById(R.id.hour_1_end);
		hour_2 = (Spinner)findViewById(R.id.hour_2);
		hour_2_end = (Spinner)findViewById(R.id.hour_2_end);
		min_1 = (Spinner)findViewById(R.id.min_1);
		min_1_end = (Spinner)findViewById(R.id.min_1_end);
		min_2 = (Spinner)findViewById(R.id.min_2);
		min_2_end = (Spinner)findViewById(R.id.min_2_end);
		am_pm_1 = (Spinner)findViewById(R.id.am_pm_1);
		am_pm_1_end = (Spinner)findViewById(R.id.am_pm_1_end);
		am_pm_2 = (Spinner)findViewById(R.id.am_pm_2);
		am_pm_2_end = (Spinner)findViewById(R.id.am_pm_2_end);

		sp_manager = (Spinner)findViewById(R.id.sp_manager);
		sp_site = (Spinner)findViewById(R.id.sp_site);
		save = (Button)findViewById(R.id.save);
		//TopLayout 
		mtv_date = (TextView)findViewById(R.id.tv_app_date);
		mtv_day = (TextView)findViewById(R.id.tv_app_day);
		mtv_name = (TextView)findViewById(R.id.tv_app_inst_name);
		mtv_time = (TextView)findViewById(R.id.tv_app_time);
		ib_back = (ImageButton)findViewById(R.id.btn_back);

	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		new GetSite().execute();

		ib_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				t.interrupt();
				finish();
			}
		});
		
		btn_app_logoff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						mContext);
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
										WW_StaticClass.InstructorID="";
										t.interrupt();
										finish();
										stopService(new Intent(mContext, DeckNotificationService.class));
										Intent loginIntent= new Intent(mContext, LoginActivity.class);
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
			}
		});

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(date_validation()){
					if(reason_validation()){
						if(spin_value(sp_site)!=0){
							if(spin_value(sp_manager)!=0){
								if(first_validate()){
									if(validate()){
										if(second_validate()){
											if(validate_spinner()){
												if(time_validation()){
													if(time_validation_2()){
														new SavingMethod().execute();
													}
												}
											}
										}else{
											if(time_validation()){
												new SavingMethod().execute();
											}
										}
									}
								}else if(second_validate()){
									if(validate_spinner()){
										if(time_validation_2()){
											new SavingMethod().execute();
										}
									}
								}else{
									ping("Please select Time");
								}
							}else{
								ping("Please chooose manager.");
							}
						}else{
							ping("Please choose site.");
						}

					}else{
						ping("Please write reason");
					}

				}

			}
		});
		date_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int mYear,mMonth,mDay;

				Calendar mcurrentDate= Calendar.getInstance();
				mYear=mcurrentDate.get(Calendar.YEAR);
				mMonth=mcurrentDate.get(Calendar.MONTH);
				mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog mDatePicker=new DatePickerDialog(SubmitWorkReport.this, new OnDateSetListener() {
					public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
						// TODO Auto-generated method stub                      
						/*      Your code   to get date and time    */
						//						date_view.setText(selected_item+1 +"/" +selectedday +"/"+selectedyear);
						System.out.println(selectedmonth +"/" +selectedday +"/" +selectedyear);
						final_date = String.valueOf(selectedmonth+1) +"/" + String.valueOf(selectedday) +"/"+ String.valueOf(selectedyear);
						date_view.setText(String.valueOf(selectedmonth+1) +"/" + String.valueOf(selectedday) +"/"+ String.valueOf(selectedyear));

						//						updateLabel();
					}
				},mYear, mMonth, mDay);
				mDatePicker.setTitle("Select date");                
				mDatePicker.show();
			}
		});

		date_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int mYear,mMonth,mDay;

				Calendar mcurrentDate= Calendar.getInstance();
				mYear=mcurrentDate.get(Calendar.YEAR);
				mMonth=mcurrentDate.get(Calendar.MONTH);
				mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog mDatePicker=new DatePickerDialog(SubmitWorkReport.this, new OnDateSetListener() {
					public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
						// TODO Auto-generated method stub                      
						/*      Your code   to get date and time    */
						System.out.println(selectedmonth +"/" +selectedday +"/" +selectedyear);
						final_date = String.valueOf(selectedmonth+1) +"/" + String.valueOf(selectedday) +"/"+ String.valueOf(selectedyear);
						date_view.setText(String.valueOf(selectedmonth+1) +"/" + String.valueOf(selectedday) +"/"+ String.valueOf(selectedyear));

						//						updateLabel();
					}
				},mYear, mMonth, mDay);
				mDatePicker.setTitle("Select date");                
				mDatePicker.show(); 
			}
		});

		sp_site.setOnItemSelectedListener(new OnItemSelectedListener() {


			public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
				// TODO Auto-generated method stub
				if(position!=0){
					String value = sp_site.getSelectedItem().toString().trim();
					selected_item = sites_list.get(value);
					//					new GetManagerList().execute();
					new GetManagerList_Method().execute();

				}
			}


			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});

	}

	public void resetandupdate(){
		date_view.setText("");
		hour_1.setSelection(0);
		hour_1_end.setSelection(0);
		min_1.setSelection(0);
		min_1_end.setSelection(0);
		am_pm_1.setSelection(0);
		am_pm_1_end.setSelection(0);
		hour_2.setSelection(0);
		hour_2_end.setSelection(0);
		min_2.setSelection(0);
		min_2_end.setSelection(0);
		am_pm_2.setSelection(0);
		am_pm_2_end.setSelection(0);

		reason.setText("");

		sp_site.setSelection(0);
		sp_manager.setSelection(0);
	}

	public boolean validate (){
		boolean value=false;
		if(spin_value(hour_1)!=0){
			if(spin_value(min_1)!=0){
				if(spin_value(am_pm_1)!=0){
					if(spin_value(hour_1_end)!=0){
						if(spin_value(min_1_end)!=0){
							if(spin_value(am_pm_1_end)!=0){
								//								ping("Done");
								if(time_validation()){
									value = true;
								}

							}else{
								ping("Please select EndTime");
							}
						}else{
							ping("Please select EndTime");
						}
					}else{
						ping("Please select EndTime");
					}
				}else{
					ping("Please select StartTime");
				}
			}else{
				ping("Please select StartTime");
			}
		}else{
			ping("Please select StartTime");
		}
		return value;
	}

	public boolean validate_spinner(){
		boolean value=false;
		if(spin_value(hour_2)!=0){
			if(spin_value(min_2)!=0){
				if(spin_value(am_pm_2)!=0){
					if(spin_value(hour_2_end)!=0){
						if(spin_value(min_2_end)!=0){
							if(spin_value(am_pm_2_end)!=0){
								value=true;
							}else{
								ping("Please select EndTime");
							}
						}else{
							ping("Please select EndTime");
						}
					}else{
						ping("Please select EndTime");
					}
				}else{
					ping("Please select StartTime");
				}
			}else{
				ping("Please select StartTime");
			}
		}else{
			ping("Please select StartTime");
		}
		return value;
	}

	public boolean second_validate(){
		boolean value=false;

		if(spin_value(hour_2)!=0){
			value=true;
		}
		if(spin_value(hour_2_end)!=0){
			value=true;
		}
		if(spin_value(min_2)!=0){
			value=true;
		}
		if(spin_value(min_2_end)!=0){
			value=true;
		}
		if(spin_value(am_pm_2)!=0){
			value=true;
		}
		if(spin_value(am_pm_2_end)!=0){
			value=true;
		}
		return value;
	}

	public boolean first_validate(){
		boolean value=false;

		if(spin_value(hour_1)!=0){
			value=true;
		}
		if(spin_value(hour_1_end)!=0){
			value=true;
		}
		if(spin_value(min_1)!=0){
			value=true;
		}
		if(spin_value(min_1_end)!=0){
			value=true;
		}
		if(spin_value(am_pm_1)!=0){
			value=true;
		}
		if(spin_value(am_pm_1_end)!=0){
			value=true;
		}
		return value;
	}

	public boolean date_validation(){
		boolean value=false;
		if(date_view.getText().toString().trim().contains("dd")){
			ping("Please select date");
		}else{
			value=true;
		}
		return value;
	}

	public boolean reason_validation(){
		boolean value=false;
		if(reason.getText().toString().trim().length()>0){
			value=true;
		}else{
			value=false;
		}
		return value;
	}
	public int spin_value(Spinner sp){
		int value = 0;
		value = sp.getSelectedItemPosition();
		return value;
	}

	public String spinner_text(Spinner sp){
		String value = "0";
		if(sp.getSelectedItemPosition()>0){
			value= sp.getSelectedItem().toString();
		}
		return value;	
	}

	public boolean time_validation(){
		boolean value=false;
		if(spinner_text(am_pm_1).contains("am")){
			if(Integer.parseInt(spinner_text(hour_1))>=8){
				if(spinner_text(am_pm_1_end).contains("am")){
					if(Integer.parseInt(spinner_text(hour_1_end))>8){
						if(Integer.parseInt((spinner_text(hour_1)+spinner_text(min_1)))< Integer.parseInt((spinner_text(hour_1_end)+spinner_text(min_1_end)))){
							value=true;
							ping("Done");
						}else{
							ping("Working hour is wrong");
						}

					}else{
						ping("Working hour is wrong");
					}
				}else if(spinner_text(am_pm_1_end).contains("pm")){
					if(Integer.parseInt(spinner_text(hour_1_end))<8){
						ping("Done");
						value=true;
					}else{
						ping("Working hour is wrong");
					}
				}
			}else{
				ping("Working hour is wrong");
			}
		}else if(spinner_text(am_pm_1).contains("pm")){
			if(Integer.parseInt(spinner_text(hour_1))<=8){
				if(spinner_text(am_pm_1_end).contains("pm")){
					if(Integer.parseInt(spinner_text(hour_1_end))<8){
						if(Integer.parseInt((spinner_text(hour_1)+spinner_text(min_1)))< Integer.parseInt((spinner_text(hour_1_end)+spinner_text(min_1_end)))){
							value=true;
							ping("Done");
						}else{
							ping("Working hour is wrong");
						}
					}else{
						ping("Working hour is wrong");
					}
				}else{
					ping("Working hour is wrong");
				}
			}else{
				ping("Working hour is wrong");
			}
		}else{
			ping("Working hour is wrong");
		}
		return value;
	}

	public boolean time_validation_2(){
		boolean value=false;
		if(spinner_text(am_pm_2).contains("am")){
			if(Integer.parseInt(spinner_text(hour_2))>=8){
				if(spinner_text(am_pm_2_end).contains("am")){
					if(Integer.parseInt(spinner_text(hour_2_end))>8){
						if(Integer.parseInt((spinner_text(hour_2)+spinner_text(min_2)))< Integer.parseInt((spinner_text(hour_2_end)+spinner_text(min_2_end)))){
							value=true;
							ping("Done");
						}else{
							ping("Working hour is wrong");
						}

					}else{
						ping("Working hour is wrong");
					}
				}else if(spinner_text(am_pm_2_end).contains("pm")){
					if(Integer.parseInt(spinner_text(hour_2_end))<8){
						ping("Done");
						value=true;
					}else{
						ping("Working hour is wrong");
					}
				}
			}else{
				ping("Working hour is wrong");
			}
		}else if(spinner_text(am_pm_2).contains("pm")){
			if(Integer.parseInt(spinner_text(hour_2))<=8){
				if(spinner_text(am_pm_2_end).contains("pm")){
					if(Integer.parseInt(spinner_text(hour_2_end))<8){
						if(Integer.parseInt((spinner_text(hour_2)+spinner_text(min_2)))< Integer.parseInt((spinner_text(hour_2_end)+spinner_text(min_2_end)))){
							value=true;
							ping("Done");
						}else{
							ping("Working hour is wrong");
						}
					}else{
						ping("Working hour is wrong");
					}
				}else{
					ping("Working hour is wrong");
				}
			}else{
				ping("Working hour is wrong");
			}
		}else{
			ping("Working hour is wrong");
		}
		return value;
	}

	public void ping(String msg){
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}
	ProgressDialog pDialog;

	private class GetSite extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(SubmitWorkReport.this);
			pDialog.setMessage(Html.fromHtml("Loading wait..."));
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetSiteList);
			request.addProperty("token", WW_StaticClass.UserToken);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetSiteList,
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
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3="+mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("Sites");
					Log.i(TAG,"jArray : " + jArray.toString());
					Siteid = new ArrayList<String>();
					Sitename = new ArrayList<String>();

					sites_list = new HashMap<String, String>();
					JSONObject jsonObject;
					Siteid.add("0");
					Sitename.add("Please Choose");

					for(int i=0;i<jArray.length();i++){
						jsonObject = jArray.getJSONObject(i);
						Sitename.add(jsonObject.getString("SiteName"));
						Siteid.add(jsonObject.getString("SiteID"));

						sites_list.put(jsonObject.getString("SiteName"), jsonObject.getString("SiteID"));
					}

				}
				else{
				}

				pDialog.dismiss();
			}
			catch(JSONException e){
				e.printStackTrace();
				pDialog.dismiss();
			}
			catch(Exception e){
				e.printStackTrace();
				pDialog.dismiss();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			ArrayAdapter<String> array__ = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,Sitename);
			sp_site.setAdapter(array__);

			pDialog.dismiss();
		}
	}


	public class GetManagerList_Method extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(SubmitWorkReport.this);
			pDialog.setMessage(Html.fromHtml("Loading wait..."));
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(true);
			pDialog.show();

		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			getList();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, UserName_Arr);
			sp_manager.setAdapter(adapter);
			pDialog.dismiss();
		}

	}
	public void getList(){
		HttpClient httpClient = new DefaultHttpClient();
		Log.d("Legal Doc Check URL",SOAP_CONSTANTS.SOAP_ADDRESS_1);
		HttpPost httpPost = new HttpPost(SOAP_CONSTANTS.SOAP_ADDRESS_1+SOAP_CONSTANTS.METHOD_NAME_CT_GetManagerListBySite);

		//Post Data
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
		nameValuePair.add(new BasicNameValuePair("Token", WW_StaticClass.UserToken));
		nameValuePair.add(new BasicNameValuePair("SiteID", selected_item));

		//Encoding POST data
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (UnsupportedEncodingException e) {
			// log exception
			e.printStackTrace();
		}

		//making POST request.
		try {
			HttpResponse response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == 200)
			{
				HttpEntity entity = response.getEntity();
				String json = EntityUtils.toString(entity);
				Log.d("Response:",json);
				System.out.println("Student List Response : "+json);
				try {
					JSONObject obj = new JSONObject(json);
					String success = obj.getString("Success");

					if(success.equals("True")){
						JSONArray jArry = obj.getJSONArray("ClkTikManagerList");
						System.out.println("Success : "+jArry);

						if(jArry.length()>0){

							UserName_Arr.clear();
							wu_mgrid_Arr.clear();
							manager_list.clear();

							for (int i = 0; i < jArry.length(); i++) {
								String row = jArry.getString(i);
								JSONObject jobj = new JSONObject(row);
								UserName_Arr.add(jobj.getString("UserName"));
								wu_mgrid_Arr.add(jobj.getString("wu_mgrid"));
								manager_list.put(jobj.getString("UserName"), jobj.getString("wu_mgrid"));
							}
						}
					}else{

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class SavingMethod extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(SubmitWorkReport.this);
			pDialog.setMessage(Html.fromHtml("Loading wait..."));
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HttpClient httpClient = new DefaultHttpClient();
			Log.d("Legal Doc Check URL",SOAP_CONSTANTS.SOAP_ADDRESS_1);
			HttpPost httpPost = new HttpPost(SOAP_CONSTANTS.SOAP_ADDRESS_1+SOAP_CONSTANTS.METHOD_NAME_CT_SaveClockTick);

			//Post Data
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(20);
			nameValuePair.add(new BasicNameValuePair("Token", WW_StaticClass.UserToken));
			nameValuePair.add(new BasicNameValuePair("SiteID", selected_item));
			nameValuePair.add(new BasicNameValuePair("txtworkdone", reason.getText().toString()));
			nameValuePair.add(new BasicNameValuePair("ManagerID", manager_list.get(sp_manager.getSelectedItem().toString())));
			nameValuePair.add(new BasicNameValuePair("txt_RequestDate", final_date));
			nameValuePair.add(new BasicNameValuePair("drpendhour1", spinner_text(hour_1_end)));
			nameValuePair.add(new BasicNameValuePair("drpendmins1", spinner_text(min_1_end)));
			nameValuePair.add(new BasicNameValuePair("drpendampm1", spinner_text(am_pm_1_end)));
			nameValuePair.add(new BasicNameValuePair("drpstarthour1", spinner_text(hour_1)));
			nameValuePair.add(new BasicNameValuePair("drpstartmins1", spinner_text(min_1)));
			nameValuePair.add(new BasicNameValuePair("drpstartampm1", spinner_text(am_pm_1)));
			nameValuePair.add(new BasicNameValuePair("drpstarthour2", spinner_text(hour_2)));
			nameValuePair.add(new BasicNameValuePair("drpstartmins2", spinner_text(min_2)));
			nameValuePair.add(new BasicNameValuePair("drpendhour2", spinner_text(hour_2_end)));
			nameValuePair.add(new BasicNameValuePair("drpendmins2", spinner_text(min_2_end)));
			nameValuePair.add(new BasicNameValuePair("drpstartampm2", spinner_text(am_pm_2)));
			nameValuePair.add(new BasicNameValuePair("drpendampm2", spinner_text(am_pm_2_end)));

			System.out.println("Request : "+nameValuePair);
			//Encoding POST data
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			} catch (UnsupportedEncodingException e) {
				// log exception
				e.printStackTrace();
			}

			//making POST request.
			try {
				HttpResponse response = httpClient.execute(httpPost);

				if (response.getStatusLine().getStatusCode() == 200)
				{
					HttpEntity entity = response.getEntity();
					String json = EntityUtils.toString(entity);
					Log.d("Response:",json);
					System.out.println("Student List Response : "+json);
					try {
						JSONObject obj = new JSONObject(json);
						String success = obj.getString("Success");

						if(success.equals("True")){
							System.out.println("Success : "+success);
						}else{
							ping("Error Occured");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();

			success_message.setVisibility(View.VISIBLE);
			resetandupdate();
		}
	}
}
