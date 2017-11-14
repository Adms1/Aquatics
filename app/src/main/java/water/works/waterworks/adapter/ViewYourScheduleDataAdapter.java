package water.works.waterworks.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import water.works.waterworks.ModifyComments;
import water.works.waterworks.MyTagHandler;
import water.works.waterworks.R;
import water.works.waterworks.ViewCurrentLessonActivity;
import water.works.waterworks.ViewYourScheduleActivity;
import water.works.waterworks.utils.WW_StaticClass;


public class ViewYourScheduleDataAdapter extends BaseAdapter implements AnimationListener{
	ArrayList<String> NewLevel = new ArrayList<String>();
	ArrayList<String> NewSchdLevel = new ArrayList<String>();
	Button submit;
	public ViewYourScheduleDataAdapter(ArrayList<String> StTimeHour,ArrayList<String> StTimeMin,
			ArrayList<String> FormateStTimeHour,
			ArrayList<String> FormatStTimeMin,ArrayList<String> siteID,
			ArrayList<String> sLevel,ArrayList<String> LevelID,
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
			ArrayList<String> SkillsCount,ArrayList<ArrayList<String>> Abbr,ArrayList<ArrayList<String>> PreReqChecked,
			ArrayList<ArrayList<String>> PreReqID,ArrayList<String> ISAAlert,ArrayList<String> ClsLvl,
			ArrayList<String> LvlAdvAvail,ArrayList<String> MainScheduleDate,
			ArrayList<String> StudentGender,ArrayList<Boolean> NewUser,Context context,Button submit,
			ArrayList<Integer> wu_attendancetaken) {
		super();
		this.StTimeHour=StTimeHour;
		this.StTimeMin=StTimeMin;
		this.FormateStTimeHour=FormateStTimeHour;
		this.FormatStTimeMin=FormatStTimeMin;
		SiteID = siteID;
		SLevel_ID = sLevel;
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
		this.ISAAlert =ISAAlert;
		this.ClsLvl=ClsLvl;
		this.LvlAdvAvail=LvlAdvAvail;
		this.MainScheduleDate=MainScheduleDate;
		this.NewUser = NewUser;
		this.StudentGender = StudentGender;
		this.context = context;
		this.submit = submit;
		this.wu_attendancetaken = wu_attendancetaken;
	}
	ArrayList<String> StTimeHour;
	ArrayList<String> StTimeMin;
	ArrayList<String> FormateStTimeHour;
	ArrayList<String> FormatStTimeMin;
	ArrayList<String> LevelID;
	ArrayList<String> LevelName;
	ArrayList<String> SiteID ;
	ArrayList<String> SLevel_ID ;
	ArrayList<String> wu_W ;
	ArrayList<String> ScheLevel ;

	ArrayList<String> SwimComp ;
	ArrayList<String> LessonName ;
	ArrayList<String> lessontypeid ;
	ArrayList<String> IScheduleID ;
	ArrayList<String> SAge ;
	ArrayList<String> ParentFirstName ;
	ArrayList<String> ParentLastName;
	ArrayList<String> BirthDay ;
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
	ArrayList<String> MainScheduleDate;
	ArrayList<String> StudentGender;
	ArrayList<Boolean> NewUser;
	ArrayList<Integer> wu_attendancetaken;
	Context context;
	ListView listView;
	CheckBox selectedcb;
	Animation animBlink;


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

	public class ViewHolder
	{
		TextView tv_sName,tv_pName,tv_sAge,tv_sPaid_cls,tv_sComments,tv_lec_time,tv_lesson_name;
		Button btn_sIsa,btn_sCls_lvl;
		ImageButton btn_sNote,btn_sCamera;
		ImageView iv_sLate;
		Button btn_sLevel,btn_sSch_leve;
		//        Button btn_s_r,btn_s_b,btn_s_w;
		ToggleButton sw_precent;
		ListPopupWindow listpopupwindow,listpopupwindow1,lpw_s_w,lpw_s_b,lpw_s_r;
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
				NewLevel = SLevel_ID;
				NewSchdLevel = ScheLevel;
				if(wu_attendancetaken.get(position)==0){
					holder.btn_sLevel.setEnabled(true);
					holder.btn_sSch_leve.setEnabled(true);
					holder.btn_sNote.setEnabled(true);
					holder.sw_precent.setEnabled(true);
				}
				else{
					holder.btn_sLevel.setEnabled(false);
					holder.btn_sSch_leve.setEnabled(false);
					holder.btn_sNote.setEnabled(false);
					holder.sw_precent.setEnabled(false);
				}
				convertView.setOnClickListener(new OnClickListener() {


					public void onClick(View v) {
						// TODO Auto-generated method stub
						((ViewYourScheduleActivity)context).t.interrupt();
						Intent i = new Intent(context, ViewCurrentLessonActivity.class);
						i.putExtra("HOUR", StTimeHour.get(position));
						i.putExtra("MIN", StTimeMin.get(position));
						i.putExtra("DATE", MainScheduleDate.get(position));
						WW_StaticClass.date_for_data = MainScheduleDate.get(position);
						WW_StaticClass.hour_for_data = StTimeHour.get(position);
						WW_StaticClass.min_for_data = StTimeMin.get(position);
						i.putExtra("DELETE", "NO");
						v.getContext().startActivity(i);
					}
				});
				holder.sw_precent.setChecked(true);
				////WBR SHOW///

