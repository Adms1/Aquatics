package water.works.waterworks;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.TimerTask;

import water.works.waterworks.adapter.AllInstructorListAdapter;
import water.works.waterworks.model.AllInstructorItems;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ViewCurrentScheduleInstructorActivity extends Activity implements AnimationListener {
	private static final String TAG = "Current Schedule";
	private DrawerLayout mDrawerLayout;
	private RelativeLayout ll_filter;
	private static final int CAMERA_PIC_REQUEST = 1111;
	public String filename;

	boolean isInternetPrecent = false, getinstructor = false,
			other_problem = false, server_response = false,
			connectionout = false, getlevel = false, getschedule = false,
			getISA = false, ISAFlag = false,shadow_click=false;
	ActionBar actionBar;
	ListView lv_filter_instructor;
	ActionBarDrawerToggle mDrawerToggle;
	public ArrayList<String> UserId, UserName;
	private ArrayList<AllInstructorItems> navDrawerItems_main;
	private AllInstructorListAdapter adapter_main;
	public ImageView drawerImageView, isa_main;
	TextView actionBarTitleview;
	LinearLayout actionBarLayout;
	Fragment fragment;
	String currentDateandTime;
	public String inst_name, inst_id;
	public ImageButton btn_next, btn_prev;
	int viewpos = 0;

	Timer t1;
	Button btn_search;
	public static Button btn_view_current_lesson_request_shadow;
	public static TextView tv_scheduletime, tv_instructor_name, tv_lessonname;
	public static ArrayList<String> ClickPos = new ArrayList<String>();
	public static ArrayList<String> Instroctorname = new ArrayList<String>();
	public static ArrayList<String> Instroctorid = new ArrayList<String>();
	public static boolean searchClick = false;
	LinearLayout llbody;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_current_schedule);
		isInternetPrecent = Utility.isNetworkConnected(ViewCurrentScheduleInstructorActivity.this);
		if (isInternetPrecent) {
			// inst_id = getIntent().getStringExtra("id");
			// inst_name = getIntent().getStringExtra("name");
			inst_id = WW_StaticClass.InstructorID;
			inst_name = WW_StaticClass.UserName;
			Instroctorname.clear();
			Instroctorid.clear();
			ClickPos.clear();
			Initialization();
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
				R.layout.currentschedule_actionbar, null);
		actionBarTitleview = (TextView) actionBarLayout
				.findViewById(R.id.actionbar_titleview);
		btn_next = (ImageButton) actionBarLayout.findViewById(R.id.btnnext);
		btn_prev = (ImageButton) actionBarLayout.findViewById(R.id.btnprev);
		tv_instructor_name = (TextView) actionBarLayout
				.findViewById(R.id.tv_instructorname);
		tv_instructor_name.setSelected(true);
		tv_lessonname = (TextView) actionBarLayout
				.findViewById(R.id.tv_lessonname);
		tv_scheduletime = (TextView) actionBarLayout
				.findViewById(R.id.tv_scheduletime);
		actionBarTitleview.setText("Current Schedule");
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT);
		drawerImageView = (ImageView) actionBarLayout
				.findViewById(R.id.drawer_imageview);
		isa_main = (ImageView) actionBarLayout
				.findViewById(R.id.drawer_imageview1);
		btn_view_current_lesson_request_shadow = (Button)actionBarLayout
				.findViewById(R.id.btn_view_current_lesson_request_shadow);
		drawerImageView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mDrawerLayout.isDrawerOpen(ll_filter)) {
					mDrawerLayout.closeDrawer(ll_filter);
				} else {
					mDrawerLayout.openDrawer(ll_filter);
				}
			}
		});
		isa_main.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		actionBar.setCustomView(actionBarLayout, params);


		ll_filter = (RelativeLayout) findViewById(R.id.ll_filter);
		btn_search = (Button) findViewById(R.id.search);
		lv_filter_instructor = (ListView) findViewById(R.id.lv_filter_instructor);
		lv_filter_instructor.setChoiceMode(ListView.CHOICE_MODE_NONE);
		llbody = (LinearLayout)findViewById(R.id.llbody);
		llbody.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchClick = true;
				for (int i = 0; i < UserId.size(); i++) {
					if (i == 0) {
						inst_id = UserId.get(i);
						inst_name = UserName.get(i);
					} else {
						inst_id = inst_id + "," + UserId.get(i);
						inst_name = inst_name + "," + UserName.get(i);
					}
					Instroctorid.add(UserId.get(i));
					Instroctorname.add(UserName.get(i));
				}
				displayView_Main(0);
				if (mDrawerLayout.isDrawerOpen(ll_filter)) {
					mDrawerLayout.closeDrawers();
				}
			}
		});
		btn_search.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "Selected item = " + ClickPos);
				searchClick = true;
				for (int i = 0; i < ClickPos.size(); i++) {
					if (i == 0) {
						inst_id = UserId.get(Integer.parseInt(ClickPos.get(i)));
						inst_name = UserName.get(Integer.parseInt(ClickPos.get(i)));
					} else {
						inst_id = inst_id + "," + UserId.get(Integer.parseInt(ClickPos.get(i)));
						inst_name = inst_name + "," + UserName.get(Integer.parseInt(ClickPos.get(i)));
					}
					Instroctorid.add(UserId.get(Integer.parseInt(ClickPos.get(i))));
					Instroctorname.add(UserName.get(Integer.parseInt(ClickPos.get(i))));
				}
				displayView_Main(0);
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
		animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.blink);
		animBlink.setAnimationListener(this);
		SetRefreshView();
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public void loadAllData(){
		isInternetPrecent = Utility.isNetworkConnected(ViewCurrentScheduleInstructorActivity.this);
		if (!isInternetPrecent) {
			onDetectNetworkState().show();
		} else {
			new GetAllInstructor().execute();
			t1 = new Timer();
			t1.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {

					new GetISAAlert().execute();
				}

			}, 0, 300000);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*isInternetPrecent = Utility.isNetworkConnected(ViewCurrentScheduleInstructorActivity.this);
		if (!isInternetPrecent) {
			onDetectNetworkState().show();
		} else {
			new GetAllInstructor().execute();
			t1 = new Timer();
			t1.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {

					new GetISAAlert().execute();
				}

			}, 0, 300000);
		}*/
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		t.interrupt();
		WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
		finish();
		searchClick = false;
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
								//								if (newmin != 0 || newmin != 00) {
								//									if (newmin > oldmin) {
								//										Log.i(TAG, "new time is greater");
								//										if (FROM.toString().equalsIgnoreCase(
								//												"CURRENT")) {
								//											// new GetISAAlert().execute();
								//										}
								//									}
								//								} else if (newmin == 0 || newmin == 20
								//										|| newmin == 40) {
								//
								//								} else {
								//									newmil = Calendar.getInstance().getTime()
								//											.getSeconds();
								//									if (count == 1 && newmil == oldmil) {
								//										if (FROM.toString().equalsIgnoreCase(
								//												"CURRENT")) {
								//											// new GetISAAlert().execute();
								//										}
								//									}
								//								}
								oldmin = newmin;
								if (newmin == 0 || newmin == 20 || newmin == 40||newmin == 00) {
									newmil = Calendar.getInstance().getTime()
											.getSeconds();
									if (count == 1 && newmil == oldmil) {
										displayView_Main(viewpos);
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

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				ViewCurrentScheduleInstructorActivity.this);
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
				searchClick = false;
				WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
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
			pDialog = new ProgressDialog(ViewCurrentScheduleInstructorActivity.this);
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
					adapter_main = new AllInstructorListAdapter(
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
			fragment = new ViewCurrentScheduleFragment();
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

	private class GetISAAlert extends AsyncTask<Void, Void, Void> {
		String DateandTime = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			DateandTime = format.format(date);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetISAAlert);
			request.addProperty("token",   WW_StaticClass.UserToken);
			request.addProperty("Rinstructorid",WW_StaticClass.InstructorID);
			request.addProperty("strRScheDate",DateandTime);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetISAAlert,
						envelope); // Calling Web service
				SoapObject response =  (SoapObject) envelope.getResponse();
				Log.i(TAG, "Respose isa = " + response);
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					getISA = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					JSONObject jo = new JSONObject(mSoapObject3.toString());
					JSONArray jArray = jo.getJSONArray("ISAAlert");
					JSONObject jsonObject;
					for (int i = 0; i < jArray.length(); i++) {
						jsonObject = jArray.getJSONObject(i);
						ISAFlag = jsonObject.getBoolean("ISAFlag");
					}
				}else{
					getISA = false;
				}
			}catch (SocketTimeoutException e) {
				// TODO: handle exception
				e.printStackTrace();
				connectionout=true;
			} 
			catch (SocketException e) {
				e.printStackTrace();
				connectionout = true;
			}
			catch (JSONException e) {
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
			if (server_response) {
				server_response = false;
				onDetectNetworkState().show();
			} else if (connectionout) {
				connectionout = false;
				new GetISAAlert().execute();
			} else {
				if (getISA) {
					getISA = false;
					if (ISAFlag) {
						isa_main.invalidate();
						isa_main.setVisibility(View.VISIBLE);
						isa_main.startAnimation(animBlink);
						ISAFlag = false;
					} else {
						isa_main.invalidate();
						isa_main.clearAnimation();
						isa_main.setVisibility(View.INVISIBLE);
					}
				} else {
					isa_main.invalidate();
					isa_main.clearAnimation();
					isa_main.setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	Animation animBlink;


	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if (animation == animBlink) {
		}
	}

	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

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

}
