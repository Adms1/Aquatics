package water.works.waterworks.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper{

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	private static final String DATABASE_NAME = "ChatManager";

	// Contacts table name
	private static final String TABLE_CHATS = "chat";
	private static final String TABLE_LastRead = "LastRead";

	// Contacts Table Columns names Chat
	private static final String KEY_UID = "uid";
	private static final String KEY_NAME = "name";
	private static final String KEY_MSG = "msg";
	private static final String KEY_Channel = "channel";
	private static final String KEY_LastRead = "lastread";

	// Contacts Table Columns names LastRead
	private static final String KEY_Channel_LR = "channel";
	private static final String KEY_LastRead_LR = "lastread";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CHATS + "("
				+ KEY_UID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_MSG + " TEXT" +  KEY_Channel + " TEXT " + KEY_LastRead + " TEXT" +")";

		String CREATE_LastRead_TABLE = "CREATE TABLE " + TABLE_LastRead + "("
				+ KEY_Channel_LR+ " TEXT," + KEY_LastRead_LR + " TEXT" +")";

		System.out.println("Chat : "+CREATE_CONTACTS_TABLE);
		System.out.println("LastRead : "+CREATE_LastRead_TABLE);

		db.execSQL(CREATE_CONTACTS_TABLE);
		db.execSQL(CREATE_LastRead_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHATS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LastRead);
		onCreate(db);
	}

	public void add_msg(int uid,String name,String msg,String channel,String lastRead){
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery("INSERT INTO chat (uid,name,msg,channel,lastread)VALUES('"+uid+"','"+name+"','"+msg+"','"+channel+"','"+lastRead+"') ", null);

		ContentValues values = new ContentValues();
		values.put(KEY_UID, uid);
		values.put(KEY_NAME, name);
		values.put(KEY_MSG, msg);
		values.put(KEY_Channel, channel);
		values.put(KEY_LastRead, lastRead);
		db.insert(TABLE_CHATS, null, values);

	}

	public String get_Msg(int uid){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_CHATS, new String[] { KEY_UID,
				KEY_NAME, KEY_MSG }, KEY_UID + "=?",
				new String[] { String.valueOf(uid) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		String msg = cursor.getString(2);
		return msg;
	}

	//	public String get_lastRead_Count(String channel){
	//		SQLiteDatabase db = this.getReadableDatabase();
	//		//		Cursor cursor = db.query(TABLE_CHATS, new String[] { KEY_UID,
	//		//	            KEY_NAME, KEY_MSG }, KEY_UID + "=?",
	//		//	            new String[] { String.valueOf(uid) }, null, null, null, null);
	//		//		if (cursor != null)
	//		//	        cursor.moveToFirst();
	//
	//		Cursor cursor = db.rawQuery("SELECT KEY_LastRead result FROM  chat WHERE KEY_Channel= '"+channel+"'", null);
	//
	//		String msg = cursor.getString(cursor.getColumnIndex("KEY_LastRead"));
	//		return msg;
	//	}


	//LastRead Table Query's
	public void LastRead(String channel,String lastRead){
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = "INSERT INTO LastRead (channel,lastread)VALUES('"+channel+"','"+lastRead+"')";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor!=null) 
		{ 
			cursor.moveToNext(); 
		} 

		System.out.println("Insert Query : "+Query);
	}

	public String CheckExistance(String channel){
		SQLiteDatabase db = this.getReadableDatabase();
		String msg="";
		String query = " SELECT channel FROM LastRead WHERE channel='"+channel+"' ";
		Cursor cursor = db.rawQuery(query, null);
		if(cursor.moveToFirst()){
			msg = cursor.getString(cursor.getColumnIndex("channel"));
		}
		
		return msg;

	}
	public String get_lastRead_Count(String channel){
		SQLiteDatabase db = this.getReadableDatabase();
		String msg="";
		String query = " SELECT lastread FROM LastRead WHERE channel='"+channel+"' ";
		System.out.println("Get Query : "+query);
		Cursor cursor = db.rawQuery(query, null);

		if(cursor.moveToFirst()){
			msg = cursor.getString(cursor.getColumnIndex("lastread"));
		}
		System.out.println("LastRead : "+msg);
		return msg;
	}
	public void updateExist(String channel, String counter){
		SQLiteDatabase db = this.getReadableDatabase();
		String Query = " UPDATE LastRead SET lastread = '"+counter+"' where channel='"+channel+"' ";
		Cursor cursor = db.rawQuery(Query, null);
		if (cursor!=null) 
		{ 
			cursor.moveToNext(); 
		} 

		System.out.println("Insert Query : "+Query);
	}
}
