package com.etranzact.institution.dto;

public class Department {
	
	private String departmentId;
	private String description;
	private String departmentName;
	private String facultyId;
	
	
	public Department()
	{
		
		
	}

	public String getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
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
	
	

}
