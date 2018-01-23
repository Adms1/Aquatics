package water.works.waterworks;

import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class MessageCenterFragment extends ListFragment implements OnRefreshListener2<ListView> {
    View rootView;
    boolean server_response = false;
    boolean inboxdata = false, sentdata = false, trashdata = false, dowithmessage = false;
    ListView listview;
    public PullToRefreshListView lv_messages;
    int pgnum = 1;
    String TAG = "MessageCenterFragment";
    FrameLayout fl_loading;
    //	int Folder_id;
    public static ArrayList<String> MsgID = new ArrayList<String>();
    ArrayList<String> Read = new ArrayList<String>();
    ArrayList<String> Priority = new ArrayList<String>();
    ArrayList<String> Username = new ArrayList<String>();
    ArrayList<String> MsgSubject = new ArrayList<String>();
    ArrayList<String> MsgTime = new ArrayList<String>();
    ArrayList<String> OtherUser = new ArrayList<String>();
    int old_pos = 0;
    //	TitanicTextViewtv ;
//    Titanic titanic;

    Shimmer shimmer;
    ShimmerTextView tv;
    Boolean isInternetPresent = false;

    public MessageCenterFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        Initializ();
        Read.clear();
        Priority.clear();
        Username.clear();
        MsgSubject.clear();
        MsgTime.clear();
        OtherUser.clear();
        setListAdapter(null);
        WW_StaticClass.duration1 = 500;
        WW_StaticClass.duration2 = 3000;
        Log.e(TAG, WW_StaticClass.lable);
        Log.e(TAG, "Here = " + WW_StaticClass.Folder_id);
        shimmer = new Shimmer();
        isInternetPresent = Utility
                .isNetworkConnected(getActivity());
        if (isInternetPresent) {
            if (WW_StaticClass.lable.equalsIgnoreCase("Lable")) {
                WW_StaticClass.Folder_id = -1;
                new DefualtInboxMessages().execute();
            } else if (WW_StaticClass.lable.equalsIgnoreCase("Inbox")) {
                WW_StaticClass.Folder_id = -1;
                new DefualtInboxMessages().execute();
            } else if (WW_StaticClass.lable.equalsIgnoreCase("Sent Item")) {
                new DefualtSentMessages().execute();
            } else if (WW_StaticClass.lable.equalsIgnoreCase("Trash")) {
                new DefualtTrashMessage().execute();
            } else {
                new DefualtInboxMessages().execute();
//			fl_loading = (FrameLayout)rootView.findViewById(R.id.fragment_message_progress);
//			fl_loading.setVisibility(View.GONE);
            }
        } else {
            onDetectNetworkState().show();
        }
        lv_messages.setOnRefreshListener(this);

        return rootView;
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        getActivity().finish();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    private void Initializ() {
        // TODO Auto-generated method stub
        lv_messages = (PullToRefreshListView) rootView.findViewById(R.id.message_list);
        listview = lv_messages.getRefreshableView();
        lv_messages.setMode(Mode.PULL_FROM_START);
		 tv = (ShimmerTextView) rootView.findViewById(R.id.my_text_view);
        tv.setTypeface(Typefaces.get(getActivity(), "Satisfy-Regular.ttf"));
    }


    ////////////////////////////// Defualt Message Inbox //////////////////////////////
    private class DefualtInboxMessages extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            fl_loading = (FrameLayout) rootView.findViewById(R.id.fragment_message_progress);
//            titanic.start(tv);
            shimmer.start(tv);
            fl_loading.setVisibility(View.VISIBLE);
            fl_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            Log.e(TAG, "" + WW_StaticClass.Folder_id);
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_Mail_GetCommonInboxData);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("CurrFolderID", WW_StaticClass.Folder_id);
            request.addProperty("PgNum1", "" + pgnum);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Mail_GetCommonInboxData,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i(TAG, "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                Log.i(TAG, "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    inboxdata = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    JSONArray jArray = jo.getJSONArray("InboxList");
                    Log.i(TAG, "jArray : " + jArray.toString());
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        MsgID.add(jsonObject.getString("MsgID"));
                        Read.add(jsonObject.getString("Read"));
                        Priority.add(jsonObject.getString("Priority"));
                        Username.add(jsonObject.getString("Username"));
                        MsgSubject.add(jsonObject.getString("MsgSubject"));
                        MsgTime.add(jsonObject.getString("MsgTime"));
                    }
                } else {
                    inboxdata = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                server_response = true;
            } catch (Exception e) {
                server_response = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            lv_messages.onRefreshComplete();
            fl_loading.setVisibility(View.GONE);
            if (server_response) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(
                        getActivity(), "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
                server_response = false;
            } else {
                if (!inboxdata) {
                    if (!MsgID.isEmpty()) {

                    } else {
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(
                                getActivity(), "WaterWorks",
                                "No mail found.", "OK");

                    }
                } else {
                    inboxdata = false;
                    if (MsgID.size() > 1) {
                        old_pos = lv_messages.getRefreshableView()
                                .getFirstVisiblePosition() + 1;
                    } else {
                        old_pos = lv_messages.getRefreshableView()
                                .getFirstVisiblePosition() + 1;
                    }
                    listview.setAdapter(adapter);
                    listview.post(new Runnable() {


                        public void run() {
                            // TODO Auto-generated method stub
                            lv_messages.getRefreshableView()
                                    .setSelection(old_pos);
                        }
                    });

                    lv_messages.onRefreshComplete();
                }
            }
        }

    }

    private BaseAdapter adapter = new BaseAdapter() {

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }


        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }


        public int getCount() {
            // TODO Auto-generated method stub
            return MsgID.size();
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            try {
                if (convertView == null) {
                    holder = new ViewHolder();


                    convertView = LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.messages_row, null);
                    holder.chb_msg_select = (CheckBox) convertView.findViewById(R.id.message_row_checkbox);
                    holder.tv_priority = (TextView) convertView.findViewById(R.id.message_row_priority);
                    holder.tv_from = (TextView) convertView.findViewById(R.id.message_row_from);
                    holder.tv_subject = (TextView) convertView.findViewById(R.id.message_row_subject);
                    holder.tv_time = (TextView) convertView.findViewById(R.id.message_row_time_received);
                    holder.viewDivider = (View) convertView.findViewById(R.id.background_divider);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (WW_StaticClass.lable.equalsIgnoreCase("Inbox") || WW_StaticClass.lable.equalsIgnoreCase("lable")) {
                    if (Read.get(position).equalsIgnoreCase("0")) {
                        convertView.setBackgroundColor(Color.parseColor("#D2CCCC"));
                        holder.tv_priority.setTypeface(holder.tv_priority.getTypeface(), Typeface.BOLD);
                        holder.tv_from.setTypeface(holder.tv_from.getTypeface(), Typeface.BOLD);
                        holder.tv_subject.setTypeface(holder.tv_subject.getTypeface(), Typeface.BOLD);
//					holder.tv_time.setTypeface(holder.tv_time.getTypeface(), Typeface.BOLD);
                        holder.viewDivider.setBackgroundColor(Color.rgb(210, 204, 204));
                    } else {
                        convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        holder.viewDivider.setBackgroundColor(Color.rgb(255, 255, 255));
                    }

                    String priority = Priority.get(position);
                    if (priority.equalsIgnoreCase("0")) {
                        holder.tv_priority.setText("");
                    } else if (priority.equalsIgnoreCase("1")) {
                        holder.tv_priority.setText("*");
                        holder.tv_priority.setTextColor(Color.RED);
                    } else if (priority.equalsIgnoreCase("2")) {
                        holder.tv_priority.setText("!!");
                        holder.tv_priority.setTextColor(Color.RED);
                    }

                    holder.tv_subject.setText(MsgSubject.get(position));
                    holder.tv_from.setText(Username.get(position));
                    holder.tv_time.setText(MsgTime.get(position));

                    convertView.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (!holder.chb_msg_select.isChecked()) {
                                Log.e(TAG, "Position of msg = " + position);
                                WW_StaticClass.mailid = MsgID.get(position);
                                WW_StaticClass.foldernumber = "-1";
                                if (Read.get(position).equalsIgnoreCase("0")) {
                                    new DoTaskWithMessage().execute();
                                }
                                Intent detailmailIntent = new Intent(getActivity().getApplicationContext(), DetailMailActivity.class);
                                detailmailIntent.putExtra("Lable", "Inobx");
                                startActivity(detailmailIntent);
                                getActivity().finish();
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                    });
                } else if (WW_StaticClass.lable.equalsIgnoreCase("Sent Item")) {
                    holder.chb_msg_select.setVisibility(View.GONE);
                    holder.tv_priority.setVisibility(View.GONE);
                    holder.tv_subject.setText(MsgSubject.get(position));
                    holder.tv_from.setText(Username.get(position));
                    holder.tv_time.setText(MsgTime.get(position));
                    holder.viewDivider.setBackgroundColor(Color.rgb(255, 255, 255));

                    convertView.setOnClickListener(new OnClickListener() {


                        public void onClick(View v) {
                            // TODO Auto-generated method stub
//							Log.e(TAG, "Position of msg = "+position);
//							WW_StaticClass.mailid = MsgID.get(position);
                            Log.e(TAG, "Position of msg = " + position);
                            WW_StaticClass.Msg_id_pos = position;
                            WW_StaticClass.mailid = MsgID.get(position);
                            WW_StaticClass.foldernumber = "-4";
                            Intent detailmailIntent = new Intent(getActivity(), DetailMailActivity.class);
                            detailmailIntent.putExtra("Lable", "Sent Item");
                            startActivity(detailmailIntent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        }
                    });
                } else if (WW_StaticClass.lable.equalsIgnoreCase("Trash")) {
                    holder.chb_msg_select.setVisibility(View.GONE);
                    holder.tv_priority.setVisibility(View.GONE);
                    holder.tv_subject.setText(MsgSubject.get(position));
                    holder.tv_from.setText(Username.get(position));
                    holder.tv_time.setText(MsgTime.get(position));
                    holder.viewDivider.setBackgroundColor(Color.rgb(255, 255, 255));

                    convertView.setOnClickListener(new OnClickListener() {


                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Log.e(TAG, "Position of msg = " + position);
                            WW_StaticClass.mailid = MsgID.get(position);
                            WW_StaticClass.foldernumber = "-3";
                            Intent detailmailIntent = new Intent(getActivity(), DetailMailActivity.class);
                            detailmailIntent.putExtra("Lable", "Trash");
                            startActivity(detailmailIntent);
                            getActivity().finish();
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        }
                    });
                } else {
                    if (Read.get(position).equalsIgnoreCase("0")) {
                        convertView.setBackgroundColor(Color.parseColor("#D2CCCC"));
                        holder.tv_priority.setTypeface(holder.tv_priority.getTypeface(), Typeface.BOLD);
                        holder.tv_from.setTypeface(holder.tv_from.getTypeface(), Typeface.BOLD);
                        holder.tv_subject.setTypeface(holder.tv_subject.getTypeface(), Typeface.BOLD);
                        holder.viewDivider.setBackgroundColor(Color.rgb(210, 204, 204));
                    } else {
                        convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        holder.viewDivider.setBackgroundColor(Color.rgb(255, 255, 255));
                    }

                    String priority = Priority.get(position);
                    if (priority.equalsIgnoreCase("0")) {
                        holder.tv_priority.setText("");
                    } else if (priority.equalsIgnoreCase("1")) {
                        holder.tv_priority.setText("*");
                        holder.tv_priority.setTextColor(Color.RED);
                    } else if (priority.equalsIgnoreCase("2")) {
                        holder.tv_priority.setText("!!");
                        holder.tv_priority.setTextColor(Color.RED);
                    }

                    holder.tv_subject.setText(MsgSubject.get(position));
                    holder.tv_from.setText(Username.get(position));
                    holder.tv_time.setText(MsgTime.get(position));

                    convertView.setOnClickListener(new OnClickListener() {


                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (!holder.chb_msg_select.isChecked()) {
                                Log.e(TAG, "Position of msg = " + position);
                                WW_StaticClass.mailid = MsgID.get(position);
                                WW_StaticClass.foldernumber = "-1";
                                if (Read.get(position).equalsIgnoreCase("0")) {
                                    new DoTaskWithMessage().execute();
                                }
                                Intent detailmailIntent = new Intent(getActivity().getApplicationContext(), DetailMailActivity.class);
                                detailmailIntent.putExtra("Lable", WW_StaticClass.lable);
                                startActivity(detailmailIntent);
                                getActivity().finish();
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                    });
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

    };

    public class ViewHolder {
        CheckBox chb_msg_select;
        TextView tv_priority, tv_from, tv_subject, tv_time;
        View viewDivider;

    }


    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        // TODO Auto-generated method stub
        Log.e(TAG, "Down");
        if (WW_StaticClass.lable.equalsIgnoreCase("Inbox") || WW_StaticClass.lable.equalsIgnoreCase("Lable")) {
            if (pgnum != 1) {
                pgnum--;
                WW_StaticClass.Folder_id = -1;
                new DefualtInboxMessages().execute();
            } else {
                pgnum = 1;
                MsgID.clear();
                MsgSubject.clear();
                MsgTime.clear();
                Username.clear();
                Priority.clear();
                Read.clear();
                OtherUser.clear();
                WW_StaticClass.Folder_id = -1;
                new DefualtInboxMessages().execute();
            }
        } else if (WW_StaticClass.lable.equalsIgnoreCase("Sent Item")) {
            if (pgnum != 1) {
                pgnum--;
                new DefualtSentMessages().execute();
            } else {
                pgnum = 1;
                MsgID.clear();
                Read.clear();
                MsgSubject.clear();
                OtherUser.clear();
                MsgTime.clear();
                Username.clear();
                Priority.clear();

                new DefualtSentMessages().execute();
            }
        } else if (WW_StaticClass.lable.equalsIgnoreCase("Trash")) {
            if (pgnum != 1) {
                pgnum--;
                new DefualtTrashMessage().execute();
            } else {
                pgnum = 1;
                MsgID.clear();
                Read.clear();
                MsgSubject.clear();
                OtherUser.clear();
                MsgTime.clear();
                Username.clear();
                Priority.clear();

                new DefualtTrashMessage().execute();
            }
        } else {
            if (pgnum != 1) {
                pgnum--;
                new DefualtInboxMessages().execute();
            } else {
                pgnum = 1;
                MsgID.clear();
                MsgSubject.clear();
                MsgTime.clear();
                Username.clear();
                Priority.clear();
                Read.clear();
                OtherUser.clear();

                new DefualtInboxMessages().execute();
            }
        }

    }

    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        // TODO Auto-generated method stub
        Log.e(TAG, "Up");
        pgnum++;
        if (WW_StaticClass.lable.equalsIgnoreCase("Inbox") || WW_StaticClass.lable.equalsIgnoreCase("lable")) {
            WW_StaticClass.Folder_id = -1;
            new DefualtInboxMessages().execute();
        } else if (WW_StaticClass.lable.equalsIgnoreCase("Sent Item")) {
            new DefualtSentMessages().execute();
        } else if (WW_StaticClass.lable.equalsIgnoreCase("Trash")) {
            new DefualtTrashMessage().execute();
        } else {
            new DefualtInboxMessages().execute();
        }
    }

    private class DefualtSentMessages extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            fl_loading = (FrameLayout) rootView.findViewById(R.id.fragment_message_progress);
