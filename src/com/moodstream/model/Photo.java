package com.moodstream.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;


@Entity
public class Photo {

	@Id
	private String blobPath;
	private String ownerNickname;
	private Long eventId;
	private String caption;
	private long uploadTime;
	
	
	public String getBlobPath() {
		return blobPath;
	}
	public void setBlobPath(String blobPath) {
		this.blobPath = blobPath;
	}

	public String getOwnerNickname() {
		return ownerNickname;
	}
	public void setOwnerNickname(String ownerNickname) {
		this.ownerNickname = ownerNickname;
	}
	
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public long getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}
}
