package water.works.waterworks.chat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

import water.works.waterworks.R;
import water.works.waterworks.search.SearchFunctionality;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.WW_StaticClass;


public class Chat_Friends_list extends Activity{

	ImageView back,new_chat,imgBack;
	ListView roomlist;
	public static String user_id;
	private String mStringResponse_friends = "",json_friend_request="";
	JSONArray get_json_array;
	JSONObject oneObject;
	String str_uname, str_friend_id, str_friend_email, str_friendcount,
	str_rank, str_popularity, str_status;
	ArrayList<String> fid_arr,funame_arr;
	Context mContext=this;
	public static ArrayList<String>Userid,Username,search_id,search_name;
	public static LinkedHashMap<String, String> name_id = new LinkedHashMap<String, String>();
	public static HashMap<String, String> search_name_id = new HashMap<String, String>();
	public ArrayList<String> channel_count = new ArrayList<String>();
	public ArrayList<String> multi_channel = new ArrayList<String>();

	public static String TAG="FriendsList";
	boolean status = false;
	public static String Current_Uname="";

	//Pubnub Vars
	Pubnub pubnub;
	GoogleCloudMessaging gcm;
	SharedPreferences prefs;
	public static String SENDER_ID="468292255530";
	public static String REG_ID;
	private static final String APP_VERSION = "3.6.1";
	long totalSize = 0;
	String PUBLISH_KEY = "pub-c-0a1972fb-6b7f-4893-995e-2f45ba5ea420";
	String SUBSCRIBE_KEY = "sub-c-959ec618-3440-11e5-a033-0619f8945a4f";
	String CIPHER_KEY = "";
	String SECRET_KEY = "";
	String ORIGIN = "pubsub";
	String AUTH_KEY;
	String UUID;
	Boolean SSL = false;
	String URL = "http://192.168.1.131/AndroidFileUpload/fileUpload.php";
	String userid="",c_chan="",uname="",Touid="";
	String filepath="";
	Uri selectedImage;
	ImageLoader imageLoader;
	ProgressBar progressBar1;
	private static final int SELECT_PHOTO = 100;
	String regId="RegistrationID";
	public static ArrayList<String> unread_msg = new ArrayList<String>();

