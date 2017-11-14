package water.works.waterworks.model;

import android.content.Context;
import android.widget.Button;

import java.util.ArrayList;

public class AttendanceItems {
	String hasSwim,hasISA,ClsLvl,LvlAdvAvail,SiteID,SLevel,SchLevel,Wu_w,Wu_b,Wu_r,SwimComp,Birthday,
		LessonName,lessontypeid,ISchID,Age,PFName,PLName,Comments,Wu_Comments,SFName,SLName,StudentID,
		ShwWBR,SSchID,OrderDetailID,PaidCls,stTimehour,stTimemin,Maindate,
		Skillcount,StudentGender;
	int wu_attendancetaken,ATT;
	Boolean NewStudent;
	ArrayList<String> prereqid,prereqchk,abbr,LevelName,LevelID;
	String yes_no_date;
	Context context;
	public static Button btn_footer_send_attendance;
	public AttendanceItems(ArrayList<String> levelName, ArrayList<String> levelID,String hasSwim, String hasISA,
			String clsLvl, String lvlAdvAvail, String siteID, String sLevel,
			String schLevel, String wu_w, String wu_b, String wu_r,
			String swimComp, String birthday, String lessonName,
			String lessontypeid, String iSchID, String age, String pFName,
			String pLName, String commens, String wu_Comments, String sFName,
			String sLName, String studentID, String shwWBR, String sSchID,
			String orderDetailID, String paidCls, String stTimehour,
			String stTimemin, String maindate, String skillcount,
			ArrayList<String> prereqid, ArrayList<String> prereqchk,
			ArrayList<String> abbr,String StudentGender,Boolean NewStudent,String yes_no_date,Button btn_footer_send_attendance,
			int wu_attendancetaken,int ATT,Context context) {
		super();
		LevelID = levelID;
		LevelName = levelName;
		this.hasSwim = hasSwim;
		this.hasISA = hasISA;
		ClsLvl = clsLvl;
		LvlAdvAvail = lvlAdvAvail;
		SiteID = siteID;
		SLevel = sLevel;
		SchLevel = schLevel;
		Wu_w = wu_w;
		Wu_b = wu_b;
		Wu_r = wu_r;
		SwimComp = swimComp;
		Birthday = birthday;
		LessonName = lessonName;
		this.lessontypeid = lessontypeid;
		ISchID = iSchID;
		Age = age;
		PFName = pFName;
		PLName = pLName;
		Comments = commens;
		Wu_Comments = wu_Comments;
		SFName = sFName;
		SLName = sLName;
		StudentID = studentID;
		ShwWBR = shwWBR;
		SSchID = sSchID;
		OrderDetailID = orderDetailID;
		PaidCls = paidCls;
		this.stTimehour = stTimehour;
		this.stTimemin = stTimemin;
		Maindate = maindate;
		Skillcount = skillcount;
		this.prereqid = prereqid;
		this.prereqchk = prereqchk;
		this.abbr = abbr;
		this.yes_no_date = yes_no_date;
		this.btn_footer_send_attendance = btn_footer_send_attendance;
		this.StudentGender = StudentGender;
		this.NewStudent = NewStudent;
		this.wu_attendancetaken = wu_attendancetaken;
		this.ATT = ATT;
		this.context = context;
	}
	
