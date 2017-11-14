package water.works.waterworks.model;

import java.util.ArrayList;

public class ViewScheduleRow {

	int wu_avail;
	String wu_InstructorID,wu_InstructorName,SScheduleID,IScheduleID,SiteID,MainScheduleDate,
	ScheduleDay,FormateStTimeHour,FormatStTimeMin,StTimeHour,StTimeMin,FROMWHERE,starttime,endtime;
	ArrayList<String> ISAAlert,SLastName,SFirstName,
	ParentFirstName,ParentLastName,PaidClasses,ClsLvl,wu_comments,StudentGender,
	MemStatus,SAge,SLevel,ScheLevel,StudentID,att,wu_attendancetaken,LEVELNAME,LEVELID,InstructorID,InstructorName,
	lessontypeid;
	public ViewScheduleRow(String wu_InstructorID,String wu_InstructorName,ArrayList<String> iSAAlert, 
			/*String sScheduleID,*/ String iScheduleID,
			String siteID, String MainScheduleDate, String scheduleDay,
			String formateStTimeHour, String formatStTimeMin,
			String StTimeHour, String StTimeMin,
			ArrayList<String> sLastName, ArrayList<String> sFirstName,
			ArrayList<String> parentFirstName,
			ArrayList<String> parentLastName, ArrayList<String> paidClasses,
			ArrayList<String> clsLvl, ArrayList<String> wu_comments,
			ArrayList<String> studentGender, ArrayList<String> memStatus,
			ArrayList<String> sAge, ArrayList<String> sLevel,
			ArrayList<String> scheLevel, ArrayList<String> studentID,
			ArrayList<String> att,ArrayList<String> wu_attendancetaken,
			ArrayList<String> InstructorID,ArrayList<String> InstructorName,
			ArrayList<String> lessontypeid,int wu_avail,
			ArrayList<String> LEVELNAME,ArrayList<String> LEVELID,String FROM,
			String starttime, String endtime) {
		super();
		this.wu_InstructorID = wu_InstructorID;
		this.wu_InstructorName = wu_InstructorName;
		ISAAlert = iSAAlert;
		/*SScheduleID = sScheduleID;*/
		IScheduleID = iScheduleID;
		SiteID = siteID;
		this.MainScheduleDate = MainScheduleDate;
		ScheduleDay = scheduleDay;
		FormateStTimeHour = formateStTimeHour;
		FormatStTimeMin = formatStTimeMin;
		this.StTimeHour =StTimeHour;
		this.StTimeMin =StTimeMin;
		SLastName = sLastName;
		SFirstName = sFirstName;
		ParentFirstName = parentFirstName;
		ParentLastName = parentLastName;
		PaidClasses = paidClasses;
		ClsLvl = clsLvl;
		this.wu_comments = wu_comments;
		StudentGender = studentGender;
		MemStatus = memStatus;
		SAge = sAge;
		SLevel = sLevel;
		ScheLevel = scheLevel;
		StudentID = studentID;
		this.att = att;
		this.wu_attendancetaken = wu_attendancetaken;
		this.InstructorID = InstructorID;
		this.InstructorName = InstructorName;
		this.lessontypeid = lessontypeid;
		this.wu_avail = wu_avail;
		this.LEVELNAME = LEVELNAME;
		this.LEVELID = LEVELID;
		FROMWHERE = FROM;
		this.starttime = starttime;
		this.endtime = endtime;
	}
	
	public String getWu_InstructorID() {
		return wu_InstructorID;
	}

	public void setWu_InstructorID(String wu_InstructorID) {
		this.wu_InstructorID = wu_InstructorID;
	}

	public String getWu_InstructorName() {
		return wu_InstructorName;
	}

	public void setWu_InstructorName(String wu_InstructorName) {
		this.wu_InstructorName = wu_InstructorName;
	}

