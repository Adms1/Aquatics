package water.works.waterworks.chat;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import water.works.waterworks.R;
import water.works.waterworks.utils.SOAP_CONSTANTS;


public class GcmIntentService_Pubnub extends IntentService {
	public static int NOTIFICATION_ID = 1;
	private static final String TAG = "PubnubGcm";
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	public static final String ACTION_MyIntentService = "com.example.androidintentservice.RESPONSE";
	public static final String ACTION_MyUpdate = "com.example.androidintentservice.UPDATE";
	public static final String EXTRA_KEY_IN = "EXTRA_IN";
	public static final String EXTRA_KEY_OUT = "EXTRA_OUT";
	public static final String EXTRA_KEY_UPDATE = "EXTRA_UPDATE";
	 String extraOut;

	Bundle extras ;
	public GcmIntentService_Pubnub() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.
					MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " +
						extras.toString());
			} else if (GoogleCloudMessaging.
					MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

				sendNotification(extras.toString());
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver_pubnub.completeWakefulIntent(intent);
	}


	private void sendNotification(String msg) {
		String message="";
		String firstWord = "";
		message = extras.getString("message");
		SOAP_CONSTANTS.FromName = extras.getString("FromName");
		SOAP_CONSTANTS.ChannelName = extras.getString("ChannelName");
		SOAP_CONSTANTS.From = "noti";
		SOAP_CONSTANTS.FirstMsg = message;
		if(SOAP_CONSTANTS.FromName.contains(" ")){
			firstWord= SOAP_CONSTANTS.FromName.substring(0, SOAP_CONSTANTS.FromName.indexOf(" "));
		}else{
			firstWord = SOAP_CONSTANTS.FromName;
		}
		mNotificationManager = (NotificationManager)
				getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, Chat_Room.class), 0);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.chat)
		.setContentTitle(SOAP_CONSTANTS.FromName)
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText(message))
		.setContentText(message);

		mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE);
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setAutoCancel(true);
		if(!SOAP_CONSTANTS.current_channel.equals(SOAP_CONSTANTS.ChannelName)){
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
			Chat_Friends_list.unread_msg.add(firstWord);
			sendNotification();
			NOTIFICATION_ID++;
		}
	}

	public void sendNotification(){
		Intent intentResponse = new Intent();
		intentResponse.setAction(ACTION_MyIntentService);
//		intentResponse.addCategory(Intent.CATEGORY_DEFAULT);
//		intentResponse.putExtra(EXTRA_KEY_OUT, "");
		sendBroadcast(intentResponse);
	}
}