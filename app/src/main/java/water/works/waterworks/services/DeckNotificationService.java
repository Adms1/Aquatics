package water.works.waterworks.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.WW_StaticClass;


public class DeckNotificationService extends Service {
	Handler mHandler = new Handler();
	private boolean connectiontimeout = false,getrespose = false;
	DeckNotification  dn;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dn = new DeckNotification();
		new Thread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(5000);
						mHandler.post(new Runnable() {


							public void run() {
								// TODO Auto-generated method stub
								new DeckNotification().execute();
//								Toast.makeText(getApplicationContext(), "hi", 1)
//										.show();
							}
						});
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dn.cancel(true);
		
	}

	private class DeckNotification extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if(isCancelled()){
				return null;
			}else{
			SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
					SOAP_CONSTANTS.GetDeckAssistList_Method);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("uid", WW_StaticClass.InstructorID);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					SOAP_CONSTANTS.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(SOAP_CONSTANTS.GetDeckAssistList_Action,
						envelope); // Calling Web service
				SoapObject response =  (SoapObject) envelope.getResponse();
				 Log.i("here","Result : " + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if (code.equals("000")) {
					 getrespose = true;
				 }else{
					 getrespose= false;
				 }
				
			} catch (SocketTimeoutException e) {
				connectiontimeout = true;
				e.printStackTrace();
			} catch (SocketException e) {
				connectiontimeout = true;
				e.printStackTrace();
			}
			// catch(JSONException e){
			// server_status=true;
			// e.printStackTrace();
			// }
			catch (Exception e) {
				// server_status=true;
				e.printStackTrace();
			}
			return null;
			}
			
		}
@Override
protected void onCancelled() {
	// TODO Auto-generated method stub
	super.onCancelled();
	dn = null;
}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(connectiontimeout){
				connectiontimeout=false;
			}else{
				if(getrespose){
					getrespose = false;
				}else{
					Log.i("Notification", "No detail found");
				}
			}
		}
	}

}
