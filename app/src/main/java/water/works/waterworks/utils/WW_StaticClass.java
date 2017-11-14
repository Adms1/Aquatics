package water.works.waterworks.utils;

import java.util.ArrayList;

public class WW_StaticClass {

	
	//LOGIN ACTIVITY
		public static String UserName ="UserName";
		public static String UserToken = "UserToken";
		public static String UserLevel = "UserLevel";
		public static String InstructorID="InstId";
		public static String DeckSuperID="DeckSuperId";
		public static String InstructorName = "InstructorName";
		public static ArrayList<String> siteid = new ArrayList<String>();
		public static ArrayList<String>	sitename = new ArrayList<String>();
		
		//POOLLIST ACTIVITY
		public static String PoolName="PoolName";
		public static String PoolId="PoolId";
		public static int duration1,duration2;
		//MainMenu
		public static String mainmenu_title="title";
		public static String mainmenu_content="content";
		
		//For Request shadow and desk via current lesson / attendance
		public static String Studentid = "sid";
		public static String SStudnetID = "SStudnetID";
		public static String Siteid ="siteid";
		
		// For Message Center
		public static String lable = "lable";
		public static String mailid = "mailid";
		public static String foldernumber = "f_num";
		public static int Msg_id_pos;
		public static int Folder_id ;
		
		public static String date_for_data,hour_for_data,min_for_data;
		
		public static String tempSiteId = "tempsiteid";
		
//		public static boolean attendance_sended = false;
}
