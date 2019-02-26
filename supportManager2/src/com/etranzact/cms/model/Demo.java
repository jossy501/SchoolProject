package com.etranzact.cms.model;

public class Demo {

	
	public static void main(String[] args) {
		
		String names = "Joshua:Akin:Adeyemi:Adekunle:Felicia:Grace:Adam:Tony";
		
		String[] spltNames = names.split(":");
		
		for(String displayName: spltNames)
		{
			System.out.println(displayName);
		}

	}

}
