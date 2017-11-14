package water.works.waterworks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import water.works.waterworks.adapter.TodaysScheduleAdapter;
import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.model.TodaysScheduleItems;
import water.works.waterworks.services.DeckNotificationService;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ViewYourScheduleActivity extends Activity implements OnClickListener {
	private ArrayList<TodaysScheduleItems> ts_Items;
	private TodaysScheduleAdapter adapter_TS;
	Button btn_menu_options;
	TextView tv_day,tv_date,tv_time,tv_instructorname;
	String mytime;
	String am_pm;
	Date noteTS;
	String time;
	Titanic titanic;
	TitanicTextView tv ;
	public static FrameLayout fl_ts_loading;
	Boolean isInternetPresent = false;
	ListView lv_ts_data;
	boolean server_status = false;
	private static String TAG ="View Today's Schedule";
	TextView tv_all_inst;
	int oldmin,newmin,oldmil,newmil,count=1;
	///////////////Level List
		ArrayList<String> LevelName = new ArrayList<String>();
		ArrayList<String> LevelID = new ArrayList<String>();
	/////////////////////Today's Schedule/////////////////////////
		ArrayList<String> MainScheduleDate = new ArrayList<String>();
		ArrayList<String> ISAAlert = new ArrayList<String>();
		ArrayList<String> Cls_Lvl = new ArrayList<String>();
		ArrayList<String> Lvl_Adv_Avail = new ArrayList<String>();
		ArrayList<String> SiteID = new ArrayList<String>();
		ArrayList<String> SLevel = new ArrayList<String>();
		ArrayList<String> wu_W = new ArrayList<String>();
		ArrayList<String> ScheLevel = new ArrayList<String>();
		ArrayList<String> SwimComp = new ArrayList<String>();
		ArrayList<String> LessonName = new ArrayList<String>();
		ArrayList<String> lessontypeid = new ArrayList<String>();
		ArrayList<String> IScheduleID = new ArrayList<String>();
		ArrayList<String> SAge = new ArrayList<String>();
		ArrayList<String> ParentFirstName = new ArrayList<String>();
		ArrayList<String> ParentLastName = new ArrayList<String>();
		ArrayList<String> BirthDay = new ArrayList<String>();
		ArrayList<String> Comments= new ArrayList<String>();
		ArrayList<String> wu_r= new ArrayList<String>();
		ArrayList<String> SLastName= new ArrayList<String>();
		ArrayList<String> SFirstName= new ArrayList<String>();
		ArrayList<String> StudentID= new ArrayList<String>();
		ArrayList<String> ShowWBR= new ArrayList<String>();
		ArrayList<String> wu_b= new ArrayList<String>();
		ArrayList<String> SScheduleID= new ArrayList<String>();
		ArrayList<String> OrderDetailID= new ArrayList<String>();
		ArrayList<String> PaidClasses= new ArrayList<String>();
		ArrayList<String> SkillsCount = new ArrayList<String>();
		ArrayList<String> FormateStTimeHour = new ArrayList<String>();
		ArrayList<String> FormatStTimeMin = new ArrayList<String>();
		ArrayList<String> StTimeHour = new ArrayList<String>();
		ArrayList<String> StTimeMin = new ArrayList<String>();
		ArrayList<String> StudentGender = new ArrayList<String>();
		ArrayList<Boolean> NewUser = new ArrayList<Boolean>();
		ArrayList<String> sidemenu = new ArrayList<String>();
		ArrayList<Integer> wu_attendancetaken = new ArrayList<Integer>();
		ArrayList<Integer> att = new ArrayList<Integer>();
		ArrayList<String> wu_Comments = new ArrayList<String>();
		ArrayList<ArrayList<String>> PreReqID= new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> PreReqChecked= new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> Abbr = new ArrayList<ArrayList<String>>();
	//////////////////////////////////////////////////////////////
		
		String currentDateandTime;
		private View decorView;
		private int uiOptions;
		Button submit;
	public Thread t;
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
//
//		createUiChangeListener();
		setContentView(R.layout.activity_view_your_schedule);
		isInternetPresent = Utility
				.isNetworkConnected(ViewYourScheduleActivity.this);
		
		 if(isInternetPresent){
			 Initialization();
				SetScreenDetails();
				WW_StaticClass.duration1 = 300;
				 WW_StaticClass.duration2 = 1000;
				 titanic = new Titanic();
				 titanic.start(tv);
				 String deleteornot = getIntent().getStringExtra("DELETE");
					if(deleteornot.equalsIgnoreCase("YES")){
						SingleOptionAlertWithoutTitle.ShowAlertDialog(
								ViewYourScheduleActivity.this, "WaterWorks", "Comment Deleted Successfully", "Ok");
					}
					else{
						Log.i(TAG, "Delete or not =" + deleteornot);
					}
			 Date date = new Date();
		        System.out.println("Date-->" + date);
		        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
		        System.out.println("New Date--->" + format.format(date));
		        currentDateandTime = format.format(date);
		        new GetLevel().execute();
		        new TodaysScheduleData().execute();
		}
		 else{
			onDetectNetworkState().show();
		 }
		 oldmin = Calendar.getInstance().getTime().getMinutes();
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
		        ViewYourScheduleActivity.this.finish();
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
//	private void createUiChangeListener() {
//
//	    decorView.setOnSystemUiVisibilityChangeListener (
//	            new View.OnSystemUiVisibilityChangeListener() {
//
//	                @Override
//	                public void onSystemUiVisibilityChange(int pVisibility) {
//
//	                    if ((pVisibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
//	                        decorView.setSystemUiVisibility(uiOptions);
//	                    }
//
//	                }
//
//	            });
//
//	}
	private void Initialization() {
		// TODO Auto-generated method stub
		tv = (TitanicTextView) findViewById(R.id.my_text_view);
		tv.setTypeface(Typefaces.get(ViewYourScheduleActivity.this, "Satisfy-Regular.ttf"));
		fl_ts_loading = (FrameLayout)findViewById(R.id.view_today_schedule_loading);
		fl_ts_loading.setVisibility(View.GONE);
		tv_date = (TextView)findViewById(R.id.tv_ts_date);
		tv_day = (TextView)findViewById(R.id.tv_ts_day);
		tv_time = (TextView)findViewById(R.id.tv_ts_time);
		tv_instructorname = (TextView)findViewById(R.id.tv_ts_name);
		lv_ts_data = (ListView)findViewById(R.id.lv_ts_data);
		sidemenu.add("Logout");
		btn_menu_options = (Button)findViewById(R.id.btn_menu_options);
		FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.currectlessonlist_footer,null);
		submit = (Button) footerLayout.findViewById(R.id.btn_footer_send_attendance);
		lv_ts_data.addFooterView(footerLayout);
		lv_ts_data.setSmoothScrollbarEnabled(true);
		ts_Items = new ArrayList<TodaysScheduleItems>();
		tv_all_inst = (TextView)findViewById(R.id.btn_ts_all_inst);
		tv_all_inst.setOnClickListener(new OnClickListener() {
			

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), GetAllInstructorActivity.class);
				i.putExtra("FROM", "VIEW");
				startActivity(i);
				t.interrupt();
				finish();
				WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
				WW_StaticClass.UserName = WW_StaticClass.InstructorName;
			}
		});
	}
	private void SetScreenDetails() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		int Day_Name = c.get(Calendar.DAY_OF_WEEK);
		int Date =  c.get(Calendar.DATE);
		int Month = c.get(Calendar.MONTH);
		String day_name = null;
		if(Day_Name == 1){
			day_name = "SUNDAY";
		}
		else if(Day_Name == 2){
			day_name = "MONDAY";
		}
		else if(Day_Name == 3){
			day_name = "TUESDAY";
		}
		else if(Day_Name == 4){
			day_name = "WEDNESDAY";
		}
		else if(Day_Name == 5){
			day_name = "THURSDAY";
		}
		else if(Day_Name == 6){
			day_name = "FRIDAY";
		}
		else if(Day_Name == 7){
			day_name = "SATURDAY";
		}
		Log.i("Time", "Time = " + hour + ":" + min + " "  + " " + day_name + " " + Date + "/" + Month);
		
	
	    tv_instructorname.setText(WW_StaticClass.UserName);
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
	protected void AfterMinRefresh(){
		recreate();
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
		isInternetPresent = Utility
				.isNetworkConnected(ViewYourScheduleActivity.this);
		 t = new Thread() {

		        @Override
		        public void run() {
		            try {
		                while (!isInterrupted()) {
		                    Thread.sleep(1000);
		                    runOnUiThread(new Runnable() {

		                        public void run() {
		                        	newmin = Calendar.getInstance().getTime().getMinutes();
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

								@SuppressLint("SimpleDateFormat")
								private void updateTextView() {
									// TODO Auto-generated method stub
									noteTS = Calendar.getInstance().getTime();
									Date date = new Date();
									SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm");
//							        System.out.println("New Date--->" + format1.format(date));
									mytime = format1.format(date);
								    time = "hh:mm"; // 12:00
								    oldmil = 0;
								    /*if(newmin != 0 || newmin != 00){
									    if(newmin > oldmin){
									    	Log.i(TAG, "new time is greater");
										    	AfterMinRefresh();
									    }
									   }
									   else{
										   newmil = Calendar.getInstance().getTime().getSeconds();
										   if(count==1&&newmil==oldmil){
//											   count=2;
											   AfterMinRefresh();
										   }
									   }
									    oldmin = newmin;*/
								    if(newmin==0||newmin==20 ||newmin==40){
								    	newmil = Calendar.getInstance().getTime().getSeconds();
										   if(count==1&&newmil==oldmil){
											   AfterMinRefresh();
										   }
								    }
									    
								    tv_time.setText(DateFormat.format(time, noteTS)+""+am_pm);
								}
		                    });
		                }
		            } catch (InterruptedException e) {
		            	e.printStackTrace();
		            }
		            catch (Exception e) {
						// TODO: handle exception
		            	e.printStackTrace();
					}
		        }
		    };

		    t.start();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		t.interrupt();
		finish();
		WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
		WW_StaticClass.UserName = WW_StaticClass.InstructorName;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(isInternetPresent){
			switch (v.getId()) {
			case R.id.btn_ts_home:
//				Intent homeIntent=new Intent(getApplicationContext(), MenuActivity.class);
//				startActivity(homeIntent);
				t.interrupt();
				finish();
				WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
				WW_StaticClass.UserName = WW_StaticClass.InstructorName;
				break;
			case R.id.btn_menu_options:
				final ListPopupWindow lpw_logout = new ListPopupWindow(getApplicationContext());
				lpw_logout.setAdapter(new ArrayAdapter<String>(
          	            getApplicationContext(),
          	            R.layout.edittextpopup,sidemenu));
          		lpw_logout.setAnchorView(btn_menu_options);
          		lpw_logout.setHeight(LayoutParams.WRAP_CONTENT);
          		lpw_logout.setWidth(200);
          		lpw_logout.setModal(true);
          		lpw_logout.setOnItemClickListener(
                      new OnItemClickListener() {

          				public void onItemClick(AdapterView<?> parent, View view,
                                                int pos, long id) {
          					// TODO Auto-generated method stub
          					if(sidemenu.get(pos).equalsIgnoreCase("Logout")){
          						lpw_logout.dismiss();
          						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
    	            					ViewYourScheduleActivity.this);
    
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
    	            									stopService(new Intent(ViewYourScheduleActivity.this, DeckNotificationService.class));
    	            									Intent loginIntent= new Intent(ViewYourScheduleActivity.this, LoginActivity.class);
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
          				}
          			});
          		lpw_logout.show();
//				PopupMenu popup = new PopupMenu(getBaseContext(), v);
//
//	            /** Adding menu items to the popumenu */
//	            popup.getMenuInflater().inflate(R.menu.view_cl_ts_menu, popup.getMenu());
//	            /** Defining menu item click listener for the popup menu */
//	            popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//
//	                @Override
//	                public boolean onMenuItemClick(MenuItem item) {
////	                    Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//	                	if(item.getTitle().toString().equalsIgnoreCase("Log Off")){
//	                		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
//	            					ViewYourScheduleActivity.this);
//
//	            			// set title
//	            			alertDialogBuilder.setTitle("WaterWorks");
//	            			alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
//
//	            			// set dialog message
//	            			alertDialogBuilder
//	            					.setMessage("Are you sure you want to logout ?")
//	            					.setCancelable(false)
//	            					.setPositiveButton("Logout",
//	            							new DialogInterface.OnClickListener() {
//	            								public void onClick(DialogInterface dialog, int id) {
//	            									WW_StaticClass.UserId="";
//	            									finish();
//	            									
//	            									Intent loginIntent= new Intent(ViewYourScheduleActivity.this, LoginActivity.class);
//	            									loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	            									loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//	            									
//	            									startActivity(loginIntent);
//	            									
//	            								}
//	            							})
//	            					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//	            						public void onClick(DialogInterface dialog, int id) {
//	            							dialog.cancel();
//	            						}
//	            					});
//
//	            			// create alert dialog
//	            			AlertDialog alertDialog = alertDialogBuilder.create();
//
//	            			// show it
//	            			alertDialog.show();
//
//	                	}
//	                    return true;
//	                }
//	            });
//
//	            /** Showing the popup menu */
//	            popup.show();

				break;
			default:
				break;
			}
		}
		else{
			onDetectNetworkState().show();
		}
	}

	
	private class GetLevel extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetLevelList);
			request.addProperty("token", WW_StaticClass.UserToken);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
					androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_LevelList,
						envelope); // Calling Web service
					SoapObject response =  (SoapObject) envelope.getResponse();
					 Log.i("here","Result : " + response.toString());
					 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
					 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
					 String code = mSoapObject2.getPropertyAsString(0).toString();
					 Log.i("Code", code);