	//List Updation during chat
	public BroadcastReceiver receiver;
	boolean first_called=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dialogs_activity__quickblox);

		Bundle i = getIntent().getExtras();
		user_id = i.getString("uid");
		init();
		prefs = getSharedPreferences(
				"PUBNUB_DEV_CONSOLE", Context.MODE_PRIVATE);
		//		new BackgroundLoadFriendList().execute();

		SOAP_CONSTANTS.current_channel = "channel";

		//register BroadcastReceiver
		IntentFilter intentFilter = new IntentFilter(GcmIntentService_Pubnub.ACTION_MyIntentService);
		intentFilter.addCategory(Intent.CATEGORY_DEFAULT);


		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				//				Toast.makeText(mContext, "Received", Toast.LENGTH_SHORT).show();
				unread_msgs();
			}
		};

		this.registerReceiver(receiver, intentFilter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(receiver);
	}

	public void init(){
		back = (ImageView)findViewById(R.id.imgBack);
		new_chat = (ImageView)findViewById(R.id.action_new_chat);
		roomlist = (ListView)findViewById(R.id.roomsList);
		imgBack = (ImageView)findViewById(R.id.imgBack);

		pubnub = new Pubnub(
				PUBLISH_KEY,
				SUBSCRIBE_KEY,
				SECRET_KEY,
				CIPHER_KEY,
				SSL
				);
		pubnub.setCacheBusting(false);
		pubnub.setOrigin(ORIGIN);
		pubnub.setAuthKey(AUTH_KEY);
	}


	public void unread_msgs(){
		//		Toast.makeText(mContext, "Called", Toast.LENGTH_SHORT).show();
		if(unread_msg.size()>0){
			this.runOnUiThread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub

					//					String firstWord = "";
					//					if(Username.get(firstWord).contains(" ")){
					//						firstWord= temp_name.substring(0, temp_name.indexOf(" "));
					//					}else{
					//						firstWord = temp_name;
					//					}

					for (int i = 0; i < unread_msg.size(); i++) {
						String firstWord = "";
						if(Username.contains(unread_msg.get(i))){
							int position = Username.indexOf(unread_msg.get(i));
							Toast.makeText(mContext, position, Toast.LENGTH_SHORT).show();
							System.out.println("Position : "+position);
						}else{
							System.out.println("Skipped");
						}
					}
					lviewAdapter = new ListViewAdapter(mContext, Username, Userid);
//					lviewAdapter.notifyDataSetChanged();
					roomlist.setAdapter(lviewAdapter);
				}
			});
		}
	}
	protected void onResume() {
		super.onResume();

		if(first_called==false){
			unread_msgs();
		}
		new DeskdataforCEE().execute();

		SOAP_CONSTANTS.lastCount=0;

		imgBack.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});

		roomlist.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				SOAP_CONSTANTS.From="List";
				String temp_id = Userid.get(position);
				String temp_name = Username.get(position);
				String chat_channel = "";

				if(!temp_id.contains("group")){
					if(Integer.parseInt(user_id)<Integer.parseInt(temp_id)){
						chat_channel = user_id+"_"+temp_id;
					}else{
						chat_channel = temp_id+"_"+user_id;
					}
				}else{
					chat_channel = temp_id;
				}

				String firstWord = "";
				if(temp_name.contains(" ")){
					firstWord= temp_name.substring(0, temp_name.indexOf(" "));
				}else{
					firstWord = temp_name;
				}
				
				ArrayList<String> filtered = new ArrayList<String>();
				
				for (int i = 0; i < unread_msg.size(); i++) {
					if(unread_msg.get(i).contains(firstWord)){
//						unread_msg.remove(i);
						filtered.add(unread_msg.get(i));
					}
//					unread_msg.remove(firstWord);
				}
				
				unread_msg.removeAll(filtered);
				System.out.println("Removed "+unread_msg);


				System.out.println("Chat_channel = "+chat_channel);
				Intent i = new Intent(mContext,Chat_Room.class);
				i.putExtra("c_chan", chat_channel);
				i.putExtra("uid", user_id);
				i.putExtra("Touid", temp_id);
				i.putExtra("uname", temp_name);
				startActivity(i);


			}
		});

		new_chat.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, SearchFunctionality.class);
				startActivity(i);
			}
		});
	};

	ProgressDialog pDialog;
	ListViewAdapter lviewAdapter;

	public class DeskdataforCEE extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Chat_Friends_list.this);
			pDialog.setMessage(Html.fromHtml("Loading wait..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String temp_ID = WW_StaticClass.siteid.toString().substring(1, WW_StaticClass.siteid.toString().length()-1);
			System.out.println("Multiple Site IDs : "+temp_ID);
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetAllEmployeeListByDevice );
			request.addProperty("Status", "Y");
			request.addProperty("Siteid", temp_ID);
			request.addProperty("userid", user_id);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetAllEmployeeListByDevice,
						envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i("Any CEE","Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				if (code.equals("000")) {
					first_called=false;
					status = true;
					Object mSoapObject3 =   mSoapObject1.getProperty(1);
					JSONObject jo = new JSONObject(mSoapObject3.toString());
					JSONArray jArray = jo.getJSONArray("EmpList");
					Log.i(TAG,"jArray : " + jArray.toString());
					Userid = new ArrayList<String>();
					Username = new ArrayList<String>();
					JSONObject jsonObject;

					name_id.put("Group 1", "group_1");
					Username.add("Group 1");
					Userid.add("group_1");

					for(int i=0;i<jArray.length();i++){
						jsonObject = jArray.getJSONObject(i);
						if(!jsonObject.getString("UserID").equals(user_id)){
							String temp_id = jsonObject.getString("UserID");
							String chat_channel="";
							if(!jsonObject.getString("UserID").contains("group")){
								if(Integer.parseInt(user_id)<Integer.parseInt(temp_id)){
									chat_channel = user_id+"_"+temp_id;
								}else{
									chat_channel = temp_id+"_"+user_id;
								}
							}else{
								chat_channel = temp_id;
							}

							System.out.println("Chat Channel : "+chat_channel);
							counter=0;

							//							subscribe(chat_channel);
							//
							//							multi_channel.add(chat_channel);

							Username.add(jsonObject.getString("UserName"));
							Userid.add(jsonObject.getString("UserID"));
							name_id.put(jsonObject.getString("UserName"), jsonObject.getString("UserID"));
						}else{
							Current_Uname = jsonObject.getString("UserName");
						}
					}

					Log.i(TAG, "id = "+Userid);
					Log.i(TAG, "name = "+Username);
				}

			}
			catch(SocketTimeoutException e){
				e.printStackTrace();
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
			//			pDialog3.dismiss();
			pDialog.dismiss();
			if(status==true){
				if(Username.size()>0){
					lviewAdapter = new ListViewAdapter(mContext, Username, Userid);
					//								CustomList adapter = new CustomList(Chat_Friends_list.this, Username, channel_count);
					//					ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					//							mContext, 
					//							android.R.layout.simple_list_item_1,
					//							Username );

					roomlist.setAdapter(lviewAdapter);
				}
			}
			//			GetCountAndName();
			new AllEmployeeList().execute();
		}
	}

	public void GetCountAndName(){
		for (int i = 0; i < multi_channel.size(); i++) {
			History(multi_channel.get(i));
		}
	}

	public class AllEmployeeList extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Chat_Friends_list.this);
			pDialog.setMessage(Html.fromHtml("Loading wait..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//			String temp_ID = WW_StaticClass.siteid.toString().substring(1, WW_StaticClass.siteid.toString().length()-1);
			//			System.out.println("Multiple Site IDs : "+temp_ID);
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.METHOD_NAME_GetAllEmployeeList );
			request.addProperty("Status", "Y");
			request.addProperty("Siteid", "1");
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetAllEmployeeList,
						envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i("Any CEE","Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				if (code.equals("000")) {
					status = true;
					Object mSoapObject3 =   mSoapObject1.getProperty(1);
					JSONObject jo = new JSONObject(mSoapObject3.toString());
					JSONArray jArray = jo.getJSONArray("EmpList");
					Log.i(TAG,"jArray : " + jArray.toString());
					search_id = new ArrayList<String>();
					search_name = new ArrayList<String>();
					JSONObject jsonObject;

					for(int i=0;i<jArray.length();i++){
						jsonObject = jArray.getJSONObject(i);
						if(!jsonObject.getString("UserID").equals(user_id)){

							search_name.add(jsonObject.getString("UserName"));
							search_id.add(jsonObject.getString("UserID"));
							search_name_id.put(jsonObject.getString("UserName"), jsonObject.getString("UserID"));
						}else{
							//							Current_Uname = jsonObject.getString("UserName");
						}
					}

					Log.i(TAG, "id = "+Userid);
					Log.i(TAG, "name = "+Username);
				}

			}
			catch(SocketTimeoutException e){
				//				server_status = true;
				e.printStackTrace();
			}
			catch(Exception e){
				//				server_status = true;
				e.printStackTrace();
			}

			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//			pDialog3.dismiss();
			pDialog.dismiss();
		}
	}


	public class ListViewAdapter extends BaseAdapter{
		Context context;
		ArrayList<String> title;
		ArrayList<String> description;

		public ListViewAdapter(Context context, ArrayList<String> title, ArrayList<String> description) {
			super();
			this.context = context;
			this.title = title;
			this.description = description;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return title.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		private class ViewHolder {
			TextView txtViewTitle;
			TextView txtViewDescription;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			ViewHolder holder;
			LayoutInflater inflater = getLayoutInflater();

			if (convertView == null)
			{
				convertView = inflater.inflate(R.layout.chat_customlist, null);
				holder = new ViewHolder();
				holder.txtViewTitle = (TextView) convertView.findViewById(R.id.name);
				holder.txtViewDescription = (TextView) convertView.findViewById(R.id.count);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder) convertView.getTag();
			}

			String firstWord = "";
			if(title.get(position).contains(" ")){
				firstWord= title.get(position).substring(0, title.get(position).indexOf(" "));
			}else{
				firstWord = title.get(position);
			}

			String count = "0";

			holder.txtViewTitle.setText(title.get(position));
			System.out.println("Unread MSG Array : "+unread_msg);
			if(unread_msg.size()>0){
				if(unread_msg.contains(firstWord)){
					count = String.valueOf(Collections.frequency(unread_msg, firstWord));
					holder.txtViewTitle.setTypeface(null,Typeface.BOLD);
					holder.txtViewTitle.setText(title.get(position) +" ( " +count +" ) ");
					holder.txtViewDescription.setTypeface(null,Typeface.BOLD);
					//					holder.txtViewDescription.setVisibility(View.VISIBLE);
					//					holder.txtViewDescription.setText(count);
					//					unread_msg.clear();
				}else{
					holder.txtViewDescription.setVisibility(View.GONE);
					holder.txtViewTitle.setTypeface(null,Typeface.NORMAL);
				}
			}else{
				holder.txtViewDescription.setVisibility(View.GONE);
				holder.txtViewTitle.setTypeface(null,Typeface.NORMAL);
			}

			return convertView;
		}

	}

	public class CustomList extends ArrayAdapter<String>{

		private final Activity context;
		private final ArrayList<String> web;
		private final ArrayList<String> imageId;
		public CustomList(Activity context,
				ArrayList<String> web, ArrayList<String> imageId) {
			super(context, R.layout.chat_customlist, web);
			this.context = context;
			this.web = web;
			this.imageId = imageId;

		}
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView= inflater.inflate(R.layout.chat_customlist, null, true);
			TextView name = (TextView) rowView.findViewById(R.id.name);
			TextView count = (TextView) rowView.findViewById(R.id.count);

			name.setText(web.get(position));
			count.setText(imageId.get(position));

			return rowView;
		}
	}

	private void subscribe(String channel) {

		try {
			pubnub.subscribe(channel, new Callback() {
				@Override
				public void connectCallback(String channel,
						Object message) {
					notifyUser("SUBSCRIBE : CONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());
				}

				@Override
				public void disconnectCallback(String channel,
						Object message) {
					notifyUser("SUBSCRIBE : DISCONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());
				}

				@Override
				public void reconnectCallback(String channel,
						Object message) {
					notifyUser("SUBSCRIBE : RECONNECT on channel:"
							+ channel
							+ " : "
							+ message.getClass()
							+ " : "
							+ message.toString());
				}

				@Override
				public void successCallback(String channel,
						Object message) {

					notifyUser(message.toString());
				}

				@Override
				public void errorCallback(String channel,
						PubnubError error) {
					notifyUser("SUBSCRIBE : ERROR on channel "
							+ channel + " : "
							+ error.toString());
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	Object response_ex;
	boolean response_true=false;

	int counter=0,replied=0;

	public void History(String channel){
		Callback callback = new Callback() {
			public void successCallback(String channel, Object response) {
				System.out.println(response.toString());
				response_ex = response;
				response_true = true;

				if(response_ex instanceof JSONArray){
					try {
						String temp_St = response_ex.toString().substring(1, response_ex.toString().length()-1);
						JSONArray jarray = new  JSONArray(temp_St);
						for (int i = 0; i < jarray.length(); i++) {
							counter=i;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				channel_count.add(String.valueOf(counter));
				replied++;

				if(multi_channel.size()==replied){
					if(status==true){
						if(Username.size()>0){
							Toast.makeText(mContext, "Setting list", Toast.LENGTH_SHORT).show();
							CustomList adapter = new CustomList(Chat_Friends_list.this, Username, channel_count);
							roomlist.setAdapter(adapter);
						}
					}
				}
			}
			public void errorCallback(String channel, PubnubError error) {
				System.out.println(error.toString());
			}
		};
		pubnub.history(channel, 100, true, callback);
	}

	private void notifyUser(Object message) {

		try {
			if (message instanceof JSONObject) {
				final JSONObject obj = (JSONObject) message;

				this.runOnUiThread(new Runnable() {
					public void run() { 
						Log.d(" JSON Received msg : ", String.valueOf(obj));
					}
				});
			} 
			else if (message instanceof String) {
				final String obj = (String) message;
				Log.d(" String Received msg : ", obj.toString());

				this.runOnUiThread(new Runnable() {
					public void run() { 
						Log.d(" JSON Received msg : ", String.valueOf(obj));
					}
				});

			}
			else if (message instanceof JSONArray) {
				final JSONArray obj = (JSONArray) message;
				this.runOnUiThread(new Runnable() {
					public void run() {
						Log.d(" jArray Received msg : ", obj.toString());
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}