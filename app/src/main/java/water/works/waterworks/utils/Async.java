package water.works.waterworks.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

public class Async extends AsyncTask<Void, Void, Void>{
	Context cntx;
	int class_no;
	String str,mStringResponse;
	JSONObject j_object;
	
	private ProgressDialog progressDialog;

	public Async(Context cntxt, int id, JSONObject jsonToken, String string)
	{
		cntx=cntxt;
		progressDialog = new ProgressDialog(cntx);
		class_no=id;
		str=string;
		j_object=jsonToken;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		boolean isConnected=Utility.isNetworkConnected(cntx);
		if(isConnected)
		{
				progressDialog.setMessage("Please wait...");
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(false);
				progressDialog.show();
		} else {
			new Utility().CreateToastMessage(cntx, "No Internt Connection");
		((Activity) cntx).finish(); 
		super.cancel(true);
		
		}
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		switch(class_no)
		{
		case 1:
			mStringResponse=new Utility().Hit_Server(j_object, "http://office.waterworksswimonline.com/WWWebService/Service.asmx/Login");
			System.out.println("LOGIN RESPONCEEEEEEEEEE"+mStringResponse);
			Log.v("RESPONSE LOGIN ", ""+mStringResponse);
			break;
		case 2:
			mStringResponse=new Utility().Hit_Server(j_object, "http://office.waterworksswimonline.com/WWWebService/Service.asmx/GetPoolList");
			Log.v("RESPONSE Test ", ""+mStringResponse);
			default:
				break;
		}
		
		return null;
	}

}
