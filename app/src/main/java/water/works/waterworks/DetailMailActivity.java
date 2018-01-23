package water.works.waterworks;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class DetailMailActivity extends Activity implements OnClickListener {
	boolean mail_load=false,server_status = false,dowithmessage=false,folderget=false,dialog_folder=false,
			editclick=false,folder_created_edited=false;
	String whatClick="";
	public static String TAG="DetailMailActivity";
	ImageButton show_to, hide_to,show_cc, hide_cc,show_site, hide_site;
	String FromUsername,Sitename,ToUsername,CCUsername,MsgTime,MsgSubject,Priority,MsgDetails
	,FromUserID,ToUserID,CCUserID,strFrmmsg;
	TextView tv_FromUsername,tv_Sitename,tv_ToUsername,tv_CCUsername,tv_MsgTime,tv_MsgSubject,tv_Priority;
	Boolean isInternetPresent = false;
//	Titanic titanic;
//	TitanicTextView tv ;

	Shimmer shimmer;
	ShimmerTextView tv;
	FrameLayout fl_detail_mail_loading;
	ListPopupWindow list_edit_parent;
	ArrayList<String> ParentID,Temp_FolderID,Temp_FolderName;
	ArrayList<String> FolderID,FolderName,Final_FolderName;
	String Folder_parent_id;
	String new_folder_name;
	String SelectedFolder_id;
	String F_id;
	String selected_folder_name_for_edit;
	WebView tv_MsgDetails;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_mail);
		Log.e(TAG, "F_number = "+ WW_StaticClass.Folder_id);
		F_id =""+WW_StaticClass.Folder_id;
		isInternetPresent = Utility
				.isNetworkConnected(DetailMailActivity.this);
		Initialization();
		 WW_StaticClass.duration1 = 300;
		 WW_StaticClass.duration2 = 1000;
//		 titanic = new Titanic();
//		 titanic.start(tv);

		shimmer=new Shimmer();
		shimmer.start(tv);
			Final_FolderName = new ArrayList<String>();
			FolderName = new ArrayList<String>();
			FolderID = new ArrayList<String>();
			ParentID = new ArrayList<String>();
			Temp_FolderID = new ArrayList<String>();
			Temp_FolderName = new ArrayList<String>();
			list_edit_parent = new ListPopupWindow(getApplicationContext());
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle("");
			if(isInternetPresent){
				new DetailMail().execute();
				new GetParentID().execute();
				new DialogData().execute();
			}
			else{
				onDetectNetworkState().show();
			}
			WW_StaticClass.lable = getIntent().getStringExtra("Lable");
			Log.i(TAG, "Lable = " + WW_StaticClass.lable);
			
