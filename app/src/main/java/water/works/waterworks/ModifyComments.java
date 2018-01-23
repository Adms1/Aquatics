package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import water.works.waterworks.adapter.ModifyCommentsAdapter;
import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.services.DeckNotificationService;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ModifyComments extends Activity implements OnClickListener {
	Boolean isInternetPresent = false;
	ProgressBar pb;
	String insertionresponse;
	TextView mtv_name,mtv_day,mtv_date,mtv_time;
	EditText met_newcomment;
	Button mbtn_month,mbtn_day,mbtn_year,mbtn_save;
	ListView lv_loadcomments;
	String Tag = "Comment Screen";
	boolean comment_status = false;
	boolean server_status = false;
	ArrayList<String> CommentsGetted,ExpDate,tbID;
	///Date time///
	String am_pm;
	java.util.Date noteTS;
	String time;
	String day_name;
	Calendar c;
	int hour,min,Day_Name,Date,Month;
	ListPopupWindow lpw_month,lpw_day,lpw_year;
	private String date;
	SharedPreferences mPreferences;
	String FROM;
	String yes_no_date;
	ArrayList<String> d_Day = new ArrayList<String>(Arrays.asList("for","1","2","3","4","5","6","7","8","9","10",
			"11","12","13","14","15","16","17","18","19","20",
			"21","22","23","24","25","26","27","28","29","30","31"));
	ArrayList<String> d_Month = new ArrayList<String>(Arrays.asList("Keep","1","2","3","4","5","6","7","8","9","10","11","12"));
	ArrayList<String> d_year = new ArrayList<String>();
	
//	Titanic titanic;
//	TitanicTextView tv ;
	Shimmer shimmer;
	ShimmerTextView tv;
	FrameLayout fl_modify_comments_loading;
	ImageButton btn_back;
	Thread t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_comments);
		mPreferences = PreferenceManager
				.getDefaultSharedPreferences(ModifyComments.this);
		isInternetPresent = Utility
				.isNetworkConnected(ModifyComments.this);
		FROM = getIntent().getStringExtra("FROM");
		yes_no_date = getIntent().getStringExtra("yes_no_date");
		setDateTime();
		Initialization();
		WW_StaticClass.duration1 = 300;
		 WW_StaticClass.duration2 = 1000;
