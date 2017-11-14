package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ShadowLogsActivity extends Activity implements OnClickListener {
	TextView mtv_name,mtv_day,mtv_date,mtv_time;
	Boolean isInternetPresent = false;
	EditText et_shadow;
	Button btn_save,btn_app_logoff;
	ListView lv_loadcomments;
	String Tag = "Comment Screen";
	boolean comment_status = false,connctionout=false;
	boolean server_status = false;
		String am_pm;
		java.util.Date noteTS;
		String time;
		String day_name;
		Titanic titanic;
		TitanicTextView tv ;
		FrameLayout fl_shadow_loading;
		ImageButton btn_back;
		Thread t;
		int hour,min,Day_Name,Date,Month;
		Calendar c;
		String from,ischeduleid,MgrShadowLogID;
		LinearLayout linearLayout1;
		ArrayList<String> TbID,LogText;
	String et_shadowstr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shadow_logs);
		isInternetPresent = Utility
				.isNetworkConnected(ShadowLogsActivity.this);
		if(isInternetPresent){
			from = getIntent().getStringExtra("from");
			ischeduleid = getIntent().getStringExtra("ischeduleid");
		setDateTime();
		Initialization();
		WW_StaticClass.duration1 = 300;
		 WW_StaticClass.duration2 = 1000;
		 titanic = new Titanic();
		 titanic.start(tv);
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
		new LoadShadows().execute();
		}else{
			onDetectNetworkState().show();
		}
		
	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(ShadowLogsActivity.this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		    	t.interrupt();
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
		tv = (TitanicTextView) findViewById(R.id.my_text_view);
		 tv.setTypeface(Typefaces.get(ShadowLogsActivity.this, "Satisfy-Regular.ttf"));
		 fl_shadow_loading = (FrameLayout)findViewById(R.id.shadow_loading);
		 fl_shadow_loading.setVisibility(View.GONE);
		mtv_date = (TextView)findViewById(R.id.tv_app_date);
		mtv_day = (TextView)findViewById(R.id.tv_app_day);
		mtv_name = (TextView)findViewById(R.id.tv_app_inst_name);
		mtv_time = (TextView)findViewById(R.id.tv_app_time);
		btn_save = (Button)findViewById(R.id.btn_save_shadow);
		btn_app_logoff = (Button)findViewById(R.id.btn_app_logoff);
		btn_app_logoff.setVisibility(View.GONE);
		et_shadow = (EditText)findViewById(R.id.et_shadow);
		lv_loadcomments = (ListView)findViewById(R.id.lv_shadow);
		btn_save.setOnClickListener(this);
		linearLayout1 = (LinearLayout)findViewById(R.id.linearLayout1);
//		if(from.toString().equalsIgnoreCase("edit")){
			linearLayout1.setVisibility(View.VISIBLE);
//		}
//		else{
//			linearLayout1.setVisibility(View.GONE);
//		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub\
		et_shadowstr=et_shadow.getText().toString();
		if(isInternetPresent){
		switch (v.getId()) {
		case R.id.btn_back:
				finish();
			break;
		case R.id.btn_save_shadow:
			if(et_shadow.getText().toString().equalsIgnoreCase("")){
				Toast.makeText(getApplicationContext(), "Please insert comment.", Toast.LENGTH_LONG).show();
			}else{
				new SaveShadow().execute();
			}
			break;
		default:
			break;
		}
		}else{
			onDetectNetworkState().show();
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
				.isNetworkConnected(ShadowLogsActivity.this);

	}
	private class LoadShadows extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fl_shadow_loading.setVisibility(View.VISIBLE);
			fl_shadow_loading.bringToFront();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetManabgersShadowLog);
			// Adding Username and Password for Login Invok
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("ManagerID",WW_StaticClass.InstructorID);
			request.addProperty("ischeduleid",ischeduleid);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetManabgersShadowLog,
						envelope); // Calling Web service
				SoapObject response =  (SoapObject) envelope.getResponse();
				 Log.i("here","Result : " + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if (code.equals("000")) {
					comment_status = true;
					LogText = new ArrayList<String>();
					TbID = new ArrayList<String>();
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i(Tag, "mSoapObject3="+mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jsonArray = jo.getJSONArray("ShadowLog");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject mJsonObject = jsonArray.getJSONObject(i);
						TbID.add(mJsonObject.getString("TbID"));
						LogText.add(mJsonObject.getString("LogText"));
					}
				 }
				 else {
						comment_status = false;
					} 
			}
			catch(SocketException e){
				e.printStackTrace();
				connctionout = true;
			}
			/*catch(JSONException e){
				e.printStackTrace();
				server_status = true;
			}*/
			 catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
				 server_status = true;
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			fl_shadow_loading.setVisibility(View.GONE);
			if(server_status){
				server_status = false;
				onDetectNetworkState().show();
			}
			else if(connctionout){
				new LoadShadows().execute();
			}
			else{
				if (comment_status) {
					Log.e(Tag, "Success");
					lv_loadcomments.setAdapter(new ShadowLogAdapter(
							getApplicationContext(),TbID,LogText));
					comment_status =false;
				}else {
					
				}
			}
		}
	}
	
	private class SaveShadow extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fl_shadow_loading.setVisibility(View.VISIBLE);
			fl_shadow_loading.bringToFront();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_InsertManabgersShadowLog);
			// Adding Username and Password for Login Invok
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("ManagerID",WW_StaticClass.InstructorID);
			request.addProperty("ischeduleid",ischeduleid);
			request.addProperty("LogText",et_shadowstr);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertManabgersShadowLog,
						envelope); // Calling Web service
				SoapPrimitive response =  (SoapPrimitive) envelope.getResponse();
				 Log.i("here","Result : " + response.toString());
				 String rep = response.toString();
				 JSONObject jsonObject = new JSONObject(rep);
				 JSONArray jsonObject2 = jsonObject.getJSONArray("MgrShadowLogID");
				 MgrShadowLogID = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]","");
				 Log.e("ManagerShadowID", "ManagerShadowID " + MgrShadowLogID );
				 comment_status = true;
		}
		catch(SocketException e){
			e.printStackTrace();
			connctionout = true;
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
			fl_shadow_loading.setVisibility(View.GONE);
			if(server_status){
				server_status = false;
				onDetectNetworkState().show();
			}else if(connctionout){
				connctionout = false;
				new SaveShadow().execute();
			}
			else{
				if(comment_status){
					et_shadow.setText("");
					new LoadShadows().execute();
				}
			}
		}
	}
	
	private class ShadowLogAdapter extends BaseAdapter {
		ArrayList<String> tbid,logtext;
		Context context;
		public ShadowLogAdapter(Context context, ArrayList<String> tbid,
                                ArrayList<String> logtext) {
			super();
			this.tbid = tbid;
			this.logtext = logtext;
			this.context = context;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return tbid.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

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
			TextView tv_comment,tv_date_time;
			Button btn_delete_comment;
	    }
		private int[] colors = new int[] { Color.parseColor("#EEEEEE"), Color.parseColor("#FFFFFF") };

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			try{
				if(convertView==null){
					holder = new ViewHolder();
					
					convertView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.commentsraw, null);
					int colorpos=position%colors.length;
					convertView.setBackgroundColor(colors[colorpos]);
					holder.tv_comment = (TextView)convertView.findViewById(R.id.tv_commnetsraw_comment);
					holder.tv_date_time = (TextView)convertView.findViewById(R.id.tv_commentsraw_date);
					holder.btn_delete_comment = (Button)convertView.findViewById(R.id.btn_delete_comment);
					holder.tv_date_time.setVisibility(View.GONE);
					holder.btn_delete_comment.setVisibility(View.GONE);
					holder.tv_comment.setText(logtext.get(position));
				}else{
					holder = (ViewHolder) convertView.getTag();
					
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return convertView;
		}
		
		
	}
}
