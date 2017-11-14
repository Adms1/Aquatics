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
import java.util.ArrayList;

import water.works.waterworks.ModifyComments;
import water.works.waterworks.MyTagHandler;
import water.works.waterworks.R;
import water.works.waterworks.ViewCurrentLessonActivity;
import water.works.waterworks.model.AttendanceItems;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class AttandanceAdapter extends BaseAdapter implements AnimationListener{
	File dir = Environment.getExternalStorageDirectory();
	File yourFile;
	Context context;
	ArrayList<AttendanceItems> attItem;
	Animation animBlink;
	ArrayList<String> checkedpos ;
	ArrayList<Boolean> ischange;
	ArrayList<String> newslevel,newddlw,newddlb,newddlr,newschdlevel,checkpos;
	ArrayList<Integer>newatt;
	ArrayList<String> newprereqid;
//	SparseBooleanArray mChecked = new SparseBooleanArray();
	public AttandanceAdapter(Context context, ArrayList<AttendanceItems> attItem) {
		this.context = context;
		this.attItem = attItem;
	}


	public int getCount() {
		// TODO Auto-generated method stub
		return attItem.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return attItem.get(position);
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
	public class ViewHolder
    {
        TextView tv_sName,tv_pName,tv_sAge,tv_sPaid_cls,tv_sComments,tv_cancel;
        ImageView btn_sIsa;
        ImageButton btn_sNote,btn_sBirthday,btn_sCamera;
        Button btn_sCls_lvl,btn_sSwim_comp,btn_more;
        ImageView iv_sLate;
//        TextView et_sLevel,et_sSch_leve;
        Button et_sLevel,et_sSch_leve;
        CheckBox selection;
//        Button btn_wu_w,btn_wu_r,btn_wu_b;
        ToggleButton sw_precent;
        TableLayout tl_skills;
        ListPopupWindow listpopupwindow,listpopupwindow1,lpw_wu_w,lpw_wu_b,lpw_wu_r;
        int oldlevel,newlevel;
        ImageButton btn_request_deck,level_add,level_sub,sch_level_add,sch_level_sub;
        Dialog dialog;
        RelativeLayout attandance_main;
    }
	private int[] colors = new int[] { Color.parseColor("#EEEEEE"), Color.parseColor("#FFFFFF") };
	View temp_view;

	public View getView(final int position,  View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		try{
			if (convertView == null) {
				holder = new ViewHolder();
				
				
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.viewcurrentschedulerow, null);
				int colorpos=position%colors.length;
				convertView.setBackgroundColor(colors[colorpos]);
				temp_view = convertView;
				holder.attandance_main = (RelativeLayout)convertView.findViewById(R.id.attandance_main);
			 holder.selection = (CheckBox)convertView.findViewById(R.id.selection);
             holder.btn_sIsa = (ImageView)convertView.findViewById(R.id.btn_isa_alert);
             holder.tv_sName = (TextView)convertView.findViewById(R.id.tv_cl_studentname_row);
             holder.tv_pName = (TextView)convertView.findViewById(R.id.tv_cl_parentname_row);             
             holder.btn_sCamera = (ImageButton)convertView.findViewById(R.id.btn_camera);
             holder.tv_sAge = (TextView)convertView.findViewById(R.id.tv_cl_age_row);
//             holder.et_sLevel = (TextView)convertView.findViewById(R.id.et_cl_slevel);
//             holder.et_sSch_leve = (TextView)convertView.findViewById(R.id.et_cl_sched_level);
             holder.et_sLevel = (Button)convertView.findViewById(R.id.btn_cl_slevel);
             holder.et_sSch_leve = (Button)convertView.findViewById(R.id.btn_cl_sched_level);
             holder.sw_precent = (ToggleButton)convertView.findViewById(R.id.yes_no);
             holder.btn_sCls_lvl = (Button)convertView.findViewById(R.id.btn_cl_cls_lvl_row);
             holder.tv_sPaid_cls = (TextView)convertView.findViewById(R.id.tv_cl_paid_cls_row);
//             holder.btn_wu_w = (Button)convertView.findViewById(R.id.chb_cl_wbr_w);
//             holder.btn_wu_b = (Button)convertView.findViewById(R.id.chb_cl_wbr_b);
//             holder.btn_wu_r = (Button)convertView.findViewById(R.id.chb_cl_wbr_r);
             holder.tv_sComments = (TextView)convertView.findViewById(R.id.tv_cl_comment);
             holder.btn_sNote = (ImageButton)convertView.findViewById(R.id.btn_cl_note);
             holder.btn_sSwim_comp = (Button)convertView.findViewById(R.id.btn_swim_comp);
             holder.iv_sLate = (ImageView)convertView.findViewById(R.id.img_late);
             holder.btn_sBirthday = (ImageButton)convertView.findViewById(R.id.btn_cake);
             holder.tl_skills = (TableLayout)convertView.findViewById(R.id.table_cl_data);
             holder.sch_level_add = (ImageButton)convertView.findViewById(R.id.ib_plus_sch_level);
             holder.sch_level_sub = (ImageButton)convertView.findViewById(R.id.ib_sub_sch_level);
             holder.level_add = (ImageButton)convertView.findViewById(R.id.ib_plus_level);
             holder.level_sub = (ImageButton)convertView.findViewById(R.id.ib_sub_level);
             holder.btn_more = (Button)convertView.findViewById(R.id.more);
             holder.btn_more.setVisibility(View.GONE);
             holder.listpopupwindow = new ListPopupWindow(context.getApplicationContext());
             holder.listpopupwindow1 = new ListPopupWindow(context.getApplicationContext());
             holder.btn_request_deck = (ImageButton)convertView.findViewById(R.id.btn_request_deck);
             holder.tv_cancel = (TextView)convertView.findViewById(R.id.tv_table_cancel);
//             if(WW_StaticClass.attendance_sended){
//            	 AttendanceItems.btn_footer_send_attendance.setEnabled(false);
//             }
//             else{
//            	 AttendanceItems.btn_footer_send_attendance.setEnabled(true);
//             }
             AttendanceItems.btn_footer_send_attendance.setEnabled(true);
             ischange = new ArrayList<Boolean>();
			newslevel = new ArrayList<String>();
			newschdlevel = new ArrayList<String>();
			checkpos = new ArrayList<String>();
			
			newatt = new ArrayList<Integer>();
			ddlW = new ArrayList<String>();
			ddlB = new ArrayList<String>();
			ddlR = new ArrayList<String>();
			newprereqid = new ArrayList<String>();
			for(int i=0;i<attItem.size();i++){
				ischange.add(i,false);
				newslevel.add(i,attItem.get(i).getSLevel());
				newschdlevel.add(i,attItem.get(i).getSchLevel());
				newatt.add(i,0);
				ddlW.add(i,attItem.get(i).getWu_w());
				ddlB.add(i,attItem.get(i).getWu_b());
				ddlR.add(i,attItem.get(i).getWu_r());
			}
			checkedpos = new ArrayList<String>();
			newddlw = new ArrayList<String>();
			newddlb = new ArrayList<String>();
			newddlr = new ArrayList<String>();
			animBlink = AnimationUtils.loadAnimation(context.getApplicationContext(),
	                R.anim.blink);
			animBlink.setAnimationListener(this);
			
			 //////////Att taken//////////////
			int temp_att = attItem.get(position).getATT();
            Log.i("Att adapter", "Att taken = " + attItem.get(position).getWu_attendancetaken());
            Log.i("Att adapter", "Att = " + attItem.get(position).getATT());
            if(attItem.get(position).getWu_attendancetaken()==0){
            	convertView.setBackgroundColor(colors[colorpos]);
            	holder.tl_skills.setVisibility(View.VISIBLE);
            	holder.tv_cancel.setVisibility(View.GONE);
            	holder.sw_precent.setChecked(true);
            	holder.sw_precent.setEnabled(true);
            	newatt.remove(position);
            	newatt.add(position,0);
            	if(attItem.get(position).getHasISA().equalsIgnoreCase("true")){
                	convertView.setBackgroundColor(Color.rgb(207, 255, 191));
                	holder.btn_sIsa.setVisibility(View.VISIBLE);
                	holder.btn_sIsa.startAnimation(animBlink);
                }
                else{
                	holder.btn_sIsa.setVisibility(View.INVISIBLE);
                	convertView.setBackgroundColor(colors[colorpos]);
                }
            }
            else {
            	if(temp_att==2||temp_att==3||temp_att==4||temp_att==5||temp_att==6||temp_att==7||temp_att==8||temp_att==10||
            			temp_att==12||temp_att==13||temp_att==14||temp_att==15||temp_att==16||temp_att==17){
            		convertView.setBackgroundColor(Color.rgb(252, 106, 108));
            		holder.tl_skills.setVisibility(View.GONE);
	            	holder.btn_more.setVisibility(View.GONE);
	            	holder.tv_cancel.setVisibility(View.VISIBLE);
	            	holder.sw_precent.setChecked(false);
	            	holder.sw_precent.setEnabled(false);
	            	
	            	newatt.remove(position);
	            	newatt.add(position,1);
	            	if(attItem.get(position).getHasISA().equalsIgnoreCase("true")){
                    	holder.btn_sIsa.setVisibility(View.VISIBLE);
                    	holder.btn_sIsa.startAnimation(animBlink);
                    }
                    else{
                    	holder.btn_sIsa.setVisibility(View.INVISIBLE);
                    }
            	}
            	else if(temp_att==0){
            		convertView.setBackgroundColor(colors[colorpos]);
                	holder.tl_skills.setVisibility(View.GONE);
                	holder.tv_cancel.setVisibility(View.GONE);
                	holder.sw_precent.setChecked(true);
                	holder.sw_precent.setEnabled(false);
                	newatt.remove(position);
                	newatt.add(position,0);
                	if(attItem.get(position).getHasISA().equalsIgnoreCase("true")){
                    	convertView.setBackgroundColor(Color.rgb(207, 255, 191));
                    	holder.btn_sIsa.setVisibility(View.VISIBLE);
                    	holder.btn_sIsa.startAnimation(animBlink);
                    }
                    else{
                    	holder.btn_sIsa.setVisibility(View.INVISIBLE);
                    	convertView.setBackgroundColor(colors[colorpos]);
                    }
            	}
            	else{
            		convertView.setBackgroundColor(colors[colorpos]);
                	holder.tl_skills.setVisibility(View.GONE);
                	holder.tv_cancel.setVisibility(View.GONE);
                	holder.sw_precent.setChecked(false);
                	holder.sw_precent.setEnabled(false);
                	newatt.remove(position);
                	newatt.add(position,0);
                	if(attItem.get(position).getHasISA().equalsIgnoreCase("true")){
                    	convertView.setBackgroundColor(Color.rgb(207, 255, 191));
                    	holder.btn_sIsa.setVisibility(View.VISIBLE);
                    	holder.btn_sIsa.startAnimation(animBlink);
                    }
                    else{
                    	holder.btn_sIsa.setVisibility(View.INVISIBLE);
                    	convertView.setBackgroundColor(colors[colorpos]);
                    }
            	}
            }
			
			
			holder.sw_precent.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				

				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						newatt.remove(position);
						newatt.add(position,0);
					}
					else{
						newatt.remove(position);
						newatt.add(position,1);
					}
				}
			});
			
			String gender = attItem.get(position).getStudentGender().trim();
			Log.i("Attendance adapter","Gender = "+gender);
			
			String sname = attItem.get(position).getSFName()+" "+attItem.get(position).getSLName();
			String fname = "("+attItem.get(position).getPFName()+" "+attItem.get(position).getPLName()+")";
			if(gender.toString().equalsIgnoreCase("Female")){
				holder.tv_sName.setTextColor(Color.rgb(136, 0, 183));
				 holder.tv_sName.setText(sname);
				 
			}
			else{
				holder.tv_sName.setTextColor(Color.rgb(0, 0, 102)); 
				holder.tv_sName.setText(sname);
				 
			}
			holder.tv_pName.setText(fname);
			
			holder.tv_sAge.setText(attItem.get(position).getAge());
            
           
            
            
            /// For cls lvl /////////
            String cls_lvl = attItem.get(position).getClsLvl();
            holder.btn_sCls_lvl.setText(cls_lvl);
            
            String lvlavail = attItem.get(position).getLvlAdvAvail();
            if(Integer.parseInt(lvlavail)>1){
            	holder.iv_sLate.setVisibility(View.VISIBLE);
            }
            else{
            	holder.iv_sLate.setVisibility(View.INVISIBLE);
            }
            
          /// WBR SHOW HIDE///
