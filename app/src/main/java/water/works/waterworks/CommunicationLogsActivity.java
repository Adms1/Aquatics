package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class CommunicationLogsActivity extends Activity implements
        OnClickListener {
	Boolean isInternetPresent = false;
	TextView mtv_name, mtv_day, mtv_date, mtv_time, tv_View_History;
	public static TextView emp_name;
	String am_pm, time, sendinfo;
	Date noteTS;
	Thread t;
//	Titanic titanic;
//	TitanicTextView tv;
	Shimmer shimmer;
	ShimmerTextView tv;
	FrameLayout fl_loading;
	Button btn_app_logoff;
	public static Button date_selection;
	int mYEAR, mMONTH, mDAY;
	String startday, startmonth, startyear;
	private static String TAG = "Communication log";
	public static EditText et_log;
	public static String CommLogID = "0";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_communication_logs);
		isInternetPresent = Utility
				.isNetworkConnected(CommunicationLogsActivity.this);
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
//			titanic = new Titanic();
//			titanic.start(tv);

			shimmer=new Shimmer();
			shimmer.start(tv);
			// /////////////////////

			t = new Thread() {

				@Override
				public void run() {
					try {
						while (!isInterrupted()) {
							Thread.sleep(10);
							runOnUiThread(new Runnable() {
								@Override
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
		}
	}

	private void initialize() {
		// TODO Auto-generated method stub
		tv = (ShimmerTextView) findViewById(R.id.my_text_view);
		tv.setTypeface(Typefaces.get(CommunicationLogsActivity.this,
				"Satisfy-Regular.ttf"));
		fl_loading = (FrameLayout) findViewById(R.id.commu_loading);
		fl_loading.setVisibility(View.GONE);
		fl_loading.bringToFront();
		mtv_date = (TextView) findViewById(R.id.tv_app_date);
		mtv_day = (TextView) findViewById(R.id.tv_app_day);
		mtv_name = (TextView) findViewById(R.id.tv_app_inst_name);
		mtv_time = (TextView) findViewById(R.id.tv_app_time);
		btn_app_logoff = (Button) findViewById(R.id.btn_app_logoff);
		btn_app_logoff.setVisibility(View.GONE);
		emp_name = (TextView) findViewById(R.id.tv_commu_emp_name);
		emp_name.setText(WW_StaticClass.UserName);
		date_selection = (Button) findViewById(R.id.btn_commu_date);
		et_log = (EditText) findViewById(R.id.et_communication_log);
		tv_View_History = (TextView) findViewById(R.id.tv_history_communication);
		tv_View_History.setText(Html.fromHtml("<u>"
				+ tv_View_History.getText().toString() + "</u>"));
		tv_View_History.setOnClickListener(this);
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				CommunicationLogsActivity.this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
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

					@Override
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
		WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
		WW_StaticClass.UserName = WW_StaticClass.InstructorName;
		t.interrupt();
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInternetPresent = Utility
				.isNetworkConnected(CommunicationLogsActivity.this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (isInternetPresent) {
			switch (v.getId()) {
			case R.id.btn_back:
				t.interrupt();
				WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
				WW_StaticClass.UserName = WW_StaticClass.InstructorName;
				finish();
				break;
			case R.id.btn_commu_date:
				if (date_selection.getText().toString().equalsIgnoreCase("")) {
					Calendar c = Calendar.getInstance();
					mYEAR = c.get(Calendar.YEAR);
					mMONTH = c.get(Calendar.MONTH);
					mDAY = c.get(Calendar.DAY_OF_MONTH);
					DatePickerDialog mDialog = new DatePickerDialog(
							CommunicationLogsActivity.this, mDateSetListener,
							mYEAR, mMONTH, mDAY);
					// mDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
					// mDialog.getDatePicker().setMinDate(System.currentTimeMillis());
					mDialog.show();
				}else{
					String[] temp_date = date_selection.getText().toString().split("\\/");
					mMONTH = Integer.parseInt(temp_date[0]);
					mMONTH = mMONTH -1;
					mDAY = Integer.parseInt(temp_date[1]);
					mYEAR = Integer.parseInt(temp_date[2]);
					DatePickerDialog mDialog = new DatePickerDialog(
							CommunicationLogsActivity.this, mDateSetListener,
							mYEAR, mMONTH, mDAY);
					// mDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
					// mDialog.getDatePicker().setMinDate(System.currentTimeMillis());
					mDialog.show();
				}
				break;
			case R.id.btn_save_communication:
				if (date_selection.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(getApplicationContext(),
							"Please select date.", Toast.LENGTH_LONG).show();
				} else if (et_log.getText().toString().equalsIgnoreCase("")) {
					Toast.makeText(getApplicationContext(),
							"Please add comments.", Toast.LENGTH_LONG).show();
				} else {
					new SaveLog().execute();
				}
				break;
			case R.id.tv_history_communication:
				Intent i = new Intent(getApplicationContext(),
						ViewAllCommunicationActivity.class);
				startActivity(i);
				break;
			default:
				break;
			}
		} else {
			// Internet connection is not present
			onDetectNetworkState().show();
		}
	}

	final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		private Calendar c;

		public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
			mYEAR = year;
			mMONTH = monthOfYear + 1;
			mDAY = dayOfMonth;
			c = Calendar.getInstance();
			String d, m, y;
			d = Integer.toString(mDAY);
			m = Integer.toString(mMONTH);
			y = Integer.toString(mYEAR);
			if (mDAY < 10) {
				d = "0" + d;
			}
			if (mMONTH < 10) {
				m = "0" + m;
			}

			startday = d;
			startmonth = m;
			startyear = y;
			date_selection.setText(m + "/" + d + "/" + y);
			Log.v("daySelected2", "" + d);
			Log.v("monthSelected2", "" + m);
			Log.v("yearSelected2", "" + y);
		}
	};

	boolean server_response = false;
	boolean sended = false, connection = false;

	private class SaveLog extends AsyncTask<Void, Void, Void> {
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
			SoapObject request = new SoapObject(
					SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_EmpComLog_Insert_EmpComLogDetails);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("CommLogID", CommLogID);
			request.addProperty("CurrentUser", WW_StaticClass.InstructorID);
			request.addProperty("strDate", date_selection.getText().toString());
			request.addProperty("CommnetValue", et_log.getText().toString());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport
						.call(SOAP_CONSTANTS.SOAP_Action_EmpComLog_Insert_EmpComLogDetails,
								envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i(TAG, "" + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				Log.i(TAG, "mSoapObject1=" + mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				Log.i(TAG, "mSoapObject2=" + mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					sended = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3=" + mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					sendinfo = jo.getString("Msg");
				} else {
					sended = false;
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
				new SaveLog().execute();
			} else {
				if (sended) {
					sended = false;
					AlertDialog.Builder alertdialogbuilder2 = new AlertDialog.Builder(
							CommunicationLogsActivity.this);
					alertdialogbuilder2.setTitle("Waterworks").setIcon(
							getResources().getDrawable(R.drawable.ic_launcher));
					alertdialogbuilder2
							.setMessage(sendinfo)
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
                                                DialogInterface dialog, int id) {
											dialog.cancel();
											Intent newIntent = new Intent(
													CommunicationLogsActivity.this,
													MenuActivity.class);
											startActivity(newIntent);
											t.interrupt();
											WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
											WW_StaticClass.UserName = WW_StaticClass.InstructorName;
											finish();
										}

									});
					AlertDialog alertDialog2 = alertdialogbuilder2.create();
					alertDialog2.show();
				} else {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							CommunicationLogsActivity.this, "WaterWorks",
							"Please send valid data.", "OK");
				}
			}
		}
	}
}
