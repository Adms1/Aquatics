package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class InstructorSelectionForMVCSActivity extends Activity implements OnClickListener {
	int code;
	Button btn_home;
	TextView tv_day,tv_date,tv_managername,tv_is_for_vcs_no_inst;
	LinearLayout ll_is_for_vcs;
	RadioGroup siteRadioGroup;
	RadioButton siteRadioButton[];
	Boolean isInternetPresent = false;
	static String TAG = "InstructorforVCS";
	ArrayList<String> SiteID,SiteName,male_UserID,female_UserID,male_UserName,female_UserName;
	boolean getsitelist = false,server_response=false,getinstructor = false,other_problem= false;
	public static String temp_siteid;
	public static ArrayList<String> final_list = new ArrayList<String>();
	public static ArrayList<String> final_list_name = new ArrayList<String>();
	String currentDateandTime;
	ListView ll_male,ll_female;
	ArrayList<String> temp_maleid = new ArrayList<String>();
	ArrayList<String> temp_femaleid = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_selection_for_vcs);
		isInternetPresent = Utility.isNetworkConnected(InstructorSelectionForMVCSActivity.this);
		if(isInternetPresent){
			code = getIntent().getIntExtra("CODE", 0);
			Initilization();
			new GetSiteList().execute();
		}else{
			onDetectNetworkState().show();
		}
		
	}
	
	private void Initilization() {
		// TODO Auto-generated method stub
		btn_home = (Button)findViewById(R.id.btn_home);
		tv_date = (TextView)findViewById(R.id.tv_date);
		tv_day = (TextView)findViewById(R.id.tv_day);
		tv_managername = (TextView)findViewById(R.id.tv_mngrname);
		ll_is_for_vcs = (LinearLayout)findViewById(R.id.ll_is_for_vcs);
		siteRadioGroup = new RadioGroup(getApplicationContext());
		ll_female = (ListView)findViewById(R.id.ll_is_for_vcs_female);
		ll_male = (ListView)findViewById(R.id.ll_is_for_vcs_male);
		tv_is_for_vcs_no_inst = (TextView)findViewById(R.id.tv_is_for_vcs_no_inst);
		SetUpScreen();
	}

	private void SetUpScreen() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
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
		tv_managername.setText(WW_StaticClass.UserName);
		tv_day.setText(day_name);
		String m,d;
		if(String.valueOf(Month).length()==1){
			m = "0"+(Month+1);
		}
		else{
			m =""+(Month+1);
		}
		if(String.valueOf(Date).length()==1){
			d = "0"+(Date);
		}
		else{
			d =""+(Date);
		}
		
		tv_date.setText(m + "/" + d);
	}

	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        finish();
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
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInternetPresent = Utility.isNetworkConnected(InstructorSelectionForMVCSActivity.this);
		final_list.clear();
		final_list_name.clear();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_home:
			finish();
			break;
		case R.id.btn_is_for_vcs_submit:
			Log.i(TAG, "Male id = " + temp_maleid +"\nFemale id = "+temp_femaleid);
			final_list.addAll(temp_femaleid);
			final_list.addAll(temp_maleid);
			for (int i = 0; i < temp_femaleid.size(); i++) {
				final_list_name.add(female_UserName.get(female_UserID.indexOf(temp_femaleid.get(i))));
			}
			for (int i = 0; i < temp_maleid.size(); i++) {
				final_list_name.add(male_UserName.get(male_UserID.indexOf(temp_maleid.get(i))));
			}
			if(final_list.toString().replaceAll("\\[", "").replaceAll("\\]", "").isEmpty()||final_list.toString().replaceAll("\\[", "").replaceAll("\\]", "").equalsIgnoreCase("")){
				Toast.makeText(getApplicationContext(), "Please select at least one instructor.", Toast.LENGTH_LONG).show();
			}else{
				if(code==1){
					Intent i= new Intent(getApplicationContext(), ViewScheduleDeckSupervisorActivity.class);
					i.putExtra("FROM", "MANAGER");
					startActivity(i);
				}else if(code==2){
					Intent i = new Intent(getApplicationContext(),ManagerTodaysScheduleActivity.class);
					startActivity(i);
				}
			}
			break;
		default:
			break;
		}
	}
	
	
	
	private class GetSiteList extends AsyncTask<Void, Void, Void> {
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
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(
						SOAP_CONSTANTS.SOAP_Action_GetSiteList_1, envelope); // Calling
																			// Web
				SoapObject response = (SoapObject) envelope.getResponse();
				// SoapPrimitive response = (SoapPrimitive)
				// envelope.getResponse();
				Log.i(TAG, "" + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				Log.i(TAG, "mSoapObject1=" + mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				Log.i(TAG, "mSoapObject2=" + mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equalsIgnoreCase("000")) {
					getsitelist = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3=" + mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("Sites");
					Log.i(TAG, "jArray : " + jArray.toString());
					SiteID = new ArrayList<String>();
					SiteName = new ArrayList<String>();
					JSONObject jsonObject;
					for (int i = 0; i < jArray.length(); i++) {
						jsonObject = jArray.getJSONObject(i);
						SiteName.add(jsonObject.getString("SiteName"));
						SiteID.add(jsonObject.getString("SiteID"));
					}
					SiteName.add("All");
					SiteID.add("0");
				} else {
					getsitelist = false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				server_response = true;
			} catch (Exception e) {
				server_response = true;
				e.printStackTrace();
			}
//			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
//					SOAP_CONSTANTS.METHOD_NAME_GetSiteList);
//			request.addProperty("token", WW_StaticClass.UserToken);
//			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//					SoapEnvelope.VER11); // Make an Envelop for sending as whole
//			envelope.dotNet = true;
//			envelope.setOutputSoapObject(request);
//			Log.i("Request",  "Request = " + request);
//			HttpTransportSE androidHttpTransport = new HttpTransportSE(
//					SOAP_CONSTANTS.SOAP_ADDRESS);
//			try {
//				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetSiteList,
//						envelope); // Calling Web service
//				SoapObject response = (SoapObject)envelope.getResponse();
//				 Log.i(TAG,"" + response.toString());
//				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
//				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
//				 String code = mSoapObject2.getPropertyAsString(0).toString();
//				 Log.i("Code", code);
//				if(code.equalsIgnoreCase("000")){
//					getsitelist = true;
//					Object mSoapObject3 =  mSoapObject1.getProperty(1);
//					Log.i(TAG, "mSoapObject3="+mSoapObject3);
//					String resp = mSoapObject3.toString();
//					JSONObject jo = new JSONObject(resp);
//					JSONArray jArray = jo.getJSONArray("Sites");
//					Log.i(TAG,"jArray : " + jArray.toString());
//					SiteID = new ArrayList<String>();
//					SiteName = new ArrayList<String>();
//					JSONObject jsonObject ;
//					for(int i=0;i<jArray.length();i++){
//						jsonObject = jArray.getJSONObject(i);
//						SiteName.add(jsonObject.getString("SiteName"));
//						SiteID.add(jsonObject.getString("SiteID"));
//					}
//				}
//				else{
//					getsitelist =false;
//				}
//			}
//			catch(JSONException e){
//				e.printStackTrace();
//				server_response =true;
//			}
//			catch(Exception e){
//				server_response =true;
//				e.printStackTrace();
//			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(server_response){
				server_response = false;
				onDetectNetworkState().show();
			}
			else{
				if(!getsitelist){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							InstructorSelectionForMVCSActivity.this,"Error",
							"No site found.", "OK");
				}
				else{
					getsitelist=false;
					ll_is_for_vcs.removeAllViews();
					siteRadioGroup = new RadioGroup(getApplicationContext());
					siteRadioGroup.removeAllViews();
					siteRadioButton = new RadioButton[SiteID.size()];
					
					siteRadioGroup.setOrientation(RadioGroup.HORIZONTAL);
					   for (int i = 0; i < SiteName.size(); i++) {
					    siteRadioButton[i] = new RadioButton(getApplicationContext());
					    siteRadioGroup.addView(siteRadioButton[i]);
					    siteRadioButton[i].setText(SiteName.get(i));
					    siteRadioButton[i].setId(i);
					    siteRadioButton[i].setButtonDrawable(android.R.drawable.btn_radio);
					    siteRadioButton[i].setTextColor(getResources().getColor(R.color.texts1));
					    siteRadioButton[i].setTextSize(15);
					    
					   }
					    ll_is_for_vcs.addView(siteRadioGroup);
					    siteRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
							

							public void onCheckedChanged(RadioGroup group, int checkedId) {
								// TODO Auto-generated method stub
								try{
									temp_siteid = SiteID.get(checkedId);
									Log.i(TAG, "Site id = " + temp_siteid);
									new GetInstructor().execute();
								}
								catch(Exception e){
									e.printStackTrace();
								}
							}
						});
				}
			}
		}
	}
	
	
	private class GetInstructor extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Date date = new Date();
	        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	        currentDateandTime = format.format(date);
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_Get_InstrctListForMgrBySite);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("siteid", temp_siteid);
			request.addProperty("strRScheDate", currentDateandTime);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Get_InstrctListForMgrBySite,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
				 Log.i(TAG,"" + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if(code.equalsIgnoreCase("000")){
					 getinstructor = true;
						Object mSoapObject3 =  mSoapObject1.getProperty(1);
						Log.i(TAG, "mSoapObject3="+mSoapObject3);
						String resp = mSoapObject3.toString();
						JSONObject jo = new JSONObject(resp);
						JSONArray jArray = jo.getJSONArray("InstrListBySiteid");
						Log.i(TAG,"jArray : " + jArray.toString());
						male_UserID = new ArrayList<String>();
						male_UserName = new ArrayList<String>();
						female_UserID = new ArrayList<String>();
						female_UserName = new ArrayList<String>();
						JSONObject jsonObject ;
						for(int i=0;i<jArray.length();i++){
							jsonObject = jArray.getJSONObject(i);
							String gender = jsonObject.getString("Gender");
							if(gender.toString().equalsIgnoreCase("Female")){
								female_UserName.add(jsonObject.getString("UserName"));
								female_UserID.add(jsonObject.getString("Userid"));
							}else{
								male_UserName.add(jsonObject.getString("UserName"));
								male_UserID.add(jsonObject.getString("Userid"));
							}
						}
					}
					else{
						getinstructor =false;
					}
				}
				catch(SocketTimeoutException e){
					e.printStackTrace();
					other_problem = true;
				}
				catch(JSONException e){
					e.printStackTrace();
					server_response =true;
				}
			catch(SocketException e){
				e.printStackTrace();
				other_problem = true;
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
			if(server_response){
				server_response = false;
				onDetectNetworkState().show();
			}
			else if(other_problem){
				other_problem = false;
				ReConnect().show();
			}
			else{
				if(!getinstructor){
					temp_maleid.clear();
					temp_femaleid.clear();
					ll_male.setAdapter(null);
					ll_female.setAdapter(null);
					tv_is_for_vcs_no_inst.setVisibility(View.VISIBLE);
				}
				else{
					getinstructor=false;
					tv_is_for_vcs_no_inst.setVisibility(View.GONE);
					temp_maleid.clear();
					temp_femaleid.clear();
					if(male_UserID.size()>0){
						MaleListAdapter.notifyDataSetChanged();
						ll_male.setAdapter(MaleListAdapter);
					}
					if(female_UserID.size()>0){
						FeMaleListAdapter.notifyDataSetChanged();
						ll_female.setAdapter(FeMaleListAdapter);
					}
					
				}
			}
		}
	}
	public class ViewHolder{
		CheckBox ch_male_selection,ch_female_selection;
		TextView tv_male_selection,tv_female_selection;
		
	}
	/*------------------------------------Male-----------------------------------------------------*/
	BaseAdapter MaleListAdapter = new BaseAdapter() {
		private int[] colors = new int[] { Color.parseColor("#EEEEEE"), Color.parseColor("#FFFFFF") };

		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			try{
				if (convertView == null) {
					holder = new ViewHolder();
					

					convertView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.checkbox_row, null);
					int colorpos=position%colors.length;
					convertView.setBackgroundColor(colors[colorpos]);
				 holder.ch_male_selection = (CheckBox)convertView.findViewById(R.id.chb_row);
				 holder.tv_male_selection = (TextView)convertView.findViewById(R.id.tv_row);
				 holder.tv_male_selection.setTextColor(Color.rgb(0, 0, 102));
				 holder.tv_male_selection.setText(male_UserName.get(position));
				holder.ch_male_selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(isChecked){
							temp_maleid.add(male_UserID.get(position));
						}
						else{
							temp_maleid.remove(male_UserID.get(position));
						}
					}
				});
				}
				else {
					holder = (ViewHolder) convertView.getTag();
				}
	            
			}
			catch(OutOfMemoryError e){
				e.printStackTrace();
			}
			catch(IndexOutOfBoundsException e){
				e.printStackTrace();
			}
			catch(NullPointerException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return convertView;
		}
		

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return male_UserName.get(position);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return male_UserID.size();
		}
		
		@Override
		public int getViewTypeCount() {

			return getCount();
		}

		@Override
		public int getItemViewType(int position) {

			return position;
		}
	};
	
	/*------------------------------------Female-----------------------------------------------------*/
	BaseAdapter FeMaleListAdapter = new BaseAdapter() {
		private int[] colors = new int[] { Color.parseColor("#EEEEEE"), Color.parseColor("#FFFFFF") };

		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			try{
				if (convertView == null) {
					holder = new ViewHolder();
					

					convertView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.checkbox_row, null);
					int colorpos=position%colors.length;
					convertView.setBackgroundColor(colors[colorpos]);
				 holder.ch_female_selection = (CheckBox)convertView.findViewById(R.id.chb_row);
				 holder.tv_female_selection = (TextView)convertView.findViewById(R.id.tv_row);
				 holder.tv_female_selection.setTextColor(Color.rgb(136, 0, 183));
				 holder.tv_female_selection.setText(female_UserName.get(position));
				 holder.ch_female_selection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						

						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							// TODO Auto-generated method stub
							if(isChecked){
								temp_femaleid.add(female_UserID.get(position));
							}
							else{
								temp_femaleid.remove(female_UserID.get(position));
							}
						}
					});
				}
				else {
					holder = (ViewHolder) convertView.getTag();
				}
	            
			}
			catch(OutOfMemoryError e){
				e.printStackTrace();
			}
			catch(IndexOutOfBoundsException e){
				e.printStackTrace();
			}
			catch(NullPointerException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return convertView;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return female_UserName.get(position);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return female_UserID.size();
		}
		
		@Override
		public int getViewTypeCount() {

			return getCount();
		}

		@Override
		public int getItemViewType(int position) {

			return position;
		}
	};
	
	public AlertDialog ReConnect(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Connection timeout.")
		.setTitle("WaterWorks.")
		      
		.setPositiveButton("Retry",new DialogInterface.OnClickListener() {


		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		    	new GetInstructor().execute();
		    }
		});
		    return builder1.create();
	}
}
