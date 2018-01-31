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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
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

import water.works.waterworks.Swipe.SwipeListView;
import water.works.waterworks.adapter.InstructorViewCurrentScheduleAdapter;
import water.works.waterworks.model.InstructorViewCurrentScheduleAdapterItem;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ViewCurrentScheduleFragment extends Fragment implements
        AnimationListener {
    public static final String TAG = "View Current Schedule Fragment";
    View rootView;
    boolean server_response = false, connectionout = false, getlevel = false,
            getISA = false;
    SwipeListView swipelistview;
    Boolean isInternetPresent = false;
    float width;
    public static Button btn_send_att;
    TextView tv_view_current_lesson_error;
    String hour_for_data, min_for_data, date_for_data;
    int wu_avail = 2;

    public static ArrayList<Integer> newatt = new ArrayList<Integer>();
    public static ArrayList<Integer> oldatt = new ArrayList<Integer>();
    public ArrayList<String> LevelName = new ArrayList<String>();
    public ArrayList<String> LevelID = new ArrayList<String>();
    public ArrayList<String> ExistSwimComp = new ArrayList<String>();
    public ArrayList<String> IsShowSmCampStatus = new ArrayList<String>();
    public ArrayList<String> att = new ArrayList<String>();
    public ArrayList<String> wu_attendancetaken = new ArrayList<String>();
    public ArrayList<String> ISAAlert = new ArrayList<String>();
    ArrayList<String> Cls_Lvl = new ArrayList<String>();
    ArrayList<String> SiteID = new ArrayList<String>();
    ArrayList<String> IsEmpty = new ArrayList<String>();
    ArrayList<String> CheckValue = new ArrayList<String>();
    ArrayList<String> FormatStTimeHour = new ArrayList<String>();
    ArrayList<String> FormatStTimeMin = new ArrayList<String>();
    public ArrayList<String> SScheduleID = new ArrayList<String>();
    public ArrayList<String> IScheduleID = new ArrayList<String>();
    public ArrayList<String> InstructorID = new ArrayList<String>();
    public ArrayList<String> lessontypeid = new ArrayList<String>();
    public ArrayList<String> SAge = new ArrayList<String>();
    public ArrayList<String> SLevel = new ArrayList<String>();
    public ArrayList<String> ScheLevel = new ArrayList<String>();
    public ArrayList<String> StudentID = new ArrayList<String>();
    public ArrayList<String> OrderDetailID = new ArrayList<String>();
    public ArrayList<String> StTimeHour = new ArrayList<String>();
    public ArrayList<String> StTimeMin = new ArrayList<String>();
    public ArrayList<String> SLastName = new ArrayList<String>();
    public ArrayList<String> SFirstName = new ArrayList<String>();
    public ArrayList<String> wu_count = new ArrayList<String>();
    public ArrayList<String> wu_Prev = new ArrayList<String>();
    public ArrayList<String> wu_Next = new ArrayList<String>();
    public ArrayList<String> ParentFirstName = new ArrayList<String>();
    public ArrayList<String> ParentLastName = new ArrayList<String>();
    public ArrayList<String> BirthDay = new ArrayList<String>();
    public ArrayList<String> LessonName = new ArrayList<String>();
    public ArrayList<String> LvlAdvAvail = new ArrayList<String>();
    public ArrayList<String> Comments = new ArrayList<String>();
    public ArrayList<String> wu_comments = new ArrayList<String>();
    public ArrayList<String> SwimComp = new ArrayList<String>();
    public ArrayList<String> SkillsList = new ArrayList<String>();
    public ArrayList<String> SkillsCount = new ArrayList<String>();
    public ArrayList<String> StudentGender = new ArrayList<String>();
    public ArrayList<String> NewUser = new ArrayList<String>();
    public ArrayList<String> MainScheduleDate = new ArrayList<String>();
    ArrayList<String> PaidClasses = new ArrayList<String>();
    ArrayList<String> wu_W = new ArrayList<String>();
    ArrayList<String> wu_r = new ArrayList<String>();
    ArrayList<String> wu_b = new ArrayList<String>();
    public ArrayList<ArrayList<String>> PreReqID = new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> Abbr = new ArrayList<ArrayList<String>>();
    public ArrayList<ArrayList<String>> PreReqChecked = new ArrayList<ArrayList<String>>();
    String currentDateandTime;
    ListView lv_current_schedule;
    private ArrayList<InstructorViewCurrentScheduleAdapterItem> att_Items;
    private InstructorViewCurrentScheduleAdapter adapter_Att;
    String Instructor_name, Instructor_id;
    ImageButton btn_next, btn_prev;
    // public static Thread t;
    Animation animBlink;
    public static boolean commented = false;
    TextView tv_scheduletime, tv_instructor_name, tv_lessonname;

    String mydatetime = "", tempday, tempmonth, tempyear, tempmin, temphour,
            tempampm, temp_date, temp_time_min, temp_time_hour;
    int prevcount = 0, nextcount = 0;
    Button btn_request_shadow;

    public ViewCurrentScheduleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        rootView = inflater.inflate(R.layout.fragment_currentschedule,
                container, false);
        isInternetPresent = Utility.isNetworkConnected(getActivity());
        if (isInternetPresent) {

            Initialize();
            new GetLevel().execute();
        } else {
            onDetectNetworkState().show();
        }
        return rootView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //removed by aanal 26Aug2016
        /*if (isInternetPresent) {
            if (commented) {
				ClearArray();
				commented = false;
				new GetLevel().execute();
			}
		} else {
			onDetectNetworkState().show();
		}*/
    }

    private void Initialize() {
        // TODO Auto-generated method stub
        Instructor_id = ((ViewCurrentScheduleInstructorActivity) getActivity()).inst_id;
        Instructor_name = ((ViewCurrentScheduleInstructorActivity) getActivity()).inst_name;
        tv_instructor_name = ViewCurrentScheduleInstructorActivity.tv_instructor_name;
        tv_lessonname = ViewCurrentScheduleInstructorActivity.tv_lessonname;
        tv_instructor_name.setText(Instructor_name);
        btn_request_shadow = ViewCurrentScheduleInstructorActivity.btn_view_current_lesson_request_shadow;
        tv_scheduletime = ViewCurrentScheduleInstructorActivity.tv_scheduletime;
        btn_prev = ((ViewCurrentScheduleInstructorActivity) getActivity()).btn_next;
        btn_next = ((ViewCurrentScheduleInstructorActivity) getActivity()).btn_prev;
        btn_send_att = (Button) rootView.findViewById(R.id.btn_send_att);
//		btn_send_review = (Button) rootView.findViewById(R.id.btn_send_review);
        tv_view_current_lesson_error = (TextView) rootView
                .findViewById(R.id.tv_view_current_lesson_error);
        lv_current_schedule = (ListView) rootView.findViewById(R.id.listview);
        att_Items = new ArrayList<InstructorViewCurrentScheduleAdapterItem>();
        adapter_Att = new InstructorViewCurrentScheduleAdapter(att_Items, getActivity());
        animBlink = AnimationUtils.loadAnimation(getActivity(), R.anim.blink);
        animBlink.setAnimationListener(this);
        ((ViewCurrentScheduleInstructorActivity) getActivity()).isa_main
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        ((ViewCurrentScheduleInstructorActivity) getActivity()).t.interrupt();
                        Intent i = new Intent(getActivity(),
                                DetailReportActivity.class);
                        i.putExtra("FROM", "ISA");
                        if (WW_StaticClass.UserLevel.toString()
                                .equalsIgnoreCase("3")
                                || WW_StaticClass.UserLevel.toString()
                                .equalsIgnoreCase("11")) {
                            i.putExtra(
                                    "url",
                                    SOAP_CONSTANTS.Report_Url
                                            + "InstructorAlertReport.aspx?siteid="
                                            + WW_StaticClass.siteid.toString()
                                            .replaceAll("\\[", "")
                                            .replaceAll("\\]", "")
                                            + "&wu_instructorid="
                                            + WW_StaticClass.InstructorID);
                        } else {
                            i.putExtra(
                                    "url",
                                    SOAP_CONSTANTS.Report_Url
                                            + "InstructorAlertReport.aspx?siteid="
                                            + WW_StaticClass.siteid.toString()
                                            .replaceAll("\\[", "")
                                            .replaceAll("\\]", "") + "&_instructorid="
                                            + WW_StaticClass.InstructorID);
                        }

                        startActivity(i);
                    }
                });
        btn_prev.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    /*
                     * currentDateandTime = wu_Prev.get(0).toString(); if
					 * (currentDateandTime.toString().equalsIgnoreCase("") ||
					 * currentDateandTime.isEmpty()) {
					 * SingleOptionAlertWithoutTitle.ShowAlertDialog(
					 * getActivity(), "AquaticsApp",
					 * "No more previous schedule", "Ok"); } else {
					 */
                    prevcount++;
                    nextcount--;
                    int month, year, day, min, hour;
                    String date = MainScheduleDate.get(0);
                    // ///////////////////////

                    String tempdate[] = date.toString().split("\\/");
                    month = Integer.parseInt(tempdate[0]);
                    day = Integer.parseInt(tempdate[1]);
                    year = Integer.parseInt(tempdate[2]);
                    hour = Integer.parseInt(StTimeHour.get(0));
                    min = Integer.parseInt(StTimeMin.get(0));
                    Calendar now = Calendar.getInstance();
                    now.set(Calendar.MONTH, month);
                    now.set(Calendar.DAY_OF_MONTH, day);
                    now.set(Calendar.YEAR, year);
                    now.set(Calendar.HOUR_OF_DAY, hour);
                    now.set(Calendar.MINUTE, min);
                    now.add(Calendar.MINUTE, (-20));
                    tempmonth = "" + (now.get(Calendar.MONTH));
                    tempday = "" + now.get(Calendar.DAY_OF_MONTH);
                    tempyear = "" + now.get(Calendar.YEAR);
                    temphour = "" + now.get(Calendar.HOUR_OF_DAY);
                    tempmin = "" + now.get(Calendar.MINUTE);
                    String am_pm;
                    if (Integer.parseInt(temphour) > 11) {
                        am_pm = "PM";
                    } else {
                        am_pm = "AM";
                    }
                    if (temphour.toString().equalsIgnoreCase("13")) {
                        temphour = "1";
                    } else if (temphour.toString().equalsIgnoreCase("14")) {
                        temphour = "2";
                    } else if (temphour.toString().equalsIgnoreCase("15")) {
                        temphour = "3";
                    } else if (temphour.toString().equalsIgnoreCase("16")) {
                        temphour = "4";
                    } else if (temphour.toString().equalsIgnoreCase("17")) {
                        temphour = "5";
                    } else if (temphour.toString().equalsIgnoreCase("18")) {
                        temphour = "6";
                    } else if (temphour.toString().equalsIgnoreCase("19")) {
                        temphour = "7";
                    } else if (temphour.toString().equalsIgnoreCase("20")) {
                        temphour = "8";
                    } else if (temphour.toString().equalsIgnoreCase("21")) {
                        temphour = "9";
                    } else if (temphour.toString().equalsIgnoreCase("22")) {
                        temphour = "10";
                    } else if (temphour.toString().equalsIgnoreCase("23")) {
                        temphour = "11";
                    }

                    if (tempmonth.toString().length() == 1) {
                        tempmonth = "0" + tempmonth;
                    }
                    if (tempyear.toString().length() == 1) {
                        tempyear = "0" + tempyear;
                    }
                    if (tempday.toString().length() == 1) {
                        tempday = "0" + tempday;
                    }
                    if (temphour.toString().length() == 1) {
                        temphour = "0" + temphour;
                    }
                    if (tempmin.toString().length() == 1) {
                        tempmin = "0" + tempmin;
                    }

                    temp_date = tempmonth + "/" + tempday + "/" + tempyear;
                    temp_time_hour = temphour;
                    temp_time_min = tempmin;
                    mydatetime = tempmonth + "/" + tempday + "/" + tempyear
                            + " " + temphour + ":" + tempmin + " " + am_pm;
                    Log.i(TAG, "Previous date time = " + mydatetime);
                    currentDateandTime = mydatetime;
                    tv_scheduletime.setText(temphour + ":" + tempmin + " "
                            + am_pm);
                    // /////////////////
                    ClearArray();
