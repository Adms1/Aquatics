package water.works.waterworks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class RequestDeckMenuActivity extends Activity {
	FrameLayout fl_menu_loading;
	LinearLayout ll_poollist,ll_site ;
	RadioButton[] rb_poollist,rb_sitelist;
	RadioGroup rg_poollist,rg_sitelist;
	Boolean cansend = false;
	String whattimeforassist = "-1",mytime,formattedDate,DeskAssistID_web ;
	String emp_type_for_cee="",emp_type_for_cee_manager="",emp_type_for_aquatics="",emp_userid_for_cee="",emp_userid_for_cee_manager="",emp_userid_for_aquatics="";
	ListPopupWindow listpopupwindow_cee_staff,listpopupwindow_cee_manager,listpopupwindow_aquatics_manager,Site_Selection;
	ArrayList<String> PoolName,PoolId;
	ArrayList<String> ANYCEE_name = new ArrayList<String>();
	ArrayList<String> ANYCEEManager_name = new ArrayList<String>();
	ArrayList<String> ANYAQUATICSMANAGER_name = new ArrayList<String>();
	ArrayList<String> ANYCEE_id = new ArrayList<String>();
	ArrayList<String> ANYCEEManager_id = new ArrayList<String>();
	ArrayList<String> ANYAQUATICSMANAGER_id = new ArrayList<String>();
	//	ArrayList<String> SiteID,SiteName;
	String SITEID;
	String desk_poolid ="-1";
	private static String TAG = "RequestDeckMenu";
	boolean data_load_status,server_status,pool_status,getsitelist,status = false;
	private boolean status_req_cee=false,status_req_cee_manager=false,status_req_aqu=false;
	Boolean isInternetPresent = false;
	private View decorView;
	private int uiOptions;
	Titanic titanic;
	TitanicTextView tv ;
	Button btn_site_selection,rc_send_request,btn_cee_manager,btn_cee_staff,btn_aquatics_manager;
	RadioButton rb_any_cee,rb_any_cee_manager,rb_any_aquatics_manager,rc_now,rc_min;
	ScrollView sv_request_deck;
	ImageButton close;
	CheckBox chk_cee,chk_cee_manager,chk_aquatics_manager;
	@SuppressLint({ "InlinedApi", "SimpleDateFormat" })
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
		//
		//		createUiChangeListener();

		setContentView(R.layout.requestdeck_menu_land);

		isInternetPresent = Utility
				.isNetworkConnected(RequestDeckMenuActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}

		initialize();
		WW_StaticClass.duration1 = 300;
		WW_StaticClass.duration2 = 1000;

		Date date = new Date();
		SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm");
		//        System.out.println("New Date--->" + format1.format(date));
		mytime = format1.format(date);
		Log.i(TAG, "my time = " +mytime);

		ll_site.removeAllViews();
		rg_sitelist = new RadioGroup(getApplicationContext());
		rg_sitelist.removeAllViews();
		rb_sitelist = new RadioButton[WW_StaticClass.sitename.size()];

		rg_sitelist.setOrientation(RadioGroup.HORIZONTAL);
		for (int i = 0; i < WW_StaticClass.sitename.size(); i++) {
			rb_sitelist[i] = new RadioButton(getApplicationContext());
			rg_sitelist.addView(rb_sitelist[i]);
			rb_sitelist[i].setText(WW_StaticClass.sitename.get(i));
			rb_sitelist[i].setId(i);
			rb_sitelist[i].setButtonDrawable(android.R.drawable.btn_radio);
			rb_sitelist[i].setTextColor(getResources().getColor(R.color.texts1));
			rb_sitelist[i].setTextSize(15);

		}
		ll_site.addView(rg_sitelist);
		rg_sitelist.setOnCheckedChangeListener(new OnCheckedChangeListener() {


			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				try{
					int tempsiteindex = WW_StaticClass.sitename.indexOf(WW_StaticClass.sitename.get(checkedId));
					SITEID = WW_StaticClass.siteid.get(tempsiteindex);
					Log.i(TAG, "Site id = " + SITEID);
					ll_poollist.removeAllViews();
					rg_poollist = new RadioGroup(getApplicationContext());
					rg_poollist.removeAllViews();
					rb_poollist = new RadioButton[0];
					new IamInPool().execute();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		});



		rc_now.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				// TODO Auto-generated method stub
				rc_now.setChecked(true);
				rc_min.setChecked(false);
				whattimeforassist = "1";
				rc_min.setError(null);
				rc_now.setError(null);
			}
		});
		rc_min.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				rc_now.setChecked(false);
				rc_min.setChecked(true);
				whattimeforassist = "2";
				rc_min.setError(null);
				rc_now.setError(null);
			}
		});

		/*		
		rb_any_cee.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rb_any_cee.setChecked(true);
				btn_cee_staff.setText("");
				emp_type_for_cee = "1";
				emp_userid_for_cee = "-1";

			}
		});

		rb_any_cee_manager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rb_any_cee_manager.setChecked(true);
				btn_cee_manager.setText("");

				emp_type_for_cee_manager = "2";
				emp_userid_for_cee_manager = "-1";
			}
		});

		rb_any_aquatics_manager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rb_any_aquatics_manager.setChecked(true);
				btn_aquatics_manager.setText("");

				emp_type_for_aquatics = "3";
				emp_userid_for_aquatics = "-1";
			}
		});

		btn_site_selection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Site_Selection.show();
			}
		});

		btn_cee_staff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listpopupwindow_cee_staff.show();
			}
		});



		btn_cee_manager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				rb_any_cee.setEnabled(false);
//				rb_any_cee_manager.setEnabled(false);
//				btn_cee_staff.setEnabled(false);
				listpopupwindow_cee_manager.show();
			}
		});



		btn_aquatics_manager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listpopupwindow_aquatics_manager.show();
			}
		});

        /////////////// Send request
		rc_send_request.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(desk_poolid.equalsIgnoreCase("-1")&&whattimeforassist.equalsIgnoreCase("-1")){
					for(int i=0;i<PoolName.size();i++){
						rb_poollist[i].setError("Please select anyone option");
					}
					rc_min.setError("Please select anyone option");
					rc_now.setError("Please select anyone option");
				}
				else if(desk_poolid.equalsIgnoreCase("-1")){
					for(int i=0;i<PoolName.size();i++){
						rb_poollist[i].setError("Please select anyone option");
					}
				}
				else if(whattimeforassist.equalsIgnoreCase("-1")){
					rc_min.setError("Please select anyone option");
					rc_now.setError("Please select anyone option");
				}
				else{
					new InsertRequestDesk().execute();
					rb_any_cee.setChecked(false);
					rb_any_cee_manager.setChecked(false);
					rb_any_aquatics_manager.setChecked(false);
					btn_cee_staff.setText("");
					btn_cee_manager.setText("");
					btn_aquatics_manager.setText("");
					rc_min.setChecked(false);
					rc_now.setChecked(false);
					rg_poollist.clearCheck();

					desk_poolid = "-1";
					whattimeforassist = "-1";
				}
			}
		});*/

		rc_send_request.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cansend){
					cansend = false;
					if(desk_poolid.equalsIgnoreCase("-1")&&whattimeforassist.equalsIgnoreCase("-1")){
						for(int i=0;i<PoolName.size();i++){
							rb_poollist[i].setError("Please select anyone option");
						}
						rc_min.setError("Please select anyone option");
						rc_now.setError("Please select anyone option");
					}
					else if(desk_poolid.equalsIgnoreCase("-1")){
						for(int i=0;i<PoolName.size();i++){
							rb_poollist[i].setError("Please select anyone option");
						}
					}
					else if(whattimeforassist.equalsIgnoreCase("-1")){
						rc_min.setError("Please select anyone option");
						rc_now.setError("Please select anyone option");
					}
					else{
						if(chk_cee.isChecked()){
							emp_type_for_cee = "1";
							emp_userid_for_cee ="-1";

						}
						if(chk_cee_manager.isChecked()){
							emp_type_for_cee_manager = "2";
							emp_userid_for_cee_manager ="-1";

						}
						if(chk_aquatics_manager.isChecked()){
							emp_type_for_aquatics = "3";
							emp_userid_for_aquatics ="-1";

						}
						new InsertRequestDesk().execute();
						chk_aquatics_manager.setChecked(false);
						chk_cee.setChecked(false);
						chk_cee_manager.setChecked(false);
						rc_min.setChecked(false);
						rc_now.setChecked(false);
						rg_poollist.clearCheck();
						rg_sitelist.clearCheck();

						desk_poolid = "-1";
						whattimeforassist = "-1";
					}
				}
				else{
					Toast.makeText(getApplicationContext(), "Try with other site.", Toast.LENGTH_LONG).show();
				}
			}
		});
		close.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}


	private void initialize() {
		// TODO Auto-generated method stub
		titanic = new Titanic();
		tv = (TitanicTextView) findViewById(R.id.my_text_view);
		tv.setTypeface(Typefaces.get(RequestDeckMenuActivity.this, "Satisfy-Regular.ttf"));
		titanic.start(tv);
		fl_menu_loading = (FrameLayout)findViewById(R.id.rd_menu_loading);
		fl_menu_loading.setVisibility(View.GONE);
		chk_cee = (CheckBox)findViewById(R.id.chk_cee);
		chk_cee_manager = (CheckBox)findViewById(R.id.chk_cee_manager);
		chk_aquatics_manager = (CheckBox)findViewById(R.id.chk_aquatics_manager);
		rc_now=(RadioButton)findViewById(R.id.rc_now);
		rc_min=(RadioButton)findViewById(R.id.rc_min);
		rc_send_request = (Button)findViewById(R.id.btn_rc_send_request);
		ll_poollist = (LinearLayout)findViewById(R.id.ll_pool_list);
		ll_site = (LinearLayout)findViewById(R.id.ll_site_list);
		rg_poollist = new RadioGroup(getApplicationContext());
		rg_sitelist = new RadioGroup(getApplicationContext());

		/*btn_site_selection = (Button)findViewById(R.id.btn_rc_site_list);
		rb_any_cee = (RadioButton)findViewById(R.id.rb_menu_cee);
		rb_any_cee_manager = (RadioButton)findViewById(R.id.rb_menu_cee_manager);
		rb_any_aquatics_manager = (RadioButton)findViewById(R.id.rb_menu_rc_aquatics_manager);
		rc_now=(RadioButton)findViewById(R.id.rc_menu_now);
		rc_min=(RadioButton)findViewById(R.id.rc_menu_min);
		btn_aquatics_manager = (Button)findViewById(R.id.et_menu_rc_aquatics_manager);
		btn_cee_staff = (Button)findViewById(R.id.et_menu_rc_cee_staff);
		btn_cee_manager = (Button)findViewById(R.id.et_menu_rc_cee_manager);
		ll_poollist = (LinearLayout)findViewById(R.id.ll_menu_pool_list);
		rg_poollist = new RadioGroup(getApplicationContext());
		rc_send_request = (Button)findViewById(R.id.btn_menu_rc_send_request);
		Site_Selection =  new ListPopupWindow(RequestDeckMenuActivity.this);
		listpopupwindow_cee_staff = new ListPopupWindow(RequestDeckMenuActivity.this);
		listpopupwindow_cee_manager = new ListPopupWindow(RequestDeckMenuActivity.this);
		listpopupwindow_aquatics_manager = new ListPopupWindow(RequestDeckMenuActivity.this);
		sv_request_deck = (ScrollView)findViewById(R.id.sv_request_deck);
		sv_request_deck.setVisibility(View.INVISIBLE);*/
		close = (ImageButton)findViewById(R.id.close);
	}


	public class IamInPool extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			formattedDate = mytime;
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,SOAP_CONSTANTS.METHOD_NAME_GETPOOLLIST);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("siteid", SITEID);
			// Log.i(Tag, "Login name"+mEd_User.getText().toString());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			PoolId = new ArrayList<String>();
			PoolName = new ArrayList<String>();
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_ACTION_POOLLIST, envelope); // Calling
				// Web
				// service

				SoapObject response =  (SoapObject) envelope.getResponse();
				Log.i("here","Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				Log.i("Current Lesson", "mSoapObject1="+mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				Log.i("Current Lesson", "mSoapObject2="+mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				//				response.toString();
				if (code.equals("000")) {
					pool_status=true;
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i("Current Lesson", "mSoapObject3="+mSoapObject3);
					String resp = mSoapObject3.toString();


					//				String resp = envelope.getResponse().toString();// response.toString().trim();
					Log.i("here","Result : " + resp.toString());
					JSONObject jobj = new JSONObject(resp);
					JSONArray mArray = jobj.getJSONArray("Pools");
					for (int i = 0; i < mArray.length(); i++) {
						JSONObject mJsonObjectFee = mArray.getJSONObject(i);
						PoolId.add(mJsonObjectFee.getString("PoolId"));
						PoolName.add(mJsonObjectFee.getString("PoolName"));
					}	
				}
				else{
					pool_status=false;
				}
			} catch (Exception e) {
				server_status=true;
				e.printStackTrace();

			}
			return null;

		}


		@Override
		protected void onPostExecute(Void result) {
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						RequestDeckMenuActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else{
				if(pool_status)
				{
					pool_status = false;
					cansend = true;
					rb_poollist = new RadioButton[PoolName.size()];

					rg_poollist.setOrientation(RadioGroup.HORIZONTAL);
					for (int i = 0; i < PoolName.size(); i++) {
						rb_poollist[i] = new RadioButton(getApplicationContext());
						rg_poollist.addView(rb_poollist[i]);
						rb_poollist[i].setText(PoolName.get(i));
						rb_poollist[i].setId(i);
						rb_poollist[i].setButtonDrawable(android.R.drawable.btn_radio);
						rb_poollist[i].setTextColor(getResources().getColor(R.color.texts1));
						rb_poollist[i].setTextSize(15);

					}
					ll_poollist.addView(rg_poollist);
					rg_poollist.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						public void onCheckedChanged(RadioGroup group, int checkedId) {
							// TODO Auto-generated method stub
							try{
								int a = PoolName.indexOf(PoolName.get(checkedId));
								Log.i("Here", ""+a);
								String poolidvalue = PoolId.get(a);
								Log.i("poolid", ""+poolidvalue);
								desk_poolid = poolidvalue;

								for(int j=0;j<PoolName.size();j++){
									rb_poollist[j].setError(null);
								}
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
					});
				}
				else{
					cansend = false;
					SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this,"WaterWorks", "No pool found.", "Ok");
				}
			}
		}
	}
	/*
	public class DeskdataforCEE extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			pDialog3 = new ProgressDialog(ViewCurrentLessonActivity.this);
//			pDialog3.setMessage(Html.fromHtml("Loading wait..."));
//			pDialog3.setIndeterminate(true);
//			pDialog3.setCancelable(false);
//			pDialog3.show();
			fl_menu_loading.setVisibility(View.VISIBLE);
			fl_menu_loading.bringToFront();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetUsersByType );
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("SiteID", SITEID);
			request.addProperty("usertype", 1);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
					androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetUsersByType,
						envelope); // Calling Web service
					SoapObject response = (SoapObject) envelope.getResponse();
					 Log.i("Any CEE","Result : " + response.toString());
					 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
					 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
					 String code = mSoapObject2.getPropertyAsString(0).toString();
					 if (code.equals("000")) {
						status = true;
						 Object mSoapObject3 =   mSoapObject1.getProperty(1);
							JSONObject jo = new JSONObject(mSoapObject3.toString());
							JSONArray jArray = jo.getJSONArray("CEE Staff");
							JSONObject jsonObject;
							for(int i=0;i<jArray.length();i++){
								jsonObject = jArray.getJSONObject(i);
								ANYCEE_name.add(jsonObject.getString("EmployeeName"));
								ANYCEE_id.add(jsonObject.getString("UserId"));
							}
							Log.e(TAG, "CEE NAME= " +ANYCEE_name);
							Log.e(TAG, "CEE ID = " +ANYCEE_id);
					 }

			}
			catch(SocketTimeoutException e){
				server_status = true;
				e.printStackTrace();
			}
			catch(Exception e){
				server_status = true;
				e.printStackTrace();
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
//			pDialog3.dismiss();
			fl_menu_loading.setVisibility(View.GONE);
			if(server_status==true){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
				server_status = false;
			}
			if(status==false){
				Toast.makeText(getApplicationContext(), "No users found", 1).show();
//				SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
			}
			else{
				status =false;
				listpopupwindow_cee_staff.setAdapter(new ArrayAdapter<String>(
			            getApplicationContext(),
			            R.layout.edittextpopup,ANYCEE_name ));
		        listpopupwindow_cee_staff.setAnchorView(btn_cee_staff);
		        listpopupwindow_cee_staff.setHeight(300);
		        listpopupwindow_cee_staff.setModal(true);
		        listpopupwindow_cee_staff.setOnItemClickListener(
		            new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int pos, long id) {
							// TODO Auto-generated method stub
							btn_cee_staff.setText(ANYCEE_name.get(pos));
							rb_any_cee.setChecked(false);
							emp_type_for_cee ="1";
							emp_userid_for_cee = ANYCEE_id.get(pos);
							listpopupwindow_cee_staff.dismiss();
						}
					});
			}
		}

	}

ProgressDialog pDialog4;		
public class DeskdataforCEEManager extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			pDialog4 = new ProgressDialog(ViewCurrentLessonActivity.this);
//			pDialog4.setMessage(Html.fromHtml("Loading wait..."));
//			pDialog4.setIndeterminate(true);
//			pDialog4.setCancelable(false);
//			pDialog4.show();
			fl_menu_loading.setVisibility(View.VISIBLE);
			fl_menu_loading.bringToFront();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetUsersByType );
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("SiteID",SITEID);
			request.addProperty("usertype", 2);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetUsersByType,
					envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.getResponse();
				 Log.i("Any CEE","Result : " + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 if (code.equals("000")) {
					status = true;
					 Object mSoapObject3 =   mSoapObject1.getProperty(1);
						JSONObject jo = new JSONObject(mSoapObject3.toString());
						JSONArray jArray = jo.getJSONArray("CEE Manager");
						JSONObject jsonObject;
						for(int i=0;i<jArray.length();i++){
							jsonObject = jArray.getJSONObject(i);
							ANYCEEManager_name.add(jsonObject.getString("EmployeeName"));
							ANYCEEManager_id.add(jsonObject.getString("UserId"));
						}
						Log.e(TAG, "ANYCEEManager name= " +ANYCEEManager_name);
						Log.e(TAG, "ANYCEEManager id = " +ANYCEEManager_id);
				 }

		}
		catch(SocketTimeoutException e){
			server_status = true;
			e.printStackTrace();
		}
		catch(Exception e){
			server_status = true;
			e.printStackTrace();
		}

		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		pDialog4.dismiss();
		fl_menu_loading.setVisibility(View.GONE);
		if(server_status==true){
			SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
			server_status = false;
		}
		if(status==false){
			Toast.makeText(getApplicationContext(), "No users found", 1).show();
//			SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
		}
		else{
			status = false;
			listpopupwindow_cee_manager.setAdapter(new ArrayAdapter<String>(
		            getApplicationContext(),
		            R.layout.edittextpopup,ANYCEEManager_name));
	        listpopupwindow_cee_manager.setAnchorView(btn_cee_manager);
//	        listpopupwindow.setWidth(90);
	        listpopupwindow_cee_manager.setHeight(200);
	        listpopupwindow_cee_manager.setModal(true);
	        listpopupwindow_cee_manager.setOnItemClickListener(
	            new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int pos, long id) {
						// TODO Auto-generated method stub
//						holder.et_sLevel.setText(LevelName.get(pos));
						btn_cee_manager.setText(ANYCEEManager_name.get(pos));
						rb_any_cee_manager.setChecked(false);
						emp_type_for_cee_manager ="2";
						emp_userid_for_cee_manager = ANYCEEManager_id.get(pos);
						listpopupwindow_cee_manager.dismiss();
					}
				});

		}
	}

}


ProgressDialog pDialog5;		
public class DeskdataforAquaticsManager extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			pDialog5 = new ProgressDialog(ViewCurrentLessonActivity.this);
//			pDialog5.setMessage(Html.fromHtml("Loading wait..."));
//			pDialog5.setIndeterminate(true);
//			pDialog5.setCancelable(false);
//			pDialog5.show();
			fl_menu_loading.setVisibility(View.VISIBLE);
			fl_menu_loading.bringToFront();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetUsersByType );
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("SiteID", SITEID);
			request.addProperty("usertype", 3);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetUsersByType,
					envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.getResponse();
				 Log.i("Any CEE","Result : " + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 if (code.equals("000")) {
					status = true;
					 Object mSoapObject3 =   mSoapObject1.getProperty(1);
						JSONObject jo = new JSONObject(mSoapObject3.toString());
						JSONArray jArray = jo.getJSONArray("Aquatics Manager");
						JSONObject jsonObject;
						for(int i=0;i<jArray.length();i++){
							jsonObject = jArray.getJSONObject(i);
							ANYAQUATICSMANAGER_name.add(jsonObject.getString("EmployeeName"));
							ANYAQUATICSMANAGER_id.add(jsonObject.getString("UserId"));
						}
						Log.e(TAG, "ANYAQUATICSMANAGER name= " +ANYAQUATICSMANAGER_name);
						Log.e(TAG, "ANYAQUATICSMANAGER id = " +ANYAQUATICSMANAGER_id);
				 }

		}
		catch(SocketTimeoutException e){
			server_status = true;
			e.printStackTrace();
		}
		catch(Exception e){
			server_status = true;
			e.printStackTrace();
		}

		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		pDialog5.dismiss();
		fl_menu_loading.setVisibility(View.GONE);
		if(server_status==true){
			SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
			server_status = false;
		}
		if(status==false){
			Toast.makeText(getApplicationContext(), "No users found", 1).show();
//			SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
		}
		else{
			status =false;
			listpopupwindow_aquatics_manager.setAdapter(new ArrayAdapter<String>(
		            getApplicationContext(),
		            R.layout.edittextpopup,ANYAQUATICSMANAGER_name));
	        listpopupwindow_aquatics_manager.setAnchorView(btn_aquatics_manager);
	        listpopupwindow_aquatics_manager.setHeight(300);
	        listpopupwindow_aquatics_manager.setModal(true);
	        listpopupwindow_aquatics_manager.setOnItemClickListener(
	            new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int pos, long id) {
						// TODO Auto-generated method stub
//						holder.et_sLevel.setText(LevelName.get(pos));

						btn_aquatics_manager.setText(ANYAQUATICSMANAGER_name.get(pos));
						rb_any_aquatics_manager.setChecked(false);
						emp_type_for_aquatics = "3";
						emp_userid_for_aquatics = ANYAQUATICSMANAGER_id.get(pos);
						listpopupwindow_aquatics_manager.dismiss();
					}
				});

		}
	}

}

	 */	

	public class InsertRequestDesk extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//			progressDialog = new ProgressDialog(ViewCurrentLessonActivity.this);
			//			progressDialog.setMessage(Html.fromHtml("Loading wait..."));
			//			progressDialog.setIndeterminate(true);
			//			progressDialog.setCancelable(false);
			//			progressDialog.show();
			fl_menu_loading.setVisibility(View.VISIBLE);
			fl_menu_loading.bringToFront();
			Date date = new Date();
			SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			mytime = format1.format(date);
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			formattedDate = mytime;
			Log.e("Pool id", "pool id = " + desk_poolid);
			Log.e("Time or now", "Time or now = " + whattimeforassist);
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssist );
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("Rinstructorid",WW_StaticClass.InstructorID);
			request.addProperty("RAssistDate",formattedDate);
			request.addProperty("RSiteId", SITEID);
			request.addProperty("RPoolID", desk_poolid);
			request.addProperty("RNeedTime", whattimeforassist);


			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertDeckAssist,
						envelope); // Calling Web service
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				Log.i("Insert Desk Assist","Result : " + response.toString());
				String rep = response.toString();
				JSONObject jsonObject = new JSONObject(rep);
				JSONArray jsonObject2 = jsonObject.getJSONArray("DeckAssitID");
				DeskAssistID_web = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]","");
				Log.e("DeckAssitID", "DeckAssitID " + DeskAssistID_web );
				status = true;

			}
			catch(JSONException e){
				server_status = true;
				e.printStackTrace();
			}
			catch(Exception e){
				server_status = true;
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//			progressDialog.dismiss();
			fl_menu_loading.setVisibility(View.GONE);
			if(server_status==true){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
				server_status = false;
			}
			else{
				if(status==true){

					if(!DeskAssistID_web.toString().equalsIgnoreCase("")){
						if(!emp_type_for_cee.toString().equalsIgnoreCase("")){
							new InsertDeskAssistUser_forCEE().execute();
						}
						if(!emp_type_for_cee_manager.toString().equalsIgnoreCase("")){
							new InsertDeskAssistUser_forCEEManager().execute();
						}
						if(!emp_type_for_aquatics.toString().equalsIgnoreCase("")){
							new InsertDeskAssistUser_forAquatics().execute();
						}

					}
					else{
						SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "WaterWorks", "No data found, Please try again..", "Ok");
					}
					status=false;
				}
				else{
					SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
				}
			}


		}

		ProgressDialog progressDialog2;
		public class InsertDeskAssistUser_forCEE extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				//				progressDialog2 = new ProgressDialog(ViewCurrentLessonActivity.this);
				//				progressDialog2.setMessage(Html.fromHtml("Loading wait..."));
				//				progressDialog2.setIndeterminate(true);
				//				progressDialog2.setCancelable(false);
				//				progressDialog2.show();
				fl_menu_loading.setVisibility(View.VISIBLE);
				fl_menu_loading.bringToFront();
			}
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
						SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssistUser );
				request.addProperty("token",  WW_StaticClass.UserToken);
				request.addProperty("RDeckAssistID", DeskAssistID_web);
				request.addProperty("REmpType", emp_type_for_cee);
				request.addProperty("RUserID", emp_userid_for_cee);


				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11); // Make an Envelop for sending as whole
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				Log.i("Request",  "Request = " + request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						SOAP_CONSTANTS.SOAP_ADDRESS);
				try {
					androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertDeckAssistUser,
							envelope); // Calling Web service
					SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
					Log.i("Insert Desk Assist User","Result : " + response.toString());
					String rep = response.toString();
					JSONObject jsonObject = new JSONObject(rep);
					JSONArray jsonObject2 = jsonObject.getJSONArray("DeckAssitUserID");
					String id = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]","");
					Log.e("DeckAssitUserID", "DeckAssitUserID " + id);
					status_req_cee = true;
				}
				catch(JSONException e){
					e.printStackTrace();
					server_status = true;
				}
				catch(Exception e){
					e.printStackTrace();
					server_status = true;
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				//				progressDialog2.dismiss();
				fl_menu_loading.setVisibility(View.GONE);
				if(server_status){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
					server_status = false;
				}
				if(status_req_cee){
					status_req_cee= false;
					SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "Request Send", "Request send to cee staff", "Ok");
				}
				else{

				}
			}
		}


		ProgressDialog progressDialog3;
		public class InsertDeskAssistUser_forAquatics extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				//				progressDialog3 = new ProgressDialog(ViewCurrentLessonActivity.this);
				//				progressDialog3.setMessage(Html.fromHtml("Loading wait..."));
				//				progressDialog3.setIndeterminate(true);
				//				progressDialog3.setCancelable(false);
				//				progressDialog3.show();
				fl_menu_loading.setVisibility(View.VISIBLE);
				fl_menu_loading.bringToFront();
			}
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
						SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssistUser );
				request.addProperty("token",  WW_StaticClass.UserToken);
				request.addProperty("RDeckAssistID", DeskAssistID_web);
				request.addProperty("REmpType", emp_type_for_aquatics);
				request.addProperty("RUserID", emp_userid_for_aquatics);


				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11); // Make an Envelop for sending as whole
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				Log.i("Request",  "Request = " + request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						SOAP_CONSTANTS.SOAP_ADDRESS);
				try {
					androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertDeckAssistUser,
							envelope); // Calling Web service
					SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
					Log.i("Insert Desk Assist User","Result : " + response.toString());
					String rep = response.toString();
					JSONObject jsonObject = new JSONObject(rep);
					JSONArray jsonObject2 = jsonObject.getJSONArray("DeckAssitUserID");
					String id = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]","");
					Log.e("DeckAssitUserID", "DeckAssitUserID " + id);
					status_req_aqu = true;
				}
				catch(JSONException e){
					e.printStackTrace();
					server_status= true;
				}
				catch(Exception e){
					e.printStackTrace();
					server_status = true;
				}
				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				//				progressDialog3.dismiss();
				fl_menu_loading.setVisibility(View.GONE);
				if(server_status){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
					server_status = false;
				}
				if(status_req_aqu){
					status_req_aqu=false;
					SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "Request Send", "Request send to aquatics manager", "Ok");
				}
				else{

				}
			}

		}
	}

	ProgressDialog progressDialog4;
	public class InsertDeskAssistUser_forCEEManager extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//				progressDialog4 = new ProgressDialog(ViewCurrentLessonActivity.this);
			//				progressDialog4.setMessage(Html.fromHtml("Loading wait..."));
			//				progressDialog4.setIndeterminate(true);
			//				progressDialog4.setCancelable(false);
			//				progressDialog4.show();
			fl_menu_loading.setVisibility(View.VISIBLE);
			fl_menu_loading.bringToFront();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssistUser );
			request.addProperty("token",  WW_StaticClass.UserToken);
			request.addProperty("RDeckAssistID", DeskAssistID_web);
			request.addProperty("REmpType", emp_type_for_cee_manager);
			request.addProperty("RUserID", emp_userid_for_cee_manager);


			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertDeckAssistUser,
						envelope); // Calling Web service
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				Log.i("Insert Desk Assist User","Result : " + response.toString());
				String rep = response.toString();
				JSONObject jsonObject = new JSONObject(rep);
				JSONArray jsonObject2 = jsonObject.getJSONArray("DeckAssitUserID");
				String id = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]","");
				Log.e("DeckAssitUserID", "DeckAssitUserID " + id);
				status_req_cee_manager = true;
			}
			catch(JSONException e){
				e.printStackTrace();
				server_status = true;
			}
			catch(Exception e){
				e.printStackTrace();
				server_status = true;
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//				progressDialog4.dismiss();
			fl_menu_loading.setVisibility(View.GONE);
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
				server_status = false;
			}
			if(status_req_cee_manager){
				status_req_cee_manager = false;
				SingleOptionAlertWithoutTitle.ShowAlertDialog(RequestDeckMenuActivity.this, "Request Send", "Request send to cee manager", "Ok");
			}
			else{

			}
		}
	}

	/*private class GetSiteList extends AsyncTask<Void, Void, Void>{
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
					getsitelist = true;
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3="+mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("Sites");
					Log.i(TAG,"jArray : " + jArray.toString());
					SiteID = new ArrayList<String>();
					SiteName = new ArrayList<String>();
					JSONObject jsonObject ;
					for(int i=0;i<jArray.length();i++){
						jsonObject = jArray.getJSONObject(i);
						SiteName.add(jsonObject.getString("SiteName"));
						SiteID.add(jsonObject.getString("SiteID"));
					}
				}
				else{
					getsitelist =false;
				}
			}
			catch(JSONException e){
				e.printStackTrace();
				server_status =true;
			}
			catch(Exception e){
				server_status =true;
				e.printStackTrace();
			}
			return null;
		}
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						RequestDeckMenuActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else{
				if(!getsitelist){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							RequestDeckMenuActivity.this,"WaterWorks",
							"No site found.", "OK");
					AlertDialog alertDialog = new AlertDialog.Builder(RequestDeckMenuActivity.this).create();
					alertDialog.setTitle("WaterWorks");
					alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
					alertDialog.setCanceledOnTouchOutside(false); 
					alertDialog.setCancelable(false);
					alertDialog.setMessage("No site found");
					// set button1 functionality
					alertDialog.setButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// close dialog
									finish();
									dialog.cancel();

								}
							});
					// show the alert dialog
					alertDialog.show();
				}
				else{
					getsitelist= false;
					Site_Selection.setAdapter(new ArrayAdapter<String>(
				            getApplicationContext(),
				            R.layout.edittextpopup,SiteName ));
					Site_Selection.setAnchorView(btn_site_selection);
					Site_Selection.setHeight(300);
					Site_Selection.setModal(true);
					Site_Selection.setOnItemClickListener(
			            new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int pos, long id) {
								// TODO Auto-generated method stub
								btn_site_selection.setText(SiteName.get(pos));
								ll_poollist = (LinearLayout)findViewById(R.id.ll_menu_pool_list);
								ll_poollist.removeAllViews();
								rg_poollist = new RadioGroup(getApplicationContext());
								rg_poollist.removeAllViews();
								rb_poollist = new RadioButton[0];
								SITEID = SiteID.get(pos);
								new IamInPool().execute();
								ANYCEE_id.clear();
								ANYCEE_name.clear();
								ANYCEEManager_id.clear();
								ANYCEEManager_name.clear();
								ANYAQUATICSMANAGER_id.clear();
								ANYAQUATICSMANAGER_name.clear();
//								listpopupwindow_aquatics_manager = new ListPopupWindow(getApplicationContext());
								listpopupwindow_aquatics_manager.setAdapter(new ArrayAdapter<String>
								(getApplicationContext(),  R.layout.edittextpopup,ANYAQUATICSMANAGER_name ));
//								listpopupwindow_cee_manager = new ListPopupWindow(getApplicationContext());
								listpopupwindow_cee_manager.setAdapter(new ArrayAdapter<String>
								(getApplicationContext(),  R.layout.edittextpopup,ANYCEEManager_name));
//								listpopupwindow_cee_staff = new ListPopupWindow(getApplicationContext());
								listpopupwindow_cee_staff.setAdapter(new ArrayAdapter<String>
								(getApplicationContext(),  R.layout.edittextpopup,ANYCEE_name));
								btn_aquatics_manager.setText("");
								btn_cee_manager.setText("");
								btn_cee_staff.setText("");
								new DeskdataforCEE().execute();
								new DeskdataforCEEManager().execute();
								new DeskdataforAquaticsManager().execute();
								Handler handler = new Handler();
						        handler.postDelayed(new Runnable() {
						           @Override
						           public void run() {
						        	   rb_poollist = new RadioButton[PoolName.size()];

									   rg_poollist.setOrientation(RadioGroup.HORIZONTAL);
									   for (int i = 0; i < PoolName.size(); i++) {
									    rb_poollist[i] = new RadioButton(getApplicationContext());
									    rg_poollist.addView(rb_poollist[i]);
									    rb_poollist[i].setText(PoolName.get(i));
									    rb_poollist[i].setId(i);
									    rb_poollist[i].setButtonDrawable(android.R.drawable.btn_radio);
									    rb_poollist[i].setTextColor(getResources().getColor(R.color.texts1));
									    rb_poollist[i].setTextSize(24);

									   }
									    ll_poollist.addView(rg_poollist);
									    rg_poollist.setOnCheckedChangeListener(new OnCheckedChangeListener() {

											@Override
											public void onCheckedChanged(RadioGroup group, int checkedId) {
												// TODO Auto-generated method stub
												try{
													int a = PoolName.indexOf(PoolName.get(checkedId));
													Log.i("Here", ""+a);
													String poolidvalue = PoolId.get(a);
													Log.i("poolid", ""+poolidvalue);
													desk_poolid = poolidvalue;

													for(int j=0;j<PoolName.size();j++){
														rb_poollist[j].setError(null);
													}
												}
												catch(Exception e){
													e.printStackTrace();
												}
											}
										});

						           }
						    }, 5000);

								Site_Selection.dismiss();
								sv_request_deck.setVisibility(View.VISIBLE);
							}
						});

				}
			}
		}
	}
	 */
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

	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				RequestDeckMenuActivity.this.finish();
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//		decorView.setSystemUiVisibility(uiOptions);
		if(isInternetPresent){
			//			new GetSiteList().execute();
		}
		else{
			onDetectNetworkState().show();
		}
	}


}
