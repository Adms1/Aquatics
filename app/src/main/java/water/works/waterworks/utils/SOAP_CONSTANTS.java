package water.works.waterworks.utils;

public class SOAP_CONSTANTS {

	/**
	 * Client Local & Live
	 * Make build in Client's local app will automatically detect the server if 
	 * it didn't get the response then it'll change URLs to Live.
	 */
	//	public static String SOAP_ADDRESS = "http://192.168.1.201/WWWebService/Service.asmx?WSDL"; //U.S.
	//	public static String Report_Url="http://192.168.1.201/newcode/";
//			Live
		public static String SOAP_ADDRESS_1 = "http://office.waterworksswimonline.com/WWWebServices/Service.asmx/";
		public static String SOAP_ADDRESS = "http://office.waterworksswimonline.com/WWWebService/Service.asmx?WSDL";
		public static String Report_Url = "http://office.waterworksswimonline.com/newcode/";

	//Temp Local
		public static String NAMESPACE = "http://tempuri.org/";
		//new local 103.204.192.187
//		public static String SOAP_ADDRESS = "http://103.204.192.187:8081/WWWebServices/Service.asmx?WSDL"; //local
//		public static String SOAP_ADDRESS_1 = "http://103.204.192.187:8081/WWWebServices/Service.asmx/"; //local
//		public static String Report_Url="http://103.204.192.187:8081/newcode/";


	//SOAP BASIC local
	/*public static String NAMESPACE = "http://tempuri.org/";
	public static String SOAP_ADDRESS = "http://192.168.1.50:8081/WWWebServices/Service.asmx?WSDL"; //local
	public static String SOAP_ADDRESS_1 = "http://192.168.1.50:8081/WWWebServices/Service.asmx/"; //local
	public static String Report_Url="http://192.168.1.50:8081/newcode/";*/

