package water.works.waterworks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

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
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import water.works.waterworks.adapter.AttandanceAdapter;
import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.model.AttendanceItems;
import water.works.waterworks.services.DeckNotificationService;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


@SuppressLint("SimpleDateFormat")
public class ViewCurrentLessonActivity extends Activity implements OnClickListener, AnimationListener {
    /////////////new code/////////////////
    boolean connectionout = false;
    private static final int CAMERA_PIC_REQUEST = 1111;
    Animation animBlink;
    ImageView dayisa;
    public String filename;
    private ArrayList<AttendanceItems> att_Items;
    private AttandanceAdapter adapter_Att;
    String SStudnetid;
    RelativeLayout rl_main;
    //	Button next,prev,next1,prev1;
    TextView tv_sched_time;
    int oldmin, newmin, oldmil, newmil, count = 1;
    private boolean status_req_cee = false, status_req_cee_manager = false, status_req_aqu = false;
    public ListView listView_currentlessonData;
    Boolean isInternetPresent = false;
    Button mBtn_home, btn_footer_send_attendance, btn_footer_prev, btn_footer_next;
    ArrayList<String> sidemenu = new ArrayList<String>();
    String yes_no_date;

    TextView tv_day, tv_date, tv_time, tv_lessonname, tv_instructorname, tv_view_current_lesson_error;
    Button btn_menu_options;
    SharedPreferences mPreferences;
    String currentDateandTime;
    private static String TAG = "View Current Lesson";
    ///////////////Level List
    ArrayList<String> LevelName = new ArrayList<String>();
    ArrayList<String> LevelID = new ArrayList<String>();
    ///////////////Attendance
    ArrayList<String> IsShowSmCampStatus = new ArrayList<String>();
    ArrayList<String> ISAAlert = new ArrayList<String>();
    ArrayList<String> Cls_Lvl = new ArrayList<String>();
    ArrayList<String> Lvl_Adv_Avail = new ArrayList<String>();
    ArrayList<String> SiteID = new ArrayList<String>();
    ArrayList<String> SLevel_ID = new ArrayList<String>();
    ArrayList<String> wu_W = new ArrayList<String>();
    ArrayList<String> ScheLevel = new ArrayList<String>();
    ArrayList<String> SwimComp = new ArrayList<String>();
    ArrayList<String> LessonName = new ArrayList<String>();
    ArrayList<String> lessontypeid = new ArrayList<String>();
    ArrayList<String> IScheduleID = new ArrayList<String>();
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
    ArrayList<String> StTimeHour = new ArrayList<String>();
    ArrayList<String> StTimeMin = new ArrayList<String>();
    ArrayList<String> wu_comments = new ArrayList<String>();
    ArrayList<String> MainScheduleDate = new ArrayList<String>();
    ArrayList<String> wu_Next = new ArrayList<String>();
    ArrayList<String> wu_Prev = new ArrayList<String>();
    ArrayList<Boolean> NewStudent = new ArrayList<Boolean>();
    ArrayList<Integer> wu_attendancetaken = new ArrayList<Integer>();
    ArrayList<Integer> att = new ArrayList<Integer>();
    int wu_avail = 2;
    boolean ISAFlag = false, getIsa = false;
    /// for skills list
    ArrayList<String> SkillsCount = new ArrayList<String>();
    ArrayList<String> StudentGender = new ArrayList<String>();
    ArrayList<ArrayList<String>> PreReqID = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> PreReqChecked = new ArrayList<ArrayList<String>>();
    ArrayList<ArrayList<String>> Abbr = new ArrayList<ArrayList<String>>();
    String am_pm;
    Date noteTS;
    String time;
    //Request shadow and request desk
    Button mBtn_request_shadow, mBtn_request_deck;
    TextView mTV_request_shadow, mTV_request_deck;
    String formattedDate;
    boolean pool_status, server_status = false, shadow_click = false, deck_click = false;
    ArrayList<String> PoolName, PoolId;
    Dialog dialog = null;
    public static final int request_shadow = 0;
    public static final int request_deck = 1;

    public static String Shadow_poolid = "poolid";

    boolean status = false;
    boolean status_cee = false;
    boolean status_cee_manager = false;
    boolean status_aquatics = false;
    boolean status_shadow = false;

    //Any CEE, CEE MANAGER, AQUATICSMANAGER
    ArrayList<String> ANYCEE_name = new ArrayList<String>();
    ArrayList<String> ANYCEEManager_name = new ArrayList<String>();
    ArrayList<String> ANYAQUATICSMANAGER_name = new ArrayList<String>();
    ArrayList<String> ANYCEE_id = new ArrayList<String>();
    ArrayList<String> ANYCEEManager_id = new ArrayList<String>();
    ArrayList<String> ANYAQUATICSMANAGER_id = new ArrayList<String>();

    String mytime;
    String whattimeforassist = "-1";
    String desk_poolid = "-1";
    String DeskAssistID_web;

    String emp_type_for_cee = "", emp_type_for_cee_manager = "", emp_type_for_aquatics = "", emp_userid_for_cee = "", emp_userid_for_cee_manager = "", emp_userid_for_aquatics = "";

//	Titanic titanic;
//	TitanicTextView tv ;