				/*if(ShowWBR.get(position).equalsIgnoreCase("true")){
	            	holder.btn_s_b.setVisibility(View.VISIBLE);
	            	holder.btn_s_r.setVisibility(View.VISIBLE);
	            	holder.btn_s_w.setVisibility(View.VISIBLE);
	            	holder.lpw_s_w = new ListPopupWindow(context.getApplicationContext());
	                  holder.btn_s_w.setOnClickListener(new OnClickListener() {

	      				@Override
	      				public void onClick(View v) {
	      					// TODO Auto-generated method stub
	      					holder.lpw_s_w.show();
	      				}
	      			});
	                  final String[] wu_w = context.getResources().getStringArray(R.array.wu_w);
	                  holder.lpw_s_w.setAdapter(new ArrayAdapter<String>(
	          	            context.getApplicationContext(),
	          	            R.layout.edittextpopup,wu_w));
	          		holder.lpw_s_w.setAnchorView(holder.btn_s_w);
	          		holder.lpw_s_w.setHeight(LayoutParams.WRAP_CONTENT);
	          		holder.lpw_s_w.setModal(true);
	          		holder.lpw_s_w.setOnItemClickListener(
	                      new OnItemClickListener() {

	          				@Override
	          				public void onItemClick(AdapterView<?> parent, View view,
	          						int pos, long id) {
	          					// TODO Auto-generated method stub
	          					holder.btn_s_w.setText(wu_w[pos]);
	          					holder.lpw_s_w.dismiss();
	          				}
	          			});

	          	  holder.lpw_s_b = new ListPopupWindow(context.getApplicationContext());
	              holder.btn_s_b.setOnClickListener(new OnClickListener() {

	  				@Override
	  				public void onClick(View v) {
	  					// TODO Auto-generated method stub
	  					holder.lpw_s_b.show();
	  				}
	  			});
	              final String[] wu_b = context.getResources().getStringArray(R.array.wu_b);
	              holder.lpw_s_b.setAdapter(new ArrayAdapter<String>(
	      	            context.getApplicationContext(),
	      	            R.layout.edittextpopup,wu_b));
	      		holder.lpw_s_b.setAnchorView(holder.btn_s_b);
	      		holder.lpw_s_b.setHeight(LayoutParams.WRAP_CONTENT);
	      		holder.lpw_s_b.setModal(true);
	      		holder.lpw_s_b.setOnItemClickListener(
	                  new OnItemClickListener() {

	      				@Override
	      				public void onItemClick(AdapterView<?> parent, View view,
	      						int pos, long id) {
	      					// TODO Auto-generated method stub
	      					holder.btn_s_b.setText(wu_b[pos]);
	      					holder.lpw_s_b.dismiss();
	      				}
	      			});

	      	  holder.lpw_s_r = new ListPopupWindow(context.getApplicationContext());
	          holder.btn_s_r.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						holder.lpw_s_r.show();
					}
				});
	          final String[] wu_r = context.getResources().getStringArray(R.array.wu_r);
	          holder.lpw_s_r.setAdapter(new ArrayAdapter<String>(
	  	            context.getApplicationContext(),
	  	            R.layout.edittextpopup,wu_r));
	  		holder.lpw_s_r.setAnchorView(holder.btn_s_r);
	  		holder.lpw_s_r.setHeight(LayoutParams.WRAP_CONTENT);
	  		holder.lpw_s_r.setModal(true);
	  		holder.lpw_s_r.setOnItemClickListener(
	              new OnItemClickListener() {

	  				@Override
	  				public void onItemClick(AdapterView<?> parent, View view,
	  						int pos, long id) {
	  					// TODO Auto-generated method stub
	  					holder.btn_s_r.setText(wu_r[pos]);
	  					holder.lpw_s_r.dismiss();
	  				}
	  			});


	            }
	            else{
	            	holder.btn_s_b.setVisibility(View.INVISIBLE);
	            	holder.btn_s_r.setVisibility(View.INVISIBLE);
	            	holder.btn_s_w.setVisibility(View.INVISIBLE);
	            }
				 */


