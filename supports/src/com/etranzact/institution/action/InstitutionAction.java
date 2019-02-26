package com.etranzact.institution.action;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;

import com.etranzact.cms.model.CardManagementModel;
import com.etranzact.institution.dto.Course;
import com.etranzact.institution.model.InstitutionModel;
import com.etranzact.institution.util.Sequencer;



/**
 * @author joshua.aruno
 *
 */
@Restrict("#{authenticator.curUser.loggedIn}")
@Scope(ScopeType.SESSION)
@Name("institutionAction")
public class InstitutionAction implements Serializable{
	
	
	
	@RequestParameter
	private String id;//used in passing values from a jsf to a jsf
	
	private String strParam;
	private Date start_date;
	private Date end_date;
	private String start_hr;
	private String end_hr;
	private String edit_id;
	private double total_amount = 0.0d;
	private int total_count = 0;
	private String courseName;
	private String courseDesc;
	private String courseDeptId;
	
	private String departmentId;
	private String deptDesc;
	private String departmentName;
	private String facultyId;
	
	
	private String facultDesc;
	private String facultyName;
	private String institutionId;
	
	private String intsDesc;
	private String institutionName;
	
	private String levelName;
	private String leveDesc;
	
	private String paymentName;
	private String paymentDesc;
	private double payAmt = 0.0d;

	
	private String sessionName;
	private String sessionDesc;
	
	
	private String paydesc;
	private String payDate;
	private String payStatus;
	private String paymentTypeId;
	private String sessionId;
	
	private String programId;
	private String programDesc;
	private String programName;
	
	private String metricNo;
	
	private String firstName;
	private String lastName;
	private String dateOfBirth;
	private Date dateOfBirths;
	private String sex;
	private String email;
	private String mobileno;
	
	
	
	
	
	
	
	
	
	
	private ArrayList departmentList = new ArrayList();
	private ArrayList courseReglist = new ArrayList();
	private ArrayList deptReglist = new ArrayList();
	private ArrayList facultyList = new ArrayList();
	
	private ArrayList facultytReglist = new ArrayList();
	private ArrayList instReglist = new ArrayList();
	private ArrayList levelList = new ArrayList();
	private ArrayList paymentTypeList = new ArrayList();
	private ArrayList paymentList = new ArrayList();
	private ArrayList sessionList = new ArrayList();
	private ArrayList institutionList = new ArrayList();
	private ArrayList programTypeList = new ArrayList();
	private ArrayList studentList = new ArrayList();
	
	
	private Course course;
	
	
	

	
	@In
    FacesMessages facesMessages;
	
	@Logger
	private Log log;

	FacesContext context;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStrParam() {
		return strParam;
	}

