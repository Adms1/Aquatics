package water.works.waterworks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import water.works.waterworks.adapter.CustomMenuDataPageAdapter;
import water.works.waterworks.adapter.HomeGridMenuOptionAdapter;
import water.works.waterworks.chat.Chat_Friends_list;
import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.services.DeckNotificationService;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;

public class MenuActivity extends Activity implements OnClickListener {
	boolean connectionout=false;
	LinearLayout mLinearLayout1 ;
	RadioButton[] rb1;
	RadioGroup rg1;
	TextView mtv_name,mtv_day,mtv_date,mtv_time;
	Button mBtn_logout;
	String time;
	boolean data_load_status,server_status = false;
	public static String TAG="MenuActivity";
	String am_pm;
	Date noteTS;
	public static ArrayList<String> Announcements_title,Announcements_content;
	public static ArrayList<String> title1,title2,content1,content2;
	SharedPreferences mPreferences;
	TextView tv_GetAnnouncements;
	ViewPager myPager;
	Boolean isInternetPresent = false;
//	Titanic titanic;
//	TitanicTextView tv ;
	Shimmer shimmer;
	ShimmerTextView tv;
	FrameLayout fl_menu_loading;
	//RelativeLayout menu_main;
//	private View decorView;
//	private int uiOptions;
	String formattedDate;
	ArrayList<String> PoolName,PoolId;
	ArrayList<String> ANYCEE_name = new ArrayList<String>();
	ArrayList<String> ANYCEEManager_name = new ArrayList<String>();
	ArrayList<String> ANYAQUATICSMANAGER_name = new ArrayList<String>();
	ArrayList<String> ANYCEE_id = new ArrayList<String>();
	ArrayList<String> ANYCEEManager_id = new ArrayList<String>();
	ArrayList<String> ANYAQUATICSMANAGER_id = new ArrayList<String>();
	ArrayList<String> SiteID,SiteName;
	ListPopupWindow listpopupwindow_cee_staff,listpopupwindow_cee_manager,listpopupwindow_aquatics_manager,Site_Selection;
	String mytime;
	String whattimeforassist = "-1" ;
	String desk_poolid ="-1";
	String DeskAssistID_web ;
	boolean status = false;
	boolean getsitelist = false;
	String SITEID;
	String emp_type_for_cee,emp_type_for_cee_manager,emp_type_for_aquatics,emp_userid_for_cee,emp_userid_for_cee_manager,emp_userid_for_aquatics;
	boolean pool_status = false;
	int[] imageId_INSTRUCTOR = { R.drawable.menu_currentlesson,R.drawable.menu_schedule,R.drawable.menu_mail,R.drawable.menu_ring,R.drawable.menu_cup,
			R.drawable.menu_bulb,R.drawable.clock,R.drawable.menu_reports};
	int[] imageId_DECK = { R.drawable.menu_schedule,R.drawable.dailyreport,R.drawable.menu_cup,R.drawable.menu_bulb,R.drawable.menu_mail,
			R.drawable.menu_ring,R.drawable.clock,R.drawable.question_of_the_month};
	int[] imageId_MANAGER = { R.drawable.menu_currentlesson,R.drawable.menu_schedule,R.drawable.menu_cup,R.drawable.menu_bulb,R.drawable.share,
			R.drawable.evaluation,R.drawable.menu_mail,R.drawable.menu_ring,R.drawable.clock,R.drawable.menu_reports,R.drawable.shadow,
			R.drawable.communication};
	GridView grid_home_option;
	String[] MenuName;
	HomeGridMenuOptionAdapter adapter;
	Thread t;
	
