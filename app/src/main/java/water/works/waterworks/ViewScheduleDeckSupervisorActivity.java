package water.works.waterworks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
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

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import water.works.waterworks.Swipe.BaseSwipeListViewListener;
import water.works.waterworks.Swipe.SwipeListView;
import water.works.waterworks.adapter.ViewScheduleDeckSupervisorAdapter;
import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.model.ViewScheduleRow;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ViewScheduleDeckSupervisorActivity extends Activity implements OnClickListener {
    public static boolean yesitiszero = false, hasstart = false;

    public static final String TAG = "View Schdeule Activity";
    Boolean isInternetPresent = false, pool_status = false, status = false, status_cee = false,
            status_cee_manager = false, status_aquatics = false, status_shadow = false;
    String m, d, day_name;
    Button btn_home, btn_back;
    ImageButton ib_vs_request_deck;
    TextView tv_name, tv_day, tv_date;
    boolean server_status = false, dataload = false, connectionout = false, status_req_cee = false,
            status_req_cee_manager = false, status_req_aqu = false, getlevel = false, hasdone = false;
    ArrayList<String> SScheduleID, IScheduleID, SiteID, MainScheduleDate,
            ScheduleDay, FormateStTimeHour, FormateStTimeMin, StTimeMin, StTimeHour, wu_InstructorID, wu_InstructorName,
            starttime, endtime;
    ArrayList<ArrayList<String>> SLastName, SFirstName, wu_Prev, wu_Next,
            ParentFirstName, ParentLastName, PaidClasses, ClsLvl, wu_comments, StudentGender, MemStatus, wu_attendancetaken,
            SAge, SLevel, ScheLevel, StudentID, ISAAlert, att, InstructorID, InstructorName, lessontypeid;

    String mydatetime = "", tempday, tempmonth, tempyear, tempmin, temphour, tempampm;
    int prevcount = 0, nextcount = 0;
    ///////////////Level List
    ArrayList<String> LevelName = new ArrayList<String>();
    ArrayList<String> LevelID = new ArrayList<String>();
    String currentDateandTime;
    SwipeListView swipelistview;
    ViewScheduleDeckSupervisorAdapter adapter;
    List<ViewScheduleRow> itemData;
    float width;
    //	Titanic titanic;
//	TitanicTextView tv ;
    //change by megha 23-01/2018
    ArrayList<Integer> sttimehr = new ArrayList<Integer>();
    ArrayList<Integer> endtimehr = new ArrayList<Integer>();
    ArrayList<Integer> sttimemin = new ArrayList<Integer>();
    ArrayList<Integer> endtimemin = new ArrayList<Integer>();
    ShimmerTextView tv;
    Shimmer shimmer;
    public static FrameLayout vs_loading;
    int viewpos;
    int wu_avail = 2;
    String temp_date, temp_time_min, temp_time_hour, temp_instid, temp_istname, mytime;
    int oldmin, newmin, oldmil, newmil, count = 1;
    String am_pm;
    Date noteTS;
    String time, FROM = "";
    Thread t;
    LinearLayout ll_title;
    TextView lv_title;
    String tempStHr, tempEndHr, tempStmin, tempEndmin;
    public static ArrayList<String> ManagerShadowID = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule_deck_supervisor);
        ManagerShadowID.clear();
        isInternetPresent = Utility
                .isNetworkConnected(ViewScheduleDeckSupervisorActivity.this);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            FROM = getIntent().getStringExtra("FROM");
            SetUp();
            Initialize();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            currentDateandTime = format.format(date);
            WW_StaticClass.duration1 = 300;
            WW_StaticClass.duration2 = 1000;
