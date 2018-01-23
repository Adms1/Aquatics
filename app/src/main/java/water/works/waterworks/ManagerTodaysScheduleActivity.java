package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
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

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ManagerTodaysScheduleActivity extends Activity implements OnClickListener, OnRefreshListener2<ListView> {

    TextView tv_day, tv_date, tv_instructorname;
    String am_pm;
    Date noteTS;
    String time;
    //	Titanic titanic;
//	TitanicTextView tv ;
    Shimmer shimmer;
    ShimmerTextView tv;
    public static FrameLayout fl_mts_loading;
    Boolean isInternetPresent = false;
    ListView lv_ts_data;
    public PullToRefreshListView lv_manager;
    boolean server_status = false, connectionout = false;
    private static String TAG = "Today's Schedule Manager View";
    String currentDateandTime;
    public static String DAYNAME;
    public static String InstructorName;
    int pgnum = 0;
    int old_pos = 0;
    String InstructorID = "";
    ArrayList<String> LevelName = new ArrayList<String>();
    ArrayList<String> LevelID = new ArrayList<String>();
    ArrayList<String> MainScheduleDate = new ArrayList<String>();
    ArrayList<String> SLevel = new ArrayList<String>();
    ArrayList<String> LessonName = new ArrayList<String>();
    ArrayList<String> lessontypeid = new ArrayList<String>();
    ArrayList<String> SAge = new ArrayList<String>();
    ArrayList<String> ParentFirstName = new ArrayList<String>();
    ArrayList<String> ParentLastName = new ArrayList<String>();
    ArrayList<String> SLastName = new ArrayList<String>();
    ArrayList<String> SFirstName = new ArrayList<String>();
    ArrayList<String> StudentID = new ArrayList<String>();
    ArrayList<String> FormateStTimeHour = new ArrayList<String>();
    ArrayList<String> FormatStTimeMin = new ArrayList<String>();
    ArrayList<String> StTimeHour = new ArrayList<String>();
    ArrayList<String> StTimeMin = new ArrayList<String>();
    ArrayList<String> StudentGender = new ArrayList<String>();
    ArrayList<String> wu_Comments = new ArrayList<String>();
    ArrayList<String> SwimComp = new ArrayList<String>();
    ArrayList<String> Lvl_Adv_Avail = new ArrayList<String>();
    ArrayList<String> IScheduleID = new ArrayList<String>();
    ArrayList<String> _InstructorID = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_todays_schedule);
        isInternetPresent = Utility
                .isNetworkConnected(ManagerTodaysScheduleActivity.this);
        if (isInternetPresent) {
            Initialization();
            SetScreenDetails();
            WW_StaticClass.duration1 = 300;
            WW_StaticClass.duration2 = 1000;
//			titanic = new Titanic();
//			titanic.start(tv);
            shimmer = new Shimmer();
            shimmer.start(tv);
            Date date = new Date();
            System.out.println("Date-->" + date);
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            System.out.println("New Date--->" + format.format(date));
            currentDateandTime = format.format(date);
            new GetLevel().execute();
            InstructorID = InstructorSelectionForMVCSActivity.final_list.get(0);
            InstructorName = InstructorSelectionForMVCSActivity.final_list_name.get(0);
            new TodaysScheduleData().execute();
            lv_manager.setOnRefreshListener(this);
        } else {
            onDetectNetworkState().show();
        }
    }

    private void Initialization() {
        // TODO Auto-generated method stub
        tv = (ShimmerTextView) findViewById(R.id.my_text_view);
        tv.setTypeface(Typefaces.get(ManagerTodaysScheduleActivity.this, "Satisfy-Regular.ttf"));
        fl_mts_loading = (FrameLayout) findViewById(R.id.mts_loading);
        fl_mts_loading.setVisibility(View.GONE);
        tv_date = (TextView) findViewById(R.id.tv_mts_date);
        tv_day = (TextView) findViewById(R.id.tv_mts_day);
        tv_instructorname = (TextView) findViewById(R.id.tv_mts_name);
        lv_manager = (PullToRefreshListView) findViewById(R.id.lv_mts_ds);
        lv_ts_data = lv_manager.getRefreshableView();
        lv_manager.setMode(Mode.PULL_FROM_END);
    }

    private void SetScreenDetails() {
        // TODO Auto-generated method stub
        Calendar c = Calendar.getInstance();
        int Day_Name = c.get(Calendar.DAY_OF_WEEK);
        int Date = c.get(Calendar.DATE);
        int Month = c.get(Calendar.MONTH);
        String day_name = null;
        if (Day_Name == 1) {
            day_name = "SUNDAY";
        } else if (Day_Name == 2) {
            day_name = "MONDAY";
        } else if (Day_Name == 3) {
            day_name = "TUESDAY";
        } else if (Day_Name == 4) {
            day_name = "WEDNESDAY";
        } else if (Day_Name == 5) {
            day_name = "THURSDAY";
        } else if (Day_Name == 6) {
            day_name = "FRIDAY";
        } else if (Day_Name == 7) {
            day_name = "SATURDAY";
        }
        tv_instructorname.setText(WW_StaticClass.UserName);
        tv_day.setText(day_name);
        DAYNAME = day_name;
        String m, d;
        if (String.valueOf(Month).length() == 1) {
            m = "0" + (Month + 1);
        } else {
            m = "" + (Month + 1);
        }
        if (String.valueOf(Date).length() == 1) {
            d = "0" + (Date);
        } else {
            d = "" + (Date);
        }

        tv_date.setText(m + "/" + d);
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
//		decorView.setSystemUiVisibility(uiOptions);
        isInternetPresent = Utility
                .isNetworkConnected(ManagerTodaysScheduleActivity.this);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
        WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
        WW_StaticClass.UserName = WW_StaticClass.InstructorName;
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ManagerTodaysScheduleActivity.this);
        builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                })
                .setPositiveButton("Οk", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.btn_back:
                    finish();
                    WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
                    WW_StaticClass.UserName = WW_StaticClass.InstructorName;
                    InstructorSelectionForMVCSActivity.final_list.clear();
                    InstructorSelectionForMVCSActivity.final_list_name.clear();
                    break;
            }
        } else {
            onDetectNetworkState().show();
        }
    }

    private class GetLevel extends AsyncTask<Void, Void, Void> {

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
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
//					response.toString();
                if (code.equals("000")) {
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
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private class TodaysScheduleData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            fl_mts_loading.setVisibility(View.VISIBLE);
            fl_mts_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_ViewSchl_GetViewScheduleByDateAndInstrid);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("Rinstructorid", InstructorID);
            request.addProperty("strRScheDate", currentDateandTime);
//				request.addProperty("strRScheDate","10/05/2014 08:00 AM" );

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_ViewSchl_GetViewScheduleByDateAndInstrid,
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
                    Log.i(TAG, "jArray : " + jArray.toString());
                    JSONObject jsonObject;
                    JSONObject jsonObject2, jsonObject3;
                    JSONArray jArray2;
                    JSONArray jArray3 = null;
                    for (int k = 0; k < jArray.length(); k++) {
                        jsonObject = jArray.getJSONObject(k);
                        Log.i(TAG, "jsonObject: " + jsonObject.toString());

                        jArray2 = jsonObject.getJSONArray("Items");
                        Log.i(TAG, "jArray2 : " + jArray2.toString());
                        for (int i = 0; i < jArray2.length(); i++) {
                            jsonObject2 = jArray2.getJSONObject(i);
                            FormateStTimeHour.add(jsonObject2.getString("FormateStTimeHour"));
                            FormatStTimeMin.add(jsonObject2.getString("FormatStTimeMin"));
                            StTimeHour.add(jsonObject2.getString("StTimeHour"));
                            StTimeMin.add(jsonObject2.getString("StTimeMin"));
                            SLevel.add(jsonObject2.getString("SLevel"));
                            LessonName.add(jsonObject2.getString("LessonName"));
                            lessontypeid.add(jsonObject2.getString("lessontypeid"));
                            SAge.add(jsonObject2.getString("SAge"));
                            ParentFirstName.add(jsonObject2.getString("ParentFirstName"));
                            ParentLastName.add(jsonObject2.getString("ParentLastName"));
                            SFirstName.add(jsonObject2.getString("SFirstName"));
                            SLastName.add(jsonObject2.getString("SLastName"));
                            StudentID.add(jsonObject2.getString("StudentID"));
                            MainScheduleDate.add(jsonObject2.getString("MainScheduleDate"));
                            StudentGender.add(jsonObject2.getString("StudentGender"));
                            wu_Comments.add(jsonObject2.getString("wu_scomments"));
                            SwimComp.add(jsonObject2.getString("SwimComp"));
                            Lvl_Adv_Avail.add(jsonObject2.getString("LvlAdvAvail"));
                            IScheduleID.add(jsonObject2.getString("IScheduleID"));
                            _InstructorID.add(jsonObject2.getString("InstructorID"));
                        }
                    }
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                connectionout = true;
            } catch (SocketException e) {
                // TODO: handle exception
                e.printStackTrace();
                connectionout = true;

            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                server_status = true;
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
            lv_manager.onRefreshComplete();
            fl_mts_loading.setVisibility(View.GONE);
            if (server_status) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(
                        ManagerTodaysScheduleActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
                server_status = false;
            } else if (connectionout) {
                connectionout = false;
                new TodaysScheduleData().execute();
            } else {

                if (!LessonName.isEmpty()) {
                    if (StudentID.size() > 1) {
                        old_pos = lv_manager.getRefreshableView()
                                .getFirstVisiblePosition() + 1;
                    } else {
                        old_pos = lv_manager.getRefreshableView()
                                .getFirstVisiblePosition() + 1;
                    }
                    lv_ts_data.setAdapter(TSDSAdapter);
                    lv_ts_data.post(new Runnable() {


                        public void run() {
                            // TODO Auto-generated method stub
                            lv_manager.getRefreshableView()
                                    .setSelection(old_pos);
                        }
                    });

                    lv_manager.onRefreshComplete();
//					Log.e(TAG, "L Name = " + LevelName);
//					lv_ts_data.setAdapter(new TSDSAdapter(TodaysSchedule_DeckSupervisorActivity.this,
//							LevelName, LevelID, MainScheduleDate, SLevel, LessonName, lessontypeid, SAge,
//							ParentFirstName, ParentLastName, SLastName, SFirstName, StudentID, FormateStTimeHour, FormatStTimeMin, StTimeHour, StTimeMin, StudentGender, wu_Comments));
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(ManagerTodaysScheduleActivity.this).create();
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    alertDialog.setTitle("WaterWorks");
                    alertDialog.setCanceledOnTouchOutside(false);
                    // set the message
                    alertDialog.setMessage("Connection timeout.");
                    alertDialog.setIcon(R.drawable.ic_launcher);
                    // set button1 functionality
                    alertDialog.setButton("Retry",
                            new DialogInterface.OnClickListener() {


                                public void onClick(DialogInterface dialog, int which) {
                                    // close dialog

                                    dialog.cancel();
                                    new GetLevel().execute();
                                    new TodaysScheduleData().execute();

                                }
                            });

                    // show the alert dialog
                    alertDialog.show();

                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
            Log.i(TAG, "" + System.currentTimeMillis());
        }
    }

    private BaseAdapter TSDSAdapter = new BaseAdapter() {
        private int[] colors = new int[]{Color.parseColor("#EEEEEE"), Color.parseColor("#FFFFFF")};

        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();


                    convertView = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.todays_schedule_manager_row, null);
                    int colorpos = position % colors.length;
                    convertView.setBackgroundColor(colors[colorpos]);

                    holder.tv_inst_name = (TextView) convertView.findViewById(R.id.tv_mtsi_instructor_name);
                    holder.tv_sName = (TextView) convertView.findViewById(R.id.tv_mtsi_stud_name);
                    holder.tv_sAge = (TextView) convertView.findViewById(R.id.tv_mtsi_age);
                    holder.tv_sComments = (TextView) convertView.findViewById(R.id.tv_mtsi_comment);
                    holder.tv_lvl_name = (TextView) convertView.findViewById(R.id.tv_mtsi_level);
                    holder.tv_lessontype = (TextView) convertView.findViewById(R.id.tv_mtsi_lt);
                    holder.tv_lec_time = (TextView) convertView.findViewById(R.id.tv_mtsi_time);
                    holder.late = (ImageView) convertView.findViewById(R.id.iv_mtsi_level);
                    holder.swimcomp = (Button) convertView.findViewById(R.id.btn_mtsi_swim_comp);

                    String inst_name = InstructorSelectionForMVCSActivity.final_list_name.get(InstructorSelectionForMVCSActivity.final_list.indexOf(_InstructorID.get(position)));

                    holder.tv_inst_name.setText(Html.fromHtml("<u><b><font color='#034AF3'>" + inst_name + "</font></b></u>"));
                    holder.tv_inst_name.setOnClickListener(new OnClickListener() {


                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent i = new Intent(getBaseContext(), ShadowLogsActivity.class);
                            i.putExtra("from", "view");
                            i.putExtra("ischeduleid", IScheduleID.get(position));
                            startActivity(i);
                        }
                    });
                    holder.tv_lessontype.setText(LessonName.get(position));
                    String level = LevelName.get(LevelID.indexOf(SLevel.get(position)));
                    holder.tv_lvl_name.setText(level);
                    holder.tv_sComments.setText(wu_Comments.get(position));
                    holder.tv_sAge.setText(SAge.get(position));
                    if (StudentGender.get(position).toString().equalsIgnoreCase("Female")) {
                        holder.tv_sName.setText(Html.fromHtml("<font color='#8800B7'>" + SFirstName.get(position) + " " + SLastName.get(position) + "</font><br /><small>(" + ParentFirstName.get(position) + " " + ParentLastName.get(position) + ")</small>"));
                    } else {
                        holder.tv_sName.setText(Html.fromHtml("<font color='#000066'>" + SFirstName.get(position) + " " + SLastName.get(position) + "</font><br /><small>(" + ParentFirstName.get(position) + " " + ParentLastName.get(position) + ")</small>"));
                    }
                    int hr = Integer.parseInt(StTimeHour.get(position));
                    int min = Integer.parseInt(StTimeMin.get(position));
                    String am_pm;
                    if (hr >= 12 && min >= 00) {

                        am_pm = "PM";
                    } else {
                        am_pm = "AM";
                    }
                    holder.tv_lec_time.setText(Html.fromHtml("<small>" + MainScheduleDate.get(position) + " " + FormateStTimeHour.get(position) + ":" + FormatStTimeMin.get(position) + am_pm + "</small>"));
                    if (Integer.parseInt(Lvl_Adv_Avail.get(position)) > 1) {
                        holder.late.setVisibility(View.VISIBLE);
                    } else {
                        holder.late.setVisibility(View.GONE);
                    }
                    if (SwimComp.get(position).toString().equalsIgnoreCase("false")) {
                        holder.swimcomp.setVisibility(View.GONE);
                    } else {
                        holder.swimcomp.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }


        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }


        public int getCount() {
            // TODO Auto-generated method stub
            return StudentID.size();
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

    };

    public class ViewHolder {
        TextView tv_sName, tv_sAge, tv_sComments, tv_lec_time, tv_lvl_name, tv_inst_name, tv_lessontype;
        ImageView late;
        Button swimcomp;
    }


    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        // TODO Auto-generated method stub
        lv_manager.onRefreshComplete();
    }

    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        // TODO Auto-generated method stub
        pgnum++;
        if (InstructorSelectionForMVCSActivity.final_list.size() != pgnum) {
            InstructorName = InstructorSelectionForMVCSActivity.final_list_name.get(pgnum);
            InstructorID = InstructorSelectionForMVCSActivity.final_list.get(pgnum);
            new TodaysScheduleData().execute();
        } else {
            lv_manager.onRefreshComplete();
        }

    }

}