//            titanic.start(tv);
            fl_loading.setVisibility(View.VISIBLE);
            fl_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_Mail_GetCommonSentData);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("PgNum1", "" + pgnum);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Mail_GetCommonSentData,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i(TAG, "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                Log.i(TAG, "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    sentdata = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    JSONArray jArray = jo.getJSONArray("SentItemlist");
                    Log.i(TAG, "jArray : " + jArray.toString());
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        MsgID.add(jsonObject.getString("MsgID"));
                        Username.add(jsonObject.getString("MsgTo"));
                        OtherUser.add(jsonObject.getString("MsgCCTo"));
                        MsgSubject.add(jsonObject.getString("MsgSubject"));
                        MsgTime.add(jsonObject.getString("MsgTime"));
                    }
                } else {
                    sentdata = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                server_response = true;
            } catch (Exception e) {
                server_response = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            lv_messages.onRefreshComplete();
            fl_loading.setVisibility(View.GONE);
            if (server_response) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(
                        getActivity(), "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
                server_response = false;
            } else {
                if (!sentdata) {
                    if (!MsgID.isEmpty()) {

                    } else {
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(
                                getActivity(), "WaterWorks",
                                "No mail found.", "OK");
                    }
                } else {
                    sentdata = false;
                    if (MsgID.size() > 1) {
                        old_pos = lv_messages.getRefreshableView()
                                .getFirstVisiblePosition() + 1;
                    } else {
                        old_pos = lv_messages.getRefreshableView()
                                .getFirstVisiblePosition() + 1;
                    }
                    listview.setAdapter(adapter);
                    listview.post(new Runnable() {


                        public void run() {
                            // TODO Auto-generated method stub
                            lv_messages.getRefreshableView()
                                    .setSelection(old_pos);
                        }
                    });

                    lv_messages.onRefreshComplete();
                }
            }
        }
    }

    private class DefualtTrashMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            fl_loading = (FrameLayout) rootView.findViewById(R.id.fragment_message_progress);