	ImageView chat;
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		//		startService(new Intent(MenuActivity.this, DeckNotificationService.class));
		mPreferences = PreferenceManager
				.getDefaultSharedPreferences(MenuActivity.this);
		isInternetPresent = Utility
				.isNetworkConnected(MenuActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		check_dataLost();
		
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
//		titanic = new Titanic();
//		titanic.start(tv);

		shimmer=new Shimmer();
		shimmer.start(tv);
		//		new DataLoad().execute();
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
								java.util.Date date = new Date();
								SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm");
								//						        System.out.println("New Date--->" + format1.format(date));
								mytime = format1.format(date);
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

		mtv_date.setText(m + "/" + d);
		String levelsas = WW_StaticClass.UserLevel;
		if(WW_StaticClass.UserLevel.toString().equalsIgnoreCase("1")||WW_StaticClass.UserLevel.toString().equalsIgnoreCase("13")){

			grid_home_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
					if(position == 0){
						Intent i = new Intent(getApplicationContext(), InstructorSelectionForMVCSActivity.class);
						i.putExtra("CODE", 1);
						startActivity(i);
						//						Intent currentIntent=new Intent(MenuActivity.this,ViewCurrentLessonActivity.class);
						//						currentIntent.putExtra("DELETE", "NO");
						//						currentIntent.putExtra("HOUR", "");
						//						currentIntent.putExtra("MIN", "");
						//						currentIntent.putExtra("DATE", "");
						//						startActivity(currentIntent);
					}
					else if(position == 1){
						/*Intent todaysIntent=new Intent(MenuActivity.this, ViewYourScheduleActivity.class);
        			todaysIntent.putExtra("DELETE", "NO");
        			startActivity(todaysIntent);*/
						Intent i = new Intent(getApplicationContext(), InstructorSelectionForMVCSActivity.class);
						i.putExtra("CODE", 2);
						startActivity(i);
					}
					else if(position==2){
						Intent msgcntrIntent = new Intent(MenuActivity.this, AwardTurboActivity.class);
						startActivity(msgcntrIntent);
					}
					else if(position==3){
						Intent ideaIntent = new Intent(MenuActivity.this, TurboIdeaActivity.class);
						startActivity(ideaIntent);
						t.interrupt();
						finish();
					}
					else if(position==4){
						Toast.makeText(getApplicationContext(), MenuName[position], Toast.LENGTH_LONG).show();
					}
					else if(position==5){
						Toast.makeText(getApplicationContext(), MenuName[position], Toast.LENGTH_LONG).show();
					}
					else if(position==6){
						Intent messageIntent = new Intent(MenuActivity.this, MessageCenterActivity.class);
						startActivity(messageIntent);
						t.interrupt();
						finish();
					}

					else if(position==7){
						Intent i = new Intent(MenuActivity.this, RequestDeckMenuActivity.class);
						startActivity(i);
						//             		Intent repIntent =  new Intent(MenuActivity.this,ReportActivity.class);
						//             		startActivity(repIntent);
					}
					else if(position==8){
//						Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
//						i.putExtra("FROM", "");
//						i.putExtra("url", "http://reports.waterworksswim.com/reports/office/peer.php?type=O&uid="+WW_StaticClass.UserName);
//						//					i.putExtra("url", "http://192.168.1.201/survey_office/peer.php?type=A&uid="+WW_StaticClass.UserName);
////						http://forms.waterworksswim.com/survey_office/peer.php?type=A&uid=
//						startActivity(i);
						Intent i=new Intent(MenuActivity.this,ClockinoutActivity.class);
						startActivity(i);

					}
					else if(position==9){
						Intent repIntent =  new Intent(MenuActivity.this,ReportActivity.class);
						startActivity(repIntent);
					}
					else if(position==10){
						Toast.makeText(getApplicationContext(), MenuName[position], Toast.LENGTH_LONG).show();
					}
					else if(position==11){
						Intent i = new Intent(getApplicationContext(), GetAllInstructorActivity.class);
						i.putExtra("FROM", "MENU");
						startActivity(i);
					}
				}
			});
		}
		else if(WW_StaticClass.UserLevel.toString().equalsIgnoreCase("3")||WW_StaticClass.UserLevel.toString().equalsIgnoreCase("11")){
			grid_home_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
					if(position == 0){
						Intent currentIntent=new Intent(MenuActivity.this,ViewCurrentScheduleInstructorActivity.class);
						//	        			currentIntent.putExtra("DELETE", "NO");
						//	        			currentIntent.putExtra("HOUR", "");
						//	        			currentIntent.putExtra("MIN", "");
						//	        			currentIntent.putExtra("DATE", "");
						startActivity(currentIntent);
					}
					else if(position == 1){
						Intent todaysIntent=new Intent(MenuActivity.this, ViewTodaysScheduleInstructorActivty.class);
						todaysIntent.putExtra("DELETE", "NO");
						startActivity(todaysIntent);
					}
					else if(position==2){
						Intent messageIntent = new Intent(MenuActivity.this, MessageCenterActivity.class);
						startActivity(messageIntent);
						t.interrupt();
						finish();
					}
					else if(position==3){
						Intent i = new Intent(MenuActivity.this, RequestDeckMenuActivity.class);
						startActivity(i);
					}
					else if(position==4){
						Intent msgcntrIntent = new Intent(MenuActivity.this, AwardTurboActivity.class);
						startActivity(msgcntrIntent);
					}
					else if(position==5){
						Intent ideaIntent = new Intent(MenuActivity.this, TurboIdeaActivity.class);
						startActivity(ideaIntent);
						t.interrupt();
						finish();
					}
					else if(position==6){
//						Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
//						i.putExtra("FROM", "");
//						i.putExtra("url", "http://reports.waterworksswim.com/reports/office/peer.php?type=O&uid="+WW_StaticClass.UserName);
//						//						i.putExtra("url", "http://192.168.1.201/survey_office/peer.php?type=A&uid="+WW_StaticClass.UserName);
//						startActivity(i);
						Intent i=new Intent(MenuActivity.this,ClockinoutActivity.class);
						startActivity(i);
					}

					else if(position==7){
						Intent repIntent =  new Intent(MenuActivity.this,ReportActivity.class);
						startActivity(repIntent);
					}
				}
			});
		}
		else if(WW_StaticClass.UserLevel.toString().equalsIgnoreCase("7")){
			grid_home_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
					if(position == 0){
						Intent currentIntent=new Intent(MenuActivity.this,ViewScheduleDeckSupervisorActivity.class);
						currentIntent.putExtra("FROM", "DECK");
						//	        			currentIntent.putExtra("HOUR", "");
						//	        			currentIntent.putExtra("MIN", "");
						//	        			currentIntent.putExtra("DATE", "");
						startActivity(currentIntent);
					}
					else if(position == 1){
						//	             		Intent todaysIntent=new Intent(MenuActivity.this, ViewYourScheduleActivity.class);
						//	        			todaysIntent.putExtra("DELETE", "NO");
						//	        			startActivity(todaysIntent);
						Intent i = new Intent(getApplicationContext(), DailyReportActivity.class);
						startActivity(i);
					}
					else if(position==2){
						Intent awrdtrbIntent = new Intent(MenuActivity.this, AwardTurboActivity.class);
						startActivity(awrdtrbIntent);
					}
					else if(position==3){

						Intent ideaIntent = new Intent(MenuActivity.this, TurboIdeaActivity.class);
						startActivity(ideaIntent);
						t.interrupt();
						finish();
					}
					else if(position==4){
						Intent messageIntent = new Intent(MenuActivity.this, MessageCenterActivity.class);
						startActivity(messageIntent);
						t.interrupt();
						finish();
					}
					else if(position==5){
						Intent i = new Intent(MenuActivity.this, RequestDeckMenuActivity.class);
						startActivity(i);
					}
					else if(position==6){
//						Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
//						i.putExtra("FROM", "");
//						i.putExtra("url", "http://reports.waterworksswim.com/reports/office/peer.php?type=O&uid="+WW_StaticClass.UserName);
//						//						i.putExtra("url", "http://192.168.1.201/survey_office/peer.php?type=A&uid="+WW_StaticClass.UserName);
//						startActivity(i);
						Intent i=new Intent(MenuActivity.this,ClockinoutActivity.class);
						startActivity(i);
					}

					else if(position==7){
						//	             		Intent repIntent =  new Intent(MenuActivity.this,ReportActivity.class);
						//	             		startActivity(repIntent);
						Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
						i.putExtra("FROM", "");
						i.putExtra("url", "http://forms.waterworksswim.com/survey_office/Qmonth.php");
						startActivity(i);
					}
				}
			});
		}

		if(isInternetPresent){
			new DataLoad().execute();
						new GetSiteList().execute();
		}
		else{
			onDetectNetworkState().show();
		}
		
	}
	
	public void check_dataLost(){
		if(!WW_StaticClass.UserName.equals(null) || !WW_StaticClass.UserName.contains("UserName")){
			//Ignore this case
		}else{
			SharedPreferences imp_details = getPreferences(Context.MODE_PRIVATE);
			WW_StaticClass.UserName = imp_details.getString("UserName", "");
			WW_StaticClass.UserToken = imp_details.getString("UserToken", "");
			WW_StaticClass.UserLevel = imp_details.getString("UserLevel", "");
			WW_StaticClass.InstructorID = imp_details.getString("UserId", "");
			WW_StaticClass.DeckSuperID = imp_details.getString("UserId", "");
			String sites = imp_details.getString("sitelist", "");
			
			String temp[] = sites.toString().split("\\,");
			String temp2[];
			for (int i = 0; i < temp.length; i++) {
				temp2 = temp[i].toString().split("\\:");
				WW_StaticClass.siteid.add(temp2[0].toString());
				WW_StaticClass.sitename.add(temp2[1].toString());
			}
			
			Toast.makeText(getApplicationContext(), "Restored from cache data.", Toast.LENGTH_SHORT).show();
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
				MenuActivity.this.finish();
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
		//		menu_main = (RelativeLayout)findViewById(R.id.menu_main);
		//		menu_main.setVisibility(View.VISIBLE);
		
		chat = (ImageView)findViewById(R.id.chat);
		
		tv = (ShimmerTextView) findViewById(R.id.my_text_view);
		tv.setTypeface(Typefaces.get(MenuActivity.this, "Satisfy-Regular.ttf"));
		fl_menu_loading = (FrameLayout)findViewById(R.id.menu_loading);
		fl_menu_loading.setVisibility(View.GONE);
		mtv_name = (TextView)findViewById(R.id.tv_app_inst_name);
		mtv_day = (TextView)findViewById(R.id.tv_app_day);
		mtv_date = (TextView)findViewById(R.id.tv_app_date);
		mtv_time = (TextView)findViewById(R.id.tv_app_time);
		mBtn_logout=(Button)findViewById(R.id.btn_app_logoff);
		if(WW_StaticClass.UserLevel.toString().equalsIgnoreCase("1")||WW_StaticClass.UserLevel.toString().equalsIgnoreCase("13")){
			MenuName = getResources().getStringArray(R.array.menuoption2);
			adapter = new HomeGridMenuOptionAdapter(MenuActivity.this, MenuName, imageId_MANAGER);
			grid_home_option = (GridView)findViewById(R.id.grid_home_grid);
			grid_home_option.setNumColumns(6);
			grid_home_option.setAdapter(adapter);
		}
		else if(WW_StaticClass.UserLevel.toString().equalsIgnoreCase("3")||WW_StaticClass.UserLevel.toString().equalsIgnoreCase("11")){
			MenuName = getResources().getStringArray(R.array.menuoption);
			adapter = new HomeGridMenuOptionAdapter(getApplicationContext(), MenuName, imageId_INSTRUCTOR);
			grid_home_option = (GridView)findViewById(R.id.grid_home_grid);
			grid_home_option.setAdapter(adapter);
		}
		else if(WW_StaticClass.UserLevel.toString().equalsIgnoreCase("7")){
			MenuName = getResources().getStringArray(R.array.menuoption1);
			adapter = new HomeGridMenuOptionAdapter(getApplicationContext(), MenuName, imageId_DECK);
			grid_home_option = (GridView)findViewById(R.id.grid_home_grid);
			grid_home_option.setAdapter(adapter);
		}

		grid_home_option = (GridView)findViewById(R.id.grid_home_grid);
		grid_home_option.setAdapter(adapter);
		tv_GetAnnouncements = (TextView)findViewById(R.id.tv_GetAnnouncements);
		myPager = (ViewPager) findViewById(R.id.ll_menu_rotate_data);
	}


	ProgressDialog pDialog;

	public class DataLoad extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//			pDialog = new ProgressDialog(MenuActivity.this);
			//			pDialog.setMessage(Html.fromHtml("Loading wait..."));
			//			pDialog.setIndeterminate(true);
			//			pDialog.setCancelable(true);
			//			pDialog.show();
			fl_menu_loading.setVisibility(View.VISIBLE);
			fl_menu_loading.bringToFront();
			//			menu_main.setVisibility(View.GONE);
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE, SOAP_CONSTANTS.METHOD_NAME_GetAnnouncements);
			// Adding Username and Password for Login Invok
			request.addProperty("token",  WW_StaticClass.UserToken);
			// Log.i(Tag, "Login name"+mEd_User.getText().toString());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			Announcements_title= new ArrayList<String>();
			Announcements_content = new ArrayList<String>();
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_ACTION_Announcements, envelope);

				SoapObject response = (SoapObject) envelope.getResponse();
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				Log.i(TAG, "mSoapObject1="+mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				Log.i(TAG, "mSoapObject2="+mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("code", code);
				if(code.equals("000")){
					data_load_status = true;
					Object mSoapObject3 =  mSoapObject1
							.getProperty(1);

					String resp = mSoapObject3.toString();
					JSONObject jobj = new JSONObject(resp);
					JSONArray mArray = jobj.getJSONArray("Announcements");
					for (int i = 0; i < mArray.length(); i++) {
						JSONObject mJsonObjectFee = mArray.getJSONObject(i);
						Announcements_title.add(mJsonObjectFee.getString("title"));
						Announcements_content.add(mJsonObjectFee.getString("content"));
					}
				}
				else{
					data_load_status = false;
				}
			}catch(SocketTimeoutException e){
				e.printStackTrace();
				connectionout = true;
			}
			catch (SocketException e) {
				// TODO: handle exception
				e.printStackTrace();
				connectionout = true;
			}
			catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
				server_status = true;
			}
			catch (Exception e) {
				server_status = true;
				e.printStackTrace();

			}


			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			//			pDialog.cancel();
			fl_menu_loading.setVisibility(View.GONE);
			//			menu_main.setVisibility(View.VISIBLE);
			if(server_status){
				//				SingleOptionAlertWithoutTitle.ShowAlertDialog(
				//						MenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else if(connectionout){
				connectionout = false;
				ConnectionTimeOut().show();
			}
			else{
				if (data_load_status) {
					title1 = new ArrayList<String>();
					title2 = new ArrayList<String>();
					content1 = new ArrayList<String>();
					content2 = new ArrayList<String>();
					for (int i = 0; i < Announcements_title.size(); i++) {
						if (i % 2 == 1) {
							title1.add(Announcements_title.get(i));
							Log.i("title1", "Title 1=" + Announcements_title.get(i));
						} else {
							title2.add(Announcements_title.get(i));
							Log.i("title2", "Title 2=" + Announcements_title.get(i));
						}
					}
					for (int i = 0; i < Announcements_content.size(); i++) {
						if (i % 2 == 1) {
							content1.add(Announcements_content.get(i));
							Log.i("content1", "Content 1=" + Announcements_content.get(i));
						} else {
							content2.add(Announcements_content.get(i));
							Log.i("content2", "Content 2=" + Announcements_content.get(i));
						}
					}
					// Create and set adapter
					CustomMenuDataPageAdapter adapter = new CustomMenuDataPageAdapter(MenuActivity.this);
					myPager.setVisibility(View.VISIBLE);
					myPager.setAdapter(adapter);
					myPager.setCurrentItem(0);
					data_load_status = false;
				} else {
					//				SingleOptionAlertWithoutTitle.ShowAlertDialog(
					//						MenuActivity.this,
					//						"No Announcements for Today", "OK");
					//				ViewPager myPager = (ViewPager) findViewById(R.id.ll_menu_rotate_data);
					//				myPager.setVisibility(View.GONE);
					//				tv_GetAnnouncements.setVisibility(View.VISIBLE);
					myPager.setVisibility(View.GONE);
					//				Log.i("Done", "Done");
				}
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
				new DataLoad().execute();
			}
		});
		return builder1.create();
	}
	@SuppressLint("InlinedApi")

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*	case R.id.img_current_lesson:
			Intent currentIntent=new Intent(MenuActivity.this,ViewCurrentLessonActivity.class);
			currentIntent.putExtra("DELETE", "NO");
			currentIntent.putExtra("HOUR", "");
			currentIntent.putExtra("MIN", "");
			currentIntent.putExtra("DATE", "");
			startActivity(currentIntent);
			break;
		case R.id.tv_current_lesson:
			Intent currentIntent2=new Intent(MenuActivity.this,ViewCurrentLessonActivity.class);
			currentIntent2.putExtra("DELETE", "NO");
			currentIntent2.putExtra("HOUR", "");
			currentIntent2.putExtra("MIN", "");
			currentIntent2.putExtra("DATE", "");
			startActivity(currentIntent2);
			break;
		case R.id.img_schedule:
			Intent todaysIntent=new Intent(MenuActivity.this, ViewYourScheduleActivity.class);
			todaysIntent.putExtra("DELETE", "NO");
			startActivity(todaysIntent);
