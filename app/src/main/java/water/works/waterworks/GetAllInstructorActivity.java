package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TableRow.LayoutParams;
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
import java.util.Calendar;

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class GetAllInstructorActivity extends Activity implements OnClickListener {
	int offset_in_table=0;
	int SiteID;
	TextView mtv_name,mtv_day,mtv_date,mtv_time;
	String am_pm;
	ImageView iv_turbo;
	java.util.Date noteTS;
	Boolean isInternetPresent = false;
//	Titanic titanic;
//	TitanicTextView tv ;

	Shimmer shimmer;
	ShimmerTextView tv;
	String time;
	FrameLayout fl_award_turbo_loading;
	public static String TAG="GetAllInstructorActivity";
	boolean server_response = false;
	boolean getinstructor =false;
	boolean getsitelist = false;
	ArrayList<String> Siteid,Sitename,Userid,Username;
	ListPopupWindow sitenameList;
	Button btn_site;
	GridView grid_all_inst;
	AllInstAdapter adapter;
	Thread t;
	String From;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_all_instructor);
		
		isInternetPresent = Utility
				.isNetworkConnected(GetAllInstructorActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		else{
			From = getIntent().getStringExtra("FROM");
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
//		 titanic = new Titanic();
//		 titanic.start(tv);

			shimmer=new Shimmer();
			shimmer.start(tv);
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
		new GetSite().execute();
		}
	}

	
	private void initialize() {
		// TODO Auto-generated method stub
		tv = (ShimmerTextView) findViewById(R.id.my_text_view);
		 tv.setTypeface(Typefaces.get(GetAllInstructorActivity.this, "Satisfy-Regular.ttf"));
		 fl_award_turbo_loading = (FrameLayout)findViewById(R.id.getall_inst_loading);
		 fl_award_turbo_loading.setVisibility(View.GONE);
		 fl_award_turbo_loading.bringToFront();
		mtv_date = (TextView)findViewById(R.id.tv_app_date);
		mtv_day = (TextView)findViewById(R.id.tv_app_day);
		mtv_name = (TextView)findViewById(R.id.tv_app_inst_name);
		mtv_time = (TextView)findViewById(R.id.tv_app_time);
		btn_site = (Button)findViewById(R.id.btn_award_site);
		grid_all_inst = (GridView)findViewById(R.id.grid_all_inst);
		sitenameList = new ListPopupWindow(getApplicationContext());
	}


	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		    	t.interrupt();
		        finish();
		    }
		})       
		.setPositiveButton("Οk",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		    }
		});
		    return builder1.create();
	} 
	
	
	boolean isClick =false;
	ProgressDialog pDialog,pDialog2,pDialog3;
	private class GetSite extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			super.onPreExecute();
			fl_award_turbo_loading.setVisibility(View.VISIBLE);
			fl_award_turbo_loading.bringToFront();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(From.toString().equalsIgnoreCase("View")){
				
			}
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
				            GetAllInstructorActivity.this,
				            R.layout.edittextpopup,Sitename ));
					sitenameList.setAnchorView(btn_site);
					sitenameList.setHeight(LayoutParams.WRAP_CONTENT);
					sitenameList.setModal(true);
					sitenameList.setOnItemClickListener(
			            new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
                                                    int pos, long id) {
								// TODO Auto-generated method stub
								btn_site.setText(Sitename.get(pos));
								SiteID = Integer.parseInt(Siteid.get(pos));
								sitenameList.dismiss();
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
						GetAllInstructorActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_response = false;
			}
			else{
				if(!getsitelist){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							GetAllInstructorActivity.this,"WaterWorks",
							"No site found.", "OK");
				}
				else{
					getsitelist= false;
				}
			}
		}
		
	}
	
	private class GetToWhom extends AsyncTask<Void, Void, Void> {
		String method,action;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fl_award_turbo_loading.setVisibility(View.VISIBLE);
			fl_award_turbo_loading.bringToFront();
			if(From.toString().equalsIgnoreCase("View")){
				method = SOAP_CONSTANTS.METHOD_NAME_Get_InstrctListBySite;
				action = SOAP_CONSTANTS.SOAP_Action_Get_InstrctListBySite;
				
			}else if(From.toString().equalsIgnoreCase("Menu")){
				method = SOAP_CONSTANTS.METHOD_NAME_GetAllEmployeeList;
				action = SOAP_CONSTANTS.SOAP_Action_GetAllEmployeeList;
			}
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					method);
			if(From.toString().equalsIgnoreCase("View")){
				request.addProperty("token", WW_StaticClass.UserToken);
				request.addProperty("siteid", SiteID);
			}else if(From.toString().equalsIgnoreCase("Menu")){
				request.addProperty("Status", ""); //Y for active,N for inactive ,"" for all
				request.addProperty("Siteid", SiteID);
			}
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(action,
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
						if(From.toString().equalsIgnoreCase("View")){
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
						}else if(From.toString().equalsIgnoreCase("Menu")){
							JSONArray jArray = jo.getJSONArray("EmpList");
							Log.i(TAG,"jArray : " + jArray.toString());
							Userid = new ArrayList<String>();
							Username = new ArrayList<String>();
							JSONObject jsonObject ;
							for(int i=0;i<jArray.length();i++){
								jsonObject = jArray.getJSONObject(i);
								Username.add(jsonObject.getString("UserName"));
								Userid.add(jsonObject.getString("UserID"));
							}
							Log.i(TAG, "id = "+Userid);
							Log.i(TAG, "name = "+Username);
						}
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
			fl_award_turbo_loading.setVisibility(View.GONE);
			if(server_response){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						GetAllInstructorActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_response = false;
			}
			else{
				if(getinstructor){
					getinstructor = false;
					if(From.toString().equalsIgnoreCase("View")){
					adapter = new AllInstAdapter(getApplicationContext(), Username, Userid);
					grid_all_inst.setAdapter(adapter);
					grid_all_inst.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
							// TODO Auto-generated method stub
//							Toast.makeText(getApplicationContext(), ""+Userid.get(position), 1).show();
							String name[] = Username.get(position).toString().split("\\s");
							WW_StaticClass.UserName = name[0].toString();
							WW_StaticClass.InstructorID = Userid.get(position);
							Intent i = new Intent(getApplicationContext(), ViewYourScheduleActivity.class);
							i.putExtra("DELETE", "NO");
							startActivity(i);
							t.interrupt();
							finish();
						}
					});
					}else if(From.toString().equalsIgnoreCase("Menu")){
						adapter = new AllInstAdapter(getApplicationContext(), Username, Userid);
						grid_all_inst.setAdapter(adapter);
						grid_all_inst.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
                                                    View view, int position, long id) {
								// TODO Auto-generated method stub
//								Toast.makeText(getApplicationContext(), ""+Userid.get(position), 1).show();
								String name = Username.get(position).toString();
								WW_StaticClass.UserName = name;
								WW_StaticClass.InstructorID = Userid.get(position);
								Intent i = new Intent(getApplicationContext(), CommunicationLogsActivity.class);
								startActivity(i);
								t.interrupt();
							}
						});

					}
				}
				else{
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							GetAllInstructorActivity.this,"WaterWorks",
							"Invalid selection.Please try again", "OK");
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
		WW_StaticClass.UserName = WW_StaticClass.InstructorName;
		t.interrupt();
		finish();
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
			WW_StaticClass.UserName = WW_StaticClass.InstructorName;
			t.interrupt();
			finish();
			break;
		case R.id.btn_award_site:
			sitenameList.show();
			isClick=true;
		break;

		default:
			break;
		}
	}
	
	
	public class AllInstAdapter extends BaseAdapter {
		private Context mContext;
		 ArrayList<String> Uname,Uid;
		public AllInstAdapter(Context mContext, ArrayList<String> uname,
                              ArrayList<String> uid) {
			super();
			this.mContext = mContext;
			Uname = uname;
			Uid = uid;
		}

	    public int getCount() {
	      // TODO Auto-generated method stub
	      return Uid.size();
	    }

	    public Object getItem(int position) {
	      // TODO Auto-generated method stub
	      return null;
	    }

	    public long getItemId(int position) {
	      // TODO Auto-generated method stub
	      return position;
	    }

		public int getViewTypeCount() {

			return getCount();
		}

		@Override
		public int getItemViewType(int position) {

			return position;
		}
		
		public class ViewHolder{
			TextView tv_name;
		}

	    public View getView(int position, View convertView, ViewGroup parent) {
	      // TODO Auto-generated method stub
	    	final ViewHolder holder;
			try{
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.home_grid_item, null);
					convertView.setBackgroundColor(Color.rgb(117, 117, 117));
					holder.tv_name = (TextView)convertView.findViewById(R.id.text);
					holder.tv_name.setTextColor(Color.WHITE);
					holder.tv_name.setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
					holder.tv_name.setText(Uname.get(position));
					holder.tv_name.setEllipsize(TextUtils.TruncateAt.END);
					holder.tv_name.setMaxLines(1);
					holder.tv_name.setMinLines(1);
					holder.tv_name.setHorizontallyScrolling(true);
					holder.tv_name.setPadding(5, 10, 5, 10);
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
	    
}

	
}
