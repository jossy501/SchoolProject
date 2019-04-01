package com.model;

public class Condition {

	private String id;
	private String name;
	private String descr;
	
	public Condition() {
		
	}
	
	public Condition(String id, String name, String descr) {
		this.id = id;
		this.name = name;
		this.descr = descr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Override
	public String toString() {
		return "Condition [id=" + id + ", name=" + name + ", descr=" + descr + ", getId()=" + getId() + ", getName()="
				+ getName() + ", getDescr()=" + getDescr() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
	
}
