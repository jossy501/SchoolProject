package com.josh.javabrains.messager.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.josh.javabrains.messager.model.Message;
import com.josh.javabrains.messager.service.MessageService;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON )
public class MessageResource {
	
	MessageService messageService = new MessageService();
	
	
	@GET
	public List<Message> getMessages() {
		//return "Hello World!";
		return messageService.getAllMessages();
	}
	
	@POST
	public Message addMessage(Message message) {
		return messageService.addMessage(message);
	}
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") long messageId, Message message ) {
		message.setId(messageId);
		return messageService.updateMessage(message);
		
	}
	
	@DELETE
	@Path("/{messageId}")
	public Message deleteMessage(@PathParam("messageId") long messageId) {
		return messageService.removeMessage(messageId);
	}
	
	@GET
	@Path("/{messageId}")
	public Message test(@PathParam("messageId") long messageId) {
		return messageService.getMessage(messageId);
	}
	
	

}
