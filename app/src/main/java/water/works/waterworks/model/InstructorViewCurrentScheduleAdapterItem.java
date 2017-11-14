package water.works.waterworks.model;

import android.widget.Button;

import java.util.ArrayList;

public class InstructorViewCurrentScheduleAdapterItem {
	public static Button btn_send_att;
	int wu_avail;
	String ExistSwimComp, IsShowSmCampStatus, att, wu_attendancetaken,
			ISAAlert, SScheduleID, IScheduleID, InstructorID, lessontypeid,
			SAge, SLevel, ScheLevel, StudentID, OrderDetailID, StTimeHour,
			StTimeMin, SLastName, SFirstName, wu_count, wu_Prev, wu_Next,
			ParentFirstName, ParentLastName, BirthDay, LessonName, LvlAdvAvail,
			Comments, wu_comments, SwimComp, SkillsCount, StudentGender,
			NewUser, INSTRUCTORNAME, INSTRUCTORID, Temp_StTimeHour,
			Temp_StTimeMin, MainScheduleDate, wu_w, wu_b, wu_r, SiteID, ClsLvl,
			PaidClasses;

	ArrayList<String> PreReqID, Abbr, PreReqChecked, LevelName, LevelID;

	public InstructorViewCurrentScheduleAdapterItem(int wu_avail, String existSwimComp,
			String isShowSmCampStatus, String att, String wu_attendancetaken,
			String iSAAlert, String sScheduleID, String iScheduleID,
			String instructorID, String lessontypeid, String sAge,
			String sLevel, String scheLevel, String studentID,
			String orderDetailID, String stTimeHour, String stTimeMin,
			String sLastName, String sFirstName, String wu_count,
			String wu_Prev, String wu_Next, String parentFirstName,
			String parentLastName, String birthDay, String lessonName,
			String lvlAdvAvail, String comments, String wu_comments,
			String swimComp, String skillsCount, String studentGender,
			String newUser, ArrayList<String> preReqID, ArrayList<String> abbr,
			ArrayList<String> preReqChecked, ArrayList<String> levelName,
			ArrayList<String> levelID, String INSTRUCTORNAME,
			String INSTRUCTORID, Button btn_send_att, String Temp_StTimeHour,
			String Temp_StTimeMin, String MainScheduleDate, String wu_w,
			String wu_b, String wu_r, String SiteID, String ClsLvl,
			String PaidClasses) {
		super();
		this.wu_avail = wu_avail;
		ExistSwimComp = existSwimComp;
		IsShowSmCampStatus = isShowSmCampStatus;
		this.att = att;
		this.wu_attendancetaken = wu_attendancetaken;
		ISAAlert = iSAAlert;
		SScheduleID = sScheduleID;
		IScheduleID = iScheduleID;
		InstructorID = instructorID;
		this.lessontypeid = lessontypeid;
		SAge = sAge;
		SLevel = sLevel;
		ScheLevel = scheLevel;
		StudentID = studentID;
		OrderDetailID = orderDetailID;
		StTimeHour = stTimeHour;
		StTimeMin = stTimeMin;
		SLastName = sLastName;
		SFirstName = sFirstName;
		this.wu_count = wu_count;
		this.wu_Prev = wu_Prev;
		this.wu_Next = wu_Next;
		ParentFirstName = parentFirstName;
		ParentLastName = parentLastName;
		BirthDay = birthDay;
		LessonName = lessonName;
		LvlAdvAvail = lvlAdvAvail;
		Comments = comments;
		this.wu_comments = wu_comments;
		SwimComp = swimComp;
		SkillsCount = skillsCount;
		StudentGender = studentGender;
		NewUser = newUser;
		PreReqID = preReqID;
		Abbr = abbr;
		PreReqChecked = preReqChecked;
		LevelName = levelName;
		LevelID = levelID;
		this.INSTRUCTORNAME = INSTRUCTORNAME;
		this.INSTRUCTORID = INSTRUCTORID;
		this.Temp_StTimeHour = Temp_StTimeHour;
		this.Temp_StTimeMin = Temp_StTimeMin;
		this.MainScheduleDate = MainScheduleDate;
		this.wu_w = wu_w;
		this.wu_r = wu_r;
		this.wu_b = wu_b;
		this.SiteID = SiteID;
		this.ClsLvl =ClsLvl;
		this.PaidClasses = PaidClasses;
	}

	public int getWu_avail() {
		return wu_avail;
	}

	public void setWu_avail(int wu_avail) {
		this.wu_avail = wu_avail;
	}

	public String getExistSwimComp() {
		return ExistSwimComp;
	}

	public void setExistSwimComp(String existSwimComp) {
		ExistSwimComp = existSwimComp;
	}

	public String getIsShowSmCampStatus() {
		return IsShowSmCampStatus;
	}

	public void setIsShowSmCampStatus(String isShowSmCampStatus) {
		IsShowSmCampStatus = isShowSmCampStatus;
	}

	public String getAtt() {
		return att;
	}

	public void setAtt(String att) {
		this.att = att;
	}

	public String getWu_attendancetaken() {
		return wu_attendancetaken;
	}

	public void setWu_attendancetaken(String wu_attendancetaken) {
		this.wu_attendancetaken = wu_attendancetaken;
	}

	public String getISAAlert() {
		return ISAAlert;
	}

	public void setISAAlert(String iSAAlert) {
		ISAAlert = iSAAlert;
	}

