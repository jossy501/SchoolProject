package com.etranzact.institution.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import org.jboss.seam.international.StatusMessage.Severity;
import com.etranzact.institution.util.Sequencer;
import com.etranzact.institution.dto.Department;
import com.etranzact.institution.dto.Course;
import com.etranzact.institution.dto.Faculty;
import com.etranzact.institution.dto.Institution;
import com.etranzact.institution.dto.PaymentType;
import com.etranzact.institution.dto.Payment;
import com.etranzact.institution.dto.Session;
import com.etranzact.institution.dto.ProgramType;
import com.etranzact.institution.dto.Student;
import com.etranzact.supportmanager.utility.Env;


public class InstitutionModel {
	
	
	public InstitutionModel()
	{
		
		
	}
	
	/*Method to setup courses */
	public String createCourseReg(String courseId,String courseName,String desc, String deptId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		Sequencer seq = new Sequencer();
		try
		{
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "select COURSE_ID,COURSE_NAME,DESCRIPTION,DEPARTMENT_ID from school_course where course_id ='"+courseId+"' ";
			result = stat.executeQuery(query);
			System.out.println("query existed"+query);
			if(result.next())
			{
					message = "EXISTED";		
				
			}
			else
			{
					query = "insert into school_course(COURSE_ID, COURSE_NAME, DESCRIPTION,DEPARTMENT_ID)values('"+courseId+"', '"+courseName+"', '"+desc+"','"+deptId+"') ";
				
					System.out.println("createCourseReg query " + query);
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						message = "SUCCESS";
						con.commit();
					}
					else
					{
						message = "FAILED";
						con.rollback();
					}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
			
		}
		return message;
	}
	
	
	/*Method to setup PaymentType */
	public String createPayment(String paymentId,String institutionID,String sessionId,String paymentDate,String desc)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		Sequencer seq = new Sequencer();
		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = "insert into SCHOOL_PAYMENT(PAYMENT_TYPEID, INSTITUTION_ID,SESSION_ID,paymentDate,DESCRIPTION)values('"+paymentId+"', '"+institutionID+"', '"+sessionId+"','"+paymentDate+"','"+desc+"') ";
				
