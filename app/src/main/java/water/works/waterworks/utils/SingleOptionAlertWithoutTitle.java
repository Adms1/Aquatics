package water.works.waterworks.utils;
 


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import water.works.waterworks.R;


/**
 * This class used to display Alert box without title
 */
public class SingleOptionAlertWithoutTitle {
	Context mContext;


	public static void ShowAlertDialog(Context context,String Heading ,String message,
			String buttonText) {
		// alert dialog functionality
		// this.mContext = context;
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		// hide title bar
		// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.setTitle(Heading);
		alertDialog.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
		alertDialog.setCanceledOnTouchOutside(false); 
		alertDialog.setCancelable(false);
		// set the message
		alertDialog.setMessage(message);
		// set button1 functionality
		alertDialog.setButton(buttonText,
				new DialogInterface.OnClickListener() {


					public void onClick(DialogInterface dialog, int which) {
						// close dialog

						dialog.cancel();

					}
				});
		// show the alert dialog
		alertDialog.show();
	}

	public static void ShowAlertDialogHeading(
			Context context, String Heading ,String message,
			String buttonText) {
		// TODO Auto-generated method stub
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		// hide title bar
		// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.setTitle(Heading);
		alertDialog.setCanceledOnTouchOutside(false); 
		alertDialog.setCancelable(false);
		// set the message
		alertDialog.setMessage(message);
		// set button1 functionality
		alertDialog.setButton(buttonText,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// close dialog

						dialog.cancel();

					}
				});
		// show the alert dialog
		alertDialog.show();
	}


	public static void ShowAlertDialogHead(Context context,
			String Heading, String message,String buttonText) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();
				// hide title bar
				// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				alertDialog.setTitle(Heading);
				alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.setCancelable(false);
				// set the message
				alertDialog.setMessage(message);
				// set button1 functionality
				alertDialog.setButton(buttonText,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int which) {
								// close dialog

								dialog.cancel();

							}
						});
				// show the alert dialog
				alertDialog.show();
	}

	
	

}