	public String getSScheduleID() {
		return SScheduleID;
	}

	public void setSScheduleID(String sScheduleID) {
		SScheduleID = sScheduleID;
	}

	public String getIScheduleID() {
		return IScheduleID;
	}

	public void setIScheduleID(String iScheduleID) {
		IScheduleID = iScheduleID;
	}

	public String getInstructorID() {
		return InstructorID;
	}

	public void setInstructorID(String instructorID) {
		InstructorID = instructorID;
	}

	public String getLessontypeid() {
		return lessontypeid;
	}

	public void setLessontypeid(String lessontypeid) {
		this.lessontypeid = lessontypeid;
	}

	public String getSAge() {
		return SAge;
	}

	public void setSAge(String sAge) {
		SAge = sAge;
	}

	public String getSLevel() {
		return SLevel;
	}

	public void setSLevel(String sLevel) {
		SLevel = sLevel;
	}

	public String getScheLevel() {
		return ScheLevel;
	}

	public void setScheLevel(String scheLevel) {
		ScheLevel = scheLevel;
	}

	public String getStudentID() {
		return StudentID;
	}

	public void setStudentID(String studentID) {
		StudentID = studentID;
	}

	public String getOrderDetailID() {
		return OrderDetailID;
	}

	public void setOrderDetailID(String orderDetailID) {
		OrderDetailID = orderDetailID;
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

	public String getWu_count() {
		return wu_count;
	}

	public void setWu_count(String wu_count) {
		this.wu_count = wu_count;
	}

	public String getWu_Prev() {
		return wu_Prev;
	}

	public void setWu_Prev(String wu_Prev) {
		this.wu_Prev = wu_Prev;
	}

	public String getWu_Next() {
		return wu_Next;
	}

	public void setWu_Next(String wu_Next) {
		this.wu_Next = wu_Next;
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

	public String getLessonName() {
		return LessonName;
	}

	public void setLessonName(String lessonName) {
		LessonName = lessonName;
	}

	public String getLvlAdvAvail() {
		return LvlAdvAvail;
	}

	public void setLvlAdvAvail(String lvlAdvAvail) {
		LvlAdvAvail = lvlAdvAvail;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
	}

	public String getWu_comments() {
		return wu_comments;
	}

	public void setWu_comments(String wu_comments) {
		this.wu_comments = wu_comments;
	}

	public String getSwimComp() {
		return SwimComp;
	}

	public void setSwimComp(String swimComp) {
		SwimComp = swimComp;
	}

	public String getSkillsCount() {
		return SkillsCount;
	}

	public void setSkillsCount(String skillsCount) {
		SkillsCount = skillsCount;
	}

	public String getStudentGender() {
		return StudentGender;
	}

	public void setStudentGender(String studentGender) {
		StudentGender = studentGender;
	}

	public String getNewUser() {
		return NewUser;
	}

	public void setNewUser(String newUser) {
		NewUser = newUser;
	}

	public ArrayList<String> getPreReqID() {
		return PreReqID;
	}

	public void setPreReqID(ArrayList<String> preReqID) {
		PreReqID = preReqID;
	}

	public ArrayList<String> getAbbr() {
		return Abbr;
	}

	public void setAbbr(ArrayList<String> abbr) {
		Abbr = abbr;
	}

	public ArrayList<String> getPreReqChecked() {
		return PreReqChecked;
	}

	public void setPreReqChecked(ArrayList<String> preReqChecked) {
		PreReqChecked = preReqChecked;
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

	public String getINSTRUCTORNAME() {
		return INSTRUCTORNAME;
	}

	public void setINSTRUCTORNAME(String iNSTRUCTORNAME) {
		INSTRUCTORNAME = iNSTRUCTORNAME;
	}

	public String getINSTRUCTORID() {
		return INSTRUCTORID;
	}

	public void setINSTRUCTORID(String iNSTRUCTORID) {
		INSTRUCTORID = iNSTRUCTORID;
	}

	public String getTemp_StTimeHour() {
		return Temp_StTimeHour;
	}

	public void setTemp_StTimeHour(String temp_StTimeHour) {
		Temp_StTimeHour = temp_StTimeHour;
	}

	public String getTemp_StTimeMin() {
		return Temp_StTimeMin;
	}

	public void setTemp_StTimeMin(String temp_StTimeMin) {
		Temp_StTimeMin = temp_StTimeMin;
	}

	public String getMainScheduleDate() {
		return MainScheduleDate;
	}

	public void setMainScheduleDate(String mainScheduleDate) {
		MainScheduleDate = mainScheduleDate;
	}

	public String getWu_w() {
		return wu_w;
	}

	public void setWu_w(String wu_w) {
		this.wu_w = wu_w;
	}

	public String getWu_b() {
		return wu_b;
	}

	public void setWu_b(String wu_b) {
		this.wu_b = wu_b;
	}

	public String getWu_r() {
		return wu_r;
	}

	public void setWu_r(String wu_r) {
		this.wu_r = wu_r;
	}

	public String getSiteID() {
		return SiteID;
	}

	public void setSiteID(String siteID) {
		SiteID = siteID;
	}

	public String getClsLvl() {
		return ClsLvl;
	}

	public void setClsLvl(String clsLvl) {
		ClsLvl = clsLvl;
	}

	public String getPaidClasses() {
		return PaidClasses;
	}

	public void setPaidClasses(String paidClasses) {
		PaidClasses = paidClasses;
	}

}
