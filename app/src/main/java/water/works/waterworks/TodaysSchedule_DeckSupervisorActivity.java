package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class TodaysSchedule_DeckSupervisorActivity extends Activity implements OnClickListener, OnRefreshListener2<ListView> {

	TextView tv_day,tv_date,tv_instructorname;
	String am_pm;
	Date noteTS;
	String time;
	Titanic titanic;
	TitanicTextView tv ;
	public static FrameLayout fl_ts_loading;
	Boolean isInternetPresent = false;
	ListView lv_ts_data;
	public PullToRefreshListView lv_instructors;
	boolean server_status = false;
	private static String TAG ="Today's Schedule Deck Supervisor";
	ArrayList<String> LevelName = new ArrayList<String>();
	ArrayList<String> LevelID = new ArrayList<String>();
	ArrayList<String> MainScheduleDate = new ArrayList<String>();
	ArrayList<String> SLevel = new ArrayList<String>();
	ArrayList<String> LessonName = new ArrayList<String>();
	ArrayList<String> lessontypeid = new ArrayList<String>();
	ArrayList<String> SAge = new ArrayList<String>();
	ArrayList<String> ParentFirstName = new ArrayList<String>();
	ArrayList<String> ParentLastName = new ArrayList<String>();
	ArrayList<String> SLastName= new ArrayList<String>();
	ArrayList<String> SFirstName= new ArrayList<String>();
	ArrayList<String> StudentID= new ArrayList<String>();
	ArrayList<String> FormateStTimeHour = new ArrayList<String>();
	ArrayList<String> FormatStTimeMin = new ArrayList<String>();
	ArrayList<String> StTimeHour = new ArrayList<String>();
	ArrayList<String> StTimeMin = new ArrayList<String>();
	ArrayList<String> StudentGender = new ArrayList<String>();
	ArrayList<String> wu_Comments = new ArrayList<String>();
	String currentDateandTime;
	public static String DAYNAME;
	int pgnum = 1;
	int old_pos = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todays_schedule__deck_supervisor);
		isInternetPresent = Utility
				.isNetworkConnected(TodaysSchedule_DeckSupervisorActivity.this);
		if(isInternetPresent){
			Initialization();
			SetScreenDetails();
			WW_StaticClass.duration1 = 300;
			WW_StaticClass.duration2 = 1000;
			titanic = new Titanic();
			titanic.start(tv);
			Date date = new Date();
	        System.out.println("Date-->" + date);
	        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	        System.out.println("New Date--->" + format.format(date));
	        currentDateandTime = format.format(date);
	        new GetLevel().execute();
	        new TodaysScheduleData().execute();
	        lv_instructors.setOnRefreshListener(this);
		}
		else{
			onDetectNetworkState().show();
		 }
	}
	private void Initialization() {
		// TODO Auto-generated method stub
		tv = (TitanicTextView) findViewById(R.id.my_text_view);
		tv.setTypeface(Typefaces.get(TodaysSchedule_DeckSupervisorActivity.this, "Satisfy-Regular.ttf"));
		fl_ts_loading = (FrameLayout)findViewById(R.id.ts_ds_loading);
		fl_ts_loading.setVisibility(View.GONE);
		tv_date = (TextView)findViewById(R.id.tv_ts_dc_date);
		tv_day = (TextView)findViewById(R.id.tv_ts_dc_day);
		tv_instructorname = (TextView)findViewById(R.id.tv_ts_dc_name);
		lv_instructors = (PullToRefreshListView)findViewById(R.id.lv_ts_ds);
		lv_ts_data = lv_instructors.getRefreshableView();
		lv_instructors.setMode(Mode.DISABLED);
	}
	private void SetScreenDetails() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
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
		 tv_instructorname.setText(WW_StaticClass.UserName);
			tv_day.setText(day_name);
			DAYNAME = day_name;
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
				.isNetworkConnected(TodaysSchedule_DeckSupervisorActivity.this);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
		WW_StaticClass.UserName = WW_StaticClass.InstructorName;
	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        TodaysSchedule_DeckSupervisorActivity.this.finish();
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

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(isInternetPresent){
			switch (v.getId()) {
			case R.id.btn_back:
				finish();
				WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
				WW_StaticClass.UserName = WW_StaticClass.InstructorName;
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
							SLevel.add(jsonObject2.getString("SLevel"));
							LessonName.add(jsonObject2.getString("LessonName"));
							lessontypeid.add(jsonObject2.getString("lessontypeid"));
							SAge.add(jsonObject2.getString("SAge"));
							ParentFirstName.add(jsonObject2.getString("ParentFirstName"));
							ParentLastName.add(jsonObject2.getString("ParentLastName"));
							SFirstName.add(jsonObject2.getString("SFirstName"));
							SLastName.add(jsonObject2.getString("SLastName"));
							StudentID.add(jsonObject2.getString("StudentID"));
							MainScheduleDate.add(jsonObject2.getString("MainScheduleDate"));
							StudentGender.add(jsonObject2.getString("StudentGender"));
							wu_Comments.add(jsonObject2.getString("wu_scomments"));
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
			lv_instructors.onRefreshComplete();
			fl_ts_loading.setVisibility(View.GONE);
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						TodaysSchedule_DeckSupervisorActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else{
				
				if(!LessonName.isEmpty()){
					if(StudentID.size()>1){
						old_pos = lv_instructors.getRefreshableView()
								.getFirstVisiblePosition() + 1;
					}else{
						old_pos = lv_instructors.getRefreshableView()
								.getFirstVisiblePosition() + 1;
					}
					lv_ts_data.setAdapter(TSDSAdapter);
					lv_ts_data.post(new Runnable() {


						public void run() {
							// TODO Auto-generated method stub
							lv_instructors.getRefreshableView()
									.setSelection(old_pos);
						}
					});

					lv_instructors.onRefreshComplete();
//					Log.e(TAG, "L Name = " + LevelName);
//					lv_ts_data.setAdapter(new TSDSAdapter(TodaysSchedule_DeckSupervisorActivity.this,
//							LevelName, LevelID, MainScheduleDate, SLevel, LessonName, lessontypeid, SAge,
//							ParentFirstName, ParentLastName, SLastName, SFirstName, StudentID, FormateStTimeHour, FormatStTimeMin, StTimeHour, StTimeMin, StudentGender, wu_Comments));
				}
					else{
						AlertDialog alertDialog = new AlertDialog.Builder(TodaysSchedule_DeckSupervisorActivity.this).create();
						 alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						alertDialog.setTitle("WaterWorks");
						alertDialog.setCanceledOnTouchOutside(false); 
						// set the message
						alertDialog.setMessage("Connection timeout.");
						alertDialog.setIcon(R.drawable.ic_launcher);
						// set button1 functionality
						alertDialog.setButton("Retry",
								new DialogInterface.OnClickListener() {


									public void onClick(DialogInterface dialog, int which) {
										// close dialog

										dialog.cancel();
										new GetLevel().execute();
										new TodaysScheduleData().execute();

									}
								});
						
						// show the alert dialog
						alertDialog.show();
						
					}
			}
		}
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			Log.i(TAG, ""+ System.currentTimeMillis());
		}
	}
	
	
	
	private BaseAdapter TSDSAdapter =  new BaseAdapter() {
		private int[] colors = new int[] { Color.parseColor("#EEEEEE"), Color.parseColor("#FFFFFF") };

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			try{
				if (convertView == null) {
					holder = new ViewHolder();
					

					convertView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.todays_schedule_decksupervisor_row, null);
					int colorpos=position%colors.length;
					convertView.setBackgroundColor(colors[colorpos]);
					
					holder.tv_inst_name = (TextView)convertView.findViewById(R.id.tv_ts_ds_inst_name);
					holder.tv_sName = (TextView)convertView.findViewById(R.id.tv_ts_ds_stud_name);
					holder.tv_sAge = (TextView)convertView.findViewById(R.id.tv_ts_ds_stud_age);
					holder.tv_sComments = (TextView)convertView.findViewById(R.id.tv_ts_ds_comments);
					holder.tv_lvl_name = (TextView)convertView.findViewById(R.id.tv_ts_ds_lvl);
					holder.tv_lessontype = (TextView)convertView.findViewById(R.id.tv_ts_ds_lvl_type);
					holder.tv_day = (TextView)convertView.findViewById(R.id.tv_ts_ds_day);
					holder.tv_lec_time = (TextView)convertView.findViewById(R.id.tv_ts_ds_time);
					
					holder.tv_inst_name.setText(WW_StaticClass.UserName);
					holder.tv_lessontype.setText(LessonName.get(position));
					String level = LevelName.get(LevelID.indexOf(SLevel.get(position)));
					holder.tv_lvl_name.setText(level);
					holder.tv_sComments.setText(wu_Comments.get(position));
					holder.tv_sAge.setText(SAge.get(position));
					if(StudentGender.get(position).toString().equalsIgnoreCase("Female")){
						holder.tv_sName.setText(Html.fromHtml("<font color='#8800B7'>"+SFirstName.get(position)+" "+SLastName.get(position)+"</font><br /><small>("+ParentFirstName.get(position)+" "+ParentLastName.get(position)+")</small>"));
					}
					else{
						holder.tv_sName.setText(Html.fromHtml("<font color='#000066'>"+SFirstName.get(position)+" "+SLastName.get(position)+"</font><br /><small>("+ParentFirstName.get(position)+" "+ParentLastName.get(position)+")</small>"));
					}
					 int hr = Integer.parseInt(StTimeHour.get(position));
			            int min = Integer.parseInt(StTimeMin.get(position));
			            String am_pm;
			            if(hr>=12&&min>=00){
			            	
			            	am_pm = "PM";
			            }
			            else{
			            	am_pm = "AM";
			            }
			        holder.tv_lec_time.setText(Html.fromHtml("<small>"+MainScheduleDate.get(position)+" "+FormateStTimeHour.get(position)+":"+FormatStTimeMin.get(position)+am_pm+"</small>"));
			        holder.tv_day.setText(Html.fromHtml("<small> "+TodaysSchedule_DeckSupervisorActivity.DAYNAME+"</small>"));
				}
				else {
					holder = (ViewHolder) convertView.getTag();
				}
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
			return null;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return StudentID.size();
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
	public class ViewHolder
    {
        TextView tv_sName,tv_sAge,tv_sComments,tv_lec_time,tv_lvl_name,tv_inst_name,tv_lessontype,tv_day;
    }
	
	
	
	/*private class TSDSAdapter extends BaseAdapter{
		Context context;
		ArrayList<String> LevelName,LevelID,MainScheduleDate,SLevel,LessonName,lessontypeid,
		 SAge,ParentFirstName,ParentLastName,SLastName,SFirstName,StudentID,FormateStTimeHour,
		 FormatStTimeMin,StTimeHour,StTimeMin,StudentGender,wu_Comments;
		
		public TSDSAdapter(Context context, ArrayList<String> levelName,
				ArrayList<String> levelID, ArrayList<String> mainScheduleDate,
				ArrayList<String> sLevel, ArrayList<String> lessonName,
				ArrayList<String> lessontypeid, ArrayList<String> sAge,
				ArrayList<String> parentFirstName,
				ArrayList<String> parentLastName, ArrayList<String> sLastName,
				ArrayList<String> sFirstName, ArrayList<String> studentID,
				ArrayList<String> formateStTimeHour,
				ArrayList<String> formatStTimeMin,
				ArrayList<String> stTimeHour, ArrayList<String> stTimeMin,
				ArrayList<String> studentGender, ArrayList<String> wu_Comments) {
			super();
			this.context = context;
			LevelName = levelName;
			LevelID = levelID;
			MainScheduleDate = mainScheduleDate;
			SLevel = sLevel;
			LessonName = lessonName;
			this.lessontypeid = lessontypeid;
			SAge = sAge;
			ParentFirstName = parentFirstName;
			ParentLastName = parentLastName;
			SLastName = sLastName;
			SFirstName = sFirstName;
			StudentID = studentID;
			FormateStTimeHour = formateStTimeHour;
			FormatStTimeMin = formatStTimeMin;
			StTimeHour = stTimeHour;
			StTimeMin = stTimeMin;
			StudentGender = studentGender;
			this.wu_Comments = wu_Comments;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return StudentID.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getViewTypeCount() {

			return getCount();
		}

		@Override
		public int getItemViewType(int position) {

			return position;
		}

		public class ViewHolder
	    {
	        TextView tv_sName,tv_sAge,tv_sComments,tv_lec_time,tv_lvl_name,tv_inst_name,tv_lessontype,tv_day;
	    }
		private int[] colors = new int[] { Color.parseColor("#EEEEEE"), Color.parseColor("#FFFFFF") };
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			try{
				if (convertView == null) {
					holder = new ViewHolder();
					

					convertView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.todays_schedule_decksupervisor_row, null);
					int colorpos=position%colors.length;
					convertView.setBackgroundColor(colors[colorpos]);
					
					holder.tv_inst_name = (TextView)convertView.findViewById(R.id.tv_ts_ds_inst_name);
					holder.tv_sName = (TextView)convertView.findViewById(R.id.tv_ts_ds_stud_name);
					holder.tv_sAge = (TextView)convertView.findViewById(R.id.tv_ts_ds_stud_age);
					holder.tv_sComments = (TextView)convertView.findViewById(R.id.tv_ts_ds_comments);
					holder.tv_lvl_name = (TextView)convertView.findViewById(R.id.tv_ts_ds_lvl);
					holder.tv_lessontype = (TextView)convertView.findViewById(R.id.tv_ts_ds_lvl_type);
					holder.tv_day = (TextView)convertView.findViewById(R.id.tv_ts_ds_day);
					holder.tv_lec_time = (TextView)convertView.findViewById(R.id.tv_ts_ds_time);
					
					holder.tv_inst_name.setText(WW_StaticClass.UserName);
					holder.tv_lessontype.setText(LessonName.get(position));
					String level = LevelName.get(LevelID.indexOf(SLevel.get(position)));
					holder.tv_lvl_name.setText(level);
					holder.tv_sComments.setText(wu_Comments.get(position));
					holder.tv_sAge.setText(SAge.get(position));
					if(StudentGender.get(position).toString().equalsIgnoreCase("Female")){
						holder.tv_sName.setText(Html.fromHtml("<font color='#8800B7'>"+SFirstName.get(position)+" "+SLastName.get(position)+"</font><br /><small>("+ParentFirstName.get(position)+" "+ParentLastName.get(position)+")</small>"));
					}
					else{
						holder.tv_sName.setText(Html.fromHtml("<font color='#000066'>"+SFirstName.get(position)+" "+SLastName.get(position)+"</font><br /><small>("+ParentFirstName.get(position)+" "+ParentLastName.get(position)+")</small>"));
					}
					 int hr = Integer.parseInt(StTimeHour.get(position));
			            int min = Integer.parseInt(StTimeMin.get(position));
			            String am_pm;
			            if(hr>=12&&min>=00){
			            	
			            	am_pm = "PM";
			            }
			            else{
			            	am_pm = "AM";
			            }
			        holder.tv_lec_time.setText(Html.fromHtml("<small>"+MainScheduleDate.get(position)+" "+FormateStTimeHour.get(position)+":"+FormatStTimeMin.get(position)+am_pm+"</small>"));
			        holder.tv_day.setText(Html.fromHtml("<small> "+TodaysSchedule_DeckSupervisorActivity.DAYNAME+"</small>"));
				}
				else {
					holder = (ViewHolder) convertView.getTag();
				}
				}
				catch(Exception e){
					e.printStackTrace();
				}
		             return convertView;
			}
		
	}
*/

	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
	}

	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		pgnum ++;
//		new TodaysScheduleData().execute();
		
	}

	
}
