package water.works.waterworks.model;

import android.widget.Button;

import java.util.ArrayList;

public class InstructorTodaysScheduleAdapterItem {
	public static Button btn_send_att;
	String  MainScheduleDate, ISAAlert, Cls_Lvl,
			Lvl_Adv_Avail, SiteID, SLevel, wu_W, ScheLevel, SwimComp,
			LessonName, lessontypeid, IScheduleID, SAge, ParentFirstName,
			ParentLastName, BirthDay, Comments, wu_r, SLastName, SFirstName,
			StudentID, ShowWBR, wu_b, SScheduleID, OrderDetailID, PaidClasses,
			SkillsCount, FormateStTimeHour, FormatStTimeMin, StTimeHour,
			StTimeMin, StudentGender,wu_Comments,InstructorId;
	Boolean NewUser;
	int wu_attendancetaken,att;
	boolean sw_state;
	ArrayList<String> PreReqID,PreReqChecked,Abbr,LevelName, LevelID;
	public InstructorTodaysScheduleAdapterItem(ArrayList<String> levelName, ArrayList<String> levelID,
			String mainScheduleDate, String iSAAlert, String cls_Lvl,
			String lvl_Adv_Avail, String siteID, String sLevel, String wu_W,
			String scheLevel, String swimComp, String lessonName,
			String lessontypeid, String iScheduleID, String sAge,
			String parentFirstName, String parentLastName, String birthDay,
			String comments, String wu_r, String sLastName, String sFirstName,
			String studentID, String showWBR, String wu_b, String sScheduleID,
			String orderDetailID, String paidClasses, String skillsCount,
			String formateStTimeHour, String formatStTimeMin,
			String stTimeHour, String stTimeMin, String studentGender,
			String wu_Comments, Boolean newUser, int wu_attendancetaken,
			int att, ArrayList<String> preReqID,
			ArrayList<String> preReqChecked, ArrayList<String> abbr,String InstructorId,Button btn_send_att){
		super();
		LevelName = levelName;
		LevelID = levelID;
		MainScheduleDate = mainScheduleDate;
		ISAAlert = iSAAlert;
		Cls_Lvl = cls_Lvl;
		Lvl_Adv_Avail = lvl_Adv_Avail;
		SiteID = siteID;
		SLevel = sLevel;
		this.wu_W = wu_W;
		ScheLevel = scheLevel;
		SwimComp = swimComp;
		LessonName = lessonName;
		this.lessontypeid = lessontypeid;
		IScheduleID = iScheduleID;
		SAge = sAge;
		ParentFirstName = parentFirstName;
		ParentLastName = parentLastName;
		BirthDay = birthDay;
		Comments = comments;
		this.wu_r = wu_r;
		SLastName = sLastName;
		SFirstName = sFirstName;
		StudentID = studentID;
		ShowWBR = showWBR;
		this.wu_b = wu_b;
		SScheduleID = sScheduleID;
		OrderDetailID = orderDetailID;
		PaidClasses = paidClasses;
		SkillsCount = skillsCount;
		FormateStTimeHour = formateStTimeHour;
		FormatStTimeMin = formatStTimeMin;
		StTimeHour = stTimeHour;
		StTimeMin = stTimeMin;
		StudentGender = studentGender;
		this.wu_Comments = wu_Comments;
		NewUser = newUser;
		this.wu_attendancetaken = wu_attendancetaken;
		this.att = att;
		PreReqID = preReqID;
		PreReqChecked = preReqChecked;
		Abbr = abbr;
		this.InstructorId = InstructorId;
		this.btn_send_att = btn_send_att;
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
	public String getMainScheduleDate() {
		return MainScheduleDate;
	}
	public void setMainScheduleDate(String mainScheduleDate) {
		MainScheduleDate = mainScheduleDate;
	}
	public String getISAAlert() {
		return ISAAlert;
	}
	public void setISAAlert(String iSAAlert) {
		ISAAlert = iSAAlert;
	}
	public String getCls_Lvl() {
		return Cls_Lvl;
	}
	public void setCls_Lvl(String cls_Lvl) {
		Cls_Lvl = cls_Lvl;
	}
	public String getLvl_Adv_Avail() {
		return Lvl_Adv_Avail;
	}
	public void setLvl_Adv_Avail(String lvl_Adv_Avail) {
		Lvl_Adv_Avail = lvl_Adv_Avail;
	}
	public String getSiteID() {
		return SiteID;
	}
	public void setSiteID(String siteID) {
		SiteID = siteID;
	}
	public String getSLevel() {
		return SLevel;
	}
	public void setSLevel(String sLevel) {
		SLevel = sLevel;
	}
	public String getWu_W() {
		return wu_W;
	}
	public void setWu_W(String wu_W) {
		this.wu_W = wu_W;
	}
	public String getScheLevel() {
		return ScheLevel;
	}
	public void setScheLevel(String scheLevel) {
		ScheLevel = scheLevel;
	}
	public String getSwimComp() {
		return SwimComp;
	}
	public void setSwimComp(String swimComp) {
		SwimComp = swimComp;
	}
	public String getLessonName() {
		return LessonName;
	}
	public void setLessonName(String lessonName) {
		LessonName = lessonName;
	}
	public String getLessontypeid() {
		return lessontypeid;
	}
	public void setLessontypeid(String lessontypeid) {
		this.lessontypeid = lessontypeid;
	}
	public String getIScheduleID() {
		return IScheduleID;
	}
	public void setIScheduleID(String iScheduleID) {
		IScheduleID = iScheduleID;
	}
	public String getSAge() {
		return SAge;
	}
	public void setSAge(String sAge) {
		SAge = sAge;
	}
	public String getParentFirstName() {
		return ParentFirstName;
	}
	public void setParentFirstName(String parentFirstName) {
		ParentFirstName = parentFirstName;
	}
	public String getParentLastName() {
		return ParentLastName;
	}
	public void setParentLastName(String parentLastName) {
		ParentLastName = parentLastName;
	}
	public String getBirthDay() {
		return BirthDay;
	}
	public void setBirthDay(String birthDay) {
		BirthDay = birthDay;
	}
	public String getComments() {
		return Comments;
	}
	public void setComments(String comments) {
		Comments = comments;
	}
	public String getWu_r() {
		return wu_r;
	}
	public void setWu_r(String wu_r) {
		this.wu_r = wu_r;
	}
	public String getSLastName() {
		return SLastName;
	}
	public void setSLastName(String sLastName) {
		SLastName = sLastName;
	}
	public String getSFirstName() {
		return SFirstName;
	}
	public void setSFirstName(String sFirstName) {
		SFirstName = sFirstName;
	}
	public String getStudentID() {
		return StudentID;
	}
	public void setStudentID(String studentID) {
		StudentID = studentID;
	}
	public String getShowWBR() {
		return ShowWBR;
	}
	public void setShowWBR(String showWBR) {
		ShowWBR = showWBR;
	}
	public String getWu_b() {
		return wu_b;
	}
	public void setWu_b(String wu_b) {
		this.wu_b = wu_b;
	}
	public String getSScheduleID() {
		return SScheduleID;
	}
	public void setSScheduleID(String sScheduleID) {
		SScheduleID = sScheduleID;
	}
	public String getOrderDetailID() {
		return OrderDetailID;
	}
	public void setOrderDetailID(String orderDetailID) {
		OrderDetailID = orderDetailID;
	}
	public String getPaidClasses() {
		return PaidClasses;
	}
	public void setPaidClasses(String paidClasses) {
		PaidClasses = paidClasses;
	}
	public String getSkillsCount() {
		return SkillsCount;
	}
	public void setSkillsCount(String skillsCount) {
		SkillsCount = skillsCount;
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
	public String getStudentGender() {
		return StudentGender;
	}
	public void setStudentGender(String studentGender) {
		StudentGender = studentGender;
	}
	public String getWu_Comments() {
		return wu_Comments;
	}
	public void setWu_Comments(String wu_Comments) {
		this.wu_Comments = wu_Comments;
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
	public int getAtt() {
		return att;
	}
	public void setAtt(int att) {
		this.att = att;
	}
	public ArrayList<String> getPreReqID() {
		return PreReqID;
	}
	public void setPreReqID(ArrayList<String> preReqID) {
		PreReqID = preReqID;
	}
	public ArrayList<String> getPreReqChecked() {
		return PreReqChecked;
	}
	public void setPreReqChecked(ArrayList<String> preReqChecked) {
		PreReqChecked = preReqChecked;
	}
	public ArrayList<String> getAbbr() {
		return Abbr;
	}
	public void setAbbr(ArrayList<String> abbr) {
		Abbr = abbr;
	}
	public String getInstructorId() {
		return InstructorId;
	}
	public void setInstructorId(String instructorId) {
		InstructorId = instructorId;
	}
	public boolean isSw_state() {
		return sw_state;
	}
	public void setSw_state(boolean sw_state) {
		this.sw_state = sw_state;
	}
	
	
}
