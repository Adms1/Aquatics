package water.works.waterworks.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.util.Calendar;
import java.util.List;

import water.works.waterworks.R;
import water.works.waterworks.ShadowLogsActivity;
import water.works.waterworks.TodaysSchedule_DeckSupervisorActivity;
import water.works.waterworks.ViewScheduleDeckSupervisorActivity;
import water.works.waterworks.model.ViewScheduleRow;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.WW_StaticClass;


public class ViewScheduleDeckSupervisorAdapter extends ArrayAdapter<ViewScheduleRow> {

	List<ViewScheduleRow> data;
	Context context;
	int layoutResID;
	LayoutInflater inflater;
	int count=0;
	String ischeduleid,starttime,endtime,ManagerShadowID,Msg;
	boolean start_time= false,end_time=false,server_response = false;
	View MyTextView;
public ViewScheduleDeckSupervisorAdapter(Context context, int layoutResourceId,List<ViewScheduleRow> data) {
	super(context, layoutResourceId, data);
	
	this.data=data;
	this.context=context;
	this.layoutResID=layoutResourceId;
	inflater = LayoutInflater.from(this.context);

	// TODO Auto-generated constructor stub
}




	private int[] colors = new int[] { Color.parseColor("#EEEEEE"), Color.parseColor("#FFFFFF") };
	public boolean connctionout = false;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	final ViewHolder holder;
	try{
	   if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					layoutResID, null);
//            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
//            row = inflater.inflate(layoutResID, parent, false);
            int colorpos=position%colors.length;
            final ViewScheduleRow itemdata = data.get(position);
            if(itemdata.getFROMWHERE().toString().equalsIgnoreCase("MANAGER")){
            	holder.ll_main = (LinearLayout)convertView.findViewById(R.id.front);
                holder.ll_main.setBackgroundColor(colors[colorpos]);
                holder.tv_instructor = (TextView)convertView.findViewById(R.id.tv_mvc_intructor_name);
                holder.tv_start = (TextView)convertView.findViewById(R.id.tv_shadow_start);
                holder.tv_stop = (TextView)convertView.findViewById(R.id.tv_shadow_stop);
                holder.start = (Button)convertView.findViewById(R.id.btn_start_shadow);
                holder.stop = (Button)convertView.findViewById(R.id.btn_stop_shadow);
                holder.ll_students = (LinearLayout)convertView.findViewById(R.id.ll_view_schedule_students);
                holder.tv_view_schedule_students = (TextView)convertView.findViewById(R.id.tv_view_schedule_students);
                holder.table_mvcs_shadow = (TableLayout)convertView.findViewById(R.id.table_mvcs_shadow);
                convertView.setTag(holder);
            }else if(itemdata.getFROMWHERE().toString().equalsIgnoreCase("DECK")){
			holder.ll_main = (LinearLayout)convertView.findViewById(R.id.front);
            holder.ll_main.setBackgroundColor(colors[colorpos]);
           holder.ll_students = (LinearLayout)convertView.findViewById(R.id.ll_view_schedule_students);
           holder.tv_view_schedule = (TextView)convertView.findViewById(R.id.tv_vsi_view_today_schedule);
           holder.tv_view_schedule_students = (TextView)convertView.findViewById(R.id.tv_view_schedule_students);
           convertView.setTag(holder);
        }
	   }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
	   final ViewScheduleRow itemdata = data.get(position);
	   if(itemdata.getFROMWHERE().toString().equalsIgnoreCase("DECK")){
	   String am_pm;
       if(Integer.parseInt(itemdata.getStTimeHour())>11)
       {
       	am_pm="PM";
       }
       else{
       	am_pm="AM";
       }
       if(Integer.parseInt(itemdata.getFormateStTimeHour())==13){
       	itemdata.setFormateStTimeHour("01");
       }
       else if(Integer.parseInt(itemdata.getFormateStTimeHour())==14){
       	itemdata.setFormateStTimeHour("02");
       }
       else if(Integer.parseInt(itemdata.getFormateStTimeHour())==15){
       	itemdata.setFormateStTimeHour("03");
       }
       else if(Integer.parseInt(itemdata.getFormateStTimeHour())==16){
       	itemdata.setFormateStTimeHour("04");
       }
       else if(Integer.parseInt(itemdata.getFormateStTimeHour())==17){
       	itemdata.setFormateStTimeHour("05");
       }
       else if(Integer.parseInt(itemdata.getFormateStTimeHour())==18){
       	itemdata.setFormateStTimeHour("06");
       }
       else if(Integer.parseInt(itemdata.getFormateStTimeHour())==19){
       	itemdata.setFormateStTimeHour("07");
       }
       else if(Integer.parseInt(itemdata.getFormateStTimeHour())==20){
       	itemdata.setFormateStTimeHour("08");
       }
       else if(Integer.parseInt(itemdata.getFormateStTimeHour())==21){
       	itemdata.setFormateStTimeHour("09");
       }
       else if(Integer.parseInt(itemdata.getFormateStTimeHour())==22){
       	itemdata.setFormateStTimeHour("10");
       }
       else if(Integer.parseInt(itemdata.getFormateStTimeHour())==23){
       	itemdata.setFormateStTimeHour("11");
       }
       
//       holder.tv_time.setText(Html.fromHtml("<b>Time: "+itemdata.getMainScheduleDate()+" "+itemdata.getFormateStTimeHour()+":"+itemdata.getFormatStTimeMin()+" "+am_pm+"</b>"));
   	   holder.tv_view_schedule.setText(Html.fromHtml("<u><b><font color='#034AF3'>View Today's Schedule</font></b></u>"));
   	   holder.tv_view_schedule.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent viewschedule = new Intent(context, TodaysSchedule_DeckSupervisorActivity.class);
				WW_StaticClass.InstructorID = itemdata.getWu_InstructorID();
				WW_StaticClass.UserName = itemdata.getWu_InstructorName();
				context.startActivity(viewschedule);
			}
		});
       if(itemdata.getWu_avail()==0||itemdata.getWu_avail()==1){
       	holder.ll_students.setVisibility(View.GONE);
       	holder.tv_view_schedule.setVisibility(View.GONE);
       	holder.tv_view_schedule_students.setVisibility(View.VISIBLE);
       	holder.tv_view_schedule_students.setText(Html.fromHtml("No schedule at "+ itemdata.getFormateStTimeHour()+":"+itemdata.getFormatStTimeMin()+" "+am_pm)+" for "+itemdata.getWu_InstructorName());
       }
       else{
       	holder.ll_students.setVisibility(View.VISIBLE);
       	holder.tv_view_schedule_students.setVisibility(View.GONE);
       	holder.tv_view_schedule.setVisibility(View.VISIBLE);
       	holder.ll_students.removeAllViews();
//       	LinearLayout footerLayout = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.viewschedule_student_item_header,null);
//       	TextView tv_time = (TextView)footerLayout.findViewById(R.id.tv_vsi_time);
//       	tv_time.setText(Html.fromHtml("<b>Time:</b> "+ itemdata.getFormateStTimeHour()+":"+itemdata.getFormatStTimeMin()+" "+am_pm));
//       	holder.ll_students.addView(footerLayout, 0);
       	
       	for (int i = 0; i < itemdata.getStudentID().size(); i++) {
       		LayoutInflater minflater = LayoutInflater.from(this.context);
   			View childView = inflater.inflate(R.layout.viewschedule_student_item, null);
   			TextView tv_time = (TextView)childView.findViewById(R.id.tv_vsi_time);
   			tv_time.setText(Html.fromHtml(""+ itemdata.getFormateStTimeHour()+":"+itemdata.getFormatStTimeMin()+" "+am_pm));
   			TextView tvStudName = (TextView) childView
   					.findViewById(R.id.tv_vsi_stud_name);
   			TextView tvAge = (TextView) childView.findViewById(R.id.tv_vsi_age);
   			TextView tvlevel = (TextView) childView
   					.findViewById(R.id.tv_vsi_level);
   			TextView tvcomments = (TextView) childView
   					.findViewById(R.id.tv_vsi_comment);
   			
   			TextView tvinstructorname = (TextView) childView
   					.findViewById(R.id.tv_vsi_instructor_name);
   			
   			TextView tvlt = (TextView) childView
   					.findViewById(R.id.tv_vsi_lt);
   			
   			String m_status = itemdata.getMemStatus().get(i);
   			if(m_status.toString().equalsIgnoreCase("")){
   				m_status="";	
   			}
   			else{
   				m_status ="*"+m_status+"*";
   			}
   			
   			if(itemdata.getStudentGender().get(i).toString().trim().equalsIgnoreCase("Female")){
       			tvStudName.setText(Html.fromHtml("<font color='#8800B7'>"+itemdata.getSFirstName().get(i)+" "+itemdata.getSLastName().get(i)+"</font> <b>"+ m_status +"</b><br /> <small>("+itemdata.getParentFirstName().get(i)+" "+itemdata.getParentLastName().get(i)+")</small>"));

               }
               else{
        			tvStudName.setText(Html.fromHtml("<font color='#000066'>"+itemdata.getSFirstName().get(i)+" "+itemdata.getSLastName().get(i)+"</font> <b>"+ m_status +"</b><br /> <small>("+itemdata.getParentFirstName().get(i)+" "+itemdata.getParentLastName().get(i)+")</small>"));
               }
   			tvinstructorname.setText(""+itemdata.getInstructorName().get(i));
               tvlt.setText(""+itemdata.getLessontypeid().get(i));
               tvAge.setText(""+itemdata.getSAge().get(i));
               tvcomments.setText(""+itemdata.getWu_comments().get(i));
               String level = itemdata.getLEVELNAME().get(itemdata.getLEVELID().indexOf(itemdata.getSLevel().get(i)));
               tvlevel.setText(""+level);
   			holder.ll_students.addView(childView);
       	}
       }
	   }else if(itemdata.getFROMWHERE().toString().equalsIgnoreCase("MANAGER")){
		   String am_pm;
           if(Integer.parseInt(itemdata.getStTimeHour())>11)
           {
           	am_pm="PM";
           }
           else{
           	am_pm="AM";
           }
           if(Integer.parseInt(itemdata.getFormateStTimeHour())==0||Integer.parseInt(itemdata.getFormateStTimeHour())==00){
              	itemdata.setFormateStTimeHour("12");
              }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==13){
           	itemdata.setFormateStTimeHour("01");
           }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==14){
           	itemdata.setFormateStTimeHour("02");
           }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==15){
           	itemdata.setFormateStTimeHour("03");
           }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==16){
           	itemdata.setFormateStTimeHour("04");
           }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==17){
           	itemdata.setFormateStTimeHour("05");
           }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==18){
           	itemdata.setFormateStTimeHour("06");
           }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==19){
           	itemdata.setFormateStTimeHour("07");
           }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==20){
           	itemdata.setFormateStTimeHour("08");
           }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==21){
           	itemdata.setFormateStTimeHour("09");
           }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==22){
           	itemdata.setFormateStTimeHour("10");
           }
           else if(Integer.parseInt(itemdata.getFormateStTimeHour())==23){
           	itemdata.setFormateStTimeHour("11");
           }
           if(ViewScheduleDeckSupervisorActivity.yesitiszero){
           if(ViewScheduleDeckSupervisorActivity.hasstart){
        	   holder.start.setEnabled(false);
           	}else{
           		holder.start.setEnabled(true);
           	}
           }
           holder.stop.setEnabled(false);
           holder.tv_instructor.setEnabled(false);
           holder.tv_start.setText(itemdata.getStarttime());
           holder.tv_stop.setText(itemdata.getEndtime());
           if(!holder.tv_start.getText().toString().equalsIgnoreCase("")){
        	   holder.start.setEnabled(false);
        	   holder.stop.setEnabled(false);
           }
           
           holder.start.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(ViewScheduleDeckSupervisorActivity.yesitiszero){
						ViewScheduleDeckSupervisorActivity.hasstart = true;
					}
					Calendar cal = Calendar.getInstance();
					String am_pm = ""+cal.get(Calendar.AM_PM);
					if(Integer.parseInt(am_pm)==1){
						am_pm = "PM";
					}
					else{
						am_pm = "AM";
					}
					String date = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.YEAR);

					String h = ""+cal.get(Calendar.HOUR);
					if(h.length()==1){
						h="0"+h;
					}
					if(h.equalsIgnoreCase("00")){
						h="12";
					}
					String m = ""+cal.get(Calendar.MINUTE);
					if(m.length()==1){
						m="0"+m;
					}
					String time = ""+h+":"+m+" "+am_pm;
					holder.tv_start.setText(date + " " + time);
					ischeduleid = itemdata.getIScheduleID();
					starttime = date +" "+time;
					MyTextView = holder.tv_instructor;
					new StartTime().execute();
					holder.start.setEnabled(false);
					holder.stop.setEnabled(true);
				}
			});
           holder.stop.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(ViewScheduleDeckSupervisorActivity.yesitiszero){
						ViewScheduleDeckSupervisorActivity.hasstart = false;
					}
					Calendar cal = Calendar.getInstance();
					String am_pm = ""+cal.get(Calendar.AM_PM);
					if(Integer.parseInt(am_pm)==1){
						am_pm = "PM";
					}
					else{
						am_pm = "AM";
					}
					String date = (cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.YEAR);
					String h = ""+cal.get(Calendar.HOUR);
					if(h.length()==1){
						h="0"+h;
					}
					String m = ""+cal.get(Calendar.MINUTE);
					if(m.length()==1){
						m="0"+m;
					}
					String time = ""+h+":"+m+" "+am_pm;
					holder.tv_stop.setText(date + " " + time);
					endtime = date +" "+time;
					new EndTime().execute();
					holder.stop.setEnabled(false);
					holder.start.setEnabled(false);
					holder.tv_instructor.setEnabled(false);
				}
			});
           
           String instructorname = "Instructor Name: "+itemdata.getWu_InstructorName();
           SpannableString ss = new SpannableString(instructorname);
           ClickableSpan clickableSpan = new ClickableSpan() {
               @Override
               public void onClick(View textView) {
            	  Intent i = new Intent(context, ShadowLogsActivity.class);
            	  i.putExtra("from", "edit");
            	  i.putExtra("ischeduleid", itemdata.getIScheduleID());
            	  
            	  context.startActivity(i);
            	
               }
           };
           
           ss.setSpan(clickableSpan,17, instructorname.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
           ss.setSpan(new StyleSpan(Typeface.BOLD), 0, 16, 0);
           holder.tv_instructor.setText(ss);
           holder.tv_instructor.setMovementMethod(LinkMovementMethod.getInstance());
           
           if(itemdata.getWu_avail()==0||itemdata.getWu_avail()==1){
              	holder.ll_students.setVisibility(View.GONE);
              	holder.tv_view_schedule_students.setVisibility(View.VISIBLE);
              	holder.table_mvcs_shadow.setVisibility(View.GONE);
              	holder.tv_view_schedule_students.setText(Html.fromHtml("No schedule at "+ itemdata.getFormateStTimeHour()+":"+itemdata.getFormatStTimeMin()+" "+am_pm)+" for "+itemdata.getWu_InstructorName());
              }
              else{
              	holder.ll_students.setVisibility(View.VISIBLE);
              	holder.tv_view_schedule_students.setVisibility(View.GONE);
              	holder.table_mvcs_shadow.setVisibility(View.VISIBLE);
              	holder.ll_students.removeAllViews();
//              	LinearLayout footerLayout = (LinearLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.viewschedule_student_item_header,null);
//              	TextView tv_time = (TextView)footerLayout.findViewById(R.id.tv_vsi_time);
//              	tv_time.setText(Html.fromHtml("<b>Time:</b> "+ itemdata.getFormateStTimeHour()+":"+itemdata.getFormatStTimeMin()+" "+am_pm));
//              	holder.ll_students.addView(footerLayout, 0);
              	
              	for (int i = 0; i < itemdata.getStudentID().size(); i++) {
              		LayoutInflater minflater = LayoutInflater.from(this.context);
          			View childView = inflater.inflate(R.layout.manager_vcs_student, null);
          			TextView tv_time = (TextView)childView.findViewById(R.id.tv_vsi_time);
          				tv_time.setText(Html.fromHtml("<b>Time:</b> "+ itemdata.getFormateStTimeHour()+":"+itemdata.getFormatStTimeMin()+" "+am_pm));
          			if(i>0){
          				tv_time.setText("");
          			}
          			
          		
          			TextView tvStudName = (TextView) childView
          					.findViewById(R.id.tv_vsi_stud_name);
          			TextView tvAge = (TextView) childView.findViewById(R.id.tv_vsi_age);
          			TextView tvlevel = (TextView) childView
          					.findViewById(R.id.tv_vsi_level);
          			TextView tvcomments = (TextView) childView
          					.findViewById(R.id.tv_vsi_comment);
          			
          			
          			TextView tvlt = (TextView) childView
          					.findViewById(R.id.tv_vsi_lt);
          			
          			String m_status = itemdata.getMemStatus().get(i);
          			if(m_status.toString().equalsIgnoreCase("")){
          				m_status="";	
          			}
          			else{
          				m_status ="*"+m_status+"*";
          			}
          			
          			if(itemdata.getStudentGender().get(i).toString().trim().equalsIgnoreCase("Female")){
              			tvStudName.setText(Html.fromHtml("<b>Student Name:</b> <font color='#8800B7'>"+itemdata.getSFirstName().get(i)+" "+itemdata.getSLastName().get(i)+"</font> <b>"+ m_status +"</b><br /> <small><b>Parent Name:</b> ("+itemdata.getParentFirstName().get(i)+" "+itemdata.getParentLastName().get(i)+")</small>"));

                   }
                   else{
               		tvStudName.setText(Html.fromHtml("<b>Student Name:</b> <font color='#000066'>"+itemdata.getSFirstName().get(i)+" "+itemdata.getSLastName().get(i)+"</font> <b>"+ m_status +"</b><br /> <small><b>Parent Name:</b> ("+itemdata.getParentFirstName().get(i)+" "+itemdata.getParentLastName().get(i)+")</small>"));
                   }
                      tvlt.setText(Html.fromHtml("<b>L Type:</b> "+itemdata.getLessontypeid().get(i)));
                      tvAge.setText(Html.fromHtml("<b>Age:</b> "+itemdata.getSAge().get(i)));
                      tvcomments.setText(Html.fromHtml("<b>Comments:</b> "+itemdata.getWu_comments().get(i)));
                      String level = itemdata.getLEVELNAME().get(itemdata.getLEVELID().indexOf(itemdata.getSLevel().get(i)));
                      tvlevel.setText(Html.fromHtml("<b>Level:</b> "+level));
          			holder.ll_students.addView(childView);
              	}
              }
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



	static class ViewHolder{
		
		TextView tv_time,tv_day,tv_view_schedule,tv_view_schedule_students,tv_instructor,tv_start,tv_stop;
		LinearLayout ll_main,ll_students;
		Button start,stop;
		Chronometer tv_timer;
		TableLayout table_mvcs_shadow;
	}
	
	
	private class StartTime extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_InsertManabgersShadowTime);
			request.addProperty("token",  WW_StaticClass.UserToken);
			request.addProperty("ManagerID",  WW_StaticClass.InstructorID);
			request.addProperty("ischeduleid",  ischeduleid);
			request.addProperty("starttime", starttime);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
					androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_InsertManabgersShadowTime,
						envelope); // Calling Web service
					SoapPrimitive response =  (SoapPrimitive) envelope.getResponse();
					 Log.i("here","Result : " + response.toString());
					 String rep = response.toString();
					 JSONObject jsonObject = new JSONObject(rep);
					 JSONArray jsonObject2 = jsonObject.getJSONArray("ManagerShadowID");
					 ManagerShadowID = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]","");
					 Log.e("ManagerShadowID", "ManagerShadowID " + ManagerShadowID );
					 start_time = true;
			}
			catch(SocketException e){
				e.printStackTrace();
				connctionout = true;
			}
			catch(JSONException e){
				e.printStackTrace();
				server_response = true;
			}
			catch(Exception e){
				e.printStackTrace();
				server_response = true;
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(server_response){
				server_response = false;
				Toast.makeText(context, "Check internet connection",Toast.LENGTH_LONG).show();
			}else if(connctionout){
				connctionout = false;
				new StartTime().execute();
			}
			else{
				if(start_time){
					Log.i("MVCS", "Time start");
					MyTextView.setEnabled(true);
					MyTextView.setTag(ManagerShadowID);
					ViewScheduleDeckSupervisorActivity.ManagerShadowID.add(ManagerShadowID);
				}
			}
		}
	}

	private class EndTime extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
		SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
				SOAP_CONSTANTS.METHOD_NAME_UpdateManabgersShadowEndTime);
		request.addProperty("token",  WW_StaticClass.UserToken);
		request.addProperty("MgrShadowID",  ManagerShadowID);
		request.addProperty("endtime", endtime);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11); // Make an Envelop for sending as whole
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		Log.i("Request",  "Request = " + request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				SOAP_CONSTANTS.SOAP_ADDRESS);
		try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_UpdateManabgersShadowEndTime,
					envelope); // Calling Web service
				SoapPrimitive response =  (SoapPrimitive) envelope.getResponse();
				 Log.i("here","Result : " + response.toString());
				 String rep = response.toString();
				 JSONObject jsonObject = new JSONObject(rep);
				 JSONArray jsonObject2 = jsonObject.getJSONArray("Msg");
				 Msg = jsonObject2.toString().replaceAll("\\[", "").replaceAll("\\]","");
				 Log.e("Msg", "Msg " + Msg );
				 end_time = true;
		}
		catch(SocketException e){
			e.printStackTrace();
			connctionout = true;
		}
		catch(JSONException e){
			e.printStackTrace();
			server_response = true;
		}
		catch(Exception e){
			e.printStackTrace();
			server_response = true;
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(server_response){
			server_response =false;
			Toast.makeText(context, "Check internet connection", Toast.LENGTH_LONG).show();
		}else if(connctionout){
			connctionout = false;
			new EndTime().execute();
		}
		else{
			if(end_time){
				Log.i("MVCS", "Time end");
				ManagerShadowID = "";
				ViewScheduleDeckSupervisorActivity.ManagerShadowID.remove(ManagerShadowID);
			}
		}
	}
	}
}