//			View v = findViewById(R.id.action_delete);
//			ShowTipsView showtips1 = new ShowTipsBuilder(DetailMailActivity.this)
//		    .setTarget(v)
//		    .setTitle("Delete Mail")
//		    .setTitleColor(Color.YELLOW)
//		    .setDescription("This will delete the current mail.")
//		    .setDescriptionColor(getResources().getColor(R.color.bigs))
//		    .setCircleColor(Color.RED)
//		    .displayOneTime(91)
//		    .setDelay(1000)
//		    .setCallback(new ShowTipsViewInterface() {
//				
//				@Override
//				public void gotItClicked() {
//					// TODO Auto-generated method stub
//					View v = findViewById(R.id.action_make_unread);
//					ShowTipsView showtips2 = new ShowTipsBuilder(DetailMailActivity.this)
//				    .setTarget(v)
//				    .setTitle("Make mail Unread")
//				    .setTitleColor(Color.YELLOW)
//				    .setDescription("This will change mail status to Unread.")
//				    .setDescriptionColor(getResources().getColor(R.color.bigs))
//				    .setCircleColor(Color.RED)
//				    .displayOneTime(90)
//				    .setDelay(0)
//				    .setCallback(new ShowTipsViewInterface() {
//						
//						@Override
//						public void gotItClicked() {
//							// TODO Auto-generated method stub
//							View v = findViewById(R.id.action_move_to);
//							ShowTipsView showtips3 = new ShowTipsBuilder(DetailMailActivity.this)
//						    .setTarget(v)
//						    .setTitle("Move mail")
//						    .setTitleColor(Color.YELLOW)
//						    .setDescription("Move mail in to Existing folder or Create new folder.")
//						    .setDescriptionColor(getResources().getColor(R.color.bigs))
//						    .setCircleColor(Color.RED)
//						    .displayOneTime(89)
//						    .setDelay(0)
//						    .build();
//							showtips3.show(DetailMailActivity.this);
//						}
//					})
//				    .build();
//					showtips2.show(DetailMailActivity.this);
//				}
//			})
//		    .build();
//			showtips1.show(DetailMailActivity.this);
	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        DetailMailActivity.this.finish();
		    }
		})       
		.setPositiveButton("Οk",new DialogInterface.OnClickListener() {


		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		    }
		});
		    return builder1.create();
	}
	
	private void Initialization() {
		// TODO Auto-generated method stub
//		 tv = (TitanicTextView) findViewById(R.id.my_text_view);
		tv = (ShimmerTextView) findViewById(R.id.my_text_view);
		 tv.setTypeface(Typefaces.get(DetailMailActivity.this, "Satisfy-Regular.ttf"));
		 fl_detail_mail_loading = (FrameLayout)findViewById(R.id.mail_detail_loading);
		 fl_detail_mail_loading.setVisibility(View.GONE);
		 
		 tv_FromUsername = (TextView)findViewById(R.id.detail_msg_username);
		 tv_MsgSubject = (TextView)findViewById(R.id.detail_msg_subject);
		 tv_Priority = (TextView)findViewById(R.id.detai_msg_priority);
		 tv_MsgTime = (TextView)findViewById(R.id.detail_msg_time);
		 tv_Sitename = (TextView)findViewById(R.id.detail_msg_sitename);
		 tv_ToUsername = (TextView)findViewById(R.id.detail_msg_to);
		 tv_CCUsername = (TextView)findViewById(R.id.detail_msg_cc);
		 show_to = (ImageButton)findViewById(R.id.show_to);
		 show_cc = (ImageButton)findViewById(R.id.show_cc);
		 show_site = (ImageButton)findViewById(R.id.show_site);
		 hide_to = (ImageButton)findViewById(R.id.hide_to);
		 hide_cc = (ImageButton)findViewById(R.id.hide_cc);
		 hide_site = (ImageButton)findViewById(R.id.hide_site);
		 tv_MsgDetails = (WebView) findViewById(R.id.detail_msg_msg);
		 
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(WW_StaticClass.lable.equalsIgnoreCase("Inbox")||WW_StaticClass.lable.equalsIgnoreCase("Lable")){
        	inflater.inflate(R.menu.detail_mail, menu);
        }
        else if(WW_StaticClass.lable.equalsIgnoreCase("Sent Item")){
        	inflater.inflate(R.menu.for_sent_item, menu);
        }
        else if(WW_StaticClass.lable.equalsIgnoreCase("Trash")){
        	inflater.inflate(R.menu.for_trash, menu);
        }
        else{
        	inflater.inflate(R.menu.detail_mail, menu);
        }
 
        return super.onCreateOptionsMenu(menu);
    }
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if(isInternetPresent){
			switch (item.getItemId()) {
			case android.R.id.home:
				Intent messagecenterIntent = new Intent(DetailMailActivity.this, MessageCenterActivity.class);
				startActivity(messagecenterIntent);
				WW_StaticClass.lable = "Inbox";
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				
				break;
			case R.id.action_delete:
//					Toast.makeText(DetailMailActivity.this, "Message Deleted", 1).show();
				whatClick = "Delete";
				new DoTaskWithMessage().execute();
				break;
			case R.id.action_make_unread:
				whatClick = "ReadUnread";
				new DoTaskWithMessage().execute();
				break;
			case R.id.action_new_folder:
	    					if(F_id.equalsIgnoreCase("-1")){
				
	    						Folder_parent_id = "Select";
	    						selected_folder_name_for_edit = "Please Select a Parent";
	    					}
	    					else{
	    						Folder_parent_id = F_id;
	    					
	    					
							
							int index = FolderID.indexOf(F_id);
							String f_name = FolderName.get(index).toString().trim();
	    					String at_split[] = f_name.split("\\W");
	    					String name_folder ="";
	    					for(int c=0;c<at_split.length;c++){
	    						if(at_split[c].isEmpty()||at_split[c].equalsIgnoreCase("")){
	    							
	    						}
	    						else{
	    							name_folder = name_folder+" "+at_split[c];
	    						}
	    					}
	    					selected_folder_name_for_edit = name_folder;
	    					}
	    					
	    					Log.e(TAG, "Sub folder parent id = "+Folder_parent_id);
							SelectedFolder_id="";
							
							  final Dialog dialog = new Dialog(DetailMailActivity.this);
							  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
							  dialog.setContentView(R.layout.folder_edit_folder);
							  dialog.setCancelable(false);
							  TextView tv_title = (TextView)dialog.findViewById(R.id.tv_edit_folder_name);
							  tv_title.setText("Create folder");
					    		final Button btn_edit_folder_parent = (Button)dialog.findViewById(R.id.btn_dialog_folder_list);
					    		final EditText et_editText = (EditText)dialog.findViewById(R.id.et_dialog_folder_name);
					    		Button btn_edit_ok =(Button)dialog.findViewById(R.id.btn_dialog_folder_ok);
					    		btn_edit_ok.setText("Create");
					    		Button btn_edit_cancel = (Button)dialog.findViewById(R.id.btn_dialog_folder_cancel);
					    		et_editText.setText("");
					    		et_editText.setHint("Folder name");
					    		btn_edit_folder_parent.setText(selected_folder_name_for_edit);
					    		btn_edit_cancel.setOnClickListener(new OnClickListener() {

									public void onClick(View v) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
					    		btn_edit_folder_parent.setOnClickListener(new OnClickListener() {

									public void onClick(View v) {
										// TODO Auto-generated method stub
										list_edit_parent.show();
									}
								});
					    		btn_edit_ok.setOnClickListener(new OnClickListener() {

									public void onClick(View v) {
										// TODO Auto-generated method stub
									
										if(et_editText.getText().toString().isEmpty()){
											Toast.makeText(DetailMailActivity.this, "Please select folder to edit.", Toast.LENGTH_LONG).show();
										}
										else{
											Log.i(TAG, "Selected Folder Id = " + SelectedFolder_id);
											Log.i(TAG, "Folder Parent Id = " + Folder_parent_id);
											new_folder_name = et_editText.getText().toString();
											editclick = true;
											new CreateFolder().execute();
											dialog.dismiss();
										}
									}
								});
					    		
					    		list_edit_parent.setAdapter(new ArrayAdapter<String>(
					    	            getApplicationContext(),
					    	            R.layout.edittextpopup,Final_FolderName ));
					    		list_edit_parent.setAnchorView(btn_edit_folder_parent);
					    		list_edit_parent.setHeight(500);
					    		list_edit_parent.setModal(true);
					    		list_edit_parent.setOnItemClickListener(
					                new OnItemClickListener() {

					    				public void onItemClick(AdapterView<?> parent, View view,
                                                                int pos, long id) {
					    					// TODO Auto-generated method stub
					    					String f_name = FolderName.get(pos).toString().trim();
					    					Log.e(TAG, ""+f_name);
					    					String a[] = f_name.split("\\W");
					    					String b ="";
					    					for(int c=0;c<a.length;c++){
					    						if(a[c].isEmpty()||a[c].equalsIgnoreCase("")){
					    							
					    						}
					    						else{
					    							b = b+" "+a[c];
					    						}
					    					}
					    					Log.e(TAG, "here = " + b.trim());
					    					btn_edit_folder_parent.setText(b);
					    					if(btn_edit_folder_parent.getText().toString().trim().equalsIgnoreCase("Please Select a Parent...")||btn_edit_folder_parent.getText().toString().trim().equalsIgnoreCase("Please Select a Parent")){
					    						Folder_parent_id = "Select";
					    					}
					    					else{
					    						Folder_parent_id = Temp_FolderID.get(Temp_FolderName.indexOf(btn_edit_folder_parent.getText().toString().trim()));
					    					}
					    						
					    					Log.i(TAG, "Selcted folder id = " + SelectedFolder_id);
					    					Log.i(TAG, "Folder Parent id = " + Folder_parent_id);
					    					
					    					list_edit_parent.dismiss();
					    				}
					    			});

					 	       dialog.show();
				break;
			case R.id.action_from_old_folder:
				if(F_id.equalsIgnoreCase("-1")){
	
					Folder_parent_id = "Select";
					selected_folder_name_for_edit = "Please Select a Parent";
				}
				else{
					Folder_parent_id = F_id;
				
				
				
				int index = FolderID.indexOf(F_id);
				String f_name1 = FolderName.get(index).toString().trim();
				String at_split1[] = f_name1.split("\\W");
				String name_folder1 ="";
				for(int c=0;c<at_split1.length;c++){
					if(at_split1[c].isEmpty()||at_split1[c].equalsIgnoreCase("")){
						
					}
					else{
						name_folder1 = name_folder1+" "+at_split1[c];
					}
				}
				selected_folder_name_for_edit = name_folder1;
				}
				
				Log.e(TAG, "Sub folder parent id = "+Folder_parent_id);
				SelectedFolder_id="";
	
				  final Dialog dialog1 = new Dialog(DetailMailActivity.this);
				  dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
				  dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				  dialog1.setContentView(R.layout.folder_move_to_mail);
				  dialog1.setCancelable(false);
		    		final Button btn_move_folder = (Button)dialog1.findViewById(R.id.btn_dialog_folder_list);
		    		Button btn_move_ok =(Button)dialog1.findViewById(R.id.btn_dialog_folder_ok);
		    		Button btn_move_cancel = (Button)dialog1.findViewById(R.id.btn_dialog_folder_cancel);
		    		btn_move_folder.setText(selected_folder_name_for_edit);
		    		btn_move_cancel.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog1.dismiss();
						}
					});
		    		btn_move_folder.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							list_edit_parent.show();
						}
					});
		    		btn_move_ok.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
						
