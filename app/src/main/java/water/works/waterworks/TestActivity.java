package water.works.waterworks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


public class TestActivity extends Activity {
	ListView lv_selection;
	public static String Tag = "TestActivity";
	boolean server_response = false;
	boolean getsitelist = false;
	ArrayList<String> SiteID,SiteName,Upper_Manager_userid,Upper_Manager_username,Manager_userid,Manager_username,Staff_userid,Staff_username,
	Inst_userid,Inst_username,Supervisor_userid,Supervisor_username,Mainten_userid,Mainten_username;
@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		new GetSiteList().execute();
		TextView tv = (TextView)findViewById(R.id.tv_tv_tv);
		 lv_selection = (ListView)findViewById(R.id.list_list_list);
		  tv.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(lv_selection.getVisibility()== View.VISIBLE){
					lv_selection.setVisibility(View.GONE);
				}
				else{
					lv_selection.setVisibility(View.VISIBLE);
				}
			}
		});
		 
}
private class GetSiteList extends AsyncTask<Void, Void, Void> {
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
				SOAP_CONSTANTS.METHOD_NAME_GetSiteList);
		request.addProperty("token", "162BD143-DE0F-4798-A4DA-54C04D38001D");
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11); // Make an Envelop for sending as whole
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		Log.i("Request",  "Request = " + request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				SOAP_CONSTANTS.SOAP_ADDRESS);
		try {
			androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_GetSiteList,
					envelope); // Calling Web service
			SoapObject response = (SoapObject)envelope.getResponse();
//			SoapPrimitive response =  (SoapPrimitive) envelope.getResponse();
			 Log.i(Tag,"" + response.toString());
			 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
			 Log.i(Tag, "mSoapObject1="+mSoapObject1);
			 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
			 Log.i(Tag, "mSoapObject2="+mSoapObject2);
			 String code = mSoapObject2.getPropertyAsString(0).toString();
			 Log.i("Code", code);
			if(code.equalsIgnoreCase("000")){
				getsitelist = true;
				Object mSoapObject3 =  mSoapObject1.getProperty(1);
				Log.i(Tag, "mSoapObject3="+mSoapObject3);
				String resp = mSoapObject3.toString();
				JSONObject jo = new JSONObject(resp);
				JSONArray jArray = jo.getJSONArray("Sites");
				Log.i(Tag,"jArray : " + jArray.toString());
				SiteID = new ArrayList<String>();
				SiteName = new ArrayList<String>();
				JSONObject jsonObject ;
				for(int i=0;i<jArray.length();i++){
					jsonObject = jArray.getJSONObject(i);
					SiteName.add(jsonObject.getString("SiteName"));
					SiteID.add(jsonObject.getString("SiteID"));
				}
			}
			else{
				getsitelist =false;
			}
		}
		catch(JSONException e){
			e.printStackTrace();
			server_response =true;
		}
		catch(Exception e){
			server_response =true;
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(server_response){
			SingleOptionAlertWithoutTitle.ShowAlertDialog(
					TestActivity.this,"WaterWorks", "SERVER DOWN", "OK");
			server_response = false;
		}
		else{
			if(!getsitelist){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						TestActivity.this,"WaterWorks",
						"No site found.", "OK");
			}
			else{
				 ArrayAdapter<String> adapter = new ArrayAdapter<String>(TestActivity.this, android.R.layout.simple_list_item_1, SiteName);
				  lv_selection.setAdapter(adapter);
				  lv_selection.setOnItemClickListener(new OnItemClickListener() {


					public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
						Toast.makeText(TestActivity.this, SiteName.get(position)
								, Toast.LENGTH_LONG).show();
						// TODO Auto-generated method stub
//						btn_site_list.setText(SiteName.get(position));
//						siteid = Integer.parseInt(SiteID.get(position));
//						usertype = 1;
//						new GetUsers().execute();
					}
				});

			}
		}
	}
}

}