//                    change by megha 29-01-2018
//                    new GetCurrentSchedule().execute();
                    new GetCurrentScheduleFirstTime().execute();
					/* } */
                } catch (IndexOutOfBoundsException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            getActivity(), "AquaticsApp",
                            "No more previous schedule", "Ok");
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            getActivity(), "AquaticsApp",
                            "No more previous schedule", "Ok");
                }
            }
        });
        btn_next.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
					/*
					 * currentDateandTime = wu_Next.get(0).toString(); if
					 * (currentDateandTime.toString().equalsIgnoreCase("") ||
					 * currentDateandTime.isEmpty()) {
					 * SingleOptionAlertWithoutTitle.ShowAlertDialog(
					 * getActivity(), "AquaticsApp", "No more next schedule",
					 * "Ok"); } else {
					 */

                    nextcount++;
                    prevcount--;
                    int month, year, day, min, hour;
                    String date = MainScheduleDate.get(0);
                    // ///////////////////////

                    String tempdate[] = date.toString().split("\\/");
                    month = Integer.parseInt(tempdate[0]);
                    day = Integer.parseInt(tempdate[1]);
                    year = Integer.parseInt(tempdate[2]);
                    hour = Integer.parseInt(StTimeHour.get(0));
                    min = Integer.parseInt(StTimeMin.get(0));
                    Calendar now = Calendar.getInstance();
                    now.set(Calendar.MONTH, month);
                    now.set(Calendar.DAY_OF_MONTH, day);
                    now.set(Calendar.YEAR, year);
                    now.set(Calendar.HOUR_OF_DAY, hour);
                    now.set(Calendar.MINUTE, min);

                    now.add(Calendar.MINUTE, (20));
                    tempmonth = "" + (now.get(Calendar.MONTH));
                    tempday = "" + now.get(Calendar.DAY_OF_MONTH);
                    tempyear = "" + now.get(Calendar.YEAR);
                    temphour = "" + now.get(Calendar.HOUR_OF_DAY);
                    tempmin = "" + now.get(Calendar.MINUTE);
                    String am_pm;
                    if (Integer.parseInt(temphour) > 11) {
                        am_pm = "PM";
                    } else {
                        am_pm = "AM";
                    }
                    if (temphour.toString().equalsIgnoreCase("13")) {
                        temphour = "1";
                    } else if (temphour.toString().equalsIgnoreCase("14")) {
                        temphour = "2";
                    } else if (temphour.toString().equalsIgnoreCase("15")) {
                        temphour = "3";
                    } else if (temphour.toString().equalsIgnoreCase("16")) {
                        temphour = "4";
                    } else if (temphour.toString().equalsIgnoreCase("17")) {
                        temphour = "5";
                    } else if (temphour.toString().equalsIgnoreCase("18")) {
                        temphour = "6";
                    } else if (temphour.toString().equalsIgnoreCase("19")) {
                        temphour = "7";
                    } else if (temphour.toString().equalsIgnoreCase("20")) {
                        temphour = "8";
                    } else if (temphour.toString().equalsIgnoreCase("21")) {
                        temphour = "9";
                    } else if (temphour.toString().equalsIgnoreCase("22")) {
                        temphour = "10";
                    } else if (temphour.toString().equalsIgnoreCase("23")) {
                        temphour = "11";
                    }

                    if (tempmonth.toString().length() == 1) {
                        tempmonth = "0" + tempmonth;
                    }
                    if (tempyear.toString().length() == 1) {
                        tempyear = "0" + tempyear;
                    }
                    if (tempday.toString().length() == 1) {
                        tempday = "0" + tempday;
                    }
                    if (temphour.toString().length() == 1) {
                        temphour = "0" + temphour;
                    }
                    if (tempmin.toString().length() == 1) {
                        tempmin = "0" + tempmin;
                    }

                    temp_date = tempmonth + "/" + tempday + "/" + tempyear;
                    temp_time_hour = temphour;
                    temp_time_min = tempmin;
                    mydatetime = tempmonth + "/" + tempday + "/" + tempyear
                            + " " + temphour + ":" + tempmin + " " + am_pm;
                    Log.i(TAG, "Previous date time = " + mydatetime);
                    currentDateandTime = mydatetime;
                    tv_scheduletime.setText(temphour + ":" + tempmin + " "
                            + am_pm);
                    ClearArray();