//            titanic.start(tv);
            fl_loading.setVisibility(View.VISIBLE);
            fl_loading.bringToFront();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_Mail_GetCommonDeletedData);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("PgNum1", "" + pgnum);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Mail_GetCommonDeletedData,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i(TAG, "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                Log.i(TAG, "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    trashdata = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    JSONArray jArray = jo.getJSONArray("TrashList");
                    Log.i(TAG, "jArray : " + jArray.toString());
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        MsgID.add(jsonObject.getString("MsgID"));
                        Username.add(jsonObject.getString("Username"));
                        MsgSubject.add(jsonObject.getString("MsgSubject"));
                        MsgTime.add(jsonObject.getString("MsgTime"));
                    }
                } else {
                    trashdata = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                server_response = true;
            } catch (Exception e) {
                e.printStackTrace();
                server_response = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            lv_messages.onRefreshComplete();
            fl_loading.setVisibility(View.GONE);
            if (server_response) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(
                        getActivity(), "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
                server_response = false;
            } else {
                if (!trashdata) {
                    if (!MsgID.isEmpty()) {

                    } else {
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(
                                getActivity(), "WaterWorks",
                                "No mail found.", "OK");
                    }
                } else {
                    trashdata = false;
                    if (MsgID.size() > 1) {
                        old_pos = lv_messages.getRefreshableView()
                                .getFirstVisiblePosition() + 1;
                    } else {
                        old_pos = lv_messages.getRefreshableView()
                                .getFirstVisiblePosition() + 1;
                    }
                    listview.setAdapter(adapter);
                    listview.post(new Runnable() {


                        public void run() {
                            // TODO Auto-generated method stub
                            lv_messages.getRefreshableView()
                                    .setSelection(old_pos);
                        }
                    });

                    lv_messages.onRefreshComplete();
                }
            }
        }
    }

    public void onResume() {
        super.onResume();
        MsgID.clear();
        Read.clear();
        MsgSubject.clear();
        OtherUser.clear();
        MsgTime.clear();
        Username.clear();
        Priority.clear();
    }

    ;


    ///////////Unread to read message////////
    private class DoTaskWithMessage extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//			fl_detail_mail_loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_Mail_MarkAsReadUnread);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("MoveMsgId", WW_StaticClass.mailid);
            request.addProperty("ReadUnread", 1);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Mail_MarkAsReadUnread,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i(TAG, "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                Log.i(TAG, "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    dowithmessage = true;
                } else {
                    dowithmessage = false;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                server_response = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
//			fl_detail_mail_loading.setVisibility(View.GONE);
            if (server_response) {

//				SingleOptionAlertWithoutTitle.ShowAlertDialog(
//						getActivity(),"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
                server_response = false;
            } else {
                if (!dowithmessage) {
                    Log.e(TAG, "Message Unread Unsuccessful....");
                } else {
                    dowithmessage = false;
                    Log.e(TAG, "Message ReadUnread Successfully....");
//					finish();
//					overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        }
    }
}
