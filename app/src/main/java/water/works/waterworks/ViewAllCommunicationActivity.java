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
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Calendar;

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ViewAllCommunicationActivity extends Activity implements
        OnClickListener {

	Boolean isInternetPresent = false;
	TextView mtv_name, mtv_day, mtv_date, mtv_time;
	String am_pm, time;
	java.util.Date noteTS;
	Thread t;
	Titanic titanic;
	TitanicTextView tv;
	FrameLayout fl_loading;
	private static String TAG = "View All Communication log";
	Button btn_app_logoff;
	ListView lv_viewlog;
	boolean server_response = false;
	boolean getall = false, connection = false;
	ArrayList<String> CommunicationLogsID = new ArrayList<String>();
	ArrayList<String> Comments = new ArrayList<String>();
	ArrayList<String> LogDate = new ArrayList<String>();
	ArrayList<String> CreatedBy = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_communication);

		isInternetPresent = Utility
				.isNetworkConnected(ViewAllCommunicationActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR);
			int min = c.get(Calendar.MINUTE);
			int Day_Name = c.get(Calendar.DAY_OF_WEEK);
			int Date = c.get(Calendar.DATE);
			int Month = c.get(Calendar.MONTH);
			String day_name = null;
			if (Day_Name == 1) {
				day_name = "SUN";
			} else if (Day_Name == 2) {
				day_name = "MON";
			} else if (Day_Name == 3) {
				day_name = "TUES";
			} else if (Day_Name == 4) {
				day_name = "WED";
			} else if (Day_Name == 5) {
				day_name = "THUR";
			} else if (Day_Name == 6) {
				day_name = "FRI";
			} else if (Day_Name == 7) {
				day_name = "SAT";
			}
			Log.i("Time", "Time = " + hour + ":" + min + " " + " " + day_name
					+ " " + Date + "/" + Month);
			// ////////////
			initialize();
			WW_StaticClass.duration1 = 300;
			WW_StaticClass.duration2 = 1000;
			titanic = new Titanic();
			titanic.start(tv);
			// /////////////////////

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

									if (AM_PM == 0) {
										am_pm = "AM";
									} else {
										am_pm = "PM";
									}
								}

								private void updateTextView() {
									// TODO Auto-generated method stub
									noteTS = Calendar.getInstance().getTime();

									time = "hh:mm"; // 12:00
									mtv_time.setText(DateFormat.format(time,
											noteTS) + " " + am_pm);
								}
							});
						}
					} catch (InterruptedException e) {
					}
				}
			};

			t.start();

			mtv_name.setText(WW_StaticClass.InstructorName);
			mtv_day.setText(day_name);
			mtv_date.setText(Month + 1 + "/" + Date);
			new GetCommunicationLog().execute();
		}
	}

	private void initialize() {
		// TODO Auto-generated method stub
		tv = (TitanicTextView) findViewById(R.id.my_text_view);
		tv.setTypeface(Typefaces.get(ViewAllCommunicationActivity.this,
				"Satisfy-Regular.ttf"));
		fl_loading = (FrameLayout) findViewById(R.id.fl_loading);
		fl_loading.setVisibility(View.GONE);
		fl_loading.bringToFront();
		mtv_date = (TextView) findViewById(R.id.tv_app_date);
		mtv_day = (TextView) findViewById(R.id.tv_app_day);
		mtv_name = (TextView) findViewById(R.id.tv_app_inst_name);
		mtv_time = (TextView) findViewById(R.id.tv_app_time);
		btn_app_logoff = (Button) findViewById(R.id.btn_app_logoff);
		btn_app_logoff.setVisibility(View.GONE);
		lv_viewlog = (ListView) findViewById(R.id.lv_show_communication);
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				ViewAllCommunicationActivity.this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								t.interrupt();
								WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
								WW_StaticClass.UserName = WW_StaticClass.InstructorName;
								finish();
							}
						})
				.setPositiveButton("Οk", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
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
		t.interrupt();
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInternetPresent = Utility
				.isNetworkConnected(ViewAllCommunicationActivity.this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (isInternetPresent) {
			switch (v.getId()) {
			case R.id.btn_back:
				t.interrupt();
				finish();
				break;
			default:
				break;
			}
		} else {
			// Internet connection is not present
			onDetectNetworkState().show();
		}
	}

	private class GetCommunicationLog extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fl_loading.setVisibility(View.VISIBLE);
			fl_loading.bringToFront();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_EmpComLog_Get_EmpComLogDetails);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("CurrentUser", WW_StaticClass.InstructorID);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport
						.call(SOAP_CONSTANTS.SOAP_Action_EmpComLog_Get_EmpComLogDetails,
								envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i(TAG, "" + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					getall = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3=" + mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("EmpLogListByUser");
					JSONObject jsonObject;
					for (int k = 0; k < jArray.length(); k++) {
						jsonObject = jArray.getJSONObject(k);
						CommunicationLogsID.add(jsonObject
								.getString("CommunicationLogsID"));
						Comments.add(jsonObject.getString("Comments"));
						LogDate.add(jsonObject.getString("LogDate"));
						CreatedBy.add(jsonObject.getString("CreatedBy"));
					}
				} else {
					getall = false;
				}

			} catch (SocketException e) {
				e.printStackTrace();
				connection = true;
			} catch (JSONException e) {
				e.printStackTrace();
				server_response = true;
			} catch (Exception e) {
				e.printStackTrace();
				server_response = true;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			fl_loading.setVisibility(View.GONE);

			if (server_response) {
				onDetectNetworkState().show();
				server_response = false;
			} else if (connection) {
				connection = false;
				new GetCommunicationLog().execute();
			} else {
				if (getall) {
					getall = false;
					 lv_viewlog.setAdapter(new ViewAllCommuAdapter(CommunicationLogsID, Comments, LogDate, CreatedBy, ViewAllCommunicationActivity.this));
				}
			}
		}
	}

	private class ViewAllCommuAdapter extends BaseAdapter {
		ArrayList<String> CommunicationLogsID, Comments, LogDate, CreatedBy;
		Context context;
		String CommLogID,deleteinfo;
		boolean deleted=false;
		public ViewAllCommuAdapter(ArrayList<String> communicationLogsID,
                                   ArrayList<String> comments, ArrayList<String> logDate,
                                   ArrayList<String> createdBy, Context context) {
			super();
			CommunicationLogsID = communicationLogsID;
			Comments = comments;
			LogDate = logDate;
			CreatedBy = createdBy;
			this.context = context;
		}


		public int getCount() {
			// TODO Auto-generated method stub
			return CommunicationLogsID.size();
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

		public class ViewHolder {
			TextView tv_date, tv_comment, tv_by;
			Button btn_delete, btn_edit;
		}

		private int[] colors = new int[] { Color.parseColor("#EEEEEE"),
				Color.parseColor("#FFFFFF") };

		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			try {
				if (convertView == null) {
					holder = new ViewHolder();

					convertView = LayoutInflater
							.from(parent.getContext())
							.inflate(
									R.layout.view_communication_item,
									null);
					int colorpos = position % colors.length;
					convertView.setBackgroundColor(colors[colorpos]);
					
					holder.tv_date = (TextView)convertView.findViewById(R.id.tv_vcmn_date);
					holder.tv_comment = (TextView)convertView.findViewById(R.id.tv_vcmn_comment);
					holder.tv_by = (TextView)convertView.findViewById(R.id.tv_vcmn_by);
					holder.btn_delete = (Button)convertView.findViewById(R.id.btn_vcmn_delete);
					holder.btn_edit = (Button)convertView.findViewById(R.id.btn_vcmn_edit);
					
					holder.tv_date.setText(LogDate.get(position));
					holder.tv_by.setText(CreatedBy.get(position));
					holder.tv_comment.setText(Html.fromHtml(Comments.get(position)));
					holder.btn_delete.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(isInternetPresent){
								CommLogID = CommunicationLogsID.get(position);
								new DeleteComment().execute();
							}else{
								onDetectNetworkState().show();
							}
						}
					});
					holder.btn_edit.setOnClickListener(new OnClickListener() {
						

						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(isInternetPresent){
								
								CommunicationLogsActivity.date_selection.setText(LogDate.get(position));
								CommunicationLogsActivity.emp_name.setText(CreatedBy.get(position));
								CommunicationLogsActivity.et_log.setText(Html.fromHtml(Comments.get(position)));
								CommunicationLogsActivity.CommLogID = CommunicationLogsID.get(position);
								finish();
							}else{
								onDetectNetworkState().show();
							}
						}
					});
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
		
		private class DeleteComment extends AsyncTask<Void, Void, Void> {
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				fl_loading.setVisibility(View.VISIBLE);
				fl_loading.bringToFront();
			}
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
						SOAP_CONSTANTS.METHOD_NAME_EmpComLog_Delete_EmpComLogDetails);
				request.addProperty("token", WW_StaticClass.UserToken);
				request.addProperty("CommLogID", CommLogID);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11); // Make an Envelop for sending as whole
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				Log.i("Request", "Request = " + request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						SOAP_CONSTANTS.SOAP_ADDRESS);
				try {
					androidHttpTransport
							.call(SOAP_CONSTANTS.SOAP_Action_EmpComLog_Delete_EmpComLogDetails,
									envelope); // Calling Web service
					SoapObject response = (SoapObject) envelope.getResponse();
					Log.i(TAG, "" + response.toString());
					SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
					SoapObject mSoapObject2 = (SoapObject) mSoapObject1
							.getProperty(0);
					String code = mSoapObject2.getPropertyAsString(0).toString();
					Log.i("Code", code);
					if (code.equals("000")) {
						 deleted= true;
						 Object mSoapObject3 =  mSoapObject1.getProperty(1);
							Log.i(TAG, "mSoapObject3="+mSoapObject3);
							String resp = mSoapObject3.toString();
							JSONObject jo = new JSONObject(resp);
							deleteinfo = jo.getString("Msg");
					 }
					 else{
						 deleted= false;
						}

				} catch (SocketException e) {
					e.printStackTrace();
					connection = true;
				} catch (JSONException e) {
					e.printStackTrace();
					server_response = true;
				} catch (Exception e) {
					e.printStackTrace();
					server_response = true;
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				fl_loading.setVisibility(View.GONE);

				if (server_response) {
					onDetectNetworkState().show();
					server_response = false;
				} else if (connection) {
					connection = false;
					new DeleteComment().execute();
				} else {
					if (deleted) {
						deleted = false;
						 Toast.makeText(context, deleteinfo, Toast.LENGTH_LONG).show();
						 ViewAllCommunicationActivity.this.CommunicationLogsID.clear();
						 ViewAllCommunicationActivity.this.Comments.clear();
						 ViewAllCommunicationActivity.this.LogDate.clear();
						 ViewAllCommunicationActivity.this.CreatedBy.clear();
						 notifyDataSetChanged();
						 new GetCommunicationLog().execute();
					}
				}
			}
		}
	}
}
