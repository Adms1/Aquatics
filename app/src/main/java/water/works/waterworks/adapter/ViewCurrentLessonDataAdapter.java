package water.works.waterworks.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import water.works.waterworks.ModifyComments;
import water.works.waterworks.MyTagHandler;
import water.works.waterworks.R;
import water.works.waterworks.ViewCurrentLessonActivity;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ViewCurrentLessonDataAdapter extends BaseAdapter implements AnimationListener {
    public ViewCurrentLessonDataAdapter(ArrayList<String> siteID,
                                        ArrayList<String> sLevel, ArrayList<String> LevelID,
                                        ArrayList<String> LevelName, ArrayList<String> wu_W,
                                        ArrayList<String> scheLevel, ArrayList<String> swimComp,
                                        ArrayList<String> lessonName, ArrayList<String> lessontypeid,
                                        ArrayList<String> iScheduleID, ArrayList<String> sAge,
                                        ArrayList<String> parentFirstName,
                                        ArrayList<String> parentLastName, ArrayList<String> birthDay,
                                        ArrayList<String> comments, ArrayList<String> wu_r,
                                        ArrayList<String> sLastName, ArrayList<String> sFirstName,
                                        ArrayList<String> studentID, ArrayList<String> showWBR,
                                        ArrayList<String> wu_b, ArrayList<String> sScheduleID,
                                        ArrayList<String> orderDetailID, ArrayList<String> paidClasses,
                                        ArrayList<String> SkillsCount, ArrayList<ArrayList<String>> Abbr, ArrayList<ArrayList<String>> PreReqChecked,
                                        ArrayList<ArrayList<String>> PreReqID, ArrayList<String> ISAAlert, ArrayList<String> ClsLvl,
                                        ArrayList<String> LvlAdvAvail, String yes_no_date, Button btn_footer_send_attendance,
                                        ArrayList<String> stTimeHour, ArrayList<String> stTimeMin,
                                        ArrayList<String> mainScheduleDate, ArrayList<String> wu_comments, Context context) {
        super();
        SiteID = siteID;
        SLevel = sLevel;
        this.LevelID = LevelID;
        this.LevelName = LevelName;
        this.wu_W = wu_W;
        ScheLevel = scheLevel;
        SwimComp = swimComp;
        LessonName = lessonName;
        this.lessontypeid = lessontypeid;
        IScheduleID = iScheduleID;
        SAge = sAge;
        ParentFirstName = parentFirstName;
        ParentLastName = parentLastName;
        BirthDay = birthDay;
        Comments = comments;
        this.wu_r = wu_r;
        SLastName = sLastName;
        SFirstName = sFirstName;
        StudentID = studentID;
        ShowWBR = showWBR;
        this.wu_b = wu_b;
        SScheduleID = sScheduleID;
        OrderDetailID = orderDetailID;
        PaidClasses = paidClasses;
        this.SkillsCount = SkillsCount;
        this.Abbr = Abbr;
        this.PreReqChecked = PreReqChecked;
        this.PreReqID = PreReqID;
        this.ISAAlert = ISAAlert;
        this.ClsLvl = ClsLvl;
        this.LvlAdvAvail = LvlAdvAvail;
        this.yes_no_date = yes_no_date;
        this.btn_footer_send_attendance = btn_footer_send_attendance;
        this.stTimeHour = stTimeHour;
        this.stTimeMin = stTimeMin;
        this.mainScheduleDate = mainScheduleDate;
        this.wu_comments = wu_comments;
        this.context = context;
    }

    Button btn_footer_send_attendance;
    ArrayList<String> LevelID;
    ArrayList<String> LevelName;
    ArrayList<String> SiteID;
    ArrayList<String> SLevel;
    ArrayList<String> wu_W;
    ArrayList<String> ScheLevel;

    ArrayList<String> SwimComp;
    ArrayList<String> LessonName;
    ArrayList<String> lessontypeid;
    ArrayList<String> IScheduleID;
    ArrayList<String> SAge;
    ArrayList<String> ParentFirstName;
    ArrayList<String> ParentLastName;
    ArrayList<String> BirthDay;
    ArrayList<String> Comments;
    ArrayList<String> wu_r;
    ArrayList<String> SLastName;
    ArrayList<String> SFirstName;
    ArrayList<String> StudentID;
    ArrayList<String> ShowWBR;
    ArrayList<String> wu_b;
    ArrayList<String> SScheduleID;
    ArrayList<String> OrderDetailID;
    ArrayList<String> PaidClasses;
    /// for skills list
    ArrayList<String> SkillsCount;
    ArrayList<ArrayList<String>> Abbr;
    ArrayList<ArrayList<String>> PreReqChecked;
    ArrayList<ArrayList<String>> PreReqID;
    ArrayList<String> ISAAlert;
    ArrayList<String> ClsLvl;
    ArrayList<String> LvlAdvAvail;
    String yes_no_date;
    Context context;
    ListView listView;
    CheckBox selectedcb;
    Animation animBlink;
    ArrayList<String> stTimeHour;
    ArrayList<String> stTimeMin;
    ArrayList<String> mainScheduleDate;
    ArrayList<String> wu_comments;

    ArrayList<String> checkedpos = new ArrayList<String>();
    private static String tag = "Adapter";


    public int getCount() {
        // TODO Auto-generated method stub
        return StudentID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
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
        TextView tv_sName, tv_sAge, tv_sPaid_cls, tv_sComments;
        ImageView btn_sIsa;
        Button btn_sCls_lvl, btn_sSwim_comp;
        ImageView iv_sLate;
        Button et_sLevel, et_sSch_leve;
        CheckBox selection;
        Button btn_wu_w, btn_wu_r, btn_wu_b;
        ToggleButton sw_precent;
        TableLayout tl_skills;
        ListPopupWindow listpopupwindow, listpopupwindow1, lpw_wu_w, lpw_wu_b, lpw_wu_r;
        boolean levelChanged = false;
        int oldlevel, newlevel;
        ImageButton btn_sCamera, btn_sNote, btn_sBirthday;
    }

    private int[] colors = new int[]{Color.parseColor("#EEEEEE"), Color.parseColor("#FFFFFF")};

    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        try {
            if (convertView == null) {
                holder = new ViewHolder();


                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.viewcurrentschedulerow, null);
                int colorpos = position % colors.length;
                convertView.setBackgroundColor(colors[colorpos]);
                holder.selection = (CheckBox) convertView.findViewById(R.id.selection);
                holder.btn_sIsa = (ImageView) convertView.findViewById(R.id.btn_isa_alert);
                holder.tv_sName = (TextView) convertView.findViewById(R.id.tv_cl_studentname_row);
                holder.btn_sCamera = (ImageButton) convertView.findViewById(R.id.btn_camera);
                holder.tv_sAge = (TextView) convertView.findViewById(R.id.tv_cl_age_row);
//             holder.et_sLevel = (Button)convertView.findViewById(R.id.et_cl_slevel);
//             holder.et_sSch_leve = (Button)convertView.findViewById(R.id.et_cl_sched_level);
                holder.sw_precent = (ToggleButton) convertView.findViewById(R.id.yes_no);
                holder.btn_sCls_lvl = (Button) convertView.findViewById(R.id.btn_cl_cls_lvl_row);
                holder.tv_sPaid_cls = (TextView) convertView.findViewById(R.id.tv_cl_paid_cls_row);
//             holder.btn_wu_w = (Button)convertView.findViewById(R.id.chb_cl_wbr_w);
//             holder.btn_wu_b = (Button)convertView.findViewById(R.id.chb_cl_wbr_b);
//             holder.btn_wu_r = (Button)convertView.findViewById(R.id.chb_cl_wbr_r);
                holder.tv_sComments = (TextView) convertView.findViewById(R.id.tv_cl_comment);
                holder.btn_sNote = (ImageButton) convertView.findViewById(R.id.btn_cl_note);
                holder.btn_sSwim_comp = (Button) convertView.findViewById(R.id.btn_swim_comp);
                holder.iv_sLate = (ImageView) convertView.findViewById(R.id.img_late);
                holder.btn_sBirthday = (ImageButton) convertView.findViewById(R.id.btn_cake);
                holder.tl_skills = (TableLayout) convertView.findViewById(R.id.table_cl_data);
                holder.listpopupwindow = new ListPopupWindow(context.getApplicationContext());
                holder.listpopupwindow1 = new ListPopupWindow(context.getApplicationContext());
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.sw_precent.setChecked(false);
            animBlink = AnimationUtils.loadAnimation(context.getApplicationContext(),
                    R.anim.blink);
            animBlink.setAnimationListener(this);

            String studentname = SFirstName.get(position) + " " + SLastName.get(position);
            String parentname = "(" + ParentFirstName.get(position) + " " + ParentLastName.get(position) + ")";
            holder.tv_sName.setText(studentname + "\n" + Html.fromHtml(parentname));
            holder.tv_sAge.setText(SAge.get(position));

            if (ISAAlert.get(position).equalsIgnoreCase("true")) {
                holder.btn_sIsa.setVisibility(View.VISIBLE);
                holder.btn_sIsa.startAnimation(animBlink);
            } else {
                holder.btn_sIsa.setVisibility(View.INVISIBLE);
            }

            /// For cls lvl /////////
            String cls_lvl = ClsLvl.get(position);
            holder.btn_sCls_lvl.setText(cls_lvl);

            String lvlavail = LvlAdvAvail.get(position);
            if (Integer.parseInt(lvlavail) > 1) {
                holder.iv_sLate.setVisibility(View.VISIBLE);
            } else {
                holder.iv_sLate.setVisibility(View.INVISIBLE);
            }
            /// WBR SHOW HIDE///
            if (ShowWBR.get(position).equalsIgnoreCase("true")) {
                holder.btn_wu_b.setVisibility(View.VISIBLE);
                holder.btn_wu_r.setVisibility(View.VISIBLE);
                holder.btn_wu_w.setVisibility(View.VISIBLE);
                holder.lpw_wu_w = new ListPopupWindow(context);
                holder.btn_wu_w.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        holder.lpw_wu_w.show();
                    }
                });
                final String[] wu_w = context.getResources().getStringArray(R.array.wu_w);
                holder.lpw_wu_w.setAdapter(new ArrayAdapter<String>(
                        context.getApplicationContext(),
                        R.layout.edittextpopup, wu_w));
                holder.lpw_wu_w.setAnchorView(holder.btn_wu_w);
                holder.lpw_wu_w.setHeight(LayoutParams.WRAP_CONTENT);
                holder.lpw_wu_w.setModal(true);
                holder.lpw_wu_w.setOnItemClickListener(
                        new OnItemClickListener() {

                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int pos, long id) {
                                // TODO Auto-generated method stub
                                holder.btn_wu_w.setText(wu_w[pos]);
                                holder.lpw_wu_w.dismiss();
                            }
                        });

                holder.lpw_wu_b = new ListPopupWindow(context);
                holder.btn_wu_b.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        holder.lpw_wu_b.show();
                    }
                });
                final String[] wu_b = context.getResources().getStringArray(R.array.wu_b);
                holder.lpw_wu_b.setAdapter(new ArrayAdapter<String>(
                        context.getApplicationContext(),
                        R.layout.edittextpopup, wu_b));
                holder.lpw_wu_b.setAnchorView(holder.btn_wu_b);
                holder.lpw_wu_b.setHeight(LayoutParams.WRAP_CONTENT);
                holder.lpw_wu_b.setModal(true);
                holder.lpw_wu_b.setOnItemClickListener(
                        new OnItemClickListener() {

                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int pos, long id) {
                                // TODO Auto-generated method stub
                                holder.btn_wu_b.setText(wu_b[pos]);
                                holder.lpw_wu_b.dismiss();
                            }
                        });

                holder.lpw_wu_r = new ListPopupWindow(context);
                holder.btn_wu_r.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        holder.lpw_wu_r.show();
                    }
                });
                final String[] wu_r = context.getResources().getStringArray(R.array.wu_r);
                holder.lpw_wu_r.setAdapter(new ArrayAdapter<String>(
                        context.getApplicationContext(),
                        R.layout.edittextpopup, wu_r));
                holder.lpw_wu_r.setAnchorView(holder.btn_wu_r);
                holder.lpw_wu_r.setHeight(LayoutParams.WRAP_CONTENT);
                holder.lpw_wu_r.setModal(true);
                holder.lpw_wu_r.setOnItemClickListener(
                        new OnItemClickListener() {

                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int pos, long id) {
                                // TODO Auto-generated method stub
                                holder.btn_wu_r.setText(wu_r[pos]);
                                holder.lpw_wu_r.dismiss();
                            }
                        });


            } else {
                holder.btn_wu_b.setVisibility(View.INVISIBLE);
                holder.btn_wu_r.setVisibility(View.INVISIBLE);
                holder.btn_wu_w.setVisibility(View.INVISIBLE);
            }


            /////////////////For level and schedule level//////////////////////
            holder.oldlevel = Integer.parseInt(SLevel.get(position));
            holder.newlevel = Integer.parseInt(SLevel.get(position));
            String levelname = LevelName.get(LevelID.indexOf(SLevel.get(position)));
            String schlevelname = LevelName.get(LevelID.indexOf(ScheLevel.get(position)));
            holder.et_sLevel.setText(levelname);
            holder.et_sSch_leve.setText(schlevelname);
            holder.tv_sPaid_cls.setText(PaidClasses.get(position));
            holder.et_sSch_leve.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    holder.listpopupwindow1.show();
                }
            });

            holder.listpopupwindow = new ListPopupWindow(context);
            holder.et_sLevel.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    holder.listpopupwindow.show();
                }
            });
            holder.listpopupwindow.setAdapter(new ArrayAdapter<String>(
                    context.getApplicationContext(),
                    R.layout.edittextpopup, LevelName));
            holder.listpopupwindow.setAnchorView(holder.et_sLevel);
            holder.listpopupwindow.setHeight(300);
            holder.listpopupwindow.setModal(true);
            holder.listpopupwindow.setOnItemClickListener(
                    new OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view,
                                                int pos, long id) {
                            // TODO Auto-generated method stub
                            holder.et_sLevel.setText(LevelName.get(pos));
                            holder.et_sSch_leve.setText(LevelName.get(pos));
                            holder.levelChanged = true;
                            holder.newlevel = Integer.parseInt(LevelID.get(pos));
                            if (holder.levelChanged) {
//    						if (holder.newlevel == 4) {
//    				            if ((holder.oldlevel == 11) || (holder.oldlevel == 12)) {
//    				                SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
//    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.1",
//    				                		"Ok");
//    				            }
//    				        }
//    						else if (holder.newlevel == 5) {
//    				            if ((holder.oldlevel == 11) || (holder.oldlevel == 12) || (holder.oldlevel == 13)) {
//    				                SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
//    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.2",
//    				                		"Ok");
//    				            }
//    				        }
//
//    						else if (holder.newlevel == 6) {
//    				            if (holder.oldlevel == 4) {
//    				            	SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
//    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.3",
//    				                		"Ok");
//    				            }
//    				        }
//    						else if (holder.newlevel == 7) {
//    				            if ((holder.oldlevel == 4) || (holder.oldlevel == 5)) {
//    				            	SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
//    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.4",
//    				                		"Ok");
//    				            }
//    				        }
//    						else if (holder.newlevel == 8) {
//    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 6)) && (holder.newlevel != holder.oldlevel) && (holder.oldlevel != 8)) {
//    				            	SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
//    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.5",
//    				                		"Ok");
//    				            }
//    				        }
//
//    						else if (holder.newlevel == 9) {
//    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 7)) && (holder.newlevel != holder.oldlevel)) {
//    				            	SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
//    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.6",
//    				                		"Ok");
//    				            }
//    				        }
//
//    						else if (holder.newlevel == 10) {
//    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 8)) && (holder.newlevel != holder.oldlevel)) {
//    				            	SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
//    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.7",
//    				                		"Ok");;
//    				            }
//    				        }
//
//    						else if (holder.newlevel == 13) {
//    				            if ((holder.oldlevel == 11)) {
//    				            	SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
//    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.8",
//    				                		"Ok");
//    				            }
//    				        }
//    						else if (holder.newlevel == 14) {
//    				            if ((holder.oldlevel >= 4) && (holder.oldlevel <= 9)) {
//    				            	SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
//    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.9",
//    				                		"Ok");
//    				            }
//    				            if ((holder.oldlevel >= 11) && (holder.oldlevel <= 13)) {
//    				            	SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
//    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.10",
//    				                		"Ok");
//    				            }
//    				        }
                            }
                            holder.listpopupwindow.dismiss();
                        }
                    });


            holder.listpopupwindow1.setAdapter(new ArrayAdapter<String>(
                    context,
                    R.layout.edittextpopup, LevelName));
            holder.listpopupwindow1.setAnchorView(holder.et_sSch_leve);