    Shimmer shimmer;
    ShimmerTextView tv;
    public static FrameLayout fl_vcl_loading;
    //		private View decorView;
    //		private int uiOptions;
    TextView bar_time;
    public static String hour_for_data, min_for_data, date_for_data;
    TextView tv_view_current_lesson_title;
    Button btnnext, btnprev;
    Thread t;
    Thread t2;
    Timer t1;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //		decorView = getWindow().getDecorView();
        //		uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //                | View.SYSTEM_UI_FLAG_FULLSCREEN
        //                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        //		createUiChangeListener();
        setContentView(R.layout.activity_view_current_lesson);
        mPreferences = PreferenceManager
                .getDefaultSharedPreferences(ViewCurrentLessonActivity.this);
        isInternetPresent = Utility
                .isNetworkConnected(ViewCurrentLessonActivity.this);
        if (isInternetPresent) {

            Initialization();
            SetScreenDetails();
            hour_for_data = getIntent().getStringExtra("HOUR");
            min_for_data = getIntent().getStringExtra("MIN");
            date_for_data = getIntent().getStringExtra("DATE");
            WW_StaticClass.duration1 = 300;
            WW_StaticClass.duration2 = 1000;
//            titanic = new Titanic();
//            titanic.start(tv);
            shimmer=new Shimmer();
            shimmer.start(tv);
            String deleteornot = getIntent().getStringExtra("DELETE");
            if (deleteornot.equalsIgnoreCase("YES")) {
                //				SingleOptionAlertWithoutTitle.ShowAlertDialog(
                //						ViewCurrentLessonActivity.this, "WaterWorks", "Comment Deleted Successfully", "Ok");
            } else {
                Log.i(TAG, "Delete or not =" + deleteornot);
            }
            try {
                if (date_for_data.isEmpty() || date_for_data.equalsIgnoreCase("")) {
                    Date date = new Date();
                    System.out.println("Date-->" + date);
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                    System.out.println("New Date--->" + format.format(date));
                    currentDateandTime = format.format(date);
                    yes_no_date = "no";
                    new GetLevel().execute();
                    new GetAttendance().execute();

                } else {
                    getVal();
                    yes_no_date = "yes";
                    new GetLevel().execute();
                    new GetAttendance().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Internet connection is not present
            onDetectNetworkState().show();
        }
        oldmin = Calendar.getInstance().getTime().getMinutes();


    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        t.interrupt();
                        t2.interrupt();

                        ViewCurrentLessonActivity.this.finish();
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

    private void getVal() {
        // TODO Auto-generated method stub
        //		bar_time.setVisibility(View.VISIBLE);
        tv_view_current_lesson_title.setText("Today's Schedule");
        int hours = Integer.parseInt(hour_for_data);
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
        String current_time = hour_for_data + ":" + min_for_data + " " + suffix;
        bar_time.setText(current_time);
        currentDateandTime = date_for_data + " " + current_time;
    }

    private void SetScreenDetails() {
        // TODO Auto-generated method stub
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
        Log.i("Time", "Time = " + hour + ":" + min + " " + " " + day_name + " " + Date + "/" + Month);
        t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {

                            public void run() {
                                newmin = Calendar.getInstance().getTime().getMinutes();
                                //		                        	Log.i(TAG,"old time = " + oldmin + "\nnew time = " +  newmin);
                                updateTextView();
                                Calendar cc = Calendar.getInstance();
                                int AM_PM = cc.get(Calendar.AM_PM);

                                if (AM_PM == 0) {
                                    am_pm = "AM";
                                } else {
                                    am_pm = "PM";
                                }
                            }

                            @SuppressLint("SimpleDateFormat")
                            private void updateTextView() {
                                // TODO Auto-generated method stub
                                noteTS = Calendar.getInstance().getTime();
                                java.util.Date date = new Date();
                                SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm");
                                //							        System.out.println("New Date--->" + format1.format(date));
                                mytime = format1.format(date);
                                time = "hh:mm"; // 12:00
                                oldmil = 0;
                                /*if(newmin != 0 || newmin != 00){
									    if(newmin > oldmin){
									    	Log.i(TAG, "new time is greater");
										    	AfterMinRefresh();
									    }
									   }
									   else{
										   newmil = Calendar.getInstance().getTime().getSeconds();
										   if(count==1&&newmil==oldmil){
//											   count=2;
											   AfterMinRefresh();
										   }
									   }
									    oldmin = newmin;*/
                                if (newmin == 0 || newmin == 20 || newmin == 40) {
                                    newmil = Calendar.getInstance().getTime().getSeconds();
                                    if (count == 1 && newmil == oldmil) {
                                        AfterMinRefresh();
                                    }
                                }

                                tv_time.setText(DateFormat.format(time, noteTS) + "" + am_pm);
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

        t2 = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(300000);
                        runOnUiThread(new Runnable() {

                            public void run() {
                                new GetISAAlert().execute();
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
        t2.start();

        tv_instructorname.setText(WW_StaticClass.UserName);
        tv_day.setText(day_name);
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

    protected void AfterMinRefresh() {
        recreate();
    }

    private void Initialization() {
        // TODO Auto-generated method stub
        sidemenu.add("Logout");
        rl_main = (RelativeLayout) findViewById(R.id.rl_main_current_schedule);
		/*prev = (Button)findViewById(R.id.slid_left);
		next = (Button)findViewById(R.id.slid_right);
		prev1 = (Button)findViewById(R.id.slid_left1);
		next1 = (Button)findViewById(R.id.slid_right1);
		prev.bringToFront();
		next.bringToFront();
		prev1.bringToFront();
		next1.bringToFront();
		prev1.setVisibility(View.GONE);
		next1.setVisibility(View.GONE); */
        btn_menu_options = (Button) findViewById(R.id.btn_menu_options);
        tv_view_current_lesson_title = (TextView) findViewById(R.id.tv_view_current_lesson_title);
        tv = (ShimmerTextView) findViewById(R.id.my_text_view);
        tv.setTypeface(Typefaces.get(ViewCurrentLessonActivity.this, "Satisfy-Regular.ttf"));
        fl_vcl_loading = (FrameLayout) findViewById(R.id.view_current_lesson_loading);
        fl_vcl_loading.setVisibility(View.GONE);
        tv_date = (TextView) findViewById(R.id.tv_view_current_lesson_date);
        tv_day = (TextView) findViewById(R.id.tv_view_current_lesson_day);
        tv_time = (TextView) findViewById(R.id.tv_view_current_lesson_time);
        tv_lessonname = (TextView) findViewById(R.id.tv_view_current_lesson_lessonname);
        tv_instructorname = (TextView) findViewById(R.id.tv_view_current_lesson_name);
        mBtn_home = (Button) findViewById(R.id.btn_view_current_lesson_home);
        listView_currentlessonData = (ListView) findViewById(R.id.lv_view_current_lesson_data);
        tv_view_current_lesson_error = (TextView) findViewById(R.id.tv_view_current_lesson_error);
        mBtn_request_shadow = (Button) findViewById(R.id.btn_view_current_lesson_request_shadow);
        mTV_request_shadow = (TextView) findViewById(R.id.tv_view_current_lesson_request_shadow);
        mBtn_request_deck = (Button) findViewById(R.id.btn_view_current_lesson_request_deck);
        mTV_request_deck = (TextView) findViewById(R.id.tv_view_current_lesson_request_deck);
        bar_time = (TextView) findViewById(R.id.tv_view_current_lesson_bar_time);
        bar_time.setVisibility(View.GONE);
        btnnext = (Button) findViewById(R.id.btnnext);
        btnprev = (Button) findViewById(R.id.btnprev);
        FrameLayout footerLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.currectlessonlist_footer, null);
        btn_footer_send_attendance = (Button) footerLayout.findViewById(R.id.btn_footer_send_attendance);
        //		btn_footer_next = (Button)footerLayout.findViewById(R.id.slid_right);
        //		btn_footer_prev = (Button)footerLayout.findViewById(R.id.slid_left);
        listView_currentlessonData.addFooterView(footerLayout);
        listView_currentlessonData.setSmoothScrollbarEnabled(true);
        btnnext.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                currentDateandTime = wu_Prev.get(0).toString();
                if (currentDateandTime.toString().equalsIgnoreCase("") || currentDateandTime.isEmpty()) {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this,
                            "WaterWorks", "No more previous schedule", "Ok");
                } else {
                    ClearArray();
                    new GetAttendance().execute();
                }
            }
        });
        btnprev.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    currentDateandTime = wu_Next.get(0).toString();
                    if (currentDateandTime.toString().equalsIgnoreCase("") || currentDateandTime.isEmpty()) {
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this,
                                "WaterWorks", "No more next schedule", "Ok");
                    } else {
                        ClearArray();
                        new GetAttendance().execute();
                    }
                } catch (IndexOutOfBoundsException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        att_Items = new ArrayList<AttendanceItems>();
        tv_sched_time = (TextView) findViewById(R.id.tv_view_current_sched_time);
        dayisa = (ImageView) findViewById(R.id.day_isa);
        dayisa.clearAnimation();
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        animBlink.setAnimationListener(this);
        dayisa.setVisibility(View.INVISIBLE);
        dayisa.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), DetailReportActivity.class);
                //				i.putExtra("url", "http://office.waterworksswimonline.com/newcode/InstructorAlertReport.aspx?wu_instructorid="+WW_StaticClass.InstructorID);
                //				i.putExtra("url", "http://192.168.1.201/newcode/InstructorAlertReport.aspx?wu_instructorid="+WW_StaticClass.InstructorID);
                i.putExtra("FROM", "ISA");
                i.putExtra("url", SOAP_CONSTANTS.Report_Url + "InstructorAlertReport.aspx?wu_instructorid=" + WW_StaticClass.InstructorID);
                startActivity(i);
            }
        });
        adapter_Att = new AttandanceAdapter(ViewCurrentLessonActivity.this,
                att_Items);

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

    ProgressDialog pDialog;

    private class GetAttendance extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //			pDialog = new ProgressDialog(ViewCurrentLessonActivity.this);
            //			pDialog.setMessage(Html.fromHtml("Loading wait..."));
            //			pDialog.setIndeterminate(true);
            //			pDialog.setCancelable(false);
            //			pDialog.show();
            fl_vcl_loading.setVisibility(View.VISIBLE);
            fl_vcl_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_GetAttendanceList);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("Rinstructorid", WW_StaticClass.InstructorID);
            request.addProperty("strRScheDate", currentDateandTime);
            //			request.addProperty("strRScheDate","01/18/2015 09:00 AM" );

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_AttendanceList,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.bodyIn;
                Log.i(TAG, "Result : " + response.toString());
                SoapPrimitive sp1 = (SoapPrimitive) response.getProperty(0);
                String resp = sp1.toString();
                JSONObject jo = new JSONObject(resp);
                wu_avail = jo.getInt("wu_avail");
                //				ISAFlag = jo.getBoolean("ISAFlag");
                if (wu_avail == 0) {

                } else if (wu_avail == 1 || wu_avail == 2) {

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
                            IsShowSmCampStatus.add(jsonObject2.getString("IsShowSmCampStatus"));
                            ISAAlert.add(jsonObject2.getString("ISAAlert"));
                            SiteID.add(jsonObject2.getString("SiteID"));
                            SLevel_ID.add(jsonObject2.getString("SLevel"));
                            wu_W.add(jsonObject2.getString("wu_W"));
                            ScheLevel.add(jsonObject2.getString("ScheLevel"));
                            SwimComp.add(jsonObject2.getString("SwimComp"));
                            LessonName.add(jsonObject2.getString("LessonName"));
                            lessontypeid.add(jsonObject2.getString("lessontypeid"));
                            IScheduleID.add(jsonObject2.getString("IScheduleID"));
                            SAge.add(jsonObject2.getString("SAge"));
                            ParentFirstName.add(jsonObject2.getString("ParentFirstName"));
                            ParentLastName.add(jsonObject2.getString("ParentLastName"));
                            BirthDay.add(jsonObject2.getString("BirthDay"));
                            Comments.add(jsonObject2.getString("Comments"));
                            wu_r.add(jsonObject2.getString("wu_r"));
                            SFirstName.add(jsonObject2.getString("SFirstName"));
                            SLastName.add(jsonObject2.getString("SLastName"));
                            StudentID.add(jsonObject2.getString("StudentID"));
                            ShowWBR.add(jsonObject2.getString("ShowWBR"));
                            wu_b.add(jsonObject2.getString("wu_b"));
                            SScheduleID.add(jsonObject2.getString("SScheduleID"));
                            OrderDetailID.add(jsonObject2.getString("OrderDetailID"));
                            PaidClasses.add(jsonObject2.getString("PaidClasses"));
                            Cls_Lvl.add(jsonObject2.getString("ClsLvl"));
                            Lvl_Adv_Avail.add(jsonObject2.getString("LvlAdvAvail"));
                            StTimeHour.add(jsonObject2.getString("StTimeHour"));
                            StTimeMin.add(jsonObject2.getString("StTimeMin"));
                            wu_comments.add(jsonObject2.getString("wu_comments"));
                            MainScheduleDate.add(jsonObject2.getString("MainScheduleDate"));
                            SkillsCount.add(jsonObject2.getString("SkillsCount"));
                            StudentGender.add(jsonObject2.getString("StudentGender"));
                            wu_Next.add(jsonObject2.getString("wu_Next"));
                            wu_Prev.add(jsonObject2.getString("wu_Prev"));
                            NewStudent.add(jsonObject2.optBoolean("NewUser"));
                            if (StudentID.get(0).toString().equalsIgnoreCase("")) {

                            } else {
                                att.add(jsonObject2.getInt("att"));
                                wu_attendancetaken.add(jsonObject2.getInt("wu_attendancetaken"));
                                jArray3 = jsonObject2.getJSONArray("SkillsList");

                                ArrayList<String> tempabbr = new ArrayList<String>();
                                ArrayList<String> tempPreReqId = new ArrayList<String>();
                                ArrayList<String> tempPreReqChecked = new ArrayList<String>();
                                for (int b = 0; b < jArray3.length(); b++) {
                                    jsonObject3 = jArray3.getJSONObject(b);
                                    String abbr = jsonObject3.getString("Abbr");
                                    String prereqid = jsonObject3.getString("PreReqID");
                                    String prereqchecked = jsonObject3.getString("PreReqChecked");

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
                    Log.i(TAG, "new student = " + NewStudent);
                }
            } catch (JSONException e) {
                server_status = true;
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                // TODO: handle exception
                e.printStackTrace();
                connectionout = true;
            } catch (Exception e) {
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
            //			pDialog.dismiss();
            fl_vcl_loading.setVisibility(View.GONE);

            try {
                if (server_status) {
                    server_status = false;
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            ViewCurrentLessonActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
                } else if (connectionout) {
                    connectionout = false;
                    ConnectionTimeOut().show();
                } else {
                    if (wu_avail == 0) {
                        tv_view_current_lesson_error.setVisibility(View.VISIBLE);
                        tv_view_current_lesson_error.setText("No availability at this time.");
						/*AlertDialog alertDialog = new AlertDialog.Builder(ViewCurrentLessonActivity.this).create();
						 alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						alertDialog.setTitle("WaterWorks");
						alertDialog.setCanceledOnTouchOutside(false); 
						alertDialog.setCancelable(false);
						// set the message
						alertDialog.setMessage("No availability at this time.");
						alertDialog.setIcon(R.drawable.ic_launcher);
						// set button1 functionality
						alertDialog.setButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// close dialog

										dialog.cancel();
										t.interrupt();
										finish();

									}
								});

						// show the alert dialog
						alertDialog.show();*/
                        btnnext.setEnabled(false);
                        btnprev.setEnabled(false);
                    } else {
                        if (!LessonName.isEmpty() && !LessonName.get(0).equalsIgnoreCase("")) {
                            tv_view_current_lesson_error.setVisibility(View.GONE);
                            tv_lessonname.setText(LessonName.get(0).toString());
                            setScheduleTime();
                            for (int i = 0; i < StudentID.size(); i++) {
                                att_Items.add(new AttendanceItems(LevelName
                                        , LevelID, IsShowSmCampStatus.get(i), ISAAlert.get(i), Cls_Lvl.get(i), Lvl_Adv_Avail.get(i), SiteID.get(i),
                                        SLevel_ID.get(i), ScheLevel.get(i), wu_W.get(i), wu_b.get(i), wu_r.get(i),
                                        SwimComp.get(i), BirthDay.get(i), LessonName.get(i), lessontypeid.get(i), IScheduleID.get(i),
                                        SAge.get(i), ParentFirstName.get(i), ParentLastName.get(i), Comments.get(i), wu_comments.get(i),
                                        SFirstName.get(i), SLastName.get(i), StudentID.get(i), ShowWBR.get(i), SScheduleID.get(i),
                                        OrderDetailID.get(i), PaidClasses.get(i), StTimeHour.get(i), StTimeMin.get(i),
                                        MainScheduleDate.get(i), SkillsCount.get(i), PreReqID.get(i), PreReqChecked.get(i), Abbr.get(i), StudentGender.get(i),
                                        NewStudent.get(i), yes_no_date, btn_footer_send_attendance,
                                        wu_attendancetaken.get(i), att.get(i), ViewCurrentLessonActivity.this));
                            }
                            //				adapter_Att = new AttandanceAdapter(ViewCurrentLessonActivity.this,
                            //						att_Items);
                            adapter_Att.notifyDataSetChanged();
                            listView_currentlessonData.setAdapter(adapter_Att);
                        } else {
                            tv_view_current_lesson_error.setVisibility(View.VISIBLE);
                            tv_view_current_lesson_error.setText("No lesson at this time.");
							/*AlertDialog alertDialog = new AlertDialog.Builder(ViewCurrentLessonActivity.this).create();
					 alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alertDialog.setTitle("WaterWorks");
					alertDialog.setCanceledOnTouchOutside(false); 
					alertDialog.setCancelable(false);
					// set the message
					alertDialog.setMessage("No lesson at this time.");
					alertDialog.setIcon(R.drawable.ic_launcher);
					// set button1 functionality
					alertDialog.setButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// close dialog

									dialog.cancel();
//									finish();

								}
							});

					// show the alert dialog
					alertDialog.show();*/

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

    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.btn_view_current_lesson_home:
                    //			Intent homeIntent=new Intent(getApplicationContext(), MenuActivity.class);
                    //			startActivity(homeIntent);
                    t.interrupt();
                    t2.interrupt();

                    finish();
                    break;
                case R.id.btn_view_current_lesson_request_shadow:

                    shadow_click = true;
                    new IamInPool().execute();
                    break;
                //		case R.id.tv_view_current_lesson_request_shadow:
                //			if(WW_StaticClass.Studentid.equalsIgnoreCase("sid")){
                //				SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Please select student first befor requesting..", "Ok");
                //			}
                //			else{
                //		        shadow_click = true;
                //				new IamInPool().execute();
                //			}
                //			break;

                case R.id.btn_view_current_lesson_request_deck:
                    if (WW_StaticClass.Studentid.equalsIgnoreCase("sid")) {
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Please select student first befor requesting..", "Ok");
                    } else if (WW_StaticClass.Siteid.equalsIgnoreCase("siteid")) {
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Please select student first befor requesting..", "Ok");
                    } else {
                        deck_click = true;

                        new IamInPool().execute();
                        new DeskdataforCEE().execute();
                        new DeskdataforCEEManager().execute();
                        new DeskdataforAquaticsManager().execute();
                    }
                    break;
                //		case R.id.tv_view_current_lesson_request_deck:
                //			if(WW_StaticClass.Studentid.equalsIgnoreCase("sid")){
                //				SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Please select student first befor requesting..", "Ok");
                //			}
                //			else if(WW_StaticClass.Siteid.equalsIgnoreCase("siteid")){
                //				SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Please select student first befor requesting..", "Ok");
                //			}
                //			else{
                //		        deck_click = true;
                //				new IamInPool().execute();
                //				new DeskdataforCEE().execute();
                //				new DeskdataforCEEManager().execute();
                //				new DeskdataforAquaticsManager().execute();
                //			}
                //			break;

				/*case R.id.slid_left1:
			currentDateandTime = wu_Prev.get(0).toString();
			if(currentDateandTime.toString().equalsIgnoreCase("")||currentDateandTime.isEmpty()){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this,
						"WaterWorks", "No more previous schedule", "Ok");
			}
			else{
				ClearArray();
				new GetAttendance().execute();
			}
			break;
		case R.id.slid_right1:
			try{
			currentDateandTime = wu_Next.get(0).toString();
			if(currentDateandTime.toString().equalsIgnoreCase("")||currentDateandTime.isEmpty()){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this,
						"WaterWorks", "No more next schedule", "Ok");
			}
			else{
				ClearArray();
				new GetAttendance().execute();
			}
			}
			catch (IndexOutOfBoundsException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			break;*/
                case R.id.btn_menu_options:
                    final ListPopupWindow lpw_logout = new ListPopupWindow(getApplicationContext());
                    lpw_logout.setAdapter(new ArrayAdapter<String>(
                            getApplicationContext(),
                            R.layout.edittextpopup, sidemenu));
                    lpw_logout.setAnchorView(btn_menu_options);
                    lpw_logout.setHeight(LayoutParams.WRAP_CONTENT);
                    lpw_logout.setWidth(200);
                    lpw_logout.setModal(true);
                    lpw_logout.setOnItemClickListener(
                            new OnItemClickListener() {

                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int pos, long id) {
                                    // TODO Auto-generated method stub
                                    if (sidemenu.get(pos).equalsIgnoreCase("Logout")) {
                                        lpw_logout.dismiss();
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                ViewCurrentLessonActivity.this);

                                        // set title
                                        alertDialogBuilder.setTitle("WaterWorks");
                                        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

                                        // set dialog message
                                        alertDialogBuilder
                                                .setMessage("Are you sure you want to logout ?")
                                                .setCancelable(false)
                                                .setPositiveButton("Logout",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                WW_StaticClass.InstructorID = "";
                                                                t.interrupt();
                                                                t2.interrupt();
                                                                stopService(new Intent(ViewCurrentLessonActivity.this, DeckNotificationService.class));
                                                                finish();
                                                                Intent loginIntent = new Intent(ViewCurrentLessonActivity.this, LoginActivity.class);
                                                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                                startActivity(loginIntent);
                                                                android.os.Process.killProcess(android.os.Process.myPid());
                                                            }
                                                        })
                                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                        // create alert dialog
                                        AlertDialog alertDialog = alertDialogBuilder.create();

                                        // show it
                                        alertDialog.show();
                                    }
                                }
                            });
                    lpw_logout.show();

                    break;
                default:
                    break;
            }
        } else {
            onDetectNetworkState().show();

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
        //	        bar_time.setText(current_time);
        tv_sched_time.setText(current_time);
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
                .isNetworkConnected(ViewCurrentLessonActivity.this);
        if (isInternetPresent) {
            //			t1 = new Timer();
            //			t1.scheduleAtFixedRate(new TimerTask() {
            //
            //				@Override
            //				public void run() {
            //
            //					new GetISAAlert().execute();
            //				}
            //
            //			}, 0, 300000);
        } else {
            onDetectNetworkState().show();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        t.interrupt();
        t2.interrupt();

        finish();

        //		stopThread(t);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public class IamInPool extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            WW_StaticClass.Siteid = SiteID.get(0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            formattedDate = mytime;

            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE, SOAP_CONSTANTS.METHOD_NAME_GETPOOLLIST);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("siteid", WW_StaticClass.Siteid);
            // Log.i(Tag, "Login name"+mEd_User.getText().toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            PoolId = new ArrayList<String>();
            PoolName = new ArrayList<String>();
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_ACTION_POOLLIST, envelope); // Calling
                // Web
                // service

                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("here", "Result : " + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i("Current Lesson", "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                Log.i("Current Lesson", "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                //			response.toString();
                if (code.equals("000")) {
                    pool_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i("Current Lesson", "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();


                    //			String resp = envelope.getResponse().toString();// response.toString().trim();
                    Log.i("here", "Result : " + resp.toString());
                    JSONObject jobj = new JSONObject(resp);
                    JSONArray mArray = jobj.getJSONArray("Pools");
                    for (int i = 0; i < mArray.length(); i++) {
                        JSONObject mJsonObjectFee = mArray.getJSONObject(i);
                        PoolId.add(mJsonObjectFee.getString("PoolId"));
                        PoolName.add(mJsonObjectFee.getString("PoolName"));
                    }
                    // Name.add(mJsonObjectFee.getString("Name"));
                    // Term1.add(mJsonObjectFee.getString("Term1"));
                    // Term2.add(mJsonObjectFee.getString("Term2"));

                } else {
                    pool_status = false;
                }
            } catch (Exception e) {
                server_status = true;
                e.printStackTrace();

            }
            //		Log.i("ID", PoolId.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", ""));
            //		Log.i("Name", PoolName.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", ""));
            //		String a =PoolId.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "");
            //		ArrayList<String> list = new ArrayList<String>(Arrays.asList(a.split(",")));
            //		Log.i("List", "List = "+list);
            //		Log.i("I m in pool", "I am in pool " + PoolName.get(1).toString());
            return null;

        }


        @Override
        protected void onPostExecute(Void result) {
            dialog = new Dialog(ViewCurrentLessonActivity.this);
            if (server_status) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(
                        ViewCurrentLessonActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
                server_status = false;
            } else {
                if (pool_status) {
                    if (shadow_click) {
                        showDialog(request_shadow);
                        shadow_click = false;
                    } else if (deck_click) {
                        showDialog(request_deck);
                        deck_click = false;
                    } else {
                        Log.i("Nothing Click", "Nothing Click");
                    }
                    pool_status = false;
                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "No pool found", "Ok");
                }
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case request_shadow:
                dialog = new Dialog(ViewCurrentLessonActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.requestshadow);
                Button send_request = (Button) dialog.findViewById(R.id.btn_rs_send_request);
                send_request.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (Shadow_poolid.isEmpty()) {
                            SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Please select pool befor requesting", "Ok");
                        }
                        dialog.dismiss();
                        //				for(int i=0;i<WW_StaticClass.SStudnetID.size();i++){
                        //					SStudnetid = WW_StaticClass.SStudnetID.get(i);
                        new InsertShadowRequest().execute();
                        //				}
                    }
                });

                LinearLayout mLinearLayout = (LinearLayout) dialog.findViewById(R.id.ll_pool_list);
                final RadioButton[] rb = new RadioButton[PoolName.size()];
                RadioGroup rg = new RadioGroup(getApplicationContext());
                rg.setOrientation(RadioGroup.HORIZONTAL);
                for (int i = 0; i < PoolName.size(); i++) {
                    rb[i] = new RadioButton(getApplicationContext());
                    rg.addView(rb[i]);
                    rb[i].setText(PoolName.get(i));
                    rb[i].setId(i);
                    rb[i].setButtonDrawable(android.R.drawable.btn_radio);
                    rb[i].setTextColor(getResources().getColor(R.color.texts1));
                    rb[i].setTextSize(18);

                }
                mLinearLayout.addView(rg);
                rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // TODO Auto-generated method stub
                        Log.i("Radio Check", "" + checkedId);
                        Log.i("Pool Name", "Pool name = " + PoolName.get(checkedId));
                        int a = PoolName.indexOf(PoolName.get(checkedId));
                        Log.i("Here", "" + a);
                        String poolidvalue = PoolId.get(a);
                        Shadow_poolid = poolidvalue;
                        Log.i("poolid", "" + poolidvalue);

                    }
                });
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = LayoutParams.MATCH_PARENT;
                lp.height = LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
                dialog.setCanceledOnTouchOutside(true);

                break;

            case request_deck:
                dialog = new Dialog(ViewCurrentLessonActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.requestdeck);
                final CheckBox chk_cee = (CheckBox) dialog.findViewById(R.id.chk_cee);
                final CheckBox chk_cee_manager = (CheckBox) dialog.findViewById(R.id.chk_cee_manager);
                final CheckBox chk_aquatics_manager = (CheckBox) dialog.findViewById(R.id.chk_aquatics_manager);
                final RadioButton rc_now = (RadioButton) dialog.findViewById(R.id.rc_now);
                final RadioButton rc_min = (RadioButton) dialog.findViewById(R.id.rc_min);
                Button rc_send_request = (Button) dialog.findViewById(R.id.btn_rc_send_request);
                LinearLayout mLinearLayout1 = (LinearLayout) dialog.findViewById(R.id.ll_pool_list);
                final RadioButton[] rb1 = new RadioButton[PoolName.size()];
                final RadioGroup rg1 = new RadioGroup(getApplicationContext());
                rg1.setOrientation(RadioGroup.HORIZONTAL);
                for (int i = 0; i < PoolName.size(); i++) {
                    rb1[i] = new RadioButton(getApplicationContext());
                    rg1.addView(rb1[i]);
                    rb1[i].setText(PoolName.get(i));
                    rb1[i].setId(i);
                    rb1[i].setButtonDrawable(android.R.drawable.btn_radio);
                    rb1[i].setTextColor(getResources().getColor(R.color.texts1));
                    rb1[i].setTextSize(18);

                }
                mLinearLayout1.addView(rg1);
                rg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // TODO Auto-generated method stub
                        try {
                            int a = PoolName.indexOf(PoolName.get(checkedId));
                            Log.i("Here", "" + a);
                            String poolidvalue = PoolId.get(a);
                            Log.i("poolid", "" + poolidvalue);
                            desk_poolid = poolidvalue;

                            for (int j = 0; j < PoolName.size(); j++) {
                                rb1[j].setError(null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                rc_now.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        rc_now.setChecked(true);
                        rc_min.setChecked(false);
                        whattimeforassist = "1";
                        rc_min.setError(null);
                        rc_now.setError(null);
                    }
                });
                rc_min.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        rc_now.setChecked(false);
                        rc_min.setChecked(true);
                        whattimeforassist = "2";
                        rc_min.setError(null);
                        rc_now.setError(null);
                    }
                });
                rc_send_request.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (desk_poolid.equalsIgnoreCase("-1") && whattimeforassist.equalsIgnoreCase("-1")) {
                            for (int i = 0; i < PoolName.size(); i++) {
                                rb1[i].setError("Please select anyone option");
                            }
                            rc_min.setError("Please select anyone option");
                            rc_now.setError("Please select anyone option");
                        } else if (desk_poolid.equalsIgnoreCase("-1")) {
                            for (int i = 0; i < PoolName.size(); i++) {
                                rb1[i].setError("Please select anyone option");
                            }
                        } else if (whattimeforassist.equalsIgnoreCase("-1")) {
                            rc_min.setError("Please select anyone option");
                            rc_now.setError("Please select anyone option");
                        } else {
                            if (chk_cee.isChecked()) {
                                emp_type_for_cee = "1";
                                emp_userid_for_cee = "-1";

                            }
                            if (chk_cee_manager.isChecked()) {
                                emp_type_for_cee_manager = "2";
                                emp_userid_for_cee_manager = "-1";

                            }
                            if (chk_aquatics_manager.isChecked()) {
                                emp_type_for_aquatics = "3";
                                emp_userid_for_aquatics = "-1";

                            }
                            new InsertRequestDesk().execute();
                            chk_aquatics_manager.setChecked(false);
                            chk_cee.setChecked(false);
                            chk_cee_manager.setChecked(false);
                            rc_min.setChecked(false);
                            rc_now.setChecked(false);
                            rg1.clearCheck();

                            desk_poolid = "-1";
                            whattimeforassist = "-1";
                            dialog.dismiss();
                        }
                    }
                });

                dialog.setOnCancelListener(new OnCancelListener() {

                    public void onCancel(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        chk_aquatics_manager.setChecked(false);
                        chk_cee.setChecked(false);
                        chk_cee_manager.setChecked(false);
                        rc_min.setChecked(false);
                        rc_now.setChecked(false);
                        rg1.clearCheck();
                        desk_poolid = "-1";
                        whattimeforassist = "-1";
                    }
                });

			/*		final RadioButton rb_any_cee = (RadioButton)dialog.findViewById(R.id.rb_cee);
		final RadioButton rb_any_cee_manager = (RadioButton)dialog.findViewById(R.id.rb_cee_manager);
		final RadioButton rb_any_aquatics_manager = (RadioButton)dialog.findViewById(R.id.rb_rc_aquatics_manager);
		final RadioButton rc_now=(RadioButton)dialog.findViewById(R.id.rc_now);
		final RadioButton rc_min=(RadioButton)dialog.findViewById(R.id.rc_min);
		final Button btn_aquatics_manager = (Button)dialog.findViewById(R.id.et_rc_aquatics_manager);
		final Button btn_cee_staff = (Button)dialog.findViewById(R.id.et_rc_cee_staff);
		final Button btn_cee_manager = (Button)dialog.findViewById(R.id.et_rc_cee_manager);
		Button rc_send_request = (Button)dialog.findViewById(R.id.btn_rc_send_request);



		LinearLayout mLinearLayout1 = (LinearLayout)dialog.findViewById(R.id.ll_pool_list);
		final RadioButton[] rb1 = new RadioButton[PoolName.size()];
		   final RadioGroup rg1 = new RadioGroup(getApplicationContext());
		   rg1.setOrientation(RadioGroup.HORIZONTAL);
		   for (int i = 0; i < PoolName.size(); i++) {
		    rb1[i] = new RadioButton(getApplicationContext());
		    rg1.addView(rb1[i]);
		    rb1[i].setText(PoolName.get(i));
		    rb1[i].setId(i);
		    rb1[i].setButtonDrawable(android.R.drawable.btn_radio);
		    rb1[i].setTextColor(getResources().getColor(R.color.texts1));
		    rb1[i].setTextSize(18);

		   }
		    mLinearLayout1.addView(rg1);
		    rg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					try{
						int a = PoolName.indexOf(PoolName.get(checkedId));
						Log.i("Here", ""+a);
						String poolidvalue = PoolId.get(a);
						Log.i("poolid", ""+poolidvalue);
						desk_poolid = poolidvalue;

						for(int j=0;j<PoolName.size();j++){
							rb1[j].setError(null);
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			});



		rc_now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rc_now.setChecked(true);
				rc_min.setChecked(false);
				whattimeforassist = "1";
				rc_min.setError(null);
				rc_now.setError(null);
			}
		});
		rc_min.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rc_now.setChecked(false);
				rc_min.setChecked(true);
				whattimeforassist = "2";
				rc_min.setError(null);
				rc_now.setError(null);
			}
		});


		rb_any_cee.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rb_any_cee.setChecked(true);
				btn_cee_staff.setText("");
//				rb_any_cee_manager.setChecked(false);
//				rb_any_cee_manager.setEnabled(false);
//				btn_cee_staff.setEnabled(false);
//				btn_cee_manager.setEnabled(false);
				emp_type_for_cee = "1";
				emp_userid_for_cee = "-1";

			}
		});

		rb_any_cee_manager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				rb_any_cee.setChecked(false);
				rb_any_cee_manager.setChecked(true);
				btn_cee_manager.setText("");
//				rb_any_cee.setEnabled(false);
//				btn_cee_staff.setEnabled(false);
//				btn_cee_manager.setEnabled(false);

				emp_type_for_cee_manager = "2";
				emp_userid_for_cee_manager = "-1";
			}
		});

		rb_any_aquatics_manager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rb_any_aquatics_manager.setChecked(true);
//				btn_aquatics_manager.setEnabled(false);
				btn_aquatics_manager.setText("");

				emp_type_for_aquatics = "3";
				emp_userid_for_aquatics = "-1";
			}
		});

		listpopupwindow_cee_staff = new ListPopupWindow(getApplicationContext());
		btn_cee_staff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				rb_any_cee.setEnabled(false);
//				rb_any_cee_manager.setEnabled(false);
//				btn_cee_manager.setEnabled(false);
				listpopupwindow_cee_staff.show();
			}
		});
		listpopupwindow_cee_staff.setAdapter(new ArrayAdapter<String>(
	            getApplicationContext(),
	            R.layout.edittextpopup,ANYCEE_name ));
        listpopupwindow_cee_staff.setAnchorView(btn_cee_staff);
//        listpopupwindow.setWidth(90);
        listpopupwindow_cee_staff.setHeight(300);
        listpopupwindow_cee_staff.setModal(true);
        listpopupwindow_cee_staff.setOnItemClickListener(
            new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
//					holder.et_sLevel.setText(LevelName.get(pos));
					btn_cee_staff.setText(ANYCEE_name.get(pos));
					rb_any_cee.setChecked(false);
					emp_type_for_cee ="1";
					emp_userid_for_cee = ANYCEE_id.get(pos);
					listpopupwindow_cee_staff.dismiss();
				}
			});

		listpopupwindow_cee_manager = new ListPopupWindow(getApplicationContext());
		btn_cee_manager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				rb_any_cee.setEnabled(false);
//				rb_any_cee_manager.setEnabled(false);
//				btn_cee_staff.setEnabled(false);
				listpopupwindow_cee_manager.show();
			}
		});
		listpopupwindow_cee_manager.setAdapter(new ArrayAdapter<String>(
	            getApplicationContext(),
	            R.layout.edittextpopup,ANYCEEManager_name));
        listpopupwindow_cee_manager.setAnchorView(btn_cee_manager);
//        listpopupwindow.setWidth(90);
        listpopupwindow_cee_manager.setHeight(200);
        listpopupwindow_cee_manager.setModal(true);
        listpopupwindow_cee_manager.setOnItemClickListener(
            new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
//					holder.et_sLevel.setText(LevelName.get(pos));
					btn_cee_manager.setText(ANYCEEManager_name.get(pos));
					rb_any_cee_manager.setChecked(false);
					emp_type_for_cee_manager ="2";
					emp_userid_for_cee_manager = ANYCEEManager_id.get(pos);
					listpopupwindow_cee_manager.dismiss();
				}
			});


		listpopupwindow_aquatics_manager = new ListPopupWindow(getApplicationContext());
		btn_aquatics_manager.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				rb_any_aquatics_manager.setEnabled(false);
				listpopupwindow_aquatics_manager.show();
			}
		});
		listpopupwindow_aquatics_manager.setAdapter(new ArrayAdapter<String>(
	            getApplicationContext(),
	            R.layout.edittextpopup,ANYAQUATICSMANAGER_name));
        listpopupwindow_aquatics_manager.setAnchorView(btn_aquatics_manager);
        listpopupwindow_aquatics_manager.setHeight(300);
        listpopupwindow_aquatics_manager.setModal(true);
        listpopupwindow_aquatics_manager.setOnItemClickListener(
            new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					// TODO Auto-generated method stub
//					holder.et_sLevel.setText(LevelName.get(pos));

					btn_aquatics_manager.setText(ANYAQUATICSMANAGER_name.get(pos));
					rb_any_aquatics_manager.setChecked(false);
					emp_type_for_aquatics = "3";
					emp_userid_for_aquatics = ANYAQUATICSMANAGER_id.get(pos);
					listpopupwindow_aquatics_manager.dismiss();
				}
			});




        /////////////// Send request
		rc_send_request.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(desk_poolid.equalsIgnoreCase("-1")&&whattimeforassist.equalsIgnoreCase("-1")){
					for(int i=0;i<PoolName.size();i++){
						rb1[i].setError("Please select anyone option");
					}
					rc_min.setError("Please select anyone option");
					rc_now.setError("Please select anyone option");
				}
				else if(desk_poolid.equalsIgnoreCase("-1")){
					for(int i=0;i<PoolName.size();i++){
						rb1[i].setError("Please select anyone option");
					}
				}
				else if(whattimeforassist.equalsIgnoreCase("-1")){
					rc_min.setError("Please select anyone option");
					rc_now.setError("Please select anyone option");
				}
				else{
					new InsertRequestDesk().execute();
					rb_any_cee.setChecked(false);
					rb_any_cee_manager.setChecked(false);
					rb_any_aquatics_manager.setChecked(false);
					btn_cee_staff.setText("");
					btn_cee_manager.setText("");
					btn_aquatics_manager.setText("");
					rc_min.setChecked(false);
					rc_now.setChecked(false);
					rg1.clearCheck();

					desk_poolid = "-1";
					whattimeforassist = "-1";
//					dialog.cancel();
					dialog.dismiss();
				}
			}
		});

		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				rb_any_cee.setChecked(false);
				rb_any_cee_manager.setChecked(false);
				rb_any_aquatics_manager.setChecked(false);
				btn_cee_staff.setText("");
				btn_cee_manager.setText("");
				btn_aquatics_manager.setText("");
				rc_min.setChecked(false);
				rc_now.setChecked(false);
				rg1.clearCheck();
				desk_poolid = "-1";
				whattimeforassist = "-1";
			}
		});*/

                WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
                lp1.copyFrom(dialog.getWindow().getAttributes());
                lp1.width = LayoutParams.MATCH_PARENT;
                lp1.height = LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp1);
                dialog.setCanceledOnTouchOutside(true);
                break;


        }
        return dialog;
    }

    ListPopupWindow listpopupwindow_cee_staff, listpopupwindow_cee_manager, listpopupwindow_aquatics_manager;

    ProgressDialog pDialog2;

    public class InsertShadowRequest extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //				pDialog2 = new ProgressDialog(ViewCurrentLessonActivity.this);
            //				pDialog2.setMessage(Html.fromHtml("Loading wait..."));
            //				pDialog2.setIndeterminate(true);
            //				pDialog2.setCancelable(false);
            //				pDialog2.show();
            fl_vcl_loading.setVisibility(View.VISIBLE);
            fl_vcl_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_InsertShadowRequest);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("IScheduleID", IScheduleID.get(0).toString());
            request.addProperty("Rinstructorid", WW_StaticClass.InstructorID);
            request.addProperty("RPoolID", Shadow_poolid);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertShadowRequest,
                        envelope); // Calling Web service
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                Log.i("here", "Result : " + response.toString());
                if (response.toString().equalsIgnoreCase("Inserted Successfully") || response.toString().equalsIgnoreCase("Registered Successfully")) {
                    status_shadow = true;
                } else {
                    status_shadow = false;
                }
            } catch (SocketTimeoutException e) {
                server_status = true;
                e.printStackTrace();
            } catch (Exception e) {
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
            fl_vcl_loading.setVisibility(View.GONE);
            if (server_status == true) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                server_status = false;
            }
            if (status_shadow) {
                status_shadow = false;
                //					SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "Request Shadow", "Request submitted.", "Ok");
                AlertDialog alertDialog = new AlertDialog.Builder(ViewCurrentLessonActivity.this).create();
                // hide title bar
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setTitle("WaterWorks");
                alertDialog.setIcon(R.drawable.ic_launcher);
                alertDialog.setCanceledOnTouchOutside(false);
                // set the message
                alertDialog.setMessage("Request submitted.");
                // set button1 functionality
                alertDialog.setButton("Ok",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // close dialog

                                dialog.cancel();
                                ViewCurrentLessonActivity.this.dialog.dismiss();
                            }
                        });
                // show the alert dialog
                alertDialog.show();

            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(ViewCurrentLessonActivity.this).create();
                // hide title bar
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setTitle("WaterWorks");
                alertDialog.setIcon(R.drawable.ic_launcher);
                alertDialog.setCanceledOnTouchOutside(false);
                // set the message
                alertDialog.setMessage("Request not submitted.");
                // set button1 functionality
                alertDialog.setButton("Ok",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                // close dialog
                                dialog.cancel();
                                ViewCurrentLessonActivity.this.dialog.dismiss();
                            }
                        });
                // show the alert dialog
                alertDialog.show();

            }
        }

    }

    ProgressDialog pDialog3;

    public class DeskdataforCEE extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //				pDialog3 = new ProgressDialog(ViewCurrentLessonActivity.this);
            //				pDialog3.setMessage(Html.fromHtml("Loading wait..."));
            //				pDialog3.setIndeterminate(true);
            //				pDialog3.setCancelable(false);
            //				pDialog3.show();
            fl_vcl_loading.setVisibility(View.VISIBLE);
            fl_vcl_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_GetUsersByType);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("SiteID", WW_StaticClass.Siteid);
            request.addProperty("usertype", 1);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetUsersByType,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("Any CEE", "Result : " + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                if (code.equals("000")) {
                    status_cee = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    JSONObject jo = new JSONObject(mSoapObject3.toString());
                    JSONArray jArray = jo.getJSONArray("CEE Staff");
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        ANYCEE_name.add(jsonObject.getString("EmployeeName"));
                        ANYCEE_id.add(jsonObject.getString("UserId"));
                    }
                    Log.e(TAG, "CEE NAME= " + ANYCEE_name);
                    Log.e(TAG, "CEE ID = " + ANYCEE_id);
                } else {
                    status_cee = false;
                }

            } catch (SocketTimeoutException e) {
                server_status = true;
                e.printStackTrace();
            } catch (Exception e) {
                server_status = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //				pDialog3.dismiss();
            fl_vcl_loading.setVisibility(View.GONE);
            if (server_status == true) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                server_status = false;
            }
            if (status_cee) {
                status_cee = false;
            } else {
                Toast.makeText(getApplicationContext(), "CEE : No users found", Toast.LENGTH_LONG).show();
            }
        }

    }

    ProgressDialog pDialog4;

    public class DeskdataforCEEManager extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //				pDialog4 = new ProgressDialog(ViewCurrentLessonActivity.this);
            //				pDialog4.setMessage(Html.fromHtml("Loading wait..."));
            //				pDialog4.setIndeterminate(true);
            //				pDialog4.setCancelable(false);
            //				pDialog4.show();
            fl_vcl_loading.setVisibility(View.VISIBLE);
            fl_vcl_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_GetUsersByType);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("SiteID", WW_StaticClass.Siteid);
            request.addProperty("usertype", 2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetUsersByType,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("Any CEE Manager", "Result : " + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                if (code.equals("000")) {
                    status_cee_manager = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    JSONObject jo = new JSONObject(mSoapObject3.toString());
                    JSONArray jArray = jo.getJSONArray("CEE Manager");
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        ANYCEEManager_name.add(jsonObject.getString("EmployeeName"));
                        ANYCEEManager_id.add(jsonObject.getString("UserId"));
                    }
                    Log.e(TAG, "ANYCEEManager name= " + ANYCEEManager_name);
                    Log.e(TAG, "ANYCEEManager id = " + ANYCEEManager_id);
                } else {
                    status_cee_manager = false;
                }

            } catch (SocketTimeoutException e) {
                server_status = true;
                e.printStackTrace();
            } catch (Exception e) {
                server_status = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //			pDialog4.dismiss();
            fl_vcl_loading.setVisibility(View.GONE);
            if (server_status == true) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                server_status = false;
            }
            if (status_cee_manager) {
                status_cee_manager = false;

                //				SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
            } else {
                Toast.makeText(getApplicationContext(), "CEE manager : No users found", Toast.LENGTH_LONG).show();
            }
        }

    }


    ProgressDialog pDialog5;

    public class DeskdataforAquaticsManager extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //				pDialog5 = new ProgressDialog(ViewCurrentLessonActivity.this);
            //				pDialog5.setMessage(Html.fromHtml("Loading wait..."));
            //				pDialog5.setIndeterminate(true);
            //				pDialog5.setCancelable(false);
            //				pDialog5.show();
            fl_vcl_loading.setVisibility(View.VISIBLE);
            fl_vcl_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_GetUsersByType);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("SiteID", WW_StaticClass.Siteid);
            request.addProperty("usertype", 3);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetUsersByType,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("Any Aquatics", "Result : " + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                if (code.equals("000")) {
                    status_aquatics = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    JSONObject jo = new JSONObject(mSoapObject3.toString());
                    JSONArray jArray = jo.getJSONArray("Aquatics Manager");
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        ANYAQUATICSMANAGER_name.add(jsonObject.getString("EmployeeName"));
                        ANYAQUATICSMANAGER_id.add(jsonObject.getString("UserId"));
                    }
                    Log.e(TAG, "ANYAQUATICSMANAGER name= " + ANYAQUATICSMANAGER_name);
                    Log.e(TAG, "ANYAQUATICSMANAGER id = " + ANYAQUATICSMANAGER_id);
                } else {
                    status_aquatics = false;
                }

            } catch (SocketTimeoutException e) {
                server_status = true;
                e.printStackTrace();
            } catch (Exception e) {
                server_status = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //			pDialog5.dismiss();
            fl_vcl_loading.setVisibility(View.GONE);
            if (server_status == true) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                server_status = false;
            }
            if (status_aquatics) {
                status_aquatics = false;
                //				SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
            } else {
                Toast.makeText(getApplicationContext(), "Aquatics : No users found", Toast.LENGTH_LONG).show();
            }
        }

    }


    ///////////////////////////Insert Request Desk Assiste///////////////////////////////
    ProgressDialog progressDialog;

    public class InsertRequestDesk extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //		progressDialog = new ProgressDialog(ViewCurrentLessonActivity.this);
            //		progressDialog.setMessage(Html.fromHtml("Loading wait..."));
            //		progressDialog.setIndeterminate(true);
            //		progressDialog.setCancelable(false);
            //		progressDialog.show();
            fl_vcl_loading.setVisibility(View.VISIBLE);
            fl_vcl_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            formattedDate = mytime;
            Log.e("Pool id", "pool id = " + desk_poolid);
            Log.e("Time or now", "Time or now = " + whattimeforassist);
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssist);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("Rinstructorid", WW_StaticClass.InstructorID);
            request.addProperty("RAssistDate", formattedDate);
            request.addProperty("RSiteId", WW_StaticClass.Siteid);
            request.addProperty("RPoolID", desk_poolid);
            request.addProperty("RNeedTime", whattimeforassist);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertDeckAssist,
                        envelope); // Calling Web service
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                Log.i("Insert Desk Assist", "Result : " + response.toString());
                String rep = response.toString();
                JSONObject jsonObject = new JSONObject(rep);
                JSONArray jsonObject2 = jsonObject.getJSONArray("DeckAssitID");
                DeskAssistID_web = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                Log.e("DeckAssitID", "DeckAssitID " + DeskAssistID_web);
                status = true;

            } catch (JSONException e) {
                server_status = true;
                e.printStackTrace();
            } catch (Exception e) {
                server_status = true;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //		progressDialog.dismiss();
            fl_vcl_loading.setVisibility(View.GONE);
            if (server_status == true) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                server_status = false;
            } else {
                if (status == true) {
                    status = false;

                    if (!DeskAssistID_web.isEmpty()) {
                        if (!emp_type_for_cee.toString().equalsIgnoreCase("")) {
                            new InsertDeskAssistUser_forCEE().execute();
                        }
                        if (!emp_type_for_cee_manager.toString().equalsIgnoreCase("")) {
                            new InsertDeskAssistUser_forCEEManager().execute();
                        }
                        if (!emp_type_for_aquatics.toString().equalsIgnoreCase("")) {
                            new InsertDeskAssistUser_forAquatics().execute();
                        }

                    } else {
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "No data found, Please try again..", "Ok");
                    }
                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
                }
            }


        }

        ProgressDialog progressDialog2;

        public class InsertDeskAssistUser_forCEE extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                //		progressDialog2 = new ProgressDialog(ViewCurrentLessonActivity.this);
                //		progressDialog2.setMessage(Html.fromHtml("Loading wait..."));
                //		progressDialog2.setIndeterminate(true);
                //		progressDialog2.setCancelable(false);
                //		progressDialog2.show();
                fl_vcl_loading.setVisibility(View.VISIBLE);
                fl_vcl_loading.bringToFront();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                        SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssistUser);
                request.addProperty("token", WW_StaticClass.UserToken);
                request.addProperty("RDeckAssistID", DeskAssistID_web);
                request.addProperty("REmpType", emp_type_for_cee);
                request.addProperty("RUserID", emp_userid_for_cee);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11); // Make an Envelop for sending as whole
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                Log.i("Request", "Request = " + request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        SOAP_CONSTANTS.SOAP_ADDRESS);
                try {
                    androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertDeckAssistUser,
                            envelope); // Calling Web service
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    Log.i("Insert Desk Assist User", "Result : " + response.toString());
                    String rep = response.toString();
                    JSONObject jsonObject = new JSONObject(rep);
                    JSONArray jsonObject2 = jsonObject.getJSONArray("DeckAssitUserID");
                    String id = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    Log.e("DeckAssitUserID", "DeckAssitUserID " + id);
                    status_req_cee = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    server_status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    server_status = true;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                //		progressDialog2.dismiss();
                fl_vcl_loading.setVisibility(View.GONE);
                if (server_status) {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                    server_status = false;
                }
                if (status_req_cee) {
                    status_req_cee = false;
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "Request Send", "Request send to cee staff", "Ok");
                } else {

                }
            }
        }


        ProgressDialog progressDialog3;

        public class InsertDeskAssistUser_forAquatics extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
                //		progressDialog3 = new ProgressDialog(ViewCurrentLessonActivity.this);
                //		progressDialog3.setMessage(Html.fromHtml("Loading wait..."));
                //		progressDialog3.setIndeterminate(true);
                //		progressDialog3.setCancelable(false);
                //		progressDialog3.show();
                fl_vcl_loading.setVisibility(View.VISIBLE);
                fl_vcl_loading.bringToFront();
            }

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                        SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssistUser);
                request.addProperty("token", WW_StaticClass.UserToken);
                request.addProperty("RDeckAssistID", DeskAssistID_web);
                request.addProperty("REmpType", emp_type_for_aquatics);
                request.addProperty("RUserID", emp_userid_for_aquatics);


                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11); // Make an Envelop for sending as whole
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                Log.i("Request", "Request = " + request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        SOAP_CONSTANTS.SOAP_ADDRESS);
                try {
                    androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertDeckAssistUser,
                            envelope); // Calling Web service
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    Log.i("Insert Desk Assist User", "Result : " + response.toString());
                    String rep = response.toString();
                    JSONObject jsonObject = new JSONObject(rep);
                    JSONArray jsonObject2 = jsonObject.getJSONArray("DeckAssitUserID");
                    String id = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                    Log.e("DeckAssitUserID", "DeckAssitUserID " + id);
                    status_req_aqu = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    server_status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    server_status = true;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                //		progressDialog3.dismiss();
                fl_vcl_loading.setVisibility(View.GONE);
                if (server_status) {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                    server_status = false;
                }
                if (status_req_aqu) {
                    status_req_aqu = false;
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "Request Send", "Request send to aquatics manager", "Ok");
                } else {

                }
            }

        }
    }

    ProgressDialog progressDialog4;

    public class InsertDeskAssistUser_forCEEManager extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //		progressDialog4 = new ProgressDialog(ViewCurrentLessonActivity.this);
            //		progressDialog4.setMessage(Html.fromHtml("Loading wait..."));
            //		progressDialog4.setIndeterminate(true);
            //		progressDialog4.setCancelable(false);
            //		progressDialog4.show();
            fl_vcl_loading.setVisibility(View.VISIBLE);
            fl_vcl_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssistUser);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("RDeckAssistID", DeskAssistID_web);
            request.addProperty("REmpType", emp_type_for_cee_manager);
            request.addProperty("RUserID", emp_userid_for_cee_manager);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertDeckAssistUser,
                        envelope); // Calling Web service
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                Log.i("Insert Desk Assist User", "Result : " + response.toString());
                String rep = response.toString();
                JSONObject jsonObject = new JSONObject(rep);
                JSONArray jsonObject2 = jsonObject.getJSONArray("DeckAssitUserID");
                String id = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                Log.e("DeckAssitUserID", "DeckAssitUserID " + id);
                status_req_cee_manager = true;
            } catch (JSONException e) {
                e.printStackTrace();
                server_status = true;
            } catch (Exception e) {
                e.printStackTrace();
                server_status = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //		progressDialog4.dismiss();
            fl_vcl_loading.setVisibility(View.GONE);
            if (server_status) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                server_status = false;
            }
            if (status_req_cee_manager) {
                status_req_cee_manager = false;
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "Request Send", "Request send to cee manager", "Ok");
            } else {

            }
        }
    }

    public void OpenCamera() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == CAMERA_PIC_REQUEST) {
                //2
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                //3
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                //4
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + filename + ".png");
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
                Log.i(TAG, "Abc = " + abc);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void RequestDeck() {
        deck_click = true;
        new IamInPool().execute();
        //		new DeskdataforCEE().execute();
        //		new DeskdataforCEEManager().execute();
        //		new DeskdataforAquaticsManager().execute();
    }

    /*	@Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventaction = event.getAction();

        switch (eventaction) {

        case MotionEvent.ACTION_DOWN:
            isTouch = true;
            break;

        case MotionEvent.ACTION_UP:
//	    	slideToLeft();
//	    	slideToRight();
            break;
        }
        return true;

    }*/
	/*public void slideToRight(){
		final TranslateAnimation animate = new TranslateAnimation(0,prev.getWidth(),0,0);
		animate.setDuration(500);
		animate.setFillAfter(true);
		prev.startAnimation(animate);
		next.postDelayed(new Runnable() { public void run() { prev1.setVisibility(View.VISIBLE);} }, 500);
		final TranslateAnimation animate1 = new TranslateAnimation(prev.getWidth(),0,0,0);
		animate1.setDuration(500);
		animate1.setFillAfter(true);
		prev.postDelayed(new Runnable() { public void run() { prev.startAnimation(animate1);
		prev1.setVisibility(View.GONE);} }, 5000);

	}
	public void slideToLeft(){
		final TranslateAnimation animate = new TranslateAnimation(0,-next.getWidth(),0,0);
		animate.setDuration(500);
		animate.setFillAfter(true);
		next.startAnimation(animate);
		next.postDelayed(new Runnable() { public void run() { next1.setVisibility(View.VISIBLE);} }, 500);
		final TranslateAnimation animate1 = new TranslateAnimation(-next.getWidth(),0,0,0);
		animate1.setDuration(500);
		animate1.setFillAfter(true);
		next.postDelayed(new Runnable() { public void run() { next.startAnimation(animate1);
		next1.setVisibility(View.GONE);} }, 5000);
	} */
    private void ClearArray() {
        IsShowSmCampStatus.clear();
        ISAAlert.clear();
        SiteID.clear();
        SLevel_ID.clear();
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
        SFirstName.clear();
        SLastName.clear();
        StudentID.clear();
        ShowWBR.clear();
        wu_b.clear();
        SScheduleID.clear();
        OrderDetailID.clear();
        PaidClasses.clear();
        Cls_Lvl.clear();
        Lvl_Adv_Avail.clear();
        StTimeHour.clear();
        StTimeMin.clear();
        wu_comments.clear();
        MainScheduleDate.clear();
        SkillsCount.clear();
        StudentGender.clear();
        wu_Next.clear();
        wu_Prev.clear();
        NewStudent.clear();
        att.clear();
        wu_attendancetaken.clear();
        Abbr.clear();
        PreReqID.clear();
        PreReqChecked.clear();
        att_Items.clear();
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


    public AlertDialog ConnectionTimeOut() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        builder1.setMessage("Connection timeout.")
                .setTitle("WaterWorks.")

                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        new GetAttendance().execute();
                    }
                });
        return builder1.create();
    }


	/*private class AT extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			pDialog = new ProgressDialog(ViewCurrentLessonActivity.this);
//			pDialog.setMessage(Html.fromHtml("Loading wait..."));
//			pDialog.setIndeterminate(true);
//			pDialog.setCancelable(false);
//			pDialog.show();
			fl_vcl_loading.setVisibility(View.VISIBLE);
			fl_vcl_loading.bringToFront();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetAttendanceList);
			request.addProperty("token",   WW_StaticClass.UserToken);
			request.addProperty("Rinstructorid",WW_StaticClass.InstructorID);
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
				ISAFlag = jo.getBoolean("ISAFlag");
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
						IsShowSmCampStatus.add(jsonObject2.getString("IsShowSmCampStatus"));
						ISAAlert.add(jsonObject2.getString("ISAAlert"));
						SiteID.add(jsonObject2.getString("SiteID"));
						SLevel_ID.add(jsonObject2.getString("SLevel"));
						wu_W.add(jsonObject2.getString("wu_W"));
						ScheLevel.add(jsonObject2.getString("ScheLevel"));
						SwimComp.add(jsonObject2.getString("SwimComp"));
						LessonName.add(jsonObject2.getString("LessonName"));
						lessontypeid.add(jsonObject2.getString("lessontypeid"));
						IScheduleID.add(jsonObject2.getString("IScheduleID"));
						SAge.add(jsonObject2.getString("SAge"));
						ParentFirstName.add(jsonObject2.getString("ParentFirstName"));
						ParentLastName.add(jsonObject2.getString("ParentLastName"));
						BirthDay.add(jsonObject2.getString("BirthDay"));
						Comments.add(jsonObject2.getString("Comments"));
						wu_r.add(jsonObject2.getString("wu_r"));
						SFirstName.add(jsonObject2.getString("SFirstName"));
						SLastName.add(jsonObject2.getString("SLastName"));
						StudentID.add(jsonObject2.getString("StudentID"));
						ShowWBR.add(jsonObject2.getString("ShowWBR"));
						wu_b.add(jsonObject2.getString("wu_b"));
						SScheduleID.add(jsonObject2.getString("SScheduleID"));
						OrderDetailID.add(jsonObject2.getString("OrderDetailID"));
						PaidClasses.add(jsonObject2.getString("PaidClasses"));
						Cls_Lvl.add(jsonObject2.getString("ClsLvl"));
						Lvl_Adv_Avail.add(jsonObject2.getString("LvlAdvAvail"));
						StTimeHour.add(jsonObject2.getString("StTimeHour"));
						StTimeMin.add(jsonObject2.getString("StTimeMin"));
						wu_comments.add(jsonObject2.getString("wu_comments"));
						MainScheduleDate.add(jsonObject2.getString("MainScheduleDate"));
						SkillsCount.add(jsonObject2.getString("SkillsCount"));
						StudentGender.add(jsonObject2.getString("StudentGender"));
						wu_Next.add(jsonObject2.getString("wu_Next"));
						wu_Prev.add(jsonObject2.getString("wu_Prev"));
						NewStudent.add(jsonObject2.optBoolean("NewUser"));
						if(StudentID.get(0).toString().equalsIgnoreCase("")){

						}
						else{
							att.add(jsonObject2.getInt("att"));
							wu_attendancetaken.add(jsonObject2.getInt("wu_attendancetaken"));
						jArray3 = jsonObject2.getJSONArray("SkillsList");

						ArrayList<String> tempabbr = new ArrayList<String>();
						ArrayList<String> tempPreReqId = new ArrayList<String>();
						ArrayList<String> tempPreReqChecked = new ArrayList<String>();
						for (int b = 0; b < jArray3.length(); b++) {
							jsonObject3 = jArray3.getJSONObject(b);
							String abbr = jsonObject3.getString("Abbr");
							String prereqid = jsonObject3.getString("PreReqID");
							String prereqchecked = jsonObject3.getString("PreReqChecked");

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
				Log.i(TAG, "new student = "+NewStudent);
				}
			}
			catch(JSONException e){
				server_status = true;
				e.printStackTrace();
			}
			catch (SocketTimeoutException e) {
				// TODO: handle exception
				e.printStackTrace();
				connectionout = true;
			}
			catch(Exception e)
			{
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
//			pDialog.dismiss();
			fl_vcl_loading.setVisibility(View.GONE);

			try{
				if(server_status){
					server_status = false;
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							ViewCurrentLessonActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				}
				else if(connectionout){
					connectionout = false;
					ConnectionTimeOut().show();
				}
				else{
					if(ISAFlag){
						dayisa.setVisibility(View.VISIBLE);
						dayisa.startAnimation(animBlink);
					}
					else{
						dayisa.setVisibility(View.INVISIBLE);
					}
					if(wu_avail==0){
						tv_view_current_lesson_error.setVisibility(View.VISIBLE);
						tv_view_current_lesson_error.setText("No availability at this time.");
						AlertDialog alertDialog = new AlertDialog.Builder(ViewCurrentLessonActivity.this).create();
						 alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						alertDialog.setTitle("WaterWorks");
						alertDialog.setCanceledOnTouchOutside(false); 
						alertDialog.setCancelable(false);
						// set the message
						alertDialog.setMessage("No availability at this time.");
						alertDialog.setIcon(R.drawable.ic_launcher);
						// set button1 functionality
						alertDialog.setButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// close dialog

										dialog.cancel();
										t.interrupt();
										finish();

									}
								});

						// show the alert dialog
						alertDialog.show();
						btnnext.setEnabled(false);
						btnprev.setEnabled(false);
					}else{
				if(!LessonName.isEmpty()&&!LessonName.get(0).equalsIgnoreCase("")){
					tv_view_current_lesson_error.setVisibility(View.GONE);
					btn_footer_send_attendance.setVisibility(View.VISIBLE);
				tv_lessonname.setText(LessonName.get(0).toString());
				setScheduleTime();
				for(int i=0;i<StudentID.size();i++){
					att_Items.add(new AttendanceItems(LevelName
							, LevelID,IsShowSmCampStatus.get(i) ,ISAAlert.get(i), Cls_Lvl.get(i), Lvl_Adv_Avail.get(i), SiteID.get(i),
							SLevel_ID.get(i), ScheLevel.get(i), wu_W.get(i), wu_b.get(i), wu_r.get(i),
							SwimComp.get(i), BirthDay.get(i), LessonName.get(i), lessontypeid.get(i), IScheduleID.get(i),
							SAge.get(i), ParentFirstName.get(i), ParentLastName.get(i), Comments.get(i), wu_comments.get(i),
							SFirstName.get(i), SLastName.get(i), StudentID.get(i), ShowWBR.get(i), SScheduleID.get(i),
							OrderDetailID.get(i), PaidClasses.get(i), StTimeHour.get(i), StTimeMin.get(i),
							MainScheduleDate.get(i), SkillsCount.get(i), PreReqID.get(i), PreReqChecked.get(i), Abbr.get(i),StudentGender.get(i),
							NewStudent.get(i),yes_no_date,btn_footer_send_attendance,
							wu_attendancetaken.get(i),att.get(i),ViewCurrentLessonActivity.this));
				}
//				adapter_Att = new AttandanceAdapter(ViewCurrentLessonActivity.this,
//						att_Items);
				adapter_Att.notifyDataSetChanged();
				listView_currentlessonData.setAdapter(adapter_Att);
				}
				else{
					tv_view_current_lesson_error.setVisibility(View.VISIBLE);
					tv_view_current_lesson_error.setText("No lesson at this time.");
					btn_footer_send_attendance.setVisibility(View.GONE);
					AlertDialog alertDialog = new AlertDialog.Builder(ViewCurrentLessonActivity.this).create();
					 alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alertDialog.setTitle("WaterWorks");
					alertDialog.setCanceledOnTouchOutside(false); 
					alertDialog.setCancelable(false);
					// set the message
					alertDialog.setMessage("No lesson at this time.");
					alertDialog.setIcon(R.drawable.ic_launcher);
					// set button1 functionality
					alertDialog.setButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// close dialog

									dialog.cancel();
//									finish();

								}
							});

					// show the alert dialog
					alertDialog.show();

				}
				}
				}
			}
			catch(ArrayIndexOutOfBoundsException e){
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	 */

    private class GetISAAlert extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //			fl_vcl_loading.setVisibility(View.VISIBLE);
            //			fl_vcl_loading.bringToFront();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            currentDateandTime = format.format(date);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_GetISAAlert);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("Rinstructorid", WW_StaticClass.InstructorID);
            request.addProperty("strRScheDate", currentDateandTime);
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
                Log.i(TAG, "Respose isa = " + response);
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    getIsa = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    JSONObject jo = new JSONObject(mSoapObject3.toString());
                    JSONArray jArray = jo.getJSONArray("ISAAlert");
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        ISAFlag = jsonObject.getBoolean("ISAFlag");
                    }
                } else {
                    getIsa = false;
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                connectionout = true;
            } catch (SocketException e) {
                // TODO: handle exception
                e.printStackTrace();
                connectionout = true;
            } catch (Exception e) {
                server_status = true;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //			fl_vcl_loading.setVisibility(View.GONE);
            if (server_status) {
                server_status = false;
                onDetectNetworkState().show();
            } else if (connectionout) {
                connectionout = false;
                new GetISAAlert().execute();
            } else if (getIsa) {
                getIsa = false;
                if (ISAFlag) {
                    dayisa.setVisibility(View.VISIBLE);
                    dayisa.startAnimation(animBlink);
                    ISAFlag = false;
                } else {
                    dayisa.clearAnimation();
                    dayisa.setVisibility(View.INVISIBLE);

                }
            } else {
                dayisa.clearAnimation();
                dayisa.setVisibility(View.INVISIBLE);
            }
        }
    }
}


