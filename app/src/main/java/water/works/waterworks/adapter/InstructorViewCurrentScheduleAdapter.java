package water.works.waterworks.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import water.works.waterworks.MyTagHandler;
import water.works.waterworks.NewModifyComments;
import water.works.waterworks.R;
import water.works.waterworks.ViewCurrentScheduleFragment;
import water.works.waterworks.ViewCurrentScheduleInstructorActivity;
import water.works.waterworks.model.InstructorViewCurrentScheduleAdapterItem;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class InstructorViewCurrentScheduleAdapter extends BaseAdapter implements
        AnimationListener {
    // List<CurrentScheduleAdapterItem> data1;
    List<InstructorViewCurrentScheduleAdapterItem> data;
    Context context;
    int layoutResID;
    Animation animBlink;
    ArrayList<Boolean> ischange;
    ArrayList<String> newslevel, newschdlevel, newprereqid, isched, comments,
            wu_sscheduleid, wu_studentid, wu_orderdetailid, wu_lessontypeid,
            wu_sttimehr, wu_sttimemin, wu_scheduledate, ddlW, ddlB, ddlR,
            wu_siteid, wu_slevel, ddlSchedLevel, wu_schedlevel, chkschedselect,
            straarylist, Msg_Status, Msg_Str, prereqid, lev, oldlev, newddlw,
            newddlb, newddlr;
    String siteid;
    ArrayList<String> FinalPreReqId = new ArrayList<String>();
    ArrayList<Character> levelchanged;
    String comment = "", reason = "", SwimCompId = "", StudentId = "",
            UserToken, UserLevel;
    boolean attendance_response = false, server_response = false,
            cancel_response = false, connectionout = false,
            login_status = false;
    View temp_view;
    String Instructor_ID = "";
    final static ArrayList<String> checked = new ArrayList<String>();
    final static ArrayList<String> checked_att = new ArrayList<String>();

    public InstructorViewCurrentScheduleAdapter(
            List<InstructorViewCurrentScheduleAdapterItem> data, Context context) {
        super();
        this.data = data;
        this.context = context;
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }


    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }


    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public class ViewHolder {
        // TextView tv_time;
        // TextView tv_view_schedule_students;
        // LinearLayout ll_main, ll_student;
        TextView tv_sName, tv_pName, tv_sAge, tv_sPaid_cls, tv_sComments, tv_cl_time,
                tv_cancel, tv_count;
        ImageView iv_sLate;
        CheckBox review_check, review_atta;

        ToggleButton sw_precent;
        TableLayout tl_skills;
        ImageView btn_sIsa;
        ImageButton level_add, level_sub, sch_level_add, sch_level_sub,
                btn_sBirthday, btn_sNote, btn_sCamera;
        Button btn_more, btn_sLevel, btn_sSch_leve, btn_sSwim_comp,
                btn_sCls_lvl;
        int oldlevel, newlevel;
        ListPopupWindow listpopupwindow, listpopupwindow1;
        Dialog dialog;
        TextView tv_instructorname_row;
        RelativeLayout rl_cl_third_bar_row3;
    }

    private int[] colors = new int[]{Color.parseColor("#EEEEEE"),
            Color.parseColor("#FFFFFF")};


    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.viewcurrentscheduleinstructor_list_item, null);
            int colorpos = position % colors.length;
            convertView.setBackgroundColor(colors[colorpos]);
            holder = new ViewHolder();
            /*
             * holder.ll_main = (LinearLayout) convertView
			 * .findViewById(R.id.front);
			 * holder.ll_main.setBackgroundColor(colors[colorpos]);
			 * holder.ll_student = (LinearLayout) convertView
			 * .findViewById(R.id.ll_view_schedule_students);
			 * holder.tv_view_schedule_students = (TextView) convertView
			 * .findViewById(R.id.tv_view_schedule_students);
			 */
            // holder.tv_time = (TextView)
            // convertView.findViewById(R.id.tv_time);
            holder.tv_sName = (TextView) convertView
                    .findViewById(R.id.tv_cl_studentname_row);
            holder.tv_pName = (TextView) convertView
                    .findViewById(R.id.tv_cl_parentname_row);
            holder.tv_sAge = (TextView) convertView
                    .findViewById(R.id.tv_cl_age_row);
            holder.review_check = (CheckBox) convertView
                    .findViewById(R.id.review_check);
            holder.review_atta = (CheckBox) convertView
                    .findViewById(R.id.review_atta);
            /*
			 * holder.tv_count = (TextView) convertView
			 * .findViewById(R.id.tv_cl_count_row);
			 */

            holder.rl_cl_third_bar_row3 = (RelativeLayout) convertView.findViewById(R.id.rl_cl_third_bar_row3);
            holder.tv_cancel = (TextView) convertView
                    .findViewById(R.id.tv_table_cancel);
            holder.tl_skills = (TableLayout) convertView
                    .findViewById(R.id.table_cl_data);
            holder.sw_precent = (ToggleButton) convertView
                    .findViewById(R.id.yes_no);
            holder.btn_sIsa = (ImageView) convertView
                    .findViewById(R.id.btn_isa_alert);
            holder.btn_more = (Button) convertView.findViewById(R.id.more);
            holder.btn_sLevel = (Button) convertView
                    .findViewById(R.id.btn_cl_slevel);
            holder.btn_sSch_leve = (Button) convertView
                    .findViewById(R.id.btn_cl_sched_level);
            holder.level_add = (ImageButton) convertView
                    .findViewById(R.id.ib_plus_level);
            holder.level_sub = (ImageButton) convertView
                    .findViewById(R.id.ib_sub_level);
            holder.sch_level_add = (ImageButton) convertView
                    .findViewById(R.id.ib_plus_sch_level);
            holder.sch_level_sub = (ImageButton) convertView
                    .findViewById(R.id.ib_sub_sch_level);
            holder.btn_sBirthday = (ImageButton) convertView
                    .findViewById(R.id.btn_cake);
            holder.btn_sSwim_comp = (Button) convertView
                    .findViewById(R.id.btn_swim_comp);
            holder.tv_sComments = (TextView) convertView
                    .findViewById(R.id.tv_cl_comment);
            holder.tv_cl_time = (TextView) convertView.findViewById(R.id.tv_cl_time);
            holder.btn_sNote = (ImageButton) convertView
                    .findViewById(R.id.btn_cl_note);
            holder.btn_sCamera = (ImageButton) convertView
                    .findViewById(R.id.btn_camera);
            holder.iv_sLate = (ImageView) convertView
                    .findViewById(R.id.img_late);
            holder.btn_sCls_lvl = (Button) convertView
                    .findViewById(R.id.btn_cl_cls_lvl_row);
            holder.tv_sPaid_cls = (TextView) convertView
                    .findViewById(R.id.tv_cl_paid_cls_row);
            holder.listpopupwindow = new ListPopupWindow(
                    context.getApplicationContext());
            holder.listpopupwindow1 = new ListPopupWindow(
                    context.getApplicationContext());
            holder.tv_instructorname_row = (TextView) convertView
                    .findViewById(R.id.tv_instructorname_row);
            ischange = new ArrayList<Boolean>();
            newslevel = new ArrayList<String>();
            newschdlevel = new ArrayList<String>();
            newprereqid = new ArrayList<String>();
            ddlW = new ArrayList<String>();
            ddlB = new ArrayList<String>();
            ddlR = new ArrayList<String>();
            for (int i = 0; i < data.size(); i++) {
                ischange.add(i, false);
                newslevel.add(i, data.get(i).getSLevel());
                newschdlevel.add(i, data.get(i).getScheLevel());
                ddlW.add(i, data.get(i).getWu_w());
                ddlB.add(i, data.get(i).getWu_b());
                ddlR.add(i, data.get(i).getWu_r());
            }
            Instructor_ID = data.get(position).getINSTRUCTORID();
            // getInstructorID
            try {
                if (position == 0) {
                    holder.tv_instructorname_row
                            .setText(ViewCurrentScheduleInstructorActivity.Instroctorname
                                    .get(ViewCurrentScheduleInstructorActivity.Instroctorid
                                            .indexOf(data.get(position)
                                                    .getInstructorID())) + " (" + data.get(position).getLessonName() + ")");
                    holder.tv_instructorname_row.setVisibility(View.VISIBLE);
                } else {
                    if (data.get(position)
                            .getInstructorID()
                            .toString()
                            .equalsIgnoreCase(
                                    data.get(position - 1).getInstructorID()
                                            .toString())) {
                        holder.tv_instructorname_row.setText("");
                        holder.tv_instructorname_row.setVisibility(View.GONE);

                    } else {
                        holder.tv_instructorname_row
                                .setText(ViewCurrentScheduleInstructorActivity.Instroctorname
                                        .get(ViewCurrentScheduleInstructorActivity.Instroctorid
                                                .indexOf(data.get(position)
                                                        .getInstructorID())) + " (" + data.get(position).getLessonName() + ")");
                        holder.tv_instructorname_row.setVisibility(View.VISIBLE);
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            animBlink = AnimationUtils.loadAnimation(
                    context.getApplicationContext(), R.anim.blink);
            animBlink.setAnimationListener(this);
            // ////////Att taken//////////////
            int temp_att = Integer.parseInt(data.get(position).getAtt());
            if (Integer.parseInt(data.get(position).getWu_attendancetaken()) == 0) {
                convertView.setBackgroundColor(colors[colorpos]);
                holder.tl_skills.setVisibility(View.VISIBLE);
                holder.tv_cancel.setVisibility(View.GONE);
                holder.sw_precent.setChecked(true);
                holder.sw_precent.setEnabled(true);
                if (data.get(position).getISAAlert().equalsIgnoreCase("true")) {
                    convertView.setBackgroundColor(Color.rgb(207, 255, 191));
                    holder.btn_sIsa.setVisibility(View.VISIBLE);
                    holder.btn_sIsa.startAnimation(animBlink);
                } else {
                    holder.btn_sIsa.setVisibility(View.INVISIBLE);
                    convertView.setBackgroundColor(colors[colorpos]);
                }
            } else {
                if (temp_att == 2 || temp_att == 3 || temp_att == 4
                        || temp_att == 5 || temp_att == 6 || temp_att == 7
                        || temp_att == 8 || temp_att == 10 || temp_att == 12
                        || temp_att == 13 || temp_att == 14 || temp_att == 15
                        || temp_att == 16 || temp_att == 17) {
                    convertView.setBackgroundColor(Color.rgb(252, 106, 108));
                    holder.tl_skills.setVisibility(View.GONE);
                    holder.btn_more.setVisibility(View.GONE);
                    holder.tv_cancel.setVisibility(View.VISIBLE);
                    holder.sw_precent.setChecked(false);
                    holder.sw_precent.setEnabled(false);

                    if (data.get(position).getISAAlert()
                            .equalsIgnoreCase("true")) {
                        holder.btn_sIsa.setVisibility(View.VISIBLE);
                        holder.btn_sIsa.startAnimation(animBlink);
                    } else {
                        holder.btn_sIsa.setVisibility(View.INVISIBLE);
                    }
                } else if (temp_att == 0) {
                    convertView.setBackgroundColor(colors[colorpos]);
                    holder.tl_skills.setVisibility(View.VISIBLE);
                    holder.tv_cancel.setVisibility(View.GONE);
                    holder.sw_precent.setChecked(true);
                    holder.sw_precent.setEnabled(false);
                    if (data.get(position).getISAAlert()
                            .equalsIgnoreCase("true")) {
                        convertView
                                .setBackgroundColor(Color.rgb(207, 255, 191));
                        holder.btn_sIsa.setVisibility(View.VISIBLE);
                        holder.btn_sIsa.startAnimation(animBlink);
                    } else {
                        holder.btn_sIsa.setVisibility(View.INVISIBLE);
                        convertView.setBackgroundColor(colors[colorpos]);
                    }
                } else {
                    convertView.setBackgroundColor(colors[colorpos]);
                    holder.tl_skills.setVisibility(View.GONE);
                    holder.tv_cancel.setVisibility(View.GONE);
                    holder.sw_precent.setChecked(false);
                    holder.sw_precent.setEnabled(false);
                    if (data.get(position).getISAAlert()
                            .equalsIgnoreCase("true")) {
                        convertView
                                .setBackgroundColor(Color.rgb(207, 255, 191));
                        holder.btn_sIsa.setVisibility(View.VISIBLE);
                        holder.btn_sIsa.startAnimation(animBlink);
                    } else {
                        holder.btn_sIsa.setVisibility(View.INVISIBLE);
                        convertView.setBackgroundColor(colors[colorpos]);
                    }
                }

            }
            holder.tv_cl_time.setText(data.get(position).getFormatStTimeHour()+":"+data.get(position).getFormatStTimeMin());
            if (!data.get(position).getCheckValue().equalsIgnoreCase("0")) {
                holder.rl_cl_third_bar_row3.setVisibility(View.VISIBLE);
            } else {
                holder.rl_cl_third_bar_row3.setVisibility(View.GONE);
            }
            // ////////// sw precent click///////////////
            holder.sw_precent.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (holder.sw_precent.isChecked()) {
                        ViewCurrentScheduleFragment.newatt.remove(position);
                        ViewCurrentScheduleFragment.newatt.add(position, 0);
                    } else {
                        ViewCurrentScheduleFragment.newatt.remove(position);
                        ViewCurrentScheduleFragment.newatt.add(position, 1);
                    }

                    Log.i("todays new att",
                            ViewCurrentScheduleFragment.newatt.toString());
                }
            });


            holder.review_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (isChecked) {
                        System.out.println("Checked : " + holder.review_check.getTag());
                        checked.add(String.valueOf(holder.review_check.getTag()));
                    } else {
                        if (checked.contains(holder.review_check.getTag())) {
                            checked.remove(holder.review_check.getTag());
                        }
                    }
                    System.out.println("Checked Array : " + checked.toString());
                }
            });

            holder.review_atta.setOnCheckedChangeListener(new OnCheckedChangeListener() {


                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub
                    if (isChecked) {
                        System.out.println("Checked atta: " + holder.review_atta.getTag());
                        checked_att.add(String.valueOf(holder.review_atta.getTag()));
                    } else {
                        if (checked_att.contains(holder.review_atta.getTag())) {
                            checked_att.remove(holder.review_atta.getTag());
                        }
                    }
                    System.out.println("Checked Array : " + checked_att.toString());
                }
            });

			/*
			 * holder.sw_precent .setOnCheckedChangeListener(new
			 * OnCheckedChangeListener() {
			 * 
			 * @Override public void onCheckedChanged(CompoundButton buttonView,
			 * boolean isChecked) { // TODO Auto-generated method stub if
			 * (isChecked) { newatt.remove(position); newatt.add(position, 0);
			 * Log.i("new att", "new att = " + newatt.toString()); } else {
			 * newatt.remove(position); newatt.add(position, 1);
			 * Log.i("new att", "new att = " + newatt.toString()); } } });
			 */

            String am_pm;
            if (Integer.parseInt(data.get(position).getStTimeHour()) > 11) {
                am_pm = "PM";
            } else {
                am_pm = "AM";
            }
            if (Integer.parseInt(data.get(position).getStTimeHour()) == 13) {
                data.get(position).setStTimeHour("01");
            } else if (Integer.parseInt(data.get(position).getStTimeHour()) == 14) {
                data.get(position).setStTimeHour("02");
            } else if (Integer.parseInt(data.get(position).getStTimeHour()) == 15) {
                data.get(position).setStTimeHour("03");
            } else if (Integer.parseInt(data.get(position).getStTimeHour()) == 16) {
                data.get(position).setStTimeHour("04");
            } else if (Integer.parseInt(data.get(position).getStTimeHour()) == 17) {
                data.get(position).setStTimeHour("05");
            } else if (Integer.parseInt(data.get(position).getStTimeHour()) == 18) {
                data.get(position).setStTimeHour("06");
            } else if (Integer.parseInt(data.get(position).getStTimeHour()) == 19) {
                data.get(position).setStTimeHour("07");
            } else if (Integer.parseInt(data.get(position).getStTimeHour()) == 20) {
                data.get(position).setStTimeHour("08");
            } else if (Integer.parseInt(data.get(position).getStTimeHour()) == 21) {
                data.get(position).setStTimeHour("09");
            } else if (Integer.parseInt(data.get(position).getStTimeHour()) == 22) {
                data.get(position).setStTimeHour("10");
            } else if (Integer.parseInt(data.get(position).getStTimeHour()) == 23) {
                data.get(position).setStTimeHour("11");
            }
            if (data.get(position).getStTimeMin().length() == 1) {
                data.get(position).setStTimeMin(
                        "0" + data.get(position).getStTimeMin());
            }
            if (data.get(position).getWu_avail() == 0
                    || data.get(position).getWu_avail() == 1) {
            } else {
                String gender = data.get(position).getStudentGender().trim();
                String sname = data.get(position).getSFirstName() + " "
                        + data.get(position).getSLastName();
                String fname = "(" + data.get(position).getParentFirstName()
                        + " " + data.get(position).getParentLastName() + ")";
                if (gender.toString().equalsIgnoreCase("Female")) {
                    holder.tv_sName.setTextColor(Color.rgb(136, 0, 183));
                    holder.tv_sName.setText(sname);

                } else {
                    holder.tv_sName.setTextColor(Color.rgb(0, 0, 102));
                    holder.tv_sName.setText(sname);

                }

                holder.review_check.setTag(data.get(position).getStudentID() + "|" + data.get(position).getSFirstName() + " " +
                        data.get(position).getSLastName() + "|" + data.get(position).getSiteID());
                holder.review_atta.setTag(position);
                holder.tv_pName.setText(fname);

                holder.tv_sAge.setText(data.get(position).getSAge());

				/*
				 * if (Integer.parseInt(data.get(position).getWu_count()) > 0) {
				 * holder.tv_count.setText("Paid"); } else {
				 * holder.tv_count.setText("UnPaid"); }
				 */
                String lvlavail = data.get(position).getLvlAdvAvail();
                if (Integer.parseInt(lvlavail) > 1) {
                    holder.iv_sLate.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_sLate.setVisibility(View.INVISIBLE);
                }

                // /Paid class/////
                String paidcls = data.get(position).getPaidClasses();
                String temp1[] = paidcls.toString().split("\\.");
                int paid_cls = Integer.parseInt(temp1[0]);
                if (paid_cls < 2) {

                    holder.tv_sPaid_cls.setText(Html.fromHtml("<b>" + paid_cls
                            + "</b>"));
                    holder.tv_sPaid_cls.setBackgroundColor(Color.RED);
                    holder.tv_sPaid_cls.startAnimation(animBlink);
                } else {
                    holder.tv_sPaid_cls.setText("" + paid_cls);
                }
                // / For cls lvl /////////
                String cls_lvl = data.get(position).getClsLvl();
                holder.btn_sCls_lvl.setText(cls_lvl);

                // ///////////////For level and schedule
                // level//////////////////////
                holder.oldlevel = Integer.parseInt(data.get(position)
                        .getSLevel());
                holder.newlevel = Integer.parseInt(data.get(position)
                        .getSLevel());
                final ArrayList<String> LevelName = new ArrayList<String>();
                final ArrayList<String> LevelID = new ArrayList<String>();
                LevelName.addAll(data.get(position).getLevelName());
                LevelID.addAll(data.get(position).getLevelID());
                String levelname = LevelName.get(LevelID.indexOf(data.get(
                        position).getSLevel()));
                String schlevelname = LevelName.get(LevelID.indexOf(data.get(
                        position).getSLevel()));
                if (levelname.length() == 1) {
                    levelname = "0" + levelname;
                }
                if (schlevelname.length() == 1) {
                    schlevelname = "0" + schlevelname;
                }
                holder.btn_sLevel.setText(levelname);
                holder.btn_sSch_leve.setText(schlevelname);

                holder.level_add.setOnClickListener(new OnClickListener() {

                    @SuppressWarnings("deprecation")

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        int size = LevelName.size();
                        String temp_level = holder.btn_sLevel.getText()
                                .toString();
                        final String templevel, tempschdlevel;
                        templevel = holder.btn_sLevel.getText().toString();
                        tempschdlevel = holder.btn_sSch_leve.getText()
                                .toString();
                        if (temp_level.charAt(0) == '0') {
                            temp_level = "" + temp_level.charAt(1);
                        }
                        final int index = LevelName.indexOf(temp_level);
                        if (size == index + 1) {
                            Toast.makeText(context, "Level Maximum", Toast.LENGTH_LONG).show();
                        } else {
                            String lname = LevelName.get(index + 1);
                            if (lname.length() == 1) {
                                lname = "0" + lname;
                            }
                            holder.btn_sLevel.setText(lname);
                            holder.btn_sSch_leve.setText(lname);
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    context).create();
                            alertDialog.setTitle("AquaticsApp");
                            alertDialog.setIcon(R.drawable.ic_launcher);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            // set the message
                            alertDialog
                                    .setMessage("You have selected to change this student’s level. From "
                                            + temp_level
                                            + " To "
                                            + lname
                                            + " Is this correct?");
                            // set button1 functionality
                            alertDialog.setButton("Yes",
                                    new DialogInterface.OnClickListener() {


                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // close dialog
                                            ischange.remove(position);
                                            ischange.add(position, true);
                                            holder.newlevel = Integer
                                                    .parseInt(LevelID
                                                            .get(index + 1));

                                            if (ischange.get(position).equals(
                                                    true)) {
                                                newslevel.remove(position);
                                                newschdlevel.remove(position);
                                                newslevel.add(position,
                                                        LevelID.get(index + 1));
                                                newschdlevel.add(position,
                                                        LevelID.get(index + 1));
                                                if (holder.newlevel == 4) {
                                                    if ((holder.oldlevel == 11)
                                                            || (holder.oldlevel == 12)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.1",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 5) {
                                                    if ((holder.oldlevel == 11)
                                                            || (holder.oldlevel == 12)
                                                            || (holder.oldlevel == 13)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.2",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 6) {
                                                    if (holder.oldlevel == 4) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.3",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 7) {
                                                    if ((holder.oldlevel == 4)
                                                            || (holder.oldlevel == 5)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.4",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 8) {
                                                    if (((holder.oldlevel >= 4) && (holder.oldlevel <= 6))
                                                            && (holder.newlevel != holder.oldlevel)
                                                            && (holder.oldlevel != 8)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.5",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 9) {
                                                    if (((holder.oldlevel >= 4) && (holder.oldlevel <= 7))
                                                            && (holder.newlevel != holder.oldlevel)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.6",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 10) {
                                                    if (((holder.oldlevel >= 4) && (holder.oldlevel <= 8))
                                                            && (holder.newlevel != holder.oldlevel)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.7",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 13) {
                                                    if ((holder.oldlevel == 11)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.8",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 14) {
                                                    if ((holder.oldlevel >= 4)
                                                            && (holder.oldlevel <= 9)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.9",
                                                                "Ok");
                                                    }
                                                    if ((holder.oldlevel >= 11)
                                                            && (holder.oldlevel <= 13)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.10",
                                                                "Ok");
                                                    }
                                                }
                                            }
                                            dialog.cancel();

                                        }
                                    });
                            alertDialog.setButton2("No",
                                    new DialogInterface.OnClickListener() {


                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // TODO Auto-generated method stub
                                            dialog.cancel();
                                            holder.btn_sLevel
                                                    .setText(templevel);
                                            holder.btn_sSch_leve
                                                    .setText(tempschdlevel);
                                        }
                                    });
                            // show the alert dialog
                            alertDialog.show();
                        }
                    }
                });

                holder.level_sub.setOnClickListener(new OnClickListener() {

                    @SuppressWarnings("deprecation")

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        int size = 0;
                        String temp_level = holder.btn_sLevel.getText()
                                .toString();
                        final String templevel, tempschdlevel;
                        templevel = holder.btn_sLevel.getText().toString();
                        tempschdlevel = holder.btn_sSch_leve.getText()
                                .toString();
                        if (temp_level.charAt(0) == '0') {
                            temp_level = "" + temp_level.charAt(1);
                        }
                        final int index = LevelName.indexOf(temp_level);
                        if (size == index) {
                            Toast.makeText(context, "Level Minimum.", Toast.LENGTH_LONG).show();
                        } else {
                            String lname = LevelName.get(index - 1);
                            if (lname.length() == 1) {
                                lname = "0" + lname;
                            }
                            holder.btn_sLevel.setText(lname);
                            holder.btn_sSch_leve.setText(lname);
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    context).create();
                            alertDialog.setTitle("AquaticsApp");
                            alertDialog.setIcon(R.drawable.ic_launcher);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            // set the message
                            alertDialog
                                    .setMessage("You have selected to change this student’s level. From "
                                            + temp_level
                                            + " To "
                                            + lname
                                            + " Is this correct?");
                            // set button1 functionality
                            alertDialog.setButton("Yes",
                                    new DialogInterface.OnClickListener() {


                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // close dialog
                                            ischange.remove(position);
                                            ischange.add(position, true);
                                            holder.newlevel = Integer
                                                    .parseInt(LevelID
                                                            .get(index - 1));
                                            if (ischange.get(position).equals(
                                                    true)) {
                                                newslevel.remove(position);
                                                newschdlevel.remove(position);
                                                newslevel.add(position,
                                                        LevelID.get(index + 1));
                                                newschdlevel.add(position,
                                                        LevelID.get(index + 1));
                                                if (holder.newlevel == 4) {
                                                    if ((holder.oldlevel == 11)
                                                            || (holder.oldlevel == 12)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.1",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 5) {
                                                    if ((holder.oldlevel == 11)
                                                            || (holder.oldlevel == 12)
                                                            || (holder.oldlevel == 13)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.2",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 6) {
                                                    if (holder.oldlevel == 4) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.3",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 7) {
                                                    if ((holder.oldlevel == 4)
                                                            || (holder.oldlevel == 5)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.4",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 8) {
                                                    if (((holder.oldlevel >= 4) && (holder.oldlevel <= 6))
                                                            && (holder.newlevel != holder.oldlevel)
                                                            && (holder.oldlevel != 8)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.5",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 9) {
                                                    if (((holder.oldlevel >= 4) && (holder.oldlevel <= 7))
                                                            && (holder.newlevel != holder.oldlevel)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.6",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 10) {
                                                    if (((holder.oldlevel >= 4) && (holder.oldlevel <= 8))
                                                            && (holder.newlevel != holder.oldlevel)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.7",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 13) {
                                                    if ((holder.oldlevel == 11)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.8",
                                                                "Ok");
                                                    }
                                                } else if (holder.newlevel == 14) {
                                                    if ((holder.oldlevel >= 4)
                                                            && (holder.oldlevel <= 9)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.9",
                                                                "Ok");
                                                    }
                                                    if ((holder.oldlevel >= 11)
                                                            && (holder.oldlevel <= 13)) {
                                                        showAlert(
                                                                context,
                                                                "AquaticsApp",
                                                                "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.10",
                                                                "Ok");
                                                    }
                                                }
                                            }

                                            dialog.cancel();

                                        }
                                    });
                            alertDialog.setButton2("No",
                                    new DialogInterface.OnClickListener() {


                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // TODO Auto-generated method stub
                                            dialog.cancel();
                                            holder.btn_sLevel
                                                    .setText(templevel);
                                            holder.btn_sSch_leve
                                                    .setText(tempschdlevel);
                                        }
                                    });
                            // show the alert dialog
                            alertDialog.show();
                        }
                    }
                });

                holder.sch_level_add.setOnClickListener(new OnClickListener() {

                    @SuppressWarnings("deprecation")

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        int size = LevelName.size();
                        String temp_level = holder.btn_sSch_leve.getText()
                                .toString();
                        final String templevel = holder.btn_sSch_leve.getText()
                                .toString();
                        if (temp_level.charAt(0) == '0') {
                            temp_level = "" + temp_level.charAt(1);
                        }
                        final int index = LevelName.indexOf(temp_level);
                        if (size == index + 1) {
                            Toast.makeText(context, "Schedule Level Maximum", Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            String lname = LevelName.get(index + 1);
                            if (lname.length() == 1) {
                                lname = "0" + lname;
                            }
                            holder.btn_sSch_leve.setText(lname);
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    context).create();
                            alertDialog.setTitle("AquaticsApp");
                            alertDialog.setIcon(R.drawable.ic_launcher);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            // set the message
                            alertDialog
                                    .setMessage("You have selected to change this student’s level. From "
                                            + temp_level
                                            + " To "
                                            + lname
                                            + " Is this correct?");
                            // set button1 functionality
                            alertDialog.setButton("Yes",
                                    new DialogInterface.OnClickListener() {


                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // close dialog
                                            newschdlevel.remove(position);
                                            newschdlevel.add(position,
                                                    LevelID.get(index + 1));
                                            dialog.cancel();

                                        }
                                    });
                            alertDialog.setButton2("No",
                                    new DialogInterface.OnClickListener() {

                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // TODO Auto-generated method stub
                                            dialog.cancel();
                                            holder.btn_sSch_leve
                                                    .setText(templevel);
                                        }
                                    });
                            // show the alert dialog
                            alertDialog.show();
                        }
                    }
                });

                holder.sch_level_sub.setOnClickListener(new OnClickListener() {

                    @SuppressWarnings("deprecation")

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        int size = 0;
                        String temp_level = holder.btn_sSch_leve.getText()
                                .toString();
                        final String templevel = holder.btn_sSch_leve.getText()
                                .toString();
                        if (temp_level.charAt(0) == '0') {
                            temp_level = "" + temp_level.charAt(1);
                        }
                        final int index = LevelName.indexOf(temp_level);
                        if (size == index) {
                            Toast.makeText(context, "Schedule Level Minimum.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            String lname = LevelName.get(index - 1);
                            if (lname.length() == 1) {
                                lname = "0" + lname;
                            }
                            holder.btn_sSch_leve.setText(lname);
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    context).create();
                            alertDialog.setTitle("AquaticsApp");
                            alertDialog.setIcon(R.drawable.ic_launcher);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);
                            // set the message
                            alertDialog
                                    .setMessage("You have selected to change this student’s level. From "
                                            + temp_level
                                            + " To "
                                            + lname
                                            + " Is this correct?");
                            // set button1 functionality
                            alertDialog.setButton("Yes",
                                    new DialogInterface.OnClickListener() {


                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // close dialog
                                            newschdlevel.remove(position);
                                            newschdlevel.add(position,
                                                    LevelID.get(index - 1));
                                            dialog.cancel();

                                        }
                                    });
                            alertDialog.setButton2("No",
                                    new DialogInterface.OnClickListener() {


                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // TODO Auto-generated method stub
                                            holder.btn_sSch_leve
                                                    .setText(templevel);
                                            dialog.cancel();
                                        }
                                    });
                            // show the alert dialog
                            alertDialog.show();
                        }

                    }
                });

                // /////////level and schedule level
                holder.btn_sSch_leve.setOnClickListener(new OnClickListener() {


                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        holder.listpopupwindow1.show();
                    }
                });

                holder.listpopupwindow = new ListPopupWindow(
                        context.getApplicationContext());
                holder.btn_sLevel.setOnClickListener(new OnClickListener() {


                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        holder.listpopupwindow.show();
                    }
                });
                holder.listpopupwindow.setAdapter(new ArrayAdapter<String>(
                        context.getApplicationContext(),
                        R.layout.edittextpopup, LevelName));
                holder.listpopupwindow.setAnchorView(holder.btn_sLevel);
                holder.listpopupwindow.setHeight(LayoutParams.WRAP_CONTENT);
                holder.listpopupwindow.setModal(true);
                holder.listpopupwindow
                        .setOnItemClickListener(new OnItemClickListener() {
                            @SuppressWarnings("deprecation")

                            public void onItemClick(AdapterView<?> parent,
                                                    View view, final int pos, long id) {
                                // TODO Auto-generated method stub
                                String levelname, schlevelname;
                                final String level, schdlevel;
                                level = holder.btn_sLevel.getText().toString();
                                schdlevel = holder.btn_sSch_leve.getText()
                                        .toString();
                                schlevelname = LevelName.get(pos);
                                levelname = LevelName.get(pos);
                                if (levelname.length() == 1) {
                                    levelname = "0" + levelname;
                                }
                                if (schlevelname.length() == 1) {
                                    schlevelname = "0" + schlevelname;
                                }
                                holder.btn_sLevel.setText(levelname);
                                holder.btn_sSch_leve.setText(schlevelname);
                                holder.listpopupwindow.dismiss();
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        context).create();
                                alertDialog.setTitle("AquaticsApp");
                                alertDialog.setIcon(R.drawable.ic_launcher);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                // set the message
                                alertDialog
                                        .setMessage("You have selected to change this student’s level. From "
                                                + level
                                                + " To "
                                                + levelname
                                                + " Is this correct?");
                                alertDialog.setButton("Yes",
                                        new DialogInterface.OnClickListener() {


                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // close dialog
                                                ischange.remove(position);
                                                ischange.add(position, true);
                                                holder.newlevel = Integer
                                                        .parseInt(LevelID
                                                                .get(pos));
                                                if (ischange.get(position)
                                                        .equals(true)) {
                                                    newslevel.remove(position);
                                                    newschdlevel
                                                            .remove(position);
                                                    newslevel.add(position,
                                                            LevelID.get(pos));
                                                    newschdlevel.add(position,
                                                            LevelID.get(pos));
                                                    if (holder.newlevel == 4) {
                                                        if ((holder.oldlevel == 11)
                                                                || (holder.oldlevel == 12)) {
                                                            showAlert(
                                                                    context,
                                                                    "AquaticsApp",
                                                                    "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.1",
                                                                    "Ok");
                                                        }
                                                    } else if (holder.newlevel == 5) {
                                                        if ((holder.oldlevel == 11)
                                                                || (holder.oldlevel == 12)
                                                                || (holder.oldlevel == 13)) {
                                                            showAlert(
                                                                    context,
                                                                    "AquaticsApp",
                                                                    "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.2",
                                                                    "Ok");
                                                        }
                                                    } else if (holder.newlevel == 6) {
                                                        if (holder.oldlevel == 4) {
                                                            showAlert(
                                                                    context,
                                                                    "AquaticsApp",
                                                                    "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.3",
                                                                    "Ok");
                                                        }
                                                    } else if (holder.newlevel == 7) {
                                                        if ((holder.oldlevel == 4)
                                                                || (holder.oldlevel == 5)) {
                                                            showAlert(
                                                                    context,
                                                                    "AquaticsApp",
                                                                    "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.4",
                                                                    "Ok");
                                                        }
                                                    } else if (holder.newlevel == 8) {
                                                        if (((holder.oldlevel >= 4) && (holder.oldlevel <= 6))
                                                                && (holder.newlevel != holder.oldlevel)
                                                                && (holder.oldlevel != 8)) {
                                                            showAlert(
                                                                    context,
                                                                    "AquaticsApp",
                                                                    "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.5",
                                                                    "Ok");
                                                        }
                                                    } else if (holder.newlevel == 9) {
                                                        if (((holder.oldlevel >= 4) && (holder.oldlevel <= 7))
                                                                && (holder.newlevel != holder.oldlevel)) {
                                                            showAlert(
                                                                    context,
                                                                    "AquaticsApp",
                                                                    "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.6",
                                                                    "Ok");
                                                        }
                                                    } else if (holder.newlevel == 10) {
                                                        if (((holder.oldlevel >= 4) && (holder.oldlevel <= 8))
                                                                && (holder.newlevel != holder.oldlevel)) {
                                                            showAlert(
                                                                    context,
                                                                    "AquaticsApp",
                                                                    "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.7",
                                                                    "Ok");
                                                        }
                                                    } else if (holder.newlevel == 13) {
                                                        if ((holder.oldlevel == 11)) {
                                                            showAlert(
                                                                    context,
                                                                    "AquaticsApp",
                                                                    "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.8",
                                                                    "Ok");
                                                        }
                                                    } else if (holder.newlevel == 14) {
                                                        if ((holder.oldlevel >= 4)
                                                                && (holder.oldlevel <= 9)) {
                                                            showAlert(
                                                                    context,
                                                                    "AquaticsApp",
                                                                    "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.9",
                                                                    "Ok");
                                                        }
                                                        if ((holder.oldlevel >= 11)
                                                                && (holder.oldlevel <= 13)) {
                                                            showAlert(
                                                                    context,
                                                                    "AquaticsApp",
                                                                    "Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.10",
                                                                    "Ok");
                                                        }
                                                    }
                                                }

                                            }
                                        });
                                alertDialog.setButton2("No",
                                        new DialogInterface.OnClickListener() {


                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // TODO Auto-generated method
                                                // stub
                                                dialog.cancel();
                                                holder.btn_sLevel
                                                        .setText(level);
                                                holder.btn_sSch_leve
                                                        .setText(schdlevel);
                                            }
                                        });
                                // show the alert dialog
                                alertDialog.show();
                            }
                        });
                holder.listpopupwindow1.setAdapter(new ArrayAdapter<String>(
                        context.getApplicationContext(),
                        R.layout.edittextpopup, LevelName));
                holder.listpopupwindow1.setAnchorView(holder.btn_sSch_leve);
                holder.listpopupwindow1.setHeight(LayoutParams.WRAP_CONTENT);
                holder.listpopupwindow1.setModal(true);
                holder.listpopupwindow1
                        .setOnItemClickListener(new OnItemClickListener() {

                            @SuppressWarnings("deprecation")

                            public void onItemClick(AdapterView<?> parent,
                                                    View view, final int pos, long id) {
                                // TODO Auto-generated method stub
                                String schlevelname = "";
                                schlevelname = LevelName.get(pos);
                                final String schdlevel = holder.btn_sSch_leve
                                        .getText().toString();
                                if (schlevelname.toString().length() == 1) {
                                    schlevelname = "0" + schlevelname;
                                }
                                holder.btn_sSch_leve.setText(schlevelname);
                                holder.listpopupwindow1.dismiss();
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        context).create();
                                alertDialog.setTitle("AquaticsApp");
                                alertDialog.setIcon(R.drawable.ic_launcher);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                // set the message
                                alertDialog
                                        .setMessage("You have selected to change this student’s level.From "
                                                + schdlevel
                                                + " To "
                                                + schlevelname
                                                + " Is this correct?");
                                alertDialog.setButton("Yes",
                                        new DialogInterface.OnClickListener() {


                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // close dialog
                                                newschdlevel.remove(position);
                                                newschdlevel.add(position,
                                                        LevelID.get(position));
                                                // holder.listpopupwindow1.dismiss();
                                            }
                                        });
                                alertDialog.setButton2("No",
                                        new DialogInterface.OnClickListener() {


                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                // TODO Auto-generated method
                                                // stub
                                                dialog.cancel();
                                                holder.btn_sSch_leve
                                                        .setText(schdlevel);
                                            }
                                        });
                                // show the alert dialog
                                alertDialog.show();
                            }
                        });

                String newstudent1 = data.get(position).getNewUser();
                boolean newstudent;
                if (newstudent1.toString().equalsIgnoreCase("false")) {
                    newstudent = false;
                } else {
                    newstudent = true;
                }
                Log.i("here", "New student = " + newstudent);
                if (newstudent == true) {
                    String next = "<font color='#EE0000'>New Student</font>";
                    holder.tv_sComments.setText(Html.fromHtml(
                            next + data.get(position).getComments().toString(),
                            null, new MyTagHandler()));
                } else {
                    // Comment//
                    // Using MyTagHandler class for generating list//
                    holder.tv_sComments.setText(Html.fromHtml(data
                                    .get(position).getComments().toString(), null,
                            new MyTagHandler()));
                }
                // Birthday//
                String birthdate = data.get(position).getBirthDay();
                if (birthdate.equalsIgnoreCase("false")) {
                    holder.btn_sBirthday.setVisibility(View.INVISIBLE);
                } else {
                    holder.btn_sBirthday.setVisibility(View.VISIBLE);
                    holder.btn_sBirthday.startAnimation(animBlink);
                }

                // Swim Comp//
                if (Integer.parseInt(data.get(position).getExistSwimComp()
                        .toString()) > 0) {
                    holder.btn_sSwim_comp.setVisibility(View.VISIBLE);
                    holder.btn_sSwim_comp.setEnabled(false);
                } else {
                    String swimcomp = data.get(position).getSwimComp();
                    Log.i("Att adpter", swimcomp + "\n"
                            + data.get(position).getIsShowSmCampStatus());
                    if (data.get(position).getIsShowSmCampStatus()
                            .equalsIgnoreCase("true")
                            && swimcomp.equalsIgnoreCase("true")) {
                        holder.btn_sSwim_comp.setVisibility(View.VISIBLE);
                        holder.btn_sSwim_comp.setEnabled(true);
                        holder.btn_sSwim_comp.startAnimation(animBlink);
                    } else {
                        holder.btn_sSwim_comp.setVisibility(View.INVISIBLE);
                    }
                    holder.btn_sSwim_comp
                            .setOnClickListener(new OnClickListener() {

                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    final Dialog d = new Dialog(context);
                                    d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    d.setContentView(R.layout.cancelswimcomp_dialog);
                                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                    lp.copyFrom(d.getWindow().getAttributes());
                                    lp.width = LayoutParams.MATCH_PARENT;
                                    lp.height = LayoutParams.WRAP_CONTENT;
                                    d.getWindow().setAttributes(lp);
                                    d.setCanceledOnTouchOutside(true);
                                    final EditText et_reason = (EditText) d
                                            .findViewById(R.id.et_csc_reason);
                                    final Button reason1 = (Button) d
                                            .findViewById(R.id.btn_csc_1);
                                    final Button reason2 = (Button) d
                                            .findViewById(R.id.btn_csc_2);
                                    final Button reason3 = (Button) d
                                            .findViewById(R.id.btn_csc_3);
                                    final Button reason4 = (Button) d
                                            .findViewById(R.id.btn_csc_4);
                                    final Button reason5 = (Button) d
                                            .findViewById(R.id.btn_csc_5);
                                    final Button reason6 = (Button) d
                                            .findViewById(R.id.btn_csc_6);
                                    final Button reason7 = (Button) d
                                            .findViewById(R.id.btn_csc_7);
                                    final Button send_request = (Button) d
                                            .findViewById(R.id.btn_csc_add_other);
                                    et_reason.setVisibility(View.GONE);
                                    send_request.setVisibility(View.GONE);
                                    reason7.setOnClickListener(new OnClickListener() {


                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            et_reason
                                                    .setVisibility(View.VISIBLE);
                                            send_request
                                                    .setVisibility(View.VISIBLE);
                                        }
                                    });

                                    reason1.setOnClickListener(new OnClickListener() {

                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            reason = "Not Available : Out of town";
                                            SwimCompId = "0";
                                            StudentId = data.get(position)
                                                    .getStudentID();
                                            d.dismiss();
                                            new SubmitReason().execute();
                                        }
                                    });
                                    reason2.setOnClickListener(new OnClickListener() {


                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            reason = "Not Available : Another event planned";
                                            SwimCompId = "0";
                                            StudentId = data.get(position)
                                                    .getStudentID();
                                            d.dismiss();
                                            new SubmitReason().execute();
                                        }
                                    });
                                    reason3.setOnClickListener(new OnClickListener() {

                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            reason = "Parent feels child is not ready";
                                            SwimCompId = "0";
                                            StudentId = data.get(position)
                                                    .getStudentID();
                                            d.dismiss();
                                            new SubmitReason().execute();
                                        }
                                    });
                                    reason4.setOnClickListener(new OnClickListener() {

                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            reason = "Parent is thinking about it";
                                            SwimCompId = "0";
                                            StudentId = data.get(position)
                                                    .getStudentID();
                                            d.dismiss();
                                            new SubmitReason().execute();
                                        }
                                    });
                                    reason5.setOnClickListener(new OnClickListener() {


                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            reason = "Parent said yes";
                                            SwimCompId = "0";
                                            StudentId = data.get(position)
                                                    .getStudentID();
                                            d.dismiss();
                                            new SubmitReason().execute();
                                        }
                                    });
                                    reason6.setOnClickListener(new OnClickListener() {


                                        public void onClick(View v) {
                                            // TODO Auto-generated method stub
                                            reason = "Forgot to mention to parent";
                                            SwimCompId = "0";
                                            StudentId = data.get(position)
                                                    .getStudentID();
                                            d.dismiss();
                                            new SubmitReason().execute();
                                        }
                                    });
                                    send_request
                                            .setOnClickListener(new OnClickListener() {


                                                public void onClick(View v) {
                                                    // TODO Auto-generated
                                                    // method stub
                                                    reason = "Other";
                                                    comment = et_reason
                                                            .getText()
                                                            .toString();
                                                    SwimCompId = "0";
                                                    StudentId = data.get(
                                                            position)
                                                            .getStudentID();
                                                    if (comment.toString()
                                                            .equalsIgnoreCase(
                                                                    "")) {
                                                        Toast.makeText(
                                                                context,
                                                                "Please enter reason",
                                                                Toast.LENGTH_LONG).show();
                                                    } else {
                                                        d.dismiss();
                                                        new SubmitReason()
                                                                .execute();
                                                    }
                                                }
                                            });

                                    d.show();
                                }
                            });
                }
            }
            // / Skill list///
            final ArrayList<String> finalABBR = (ArrayList<String>) data.get(
                    position).getAbbr();
            final ArrayList<String> finalPreReqId = (ArrayList<String>) data
                    .get(position).getPreReqID();
            final ArrayList<String> finalPreReqChecked = (ArrayList<String>) data
                    .get(position).getPreReqChecked();
            int offset_in_column = 0, table_size = Integer.parseInt(data.get(
                    position).getSkillsCount());
            if (holder.tl_skills.getVisibility() == View.VISIBLE) {
                if (table_size > 12) {
                    table_size = 12;
                    holder.btn_more.setVisibility(View.VISIBLE);
                    String more = "More Skill Sets...";
                    SpannableString ss = new SpannableString(more);
                    ss.setSpan(new RelativeSizeSpan(2f), 15, more.length(), 0);
                    holder.btn_more.setText(ss);
                    final ArrayList<String> abbr = new ArrayList<String>();
                    final ArrayList<String> prereqID = new ArrayList<String>();
                    final ArrayList<String> prereqCheck = new ArrayList<String>();
                    holder.btn_more.setOnClickListener(new OnClickListener() {


                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            abbr.clear();
                            prereqCheck.clear();
                            prereqID.clear();
                            for (int i = 12; i < finalABBR.size(); i++) {
                                abbr.add(finalABBR.get(i));
                                prereqID.add(finalPreReqId.get(i));
                                prereqCheck.add(finalPreReqChecked.get(i));
                            }

                            Log.i("Abbr", "Abbr = " + abbr);
                            holder.dialog = new Dialog(context);
                            holder.dialog
                                    .requestWindowFeature(Window.FEATURE_NO_TITLE);
                            // holder.dialog.setTitle("More Skills:");

                            holder.dialog.getWindow().setBackgroundDrawable(
                                    new ColorDrawable(Color.BLACK));
                            // holder.dialog.getWindow().setBackgroundDrawable(
                            // new
                            // ColorDrawable(android.graphics.Color.TRANSPARENT));
                            holder.dialog.setContentView(R.layout.moreabbr);
                            holder.dialog.setCanceledOnTouchOutside(false);
                            holder.dialog.setCancelable(true);
                            holder.dialog.getWindow().getAttributes().width = LayoutParams.WRAP_CONTENT;
                            holder.dialog.getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
                            final ListView lv_final_to_cc = (ListView) holder.dialog
                                    .findViewById(R.id.lv_final_to_cc_dialog);
                            Button save = (Button) holder.dialog
                                    .findViewById(R.id.btn_final_to_cc_save);
                            Button cancel = (Button) holder.dialog
                                    .findViewById(R.id.btn_final_to_cc_cancel);
                            lv_final_to_cc
                                    .setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    context,
                                    android.R.layout.simple_list_item_multiple_choice,
                                    abbr);
                            adapter.hasStableIds();
                            lv_final_to_cc.setAdapter(adapter);
                            for (int i = 0; i < abbr.size(); i++) {
                                if (prereqCheck.get(i).equalsIgnoreCase("true")) {
                                    lv_final_to_cc.setItemChecked(i, true);
                                }
                            }
                            save.setOnClickListener(new OnClickListener() {


                                public void onClick(View v) {
                                    // TODO Auto-generated method
                                    // stub
                                    // String name="";
                                    SparseBooleanArray checkedO = lv_final_to_cc
                                            .getCheckedItemPositions();
                                    for (int i = 0; i < checkedO.size(); i++) {
                                        // Item position in adapter
                                        int pos = checkedO.keyAt(i);
                                        // Add sport if it is
                                        // checked i.e.) == TRUE!
                                        if (checkedO.valueAt(i)) {
                                            newprereqid.add(data.get(position)
                                                    .getSScheduleID()
                                                    + "*"
                                                    + prereqID.get(pos) + "*");
                                            finalPreReqChecked.remove(pos + 12);
                                            finalPreReqChecked.add(pos + 12,
                                                    "" + true);
                                        }
                                    }
                                    // if(name.charAt(0)==','){
                                    // name = name.substring(1);
                                    // }
                                    Log.i("check id", "Final id = "
                                            + newprereqid);
                                    holder.dialog.cancel();

                                }
                            });
                            cancel.setOnClickListener(new OnClickListener() {

                                public void onClick(View v) {
                                    // TODO Auto-generated method
                                    // stub
                                    holder.dialog.cancel();
                                }
                            });
                            holder.dialog.show();
                        }
                    });
                } else {
                    holder.btn_more.setVisibility(View.GONE);
                }
            }
            TableRow tr = null;
            int offset_in_table = 0;
            for (offset_in_table = 0; offset_in_table < table_size; offset_in_table++) {

                if (offset_in_column == 0) {
                    tr = new TableRow(context.getApplicationContext());
                    TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
                            TableLayout.LayoutParams.FILL_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT);
                    tr.setLayoutParams(tableRowParams);
                }

                final CheckBox check = new CheckBox(
                        context.getApplicationContext());
                check.setText(finalABBR.get(offset_in_table));
                check.setId(Integer.parseInt(finalPreReqId.get(offset_in_table)));
                check.setButtonDrawable(context.getResources().getDrawable(
                        R.drawable.checkbox_selection));
                check.setTextColor(context.getResources().getColor(
                        R.color.texts1));
                check.setPadding(5, 5, 5, 5);
                check.setSingleLine(true);
                check.setChecked(Boolean.valueOf(finalPreReqChecked
                        .get(offset_in_table)));
                // check.setLayoutParams(new
                // TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
                // LayoutParams.WRAP_CONTENT, 1));

                tr.addView(check);

                offset_in_column++;
                if (offset_in_column == 4) {
                    holder.tl_skills.addView(tr);
                    offset_in_column = 0;
                }

                check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        // TODO Auto-generated method stub
                        if (isChecked) {
                            newprereqid.add(data.get(position).getSScheduleID()
                                    + "*" + check.getId() + "*");
                        } else {
                            newprereqid.remove(data.get(position)
                                    .getSScheduleID()
                                    + "*"
                                    + check.getId()
                                    + "*");
                        }
                    }
                });
            }
            if (offset_in_column != 0) {
                holder.tl_skills.addView(tr);
            }

            // // Photo / Camera ///
            holder.btn_sCamera.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    String firname = data.get(position).getSFirstName();
                    if (firname.contains("_")) {
                        firname = firname.replaceAll("_", "");
                    }

                    ((ViewCurrentScheduleInstructorActivity) context).filename = "Water_"
                            + firname + "_" +
                            data.get(position).getStudentID();
                    //					((ViewCurrentScheduleInstructorActivity)context).filename = "TempName";

                    System.out.println("Context : " + context);
                    ((ViewCurrentScheduleInstructorActivity) context).OpenCamera();

                }
            });

            // /New comment///
            holder.btn_sNote.setOnClickListener(new OnClickListener() {


                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(context, "Click", 1).show();

                    // WW_StaticClass.Studentid = data.get(position)
                    // .getStudentID();
                    // ((Activity) context).finish();
                    ViewCurrentScheduleInstructorActivity.t.interrupt();
                    Intent it = new Intent(v.getContext(), NewModifyComments.class);
                    it.putExtra("studentid", data.get(position).getStudentID());
                    it.putExtra("userid", data.get(position).getInstructorID());
                    it.putExtra("FROM", "CURRENT");
                    // it.putExtra("FROM", "CURRENT");
					/*
					 * it.putExtra("yes_no_date", data.get(position)
					 * .getYes_no_date());
					 */
                    v.getContext().startActivity(it);

                }
            });