	//SOAP ACTION
	public static String SOAP_ACTION_LOGIN = "http://tempuri.org/Login";
	public static String SOAP_ACTION_POOLLIST = "http://tempuri.org/GetPoolList";
	public static String SOAP_ACTION_Announcements = "http://tempuri.org/GetAnnouncements";
	public static String SOAP_Action_AttendanceList =  "http://tempuri.org/GetAttendanceList";
	public static String SOAP_Action_LevelList = "http://tempuri.org/GetLevelList";
	public static String SOAP_Action_InsertShadowRequest = "http://tempuri.org/InsertShadowRequest";
	public static String SOAP_Action_GetUsersByType = "http://tempuri.org/GetUsersByType";
	public static String SOAP_Action_InsertDeckAssist = "http://tempuri.org/InsertDeckAssist";
	public static String SOAP_Action_InsertDeckAssistUser = "http://tempuri.org/InsertDeckAssistUser";
	public static String SOAP_Action_GetStudentCommentsList = "http://tempuri.org/GetStudentCommentsList";
	public static String SOAP_Action_InsertStudentComment = "http://tempuri.org/InsertStudentComment";
	public static String SOAP_Action_DeleteStudentCommentByID= "http://tempuri.org/DeleteStudentCommentByID";
	public static String SOAP_Action_GetSiteList= "http://tempuri.org/Get_SiteList";
	public static String SOAP_Action_GetSiteList_new= "http://tempuri.org/GetSiteList_new";
	public static String SOAP_Action_GetSiteList_1= "http://tempuri.org/GetSiteList";
	public static String SOAP_Action_CT_GetManagerListBySite= "http://tempuri.org/CT_GetManagerListBySite";
	public static String SOAP_Action_Get_InstrctListBySite= "http://tempuri.org/Get_InstrctListBySite";
	public static String SOAP_ACTION_Insert_TerificTurbo="http://tempuri.org/Insert_TerificTurbo";
	public static String SOAP_Action_Get_Insert_TurboFlash= "http://tempuri.org/Insert_TurboFlash";
	public static String SOAP_Action_Mail_GetCommonInboxData= "http://tempuri.org/Mail_GetCommonInboxData";
	public static String SOAP_Action_Mail_Get_FolderList= "http://tempuri.org/Mail_Get_FolderList";
	public static String SOAP_Action_Mail_GetCommonSentData= "http://tempuri.org/Mail_GetCommonSentData";
	public static String SOAP_Action_Mail_GetCommonDeletedData= "http://tempuri.org/Mail_GetCommonDeletedData";
	public static String SOAP_Action_Mail_GetMessageById= "http://tempuri.org/Mail_GetMessageById";
	public static String SOAP_Action_Mail_MarkAsReadUnread= "http://tempuri.org/Mail_MarkAsReadUnread";
	public static String SOAP_Action_Mail_DeleteSelectedMessages= "http://tempuri.org/Mail_DeleteSelectedMessages";
	public static String SOAP_Action_Mail_CreateMessageFolder= "http://tempuri.org/Mail_CreateMessageFolder";
	public static String SOAP_Action_Mail_BindFolderTree= "http://tempuri.org/Mail_BindFolderTree";
	public static String SOAP_Action_Mail_DeleteFolder= "http://tempuri.org/Mail_DeleteFolder";
	public static String SOAP_Action_Mail_MoveMessageToFolder= "http://tempuri.org/Mail_MoveMessageToFolder";
	public static String SOAP_Action_NewMail_Get_UserListBySite= "http://tempuri.org/NewMail_Get_UserListBySite";
	public static String SOAP_Action_NewMail_NewMail_SendMail= "http://tempuri.org/NewMail_SendMail";
	public static String SOAP_Action_ViewSchl_GetViewScheduleByDateAndInstrid= "http://tempuri.org/ViewSchl_GetViewScheduleByDateAndInstrid";
	public static String SOAP_Action_Insert_Attandance= "http://tempuri.org/Insert_Attandance";
	public static String SOAP_Action_InsertShadowRequest_LA = "http://tempuri.org/InsertShadowRequest_LA";
	public static String SOAP_Action_Insert_SwimCompCancellation= "http://tempuri.org/Insert_SwimCompCancellation";
	public static String SOAP_Action_Insert_SendAttForReview= "http://tempuri.org/SendAttForReview";
	public static String SOAP_Action_Insert_Attandance_ForToday = "http://tempuri.org/Insert_Attandance_ForToday";
	public static String SOAP_Action_ViewSchl_GetViewScheduleByDeckSupervisorAllInstr = "http://tempuri.org/ViewSchl_GetViewScheduleByDeckSupervisorAllInstr";
	public static String SOAP_Action_ViewSchl_GetViewScheduleByDeckSupervisorAndInstrWise = "http://tempuri.org/ViewSchl_GetViewScheduleByDeckSupervisorAndInstrWise";
	public static String SOAP_Action_Get_InstrctListForMgrBySite = "http://tempuri.org/Get_InstrctListForMgrBySite";
	public static String SOAP_Action_ViewSchl_GetViewScheduleByDeckSupervisorSelectedInstr = "http://tempuri.org/ViewSchl_GetViewScheduleByDeckSupervisorSelectedInstr";
	public static String SOAP_Action_InsertManabgersShadowTime = "http://tempuri.org/InsertManabgersShadowTime";
	public static String SOAP_Action_UpdateManabgersShadowEndTime = "http://tempuri.org/UpdateManabgersShadowEndTime";
	public static String SOAP_Action_InsertManabgersShadowLog = "http://tempuri.org/InsertManabgersShadowLog";
	public static String SOAP_Action_EndManabgersAllShadows = "http://tempuri.org/EndManabgersAllShadows";
	public static String SOAP_Action_GetManabgersShadowLog = "http://tempuri.org/GetManabgersShadowLog";
	public static String SOAP_Action_GetAllEmployeeList = "http://tempuri.org/GetAllEmployeeList";
	public static String SOAP_Action_GetAllEmployeeListByDevice = "http://tempuri.org/GetAllEmployeeListByDevice";
	public static String SOAP_Action_SendNotification = "http://tempuri.org/SendNotification";
	public static String SOAP_Action_EmpComLog_Insert_EmpComLogDetails = "http://tempuri.org/EmpComLog_Insert_EmpComLogDetails";
	public static String SOAP_Action_EmpComLog_Get_EmpComLogDetails = "http://tempuri.org/EmpComLog_Get_EmpComLogDetails";
	public static String SOAP_Action_EmpComLog_Delete_EmpComLogDetails = "http://tempuri.org/EmpComLog_Delete_EmpComLogDetails";
	public static String SOAP_Action_GetISAAlert = "http://tempuri.org/GetISAAlert";
	public static String SOAP_Action_SaveStudentImage = "http://tempuri.org/SaveStudentImage";
	public static String GetAttendanceList_Multiple_Action = NAMESPACE
			+"GetAttendanceList_Multiple";
	public static String Insert_Attandance_Action = NAMESPACE
			+ "Insert_Attandance_LA";
	public static String GetDeckAssistList_Action = NAMESPACE+"GetDeckAssistList";

