package water.works.waterworks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import net.frederico.showtipsview.ShowTipsBuilder;
import net.frederico.showtipsview.ShowTipsView;
import net.frederico.showtipsview.ShowTipsViewInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import water.works.waterworks.adapter.MessageItemListAdapter;
import water.works.waterworks.customlibrary.Titanic;
import water.works.waterworks.customlibrary.TitanicTextView;
import water.works.waterworks.model.MessageItems;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.SingleOptionAlertWithoutTitle;
import water.works.waterworks.utils.Utility;
import water.works.waterworks.utils.WW_StaticClass;


public class MessageCenterActivity extends Activity {
    int pgnum = 1;
    boolean server_response = false, connectionout = false;
    boolean data_response = false;
    boolean folder_created_edited = false;
    public static String TAG = "MessageCenterActivity";
    Boolean isInternetPresent = false;
    boolean editclick = false;
    ListPopupWindow listpw_folders, list_edit_parent, list_edit_select;
    Fragment fragment;
    String new_folder_name;
    String SelectedFolder_id = "";
    String Folder_parent_id;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList_main;
    private ListView mDrawerList_folder;

    LinearLayout lldrawercontent;
    TextView folders;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;
    private String selected_folder_name_for_edit;
    private ArrayList<MessageItems> navDrawerItems_main;
    private MessageItemListAdapter adapter_main;
    private ArrayList<MessageItems> navDrawerItems_folder;
    private MessageItemListAdapter adapter_folder;
    private ArrayList<String> titles;

    ArrayList<String> ParentID, Temp_FolderID, Temp_FolderName;
    boolean folderget = false;
    boolean dialog_folder = false;
    boolean delete_folder = false;
    ArrayList<String> FolderID, FolderName, Final_FolderName;

    private PopupWindow mpopup;

    private View decorView;
    private int uiOptions;
    //    Titanic titanic;
    //	TitanicTextView tv ;
    Shimmer shimmer;
    ShimmerTextView tv;

    FrameLayout fl_msgcntr_loading;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().show();
        decorView = getWindow().getDecorView();
        uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        createUiChangeListener();
        setContentView(R.layout.activity_message_center);
        isInternetPresent = Utility
                .isNetworkConnected(MessageCenterActivity.this);
        tv = (ShimmerTextView) findViewById(R.id.my_text_view);
        tv.setTypeface(Typefaces.get(MessageCenterActivity.this, "Satisfy-Regular.ttf"));
        fl_msgcntr_loading = (FrameLayout) findViewById(R.id.msg_cntr_loading);
        fl_msgcntr_loading.setVisibility(View.GONE);
        WW_StaticClass.duration1 = 300;
        WW_StaticClass.duration2 = 1000;
//        titanic = new Titanic();
//        titanic.start(tv);