				//CREATE NOTE(COMMENTS)//
				holder.btn_sNote.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						WW_StaticClass.Studentid = StudentID.get(position);
						((Activity) context).finish();
						Intent it = new Intent(v.getContext(), ModifyComments.class);
						it.putExtra("FROM", "TODAY");
						it.putExtra("yes_no_date", "");
						v.getContext().startActivity(it);

					}
				});

				//ISA//
				animBlink = AnimationUtils.loadAnimation(context.getApplicationContext(),
						R.anim.blink);
				animBlink.setAnimationListener(this);
				if(ISAAlert.get(position).equalsIgnoreCase("true")){
					holder.btn_sIsa.setVisibility(View.VISIBLE);
					holder.btn_sIsa.startAnimation(animBlink);
				}
				else{
					holder.btn_sIsa.setVisibility(View.INVISIBLE);
				}
				// Level and schedule level
				String levelname = LevelName.get(LevelID.indexOf(SLevel_ID.get(position)));
				String schlevelname = LevelName.get(LevelID.indexOf(ScheLevel.get(position)));
				holder.btn_sLevel.setText(levelname);
				holder.btn_sSch_leve.setText(schlevelname);
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
				holder.listpopupwindow.setHeight(300);
				holder.listpopupwindow.setModal(true);
				holder.listpopupwindow.setOnItemClickListener(
						new OnItemClickListener() {


							public void onItemClick(AdapterView<?> parent, View view,
									int pos, long id) {
								// TODO Auto-generated method stub
								holder.btn_sLevel.setText(LevelName.get(pos));
								holder.btn_sSch_leve.setText(LevelName.get(pos));
								NewLevel.remove(position);
								NewLevel.add(position,LevelID.get(pos));
								NewSchdLevel.remove(position);
								NewSchdLevel.add(position,LevelID.get(pos));
								holder.listpopupwindow.dismiss();

							}
						});
				holder.btn_sSch_leve.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						holder.listpopupwindow1.show();
					}
				});
				holder.listpopupwindow1.setAdapter(new ArrayAdapter<String>(
						context,
						R.layout.edittextpopup,LevelName ));
				holder.listpopupwindow1.setAnchorView(holder.btn_sSch_leve);
				holder.listpopupwindow1.setHeight(300);
				holder.listpopupwindow1.setModal(true);
				holder.listpopupwindow1.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> parent, View view,
							int pos, long id) {
						// TODO Auto-generated method stub
						holder.btn_sSch_leve.setText(LevelName.get(pos));
						NewSchdLevel.remove(position);
						NewSchdLevel.add(position,LevelID.get(pos));
						holder.listpopupwindow1.dismiss();

					}
				});
				//Lec time//
				int hr = Integer.parseInt(StTimeHour.get(position));
				int min = Integer.parseInt(StTimeMin.get(position));
				String am_pm;
				if(hr>=12&&min>=00){

					am_pm = "PM";
				}
				else{
					am_pm = "AM";
				}
				holder.tv_lec_time.setText(Html.fromHtml("<small>"+FormateStTimeHour.get(position)+":"+FormatStTimeMin.get(position)+am_pm+"</small>"));

				//new student//
				Boolean newstudent = NewUser.get(position);
				Log.i("here", "New student = " +newstudent);
				String comments = Comments.get(position);
				if(newstudent==true)
				{
					String next = "<font color='#EE0000'>New Student</font>";
					holder.tv_sComments.setText(Html.fromHtml(next+comments, null, new MyTagHandler()));
				}
				else{
					//Comment//
					//Using MyTagHandler class for generating list//

					if(comments.isEmpty()){
						holder.tv_sComments.setText("");
					}
					else{
						holder.tv_sComments.setText(Html.fromHtml(comments, null, new MyTagHandler()));
					}
				}



				//name//
				String gender = StudentGender.get(position).trim();
				String sname = SFirstName.get(position)+" "+SLastName.get(position);
				String fname = "("+ParentFirstName.get(position)+" "+ParentLastName.get(position)+")";
				if(gender.toString().equalsIgnoreCase("Female")){
					holder.tv_sName.setTextColor(Color.rgb(136, 0, 183));
					holder.tv_sName.setText(sname);

				}
				else{
					holder.tv_sName.setTextColor(Color.rgb(0, 0, 102)); 
					holder.tv_sName.setText(sname);

				}
				holder.tv_pName.setText(fname);

				//age//
				holder.tv_sAge.setText(SAge.get(position));

				//CLS(Lesson name)//
				holder.tv_lesson_name.setText(LessonName.get(position));

				//cls/lvl//
				holder.btn_sCls_lvl.setText(ClsLvl.get(position));

				///Paid class/////
				String paidcls = PaidClasses.get(position);
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

				// LATE or NOT//
				String lvlavail = LvlAdvAvail.get(position);
				holder.iv_sLate.setVisibility(View.INVISIBLE);
				if(Integer.parseInt(lvlavail)>1){
					holder.iv_sLate.setVisibility(View.VISIBLE);
				}
				else{
					holder.iv_sLate.setVisibility(View.INVISIBLE);
				}
				submit.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "Click", Toast.LENGTH_LONG).show();
						Log.i("Adapter", "new level = " + NewLevel);
						Log.i("Adapter", "new schlevel = " + NewSchdLevel);
					}
				});
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
}