	//SOAP METHOD
	public static String GetAttendanceList_Multiple_Method = "GetAttendanceList_Multiple";
	public static String METHOD_NAME_LOGIN= "Login";
	public static String METHOD_NAME_GETPOOLLIST = "GetPoolList";
	public static String METHOD_NAME_GetAnnouncements  = "GetAnnouncements";
	//	public static String METHOD_NAME_GetCurrentScheduleSite  = "GetCurrentScheduleSite"; 
	public static String METHOD_NAME_GetAttendanceList  = "GetAttendanceList";
	public static String METHOD_NAME_GetLevelList = "GetLevelList";
	public static String METHOD_NAME_InsertShadowRequest ="InsertShadowRequest";
	public static String METHOD_NAME_GetUsersByType ="GetUsersByType";
	public static String METHOD_NAME_InsertDeckAssist ="InsertDeckAssist";
	public static String METHOD_NAME_InsertDeckAssistUser ="InsertDeckAssistUser";
	public static String METHOD_NAME_GetStudentCommentsList="GetStudentCommentsList";
	public static String METHOD_NAME_InsertStudentComment ="InsertStudentComment";
	public static String METHOD_NAME_DeleteStudentCommentByID  ="DeleteStudentCommentByID";
	public static String METHOD_NAME_GetSiteList ="Get_SiteList";
	public static String METHOD_NAME_GetSiteList_new ="GetSiteList_new";
	public static String METHOD_NAME_GetSiteList_1 ="GetSiteList";
	public static String METHOD_NAME_CT_GetManagerListBySite ="CT_GetManagerListBySite";
	public static String METHOD_NAME_CT_SaveClockTick ="CT_SaveClockTick";