//			Toast.makeText(MenuActivity.this, "Work in...", 1).show();
			break;
		case R.id.tv_schedule:
			Intent todaysIntent2=new Intent(MenuActivity.this, ViewYourScheduleActivity.class);
			todaysIntent2.putExtra("DELETE", "NO");
			startActivity(todaysIntent2);
//			Toast.makeText(MenuActivity.this, "Work in...", 1).show();
			break;
		case R.id.img_deck:
//			new IamInPool().execute();
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			RequestDeck();
			Intent i1 = new Intent(MenuActivity.this, RequestDeckMenuActivity.class);
			startActivity(i1);
			break;
		case R.id.tv_deck:
//				new IamInPool().execute();
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			RequestDeck();
			Intent i = new Intent(MenuActivity.this, RequestDeckMenuActivity.class);
			startActivity(i);
			break;
		case R.id.img_cup:
				Intent msgcntrIntent = new Intent(MenuActivity.this, AwardTurboActivity.class);
				startActivity(msgcntrIntent);
			break;
		case R.id.tv_cup:
			Intent msgcntrIntent1 = new Intent(MenuActivity.this, AwardTurboActivity.class);
			startActivity(msgcntrIntent1);
			break;
		case R.id.tv_mail:
			Intent messageIntent = new Intent(MenuActivity.this, MessageCenterActivity.class);
			startActivity(messageIntent);
			finish();
			break;
		case R.id.img_mail:
			Intent messageIntent1 = new Intent(MenuActivity.this, MessageCenterActivity.class);
			startActivity(messageIntent1);
			finish();
			break;*/
		case R.id.btn_app_logoff:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					MenuActivity.this);

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
					stopService(new Intent(MenuActivity.this, DeckNotificationService.class));
					Intent loginIntent= new Intent(MenuActivity.this, LoginActivity.class);
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
			/*case R.id.tv_bulb:
			Intent ideaIntent = new Intent(MenuActivity.this, TurboIdeaActivity.class);
			startActivity(ideaIntent);
			finish();
			break;
		case R.id.img_bulb:
			Intent ideaIntent1 = new Intent(MenuActivity.this, TurboIdeaActivity.class);
			startActivity(ideaIntent1);
			finish();
			break;*/
		default:
			break;
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
		//		decorView.setSystemUiVisibility(uiOptions);
		
		
		chat.setOnClickListener(new OnClickListener() {
			

			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(MenuActivity.this, Chat_Friends_list.class);
				i.putExtra("uid", WW_StaticClass.DeckSuperID);
				startActivity(i);
				
//				Intent i = new Intent(MenuActivity.this,Chat_Room.class);
////				i.putExtra("c_chan", chat_channel);
//				i.putExtra("uid", ANYCEE_id);
//				i.putExtra("uname", ANYCEE_name);
//				startActivity(i);
				
			}
		});
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//		super.onBackPressed();
		//		finish();


		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MenuActivity.this);

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
				stopService(new Intent(MenuActivity.this, DeckNotificationService.class));
				Intent loginIntent= new Intent(MenuActivity.this, LoginActivity.class);
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

	/*	@SuppressLint("InlinedApi")
	private void RequestDeck(){
		final Dialog dialog = new Dialog(MenuActivity.this);
		  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//		  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		  dialog.setContentView(R.layout.requestdeck_menu_land);
		  dialog.setCanceledOnTouchOutside(false);
		  dialog.setCancelable(true);

//			final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN);
//			params.copyFrom(dialog.getWindow().getAttributes());
//			params.width = WindowManager.LayoutParams.MATCH_PARENT;
//			params.height = WindowManager.LayoutParams.MATCH_PARENT;
////			params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//			dialog.getWindow().setAttributes(params);

			final Button btn_site_selection = (Button)dialog.findViewById(R.id.btn_rc_site_list);
			final RadioButton rb_any_cee = (RadioButton)dialog.findViewById(R.id.rb_menu_cee);
			final RadioButton rb_any_cee_manager = (RadioButton)dialog.findViewById(R.id.rb_menu_cee_manager);
			final RadioButton rb_any_aquatics_manager = (RadioButton)dialog.findViewById(R.id.rb_menu_rc_aquatics_manager);
			final RadioButton rc_now=(RadioButton)dialog.findViewById(R.id.rc_menu_now);
			final RadioButton rc_min=(RadioButton)dialog.findViewById(R.id.rc_menu_min);
			final Button btn_aquatics_manager = (Button)dialog.findViewById(R.id.et_menu_rc_aquatics_manager);
			final Button btn_cee_staff = (Button)dialog.findViewById(R.id.et_menu_rc_cee_staff);
			final Button btn_cee_manager = (Button)dialog.findViewById(R.id.et_menu_rc_cee_manager);
			mLinearLayout1 = (LinearLayout)dialog.findViewById(R.id.ll_menu_pool_list);
			rg1 = new RadioGroup(getApplicationContext());

			Button rc_send_request = (Button)dialog.findViewById(R.id.btn_menu_rc_send_request);
			rc_now.setOnClickListener(new OnClickListener() {

				@Override
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

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					rc_now.setChecked(false);
					rc_min.setChecked(true);
					whattimeforassist = "2";
					rc_min.setError(null);
					rc_now.setError(null);
				}
			});


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
			Site_Selection =  new ListPopupWindow(getApplicationContext());
			btn_site_selection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Site_Selection.show();
				}
			});
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
						mLinearLayout1 = (LinearLayout)dialog.findViewById(R.id.ll_menu_pool_list);
						mLinearLayout1.removeAllViews();
						rg1 = new RadioGroup(getApplicationContext());
						rg1.removeAllViews();
						rb1 = new RadioButton[0];
						SITEID = SiteID.get(pos);
						new IamInPool().execute();
						ANYCEE_id.clear();
						ANYCEE_name.clear();
						ANYCEEManager_id.clear();
						ANYCEEManager_name.clear();
						ANYAQUATICSMANAGER_id.clear();
						ANYAQUATICSMANAGER_name.clear();
//						listpopupwindow_aquatics_manager = new ListPopupWindow(getApplicationContext());
						listpopupwindow_aquatics_manager.setAdapter(new ArrayAdapter<String>
						(getApplicationContext(),  R.layout.edittextpopup,ANYAQUATICSMANAGER_name ));
//						listpopupwindow_cee_manager = new ListPopupWindow(getApplicationContext());
						listpopupwindow_cee_manager.setAdapter(new ArrayAdapter<String>
						(getApplicationContext(),  R.layout.edittextpopup,ANYCEEManager_name));
//						listpopupwindow_cee_staff = new ListPopupWindow(getApplicationContext());
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
				        	   rb1 = new RadioButton[PoolName.size()];

							   rg1.setOrientation(RadioGroup.HORIZONTAL);
							   for (int i = 0; i < PoolName.size(); i++) {
							    rb1[i] = new RadioButton(getApplicationContext());
							    rg1.addView(rb1[i]);
							    rb1[i].setText(PoolName.get(i));
							    rb1[i].setId(i);
							    rb1[i].setButtonDrawable(android.R.drawable.btn_radio);
							    rb1[i].setTextColor(getResources().getColor(R.color.texts1));
							    rb1[i].setTextSize(24);

							   }
							    mLinearLayout1.addView(rg1);
							    rg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
												rb1[j].setError(null);
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
					}
				});

			listpopupwindow_cee_staff = new ListPopupWindow(getApplicationContext());
			btn_cee_staff.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					listpopupwindow_cee_staff.show();
				}
			});
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

			listpopupwindow_cee_manager = new ListPopupWindow(getApplicationContext());
			btn_cee_manager.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					rb_any_cee.setEnabled(false);
//					rb_any_cee_manager.setEnabled(false);
//					btn_cee_staff.setEnabled(false);
					listpopupwindow_cee_manager.show();
				}
			});
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


			listpopupwindow_aquatics_manager = new ListPopupWindow(getApplicationContext());
			btn_aquatics_manager.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					listpopupwindow_aquatics_manager.show();
				}
			});
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




	        /////////////// Send request
			rc_send_request.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(desk_poolid.equalsIgnoreCase("-1")&&whattimeforassist.equalsIgnoreCase("-1")){
						for(int i=0;i<PoolName.size();i++){
							rb1[i].setError("Please select anyone option");
						}
						rc_min.setError("Please select anyone option");
						rc_now.setError("Please select anyone option");
					}
					else if(desk_poolid.equalsIgnoreCase("-1")){
						for(int i=0;i<PoolName.size();i++){
							rb1[i].setError("Please select anyone option");
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
						rg1.clearCheck();

						desk_poolid = "-1";
						whattimeforassist = "-1";
//						dialog.cancel();
						dialog.dismiss();
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					}
				}
			});

			dialog.show();

//			dialog.setOnDismissListener(new OnDismissListener() {
//				
//				@Override
//				public void onDismiss(DialogInterface dialog) {
//					// TODO Auto-generated method stub
//					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);	
//				}
//			});
	}

	 */	

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
					// Name.add(mJsonObjectFee.getString("Name"));
					// Term1.add(mJsonObjectFee.getString("Term1"));
					// Term2.add(mJsonObjectFee.getString("Term2"));

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
						MenuActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else{
				if(pool_status)
				{
					pool_status = false;
				}
				else{
					SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this,"WaterWorks", "No pool found.", "Ok");
				}
			}
		}
	}


	public class DeskdataforCEE extends AsyncTask<Void, Void, Void> {
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
				SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
				server_status = false;
			}
			if(status==false){
				Toast.makeText(getApplicationContext(), "No users found", 1).show();
				//				SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
			}
			else{
				status =false;
			}
		}

	}

	ProgressDialog pDialog4;
	public class DeskdataforCEEManager extends AsyncTask<Void, Void, Void> {
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
				SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
				server_status = false;
			}
			if(status==false){
				Toast.makeText(getApplicationContext(), "No users found", 1).show();
				//			SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
			}
			else{
				status = false;
			}
		}

	}


	ProgressDialog pDialog5;
	public class DeskdataforAquaticsManager extends AsyncTask<Void, Void, Void> {
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
				SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
				server_status = false;
			}
			if(status==false){
				Toast.makeText(getApplicationContext(), "No users found", 1).show();
				//			SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
			}
			else{
				status =false;
			}
		}

	}



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
				SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
				server_status = false;
			}
			else{
				if(status==true){

					if(!DeskAssistID_web.isEmpty()){
						if(!emp_type_for_cee.isEmpty()){
							new InsertDeskAssistUser_forCEE().execute();
						}
						if(!emp_type_for_cee_manager.isEmpty()){
							new InsertDeskAssistUser_forCEEManager().execute();
						}
						if(!emp_type_for_aquatics.isEmpty()){
							new InsertDeskAssistUser_forAquatics().execute();
						}

					}
					else{
						SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "No data found, Please try again..", "Ok");
					}
					status=false;
				}
				else{
					SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
				}
			}


		}

		ProgressDialog progressDialog2;
		public class InsertDeskAssistUser_forCEE extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				//			progressDialog2 = new ProgressDialog(ViewCurrentLessonActivity.this);
				//			progressDialog2.setMessage(Html.fromHtml("Loading wait..."));
				//			progressDialog2.setIndeterminate(true);
				//			progressDialog2.setCancelable(false);
				//			progressDialog2.show();
				fl_menu_loading.setVisibility(View.VISIBLE);
				fl_menu_loading.bringToFront();
			}
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
						SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssistUser );
				request.addProperty("token", WW_StaticClass.UserToken);
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
					status = true;
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
				//			progressDialog2.dismiss();
				fl_menu_loading.setVisibility(View.GONE);
				if(server_status==true){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
					server_status = false;
				}
				if(status == true){
					if(emp_type_for_cee.equalsIgnoreCase("1")){
						SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "Request Send", "Request send to cee staff", "Ok");
					}
					status = false;
				}
			}
		}


		ProgressDialog progressDialog3;
		public class InsertDeskAssistUser_forAquatics extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				//			progressDialog3 = new ProgressDialog(ViewCurrentLessonActivity.this);
				//			progressDialog3.setMessage(Html.fromHtml("Loading wait..."));
				//			progressDialog3.setIndeterminate(true);
				//			progressDialog3.setCancelable(false);
				//			progressDialog3.show();
				fl_menu_loading.setVisibility(View.VISIBLE);
				fl_menu_loading.bringToFront();
			}
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
						SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssistUser );
				request.addProperty("token", WW_StaticClass.UserToken);
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
					status = true;
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
				//			progressDialog3.dismiss();
				fl_menu_loading.setVisibility(View.GONE);
				if(server_status==true){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
					server_status = false;
				}
				if(status == true){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "Request Send", "Request send to aquatics manager", "Ok");
					status = false;
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
			//			progressDialog4 = new ProgressDialog(ViewCurrentLessonActivity.this);
			//			progressDialog4.setMessage(Html.fromHtml("Loading wait..."));
			//			progressDialog4.setIndeterminate(true);
			//			progressDialog4.setCancelable(false);
			//			progressDialog4.show();
			fl_menu_loading.setVisibility(View.VISIBLE);
			fl_menu_loading.bringToFront();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssistUser );
			request.addProperty("token", WW_StaticClass.UserToken);
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
				status = true;
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
			//			progressDialog4.dismiss();
			fl_menu_loading.setVisibility(View.GONE);
			if(server_status==true){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
				server_status = false;
			}
			if(status == true){
				if(emp_type_for_cee_manager.equalsIgnoreCase("2")){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(MenuActivity.this, "Request Send", "Request send to cee manager", "Ok");
				}
				status = false;
			}
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
				server_status = true;
			} catch (Exception e) {
				server_status = true;
				e.printStackTrace();
			}
			
