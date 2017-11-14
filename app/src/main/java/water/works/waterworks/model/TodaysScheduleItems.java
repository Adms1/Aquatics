package water.works.waterworks.model;

import android.content.Context;
import android.widget.Button;

import java.util.ArrayList;

public class TodaysScheduleItems {
	String StTimeHour, StTimeMin,FormateStTimeHour,FormatStTimeMin, siteID,sLevel,
	scheLevel,lessonName, lessontypeid,iScheduleID,sAge,parentFirstName,
	parentLastName,comments,sLastName,sFirstName,studentID,sScheduleID,orderDetailID,
	paidClasses,ISAAlert,ClsLvl,LvlAdvAvail, MainScheduleDate,StudentGender,wu_Comments;
	int Att;
	Boolean NewUser;
	Context context;
	int wu_attendancetaken;
	ArrayList<String> LevelName,LevelID;
	public static Button submit;
	public TodaysScheduleItems(String stTimeHour, String stTimeMin,
			String formateStTimeHour, String formatStTimeMin, String siteID,
			String sLevel, String scheLevel, 
			String lessonName, String lessontypeid, String iScheduleID,
			String sAge, String parentFirstName, String parentLastName,
			String comments, String sLastName,
			String sFirstName, String studentID, String sScheduleID,
			String orderDetailID, String paidClasses,
			String iSAAlert, String clsLvl, String lvlAdvAvail,
			String mainScheduleDate, String studentGender, Boolean newUser,
			Context context, int wu_attendancetaken,
			ArrayList<String> levelName, ArrayList<String> levelID,int att,Button submit,String wu_Comments) {
		super();
		StTimeHour = stTimeHour;
		StTimeMin = stTimeMin;
		FormateStTimeHour = formateStTimeHour;
		FormatStTimeMin = formatStTimeMin;
		this.siteID = siteID;
		this.sLevel = sLevel;
		this.scheLevel = scheLevel;
		this.lessonName = lessonName;
		this.lessontypeid = lessontypeid;
		this.iScheduleID = iScheduleID;
		this.sAge = sAge;
		this.parentFirstName = parentFirstName;
		this.parentLastName = parentLastName;
		this.comments = comments;
		this.sLastName = sLastName;
		this.sFirstName = sFirstName;
		this.studentID = studentID;
		this.sScheduleID = sScheduleID;
		this.orderDetailID = orderDetailID;
		this.paidClasses = paidClasses;
		ISAAlert = iSAAlert;
		ClsLvl = clsLvl;
		LvlAdvAvail = lvlAdvAvail;
		MainScheduleDate = mainScheduleDate;
		StudentGender = studentGender;
		NewUser = newUser;
		this.context = context;
		this.wu_attendancetaken = wu_attendancetaken;
		LevelName = levelName;
		LevelID = levelID;
		Att = att ;
		this.submit = submit;
		this.wu_Comments = wu_Comments;
	}
	public String getStTimeHour() {
		return StTimeHour;
	}
	public void setStTimeHour(String stTimeHour) {
		StTimeHour = stTimeHour;
	}
	public String getStTimeMin() {
		return StTimeMin;
	}
	public void setStTimeMin(String stTimeMin) {
		StTimeMin = stTimeMin;
	}
	public String getFormateStTimeHour() {
		return FormateStTimeHour;
	}
	public void setFormateStTimeHour(String formateStTimeHour) {
		FormateStTimeHour = formateStTimeHour;
	}
	public String getFormatStTimeMin() {
		return FormatStTimeMin;
	}
	public void setFormatStTimeMin(String formatStTimeMin) {
		FormatStTimeMin = formatStTimeMin;
	}
	public String getSiteID() {
		return siteID;
	}
	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}
	public String getsLevel() {
		return sLevel;
	}
	public void setsLevel(String sLevel) {
		this.sLevel = sLevel;
	}
	public String getScheLevel() {
		return scheLevel;
	}
	public void setScheLevel(String scheLevel) {
		this.scheLevel = scheLevel;
	}
	
	public String getLessonName() {
		return lessonName;
	}
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
	public String getLessontypeid() {
		return lessontypeid;
	}
	public void setLessontypeid(String lessontypeid) {
		this.lessontypeid = lessontypeid;
	}
	public String getiScheduleID() {
		return iScheduleID;
	}
	public void setiScheduleID(String iScheduleID) {
		this.iScheduleID = iScheduleID;
	}
	public String getsAge() {
		return sAge;
	}
	public void setsAge(String sAge) {
		this.sAge = sAge;
	}
	public String getParentFirstName() {
		return parentFirstName;
	}
	public void setParentFirstName(String parentFirstName) {
		this.parentFirstName = parentFirstName;
	}
	public String getParentLastName() {
		return parentLastName;
	}
	public void setParentLastName(String parentLastName) {
		this.parentLastName = parentLastName;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getsLastName() {
		return sLastName;
	}
	public void setsLastName(String sLastName) {
		this.sLastName = sLastName;
	}
	public String getsFirstName() {
		return sFirstName;
	}
	public void setsFirstName(String sFirstName) {
		this.sFirstName = sFirstName;
	}
	public String getStudentID() {
		return studentID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public String getsScheduleID() {
		return sScheduleID;
	}
	public void setsScheduleID(String sScheduleID) {
		this.sScheduleID = sScheduleID;
	}
	public String getOrderDetailID() {
		return orderDetailID;
	}
	public void setOrderDetailID(String orderDetailID) {
		this.orderDetailID = orderDetailID;
	}
	public String getPaidClasses() {
		return paidClasses;
	}
	public void setPaidClasses(String paidClasses) {
		this.paidClasses = paidClasses;
	}
	public String getISAAlert() {
		return ISAAlert;
	}
	public void setISAAlert(String iSAAlert) {
		ISAAlert = iSAAlert;
	}
	public String getClsLvl() {
		return ClsLvl;
	}
	public void setClsLvl(String clsLvl) {
		ClsLvl = clsLvl;
	}
	public String getLvlAdvAvail() {
		return LvlAdvAvail;
	}
	public void setLvlAdvAvail(String lvlAdvAvail) {
		LvlAdvAvail = lvlAdvAvail;
	}
	public String getMainScheduleDate() {
		return MainScheduleDate;
	}
	public void setMainScheduleDate(String mainScheduleDate) {
		MainScheduleDate = mainScheduleDate;
	}
	public String getStudentGender() {
		return StudentGender;
	}
	public void setStudentGender(String studentGender) {
		StudentGender = studentGender;
	}
	public Boolean getNewUser() {
		return NewUser;
	}
	public void setNewUser(Boolean newUser) {
		NewUser = newUser;
	}
	public int getWu_attendancetaken() {
		return wu_attendancetaken;
	}
	public void setWu_attendancetaken(int wu_attendancetaken) {
		this.wu_attendancetaken = wu_attendancetaken;
	}
	public ArrayList<String> getLevelName() {
		return LevelName;
	}
	public void setLevelName(ArrayList<String> levelName) {
		LevelName = levelName;
	}
	public ArrayList<String> getLevelID() {
		return LevelID;
	}
	public void setLevelID(ArrayList<String> levelID) {
		LevelID = levelID;
	}
	public int getAtt() {
		return Att;
	}
	public void setAtt(int att) {
		Att = att;
	}
	public String getWu_Comments() {
		return wu_Comments;
	}
	public void setWu_Comments(String wu_Comments) {
		this.wu_Comments = wu_Comments;
	}
	
	
}