	public static String METHOD_NAME_Get_InstrctListBySite  = "Get_InstrctListBySite";
	//	public static String METHOD_NAME_Insert_TerificTurbo ="Insert_TerificTurbo";
	public static String METHOD_NAME_Insert_TerificTurbo="Insert_TerificTurbo";
	public static String METHOD_NAME_Insert_TurboFlash = "Insert_TurboFlash";
	public static String METHOD_NAME_Mail_GetCommonInboxData = "Mail_GetCommonInboxData";
	public static String METHOD_NAME_Mail_Get_FolderList = "Mail_Get_FolderList";
	public static String METHOD_NAME_Mail_GetCommonSentData = "Mail_GetCommonSentData";
	public static String METHOD_NAME_Mail_GetCommonDeletedData = "Mail_GetCommonDeletedData";
	public static String METHOD_NAME_Mail_GetMessageById = "Mail_GetMessageById";
	public static String METHOD_NAME_Mail_MarkAsReadUnread = "Mail_MarkAsReadUnread";
	public static String METHOD_NAME_Mail_DeleteSelectedMessages = "Mail_DeleteSelectedMessages";
	public static String METHOD_NAME_Mail_CreateMessageFolder = "Mail_CreateMessageFolder";
	public static String METHOD_NAME_Mail_BindFolderTree = "Mail_BindFolderTree";
	public static String METHOD_NAME_Mail_DeleteFolder = "Mail_DeleteFolder";
	public static String METHOD_NAME_Mail_MoveMessageToFolder = "Mail_MoveMessageToFolder";
	public static String METHOD_NAME_NewMail_Get_UserListBySite = "NewMail_Get_UserListBySite";
	public static String METHOD_NAME_NewMail_SendMail = "NewMail_SendMail";
	public static String METHOD_NAME_ViewSchl_GetViewScheduleByDateAndInstrid = "ViewSchl_GetViewScheduleByDateAndInstrid";
	public static String METHOD_NAME_Insert_Attandance = "Insert_Attandance";
	public static String METHOD_NAME_Insert_SwimCompCancellation = "Insert_SwimCompCancellation";
	public static String METHOD_NAME_SendAttForReview = "SendAttForReview";
	public static String METHOD_NAME_Insert_Attandance_ForToday = "Insert_Attandance_ForToday";
	public static String METHOD_NAME_ViewSchl_GetViewScheduleByDeckSupervisorAllInstr = "ViewSchl_GetViewScheduleByDeckSupervisorAllInstr";
	public static String METHOD_NAME_ViewSchl_GetViewScheduleByDeckSupervisorAndInstrWise = "ViewSchl_GetViewScheduleByDeckSupervisorAndInstrWise";
	public static String METHOD_NAME_Get_InstrctListForMgrBySite = "Get_InstrctListForMgrBySite";
	public static String METHOD_NAME_ViewSchl_GetViewScheduleByDeckSupervisorSelectedInstr = "ViewSchl_GetViewScheduleByDeckSupervisorSelectedInstr";
	public static String METHOD_NAME_InsertManabgersShadowTime = "InsertManabgersShadowTime";
	public static String METHOD_NAME_UpdateManabgersShadowEndTime = "UpdateManabgersShadowEndTime";
	public static String METHOD_NAME_InsertManabgersShadowLog = "InsertManabgersShadowLog";
	public static String METHOD_NAME_EndManabgersAllShadows = "EndManabgersAllShadows";
	public static String METHOD_NAME_GetManabgersShadowLog = "GetManabgersShadowLog";
	public static String METHOD_NAME_GetAllEmployeeList = "GetAllEmployeeList";
	public static String METHOD_NAME_GetAllEmployeeListByDevice = "GetAllEmployeeListByDevice";
	public static String METHOD_NAME_EmpComLog_Insert_EmpComLogDetails = "EmpComLog_Insert_EmpComLogDetails";
	public static String METHOD_NAME_EmpComLog_Get_EmpComLogDetails = "EmpComLog_Get_EmpComLogDetails";
	public static String METHOD_NAME_EmpComLog_Delete_EmpComLogDetails = "EmpComLog_Delete_EmpComLogDetails";
	public static String METHOD_NAME_GetISAAlert = "GetISAAlert";
	public static String METHOD_NAME_SaveStudentImage = "SaveStudentImage";
	public static String Insert_Attandance_Method = "Insert_Attandance_LA";
	public static String GetDeckAssistList_Method = "GetDeckAssistList";
	public static String SendNotification = "SendNotification";
	public static String METHOD_NAME_InsertShadowRequest_LA ="InsertShadowRequest_LA";


	//Chat Constants
	public static String FromName = "FromName";
	public static String ChannelName = "ChannelName";
	public static String From = "From";
	public static String FirstMsg = "FirstMsg";
	public static String current_channel="channel";
	public static int lastCount = 0;

}	