//					response.toString();
					 if (code.equals("000")) {
						 Object mSoapObject3 =   mSoapObject1.getProperty(1);
							Log.i(TAG, "mSoapObject3="+mSoapObject3);
							JSONObject jo = new JSONObject(mSoapObject3.toString());
							JSONArray jArray = jo.getJSONArray("Levels");
							JSONObject jsonObject;
							for(int i=0;i<jArray.length();i++){
								jsonObject = jArray.getJSONObject(i);
								LevelName.add(jsonObject.getString("LevelName"));
								LevelID.add(jsonObject.getString("LevelId"));
							}
							Log.e(TAG, "Level name = " +LevelName);
							Log.e(TAG, "Level id = " +LevelID);
					 }
					 else{
					 }
					 
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
	}

	
	
	private class TodaysScheduleData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fl_ts_loading.setVisibility(View.VISIBLE);
			fl_ts_loading.bringToFront();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
				SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
						SOAP_CONSTANTS.METHOD_NAME_ViewSchl_GetViewScheduleByDateAndInstrid);
				request.addProperty("token", WW_StaticClass.UserToken);
				request.addProperty("Rinstructorid", WW_StaticClass.InstructorID);
				request.addProperty("strRScheDate",currentDateandTime );
//				request.addProperty("strRScheDate","10/05/2014 08:00 AM" );
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11); // Make an Envelop for sending as whole
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				Log.i("Request",  "Request = " + request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						SOAP_CONSTANTS.SOAP_ADDRESS);
				try {
						androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_ViewSchl_GetViewScheduleByDateAndInstrid,
							envelope); // Calling Web service
						if(envelope.bodyIn instanceof SoapFault) {
						    String str= ((SoapFault) envelope.bodyIn).faultstring;
						    Log.i(TAG,"Error = " +  str);
						}
						else{
							
					SoapObject response = (SoapObject) envelope.bodyIn;
					Log.i(TAG,"Result : " + response.toString());
					SoapPrimitive sp1 = (SoapPrimitive) response.getProperty(0);
					String resp = sp1.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("Attendance");
					Log.i(TAG,"jArray : " + jArray.toString());
					JSONObject jsonObject;
					JSONObject jsonObject2,jsonObject3;
					JSONArray jArray2;
					JSONArray jArray3 = null;
					for(int k=0;k<jArray.length();k++){
						jsonObject = jArray.getJSONObject(k);
						Log.i(TAG,"jsonObject: " + jsonObject.toString());
						
						jArray2 = jsonObject.getJSONArray("Items");
						Log.i(TAG,"jArray2 : " + jArray2.toString());
						for(int i=0;i<jArray2.length();i++){
							jsonObject2 = jArray2.getJSONObject(i);
							FormateStTimeHour.add(jsonObject2.getString("FormateStTimeHour"));
							FormatStTimeMin.add(jsonObject2.getString("FormatStTimeMin"));
							StTimeHour.add(jsonObject2.getString("StTimeHour"));
							StTimeMin.add(jsonObject2.getString("StTimeMin"));
							ISAAlert.add(jsonObject2.getString("ISAAlert"));
							SiteID.add(jsonObject2.getString("SiteID"));
							SLevel.add(jsonObject2.getString("SLevel"));
							wu_W.add(jsonObject2.getString("wu_W"));
							ScheLevel.add(jsonObject2.getString("ScheLevel"));
							SwimComp.add(jsonObject2.getString("SwimComp"));
							LessonName.add(jsonObject2.getString("LessonName"));
							lessontypeid.add(jsonObject2.getString("lessontypeid"));
							IScheduleID.add(jsonObject2.getString("IScheduleID"));
							SAge.add(jsonObject2.getString("SAge"));
							ParentFirstName.add(jsonObject2.getString("ParentFirstName"));
							ParentLastName.add(jsonObject2.getString("ParentLastName"));
							BirthDay.add(jsonObject2.getString("BirthDay"));
							Comments.add(jsonObject2.getString("Comments"));
							wu_r.add(jsonObject2.getString("wu_r"));
							SFirstName.add(jsonObject2.getString("SFirstName"));
							SLastName.add(jsonObject2.getString("SLastName"));
							StudentID.add(jsonObject2.getString("StudentID"));
							ShowWBR.add(jsonObject2.getString("ShowWBR"));
							wu_b.add(jsonObject2.getString("wu_b"));
							SScheduleID.add(jsonObject2.getString("SScheduleID"));
							OrderDetailID.add(jsonObject2.getString("OrderDetailID"));
							PaidClasses.add(jsonObject2.getString("PaidClasses"));
							Cls_Lvl.add(jsonObject2.getString("ClsLvl"));
							Lvl_Adv_Avail.add(jsonObject2.getString("LvlAdvAvail"));
							MainScheduleDate.add(jsonObject2.getString("MainScheduleDate"));
							SkillsCount.add(jsonObject2.getString("SkillsCount"));
							StudentGender.add(jsonObject2.getString("StudentGender"));
							NewUser.add(jsonObject2.optBoolean("NewUser"));
							wu_attendancetaken.add(jsonObject2.getInt("wu_attendancetaken"));
							att.add(jsonObject2.getInt("att"));
							wu_Comments.add(jsonObject2.getString("wu_comments"));
							jArray3 = jsonObject2.getJSONArray("SkillsList");
							
							ArrayList<String> tempabbr = new ArrayList<String>();
							ArrayList<String> tempPreReqId = new ArrayList<String>();
							ArrayList<String> tempPreReqChecked = new ArrayList<String>();
							for (int b = 0; b < jArray3.length(); b++) {
								jsonObject3 = jArray3.getJSONObject(b);
								String abbr = jsonObject3.getString("Abbr");
								String prereqid = jsonObject3.getString("PreReqID");
								String prereqchecked = jsonObject3.getString("PreReqChecked");
								
									tempabbr.add(abbr);
									tempPreReqId.add(prereqid);
									tempPreReqChecked.add(prereqchecked);
						}
							Abbr.add(tempabbr);
							PreReqID.add(tempPreReqId);
							PreReqChecked.add(tempPreReqChecked);
						}
					}
				}
				}
				catch(ArrayIndexOutOfBoundsException e){
					e.printStackTrace();
					server_status = true;
				}
				catch (OutOfMemoryError e) {
					// TODO: handle exception
					server_status = true;
					e.printStackTrace();
				}
				catch (NullPointerException e) {
					// TODO: handle exception
					e.printStackTrace();
					server_status = true;
				}
				catch (Exception e) {
					// TODO: handle exception
					server_status = true;
					e.printStackTrace();
				}
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			fl_ts_loading.setVisibility(View.GONE);
			try{
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						ViewYourScheduleActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else{
				
				if(!LessonName.isEmpty()){
					Log.e(TAG, "L Name = " + LevelName);
//					lv_ts_data.setAdapter(
//						new ViewYourScheduleDataAdapter(StTimeHour,StTimeMin,FormateStTimeHour,FormatStTimeMin,SiteID, SLevel, 
//								LevelID,LevelName ,wu_W, ScheLevel, SwimComp, LessonName, 
//								lessontypeid, IScheduleID, SAge, ParentFirstName, ParentLastName, BirthDay, 
//								Comments, wu_r, SLastName, SFirstName, StudentID, ShowWBR, wu_b, 
//								SScheduleID, OrderDetailID, PaidClasses, SkillsCount,
//								Abbr,PreReqChecked,PreReqID,ISAAlert,Cls_Lvl,Lvl_Adv_Avail,MainScheduleDate,
//								StudentGender,NewUser,ViewYourScheduleActivity.this,submit,wu_attendancetaken));
					for (int i = 0; i < StudentID.size(); i++) {
						ts_Items.add(new TodaysScheduleItems(StTimeHour.get(i), StTimeMin.get(i),
								FormateStTimeHour.get(i), FormatStTimeMin.get(i), SiteID.get(i), SLevel.get(i),
								ScheLevel.get(i), LessonName.get(i), lessontypeid.get(i),
								IScheduleID.get(i), SAge.get(i), ParentFirstName.get(i), 
								ParentLastName.get(i), Comments.get(i), SLastName.get(i),
								SFirstName.get(i), StudentID.get(i), SScheduleID.get(i),
								OrderDetailID.get(i), PaidClasses.get(i), ISAAlert.get(i),
								Cls_Lvl.get(i), Lvl_Adv_Avail.get(i), MainScheduleDate.get(i),
								StudentGender.get(i), NewUser.get(i), ViewYourScheduleActivity.this,
								wu_attendancetaken.get(i), LevelName, LevelID,att.get(i),submit,wu_Comments.get(i)));
						
					}
					adapter_TS = new TodaysScheduleAdapter(ts_Items,ViewYourScheduleActivity.this);
					lv_ts_data.setAdapter(adapter_TS);
					
				}
					else{
						AlertDialog alertDialog = new AlertDialog.Builder(ViewYourScheduleActivity.this).create();
						 alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						alertDialog.setTitle("WaterWorks");
						alertDialog.setCanceledOnTouchOutside(false); 
						// set the message
						alertDialog.setMessage("No lesson.");
						alertDialog.setIcon(R.drawable.ic_launcher);
						// set button1 functionality
						alertDialog.setButton("Ok",
								new DialogInterface.OnClickListener() {


									public void onClick(DialogInterface dialog, int which) {
										// close dialog

										dialog.cancel();
										t.interrupt();
										finish();

									}
								});
						
						// show the alert dialog
						alertDialog.show();
						
					}
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			Log.i(TAG, ""+ System.currentTimeMillis());
		}
	}
}