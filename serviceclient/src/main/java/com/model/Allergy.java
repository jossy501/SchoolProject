package com.model;

public class Allergy {
	
	private String id;
	private String type;
	private String descr;
	
	public Allergy() {
		
	}
	
	public Allergy(String id, String type, String descr) {
		this.id = id;
		this.type = type;
		this.descr = descr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return "Allergy [id=" + id + ", type=" + type + ", descr=" + descr + ", getId()=" + getId() + ", getType()="
				+ getType() + ", getDescr()=" + getDescr() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	

}
