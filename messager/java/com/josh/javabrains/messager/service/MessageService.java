package com.josh.javabrains.messager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.josh.javabrains.messager.database.Database;
import com.josh.javabrains.messager.model.Message;

public class MessageService {
	
	private static Map<Long, Message> messages = Database.getMessages();
	
	public MessageService() {
		
		messages.put(1L, new Message(1,"Hello World","Joshua Aruno"));
		messages.put(2L, new Message(2,"Hello Jersy","Joshua Aruno"));
		messages.put(3L, new Message(3,"HI Eclipse","Joshua Aruno"));
	}
	
	public List<Message> getAllMessages(){
		return new ArrayList<Message>(messages.values());
	}
	
	public Message getMessage(long id){
		return messages.get(id);
	}
	
	public Message addMessage(Message message) {
		message.setId(messages.size() + 1);
		messages.put(message.getId(), message);
		return message;
	}
	
	public Message updateMessage(Message message) {
		if(message.getId() <= 0) {
			return null;
		}
		messages.put(message.getId(), message);
		return message;
	}
	
	public Message removeMessage(long id) {
		return messages.remove(id);
	}

}