//			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
//					SOAP_CONSTANTS.METHOD_NAME_GetSiteList_new);
//			request.addProperty("token", WW_StaticClass.UserToken);
//			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//					SoapEnvelope.VER11); // Make an Envelop for sending as whole
//			envelope.dotNet = true;
//			envelope.setOutputSoapObject(request);
//			Log.i("Request",  "Request = " + request);
//			HttpTransportSE androidHttpTransport = new HttpTransportSE(
//					SOAP_CONSTANTS.SOAP_ADDRESS);
//			try {
//				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetSiteList_new,
//						envelope); // Calling Web service
//				SoapObject response = (SoapObject)envelope.getResponse();
//				//				SoapPrimitive response =  (SoapPrimitive) envelope.getResponse();
//				Log.i(TAG,"" + response.toString());
//				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
//				Log.i(TAG, "mSoapObject1="+mSoapObject1);
//				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
//				Log.i(TAG, "mSoapObject2="+mSoapObject2);
//				String code = mSoapObject2.getPropertyAsString(0).toString();
//				Log.i("Code", code);
//				if(code.equalsIgnoreCase("000")){
//					getsitelist = true;
//					Object mSoapObject3 =  mSoapObject1.getProperty(1);
//					Log.i(TAG, "mSoapObject3="+mSoapObject3);
//					String resp = mSoapObject3.toString();
//					if(resp.contains("anyType{Sites=anyType{")){
//						resp = resp.replaceAll("anyType\\{Sites\\=anyType\\{","Sites\\{");
//						if(resp.contains("; }; }"))
//							resp = resp.replaceAll("\\; \\}; \\}", "\\; \\}");
//					}
//					
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
//				server_status =true;
//			}
//			catch(Exception e){
//				server_status =true;
//				e.printStackTrace();
//			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						MenuActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else{
				if(!getsitelist){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							MenuActivity.this,"WaterWorks",
							"No site found.", "OK");
				}
				else{
					getsitelist= false;
				}
			}
		}
	}
	
	
}