//            holder.listpopupwindow1.setWidth(90);
            holder.listpopupwindow1.setHeight(300);
            holder.listpopupwindow1.setModal(true);
            holder.listpopupwindow1.setOnItemClickListener(new OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view,
                                        int pos, long id) {
                    // TODO Auto-generated method stub
                    holder.et_sSch_leve.setText(LevelName.get(pos));
                    holder.listpopupwindow1.dismiss();

                }
            });

            //Comment//
            //Using MyTagHandler class for generating list//
            holder.tv_sComments.setText(Html.fromHtml(Comments.get(position).toString(), null, new MyTagHandler()));


            String birthdate = BirthDay.get(position);
            if (birthdate.equalsIgnoreCase("false")) {
                holder.btn_sBirthday.setVisibility(View.INVISIBLE);
            }

            String swimcomp = SwimComp.get(position);
            if (swimcomp.equalsIgnoreCase("false")) {
                holder.btn_sSwim_comp.setVisibility(View.INVISIBLE);
            }

            ArrayList<String> finalABBR = (ArrayList<String>) Abbr.get(position);
            ArrayList<String> finalPreReqId = (ArrayList<String>) PreReqID.get(position);
            ArrayList<String> finalPreReqChecked = (ArrayList<String>) PreReqChecked.get(position);

            int offset_in_column = 0, table_size = Integer.parseInt(SkillsCount.get(position));
            TableRow tr = null;

            int offset_in_table = 0;
            for (offset_in_table = 0; offset_in_table < table_size; offset_in_table++) {

                if (offset_in_column == 0) {
                    tr = new TableRow(context.getApplicationContext());
                    tr.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                }

                final CheckBox check = new CheckBox(context.getApplicationContext());
                check.setText(finalABBR.get(offset_in_table));
                check.setId(Integer.parseInt(finalPreReqId.get(offset_in_table)));
                check.setButtonDrawable(context.getResources().getDrawable(R.drawable.checkbox_selection));
                check.setTextColor(context.getResources().getColor(R.color.texts1));
                check.setPadding(2, 0, 0, 2);
                check.setMaxLines(2);

                check.setChecked(Boolean.valueOf(finalPreReqChecked.get(offset_in_table)));

                check.setLayoutParams(new TableRow.LayoutParams(0, 50, 1));

                tr.addView(check);

                offset_in_column++;
                if (offset_in_column == 4) {

                    holder.tl_skills.addView(tr);
                    offset_in_column = 0;
                }
                check.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
//						int a = check.getId();
//						if()
                        if (check.isChecked()) {
//							Toast.makeText(context,"Checkbox text = "+ check.getText().toString()+"\nCheckbox Id = "+check.getId(), 1).show();
                        }
                    }
                });
            }

            if (offset_in_column != 0) {
                holder.tl_skills.addView(tr);
            }
            holder.selection.setTag(position);
            holder.selection.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    CheckBox cb = (CheckBox) v;
                    if (selectedcb != null) {
                        if (!cb.getTag().equals(selectedcb.getTag())) {
                            selectedcb.setChecked(false);
                        }
                    }
                    selectedcb = cb;
                    if (holder.selection.isChecked()) {
//            			Toast.makeText(context, ""+StudentID.get(position), 1).show();
                        WW_StaticClass.Studentid = StudentID.get(position);
                        WW_StaticClass.Siteid = SiteID.get(position);
                        checkedpos.add("" + position);
                    }
                }
            });


            holder.btn_sNote.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