/*            if(attItem.get(position).getShwWBR().equalsIgnoreCase("true")){
            	holder.btn_wu_b.setVisibility(View.VISIBLE);
            	holder.btn_wu_r.setVisibility(View.VISIBLE);
            	holder.btn_wu_w.setVisibility(View.VISIBLE);
            	holder.lpw_wu_w = new ListPopupWindow(context.getApplicationContext());
                  holder.btn_wu_w.setOnClickListener(new OnClickListener() {
      				
      				@Override
      				public void onClick(View v) {
      					// TODO Auto-generated method stub
      					holder.lpw_wu_w.show();
      				}
      			});
                  final String[] wu_w = context.getResources().getStringArray(R.array.wu_w);
                  holder.lpw_wu_w.setAdapter(new ArrayAdapter<String>(
          	            context.getApplicationContext(),
          	            R.layout.edittextpopup,wu_w));
          		holder.lpw_wu_w.setAnchorView(holder.btn_wu_w);
          		holder.lpw_wu_w.setHeight(LayoutParams.WRAP_CONTENT);
          		holder.lpw_wu_w.setModal(true);
          		holder.lpw_wu_w.setOnItemClickListener(
                      new OnItemClickListener() {

          				@Override
          				public void onItemClick(AdapterView<?> parent, View view,
          						int pos, long id) {
          					// TODO Auto-generated method stub
          					holder.btn_wu_w.setText(wu_w[pos]);
          					ddlW.remove(position);
          					ddlW.add(position,""+pos);
          					holder.lpw_wu_w.dismiss();
          				}
          			});
          		
          	  holder.lpw_wu_b = new ListPopupWindow(context.getApplicationContext());
              holder.btn_wu_b.setOnClickListener(new OnClickListener() {
  				
  				@Override
  				public void onClick(View v) {
  					// TODO Auto-generated method stub
  					holder.lpw_wu_b.show();
  				}
  			});
              final String[] wu_b = context.getResources().getStringArray(R.array.wu_b);
              holder.lpw_wu_b.setAdapter(new ArrayAdapter<String>(
      	            context.getApplicationContext(),
      	            R.layout.edittextpopup,wu_b));
      		holder.lpw_wu_b.setAnchorView(holder.btn_wu_b);
      		holder.lpw_wu_b.setHeight(LayoutParams.WRAP_CONTENT);
      		holder.lpw_wu_b.setModal(true);
      		holder.lpw_wu_b.setOnItemClickListener(
                  new OnItemClickListener() {

      				@Override
      				public void onItemClick(AdapterView<?> parent, View view,
      						int pos, long id) {
      					// TODO Auto-generated method stub
      					holder.btn_wu_b.setText(wu_b[pos]);
      					ddlB.remove(position);
      					ddlB.add(position,""+pos);
      					holder.lpw_wu_b.dismiss();
      				}
      			});

      	  holder.lpw_wu_r = new ListPopupWindow(context.getApplicationContext());
          holder.btn_wu_r.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					holder.lpw_wu_r.show();
				}
			});
          final String[] wu_r = context.getResources().getStringArray(R.array.wu_r);
          holder.lpw_wu_r.setAdapter(new ArrayAdapter<String>(
  	            context.getApplicationContext(),
  	            R.layout.edittextpopup,wu_r));
  		holder.lpw_wu_r.setAnchorView(holder.btn_wu_r);
  		holder.lpw_wu_r.setHeight(LayoutParams.WRAP_CONTENT);
  		holder.lpw_wu_r.setModal(true);
  		holder.lpw_wu_r.setOnItemClickListener(
              new OnItemClickListener() {

  				@Override
  				public void onItemClick(AdapterView<?> parent, View view,
  						int pos, long id) {
  					// TODO Auto-generated method stub
  					holder.btn_wu_r.setText(wu_r[pos]);
  					ddlR.remove(position);
  					ddlR.add(position,""+pos);
  					holder.lpw_wu_r.dismiss();
  				}
  			});


            }
            else{
            	holder.btn_wu_b.setVisibility(View.INVISIBLE);
            	holder.btn_wu_r.setVisibility(View.INVISIBLE);
            	holder.btn_wu_w.setVisibility(View.INVISIBLE);
            }*/
          
            /////////////////For level and schedule level//////////////////////
            holder.oldlevel = Integer.parseInt(attItem.get(position).getSLevel());
            holder.newlevel = Integer.parseInt(attItem.get(position).getSLevel());
            final ArrayList<String> LevelName = new ArrayList<String>();
            final ArrayList<String> LevelID = new ArrayList<String>();
            LevelName.addAll(attItem.get(position).getLevelName());
            LevelID.addAll(attItem.get(position).getLevelID());
            String levelname = LevelName.get(LevelID.indexOf(attItem.get(position).getSLevel()));
            String schlevelname = LevelName.get(LevelID.indexOf(attItem.get(position).getSLevel()));
            if(levelname.length()==1){
            	levelname = "0"+levelname;
			}
            if(schlevelname.length()==1){
            	schlevelname = "0"+schlevelname;
			}
             holder.et_sLevel.setText(levelname);
             holder.et_sSch_leve.setText(schlevelname);
             
             holder.level_add.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("deprecation")

				public void onClick(View v) {
					// TODO Auto-generated method stub
					int size = LevelName.size();
					String temp_level =holder.et_sLevel.getText().toString();
					final String templevel,tempschdlevel;
					templevel = holder.et_sLevel.getText().toString();
					tempschdlevel = holder.et_sSch_leve.getText().toString();
					if(temp_level.charAt(0)=='0'){
						temp_level = ""+temp_level.charAt(1);
					}
					final int index = LevelName.indexOf(temp_level);
					if(size==index+1){
						Toast.makeText(context, "Level Maximum", Toast.LENGTH_LONG).show();
					}
					else{
						String lname = LevelName.get(index+1);
						if(lname.length()==1){
							lname = "0"+lname;
						}
						holder.et_sLevel.setText(lname);
						holder.et_sSch_leve.setText(lname);
						AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle("WaterWorks");
						alertDialog.setIcon(R.drawable.ic_launcher);
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.setCancelable(false);
						// set the message
						alertDialog.setMessage("You have selected to change this student’s level. From "+temp_level+" To "+lname +" Is this correct?");
						// set button1 functionality
						alertDialog.setButton("Yes",
								new DialogInterface.OnClickListener() {


									public void onClick(DialogInterface dialog, int which) {
										// close dialog
										ischange.remove(position);
										ischange.add(position, true);
										holder.newlevel = Integer.parseInt(LevelID.get(index+1));
										
										if(ischange.get(position).equals(true)){
				    						newslevel.remove(position);
				    						newschdlevel.remove(position);
				    						newslevel.add(position,LevelID.get(index+1));
				    						newschdlevel.add(position,LevelID.get(index+1));
				    						if (holder.newlevel == 4) {
				    				            if ((holder.oldlevel == 11) || (holder.oldlevel == 12)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.1",
				    				                		"Ok");
				    				            }
				    				        }
				    						else if (holder.newlevel == 5) {
				    				            if ((holder.oldlevel == 11) || (holder.oldlevel == 12) || (holder.oldlevel == 13)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.2",
				    				                		"Ok");
				    				            }
				    				        }

				    						else if (holder.newlevel == 6) {
				    				            if (holder.oldlevel == 4) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.3",
				    				                		"Ok");
				    				            }
				    				        }
				    						else if (holder.newlevel == 7) {
				    				            if ((holder.oldlevel == 4) || (holder.oldlevel == 5)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.4",
				    				                		"Ok");
				    				            }
				    				        }
				    						else if (holder.newlevel == 8) {
				    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 6)) && (holder.newlevel != holder.oldlevel) && (holder.oldlevel != 8)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.5",
				    				                		"Ok");
				    				            }
				    				        }

				    						else if (holder.newlevel == 9) {
				    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 7)) && (holder.newlevel != holder.oldlevel)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.6",
				    				                		"Ok");
				    				            }
				    				        }

				    						else if (holder.newlevel == 10) {
				    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 8)) && (holder.newlevel != holder.oldlevel)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.7",
				    				                		"Ok");
				    				            }
				    				        }

				    						else if (holder.newlevel == 13) {
				    				            if ((holder.oldlevel == 11)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.8",
				    				                		"Ok");
				    				            }
				    				        }
				    						else if (holder.newlevel == 14) {
				    				            if ((holder.oldlevel >= 4) && (holder.oldlevel <= 9)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.9",
				    				                		"Ok");
				    				            }
				    				            if ((holder.oldlevel >= 11) && (holder.oldlevel <= 13)) {
				    				            	showAlert(context, "WaterWorks",
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
									

									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										dialog.cancel();
										holder.et_sLevel.setText(templevel);
										holder.et_sSch_leve.setText(tempschdlevel);
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
					String temp_level =holder.et_sLevel.getText().toString();
					final String templevel,tempschdlevel;
					templevel = holder.et_sLevel.getText().toString();
					tempschdlevel = holder.et_sSch_leve.getText().toString();
					if(temp_level.charAt(0)=='0'){
						temp_level = ""+temp_level.charAt(1);
					}
					final int index = LevelName.indexOf(temp_level);
					if(size==index){
						Toast.makeText(context, "Level Minimum.", Toast.LENGTH_LONG).show();
					}
					else{
						String lname = LevelName.get(index-1);
						if(lname.length()==1){
							lname = "0"+lname;
						}
						holder.et_sLevel.setText(lname);
						holder.et_sSch_leve.setText(lname);
						AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle("WaterWorks");
						alertDialog.setIcon(R.drawable.ic_launcher);
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.setCancelable(false);
						// set the message
						alertDialog.setMessage("You have selected to change this student’s level. From "+temp_level+" To "+lname +" Is this correct?");
						// set button1 functionality
						alertDialog.setButton("Yes",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int which) {
										// close dialog
										ischange.remove(position);
										ischange.add(position, true);
										holder.newlevel = Integer.parseInt(LevelID.get(index-1));
										if(ischange.get(position).equals(true)){
				    						newslevel.remove(position);
				    						newschdlevel.remove(position);
				    						newslevel.add(position,LevelID.get(index+1));
				    						newschdlevel.add(position,LevelID.get(index+1));
				    						if (holder.newlevel == 4) {
				    				            if ((holder.oldlevel == 11) || (holder.oldlevel == 12)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.1",
				    				                		"Ok");
				    				            }
				    				        }
				    						else if (holder.newlevel == 5) {
				    				            if ((holder.oldlevel == 11) || (holder.oldlevel == 12) || (holder.oldlevel == 13)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.2",
				    				                		"Ok");
				    				            }
				    				        }

				    						else if (holder.newlevel == 6) {
				    				            if (holder.oldlevel == 4) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.3",
				    				                		"Ok");
				    				            }
				    				        }
				    						else if (holder.newlevel == 7) {
				    				            if ((holder.oldlevel == 4) || (holder.oldlevel == 5)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.4",
				    				                		"Ok");
				    				            }
				    				        }
				    						else if (holder.newlevel == 8) {
				    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 6)) && (holder.newlevel != holder.oldlevel) && (holder.oldlevel != 8)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.5",
				    				                		"Ok");
				    				            }
				    				        }

				    						else if (holder.newlevel == 9) {
				    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 7)) && (holder.newlevel != holder.oldlevel)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.6",
				    				                		"Ok");
				    				            }
				    				        }

				    						else if (holder.newlevel == 10) {
				    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 8)) && (holder.newlevel != holder.oldlevel)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.7",
				    				                		"Ok");
				    				            }
				    				        }

				    						else if (holder.newlevel == 13) {
				    				            if ((holder.oldlevel == 11)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.8",
				    				                		"Ok");
				    				            }
				    				        }
				    						else if (holder.newlevel == 14) {
				    				            if ((holder.oldlevel >= 4) && (holder.oldlevel <= 9)) {
				    				            	showAlert(context, "WaterWorks",
				    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.9",
				    				                		"Ok");
				    				            }
				    				            if ((holder.oldlevel >= 11) && (holder.oldlevel <= 13)) {
				    				            	showAlert(context, "WaterWorks",
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

									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										dialog.cancel();
										holder.et_sLevel.setText(templevel);
										holder.et_sSch_leve.setText(tempschdlevel);
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
					String temp_level =holder.et_sSch_leve.getText().toString();
					final String templevel = holder.et_sSch_leve.getText().toString();
					if(temp_level.charAt(0)=='0'){
						temp_level = ""+temp_level.charAt(1);
					}
					final int index = LevelName.indexOf(temp_level);
					if(size==index+1){
						Toast.makeText(context, "Schedule Level Maximum", Toast.LENGTH_LONG).show();
					}
					else{
						String lname = LevelName.get(index+1);
						if(lname.length()==1){
							lname = "0"+lname;
						}
						holder.et_sSch_leve.setText(lname);
						AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle("WaterWorks");
						alertDialog.setIcon(R.drawable.ic_launcher);
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.setCancelable(false);
						// set the message
						alertDialog.setMessage("You have selected to change this student’s level. From "+temp_level+" To "+lname +" Is this correct?");
						// set button1 functionality
						alertDialog.setButton("Yes",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int which) {
										// close dialog
										newschdlevel.remove(position);
										newschdlevel.add(position,LevelID.get(index+1));
										dialog.cancel();

									}
								});
						alertDialog.setButton2("No",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										dialog.cancel();
										holder.et_sSch_leve.setText(templevel);
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
					String temp_level =holder.et_sSch_leve.getText().toString();
					final String templevel = holder.et_sSch_leve.getText().toString();
					if(temp_level.charAt(0)=='0'){
						temp_level = ""+temp_level.charAt(1);
					}
					final int index = LevelName.indexOf(temp_level);
					if(size==index){
						Toast.makeText(context, "Schedule Level Minimum.", Toast.LENGTH_LONG).show();
					}
					else{
						String lname = LevelName.get(index-1);
						if(lname.length()==1){
							lname = "0"+lname;
						}
						holder.et_sSch_leve.setText(lname);
						AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle("WaterWorks");
						alertDialog.setIcon(R.drawable.ic_launcher);
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.setCancelable(false);
						// set the message
						alertDialog.setMessage("You have selected to change this student’s level. From "+temp_level+" To "+lname +" Is this correct?");
						// set button1 functionality
						alertDialog.setButton("Yes",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int which) {
										// close dialog
										newschdlevel.remove(position);
										newschdlevel.add(position,LevelID.get(index-1));
										dialog.cancel();

									}
								});
						alertDialog.setButton2("No",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										holder.et_sSch_leve.setText(templevel);
										dialog.cancel();
									}
								});
						// show the alert dialog
						alertDialog.show();
					}

				}
			});
             
             
             ///Paid class/////
             String paidcls = attItem.get(position).getPaidCls();
             String temp1[] = paidcls.toString().split("\\.");
             int paid_cls = Integer.parseInt(temp1[0]);
             if(paid_cls<2){
            	 
            	 holder.tv_sPaid_cls.setText(Html.fromHtml("<b>"+paid_cls+"</b>"));
            	 holder.tv_sPaid_cls.setBackgroundColor(Color.RED);
            	 holder.tv_sPaid_cls.startAnimation(animBlink);
             }
             else{
            	 holder.tv_sPaid_cls.setText(""+paid_cls);
             }
             
             
             ///////////level and schedule level
            holder.et_sSch_leve.setOnClickListener(new OnClickListener() {
				

				public void onClick(View v) {
					// TODO Auto-generated method stub
					holder.listpopupwindow1.show();
				}
			});

            holder.listpopupwindow = new ListPopupWindow(context.getApplicationContext());
    		holder.et_sLevel.setOnClickListener(new OnClickListener() {

    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				holder.listpopupwindow.show();
    			}
    		});
    		holder.listpopupwindow.setAdapter(new ArrayAdapter<String>(
    	            context.getApplicationContext(),
    	            R.layout.edittextpopup,LevelName ));
    		holder.listpopupwindow.setAnchorView(holder.et_sLevel);
    		holder.listpopupwindow.setHeight(LayoutParams.WRAP_CONTENT);
    		holder.listpopupwindow.setModal(true);
    		holder.listpopupwindow.setOnItemClickListener(
                new OnItemClickListener() {
                	@SuppressWarnings("deprecation")

    				public void onItemClick(AdapterView<?> parent, View view,
    						final int pos, long id) {
    					// TODO Auto-generated method stub
    					String levelname,schlevelname;
    					final String level,schdlevel;
    					level = holder.et_sLevel.getText().toString();
    					schdlevel = holder.et_sSch_leve.getText().toString();
    					schlevelname = LevelName.get(pos);
    					levelname = LevelName.get(pos);
    					if(levelname.length()==1){
    		            	levelname = "0"+levelname;
    					}
    		            if(schlevelname.length()==1){
    		            	schlevelname = "0"+schlevelname;
    					}
    					holder.et_sLevel.setText(levelname);
    					holder.et_sSch_leve.setText(schlevelname);
    					holder.listpopupwindow.dismiss();
    					AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    					alertDialog.setTitle("WaterWorks");
    					alertDialog.setIcon(R.drawable.ic_launcher);
    					alertDialog.setCanceledOnTouchOutside(false);
    					alertDialog.setCancelable(false);
    					// set the message
    					alertDialog.setMessage("You have selected to change this student’s level. From "+level +" To " + levelname +" Is this correct?");
    					alertDialog.setButton("Yes",
    							new DialogInterface.OnClickListener() {

    						public void onClick(DialogInterface dialog, int which) {
    							// close dialog
    							ischange.remove(position);
    	    					ischange.add(position, true);
    	    					holder.newlevel = Integer.parseInt(LevelID.get(pos));
    	    					if(ischange.get(position).equals(true)){
    	    						newslevel.remove(position);
    	    						newschdlevel.remove(position);
    	    						newslevel.add(position,LevelID.get(pos));
    	    						newschdlevel.add(position,LevelID.get(pos));
    	    						if (holder.newlevel == 4) {
    	    				            if ((holder.oldlevel == 11) || (holder.oldlevel == 12)) {
    	    				            	showAlert(context, "WaterWorks",
    	    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.1",
    	    				                		"Ok");
    	    				            }
    	    				        }
    	    						else if (holder.newlevel == 5) {
    	    				            if ((holder.oldlevel == 11) || (holder.oldlevel == 12) || (holder.oldlevel == 13)) {
    	    				            	showAlert(context, "WaterWorks",
    	    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.2",
    	    				                		"Ok");
    	    				            }
    	    				        }

    	    						else if (holder.newlevel == 6) {
    	    				            if (holder.oldlevel == 4) {
    	    				            	showAlert(context, "WaterWorks",
    	    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.3",
    	    				                		"Ok");
    	    				            }
    	    				        }
    	    						else if (holder.newlevel == 7) {
    	    				            if ((holder.oldlevel == 4) || (holder.oldlevel == 5)) {
    	    				            	showAlert(context, "WaterWorks",
    	    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.4",
    	    				                		"Ok");
    	    				            }
    	    				        }
    	    						else if (holder.newlevel == 8) {
    	    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 6)) && (holder.newlevel != holder.oldlevel) && (holder.oldlevel != 8)) {
    	    				            	showAlert(context, "WaterWorks",
    	    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.5",
    	    				                		"Ok");
    	    				            }
    	    				        }

    	    						else if (holder.newlevel == 9) {
    	    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 7)) && (holder.newlevel != holder.oldlevel)) {
    	    				            	showAlert(context, "WaterWorks",
    	    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.6",
    	    				                		"Ok");
    	    				            }
    	    				        }

    	    						else if (holder.newlevel == 10) {
    	    				            if (((holder.oldlevel >= 4) && (holder.oldlevel <= 8)) && (holder.newlevel != holder.oldlevel)) {
    	    				            	showAlert(context, "WaterWorks",
    	    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.7",
    	    				                		"Ok");
    	    				            }
    	    				        }

    	    						else if (holder.newlevel == 13) {
    	    				            if ((holder.oldlevel == 11)) {
    	    				            	showAlert(context, "WaterWorks",
    	    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.8",
    	    				                		"Ok");
    	    				            }
    	    				        }
    	    						else if (holder.newlevel == 14) {
    	    				            if ((holder.oldlevel >= 4) && (holder.oldlevel <= 9)) {
    	    				            	showAlert(context, "WaterWorks",
    	    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.9",
    	    				                		"Ok");
    	    				            }
    	    				            if ((holder.oldlevel >= 11) && (holder.oldlevel <= 13)) {
    	    				            	showAlert(context, "WaterWorks",
    	    				                		"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.10",
    	    				                		"Ok");
    	    				            }
    	    				        }
    	    					}

    						}
    					});
    					alertDialog.setButton2("No",
    							new DialogInterface.OnClickListener() {

    								public void onClick(DialogInterface dialog, int which) {
    									// TODO Auto-generated method stub
    									dialog.cancel();
    									holder.et_sLevel.setText(level);
    									holder.et_sSch_leve.setText(schdlevel);
    								}
    							});
    					// show the alert dialog
    					alertDialog.show();
    				}
    			});

            
            holder.listpopupwindow1.setAdapter(new ArrayAdapter<String>(
		            context.getApplicationContext(),
		            R.layout.edittextpopup,LevelName ));
            holder.listpopupwindow1.setAnchorView(holder.et_sSch_leve);
            holder.listpopupwindow1.setHeight(LayoutParams.WRAP_CONTENT);
            holder.listpopupwindow1.setModal(true);
            holder.listpopupwindow1.setOnItemClickListener(new OnItemClickListener() {

				@SuppressWarnings("deprecation")

				public void onItemClick(AdapterView<?> parent, View view,
						final int pos, long id) {
					// TODO Auto-generated method stub
					 String schlevelname = "";
					schlevelname = LevelName.get(pos);
					final String schdlevel= holder.et_sSch_leve.getText().toString();
					if(schlevelname.toString().length()==1){
						schlevelname = "0"+schlevelname;
					}
					holder.et_sSch_leve.setText(schlevelname);
					holder.listpopupwindow1.dismiss();
					AlertDialog alertDialog = new AlertDialog.Builder(context).create();
					alertDialog.setTitle("WaterWorks");
					alertDialog.setIcon(R.drawable.ic_launcher);
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.setCancelable(false);
					// set the message
					alertDialog.setMessage("You have selected to change this student’s level.From "+schdlevel +" To "+schlevelname+ " Is this correct?");
					alertDialog.setButton("Yes",
							new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// close dialog
							newschdlevel.remove(position);
							newschdlevel.add(position,LevelID.get(position));
//							holder.listpopupwindow1.dismiss();
						}
					});
					alertDialog.setButton2("No",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
									holder.et_sSch_leve.setText(schdlevel);
								}
							});
					// show the alert dialog
					alertDialog.show();
				}
			});
            
             //new student//
            Boolean newstudent = attItem.get(position).getNewStudent();
            Log.i("here", "New student = " +newstudent);
            if(newstudent==true)
            {
            	String next = "<font color='#EE0000'>New Student</font>";
            	holder.tv_sComments.setText(Html.fromHtml(next+attItem.get(position).getComments().toString(), null, new MyTagHandler()));
            }
            else{
           //Comment//
             //Using MyTagHandler class for generating list//
             holder.tv_sComments.setText(Html.fromHtml(attItem.get(position).getComments().toString(), null, new MyTagHandler()));
            }
             //Birthday//
             String birthdate = attItem.get(position).getBirthday();
             if(birthdate.equalsIgnoreCase("false")){
             	holder.btn_sBirthday.setVisibility(View.INVISIBLE);
             }
             else{
            	 holder.btn_sBirthday.setVisibility(View.VISIBLE);
            	 holder.btn_sBirthday.startAnimation(animBlink);
             }
             
             //Swim Comp//
             String swimcomp = attItem.get(position).getSwimComp();
             Log.i("Att adpter", swimcomp + "\n"+attItem.get(position).getHasSwim());
             if(attItem.get(position).getHasSwim().equalsIgnoreCase("true")&&swimcomp.equalsIgnoreCase("true")){
            	 holder.btn_sSwim_comp.setVisibility(View.VISIBLE);
            	 holder.btn_sSwim_comp.startAnimation(animBlink);
             }
             else{
             	holder.btn_sSwim_comp.setVisibility(View.INVISIBLE);
             }
             holder.btn_sSwim_comp.setOnClickListener(new OnClickListener() {

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
				final	EditText et_reason = (EditText)d.findViewById(R.id.et_csc_reason);
				final	Button reason1 = (Button)d.findViewById(R.id.btn_csc_1);
				final	Button reason2 = (Button)d.findViewById(R.id.btn_csc_2);
				final	Button reason3 = (Button)d.findViewById(R.id.btn_csc_3);
				final	Button reason4 = (Button)d.findViewById(R.id.btn_csc_4);
				final	Button reason5 = (Button)d.findViewById(R.id.btn_csc_5);
				final	Button reason6 = (Button)d.findViewById(R.id.btn_csc_6);
				final	Button reason7 = (Button)d.findViewById(R.id.btn_csc_7);
				final	Button send_request = (Button)d.findViewById(R.id.btn_csc_add_other);
				et_reason.setVisibility(View.GONE);
				send_request.setVisibility(View.GONE);
					reason7.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							et_reason.setVisibility(View.VISIBLE);
							send_request.setVisibility(View.VISIBLE);
						}
					});
					
					reason1.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							reason = "Not Available : Out of town";
							SwimCompId = "0";
							StudentId = attItem.get(position).getStudentID();
							d.dismiss();
							new SubmitReason().execute();
						}
					});
					reason2.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							reason = "Not Available : Another event planned";
							SwimCompId = "0";
							StudentId = attItem.get(position).getStudentID();
							d.dismiss();
							new SubmitReason().execute();
						}
					});
					reason3.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							reason = "Parent feels child is not ready";
							SwimCompId = "0";
							StudentId = attItem.get(position).getStudentID();
							d.dismiss();
							new SubmitReason().execute();
						}
					});
					reason4.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							reason = "Parent is thinking about it";
							SwimCompId = "0";
							StudentId = attItem.get(position).getStudentID();
							d.dismiss();
							new SubmitReason().execute();
						}
					});
					reason5.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							reason = "Parent said yes";
							SwimCompId = "0";
							StudentId = attItem.get(position).getStudentID();
							d.dismiss();
							new SubmitReason().execute();
						}
					});
					reason6.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							reason = "Forgot to mention to parent";
							SwimCompId = "0";
							StudentId = attItem.get(position).getStudentID();
							d.dismiss();
							new SubmitReason().execute();
						}
					});
					send_request.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							reason = "Other";
							comment = et_reason.getText().toString();
							SwimCompId = "0";
							StudentId = attItem.get(position).getStudentID();
							if(comment.toString().equalsIgnoreCase("")){
								Toast.makeText(context, "Please enter reason", 1).show();
							}
							else{
								d.dismiss();
								new SubmitReason().execute();
							}
						}
					});
					
					d.show();
				}
			});
             
             ///// requets deck/////////
             holder.btn_request_deck.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					WW_StaticClass.SStudnetID = attItem.get(position).getStudentID();
        			WW_StaticClass.Siteid = attItem.get(position).getSiteID();
					((ViewCurrentLessonActivity)context).RequestDeck();
				}
			});
             
             
             /// Skill list///
         	final ArrayList<String> finalABBR = (ArrayList<String>)attItem.get(position).getAbbr();
        	final ArrayList<String> finalPreReqId = (ArrayList<String>)attItem.get(position).getPrereqid();
        	final ArrayList<String> finalPreReqChecked = (ArrayList<String>)attItem.get(position).getPrereqchk();
        	int offset_in_column=0, table_size=Integer.parseInt(attItem.get(position).getSkillcount());
        	if(holder.tl_skills.getVisibility()==View.VISIBLE){
        	if(table_size >12){
        		table_size =12;
        		holder.btn_more.setVisibility(View.VISIBLE);
        		String more = "More Skill Sets...";
        		SpannableString ss=  new SpannableString(more);
        		 ss.setSpan(new RelativeSizeSpan(2f),15,more.length(),0);
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
    					
    					Log.i("Abbr", "Abbr = " +  abbr);
    					holder.dialog = new Dialog(context);
    					holder.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//    					 holder.dialog.setTitle("More Skills:");
    					  
    					 holder.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//    					 holder.dialog.getWindow().setBackgroundDrawable(
//    				                new ColorDrawable(android.graphics.Color.TRANSPARENT));
    					 holder.dialog.setContentView(R.layout.moreabbr);
    					 holder.dialog.setCanceledOnTouchOutside(false);
    					 holder.dialog.setCancelable(true);
    					 holder.dialog.getWindow().getAttributes().width = LayoutParams.WRAP_CONTENT;
    					 holder.dialog.getWindow().getAttributes().height = LayoutParams.WRAP_CONTENT;
    					  final ListView lv_final_to_cc = (ListView)holder.dialog.findViewById(R.id.lv_final_to_cc_dialog);
    					  Button save = (Button)holder.dialog.findViewById(R.id.btn_final_to_cc_save);
    					  Button cancel = (Button)holder.dialog.findViewById(R.id.btn_final_to_cc_cancel);
    					  lv_final_to_cc.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
    					  final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_multiple_choice, abbr);
    					  adapter.hasStableIds();
    					  lv_final_to_cc.setAdapter(adapter);
    					  for (int i = 0; i < abbr.size(); i++) {
    						  if(prereqCheck.get(i).equalsIgnoreCase("true")){
    							lv_final_to_cc.setItemChecked(i, true);
    						  }
    						}
    					  save.setOnClickListener(new OnClickListener() {

    							public void onClick(View v) {
    								// TODO Auto-generated method stub
//    								String name="";
    								SparseBooleanArray checkedO = lv_final_to_cc.getCheckedItemPositions();
    						        for (int i = 0; i < checkedO.size(); i++) {
    						            // Item position in adapter
    						            int pos = checkedO.keyAt(i);
    						            // Add sport if it is checked i.e.) == TRUE!
    						            if (checkedO.valueAt(i)){
	    						            	newprereqid.add(attItem.get(position).getSSchID()+"*"+prereqID.get(pos)+"*");
	    						            	finalPreReqChecked.remove(pos+12);
	    						            	finalPreReqChecked.add(pos+12,""+true);
    						            }
    						        }
//    						        if(name.charAt(0)==','){
//    						        	name = name.substring(1);
//    						        }
    						        Log.i("check id", "Final id = "+newprereqid);
    								holder.dialog.cancel();
    								
    							}
    						});
    					  cancel.setOnClickListener(new OnClickListener() {

    							public void onClick(View v) {
    								// TODO Auto-generated method stub
    								holder.dialog.cancel();
    							}
    						});
    					holder.dialog.show();
    				}
    			});
        	}
        	else{
        		holder.btn_more.setVisibility(View.GONE);
        	}
        	}
            TableRow tr=null;
            int offset_in_table=0;
            for (offset_in_table=0; offset_in_table < table_size; offset_in_table++) {

                if (offset_in_column == 0) {
                    tr = new TableRow(context.getApplicationContext());
                    TableLayout.LayoutParams tableRowParams=
                    		  new TableLayout.LayoutParams
                    		  (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
                    		tr.setLayoutParams(tableRowParams);
                }
                
                 final CheckBox check = new CheckBox(context.getApplicationContext());
                check.setText(finalABBR.get(offset_in_table)); 
                check.setId(Integer.parseInt(finalPreReqId.get(offset_in_table)));
                check.setButtonDrawable(context.getResources().getDrawable(R.drawable.checkbox_selection));
                check.setTextColor(context.getResources().getColor(R.color.texts1));
                check.setPadding(5,5,5,5);
                check.setSingleLine(true);
                check.setChecked(Boolean.valueOf(finalPreReqChecked.get(offset_in_table)));
//                check.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
                
                tr.addView(check);

                offset_in_column++;
                if (offset_in_column == 4) {
                    holder.tl_skills.addView(tr);
                    offset_in_column = 0;
                }
                
                check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(isChecked){
							newprereqid.add(attItem.get(position).getSSchID()+"*"+check.getId()+"*");
						}
						else{
							newprereqid.remove(attItem.get(position).getSSchID()+"*"+check.getId()+"*");
						}
					}
				});
            }
            if (offset_in_column != 0){
                holder.tl_skills.addView(tr);
            }
             
            
            ///Selection for request deck////
