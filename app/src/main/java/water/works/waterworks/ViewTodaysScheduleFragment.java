package water.works.waterworks;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
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

import water.works.waterworks.adapter.InstructorTodaysScheduleAdapter;
import water.works.waterworks.model.InstructorTodaysScheduleAdapterItem;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ViewTodaysScheduleFragment extends Fragment {
	public static final String TAG = "Todays Schedule";
	View rootView;
	boolean server_response = false, connectionout = false, getlevel = false,
			getISA = false;
	Boolean isInternetPresent = false;
	ListView lv_ts_data;
	boolean server_status = false;
	public static ArrayList<Integer> newatt = new ArrayList<Integer>();
	public static ArrayList<Integer> oldatt = new ArrayList<Integer>();
	ArrayList<String> LevelName = new ArrayList<String>();
	ArrayList<String> LevelID = new ArrayList<String>();
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
	static ArrayList<String> IScheduleID = new ArrayList<String>();
	ArrayList<String> SAge = new ArrayList<String>();
	ArrayList<String> ParentFirstName = new ArrayList<String>();
	ArrayList<String> ParentLastName = new ArrayList<String>();
	ArrayList<String> BirthDay = new ArrayList<String>();
	ArrayList<String> Comments = new ArrayList<String>();
	ArrayList<String> wu_r = new ArrayList<String>();
	ArrayList<String> SLastName = new ArrayList<String>();
	ArrayList<String> SFirstName = new ArrayList<String>();
	ArrayList<String> StudentID = new ArrayList<String>();
	ArrayList<String> ShowWBR = new ArrayList<String>();
	ArrayList<String> wu_b = new ArrayList<String>();
	ArrayList<String> SScheduleID = new ArrayList<String>();
	ArrayList<String> OrderDetailID = new ArrayList<String>();
	ArrayList<String> PaidClasses = new ArrayList<String>();
	ArrayList<String> SkillsCount = new ArrayList<String>();
	ArrayList<String> FormateStTimeHour = new ArrayList<String>();
	ArrayList<String> FormatStTimeMin = new ArrayList<String>();
	ArrayList<String> StTimeHour = new ArrayList<String>();
	ArrayList<String> StTimeMin = new ArrayList<String>();
	ArrayList<String> StudentGender = new ArrayList<String>();
	ArrayList<Boolean> NewUser = new ArrayList<Boolean>();
	ArrayList<Integer> sttimehr = new ArrayList<Integer>();
	ArrayList<Integer> endtimehr = new ArrayList<Integer>();
	ArrayList<Integer> sttimemin = new ArrayList<Integer>();
	ArrayList<Integer> endtimemin = new ArrayList<Integer>();
	ArrayList<Integer> wu_attendancetaken = new ArrayList<Integer>();
	ArrayList<Integer> att = new ArrayList<Integer>();
	ArrayList<String> wu_Comments = new ArrayList<String>();
	ArrayList<ArrayList<String>> PreReqID = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> PreReqChecked = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> Abbr = new ArrayList<ArrayList<String>>();
	
	public static ArrayList<String> TimeList = new ArrayList<String>();
	String currentDateandTime;
	public static Button btn_send_att;
	String Instructor_name, Instructor_id;
	TextView tv_instructor_name;
	private ArrayList<InstructorTodaysScheduleAdapterItem> att_Items;
	private InstructorTodaysScheduleAdapter adapter_Att;
	public static boolean commented = false;
	Button btn_show_all;
	public static boolean showall = false;
	public ViewTodaysScheduleFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_todaysschedule,
				container, false);
		isInternetPresent = Utility.isNetworkConnected(getActivity());
		if (isInternetPresent) {
			Initialize();
			Date date = new Date();
			System.out.println("Date-->" + date);
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			System.out.println("New Date--->" + format.format(date));
			currentDateandTime = format.format(date);
			btn_show_all.setOnClickListener(new OnClickListener() {
				

				public void onClick(View v) {
					// TODO Auto-generated method stub
					showall = true;
					new GetLevel().execute();
					
					
				}
			});
			new GetLevel().execute();
		} else {
			onDetectNetworkState().show();
		}
		return rootView;
	}

	private void Initialize() {
		// TODO Auto-generated method stub
		btn_show_all = ViewTodaysScheduleInstructorActivty.btn_show_all;
				
		Instructor_id = ((ViewTodaysScheduleInstructorActivty) getActivity()).inst_id;
		Instructor_name = ((ViewTodaysScheduleInstructorActivty) getActivity()).inst_name;
		tv_instructor_name = ViewTodaysScheduleInstructorActivty.tv_instructor_name;
		tv_instructor_name.setText(Instructor_name);
		lv_ts_data = (ListView) rootView.findViewById(R.id.listview);
		btn_send_att = (Button) rootView.findViewById(R.id.btn_send_att);
		att_Items = new ArrayList<InstructorTodaysScheduleAdapterItem>();
		adapter_Att = new InstructorTodaysScheduleAdapter(att_Items, getActivity());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isInternetPresent) {
			if (commented) {
				ClearArray();
				commented = false;
				new GetLevel().execute();
			}
		} else {
			onDetectNetworkState().show();
		}
	}

	private void ClearArray() {
		// TODO Auto-generated method stub
		LevelName.clear();
		LevelID.clear();
		MainScheduleDate.clear();
		ISAAlert.clear();
		Cls_Lvl.clear();
		Lvl_Adv_Avail.clear();
		SiteID.clear();
		SLevel.clear();
		wu_W.clear();
		ScheLevel.clear();
		SwimComp.clear();
		LessonName.clear();
		lessontypeid.clear();
		IScheduleID.clear();
		SAge.clear();
		ParentFirstName.clear();
		ParentLastName.clear();
		BirthDay.clear();
		Comments.clear();
		wu_r.clear();
		SLastName.clear();
		SFirstName.clear();
		StudentID.clear();
		ShowWBR.clear();
		wu_b.clear();
		SScheduleID.clear();
		OrderDetailID.clear();
		PaidClasses.clear();
		SkillsCount.clear();
		FormateStTimeHour.clear();
		FormatStTimeMin.clear();
		StTimeHour.clear();
		StTimeMin.clear();
		StudentGender.clear();
		NewUser.clear();
		wu_attendancetaken.clear();
		att.clear();
		wu_Comments.clear();
		PreReqID.clear();
		PreReqChecked.clear();
		Abbr.clear();
		newatt.clear();
		oldatt.clear();
//		att_Items.clear();
		sttimehr.clear();
		sttimemin.clear();
		endtimehr.clear();
		endtimemin.clear();
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {


							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// t.interrupt();
								getActivity().finish();
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

	// ////////////Get level list/////////////
	private class GetLevel extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setTitle("Please wait...");
			pd.setMessage("Loading Levels...");
			pd.setCancelable(false);
			pd.show();
		}

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
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_LevelList,
						envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i("here", "Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				// response.toString();
				if (code.equals("000")) {
					getlevel = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3=" + mSoapObject3);
					JSONObject jo = new JSONObject(mSoapObject3.toString());
					JSONArray jArray = jo.getJSONArray("Levels");
					JSONObject jsonObject;
					for (int i = 0; i < jArray.length(); i++) {
						jsonObject = jArray.getJSONObject(i);
						LevelName.add(jsonObject.getString("LevelName"));
						LevelID.add(jsonObject.getString("LevelId"));
					}
					Log.e(TAG, "Level name = " + LevelName);
					Log.e(TAG, "Level id = " + LevelID);
				} else {
					getlevel = false;
				}

			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				connectionout = true;
			} catch (SocketException e) {
				e.printStackTrace();
				connectionout = true;
			} catch (JSONException e) {
				e.printStackTrace();
				server_response = false;
			} catch (Exception e) {
				e.printStackTrace();
				server_response = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			if (server_response) {
				server_response = false;
				onDetectNetworkState().show();
			} else if (connectionout) {
				connectionout = false;
				new GetLevel().execute();
			} else {
				if (getlevel) {
					getlevel = false;
					Date date = new Date();
					SimpleDateFormat format = new SimpleDateFormat(
							"MM/dd/yyyy hh:mm a");
					currentDateandTime = format.format(date);
					new TodaysScheduleData().execute();
				}
			}
		}
	}
	private static int SPLASH_TIME_OUT = 1000;
	private class TodaysScheduleData extends AsyncTask<Void, Void, Void> {
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			try{
			pd = new ProgressDialog(getActivity());
			pd.setTitle("Please wait...");
			pd.setMessage("Loading Schedules...");
			pd.setCancelable(false);
			pd.show();
			Date date = new Date();
			System.out.println("Date-->" + date);
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			System.out.println("New Date--->" + format.format(date));
			currentDateandTime = format.format(date);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_ViewSchl_GetViewScheduleByDateAndInstrid);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("Rinstructorid", Instructor_id);
			request.addProperty("strRScheDate", currentDateandTime);
			// request.addProperty("strRScheDate","10/05/2014 08:00 AM" );

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport
						.call(SOAP_CONSTANTS.SOAP_Action_ViewSchl_GetViewScheduleByDateAndInstrid,
								envelope); // Calling Web service
				if (envelope.bodyIn instanceof SoapFault) {
					String str = ((SoapFault) envelope.bodyIn).faultstring;
					Log.i(TAG, "Error = " + str);
				} else {

					SoapObject response = (SoapObject) envelope.bodyIn;
					Log.i(TAG, "Result : " + response.toString());
					SoapPrimitive sp1 = (SoapPrimitive) response.getProperty(0);
					String resp = sp1.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("Attendance");
//					Log.i(TAG, "jArray : " + jArray.toString());
					JSONObject jsonObject;
					JSONObject jsonObject2, jsonObject3;
					JSONArray jArray2;
					JSONArray jArray3 = null;
					for (int k = 0; k < jArray.length(); k++) {
						jsonObject = jArray.getJSONObject(k);
//						Log.i(TAG, "jsonObject: " + jsonObject.toString());

						jArray2 = jsonObject.getJSONArray("Items");
//						Log.i(TAG, "jArray2 : " + jArray2.toString());
						for (int i = 0; i < jArray2.length(); i++) {
							jsonObject2 = jArray2.getJSONObject(i);
							FormateStTimeHour.add(jsonObject2
									.getString("FormateStTimeHour"));
							FormatStTimeMin.add(jsonObject2
									.getString("FormatStTimeMin"));
							StTimeHour.add(jsonObject2.getString("StTimeHour"));
							StTimeMin.add(jsonObject2.getString("StTimeMin"));
							ISAAlert.add(jsonObject2.getString("ISAAlert"));
							SiteID.add(jsonObject2.getString("SiteID"));
							SLevel.add(jsonObject2.getString("SLevel"));
							wu_W.add(jsonObject2.getString("wu_W"));
							ScheLevel.add(jsonObject2.getString("ScheLevel"));
							SwimComp.add(jsonObject2.getString("SwimComp"));
							LessonName.add(jsonObject2.getString("LessonName"));
							lessontypeid.add(jsonObject2
									.getString("lessontypeid"));
							IScheduleID.add(jsonObject2
									.getString("IScheduleID"));
							SAge.add(jsonObject2.getString("SAge"));
							ParentFirstName.add(jsonObject2
									.getString("ParentFirstName"));
							ParentLastName.add(jsonObject2
									.getString("ParentLastName"));
							BirthDay.add(jsonObject2.getString("BirthDay"));
							Comments.add(jsonObject2.getString("Comments"));
							wu_r.add(jsonObject2.getString("wu_r"));
							SFirstName.add(jsonObject2.getString("SFirstName"));
							SLastName.add(jsonObject2.getString("SLastName"));
							StudentID.add(jsonObject2.getString("StudentID"));
							ShowWBR.add(jsonObject2.getString("ShowWBR"));
							wu_b.add(jsonObject2.getString("wu_b"));
							SScheduleID.add(jsonObject2
									.getString("SScheduleID"));
							OrderDetailID.add(jsonObject2
									.getString("OrderDetailID"));
							PaidClasses.add(jsonObject2
									.getString("PaidClasses"));
							Cls_Lvl.add(jsonObject2.getString("ClsLvl"));
							Lvl_Adv_Avail.add(jsonObject2
									.getString("LvlAdvAvail"));
							MainScheduleDate.add(jsonObject2
									.getString("MainScheduleDate"));
							SkillsCount.add(jsonObject2
									.getString("SkillsCount"));
							StudentGender.add(jsonObject2
									.getString("StudentGender"));
							NewUser.add(jsonObject2.optBoolean("NewUser"));
							wu_attendancetaken.add(jsonObject2
									.getInt("wu_attendancetaken"));
							att.add(jsonObject2.getInt("att"));
							wu_Comments.add(jsonObject2
									.getString("wu_comments"));
							sttimehr.add(jsonObject2.getInt("sttimehr"));
							sttimemin.add(jsonObject2.getInt("sttimemin"));
							endtimehr.add(jsonObject2.getInt("endtimehr"));
							endtimemin.add(jsonObject2.getInt("endtimemin"));
							TimeList.add(jsonObject2.getString("MainScheduleDate")+" "+jsonObject2.getString("sttimehr")+":"+jsonObject2.getString("FormatStTimeMin"));
							if (jsonObject2.getString("SkillsCount").toString().equalsIgnoreCase("0")) {
								ArrayList<String> tempabbr = new ArrayList<String>();
								ArrayList<String> tempPreReqId = new ArrayList<String>();
								ArrayList<String> tempPreReqChecked = new ArrayList<String>();
								tempabbr.add("");
								tempPreReqId.add("");
								tempPreReqChecked.add("");
								Abbr.add(tempabbr);
								PreReqID.add(tempPreReqId);
								PreReqChecked.add(tempPreReqChecked);
							} else {
								jArray3 = jsonObject2
										.getJSONArray("SkillsList");

								ArrayList<String> tempabbr = new ArrayList<String>();
								ArrayList<String> tempPreReqId = new ArrayList<String>();
								ArrayList<String> tempPreReqChecked = new ArrayList<String>();
								for (int b = 0; b < jArray3.length(); b++) {
									jsonObject3 = jArray3.getJSONObject(b);
									String abbr = jsonObject3.getString("Abbr");
									String prereqid = jsonObject3
											.getString("PreReqID");
									String prereqchecked = jsonObject3
											.getString("PreReqChecked");

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
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
				server_status = true;
			} catch (SocketTimeoutException e) {
				// TODO: handle exception
				connectionout = true;
			} catch (SocketException e) {
				// TODO: handle exception
				connectionout = true;
			} catch (OutOfMemoryError e) {
				// TODO: handle exception
				server_status = true;
				e.printStackTrace();
			} catch (NullPointerException e) {
				// TODO: handle exception
				e.printStackTrace();
				server_status = true;
			} catch (Exception e) {
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
			try {
				pd.dismiss();
				if (server_status) {
					onDetectNetworkState().show();
					server_status = false;

				} else if (connectionout) {
					connectionout = false;
					new TodaysScheduleData().execute();
				} else {

					if (!LessonName.isEmpty()) {
						Log.e(TAG, "L Name = " + LevelName);
						GenerateNewAndOldAtt();
						for (int i = 0; i < SAge.size(); i++) {
							att_Items.add(new InstructorTodaysScheduleAdapterItem(
									LevelName, LevelID,
									MainScheduleDate.get(i), ISAAlert.get(i),
									Cls_Lvl.get(i), Lvl_Adv_Avail.get(i),
									SiteID.get(i), SLevel.get(i), wu_W.get(i),
									ScheLevel.get(i), SwimComp.get(i),
									LessonName.get(i), lessontypeid.get(i),
									IScheduleID.get(i), SAge.get(i),
									ParentFirstName.get(i), ParentLastName
											.get(i), BirthDay.get(i), Comments
											.get(i), wu_r.get(i), SLastName
											.get(i), SFirstName.get(i),
									StudentID.get(i), ShowWBR.get(i), wu_b
											.get(i), SScheduleID.get(i),
									OrderDetailID.get(i), PaidClasses.get(i),
									SkillsCount.get(i), FormateStTimeHour
											.get(i), FormatStTimeMin.get(i),
									StTimeHour.get(i), StTimeMin.get(i),
									StudentGender.get(i), wu_Comments.get(i),
									NewUser.get(i), wu_attendancetaken.get(i),
									att.get(i), PreReqID.get(i), PreReqChecked
											.get(i), Abbr.get(i),
									Instructor_id, btn_send_att));
						}
						adapter_Att.notifyDataSetChanged();
						lv_ts_data.setAdapter(adapter_Att);
						lv_ts_data.clearFocus();
						
								Calendar c = Calendar.getInstance();
								try {
									for (int i = 0; i < StTimeHour.size(); i++) {
										if ((c.get(Calendar.HOUR_OF_DAY) >= sttimehr
												.get(i) && c.get(Calendar.HOUR_OF_DAY) <= endtimehr
												.get(i))){
											if(endtimemin.get(i)==0){
												if (c.get(Calendar.MINUTE) >= sttimemin
														.get(i) && c
														.get(Calendar.MINUTE) <= 59) {
												lv_ts_data.setSelection(i);
														}
											}else{
												if (c.get(Calendar.MINUTE) >= sttimemin
														.get(i) && c
														.get(Calendar.MINUTE) <= endtimemin
														.get(i)) {
												lv_ts_data.setSelection(i);
														}
											}
										}
											
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					} else {
						AlertDialog alertDialog = new AlertDialog.Builder(
								getActivity()).create();
						alertDialog
								.requestWindowFeature(Window.FEATURE_NO_TITLE);
						alertDialog.setTitle("AquaticsApp");
						alertDialog.setCanceledOnTouchOutside(false);
						// set the message
						alertDialog.setMessage("No lesson.");
						alertDialog.setIcon(R.drawable.ic_launcher);
						// set button1 functionality
						alertDialog.setButton("Ok",
								new DialogInterface.OnClickListener() {


									public void onClick(DialogInterface dialog,
											int which) {
										// close dialog

										dialog.cancel();

									}
								});

						// show the alert dialog
						alertDialog.show();

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			Log.i(TAG, "" + System.currentTimeMillis());
		}
	}

	public void GenerateNewAndOldAtt() {
		// TODO Auto-generated method stub
		if (!newatt.isEmpty()) {
			newatt.clear();
		}
		if (!oldatt.isEmpty()) {
			oldatt.clear();
		}
		for (int j = 0; j < StudentID.size(); j++) {
			int temp_att = att.get(j);
			oldatt.add(temp_att);
			if (wu_attendancetaken.get(j) == 0) {
				newatt.add(0);
			} else {
				if (temp_att == 2 || temp_att == 3 || temp_att == 4

				|| temp_att == 5 || temp_att == 6 || temp_att == 7
						|| temp_att == 8 || temp_att == 10 || temp_att == 12
						|| temp_att == 13 || temp_att == 14 || temp_att == 15
						|| temp_att == 16 || temp_att == 17) {
					newatt.add(1);
				} else if (temp_att == 0) {
					newatt.add(0);
				} else {
					newatt.add(1);
				}
			}
		}
		Log.e(TAG, "New att = " + newatt);
		Log.e(TAG, "Old att = " + oldatt);
	}
}