//					Toast.makeText(context, "Click", 1).show();
                    WW_StaticClass.Studentid = StudentID.get(position);
                    ((Activity) context).finish();
                    Intent it = new Intent(v.getContext(), ModifyComments.class);
                    it.putExtra("FROM", "CURRENT");
                    it.putExtra("yes_no_date", yes_no_date);
                    v.getContext().startActivity(it);

                }
            });
            btn_footer_send_attendance.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (Utility.isNetworkConnected(context)) {

                    } else {
                        onDetectNetworkState().show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    char levelchanged;
    int lev, oldlev;

    private class Insert_Attandance extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            ViewCurrentLessonActivity.fl_vcl_loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_Insert_Attandance);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("UserLevelStatus", WW_StaticClass.UserLevel);
            request.addProperty("levelschanged", levelchanged);
            request.addProperty("chkschedselect", true);
            request.addProperty("oldlev", oldlev);
            request.addProperty("lev", lev);
            request.addProperty("oldatt", 0);
//			att


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Insert_Attandance,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.bodyIn;
                Log.i(tag, "Result : " + response.toString());
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            ViewCurrentLessonActivity.fl_vcl_loading.setVisibility(View.GONE);
        }
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ((Activity) context).finish();
                    }
                })
                .setPositiveButton("Οk", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
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
}
