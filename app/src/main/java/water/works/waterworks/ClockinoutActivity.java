package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;

import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;
import android.view.View.OnClickListener;

public class ClockinoutActivity extends Activity implements View.OnClickListener {
    public static boolean fromclock = false;
    private static String Tag = "Clock in/out";
    boolean connectionout = false, isInternetPresent = false,
            server_status = false, login_status = false,
            CheckTimeClicks_status = false, Attleft = false;
    TextView mtv_name, mtv_day, mtv_date;
    Button Btn_logout;
    TextView tv_clockinday, tv_clockinbrk, tv_clockoutday, tv_clockoutbrk;
    String username, password, token, userid, UserName, userlevel, julinkid, processMsg,
            WU_Att, clockInStr;
    String[] spiltkey = new String[0];
    int whatclick = 0;
    String btnclick, Timemsg;
    ArrayList<String> ticktype, juid, jobtitle, Msg;
    LinearLayout ll_clockinday, ll_clockinbrk, ll_clockoutday, ll_clockoutbrk;
    String reasonStr = "", checkboxValueStr = "";
    private String clockInTypeStr = "0",clockInForBreakStr = "0";
    //code by megha
    int CheckTimeLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clockinout);

        isInternetPresent = Utility.isNetworkConnected(ClockinoutActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            Initialization();
        }
    }

    private void Initialization() {
        // TODO Auto-generated method stub
        mtv_name = (TextView) findViewById(R.id.tv_app_inst_name);
        mtv_day = (TextView) findViewById(R.id.tv_app_day);
        mtv_date = (TextView) findViewById(R.id.tv_app_date);
        Btn_logout = (Button) findViewById(R.id.btn_app_logoff);
        Btn_logout.setVisibility(View.GONE);
        tv_clockinbrk = (TextView) findViewById(R.id.clockinbrk);
        tv_clockinday = (TextView) findViewById(R.id.clockinday);
        tv_clockoutbrk = (TextView) findViewById(R.id.clockoutbrk);
        tv_clockoutday = (TextView) findViewById(R.id.clockoutday);
        ll_clockinbrk = (LinearLayout) findViewById(R.id.ll_clockinbrk);
        ll_clockinday = (LinearLayout) findViewById(R.id.ll_clockinday);
        ll_clockoutbrk = (LinearLayout) findViewById(R.id.ll_clockoutbrk);
        ll_clockoutday = (LinearLayout) findViewById(R.id.ll_clockoutday);
        tv_clockinbrk.setOnClickListener(this);
        tv_clockinday.setOnClickListener(this);
        tv_clockoutbrk.setOnClickListener(this);
        tv_clockoutday.setOnClickListener(this);
        SetUp();
    }

    private void SetUp() {
        // TODO Auto-generated method stub
        Calendar c = Calendar.getInstance();
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
        mtv_name.setText(WW_StaticClass.UserName);
        mtv_day.setText(day_name);
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

        mtv_date.setText(m + "/" + d);
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
        isInternetPresent = Utility.isNetworkConnected(ClockinoutActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        }
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ClockinoutActivity.this);
        builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
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
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.clockinday:
                    whatclick = 1;
                    btnclick = "1";
                    LoginDialog();
                    break;
                case R.id.clockinbrk:
                    whatclick = 3;
                    btnclick = "3";
                    LoginDialog();
                    break;
                case R.id.clockoutday:
                    whatclick = 4;
                    btnclick = "4";
                    LoginDialog();
                    break;
                case R.id.clockoutbrk:
                    whatclick = 2;
                    btnclick = "2";
                    LoginDialog();
                    break;
                case R.id.btn_back:
                    finish();
                    break;
                default:
                    break;
            }
        } else {
            onDetectNetworkState().show();
        }
    }

    private void LoginDialog() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(ClockinoutActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        final EditText et_username = (EditText) dialog
                .findViewById(R.id.et_login_username);
        final EditText et_password = (EditText) dialog
                .findViewById(R.id.et_login_password);
        Button btn_login = (Button) dialog.findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method
                // stub
                if (isInternetPresent) {
                    if (et_username.getText().length() > 2 && et_password.getText().length() > 2) {
                        username = et_username.getText().toString();
                        password = et_password.getText().toString();
                        dialog.dismiss();
                        new LoginAsyn().execute();
                    } else {
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(
                                ClockinoutActivity.this, "WaterWorks",
                                "Username/Password Is Invalid", "OK");
                    }
                } else {
                    onDetectNetworkState().show();
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void AlertDialogButton4Msg() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ClockinoutActivity.this);

        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        // set dialog message
        alertDialogBuilder
                .setMessage("YOU HAVE NOT BEEN CLOCKED OUT.You have not taken attendance for today. Please take the attendance before clocking out.")
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(ClockinoutActivity.this, ViewTodaysScheduleInstructorActivty.class);
                                Toast.makeText(getApplicationContext(), "Please take your attendance before clocking out.", Toast.LENGTH_LONG).show();
                                fromclock = true;
                                WW_StaticClass.InstructorID = userid;
                                i.putExtra("FROM", "CLOCK");
                                i.putExtra("DELETE", "NO");
                                startActivity(i);
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void AlertDialogMsg() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ClockinoutActivity.this);

        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        // set dialog message
        alertDialogBuilder
                .setMessage("You are clocking in more than 10 minutes before your first lesson. Are you sure you want to continue?.")
                .setCancelable(false)
                .setPositiveButton("Yes, clock me in now",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new ProcessTimeClicks().execute();
                               //   AlertReasonDialog();

                            }
                        })
                .setNegativeButton("No, I will clock in later",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    public void AlertDialogMsgForClockInBreakEarly() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ClockinoutActivity.this);

        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        // set dialog message
        alertDialogBuilder
                .setMessage("You are clocking in more than 2 minutes before the end of your lunch break. Are you sure you want to continue?")
                .setCancelable(false)
                .setPositiveButton("Yes, clock me in now",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  new ProcessTimeClicks().execute();
                                AlertReasonDialog();

                            }
                        })
                .setNegativeButton("No, I will clock in later",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    private void AlertButton4ReasonDialog() {
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(ClockinoutActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.button4_reason_dialog);
        final TextView alert_txt = (TextView) dialog.findViewById(R.id.alert_txt);
        final EditText reason_edt = (EditText) dialog
                .findViewById(R.id.reason_edt);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkbox);
        Button submit = (Button) dialog.findViewById(R.id.submit);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkboxValueStr = checkBox.getTag().toString();
                } else {
                    checkboxValueStr = "";
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method
                reasonStr = reason_edt.getText().toString();
                if (checkboxValueStr.equalsIgnoreCase("")) {
                    if (!reasonStr.equalsIgnoreCase("")) {
                        new AddTickReasonSessionOut().execute();
//                    new CheckTimeClicks().execute();
                        new ProcessTimeClicks().execute();


                    } else {
                        AlertDialogButtonlast4Msg();
                    }
                } else {
//                    if (!reasonStr.equalsIgnoreCase("")) {
//                            new AddTickReasonSessionOut().execute();
////                    new CheckTimeClicks().execute();
//                            new ProcessTimeClicks().execute();
//
//
//                    } else {
                    AlertDialogButtonlast4Msg();
//                    }
                }
                dialog.cancel();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void AlertReasonDialog() {
        final Dialog dialog = new Dialog(ClockinoutActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.clock_alertdialog);
        final TextView alert_txt = (TextView) dialog.findViewById(R.id.alert_txt);
        final EditText reason_edt = (EditText) dialog
                .findViewById(R.id.reason_edt);
        Button submit = (Button) dialog.findViewById(R.id.submit);
        if (btnclick.equalsIgnoreCase("1")) {
            alert_txt.setText("You have Clocked In successfully.\nPlease enter the reason you are clocking in early.");
        } else if (btnclick.equalsIgnoreCase("2")) {
            alert_txt.setText("You are attempting to clock out more than 5 minutes from the start of your meal break. Please enter the reason you are clocking out at this time:");
        } else if (btnclick.equalsIgnoreCase("3")) {
            alert_txt.setText("You have Clocked In From Break successfully.\nPlease enter the reason you are clocking in early.");
        }
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method
                // stub
                reasonStr = reason_edt.getText().toString();
                if (reasonStr.equalsIgnoreCase("")) {
//                    dialog.dismiss();
                    if (btnclick.equalsIgnoreCase("1")) {
                        AlertDialog2Msg();
                    } else if (btnclick.equalsIgnoreCase("2")) {
                        AlertDialogButton2Msg();
                    } else if (btnclick.equalsIgnoreCase("3")) {
                        AlertDialogButton3Msg();
                    }

                } else {
                    dialog.dismiss();
                    if (btnclick.equalsIgnoreCase("1")) {
                        new AddTickReasonSession().execute();
//                        new CheckTimeClicks().execute();
                    } else if (btnclick.equalsIgnoreCase("2")) {
                        new AddTickReasonSessionOut().execute();
//                        new CheckTimeClicks().execute();
                        new ProcessTimeClicks().execute();
                    } else if (btnclick.equalsIgnoreCase("3")) {
                        new AddTickReasonSession().execute();
                    }

                }
                dialog.cancel();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void AlertDialog2Msg() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ClockinoutActivity.this);

        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        // set dialog message
        alertDialogBuilder
                .setMessage("You are attempting to clock in earlier than 10 minutes prior to the start time of your shift without listing a reason. If you do so, this will result in a written warning. Please note that listing an unapproved or improper reason might still result in discipline. Do you wish to proceed?")
                .setCancelable(false)
                .setPositiveButton("Yes, I would like to proceed.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new AddTickReasonSession().execute();
//                                new CheckTimeClicks().execute();

                            }
                        })
                .setNegativeButton("No, I would like to enter a reason now.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AlertReasonDialog();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void AlertDialogButton2Msg() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ClockinoutActivity.this);

        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        // set dialog message
        alertDialogBuilder
                .setMessage("You are attempting to clock out for your meal break beyond the 5 minutes window without listing a reason. If you do so, this will result in a written warning. Please note that listing an unapproved or improper reason might still result in discipline. Do you wish to proceed?")
                .setCancelable(false)
                .setPositiveButton("Yes, I would like to proceed",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new AddTickReasonSessionOut().execute();
//                                new CheckTimeClicks().execute();
                                new ProcessTimeClicks().execute();
                            }
                        })
                .setNegativeButton("No, I would like to enter a reason now.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AlertReasonDialog();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void AlertDialogButton3Msg() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ClockinoutActivity.this);

        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        // set dialog message
        alertDialogBuilder
                .setMessage("You are attempting to clock in early from your meal break window without listing a reason. If you do so, this will result in a written warning. Please note that listing an unapproved or improper reason might still result in discipline. Do you wish to proceed?")
                .setCancelable(false)
                .setPositiveButton("Yes, I would like to proceed",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new AddTickReasonSession().execute();
//                                new CheckTimeClicks().execute();

                            }
                        })
                .setNegativeButton("No, I would like to enter a reason now.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AlertReasonDialog();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void AlertDialogButtonlast4Msg() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ClockinoutActivity.this);

        alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        // set dialog message
        alertDialogBuilder
                .setMessage("You are attempting to clock out later than the established guideline without listing a reason. If you do so, this will result in a written warning. Please note that listing an unapproved or improper reason might still result in discipline. Do you wish to proceed?")
                .setCancelable(false)
                .setPositiveButton("Yes, I would like to proceed",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new AddTickReasonSessionOut().execute();
//                                new CheckTimeClicks().execute();
                                new ProcessTimeClicks().execute();
                            }
                        })
                .setNegativeButton("No, I would like to enter a reason now.",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AlertButton4ReasonDialog();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    // ////////////////////////////////////////////////Login///////////////////////////////////
    public class LoginAsyn extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(ClockinoutActivity.this);
            pDialog.setTitle("Authenticating...");
            pDialog.setMessage(Html.fromHtml("Loading wait..."));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,SOAP_CONSTANTS.METHOD_NAME_LOGIN);
            // Adding Username and Password for Login Invok
            request.addProperty("username", username);
            request.addProperty("password", password);
            request.addProperty("deviceid", "");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                // Calling
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_ACTION_LOGIN,envelope);
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    login_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(Tag, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    token = jo.getString("UserToken");
                    userlevel = jo.getString("UserLevel");
                    userid = jo.getString("UserId");
                    UserName = jo.getString("UserName");

                    if (userid.equals(WW_StaticClass.InstructorID)) {
                        WW_StaticClass.UserToken = token;
                    }
                    WW_StaticClass.CLOCKED_TODAY_INSERT = true;
                    WW_StaticClass.UserToken_Extra = token;

                } else {
                    login_status = false;
                }
            } catch (SocketException e) {
                connectionout = true;
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                connectionout = true;
                e.printStackTrace();
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
            pDialog.dismiss();
            if (server_status) {
                onDetectNetworkState().show();
                server_status = false;
            } else if (connectionout) {
                connectionout = false;
                new LoginAsyn().execute();
            } else {
                if (login_status) {
                    login_status = false;
                    new CheckTimeClicks().execute();

                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            ClockinoutActivity.this, getString(R.string.app_name),
                            "Please Enter Valid Username and Password", "OK");
                }
            }
        }
    }

    private class CheckClockIn extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ClockinoutActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage(Html.fromHtml("Loading data..."));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.Check_ClockIn);
            // Adding Username and Password for Login Invok
            request.addProperty("token", token);
            request.addProperty("UserId", userid);
            request.addProperty("Status", userlevel);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.Check_ClockIn_Action,
                        envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(Tag, "Response = " + response);
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    CheckTimeClicks_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    clockInStr = jo.getString("key");
                } else {
                    CheckTimeClicks_status = false;
                }
            } catch (SocketException e) {
                connectionout = true;
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                connectionout = true;
                e.printStackTrace();

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
            pDialog.dismiss();
            if (server_status) {
                onDetectNetworkState().show();
                server_status = false;
            } else if (connectionout) {
                connectionout = false;

            } else {
                if (CheckTimeClicks_status) {
                    CheckTimeClicks_status = false;
                    try {
                        spiltkey = clockInStr.split("\\|");
                        Timemsg = spiltkey[1];
//                        spiltkey[0]="1";
                        if (spiltkey[0].equalsIgnoreCase("0")) {
                            clockInTypeStr = spiltkey[0];
                            new ProcessTimeClicks().execute();
                        } else {
                            clockInTypeStr = spiltkey[0];
                            AlertDialogMsg();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            ClockinoutActivity.this, getString(R.string.app_name), "Please login again to clock in / out.", "OK");
                }
            }
        }
    }

    private class CheckClockOutForBreak extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ClockinoutActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage(Html.fromHtml("Loading data..."));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.Check_ClockOut_ForBreak);
            // Adding Username and Password for Login Invok
            request.addProperty("token", token);
            request.addProperty("UserId", userid);
            request.addProperty("Status", userlevel);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.Check_ClockOut_ForBreak_Action,
                        envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(Tag, "Response = " + response);
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    CheckTimeClicks_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    clockInStr = jo.getString("key");
                } else {
                    CheckTimeClicks_status = false;
                }
            } catch (SocketException e) {
                connectionout = true;
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                connectionout = true;
                e.printStackTrace();

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
            pDialog.dismiss();
            if (server_status) {
                onDetectNetworkState().show();
                server_status = false;
            } else if (connectionout) {
                connectionout = false;

            } else {
                if (CheckTimeClicks_status) {
                    CheckTimeClicks_status = false;
                    try {
                        spiltkey = clockInStr.split("\\|");
                        Timemsg = spiltkey[1];
                        if (spiltkey[0].equalsIgnoreCase("0")) {
                            new ProcessTimeClicks().execute();
                        } else {
                            AlertReasonDialog();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            ClockinoutActivity.this, getString(R.string.app_name),
                            "Please login again to clock in / out.", "OK");
                }
            }
        }
    }

    private class CheckClockInFromBreak extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ClockinoutActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage(Html.fromHtml("Loading data..."));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.Check_ClockIn_FromBreak);
            // Adding Username and Password for Login Invok
            request.addProperty("token", token);
            request.addProperty("UserId", userid);
            request.addProperty("Status", userlevel);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.Check_ClockIn_FromBreak_Action,
                        envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(Tag, "Response = " + response);
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    CheckTimeClicks_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    clockInStr = jo.getString("key");
                } else {
                    CheckTimeClicks_status = false;
                }
            } catch (SocketException e) {
                connectionout = true;
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                connectionout = true;
                e.printStackTrace();

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
            pDialog.dismiss();
            if (server_status) {
                onDetectNetworkState().show();
                server_status = false;
            } else if (connectionout) {
                connectionout = false;

            } else {
                if (CheckTimeClicks_status) {
                    CheckTimeClicks_status = false;
                    try {
                        spiltkey = clockInStr.split("\\|");
                        Timemsg = spiltkey[1];
                        if (spiltkey[0].equalsIgnoreCase("0")) {
                           clockInForBreakStr = spiltkey[0];
                            new ProcessTimeClicks().execute();
                        } else {
                            clockInForBreakStr = spiltkey[0];
                            AlertDialogMsgForClockInBreakEarly();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            ClockinoutActivity.this, getString(R.string.app_name),
                            "Please login again to clock in / out.", "OK");
                }
            }
        }
    }

    private class CheckClockOut extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ClockinoutActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage(Html.fromHtml("Loading data..."));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.Check_ClockOut);
            // Adding Username and Password for Login Invok
            request.addProperty("token", token);
            request.addProperty("UserId", userid);
            request.addProperty("Status", userlevel);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.CheckClockOut_Action,
                        envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(Tag, "Response = " + response);
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    CheckTimeClicks_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    clockInStr = jo.getString("key");
                } else {
                    CheckTimeClicks_status = false;
                }
            } catch (SocketException e) {
                connectionout = true;
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                connectionout = true;
                e.printStackTrace();

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
            pDialog.dismiss();
            if (server_status) {
                onDetectNetworkState().show();
                server_status = false;
            } else if (connectionout) {
                connectionout = false;

            } else {
                if (CheckTimeClicks_status) {
                    CheckTimeClicks_status = false;
                    try {
                        spiltkey = clockInStr.split("\\|");
                        Timemsg = spiltkey[1];
                        if (spiltkey[0].equalsIgnoreCase("0")) {
                            new ProcessTimeClicks().execute();
                        } else if (spiltkey[0].equalsIgnoreCase("1")) {
                            AlertButton4ReasonDialog();
                        } else if (spiltkey[0].equalsIgnoreCase("3")) {
                            AlertDialogButton4Msg();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            ClockinoutActivity.this, getString(R.string.app_name),
                            "Please login again to clock in / out.", "OK");
                }
            }
        }
    }


    private class AddTickReasonSession extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ClockinoutActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage(Html.fromHtml("Loading data..."));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.AddTickReasonSession);
            // Adding Username and Password for Login Invok
            request.addProperty("token", token);
            request.addProperty("strReason",reasonStr);
            request.addProperty("hdnTick", btnclick);
            request.addProperty("TimeMsg", Timemsg);
            request.addProperty("strUserName", UserName);
            request.addProperty("UserId", userid);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.AddTickReasonSession_Action,
                        envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(Tag, "Response = " + response);
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    CheckTimeClicks_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);

                } else {
                    CheckTimeClicks_status = false;
                }
            } catch (SocketException e) {
                connectionout = true;
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                connectionout = true;
                e.printStackTrace();

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
            pDialog.dismiss();
            if (server_status) {
                onDetectNetworkState().show();
                server_status = false;
            } else if (connectionout) {
                connectionout = false;

            } else {
                if (CheckTimeClicks_status) {
                   CheckTimeClicks_status = true;
                    try {
//                        new CheckTimeClicks().execute();
                        if(whatclick == 3){
                            new ProcessTimeClicks().execute();
                            clockInForBreakStr = "0";
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ClockinoutActivity.this, getString(R.string.app_name), "Please login again to clock in / out.", "OK");
                }
            }
        }
    }

    private class AddTickReasonSessionOut extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ClockinoutActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage(Html.fromHtml("Loading data..."));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.AddTickReasonSessionOut);
            // Adding Username and Password for Login Invok
            request.addProperty("token", token);
            request.addProperty("strReason", reasonStr);
            request.addProperty("hdnTick", btnclick);
            request.addProperty("TimeMsg", Timemsg);
            request.addProperty("strUserName", UserName);
            request.addProperty("UserId", userid);

            // Make an Envelop for sending as whole
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.AddTickReasonSessionOut_Action,
                        envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(Tag, "Response = " + response);
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    CheckTimeClicks_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);

                } else {
                    CheckTimeClicks_status = false;
                }
            } catch (SocketException e) {
                connectionout = true;
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                connectionout = true;
                e.printStackTrace();

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
            pDialog.dismiss();
            if (server_status) {
                onDetectNetworkState().show();
                server_status = false;
            } else if (connectionout) {
                connectionout = false;

            } else {
                if (CheckTimeClicks_status) {
//                    CheckTimeClicks_status = false;
                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ClockinoutActivity.this, getString(R.string.app_name), "Please login again to clock in / out.", "OK");
                }
            }
        }
    }


    private class CheckTimeClicks extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pDialog = new ProgressDialog(ClockinoutActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage(Html.fromHtml("Loading data..."));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
            ticktype = new ArrayList<String>();
            juid = new ArrayList<String>();
            jobtitle = new ArrayList<String>();
            Msg = new ArrayList<String>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.CheckTimeClicks_Method);
            // Adding Username and Password for Login Invok
            request.addProperty("token", token);
            request.addProperty("userid", userid);
            request.addProperty("getticktype", whatclick);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.CheckTimeClicks_Action,
                        envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(Tag, "Response = " + response);
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    CheckTimeClicks_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    if (jo.getString("WU_Att").toString().equalsIgnoreCase("1")) {
                        Attleft = true;
                    } else {
                        Attleft = false;
                        JSONArray jsonArray = jo.getJSONArray("ClockTicks");
                        Log.i(Tag, "" + jsonArray);
                        CheckTimeLength = jsonArray.length();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject mJsonObject = jsonArray.getJSONObject(i);
                            ticktype.add(mJsonObject.getString("ticktype"));
                            juid.add(mJsonObject.getString("juid"));
                            jobtitle.add(mJsonObject.getString("jobtitle"));
                            Msg.add(mJsonObject.getString("Msg"));
                        }
                        CheckTimeClicks_status = true;
                    }
                } else {
                    CheckTimeClicks_status = false;
                }
            } catch (SocketException e) {
                connectionout = true;
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                connectionout = true;
                e.printStackTrace();

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
            pDialog.dismiss();
            if (server_status) {
                onDetectNetworkState().show();
                server_status = false;
            } else if (connectionout) {
                connectionout = false;
                new CheckTimeClicks().execute();
            } else {
                if (CheckTimeClicks_status) {
                    CheckTimeClicks_status = false;
                    try {
                        LinearLayout v = null;
                        try {
                            if (whatclick == 1) {
                                v = ll_clockinday;
                                ll_clockinbrk.removeAllViews();
                                ll_clockinday.removeAllViews();
                                ll_clockoutbrk.removeAllViews();
                                ll_clockoutday.removeAllViews();
                            } else if (whatclick == 2) {
                                v = ll_clockoutbrk;
                                ll_clockinbrk.removeAllViews();
                                ll_clockinday.removeAllViews();
                                ll_clockoutbrk.removeAllViews();
                                ll_clockoutday.removeAllViews();
                            } else if (whatclick == 3) {
                                v = ll_clockinbrk;
                                ll_clockinbrk.removeAllViews();
                                ll_clockinday.removeAllViews();
                                ll_clockoutbrk.removeAllViews();
                                ll_clockoutday.removeAllViews();
                            } else if (whatclick == 4) {
                                v = ll_clockoutday;
                                ll_clockinbrk.removeAllViews();
                                ll_clockinday.removeAllViews();
                                ll_clockoutbrk.removeAllViews();
                                ll_clockoutday.removeAllViews();
                            }
                        }catch (Exception ex){

                        }
                        if (Msg.get(0).toString().equalsIgnoreCase("")) {
                            if (CheckTimeLength > 1) {
                                if (whatclick == 4) {
                                    if (Attleft) {
                                        Attleft = false;
//								Intent i = new Intent(ClockinoutActivity.this, ScheduleActivity.class);
                                        Intent i = new Intent(ClockinoutActivity.this, ViewTodaysScheduleInstructorActivty.class);
                                        Toast.makeText(getApplicationContext(), "Please take your attendance before clocking out.", Toast.LENGTH_LONG).show();
                                        fromclock = true;
                                        WW_StaticClass.InstructorID = userid;
                                        i.putExtra("FROM", "CLOCK");
                                        i.putExtra("DELETE", "NO");

                                        startActivity(i);
                                    } else {
                                        if (Msg.get(0).toString().equalsIgnoreCase("")) {
                                            for (int i = 0; i < jobtitle.size(); i++) {
                                                View childView = getLayoutInflater().inflate(
                                                                R.layout.home_grid_item,
                                                                v, false);
                                                final TextView tv_name = (TextView) childView
                                                        .findViewById(R.id.text);
                                                // tv_name.setBackgroundColor(Color.GRAY);
                                                tv_name.setTag(juid.get(i));
                                                tv_name.setTextColor(getResources()
                                                        .getColor(R.color.link));
                                                tv_name.setGravity(Gravity.LEFT
                                                        | Gravity.CENTER_VERTICAL);
                                                tv_name.setText(Html.fromHtml("<u>"
                                                        + jobtitle.get(i) + "</u>"));
                                                tv_name.setEllipsize(TextUtils.TruncateAt.END);
                                                tv_name.setMaxLines(1);
                                                tv_name.setMinLines(1);
                                                tv_name.setHorizontallyScrolling(true);
                                                tv_name.setPadding(5, 10, 5, 10);
                                                tv_name.setOnClickListener(new View.OnClickListener() {

                                                    @Override
                                                    public void onClick(View v) {
                                                        // TODO Auto-generated method
                                                        // stub
                                                        julinkid = tv_name.getTag().toString();
//                                                new ProcessTimeClicks().execute();
                                                        new CheckClockOut().execute();

                                                        try {

                                                            LinearLayout v1 = null;
                                                            if (whatclick == 1) {
                                                                v1 = ll_clockinday;
                                                                ll_clockinbrk.removeAllViews();
                                                                ll_clockinday.removeAllViews();
                                                                ll_clockoutbrk.removeAllViews();
                                                                ll_clockoutday.removeAllViews();
                                                            } else if (whatclick == 2) {
                                                                v1 = ll_clockoutbrk;
                                                                ll_clockinbrk.removeAllViews();
                                                                ll_clockinday.removeAllViews();
                                                                ll_clockoutbrk.removeAllViews();
                                                                ll_clockoutday.removeAllViews();
                                                            } else if (whatclick == 3) {
                                                                v1 = ll_clockinbrk;
                                                                ll_clockinbrk.removeAllViews();
                                                                ll_clockinday.removeAllViews();
                                                                ll_clockoutbrk.removeAllViews();
                                                                ll_clockoutday.removeAllViews();
                                                            } else if (whatclick == 4) {
                                                                v1 = ll_clockoutday;
                                                                ll_clockinbrk.removeAllViews();
                                                                ll_clockinday.removeAllViews();
                                                                ll_clockoutbrk.removeAllViews();
                                                                ll_clockoutday.removeAllViews();
                                                            }
                                                        }catch (Exception ex){
                                                            ex.printStackTrace();
                                                        }


                                                    }
                                                });
                                                v.addView(childView);
                                            }
                                        } else {
                                            SingleOptionAlertWithoutTitle
                                                    .ShowAlertDialog(ClockinoutActivity.this,
                                                            getString(R.string.app_name), Msg.get(0)
                                                                    .toString(), "Ok");
                                        }
                                    }
                                } else {
                                    if (Msg.get(0).toString().equalsIgnoreCase("")) {
                                        for (int i = 0; i < jobtitle.size(); i++) {
                                            View childView = getLayoutInflater()
                                                    .inflate(R.layout.home_grid_item,
                                                            v, false);
                                            final TextView tv_name = (TextView) childView
                                                    .findViewById(R.id.text);
                                            // tv_name.setBackgroundColor(Color.GRAY);
                                            tv_name.setTag(juid.get(i));
                                            tv_name.setTextColor(getResources()
                                                    .getColor(R.color.link));
                                            tv_name.setGravity(Gravity.LEFT
                                                    | Gravity.CENTER_VERTICAL);
                                            tv_name.setText(Html.fromHtml("<u>"
                                                    + jobtitle.get(i) + "</u>"));
                                            tv_name.setEllipsize(TextUtils.TruncateAt.END);
                                            tv_name.setMaxLines(1);
                                            tv_name.setMinLines(1);
                                            tv_name.setHorizontallyScrolling(true);
                                            tv_name.setPadding(5, 10, 5, 10);
                                            tv_name.setOnClickListener(new View.OnClickListener() {

                                                @Override
                                                public void onClick(View v) {
                                                    // TODO Auto-generated method stub
                                                    julinkid = tv_name.getTag()
                                                            .toString();
//                                            new ProcessTimeClicks().execute();
                                                    LinearLayout v1 = null;

                                                    try {

                                                        if (whatclick == 1) {
                                                            new CheckClockIn().execute();

                                                            v1 = ll_clockinday;
                                                            ll_clockinbrk.removeAllViews();
                                                            ll_clockinday.removeAllViews();
                                                            ll_clockoutbrk.removeAllViews();
                                                            ll_clockoutday.removeAllViews();

                                                        } else if (whatclick == 2) {
                                                            new CheckClockOutForBreak().execute();
                                                            v1 = ll_clockoutbrk;
                                                            ll_clockinbrk.removeAllViews();
                                                            ll_clockinday.removeAllViews();
                                                            ll_clockoutbrk.removeAllViews();
                                                            ll_clockoutday.removeAllViews();

                                                        } else if (whatclick == 3) {
                                                            new CheckClockInFromBreak().execute();
                                                            v1 = ll_clockinbrk;
                                                            ll_clockinbrk.removeAllViews();
                                                            ll_clockinday.removeAllViews();
                                                            ll_clockoutbrk.removeAllViews();
                                                            ll_clockoutday.removeAllViews();

                                                        } else if (whatclick == 4) {
                                                            new CheckClockOut().execute();
                                                            v1 = ll_clockoutday;
                                                            ll_clockinbrk.removeAllViews();
                                                            ll_clockinday.removeAllViews();
                                                            ll_clockoutbrk.removeAllViews();
                                                            ll_clockoutday.removeAllViews();
                                                        }
                                                    }catch (Exception ex){
                                                        ex.printStackTrace();
                                                    }

                                                }
                                            });
                                            v.addView(childView);
                                        }
                                    } else {
                                        SingleOptionAlertWithoutTitle.ShowAlertDialog(
                                                ClockinoutActivity.this,
                                                getString(R.string.app_name), Msg.get(0).toString(),
                                                "Ok");
                                    }

                                }
                            } else {
                                if (jobtitle.size() > 0) {
                                    for (int i = 0; i < jobtitle.size(); i++) {
                                        julinkid = juid.get(i);

                                    }
                                    if (whatclick == 1) {
                                        new CheckClockIn().execute();
                                    } else if (whatclick == 2) {
                                        new CheckClockOutForBreak().execute();
                                    } else if (whatclick == 3) {
                                        new CheckClockInFromBreak().execute();
                                    } else if (whatclick == 4) {
                                        new CheckClockOut().execute();
                                    }

                                } else {
                                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                                            ClockinoutActivity.this,
                                            getString(R.string.app_name), Msg.get(0).toString(),
                                            "Ok");
                                }
                            }
                        } else {
                            SingleOptionAlertWithoutTitle.ShowAlertDialog(
                                    ClockinoutActivity.this,
                                    getString(R.string.app_name), Msg.get(0).toString(),
                                    "Ok");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            ClockinoutActivity.this,  getString(R.string.app_name),
                            "Please login again to clock in / out.", "OK");
                }
            }
        }
    }

    private class ProcessTimeClicks extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(ClockinoutActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage(Html.fromHtml("Loading data..."));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.ProcessTimeClicks_Method);
            // Adding Username and Password for Login Invok
            request.addProperty("token", token);
            request.addProperty("userid", userid);
            request.addProperty("TypeId", whatclick);
            request.addProperty("JuId", julinkid);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.ProcessTimeClicks_Action,
                        envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    CheckTimeClicks_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    JSONArray jsonArray = jo.getJSONArray("ClockTicks");
                    Log.i(Tag, "" + jsonArray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject mJsonObject = jsonArray.getJSONObject(i);
                        processMsg = mJsonObject.getString("Msg");
                        WU_Att = mJsonObject.getString("WU_Att");
                    }

                } else {
                    CheckTimeClicks_status = false;
                }
            } catch (SocketException e) {
                connectionout = true;
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                connectionout = true;
                e.printStackTrace();

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
            pDialog.dismiss();
            if (server_status) {
                server_status = false;
                onDetectNetworkState().show();
            } else if (connectionout) {
                connectionout = false;
                new ProcessTimeClicks().execute();
            } else {
                if (CheckTimeClicks_status) {
                    CheckTimeClicks_status = false;
                    if (whatclick == 4) {
                        if (WU_Att.toString().equalsIgnoreCase("1")) {
//							Intent i = new Intent(ClockinoutActivity.this, ScheduleActivity.class);
                            Intent i = new Intent(ClockinoutActivity.this, ViewTodaysScheduleInstructorActivty.class);
                            Toast.makeText(getApplicationContext(), "Please take your attendance before clocking out.", Toast.LENGTH_LONG).show();
                            WW_StaticClass.InstructorID = userid;
                            fromclock = true;
                            i.putExtra("FROM", "CLOCK");
                            i.putExtra("DELETE", "NO");
                            startActivity(i);
                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ClockinoutActivity.this);
                            alertDialogBuilder.setCancelable(false);
                            // set title
                            alertDialogBuilder.setTitle(getString(R.string.app_name));
                            alertDialogBuilder.setIcon(getResources()
                                    .getDrawable(R.drawable.ic_launcher));

                            // set dialog message
                            alertDialogBuilder
                                    .setMessage(processMsg)
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            "Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    recreate();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }
                    } else if (whatclick == 2) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                ClockinoutActivity.this);
                        alertDialogBuilder.setCancelable(false);
                        // set title
                        alertDialogBuilder.setTitle(getString(R.string.app_name));
                        alertDialogBuilder.setIcon(getResources()
                                .getDrawable(R.drawable.ic_launcher));

                        // set dialog message
                        alertDialogBuilder
                                .setMessage(processMsg)
                                .setCancelable(false)
                                .setPositiveButton(
                                        "Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int id) {
                                                recreate();
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder
                                .create();

                        // show it
                        alertDialog.show();
                    }else if(whatclick == 1) {
                        if (clockInTypeStr.equalsIgnoreCase("0")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ClockinoutActivity.this);
                            alertDialogBuilder.setCancelable(false);
                            // set title
                            alertDialogBuilder.setTitle(getString(R.string.app_name));
                            alertDialogBuilder.setIcon(getResources()
                                    .getDrawable(R.drawable.ic_launcher));

                            // set dialog message
                            alertDialogBuilder
                                    .setMessage(processMsg)
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {
                                                    recreate();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder
                                    .create();

                            // show it
                            alertDialog.show();

                        } else {
                            AlertReasonDialog();
                        }
                        clockInTypeStr = "0";

                    } else if(whatclick == 3){
                        if (clockInForBreakStr.equalsIgnoreCase("0")) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    ClockinoutActivity.this);
                            alertDialogBuilder.setCancelable(false);
                            // set title
                            alertDialogBuilder.setTitle(getString(R.string.app_name));
                            alertDialogBuilder.setIcon(getResources()
                                    .getDrawable(R.drawable.ic_launcher));

                            // set dialog message
                            alertDialogBuilder
                                    .setMessage(processMsg)
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            "Ok",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    recreate();
                                                }
                                            });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder
                                    .create();

                            // show it
                            alertDialog.show();

                        } else {
                            AlertDialogMsgForClockInBreakEarly();
                        }
                        clockInForBreakStr = "0";

                    }else{
                        AlertReasonDialog();
                    }


                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(ClockinoutActivity.this, getString(R.string.app_name), "please login again to clock in / out.", "Ok");
                }
            }

        }
    }
}