//								new CreateFolder().execute();
							if(Folder_parent_id.equalsIgnoreCase("Select")){
								dialog1.dismiss();
								dialog1.show();
								Toast.makeText(DetailMailActivity.this,"Please select folder", Toast.LENGTH_LONG).show();
							}
							else{
									new MoveToFolder().execute();
									dialog1.dismiss();
							}
							}
					});
		    		
		    		list_edit_parent.setAdapter(new ArrayAdapter<String>(
		    	            getApplicationContext(),
		    	            R.layout.edittextpopup,Final_FolderName ));
		    		list_edit_parent.setAnchorView(btn_move_folder);
		    		list_edit_parent.setHeight(500);
		    		list_edit_parent.setModal(true);
		    		list_edit_parent.setOnItemClickListener(
		                new OnItemClickListener() {

		    				public void onItemClick(AdapterView<?> parent, View view,
                                                    int pos, long id) {
		    					// TODO Auto-generated method stub
		    					String f_name = FolderName.get(pos).toString().trim();
		    					Log.e(TAG, ""+f_name);
		    					String a[] = f_name.split("\\W");
		    					String b ="";
		    					for(int c=0;c<a.length;c++){
		    						if(a[c].isEmpty()||a[c].equalsIgnoreCase("")){
		    							
		    						}
		    						else{
		    							b = b+" "+a[c];
		    						}
		    					}
		    					Log.e(TAG, "here = " + b.trim());
		    					btn_move_folder.setText(b);
		    					if(btn_move_folder.getText().toString().trim().equalsIgnoreCase("Please Select a Parent...")||btn_move_folder.getText().toString().trim().equalsIgnoreCase("Please Select a Parent")){
		    						Folder_parent_id = "Select";
		    					}
		    					else{
		    						Folder_parent_id = Temp_FolderID.get(Temp_FolderName.indexOf(btn_move_folder.getText().toString().trim()));
		    					}
		    						
		    					Log.i(TAG, "Selcted folder id = " + SelectedFolder_id);
		    					Log.i(TAG, "Folder Parent id = " + Folder_parent_id);
		    					
		    					list_edit_parent.dismiss();
		    				}
		    			});

		 	       dialog1.show();

				
				break;
			default:
				
				break;
			}
		}
		else{
			onDetectNetworkState().show();
		}
		return super.onOptionsItemSelected(item);	
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isInternetPresent = Utility
				.isNetworkConnected(DetailMailActivity.this);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent messagecenterIntent = new Intent(DetailMailActivity.this, MessageCenterActivity.class);
		startActivity(messagecenterIntent);
		WW_StaticClass.lable = "Inbox";
		finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
		case R.id.show_to:
			System.out.println("Show button");
			show_to.setVisibility(View.INVISIBLE);
			hide_to.setVisibility(View.VISIBLE);
			tv_ToUsername.setMaxLines(Integer.MAX_VALUE);
			break;
			
		case R.id.show_cc:
			System.out.println("Show button");
			show_cc.setVisibility(View.INVISIBLE);
			hide_cc.setVisibility(View.VISIBLE);
			tv_CCUsername.setMaxLines(Integer.MAX_VALUE);
			break;
			
		case R.id.show_site:
			System.out.println("Show button");
			show_site.setVisibility(View.INVISIBLE);
			hide_site.setVisibility(View.VISIBLE);
			tv_Sitename.setMaxLines(Integer.MAX_VALUE);
			break;
			
		case R.id.hide_to:
			System.out.println("Hide button");
			hide_to.setVisibility(View.INVISIBLE);
			show_to.setVisibility(View.VISIBLE);
			tv_ToUsername.setMaxLines(1);
			break;
			
		case R.id.hide_cc:
			System.out.println("Hide button");
			hide_cc.setVisibility(View.INVISIBLE);
			show_cc.setVisibility(View.VISIBLE);
			tv_CCUsername.setMaxLines(1);
			break;
			
		case R.id.hide_site:
			System.out.println("Hide button");
			hide_site.setVisibility(View.INVISIBLE);
			show_site.setVisibility(View.VISIBLE);
			tv_Sitename.setMaxLines(0);
			break;
		
		case R.id.ib_detail_msg_reply:
//			Toast.makeText(DetailMailActivity.this, "Reply", 1).show();
			Intent itnewmailIntent = new Intent(DetailMailActivity.this, CreateNewMessageActivity.class);
			itnewmailIntent.putExtra("IAMFOR", "REPLY");
			itnewmailIntent.putExtra("From", FromUsername);
			itnewmailIntent.putExtra("To", ToUsername);
			itnewmailIntent.putExtra("Cc", CCUsername);
			itnewmailIntent.putExtra("Subject", MsgSubject);
			itnewmailIntent.putExtra("Priority", Priority);
			itnewmailIntent.putExtra("Sitename", Sitename);
			itnewmailIntent.putExtra("FromID", FromUserID);
			itnewmailIntent.putExtra("ToID", ToUserID);
			itnewmailIntent.putExtra("CcID", CCUserID);
			itnewmailIntent.putExtra("strFrmmsg", strFrmmsg);
			
			startActivity(itnewmailIntent);
			break;
			
		case R.id.ib_detail_msg_reply_all:
//			Toast.makeText(DetailMailActivity.this, "Reply All", 1).show();
			Intent itnewmailIntent1 = new Intent(DetailMailActivity.this, CreateNewMessageActivity.class);
			itnewmailIntent1.putExtra("IAMFOR", "REPLYALL");
			itnewmailIntent1.putExtra("From", FromUsername);
			itnewmailIntent1.putExtra("To", ToUsername);
			itnewmailIntent1.putExtra("Cc", CCUsername);
			itnewmailIntent1.putExtra("Subject", MsgSubject);
			itnewmailIntent1.putExtra("Priority", Priority);
			itnewmailIntent1.putExtra("Sitename", Sitename);
			itnewmailIntent1.putExtra("FromID", FromUserID);
			itnewmailIntent1.putExtra("ToID", ToUserID);
			itnewmailIntent1.putExtra("CcID", CCUserID);
			itnewmailIntent1.putExtra("strFrmmsg", strFrmmsg);
			
			startActivity(itnewmailIntent1);
			break;
			
		case R.id.ib_detail_msg_forward:
//			Toast.makeText(DetailMailActivity.this, "Forward", 1).show();
			Intent itnewmailIntent2 = new Intent(DetailMailActivity.this, CreateNewMessageActivity.class);
			itnewmailIntent2.putExtra("IAMFOR", "FWD");
			itnewmailIntent2.putExtra("From", "");
			itnewmailIntent2.putExtra("To", "");
			itnewmailIntent2.putExtra("Cc", "");
			itnewmailIntent2.putExtra("Subject", MsgSubject);
			itnewmailIntent2.putExtra("Priority", Priority);
			itnewmailIntent2.putExtra("Sitename", Sitename);
			itnewmailIntent2.putExtra("FromID", "");
			itnewmailIntent2.putExtra("ToID", "");
			itnewmailIntent2.putExtra("CcID", "");
			itnewmailIntent2.putExtra("strFrmmsg", strFrmmsg);
			
			startActivity(itnewmailIntent2);
			break;
		default:
			break;
		}
	}
	
	private class DetailMail extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fl_detail_mail_loading.setVisibility(View.VISIBLE);
			fl_detail_mail_loading.bringToFront();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_Mail_GetMessageById);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("MsgID", WW_StaticClass.mailid);
			request.addProperty("CurrFolderID", WW_StaticClass.foldernumber);
			
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Mail_GetMessageById,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
//				 Log.i(TAG,"" + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
//				 Log.i(TAG, "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
//				 Log.i(TAG, "mSoapObject2="+mSoapObject2);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				if(code.equalsIgnoreCase("000")){
					mail_load = true;
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
//					Log.i(TAG, "mSoapObject3="+mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("MessageByID");
					Log.i(TAG,"jArray : " + jArray.toString());
					JSONObject jsonObject ;
					for(int i=0;i<jArray.length();i++){
						jsonObject = jArray.getJSONObject(i);
						FromUsername = jsonObject.getString("FromUsername");
						Sitename = jsonObject.getString("Sitename");
						ToUsername = jsonObject.getString("ToUsername");
						CCUsername = jsonObject.getString("CCUsername");
						MsgTime = jsonObject.getString("MsgTime");
						MsgSubject = jsonObject.getString("MsgSubject");
						MsgDetails = jsonObject.getString("MsgDetails");
						Priority = jsonObject.getString("Priority");
						FromUserID = jsonObject.getString("FromUserID");
						ToUserID = jsonObject.getString("ToUserID");
						CCUserID =jsonObject.getString("CCUserID");
						strFrmmsg = jsonObject.getString("strFrmmsg");
					}
					
				}
				else{
					mail_load =false;
				}
			}
			catch(JSONException e){
				e.printStackTrace();
				server_status =true;
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			fl_detail_mail_loading.setVisibility(View.GONE);
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						DetailMailActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else{
				if(!mail_load){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							DetailMailActivity.this,"WaterWorks",
							"No mail found.", "OK");
				}
				else{
					mail_load = false;
					if(MsgSubject.isEmpty()||MsgSubject.equalsIgnoreCase("")){
						tv_MsgSubject.setText("(no subject)");
					}
					else{
						tv_MsgSubject.setText(MsgSubject);
					}
					tv_FromUsername.setText("From: "+FromUsername);
					tv_Sitename.setText("(" + Sitename + ")");
					if(Priority.equalsIgnoreCase("0")){
						tv_Priority.setText("");
					}
					else if(Priority.equalsIgnoreCase("1")){
						tv_Priority.setText("*");
					}else if(Priority.equalsIgnoreCase("2")){
						tv_Priority.setText("!!");
					}
					tv_MsgTime.setText("Sent: "+MsgTime);
					tv_ToUsername.setText("To: " + ToUsername);
					tv_CCUsername.setText("CC: " + CCUsername);
//					change by megha shah 05/01/2018
//					tv_MsgDetails.setText(Html.fromHtml(MsgDetails));
					tv_MsgDetails.loadData(MsgDetails, "text/html; charset=utf-8", null);//"UTF-8"
				}
			}
		}
		
	}
	
	
	private class DoTaskWithMessage extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			fl_detail_mail_loading.setVisibility(View.VISIBLE);
			fl_detail_mail_loading.bringToFront();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String Method = "" , Action ="";
			if(whatClick.equalsIgnoreCase("ReadUnread")){
				Method = SOAP_CONSTANTS.METHOD_NAME_Mail_MarkAsReadUnread;
				Action = SOAP_CONSTANTS.SOAP_Action_Mail_MarkAsReadUnread;
			}
			else if(whatClick.equalsIgnoreCase("Delete")){
				Method = SOAP_CONSTANTS.METHOD_NAME_Mail_DeleteSelectedMessages;
				Action = SOAP_CONSTANTS.SOAP_Action_Mail_DeleteSelectedMessages;
			}
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					Method);
			if(whatClick.equalsIgnoreCase("ReadUnread")){
				request.addProperty("token", WW_StaticClass.UserToken);
				request.addProperty("MoveMsgId", WW_StaticClass.mailid);
				request.addProperty("ReadUnread", 0);
			}
			else if(whatClick.equalsIgnoreCase("Delete")){
				request.addProperty("token", WW_StaticClass.UserToken);
				request.addProperty("MoveMsgId", WW_StaticClass.mailid);
			}
			
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(Action,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
//				 Log.i(TAG,"" + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
//				 Log.i(TAG, "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
//				 Log.i(TAG, "mSoapObject2="+mSoapObject2);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				if(code.equalsIgnoreCase("000")){
					dowithmessage=true;
				}
				else{
					dowithmessage=false;
				}
			}
			catch(Exception e){
				e.printStackTrace();
				server_status = true;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			fl_detail_mail_loading.setVisibility(View.GONE);
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						DetailMailActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else{
				if(!dowithmessage){
					if(whatClick.equalsIgnoreCase("ReadUnread")){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							DetailMailActivity.this,"WaterWorks",
							"Message Unread Unsuccessful....", "OK");
					}
					else if(whatClick.equalsIgnoreCase("Delete")){
						SingleOptionAlertWithoutTitle.ShowAlertDialog(
								DetailMailActivity.this,"WaterWorks",
								"Message Not Deleted....", "OK");	
					}
					
				}
				else{
					dowithmessage=false;
					if(whatClick.equalsIgnoreCase("ReadUnread")){
						Log.e(TAG, "Message ReadUnread Successfully....");
					}
					else if(whatClick.equalsIgnoreCase("Delete")){
						Log.e(TAG, "Message Deleted Successfully....");
					}
					Intent messagecenterIntent = new Intent(DetailMailActivity.this, MessageCenterActivity.class);
					startActivity(messagecenterIntent);
					WW_StaticClass.lable = "Inbox";
					finish();
					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				}
			}
		}
	}
	
	
	/*            
	 *     Get Parent ID
	 */
	
	private class GetParentID extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			ParentID.clear();
			Temp_FolderID.clear();
			Temp_FolderName.clear();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_Mail_Get_FolderList);
			request.addProperty("token", WW_StaticClass.UserToken);
			
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Mail_Get_FolderList,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
				 Log.i(TAG,"" + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 Log.i(TAG, "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 Log.i(TAG, "mSoapObject2="+mSoapObject2);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if(code.equalsIgnoreCase("000")){
					 folderget = true;
					 Object mSoapObject3 =  mSoapObject1.getProperty(1);
//						Log.i(TAG, "mSoapObject3="+mSoapObject3);
						String resp = mSoapObject3.toString();
						JSONObject jo = new JSONObject(resp);
						JSONArray jArray = jo.getJSONArray("FolderList");
//						Log.i(TAG,"jArray : " + jArray.toString());
						JSONObject jsonObject ;
						for(int i=0;i<jArray.length();i++){
							jsonObject = jArray.getJSONObject(i);
							Temp_FolderID.add(jsonObject.getString("FolderId"));
							Temp_FolderName.add(jsonObject.getString("FolderName").trim());
							ParentID.add(jsonObject.getString("ParentId"));
						}
				 }
				 else if(code.equalsIgnoreCase("111")){
					 folderget = false;
//					 Object mSoapObject3 =  mSoapObject1.getProperty(1);
//						Log.i(TAG, "mSoapObject3="+mSoapObject3);
				 }
				 else{
					 folderget = false;
				 }
			}
			catch(JSONException e){
				server_status = true;
				e.printStackTrace();
			}
			catch(Exception e){
				server_status = true;
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.e(TAG, "Temp Folder id = " + Temp_FolderID);
			Log.e(TAG, "Temp Folder name = " + Temp_FolderName);
			Log.e(TAG, "Parent id = " + ParentID);
			if(server_status){
				server_status = false;
				Log.e(TAG, "Folder not found Exception");
			}else{
			if(folderget){
				if(Temp_FolderID.size()>0){
					Log.e(TAG, "in if");
					Log.e(TAG, "Folder found");
				}
				else{
					Log.e(TAG, "in else");
					Log.e(TAG, "No folder found");
				}
			}else{
				Log.e(TAG, "in else");
				Log.e(TAG, "No folder found");
			}
		}
	}
	}
	
	
	/*
	 *		Dialog Data 
	 */
	
	private class DialogData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog_folder=false;
			FolderID.clear();
			FolderName.clear();
			Final_FolderName.clear();
			WW_StaticClass.lable="lable";
			WW_StaticClass.Folder_id =0;
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_Mail_BindFolderTree);
			request.addProperty("token", WW_StaticClass.UserToken);
			
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Mail_BindFolderTree,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
				 Log.i(TAG,"" + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 Log.i(TAG, "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 Log.i(TAG, "mSoapObject2="+mSoapObject2);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if(code.equalsIgnoreCase("000")){
					 dialog_folder = true;
					 Object mSoapObject3 =  mSoapObject1.getProperty(1);
//						Log.i(TAG, "mSoapObject3="+mSoapObject3);
						String resp = mSoapObject3.toString();
						JSONObject jo = new JSONObject(resp);
						JSONArray jArray = jo.getJSONArray("FolderList");
//						Log.i(TAG,"jArray : " + jArray.toString());
						JSONObject jsonObject ;
						for(int i=0;i<jArray.length();i++){
							jsonObject = jArray.getJSONObject(i);
							FolderID.add(jsonObject.getString("FolderID"));
							FolderName.add(jsonObject.getString("FolderName"));
						}
				 }
				 else if(code.equalsIgnoreCase("111")){
					 dialog_folder = false;
//					 Object mSoapObject3 =  mSoapObject1.getProperty(1);
//						Log.i(TAG, "mSoapObject3="+mSoapObject3);
				 }
			}
			catch(JSONException e){
				server_status = true;
				e.printStackTrace();
			}
			catch(Exception e){
				server_status = true;
				e.printStackTrace();
			}
		
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(server_status){
				server_status = false;
				Log.e(TAG, "Folder not found Exception");
			}else{
			if(dialog_folder){
			if(FolderID.size()>0){
				Log.e(TAG, "in if");
				
				for(int x =0;x<FolderName.size();x++){
					String f_name = FolderName.get(x).toString();
					String at_split[] = f_name.split("\\W");
					String after_split ="";
					String pre_split="";
					for(int c=0;c<at_split.length;c++){
						if(at_split[c].isEmpty()||at_split[c].equalsIgnoreCase("")){
							pre_split = pre_split+"*"+at_split[c];
						}
						else{
							after_split = after_split+at_split[c]+" ";
							
						}
					}
					String name_folder = pre_split+"└"+after_split;
					name_folder = name_folder.replaceAll("\\*", " ");
					Final_FolderName.add(name_folder);
				}
				Log.e(TAG, ""+Final_FolderName);
				
			}
			else{
				Log.e(TAG, "in else no folder found");
			}
			}else{
				Log.e(TAG, "no folder found");
			}
		}
		
			
		}
	}
	
	/*
	 * Create sub folder 
	 */
	