	public void setISAAlert(ArrayList<String> iSAAlert) {
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
	public String getSiteID() {
		return SiteID;
	}
	public void setSiteID(String siteID) {
		SiteID = siteID;
	}
	public String getMainScheduleDate() {
		return MainScheduleDate;
	}
	public void setMainScheduleDate(String MainScheduleDate) {
		this.MainScheduleDate = MainScheduleDate;
	}
	
	public ArrayList<String> getInstructorID() {
		return InstructorID;
	}
	public void setInstructorID(ArrayList<String> instructorID) {
		InstructorID = instructorID;
	}
	public ArrayList<String> getInstructorName() {
		return InstructorName;
	}
	public void setInstructorName(ArrayList<String> instructorName) {
		InstructorName = instructorName;
	}
	public ArrayList<String> getLessontypeid() {
		return lessontypeid;
	}
	public void setLessontypeid(ArrayList<String> lessontypeid) {
		this.lessontypeid = lessontypeid;
	}
	public String getScheduleDay() {
		return ScheduleDay;
	}
	public void setScheduleDay(String scheduleDay) {
		ScheduleDay = scheduleDay;
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
	public ArrayList<String> getSLastName() {
		return SLastName;
	}
	public void setSLastName(ArrayList<String> sLastName) {
		SLastName = sLastName;
	}
	public ArrayList<String> getSFirstName() {
		return SFirstName;
	}
	public void setSFirstName(ArrayList<String> sFirstName) {
		SFirstName = sFirstName;
	}
	public ArrayList<String> getParentFirstName() {
		return ParentFirstName;
	}
	public void setParentFirstName(ArrayList<String> parentFirstName) {
		ParentFirstName = parentFirstName;
	}
	public ArrayList<String> getParentLastName() {
		return ParentLastName;
	}
	public void setParentLastName(ArrayList<String> parentLastName) {
		ParentLastName = parentLastName;
	}
	public ArrayList<String> getPaidClasses() {
		return PaidClasses;
	}
	public void setPaidClasses(ArrayList<String> paidClasses) {
		PaidClasses = paidClasses;
	}
	public ArrayList<String> getClsLvl() {
		return ClsLvl;
	}
	public void setClsLvl(ArrayList<String> clsLvl) {
		ClsLvl = clsLvl;
	}
	public ArrayList<String> getWu_comments() {
		return wu_comments;
	}
	public void setWu_comments(ArrayList<String> wu_comments) {
		this.wu_comments = wu_comments;
	}
	public ArrayList<String> getStudentGender() {
		return StudentGender;
	}
	public void setStudentGender(ArrayList<String> studentGender) {
		StudentGender = studentGender;
	}
	public ArrayList<String> getMemStatus() {
		return MemStatus;
	}
	public void setMemStatus(ArrayList<String> memStatus) {
		MemStatus = memStatus;
	}
	public ArrayList<String> getSAge() {
		return SAge;
	}
	public void setSAge(ArrayList<String> sAge) {
		SAge = sAge;
	}
	public ArrayList<String> getSLevel() {
		return SLevel;
	}
	public void setSLevel(ArrayList<String> sLevel) {
		SLevel = sLevel;
	}
	public ArrayList<String> getScheLevel() {
		return ScheLevel;
	}
	public void setScheLevel(ArrayList<String> scheLevel) {
		ScheLevel = scheLevel;
	}
	public ArrayList<String> getStudentID() {
		return StudentID;
	}
	public void setStudentID(ArrayList<String> studentID) {
		StudentID = studentID;
	}
	public ArrayList<String> getAtt() {
		return att;
	}
	public void setAtt(ArrayList<String> att) {
		this.att = att;
	}
	public ArrayList<String> getLEVELNAME() {
		return LEVELNAME;
	}
	public void setLEVELNAME(ArrayList<String> lEVELNAME) {
		LEVELNAME = lEVELNAME;
	}
	public ArrayList<String> getLEVELID() {
		return LEVELID;
	}
	public void setLEVELID(ArrayList<String> lEVELID) {
		LEVELID = lEVELID;
	}
	public ArrayList<String> getISAAlert() {
		return ISAAlert;
	}
	public int getWu_avail() {
		return wu_avail;
	}
	public void setWu_avail(int wu_avail) {
		this.wu_avail = wu_avail;
	}
	public ArrayList<String> getWu_attendancetaken() {
		return wu_attendancetaken;
	}
	public void setWu_attendancetaken(ArrayList<String> wu_attendancetaken) {
		this.wu_attendancetaken = wu_attendancetaken;
	}

	public String getFROMWHERE() {
		return FROMWHERE;
	}

	public void setFROMWHERE(String fROMWHERE) {
		FROMWHERE = fROMWHERE;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
		
}
