package water.works.waterworks.adapter;

import android.annotation.SuppressLint;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
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

import java.util.ArrayList;

import water.works.waterworks.ModifyComments;
import water.works.waterworks.MyTagHandler;
import water.works.waterworks.R;
import water.works.waterworks.ViewCurrentLessonActivity;
import water.works.waterworks.ViewYourScheduleActivity;
import water.works.waterworks.model.TodaysScheduleItems;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class TodaysScheduleAdapter extends BaseAdapter implements AnimationListener{
	ArrayList<TodaysScheduleItems> tsItem;
	Animation animBlink;
	ArrayList<Boolean> ischange;
	ArrayList<String> newslevel,newschdlevel;
	ArrayList<Integer>newatt;
	Context context;
	public TodaysScheduleAdapter(ArrayList<TodaysScheduleItems> tsItem,
			Context context) {
		super();
		this.tsItem = tsItem;
		this.context = context;
	}
	

	public int getCount() {
		// TODO Auto-generated method stub
		return tsItem.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tsItem.get(position);
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
        TextView tv_sName,tv_pName,tv_sAge,tv_sPaid_cls,tv_sComments,tv_lec_time,tv_lesson_name;
        Button btn_sIsa,btn_sCls_lvl;
        ImageButton btn_sNote,btn_sCamera;
        ImageView iv_sLate;
        Button btn_sLevel,btn_sSch_leve;
//        Button btn_s_r,btn_s_b,btn_s_w;
        ToggleButton sw_precent;
        ListPopupWindow listpopupwindow,listpopupwindow1;
//        lpw_s_w,lpw_s_b,lpw_s_r;
        int oldlevel,newlevel;
        ImageButton level_add,level_sub,sch_level_add,sch_level_sub;
    }
	private int[] colors = new int[] { Color.parseColor("#EEEEEE"), Color.parseColor("#FFFFFF") };
	@SuppressLint("ShowToast")

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		try{
			if (convertView == null) {
				holder = new ViewHolder();
				

				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.todays_schedule_row, null);
				int colorpos=position%colors.length;
				convertView.setBackgroundColor(colors[colorpos]);
				holder.tv_lesson_name = (TextView)convertView.findViewById(R.id.tv_ts_level_name_row);
				holder.tv_lec_time = (TextView)convertView.findViewById(R.id.tv_ts_time_row);
             holder.btn_sIsa = (Button)convertView.findViewById(R.id.btn_isa_alert);
             holder.tv_sName = (TextView)convertView.findViewById(R.id.tv_ts_studentname_row);
             holder.tv_pName = (TextView)convertView.findViewById(R.id.tv_ts_parentname_row);
             holder.btn_sCamera = (ImageButton)convertView.findViewById(R.id.btn_camera);
             holder.tv_sAge = (TextView)convertView.findViewById(R.id.tv_ts_age_row);
             holder.btn_sLevel = (Button)convertView.findViewById(R.id.btn_ts_slevel_row);
             holder.btn_sSch_leve = (Button)convertView.findViewById(R.id.btn_ts_sched_level_row);
             holder.sw_precent = (ToggleButton)convertView.findViewById(R.id.btn_ts_yes_no);
             holder.btn_sCls_lvl = (Button)convertView.findViewById(R.id.btn_ts_cls_lvl_row);
             holder.tv_sPaid_cls = (TextView)convertView.findViewById(R.id.tv_ts_paid_cls_row);
//             holder.btn_s_w = (Button)convertView.findViewById(R.id.chb_ts_wbr_w);
//             holder.btn_s_b = (Button)convertView.findViewById(R.id.chb_ts_wbr_b);
//             holder.btn_s_r = (Button)convertView.findViewById(R.id.chb_ts_wbr_r);
             holder.tv_sComments = (TextView)convertView.findViewById(R.id.tv_ts_comment_row);
             holder.btn_sNote = (ImageButton)convertView.findViewById(R.id.btn_ts_note_row);
             holder.iv_sLate = (ImageView)convertView.findViewById(R.id.img_ts_late);
             holder.listpopupwindow = new ListPopupWindow(context.getApplicationContext());
             holder.listpopupwindow1 = new ListPopupWindow(context.getApplicationContext());
             holder.sch_level_add = (ImageButton)convertView.findViewById(R.id.ib_ts_plus_sch_level);
             holder.sch_level_sub = (ImageButton)convertView.findViewById(R.id.ib_ts_sub_sch_level);
             holder.level_add = (ImageButton)convertView.findViewById(R.id.ib_ts_plus_level);
             holder.level_sub = (ImageButton)convertView.findViewById(R.id.ib_ts_sub_level);
            
             ischange = new ArrayList<Boolean>();
 			newslevel = new ArrayList<String>();
 			newschdlevel = new ArrayList<String>();
 			newatt = new ArrayList<Integer>();
 			for(int i=0;i<tsItem.size();i++){
				ischange.add(i,false);
				newslevel.add(i,tsItem.get(i).getsLevel());
				newschdlevel.add(i,tsItem.get(i).getScheLevel());
				newatt.add(i,0);
			}
 			
 			animBlink = AnimationUtils.loadAnimation(context.getApplicationContext(),
	                R.anim.blink);
			animBlink.setAnimationListener(this);
			int temp_att = tsItem.get(position).getAtt();
			if(tsItem.get(position).getWu_attendancetaken()==0){
				holder.btn_sLevel.setEnabled(true);
				holder.btn_sSch_leve.setEnabled(true);
				holder.btn_sNote.setEnabled(true);
				holder.sw_precent.setEnabled(true);
				holder.sw_precent.setChecked(true);
				newatt.remove(position);
				newatt.add(position,0);
			}
			else{
				if(temp_att==2||temp_att==3||temp_att==4||temp_att==5||temp_att==6||temp_att==7||temp_att==8||temp_att==12||temp_att==13||temp_att==15||temp_att==16||temp_att==17){
					holder.btn_sLevel.setEnabled(false);
					holder.btn_sSch_leve.setEnabled(false);
					holder.btn_sNote.setEnabled(false);
					holder.sw_precent.setEnabled(false);
					holder.sw_precent.setChecked(false);
					newatt.remove(position);
					newatt.add(position,1);
				}
				else{
					holder.sw_precent.setChecked(true);
					holder.sw_precent.setEnabled(true);
					holder.btn_sLevel.setEnabled(true);
					holder.btn_sSch_leve.setEnabled(true);
					holder.btn_sNote.setEnabled(true);
					newatt.remove(position);
					newatt.add(position,0);
				}
					
				/*if(temp_att==0){
					holder.sw_precent.setChecked(true);
					holder.sw_precent.setEnabled(true);
					holder.btn_sLevel.setEnabled(true);
					holder.btn_sSch_leve.setEnabled(true);
					holder.btn_sNote.setEnabled(true);
					newatt.remove(position);
					newatt.add(position,0);
				}
				else{
					holder.btn_sLevel.setEnabled(false);
					holder.btn_sSch_leve.setEnabled(false);
					holder.btn_sNote.setEnabled(false);
					holder.sw_precent.setEnabled(false);
					holder.sw_precent.setChecked(false);
					newatt.remove(position);
					newatt.add(position,1);
				}*/
			}
			//////////// sw precent click///////////////
			
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
			
			///////////////////////////////////////////
			//convert view click //////////////
			convertView.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(context, ViewCurrentLessonActivity.class);
					i.putExtra("HOUR", tsItem.get(position).getStTimeHour());
					i.putExtra("MIN", tsItem.get(position).getStTimeMin());
					i.putExtra("DATE", tsItem.get(position).getMainScheduleDate());
					WW_StaticClass.date_for_data = tsItem.get(position).getMainScheduleDate();
					WW_StaticClass.hour_for_data = tsItem.get(position).getStTimeHour();
					WW_StaticClass.min_for_data = tsItem.get(position).getStTimeMin();
					i.putExtra("DELETE", "NO");
					v.getContext().startActivity(i);
				}
			});
			
			///////////////////////////////////
 			////isa and L and cls/lvl ////////
 			
 			if(tsItem.get(position).getISAAlert().equalsIgnoreCase("true")){
            	holder.btn_sIsa.setVisibility(View.VISIBLE);
            	holder.btn_sIsa.startAnimation(animBlink);
            }
            else{
            	holder.btn_sIsa.setVisibility(View.INVISIBLE);
            }
            /// For cls lvl /////////
            String cls_lvl = tsItem.get(position).getClsLvl();
            holder.btn_sCls_lvl.setText(cls_lvl);
            /// L ///
            String lvlavail = tsItem.get(position).getLvlAdvAvail();
            if(Integer.parseInt(lvlavail)>1){
            	holder.iv_sLate.setVisibility(View.VISIBLE);
            }
            else{
            	holder.iv_sLate.setVisibility(View.INVISIBLE);
            }
 			
 			////////////////////////
            //CLS (Lesson name)////
            	holder.tv_lesson_name.setText(tsItem.get(position).getLessonName());
            //////
            	//Lec time//
	            int hr = Integer.parseInt(tsItem.get(position).getStTimeHour());
	            int min = Integer.parseInt(tsItem.get(position).getStTimeMin());
	            String am_pm;
	            if(hr>=12&&min>=00){
	            	
	            	am_pm = "PM";
	            }
	            else{
	            	am_pm = "AM";
	            }
			holder.tv_lec_time.setText(Html.fromHtml("<small>"+tsItem.get(position).getFormateStTimeHour()+":"+tsItem.get(position).getFormatStTimeMin()+am_pm+"</small>"));
            	
            	
            	///////////////////////////
             /// Name and Age /////
             String gender = tsItem.get(position).getStudentGender().trim();
 			Log.i("Attendance adapter","Gender = "+gender);
 			
 			String sname = tsItem.get(position).getsFirstName()+" "+tsItem.get(position).getsLastName();
 			String fname = "("+tsItem.get(position).getParentFirstName()+" "+tsItem.get(position).getParentLastName()+")";
 			if(gender.toString().equalsIgnoreCase("Female")){
 				holder.tv_sName.setTextColor(Color.rgb(136, 0, 183));
 				 holder.tv_sName.setText(sname);
 				 
 			}
 			else{
 				holder.tv_sName.setTextColor(Color.rgb(0, 0, 102)); 
 				holder.tv_sName.setText(sname);
 				 
 			}
 			holder.tv_pName.setText(fname);
 			
 			holder.tv_sAge.setText(tsItem.get(position).getsAge());
 			////////////////
 			
 			/////////Level and Schedule Level/////////////
 			
 			holder.oldlevel = Integer.parseInt(tsItem.get(position).getsLevel());
            holder.newlevel = Integer.parseInt(tsItem.get(position).getsLevel());
            final ArrayList<String> LevelName = new ArrayList<String>();
            final ArrayList<String> LevelID = new ArrayList<String>();
            LevelName.addAll(tsItem.get(position).getLevelName());
            LevelID.addAll(tsItem.get(position).getLevelID());
            String levelname = LevelName.get(LevelID.indexOf(tsItem.get(position).getsLevel()));
            String schlevelname = LevelName.get(LevelID.indexOf(tsItem.get(position).getsLevel()));
            if(levelname.length()==1){
            	levelname = "0"+levelname;
			}
            if(schlevelname.length()==1){
            	schlevelname = "0"+schlevelname;
			}
             holder.btn_sLevel.setText(levelname);
             holder.btn_sSch_leve.setText(schlevelname);
             
             holder.level_add.setOnClickListener(new OnClickListener() {
				
				@SuppressWarnings("deprecation")

				public void onClick(View v) {
					// TODO Auto-generated method stub
					int size = LevelName.size();
					String temp_level =holder.btn_sLevel.getText().toString();
					final String templevel,tempschdlevel;
					templevel = holder.btn_sLevel.getText().toString();
					tempschdlevel = holder.btn_sSch_leve.getText().toString();
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
						holder.btn_sLevel.setText(lname);
						holder.btn_sSch_leve.setText(lname);
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
										holder.btn_sLevel.setText(templevel);
										holder.btn_sSch_leve.setText(tempschdlevel);
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
					String temp_level =holder.btn_sLevel.getText().toString();
					final String templevel,tempschdlevel;
					templevel = holder.btn_sLevel.getText().toString();
					tempschdlevel = holder.btn_sSch_leve.getText().toString();
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
						holder.btn_sLevel.setText(lname);
						holder.btn_sSch_leve.setText(lname);
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
										holder.btn_sLevel.setText(templevel);
										holder.btn_sSch_leve.setText(tempschdlevel);
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
					String temp_level =holder.btn_sSch_leve.getText().toString();
					final String templevel = holder.btn_sSch_leve.getText().toString();
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
						holder.btn_sSch_leve.setText(lname);
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
										holder.btn_sSch_leve.setText(templevel);
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
					String temp_level =holder.btn_sSch_leve.getText().toString();
					final String templevel = holder.btn_sSch_leve.getText().toString();
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
						holder.btn_sSch_leve.setText(lname);
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
										holder.btn_sSch_leve.setText(templevel);
										dialog.cancel();
									}
								});
						// show the alert dialog
						alertDialog.show();
					}

				}
			});

             holder.btn_sSch_leve.setOnClickListener(new OnClickListener() {
 				

 				public void onClick(View v) {
 					// TODO Auto-generated method stub
 					holder.listpopupwindow1.show();
 				}
 			});

             holder.listpopupwindow = new ListPopupWindow(context.getApplicationContext());
     		holder.btn_sLevel.setOnClickListener(new OnClickListener() {

     			public void onClick(View v) {
     				// TODO Auto-generated method stub
     				holder.listpopupwindow.show();
     			}
     		});
     		holder.listpopupwindow.setAdapter(new ArrayAdapter<String>(
     	            context.getApplicationContext(),
     	            R.layout.edittextpopup,LevelName ));
     		holder.listpopupwindow.setAnchorView(holder.btn_sLevel);
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
     					level = holder.btn_sLevel.getText().toString();
     					schdlevel = holder.btn_sSch_leve.getText().toString();
     					schlevelname = LevelName.get(pos);
     					levelname = LevelName.get(pos);
     					if(levelname.length()==1){
     		            	levelname = "0"+levelname;
     					}
     		            if(schlevelname.length()==1){
     		            	schlevelname = "0"+schlevelname;
     					}
     					holder.btn_sLevel.setText(levelname);
     					holder.btn_sSch_leve.setText(schlevelname);
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
     									holder.btn_sLevel.setText(level);
     									holder.btn_sSch_leve.setText(schdlevel);
     								}
     							});
     					// show the alert dialog
     					alertDialog.show();
     				}
     			});

             
             holder.listpopupwindow1.setAdapter(new ArrayAdapter<String>(
 		            context.getApplicationContext(),
 		            R.layout.edittextpopup,LevelName ));
             holder.listpopupwindow1.setAnchorView(holder.btn_sSch_leve);
             holder.listpopupwindow1.setHeight(LayoutParams.WRAP_CONTENT);
             holder.listpopupwindow1.setModal(true);
             holder.listpopupwindow1.setOnItemClickListener(new OnItemClickListener() {

 				@SuppressWarnings("deprecation")

 				public void onItemClick(AdapterView<?> parent, View view,
 						final int pos, long id) {
 					// TODO Auto-generated method stub
 					 String schlevelname = "";
 					schlevelname = LevelName.get(pos);
 					final String schdlevel= holder.btn_sSch_leve.getText().toString();
 					if(schlevelname.toString().length()==1){
 						schlevelname = "0"+schlevelname;
 					}
 					holder.btn_sSch_leve.setText(schlevelname);
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
// 							holder.listpopupwindow1.dismiss();
 						}
 					});
 					alertDialog.setButton2("No",
 							new DialogInterface.OnClickListener() {

 								public void onClick(DialogInterface dialog, int which) {
 									// TODO Auto-generated method stub
 									dialog.cancel();
 									holder.btn_sSch_leve.setText(schdlevel);
 								}
 							});
 					// show the alert dialog
 					alertDialog.show();
 				}
 			});

 			
 			//////////////////////////////////////////////
             
           ///Paid class/////
             String paidcls = tsItem.get(position).getPaidClasses();
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
             
             //////////////////////////////
             
           //new student//
             Boolean newstudent = tsItem.get(position).getNewUser();
             Log.i("here", "New student = " +newstudent);
             if(newstudent==true)
             {
             	String next = "<font color='#EE0000'>New Student</font>";
             	holder.tv_sComments.setText(Html.fromHtml(next+tsItem.get(position).getComments().toString(), null, new MyTagHandler()));
             }
             else{
            //Comment//
              //Using MyTagHandler class for generating list//
              holder.tv_sComments.setText(Html.fromHtml(tsItem.get(position).getComments().toString(), null, new MyTagHandler()));
             }
             ///////////
             
             ///New comment///
             holder.btn_sNote.setOnClickListener(new OnClickListener() {
 				

 				public void onClick(View v) {
 					// TODO Auto-generated method stub
 					WW_StaticClass.Studentid = tsItem.get(position).getStudentID();
 					((Activity) context).finish();
 					Intent it = new Intent(v.getContext(), ModifyComments.class);
 					it.putExtra("FROM", "TODAY");
 					it.putExtra("yes_no_date", "");
 					v.getContext().startActivity(it);

 				}
 			});
             ////////////////////////
             
             
             ///Insert att////
             
             TodaysScheduleItems.submit.setOnClickListener(new OnClickListener() {
				

				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(Utility.isNetworkConnected(context)){
						straarylist=new ArrayList<String>();
						levelchanged = new ArrayList<Character>();
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
						wu_slevel=new ArrayList<String>();
						ddlSchedLevel = new ArrayList<String>();
						wu_schedlevel = new ArrayList<String>();
						oldlev = new ArrayList<String>();
						lev = new ArrayList<String>();
						att = new ArrayList<String>();
						chkschedselect = new ArrayList<String>();
						siteid = tsItem.get(0).getSiteID();
						int newpos;
						for(int i=0;i<tsItem.size();i++){
							newpos = i;
							if(ischange.get(newpos).equals(true)){
								levelchanged.add('Y');
							}
							else{
								levelchanged.add('N');
							}
							oldlev.add(tsItem.get(newpos).getsLevel());
							lev.add(newslevel.get(newpos));
							ddlSchedLevel.add(tsItem.get(newpos).getScheLevel());
							wu_schedlevel.add(newschdlevel.get(newpos));
							att.add(""+newatt.get(newpos));
							oldatt.add("0");
							chkschedselect.add("true");
							isched.add(tsItem.get(newpos).getiScheduleID());
							comments.add(tsItem.get(newpos).getWu_Comments());
							wu_sscheduleid.add(tsItem.get(newpos).getsScheduleID());
							wu_studentid.add(tsItem.get(newpos).getStudentID());
							wu_orderdetailid.add(tsItem.get(newpos).getOrderDetailID());
							wu_lessontypeid.add(tsItem.get(newpos).getLessontypeid());
							wu_sttimehr.add(tsItem.get(newpos).getStTimeHour());
							wu_sttimemin.add(tsItem.get(newpos).getStTimeMin());
							wu_scheduledate.add(tsItem.get(newpos).getMainScheduleDate());
							wu_slevel.add(tsItem.get(newpos).getsLevel());
							String match = tsItem.get(newpos).getsScheduleID();
//							Log.i("Here", ""+newprereqid);
//							for(int q=0;q<newprereqid.size();q++){
//								if(newprereqid.get(q).contains(match)){
//									prereqid.add(newprereqid.get(q));
//								}
//								else{}
//							}
						}
						new Insert_Attandance().execute();
					}
					else{
						onDetectNetworkState().show();
					}
				}
			});
             
             /////////
             
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
	             return convertView;
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
		
		//////////////////////////////Insert /////////////////////////////////////////
		
		String siteid;
		ArrayList<String> lev,oldlev,att;
		ArrayList<Character> levelchanged;
		ArrayList<String> oldatt,isched,comments,wu_sscheduleid,wu_studentid,wu_orderdetailid,
		wu_lessontypeid,wu_sttimehr,wu_sttimemin,wu_scheduledate,ddlW,ddlB,ddlR,wu_siteid,wu_slevel,
		ddlSchedLevel,wu_schedlevel,chkschedselect,straarylist,Msg_Status,Msg_Str;
		boolean attendance_response=false,server_response=false,cancel_response=false;
		
		private class Insert_Attandance extends AsyncTask<Void, Void, Void>{
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				ViewYourScheduleActivity.fl_ts_loading.setVisibility(View.GONE);
				
				
				straarylist.add("levelschanged="+levelchanged+";chkschedselect="+chkschedselect+";lev="+lev+
							";oldatt="+oldatt+";att="+att+";isched="+isched+";comments="+comments+
							";wu_sscheduleid="+wu_sscheduleid+";wu_studentid="+wu_studentid+";wu_orderdetailid="+wu_orderdetailid+
							";wu_lessontypeid="+wu_lessontypeid+";wu_sttimehr="+wu_sttimehr+";wu_sttimemin="+wu_sttimemin+
							";wu_scheduledate="+wu_scheduledate+
							";oldlev="+oldlev+";ddlSchedLevel="+ddlSchedLevel+";wu_schedlevel="+wu_schedlevel);
				
			}
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String str = straarylist.toString();
				str = str.replaceFirst("\\[", "");
				str = str.substring(0,str.lastIndexOf("]"));
				SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
						SOAP_CONSTANTS.METHOD_NAME_Insert_Attandance_ForToday);
				request.addProperty("token", WW_StaticClass.UserToken); //1
				request.addProperty("UserLevelStatus", WW_StaticClass.UserLevel); //2
				request.addProperty("wu_siteid", siteid.toString()); //3
//				request.addProperty("_prereq", FinalPreReqId.toString()); //4
				request.addProperty("straarylist",str);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11); // Make an Envelop for sending as whole
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				Log.i("Request",  "Request = " + request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						SOAP_CONSTANTS.SOAP_ADDRESS);
				try {
						androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Insert_Attandance_ForToday,
							envelope); // Calling Web service
					SoapObject response = (SoapObject) envelope.bodyIn;
					Log.i("Attendance apdater","Result : " + response.toString());
					SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
//					 Log.i("Attendance Adapter", "mSoapObject1="+mSoapObject1);
					 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
//					 Log.i("Attendance Adapter", "mSoapObject2="+mSoapObject2);
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
				ViewYourScheduleActivity.fl_ts_loading.setVisibility(View.GONE);
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
						SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks", msg, "Ok");
					}
					else{
						SingleOptionAlertWithoutTitle.ShowAlertDialog(context, "WaterWorks",
								"Some internal error. Please try again after sometime", "Ok");
					}
				}
			}
		}
		
		//////////////////////////////////////////////////////////////////////////////
	}
