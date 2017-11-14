package water.works.waterworks;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;
import net.frederico.showtipsview.ShowTipsViewInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class CreateNewMessageActivity extends Activity implements
        OnClickListener {
    static ArrayList<String> TO_NAME, CC_NAME;
    static ArrayList<String> temp_to_name, temp_cc_name;
    // ListView
    // list_site_list,list_upper_manager_list,list_manager_list,list_staff_list,list_inst_list,
    // list_supervisor_list,list_maintenance_list;
    // ArrayAdapter<String>
    // adapter,adapterMaintenance,adapterSupervisor,adapterInstructor,
    // adapterOfficeStaff,adapterManager,adapterUpperManager;
    Boolean isInternetPresent = false, user_finish = false;
    EditText et_cm_from, et_cm_subject, et_cm_message;
    static TextView tv_cm_to;
    static TextView tv_cm_cc;
    ImageButton ib_cm_show_to, ib_cm_hide_to, ib_cm_show_cc, ib_cm_hide_cc;
    Button btn_cm_to, btn_cm_cc, btn_cm_open_cc, btn_cm_priority;
    static Button btn_site_name;
    CheckBox chb_cm_read;
    RelativeLayout rl_cm_cc;
    static int siteid = 0, usertype;
    public static String TAG = "New Message Activity";
    boolean server_response = false;
    boolean getsitelist = false;
    boolean mailsend = false;
    int TO_CC_CLICK;
    String priority_id;
    String subjectStr,messageStr;
    String FromUsername, ToUsername, CcUsername, MsgSubject, MsgPriority,
            MsgSitename, IAMFOR, FromUserID, ToUserID, CCUserID, strFrmmsg;
    static ArrayList<String> TO_ID, Final_TO_ID;
    static ArrayList<String> CC_ID, Final_CC_ID;
    ArrayList<String> SiteID, SiteName,/*
                                         * Upper_Manager_userid,
										 * Upper_Manager_username
										 * ,Manager_userid,
										 * Manager_username,Staff_userid,
										 * Staff_username
										 * ,Inst_userid,Inst_username
										 * ,Supervisor_userid
										 * ,Supervisor_username
										 * ,Mainten_userid,Mainten_username,
										 * mUpper_manager_name
										 * ,mUpper_manager_id,
										 * mManager_name,mManager_id
										 * ,mStaff_name,
										 * mStaff_id,mInst_name,mInst_id,
										 * mSupervisor_name
										 * ,mSupervisor_id,mMainten_name
										 * ,mMainten_id,
										 */
            Priority;
    boolean selectAll = true;
    ListPopupWindow PW_Priority, PW_sitename;

    static ArrayList<String> temp_um_id_to, temp_um_id_cc, temp_mng_id_to,
            temp_mng_id_cc, temp_office_id_to, temp_office_id_cc,
            temp_inst_id_to, temp_inst_id_cc, temp_super_id_to,
            temp_super_id_cc, temp_main_id_to, temp_main_id_cc, temp_exe_id_to,
            temp_exe_id_cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_message);
        isInternetPresent = Utility
                .isNetworkConnected(CreateNewMessageActivity.this);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Compose");

        if (isInternetPresent) {
            FromUsername = getIntent().getStringExtra("From");
            ToUsername = getIntent().getStringExtra("To");
            CcUsername = getIntent().getStringExtra("Cc");
            MsgSubject = getIntent().getStringExtra("Subject");
            MsgPriority = getIntent().getStringExtra("Priority");
            MsgSitename = getIntent().getStringExtra("Sitename");
            IAMFOR = getIntent().getStringExtra("IAMFOR");
            FromUserID = getIntent().getStringExtra("FromID");
            ToUserID = getIntent().getStringExtra("ToID");
            CCUserID = getIntent().getStringExtra("CcID");
            strFrmmsg = getIntent().getStringExtra("strFrmmsg");
            Log.e(TAG, "Uid = " + FromUserID);
            Log.e(TAG, "Toid = " + ToUserID);
            Log.e(TAG, "CCid = " + CCUserID);
            TO_ID = new ArrayList<String>();
            CC_ID = new ArrayList<String>();
            TO_NAME = new ArrayList<String>();
            CC_NAME = new ArrayList<String>();
            Initialization();

            new GetSiteList().execute();
            // usertype = 1;
            // new GetUsers().execute();
        } else {

            onDetectNetworkState().show();
        }
        Priority = new ArrayList<String>();
        Priority.add("Low");
        Priority.add("Medium");
        Priority.add("High");

        PW_Priority.setAdapter(new ArrayAdapter<String>(
                getApplicationContext(), R.layout.edittextpopup, Priority));
        PW_Priority.setAnchorView(btn_cm_priority);
        PW_Priority.setHeight(LayoutParams.WRAP_CONTENT);
        PW_Priority.setModal(true);
        PW_Priority.setOnItemClickListener(new OnItemClickListener() {


            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                // TODO Auto-generated method stub
                btn_cm_priority.setText(Priority.get(pos));
                priority_id = "" + pos;
                PW_Priority.dismiss();
            }
        });

        try {
            View v = findViewById(android.R.id.home);
            ShowTipsView showtips2 = new ShowTipsBuilder(
                    CreateNewMessageActivity.this).setTarget(v)
                    .setTitle("Home Button").setTitleColor(Color.YELLOW)
                    .setDescription("Use for going back to inbox.")
                    .setDescriptionColor(getResources().getColor(R.color.bigs))
                    .setCircleColor(Color.RED).displayOneTime(95).setDelay(0)
                    .setCallback(new ShowTipsViewInterface() {


                        public void gotItClicked() {
                            // TODO Auto-generated method stub
                            View v = findViewById(R.id.action_send);
                            ShowTipsView showtips3 = new ShowTipsBuilder(
                                    CreateNewMessageActivity.this)
                                    .setTarget(v)
                                    .setTitle("Send")
                                    .setTitleColor(Color.YELLOW)
                                    .setDescription("Send a mail.")
                                    .setDescriptionColor(
                                            getResources().getColor(R.color.bigs))
                                    .setCircleColor(Color.RED).displayOneTime(94)
                                    .setDelay(0)
                                    .setCallback(new ShowTipsViewInterface() {
                                        public void gotItClicked() {
                                            // TODO Auto-generated method stub
                                            View v = findViewById(R.id.btn_site_name);
                                            ShowTipsView showtips1 = new ShowTipsBuilder(
                                                    CreateNewMessageActivity.this)
                                                    .setTarget(v)
                                                    .setTitle("Site Selection")
                                                    .setTitleColor(Color.YELLOW)
                                                    .setDescription(
                                                            "Select site from list.")
                                                    .setDescriptionColor(
                                                            getResources()
                                                                    .getColor(
                                                                            R.color.bigs))
                                                    .setCircleColor(Color.RED)
                                                    .displayOneTime(93).setDelay(0)
                                                    .build();
                                            showtips1
                                                    .show(CreateNewMessageActivity.this);
                                        }
                                    }).build();
                            showtips3.show(CreateNewMessageActivity.this);
                        }
                    }).build();
            showtips2.show(CreateNewMessageActivity.this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void Initialization() {
        // TODO Auto-generated method stub
        et_cm_from = (EditText) findViewById(R.id.et_cm_from);
        et_cm_subject = (EditText) findViewById(R.id.et_cm_subject);
        et_cm_message = (EditText) findViewById(R.id.et_cm_message);
        tv_cm_to = (TextView) findViewById(R.id.tv_cm_to);
        tv_cm_cc = (TextView) findViewById(R.id.tv_cm_cc);
        ib_cm_show_to = (ImageButton) findViewById(R.id.cm_show_to);
        ib_cm_show_cc = (ImageButton) findViewById(R.id.cm_show_cc);
        ib_cm_hide_to = (ImageButton) findViewById(R.id.cm_hide_to);
        ib_cm_hide_cc = (ImageButton) findViewById(R.id.cm_hide_cc);
        btn_cm_to = (Button) findViewById(R.id.btn_cm_to);
        btn_cm_open_cc = (Button) findViewById(R.id.btn_cm_open_cc);
        btn_cm_cc = (Button) findViewById(R.id.btn_cm_cc);
        btn_cm_priority = (Button) findViewById(R.id.btn_cm_priority);
        btn_site_name = (Button) findViewById(R.id.btn_site_name);
        chb_cm_read = (CheckBox) findViewById(R.id.chb_cm_read_recipt);
        rl_cm_cc = (RelativeLayout) findViewById(R.id.rl_cm_cc);
        et_cm_from.setText(WW_StaticClass.UserName);
        PW_Priority = new ListPopupWindow(CreateNewMessageActivity.this);
        PW_sitename = new ListPopupWindow(CreateNewMessageActivity.this);
        btn_site_name.setEnabled(false);
        btn_cm_cc.setEnabled(false);
        btn_cm_open_cc.setEnabled(false);
        btn_cm_to.setEnabled(false);
        et_cm_from.setEnabled(false);
        btn_cm_priority.setText("Low");
        if (IAMFOR.equalsIgnoreCase("REPLY")) {
            if (MsgPriority.equalsIgnoreCase("0")) {
                btn_cm_priority.setText("Low");
            } else if (MsgPriority.equalsIgnoreCase("1")) {
                btn_cm_priority.setText("Medium");
            } else if (MsgPriority.equalsIgnoreCase("2")) {
                btn_cm_priority.setText("High");
            }
            priority_id = MsgPriority;
            tv_cm_to.setText(FromUsername);
            tv_cm_cc.setText(CcUsername);
            et_cm_subject.setText("Re: " + MsgSubject);
            et_cm_message.setText("\n\n\n"
                    + Html.fromHtml(strFrmmsg).toString());
            TO_ID.add(FromUserID);
        } else if (IAMFOR.equalsIgnoreCase("REPLYALL")) {
            if (MsgPriority.equalsIgnoreCase("0")) {
                btn_cm_priority.setText("Low");
            } else if (MsgPriority.equalsIgnoreCase("1")) {
                btn_cm_priority.setText("Medium");
            } else if (MsgPriority.equalsIgnoreCase("2")) {
                btn_cm_priority.setText("High");
            }
            priority_id = MsgPriority;
            tv_cm_to.setText(FromUsername + ";" + ToUserID);
            tv_cm_cc.setText(CcUsername);
            et_cm_subject.setText("Re: " + MsgSubject);
            et_cm_message.setText("\n\n\n"
                    + Html.fromHtml(strFrmmsg).toString());
            TO_ID.add(FromUserID + "," + ToUserID);
            CC_ID.add(CCUserID);
        } else if (IAMFOR.equalsIgnoreCase("FWD")) {
            if (MsgPriority.equalsIgnoreCase("0")) {
                btn_cm_priority.setText("Low");
            } else if (MsgPriority.equalsIgnoreCase("1")) {
                btn_cm_priority.setText("Medium");
            } else if (MsgPriority.equalsIgnoreCase("2")) {
                btn_cm_priority.setText("High");
            }
            priority_id = MsgPriority;
            tv_cm_to.setText("");
            tv_cm_cc.setText("");
            et_cm_subject.setText("Fwd: " + MsgSubject);
            et_cm_message.setText("\n\n\n"
                    + Html.fromHtml(strFrmmsg).toString());
        }
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
        // mUpper_manager_name = new ArrayList<String>();
        // mUpper_manager_id = new ArrayList<String>();
        // mManager_name = new ArrayList<String>();
        // mManager_id = new ArrayList<String>();
        // mStaff_name = new ArrayList<String>();
        // mStaff_id = new ArrayList<String>();
        // mInst_name = new ArrayList<String>();
        // mInst_id = new ArrayList<String>();
        // mSupervisor_name =new ArrayList<String>();
        // mSupervisor_id = new ArrayList<String>();
        // mMainten_name = new ArrayList<String>();
        // mMainten_id = new ArrayList<String>();
//		TO_ID = new ArrayList<String>();
//		CC_ID = new ArrayList<String>();
//		TO_NAME = new ArrayList<String>();
//		CC_NAME = new ArrayList<String>();
        temp_to_name = new ArrayList<String>();
        temp_cc_name = new ArrayList<String>();
        tv_cm_to.setOnClickListener(this);
        tv_cm_cc.setOnClickListener(this);
        Final_CC_ID = new ArrayList<String>();
        Final_TO_ID = new ArrayList<String>();
    }

    Boolean tv_to_click = false, tv_cc_click = false;

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
                .isNetworkConnected(CreateNewMessageActivity.this);
    }

    // public static void setToField(String to){
    // String text = tv_cm_to.getText().toString();
    // String btnsitename = btn_site_name.getText().toString();
    // if(btnsitename.equalsIgnoreCase("All")){
    // btnsitename = "All site";
    // }
    // if(to.contains("All")){
    // to = btnsitename+"-"+to;
    // }
    // if(text.equalsIgnoreCase("")){
    // tv_cm_to.setText(to);
    // }
    // else{
    // tv_cm_to.setText(text+", "+to);
    // }
    //
    // }
    public static void setToField() {
        ArrayList<String> name = new ArrayList<String>();
        for (int i = 0; i < temp_to_name.size(); i++) {
            if (name.contains(temp_to_name.get(i))) {

            } else {
                name.add(temp_to_name.get(i));
            }
        }
        tv_cm_to.setText(name.toString().trim().replaceAll("\\[", "")
                .replaceAll("\\]", ""));
    }

    public static void setCcField() {
        ArrayList<String> name = new ArrayList<String>();
        for (int i = 0; i < temp_cc_name.size(); i++) {
            if (name.contains(temp_cc_name.get(i))) {

            } else {
                name.add(temp_cc_name.get(i));
            }
        }
        tv_cm_cc.setText(name.toString().trim().replaceAll("\\[", "")
                .replaceAll("\\]", ""));
    }

    // public static void setCcField(String cc){
    // String text = tv_cm_cc.getText().toString();
    // String btnsitename = btn_site_name.getText().toString();
    // if(btnsitename.equalsIgnoreCase("All")){
    // btnsitename = "All site";
    // }
    // if(cc.contains("All")){
    // cc = btnsitename+"-"+cc;
    // }
    // if(text.equalsIgnoreCase("")){
    // tv_cm_cc.setText(cc);
    // }
    // else{
    // tv_cm_cc.setText(text+", "+cc);
    // }
    // }
    // ///////////////////// get and set temp array//////////////////////
    public static void addTo_name(String name) {
        temp_to_name.add(name);
    }

    public static void addCc_name(String name) {
        temp_cc_name.add(name);
    }

    public static void SetToID(ArrayList<String> to_id) {
        // TODO Auto-generated method stub
        TO_ID.addAll(to_id);
        Log.i(TAG, "TO id = " + TO_ID);
    }

    public static void SetCcID(ArrayList<String> cc_id) {
        // TODO Auto-generated method stub
        CC_ID.addAll(cc_id);
        Log.i(TAG, "CC id = " + CC_ID);
    }

    public static ArrayList<String> getToID(ArrayList<String> to_id) {
        return to_id;
    }

    public static ArrayList<String> getCcID(ArrayList<String> cc_id) {
        return cc_id;
    }

    public static ArrayList<String> getTo_name() {
        return TO_NAME;
    }

    public static void setTo_name(ArrayList<String> to_name) {
        TO_NAME.addAll(to_name);
        Log.i(TAG, "To name = " + TO_NAME);
    }

    public static ArrayList<String> getCc_name() {
        return CC_NAME;
    }

    public static void setCc_name(ArrayList<String> cc_name) {
        CC_NAME.addAll(cc_name);
        Log.i(TAG, "Cc name = " + CC_NAME);
    }

    public static ArrayList<String> getTemp_um_id_to() {
        return temp_um_id_to;
    }

    public static void setTemp_um_id_to(ArrayList<String> temp_um_id_to) {
        CreateNewMessageActivity.temp_um_id_to = temp_um_id_to;
    }

    public static ArrayList<String> getTemp_um_id_cc() {
        return temp_um_id_cc;
    }

    public static void setTemp_um_id_cc(ArrayList<String> temp_um_id_cc) {
        CreateNewMessageActivity.temp_um_id_cc = temp_um_id_cc;
    }

    public static ArrayList<String> getTemp_mng_id_to() {
        return temp_mng_id_to;
    }

    public static void setTemp_mng_id_to(ArrayList<String> temp_mng_id_to) {
        CreateNewMessageActivity.temp_mng_id_to = temp_mng_id_to;
    }

    public static ArrayList<String> getTemp_mng_id_cc() {
        return temp_mng_id_cc;
    }

    public static void setTemp_mng_id_cc(ArrayList<String> temp_mng_id_cc) {
        CreateNewMessageActivity.temp_mng_id_cc = temp_mng_id_cc;
    }

    public static ArrayList<String> getTemp_office_id_to() {
        return temp_office_id_to;
    }

    public static void setTemp_office_id_to(ArrayList<String> temp_office_id_to) {
        CreateNewMessageActivity.temp_office_id_to = temp_office_id_to;
    }

    public static ArrayList<String> getTemp_office_id_cc() {
        return temp_office_id_cc;
    }

    public static void setTemp_office_id_cc(ArrayList<String> temp_office_id_cc) {
        CreateNewMessageActivity.temp_office_id_cc = temp_office_id_cc;
    }

    public static ArrayList<String> getTemp_inst_id_to() {
        return temp_inst_id_to;
    }

    public static void setTemp_inst_id_to(ArrayList<String> temp_inst_id_to) {
        CreateNewMessageActivity.temp_inst_id_to = temp_inst_id_to;
    }

    public static ArrayList<String> getTemp_inst_id_cc() {
        return temp_inst_id_cc;
    }

    public static void setTemp_inst_id_cc(ArrayList<String> temp_inst_id_cc) {
        CreateNewMessageActivity.temp_inst_id_cc = temp_inst_id_cc;
    }

    public static ArrayList<String> getTemp_super_id_to() {
        return temp_super_id_to;
    }

    public static void setTemp_super_id_to(ArrayList<String> temp_super_id_to) {
        CreateNewMessageActivity.temp_super_id_to = temp_super_id_to;
    }

    public static ArrayList<String> getTemp_super_id_cc() {
        return temp_super_id_cc;
    }

    public static void setTemp_super_id_cc(ArrayList<String> temp_super_id_cc) {
        CreateNewMessageActivity.temp_super_id_cc = temp_super_id_cc;
    }

    public static ArrayList<String> getTemp_main_id_to() {
        return temp_main_id_to;
    }

    public static void setTemp_main_id_to(ArrayList<String> temp_main_id_to) {
        CreateNewMessageActivity.temp_main_id_to = temp_main_id_to;
    }

    public static ArrayList<String> getTemp_main_id_cc() {
        return temp_main_id_cc;
    }

    public static void setTemp_main_id_cc(ArrayList<String> temp_main_id_cc) {
        CreateNewMessageActivity.temp_main_id_cc = temp_main_id_cc;
    }

    public static ArrayList<String> getTemp_exe_id_to() {
        return temp_exe_id_to;
    }

    public static void setTemp_exe_id_to(ArrayList<String> temp_exe_id_to) {
        CreateNewMessageActivity.temp_exe_id_to = temp_exe_id_to;
    }

    public static ArrayList<String> getTemp_exe_id_cc() {
        return temp_exe_id_cc;
    }

    public static void setTemp_exe_id_cc(ArrayList<String> temp_exe_id_cc) {
        CreateNewMessageActivity.temp_exe_id_cc = temp_exe_id_cc;
    }

    // ///////////////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent messagecenterIntent = new Intent(CreateNewMessageActivity.this,
                MessageCenterActivity.class);
        startActivity(messagecenterIntent);
        WW_StaticClass.lable = "Inbox";
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_new_message, menu);
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (isInternetPresent) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    Intent messagecenterIntent = new Intent(
                            CreateNewMessageActivity.this,
                            MessageCenterActivity.class);
                    startActivity(messagecenterIntent);
                    WW_StaticClass.lable = "Inbox";
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_right);
                    break;
                case R.id.action_send:
                    subjectStr=et_cm_subject.getText().toString();
                    messageStr =et_cm_message.getText().toString();
                    if (tv_cm_to.getText().toString().isEmpty()
                            || tv_cm_to.getText().toString().equalsIgnoreCase("")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                CreateNewMessageActivity.this).create();
                        alertDialog.setTitle("WaterWorks");
                        alertDialog.setIcon(getResources().getDrawable(
                                R.drawable.ic_launcher));
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        // set the message
                        alertDialog
                                .setMessage("Please specify at least one recipient.");
                        // set button1 functionality
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // close dialog

                                        dialog.cancel();
                                        // new SendMessage().execute();

                                    }
                                });
                        // show the alert dialog
                        alertDialog.show();

                    } else if (et_cm_subject.getText().toString().isEmpty()
                            || et_cm_subject.getText().toString()
                            .equalsIgnoreCase("")) {

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                CreateNewMessageActivity.this).create();
                        alertDialog.setTitle("WaterWorks");
                        alertDialog.setIcon(getResources().getDrawable(
                                R.drawable.ic_launcher));
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        // set the message
                        alertDialog
                                .setMessage("Would like to send message without subject?");
                        // set button1 functionality
                        alertDialog.setButton("Yes",
                                new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // close dialog

                                        dialog.cancel();
                                        new SendMessage().execute();

                                    }
                                });
                        alertDialog.setButton2("No",
                                new DialogInterface.OnClickListener() {


                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        dialog.cancel();
                                    }
                                });
                        // show the alert dialog
                        alertDialog.show();
                    } else {
                        new SendMessage().execute();
                    }
                    break;
                default:
                    break;
            }
        } else {
            onDetectNetworkState().show();
        }
        return super.onOptionsItemSelected(item);
    }

	/*
	 * Get Site List and Site Selection
	 */

    private class GetSiteList extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_GetSiteList_1);
            request.addProperty("token", WW_StaticClass.UserToken);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(
                        SOAP_CONSTANTS.SOAP_Action_GetSiteList_1, envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                // SoapPrimitive response = (SoapPrimitive)
                // envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i(TAG, "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                Log.i(TAG, "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    getsitelist = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    JSONArray jArray = jo.getJSONArray("Sites");
                    Log.i(TAG, "jArray : " + jArray.toString());
                    SiteID = new ArrayList<String>();
                    SiteName = new ArrayList<String>();
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        SiteName.add(jsonObject.getString("SiteName"));
                        SiteID.add(jsonObject.getString("SiteID"));
                    }
                    SiteName.add("All");
                    SiteID.add("0");
                } else {
                    getsitelist = false;
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
                SingleOptionAlertWithoutTitle.ShowAlertDialog(
                        CreateNewMessageActivity.this, "Error", "SERVER DOWN",
                        "OK");
                server_response = false;
            } else {
                if (!getsitelist) {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            CreateNewMessageActivity.this, "Error",
                            "No site found.", "OK");
                } else {
                    getsitelist = false;
                    btn_site_name.setEnabled(true);
                    PW_sitename.setAdapter(new ArrayAdapter<String>(
                            getApplicationContext(), R.layout.edittextpopup,
                            SiteName));
                    PW_sitename.setAnchorView(btn_site_name);
                    PW_sitename.setHeight(LayoutParams.WRAP_CONTENT);
                    PW_sitename.setModal(true);
                    PW_sitename
                            .setOnItemClickListener(new OnItemClickListener() {


                                public void onItemClick(AdapterView<?> parent,
                                                        View view, int pos, long id) {
                                    try {
                                        // TODO Auto-generated method stub
                                        View v = findViewById(R.id.btn_cm_to);
                                        ShowTipsView showtips1 = new ShowTipsBuilder(
                                                CreateNewMessageActivity.this)
                                                .setTarget(v)
                                                .setTitle("To selection")
                                                .setTitleColor(Color.YELLOW)
                                                .setDescription(
                                                        "Click for recipient selection.")
                                                .setDescriptionColor(
                                                        getResources().getColor(
                                                                R.color.bigs))
                                                .setCircleColor(Color.RED)
                                                .displayOneTime(92).setDelay(0)
                                                .build();
                                        showtips1
                                                .show(CreateNewMessageActivity.this);
                                        btn_site_name.setText(SiteName.get(pos));
                                        siteid = Integer.parseInt(SiteID.get(pos));
                                        PW_sitename.dismiss();
                                        btn_cm_to.setEnabled(true);
                                        btn_cm_open_cc.setEnabled(true);
                                        btn_cm_cc.setEnabled(true);
                                    } catch (NumberFormatException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (NotFoundException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            });

                }
            }
        }
    }


    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.btn_cm_to:
                    TO_CC_CLICK = 0;
                    Intent intentNew = new Intent(CreateNewMessageActivity.this,
                            ToAndCcSelectionActivity.class);
                    intentNew.putExtra("siteid", siteid);
                    intentNew.putExtra("WhichClick", TO_CC_CLICK);
                    intentNew.putExtra("sitename", btn_site_name.getText()
                            .toString());
                    startActivity(intentNew);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    // final Toast toast = Toast.makeText(getApplicationContext(),
                    // "Users loading... Please wait for few moment.",
                    // Toast.LENGTH_LONG);
                    // toast.show();
                    //
                    // Handler handler = new Handler();
                    // handler.postDelayed(new Runnable() {
                    // @Override
                    // public void run() {
                    // toast.cancel();
                    // }
                    // }, 4000);
                    // TO_CC_DIALOG();
                    break;
                case R.id.btn_cm_open_cc:
                    rl_cm_cc.setVisibility(View.VISIBLE);
                    btn_cm_open_cc.setVisibility(View.GONE);
                    break;
                case R.id.btn_cm_cc:
                    TO_CC_CLICK = 1;
                    Intent intentNew1 = new Intent(CreateNewMessageActivity.this,
                            ToAndCcSelectionActivity.class);
                    intentNew1.putExtra("siteid", siteid);
                    intentNew1.putExtra("WhichClick", TO_CC_CLICK);
                    intentNew1.putExtra("sitename", btn_site_name.getText()
                            .toString());
                    startActivity(intentNew1);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    // final Toast toast1 = Toast.makeText(getApplicationContext(),
                    // "Users loading... Please wait for few moment.",
                    // Toast.LENGTH_LONG);
                    // toast1.show();
                    //
                    // Handler handler1 = new Handler();
                    // handler1.postDelayed(new Runnable() {
                    // @Override
                    // public void run() {
                    // toast1.cancel();
                    // }
                    // }, 4000);
                    // Temp_TO_CC_DIALOG();
                    break;
                case R.id.cm_show_to:
                    ib_cm_show_to.setVisibility(View.INVISIBLE);
                    ib_cm_hide_to.setVisibility(View.VISIBLE);
                    tv_cm_to.setMaxLines(Integer.MAX_VALUE);
                    break;
                case R.id.cm_hide_to:
                    ib_cm_hide_to.setVisibility(View.INVISIBLE);
                    ib_cm_show_to.setVisibility(View.VISIBLE);
                    tv_cm_to.setMaxLines(1);
                    break;
                case R.id.cm_show_cc:
                    ib_cm_show_cc.setVisibility(View.INVISIBLE);
                    ib_cm_hide_cc.setVisibility(View.VISIBLE);
                    tv_cm_cc.setMaxLines(Integer.MAX_VALUE);
                    break;
                case R.id.cm_hide_cc:
                    ib_cm_hide_cc.setVisibility(View.INVISIBLE);
                    ib_cm_show_cc.setVisibility(View.VISIBLE);
                    tv_cm_cc.setMaxLines(1);
                    break;
                case R.id.btn_cm_priority:
                    PW_Priority.show();
                    break;
                case R.id.btn_site_name:
                    PW_sitename.show();
                    break;
                case R.id.tv_cm_to:
                    ArrayList<String> final_name = new ArrayList<String>();
                    final ArrayList<String> final_id = new ArrayList<String>();
                    for (int i = 0; i < TO_NAME.size(); i++) {
                        if (final_name.contains(TO_NAME.get(i))) {

                        } else {
                            final_name.add(TO_NAME.get(i));
                            final_id.add(TO_ID.get(i));
                        }
                    }
                    final Dialog dialog = new Dialog(CreateNewMessageActivity.this);
                    // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setTitle("Send To:");
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.WHITE));
                    dialog.setContentView(R.layout.final_to_cc_dialog);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(true);
                    dialog.getWindow().getAttributes().width = LayoutParams.WRAP_CONTENT;
                    final ListView lv_final_to_cc = (ListView) dialog
                            .findViewById(R.id.lv_final_to_cc_dialog);
                    Button save = (Button) dialog
                            .findViewById(R.id.btn_final_to_cc_save);
                    Button cancel = (Button) dialog
                            .findViewById(R.id.btn_final_to_cc_cancel);
                    lv_final_to_cc.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            CreateNewMessageActivity.this,
                            android.R.layout.simple_list_item_multiple_choice,
                            final_name);
                    adapter.hasStableIds();
                    lv_final_to_cc.setAdapter(adapter);

                    for (int i = 0; i < final_name.size(); i++) {
                        if (final_name.get(i).equalsIgnoreCase("All")) {
                            final_name.remove(i);
                        }
                        if (final_id.get(i).equalsIgnoreCase("-1")) {
                            final_id.remove(i);
                        }
                    }
                    for (int i = 0; i < final_name.size(); i++) {
                        lv_final_to_cc.setItemChecked(i, true);
                    }
                    save.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            String name = "";
                            SparseBooleanArray checkedO = lv_final_to_cc
                                    .getCheckedItemPositions();
                            for (int i = 0; i < checkedO.size(); i++) {
                                // Item position in adapter
                                int position = checkedO.keyAt(i);
                                // Add sport if it is checked i.e.) == TRUE!
                                if (checkedO.valueAt(i)) {
                                    name = name + "," + adapter.getItem(position);
                                    Final_TO_ID.add(final_id.get(position));
                                }
                            }
                            if (name.toString().equalsIgnoreCase("")) {
                            } else {
                                if (name.charAt(0) == ',') {
                                    name = name.substring(1);
                                }
                            }
                            Log.i(TAG, "Final to id = " + Final_TO_ID);
                            tv_cm_to.setText(name);
                            tv_cm_to.setEnabled(false);
                            btn_cm_to.setEnabled(false);
                            tv_to_click = true;
                            dialog.cancel();

                        }
                    });
                    cancel.setOnClickListener(new OnClickListener() {


                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                    break;
                case R.id.tv_cm_cc:
                    ArrayList<String> final_name1 = new ArrayList<String>();
                    final ArrayList<String> final_id1 = new ArrayList<String>();
                    for (int i = 0; i < CC_NAME.size(); i++) {
                        if (final_name1.contains(CC_NAME.get(i))) {

                        } else {
                            final_name1.add(CC_NAME.get(i));
                            final_id1.add(CC_ID.get(i));
                        }
                    }
                    final Dialog dialog1 = new Dialog(CreateNewMessageActivity.this);
                    // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setTitle("Send CC:");
                    dialog1.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.WHITE));
                    dialog1.setContentView(R.layout.final_to_cc_dialog);
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.setCancelable(true);
                    dialog1.getWindow().getAttributes().width = LayoutParams.WRAP_CONTENT;
                    final ListView lv_final_to_cc1 = (ListView) dialog1
                            .findViewById(R.id.lv_final_to_cc_dialog);
                    Button save1 = (Button) dialog1
                            .findViewById(R.id.btn_final_to_cc_save);
                    Button cancel1 = (Button) dialog1
                            .findViewById(R.id.btn_final_to_cc_cancel);
                    lv_final_to_cc1.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                    final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                            CreateNewMessageActivity.this,
                            android.R.layout.simple_list_item_multiple_choice,
                            final_name1);
                    adapter1.hasStableIds();
                    lv_final_to_cc1.setAdapter(adapter1);

                    for (int i = 0; i < final_name1.size(); i++) {
                        if (final_name1.get(i).equalsIgnoreCase("All")) {
                            final_name1.remove(i);
                        }
                        if (final_id1.get(i).equalsIgnoreCase("-1")) {
                            final_id1.remove(i);
                        }
                    }
                    for (int i = 0; i < final_name1.size(); i++) {
                        lv_final_to_cc1.setItemChecked(i, true);
                    }
                    save1.setOnClickListener(new OnClickListener() {


                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            String name = "";
                            SparseBooleanArray checkedO = lv_final_to_cc1
                                    .getCheckedItemPositions();
                            for (int i = 0; i < checkedO.size(); i++) {
                                // Item position in adapter
                                int position = checkedO.keyAt(i);
                                // Add sport if it is checked i.e.) == TRUE!
                                if (checkedO.valueAt(i)) {
                                    name = name + "," + adapter1.getItem(position);
                                    Final_CC_ID.add(final_id1.get(position));
                                }
                            }
                            if (name.charAt(0) == ',') {
                                name = name.substring(1);
                            }
                            Log.i(TAG, "Final cc id = " + Final_CC_ID);
                            tv_cm_cc.setText(name);
                            tv_cm_cc.setEnabled(false);
                            btn_cm_cc.setEnabled(false);
                            tv_cc_click = true;
                            dialog1.cancel();

                        }
                    });
                    cancel1.setOnClickListener(new OnClickListener() {


                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            dialog1.cancel();
                        }
                    });
                    dialog1.show();
                    break;
                default:
                    break;
            }
        } else {
            onDetectNetworkState().show();
        }
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
                                CreateNewMessageActivity.this.finish();
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

    boolean chkReadRec = false;

    private class SendMessage extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (chb_cm_read.isChecked()) {
                chkReadRec = true;
            } else {
                chkReadRec = false;
            }
            if (btn_cm_priority.getText().toString().equalsIgnoreCase("Low")) {
                priority_id = "0";
            } else if (btn_cm_priority.getText().toString()
                    .equalsIgnoreCase("Medium")) {
                priority_id = "1";
            } else if (btn_cm_priority.getText().toString()
                    .equalsIgnoreCase("High")) {
                priority_id = "2";
            }
            if (tv_to_click) {

            } else {
                Final_TO_ID.addAll(TO_ID);
            }
            if (tv_cc_click) {
            } else {
                Final_CC_ID.addAll(CC_ID);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_NewMail_SendMail);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("subject", subjectStr);
            request.addProperty("message",messageStr);
            request.addProperty(
                    "emailto",
                    Final_TO_ID.toString().replaceAll("\\[", "")
                            .replaceAll("\\]", ""));
            request.addProperty(
                    "emailcc",
                    Final_CC_ID.toString().replaceAll("\\[", "")
                            .replaceAll("\\]", ""));
            request.addProperty("chkReadRec", chkReadRec);
            request.addProperty("ddlPriority", priority_id);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(
                        SOAP_CONSTANTS.SOAP_Action_NewMail_NewMail_SendMail,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                // SoapPrimitive response = (SoapPrimitive)
                // envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i(TAG, "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                Log.i(TAG, "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    mailsend = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    Log.e(TAG, jo.getString("SendMail"));
                    mailsended = jo.getString("SendMail");
                } else if (code.equalsIgnoreCase("111")) {
                    mailsend = false;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    Log.e(TAG, jo.getString("SendMail"));
                    mailsended = jo.getString("SendMail");

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
                SingleOptionAlertWithoutTitle.ShowAlertDialog(
                        CreateNewMessageActivity.this, "Error", "SERVER DOWN",
                        "OK");
                server_response = false;
            } else {
                if (!mailsend) {
                    Toast.makeText(CreateNewMessageActivity.this, mailsended, Toast.LENGTH_LONG)
                            .show();
                    mailsend = false;
                } else {
                    Toast.makeText(CreateNewMessageActivity.this, mailsended, Toast.LENGTH_LONG)
                            .show();
                    Intent messagecenterIntent = new Intent(
                            CreateNewMessageActivity.this,
                            MessageCenterActivity.class);
                    startActivity(messagecenterIntent);
                    WW_StaticClass.lable = "Inbox";
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,
                            R.anim.slide_out_right);
                }
            }
        }
    }

    String mailsended = "";

}