private class CreateFolder extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_Mail_CreateMessageFolder);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("FolderName", new_folder_name); // folder name new or existing
			request.addProperty("ParentID", Folder_parent_id); // if new than Select , if edit than that folder's parent id or that folder id for modify
			request.addProperty("tempSelect", 1); // from main activity i.e inbox = 0 else 1
			request.addProperty("MoveMsgId", WW_StaticClass.mailid); // if message open than its id
			request.addProperty("SelectedFldr",SelectedFolder_id); // if edit than folder's id
			
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Mail_CreateMessageFolder,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
//				 Log.i(TAG,"" + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
//				 Log.i(TAG, "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
//				 Log.i(TAG, "mSoapObject2="+mSoapObject2);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if(code.equalsIgnoreCase("000")){
					 folder_created_edited= true;
					 Object mSoapObject3 =  mSoapObject1.getProperty(1);
						Log.i(TAG, "mSoapObject3="+mSoapObject3);
						String resp = mSoapObject3.toString();
						JSONObject jo = new JSONObject(resp);
						Log.e(TAG, jo.getString("CreateFolder"));
						folder_created_or_not = jo.getString("CreateFolder");
				 }
				 else if(code.equalsIgnoreCase("111")){
					 folder_created_edited= false;
					 Object mSoapObject3 =  mSoapObject1.getProperty(1);
						Log.i(TAG, "mSoapObject3="+mSoapObject3);
						String resp = mSoapObject3.toString();
						JSONObject jo = new JSONObject(resp);
						folder_created_or_not = jo.getString("CreateFolder");
						
				 }
			}
			catch(JSONException e){
				server_status = true;
				e.printStackTrace();
			}
			catch(Exception e){
				server_status = true;
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						DetailMailActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else{
				if(folder_created_edited){
					if(editclick){
						Toast.makeText(DetailMailActivity.this, folder_created_or_not, Toast.LENGTH_LONG).show();
//						new DialogData().execute();
//						new GetParentID().execute();
						Intent i=new Intent(DetailMailActivity.this, MessageCenterActivity.class);
						startActivity(i);
						finish();
						overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
						editclick=false;
					}
					else{
						Toast.makeText(DetailMailActivity.this, folder_created_or_not, Toast.LENGTH_LONG).show();
						Intent i=new Intent(DetailMailActivity.this, MessageCenterActivity.class);
						startActivity(i);
						finish();
						overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//						new DialogData().execute();
//						new GetParentID().execute();
					}
					folder_created_edited=false;
				}
				else{
					
					Toast.makeText(DetailMailActivity.this, folder_created_or_not, Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	String folder_created_or_not="";

	
	private class MoveToFolder extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_Mail_MoveMessageToFolder);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("MoveFolderID", Folder_parent_id); // if new than Select , if edit than that folder's parent id or that folder id for modify
			request.addProperty("MoveMsgId", WW_StaticClass.mailid); // if message open than its id
			
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Mail_MoveMessageToFolder,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if(code.equalsIgnoreCase("000")){
					 folder_created_edited= true;
					 Object mSoapObject3 =  mSoapObject1.getProperty(1);
						Log.i(TAG, "mSoapObject3="+mSoapObject3);
						String resp = mSoapObject3.toString();
						JSONObject jo = new JSONObject(resp);
						Log.e(TAG, jo.getString("MoveMsg"));
						folder_created_or_not = jo.getString("MoveMsg");
				 }
				 else if(code.equalsIgnoreCase("111")){
					 folder_created_edited= false;
					 Object mSoapObject3 =  mSoapObject1.getProperty(1);
						Log.i(TAG, "mSoapObject3="+mSoapObject3);
						String resp = mSoapObject3.toString();
						JSONObject jo = new JSONObject(resp);
						Log.e(TAG, jo.getString("MoveMsg"));
						folder_created_or_not = jo.getString("MoveMsg");
						
				 }
			}
			catch(JSONException e){
				server_status = true;
				e.printStackTrace();
			}
			catch(Exception e){
				server_status = true;
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(server_status){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						DetailMailActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_status = false;
			}
			else{
				if(folder_created_edited){
						Toast.makeText(DetailMailActivity.this, folder_created_or_not, Toast.LENGTH_LONG).show();
//						new DialogData().execute();
//						new GetParentID().execute();
						Intent i = new Intent(DetailMailActivity.this, MessageCenterActivity.class);
						startActivity(i);
						finish();
						overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
						folder_created_edited=false;
				}
				else{
					Toast.makeText(DetailMailActivity.this, folder_created_or_not, Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	}
