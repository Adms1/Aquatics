package water.works.waterworks.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import water.works.waterworks.NewModifyComments;
import water.works.waterworks.R;
import water.works.waterworks.ViewCurrentScheduleFragment;
import water.works.waterworks.ViewTodaysScheduleFragment;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.WW_StaticClass;


public class ModifyCommentsAdapter extends BaseAdapter{

	public ModifyCommentsAdapter(ArrayList<String> commentsgetted,
			ArrayList<String> expdate, ArrayList<String> tbid,String FROM, Context context) {
		super();
		this.commentsgetted = commentsgetted;
		this.expdate = expdate;
		this.tbid = tbid;
		this.context = context;
		this.FROM = FROM;
	}
	String FROM;
	boolean comment_status = false;
	boolean server_status = false;
	ArrayList<String> commentsgetted,expdate,tbid;
	Context context;

	public int getCount() {
		// TODO Auto-generated method stub
		return commentsgetted.size();
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
		TextView tv_comment,tv_date_time;
		Button btn_delete_comment;
    }
	View temp_view;

	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		try{
			if(convertView==null){
				holder = new ViewHolder();
				
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.commentsraw, null);
				holder.tv_comment = (TextView)convertView.findViewById(R.id.tv_commnetsraw_comment);
				holder.tv_date_time = (TextView)convertView.findViewById(R.id.tv_commentsraw_date);
				holder.btn_delete_comment = (Button)convertView.findViewById(R.id.btn_delete_comment);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
				
			}
			holder.tv_comment.setText(commentsgetted.get(position));
			holder.tv_date_time.setText(expdate.get(position));
			holder.btn_delete_comment.setOnClickListener(new OnClickListener() {
				

				public void onClick(View v) {
					// TODO Auto-generated method stub
					whichdelete = tbid.get(position);
					new deletecomment().execute();
					temp_view = v;
//					((Activity)v.getContext()).finish();
//					if(FROM.toString().equalsIgnoreCase("CURRENT")){
//					Intent it = new Intent(v.getContext(), ViewCurrentLessonActivity.class);
////					it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					it.putExtra("DELETE", "YES");
//					it.putExtra("HOUR", ViewCurrentLessonActivity.hour_for_data);
//					it.putExtra("MIN", ViewCurrentLessonActivity.min_for_data);
//					it.putExtra("DATE", ViewCurrentLessonActivity.date_for_data);
//					v.getContext().startActivity(it);
//					}else{
//						Intent it = new Intent(v.getContext(), ViewTodaysScheduleInstructorActivty.class);
//						it.putExtra("DELETE", "YES");
//						v.getContext().startActivity(it);
//					}
				}
			});
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return convertView;
	}
	String whichdelete;
	public String insertionresponse;
	private class deletecomment extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_DeleteStudentCommentByID);
			// Adding Username and Password for Login Invok
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("studentid", WW_StaticClass.Studentid);
			request.addProperty("tbid",whichdelete);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_DeleteStudentCommentByID,
						envelope); // Calling Web service
				SoapObject response =  (SoapObject) envelope.getResponse();
				 Log.i("here","Result : " + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 Log.i("Comment adapter", "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 Log.i("Comment adapter", "mSoapObject2="+mSoapObject2);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if (code.equals("000")) {
					comment_status = true;
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i("Comment adapter", "mSoapObject3="+mSoapObject3);
					insertionresponse = mSoapObject3.toString();
					
				 }
				 else {
						comment_status = false;
					}
			}
			catch(Exception e){
				server_status=true;
				e.printStackTrace();
			}
			return null;
		}
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(server_status){
				Toast.makeText(context, "Server not responding.\nPlease check internet connection or try after sometime.", Toast.LENGTH_LONG).show();
			}
			else{
				if (comment_status) {
					// Toast.makeText(context, insertionresponse, 1).show();
					AlertDialog alertDialog = new AlertDialog.Builder(
							temp_view.getContext()).create();
					// hide title bar
					// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alertDialog.setTitle(context.getString(R.string.app_name));
					alertDialog.setIcon(temp_view.getContext().getResources().getDrawable(
							R.drawable.ic_launcher));
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.setCancelable(false);
					// set the message
					alertDialog.setMessage(insertionresponse);
					// set button1 functionality
					alertDialog.setButton("Ok",
							new DialogInterface.OnClickListener() {


								public void onClick(DialogInterface dialog,
										int which) {
									// close dialog

									dialog.cancel();
									((NewModifyComments)temp_view.getContext()).recreate();
									if(FROM.toString().equalsIgnoreCase("CURRENT")){
										ViewCurrentScheduleFragment.commented = true;
									}else{
										ViewTodaysScheduleFragment.commented = true;
									}
									
									notifyDataSetChanged();

								}
							});
					// show the alert dialog
					alertDialog.show();
				} else {
					Toast.makeText(context, "Comment not deleted",Toast.LENGTH_LONG).show();
				}

		}
	}
	}
}
