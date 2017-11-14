package water.works.waterworks.chat;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.views.ScrollView;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import water.works.waterworks.R;
import water.works.waterworks.dbhelper.DatabaseHandler;
import water.works.waterworks.utils.SOAP_CONSTANTS;
import water.works.waterworks.utils.WW_StaticClass;


public class Chat_Room extends Activity {
    String message;
    EditText msg;
    TextView received, chat_name;
    Button send;
    ImageView online_Status, attach, imgBack;
    Context mContext = this;
    LinearLayout inflate_msg;
    //Pubnub Vars
    Pubnub pubnub;
    GoogleCloudMessaging gcm;
    SharedPreferences prefs;
    public static String SENDER_ID = "468292255530";
    public static String REG_ID;
    private static final String APP_VERSION = "3.6.1";
    long totalSize = 0;

    String PUBLISH_KEY = "pub-c-0a1972fb-6b7f-4893-995e-2f45ba5ea420";
    String SUBSCRIBE_KEY = "sub-c-959ec618-3440-11e5-a033-0619f8945a4f";
    String CIPHER_KEY = "";
    String SECRET_KEY = "";
    String ORIGIN = "pubsub";
    String AUTH_KEY;
    String UUID;
    Boolean SSL = false;
    String URL = "http://192.168.1.131/AndroidFileUpload/fileUpload.php";
    String userid = "", c_chan = "", uname = "", Touid = "";
    String filepath = "";
    Uri selectedImage;
    ImageLoader imageLoader;
    ProgressBar progressBar1;
    private static final int SELECT_PHOTO = 100;
    String regId = "RegistrationID";
    //	ScrollView inflate_scroll;
    ScrollView inflate_scroll;
    static final String TAG = "Chat Activity";

    //History Arrays
    ArrayList<String> Arr_text, Arr_time, Arr_uid, Arr_fromname;
    public boolean first_noti = true;
    public String check_exist;

    //	ProgressBar history_loading;
    ProgressBarCircularIndeterminate history_loading;
    //Multicolor Chat Users
    int GlobalC = 0;
    String colors[] = {"#060505", "#6F2929", "#C90707", "#FF9A9A", "#8979EE"
            , "#221A55"};
    HashMap<String, String> userColor = new HashMap<String, String>();
    String action = "";
    DatabaseHandler db = new DatabaseHandler(mContext);
    Thread thread;

    public int LastReadCount = 0, AllMsgs = 0;


