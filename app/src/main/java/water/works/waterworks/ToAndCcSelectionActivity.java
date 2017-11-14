package water.works.waterworks;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ToAndCcSelectionActivity extends Activity implements
        OnClickListener {
	int siteid, usertype;
	int TO_CC_CLICK;
	String sitename;
	private static String TAG = "To and CC selection";
	Boolean isInternetPresent = false, server_response = false,
			userload = false;
	ArrayList<String> SiteID, SiteName, Upper_Manager_userid,
			Upper_Manager_username, Manager_userid, Manager_username,
			Staff_userid, Staff_username, Inst_userid, Inst_username,
			Supervisor_userid, Supervisor_username, Mainten_userid,
			Mainten_username,Executive_userid,
			Executive_username, mUpper_manager_name, mUpper_manager_id,
			mManager_name, mManager_id, mStaff_name, mStaff_id, mInst_name,
			mInst_id, mSupervisor_name, mSupervisor_id, mMainten_name,
			mMainten_id,mExecutive_name,
			mExecutive_id, TO_ID, CC_ID, TO_NAME, CC_NAME;
	ArrayAdapter<String> adapterMaintenance, adapterSupervisor,
			adapterInstructor, adapterOfficeStaff, adapterManager,
			adapterUpperManager,adapterExecutive;
	Button btn_um, btn_mng, btn_staff, btn_super, btn_main, btn_inst,btn_executive;
	ListView list_site_list, list_upper_manager_list, list_manager_list,
			list_staff_list, list_inst_list, list_supervisor_list,
			list_maintenance_list,list_executive_list;
	FrameLayout toandccselection_loading;
	Titanic titanic;
//	TitanicTextView tv;
TextView tv;
	int length, length1, length2, length3, length4, length5,length6;
	TextView title;
	boolean selectAll = true;
	ArrayList<String> temp_um_id_to, temp_um_id_cc, temp_mng_id_to,
			temp_mng_id_cc, temp_office_id_to, temp_office_id_cc,
			temp_inst_id_to, temp_inst_id_cc, temp_super_id_to,
			temp_super_id_cc, temp_main_id_to, temp_main_id_cc,
			temp_exe_id_to, temp_exe_id_cc;
	String tempsiteid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_and_cc_selection);
		isInternetPresent = Utility
				.isNetworkConnected(ToAndCcSelectionActivity.this);

		if (isInternetPresent) {
			tempsiteid = WW_StaticClass.tempSiteId;
			siteid = getIntent().getIntExtra("siteid", 0);
			TO_CC_CLICK = getIntent().getIntExtra("WhichClick", 0);
			sitename = getIntent().getStringExtra("sitename");
			if (sitename.equalsIgnoreCase("All")) {
				sitename = "All Site";
			}
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(sitename);
			Initialization();
			try {
				if (tempsiteid.equalsIgnoreCase("" + siteid)) {
					temp_um_id_to = CreateNewMessageActivity.getTemp_um_id_to();
					temp_um_id_cc = CreateNewMessageActivity.getTemp_um_id_cc();
					temp_mng_id_to = CreateNewMessageActivity
							.getTemp_mng_id_to();
					temp_mng_id_cc = CreateNewMessageActivity
							.getTemp_mng_id_cc();
					temp_inst_id_to = CreateNewMessageActivity
							.getTemp_inst_id_to();
					temp_inst_id_cc = CreateNewMessageActivity
							.getTemp_inst_id_cc();
					temp_office_id_to = CreateNewMessageActivity
							.getTemp_office_id_to();
					temp_office_id_cc = CreateNewMessageActivity
							.getTemp_office_id_cc();
					temp_super_id_to = CreateNewMessageActivity
							.getTemp_super_id_to();
					temp_super_id_cc = CreateNewMessageActivity
							.getTemp_super_id_cc();
					temp_main_id_to = CreateNewMessageActivity
							.getTemp_main_id_to();
					temp_main_id_cc = CreateNewMessageActivity
							.getTemp_main_id_cc();
					temp_exe_id_to =  CreateNewMessageActivity
							.getTemp_exe_id_to();
					temp_exe_id_cc =  CreateNewMessageActivity
							.getTemp_exe_id_cc();

				} else {
					temp_um_id_to.clear();
					temp_um_id_cc.clear();
					temp_mng_id_to.clear();
					temp_mng_id_cc.clear();
					temp_inst_id_to.clear();
					temp_inst_id_cc.clear();
					temp_office_id_to.clear();
					temp_office_id_cc.clear();
					temp_super_id_to.clear();
					temp_super_id_cc.clear();
					temp_main_id_to.clear();
					temp_main_id_cc.clear();
					temp_exe_id_to.clear();
					temp_exe_id_cc.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (TO_CC_CLICK == 0) {
				title.setText("To:");
			} else {
				title.setText("Cc:");
			}
			WW_StaticClass.duration1 = 300;
			WW_StaticClass.duration2 = 1000;
//			titanic = new Titanic();
//			titanic.start(tv);
			usertype = 1;
			new GetUsers().execute();
			
			list_executive_list
			.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
					String index_val = Executive_userid
							.get(position);
					int pos_val = Executive_userid
							.indexOf(index_val);
					if (!Executive_userid.contains("-1")) {
					} else {
						boolean isChecked = list_executive_list
								.isItemChecked(position);
						if (index_val.equalsIgnoreCase("-1")) {
							if (selectAll) {
								for (int i = 1; i < length6; i++) { // we
																	// start
																	// with
																	// first
																	// element
																	// after
																	// "Select all"
																	// choice
									if (isChecked
											&& !list_executive_list
													.isItemChecked(i)
											|| !isChecked
											&& list_executive_list
													.isItemChecked(i)) {
										list_executive_list
												.performItemClick(
														list_executive_list,
														i, 0);
									}
								}
							}
						} else {
							if (!isChecked
									&& list_executive_list
											.isItemChecked(pos_val)) {
								// if other item is unselected while
								// "Select all" is selected, unselect
								// "Select all"
								// false, performItemClick, true is a
								// must in order for this code to work
								selectAll = false;
								list_executive_list
										.performItemClick(
												list_executive_list,
												0, 0);
								selectAll = true;
							}
						}
					}
				}
			});
			
			
			
			list_upper_manager_list
					.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
							String index_val = Upper_Manager_userid
									.get(position);
							int pos_val = Upper_Manager_userid
									.indexOf(index_val);
							if (!Upper_Manager_userid.contains("-1")) {
							} else {
								boolean isChecked = list_upper_manager_list
										.isItemChecked(position);
								if (index_val.equalsIgnoreCase("-1")) {
									if (selectAll) {
										for (int i = 1; i < length; i++) { // we
																			// start
																			// with
																			// first
																			// element
																			// after
																			// "Select all"
																			// choice
											if (isChecked
													&& !list_upper_manager_list
															.isItemChecked(i)
													|| !isChecked
													&& list_upper_manager_list
															.isItemChecked(i)) {
												list_upper_manager_list
														.performItemClick(
																list_upper_manager_list,
																i, 0);
											}
										}
									}
								} else {
									if (!isChecked
											&& list_upper_manager_list
													.isItemChecked(pos_val)) {
										// if other item is unselected while
										// "Select all" is selected, unselect
										// "Select all"
										// false, performItemClick, true is a
										// must in order for this code to work
										selectAll = false;
										list_upper_manager_list
												.performItemClick(
														list_upper_manager_list,
														0, 0);
										selectAll = true;
									}
								}
							}
						}
					});
			list_manager_list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
					String index_val = Manager_userid.get(position);
					int pos_val = Manager_userid.indexOf(index_val);
					if (!Manager_userid.contains("-1")) {
					} else {
						boolean isChecked = list_manager_list
								.isItemChecked(position);
						if (index_val.equalsIgnoreCase("-1")) {
							if (selectAll) {
								for (int i = 1; i < length1; i++) { // we start
																	// with
																	// first
																	// element
																	// after
																	// "Select all"
																	// choice
									if (isChecked
											&& !list_manager_list
													.isItemChecked(i)
											|| !isChecked
											&& list_manager_list
													.isItemChecked(i)) {
										list_manager_list.performItemClick(
												list_manager_list, i, 0);
									}
								}
							}
						} else {
							if (!isChecked
									&& list_manager_list.isItemChecked(pos_val)) {
								// if other item is unselected while
								// "Select all" is selected, unselect
								// "Select all"
								// false, performItemClick, true is a must in
								// order for this code to work
								selectAll = false;
								list_manager_list.performItemClick(
										list_manager_list, 0, 0);
								selectAll = true;
							}
						}
					}
				}
			});
			list_staff_list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
					String index_val = Staff_userid.get(position);
					int pos_val = Staff_userid.indexOf(index_val);
					if (!Staff_userid.contains("-1")) {
					} else {
						boolean isChecked = list_staff_list
								.isItemChecked(position);
						if (index_val.equalsIgnoreCase("-1")) {
							if (selectAll) {
								for (int i = 1; i < length2; i++) { // we start
																	// with
																	// first
																	// element
																	// after
																	// "Select all"
																	// choice
									if (isChecked
											&& !list_staff_list
													.isItemChecked(i)
											|| !isChecked
											&& list_staff_list.isItemChecked(i)) {
										list_staff_list.performItemClick(
												list_staff_list, i, 0);
									}
								}
							}
						} else {
							if (!isChecked
									&& list_staff_list.isItemChecked(pos_val)) {
								// if other item is unselected while
								// "Select all" is selected, unselect
								// "Select all"
								// false, performItemClick, true is a must in
								// order for this code to work
								selectAll = false;
								list_staff_list.performItemClick(
										list_staff_list, 0, 0);
								selectAll = true;
							}
						}
					}
				}
			});
			list_inst_list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
					String index_val = Inst_userid.get(position);
					int pos_val = Inst_userid.indexOf(index_val);
					if (!Inst_userid.contains("-1")) {
					} else {
						boolean isChecked = list_inst_list
								.isItemChecked(position);
						if (index_val.equalsIgnoreCase("-1")) {
							if (selectAll) {
								for (int i = 1; i < length3; i++) { // we start
																	// with
																	// first
																	// element
																	// after
																	// "Select all"
																	// choice
									if (isChecked
											&& !list_inst_list.isItemChecked(i)
											|| !isChecked
											&& list_inst_list.isItemChecked(i)) {
										list_inst_list.performItemClick(
												list_inst_list, i, 0);
									}
								}
							}
						} else {
							if (!isChecked
									&& list_inst_list.isItemChecked(pos_val)) {
								// if other item is unselected while
								// "Select all" is selected, unselect
								// "Select all"
								// false, performItemClick, true is a must in
								// order for this code to work
								selectAll = false;
								list_inst_list.performItemClick(list_inst_list,
										0, 0);
								selectAll = true;
							}
						}
					}
				}
			});
			list_supervisor_list
					.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
							String index_val = Supervisor_userid.get(position);
							int pos_val = Supervisor_userid.indexOf(index_val);
							if (!Supervisor_userid.contains("-1")) {
							} else {
								boolean isChecked = list_supervisor_list
										.isItemChecked(position);
								if (index_val.equalsIgnoreCase("-1")) {
									if (selectAll) {
										for (int i = 1; i < length4; i++) { // we
																			// start
																			// with
																			// first
																			// element
																			// after
																			// "Select all"
																			// choice
											if (isChecked
													&& !list_supervisor_list
															.isItemChecked(i)
													|| !isChecked
													&& list_supervisor_list
															.isItemChecked(i)) {
												list_supervisor_list
														.performItemClick(
																list_supervisor_list,
																i, 0);
											}
										}
									}
								} else {
									if (!isChecked
											&& list_supervisor_list
													.isItemChecked(pos_val)) {
										// if other item is unselected while
										// "Select all" is selected, unselect
										// "Select all"
										// false, performItemClick, true is a
										// must in order for this code to work
										selectAll = false;
										list_supervisor_list.performItemClick(
												list_supervisor_list, 0, 0);
										selectAll = true;
									}
								}
							}
						}
					});
			list_maintenance_list
					.setOnItemClickListener(new OnItemClickListener() {

						public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
							String index_val = Mainten_userid.get(position);
							int pos_val = Mainten_userid.indexOf(index_val);
							if (!Mainten_userid.contains("-1")) {
							} else {
								boolean isChecked = list_maintenance_list
										.isItemChecked(position);
								if (index_val.equalsIgnoreCase("-1")) {
									if (selectAll) {
										for (int i = 1; i < length5; i++) { // we
																			// start
																			// with
																			// first
																			// element
																			// after
																			// "Select all"
																			// choice
											if (isChecked
													&& !list_maintenance_list
															.isItemChecked(i)
													|| !isChecked
													&& list_maintenance_list
															.isItemChecked(i)) {
												list_maintenance_list
														.performItemClick(
																list_maintenance_list,
																i, 0);
											}
										}
									}
								} else {
									if (!isChecked
											&& list_maintenance_list
													.isItemChecked(pos_val)) {
										// if other item is unselected while
										// "Select all" is selected, unselect
										// "Select all"
										// false, performItemClick, true is a
										// must in order for this code to work
										selectAll = false;
										list_maintenance_list.performItemClick(
												list_maintenance_list, 0, 0);
										selectAll = true;
									}
								}
							}
						}
					});
		} else {
			onDetectNetworkState().show();
		}
	}

	private void Initialization() {
		// TODO Auto-generated method stub
//		tv = (TitanicTextView) findViewById(R.id.my_text_view);
		tv = (TextView) findViewById(R.id.my_text_view);
		tv.setTypeface(Typefaces.get(ToAndCcSelectionActivity.this,
				"Satisfy-Regular.ttf"));
		toandccselection_loading = (FrameLayout) findViewById(R.id.toandccselection_loading);
		toandccselection_loading.setVisibility(View.GONE);
		mUpper_manager_name = new ArrayList<String>();
		mUpper_manager_id = new ArrayList<String>();
		mManager_name = new ArrayList<String>();
		mManager_id = new ArrayList<String>();
		mStaff_name = new ArrayList<String>();
		mStaff_id = new ArrayList<String>();
		mInst_name = new ArrayList<String>();
		mInst_id = new ArrayList<String>();
		mSupervisor_name = new ArrayList<String>();
		mSupervisor_id = new ArrayList<String>();
		mMainten_name = new ArrayList<String>();
		mMainten_id = new ArrayList<String>();
		mExecutive_name = new ArrayList<String>();
		mExecutive_id = new ArrayList<String>();
		TO_ID = new ArrayList<String>();
		CC_ID = new ArrayList<String>();
		TO_NAME = new ArrayList<String>();
		CC_NAME = new ArrayList<String>();
		title = (TextView) findViewById(R.id.to_cc_selection_title);
		list_upper_manager_list = (ListView) findViewById(R.id.list_upper_manager_list);
		list_manager_list = (ListView) findViewById(R.id.list_manager_list);
		list_staff_list = (ListView) findViewById(R.id.list_office_staff_list);
		list_inst_list = (ListView) findViewById(R.id.list_instructor_list);
		list_supervisor_list = (ListView) findViewById(R.id.list_supervisor_list);
		list_maintenance_list = (ListView) findViewById(R.id.list_maintenance_list);
		list_executive_list = (ListView)findViewById(R.id.list_executive_list);
		btn_um = (Button) findViewById(R.id.to_cc_upper_managers);
		btn_mng = (Button) findViewById(R.id.to_cc_managers);
		btn_staff = (Button) findViewById(R.id.to_cc_office_staff);
		btn_inst = (Button) findViewById(R.id.to_cc_instructors);
		btn_super = (Button) findViewById(R.id.to_cc_deck_supervisor);
		btn_main = (Button) findViewById(R.id.to_cc_maintenance);
		btn_executive = (Button)findViewById(R.id.to_cc_executive);
		Upper_Manager_userid = new ArrayList<String>();
		Upper_Manager_username = new ArrayList<String>();
		Manager_userid = new ArrayList<String>();
		Manager_username = new ArrayList<String>();
		Inst_userid = new ArrayList<String>();
		Inst_username = new ArrayList<String>();
		Staff_userid = new ArrayList<String>();
		Staff_username = new ArrayList<String>();
		Supervisor_userid = new ArrayList<String>();
		Supervisor_username = new ArrayList<String>();
		Mainten_userid = new ArrayList<String>();
		Mainten_username = new ArrayList<String>();
		Executive_userid = new ArrayList<String>();
		Executive_username = new ArrayList<String>();
		
		temp_um_id_to = new ArrayList<String>();
		temp_um_id_cc = new ArrayList<String>();
		temp_mng_id_to = new ArrayList<String>();
		temp_mng_id_cc = new ArrayList<String>();
		temp_office_id_to = new ArrayList<String>();
		temp_office_id_cc = new ArrayList<String>();
		temp_inst_id_to = new ArrayList<String>();
		temp_inst_id_cc = new ArrayList<String>();
		temp_super_id_to = new ArrayList<String>();
		temp_super_id_cc = new ArrayList<String>();
		temp_main_id_to = new ArrayList<String>();
		temp_main_id_cc = new ArrayList<String>();
		temp_exe_id_to = new ArrayList<String>();
		temp_exe_id_cc = new ArrayList<String>();
	}

	private class GetUsers extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			toandccselection_loading.setVisibility(View.VISIBLE);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_NewMail_Get_UserListBySite);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("siteid", siteid);
			request.addProperty("userlevel", WW_StaticClass.UserLevel);
			request.addProperty("UserType", usertype);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(
						SOAP_CONSTANTS.SOAP_Action_NewMail_Get_UserListBySite,
						envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.getResponse();
				// SoapPrimitive response = (SoapPrimitive)
				// envelope.getResponse();
				// Log.i(TAG,"" + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				// Log.i(TAG, "mSoapObject1="+mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				// Log.i(TAG, "mSoapObject2="+mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equalsIgnoreCase("000")) {
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3=" + mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("UserTypeList");
					// Log.i(TAG,"jArray : " + jArray.toString());
					if (usertype == 1) {
						JSONObject jsonObject;
						for (int i = 0; i < jArray.length(); i++) {
							jsonObject = jArray.getJSONObject(i);
							Upper_Manager_userid.add(jsonObject
									.getString("UserID"));
							Upper_Manager_username.add(jsonObject
									.getString("UserName"));
						}
						length = Upper_Manager_username.size();
						userload = true;
					} else if (usertype == 2) {
						JSONObject jsonObject;
						for (int i = 0; i < jArray.length(); i++) {
							jsonObject = jArray.getJSONObject(i);
							Manager_userid.add(jsonObject.getString("UserID"));
							Manager_username.add(jsonObject
									.getString("UserName"));
						}
						length1 = Manager_username.size();
						userload = true;
					} else if (usertype == 3) {
						JSONObject jsonObject;
						for (int i = 0; i < jArray.length(); i++) {
							jsonObject = jArray.getJSONObject(i);
							Inst_userid.add(jsonObject.getString("UserID"));
							Inst_username.add(jsonObject.getString("UserName"));
						}
						length3 = Inst_username.size();
						userload = true;
					} else if (usertype == 4) {
						JSONObject jsonObject;
						for (int i = 0; i < jArray.length(); i++) {
							jsonObject = jArray.getJSONObject(i);
							Staff_userid.add(jsonObject.getString("UserID"));
							Staff_username
									.add(jsonObject.getString("UserName"));
						}
						length2 = Staff_username.size();
						userload = true;
					} else if (usertype == 5) {
						JSONObject jsonObject;
						for (int i = 0; i < jArray.length(); i++) {
							jsonObject = jArray.getJSONObject(i);
							Supervisor_userid.add(jsonObject
									.getString("UserID"));
							Supervisor_username.add(jsonObject
									.getString("UserName"));
						}
						length4 = Supervisor_username.size();
						userload = true;
					} else if (usertype == 6) {
						JSONObject jsonObject;
						for (int i = 0; i < jArray.length(); i++) {
							jsonObject = jArray.getJSONObject(i);
							Mainten_userid.add(jsonObject.getString("UserID"));
							Mainten_username.add(jsonObject
									.getString("UserName"));
						}
						length5 = Mainten_username.size();
						userload = true;
					} else if (usertype == 7) {
						JSONObject jsonObject;
						for (int i = 0; i < jArray.length(); i++) {
							jsonObject = jArray.getJSONObject(i);
							Executive_userid.add(jsonObject.getString("UserID"));
							Executive_username.add(jsonObject
									.getString("UserName"));
						}
						length6 = Executive_username.size();
						userload = true;
					}
				} else {
					if (usertype == 1) {
						if (!Upper_Manager_userid.isEmpty()) {
							Upper_Manager_userid.clear();
							Upper_Manager_username.clear();
							userload = false;
						}
						// usertype=2;
						// new GetUsers().execute();
					} else if (usertype == 2) {
						if (!Manager_userid.isEmpty()) {
							Manager_userid.clear();
							Manager_username.clear();
							userload = false;
						}
						// usertype=3;
						// new GetUsers().execute();
					} else if (usertype == 3) {
						if (!Staff_userid.isEmpty()) {
							Staff_userid.clear();
							Staff_username.clear();
							userload = false;
						}
						// usertype=4;
						// new GetUsers().execute();
					} else if (usertype == 4) {
						if (!Inst_userid.isEmpty()) {
							Inst_userid.clear();
							Inst_username.clear();
							userload = false;
						}
						// usertype=5;
						// new GetUsers().execute();
					} else if (usertype == 5) {
						if (!Supervisor_userid.isEmpty()) {
							Supervisor_userid.clear();
							Supervisor_username.clear();
							userload = false;
						}
						// usertype=6;
						// new GetUsers().execute();
					} else if (usertype == 6) {
						if (!Mainten_userid.isEmpty()) {
							Mainten_userid.clear();
							Mainten_username.clear();
							userload = false;
						}
					} else if (usertype == 7) {
						if (!Executive_userid.isEmpty()) {
							Executive_userid.clear();
							Executive_username.clear();
							userload = false;
						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
				server_response = true;
			} catch (Exception e) {
				server_response = true;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (server_response) {
				onDetectNetworkState().show();
				server_response = false;
			} else {
				if (userload) {
					Log.e(TAG, "User type = " + usertype);
					if (usertype == 1) {
						userload = false;
						adapterUpperManager = new ArrayAdapter<String>(
								ToAndCcSelectionActivity.this,
								android.R.layout.simple_list_item_multiple_choice,
								Upper_Manager_username);
						list_upper_manager_list.setAdapter(adapterUpperManager);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							public void run() {
								if (TO_CC_CLICK == 0) {
									if (temp_um_id_to.size() > 0) {
										if (temp_um_id_to.contains("-1")) {
											for (int i = 0; i < Upper_Manager_username
													.size(); i++) {
												list_upper_manager_list
														.setItemChecked(i, true);
											}
										} else {
											for (int i = 0; i < temp_um_id_to
													.size(); i++) {
												String id = temp_um_id_to
														.get(i);
												int pos = Upper_Manager_userid
														.indexOf(id);
												list_upper_manager_list
														.setItemChecked(pos,
																true);
											}
										}
									}
								} else {
									if (temp_um_id_cc.size() > 0) {
										if (temp_um_id_cc.contains("-1")) {
											for (int i = 0; i < Upper_Manager_username
													.size(); i++) {
												list_upper_manager_list
														.setItemChecked(i, true);
											}
										} else {
											for (int i = 0; i < temp_um_id_cc
													.size(); i++) {
												String id = temp_um_id_cc
														.get(i);
												int pos = Upper_Manager_userid
														.indexOf(id);
												list_upper_manager_list
														.setItemChecked(pos,
																true);
											}
										}
									}
								}
							}
						}, 2000);

						usertype = 2;
						new GetUsers().execute();
					} else if (usertype == 2) {
						userload = false;
						adapterManager = new ArrayAdapter<String>(
								ToAndCcSelectionActivity.this,
								android.R.layout.simple_list_item_multiple_choice,
								Manager_username);
						list_manager_list.setAdapter(adapterManager);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							public void run() {
								if (TO_CC_CLICK == 0) {
									if (temp_mng_id_to.size() > 0) {
										if (temp_mng_id_to.contains("-1")) {
											for (int i = 0; i < Manager_username
													.size(); i++) {
												list_manager_list
														.setItemChecked(i, true);
											}
										} else {
											for (int i = 0; i < temp_mng_id_to
													.size(); i++) {
												String id = temp_mng_id_to
														.get(i);
												int pos = Manager_userid
														.indexOf(id);
												list_manager_list
														.setItemChecked(pos,
																true);
											}
										}
									}
								} else {
									if (temp_mng_id_cc.size() > 0) {
										if (temp_mng_id_cc.contains("-1")) {
											for (int i = 0; i < Manager_username
													.size(); i++) {
												list_manager_list
														.setItemChecked(i, true);
											}
										} else {
											for (int i = 0; i < temp_mng_id_cc
													.size(); i++) {
												String id = temp_mng_id_cc
														.get(i);
												int pos = Manager_userid
														.indexOf(id);
												list_manager_list
														.setItemChecked(pos,
																true);
											}
										}
									}
								}
							}
						}, 2000);

						usertype = 3;
						new GetUsers().execute();
					} else if (usertype == 3) {
						userload = false;
						adapterOfficeStaff = new ArrayAdapter<String>(
								ToAndCcSelectionActivity.this,
								android.R.layout.simple_list_item_multiple_choice,
								Staff_username);
						list_staff_list.setAdapter(adapterOfficeStaff);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							public void run() {
								if (TO_CC_CLICK == 0) {
									if (temp_office_id_to.size() > 0) {
										if (temp_office_id_to.contains("-1")) {
											for (int i = 0; i < Staff_username
													.size(); i++) {
												list_staff_list.setItemChecked(
														i, true);
											}
										} else {
											for (int i = 0; i < temp_office_id_to
													.size(); i++) {
												String id = temp_office_id_to
														.get(i);
												int pos = Staff_userid
														.indexOf(id);
												list_staff_list.setItemChecked(
														pos, true);
											}
										}
									}
								} else {
									if (temp_office_id_cc.size() > 0) {
										if (temp_office_id_cc.contains("-1")) {
											for (int i = 0; i < Staff_username
													.size(); i++) {
												list_staff_list.setItemChecked(
														i, true);
											}
										} else {
											for (int i = 0; i < temp_office_id_cc
													.size(); i++) {
												String id = temp_mng_id_cc
														.get(i);
												int pos = Staff_userid
														.indexOf(id);
												list_staff_list.setItemChecked(
														pos, true);
											}
										}
									}
								}
							}
						}, 2000);
						usertype = 4;
						new GetUsers().execute();
					} else if (usertype == 4) {
						userload = false;
						adapterInstructor = new ArrayAdapter<String>(
								ToAndCcSelectionActivity.this,
								android.R.layout.simple_list_item_multiple_choice,
								Inst_username);
						list_inst_list.setAdapter(adapterInstructor);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							public void run() {
								if (TO_CC_CLICK == 0) {
									if (temp_inst_id_to.size() > 0) {
										if (temp_inst_id_to.contains("-1")) {
											for (int i = 0; i < Inst_username
													.size(); i++) {
												list_inst_list.setItemChecked(
														i, true);
											}
										} else {
											for (int i = 0; i < temp_inst_id_to
													.size(); i++) {
												String id = temp_inst_id_to
														.get(i);
												int pos = Inst_userid
														.indexOf(id);
												list_inst_list.setItemChecked(
														pos, true);
											}
										}
									}
								} else {
									if (temp_office_id_cc.size() > 0) {
										if (temp_office_id_cc.contains("-1")) {
											for (int i = 0; i < Inst_username
													.size(); i++) {
												list_inst_list.setItemChecked(
														i, true);
											}
										} else {
											for (int i = 0; i < temp_office_id_cc
													.size(); i++) {
												String id = temp_office_id_cc
														.get(i);
												int pos = Inst_userid
														.indexOf(id);
												list_inst_list.setItemChecked(
														pos, true);
											}
										}
									}
								}
							}
						}, 2000);
						usertype = 5;
						new GetUsers().execute();
					} else if (usertype == 5) {
						userload = false;
						adapterSupervisor = new ArrayAdapter<String>(
								ToAndCcSelectionActivity.this,
								android.R.layout.simple_list_item_multiple_choice,
								Supervisor_username);
						list_supervisor_list.setAdapter(adapterSupervisor);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							public void run() {
								if (TO_CC_CLICK == 0) {
									if (temp_super_id_to.size() > 0) {
										if (temp_super_id_to.contains("-1")) {
											for (int i = 0; i < Supervisor_username
													.size(); i++) {
												list_supervisor_list
														.setItemChecked(i, true);
											}
										} else {
											for (int i = 0; i < temp_super_id_to
													.size(); i++) {
												String id = temp_super_id_to
														.get(i);
												int pos = Supervisor_userid
														.indexOf(id);
												list_supervisor_list
														.setItemChecked(pos,
																true);
											}
										}
									}
								} else {
									if (temp_super_id_cc.size() > 0) {
										if (temp_super_id_cc.contains("-1")) {
											for (int i = 0; i < Supervisor_userid
													.size(); i++) {
												list_supervisor_list
														.setItemChecked(i, true);
											}
										} else {
											for (int i = 0; i < temp_super_id_cc
													.size(); i++) {
												String id = temp_super_id_cc
														.get(i);
												int pos = Supervisor_userid
														.indexOf(id);
												list_supervisor_list
														.setItemChecked(pos,
																true);
											}
										}
									}
								}
							}
						}, 2000);
						usertype = 6;
						new GetUsers().execute();
					} else if (usertype == 6) {
						userload = false;
						adapterMaintenance = new ArrayAdapter<String>(
								ToAndCcSelectionActivity.this,
								android.R.layout.simple_list_item_multiple_choice,
								Mainten_username);
						list_maintenance_list.setAdapter(adapterMaintenance);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							public void run() {
								if (TO_CC_CLICK == 0) {
									if (temp_main_id_to.size() > 0) {
										if (temp_main_id_to.contains("-1")) {
											for (int i = 0; i < Mainten_username
													.size(); i++) {
												list_maintenance_list
														.setItemChecked(i, true);
											}
										} else {
											for (int i = 0; i < temp_main_id_to
													.size(); i++) {
												String id = temp_main_id_to
														.get(i);
												int pos = Mainten_username
														.indexOf(id);
												list_maintenance_list
														.setItemChecked(pos,
																true);
											}
										}
									}
								} else {
									if (temp_main_id_cc.size() > 0) {
										if (temp_main_id_cc.contains("-1")) {
											for (int i = 0; i < Mainten_userid
													.size(); i++) {
												list_maintenance_list
														.setItemChecked(i, true);
											}
										} else {
											for (int i = 0; i < temp_main_id_cc
													.size(); i++) {
												String id = temp_main_id_cc
														.get(i);
												int pos = Mainten_userid
														.indexOf(id);
												list_maintenance_list
														.setItemChecked(pos,
																true);
											}
										}
									}
								}
							}
						}, 2000);
						usertype = 7;
						new GetUsers().execute();
					} else if (usertype == 7) {
						userload = false;
						adapterExecutive = new ArrayAdapter<String>(
								ToAndCcSelectionActivity.this,
								android.R.layout.simple_list_item_multiple_choice,
								Executive_username);
						list_executive_list.setAdapter(adapterExecutive);
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							public void run() {
								if (TO_CC_CLICK == 0) {
									if (temp_exe_id_to.size() > 0) {
										if (temp_exe_id_to.contains("-1")) {
											for (int i = 0; i < Executive_username
													.size(); i++) {
												list_executive_list
														.setItemChecked(i, true);
											}
										} else {
											for (int i = 0; i < temp_exe_id_to
													.size(); i++) {
												String id = temp_exe_id_to
														.get(i);
												int pos = Executive_userid
														.indexOf(id);
												list_executive_list
														.setItemChecked(pos,
																true);
											}
										}
									}
								} else {
									if (temp_exe_id_cc.size() > 0) {
										if (temp_exe_id_cc.contains("-1")) {
											for (int i = 0; i < Executive_userid
													.size(); i++) {
												list_executive_list
														.setItemChecked(i, true);
											}
										} else {
											for (int i = 0; i < temp_exe_id_cc
													.size(); i++) {
												String id = temp_exe_id_cc
														.get(i);
												int pos = Executive_userid
														.indexOf(id);
												list_executive_list
														.setItemChecked(pos,
																true);
											}
										}
									}
								}
							}
						}, 2000);
						Handler handler2 = new Handler();
						handler2.postDelayed(new Runnable() {

							public void run() {
								toandccselection_loading
										.setVisibility(View.GONE);
							}
						}, 5000);
					}
				} else {
					if (usertype == 1) {
						usertype = 2;
						new GetUsers().execute();
					} else if (usertype == 2) {
						usertype = 3;
						new GetUsers().execute();
					} else if (usertype == 3) {
						usertype = 4;
						new GetUsers().execute();
					} else if (usertype == 4) {
						usertype = 5;
						new GetUsers().execute();
					} else if (usertype == 5) {
						usertype = 6;
						new GetUsers().execute();
					} else if (usertype == 6) {
						usertype = 7;
						new GetUsers().execute();
					} else if(usertype == 7){
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {

							public void run() {
								toandccselection_loading
										.setVisibility(View.GONE);
							}
						}, 5000);
					}

				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_and_cc_selection, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		if (isInternetPresent) {
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				overridePendingTransition(R.anim.slide_in_left,
						R.anim.slide_out_right);
				break;
			}
		} else {
			onDetectNetworkState().show();
		}
		return super.onOptionsItemSelected(item);
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
				.isNetworkConnected(ToAndCcSelectionActivity.this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {


							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								ToAndCcSelectionActivity.this.finish();
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


	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (isInternetPresent) {
			switch (v.getId()) {
			case R.id.to_cc_upper_managers:
				list_executive_list.setVisibility(View.GONE);
				list_inst_list.setVisibility(View.GONE);
				list_maintenance_list.setVisibility(View.GONE);
				list_manager_list.setVisibility(View.GONE);
				list_staff_list.setVisibility(View.GONE);
				list_supervisor_list.setVisibility(View.GONE);
				if (!Upper_Manager_username.isEmpty()) {
					if (list_upper_manager_list.getVisibility() == View.VISIBLE) {
						list_upper_manager_list.setVisibility(View.GONE);
					} else {
						list_upper_manager_list.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(ToAndCcSelectionActivity.this,
							"No user found", Toast.LENGTH_LONG).show();
				}

				break;
			case R.id.to_cc_managers:
				list_executive_list.setVisibility(View.GONE);
				list_upper_manager_list.setVisibility(View.GONE);
				list_inst_list.setVisibility(View.GONE);
				list_maintenance_list.setVisibility(View.GONE);
				list_staff_list.setVisibility(View.GONE);
				list_supervisor_list.setVisibility(View.GONE);
				if (!Manager_username.isEmpty()) {
					if (list_manager_list.getVisibility() == View.VISIBLE) {
						list_manager_list.setVisibility(View.GONE);
					} else {
						list_manager_list.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(ToAndCcSelectionActivity.this,
							"No user found", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.to_cc_instructors:
				list_executive_list.setVisibility(View.GONE);
				list_upper_manager_list.setVisibility(View.GONE);
				list_maintenance_list.setVisibility(View.GONE);
				list_manager_list.setVisibility(View.GONE);
				list_staff_list.setVisibility(View.GONE);
				list_supervisor_list.setVisibility(View.GONE);
				if (!Inst_username.isEmpty()) {
					if (list_inst_list.getVisibility() == View.VISIBLE) {
						list_inst_list.setVisibility(View.GONE);
					} else {

						list_inst_list.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(ToAndCcSelectionActivity.this,
							"No user found", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.to_cc_office_staff:
				list_executive_list.setVisibility(View.GONE);
				list_upper_manager_list.setVisibility(View.GONE);
				list_inst_list.setVisibility(View.GONE);
				list_maintenance_list.setVisibility(View.GONE);
				list_manager_list.setVisibility(View.GONE);
				list_supervisor_list.setVisibility(View.GONE);
				if (!Staff_username.isEmpty()) {
					if (list_staff_list.getVisibility() == View.VISIBLE) {
						list_staff_list.setVisibility(View.GONE);
					} else {
						list_staff_list.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(ToAndCcSelectionActivity.this,
							"No user found", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.to_cc_deck_supervisor:
				list_executive_list.setVisibility(View.GONE);
				list_upper_manager_list.setVisibility(View.GONE);
				list_inst_list.setVisibility(View.GONE);
				list_maintenance_list.setVisibility(View.GONE);
				list_manager_list.setVisibility(View.GONE);
				list_staff_list.setVisibility(View.GONE);
				if (!Supervisor_username.isEmpty()) {
					if (list_supervisor_list.getVisibility() == View.VISIBLE) {
						list_supervisor_list.setVisibility(View.GONE);
					} else {
						list_supervisor_list.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(ToAndCcSelectionActivity.this,
							"No user found", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.to_cc_maintenance:
				list_executive_list.setVisibility(View.GONE);
				list_upper_manager_list.setVisibility(View.GONE);
				list_inst_list.setVisibility(View.GONE);
				list_supervisor_list.setVisibility(View.GONE);
				list_manager_list.setVisibility(View.GONE);
				list_staff_list.setVisibility(View.GONE);
				if (!Mainten_username.isEmpty()) {
					if (list_maintenance_list.getVisibility() == View.VISIBLE) {
						list_maintenance_list.setVisibility(View.GONE);
					} else {
						list_maintenance_list.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(ToAndCcSelectionActivity.this,
							"No user found", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.to_cc_executive:
				list_upper_manager_list.setVisibility(View.GONE);
				list_inst_list.setVisibility(View.GONE);
				list_supervisor_list.setVisibility(View.GONE);
				list_manager_list.setVisibility(View.GONE);
				list_staff_list.setVisibility(View.GONE);
				list_maintenance_list.setVisibility(View.GONE);
				if (!Executive_username.isEmpty()) {
					if (list_executive_list.getVisibility() == View.VISIBLE) {
						list_executive_list.setVisibility(View.GONE);
					} else {
						list_executive_list.setVisibility(View.VISIBLE);
					}
				} else {
					Toast.makeText(ToAndCcSelectionActivity.this,
							"No user found", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.btn_to_cc_cancel:
				finish();
				break;
			case R.id.btn_to_cc_ok:
				
				// //////////////////////////////
				WW_StaticClass.tempSiteId = "" + siteid;
					SparseBooleanArray checkedEX = list_executive_list
							.getCheckedItemPositions();
					for (int i = 0; i < checkedEX.size(); i++) {
						// Item position in adapter
						int position = checkedEX.keyAt(i);
						// Add sport if it is checked i.e.) == TRUE!
						if (checkedEX.valueAt(i)) {
							mExecutive_name.add(adapterExecutive
									.getItem(position));
							mExecutive_id.add(Executive_userid
									.get(position));
							if (TO_CC_CLICK == 0) {
								temp_exe_id_to.add(Executive_userid
										.get(position));
								CreateNewMessageActivity
										.setTemp_exe_id_to(temp_exe_id_to);
							} else {
								temp_exe_id_cc.add(Executive_userid
										.get(position));
								CreateNewMessageActivity
										.setTemp_exe_id_cc(temp_exe_id_cc);
							}
						}
					}
				SparseBooleanArray checkedUM = list_upper_manager_list
						.getCheckedItemPositions();
				for (int i = 0; i < checkedUM.size(); i++) {
					// Item position in adapter
					int position = checkedUM.keyAt(i);
					// Add sport if it is checked i.e.) == TRUE!
					if (checkedUM.valueAt(i)) {
						mUpper_manager_name.add(adapterUpperManager
								.getItem(position));
						mUpper_manager_id.add(Upper_Manager_userid
								.get(position));
						if (TO_CC_CLICK == 0) {
							temp_um_id_to.add(Upper_Manager_userid
									.get(position));
							CreateNewMessageActivity
									.setTemp_um_id_to(temp_um_id_to);
						} else {
							temp_um_id_cc.add(Upper_Manager_userid
									.get(position));
							CreateNewMessageActivity
									.setTemp_um_id_cc(temp_um_id_cc);
						}
					}
				}
				SparseBooleanArray checkedM = list_manager_list
						.getCheckedItemPositions();
				for (int i = 0; i < checkedM.size(); i++) {
					// Item position in adapter
					int position = checkedM.keyAt(i);
					// Add sport if it is checked i.e.) == TRUE!
					if (checkedM.valueAt(i)) {
						mManager_name.add(adapterManager.getItem(position));
						mManager_id.add(Manager_userid.get(position));
						if (TO_CC_CLICK == 0) {
							temp_mng_id_to.add(Manager_userid.get(position));
							CreateNewMessageActivity
									.setTemp_mng_id_to(temp_mng_id_to);
						} else {
							temp_mng_id_cc.add(Manager_userid.get(position));
							CreateNewMessageActivity
									.setTemp_mng_id_cc(temp_mng_id_cc);
						}
					}
				}
				SparseBooleanArray checkedO = list_staff_list
						.getCheckedItemPositions();
				for (int i = 0; i < checkedO.size(); i++) {
					// Item position in adapter
					int position = checkedO.keyAt(i);
					// Add sport if it is checked i.e.) == TRUE!
					if (checkedO.valueAt(i)) {
						mStaff_name.add(adapterOfficeStaff.getItem(position));
						mStaff_id.add(Staff_userid.get(position));
						if (TO_CC_CLICK == 0) {
							temp_office_id_to.add(Staff_userid.get(position));
							CreateNewMessageActivity
									.setTemp_office_id_to(temp_office_id_to);
						} else {
							temp_office_id_cc.add(Staff_userid.get(position));
							CreateNewMessageActivity
									.setTemp_office_id_cc(temp_office_id_cc);
						}
					}
				}
				SparseBooleanArray checkedI = list_inst_list
						.getCheckedItemPositions();
				for (int i = 0; i < checkedI.size(); i++) {
					// Item position in adapter
					int position = checkedI.keyAt(i);
					// Add sport if it is checked i.e.) == TRUE!
					if (checkedI.valueAt(i)) {
						mInst_name.add(adapterInstructor.getItem(position));
						mInst_id.add(Inst_userid.get(position));
						if (TO_CC_CLICK == 0) {
							temp_inst_id_to.add(Inst_userid.get(position));
							CreateNewMessageActivity
									.setTemp_inst_id_to(temp_inst_id_to);
						} else {
							temp_inst_id_cc.add(Inst_userid.get(position));
							CreateNewMessageActivity
									.setTemp_inst_id_cc(temp_inst_id_cc);
						}
					}
				}
				SparseBooleanArray checkedSU = list_supervisor_list
						.getCheckedItemPositions();
				for (int i = 0; i < checkedSU.size(); i++) {
					// Item position in adapter
					int position = checkedSU.keyAt(i);
					// Add sport if it is checked i.e.) == TRUE!
					if (checkedSU.valueAt(i)) {
						mSupervisor_name.add(adapterSupervisor
								.getItem(position));
						mSupervisor_id.add(Supervisor_userid.get(position));
						if (TO_CC_CLICK == 0) {
							temp_super_id_to.add(Supervisor_userid
									.get(position));
							CreateNewMessageActivity
									.setTemp_super_id_to(temp_super_id_to);
						} else {
							temp_super_id_cc.add(Supervisor_userid
									.get(position));
							CreateNewMessageActivity
									.setTemp_super_id_cc(temp_super_id_cc);
						}
					}
				}
				SparseBooleanArray checkedMN = list_maintenance_list
						.getCheckedItemPositions();
				for (int i = 0; i < checkedMN.size(); i++) {
					// Item position in adapter
					int position = checkedMN.keyAt(i);
					// Add sport if it is checked i.e.) == TRUE!
					if (checkedMN.valueAt(i)) {
						mMainten_name.add(adapterMaintenance.getItem(position));
						mMainten_id.add(Mainten_userid.get(position));
						if (TO_CC_CLICK == 0) {
							temp_main_id_to.add(Mainten_userid.get(position));
							CreateNewMessageActivity
									.setTemp_main_id_to(temp_main_id_to);
						} else {
							temp_main_id_cc.add(Mainten_userid.get(position));
							CreateNewMessageActivity
									.setTemp_main_id_cc(temp_main_id_cc);
						}
					}
				}

				// String final_string="";
				
				if (mExecutive_name.contains("All")) {
					// final_string = final_string+"Upper Manager(All)";
					if (TO_CC_CLICK == 0) {
						CreateNewMessageActivity.addTo_name(sitename
								+ "-Executive(All)");
					} else {
						CreateNewMessageActivity.addCc_name(sitename
								+ "-Executive(All)");
					}

				} else {
					if (TO_CC_CLICK == 0) {
						for (int i = 0; i < mExecutive_name.size(); i++) {
							CreateNewMessageActivity
									.addTo_name(mExecutive_name.get(i)
											.toString());
							// if(final_string.equalsIgnoreCase("")){
							// final_string= final_string+
							// mUpper_manager_name.get(i);
							// }
							// else{
							// final_string = final_string+", "+
							// mUpper_manager_name.get(i);
							// }
						}
					} else {
						for (int i = 0; i < mExecutive_name.size(); i++) {
							CreateNewMessageActivity
									.addCc_name(mExecutive_name.get(i)
											.toString());
						}
					}
				}
				if (mUpper_manager_name.contains("All")) {
					// final_string = final_string+"Upper Manager(All)";
					if (TO_CC_CLICK == 0) {
						CreateNewMessageActivity.addTo_name(sitename
								+ "-Upper Manager(All)");
					} else {
						CreateNewMessageActivity.addCc_name(sitename
								+ "-Upper Manager(All)");
					}

				} else {
					if (TO_CC_CLICK == 0) {
						for (int i = 0; i < mUpper_manager_name.size(); i++) {
							CreateNewMessageActivity
									.addTo_name(mUpper_manager_name.get(i)
											.toString());
							// if(final_string.equalsIgnoreCase("")){
							// final_string= final_string+
							// mUpper_manager_name.get(i);
							// }
							// else{
							// final_string = final_string+", "+
							// mUpper_manager_name.get(i);
							// }
						}
					} else {
						for (int i = 0; i < mUpper_manager_name.size(); i++) {
							CreateNewMessageActivity
									.addCc_name(mUpper_manager_name.get(i)
											.toString());
						}
					}
				}
				if (mManager_name.contains("All")) {
					// final_string = final_string+", "+"Manager(All)";
					if (TO_CC_CLICK == 0) {
						CreateNewMessageActivity.addTo_name(sitename
								+ "-Manager(All)");
					} else {
						CreateNewMessageActivity.addCc_name(sitename
								+ "-Manager (All)");
					}
					for (int i = 1; i < mManager_id.size(); i++) {
						if (TO_CC_CLICK == 0) {
							// TO_ID.add(mManager_id.get(i));
						} else if (TO_CC_CLICK == 1) {
							// CC_ID.add(mManager_id.get(i));
						}
					}
				} else {
					if (TO_CC_CLICK == 0) {
						for (int i = 0; i < mManager_name.size(); i++) {
							CreateNewMessageActivity.addTo_name(mManager_name
									.get(i).toString());
							// if(final_string.equalsIgnoreCase("")){
							// final_string = final_string +
							// mManager_name.get(i);
							// }
							// else{
							// final_string = final_string+", "+
							// mManager_name.get(i);
							// }
						}
					} else {
						for (int i = 0; i < mManager_name.size(); i++) {
							CreateNewMessageActivity.addCc_name(mManager_name
									.get(i).toString());
						}
					}
				}
				if (mStaff_name.contains("All")) {
					if (TO_CC_CLICK == 0) {
						// final_string =final_string+", "+
						// "Office Staff (All)";
						CreateNewMessageActivity.addTo_name(sitename
								+ "-Office Staff (All)");
					} else {
						CreateNewMessageActivity.addCc_name(sitename
								+ "-Office Staff (All)");
					}
				} else {
					if (TO_CC_CLICK == 0) {
						for (int i = 0; i < mStaff_name.size(); i++) {
							CreateNewMessageActivity.addTo_name(mStaff_name
									.get(i).toString());
							// if(final_string.equalsIgnoreCase("")){
							// final_string= final_string+ mStaff_name.get(i);
							// }
							// else{
							// final_string = final_string+", "+
							// mStaff_name.get(i);
							// }
						}
					} else {
						for (int i = 0; i < mStaff_name.size(); i++) {
							CreateNewMessageActivity.addCc_name(mStaff_name
									.get(i).toString());
						}
					}
				}
				if (mInst_name.contains("All")) {
					if (TO_CC_CLICK == 0) {
						// final_string= final_string +", " +"Instructor (All)";
						CreateNewMessageActivity.addTo_name(sitename
								+ "-Instructor (All)");
					} else {
						CreateNewMessageActivity.addCc_name(sitename
								+ "-Instructor (All)");
					}
				} else {
					if (TO_CC_CLICK == 0) {
						for (int i = 0; i < mInst_name.size(); i++) {
							CreateNewMessageActivity.addTo_name(mInst_name.get(
									i).toString());
							// if(final_string.equalsIgnoreCase("")){
							// final_string= final_string+ mInst_name.get(i);
							// }
							// else{
							// final_string = final_string+", "+
							// mInst_name.get(i);
							// }
						}
					} else {
						for (int i = 0; i < mInst_name.size(); i++) {
							CreateNewMessageActivity.addTo_name(mInst_name.get(
									i).toString());
						}
					}
				}
				if (mSupervisor_name.contains("All")) {
					if (TO_CC_CLICK == 0) {
						// final_string= final_string + ", " +
						// "Desk Supervisor (All)";
						CreateNewMessageActivity.addTo_name(sitename
								+ "-Desk Supervisor (All)");
					} else {
						CreateNewMessageActivity.addCc_name(sitename
								+ "-Desk Supervisor (All)");
					}
				} else {
					if (TO_CC_CLICK == 0) {
						for (int i = 0; i < mSupervisor_name.size(); i++) {
							CreateNewMessageActivity
									.addTo_name(mSupervisor_name.get(i)
											.toString());
							// if(final_string.equalsIgnoreCase("")){
							// final_string= final_string+
							// mSupervisor_name.get(i);
							// }
							// else{
							// final_string= final_string+", "+
							// mSupervisor_name.get(i);
							// }
						}
					} else {
						for (int i = 0; i < mSupervisor_name.size(); i++) {
							CreateNewMessageActivity
									.addTo_name(mSupervisor_name.get(i)
											.toString());
						}
					}
				}
				if (mMainten_name.contains("All")) {
					if (TO_CC_CLICK == 0) {
						CreateNewMessageActivity.addTo_name(sitename
								+ "-Maintenance (All)");
					} else {
						CreateNewMessageActivity.addCc_name(sitename
								+ "-Maintenance (All)");
					}
					// final_string=final_string+", "+ "Maintenance (All)";
				} else {
					if (TO_CC_CLICK == 0) {
						for (int i = 0; i < mMainten_name.size(); i++) {
							CreateNewMessageActivity.addTo_name(mMainten_name
									.get(i).toString());
							// if(final_string.equalsIgnoreCase("")){
							// final_string= final_string+ mMainten_name.get(i);
							// }
							// else{
							// final_string = final_string+", "+
							// mMainten_name.get(i);
							// }
						}
					} else {
						for (int i = 0; i < mMainten_name.size(); i++) {
							CreateNewMessageActivity.addCc_name(mMainten_name
									.get(i).toString());
						}
					}
				}
				// try{
				// String temp_first =",";
				// char first = temp_first.charAt(0);
				// if(final_string.charAt(0)==first){
				// final_string = final_string.substring(1);
				// }
				// }
				// catch(IndexOutOfBoundsException e){
				// e.printStackTrace();
				// }
				// catch (Exception e) {
				// // TODO: handle exception
				// e.printStackTrace();
				// }
				if (TO_CC_CLICK == 0) {
					CreateNewMessageActivity.setToField();
					
					// if(mUpper_manager_id.contains("-1")){
					// mUpper_manager_id.remove("-1");
					// TO_ID.addAll(mUpper_manager_id);
					// }
					// else{
					TO_ID.addAll(mExecutive_id);
					TO_NAME.addAll(mExecutive_name);
					TO_ID.addAll(mUpper_manager_id);
					TO_NAME.addAll(mUpper_manager_name);
					// }
					// if(mManager_id.contains("-1")){
					// // mManager_id.remove("-1");
					// TO_ID.addAll(mManager_id);
					// }
					// else{
					TO_ID.addAll(mManager_id);
					TO_NAME.addAll(mManager_name);
					// }
					// if(mStaff_id.contains("-1")){
					// // mStaff_id.remove("-1");
					// TO_ID.addAll(mStaff_id);
					// }
					// else{
					TO_ID.addAll(mStaff_id);
					TO_NAME.addAll(mStaff_name);
					// }
					// if(mInst_id.contains("-1")){
					// // mInst_id.remove("-1");
					// TO_ID.addAll(mInst_id);
					// }
					// else{
					TO_ID.addAll(mInst_id);
					TO_NAME.addAll(mInst_name);
					// }
					// if(mSupervisor_id.contains("-1")){
					// // mSupervisor_id.remove("-1");
					// TO_ID.addAll(mSupervisor_id);
					// }
					// else{
					TO_ID.addAll(mSupervisor_id);
					TO_NAME.addAll(mSupervisor_name);
					// }
					// if(mMainten_id.contains("-1")){
					// // mMainten_id.remove("-1");
					// TO_ID.addAll(mMainten_id);
					// }
					// else{
					TO_ID.addAll(mMainten_id);
					TO_NAME.addAll(mMainten_name);
					// }
					CreateNewMessageActivity.SetToID(TO_ID);
					CreateNewMessageActivity.setTo_name(TO_NAME);
				} else if (TO_CC_CLICK == 1) {
					CreateNewMessageActivity.setCcField();

					// if(mUpper_manager_id.contains("-1")){
					// // mUpper_manager_id.remove("-1");
					// CC_ID.addAll(mUpper_manager_id);
					// }
					// else{
					CC_ID.addAll(mExecutive_id);
					CC_NAME.addAll(mExecutive_name);
					CC_ID.addAll(mUpper_manager_id);
					CC_NAME.addAll(mUpper_manager_name);
					// }
					// if(mManager_id.contains("-1")){
					// // mManager_id.remove("-1");
					// CC_ID.addAll(mManager_id);
					// }
					// else{
					CC_ID.addAll(mManager_id);
					CC_NAME.addAll(mManager_name);
					// }
					// if(mStaff_id.contains("-1")){
					// // mStaff_id.remove("-1");
					// CC_ID.addAll(mStaff_id);
					// }
					// else{
					CC_ID.addAll(mStaff_id);
					CC_NAME.addAll(mStaff_name);
					// }
					// if(mInst_id.contains("-1")){
					// // mInst_id.remove("-1");
					// CC_ID.addAll(mInst_id);
					// }
					// else{
					CC_ID.addAll(mInst_id);
					CC_NAME.addAll(mInst_name);
					// }
					// if(mSupervisor_id.contains("-1")){
					// // mSupervisor_id.remove("-1");
					// CC_ID.addAll(mSupervisor_id);
					// }
					// else{
					CC_ID.addAll(mSupervisor_id);
					CC_NAME.addAll(mSupervisor_name);
					// }
					// if(mMainten_id.contains("-1")){
					// // mMainten_id.remove("-1");
					// CC_ID.addAll(mMainten_id);
					// }
					// else{
					CC_ID.addAll(mMainten_id);
					CC_NAME.addAll(mMainten_name);
					// }
					CreateNewMessageActivity.SetCcID(CC_ID);
					CreateNewMessageActivity.setCc_name(CC_NAME);
				}
				finish();
				// ///////////////////////////////
				break;
			default:
				break;
			}
		} else {
			onDetectNetworkState().show();
		}
	}

}