//			 titanic = new Titanic();
//			 titanic.start(tv);
            shimmer = new Shimmer();
            shimmer.start(tv);
            new GetLevel().execute();
            swipelistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
                @Override
                public void onOpened(int position, boolean toRight) {
                    Log.e(TAG, "onopen");
                    if (toRight) {
                        prevcount++;
                        nextcount--;
                        if (prevcount == nextcount) {
                            yesitiszero = true;
                        } else {
                            yesitiszero = false;
                        }

                        if (prevcount == 1 && nextcount == -1) {
                            if (!hasdone) {
                                if (ManagerShadowID.size() > 0) {
                                    new EndtimeOfAll().execute();
                                }
                            }
                        }
                        Log.i(TAG, "Prev count = " + prevcount + "\nNext count = " + nextcount);
                        viewpos = position;
                        int month, year, day, min, hour;
                        String date = adapter.getItem(viewpos).getMainScheduleDate();
                        String tempdate[] = date.toString().split("\\/");
                        month = Integer.parseInt(tempdate[0]);
                        day = Integer.parseInt(tempdate[1]);
                        year = Integer.parseInt(tempdate[2]);
                        hour = Integer.parseInt(adapter.getItem(viewpos).getStTimeHour());
                        min = Integer.parseInt(adapter.getItem(viewpos).getStTimeMin());
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
                        temp_instid = adapter.getItem(viewpos).getWu_InstructorID();
                        temp_istname = adapter.getItem(viewpos).getWu_InstructorName();
                        temp_date = tempmonth + "/" + tempday + "/" + tempyear;
                        temp_time_hour = temphour;
                        temp_time_min = tempmin;
                        mydatetime = tempmonth + "/" + tempday + "/" + tempyear + " " + temphour + ":" + tempmin + " " + am_pm;
                        Log.i(TAG, mydatetime);
                        swipelistview.closeOpenedItems();
                        new PrevNextData().execute();
                    } else {
                        nextcount++;
                        prevcount--;
                        if (nextcount == prevcount) {
                            yesitiszero = true;
                        } else {
                            yesitiszero = false;
                        }

                        if (nextcount == 1 && prevcount == -1) {
                            if (!hasdone) {
                                if (ManagerShadowID.size() > 0) {
                                    new EndtimeOfAll().execute();
                                }
                            }
                        }
                        Log.i(TAG, "Prev count = " + prevcount + "\nNext count = " + nextcount);
                        viewpos = position;
                        int month, year, day, min, hour;
                        String date = adapter.getItem(viewpos).getMainScheduleDate();
                        String tempdate[] = date.toString().split("\\/");
                        month = Integer.parseInt(tempdate[0]);
                        day = Integer.parseInt(tempdate[1]);
                        year = Integer.parseInt(tempdate[2]);
                        hour = Integer.parseInt(adapter.getItem(viewpos).getStTimeHour());
                        min = Integer.parseInt(adapter.getItem(viewpos).getStTimeMin());
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
                        temp_instid = adapter.getItem(viewpos).getWu_InstructorID();
                        temp_istname = adapter.getItem(viewpos).getWu_InstructorName();
                        temp_date = tempmonth + "/" + tempday + "/" + tempyear;
                        temp_time_hour = temphour;
                        temp_time_min = tempmin;
                        mydatetime = tempmonth + "/" + tempday + "/" + tempyear + " " + temphour + ":" + tempmin + " " + am_pm;
                        swipelistview.closeOpenedItems();
                        Log.i(TAG, mydatetime);
                        new PrevNextData().execute();
                    }
                }

                @Override
                public void onClosed(int position, boolean fromRight) {
                }

                @Override
                public void onListChanged() {
                }

                @Override
                public void onMove(int position, float x) {
                }

                @Override
                public void onStartOpen(int position, int action, boolean right) {
                }

                @Override
                public void onStartClose(int position, boolean right) {
                }

                @Override
                public void onClickFrontView(int position) {
                }

                @Override
                public void onClickBackView(int position) {
                }

                @Override
                public void onDismiss(int[] reverseSortedPositions) {

                }


            });

        }
        oldmin = Calendar.getInstance().getTime().getMinutes();
    }

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }


    private void Initialize() {
        // TODO Auto-generated method stub
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        lv_title = (TextView) findViewById(R.id.tv_mvcs_title);
        btn_home = (Button) findViewById(R.id.btn_vs_home);
        btn_back = (Button) findViewById(R.id.btn_back);
        if (FROM.toString().equalsIgnoreCase("DECK")) {
            lv_title.setVisibility(View.GONE);
            ll_title.setVisibility(View.VISIBLE);
            btn_back.setVisibility(View.GONE);
            btn_home.setVisibility(View.VISIBLE);
        } else if (FROM.toString().equalsIgnoreCase("MANAGER")) {
            lv_title.setVisibility(View.VISIBLE);
            ll_title.setVisibility(View.GONE);
            btn_back.setVisibility(View.VISIBLE);
            btn_home.setVisibility(View.GONE);
        }
        tv = (ShimmerTextView) findViewById(R.id.my_text_view);
        tv.setTypeface(Typefaces.get(ViewScheduleDeckSupervisorActivity.this, "Satisfy-Regular.ttf"));
        vs_loading = (FrameLayout) findViewById(R.id.vs_loading);
        vs_loading.setVisibility(View.GONE);
        ib_vs_request_deck = (ImageButton) findViewById(R.id.ib_vs_request_deck);
        ib_vs_request_deck.setOnClickListener(this);
        tv_date = (TextView) findViewById(R.id.tv_vs_date);
        tv_day = (TextView) findViewById(R.id.tv_vs_day);
        tv_name = (TextView) findViewById(R.id.tv_vs_name);
        tv_name.setText(WW_StaticClass.UserName);
        tv_date.setText(m + "/" + d);
        tv_day.setText(day_name);
        swipelistview = (SwipeListView) findViewById(R.id.example_swipe_lv_list);
        itemData = new ArrayList<ViewScheduleRow>();
        if (FROM.toString().equalsIgnoreCase("DECK")) {
            adapter = new ViewScheduleDeckSupervisorAdapter(this, R.layout.view_schedule_deck_supervisor_item, itemData);
        } else if (FROM.toString().equalsIgnoreCase("MANAGER")) {
            adapter = new ViewScheduleDeckSupervisorAdapter(this, R.layout.view_current_lesson_manager_item, itemData);
        }
        swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH); // there are five swiping modes
        swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions 
        swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        swipelistview.setOffsetLeft(convertDpToPixel(width / 2)); // left side offset
        swipelistview.setOffsetRight(convertDpToPixel(width / 2)); // right side offset
        swipelistview.setAnimationTime(500); // Animation time
        swipelistview.setSwipeOpenOnLongPress(false); // enable or disable SwipeOpenOnLongPress

        ArrayListing();
    }

    private void ArrayListing() {
        // TODO Auto-generated method stub
        ISAAlert = new ArrayList<ArrayList<String>>();
        IScheduleID = new ArrayList<String>();
        SiteID = new ArrayList<String>();
        wu_InstructorID = new ArrayList<String>();
        wu_InstructorName = new ArrayList<String>();
        MainScheduleDate = new ArrayList<String>();
        InstructorID = new ArrayList<ArrayList<String>>();
        InstructorName = new ArrayList<ArrayList<String>>();
        lessontypeid = new ArrayList<ArrayList<String>>();
        FormateStTimeHour = new ArrayList<String>();
        FormateStTimeMin = new ArrayList<String>();
        SAge = new ArrayList<ArrayList<String>>();
        SLevel = new ArrayList<ArrayList<String>>();
        ScheLevel = new ArrayList<ArrayList<String>>();
        StudentID = new ArrayList<ArrayList<String>>();
        ScheduleDay = new ArrayList<String>();
        SLastName = new ArrayList<ArrayList<String>>();
        SFirstName = new ArrayList<ArrayList<String>>();
        ParentFirstName = new ArrayList<ArrayList<String>>();
        ParentLastName = new ArrayList<ArrayList<String>>();
        PaidClasses = new ArrayList<ArrayList<String>>();
        ClsLvl = new ArrayList<ArrayList<String>>();
        wu_comments = new ArrayList<ArrayList<String>>();
        StudentGender = new ArrayList<ArrayList<String>>();
        MemStatus = new ArrayList<ArrayList<String>>();
        att = new ArrayList<ArrayList<String>>();
        StTimeHour = new ArrayList<String>();
        StTimeMin = new ArrayList<String>();
        wu_attendancetaken = new ArrayList<ArrayList<String>>();
        starttime = new ArrayList<String>();
        endtime = new ArrayList<String>();
    }

    private void SetUp() {
        // TODO Auto-generated method stub

        Calendar c = Calendar.getInstance();
        int Day_Name = c.get(Calendar.DAY_OF_WEEK);
        int Date = c.get(Calendar.DATE);
        int Month = c.get(Calendar.MONTH);
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
        java.util.Date date = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        mytime = format1.format(date);

        t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {

                            public void run() {
                                newmin = Calendar.getInstance().getTime().getMinutes();
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
//						        System.out.println("New Date--->" + format1.format(date));
                                mytime = format1.format(date);
                                time = "hh:mm"; // 12:00
                                oldmil = 0;
                                if (newmin == 0 || newmin == 20 || newmin == 40) {
                                    newmil = Calendar.getInstance().getTime().getSeconds();
                                    if (count == 1 && newmil == oldmil) {
                                        AfterMinRefresh();
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

    protected void AfterMinRefresh() {
        if (FROM.toString().equalsIgnoreCase("MANAGER")) {
            if (!hasdone) {
                if (ManagerShadowID.size() > 0) {
                    new EndtimeOfAll().execute();
                }
                recreate();
            } else {
                recreate();
            }
        } else {
            //change by megha 24-01-2018
//            recreate();
        }
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
        builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        t.interrupt();
                        InstructorSelectionForMVCSActivity.final_list.clear();
                        InstructorSelectionForMVCSActivity.final_list_name.clear();
                        ManagerShadowID.clear();
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
                .isNetworkConnected(ViewScheduleDeckSupervisorActivity.this);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        t.interrupt();
        InstructorSelectionForMVCSActivity.final_list.clear();
        InstructorSelectionForMVCSActivity.final_list_name.clear();
        if (FROM.toString().equalsIgnoreCase("MANAGER")) {
            if (!hasdone) {
                if (ManagerShadowID.size() > 0) {
                    new EndtimeOfAll().execute();
                }
            }
        }
        ManagerShadowID.clear();
        finish();
    }

    //////////////Get level list/////////////
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

            } catch (SocketException e) {
                e.printStackTrace();
                connectionout = true;
            } catch (JSONException e) {
                e.printStackTrace();
                server_status = false;
            } catch (Exception e) {
                e.printStackTrace();
                server_status = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (server_status) {
                server_status = false;
                onDetectNetworkState().show();
            } else if (connectionout) {
                connectionout = false;
                new GetLevel().execute();
            } else {
                if (getlevel) {
                    getlevel = false;
                    new GetViewSchedule().execute();
                }
            }
        }
    }

    private class GetViewSchedule extends AsyncTask<Void, Void, Void> {
        String method, action;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            vs_loading.setVisibility(View.VISIBLE);
            if (FROM.toString().equalsIgnoreCase("DECK")) {
                method = SOAP_CONSTANTS.METHOD_NAME_ViewSchl_GetViewScheduleByDeckSupervisorAllInstr;
                action = SOAP_CONSTANTS.SOAP_Action_ViewSchl_GetViewScheduleByDeckSupervisorAllInstr;

            } else if (FROM.toString().equalsIgnoreCase("MANAGER")) {
                method = SOAP_CONSTANTS.METHOD_NAME_ViewSchl_GetViewScheduleByDeckSupervisorSelectedInstr;
                action = SOAP_CONSTANTS.SOAP_Action_ViewSchl_GetViewScheduleByDeckSupervisorSelectedInstr;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    method);
            if (FROM.toString().equalsIgnoreCase("DECK")) {
                request.addProperty("token", WW_StaticClass.UserToken);
                request.addProperty("strRScheDate", currentDateandTime);
                request.addProperty("strSite", WW_StaticClass.siteid.toString().replaceAll("\\[", "").replaceAll("\\]", "").trim());
            } else if (FROM.toString().equalsIgnoreCase("MANAGER")) {
                request.addProperty("token", WW_StaticClass.UserToken);
                request.addProperty("strRScheDate", currentDateandTime);
                request.addProperty("strSite", InstructorSelectionForMVCSActivity.temp_siteid);
                request.addProperty("strInstructor", InstructorSelectionForMVCSActivity.final_list.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                request.addProperty("MgrId", WW_StaticClass.InstructorID);
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(action,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);

                if (code.equals("000")) {
                    dataload = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    String resp1 = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp1);

                    JSONArray jArray = jo.getJSONArray("Attendance");
                    Log.i(TAG, "jArray : " + jArray.toString());

                    JSONObject jsonObject;
                    JSONObject jsonObject2, jsonObject3;
                    JSONArray jArray2;
                    JSONArray jArray3 = null;
                    for (int k = 0; k < jArray.length(); k++) {
                        jsonObject = jArray.getJSONObject(k);
                        Log.i(TAG, "jsonObject: " + jsonObject.toString());
                        jArray2 = jsonObject.getJSONArray("Schedule1");
                        Log.i(TAG, "jArray2 : " + jArray2.toString());
                        for (int i = 0; i < jArray2.length(); i++) {
                            jsonObject2 = jArray2.getJSONObject(i);
                            IScheduleID.add(jsonObject2.getString("IScheduleID"));
                            SiteID.add(jsonObject2.getString("SiteID"));
                            MainScheduleDate.add(jsonObject2.getString("MainScheduleDate"));
                            ScheduleDay.add(jsonObject2.getString("ScheduleDay"));
                            FormateStTimeHour.add(jsonObject2.getString("FormateStTimeHour"));
                            FormateStTimeMin.add(jsonObject2.getString("FormatStTimeMin"));
                            StTimeHour.add(jsonObject2.getString("StTimeHour"));
                            StTimeMin.add(jsonObject2.getString("StTimeMin"));
                            wu_InstructorID.add(jsonObject2.getString("wu_InstructorID"));
                            wu_InstructorName.add(jsonObject2.getString("wu_InstructorName"));
                            sttimehr.add(Integer.valueOf(jsonObject2.getString("StTimeHour")));
                            endtimehr.add(Integer.valueOf(jsonObject2.getString("EndTimeHour")));
                            sttimemin.add(Integer.valueOf(jsonObject2.getString("StTimeMin")));
                            endtimemin.add(Integer.valueOf(jsonObject2.getString("EndTimeMin")));
                            if (jsonObject2.getString("ShadowTimeSlot").toString().equalsIgnoreCase("|")) {
                                starttime.add("");
                                endtime.add("");
                            } else {
                                String[] shadowtime = jsonObject2.getString("ShadowTimeSlot").toString().split("\\|");
                                String newDateStr[] = new String[shadowtime.length];
                                for (int j = 0; j < shadowtime.length; j++) {
                                    SimpleDateFormat form = new SimpleDateFormat(
                                            "yyyy-MM-dd HH:mm:ss.SSS");
                                    Date date = null;
                                    try {
                                        date = form.parse(shadowtime[j]);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    SimpleDateFormat postFormater = new SimpleDateFormat(
                                            "MM/dd/yyyy hh:mm aa");
                                    newDateStr[j] = postFormater.format(date);
                                }
                                starttime.add(newDateStr[0]);
                                endtime.add(newDateStr[1]);
                            }
                            jArray3 = jsonObject2.getJSONArray("Student");
                            ArrayList<String> tempSLastName = new ArrayList<String>();
                            ArrayList<String> tempSFirstName = new ArrayList<String>();
                            ArrayList<String> tempSAge = new ArrayList<String>();
                            ArrayList<String> tempSLevel = new ArrayList<String>();
                            ArrayList<String> tempScheLevel = new ArrayList<String>();
                            ArrayList<String> tempStudentID = new ArrayList<String>();
                            ArrayList<String> tempParentFirstName = new ArrayList<String>();
                            ArrayList<String> tempParentLastName = new ArrayList<String>();
                            ArrayList<String> tempMemStatus = new ArrayList<String>();
                            ArrayList<String> tempPaidClasses = new ArrayList<String>();
                            ArrayList<String> tempClsLvl = new ArrayList<String>();
                            ArrayList<String> tempwu_comments = new ArrayList<String>();
                            ArrayList<String> tempStudentGender = new ArrayList<String>();
                            ArrayList<String> tempISAAlert = new ArrayList<String>();
                            ArrayList<String> tempAtt = new ArrayList<String>();
                            ArrayList<String> tempwu_attendancetaken = new ArrayList<String>();
                            ArrayList<String> tempInstructorID = new ArrayList<String>();
                            ArrayList<String> tempInstructorName = new ArrayList<String>();
                            ArrayList<String> templessontypeid = new ArrayList<String>();

                            for (int b = 0; b < jArray3.length(); b++) {
                                jsonObject3 = jArray3.getJSONObject(b);
                                tempSLastName.add(jsonObject3.getString("SLastName"));
                                tempSFirstName.add(jsonObject3.getString("SFirstName"));
                                tempSAge.add(jsonObject3.getString("SAge"));
                                tempSLevel.add(jsonObject3.getString("SLevel"));
                                tempScheLevel.add(jsonObject3.getString("ScheLevel"));
                                tempStudentID.add(jsonObject3.getString("StudentID"));
                                tempParentFirstName.add(jsonObject3.getString("ParentFirstName"));
                                tempParentLastName.add(jsonObject3.getString("ParentLastName"));
                                tempMemStatus.add(jsonObject3.getString("MemStatus"));
                                tempPaidClasses.add(jsonObject3.getString("PaidClasses"));
                                tempClsLvl.add(jsonObject3.getString("ClsLvl"));
                                tempwu_comments.add(jsonObject3.getString("wu_comments"));
                                tempStudentGender.add(jsonObject3.getString("StudentGender"));
                                tempISAAlert.add(jsonObject3.getString("ISAAlert"));
                                tempAtt.add(jsonObject3.getString("att"));
                                tempwu_attendancetaken.add(jsonObject3.getString("wu_attendancetaken"));
                                tempInstructorID.add(jsonObject3.getString("InstructorID"));
                                tempInstructorName.add(jsonObject3.getString("InstructorName"));
                                templessontypeid.add(jsonObject3.getString("lessontypeid"));

                            }
                            SLastName.add(tempSLastName);
                            SFirstName.add(tempSFirstName);
                            SAge.add(tempSAge);
                            SLevel.add(tempSLevel);
                            ScheLevel.add(tempScheLevel);
                            StudentID.add(tempStudentID);
                            ParentFirstName.add(tempParentFirstName);
                            ParentLastName.add(tempParentLastName);
                            MemStatus.add(tempMemStatus);
                            PaidClasses.add(tempPaidClasses);
                            ClsLvl.add(tempClsLvl);
                            wu_comments.add(tempwu_comments);
                            StudentGender.add(tempStudentGender);
                            ISAAlert.add(tempISAAlert);
                            att.add(tempAtt);
                            wu_attendancetaken.add(tempwu_attendancetaken);
                            InstructorID.add(tempInstructorID);
                            InstructorName.add(tempInstructorName);
                            lessontypeid.add(templessontypeid);

                        }
                    }
                } else {
                    dataload = false;
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

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (server_status) {
                server_status = false;
                vs_loading.setVisibility(View.GONE);
                onDetectNetworkState().show();
            } else if (connectionout) {
                connectionout = false;
                vs_loading.setVisibility(View.GONE);
                ConnectionTimeOut().show();
            } else {
                if (dataload) {
                    dataload = false;
                    yesitiszero = true;
                    swipelistview.setAdapter(adapter);
                    //code by megha 24/-01-2018
                    swipelistview.clearFocus();
                    Calendar c = Calendar.getInstance();

                    try {
                        for (int i = 0; i < StTimeHour.size(); i++) {

                            tempStHr = String.valueOf(sttimehr.get(i) + ":" + (sttimemin.get(i)));
                            if (c.get(Calendar.MINUTE) >= 0 && c.get(Calendar.MINUTE) < 20) {
                                tempEndHr = c.get(Calendar.HOUR_OF_DAY) + ":" + "0";
                            } else if (c.get(Calendar.MINUTE) >= 20 && c.get(Calendar.MINUTE) < 40) {
                                tempEndHr = c.get(Calendar.HOUR_OF_DAY) + ":" + "20";
                            } else if (c.get(Calendar.MINUTE) >= 40) {
                                tempEndHr = c.get(Calendar.HOUR_OF_DAY) + ":" + "40";
                            }
                            boolean check = checktimings(tempStHr, tempEndHr);

                            Log.d("tempStHr", tempStHr + "tempEndHr" + tempEndHr + "check" + check + "index" + i);
                            if (check) {
                                swipelistview.setSelection(i);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //-----------
                    for (int i = 0; i < InstructorID.size(); i++) {
                            /*itemData.add(new ViewScheduleRow(wu_InstructorID.get(i),wu_InstructorName.get(i),ISAAlert.get(i), SScheduleID.get(i), IScheduleID.get(i), SiteID.get(i), MainScheduleDate.get(i),
                                    ScheduleDay.get(i), FormateStTimeHour.get(i),
				        			FormateStTimeMin.get(i),StTimeHour.get(i),StTimeMin.get(i), SLastName.get(i), SFirstName.get(i), ParentFirstName.get(i), ParentLastName.get(i),
				        			PaidClasses.get(i), ClsLvl.get(i), wu_comments.get(i), StudentGender.get(i), MemStatus.get(i), SAge.get(i),
				        			SLevel.get(i), ScheLevel.get(i), StudentID.get(i),att.get(i),wu_attendancetaken.get(i),
				        			InstructorID.get(i), InstructorName.get(i), lessontypeid.get(i),wu_avail,LevelName,LevelID,FROM));*/
                        itemData.add(new ViewScheduleRow(wu_InstructorID.get(i), wu_InstructorName.get(i), ISAAlert.get(i), IScheduleID.get(i), SiteID.get(i), MainScheduleDate.get(i),
                                ScheduleDay.get(i), FormateStTimeHour.get(i),
                                FormateStTimeMin.get(i), StTimeHour.get(i), StTimeMin.get(i), SLastName.get(i), SFirstName.get(i), ParentFirstName.get(i), ParentLastName.get(i),
                                PaidClasses.get(i), ClsLvl.get(i), wu_comments.get(i), StudentGender.get(i), MemStatus.get(i), SAge.get(i),
                                SLevel.get(i), ScheLevel.get(i), StudentID.get(i), att.get(i), wu_attendancetaken.get(i),
                                InstructorID.get(i), InstructorName.get(i), lessontypeid.get(i), wu_avail, LevelName, LevelID, FROM,
                                starttime.get(i), endtime.get(i)));
                    }
                    adapter.notifyDataSetChanged();
                    vs_loading.setVisibility(View.GONE);

                } else {
                    vs_loading.setVisibility(View.GONE);
                    Toast.makeText(ViewScheduleDeckSupervisorActivity.this, "No lesson at this time", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    public boolean checktimings(String time, String endtime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);

            Log.d("date1", "" + date1);
            Log.d("date2", "" + date2);
            if (date1.equals(date2)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public AlertDialog ConnectionTimeOut() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ViewScheduleDeckSupervisorActivity.this);
        builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        builder1.setMessage("Connection timeout.")
                .setTitle("WaterWorks.")

                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        new GetViewSchedule().execute();
                    }
                });
        return builder1.create();
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_vs_home:
                t.interrupt();
                InstructorSelectionForMVCSActivity.final_list.clear();
                InstructorSelectionForMVCSActivity.final_list_name.clear();
                ManagerShadowID.clear();
                finish();
                break;
            case R.id.btn_back:
                t.interrupt();
                InstructorSelectionForMVCSActivity.final_list.clear();
                InstructorSelectionForMVCSActivity.final_list_name.clear();
                if (!hasdone) {
                    if (ManagerShadowID.size() > 0) {
                        new EndtimeOfAll().execute();
                    }
                }
                ManagerShadowID.clear();
                finish();
                break;
            case R.id.ib_vs_request_deck:
                new IamInPool().execute();
                break;
            default:
                break;
        }

    }

    ProgressDialog pDialog;

    private class PrevNextData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewScheduleDeckSupervisorActivity.this);
            pDialog.setMessage("Please wait...\nData updating...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_ViewSchl_GetViewScheduleByDeckSupervisorAndInstrWise);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("Rinstructorid", temp_instid);
            request.addProperty("strRScheDate", mydatetime);
            request.addProperty("MgrId", WW_StaticClass.InstructorID);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_ViewSchl_GetViewScheduleByDeckSupervisorAndInstrWise,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(TAG, "next prev = " + response);
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    dataload = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    String resp1 = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp1);
                    wu_avail = jo.getInt("wu_avail");

                    JSONArray jArray = jo.getJSONArray("Attendance");
                    Log.i(TAG, "jArray : " + jArray.toString());

                    JSONObject jsonObject;
                    JSONObject jsonObject2, jsonObject3;
                    JSONArray jArray2;
                    JSONArray jArray3 = null;
                    for (int k = 0; k < jArray.length(); k++) {
                        jsonObject = jArray.getJSONObject(k);
                        Log.i(TAG, "jsonObject: " + jsonObject.toString());
                        jArray2 = jsonObject.getJSONArray("Schedule1");
                        Log.i(TAG, "jArray2 : " + jArray2.toString());
                        for (int i = 0; i < jArray2.length(); i++) {
                            jsonObject2 = jArray2.getJSONObject(i);
                            adapter.getItem(viewpos).setIScheduleID(jsonObject2.getString("IScheduleID"));
                            adapter.getItem(viewpos).setSiteID(jsonObject2.getString("SiteID"));
                            adapter.getItem(viewpos).setMainScheduleDate(jsonObject2.getString("MainScheduleDate"));
                            adapter.getItem(viewpos).setScheduleDay(jsonObject2.getString("ScheduleDay"));
                            adapter.getItem(viewpos).setFormateStTimeHour(jsonObject2.getString("FormateStTimeHour"));
                            adapter.getItem(viewpos).setFormatStTimeMin(jsonObject2.getString("FormatStTimeMin"));
                            adapter.getItem(viewpos).setStTimeHour(jsonObject2.getString("StTimeHour"));
                            adapter.getItem(viewpos).setStTimeMin(jsonObject2.getString("StTimeMin"));
                            if (jsonObject2.getString("ShadowTimeSlot").toString().equalsIgnoreCase("|")) {
                                adapter.getItem(viewpos).setStarttime("");
                                adapter.getItem(viewpos).setEndtime("");
                            } else {
                                String[] shadowtime = jsonObject2.getString("ShadowTimeSlot").toString().split("\\|");
                                String newDateStr[] = new String[shadowtime.length];
                                for (int j = 0; j < shadowtime.length; j++) {
                                    SimpleDateFormat form = new SimpleDateFormat(
                                            "yyyy-MM-dd HH:mm:ss.SSS");
                                    Date date = null;
                                    try {
                                        date = form.parse(shadowtime[j]);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    SimpleDateFormat postFormater = new SimpleDateFormat(
                                            "MM/dd/yyyy hh:mm aa");
                                    newDateStr[j] = postFormater.format(date);
                                }
                                adapter.getItem(viewpos).setStarttime(newDateStr[0]);
                                adapter.getItem(viewpos).setEndtime(newDateStr[1]);
                            }
                            jArray3 = jsonObject2.getJSONArray("Student");
                            ArrayList<String> tempSLastName = new ArrayList<String>();
                            ArrayList<String> tempSFirstName = new ArrayList<String>();
                            ArrayList<String> tempSAge = new ArrayList<String>();
                            ArrayList<String> tempSLevel = new ArrayList<String>();
                            ArrayList<String> tempScheLevel = new ArrayList<String>();
                            ArrayList<String> tempStudentID = new ArrayList<String>();
                            ArrayList<String> tempParentFirstName = new ArrayList<String>();
                            ArrayList<String> tempParentLastName = new ArrayList<String>();
                            ArrayList<String> tempMemStatus = new ArrayList<String>();
                            ArrayList<String> tempPaidClasses = new ArrayList<String>();
                            ArrayList<String> tempClsLvl = new ArrayList<String>();
                            ArrayList<String> tempwu_comments = new ArrayList<String>();
                            ArrayList<String> tempStudentGender = new ArrayList<String>();
                            ArrayList<String> tempISAAlert = new ArrayList<String>();
                            ArrayList<String> tempAtt = new ArrayList<String>();
                            ArrayList<String> tempwu_attendancetaken = new ArrayList<String>();
                            ArrayList<String> tempInstructorID = new ArrayList<String>();
                            ArrayList<String> tempInstructorName = new ArrayList<String>();
                            ArrayList<String> templessontypeid = new ArrayList<String>();
                            for (int b = 0; b < jArray3.length(); b++) {
                                jsonObject3 = jArray3.getJSONObject(b);
                                tempSLastName.add(jsonObject3.getString("SLastName"));
                                tempSFirstName.add(jsonObject3.getString("SFirstName"));
                                tempSAge.add(jsonObject3.getString("SAge"));
                                tempSLevel.add(jsonObject3.getString("SLevel"));
                                tempScheLevel.add(jsonObject3.getString("ScheLevel"));
                                tempStudentID.add(jsonObject3.getString("StudentID"));
                                tempParentFirstName.add(jsonObject3.getString("ParentFirstName"));
                                tempParentLastName.add(jsonObject3.getString("ParentLastName"));
                                tempMemStatus.add(jsonObject3.getString("MemStatus"));
                                tempPaidClasses.add(jsonObject3.getString("PaidClasses"));
                                tempClsLvl.add(jsonObject3.getString("ClsLvl"));
                                tempwu_comments.add(jsonObject3.getString("wu_comments"));
                                tempStudentGender.add(jsonObject3.getString("StudentGender"));
                                tempISAAlert.add(jsonObject3.getString("ISAAlert"));
                                tempAtt.add(jsonObject3.getString("att"));
                                tempwu_attendancetaken.add(jsonObject3.getString("wu_attendancetaken"));
                                tempInstructorID.add(jsonObject3.getString("InstructorID"));
                                tempInstructorName.add(jsonObject3.getString("InstructorName"));
                                templessontypeid.add(jsonObject3.getString("lessontypeid"));

                            }
                            adapter.getItem(viewpos).setSLastName(tempSLastName);
                            adapter.getItem(viewpos).setSFirstName(tempSFirstName);
                            adapter.getItem(viewpos).setSAge(tempSAge);
                            adapter.getItem(viewpos).setSLevel(tempSLevel);
                            adapter.getItem(viewpos).setScheLevel(tempScheLevel);
                            adapter.getItem(viewpos).setStudentID(tempStudentID);
                            adapter.getItem(viewpos).setParentFirstName(tempParentFirstName);
                            adapter.getItem(viewpos).setParentLastName(tempParentLastName);
                            adapter.getItem(viewpos).setMemStatus(tempMemStatus);
                            adapter.getItem(viewpos).setPaidClasses(tempPaidClasses);
                            adapter.getItem(viewpos).setClsLvl(tempClsLvl);
                            adapter.getItem(viewpos).setWu_comments(tempwu_comments);
                            adapter.getItem(viewpos).setStudentGender(tempStudentGender);
                            adapter.getItem(viewpos).setISAAlert(tempISAAlert);
                            adapter.getItem(viewpos).setAtt(tempAtt);
                            adapter.getItem(viewpos).setWu_attendancetaken(tempwu_attendancetaken);
                            adapter.getItem(viewpos).setInstructorID(tempInstructorID);
                            adapter.getItem(viewpos).setInstructorName(tempInstructorName);
                            adapter.getItem(viewpos).setLessontypeid(templessontypeid);

                        }
                    }
                } else {
                    dataload = false;
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

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            if (server_status) {
                server_status = false;
                onDetectNetworkState().show();
            } else if (connectionout) {
                connectionout = false;
                ConnectionTimeOut().show();
            } else {
                if (dataload) {
                    dataload = false;
                    adapter.getItem(viewpos).setWu_avail(wu_avail);
                    if (wu_avail == 0) {
                        adapter.getItem(viewpos).setWu_InstructorID(temp_instid);
                        adapter.getItem(viewpos).setWu_InstructorName(temp_istname);
                        adapter.getItem(viewpos).setStTimeHour(temp_time_hour);
                        adapter.getItem(viewpos).setStTimeMin(temp_time_min);
                        adapter.getItem(viewpos).setMainScheduleDate(temp_date);
                        adapter.getItem(viewpos).setFormateStTimeHour(temp_time_hour);
                        adapter.getItem(viewpos).setFormatStTimeMin(temp_time_min);
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "WaterWorks", "No availability at this time.", "Ok");
                        adapter.notifyDataSetChanged();
                    } else if (wu_avail == 1) {
                        adapter.getItem(viewpos).setWu_InstructorID(temp_instid);
                        adapter.getItem(viewpos).setWu_InstructorName(temp_istname);
                        adapter.getItem(viewpos).setStTimeHour(temp_time_hour);
                        adapter.getItem(viewpos).setStTimeMin(temp_time_min);
                        adapter.getItem(viewpos).setFormateStTimeHour(temp_time_hour);
                        adapter.getItem(viewpos).setFormatStTimeMin(temp_time_min);
                        adapter.getItem(viewpos).setMainScheduleDate(temp_date);

                        SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "WaterWorks", "No leeson at this time.", "Ok");
                        adapter.notifyDataSetChanged();
                    } else if (wu_avail == 2) {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    onDetectNetworkState().show();
                }
            }
        }
    }

    ArrayList<String> PoolName, PoolId;
    Dialog dialog = null;
    String whattimeforassist = "-1", desk_poolid = "-1", DeskAssistID_web;
    String emp_type_for_cee = "", emp_type_for_cee_manager = "", emp_type_for_aquatics = "",
            emp_userid_for_cee = "", emp_userid_for_cee_manager = "", emp_userid_for_aquatics = "";

    public class IamInPool extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            WW_StaticClass.Siteid = SiteID.get(0);

        }

        @Override
        protected Void doInBackground(Void... params) {

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
//				response.toString();
                if (code.equals("000")) {
                    pool_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i("Current Lesson", "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();


//				String resp = envelope.getResponse().toString();// response.toString().trim();
                    Log.i("here", "Result : " + resp.toString());
                    JSONObject jobj = new JSONObject(resp);
                    JSONArray mArray = jobj.getJSONArray("Pools");
                    for (int i = 0; i < mArray.length(); i++) {
                        JSONObject mJsonObjectFee = mArray.getJSONObject(i);
                        PoolId.add(mJsonObjectFee.getString("PoolId"));
                        PoolName.add(mJsonObjectFee.getString("PoolName"));
                    }
                } else {
                    pool_status = false;
                }
            } catch (Exception e) {
                server_status = true;
                e.printStackTrace();

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            dialog = new Dialog(ViewScheduleDeckSupervisorActivity.this);
            if (server_status) {
                onDetectNetworkState().show();
                server_status = false;
            } else {
                if (pool_status) {
                    dialog = new Dialog(ViewScheduleDeckSupervisorActivity.this);
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
                    WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
                    lp1.copyFrom(dialog.getWindow().getAttributes());
                    lp1.width = LayoutParams.MATCH_PARENT;
                    lp1.height = LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setAttributes(lp1);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            }
        }
    }


    public class InsertRequestDesk extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            vs_loading.setVisibility(View.VISIBLE);
            vs_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.e("Pool id", "pool id = " + desk_poolid);
            Log.e("Time or now", "Time or now = " + whattimeforassist);
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_InsertDeckAssist);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("Rinstructorid", WW_StaticClass.InstructorID);
            request.addProperty("RAssistDate", mytime);
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
//			progressDialog.dismiss();
            vs_loading.setVisibility(View.GONE);
            if (server_status == true) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
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
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "WaterWorks", "No data found, Please try again..", "Ok");
                    }
                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "WaterWorks", "Something went wrong please try again", "Ok");
                }
            }
        }
    }

    public class InsertDeskAssistUser_forCEE extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//			progressDialog2 = new ProgressDialog(ViewCurrentLessonActivity.this);
