package water.works.waterworks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
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

import java.util.Calendar;

import water.works.waterworks.services.DeckNotificationService;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class ReportActivity extends Activity implements OnClickListener {
	TextView mtv_name, mtv_day, mtv_date, mtv_time;
	String time;
	public static String TAG = "ReportActivity";
	String am_pm;
	java.util.Date noteTS;
	Boolean isInternetPresent = false;
	GridView grid_report_option;
	String[] Reports;
	ReportsAdapter adapter;
	Thread t;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);

		isInternetPresent = Utility.isNetworkConnected(ReportActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		}
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		int Day_Name = c.get(Calendar.DAY_OF_WEEK);
		int Date = c.get(Calendar.DATE);
		int Month = c.get(Calendar.MONTH);
		String day_name = null;
		if (Day_Name == 1) {
			day_name = "SUN";
		} else if (Day_Name == 2) {
			day_name = "MON";
		} else if (Day_Name == 3) {
			day_name = "TUES";
		} else if (Day_Name == 4) {
			day_name = "WED";
		} else if (Day_Name == 5) {
			day_name = "THUR";
		} else if (Day_Name == 6) {
			day_name = "FRI";
		} else if (Day_Name == 7) {
			day_name = "SAT";
		}
		Log.i("Time", "Time = " + hour + ":" + min + " " + " " + day_name + " "
				+ Date + "/" + Month);
		// ////////////
		Initialize();

		t = new Thread() {

			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(10);
						runOnUiThread(new Runnable() {

							public void run() {
								updateTextView();
								Calendar cc = Calendar.getInstance();
								int AM_PM = cc.get(Calendar.AM_PM);

								if (AM_PM == 0) {
									am_pm = "AM";
								} else {
									am_pm = "PM";
								}
							}

							private void updateTextView() {
								// TODO Auto-generated method stub
								noteTS = Calendar.getInstance().getTime();

								time = "hh:mm"; // 12:00
								mtv_time.setText(DateFormat
										.format(time, noteTS) + " " + am_pm);
							}
						});
					}
				} catch (InterruptedException e) {
				}
			}
		};

		t.start();

		mtv_name.setText(WW_StaticClass.UserName);
		mtv_day.setText(day_name);
		mtv_date.setText(Month + 1 + "/" + Date);
	}

	private void Initialize() {
		// TODO Auto-generated method stub
		mtv_date = (TextView) findViewById(R.id.tv_app_date);
		mtv_day = (TextView) findViewById(R.id.tv_app_day);
		mtv_name = (TextView) findViewById(R.id.tv_app_inst_name);
		mtv_time = (TextView) findViewById(R.id.tv_app_time);
		if (WW_StaticClass.UserLevel.contains("1")) {
			Reports = getResources().getStringArray(R.array.reports);
		} else {
			Reports = getResources().getStringArray(R.array.reports_instrucotr);
		}

		adapter = new ReportsAdapter(getApplicationContext(), Reports);
		grid_report_option = (GridView) findViewById(R.id.gv_rep);
		grid_report_option.setAdapter(adapter);
		grid_report_option.setOnItemClickListener(new OnItemClickListener() {

			// <item>Daily Report</item>
			// <item>View Individual Performance</item>
			// <item>Peer to Peer</item>
			// <item>Renewal Rates by Sites</item>
			// <item>Question of the Month</item>
			// <item>Swim Meet Instr. Points</item>
			// <item>Swim Team Rosters</item>
			// <item>Instr. Performance</item>
			// <item>One Day Program Rosters</item>
			// <item>View My Time Card</item>
			// <item>Dives &amp; Turns</item>
			// <item>Jr. Guard Prep</item>
			// <item>Boy Scout</item>
			// <item>Girl Scout</item>
			// <item>Cub Scout</item>
			// <item>Water Polo</item>
			// <item>Level Advancement Report</item>
			// <item>Instructor Report</item>
			// Today’s First Time Students

			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					// Intent i = new
					// Intent(getApplicationContext(),DetailReportActivity.class);
					// // i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/RankingReport.aspx?Userid="+WW_StaticClass.InstructorID);
					// // i.putExtra("url",
					// "http://192.168.1.201/newcode/RankingReport.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// SOAP_CONSTANTS.Report_Url+"RankingReport.aspx?Userid="+WW_StaticClass.InstructorID);
					// startActivity(i);
					Intent i = new Intent(getApplicationContext(),
							DailyReportActivity.class);
					startActivity(i);
				} else if (position == 1) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/RankingReport.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/RankingReport.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// SOAP_CONSTANTS.Report_Url+"RankingReport.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "ViewIndividualPerformance.aspx?Userid="
							+ WW_StaticClass.InstructorID);

					startActivity(i);
				} else if (position == 2) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url",
							"http://reports.waterworksswim.com/reports/office/peer.php?type=A&uid="
									+ WW_StaticClass.UserName);
					startActivity(i);

				} else if (position == 3) {
					String new_change = "&Lafitness=0";
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/RankingReport.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/RankingReport.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// SOAP_CONSTANTS.Report_Url+"ViewMyTimeCard.aspx?Userid="+WW_StaticClass.InstructorID
					// + "&Status=" + WW_StaticClass.UserLevel+new_change);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "AppRankingReport.aspx?Userid="
							+ WW_StaticClass.InstructorID + "&Status="
							+ WW_StaticClass.UserLevel);

					startActivity(i);
				} else if (position == 4) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					i.putExtra("url",
							"http://reports.waterworksswim.com/reports/office/Qmonth.php");
					startActivity(i);
				} else if (position == 5) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/swimmeetpoint.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/swimmeetpoint.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "swimmeetpoint.aspx?Userid="
							+ WW_StaticClass.InstructorID);
					startActivity(i);
				} else if (position == 6) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "SwimTeamRosterNew.aspx?Userid="
							+ WW_StaticClass.InstructorID);
					startActivity(i);
				}
				// else if(position==7){
				// Intent i = new
				// Intent(getApplicationContext(),DetailReportActivity.class);
				// i.putExtra("FROM", "");
				// // i.putExtra("url",
				// "http://office.waterworksswimonline.com/newcode/performancereport.aspx");
				// // i.putExtra("url",
				// "http://192.168.1.201/newcode/performancereport.aspx");
				// // i.putExtra("url",
				// SOAP_CONSTANTS.Report_Url+"performancereport.aspx");
				// i.putExtra("url",
				// SOAP_CONSTANTS.Report_Url+"EmployeeStats.aspx?Userid="+WW_StaticClass.InstructorID);
				// startActivity(i);
				// }
				else if (position == 7) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/OnedayProgramRoster.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/OnedayProgramRoster.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "OnedayProgramRoster.aspx?Userid="
							+ WW_StaticClass.InstructorID);

					startActivity(i);
				} else if (position == 8) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/MergedPayrollReport.aspx?Userid="+WW_StaticClass.InstructorID+"&Status="+WW_StaticClass.UserLevel);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/MergedPayrollReport.aspx?Userid="+WW_StaticClass.InstructorID+"&Status="+WW_StaticClass.UserLevel);
					// i.putExtra("url",
					// SOAP_CONSTANTS.Report_Url+"MergedPayrollReport.aspx?Userid="+WW_StaticClass.InstructorID+"&Status="+WW_StaticClass.UserLevel);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "ViewMyTimeCard.aspx?Userid="
							+ WW_StaticClass.InstructorID + "&Status="
							+ WW_StaticClass.UserLevel);

					startActivity(i);

				}
				// else if(position==10){
				// Intent i = new
				// Intent(getApplicationContext(),SubmitWorkReport.class);
				// i.putExtra("FROM", "");
				// startActivity(i);
				// }
				else if (position == 9) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "DivesandTurnsClinicsRoster.aspx");
					startActivity(i);
				} else if (position == 10) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "GuardPrepRoster.aspx");
					startActivity(i);
				} else if (position == 11) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "SwimmingMeritBadgesRoster.aspx");
					startActivity(i);
				} else if (position == 12) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "GirlsScoutProgramRoster.aspx");
					startActivity(i);
				} else if (position == 13) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url
							+ "SwimmingActivityBadgesRoster.aspx");
					startActivity(i);
				} else if (position == 14) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url", SOAP_CONSTANTS.Report_Url + "WaterPoloConditioningRoster.aspx");
					startActivity(i);
				} else if (position == 15) {
					if (Reports[position].equalsIgnoreCase("Instructor Report")) {
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						i.putExtra("url",
								"http://reports.waterworksswim.com/reports/aquatics/inst.php");
						startActivity(i);
					} else {
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						// i.putExtra("url",
						// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						// i.putExtra("url",
						// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						i.putExtra("url", SOAP_CONSTANTS.Report_Url
								+ "leveladvancereport.aspx?Userid="
								+ WW_StaticClass.InstructorID);
						startActivity(i);
					}

				} else if (position == 16) {
					if (Reports[position].equalsIgnoreCase("Report Pool Issue")) {
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						// i.putExtra("url",
						// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						// i.putExtra("url",
						// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						i.putExtra("url",
								"http://reports.waterworksswim.com/issue/pool.php");
						startActivity(i);
					} else {
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						// i.putExtra("url",
						// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						// i.putExtra("url",
						// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						i.putExtra("url", SOAP_CONSTANTS.Report_Url
								+ "ViewStopList.aspx?Userid="
								+ WW_StaticClass.InstructorID + "&Status="
								+ WW_StaticClass.UserLevel);
						startActivity(i);
					}
				} else if (position == 17) {
					if (Reports[position]
							.equalsIgnoreCase("Report Maintenance Issue")) {
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						// i.putExtra("url",
						// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						// i.putExtra("url",
						// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						i.putExtra("url",
								"http://reports.waterworksswim.com/issue/maint.php");
						startActivity(i);
					} else {
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						// i.putExtra("url",
						// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						// i.putExtra("url",
						// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						i.putExtra("url", SOAP_CONSTANTS.Report_Url
								+ "TodayFirstTimeStudentSchedule.aspx?Userid="
								+ WW_StaticClass.InstructorID);
						startActivity(i);
					}
				} else if (position == 18) {
					if (Reports[position].equalsIgnoreCase("Report IT Issue")) {
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						// i.putExtra("url",
						// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						// i.putExtra("url",
						// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						i.putExtra("url",
								"http://reports.waterworksswim.com/issue/it.php");
						startActivity(i);
					} else {
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						// i.putExtra("url",
						// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						// i.putExtra("url",
						// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						i.putExtra("url",
								"http://reports.waterworksswim.com/issue/pool.php");
						startActivity(i);
					}
				} else if (position == 19) {
					if (Reports[position]
							.equalsIgnoreCase("Report Customer Concern")) {
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						// i.putExtra("url",
						// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						// i.putExtra("url",
						// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						i.putExtra("url",
								"http://reports.waterworksswim.com/reports/aquatics/concern_cust.php");
						startActivity(i);
					} else {
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						// i.putExtra("url",
						// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						// i.putExtra("url",
						// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
						i.putExtra("url",
								"http://reports.waterworksswim.com/issue/maint.php");
						startActivity(i);
					}
				} else if (position == 20) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url",
							"http://reports.waterworksswim.com/issue/it.php");
					startActivity(i);
				} else if (position == 21) {
					Intent i = new Intent(getApplicationContext(),
							DetailReportActivity.class);
					i.putExtra("FROM", "");
					// i.putExtra("url",
					// "http://office.waterworksswimonline.com/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					// i.putExtra("url",
					// "http://192.168.1.201/newcode/SwimTeamRosterNew.aspx?Userid="+WW_StaticClass.InstructorID);
					i.putExtra("url",
							"http://reports.waterworksswim.com/reports/aquatics/concern_cust.php");
					startActivity(i);
				}

				else {
					Toast.makeText(getApplicationContext(),
							Reports[position].toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								t.interrupt();
								ReportActivity.this.finish();
							}
						})
				.setPositiveButton("Οk", new DialogInterface.OnClickListener() {


					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
		return builder1.create();
	}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (isInternetPresent) {
			switch (v.getId()) {
			case R.id.btn_back:
				t.interrupt();
				finish();
				break;
			case R.id.btn_app_logoff:
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						ReportActivity.this);
				// set title
				alertDialogBuilder.setTitle("WaterWorks");
				alertDialogBuilder.setIcon(getResources().getDrawable(
						R.drawable.ic_launcher));

				// set dialog message
				alertDialogBuilder
						.setMessage("Are you sure you want to logout ?")
						.setCancelable(false)
						.setPositiveButton("Logout",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										WW_StaticClass.InstructorID = "";
										t.interrupt();
										finish();
										stopService(new Intent(
												ReportActivity.this,
												DeckNotificationService.class));
										Intent loginIntent = new Intent(
												ReportActivity.this,
												LoginActivity.class);
										loginIntent
												.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										loginIntent
												.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

										startActivity(loginIntent);
										android.os.Process
												.killProcess(android.os.Process
														.myPid());
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				break;
			default:
				break;
			}
		} else {
			onDetectNetworkState().show();
		}
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
		isInternetPresent = Utility.isNetworkConnected(getApplicationContext());
	}

	public class ReportsAdapter extends BaseAdapter {
		Context context;
		String Reports[];

		public ReportsAdapter(Context context, String[] reports) {
			super();
			this.context = context;
			Reports = reports;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return Reports.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return Reports[position];
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View grid;
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null) {
				grid = new View(context);
				grid = inflater.inflate(R.layout.reports_item, null);
				TextView tv_item = (TextView) grid
						.findViewById(R.id.tv_reports_item);
				tv_item.setText(Reports[position]);
			} else {
				grid = (View) convertView;
			}
			return grid;
		}

	}
}
