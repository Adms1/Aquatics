package water.works.waterworks.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;

@SuppressWarnings("unused")
public class Utility {
	public Dialog dia;
	 private DatePicker date_picker;
	 private static TimePicker time_picker;
	  static final int DATE_DIALOG_ID = 100;
	     private static int year;
	     //static android.support.v7.widget.PopupMenu popmenu;
	     private static int month;
	
	     private static int day;
	     private static int min=0;
	     private static int hrs=0;
	     private static int sec=00;
	 	static Dialog picker;
	 	static Button set;
	 	static DatePicker datep;
	static JSONObject json;
	// static QuickAction quickAction;
	private String ActioItem;
	private static String imei;
	private static JSONObject json_LL;
	static Context Cntxt;

	public void CreateToastMessage(Context activity, String string) {
		// TODO Auto-generated method stub
		Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
	}

//-------------------- Checking Internet Connection -------------------------------
	public static boolean isNetworkConnected(Context ctxt) {
		ConnectivityManager cm = (ConnectivityManager) ctxt
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}
	
	
	
	
/**---------------------- Shared Prefrences -----------------------------------------**/
	/*public static void saveStringSharedPreference(String key, String value,
			Context mContext) {
		SharedPreferences mPreferences = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		Editor editor = mPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String Get_Prefrance_Value(Context context, String Key_Value) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String Result_Val = pref.getString(Key_Value, null);

		return Result_Val;

	}
	
	public static void Delete_Prefrance_Value(Context context, String Key_Value) {
		SharedPreferences pref = context.getSharedPreferences("My Prefrences",
				0);
		pref.edit().remove(Key_Value).commit();

	}*/

/**--------------------------------------------- Yes / No Alert Dialog -----------------------------------------**/
	public static void AlerDialg(final Context user_List, String aLERT_TITLE,
			String aLERT_MESSAGE, final String str, JSONObject jsonOb){
		// TODO Auto-generated method stub
		JSONObject json=jsonOb;
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				user_List);

		// set title
		alertDialogBuilder.setTitle(aLERT_TITLE);

		// set dialog message
		alertDialogBuilder
				.setMessage(aLERT_MESSAGE)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	
	public static String Imei_Number(Context Cntxt){
		TelephonyManager tm = (TelephonyManager) Cntxt.getSystemService(Context.TELEPHONY_SERVICE);
        // get IMEI
           imei= tm.getDeviceId();
       	Log.v("imei",""+imei);
		return imei;
	}
	//public static void QuickActionApi(ArrayList<String> quickActionchList,
//			Tracking cntxt, View v, final String string) {
//		// TODO Auto-generated method stub
//		Cntxt = cntxt;
//	 
//		quickAction = new QuickAction(Cntxt);
//		//ActioItem = string;
//		
//		//quickAction = new QuickAction2(fragmentActivity);
//		
//		
//		//Log.i("STRING FROM BEHIND", "" + ActioItem);
//		for (int i = 0; i < quickActionchList.size(); i++) {
//			ActionItem nextItem = new ActionItem(i, quickActionchList.get(i));
//			quickAction.addActionItem(nextItem, i, quickActionchList.size());
//
//		}
//
//		quickAction.show(v);
//		quickAction
//				.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
//
//					private String val;
//
//					public void onItemClick(QuickAction source, int pos,
//							int actionId) {
//						if(string.equals("TRACKING")){
//							Staticclass.U_ID=Staticclass.USER_ID_LIST.get(pos);
//							Staticclass.edit.setText(Staticclass.USER_NAME_LIST.get(pos));
//						}
//					}
//				});
//	}
//	public static void DatePicker(final Tracking tracking) {
//		// TODO Auto-generated method stub
//		picker = new Dialog(tracking);
//		picker.setContentView(R.layout.picker_frag);
//		picker.setTitle("Select Date and Time");
//
//		datep = (DatePicker) picker.findViewById(R.id.datePicker);
//		time_picker = (TimePicker) picker.findViewById(R.id.timePicker1);
//		// timep = (TimePicker)picker.findViewById(R.id.timePicker1);
//		set = (Button) picker.findViewById(R.id.btnSet);
//
//		set.setOnClickListener(new View.OnClickListener() {
//
//			private Date date;
//
//			@SuppressLint("SimpleDateFormat")
//			@Override
//			public void onClick(View view) {
//				// TODO Auto-generated method stub
//				//Log.v("DATE COUNT IS ", "" + COUNT);
//				month = datep.getMonth() + 1;
//				day = datep.getDayOfMonth();
//				year = datep.getYear();
//				
//				min=time_picker.getCurrentMinute();
//				hrs=time_picker.getCurrentHour();
//				if(hrs==0 && min==0){
//					Common_Utilities.CreateToastMessage(tracking, "PLEASE SELECT THE TIME");
//				}else{
//					
//					Staticclass.edit.setText(year+"-"+month+"-"+day+" "+hrs+":"+min+":"+sec);
//				}
//				//sec=time_picker.getCurr;
//				
//				// hour = timep.getCurrentHour();
//				// minute = timep.getCurrentMinute();
//				// time.setText("Time is "+hour+":" +minute);
//				//if (COUNT == 1) {
//					//SDate.setText(day + "/" + month + "/" + year);
//					SimpleDateFormat dateConverter = new SimpleDateFormat(
//							"dd/MM/yyyy");
//					//						date = (Date) dateConverter.parse(SDate.getText()
////								.toString());
//					// date1 = (Date) dateConverter.parse(EDate);
//					//StaticClass.SDate = date;
//					Log.i("DATE", "" + date);
//					//StaticClass.SetSdate(date);
//			//	}
//					//else if (COUNT == 2) {
////					EDate.setText(day + "/" + month + "/" + year);
////					SimpleDateFormat dateConverter = new SimpleDateFormat(
////							"dd/MM/yyyy");
////					try {
////						date = (Date) dateConverter.parse(EDate.getText()
////								.toString());
////						StaticClass.EDate = date;
////						// date1 = (Date) dateConverter.parse(EDate);
////						Log.i("EDATE", "" + date);
////
////					} catch (ParseException e1) {
////						// TODO Auto-generated catch block
////						e1.printStackTrace();
////					}
////					StaticClass.SetEdate(date);
////					// StaticClass.SetEdate(EDate.getText().toString());
////				}
//				picker.dismiss();
//			}
//		});
//		picker.show();
//
//	}

//	public static void GetLocation(Context cntxt2) {
//		// TODO Auto-generated method stub
//		GPSTracker Gps=new GPSTracker(Cntxt);
//		Log.v("LATITUDEEEEEEEEEEEEEE", ""+Gps.getLatitude());
//	}

/**--------------------------------------------- Email Validation -----------------------------------------**/
	public boolean isEmailValid(String email) {
		boolean isValid;
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches()) {
			isValid = true;
		} else {

			Log.i(".....Email", "Not valid");

			isValid = false;

		}
		return isValid;
	}