        shimmer = new Shimmer();
        shimmer.start(tv);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lldrawercontent = (LinearLayout) findViewById(R.id.lldrawercontent);
        listpw_folders = new ListPopupWindow(getApplicationContext());
        list_edit_parent = new ListPopupWindow(getApplicationContext());
        list_edit_select = new ListPopupWindow(getApplicationContext());
        folders = (TextView) findViewById(R.id.folders);
        folders.setVisibility(View.GONE);
        titles = new ArrayList<String>();
        navDrawerItems_main = new ArrayList<MessageItems>();
        navDrawerItems_folder = new ArrayList<MessageItems>();
        Final_FolderName = new ArrayList<String>();
        FolderName = new ArrayList<String>();
        FolderID = new ArrayList<String>();
        ParentID = new ArrayList<String>();
        Temp_FolderID = new ArrayList<String>();
        Temp_FolderName = new ArrayList<String>();

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        // getActionBar().show();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        View v = findViewById(android.R.id.home);
        ShowTipsView showtips = new ShowTipsBuilder(MessageCenterActivity.this)
                .setTarget(v).setTitle("Folder List")
                .setTitleColor(Color.YELLOW)
                .setDescription("This will show folder list.")
                .setDescriptionColor(getResources().getColor(R.color.bigs))
                .setCircleColor(Color.RED).displayOneTime(96).setDelay(1000)
                .setCallback(new ShowTipsViewInterface() {

                    @Override
                    public void gotItClicked() {
                        // TODO Auto-generated method stub
                        View v = findViewById(R.id.action_new_mail);
                        ShowTipsView showtips1 = new ShowTipsBuilder(
                                MessageCenterActivity.this)
                                .setTarget(v)
                                .setTitle("Compose Mail")
                                .setTitleColor(Color.YELLOW)
                                .setDescription("For creating new mail.")
                                .setDescriptionColor(
                                        getResources().getColor(R.color.bigs))
                                .setCircleColor(Color.RED).displayOneTime(97)
                                .setDelay(0)
                                .setCallback(new ShowTipsViewInterface() {

                                    @Override
                                    public void gotItClicked() {
                                        // TODO Auto-generated method stub
                                        View v = findViewById(R.id.action_new_folder);
                                        ShowTipsView showtips2 = new ShowTipsBuilder(
                                                MessageCenterActivity.this)
                                                .setTarget(v)
                                                .setTitle("Folder Creation")
                                                .setTitleColor(Color.YELLOW)
                                                .setDescription(
                                                        "For creating new folder.")
                                                .setDescriptionColor(
                                                        getResources()
                                                                .getColor(
                                                                        R.color.bigs))
                                                .setCircleColor(Color.RED)
                                                .displayOneTime(98)
                                                .setDelay(0)
                                                .setCallback(
                                                        new ShowTipsViewInterface() {

                                                            @Override
                                                            public void gotItClicked() {
                                                                // TODO
                                                                // Auto-generated
                                                                // method stub
                                                                View v = findViewById(R.id.action_log_off);
                                                                ShowTipsView showtips3 = new ShowTipsBuilder(
                                                                        MessageCenterActivity.this)
                                                                        .setTarget(
                                                                                v)
                                                                        .setTitle(
                                                                                "Logoff")
                                                                        .setTitleColor(
                                                                                Color.YELLOW)
                                                                        .setDescription(
                                                                                "It will log you out from application.")
                                                                        .setDescriptionColor(
                                                                                getResources()
                                                                                        .getColor(
                                                                                                R.color.bigs))
                                                                        .setCircleColor(
                                                                                Color.RED)
                                                                        .displayOneTime(
                                                                                99)
                                                                        .setDelay(
                                                                                0)
                                                                        .build();
                                                                showtips3
                                                                        .show(MessageCenterActivity.this);
                                                            }
                                                        }).build();
                                        showtips2
                                                .show(MessageCenterActivity.this);
                                    }
                                }).build();
                        showtips1.show(MessageCenterActivity.this);
                    }
                }).build();
        showtips.show(MessageCenterActivity.this);

    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setIcon(getResources().getDrawable(R.mipmap.logo));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                MessageCenterActivity.this.finish();
                            }
                        })
                .setPositiveButton("Οk", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    /**
     * Slide menu main item click listener
     */
    private class SlideMenuMainClickListener implements
            OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView_Main(position);
            WW_StaticClass.lable = titles.get(position).toString();

        }
    }

    /**
     * Slide menu folder item click listener
     */
    private class SlideMenuFolderClickListener implements
            OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView_Folder(position);
            WW_StaticClass.lable = titles.get(4 + position).toString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_center, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (isInternetPresent) {
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
            // Handle action bar actions click
            switch (item.getItemId()) {
                case R.id.action_new_mail:
                /* Toast.makeText(getApplicationContext(), "Work in..",
                 1).show();*/
                    Intent itnewmailIntent = new Intent(MessageCenterActivity.this,
                            CreateNewMessageActivity.class);
                    itnewmailIntent.putExtra("IAMFOR", "CREATE");
                    itnewmailIntent.putExtra("From", "");
                    itnewmailIntent.putExtra("To", "");
                    itnewmailIntent.putExtra("Cc", "");
                    itnewmailIntent.putExtra("Subject", "");
                    itnewmailIntent.putExtra("Priority", "");
                    itnewmailIntent.putExtra("Sitename", "");
                    itnewmailIntent.putExtra("FromID", "");
                    itnewmailIntent.putExtra("ToID", "");
                    itnewmailIntent.putExtra("CcID", "");
                    itnewmailIntent.putExtra("strFrmmsg", "");
                    startActivity(itnewmailIntent);
                    finish();
                    break;
                case R.id.action_new_folder:
                    final Dialog dialog = new Dialog(MessageCenterActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.folder_create_new);
                    dialog.setCancelable(false);

                    final EditText et_new_folder = (EditText) dialog
                            .findViewById(R.id.et_dialog_folder_name);
                    Button buttonCancel = (Button) dialog
                            .findViewById(R.id.btn_dialog_folder_cancel);
                    Button buttonSet = (Button) dialog
                            .findViewById(R.id.btn_dialog_folder_ok);
                    buttonCancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    buttonSet.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (et_new_folder.getText().toString().trim().length() == 0) {
                                Toast.makeText(getApplicationContext(),
                                        "Please enter folder name", Toast.LENGTH_LONG).show();
                            } else {
                                new_folder_name = et_new_folder.getText()
                                        .toString();
                                Folder_parent_id = "Select";
                                SelectedFolder_id = "";
                                Log.e(TAG, "Folder parent id = " + Folder_parent_id);
                                Log.e(TAG, "Selected Folder id = "
                                        + SelectedFolder_id);
                                new CreateFolder().execute();
                                dialog.dismiss();
                            }

                        }
                    });
                    dialog.show();
                    break;
                case R.id.action_log_off:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            MessageCenterActivity.this);

                    // set title
                    alertDialogBuilder.setTitle("LAFitnessApp");
                    alertDialogBuilder.setIcon(getResources().getDrawable(
                            R.mipmap.logo));

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Are you sure you want to logout ?")
                            .setCancelable(false)
                            .setPositiveButton("Logout",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            WW_StaticClass.InstructorID = "";
                                            WW_StaticClass.lable = "lable";
                                            WW_StaticClass.foldernumber = "";
                                            finish();

                                            Intent loginIntent = new Intent(
                                                    MessageCenterActivity.this,
                                                    LoginActivity.class);
                                            loginIntent
                                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            loginIntent
                                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(loginIntent);
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
        return super.onOptionsItemSelected(item);
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        // boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.action_new_mail).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView_Main(int position) {
        // update the main content by replacing fragments
        for (int i = 0; i < navDrawerItems_main.size(); i++) {
            fragment = new MessageCenterFragment();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList_main.setItemChecked(position, true);
            mDrawerList_main.setSelection(position);
            setTitle(titles.get(position).toString());
            mDrawerLayout.closeDrawers();
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }

    // /Folders

    private void displayView_Folder(int position) {
        // update the main content by replacing fragments
        for (int i = 0; i < navDrawerItems_folder.size(); i++) {
            fragment = new MessageCenterFragment();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList_folder.setItemChecked(position, true);
            mDrawerList_folder.setSelection(position);
            setTitle(titles.get(4 + position).toString());
            String f_name = titles.get(4 + position).toString();
            Log.e(TAG, f_name);
            String f_id = FolderID.get(position + 1);
            Log.e(TAG, f_id);
            WW_StaticClass.Folder_id = Integer.parseInt(f_id);
            mDrawerLayout.closeDrawers();
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    ProgressDialog pDialog;

    private class GetParentID extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            ParentID.clear();
            Temp_FolderID.clear();
            Temp_FolderName.clear();
            pDialog = new ProgressDialog(MessageCenterActivity.this);
            pDialog.setTitle("LAFitnessApp");
            pDialog.setMessage(Html.fromHtml("Loading wait..."));
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_Mail_Get_FolderList);
            request.addProperty("token", WW_StaticClass.UserToken);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_Mail_Get_FolderList,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i(TAG, "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                Log.i(TAG, "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    folderget = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    JSONArray jArray = jo.getJSONArray("FolderList");
                    Log.i(TAG, "jArray : " + jArray.toString());
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        Temp_FolderID.add(jsonObject.getString("FolderId"));
                        Temp_FolderName.add(jsonObject.getString("FolderName")
                                .trim());
                        ParentID.add(jsonObject.getString("ParentId"));
                    }
                } else if (code.equalsIgnoreCase("111")) {
                    folderget = false;
                    // Object mSoapObject3 = mSoapObject1.getProperty(1);
                    // Log.i(TAG, "mSoapObject3="+mSoapObject3);
                } else {
                    folderget = false;
                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                connectionout = true;
            } catch (SocketException e) {
                e.printStackTrace();
                connectionout = true;
            } catch (JSONException e) {
                server_response = true;
                e.printStackTrace();
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
            pDialog.dismiss();
            Log.e(TAG, "Temp Folder id = " + Temp_FolderID);
            Log.e(TAG, "Temp Folder name = " + Temp_FolderName);
            Log.e(TAG, "Parent id = " + ParentID);
            if (server_response) {
                server_response = false;
                Log.e(TAG, "Folder not found Exception");
            } else if (connectionout) {
                connectionout = false;
                new GetParentID().execute();
            } else {
                if (folderget) {
                    if (Temp_FolderID.size() > 0) {
                        Log.e(TAG, "in if");
                        Log.e(TAG, "Folder found");
                    } else {
                        Log.e(TAG, "in else");
                        Log.e(TAG, "No folder found");
                    }
                } else {
                    Log.e(TAG, "in else");
                    Log.e(TAG, "No folder found");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        //		Intent it = new Intent(MessageCenterActivity.this,
        //				DashBoardActivity.class);
        //		startActivity(it);
        finish();
        WW_StaticClass.lable = "lable";
        WW_StaticClass.foldernumber = "";

    }

    private class CreateFolder extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_Mail_CreateMessageFolder);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("FolderName", new_folder_name); // folder name
            // new or
            // existing
            request.addProperty("ParentID", Folder_parent_id); // if new than
            // Select , if
            // edit than
            // that folder's
            // parent id
            request.addProperty("tempSelect", 0); // from main activity i.e
            // inbox
            request.addProperty("MoveMsgId", ""); // if message open than its id
            request.addProperty("SelectedFldr", SelectedFolder_id); // if edit
            // than
            // folder's
            // id

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(
                        SOAP_CONSTANTS.SOAP_Action_Mail_CreateMessageFolder,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i(TAG, "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                Log.i(TAG, "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    folder_created_edited = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    Log.e(TAG, jo.getString("CreateFolder"));
                    folder_created_or_not = jo.getString("CreateFolder");
                } else if (code.equalsIgnoreCase("111")) {
                    folder_created_edited = false;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    Log.e(TAG, jo.getString("CreateFolder"));
                    folder_created_or_not = jo.getString("CreateFolder");

                }
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                connectionout = true;
            } catch (SocketException e) {
                e.printStackTrace();
                connectionout = true;
            } catch (JSONException e) {
                server_response = true;
                e.printStackTrace();
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
            if (server_response) {
                SingleOptionAlertWithoutTitle
                        .ShowAlertDialog(
                                MessageCenterActivity.this,
                                "LAFitnessApp",
                                "Server not responding.\nPlease check internet connection or try after sometime.",
                                "OK");
                server_response = false;
            } else if (connectionout) {
                connectionout = false;
                new CreateFolder().execute();
            } else {
                if (folder_created_edited) {
                    if (editclick) {
                        Toast.makeText(MessageCenterActivity.this,
                                folder_created_or_not, Toast.LENGTH_LONG).show();
                        new DialogData().execute();
                        new GetParentID().execute();
                        editclick = false;
                    } else {
                        Toast.makeText(MessageCenterActivity.this,
                                folder_created_or_not, Toast.LENGTH_LONG).show();
                        new DialogData().execute();
                        new GetParentID().execute();
                    }
                    folder_created_edited = false;
                } else {
                    Toast.makeText(MessageCenterActivity.this,
                            folder_created_or_not, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    String folder_created_or_not = "";

    private class DialogData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog_folder = false;
            FolderID.clear();
            FolderName.clear();
            mDrawerList_main = (ListView) findViewById(R.id.list_slidermenu_main);
            mDrawerList_folder = (ListView) findViewById(R.id.list_slidermenu_folder);
            navDrawerItems_main.clear();
            navDrawerItems_folder.clear();
            Final_FolderName.clear();
            titles.clear();
            WW_StaticClass.lable = "lable";
            WW_StaticClass.Folder_id = 0;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_Mail_BindFolderTree);
            request.addProperty("token", WW_StaticClass.UserToken);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(
                        SOAP_CONSTANTS.SOAP_Action_Mail_BindFolderTree, envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i(TAG, "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                Log.i(TAG, "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    dialog_folder = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    JSONArray jArray = jo.getJSONArray("FolderList");
                    Log.i(TAG, "jArray : " + jArray.toString());
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        FolderID.add(jsonObject.getString("FolderID"));
                        FolderName.add(jsonObject.getString("FolderName")
                                .trim());
                    }
                } else if (code.equalsIgnoreCase("111")) {
                    dialog_folder = false;
                    // Object mSoapObject3 = mSoapObject1.getProperty(1);
                    // Log.i(TAG, "mSoapObject3="+mSoapObject3);

                }
            } catch (JSONException e) {
                server_response = true;
                e.printStackTrace();
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
            Log.e(TAG, "Folder id = " + FolderID);
            Log.e(TAG, "Folder name = " + FolderName);
            if (server_response) {
                server_response = false;
                Log.e(TAG, "Folder not found Exception");
                navDrawerItems_folder.clear();
                adapter_folder = new MessageItemListAdapter(getApplicationContext(), navDrawerItems_folder);
                mDrawerList_folder.setAdapter(adapter_folder);
                titles.add("Inbox");
                titles.add("Sent Item");
                titles.add("Trash");
                navDrawerItems_main.add(new MessageItems("  Inbox"));
                navDrawerItems_main.add(new MessageItems("  Sent Item"));
                navDrawerItems_main.add(new MessageItems("  Trash"));
                adapter_main = new MessageItemListAdapter(getApplicationContext(), navDrawerItems_main);
                mDrawerList_main.setAdapter(adapter_main);
                displayView_Main(0);
            } else {
                if (dialog_folder) {
                    if (FolderID.size() > 0) {
                        Log.e(TAG, "in if");
                        folders.setVisibility(View.VISIBLE);
                        navDrawerItems_main.add(new MessageItems("  Inbox"));
                        navDrawerItems_main
                                .add(new MessageItems("  Sent Item"));
                        navDrawerItems_main.add(new MessageItems("  Trash"));
                        titles.add("Inbox");
                        titles.add("Sent Item");
                        titles.add("Trash");

                        for (int x = 0; x < FolderName.size(); x++) {
                            String f_name = FolderName.get(x).toString();
                            String at_split[] = f_name.split("\\W");
                            String after_split = "";
                            String pre_split = "";
                            for (int c = 0; c < at_split.length; c++) {
                                if (at_split[c].isEmpty()
                                        || at_split[c].equalsIgnoreCase("")) {
                                    pre_split = pre_split + "*" + at_split[c];
                                } else {
                                    after_split = after_split + at_split[c]
                                            + " ";

                                }
                            }
                            titles.add(after_split.toString().trim());
                            String name_folder = pre_split + "└" + after_split;
                            name_folder = name_folder.replaceAll("\\*", " ");
                            Final_FolderName.add(name_folder);
                        }
                        Log.e(TAG, "" + Final_FolderName);

                        for (int k = 0; k < Final_FolderName.size(); k++) {
                            if (Final_FolderName
                                    .get(k)
                                    .toString()
                                    .equalsIgnoreCase(
                                            "└Please Select a Parent ")) {
                                Log.e(TAG, "ME" + Final_FolderName.get(k));
                            } else {
                                // Log.e(TAG, ""+Final_FolderName.get(k));
                                navDrawerItems_folder.add(new MessageItems(
                                        Final_FolderName.get(k)));

                            }
                        }
                        adapter_main = new MessageItemListAdapter(getApplicationContext(), navDrawerItems_main);
                        mDrawerList_main.setAdapter(adapter_main);
                        adapter_folder = new MessageItemListAdapter(getApplicationContext(), navDrawerItems_folder);
                        mDrawerList_folder.setAdapter(adapter_folder);
                        displayView_Main(0);
                    } else {
                        Log.e(TAG, "in else");
                        folders.setVisibility(View.GONE);
                        navDrawerItems_folder.clear();
                        adapter_folder = new MessageItemListAdapter(getApplicationContext(), navDrawerItems_folder);
                        mDrawerList_folder.setAdapter(adapter_folder);
                        titles.add("Inbox");
                        titles.add("Sent Item");
                        titles.add("Trash");
                        navDrawerItems_main.add(new MessageItems("  Inbox"));
                        navDrawerItems_main
                                .add(new MessageItems("  Sent Item"));
                        navDrawerItems_main.add(new MessageItems("  Trash"));
                        adapter_main = new MessageItemListAdapter(getApplicationContext(), navDrawerItems_main);
                        mDrawerList_main.setAdapter(adapter_main);
                        displayView_Main(0);
                    }
                } else {
                    Log.e(TAG, "in else");
                    folders.setVisibility(View.GONE);
                    navDrawerItems_folder.clear();
                    adapter_folder = new MessageItemListAdapter(getApplicationContext(), navDrawerItems_folder);
                    mDrawerList_folder.setAdapter(adapter_folder);
                    titles.add("Inbox");
                    titles.add("Sent Item");
                    titles.add("Trash");
                    navDrawerItems_main.add(new MessageItems("  Inbox"));
                    navDrawerItems_main.add(new MessageItems("  Sent Item"));
                    navDrawerItems_main.add(new MessageItems("  Trash"));
                    adapter_main = new MessageItemListAdapter(getApplicationContext(), navDrawerItems_main);
                    mDrawerList_main.setAdapter(adapter_main);
                    displayView_Main(0);
                }
            }
        }
    }

    private class DeleteFolder extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.METHOD_NAME_Mail_DeleteFolder);
            request.addProperty("token", WW_StaticClass.UserToken);
            request.addProperty("FolderID", SelectedFolder_id);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(
                        SOAP_CONSTANTS.SOAP_Action_Mail_DeleteFolder, envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i(TAG, "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                Log.i(TAG, "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    delete_folder = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    Log.e(TAG, jo.getString("DeleteFolder"));
                    folder_created_or_not = jo.getString("DeleteFolder");
                } else if (code.equalsIgnoreCase("111")) {
                    delete_folder = false;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    Log.e(TAG, jo.getString("DeleteFolder"));
                    folder_created_or_not = jo.getString("DeleteFolder");

                }
            } catch (JSONException e) {
                server_response = true;
                e.printStackTrace();
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
            if (server_response) {
                SingleOptionAlertWithoutTitle
                        .ShowAlertDialog(
                                MessageCenterActivity.this,
                                "LAFitnessApp",
                                "Server not responding.\nPlease check internet connection or try after sometime.",
                                "OK");
                server_response = false;
            } else {
                if (delete_folder) {
                    Toast.makeText(MessageCenterActivity.this,
                            folder_created_or_not, Toast.LENGTH_LONG).show();
                    new DialogData().execute();
                    new GetParentID().execute();
                    delete_folder = false;
                } else {
                    Toast.makeText(MessageCenterActivity.this,
                            folder_created_or_not, Toast.LENGTH_LONG).show();
                }
            }
        }

        ;
    }

    private void createUiChangeListener() {

        decorView
                .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                    @Override
                    public void onSystemUiVisibilityChange(int pVisibility) {

                        if ((pVisibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(uiOptions);
                        }

                    }

                });

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
        // decorView.setSystemUiVisibility(uiOptions);
        isInternetPresent = Utility
                .isNetworkConnected(MessageCenterActivity.this);
        if (isInternetPresent) {
            new GetParentID().execute();
            new DialogData().execute();
        } else {
            onDetectNetworkState().show();
        }

        mDrawerList_main
                .setOnItemClickListener(new SlideMenuMainClickListener());
        mDrawerList_folder
                .setOnItemClickListener(new SlideMenuFolderClickListener());
        mDrawerList_folder
                .setOnItemLongClickListener(new OnItemLongClickListener() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent,
                                                   final View view, final int position, long id) {
                        // TODO Auto-generated method stub
                        View popUpView = getLayoutInflater().inflate(
                                R.layout.popup, null);
                        mpopup = new PopupWindow(popUpView,
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT, true);
                        // mpopup.setAnimationStyle(android.R.style.Animation_Activity);
                        mpopup.setBackgroundDrawable(new BitmapDrawable());
                        mpopup.setOutsideTouchable(true);
                        mpopup.setOnDismissListener(new OnDismissListener() {

                            @Override
                            public void onDismiss() {
                                // TODO Auto-generated method stub

                            }
                        });
                        mpopup.showAsDropDown(view);
                        Button editfolder = (Button) popUpView
                                .findViewById(R.id.popupeditfolder);
                        Button newsubfolder = (Button) popUpView
                                .findViewById(R.id.popupnewfolder);
                        Button deletefolder = (Button) popUpView
                                .findViewById(R.id.popupdeletefolder);
                        editfolder.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                mpopup.dismiss();
                                mDrawerLayout.closeDrawers();
                                selected_folder_name_for_edit = "";
                                SelectedFolder_id = "";
                                Folder_parent_id = "";
                                String f_name = FolderName.get(position + 1)
                                        .toString().trim();
                                String at_split[] = f_name.split("\\W");
                                String name_folder = "";
                                for (int c = 0; c < at_split.length; c++) {
                                    if (at_split[c].isEmpty()
                                            || at_split[c].equalsIgnoreCase("")) {

                                    } else {
                                        name_folder = name_folder + " "
                                                + at_split[c];
                                    }
                                }
                                SelectedFolder_id = FolderID.get(FolderName
                                        .indexOf(f_name));
                                Folder_parent_id = ParentID.get(Temp_FolderID
                                        .indexOf(SelectedFolder_id));
                                Log.e(TAG, "Selected Folder IDD = "
                                        + SelectedFolder_id);
                                Log.e(TAG, "Parent Folder IDD = "
                                        + Folder_parent_id);
                                String parent_folder_name;
                                if (Folder_parent_id.equalsIgnoreCase("-2")) {
                                    parent_folder_name = "Please Select a Parent";
                                } else {
                                    parent_folder_name = Temp_FolderName
                                            .get(Temp_FolderID
                                                    .indexOf(Folder_parent_id));
                                    Log.e(TAG, "Temp Index of parent id = "
                                            + parent_folder_name);
                                }

                                selected_folder_name_for_edit = name_folder;

                                final Dialog dialog = new Dialog(
                                        MessageCenterActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawable(
                                        new ColorDrawable(Color.TRANSPARENT));
                                dialog.setContentView(R.layout.folder_edit_folder);
                                dialog.setCancelable(false);
                                final Button btn_edit_folder_parent = (Button) dialog
                                        .findViewById(R.id.btn_dialog_folder_list);
                                final EditText et_editText = (EditText) dialog
                                        .findViewById(R.id.et_dialog_folder_name);
                                Button btn_edit_ok = (Button) dialog
                                        .findViewById(R.id.btn_dialog_folder_ok);
                                Button btn_edit_cancel = (Button) dialog
                                        .findViewById(R.id.btn_dialog_folder_cancel);
                                btn_edit_ok.setText("Update");
                                et_editText
                                        .setText(selected_folder_name_for_edit);
                                btn_edit_folder_parent
                                        .setText(parent_folder_name);
                                btn_edit_cancel
                                        .setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method
                                                // stub
                                                dialog.dismiss();
                                            }
                                        });
                                btn_edit_folder_parent
                                        .setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method
                                                // stub
                                                list_edit_parent.show();
                                            }
                                        });
                                btn_edit_ok
                                        .setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method
                                                // stub

                                                if (et_editText.getText()
                                                        .toString().isEmpty()) {
                                                    Toast.makeText(
                                                            MessageCenterActivity.this,
                                                            "Please select folder to edit.",
                                                            Toast.LENGTH_LONG).show();
                                                } else {

                                                    new_folder_name = et_editText
                                                            .getText()
                                                            .toString();
                                                    editclick = true;
                                                    if (SelectedFolder_id
                                                            .equalsIgnoreCase(Folder_parent_id)) {
                                                        SelectedFolder_id = "";
                                                    }
                                                    Log.i(TAG,
                                                            "Selected Folder Id = "
                                                                    + SelectedFolder_id);
                                                    Log.i(TAG,
                                                            "Folder Parent Id = "
                                                                    + Folder_parent_id);
                                                    new CreateFolder()
                                                            .execute();
                                                    dialog.dismiss();
                                                }
                                            }
                                        });

                                list_edit_parent
                                        .setAdapter(new ArrayAdapter<String>(
                                                getApplicationContext(),
                                                R.layout.edittextpopup,
                                                Final_FolderName));
                                list_edit_parent
                                        .setAnchorView(btn_edit_folder_parent);
                                list_edit_parent.setHeight(500);
                                list_edit_parent.setModal(true);
                                list_edit_parent
                                        .setOnItemClickListener(new OnItemClickListener() {

                                            @Override
                                            public void onItemClick(
                                                    AdapterView<?> parent,
                                                    View view, int pos, long id) {
                                                // TODO Auto-generated method
                                                // stub
                                                String f_name = FolderName
                                                        .get(pos).toString()
                                                        .trim();
                                                Log.e(TAG, "" + f_name);
                                                String a[] = f_name
                                                        .split("\\W");
                                                String b = "";
                                                for (int c = 0; c < a.length; c++) {
                                                    if (a[c].isEmpty()
                                                            || a[c].equalsIgnoreCase("")) {

                                                    } else {
                                                        b = b + " " + a[c];
                                                    }
                                                }
                                                Log.e(TAG, "here = " + b.trim());
                                                btn_edit_folder_parent
                                                        .setText(b);
                                                if (btn_edit_folder_parent
                                                        .getText()
                                                        .toString()
                                                        .trim()
                                                        .equalsIgnoreCase(
                                                                "Please Select a Parent...")
                                                        || btn_edit_folder_parent
                                                        .getText()
                                                        .toString()
                                                        .trim()
                                                        .equalsIgnoreCase(
                                                                "Please Select a Parent")) {
                                                    Folder_parent_id = "Select";
                                                } else {
                                                    Folder_parent_id = Temp_FolderID
                                                            .get(Temp_FolderName
                                                                    .indexOf(btn_edit_folder_parent
                                                                            .getText()
                                                                            .toString()
                                                                            .trim()));
                                                }

                                                Log.i(TAG,
                                                        "Selcted folder id = "
                                                                + SelectedFolder_id);
                                                Log.i(TAG,
                                                        "Folder Parent id = "
                                                                + Folder_parent_id);

                                                list_edit_parent.dismiss();
                                            }
                                        });

                                dialog.show();
                            }
                        });

                        newsubfolder.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                selected_folder_name_for_edit = "";
                                SelectedFolder_id = "";
                                Folder_parent_id = "";

                                mDrawerLayout.closeDrawers();
                                String f_name = FolderName.get(position + 1)
                                        .toString().trim();
                                String at_split[] = f_name.split("\\W");
                                String name_folder = "";
                                for (int c = 0; c < at_split.length; c++) {
                                    if (at_split[c].isEmpty()
                                            || at_split[c].equalsIgnoreCase("")) {

                                    } else {
                                        name_folder = name_folder + " "
                                                + at_split[c];
                                    }
                                }
                                selected_folder_name_for_edit = name_folder;
                                // Folder_parent_id =
                                // Temp_FolderID.get(Temp_FolderName.indexOf(f_name));
                                Folder_parent_id = FolderID.get(FolderName
                                        .indexOf(f_name));
                                Log.e(TAG, "Sub folder parent id = "
                                        + Folder_parent_id);
                                // Toast.makeText(MessageCenterActivity.this,
                                // name_folder, 1).show();
                                mpopup.dismiss();
                                final Dialog dialog = new Dialog(
                                        MessageCenterActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.getWindow().setBackgroundDrawable(
                                        new ColorDrawable(Color.TRANSPARENT));
                                dialog.setContentView(R.layout.folder_edit_folder);
                                dialog.setCancelable(false);
                                final Button btn_edit_folder_parent = (Button) dialog
                                        .findViewById(R.id.btn_dialog_folder_list);
                                final EditText et_editText = (EditText) dialog
                                        .findViewById(R.id.et_dialog_folder_name);
                                TextView tv_edit_folder_name = (TextView) dialog
                                        .findViewById(R.id.tv_edit_folder_name);
                                tv_edit_folder_name.setText("New folder");
                                Button btn_edit_ok = (Button) dialog
                                        .findViewById(R.id.btn_dialog_folder_ok);
                                Button btn_edit_cancel = (Button) dialog
                                        .findViewById(R.id.btn_dialog_folder_cancel);
                                btn_edit_ok.setText("Update");
                                et_editText.setText("");
                                et_editText.setHint("Folder name");
                                btn_edit_folder_parent
                                        .setText(selected_folder_name_for_edit);
                                btn_edit_cancel
                                        .setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method
                                                // stub
                                                dialog.dismiss();
                                            }
                                        });
                                btn_edit_folder_parent
                                        .setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method
                                                // stub
                                                list_edit_parent.show();
                                            }
                                        });
                                btn_edit_ok
                                        .setOnClickListener(new OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                // TODO Auto-generated method
                                                // stub

                                                if (et_editText.getText()
                                                        .toString().isEmpty()) {
                                                    Toast.makeText(
                                                            MessageCenterActivity.this,
                                                            "Please select folder to edit.",
                                                            Toast.LENGTH_LONG).show();
                                                } else {
                                                    Log.i(TAG,
                                                            "Selected Folder Id = "
                                                                    + SelectedFolder_id);
                                                    Log.i(TAG,
                                                            "Folder Parent Id = "
                                                                    + Folder_parent_id);
                                                    new_folder_name = et_editText
                                                            .getText()
                                                            .toString();
                                                    editclick = true;
                                                    new CreateFolder()
                                                            .execute();
                                                    dialog.dismiss();
                                                }
                                            }
                                        });

                                list_edit_parent
                                        .setAdapter(new ArrayAdapter<String>(
                                                getApplicationContext(),
                                                R.layout.edittextpopup,
                                                Final_FolderName));
                                list_edit_parent
                                        .setAnchorView(btn_edit_folder_parent);
                                list_edit_parent.setHeight(500);
                                list_edit_parent.setModal(true);
                                list_edit_parent
                                        .setOnItemClickListener(new OnItemClickListener() {

                                            @Override
                                            public void onItemClick(
                                                    AdapterView<?> parent,
                                                    View view, int pos, long id) {
                                                // TODO Auto-generated method
                                                // stub
                                                String f_name = FolderName
                                                        .get(pos).toString()
                                                        .trim();
                                                Log.e(TAG, "" + f_name);
                                                String a[] = f_name
                                                        .split("\\W");
                                                String b = "";
                                                for (int c = 0; c < a.length; c++) {
                                                    if (a[c].isEmpty()
                                                            || a[c].equalsIgnoreCase("")) {

                                                    } else {
                                                        b = b + " " + a[c];
                                                    }
                                                }
                                                Log.e(TAG, "here = " + b.trim());
                                                btn_edit_folder_parent
                                                        .setText(b);
                                                if (btn_edit_folder_parent
                                                        .getText()
                                                        .toString()
                                                        .trim()
                                                        .equalsIgnoreCase(
                                                                "Please Select a Parent...")
                                                        || btn_edit_folder_parent
                                                        .getText()
                                                        .toString()
                                                        .trim()
                                                        .equalsIgnoreCase(
                                                                "Please Select a Parent")) {
                                                    Folder_parent_id = "Select";
                                                } else {
                                                    Folder_parent_id = Temp_FolderID
                                                            .get(Temp_FolderName
                                                                    .indexOf(btn_edit_folder_parent
                                                                            .getText()
                                                                            .toString()
                                                                            .trim()));
                                                }

                                                Log.i(TAG,
                                                        "Selcted folder id = "
                                                                + SelectedFolder_id);
                                                Log.i(TAG,
                                                        "Folder Parent id = "
                                                                + Folder_parent_id);

                                                list_edit_parent.dismiss();
                                            }
                                        });

                                dialog.show();
                            }
                        });

                        deletefolder.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                mpopup.dismiss();
                                mDrawerLayout.closeDrawers();
                                selected_folder_name_for_edit = "";
                                SelectedFolder_id = "";
                                Folder_parent_id = "";
                                String f_name = FolderName.get(position + 1)
                                        .toString().trim();
                                String at_split[] = f_name.split("\\W");
                                String name_folder = "";
                                for (int c = 0; c < at_split.length; c++) {
                                    if (at_split[c].isEmpty()
                                            || at_split[c].equalsIgnoreCase("")) {

                                    } else {
                                        name_folder = name_folder + " "
                                                + at_split[c];
                                    }
                                }
                                SelectedFolder_id = FolderID.get(FolderName
                                        .indexOf(f_name));
                                Log.e(TAG, "Folder id to delete = "
                                        + SelectedFolder_id);
                                if (SelectedFolder_id.isEmpty()) {

                                } else {
                                    new DeleteFolder().execute();
                                }
                            }
                        });
                        return true;
                    }
                });
    }

}
