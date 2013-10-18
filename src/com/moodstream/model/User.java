package com.moodstream.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class User {
	
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	//private Key key;
	private String nickname;
	private String name;
	private String lastName;
	private String email;
	
	private List<String> friends;
	
	/*public void setKey(Key key) {
		this.key = key;
	}*/

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	/*public Key getKey() {
		return key;
	}*/
	
	public void addFriend(String friendNickname)
	{
		friends.add(friendNickname);
	}

}