//		 titanic = new Titanic();
//		 titanic.start(tv);
		shimmer=new Shimmer();
		shimmer.start(tv);
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
		d_year.add("ever");
		int addyear = c.get(Calendar.YEAR);
		d_year.add(""+addyear);
		d_year.add(""+(addyear+1));
		d_year.add(""+(addyear+2));
		d_year.add(""+(addyear+3));
		d_year.add(""+(addyear+4));
		d_year.add(""+(addyear+5));
		if(isInternetPresent){
			new GetStudentCommentList().execute();
		}
		else {
			// Internet connection is not present
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
		    	t.interrupt();
		        ModifyComments.this.finish();
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
	private void setDateTime() {
		// TODO Auto-generated method stub
		c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR);
		min = c.get(Calendar.MINUTE);
		Day_Name = c.get(Calendar.DAY_OF_WEEK);
		Date =  c.get(Calendar.DATE);
		Month = c.get(Calendar.MONTH);
		day_name = null;
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

	}
	private void Initialization() {
		// TODO Auto-generated method stub
		tv = (ShimmerTextView) findViewById(R.id.my_text_view);
		 tv.setTypeface(Typefaces.get(ModifyComments.this, "Satisfy-Regular.ttf"));
		 fl_modify_comments_loading = (FrameLayout)findViewById(R.id.modify_comments_loading);
		 fl_modify_comments_loading.setVisibility(View.GONE);
		mtv_date = (TextView)findViewById(R.id.tv_app_date);
		mtv_day = (TextView)findViewById(R.id.tv_app_day);
		mtv_name = (TextView)findViewById(R.id.tv_app_inst_name);
		mtv_time = (TextView)findViewById(R.id.tv_app_time);
		mbtn_save = (Button)findViewById(R.id.btn_save_comments);
		met_newcomment = (EditText)findViewById(R.id.et_comments);
		mbtn_month = (Button)findViewById(R.id.btn_month);
		mbtn_day = (Button)findViewById(R.id.btn_day);
		mbtn_year = (Button)findViewById(R.id.btn_year);
		lv_loadcomments = (ListView)findViewById(R.id.lv_comments);
		mbtn_save.setOnClickListener(this);
		mbtn_month.setOnClickListener(this);
		mbtn_day.setOnClickListener(this);
		mbtn_year.setOnClickListener(this);
		btn_back = (ImageButton)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		lpw_day = new ListPopupWindow(ModifyComments.this);
		lpw_month = new ListPopupWindow(ModifyComments.this);
		lpw_year = new ListPopupWindow(ModifyComments.this);
		
		lpw_month.setAdapter(new ArrayAdapter<String>(
	            getApplicationContext(),
	            R.layout.edittextpopup,d_Month));
		lpw_month.setAnchorView(mbtn_month);
		lpw_month.setHeight(300);
		lpw_month.setModal(true);
		lpw_month.setOnItemClickListener(
            new OnItemClickListener() {


				public void onItemClick(AdapterView<?> parent, View view,
                                        int pos, long id) {
					// TODO Auto-generated method stub
					mbtn_month.setText(d_Month.get(pos));
					lpw_month.dismiss();
				}
			});
		
		lpw_day.setAdapter(new ArrayAdapter<String>(
	            getApplicationContext(),
	            R.layout.edittextpopup,d_Day));
		lpw_day.setAnchorView(mbtn_day);
		lpw_day.setHeight(300);
		lpw_day.setModal(true);
		lpw_day.setOnItemClickListener(
            new OnItemClickListener() {


				public void onItemClick(AdapterView<?> parent, View view,
                                        int pos, long id) {
					// TODO Auto-generated method stub
					mbtn_day.setText(d_Day.get(pos));
					lpw_day.dismiss();
				}
			});
		
		lpw_year.setAdapter(new ArrayAdapter<String>(
	            getApplicationContext(),
	            R.layout.edittextpopup,d_year));
		lpw_year.setAnchorView(mbtn_year);
		lpw_year.setHeight(300);
		lpw_year.setModal(true);
		lpw_year.setOnItemClickListener(
            new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
                                        int pos, long id) {
					// TODO Auto-generated method stub
					mbtn_year.setText(d_year.get(pos));
					lpw_year.dismiss();
				}
			});
	}
	
	private class GetStudentCommentList extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fl_modify_comments_loading.setVisibility(View.VISIBLE);
			fl_modify_comments_loading.bringToFront();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetStudentCommentsList);
			// Adding Username and Password for Login Invok
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("studentid", WW_StaticClass.Studentid);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			CommentsGetted = new ArrayList<String>();
			ExpDate = new ArrayList<String>();
			tbID = new ArrayList<String>();
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetStudentCommentsList,
						envelope); // Calling Web service
				SoapObject response =  (SoapObject) envelope.getResponse();
				 Log.i("here","Result : " + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 Log.i(Tag, "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 Log.i(Tag, "mSoapObject2="+mSoapObject2);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
//				response.toString();
				 if (code.equals("000")) {
					comment_status = true;
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i(Tag, "mSoapObject3="+mSoapObject3);
//					String UserID = mSoapObject3.getPropertyAsString(0).toString();
//					String Uname = mSoapObject3.getPropertyAsString(1).toString();
//					String user_Type = mSoapObject3.getPropertyAsString(2)
//							.toString();
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jsonArray = jo.getJSONArray("Levels");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject mJsonObject = jsonArray.getJSONObject(i);
						CommentsGetted.add(mJsonObject.getString("Comment"));
						ExpDate.add(mJsonObject.getString("ExpDate"));
						tbID.add(mJsonObject.getString("tbID"));
					}
				 }
				 else {
						comment_status = false;
					}
			}
			catch(JSONException e){
				e.printStackTrace();
				server_status = true;
			}
			catch(Exception e){
				server_status=true;
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			fl_modify_comments_loading.setVisibility(View.GONE);
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						ModifyComments.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
			}
			else{
				if (comment_status) {
					Log.e(Tag, "Success");
					lv_loadcomments.setAdapter(new ModifyCommentsAdapter(
							CommentsGetted, ExpDate, tbID,FROM,getApplicationContext()));
					comment_status =false;
				}else {
//						SingleOptionAlertWithoutTitle.ShowAlertDialog(
//								ModifyComments.this,"WaterWorks", "No comments found", "Ok");
					}
				}

		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (isInternetPresent) {
			
		
		switch (v.getId()) {
		case R.id.btn_month:
//				showDialog(0);
				lpw_month.show();
			break;
		case R.id.btn_back:
			onBackPressed();
			break;
		case R.id.btn_day:
				lpw_day.show();
			break;
		case R.id.btn_year:
				lpw_year.show();
			break;
		case R.id.btn_save_comments:
			newcomment = met_newcomment.getText().toString();
			newdate_keepforever = mbtn_month.getText().toString()+" "+mbtn_day.getText().toString()+" "
					+mbtn_year.getText().toString();
			Pattern p = Pattern.compile("(([a-zA-Z].*[0-9])|([0-9].*[a-zA-Z]))");
			Matcher m = p.matcher(newdate_keepforever);
			boolean b = m.find();
			System.out.println(b);
			if(newcomment.isEmpty()||newcomment.equalsIgnoreCase("")){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						ModifyComments.this, "WaterWorks","Add comment befor submitting.", "Ok");
			}
			else if(b==true){
				SingleOptionAlertWithoutTitle.ShowAlertDialog
				(ModifyComments.this, "WaterWorks", "Please select currect option", "Ok");
			}
			else if(newdate_keepforever.equalsIgnoreCase("Keep for ever")){
				newdate_keepforever="Keep for ever";
				new savecomment().execute();
			}
			else{
				newdate_keepforever=checkDigit(Integer.parseInt(mbtn_month.getText().toString()))+
						"/"+checkDigit(Integer.parseInt(mbtn_day.getText().toString()))+
						"/"+mbtn_year.getText().toString();
				new savecomment().execute();
			}
			Log.e("new date=", "new date = " + newdate_keepforever);
				
			break;
		case R.id.btn_app_logoff:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					ModifyComments.this);

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
									stopService(new Intent(ModifyComments.this, DeckNotificationService.class));
									Intent loginIntent= new Intent(ModifyComments.this, LoginActivity.class);
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
		default:
			break;
		}
		}
		else{
			onDetectNetworkState().show();
		}
	}
	String newcomment="";
	String newdate_keepforever="";
	