			System.out.println("createPayment query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
			
		}
		return message;
	}
	
	
	
	
	/*Method to setup PaymentType */
	public String createPaymentType(String paymentId,String paymentName,String desc, String deptId,double payAmt)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		Sequencer seq = new Sequencer();
		try
		{
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "select * from SCHOOL_PAYMENT_TYPE where PAYMENT_TYPEID ='"+paymentId+"' ";
			result = stat.executeQuery(query);
			System.out.println("query existed"+query);
			if(result.next())
			{
					message = "EXISTED";		
				
			}
			else
			{
				
					query = "insert into SCHOOL_PAYMENT_TYPE(PAYMENT_TYPEID, PAYMENT_NAME,DESCRIPTION,DEPARTMENT_ID,PAYMENT_AMOUNT)values('"+paymentId+"', '"+paymentName+"', '"+desc+"','"+deptId+"',"+payAmt+") ";
				
					System.out.println("createPaymentType query " + query);
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						message = "SUCCESS";
						con.commit();
					}
					else
					{
						message = "FAILED";
						con.rollback();
					}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
			
		}
		return message;
	}
	
	
	/*Method to setup Sessions */
	public String createSessions(String sessionId,String sessionName,String desc)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "select * from SCHOOL_SESSION where SESSION_ID ='"+sessionId+"' ";
			result = stat.executeQuery(query);
			System.out.println("query existed"+query);
			if(result.next())
			{
					message = "EXISTED";		
				
			}
			else
			{
					
					query = "insert into SCHOOL_SESSION(SESSION_ID, SESSION_NAME,DESCRIPTION)values('"+sessionId+"', '"+sessionName+"', '"+desc+"') ";
				
					System.out.println("createSessions query " + query);
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						message = "SUCCESS";
						con.commit();
					}
					else
					{
						message = "FAILED";
						con.rollback();
					}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
			
		}
		return message;
	}
	

	/*Method to setup Student */
	public String createStudent(String metircNo,String fristname,String lastname,String mobile, String email,String sex, String dateOfBirth,String creatDate,String detpId,String facultId,String institutionId,String programId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		try
		{
			con = connectToPMT();
			stat = con.createStatement();
	
		    query = "select MATRIC_NUMBER from SCHOOL_STUDENT where INSTITUTION_ID = '"+institutionId+"' ";
			result = stat.executeQuery(query);
			System.out.println("query existed"+query);
			if(result.next())
			{
					message = "EXISTED";		
				
			}
			else
			{	
					query = "insert into SCHOOL_STUDENT(MATRIC_NUMBER,FIRST_NAME,LAST_NAME,MOBILE_NUMBER,EMAIL,SEX,DATE_OF_BIRTH,REG_DATE,DEPARTMENT_ID,FACULTY_ID,INSTITUTION_ID,PROGRAM_ID)" +
							" values('"+metircNo+"', '"+fristname+"','"+lastname+"','"+mobile+"','"+email+"','"+sex+"','"+dateOfBirth+"','"+creatDate+"','"+detpId+"','"+facultId+"','"+institutionId+"','"+programId+"') ";
				
					System.out.println("createStudent query " + query);
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						message = "SUCCESS";
						con.commit();
					}
					else
					{
						message = "FAILED";
						con.rollback();
					}
					
					
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
			
		}
		return message;
	}
	
	
	/*Method to setup department */
	public String createDepartment(String deptId,String deptName,String desc, String facultyId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		Sequencer seq = new Sequencer();
		try
		{
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "select * from school_department where department_id = '"+deptId+"' ";
			result = stat.executeQuery(query);
			System.out.println("query existed"+query);
			if(result.next())
			{
					message = "EXISTED";		
				
			}
			else
			{
					
					query = "insert into school_department(DEPARTMENT_ID, DEPARTMENT_NAME, DESCRIPTION,FACULTY_ID)values('"+deptId+"', '"+deptName+"', '"+desc+"','"+facultyId+"') ";
				
					System.out.println("createDepartment query " + query);
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						message = "SUCCESS";
						con.commit();
					}
					else
					{
						message = "FAILED";
						con.rollback();
					}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
			
		}
		return message;
	}
	
	

	/*Method to setup faculty */
	public String createFaculty(String facultyId,String facultyName,String desc, String instId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		Sequencer seq = new Sequencer();
		try
		{
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "select * from school_faculty where faculty_id = '"+facultyId+"' ";
			result = stat.executeQuery(query);
			System.out.println("query existed"+query);
			if(result.next())
			{
					message = "EXISTED";		
				
			}
			else
			{
					
					query = "insert into school_faculty(FACULTY_ID, FACULTY_NAME, DESCRIPTION,INSTITUTION_ID)values('"+facultyId+"', '"+facultyName+"', '"+desc+"','"+instId+"') ";
				
					System.out.println("createFaculty query " + query);
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						message = "SUCCESS";
						con.commit();
					}
					else
					{
						message = "FAILED";
						con.rollback();
					}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
			
		}
		return message;
	}
	
	
	/*Method to setup institute */
	public String createInstitution(String instituteId,String InstituteName,String desc)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		Sequencer seq = new Sequencer();

		try
		{
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "select * from SCHOOL_INSTITUTION where institution_id = '"+instituteId+"' ";
			result = stat.executeQuery(query);
			System.out.println("query existed"+query);
			if(result.next())
			{
					message = "EXISTED";		
				
			}
			else
			{
					
					query = "insert into SCHOOL_INSTITUTION(INSTITUTION_ID, INSTITUTION_NAME, DESCRIPTION)values('"+instituteId+"', '"+InstituteName+"', '"+desc+"') ";
				
					System.out.println("ccreateInstitution query " + query);
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						message = "SUCCESS";
						con.commit();
					}
					else
					{
						message = "FAILED";
						con.rollback();
					}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
			
		}
		return message;
	}
	
	
	/*Method to setup level */
	public String createLevel(String levelId,String levelName,String desc)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		Sequencer seq = new Sequencer();
		try
		{
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "select * from SCHOOL_LEVEL where LEVEL_ID = '"+levelId+"' ";
			result = stat.executeQuery(query);
			System.out.println("query existed"+query);
			if(result.next())
			{
					message = "EXISTED";		
				
			}
			else
			{
					
					query = "insert into SCHOOL_LEVEL(LEVEL_ID, LEVEL_NAME, DESCRIPTION)values('"+levelId+"', '"+levelName+"', '"+desc+"') ";
				
					System.out.println("createLevel query " + query);
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						message = "SUCCESS";
						con.commit();
					}
					else
					{
						message = "FAILED";
						con.rollback();
					}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
			
		}
		return message;
	}

	/*Method to setup ProgramType */
	public String createProgramType(String programId,String programName,String desc)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		Sequencer seq = new Sequencer();
		try
		{
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "select * from SCHOOL_PROGRAM_TYPE where PROGRAM_ID = '"+programId+"' ";
			result = stat.executeQuery(query);
			System.out.println("query existed"+query);
			if(result.next())
			{
					message = "EXISTED";		
				
			}
			else
			{
					
					query = "insert into SCHOOL_PROGRAM_TYPE(PROGRAM_ID, PROGRAM_NAME, DESCRIPTION)values('"+programId+"', '"+programName+"', '"+desc+"') ";
				
					System.out.println("createLevel query " + query);
					output = stat.executeUpdate(query);
					if(output > 0)
					{
						message = "SUCCESS";
						con.commit();
					}
					else
					{
						message = "FAILED";
						con.rollback();
					}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
			
		}
		return message;
	}
	
	
	/*Method to delete Courses*/
	public String deleteCourse(String courseId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from SCHOOL_COURSE where COURSE_ID = '"+courseId+"' ";
			
			System.out.println("deleteCourse query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	/*Method to delete payment*/
	public String deletePayment(String paymentId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from SCHOOL_PAYMENT where PAYMENT_TYPEID = '"+paymentId+"' ";
			
			System.out.println("deletePayment query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	/*Method to delete Faculty*/
	public String deleteFaculty(String facultyId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from SCHOOL_FACULTY where FACULTY_ID = '"+facultyId+"' ";
			
			System.out.println("deletefACULTY query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	
	/*Method to delete institution*/
	public String deleteInstitute(String instituteId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from SCHOOL_INSTITUTION where INSTITUTION_ID = '"+instituteId+"' ";
			
			System.out.println("deletefACULTY query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	/*Method to delete level*/
	public String deleteLevel(String levelId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from SCHOOL_LEVEL where LEVEL_ID = '"+levelId+"' ";
			
			System.out.println("deleteLevel query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	

	/*Method to delete Program Type*/
	public String deleteProgramType(String programId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from SCHOOL_PROGRAM_TYPE where PROGRAM_ID = '"+programId+"' ";
			
			System.out.println("deleteProgramType query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	

	/*Method to delete PaymentType*/
	public String deletePaymentType(String paymentTypeId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from SCHOOL_PAYMENT_TYPE where PAYMENT_TYPEID = '"+paymentTypeId+"' ";
			
			System.out.println("deletePaymentType query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	/*Method to delete student*/
	public String deleteSudent(String studentId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from SCHOOL_STUDENT where STUDENT_ID = "+studentId+" ";
			
			System.out.println("deleteSudent query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	
	/*Method to delete Session*/
	public String deleteSession(String sessionId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from SCHOOL_SESSION where SESSION_ID = '"+sessionId+"' ";
			
			System.out.println("deleteSession query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	
	/*Method to delete Courses*/
	public String deleteDepartment(String deptId)
	{
		String query = "";
		String message = "";
		ArrayList arr = new ArrayList();
		int output = -1;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		

		try
		{
			con = connectToPMT();
			stat = con.createStatement();

			query = " delete from SCHOOL_COURSE where DEPARTMENT_ID = '"+deptId+"' ";
			
			System.out.println("deleteDepartment query " + query);
			output = stat.executeUpdate(query);
			if(output > 0)
			{
				message = "SUCCESS";
				con.commit();
			}
			else
			{
				message = "FAILED";
				con.rollback();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	
	/*Method to get the list of all department*/
	public ArrayList getDepartment()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Department dept;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = " select DEPARTMENT_ID,DEPARTMENT_NAME,DESCRIPTION,FACULTY_ID from school_department ";
			
			System.out.println("get Department " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
			
				dept = new Department();
				dept.setDepartmentId(""+result.getObject(1));
				dept.setDepartmentName(""+result.getObject(2));
				dept.setDescription(""+result.getObject(3));
				dept.setFacultyId(""+result.getObject(4));
				
				arr.add(dept);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		}
		return arr;
	}
	
	/*Method to get the list of all faculty*/
	public ArrayList getFaculty()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Faculty faculty;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = " select FACULTY_ID,FACULTY_NAME,DESCRIPTION,INSTITUTION_ID from school_faculty ";
			
			System.out.println("get Department " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
			
				faculty = new Faculty();
				faculty.setFacultyId(""+result.getObject(1));
				faculty.setFacultyName(""+result.getObject(2));
				faculty.setDescription(""+result.getObject(3));
				faculty.setInstitutionId(""+result.getObject(4));
				
				arr.add(faculty);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		}
		return arr;
	}
	
	/*Method to get the list of all institutions*/
	public ArrayList getInstitution()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Faculty faculty;
		Institution institute ;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		
		try
		{
			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = " select INSTITUTION_ID,INSTITUTION_NAME,DESCRIPTION from school_institution ";
			
			System.out.println("get institution " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				institute = new Institution();
				institute.setInstitutionId(""+result.getObject(1));
				institute.setInstitutionName(""+result.getObject(2));
				institute.setDescription(""+result.getObject(3));
				arr.add(institute);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
			
		}
		finally
		{
			closeConnectionPMT(con, result);
		}
		return arr;
	}
	
	/*Method to get the courses Registered*/
	public ArrayList getCoursesRegistered()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Course course = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "Select COURSE_ID,COURSE_NAME,DESCRIPTION,DEPARTMENT_ID FROM SCHOOL_COURSE";

			System.out.println("getCoursesRegistered " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
	
				course = new Course();	
				course.setCourseId(""+result.getObject(1));
				course.setCourseName(""+result.getObject(2));
				course.setDescription(""+result.getObject(3));
				course.setDepartmentId(""+result.getObject(4));
				arr.add(course);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	
	/*Method to get the courses by Id*/
	public ArrayList getCoursesRegisteredById(String courseId)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Course course = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "Select COURSE_ID,COURSE_NAME,DESCRIPTION,DEPARTMENT_ID FROM SCHOOL_COURSE where COURSE_ID = '"+courseId+"'";

			System.out.println("getCoursesRegistered " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
	
				course = new Course();	
				course.setCourseId(""+result.getObject(1));
				course.setCourseName(""+result.getObject(2));
				course.setDescription(""+result.getObject(3));
				course.setDepartmentId(""+result.getObject(4));
				arr.add(course);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	/*Method to get the paymentType Registered*/
	public ArrayList getPaymentTypeRegistered()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Course course = null;
		PaymentType type = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "Select PAYMENT_TYPEID,PAYMENT_NAME,DESCRIPTION,DEPARTMENT_ID,PAYMENT_AMOUNT FROM SCHOOL_PAYMENT_TYPE";

			System.out.println("getPaymentTypeRegistered " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
	
				type = new PaymentType();
				type.setPaymentTypeId(""+result.getObject(1));
				type.setPaymentName(""+result.getObject(2));
				type.setDescription(""+result.getObject(3));
				type.setDepartmentId(""+result.getObject(4));
				type.setPayAmt(""+result.getObject(5));
				
				
				arr.add(type);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	/*Method to get the paymentType by Id */
	public String getPaymentTypeById(String paymentId)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Course course = null;
		String message = "";
		PaymentType type = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "Select PAYMENT_NAME FROM SCHOOL_PAYMENT_TYPE where PAYMENT_TYPEID = '"+paymentId+"' ";

			System.out.println("getPaymentTypeName " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
	
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	
	
	/*Method to get the payment Registered*/
	public ArrayList getPaymentRegistered()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Course course = null;
		Payment payment = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "Select DESCRIPTION,paymentDate,INSTITUTION_ID,PAYMENT_TYPEID,SESSION_ID FROM SCHOOL_PAYMENT";

			System.out.println("getPayment " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
	
				payment = new Payment();
				payment.setDescription(""+result.getObject(1));
				payment.setPaymentDate(""+result.getObject(2));
				payment.setInstitutionId(""+result.getObject(3));
				payment.setPaymentTypeId(""+result.getObject(4));
				payment.setSessionId(""+result.getObject(5));
				
				arr.add(payment);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	/*Method to get the session Registered*/
	public ArrayList getSessionRegistered()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Course course = null;
		Session sess = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			 
			query = " Select SESSION_ID,SESSION_NAME,DESCRIPTION FROM SCHOOL_SESSION ";

			System.out.println("getSessionRegistered " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
	
				sess = new Session();
				sess.setSessionId(""+result.getObject(1));
				sess.setSessionName(""+result.getObject(2));
				sess.setDescription(""+result.getObject(3));	
				arr.add(sess);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	

	/*Method to get the student Registered*/
	public ArrayList getStudentRegistered()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Course course = null;
		Session sess = null;
		Student student = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
	
		
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			 
			query = " Select STUDENT_ID,MATRIC_NUMBER,REG_DATE,DEPARTMENT_ID,FACULTY_ID,INSTITUTION_ID,PROGRAM_ID FROM SCHOOL_STUDENT ";

			System.out.println("getStudentRegistered " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				student = new Student();
				student.setStudentId(""+result.getObject(1));
				student.setMatricNo(""+result.getObject(2));
				student.setRegDate(""+result.getObject(3));
				student.setDeptId(""+result.getObject(4));
				student.setFacultyId(""+result.getObject(5));
				student.setInstitutionId(""+result.getObject(6));
				student.setProgramId(""+result.getObject(7));
				arr.add(student);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	/*Method to get the department Registered*/
	public ArrayList getDepartmentRegistered()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Department dept = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "Select DEPARTMENT_ID,DEPARTMENT_NAME,DESCRIPTION,FACULTY_ID FROM SCHOOL_DEPARTMENT";

			System.out.println("getCoursesRegistered " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
	
				dept = new Department();
				dept.setDepartmentId(""+result.getObject(1));
				dept.setDepartmentName(""+result.getObject(2));
				dept.setDescription(""+result.getObject(3));
				dept.setFacultyId(""+result.getObject(4));
				arr.add(dept);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	/*Method to get the department By ID*/
	public String getDepartmentById(String deptId)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		String message = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "Select DEPARTMENT_NAME FROM SCHOOL_DEPARTMENT WHERE DEPARTMENT_ID = '"+deptId+"' ";

			System.out.println("getDepartmentBy Id " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	

	/*Method to get the faculty Registered*/
	public ArrayList getFacultyRegistered()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Department dept = null;
		Faculty faculty = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

	
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = "Select FACULTY_ID,FACULTY_NAME,DESCRIPTION,INSTITUTION_ID FROM SCHOOL_FACULTY";

			System.out.println("getFacultyRegistered " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				faculty = new Faculty();
				faculty.setFacultyId(""+result.getObject(1));
				faculty.setFacultyName(""+result.getObject(2));
				faculty.setDescription(""+result.getObject(3));
				faculty.setInstitutionId(""+result.getObject(4));
				arr.add(faculty);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	/*Method to get the faculty By Id*/
	public String getFacultyById(String facultyId)
	{
		String query = "";
		String message = "";
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

	
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = " Select FACULTY_NAME FROM SCHOOL_FACULTY where FACULTY_ID = '"+facultyId+"' ";

			System.out.println("getFaculty by ID " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
			
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	

	/*Method to get the Institution that has been Registered*/
	public ArrayList getInstituteRegistered()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Department dept = null;
		Faculty faculty = null;
		Institution institute = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

	
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = " Select INSTITUTION_ID,INSTITUTION_NAME,DESCRIPTION FROM SCHOOL_INSTITUTION ";

			System.out.println("getInstituteRegistered " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				
				institute = new Institution();
				institute.setInstitutionId(""+result.getObject(1));
				institute.setInstitutionName(""+result.getObject(2));
				institute.setDescription(""+result.getObject(3));
				arr.add(institute);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	
	/*Method to get the Institution By Id*/
	public String getInstituteById(String institutionId)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		String message = "";
		Institution institute = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

	
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = " Select INSTITUTION_NAME FROM SCHOOL_INSTITUTION  WHERE INSTITUTION_ID = '"+institutionId+"' ";
			
			System.out.println("getInstitute By Id " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				message = ""+result.getObject(1);
				
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	
	/*Method to get the Level that has been Registered*/
	public ArrayList getLevelRegistered()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		Department dept = null;
		Faculty faculty = null;
		Institution institute = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

	
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = " Select LEVEL_ID,LEVEL_NAME,DESCRIPTION FROM SCHOOL_LEVEL ";

			System.out.println("getLevelRegistered " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				
				institute = new Institution();
				institute.setInstitutionId(""+result.getObject(1));
				institute.setInstitutionName(""+result.getObject(2));
				institute.setDescription(""+result.getObject(3));
				arr.add(institute);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	
	/*Method to get programType that has been Registered*/
	public ArrayList getProgramTypeRegistered()
	{
		String query = "";
		ArrayList arr = new ArrayList();
		ProgramType program = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;

	
		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = " Select PROGRAM_ID,PROGRAM_NAME,DESCRIPTION FROM SCHOOL_PROGRAM_TYPE ";

			System.out.println("getProgramTypeRegistered " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				
				program = new ProgramType();
				program.setProgramId(""+result.getObject(1));
				program.setProgramName(""+result.getObject(2));
				program.setDescription(""+result.getObject(3));
				arr.add(program);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return arr;
	}
	
	/*Method to get program By Id */
	public String getProgramById(String programId)
	{
		String query = "";
		ArrayList arr = new ArrayList();
		ProgramType program = null;
		int counter = 0;
		Connection con = null;
		Statement stat = null;
		ResultSet result = null;
		String message = "";

		try
		{			
			con = connectToPMT();
			stat = con.createStatement();
			
			query = " Select PROGRAM_NAME FROM SCHOOL_PROGRAM_TYPE where PROGRAM_ID = '"+programId+"' ";

			System.out.println("getProgramm By Id " + query);
			result = stat.executeQuery(query);
			while(result.next())
			{
				
				message = ""+result.getObject(1);
			
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			closeConnectionPMT(con, result);
		
		}
		finally
		{
			closeConnectionPMT(con, result);
		
		}
		return message;
	}
	
	
	/*Connection to PMT database*/
	private Connection connectToPMT() throws Exception
	{
		Connection con = null;
		con = Env.getConnectionPMTDB();
		return con;
	}   
	
	private void closeConnectionPMT(Connection con, ResultSet result)
	{
		if(result != null)
		{
			try
			{
				result.close();
				result = null;

			}
			catch(Exception ignore){}
		}
		if(con != null)
		{
			try
			{
				con.close();
				con = null;
			}
			catch(Exception ignore){}
		}
	}
	
	
	
	

}
