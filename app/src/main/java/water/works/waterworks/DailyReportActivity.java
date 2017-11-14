package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class DailyReportActivity extends Activity implements OnClickListener {
	TextView tv_day,tv_date,tv_instructorname;
	Boolean isInternetPresent = false;
	String currentDateandTime;
	GridView grid_report_option;
	String[] Reports;
	ReportsAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_report);
		isInternetPresent = Utility
				.isNetworkConnected(DailyReportActivity.this);
		if(isInternetPresent){
			Initialization();
			SetScreenDetails();
			Date date = new Date();
	        System.out.println("Date-->" + date);
	        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	        System.out.println("New Date--->" + format.format(date));
	        currentDateandTime = format.format(date);
		}
		else{
			onDetectNetworkState().show();
		 }
	}
	private void Initialization() {
		// TODO Auto-generated method stub
		tv_date = (TextView)findViewById(R.id.tv_dl_date);
		tv_day = (TextView)findViewById(R.id.tv_dl_day);
		tv_instructorname = (TextView)findViewById(R.id.tv_dl_name);
		Reports = getResources().getStringArray(R.array.daily_reports);
		adapter = new ReportsAdapter(getApplicationContext(), Reports);
		grid_report_option = (GridView)findViewById(R.id.dailyreport);
		grid_report_option.setAdapter(adapter);
		grid_report_option.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				// TODO Auto-generated method stub
				if(position==0){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://forms.waterworksswim.com/survey_aquatics/index.php");
					startActivity(i);
				}
			else if(position==1){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://forms.waterworksswim.com/survey_aquatics/area_quality.php");
					startActivity(i);
				}
				else if(position==2){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://forms.waterworksswim.com/survey_aquatics/aquatic_manager.php");
					startActivity(i);
					
				}
				else if(position==3){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://forms.waterworksswim.com/survey_aquatics/aquatic_quality.php");
					startActivity(i);
				}
				else if(position==4){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://reports.waterworksswim.com/reports/aquatics/train_lead.php");
					startActivity(i);
				}
				else if(position==5){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://reports.waterworksswim.com/reports/aquatics/train.php");
					startActivity(i);
				}
				else if(position==6){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://reports.waterworksswim.com/reports/aquatics/inst_sr.php");
					startActivity(i);
				}
				else if(position==7){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://reports.waterworksswim.com/reports/aquatics/deck_guard_sr.php");
					startActivity(i);
				}
				else if(position==8){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://reports.waterworksswim.com/reports/aquatics/deck_guard.php");
					startActivity(i);
					
				}
				else if(position==9){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://reports.waterworksswim.com/reports/aquatics/lafit_inst.php");
					startActivity(i);
					
				}
				else if(position==10){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://reports.waterworksswim.com/reports/aquatics/lafit_mgr.php");
					startActivity(i);
					
				}
				else if(position==11){
					Intent i =  new Intent(getApplicationContext(),DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url", "http://reports.waterworksswim.com/reports/aquatics/lafit_lead_inst.php");
					startActivity(i);
					
				}
				else{
					Toast.makeText(getApplicationContext(), Reports[position].toString(), 1).show();
				}
			}
		});
		
		
	}
	private void SetScreenDetails() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		int Day_Name = c.get(Calendar.DAY_OF_WEEK);
		int Date =  c.get(Calendar.DATE);
		int Month = c.get(Calendar.MONTH);
		String day_name = null;
		if(Day_Name == 1){
			day_name = "SUNDAY";
		}
		else if(Day_Name == 2){
			day_name = "MONDAY";
		}
		else if(Day_Name == 3){
			day_name = "TUESDAY";
		}
		else if(Day_Name == 4){
			day_name = "WEDNESDAY";
		}
		else if(Day_Name == 5){
			day_name = "THURSDAY";
		}
		else if(Day_Name == 6){
			day_name = "FRIDAY";
		}
		else if(Day_Name == 7){
			day_name = "SATURDAY";
		}
		 tv_instructorname.setText(WW_StaticClass.UserName);
			tv_day.setText(day_name);
			String m,d;
			if(String.valueOf(Month).length()==1){
				m = "0"+(Month+1);
			}
			else{
				m =""+(Month+1);
			}
			if(String.valueOf(Date).length()==1){
				d = "0"+(Date);
			}
			else{
				d =""+(Date);
			}
			
			tv_date.setText(m + "/" + d);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		decorView.setSystemUiVisibility(uiOptions);
		isInternetPresent = Utility
				.isNetworkConnected(DailyReportActivity.this);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        DailyReportActivity.this.finish();
		    }
		})       
		.setPositiveButton("Οk",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		    }
		});
		    return builder1.create();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(isInternetPresent){
			switch (v.getId()) {
			case R.id.btn_back:
					finish();
				break;
			}
		}
		else{
			onDetectNetworkState().show();
		}
	}
	
	public class ReportsAdapter extends BaseAdapter {
		Context context;
		String Reports[];
		
		public ReportsAdapter(Context context, String[] reports) {
			super();
			this.context = context;
			Reports = reports;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Reports.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Reports[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View grid;
		      LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		          if (convertView == null) {
		            grid = new View(context);
		            grid = inflater.inflate(R.layout.reports_item, null);
		            TextView tv_item = (TextView) grid.findViewById(R.id.tv_reports_item);
		            tv_item.setText(Reports[position]);
		          } else {
		            grid = (View) convertView;
		          }
		      return grid;
		}
		
	}
}