//	protected Dialog onCreateDialog(int id) {
//    	DatePickerDialog dialog = new DatePickerDialog(this, datePickerListener, year, month, day);
////    	dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//    	dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
//    	  return dialog;
//    	 }
//	
//	 private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
//   	  public void onDateSet(DatePicker view, int selectedYear,
//   	    int selectedMonth, int selectedDay) {
//   		  
//   		  date = checkDigit(selectedMonth+1)+"/"+checkDigit(selectedDay)+"/"+selectedYear;
//   		  Log.i("Date", "Date = "+date);
//   		  mbtn_datetime.setText(date);
//   	  	}
//
//		};
		public String checkDigit(int number)
	    {
	        return number<=9?"0"+number: String.valueOf(number);
	    }
   	
   	 private class savecomment extends AsyncTask<Void, Void, Void> {
   		 @Override
   		protected void onPreExecute() {
   			// TODO Auto-generated method stub
   			super.onPreExecute();
   			fl_modify_comments_loading.setVisibility(View.VISIBLE);
   			fl_modify_comments_loading.bringToFront();
   		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_InsertStudentComment);
			// Adding Username and Password for Login Invok
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("studentid", WW_StaticClass.Studentid);
			request.addProperty("userid",WW_StaticClass.InstructorID);
			request.addProperty("instrcomments",newcomment);
			request.addProperty("ExpDate",newdate_keepforever);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			CommentsGetted = new ArrayList<String>();
			ExpDate = new ArrayList<String>();
			tbID = new ArrayList<String>();
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertStudentComment,
						envelope); // Calling Web service
				SoapObject response =  (SoapObject) envelope.getResponse();
				 Log.i("here","Result : " + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 Log.i(Tag, "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 Log.i(Tag, "mSoapObject2="+mSoapObject2);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if (code.equals("000")) {
					comment_status = true;
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i(Tag, "mSoapObject3="+mSoapObject3);
					insertionresponse = mSoapObject3.toString();
					
				 }
				 else {
						comment_status = false;
					}
			}
			catch(Exception e){
				server_status=true;
				e.printStackTrace();
			}
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		fl_modify_comments_loading.setVisibility(View.GONE);
		if(server_status){
			SingleOptionAlertWithoutTitle.ShowAlertDialog(
					ModifyComments.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
			server_status =false;
		}
		else{
			if (comment_status) {
				comment_status = false;
				AlertDialog alertDialog = new AlertDialog.Builder(ModifyComments.this).create();
				alertDialog.setTitle("WaterWorks");
				alertDialog.setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher));
				alertDialog.setCanceledOnTouchOutside(false); 
				// set the message
				alertDialog.setMessage(insertionresponse);
				// set button1 functionality
				alertDialog.setButton("Ok",
						new DialogInterface.OnClickListener() {


							public void onClick(DialogInterface dialog, int which) {
								// close dialog

								dialog.cancel();
								if(FROM.equalsIgnoreCase("CURRENT")){
									if(yes_no_date.equalsIgnoreCase("no")){
										Intent it = new Intent(getApplicationContext(), ViewCurrentLessonActivity.class);
										it.putExtra("DELETE", "NO");
										it.putExtra("HOUR", "");
										it.putExtra("MIN", "");
										it.putExtra("DATE", "");
										startActivity(it);
										t.interrupt();
										finish();
									}
									else if(yes_no_date.equalsIgnoreCase("yes")){
										Intent it = new Intent(getApplicationContext(), ViewCurrentLessonActivity.class);
										it.putExtra("DELETE", "NO");
										it.putExtra("HOUR", WW_StaticClass.hour_for_data);
										it.putExtra("MIN", WW_StaticClass.min_for_data);
										it.putExtra("DATE", WW_StaticClass.date_for_data);
										startActivity(it);
										t.interrupt();
										finish();
									}
								}
								else if(FROM.equalsIgnoreCase("TODAY")){
									Intent it = new Intent(getApplicationContext(), ViewTodaysScheduleInstructorActivty.class);
									it.putExtra("DELETE", "NO");
									startActivity(it);
									t.interrupt();
									finish();
								}

							}
						});
				// show the alert dialog
				alertDialog.show();
			}else {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							ModifyComments.this,"WaterWorks", "Comment not inserted", "Ok");
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
				.isNetworkConnected(ModifyComments.this);

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		if(FROM.equalsIgnoreCase("CURRENT")){
			if(yes_no_date.equalsIgnoreCase("no")){
				Intent it = new Intent(getApplicationContext(), ViewCurrentLessonActivity.class);
				it.putExtra("DELETE", "NO");
				it.putExtra("HOUR", "");
				it.putExtra("MIN", "");
				it.putExtra("DATE", "");
				startActivity(it);
				t.interrupt();
				finish();
			}
			else if(yes_no_date.equalsIgnoreCase("yes")){
				Intent it = new Intent(getApplicationContext(), ViewCurrentLessonActivity.class);
				it.putExtra("DELETE", "NO");
				it.putExtra("HOUR", WW_StaticClass.hour_for_data);
				it.putExtra("MIN", WW_StaticClass.min_for_data);
				it.putExtra("DATE", WW_StaticClass.date_for_data);
				startActivity(it);
				t.interrupt();
				finish();
			}
		}
		else if(FROM.equalsIgnoreCase("TODAY")){
			Intent it = new Intent(getApplicationContext(), ViewTodaysScheduleInstructorActivty.class);
			it.putExtra("DELETE", "NO");
			startActivity(it);
			t.interrupt();
			finish();
		}

	}
}
