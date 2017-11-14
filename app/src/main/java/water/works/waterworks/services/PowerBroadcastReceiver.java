package water.works.waterworks.services;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import water.works.waterworks.R;


/**
 * Created by glenns on 09/08/13.
 */
public class PowerBroadcastReceiver extends BroadcastReceiver {
	static Activity obj;
    @Override
    public void onReceive(final Context ctx, Intent intent){
    	boolean yesno = isAppInForeground(ctx);
    	if(yesno){
//    		Intent i = new Intent(ctx, TestActivity.class); 
//    	    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
//    	    ctx.startActivity(i);
    		
    		final Dialog d = new Dialog(ctx);
    		d.requestWindowFeature(Window.FEATURE_NO_TITLE); 	
    		d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    		d.setTitle("Testing");
    		d.setCancelable(true);
    		d.setContentView(R.layout.dialog_deck_supervisor_notification);
    		d.getWindow().setLayout(800, LayoutParams.WRAP_CONTENT);
    		Button btn_click =(Button)d.findViewById(R.id.btn_dismiss_noti);
    		btn_click.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					d.cancel();
					Toast.makeText(ctx, "Click", Toast.LENGTH_LONG).show();
				}
			});
    		d.show();
    	}else{
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentText("Power cable has been plugged in")
//                .setContentTitle("Attention");
//        NotificationManager manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, builder.build());
    	}
    }
    
    public static boolean isAppInForeground(Context context) {
    	  List<RunningTaskInfo> task =
    	      ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
    	          .getRunningTasks(1);
    	  if (task.isEmpty()) {
    	    return false;
    	  }else{
    		  String c = task.get(0).topActivity.getClassName();
    		  try {
				Class<?> myClass = Class.forName(c);
				  obj = (Activity) myClass.newInstance();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    		  //Noe pass your object here
    		  
    		  Log.d("topActivity", "CURRENT Activity ::" + task.get(0).topActivity.getShortClassName());
//    		    ComponentName componentInfo = task.get(0).topActivity;
//    		    componentInfo.getPackageName();
    	  }
    	  return task
    	      .get(0)
    	      .topActivity
    	      .getPackageName()
    	      .equalsIgnoreCase(context.getPackageName());
    	}
}
