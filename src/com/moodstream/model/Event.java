package com.moodstream.model;


import java.util.Date;
import java.util.List;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;


@Entity
public class Event {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Key key;
	
	private String eventName;
	private GeoPt location;
	private Date date;
	private String description;
	private String creator;
	
	List<String> invitees;
	
	
	public Key getKey() {
		return key;
	}
	public void setKey(Key key) {
		this.key = key;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public GeoPt getLocation() {
		return location;
	}
	public void setLocation(GeoPt location) {
		this.location = location;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public List<String> getInvitees() {
		return invitees;
	}
	public void setInvitees(List<String> invitees) {
		this.invitees = invitees;
	}	
	

}