    private void notifyUser(Object message) {

        try {
            if (message instanceof JSONObject) {
                final JSONObject obj = (JSONObject) message;

                final String text = obj.getString("text");
                final String uid = obj.getString("uid");
                final String filepath = obj.getString("path");
                final String time = obj.getString("time");

                this.runOnUiThread(new Runnable() {
                    public void run() {
                        first_noti = false;
                        if (!uid.equals(userid)) {
                            inflate_chat_receive(text, filepath, time, "", uid);
                        } else {
                            inflate_chat_send(text, filepath, time);
                        }
                        Log.d(" JSON Received msg : ", String.valueOf(obj));
                    }
                });
            } else if (message instanceof JSONObject) {
//				final String obj = (String) message;
                JSONObject jsn = (JSONObject) message;

                AllMsgs++;
                String uid_temp = "";

                System.out.println("Count : " + AllMsgs);
                if (jsn.toString().contains("uuids")) {
                    System.out.println("Getting UUIDS");
                    final JSONArray jArray = jsn.getJSONArray("uuids");
                    if (jsn.toString().contains("action")) {
                        action = jsn.getString("action");
                    }
                    this.runOnUiThread(new Runnable() {

                        public void run() {
                            // TODO Auto-generated method stub
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    if (jArray.getString(i).equals(WW_StaticClass.UserName)) {
                                        if (action != "" && action.equals("join")) {
                                            online_Status.setVisibility(View.VISIBLE);
                                        } else {
                                            online_Status.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    new_msg_scroll();
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                } else {
                    String from_nametemp = "";
                    String time_stamp = "00:00";
                    final String text = jsn.getString("text");

                    if (jsn.toString().contains("uid")) {
                        uid_temp = jsn.getString("uid");
                    } else {
                        uid_temp = userid;
                    }
                    final String uid = uid_temp;

//					final String filepath = jsn.getString("path");
                    //				final String name = jsn.getString("uname");
                    if (jsn.toString().contains("time")) {
                        time_stamp = jsn.getString("time");
                    }

                    final String time = time_stamp;

                    if (jsn.toString().contains("my_name")) {
                        from_nametemp = jsn.getString("my_name");
                    }
                    final String from_name = from_nametemp;

                    this.runOnUiThread(new Runnable() {
                        public void run() {
                            first_noti = false;
                            if (!uid.equals(userid)) {
                                inflate_chat_receive(text, filepath, time, from_name, uid);
                            } else {
                                inflate_chat_send(text, filepath, time);
                            }
                            new_msg_scroll();
//							Log.d(" String Received msg : ", jsn.toString());
                        }
                    });

                }
            } else if (message instanceof JSONArray) {
                final JSONArray obj = (JSONArray) message;
                this.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d(" jArray Received msg : ", obj.toString());
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String registerGCM() {
        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(mContext);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
        }
        return regId;
    }

    @SuppressWarnings("unchecked")
    private void registerInBackground() {
        new AsyncTask() {
            @Override
            protected String doInBackground(Object... arg0) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regId = gcm.register(SENDER_ID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                    storeRegistrationId(mContext, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }

            protected void onPostExecute(Object result) {
                //				Toast.makeText(getApplicationContext(),
                //						"Registered with GCM Server." + msg, Toast.LENGTH_LONG)
                //						.show();
            }

            ;
        }.execute(null, null, null);
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(
                Chat_Room.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                Chat_Room.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.chat_lay);

        regId = registerGCM();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration
                .createDefault(mContext));

        init();

        if (SOAP_CONSTANTS.From.equals("noti")) {
            first_noti = false;
            userid = WW_StaticClass.InstructorID;
            c_chan = SOAP_CONSTANTS.ChannelName;
            uname = SOAP_CONSTANTS.FromName;
            Touid = getToUid(c_chan);
            inflate_chat_receive(SOAP_CONSTANTS.FirstMsg, "", getcurrenttime(), "", Touid);
        } else {
            Intent i = getIntent();
            userid = i.getStringExtra("uid");
            c_chan = i.getStringExtra("c_chan");
            uname = i.getStringExtra("uname");
            Touid = i.getStringExtra("Touid");
        }

        check_exist = db.CheckExistance(c_chan);
        String lastCount = db.get_lastRead_Count(c_chan);
        if (!lastCount.equals("")) {
            LastReadCount = Integer.valueOf(lastCount);
        } else {
            LastReadCount = 0;
        }


        SOAP_CONSTANTS.current_channel = c_chan;

        UUID = WW_StaticClass.UserName;
        pubnub.setUUID(UUID);

        chat_name.setText(uname);
        prefs = getSharedPreferences(
                "PUBNUB_DEV_CONSOLE", Context.MODE_PRIVATE);

        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                pubnub.disconnectAndResubscribe();
            }

        }, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        subscribe(c_chan);
        History(c_chan);
        //		set_history();
        //		new Set_His().execute();

        //		if(response_ex!=null){

        //		}
        onlineStatus();
        hereNow();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        updateTodb();
        SOAP_CONSTANTS.current_channel = "channel";
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        updateTodb();
        SOAP_CONSTANTS.current_channel = "channel";
        finish();
    }

    public void hereNow() {
        pubnub.hereNow(c_chan, new Callback() {
            public void successCallback(String channel,
                                        Object message) {
                System.out.println("Online status HereNow : " + message.toString());
                notifyUser(message.toString());
            }

            public void errorCallback(String channel,
                                      Object message) {
                notifyUser(channel + " : " +
                        message.toString());
            }
        });
    }

    public void onlineStatus() {
        Callback callback = new Callback() {
            @Override
            public void connectCallback(String channel, Object message) {
                System.out.println("CONNECT on channel:" + channel
                        + " : " + message.getClass() + " : "
                        + message.toString());
            }

            @Override
            public void disconnectCallback(String channel, Object message) {
                System.out.println("DISCONNECT on channel:" + channel
                        + " : " + message.getClass() + " : "
                        + message.toString());
            }

            @Override
            public void reconnectCallback(String channel, Object message) {
                System.out.println("RECONNECT on channel:" + channel
                        + " : " + message.getClass() + " : "
                        + message.toString());
            }

            @Override
            public void successCallback(String channel, Object message) {
                System.out.println(channel + " : "
                        + message.getClass() + " : " + message.toString());
            }

            @Override
            public void errorCallback(String channel, PubnubError error) {
                System.out.println("ERROR on channel " + channel
                        + " : " + error.toString());
            }
        };

        try {
            pubnub.presence(c_chan, callback);
        } catch (PubnubException e) {
            System.out.println(e.toString());
        }
    }

    public void new_msg_scroll() {
//		inflate_scroll.scrollTo(0, inflate_scroll.getBottom());
//		inflate_scroll.fullScroll(inflate_scroll.FOCUS_DOWN);
        inflate_scroll.fullScroll(ScrollView.FOCUS_DOWN);
        inflate_scroll.postDelayed(new Runnable() {

            public void run() {
                inflate_scroll.fullScroll(ScrollView.FOCUS_DOWN);
//				inflate_scroll.scrollTo(0, inflate_scroll.getBottom());
            }
        }, 500);
    }

    private void init() {

        msg = (EditText) findViewById(R.id.msg);
        chat_name = (TextView) findViewById(R.id.chat_name);
        online_Status = (ImageView) findViewById(R.id.online_Status);
        send = (Button) findViewById(R.id.senddd);
        inflate_msg = (LinearLayout) findViewById(R.id.inflate_msg);
        attach = (ImageView) findViewById(R.id.attach);
        inflate_scroll = (ScrollView) findViewById(R.id.inflate_scroll);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        imgBack = (ImageView) findViewById(R.id.imgBack);
        history_loading = (ProgressBarCircularIndeterminate) findViewById(R.id.history_loading);

        pubnub = new Pubnub(
                PUBLISH_KEY,
                SUBSCRIBE_KEY,
                SECRET_KEY,
                CIPHER_KEY,
                SSL
        );
        pubnub.setCacheBusting(false);
        pubnub.setOrigin(ORIGIN);
        pubnub.setAuthKey(AUTH_KEY);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        attach.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, SELECT_PHOTO);
            }
        });

        send.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                message = msg.getText().toString();
                if (msg.getText().toString().trim().length() > 0) {
                    if (first_noti) {
                        new FirstTime().execute();
                        String msgss = msg.getText().toString();
                        publish(c_chan, uname, msgss, "");
                        msg.setText("");
                    } else {
                        new FirstTime().execute();
                        String msgss = msg.getText().toString();
                        publish(c_chan, uname, msgss, "");
                        msg.setText("");
                    }
                }
            }
        });

        imgBack.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                updateTodb();
                onBackPressed();
            }
        });
    }

    public void updateTodb() {
        if (check_exist.equals("")) {
            db.LastRead(c_chan, String.valueOf(LastReadCount + (AllMsgs - LastReadCount)));
        } else {
            db.updateExist(c_chan, String.valueOf(LastReadCount + (AllMsgs - LastReadCount)));
        }
    }

    private void subscribe(String channel) {

        try {
            pubnub.subscribe(channel, new Callback() {
                @Override
                public void connectCallback(String channel,
                                            Object message) {
                    notifyUser("SUBSCRIBE : CONNECT on channel:"
                            + channel
                            + " : "
                            + message.getClass()
                            + " : "
                            + message.toString());
                }

                @Override
                public void disconnectCallback(String channel,
                                               Object message) {
                    notifyUser("SUBSCRIBE : DISCONNECT on channel:"
                            + channel
                            + " : "
                            + message.getClass()
                            + " : "
                            + message.toString());
                }

                @Override
                public void reconnectCallback(String channel,
                                              Object message) {
                    notifyUser("SUBSCRIBE : RECONNECT on channel:"
                            + channel
                            + " : "
                            + message.getClass()
                            + " : "
                            + message.toString());
                }

                @Override
                public void successCallback(String channel,
                                            Object message) {

                    notifyUser(message.toString());
                }

                @Override
                public void errorCallback(String channel,
                                          PubnubError error) {
                    notifyUser("SUBSCRIBE : ERROR on channel "
                            + channel + " : "
                            + error.toString());
                }
            });

        } catch (Exception e) {

        }
    }

    Object response_ex;
    boolean response_true = false;

    public void History(String channel) {
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                System.out.println(response.toString());
                response_ex = response;
                response_true = true;
                //				new Set_His().execute();

                Arr_fromname = new ArrayList<String>();
                Arr_text = new ArrayList<String>();
                Arr_time = new ArrayList<String>();
                Arr_uid = new ArrayList<String>();

                if (response_ex instanceof JSONArray) {
                    try {
                        String temp_St = response_ex.toString().substring(1, response_ex.toString().length() - 1);
                        JSONArray jarray = new JSONArray(temp_St);
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject obj = jarray.getJSONObject(i);
                            notifyUser(obj.toString());

                            if (obj.toString().contains("uid")) {
                                final String uid = obj.getString("uid");
                                multiColor(uid);
                            }
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    history_loading.setVisibility(View.GONE);
                }
                new_msg_scroll();
            }

            public void errorCallback(String channel, PubnubError error) {
                System.out.println(error.toString());
                thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            synchronized (this) {
                                wait(5000);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        history_loading.setVisibility(View.GONE);
                                    }
                                });

                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    ;
                };
                thread.start();

            }


        };
        pubnub.history(channel, 100, false, callback);
    }

    public void multiColor(String uid) {
        if (GlobalC >= colors.length) {
            GlobalC = 0;
        }
        if (userColor.containsKey(uid)) {

        } else {
            userColor.put(uid, colors[GlobalC]);
            GlobalC++;
        }
    }

    private void publish(String channel, String name, String message, String attachment_path) {
        Callback publishCallback = new Callback() {
            @Override
            public void successCallback(String channel,
                                        Object message) {
//				notifyUser("PUBLISH : " + message);
            }

            @Override
            public void errorCallback(String channel,
                                      PubnubError error) {
                notifyUser("PUBLISH : " + error);
            }
        };

        try {
            JSONObject js = new JSONObject();
            js.put("text", message);
            js.put("uid", userid);
            js.put("uname", name);
            js.put("path", attachment_path);
            js.put("time", getcurrenttime());
            js.put("my_name", Chat_Friends_list.Current_Uname);
            pubnub.publish(channel, js, publishCallback);

            //			db.add_msg(Integer.valueOf(userid), name, message, c_chan, "0");


            return;
        } catch (Exception e) {
        }

    }


    public void inflate_chat_receive(String msg, String path, String time, String from_name, String uid) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_receivemsg_lay, null);
        TextView text = (TextView) view.findViewById(R.id.receive_msg);
        ImageView attach_image = (ImageView) view.findViewById(R.id.attach_image);
        LinearLayout txt_lay = (LinearLayout) view.findViewById(R.id.txt_lay);
        TextView time_lay = (TextView) view.findViewById(R.id.time_lay);
        final TextView from_nameTV = (TextView) view.findViewById(R.id.from_name);

        time_lay.setText(time);

        if (c_chan.contains("group")) {
            from_nameTV.setVisibility(View.VISIBLE);
            from_nameTV.setText(from_name);
            if (userColor.containsKey(uid)) {
                from_nameTV.setTextColor(Color.parseColor(userColor.get(uid)));
            }
        } else {
            from_nameTV.setVisibility(View.GONE);
        }
        if (path.trim().length() > 0) {
            attach_image.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            txt_lay.setVisibility(View.GONE);

            imageLoader.displayImage(path, attach_image);
        } else {
            text.setText(msg);
        }

        from_nameTV.setOnClickListener(new OnClickListener() {


            public void onClick(View v) {
                // TODO Auto-generated method stub
                System.out.println("Clicked View : " + Chat_Friends_list.name_id);
                System.out.println("Clicked View Name : " + from_nameTV.getText().toString());
                String firstWord = null, check_it = null;
                check_it = Chat_Friends_list.name_id.get(from_nameTV.getText().toString());

                if (check_it != null) {
                    individual_chat(Chat_Friends_list.name_id.get(from_nameTV.getText().toString()), from_nameTV.getText().toString());
                } else {
                    if (from_nameTV.getText().toString().contains(" ")) {
                        firstWord = from_nameTV.getText().toString().substring(0, from_nameTV.getText().toString().indexOf(" "));
                    } else {
                        firstWord = from_nameTV.getText().toString();
                    }
                    int gc = 0;
                    Iterator<Entry<String, String>> it = Chat_Friends_list.name_id.entrySet().iterator();
                    while (it.hasNext()) {
                        Entry<String, String> pairs = (Entry<String, String>) it.next();
                        boolean find_it = pairs.getKey().contains(firstWord);
                        System.out.println(pairs.getKey().contains(firstWord) + "" + pairs.getValue().contains(firstWord));
                        if (find_it) {
                            (new ArrayList<String>(Chat_Friends_list.name_id.values())).get(gc);
                            Chat_Friends_list.name_id.get(gc);
                            individual_chat((new ArrayList<String>(Chat_Friends_list.name_id.values())).get(gc), from_nameTV.getText().toString());
                            break;
                        }
                        gc++;
                    }
                }

                //				if(check_it==null){
                //
                //				}else{
                //
                //				}
            }
        });
        inflate_msg.addView(view);
    }

    public void inflate_chat_send(String msg, String path, String time) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_sendmsg_lay, null);
        TextView text = (TextView) view.findViewById(R.id.send_msg);
        ImageView attach_image = (ImageView) view.findViewById(R.id.attach_image);
        LinearLayout txt_lay = (LinearLayout) view.findViewById(R.id.txt_lay);
        TextView time_lay = (TextView) view.findViewById(R.id.time_lay);
        time_lay.setText(time);

        if (msg.contains("content:")) {
            Uri uri = Uri.parse(msg);
            attach_image.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            txt_lay.setVisibility(View.GONE);

            imageLoader.displayImage(getRealPathFromURI(uri), attach_image);

        } else {
            text.setText(msg);
        }
        inflate_msg.addView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (requestCode == SELECT_PHOTO) {
                    if (data != null) {
                        selectedImage = data.getData();
                        filepath = getRealPathFromURI(selectedImage);
                    }
                }
                break;

            default:
                break;
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }


    public void decodeFile(String filePath) {
        Bitmap bitmap;
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        bit_img(bitmap);

        //		image.setImageBitmap(bitmap);
    }

    public void bit_img(Bitmap bmp) {
        File f = new File("/sdcard/", "temp.png");
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Convert bitmap to byte array
        Bitmap bitmap = bmp;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getcurrenttime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateandTime = sdf.format(new Date());
        System.out.println("Current time : " + currentDateandTime);

        return currentDateandTime;
    }

    //	ProgressDialog pDialog;
    public class FirstTime extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //			pDialog = new ProgressDialog(Chat_Room.this);
            //			pDialog.setMessage(Html.fromHtml("Loading wait..."));
            //			pDialog.setIndeterminate(false);
            //			pDialog.setCancelable(true);
            //			pDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            SoapObject request = new SoapObject(SOAP_CONSTANTS.NAMESPACE,
                    SOAP_CONSTANTS.SendNotification);
            request.addProperty("UserID", Touid);
            request.addProperty("message", message);
            request.addProperty("FromName", WW_StaticClass.UserName);
            request.addProperty("ChannelName", c_chan);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    SOAP_CONSTANTS.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(SOAP_CONSTANTS.SOAP_Action_SendNotification,
                        envelope); // Calling Web service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("Any CEE", "Result : " + response.toString());
                first_noti = false;
            } catch (SocketTimeoutException e) {
                //				server_status = true;
                e.printStackTrace();
            } catch (Exception e) {
                //				server_status = true;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //			if(pDialog!=null && pDialog.isShowing()){
            //				pDialog.dismiss();
            //			}
        }
    }

    public String getToUid(String CurrentString) {
        String[] separated = CurrentString.split("_");
        String st_1 = separated[0]; // this will contain "Fruit"
        String st_2 = separated[1];
        String final_id = "";
        if (st_1.equals(userid)) {
            final_id = st_2;
        } else {
            final_id = st_1;
        }

        return final_id;
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
//        this.unregisterReceiver(null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//

    }

    public void individual_chat(String clicked_id, String clicked_name) {
        String temp_id = clicked_id;
        String temp_name = clicked_name;
        String chat_channel = "";
        System.out.println("ClickID : " + temp_id);

        if (!temp_id.contains("group")) {
            if (Integer.parseInt(userid) < Integer.parseInt(temp_id)) {
                chat_channel = userid + "_" + temp_id;
            } else {
                chat_channel = temp_id + "_" + userid;
            }
        } else {
            chat_channel = temp_id;
        }

        System.out.println("Chat_channel = " + chat_channel);
        Intent i = new Intent(mContext, Chat_Room.class);
        i.putExtra("c_chan", chat_channel);
        i.putExtra("uid", userid);
        i.putExtra("Touid", temp_id);
        i.putExtra("uname", temp_name);
        startActivity(i);
        finish();
    }
}