//			progressDialog2.setMessage(Html.fromHtml("Loading wait..."));
//			progressDialog2.setIndeterminate(true);
//			progressDialog2.setCancelable(false);
//			progressDialog2.show();
            vs_loading.setVisibility(View.VISIBLE);
            vs_loading.bringToFront();
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
//			progressDialog2.dismiss();
            vs_loading.setVisibility(View.GONE);
            if (server_status) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                server_status = false;
            }
            if (status_req_cee) {
                status_req_cee = false;
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "Request Send", "Request send to cee staff", "Ok");
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
//			progressDialog3 = new ProgressDialog(ViewCurrentLessonActivity.this);
//			progressDialog3.setMessage(Html.fromHtml("Loading wait..."));
//			progressDialog3.setIndeterminate(true);
//			progressDialog3.setCancelable(false);
//			progressDialog3.show();
            vs_loading.setVisibility(View.VISIBLE);
            vs_loading.bringToFront();
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
//			progressDialog3.dismiss();
            vs_loading.setVisibility(View.GONE);
            if (server_status) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                server_status = false;
            }
            if (status_req_aqu) {
                status_req_aqu = false;
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "Request Send", "Request send to aquatics manager", "Ok");
            } else {

            }
        }

    }

    ProgressDialog progressDialog4;
    public boolean end_time = false;
    String MSG;

    public class InsertDeskAssistUser_forCEEManager extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//			progressDialog4 = new ProgressDialog(ViewCurrentLessonActivity.this);
