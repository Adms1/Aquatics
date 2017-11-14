package water.works.waterworks;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import water.works.waterworks.adapter.AllInstructorTodays;
import water.works.waterworks.model.AllInstructorItems;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ViewTodaysScheduleInstructorActivty extends Activity {
	private static final String TAG = "Todays Schedule";
	private DrawerLayout mDrawerLayout;
	private static final int CAMERA_PIC_REQUEST = 1111;
	public String filename;
	public static String Shadow_reason="";
	public static int positions=0;

	boolean isInternetPrecent = false, getinstructor = false,
			other_problem = false, server_response = false,
			connectionout = false, getlevel = false, getschedule = false,
			getISA = false, ISAFlag = false;;
			ActionBar actionBar;
			ListView lv_filter_instructor;
			ActionBarDrawerToggle mDrawerToggle;
			public ArrayList<String> UserId, UserName;
			private ArrayList<AllInstructorItems> navDrawerItems_main;
			private AllInstructorTodays adapter_main;
			public ImageView drawerImageView, isa_main;
			TextView actionBarTitleview;
			LinearLayout actionBarLayout;
			Fragment fragment;
			String currentDateandTime;
			public String inst_name, inst_id;
			int viewpos = 0;
			Timer t1;
			private RelativeLayout ll_filter;
			Button btn_search;
			public static TextView tv_scheduletime, tv_instructor_name, tv_lessonname,tv_date,tv_day;
			public static ArrayList<String> ClickPos = new ArrayList<String>();
			public static ArrayList<String> Instroctorname = new ArrayList<String>();
			public static ArrayList<String> Instroctorid = new ArrayList<String>();
			public static boolean searchClick = false;
			LinearLayout llbody;
			public static Button btn_show_all;
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_view_todays_schedule_activty);
				isInternetPrecent = Utility.isNetworkConnected(ViewTodaysScheduleInstructorActivty.this);
				if (isInternetPrecent) {
					inst_id = WW_StaticClass.InstructorID;
					inst_name = WW_StaticClass.UserName;

					Initialization();
					String deleteornot = getIntent().getStringExtra("DELETE");
					if(deleteornot.equalsIgnoreCase("YES")){
						SingleOptionAlertWithoutTitle.ShowAlertDialog(
								ViewTodaysScheduleInstructorActivty.this, "AquaticsApp", "Comment Deleted Successfully", "Ok");
					}
					else{
						Log.i(TAG, "Delete or not =" + deleteornot);
					}
				} else {
					onDetectNetworkState().show();
				}
				loadAllData();
			}

			@SuppressLint("NewApi")
			private void Initialization() {
				// TODO Auto-generated method stub
				mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
				actionBar = getActionBar();
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

				actionBarLayout = (LinearLayout) getLayoutInflater().inflate(
						R.layout.todaysscheduleinstructor_actionbar, null);
				actionBarTitleview = (TextView) actionBarLayout
						.findViewById(R.id.actionbar_titleview);
				tv_instructor_name = (TextView) actionBarLayout
						.findViewById(R.id.tv_instructorname);
				btn_show_all=(Button)actionBarLayout
						.findViewById(R.id.btn_show_all);
				actionBarTitleview.setText("Today's Schedule");
				ActionBar.LayoutParams params = new ActionBar.LayoutParams(
						ActionBar.LayoutParams.MATCH_PARENT,
						ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT);

				tv_date = (TextView)actionBarLayout.findViewById(R.id.tv_ts_date);
				tv_day = (TextView)actionBarLayout.findViewById(R.id.tv_ts_day);
				SetScreenDetails();
				drawerImageView = (ImageView) actionBarLayout
						.findViewById(R.id.drawer_imageview);
				drawerImageView.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						if (mDrawerLayout.isDrawerOpen(ll_filter)) {
							mDrawerLayout.closeDrawer(ll_filter);
						} else {
							mDrawerLayout.openDrawer(ll_filter);
						}
					}
				});
				actionBar.setCustomView(actionBarLayout, params);
				ll_filter = (RelativeLayout) findViewById(R.id.ll_filter);
				lv_filter_instructor = (ListView) findViewById(R.id.lv_filter_instructor);
				lv_filter_instructor.setOnItemClickListener(new OnItemClickListener() {


					public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
						// TODO Auto-generated method stub
						viewpos = position;
						displayView_Main(position);
						inst_id = UserId.get(position);
						inst_name = UserName.get(position);
						if (mDrawerLayout.isDrawerOpen(ll_filter)) {
							mDrawerLayout.closeDrawers();
						}

					}
				});
				mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
						R.drawable.ic_drawer1, R.string.app_name, R.string.app_name) {
					@Override
					public void onDrawerSlide(View drawerView, float slideOffset) {
						if (slideOffset == 0
								&& getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD) {
							// drawer closed
							getActionBar().setNavigationMode(
									ActionBar.NAVIGATION_MODE_STANDARD);
							invalidateOptionsMenu();
							drawerImageView.setImageDrawable(getResources()
									.getDrawable(R.drawable.ic_drawer1));
						} else if (slideOffset != 0
								&& getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD) {
							// started opening
							getActionBar().setNavigationMode(
									ActionBar.NAVIGATION_MODE_STANDARD);
							invalidateOptionsMenu();
							drawerImageView.setImageDrawable(getResources()
									.getDrawable(R.drawable.ic_action_back));
						}
						super.onDrawerSlide(drawerView, slideOffset);
					}
				};
				mDrawerLayout.setDrawerListener(mDrawerToggle);
				navDrawerItems_main = new ArrayList<AllInstructorItems>();
				SetRefreshView();
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


				tv_instructor_name.setText(WW_StaticClass.UserName);
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
			@Override
			protected void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			public void loadAllData(){
				isInternetPrecent = Utility.isNetworkConnected(ViewTodaysScheduleInstructorActivty.this);
				if (!isInternetPrecent) {
					onDetectNetworkState().show();
				} else {
					new GetAllInstructor().execute();
				}
			}
			
			@Override
			protected void onResume() {
				// TODO Auto-generated method stub
				super.onResume();
				/*isInternetPrecent = Utility.isNetworkConnected(ViewTodaysScheduleInstructorActivty.this);
				if (!isInternetPrecent) {
					onDetectNetworkState().show();
				} else {
					new GetAllInstructor().execute();
				}*/
			}

			public AlertDialog onDetectNetworkState() {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(
						ViewTodaysScheduleInstructorActivty.this);
				builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
				builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method stub
						t.interrupt();
						finish();
						ViewTodaysScheduleFragment.TimeList.clear();
						ViewTodaysScheduleFragment.showall = false;
						WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
					}
				})
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
				return builder1.create();
			}
			@Override
			public void onBackPressed() {
				// TODO Auto-generated method stub
				super.onBackPressed();
				WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
				t.interrupt();
				finish();
				ViewTodaysScheduleFragment.TimeList.clear();
				ViewTodaysScheduleFragment.showall = false;
			}
			public static Thread t;
			int oldmin, newmin, oldmil, newmil, count = 1;

			private void SetRefreshView() {
				// TODO Auto-generated method stub

				t = new Thread() {

					@Override
					public void run() {
						try {
							while (!isInterrupted()) {
								Thread.sleep(1000);
								runOnUiThread(new Runnable() {

									public void run() {
										newmin = Calendar.getInstance().getTime()
												.getMinutes();
										updateTextView();
									}

									@SuppressLint("SimpleDateFormat")
									private void updateTextView() {
										// TODO Auto-generated method stub
										oldmil = 0;
										oldmin = newmin;
										if (newmin == 0 || newmin == 20 || newmin == 40) {
											newmil = Calendar.getInstance().getTime()
													.getSeconds();
											if (count == 1 && newmil == oldmil) {
												try {
													if(ViewTodaysScheduleFragment.TimeList.size()>0){
														Date date = new Date();
														SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
														String datetime = format.format(date);
														for (int i = 0; i < ViewTodaysScheduleFragment.TimeList.size(); i++) {
															Log.i(TAG, "Date time = " + ViewTodaysScheduleFragment.TimeList.get(i));
															if(ViewTodaysScheduleFragment.TimeList.get(i).toString().equalsIgnoreCase(datetime)){
																displayView_Main(viewpos);
																break;
															}else{
																Log.i(TAG, datetime);
																//														Toast.makeText(getApplicationContext(), "No more", 1).show();
																//														break;

															}
														}
													}

												} catch (Exception e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
									}
								});
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
				};

				t.start();
			}


			private class GetAllInstructor extends AsyncTask<Void, Void, Void> {
				ProgressDialog pDialog;

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					Date date = new Date();
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
					currentDateandTime = format.format(date);
					navDrawerItems_main.clear();
					pDialog = new ProgressDialog(ViewTodaysScheduleInstructorActivty.this);
					pDialog.setTitle("Please wait...");
					pDialog.setMessage("Fetching Instructors...");
					pDialog.setCancelable(false);
					pDialog.show();
				}

				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
							SOAP_CONSTANTS.METHOD_NAME_Get_InstrctListForMgrBySite);
					request.addProperty("token", WW_StaticClass.UserToken);
					request.addProperty("siteid", WW_StaticClass.siteid.toString()
							.replaceAll("\\[", "").replaceAll("\\]", ""));
					request.addProperty("strRScheDate", currentDateandTime);
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11); // Make an Envelop for sending as whole
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					Log.i("Request", "Request = " + request);
					HttpTransportSE androidHttpTransport = new HttpTransportSE(
							SOAP_CONSTANTS.SOAP_ADDRESS);
					try {
						androidHttpTransport.call(
								SOAP_CONSTANTS.SOAP_Action_Get_InstrctListForMgrBySite, envelope); // Calling
						// Web
						// service
						SoapObject response = (SoapObject) envelope.getResponse();
						Log.i(TAG, "" + response.toString());
						SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
						SoapObject mSoapObject2 = (SoapObject) mSoapObject1
								.getProperty(0);
						String code = mSoapObject2.getPropertyAsString(0).toString();
						Log.i("Code", code);
						if (code.equalsIgnoreCase("000")) {
							getinstructor = true;
							Object mSoapObject3 = mSoapObject1.getProperty(1);
							Log.i(TAG, "mSoapObject3=" + mSoapObject3);
							String resp = mSoapObject3.toString();
							JSONObject jo = new JSONObject(resp);
							JSONArray jArray = jo.getJSONArray("InstrListBySiteid");
							Log.i(TAG, "jArray : " + jArray.toString());
							UserId = new ArrayList<String>();
							UserName = new ArrayList<String>();
							JSONObject jsonObject;
							for (int i = 0; i < jArray.length(); i++) {
								jsonObject = jArray.getJSONObject(i);
								UserName.add(jsonObject.getString("UserName"));
								UserId.add(jsonObject.getString("Userid"));
							}
						} else {
							getinstructor = false;
						}
					} catch (SocketException e) {
						e.printStackTrace();
						other_problem = true;
					} catch (SocketTimeoutException e) {
						e.printStackTrace();
						other_problem = true;
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
					pDialog.dismiss();
					if (server_response) {
						server_response = false;
						onDetectNetworkState().show();
					} else if (other_problem) {
						other_problem = false;
						new GetAllInstructor().execute();
					} else {
						if (!getinstructor) {
							Toast.makeText(getApplicationContext(),
									"No instructor found", Toast.LENGTH_LONG).show();
							navDrawerItems_main.add(new AllInstructorItems(
									"No instructor found."));
						} else {
							getinstructor = false;
							for (int i = 0; i < UserId.size(); i++) {
								navDrawerItems_main.add(new AllInstructorItems(UserName
										.get(i)));
							}
							adapter_main = new AllInstructorTodays(
									getApplicationContext(), navDrawerItems_main);
							lv_filter_instructor.setAdapter(adapter_main);
							displayView_Main(0);
						}
					}
				}
			}

			public void displayView_Main(int position) {
				// update the main content by replacing fragments

				for (int i = 0; i < navDrawerItems_main.size(); i++) {
					fragment = new ViewTodaysScheduleFragment();
				}
				if (fragment != null) {
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

					// update selected item and title, then close the drawer
					lv_filter_instructor.setItemChecked(position, true);
					lv_filter_instructor.setSelection(position);
					mDrawerLayout.closeDrawers();
				} else {
					// error in creating fragment
					Log.e(TAG, "Error in creating fragment");
				}
			}

			public void OpenCamera() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, CAMERA_PIC_REQUEST);
			}

			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				try{
					if (requestCode == CAMERA_PIC_REQUEST) {
						if(resultCode == RESULT_OK ){
							//2
							Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
							//3
							ByteArrayOutputStream bytes = new ByteArrayOutputStream();
							thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
							//4
							File file = new File(Environment.getExternalStorageDirectory()+ File.separator + filename+".png");
							System.out.println("Filename : "+filename);
							try {
								file.createNewFile();
								FileOutputStream fo = new FileOutputStream(file);
								//5
								fo.write(bytes.toByteArray());
								fo.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							String abc = Utility.convertBitmapUriToBase64(file, getApplicationContext());

							generateNoteOnSD("ByteArray.txt", abc);
							if(abc.length()>0){
								new StudentImageUpload().execute(abc);
							}
							Log.i(TAG, "Abc = " + abc);
						}
					}
				}
				catch(NullPointerException e){
					e.printStackTrace();
				}
				catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			public void generateNoteOnSD(String sFileName, String sBody){
				try
				{
					File root = new File(Environment.getExternalStorageDirectory(), "Notes");
					if (!root.exists()) {
						root.mkdirs();
					}
					File gpxfile = new File(root, sFileName);
					FileWriter writer = new FileWriter(gpxfile);
					writer.append(sBody);
					writer.flush();
					writer.close();
					//	        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
				}
				catch(IOException e)
				{
					e.printStackTrace();
					//	         importError = e.getMessage();
					//	         iError();
				}
			}  

			String code="";

			private class StudentImageUpload extends AsyncTask<String, String, Void> {

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
				}

				@Override
				protected Void doInBackground(String... params) {
					// TODO Auto-generated method stub

					System.out.println("Params : "+params[0]);
					SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
							SOAP_CONSTANTS.METHOD_NAME_SaveStudentImage);
					request.addProperty("token",   WW_StaticClass.UserToken);
					request.addProperty("bytearray",params[0]);
					request.addProperty("ImageName",filename+".png");

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11); // Make an Envelop for sending as whole
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					Log.i("Request", "Request = " + request);
					HttpTransportSE androidHttpTransport = new HttpTransportSE(
							SOAP_CONSTANTS.SOAP_ADDRESS);
					try {
						androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_SaveStudentImage,
								envelope); // Calling Web service
						SoapObject response =  (SoapObject) envelope.getResponse();
						Log.i(TAG, "Respose isa = " + response);
						SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
						SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
						code = mSoapObject2.getPropertyAsString(0).toString();
						Log.i("Code", code);
					}catch (SocketTimeoutException e) {
						// TODO: handle exception
						e.printStackTrace();
						connectionout=true;
					} 
					catch (SocketException e) {
						e.printStackTrace();
						connectionout = true;
					}
					catch (Exception e) {
						e.printStackTrace();
						server_response = true;
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					if (code.equals("000")) {
						Toast.makeText(getApplicationContext(), "Attached Successfully", Toast.LENGTH_SHORT ).show();
					} else {
						Toast.makeText(getApplicationContext(), "Not Attached..!!", Toast.LENGTH_LONG ).show();
					}
				}
			}
			
			
			
			public void direct_request(){
				showDialog(1);
			}
			Dialog dialog=null;
			@Override
			protected Dialog onCreateDialog(int id) {
				switch (id) {
				case 1:
					dialog = new Dialog(ViewTodaysScheduleInstructorActivty.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.requestshadow);
					Button send_request = (Button)dialog.findViewById(R.id.btn_rs_send_request);
					final EditText txt_reason = (EditText)dialog.findViewById(R.id.txt_reason);
					

					send_request.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							//					if(Shadow_poolid.isEmpty()){
							//						SingleOptionAlertWithoutTitle.ShowAlertDialog(ScheduleActivity.this, "WaterWorks", "Please select pool befor requesting", "Ok");
							//					}
							dialog.dismiss();
							//				for(int i=0;i<WW_StaticClass.SStudnetID.size();i++){
							//					SStudnetid = WW_StaticClass.SStudnetID.get(i);
							Shadow_reason = txt_reason.getText().toString();
							new InsertShadowRequest().execute();
							//				}
						}
					});

					/*dialog.getWindow().setAttributes(lp);
					dialog.setCanceledOnTouchOutside(true);*/

					break;
				}
				return dialog;
			}
			boolean status_shadow = false, server_status=false;
			public class InsertShadowRequest extends AsyncTask<Void, Void, Void> {
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					//					pDialog2 = new ProgressDialog(ScheduleActivity.this);
					//					pDialog2.setMessage(Html.fromHtml("Loading wait..."));
					//					pDialog2.setIndeterminate(true);
					//					pDialog2.setCancelable(false);
					//					pDialog2.show();
				}
				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					//SoapObject request = new SoapObject(AppConfig.NAMESPACE,AppConfig.METHOD_NAME_InsertShadowRequest_New1);
					SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,SOAP_CONSTANTS.METHOD_NAME_InsertShadowRequest_LA);
					//			request.addProperty("token",  WW_StaticClass.UserToken);
					//			request.addProperty("IScheduleID", IScheduleID.get(0).toString());
					//			request.addProperty("Rinstructorid", WW_StaticClass.InstructorID);
					//			request.addProperty("RPoolID", Shadow_poolid);


					request.addProperty("token",  WW_StaticClass.UserToken);
					//request.addProperty("RPoolID", Shadow_poolid);
					request.addProperty("RPoolID", 0);
					request.addProperty("InstrIschlAry", InstrIschlAry);
					request.addProperty("Reason", Shadow_reason);
					

					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11); // Make an Envelop for sending as whole
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					Log.i("Request",  "Request = " + request);
					HttpTransportSE androidHttpTransport = new HttpTransportSE(
							SOAP_CONSTANTS.SOAP_ADDRESS);
					try {
						//androidHttpTransport.call(AppConfig.SOAP_Action_InsertShadowRequest_New1,envelope); // Calling Web service
						androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertShadowRequest_LA,envelope); // Calling Web service
						SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
						Log.i("here","Result : " + response.toString());
						if(response.toString().equalsIgnoreCase("Inserted Successfully")||response.toString().equalsIgnoreCase("Registered Successfully")){
							status_shadow=true;
						}
						else{
							status_shadow= false;
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
				@SuppressWarnings("deprecation")
				@Override
				protected void onPostExecute(Void result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					//				pDialog2.dismiss();
					//			fl_vcl_loading.setVisibility(View.GONE);
					if(server_status==true){
						SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewTodaysScheduleInstructorActivty.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
						server_status = false;
					}
					if(status_shadow){
						status_shadow=false;
						//					SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "Request Shadow", "Request submitted.", "Ok");
						AlertDialog alertDialog = new AlertDialog.Builder(ViewTodaysScheduleInstructorActivty.this).create();
						// hide title bar
						alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						alertDialog.setTitle("WaterWorks");
						alertDialog.setCanceledOnTouchOutside(false); 
						// set the message
						alertDialog.setMessage("Request submitted.");
						// set button1 functionality
						alertDialog.setButton("Ok",
								new DialogInterface.OnClickListener() {


							public void onClick(DialogInterface dialog, int which) {
								// close dialog

								dialog.cancel();
								ViewTodaysScheduleInstructorActivty.this.dialog.dismiss();
							}
						});
						// show the alert dialog
						alertDialog.show();

					}
					else{
						AlertDialog alertDialog = new AlertDialog.Builder(ViewTodaysScheduleInstructorActivty.this).create();
						// hide title bar
						alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						alertDialog.setTitle("WaterWorks");
						alertDialog.setCanceledOnTouchOutside(false); 
						// set the message
						alertDialog.setMessage("Request not submitted.");
						// set button1 functionality
						alertDialog.setButton("Ok",
								new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								// close dialog
								dialog.cancel();
								ViewTodaysScheduleInstructorActivty.this.dialog.dismiss();
							}
						});
						// show the alert dialog
						alertDialog.show();
					}
				}

			}
			
			public void call_attendance(int position) {
				// TODO Auto-generated method stub
				positions=position;
				new GetAttendance().execute();
			}
			
			int wu_avail =2;
			static String InstrIschlAry = "";
			ArrayList<String> IScheduleID = new ArrayList<String>();
			
			private class GetAttendance extends AsyncTask<Void, Void, Void> {

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					Date date = new Date();
					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy h:mm a");
					currentDateandTime = format.format(date);
					
					//single instructor patch : Aanal : 23Aug2016
					Instroctorid.clear();
					Instroctorid.add(inst_id);
				}
				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub

					for (int j = 0; j < Instroctorid.size(); j++) {

						SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
								SOAP_CONSTANTS.METHOD_NAME_GetAttendanceList);
						request.addProperty("token",  WW_StaticClass.UserToken);
						request.addProperty("Rinstructorid",Instroctorid.get(j));
						request.addProperty("strRScheDate",currentDateandTime );
						//			request.addProperty("strRScheDate","01/18/2015 09:00 AM" );

						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
								SoapEnvelope.VER11); // Make an Envelop for sending as whole
						envelope.dotNet = true;
						envelope.setOutputSoapObject(request);
						Log.i("Request",  "Request = " + request);
						HttpTransportSE androidHttpTransport = new HttpTransportSE(
								SOAP_CONSTANTS.SOAP_ADDRESS);
						try {
							androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_AttendanceList,
									envelope); // Calling Web service
							SoapObject response = (SoapObject) envelope.bodyIn;
							Log.i(TAG,"Result : " + response.toString());
							SoapPrimitive sp1 = (SoapPrimitive) response.getProperty(0);
							String resp = sp1.toString();
							JSONObject jo = new JSONObject(resp);
							wu_avail = jo.getInt("wu_avail");
							//				ISAFlag = jo.getBoolean("ISAFlag");
							if(wu_avail==0){

							}else if(wu_avail==1||wu_avail==2){

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
										//								SiteID.add(jsonObject2.getString("SiteID"));
										IScheduleID.add(jsonObject2.getString("IScheduleID"));
									}
								}
							}
						}
						catch(JSONException e){
							e.printStackTrace();
						}
						catch (SocketTimeoutException e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					return null;

				}

				@SuppressWarnings("deprecation")
				@Override
				protected void onPostExecute(Void result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					InstrIschlAry="";
					if(ViewTodaysScheduleFragment.IScheduleID.size()>0){
						ArrayList<String> temp_array = new ArrayList<String>();
						for (int i = 0; i < Instroctorid.size(); i++) {
							temp_array.add(Instroctorid.get(i) +"|"+ViewTodaysScheduleFragment.IScheduleID.get(positions));
						}
						
						for (String s : temp_array)
						{
							InstrIschlAry += s +",";
						}
					}
				}
			}
			
}