//                    new GetCurrentSchedule().execute();
//                    change by megha 29-01-2018
                    new GetCurrentScheduleFirstTime().execute();
					/* } */
                } catch (IndexOutOfBoundsException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            getActivity(), "AquaticsApp",
                            "No more next schedule", "Ok");
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            getActivity(), "AquaticsApp",
                            "No more next schedule", "Ok");
                }
            }
        });

        btn_request_shadow.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
//				new IamInPool().execute();
            }
        });
    }


    int oldmin, newmin, oldmil, newmil, count = 1;

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
                    new GetCurrentScheduleFirstTime().execute();
                }
            }
        }
    }

    private class GetCurrentSchedule extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                pd = new ProgressDialog(getActivity());
                pd.setTitle("Please wait...");
                pd.setMessage("Loading Schedule...");
                pd.setCancelable(false);
                pd.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.GetAttendanceList_Multiple_Method);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("Rinstructorid", Instructor_id);
            request.addProperty("strRScheDate", currentDateandTime);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.GetAttendanceList_Multiple_Action,
                        envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.bodyIn;
                Log.i("here", "Result : " + response.toString());
                SoapPrimitive sp1 = (SoapPrimitive) response.getProperty(0);
                String resp = sp1.toString();
                JSONObject jo = new JSONObject(resp);
                wu_avail = jo.getInt("wu_avail");
                if (wu_avail == 0) {
                    JSONArray jArray = jo.getJSONArray("Attendance");
                    JSONArray jArray2;
                    JSONObject jsonObject, jsonObject2;
                    for (int k = 0; k < jArray.length(); k++) {
                        jsonObject = jArray.getJSONObject(k);
                        jArray2 = jsonObject.getJSONArray("Items");
                        for (int i = 0; i < jArray2.length(); i++) {
                            jsonObject2 = jArray2.getJSONObject(i);
                            StTimeHour.add(jsonObject2.getString("StTimeHour"));
                            StTimeMin.add(jsonObject2.getString("StTimeMin"));
                            MainScheduleDate.add(jsonObject2
                                    .getString("MainScheduleDate"));
                        }
                    }
                } else if (wu_avail == 1 || wu_avail == 2) {
                    JSONArray jArray = jo.getJSONArray("Attendance");
                    JSONArray jArray2, jArray3;
                    JSONObject jsonObject, jsonObject2, jsonObject3;
                    for (int k = 0; k < jArray.length(); k++) {
                        jsonObject = jArray.getJSONObject(k);
                        jArray2 = jsonObject.getJSONArray("Items");
                        for (int i = 0; i < jArray2.length(); i++) {
                            jsonObject2 = jArray2.getJSONObject(i);
                            CheckValue.add(jsonObject2.getString("CheckValue"));
                            FormatStTimeHour.add(jsonObject2.getString("FormateStTimeHour"));
                            FormatStTimeMin.add(jsonObject2.getString("FormatStTimeMin"));
                            ExistSwimComp.add(jsonObject2
                                    .getString("ExistSwimComp"));
                            IsShowSmCampStatus.add(jsonObject2
                                    .getString("IsShowSmCampStatus"));
                            att.add(jsonObject2.getString("att"));
                            wu_attendancetaken.add(jsonObject2
                                    .getString("wu_attendancetaken"));
                            ISAAlert.add(jsonObject2.getString("ISAAlert"));
                            SiteID.add(jsonObject2.getString("SiteID"));
                            SScheduleID.add(jsonObject2
                                    .getString("SScheduleID"));
                            IScheduleID.add(jsonObject2
                                    .getString("IScheduleID"));
                            InstructorID.add(jsonObject2
                                    .getString("InstructorID"));
                            lessontypeid.add(jsonObject2
                                    .getString("lessontypeid"));
                            SAge.add(jsonObject2.getString("SAge"));
                            SLevel.add(jsonObject2.getString("SLevel"));
                            ScheLevel.add(jsonObject2.getString("ScheLevel"));
                            StudentID.add(jsonObject2.getString("StudentID"));
                            OrderDetailID.add(jsonObject2
                                    .getString("OrderDetailID"));
                            PaidClasses.add(jsonObject2
                                    .getString("PaidClasses"));
                            Cls_Lvl.add(jsonObject2.getString("ClsLvl"));
                            StTimeHour.add(jsonObject2.getString("StTimeHour"));
                            StTimeMin.add(jsonObject2.getString("StTimeMin"));
                            SLastName.add(jsonObject2.getString("SLastName"));
                            SFirstName.add(jsonObject2.getString("SFirstName"));
                            wu_count.add(jsonObject2.getString("wu_count"));
                            wu_Prev.add(jsonObject2.getString("wu_Prev"));
                            wu_Next.add(jsonObject2.getString("wu_Next"));
                            ParentFirstName.add(jsonObject2
                                    .getString("ParentFirstName"));
                            ParentLastName.add(jsonObject2
                                    .getString("ParentLastName"));
                            BirthDay.add(jsonObject2.getString("BirthDay"));
                            LessonName.add(jsonObject2.getString("LessonName"));
                            LvlAdvAvail.add(jsonObject2
                                    .getString("LvlAdvAvail"));
                            Comments.add(jsonObject2.getString("Comments"));
                            wu_comments.add(jsonObject2
                                    .getString("wu_comments"));
                            SwimComp.add(jsonObject2.getString("SwimComp"));
                            MainScheduleDate.add(jsonObject2
                                    .getString("MainScheduleDate"));
                            SkillsCount.add(jsonObject2
                                    .getString("SkillsCount"));
                            StudentGender.add(jsonObject2
                                    .getString("StudentGender"));
                            NewUser.add(jsonObject2.getString("NewUser"));
                            wu_W.add(jsonObject2.getString("wu_W"));
                            wu_r.add(jsonObject2.getString("wu_r"));
                            wu_b.add(jsonObject2.getString("wu_b"));
                            if (SkillsCount.get(i).toString()
                                    .equalsIgnoreCase("0")) {
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

            try {
                pd.dismiss();
                if (server_response) {
                    server_response = false;
                    onDetectNetworkState().show();
                } else if (connectionout) {
                    connectionout = false;
//                    new GetCurrentSchedule().execute();
//                    change by megha 29-01-2018
                    new GetCurrentScheduleFirstTime().execute();
                } else {
					/*
					 * if (ISAFlag) { dayisa.setVisibility(View.VISIBLE);
					 * dayisa.startAnimation(animBlink); ISAFlag = false; } else
					 * { dayisa.setVisibility(View.INVISIBLE); }
					 */
                    if (wu_avail == 0) {
                        tv_view_current_lesson_error
                                .setVisibility(View.VISIBLE);
                        tv_view_current_lesson_error
                                .setText("No availability at this time.");
                        tv_lessonname.setText("");
                        lv_current_schedule.setVisibility(View.INVISIBLE);
                        btn_send_att.setVisibility(View.INVISIBLE);
//						btn_send_review.setVisibility(View.INVISIBLE);
                        int month, year, day, min, hour;
                        String date = MainScheduleDate.get(0);
                        // ///////////////////////

                        String tempdate[] = date.toString().split("\\/");
                        month = Integer.parseInt(tempdate[0]);
                        day = Integer.parseInt(tempdate[1]);
                        year = Integer.parseInt(tempdate[2]);
                        hour = Integer.parseInt(StTimeHour.get(0));
                        min = Integer.parseInt(StTimeMin.get(0));
                        Calendar now = Calendar.getInstance();
                        now.set(Calendar.MONTH, month);
                        now.set(Calendar.DAY_OF_MONTH, day);
                        now.set(Calendar.YEAR, year);
                        now.set(Calendar.HOUR_OF_DAY, hour);
                        now.set(Calendar.MINUTE, min);

                        now.add(Calendar.MINUTE, 0);
                        tempmonth = "" + (now.get(Calendar.MONTH));
                        tempday = "" + now.get(Calendar.DAY_OF_MONTH);
                        tempyear = "" + now.get(Calendar.YEAR);
                        temphour = "" + now.get(Calendar.HOUR_OF_DAY);
                        tempmin = "" + now.get(Calendar.MINUTE);
                        String am_pm;
                        if (Integer.parseInt(temphour) > 11) {
                            am_pm = "PM";
                        } else {
                            am_pm = "AM";
                        }
                        if (temphour.toString().equalsIgnoreCase("13")) {
                            temphour = "1";
                        } else if (temphour.toString().equalsIgnoreCase("14")) {
                            temphour = "2";
                        } else if (temphour.toString().equalsIgnoreCase("15")) {
                            temphour = "3";
                        } else if (temphour.toString().equalsIgnoreCase("16")) {
                            temphour = "4";
                        } else if (temphour.toString().equalsIgnoreCase("17")) {
                            temphour = "5";
                        } else if (temphour.toString().equalsIgnoreCase("18")) {
                            temphour = "6";
                        } else if (temphour.toString().equalsIgnoreCase("19")) {
                            temphour = "7";
                        } else if (temphour.toString().equalsIgnoreCase("20")) {
                            temphour = "8";
                        } else if (temphour.toString().equalsIgnoreCase("21")) {
                            temphour = "9";
                        } else if (temphour.toString().equalsIgnoreCase("22")) {
                            temphour = "10";
                        } else if (temphour.toString().equalsIgnoreCase("23")) {
                            temphour = "11";
                        }
                        if (temphour.toString().length() == 1) {
                            temphour = "0" + temphour;
                        }
                        if (tempmin.toString().length() == 1) {
                            tempmin = "0" + tempmin;
                        }
                        tv_scheduletime.setText(temphour + ":" + tempmin + " "
                                + am_pm);
                        // btnnext.setEnabled(false);
                        // btnprev.setEnabled(false);
                    } else {
                        if (!LessonName.isEmpty()
                                && !LessonName.get(0).equalsIgnoreCase("")) {
                            tv_view_current_lesson_error
                                    .setVisibility(View.GONE);
                            if (ViewCurrentScheduleInstructorActivity.searchClick) {
                                tv_lessonname.setText("");
                            } else {
                                tv_lessonname.setText(LessonName.get(0).toString());
                            }
                            setScheduleTime();
                            GenerateNewAndOldAtt();
                            lv_current_schedule.setVisibility(View.VISIBLE);
                            btn_send_att.setVisibility(View.VISIBLE);
//							btn_send_review.setVisibility(View.VISIBLE);
                            for (int i = 0; i < SAge.size(); i++) {
                                att_Items
                                        .add(new InstructorViewCurrentScheduleAdapterItem(
                                                wu_avail, IsEmpty.get(i), CheckValue.get(i), FormatStTimeHour.get(i), FormatStTimeMin.get(i), ExistSwimComp.get(i),
                                                IsShowSmCampStatus.get(i), att
                                                .get(i),
                                                wu_attendancetaken.get(i),
                                                ISAAlert.get(i), SScheduleID
                                                .get(i), IScheduleID
                                                .get(i), InstructorID
                                                .get(i), lessontypeid
                                                .get(i), SAge.get(i),
                                                SLevel.get(i),
                                                ScheLevel.get(i), StudentID
                                                .get(i), OrderDetailID
                                                .get(i), StTimeHour
                                                .get(i), StTimeMin
                                                .get(i), SLastName
                                                .get(i), SFirstName
                                                .get(i), wu_count
                                                .get(i),
                                                wu_Prev.get(i), wu_Next.get(i),
                                                ParentFirstName.get(i),
                                                ParentLastName.get(i), BirthDay
                                                .get(i), LessonName
                                                .get(i), LvlAdvAvail
                                                .get(i), Comments
                                                .get(i), wu_comments
                                                .get(i), SwimComp
                                                .get(i), SkillsCount
                                                .get(i), StudentGender
                                                .get(i),
                                                NewUser.get(i),
                                                PreReqID.get(i), Abbr.get(i),
                                                PreReqChecked.get(i),
                                                LevelName, LevelID,
                                                Instructor_name, Instructor_id,
                                                btn_send_att,
                                                StTimeHour.get(i), StTimeMin
                                                .get(i),
                                                MainScheduleDate.get(i), wu_W
                                                .get(i), wu_b.get(i),
                                                wu_r.get(i), SiteID.get(i),
                                                Cls_Lvl.get(i), PaidClasses
                                                .get(i)));
                            }
                            lv_current_schedule.setAdapter(adapter_Att);
                            adapter_Att.notifyDataSetChanged();
                        } else {
                            tv_view_current_lesson_error
                                    .setVisibility(View.VISIBLE);
                            tv_lessonname.setText("");
                            tv_view_current_lesson_error
                                    .setText("No lesson at this time.");
                            lv_current_schedule.setVisibility(View.INVISIBLE);
                            btn_send_att.setVisibility(View.INVISIBLE);
//							btn_send_review.setVisibility(View.INVISIBLE);
                            int month, year, day, min, hour;
                            String date = MainScheduleDate.get(0);
                            // ///////////////////////

                            String tempdate[] = date.toString().split("\\/");
                            month = Integer.parseInt(tempdate[0]);
                            day = Integer.parseInt(tempdate[1]);
                            year = Integer.parseInt(tempdate[2]);
                            hour = Integer.parseInt(StTimeHour.get(0));
                            min = Integer.parseInt(StTimeMin.get(0));
                            Calendar now = Calendar.getInstance();
                            now.set(Calendar.MONTH, month);
                            now.set(Calendar.DAY_OF_MONTH, day);
                            now.set(Calendar.YEAR, year);
                            now.set(Calendar.HOUR_OF_DAY, hour);
                            now.set(Calendar.MINUTE, min);

                            now.add(Calendar.MINUTE, 0);
                            tempmonth = "" + (now.get(Calendar.MONTH));
                            tempday = "" + now.get(Calendar.DAY_OF_MONTH);
                            tempyear = "" + now.get(Calendar.YEAR);
                            temphour = "" + now.get(Calendar.HOUR_OF_DAY);
                            tempmin = "" + now.get(Calendar.MINUTE);
                            String am_pm;
                            if (Integer.parseInt(temphour) > 11) {
                                am_pm = "PM";
                            } else {
                                am_pm = "AM";
                            }
                            if (temphour.toString().equalsIgnoreCase("13")) {
                                temphour = "1";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "14")) {
                                temphour = "2";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "15")) {
                                temphour = "3";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "16")) {
                                temphour = "4";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "17")) {
                                temphour = "5";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "18")) {
                                temphour = "6";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "19")) {
                                temphour = "7";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "20")) {
                                temphour = "8";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "21")) {
                                temphour = "9";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "22")) {
                                temphour = "10";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "23")) {
                                temphour = "11";
                            }
                            if (temphour.toString().length() == 1) {
                                temphour = "0" + temphour;
                            }
                            if (tempmin.toString().length() == 1) {
                                tempmin = "0" + tempmin;
                            }
                            tv_scheduletime.setText(temphour + ":" + tempmin
                                    + " " + am_pm);
                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    //change by megha 23-01-2018
    private class GetCurrentScheduleFirstTime extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                pd = new ProgressDialog(getActivity());
                pd.setTitle("Please wait...");
                pd.setMessage("Loading Schedule...");
                pd.setCancelable(false);
                pd.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.GetAttendanceList_Multiple_Front_Method);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("Rinstructorid", Instructor_id);
            request.addProperty("strRScheDate", currentDateandTime);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.GetAttendanceList_Multiple_Front_Action,
                        envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.bodyIn;
                Log.i("here", "Result : " + response.toString());
                SoapPrimitive sp1 = (SoapPrimitive) response.getProperty(0);
                String resp = sp1.toString();
                JSONObject jo = new JSONObject(resp);
                wu_avail = jo.getInt("wu_avail");

                if (wu_avail == 0) {
                    JSONArray jArray = jo.getJSONArray("Attendance");
                    JSONArray jArray2;
                    JSONObject jsonObject, jsonObject2;
                    for (int k = 0; k < jArray.length(); k++) {
                        jsonObject = jArray.getJSONObject(k);
                        jArray2 = jsonObject.getJSONArray("Items");
                        for (int i = 0; i < jArray2.length(); i++) {
                            jsonObject2 = jArray2.getJSONObject(i);
                            StTimeHour.add(jsonObject2.getString("StTimeHour"));
                            StTimeMin.add(jsonObject2.getString("StTimeMin"));
                            MainScheduleDate.add(jsonObject2
                                    .getString("MainScheduleDate"));
                        }

                    }
                } else if (wu_avail == 1 || wu_avail == 2) {
                    JSONArray jArray = jo.getJSONArray("Attendance");
                    JSONArray jArray2, jArray3;
                    JSONObject jsonObject, jsonObject2, jsonObject3;
                    for (int k = 0; k < jArray.length(); k++) {
                        jsonObject = jArray.getJSONObject(k);

                        IsEmpty.add(jsonObject.getString("IsEmpty"));
                        jArray2 = jsonObject.getJSONArray("Items");
                        for (int i = 0; i < jArray2.length(); i++) {
                            jsonObject2 = jArray2.getJSONObject(i);
                            CheckValue.add(jsonObject2.getString("CheckValue"));
                            FormatStTimeHour.add(jsonObject2.getString("FormateStTimeHour"));
                            FormatStTimeMin.add(jsonObject2.getString("FormatStTimeMin"));
                            ExistSwimComp.add(jsonObject2
                                    .getString("ExistSwimComp"));
                            IsShowSmCampStatus.add(jsonObject2
                                    .getString("IsShowSmCampStatus"));
                            att.add(jsonObject2.getString("att"));
                            wu_attendancetaken.add(jsonObject2
                                    .getString("wu_attendancetaken"));
                            ISAAlert.add(jsonObject2.getString("ISAAlert"));
                            SiteID.add(jsonObject2.getString("SiteID"));
                            SScheduleID.add(jsonObject2
                                    .getString("SScheduleID"));
                            IScheduleID.add(jsonObject2
                                    .getString("IScheduleID"));
                            InstructorID.add(jsonObject2
                                    .getString("InstructorID"));
                            lessontypeid.add(jsonObject2
                                    .getString("lessontypeid"));
                            SAge.add(jsonObject2.getString("SAge"));
                            SLevel.add(jsonObject2.getString("SLevel"));
                            ScheLevel.add(jsonObject2.getString("ScheLevel"));
                            StudentID.add(jsonObject2.getString("StudentID"));
                            OrderDetailID.add(jsonObject2
                                    .getString("OrderDetailID"));
                            PaidClasses.add(jsonObject2
                                    .getString("PaidClasses"));
                            Cls_Lvl.add(jsonObject2.getString("ClsLvl"));
                            StTimeHour.add(jsonObject2.getString("StTimeHour"));
                            StTimeMin.add(jsonObject2.getString("StTimeMin"));
                            SLastName.add(jsonObject2.getString("SLastName"));
                            SFirstName.add(jsonObject2.getString("SFirstName"));
                            wu_count.add(jsonObject2.getString("wu_count"));
                            wu_Prev.add(jsonObject2.getString("wu_Prev"));
                            wu_Next.add(jsonObject2.getString("wu_Next"));
                            ParentFirstName.add(jsonObject2
                                    .getString("ParentFirstName"));
                            ParentLastName.add(jsonObject2
                                    .getString("ParentLastName"));
                            BirthDay.add(jsonObject2.getString("BirthDay"));
                            LessonName.add(jsonObject2.getString("LessonName"));
                            LvlAdvAvail.add(jsonObject2
                                    .getString("LvlAdvAvail"));
                            Comments.add(jsonObject2.getString("Comments"));
                            wu_comments.add(jsonObject2
                                    .getString("wu_comments"));
                            SwimComp.add(jsonObject2.getString("SwimComp"));
                            MainScheduleDate.add(jsonObject2
                                    .getString("MainScheduleDate"));
                            SkillsCount.add(jsonObject2
                                    .getString("SkillsCount"));
                            StudentGender.add(jsonObject2
                                    .getString("StudentGender"));
                            NewUser.add(jsonObject2.getString("NewUser"));
                            wu_W.add(jsonObject2.getString("wu_W"));
                            wu_r.add(jsonObject2.getString("wu_r"));
                            wu_b.add(jsonObject2.getString("wu_b"));
                            if (SkillsCount.get(i).toString()
                                    .equalsIgnoreCase("0")) {
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

            try {
                pd.dismiss();
                if (server_response) {
                    server_response = false;
                    onDetectNetworkState().show();
                } else if (connectionout) {
                    connectionout = false;
//                    new GetCurrentSchedule().execute();
//                    change by megha 29-01-2018
                    new GetCurrentScheduleFirstTime().execute();
                } else {
					/*
					 * if (ISAFlag) { dayisa.setVisibility(View.VISIBLE);
					 * dayisa.startAnimation(animBlink); ISAFlag = false; } else
					 * { dayisa.setVisibility(View.INVISIBLE); }
					 */
                    if (wu_avail == 0) {
                        tv_view_current_lesson_error
                                .setVisibility(View.VISIBLE);
                        tv_view_current_lesson_error
                                .setText("No availability at this time.");
                        tv_lessonname.setText("");
                        lv_current_schedule.setVisibility(View.INVISIBLE);
                        btn_send_att.setVisibility(View.INVISIBLE);
//						btn_send_review.setVisibility(View.INVISIBLE);
                        int month, year, day, min, hour;
                        String date = MainScheduleDate.get(0);
                        // ///////////////////////

                        String tempdate[] = date.toString().split("\\/");
                        month = Integer.parseInt(tempdate[0]);
                        day = Integer.parseInt(tempdate[1]);
                        year = Integer.parseInt(tempdate[2]);
                        hour = Integer.parseInt(StTimeHour.get(0));
                        min = Integer.parseInt(StTimeMin.get(0));
                        Calendar now = Calendar.getInstance();
                        now.set(Calendar.MONTH, month);
                        now.set(Calendar.DAY_OF_MONTH, day);
                        now.set(Calendar.YEAR, year);
                        now.set(Calendar.HOUR_OF_DAY, hour);
                        now.set(Calendar.MINUTE, min);

                        now.add(Calendar.MINUTE, 0);
                        tempmonth = "" + (now.get(Calendar.MONTH));
                        tempday = "" + now.get(Calendar.DAY_OF_MONTH);
                        tempyear = "" + now.get(Calendar.YEAR);
                        temphour = "" + now.get(Calendar.HOUR_OF_DAY);
                        tempmin = "" + now.get(Calendar.MINUTE);
                        String am_pm;
                        if (Integer.parseInt(temphour) > 11) {
                            am_pm = "PM";
                        } else {
                            am_pm = "AM";
                        }
                        if (temphour.toString().equalsIgnoreCase("13")) {
                            temphour = "1";
                        } else if (temphour.toString().equalsIgnoreCase("14")) {
                            temphour = "2";
                        } else if (temphour.toString().equalsIgnoreCase("15")) {
                            temphour = "3";
                        } else if (temphour.toString().equalsIgnoreCase("16")) {
                            temphour = "4";
                        } else if (temphour.toString().equalsIgnoreCase("17")) {
                            temphour = "5";
                        } else if (temphour.toString().equalsIgnoreCase("18")) {
                            temphour = "6";
                        } else if (temphour.toString().equalsIgnoreCase("19")) {
                            temphour = "7";
                        } else if (temphour.toString().equalsIgnoreCase("20")) {
                            temphour = "8";
                        } else if (temphour.toString().equalsIgnoreCase("21")) {
                            temphour = "9";
                        } else if (temphour.toString().equalsIgnoreCase("22")) {
                            temphour = "10";
                        } else if (temphour.toString().equalsIgnoreCase("23")) {
                            temphour = "11";
                        }
                        if (temphour.toString().length() == 1) {
                            temphour = "0" + temphour;
                        }
                        if (tempmin.toString().length() == 1) {
                            tempmin = "0" + tempmin;
                        }
                        tv_scheduletime.setText(temphour + ":" + tempmin + " "
                                + am_pm);
                        // btnnext.setEnabled(false);
                        // btnprev.setEnabled(false);
                    } else {
                        if (!LessonName.isEmpty()
                                && !LessonName.get(0).equalsIgnoreCase("")) {
                            tv_view_current_lesson_error
                                    .setVisibility(View.GONE);
                            if (ViewCurrentScheduleInstructorActivity.searchClick) {
                                tv_lessonname.setText("");
                            } else {
                                tv_lessonname.setText(LessonName.get(0).toString());
                            }
                            setScheduleTime();
                            GenerateNewAndOldAtt();
                            lv_current_schedule.setVisibility(View.VISIBLE);
                            btn_send_att.setVisibility(View.VISIBLE);
//							btn_send_review.setVisibility(View.VISIBLE);
                            for (int i = 0; i < SAge.size(); i++) {
                                att_Items
                                        .add(new InstructorViewCurrentScheduleAdapterItem(
                                                wu_avail, IsEmpty.get(i), CheckValue.get(i), FormatStTimeHour.get(i), FormatStTimeMin.get(i), ExistSwimComp.get(i),
                                                IsShowSmCampStatus.get(i), att
                                                .get(i),
                                                wu_attendancetaken.get(i),
                                                ISAAlert.get(i), SScheduleID
                                                .get(i), IScheduleID
                                                .get(i), InstructorID
                                                .get(i), lessontypeid
                                                .get(i), SAge.get(i),
                                                SLevel.get(i),
                                                ScheLevel.get(i), StudentID
                                                .get(i), OrderDetailID
                                                .get(i), StTimeHour
                                                .get(i), StTimeMin
                                                .get(i), SLastName
                                                .get(i), SFirstName
                                                .get(i), wu_count
                                                .get(i),
                                                wu_Prev.get(i), wu_Next.get(i),
                                                ParentFirstName.get(i),
                                                ParentLastName.get(i), BirthDay
                                                .get(i), LessonName
                                                .get(i), LvlAdvAvail
                                                .get(i), Comments
                                                .get(i), wu_comments
                                                .get(i), SwimComp
                                                .get(i), SkillsCount
                                                .get(i), StudentGender
                                                .get(i),
                                                NewUser.get(i),
                                                PreReqID.get(i), Abbr.get(i),
                                                PreReqChecked.get(i),
                                                LevelName, LevelID,
                                                Instructor_name, Instructor_id,
                                                btn_send_att,
                                                StTimeHour.get(i), StTimeMin
                                                .get(i),
                                                MainScheduleDate.get(i), wu_W
                                                .get(i), wu_b.get(i),
                                                wu_r.get(i), SiteID.get(i),
                                                Cls_Lvl.get(i), PaidClasses
                                                .get(i)));
                            }
                            lv_current_schedule.setAdapter(adapter_Att);
                            adapter_Att.notifyDataSetChanged();
                        } else {
                            tv_view_current_lesson_error
                                    .setVisibility(View.VISIBLE);
                            tv_lessonname.setText("");
                            tv_view_current_lesson_error
                                    .setText("No lesson at this time.");
                            lv_current_schedule.setVisibility(View.INVISIBLE);
                            btn_send_att.setVisibility(View.INVISIBLE);
//							btn_send_review.setVisibility(View.INVISIBLE);
                            int month, year, day, min, hour;
                            String date = MainScheduleDate.get(0);
                            // ///////////////////////

                            String tempdate[] = date.toString().split("\\/");
                            month = Integer.parseInt(tempdate[0]);
                            day = Integer.parseInt(tempdate[1]);
                            year = Integer.parseInt(tempdate[2]);
                            hour = Integer.parseInt(StTimeHour.get(0));
                            min = Integer.parseInt(StTimeMin.get(0));
                            Calendar now = Calendar.getInstance();
                            now.set(Calendar.MONTH, month);
                            now.set(Calendar.DAY_OF_MONTH, day);
                            now.set(Calendar.YEAR, year);
                            now.set(Calendar.HOUR_OF_DAY, hour);
                            now.set(Calendar.MINUTE, min);

                            now.add(Calendar.MINUTE, 0);
                            tempmonth = "" + (now.get(Calendar.MONTH));
                            tempday = "" + now.get(Calendar.DAY_OF_MONTH);
                            tempyear = "" + now.get(Calendar.YEAR);
                            temphour = "" + now.get(Calendar.HOUR_OF_DAY);
                            tempmin = "" + now.get(Calendar.MINUTE);
                            String am_pm;
                            if (Integer.parseInt(temphour) > 11) {
                                am_pm = "PM";
                            } else {
                                am_pm = "AM";
                            }
                            if (temphour.toString().equalsIgnoreCase("13")) {
                                temphour = "1";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "14")) {
                                temphour = "2";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "15")) {
                                temphour = "3";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "16")) {
                                temphour = "4";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "17")) {
                                temphour = "5";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "18")) {
                                temphour = "6";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "19")) {
                                temphour = "7";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "20")) {
                                temphour = "8";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "21")) {
                                temphour = "9";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "22")) {
                                temphour = "10";
                            } else if (temphour.toString().equalsIgnoreCase(
                                    "23")) {
                                temphour = "11";
                            }
                            if (temphour.toString().length() == 1) {
                                temphour = "0" + temphour;
                            }
                            if (tempmin.toString().length() == 1) {
                                tempmin = "0" + tempmin;
                            }
                            tv_scheduletime.setText(temphour + ":" + tempmin
                                    + " " + am_pm);
                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public void setScheduleTime() {
        // TODO Auto-generated method stub
        int hours = Integer.parseInt(StTimeHour.get(0));
        hour_for_data = StTimeHour.get(0);
        min_for_data = StTimeMin.get(0);
        String suffix = "AM";
        if (hours >= 12) {
            suffix = "PM";
            hours = hours - 12;
        }
        if (hours == 0) {
            hours = 12;
        }

        if (hour_for_data.equalsIgnoreCase("13")) {
            hour_for_data = "01";
        } else if (hour_for_data.equalsIgnoreCase("14")) {
            hour_for_data = "02";
        } else if (hour_for_data.equalsIgnoreCase("15")) {
            hour_for_data = "03";
        } else if (hour_for_data.equalsIgnoreCase("16")) {
            hour_for_data = "04";
        } else if (hour_for_data.equalsIgnoreCase("17")) {
            hour_for_data = "05";
        } else if (hour_for_data.equalsIgnoreCase("18")) {
            hour_for_data = "06";
        } else if (hour_for_data.equalsIgnoreCase("19")) {
            hour_for_data = "07";
        } else if (hour_for_data.equalsIgnoreCase("20")) {
            hour_for_data = "08";
        } else if (hour_for_data.equalsIgnoreCase("21")) {
            hour_for_data = "09";
        } else if (hour_for_data.equalsIgnoreCase("22")) {
            hour_for_data = "10";
        } else if (hour_for_data.equalsIgnoreCase("23")) {
            hour_for_data = "11";
        } else if (hour_for_data.equalsIgnoreCase("24")) {
            hour_for_data = "12";
        }
        if (min_for_data.length() == 1) {
            min_for_data = "0" + min_for_data;
        }
        if (hour_for_data.length() == 1) {
            hour_for_data = "0" + hour_for_data;
        }
        String current_time = hour_for_data + ":" + min_for_data + " " + suffix;
        tv_scheduletime.setText(current_time);
    }

    private void ClearArray() {
        ExistSwimComp.clear();
        IsShowSmCampStatus.clear();
        att.clear();
        wu_attendancetaken.clear();
        ISAAlert.clear();
        SScheduleID.clear();
        IScheduleID.clear();
        InstructorID.clear();
        lessontypeid.clear();
        SAge.clear();
        SLevel.clear();
        ScheLevel.clear();
        StudentID.clear();
        OrderDetailID.clear();
        StTimeHour.clear();
        StTimeMin.clear();
        SLastName.clear();
        SFirstName.clear();
        wu_count.clear();
        wu_Prev.clear();
        wu_Next.clear();
        ParentFirstName.clear();
        ParentLastName.clear();
        BirthDay.clear();
        LessonName.clear();
        LvlAdvAvail.clear();
        Comments.clear();
        wu_comments.clear();
        SwimComp.clear();
        SkillsList.clear();
        SkillsCount.clear();
        StudentGender.clear();
        NewUser.clear();
        PreReqID.clear();
        Abbr.clear();
        PreReqChecked.clear();
        MainScheduleDate.clear();
        wu_b.clear();
        wu_W.clear();
        wu_r.clear();
        SiteID.clear();
        IsEmpty.clear();
        CheckValue.clear();
        FormatStTimeHour.clear();
        FormatStTimeMin.clear();
        Cls_Lvl.clear();
        newatt.clear();
        oldatt.clear();
        PaidClasses.clear();
        att_Items.clear();
    }

    boolean ISAFlag = false;

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
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("Rinstructorid", Instructor_id);
            request.addProperty("strRScheDate", DateandTime);

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
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("Any Aquatics", "Result : " + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
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
                    Log.i(TAG, "" + ISAFlag);
                } else {
                    getISA = false;
                }
            } catch (SocketException e) {
                e.printStackTrace();
                connectionout = true;
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
                        ((ViewCurrentScheduleInstructorActivity) getActivity()).isa_main
                                .setVisibility(View.VISIBLE);
                        ((ViewCurrentScheduleInstructorActivity) getActivity()).isa_main
                                .startAnimation(animBlink);
                        ISAFlag = false;
                    } else {
                        ((ViewCurrentScheduleInstructorActivity) getActivity()).isa_main
                                .clearAnimation();
                        ((ViewCurrentScheduleInstructorActivity) getActivity()).isa_main
                                .setVisibility(View.INVISIBLE);

                    }
                } else {
                    ((ViewCurrentScheduleInstructorActivity) getActivity()).isa_main
                            .clearAnimation();
                    ((ViewCurrentScheduleInstructorActivity) getActivity()).isa_main
                            .setVisibility(View.INVISIBLE);
                }
            }
        }
    }


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

    public void GenerateNewAndOldAtt() {
        // TODO Auto-generated method stub
        if (!newatt.isEmpty()) {
            newatt.clear();
        }
        if (!oldatt.isEmpty()) {
            oldatt.clear();
        }
        for (int j = 0; j < StudentID.size(); j++) {
            int temp_att = Integer.parseInt(att.get(j));
            oldatt.add(temp_att);
            if (Integer.parseInt(wu_attendancetaken.get(j)) == 0) {
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