//			progressDialog4.setMessage(Html.fromHtml("Loading wait..."));
//			progressDialog4.setIndeterminate(true);
//			progressDialog4.setCancelable(false);
//			progressDialog4.show();
            vs_loading.setVisibility(View.VISIBLE);
            vs_loading.bringToFront();
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
//			progressDialog4.dismiss();
            vs_loading.setVisibility(View.GONE);
            if (server_status) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                server_status = false;
            }
            if (status_req_cee_manager) {
                status_req_cee_manager = false;
                SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewScheduleDeckSupervisorActivity.this, "Request Send", "Request send to cee manager", "Ok");
            } else {

            }
        }
    }

    private class EndtimeOfAll extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_EndManabgersAllShadows);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("MgrShadowID", ManagerShadowID.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_EndManabgersAllShadows,
                        envelope); // Calling Web service
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                Log.i("here", "Result : " + response.toString());
                String rep = response.toString();
                JSONObject jsonObject = new JSONObject(rep);
                JSONArray jsonObject2 = jsonObject.getJSONArray("Msg");
                MSG = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]", "");
                Log.e("Msg", "Msg " + MSG);
                end_time = true;
            } catch (SocketException e) {
                e.printStackTrace();
                connectionout = true;
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
            if (server_status) {
                server_status = false;
                Toast.makeText(getApplicationContext(), "Check internet connection", Toast.LENGTH_LONG).show();
            } else if (connectionout) {
                connectionout = false;
                new EndtimeOfAll().execute();
            } else {
                if (end_time) {
                    Log.i("MVCS", "Time end");
                    hasdone = true;
                }
            }
        }
    }
}