	public String getYes_no_date() {
		return yes_no_date;
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

	public void setYes_no_date(String yes_no_date) {
		this.yes_no_date = yes_no_date;
	}
	
	
	
	public String getHasSwim() {
		return hasSwim;
	}

	public void setHasSwim(String hasSwim) {
		this.hasSwim = hasSwim;
	}

	public String getHasISA() {
		return hasISA;
	}
	public void setHasISA(String hasISA) {
		this.hasISA = hasISA;
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
	public String getSchLevel() {
		return SchLevel;
	}
	public void setSchLevel(String schLevel) {
		SchLevel = schLevel;
	}
	public String getWu_w() {
		return Wu_w;
	}
	public void setWu_w(String wu_w) {
		Wu_w = wu_w;
	}
	public String getWu_b() {
		return Wu_b;
	}
	public void setWu_b(String wu_b) {
		Wu_b = wu_b;
	}
	public String getWu_r() {
		return Wu_r;
	}
	public void setWu_r(String wu_r) {
		Wu_r = wu_r;
	}
	public String getSwimComp() {
		return SwimComp;
	}
	public void setSwimComp(String swimComp) {
		SwimComp = swimComp;
	}
	public String getBirthday() {
		return Birthday;
	}
	public void setBirthday(String birthday) {
		Birthday = birthday;
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
	public String getISchID() {
		return ISchID;
	}
	public void setISchID(String iSchID) {
		ISchID = iSchID;
	}
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age;
	}
	public String getPFName() {
		return PFName;
	}
	public void setPFName(String pFName) {
		PFName = pFName;
	}
	public String getPLName() {
		return PLName;
	}
	public void setPLName(String pLName) {
		PLName = pLName;
	}
	public String getComments() {
		return Comments;
	}
	public void setComments(String comments) {
		Comments = comments;
	}
	public String getWu_Comments() {
		return Wu_Comments;
	}
	public void setWu_Comments(String wu_Comments) {
		Wu_Comments = wu_Comments;
	}
	public String getSFName() {
		return SFName;
	}
	public void setSFName(String sFName) {
		SFName = sFName;
	}
	public String getSLName() {
		return SLName;
	}
	public void setSLName(String sLName) {
		SLName = sLName;
	}
	public String getStudentID() {
		return StudentID;
	}
	public void setStudentID(String studentID) {
		StudentID = studentID;
	}
	public String getShwWBR() {
		return ShwWBR;
	}
	public void setShwWBR(String shwWBR) {
		ShwWBR = shwWBR;
	}
	public String getSSchID() {
		return SSchID;
	}
	public void setSSchID(String sSchID) {
		SSchID = sSchID;
	}
	public String getOrderDetailID() {
		return OrderDetailID;
	}
	public void setOrderDetailID(String orderDetailID) {
		OrderDetailID = orderDetailID;
	}
	public String getPaidCls() {
		return PaidCls;
	}
	public void setPaidCls(String paidCls) {
		PaidCls = paidCls;
	}
	public String getStTimehour() {
		return stTimehour;
	}
	public void setStTimehour(String stTimehour) {
		this.stTimehour = stTimehour;
	}
	public String getStTimemin() {
		return stTimemin;
	}
	public void setStTimemin(String stTimemin) {
		this.stTimemin = stTimemin;
	}
	public String getMaindate() {
		return Maindate;
	}
	public void setMaindate(String maindate) {
		Maindate = maindate;
	}
	public String getSkillcount() {
		return Skillcount;
	}
	public void setSkillcount(String skillcount) {
		Skillcount = skillcount;
	}
	public ArrayList<String> getPrereqid() {
		return prereqid;
	}
	public void setPrereqid(ArrayList<String> prereqid) {
		this.prereqid = prereqid;
	}
	public ArrayList<String> getPrereqchk() {
		return prereqchk;
	}
	public void setPrereqchk(ArrayList<String> prereqchk) {
		this.prereqchk = prereqchk;
	}
	public ArrayList<String> getAbbr() {
		return abbr;
	}
	public void setAbbr(ArrayList<String> abbr) {
		this.abbr = abbr;
	}

	public String getStudentGender() {
		return StudentGender;
	}

	public void setStudentGender(String studentGender) {
		StudentGender = studentGender;
	}

	public Boolean getNewStudent() {
		return NewStudent;
	}

	public void setNewStudent(Boolean newStudent) {
		NewStudent = newStudent;
	}

	public Integer getWu_attendancetaken() {
		return wu_attendancetaken;
	}

	public void setWu_attendancetaken(int wu_attendancetaken) {
		this.wu_attendancetaken = wu_attendancetaken;
	}

	public Integer getATT() {
		return ATT;
	}

	public void setATT(int aTT) {
		ATT = aTT;
	}

	
}