/**--------------------------------------------- Hit Server code -----------------------------------------**/
	public String Hit_Server(JSONObject mJsonToken, String string) {
		String status = "";

		HttpPost httppost = null;
		
		try {
			httppost = new HttpPost(string);
			HttpParams myParams = new BasicHttpParams();
//			HttpConnectionParams.setConnectionTimeout(myParams, 10000);
			httppost.setHeader("Content-type", "application/json");
			
			HttpClient httpclient = new DefaultHttpClient(myParams);
			StringEntity se;
			if (mJsonToken.toString() == null) {
				se = new StringEntity("", HTTP.UTF_8);
			} else {
				se = new StringEntity(mJsonToken.toString(), HTTP.UTF_8);
			}
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			httppost.setEntity(se);
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String getResult = EntityUtils.toString(entity);
			status = getResult;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
		}
		return status;

	}
	
	/*----------------------------------------get bitmap------------------------------*/
	public static Bitmap getBitmapFromURL(String src,Context context) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			// Log.e("Bitmap","returned");
			if (myBitmap != null) {
				return myBitmap;
			} else {
				// getBitmapFromURL(CONSTANTS.getDefaultIcon);
			}

			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Exception", e.getMessage());
//			Writer writer = new StringWriter();
//			PrintWriter printWriter = new PrintWriter(writer);
//			e.printStackTrace(printWriter); 
//			MailUtils.sendMail("" + writer.toString());
			return null;
		}
	}
	
	public static String convertBitmapUriToBase64(File mfile, Context mContext) {

		String mBase64String = "";
		Bitmap mPhoto;

		String filePath = mfile.getAbsoluteFile().toString();
		String ext = filePath.substring(filePath.lastIndexOf("/"),
				filePath.length());

		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inSampleSize = 100;
		mPhoto = BitmapFactory.decodeFile(filePath, options);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (ext.equalsIgnoreCase("png")) {
			mPhoto.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm
		} else {
			mPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm
		}
		byte[] b = baos.toByteArray();
		System.out.println("ByteSize : "+b.length);
		
		mBase64String = Base64.encodeToString(b, Base64.DEFAULT);
		try {
			b = null;
			baos.flush();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mBase64String;

	}
	
	
}