	public void setStrParam(String strParam) {
		this.strParam = strParam;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getStart_hr() {
		return start_hr;
	}

	public void setStart_hr(String start_hr) {
		this.start_hr = start_hr;
	}

	public String getEnd_hr() {
		return end_hr;
	}

	public void setEnd_hr(String end_hr) {
		this.end_hr = end_hr;
	}

	public double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public FacesMessages getFacesMessages() {
		return facesMessages;
	}

	public void setFacesMessages(FacesMessages facesMessages) {
		this.facesMessages = facesMessages;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public FacesContext getContext() {
		return context;
	}

	public void setContext(FacesContext context) {
		this.context = context;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseDesc() {
		return courseDesc;
	}

	public void setCourseDesc(String courseDesc) {
		this.courseDesc = courseDesc;
	}

	public String getCourseDeptId() {
		return courseDeptId;
	}

	public void setCourseDeptId(String courseDeptId) {
		this.courseDeptId = courseDeptId;
	}

	public ArrayList getDepartmentList() {
		getDepartment();
		return departmentList;
	}

	public void setDepartmentList(ArrayList departmentList) {
		this.departmentList = departmentList;
	}
	
	public ArrayList getCourseReglist() {
		getexistedCourses();
		return courseReglist;
	}

	public void setCourseReglist(ArrayList courseReglist) {
		this.courseReglist = courseReglist;
	}

	
	public String getEdit_id() {
		return edit_id;
	}

	public void setEdit_id(String edit_id) {
		this.edit_id = edit_id;
	}
	
	
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(String facultyId) {
		this.facultyId = facultyId;
	}
	

	public ArrayList getFacultyList() {
		getFaculty();
		return facultyList;
	}

	public void setFacultyList(ArrayList facultyList) {
		this.facultyList = facultyList;
	}

	
	public ArrayList getDeptReglist() {
		getexistedDepartment();
		return deptReglist;
	}

	public void setDeptReglist(ArrayList deptReglist) {
		this.deptReglist = deptReglist;
	}

	
	public String getFacultDesc() {
		return facultDesc;
	}

	public void setFacultDesc(String facultDesc) {
		this.facultDesc = facultDesc;
	}

	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public String getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(String institutionId) {
		this.institutionId = institutionId;
	}

	
	public ArrayList getInstitutionList() {
		getInstitution();
		return institutionList;
	}

	public void setInstitutionList(ArrayList institutionList) {
		this.institutionList = institutionList;
	}

	
	public ArrayList getFacultytReglist() {
		getexistedFaculty();
		return facultytReglist;
	}

	public void setFacultytReglist(ArrayList facultytReglist) {
		this.facultytReglist = facultytReglist;
	}
	

	public String getIntsDesc() {
		return intsDesc;
	}

	public void setIntsDesc(String intsDesc) {
		this.intsDesc = intsDesc;
	}

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public ArrayList getInstReglist() {
		getexistedInstitute();
		return instReglist;
	}

	public void setInstReglist(ArrayList instReglist) {
		this.instReglist = instReglist;
	}

	
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getLeveDesc() {
		return leveDesc;
	}

	public void setLeveDesc(String leveDesc) {
		this.leveDesc = leveDesc;
	}

	
	public ArrayList getLevelList() {
		getexistedLevel();
		return levelList;
	}

	public void setLevelList(ArrayList levelList) {
		this.levelList = levelList;
	}

	
	
	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getPaymentDesc() {
		return paymentDesc;
	}

	public void setPaymentDesc(String paymentDesc) {
		this.paymentDesc = paymentDesc;
	}
	
	

	public ArrayList getPaymentTypeList() {
		getexistedPaymentType();
		return paymentTypeList;
	}

	public void setPaymentTypeList(ArrayList paymentTypeList) {
		this.paymentTypeList = paymentTypeList;
	}
	
	

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getSessionDesc() {
		return sessionDesc;
	}

	public void setSessionDesc(String sessionDesc) {
		this.sessionDesc = sessionDesc;
	}

	
	public ArrayList getSessionList() {
		getexistedSession();
		return sessionList;
	}

	public void setSessionList(ArrayList sessionList) {
		this.sessionList = sessionList;
	}

	
	public double getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(double payAmt) {
		this.payAmt = payAmt;
	}
	
	

	public String getPaydesc() {
		return paydesc;
	}

	public void setPaydesc(String paydesc) {
		this.paydesc = paydesc;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	

	public ArrayList getPaymentList() {
		getexistedPayment();
		return paymentList;
	}

	public void setPaymentList(ArrayList paymentList) {
		this.paymentList = paymentList;
	}


	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getProgramDesc() {
		return programDesc;
	}

	public void setProgramDesc(String programDesc) {
		this.programDesc = programDesc;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}
	
	

	public ArrayList getProgramTypeList() {
		getexistedProgramType();
		return programTypeList;
	}

	public void setProgramTypeList(ArrayList programTypeList) {
		this.programTypeList = programTypeList;
	}
	

	public String getMetricNo() {
		return metricNo;
	}

	public void setMetricNo(String metricNo) {
		this.metricNo = metricNo;
	}

	
	public ArrayList getStudentList() {
		getexistedStudent();
		return studentList;
	}

	public void setStudentList(ArrayList studentList) {
		this.studentList = studentList;
	}
	
	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	
	
	public Date getDateOfBirths() {
		return dateOfBirths;
	}

	public void setDateOfBirths(Date dateOfBirths) {
		this.dateOfBirths = dateOfBirths;
	}

	public void resetValues()
	{
	
		setCourseDeptId(null);
		setCourseDesc(null);
		setCourseName(null);
		setDepartmentName(null);
		setDeptDesc(null);
		setInstitutionName(null);
		setIntsDesc(null);
		setLeveDesc(null);
		setLevelName(null);
		setPaymentDesc(null);
		setPaymentName(null);
		setSessionDesc(null);
		setSessionName(null);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date d = df.parse(df.format(new Date()));
			setStart_date(d);
			setEnd_date(d);
		}
		catch(Exception ex){ex.printStackTrace();
		}
		
		setStart_hr("00");
		setEnd_hr("23");
		
	}

	public void getexistedInstitute()
	{
			InstitutionModel model = new InstitutionModel();
			instReglist = model.getInstituteRegistered();
	}

	
	public void getexistedLevel()
	{
			InstitutionModel model = new InstitutionModel();
			levelList = model.getLevelRegistered();
	}

	public void getexistedPaymentType()
	{
			InstitutionModel model = new InstitutionModel();
			paymentTypeList = model.getPaymentTypeRegistered();
	}
	
	public void getexistedSession()
	{
			InstitutionModel model = new InstitutionModel();
			sessionList = model.getSessionRegistered();
	}
	public void getexistedFaculty()
	{
			InstitutionModel model = new InstitutionModel();
			facultytReglist = model.getFacultyRegistered();
	}
	
	public void getexistedCourses()
	{
			InstitutionModel model = new InstitutionModel();
			courseReglist = model.getCoursesRegistered();
	}
	
	public void getexistedPayment()
	{
			InstitutionModel model = new InstitutionModel();
			paymentList = model.getPaymentRegistered();
	}
	
	public void getexistedProgramType()
	{
		InstitutionModel model = new InstitutionModel();
		programTypeList = model.getProgramTypeRegistered();
		
	}
	
	public void getexistedStudent()
	{
		InstitutionModel model = new InstitutionModel();
		studentList = model.getStudentRegistered();
	
	}
	
	public void getexistedDepartment()
	{
		InstitutionModel model = new InstitutionModel();
		deptReglist = model.getDepartmentRegistered();
	
	}
	
	public String getDepartmentByName(String deptId)
	{
		InstitutionModel model = new InstitutionModel();
		return model.getDepartmentById(deptId);
	}
	public String getFacultyByName(String facultyId)
	{
		InstitutionModel model = new InstitutionModel();
		return model.getFacultyById(facultyId);
		
	}
	public String getSchoolByName(String instituteId)
	{
		InstitutionModel model = new InstitutionModel();
		return model.getInstituteById(instituteId);	
	}
	public String getProgramByName(String programId)
	{
		InstitutionModel model = new InstitutionModel();
		return model.getProgramById(programId);
		
	}
	public String getPaymentByName(String paymentTypeId)
	{
		InstitutionModel model = new InstitutionModel();
		return model.getPaymentTypeById(paymentTypeId);
	}
	
	
	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public void createCourseReg()
	{
		
		try
		{
				InstitutionModel model = new InstitutionModel();
				Sequencer seq = new Sequencer();
				
				//String courseId = "001";
				String courseId = seq.getNext("02", 5);
				String courseName = getCourseName();
				String desc = getCourseDesc();
				String deptId = getCourseDeptId();
				
				System.out.println("courseId "+courseId+"course Name"+"Course Desc"+desc+"Departement ID "+deptId);
				
					String mess = model.createCourseReg(courseId, courseName, desc, deptId);
				
					if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "Course Already Regisgtered";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = " Course Successfully Registered ! ";
						
						courseReglist = model.getCoursesRegistered();
						System.out.println("courses list "+courseReglist.size());
					}
					else
					{
						mess = " Course not Registered !";
					}	
			    	
					facesMessages.add(Severity.INFO, mess);
					setCourseName(null);
					setCourseDesc(null);
				
				
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	public void setCourseRegToEdit()
	{
		try
		{
			
			InstitutionModel model = new InstitutionModel();
			String edit = getEdit_id();
			
			System.out.println("Edit values   "+edit);
			
			ArrayList a = model.getCoursesRegisteredById(edit);
			System.out.println("Array Size for  setCourseRegToEdit()  "+a.size());
			if(a.size() > 0)
			{
				Course course = (Course)a.get(0);
				getCourse().setCourseName(course.getCourseName());
				getCourse().setDepartmentId(course.getDepartmentId()); 
				getCourse().setDescription(course.getDescription());
			}
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			
		}
		
		
	}
	
	
	public void createPayment()
	{
		
		try
		{
				InstitutionModel model = new InstitutionModel();
				
			/*	Sequencer seq = new Sequencer();
		
				String paymentId = seq.getNext("08", 11);
				
				*/
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");

	            String beginDate = df.format(getStart_date());
	            Calendar cal = Calendar.getInstance();
	         
	            String createdDate = df.format(cal.getTime());
	            
	       
	            
	            String institutionId = getInstitutionId();
	            String paymentIds = getPaymentTypeId();
	            String sessionId = getSessionId();
	            String paydesc = getPaydesc();
				
	          
				//System.out.println("paymentId "+paymentId+"Payment Name"+paymentName+"Payment DESC"+desc+"Departement ID "+deptId);
				
					String mess = model.createPayment(paymentIds, institutionId, sessionId, createdDate,paydesc);
				
				/*	if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "Payment Type  Already Registered";
					}*/
					if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = " Payment Successfully Setup ! ";
						
						paymentList = model.getPaymentRegistered();
						System.out.println("Payment  list "+paymentList.size());
					}
					else
					{
						mess = " Payment not Setup !";
					}	
			    	
					facesMessages.add(Severity.INFO, mess);
					setPaydesc(null);
					
				
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void createPaymentType()
	{
		
		try
		{
				InstitutionModel model = new InstitutionModel();
				
				Sequencer seq = new Sequencer();
		
				String paymentId = seq.getNext("08", 5);
				String paymentName = getPaymentName();
				String desc = getPaymentDesc();
				String deptId = getCourseDeptId();
				double payAmt = getPayAmt();
			    
				
				
				System.out.println("paymentId "+paymentId+"Payment Name"+paymentName+"Payment DESC"+desc+"Departement ID "+deptId);
				
					String mess = model.createPaymentType(paymentId, paymentName, desc, deptId,payAmt);
				
					if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "Payment Type  Already Registered";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = " Payment Type Successfully Registered ! ";
						
						paymentTypeList = model.getPaymentTypeRegistered();
						System.out.println("Payment Type list "+paymentTypeList.size());
					}
					else
					{
						mess = " Payment Type not Registered !";
					}	
			    	
					facesMessages.add(Severity.INFO, mess);
					setPaymentName(null);
					setPaymentDesc(null);
					setPayAmt(0.0);
				
				
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void createStudent()
	{
		
		try
		{
				InstitutionModel model = new InstitutionModel();
				Sequencer seq = new Sequencer();
				
				String studentId  = seq.getNext("07",5);
			
				String matricNo = getMetricNo();
				
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
				
				DateFormat db = new SimpleDateFormat("yyyy-MM-dd");

	            String beginDate = df.format(getStart_date());
	            Calendar cal = Calendar.getInstance();
	         
	            String createdDate = df.format(cal.getTime());
	            
	            String firstname = getFirstName();
	            String lastname = getLastName();
	            String mobileno = getMobileno();
	            String email =getEmail();
	            String sex = getSex(); 
	            String dateOfBirth = df.format(getDateOfBirths());
	            
	            
	            String deptId = getDepartmentId();
	            String facultyId = getFacultyId();
	            String institutionId = getInstitutionId();
	            String programId = getProgramId();
	            
				
				System.out.println("studentId "+studentId+"matricNo "+matricNo+"CreateDate"+createdDate+"Deptment ID"+deptId+"Faculty ID"+facultyId+"iNSTITUTION Id"+institutionId+"Program Id"+programId);
				
					String mess = model.createStudent(matricNo,firstname,lastname,mobileno,email,sex,dateOfBirth,createdDate,deptId,facultyId,institutionId,programId);
				
					if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "  Matic No ["+matricNo+"] Has Already Been Registered with this School  ";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = " Student Successfully Registered ! ";
						
						studentList = model.getStudentRegistered();
						System.out.println("Student List "+studentList.size());
					}
					else
					{
						mess = " Student not Registered !";
					}	
			    	
					facesMessages.add(Severity.INFO, mess);
					setMetricNo(null);
					setDepartmentId(null);
					setFacultyId(null);
					setInstitutionId(null);
					setProgramId(null);
					setFirstName(null);
					setLastName(null);
					setMobileno(null);
					setEmail(null);
					setDateOfBirths(null);
					
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void createSession()
	{
		
		try
		{
				InstitutionModel model = new InstitutionModel();
				Sequencer seq = new Sequencer();
				
				String sessionId  = seq.getNext("07",5);
			
				String sessionName = getSessionName();
				String desc = getSessionDesc();
			
				
				System.out.println("sessionId "+sessionId+"Session Name"+sessionName+"Session DESC"+desc);
				
					String mess = model.createSessions(sessionId, sessionName,desc);
				
					if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "Session  Already Registered";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = " Session Successfully Registered ! ";
						
						sessionList = model.getSessionRegistered();
						System.out.println("Session List "+sessionList.size());
					}
					else
					{
						mess = " Session not Registered !";
					}	
			    	
					facesMessages.add(Severity.INFO, mess);
					setSessionName(null);
					setSessionDesc(null);
				
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	public void createDepartment()
	{
		
		try
		{
				InstitutionModel model = new InstitutionModel();
				Sequencer seq = new Sequencer();
				
				String deptId  = seq.getNext("03",5);
				String deparment = getDepartmentName();
				String desc = getDeptDesc();
				String facultyId = getFacultyId();
				
				System.out.println("deptId "+deptId+"deparment Name"+deparment+" Desc"+desc+"facultyId ID "+deptId);
				
					String mess = model.createDepartment(deptId, deparment, desc, facultyId);
				
					if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "Department Already Regisgtered";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = " Department Successfully Registered ! ";
						
						deptReglist = model.getDepartmentRegistered();
						System.out.println("department list "+deptReglist.size());
					}
					else
					{
						mess = " Department not Registered !";
					}	
			    	
					facesMessages.add(Severity.INFO, mess);
					setDepartmentName(null);
					setDeptDesc(null);
				
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void deleteCourse()
	{
	
		
		try
		{
			InstitutionModel model = new InstitutionModel();
	
				String courseId = getId();
				String courseEdith = getEdit_id();
			
				System.out.println("CourseId  "+courseId+"Course Edith "+courseEdith);
				
				String mess = model.deleteCourse(courseEdith);
				if(mess.equalsIgnoreCase("SUCCESS"))
				{
					mess = "Course Successfully Deleted";
				
				}
				else
				{
					mess = "Course Not Deleted";
				}
				
				
				facesMessages.add(Severity.INFO, mess);
			
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	
	public void deletePayment()
	{
	
		
		try
		{
			InstitutionModel model = new InstitutionModel();
	
				String payId = getId();
				String payEdith = getEdit_id();
			
				System.out.println("CourseId  "+payId+"Course Edith "+payEdith);
				
				String mess = model.deletePayment(payEdith);
				if(mess.equalsIgnoreCase("SUCCESS"))
				{
					mess = "Payment Successfully Deleted";
				
				}
				else
				{
					mess = "Payment can Not be Deleted";
				}
				
				
				facesMessages.add(Severity.INFO, mess);
			
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void deleteDepartment()
	{
	
		
		try
		{
			InstitutionModel model = new InstitutionModel();
	
				String deptId = getId();
				String deptEdith = getEdit_id();
			
				System.out.println("deptment ID  "+deptId+"dept Edith "+deptEdith);
				
				String mess = model.deleteCourse(deptEdith);
				if(mess.equalsIgnoreCase("SUCCESS"))
				{
					mess = "Department Successfully Deleted";
				
				}
				else
				{
					mess = "Department Not Deleted";
				}
				
				
				facesMessages.add(Severity.INFO, mess);
			
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void createFaculty()
	{
		
		try
		{
				InstitutionModel model = new InstitutionModel();
				Sequencer seq = new Sequencer();
				
				String facultId  = seq.getNext("04", 5);
				
				String facultName = getFacultyName();
				String desc = getFacultDesc();
				String institutionId = getInstitutionId();
				
				System.out.println("faculty Id "+facultId+"faculty Name"+facultName+" Desc"+desc+" Institution ID "+institutionId);
				
					String mess = model.createFaculty(facultId,facultName, desc, institutionId);
				
					if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "Faculty Already Registered";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = " Faculty Successfully Registered ! ";
						
						facultytReglist = model.getFacultyRegistered();
						System.out.println("facultytReglist list "+facultytReglist.size());
					}
					else
					{
						mess = " Faculty not Registered !";
					}	
			    	
					facesMessages.add(Severity.INFO, mess);
					setFacultyName(null);
					setFacultDesc(null);
				
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void createInstitute()
	{
		
		try
		{
				InstitutionModel model = new InstitutionModel();
				
				//String instituteId = "001";
				Sequencer seq = new Sequencer();
				
				String instituteId  = seq.getNext("05", 5);
				String InstituteName = getInstitutionName();
				String desc = getIntsDesc();
				
				System.out.println("Institute Id "+instituteId+"Institute Name"+InstituteName+" Desc"+desc);
				
					String mess = model.createInstitution(instituteId, InstituteName, desc);
				
					if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "Institution Already Registered";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = " Institution Successfully Registered ! ";
						
						instReglist = model.getInstituteRegistered();
						System.out.println("instReglist list "+instReglist.size());
					}
					else
					{
						mess = " Institution not Registered !";
					}	
			    	
					facesMessages.add(Severity.INFO, mess);
					setInstitutionName(null);
					setIntsDesc(null);
				
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void createLevel()
	{
		
		try
		{
				InstitutionModel model = new InstitutionModel();
				Sequencer seq = new Sequencer();
				
				String levelId  = seq.getNext("06",5);
				//String levelId = "001";
				String LevelName = getLevelName();
				String desc = getLeveDesc();
				
				System.out.println("level Id "+levelId+"Level Name"+LevelName+" Desc"+desc);
				
					String mess = model.createLevel(levelId, LevelName, desc);
				
					if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "Level Already Registered";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = " Level Successfully Registered ! ";
						
						instReglist = model.getLevelRegistered();
						System.out.println("instReglist list "+instReglist.size());
					}
					else
					{
						mess = " Institution not Registered !";
					}	
			    	
					facesMessages.add(Severity.INFO, mess);
					setLevelName(null);
					setLeveDesc(null);
				
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void createProgramType()
	{
		
		try
		{
				InstitutionModel model = new InstitutionModel();
				Sequencer seq = new Sequencer();
				
				String programId  = seq.getNext("10",5);
		
				String programName = getProgramName();
				
				String desc = getProgramDesc();
				
				System.out.println("program Id "+programId+"prgroma Name"+programName+" Desc"+desc);
				
					String mess = model.createProgramType(programId, programName, desc);
				
					if(mess.equalsIgnoreCase("EXISTED"))
					{
						mess = "Program Type Already Registered";
					}
					else if(mess.equalsIgnoreCase("SUCCESS"))
					{
						mess = " Program Type Successfully Registered ! ";
						programTypeList = model.getProgramTypeRegistered();
						System.out.println("Program Type list "+programTypeList.size());
					}
					else
					{
						mess = " Program Type not Registered !";
					}	
			    	
					facesMessages.add(Severity.INFO, mess);
					setProgramName(null);
					setProgramDesc(null);
				
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	
	
	public void deleteFaculty()
	{
	
		
		try
		{
			InstitutionModel model = new InstitutionModel();
	
				String facId = getId();
				String facEdith = getEdit_id();
			
				System.out.println("Faculty ID  "+facId+"Faculty Edith "+facEdith);
				
				String mess = model.deleteFaculty(facEdith);
				if(mess.equalsIgnoreCase("SUCCESS"))
				{
					mess = "Faculty Successfully Deleted";
				
				}
				else
				{
					mess = "Faculty Not Deleted";
				}
				
				
				facesMessages.add(Severity.INFO, mess);
			
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	public void deleteInstitute()
	{
	
		
		try
		{
			InstitutionModel model = new InstitutionModel();
	
				String instId = getId();
				String instEdith = getEdit_id();
			
				System.out.println("Institute ID  "+instId+"institute Edith "+instEdith);
				
				String mess = model.deleteInstitute(instEdith);
				if(mess.equalsIgnoreCase("SUCCESS"))
				{
					mess = "Institution Successfully Deleted";
				
				}
				else
				{
					mess = "Institution Not Deleted";
				}
				
				
				facesMessages.add(Severity.INFO, mess);
			
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void deleteLevel()
	{
	
		
		try
		{
			InstitutionModel model = new InstitutionModel();
	
				String levelId = getId();
				String levelEdith = getEdit_id();
			
				System.out.println("level ID  "+levelId+"level Edith "+levelEdith);
				
				String mess = model.deleteLevel(levelEdith);
				if(mess.equalsIgnoreCase("SUCCESS"))
				{
					mess = "Level Successfully Deleted";
				
				}
				else
				{
					mess = "Level Not Deleted";
				}
				
				
				facesMessages.add(Severity.INFO, mess);
			
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	public void deleteProgramType()
	{
	
		
		try
		{
			InstitutionModel model = new InstitutionModel();
	
				String programId = getId();
				String programEdith = getEdit_id();
			
				System.out.println("Program ID  "+programId+"Program Edith "+programEdith);
				
				String mess = model.deleteProgramType(programEdith);
				if(mess.equalsIgnoreCase("SUCCESS"))
				{
					mess = "Program Type Successfully Deleted";
				
				}
				else
				{
					mess = "Programm Type Not Deleted";
				}
				
				
				facesMessages.add(Severity.INFO, mess);
			
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	public void deletePaymentType()
	{
	
		
		try
		{
			InstitutionModel model = new InstitutionModel();
	
				String paymentId = getId();
				String paymentEdith = getEdit_id();
			
				System.out.println("Payment ID  "+paymentId+"level Edith "+paymentEdith);
				
				String mess = model.deletePaymentType(paymentEdith);
				if(mess.equalsIgnoreCase("SUCCESS"))
				{
					mess = "Payment Type Successfully Deleted";
				
				}
				else
				{
					mess = "Payment Type Not Deleted";
				}
				
				
				facesMessages.add(Severity.INFO, mess);
			
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void deleteStudent()
	{
	
		
		try
		{
			InstitutionModel model = new InstitutionModel();
	
				String schId = getId();
				String schEdith = getEdit_id();
			
				System.out.println("school  "+schId+"School Edith "+schEdith);
				
				String mess = model.deleteSudent(schEdith);
				if(mess.equalsIgnoreCase("SUCCESS"))
				{
					mess = "Student Successfully Deleted";
				
				}
				else
				{
					mess = "Student can Not be Deleted";
				}
				
				
				facesMessages.add(Severity.INFO, mess);
			
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	public void deleteSession()
	{
	
		
		try
		{
			InstitutionModel model = new InstitutionModel();
	
				String sessionId = getId();
				String sessionEdith = getEdit_id();
			
				System.out.println("Session ID  "+sessionId+"Session Edith "+sessionEdith);
				
				String mess = model.deleteSession(sessionEdith);
				if(mess.equalsIgnoreCase("SUCCESS"))
				{
					mess = "Session Successfully Deleted";
				
				}
				else
				{
					mess = "Session  Not Deleted";
				}
				
				
				facesMessages.add(Severity.INFO, mess);
			
			
		}catch(Exception ex)
		{
				ex.printStackTrace();
		}
		
	}
	
	
	public void getDepartment()
	{
			try
			{
				
				InstitutionModel model = new InstitutionModel();
				departmentList = model.getDepartment();
				System.out.println("  departmentList  "+departmentList.size());

			}catch(Exception ex)
			{	
				ex.printStackTrace();
				
			}
		 
	}


	public void getFaculty()
	{
			try
			{
				
				InstitutionModel model = new InstitutionModel();
				facultyList = model.getFaculty();
				System.out.println("  facultyList  "+facultyList.size());

			}catch(Exception ex)
			{	
				ex.printStackTrace();
				
			}
		 
	}
	
	public void getInstitution()
	{
			try
			{
				
				InstitutionModel model = new InstitutionModel();
				institutionList = model.getInstitution();
				System.out.println("  institutionList  "+institutionList.size());

			}catch(Exception ex)
			{	
				ex.printStackTrace();
				
			}
		 
	}
	
	public double makeDouble(String value)
	{
		double resp = 0.00;
		try
		{
			if(value == null || value.equals("") || value.equals("null"))
			{
				value= "0.00";
			}
			else
			{
				resp = Double.parseDouble(value);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return resp;
	}
	
	
	
	
	
	
	
	
	
	

}