//            holder.selection.setTag(position);
//            holder.selection.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				
//				@Override
//				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//					// TODO Auto-generated method stub
//					if(isChecked){          
//	        			WW_StaticClass.SStudnetID = (attItem.get(position).getStudentID());
//	        			WW_StaticClass.Siteid = attItem.get(position).getSiteID();
////	        			checkedpos.add(position,""+isChecked);
//	        			mChecked.put(position,isChecked);
//	        			Log.i("Value", ""+mChecked.toString());
//					
//					} else{          
////						checkpos.remove(isChecked);
//						mChecked.delete(position);
//						Log.i("Value1", ""+mChecked);
//						
//					}
//					
//
//				}
//			});
            
            //// Photo / Camera ///
            holder.btn_sCamera.setOnClickListener(new OnClickListener() {
				

				public void onClick(View v) {
					// TODO Auto-generated method stub
					((ViewCurrentLessonActivity)context).filename = "Water_"+attItem.get(position).getSFName()+"_"+attItem.get(position).getStudentID();
					((ViewCurrentLessonActivity)context).OpenCamera();
				}
			});
            
            ///New comment///
            holder.btn_sNote.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
//					Toast.makeText(context, "Click", 1).show();
					WW_StaticClass.Studentid = attItem.get(position).getStudentID();
					((Activity) context).finish();
					Intent it = new Intent(v.getContext(), ModifyComments.class);
					it.putExtra("FROM", "CURRENT");
					it.putExtra("yes_no_date", attItem.get(position).getYes_no_date());
					v.getContext().startActivity(it);

				}
			});
            
			AttendanceItems.btn_footer_send_attendance.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(Utility.isNetworkConnected(context)){
						straarylist=new ArrayList<String>();
						levelchanged = new ArrayList<Character>();
						prereqid = new ArrayList<String>();
						isched = new ArrayList<String>();
						oldatt = new ArrayList<String>();
						comments = new ArrayList<String>();
						wu_sscheduleid = new ArrayList<String>();
						wu_studentid = new ArrayList<String>();
						wu_orderdetailid = new ArrayList<String>();
						wu_lessontypeid = new ArrayList<String>();
						wu_sttimehr = new ArrayList<String>();
						wu_sttimemin = new ArrayList<String>();
						wu_scheduledate = new ArrayList<String>();
//						ddlW,ddlB,ddlR,wu_siteid,
						wu_slevel=new ArrayList<String>();
						ddlSchedLevel = new ArrayList<String>();
						wu_schedlevel = new ArrayList<String>();
						oldlev = new ArrayList<String>();
						lev = new ArrayList<String>();
						att = new ArrayList<String>();
						chkschedselect = new ArrayList<String>();
						siteid = attItem.get(0).getSiteID();
						int newpos;
						for(int i=0;i<attItem.size();i++){
							newpos = i;
							if(ischange.get(newpos).equals(true)){
								levelchanged.add('Y');
							}
							else{
								levelchanged.add('N');
							}
							newddlw.add(ddlW.get(newpos));
							newddlb.add(ddlB.get(newpos));
							newddlr.add(ddlR.get(newpos));
							oldlev.add(attItem.get(newpos).getSLevel());
							lev.add(newslevel.get(newpos));
							ddlSchedLevel.add(attItem.get(newpos).getSchLevel());
							wu_schedlevel.add(newschdlevel.get(newpos));
							att.add(""+newatt.get(newpos));
							oldatt.add("0");
							chkschedselect.add("true");
							isched.add(attItem.get(newpos).getISchID());
							comments.add(attItem.get(newpos).getWu_Comments());
							wu_sscheduleid.add(attItem.get(newpos).getSSchID());
							wu_studentid.add(attItem.get(newpos).getStudentID());
							wu_orderdetailid.add(attItem.get(newpos).getOrderDetailID());
							wu_lessontypeid.add(attItem.get(newpos).getLessontypeid());
							wu_sttimehr.add(attItem.get(newpos).getStTimehour());
							wu_sttimemin.add(attItem.get(newpos).getStTimemin());
							wu_scheduledate.add(attItem.get(newpos).getMaindate());
							wu_slevel.add(attItem.get(newpos).getSLevel());
							String match = attItem.get(newpos).getSSchID();
							Log.i("Here", ""+newprereqid);
							for(int q=0;q<newprereqid.size();q++){
								if(newprereqid.get(q).contains(match)){
									prereqid.add(newprereqid.get(q));
								}
								else{}
							}
						}
						new Insert_Attandance().execute();
					}
					else{
						onDetectNetworkState().show();
					}
				}
			});
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
            
		}
		catch(OutOfMemoryError e){
			e.printStackTrace();
		}
		catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return convertView;
	}
	

	String siteid;
	ArrayList<String> prereqid;
	ArrayList<String> FinalPreReqId = new ArrayList<String>();
	ArrayList<String> lev,oldlev,att;
	ArrayList<Character> levelchanged;
	ArrayList<String> oldatt,isched,comments,wu_sscheduleid,wu_studentid,wu_orderdetailid,wu_lessontypeid,wu_sttimehr,wu_sttimemin,
	wu_scheduledate,ddlW,ddlB,ddlR,wu_siteid,wu_slevel,ddlSchedLevel,wu_schedlevel,chkschedselect,straarylist,Msg_Status,Msg_Str;
	boolean attendance_response=false,server_response=false,cancel_response=false;
	private class Insert_Attandance extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			ViewCurrentLessonActivity.fl_vcl_loading.setVisibility(View.VISIBLE);
			for (int i = 0; i < prereqid.size(); i++) {
				if(FinalPreReqId.contains(prereqid.get(i))){
					
				}
				else{
					FinalPreReqId.add(prereqid.get(i));
				}
			}
			Log.i("Final Prereq Id", "Final prereq id = " + FinalPreReqId);
			straarylist.add("levelschanged="+levelchanged+";chkschedselect="+chkschedselect+";lev="+lev+
						";oldatt="+oldatt+";att="+att+";isched="+isched+";comments="+comments+
						";wu_sscheduleid="+wu_sscheduleid+";wu_studentid="+wu_studentid+";wu_orderdetailid="+wu_orderdetailid+
						";wu_lessontypeid="+wu_lessontypeid+";wu_sttimehr="+wu_sttimehr+";wu_sttimemin="+wu_sttimemin+
						";wu_scheduledate="+wu_scheduledate+";ddlW="+newddlw+";ddlB="+newddlb+";ddlR="+newddlr+
						";wu_slevel="+oldlev+";ddlSchedLevel="+ddlSchedLevel+";wu_schedlevel="+wu_schedlevel);
			
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String str = straarylist.toString();
			str = str.replaceFirst("\\[", "");
			str = str.substring(0,str.lastIndexOf("]"));
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_Insert_Attandance);
			request.addProperty("token", WW_StaticClass.UserToken); //1
			request.addProperty("UserLevelStatus", WW_StaticClass.UserLevel); //2
			request.addProperty("wu_siteid", siteid.toString()); //3
			request.addProperty("_prereq", FinalPreReqId.toString()); //4
			request.addProperty("straarylist",str);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
					androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Insert_Attandance,
						envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.bodyIn;
				Log.i("Attendance apdater","Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
//				 Log.i("Attendance Adapter", "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
//				 Log.i("Attendance Adapter", "mSoapObject2="+mSoapObject2);
				 SoapObject mSoapObject3 = (SoapObject) mSoapObject2.getProperty(0);
				 String code = mSoapObject3.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if (code.equals("000")) {
					 attendance_response = true;
					 Object mSoapObject4 =  mSoapObject2
								.getProperty(1);
					
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
					}
					else{
						attendance_response = false;
					}
			}
			catch(JSONException e){
				server_response = true;
				e.printStackTrace();
			}
			catch(NullPointerException e){
				server_response = true;
				e.printStackTrace();
			}
			catch(Exception e){
				server_response = true;
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ViewCurrentLessonActivity.fl_vcl_loading.setVisibility(View.GONE);
			if(server_response){
				onDetectNetworkState().show();
			}
			else{
				if(attendance_response){
					String msg="";
					for (int i = 0; i < Msg_Status.size(); i++) {
						if(Msg_Status.get(i).equalsIgnoreCase("Failure")&&Msg_Str.get(i).equalsIgnoreCase("")){
							msg = msg +"\n"+Msg_Str.get(i);
						}
						else{
							msg = msg+"\n"+Msg_Str.get(i);
						}
					}
					AttendanceItems.btn_footer_send_attendance.setEnabled(false);
//					WW_StaticClass.attendance_sended = true;
					SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks", msg, "Ok");
				}
				else{
					SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
							"Some internal error. Please try again after sometime", "Ok");
				}
			}
		}
	}

	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        ((Activity) context).finish();
		    }
		})       
		.setPositiveButton("Οk",new DialogInterface.OnClickListener() {

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
	
	@SuppressWarnings("deprecation")
	public void showAlert(Context context,String Heading ,String message,
			String buttonText) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		// hide title bar
		// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.setTitle(Heading);
		alertDialog.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
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
	
	String reason="";
	String comment="";
	String SwimCompId="",StudentId="";
	private class SubmitReason extends AsyncTask<Void, Void, Void>{
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
					SOAP_CONSTANTS.METHOD_NAME_Insert_SwimCompCancellation);
			request.addProperty("token", WW_StaticClass.UserToken); //1
			request.addProperty("SwimCompId", SwimCompId); //2
			request.addProperty("StudentId", StudentId); //3
			request.addProperty("Reason", reason); //4
			request.addProperty("Comments", comment); //5
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
					androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Insert_SwimCompCancellation,
						envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.bodyIn;
				Log.i("Attendance apdater","Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 Log.i("Attendance Adapter", "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 Log.i("Attendance Adapter", "mSoapObject2="+mSoapObject2);
				 SoapObject mSoapObject3 = (SoapObject) mSoapObject2.getProperty(0);
				 String code = mSoapObject3.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if (code.equals("000")) {
					 cancel_response = true;
				 }
					else{
						cancel_response = false;
					}
			}
			catch(NullPointerException e){
				server_response = true;
				e.printStackTrace();
			}
			catch(Exception e){
				server_response = true;
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ViewCurrentLessonActivity.fl_vcl_loading.setVisibility(View.GONE);
			if(server_response){
				onDetectNetworkState().show();
			}
			else{
				if(cancel_response){
					Toast.makeText(context, "Add successfully..!!", 1).show();
				}
				else{
					Toast.makeText(context, "Not Add successfully..!!", 1).show();
				}
			}
		}
	}
}
