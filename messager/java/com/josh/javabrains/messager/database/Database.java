package com.josh.javabrains.messager.database;

import java.util.HashMap;
import java.util.Map;

import com.josh.javabrains.messager.model.Message;
import com.josh.javabrains.messager.model.Profile;

public class Database {
	
	private static Map<Long, Message> message = new HashMap<>();
	private static Map<Long, Profile> profiles = new HashMap<>();
	
	public static Map<Long, Message> getMessages(){
		return message;
	}
	
	public static Map<Long, Profile> getProfiles(){
		return profiles;
	}

}