//			ViewCurrentScheduleFragment.btn_send_review
//			.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					new SubmitReview().execute();
//				}
//			});
            ViewCurrentScheduleFragment.btn_send_att
                    .setOnClickListener(new OnClickListener() {


                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (checked.size() <= 0 && checked_att.size() <= 0) {
                                Toast.makeText(context, "Please select at least one student to send Review Or Attendance", Toast.LENGTH_SHORT).show();

                            } else {
                                if (checked.size() > 0) {
                                    new SubmitReview().execute();
                                }

                                if (checked_att.size() > 0) {
                                    temp_view = v;

                                    if (Utility.isNetworkConnected(context) == true) {
                                        straarylist = new ArrayList<String>();
                                        levelchanged = new ArrayList<Character>();
                                        prereqid = new ArrayList<String>();
                                        isched = new ArrayList<String>();

                                        comments = new ArrayList<String>();
                                        wu_sscheduleid = new ArrayList<String>();
                                        wu_studentid = new ArrayList<String>();
                                        wu_orderdetailid = new ArrayList<String>();
                                        wu_lessontypeid = new ArrayList<String>();
                                        wu_sttimehr = new ArrayList<String>();
                                        wu_sttimemin = new ArrayList<String>();
                                        wu_scheduledate = new ArrayList<String>();
                                        // ddlW,ddlB,ddlR,wu_siteid,
                                        wu_slevel = new ArrayList<String>();
                                        ddlSchedLevel = new ArrayList<String>();
                                        wu_schedlevel = new ArrayList<String>();
                                        oldlev = new ArrayList<String>();
                                        lev = new ArrayList<String>();
                                        chkschedselect = new ArrayList<String>();
                                        newddlw = new ArrayList<String>();
                                        newddlb = new ArrayList<String>();
                                        newddlr = new ArrayList<String>();
                                        siteid = data.get(0).getSiteID();
                                        int newpos;
                                        for (int i = 0; i < checked_att.size(); i++) {
                                            newpos = i;
                                            if (ischange.size() > 0) {
                                                if (ischange.get(newpos).equals(true)) {
                                                    levelchanged.add('Y');
                                                } else {
                                                    levelchanged.add('N');
                                                }
                                            } else {
                                                levelchanged.add('N');
                                            }
                                            newddlw.add(ddlW.get(newpos));
                                            newddlb.add(ddlB.get(newpos));
                                            newddlr.add(ddlR.get(newpos));
                                            oldlev.add(data.get(newpos).getSLevel());
                                            lev.add(newslevel.get(newpos));
                                            ddlSchedLevel.add(data.get(newpos)
                                                    .getScheLevel());
                                            wu_schedlevel.add(newschdlevel.get(newpos));
                                            chkschedselect.add("true");
                                            isched.add(data.get(newpos)
                                                    .getIScheduleID());

                                            comments.add(data.get(newpos)
                                                    .getWu_comments());
                                            wu_sscheduleid.add(data.get(newpos)
                                                    .getSScheduleID());
                                            wu_studentid.add(data.get(newpos)
                                                    .getStudentID());
                                            wu_orderdetailid.add(data.get(newpos)
                                                    .getOrderDetailID());
                                            wu_lessontypeid.add(data.get(newpos)
                                                    .getLessontypeid());
                                            wu_sttimehr.add(data.get(newpos)
                                                    .getTemp_StTimeHour());
                                            wu_sttimemin.add(data.get(newpos)
                                                    .getTemp_StTimeMin());
                                            wu_scheduledate.add(data.get(newpos)
                                                    .getMainScheduleDate());
                                            wu_slevel.add(data.get(newpos).getSLevel());
                                            String match = data.get(newpos)
                                                    .getSScheduleID();
                                            Log.i("Here", "" + newprereqid);
                                            for (int q = 0; q < newprereqid.size(); q++) {
                                                if (newprereqid.get(q).contains(match)) {
                                                    prereqid.add(newprereqid.get(q));
                                                } else {
                                                }
                                            }
                                        }
                                        new Insert_Attandance().execute();
                                    } else {
                                        onDetectNetworkState().show();
                                    }
                                }
                            }
                        }


                    });
        }

        return convertView;
    }

    String username, password;

    private class Insert_Attandance extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            for (int i = 0; i < prereqid.size(); i++) {
                if (FinalPreReqId.contains(prereqid.get(i))) {

                } else {
                    FinalPreReqId.add(prereqid.get(i));
                }
            }
            Log.i("Final Prereq Id", "Final prereq id = " + FinalPreReqId);
            straarylist.add("levelschanged=" + levelchanged
                    + ";chkschedselect=" + chkschedselect + ";lev=" + lev
                    + ";oldatt=" + ViewCurrentScheduleFragment.oldatt + ";att="
                    + ViewCurrentScheduleFragment.newatt + ";isched=" + isched
                    + ";comments=" + comments + ";wu_sscheduleid="
                    + wu_sscheduleid + ";wu_studentid=" + wu_studentid
                    + ";wu_orderdetailid=" + wu_orderdetailid
                    + ";wu_lessontypeid=" + wu_lessontypeid + ";wu_sttimehr="
                    + wu_sttimehr + ";wu_sttimemin=" + wu_sttimemin
                    + ";wu_scheduledate=" + wu_scheduledate + ";ddlW="
                    + newddlw + ";ddlB=" + newddlb + ";ddlR=" + newddlr
                    + ";wu_slevel=" + oldlev + ";ddlSchedLevel="
                    + ddlSchedLevel + ";wu_schedlevel=" + wu_schedlevel);

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODOAuto-generated method stub
            String str = straarylist.toString();
            str = str.replaceFirst("\\[", "");
            str = str.substring(0, str.lastIndexOf("]"));
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.Insert_Attandance_Method);
            request.addProperty("token", WW_StaticClass.UserToken); // 1
            request.addProperty("wu_InstructorID", Instructor_ID); // 2
            request.addProperty("wu_siteid", siteid.toString()); // 3
            request.addProperty("_prereq", FinalPreReqId.toString()); // 4
            request.addProperty("straarylist", str);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            // /////////////new code//////////////////

            try {
                File myFile = new File(
                        Environment.getExternalStorageDirectory()
                                + "/att_request.txt");
                myFile.createNewFile();
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(request.toString());
                myOutWriter.close();
                fOut.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // /////////////new code/////////////////////
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.Insert_Attandance_Action,
                        envelope);
                // Calling // Web // service
                SoapObject response = (SoapObject) envelope.bodyIn;
                Log.i("Attendance apdater", "Result : " + response.toString());
                // /////////////new code//////////////////

                try {
                    File myFile = new File(
                            Environment.getExternalStorageDirectory()
                                    + "/att_response.txt");
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(
                            fOut);
                    myOutWriter.append(response.toString());
                    myOutWriter.close();
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // /////////////new code/////////////////////
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                SoapObject mSoapObject3 = (SoapObject) mSoapObject2
                        .getProperty(0);
                String code = mSoapObject3.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    attendance_response = true;
                    Object mSoapObject4 = mSoapObject2.getProperty(1);

                    String resp = mSoapObject4.toString();
                    JSONObject jobj = new JSONObject(resp);
                    Msg_Status = new ArrayList<String>();
                    Msg_Str = new ArrayList<String>();
                    JSONArray mArray = jobj.getJSONArray("InsrtAttnDtl");
                    for (int i = 0; i < mArray.length(); i++) {
                        JSONObject mJsonObject = mArray.getJSONObject(i);
                        Msg_Status.add(mJsonObject.getString("Msg_Status"));
                        Msg_Str.add(mJsonObject.getString("Msg_Str"));
                    }
                } else {
                    attendance_response = false;
                }
            } catch (JSONException e) {
                server_response = true;
                e.printStackTrace();
            } catch (NullPointerException e) {
                server_response = true;
                e.printStackTrace();
            } catch (Exception e) {
                server_response = true;
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (server_response) {
                server_response = false;
                onDetectNetworkState().show();
            } else {
                if (attendance_response) {
                    checked_att.clear();
                    attendance_response = false;
                    String msg = "";
                    for (int i = 0; i < Msg_Status.size(); i++) {
                        if (Msg_Status.get(i).equalsIgnoreCase("Failure")
                                && Msg_Str.get(i).equalsIgnoreCase("")) {
                            msg = msg + "\n" + Msg_Str.get(i);
                        } else {
                            msg = msg + "\n" + Msg_Str.get(i);
                        }
                    }
                    //					SingleOptionAlertWithoutTitle.ShowAlertDialog(context,
                    //							"AquaticsApp", msg, "Ok");
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("AquaticsApp");
                    alertDialog
                            .setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(msg);
                    alertDialog.setButton("Ok",
                            new DialogInterface.OnClickListener() {


                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    ((ViewCurrentScheduleInstructorActivity) context).displayView_Main(0);

                                }
                            });
                    // show the alert dialog
                    alertDialog.show();
                } else {
                    SingleOptionAlertWithoutTitle
                            .ShowAlertDialog(
                                    context,
                                    "AquaticsApp",
                                    "Some internal error. Please try again after sometime",
                                    "Ok");
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

    @SuppressWarnings("deprecation")
    public void showAlert(Context context, String Heading, String message,
                          String buttonText) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // hide title bar
        // alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setTitle(Heading);
        alertDialog
                .setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        // set the message
        alertDialog.setMessage(message);
        // set button1 functionality
        alertDialog.setButton(buttonText,
                new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog

                        dialog.cancel();

                    }
                });
        // show the alert dialog
        alertDialog.show();
    }

    String checked_values = "";

    private class SubmitReview extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            for (String s : checked) {
                checked_values += s + ",";
            }

            if (checked_values.endsWith(",")) {
                checked_values = checked_values.substring(0, checked_values.length() - 1);
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_SendAttForReview);
            request.addProperty("token", WW_StaticClass.UserToken); // 1
            request.addProperty("userid", WW_StaticClass.InstructorID); // 2
            request.addProperty("username", WW_StaticClass.UserName); // 3
            request.addProperty("selection", checked_values); // 4
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(
                        SOAP_CONSTANTS.SOAP_Action_Insert_SendAttForReview, envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.bodyIn;
                Log.i("Attendance apdater", "Result : " + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i("Attendance Adapter", "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                Log.i("Attendance Adapter", "mSoapObject2=" + mSoapObject2);
                SoapObject mSoapObject3 = (SoapObject) mSoapObject2
                        .getProperty(0);
                String code = mSoapObject3.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    cancel_response = true;
                } else {
                    cancel_response = false;
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();

            } catch (SocketException e) {
                // TODO: handle exception
                e.printStackTrace();

            } catch (NullPointerException e) {
                server_response = true;
                e.printStackTrace();
            } catch (Exception e) {
                server_response = true;
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (server_response) {
                server_response = false;
                onDetectNetworkState().show();
            } else {
                if (cancel_response) {
                    cancel_response = false;
                    checked.clear();
                    checked_att.clear();
                    String msg = "Added successfully..!!";
                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("AquaticsApp");
                    alertDialog
                            .setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.setMessage(msg);
                    alertDialog.setButton("Ok",
                            new DialogInterface.OnClickListener() {


                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    ((ViewCurrentScheduleInstructorActivity) context).displayView_Main(0);
                                }
                            });
                    // show the alert dialog
                    alertDialog.show();

                    Toast.makeText(context, "Add successfully..!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Not Add successfully..!!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    }

    private class SubmitReason extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_Insert_SwimCompCancellation);
            request.addProperty("token", WW_StaticClass.UserToken); // 1
            request.addProperty("SwimCompId", SwimCompId); // 2
            request.addProperty("StudentId", StudentId); // 3
            request.addProperty("Reason", reason); // 4
            request.addProperty("Comments", comment); // 5
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(
                        SOAP_CONSTANTS.SOAP_Action_Insert_SwimCompCancellation, envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.bodyIn;
                Log.i("Attendance apdater", "Result : " + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i("Attendance Adapter", "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                Log.i("Attendance Adapter", "mSoapObject2=" + mSoapObject2);
                SoapObject mSoapObject3 = (SoapObject) mSoapObject2
                        .getProperty(0);
                String code = mSoapObject3.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equals("000")) {
                    cancel_response = true;
                } else {
                    cancel_response = false;
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();

            } catch (SocketException e) {
                // TODO: handle exception
                e.printStackTrace();

            } catch (NullPointerException e) {
                server_response = true;
                e.printStackTrace();
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
                server_response = false;
                onDetectNetworkState().show();
            } else {
                if (cancel_response) {
                    cancel_response = false;
                    Toast.makeText(context, "Add successfully..!!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Not Add successfully..!!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {


                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                ((Activity) context).finish();
                            }
                        })
                .setPositiveButton("Οk", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        context.startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    //	public class LoginAsyn extends AsyncTask<Void, Void, Void> {
    //
    //		@Override
    //		protected void onPreExecute() {
    //			// TODO Auto-generated method stub
    //			super.onPreExecute();
    //
    //		}
    //
    //		@Override
    //		protected Void doInBackground(Void... params) {
    //			// TODO Auto-generated method stub
    //			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
    //					AppConfig.Login_Method);
    //			// Adding Username and Password for Login Invok
    //			request.addProperty("username", username);
    //			request.addProperty("password", password);
    //			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
    //					SoapEnvelope.VER11); // Make an Envelop for sending as whole
    //			envelope.dotNet = true;
    //			envelope.setOutputSoapObject(request);
    //			Log.i("Request", "Request = " + request);
    //			HttpTransportSE androidHttpTransport = new HttpTransportSE(
    //					AppConfig.SOAP_ADDRESS);
    //			try {
    //				androidHttpTransport.call(AppConfig.Login_Action, envelope); // Calling
    //																				// Web
    //																				// service
    //				SoapObject response = (SoapObject) envelope.getResponse();
    //				// Log.i("here","Result : " + response.toString());
    //				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
    //				// Log.i(Tag, "mSoapObject1="+mSoapObject1);
    //				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
    //						.getProperty(0);
    //				// Log.i(Tag, "mSoapObject2="+mSoapObject2);
    //				String code = mSoapObject2.getPropertyAsString(0).toString();
    //				Log.i("Code", code);
    //				// response.toString();
    //				if (code.equals("000")) {
    //					login_status = true;
    //					Object mSoapObject3 = mSoapObject1.getProperty(1);
    //					Log.i("Adapter Login", "mSoapObject3=" + mSoapObject3);
    //					String resp = mSoapObject3.toString();
    //					JSONObject jo = new JSONObject(resp);
    //					UserToken = jo.getString("UserToken");
    //					UserLevel = jo.getString("UserLevel");
    //				} else {
    //					login_status = false;
    //				}
    //			} catch (SocketException e) {
    //				connectionout = true;
    //				e.printStackTrace();
    //			} catch (SocketTimeoutException e) {
    //				connectionout = true;
    //				e.printStackTrace();
    //			} catch (JSONException e) {
    //				server_response = true;
    //				e.printStackTrace();
    //			} catch (Exception e) {
    //				server_response = true;
    //				e.printStackTrace();
    //			}
    //			return null;
    //		}
    //
    //		@Override
    //		protected void onPostExecute(Void result) {
    //			// TODO Auto-generated method stub
    //			super.onPostExecute(result);
    //			if (server_response) {
    //				onDetectNetworkState().show();
    //				server_response = false;
    //			} else {
    //				if (login_status) {
    //					login_status = false;
    //					// new Insert_Attandance().execute();
    //				} else {
    //					SingleOptionAlertWithoutTitle.ShowAlertDialog(
    //							temp_view.getContext(), "AquaticsApp",
    //							"Please Enter Valid Username and Password", "OK");
    //				}
    //			}
    //		}
    //	}
}
