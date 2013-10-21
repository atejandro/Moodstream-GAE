package com.moodstream.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;
import com.moodstream.util.EMF;

@Api(name = "eventendpoint", namespace = @ApiNamespace(ownerDomain = "moodstream.com", ownerName = "moodstream.com", packagePath = "model"))
public class EventEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@ApiMethod(name = "listEvent")
	public CollectionResponse<Event> listEvent(
			@Nullable @Named("nickname") String nickname,
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Event> execute_creator = null;
		List<Event> execute_invitees=null;
		
		List<Event> execute_merged=null;

		try {
			mgr = getEntityManager();
			
			String querystr_creator="select e from Event e where e.creator= :nickname";
			String querystr_invitees="select e from Event e where e.invitees.contains(:nickname)";
			
			Query query_creator = mgr.createQuery(querystr_creator,Event.class);
			query_creator.setParameter("nickname", nickname);
			
			Query query_invitees = mgr.createQuery(querystr_invitees,Event.class);
			query_invitees.setParameter("nickname", nickname);
			//query.setParameter("nickname2", nickname);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query_creator.setHint(JPACursorHelper.CURSOR_HINT, cursor);
				query_invitees.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query_creator.setFirstResult(0);
				query_creator.setMaxResults(limit);
				
				query_invitees.setFirstResult(0);
				query_invitees.setMaxResults(limit);
			}

			execute_creator = (List<Event>) query_creator.getResultList();
			execute_invitees = (List<Event>) query_invitees.getResultList();
			
			cursor = JPACursorHelper.getCursor(execute_creator);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Event obj : execute_creator)
				;
			
			for (Event obj : execute_invitees)
				;
			
			execute_merged=new ArrayList(execute_creator);
			execute_merged.addAll(execute_invitees);
			
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Event> builder().setItems(execute_merged)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getEvent")
	public Event getEvent(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Event event = null;
		try {
			event = mgr.find(Event.class, id);
		} finally {
			mgr.close();
		}
		return event;
	}
	
	
	

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param event the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertEvent")
	public Event insertEvent(Event event) {
		EntityManager mgr = getEntityManager();
		
			mgr.persist(event);
		
			mgr.close();
		
		return event;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param event the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateEvent")
	public Event updateEvent(Event event) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsEvent(event)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(event);
		} finally {
			mgr.close();
		}
		return event;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeEvent")
	public void removeEvent(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Event event = mgr.find(Event.class, id);
			mgr.remove(event);
		} finally {
			mgr.close();
		}
	}
	
	
	@ApiMethod(name="inviteFriend")
	public void inviteFriend(@Named("id") Long id,@Named("invitee")String invitee)
	{
		EntityManager mgr=getEntityManager();
		
		try {
			Event event=mgr.find(Event.class, id);
			System.out.println("Event name: "+event.getEventName());
			List<String>invitees;
			if((invitees=event.getInvitees())==null)
				invitees=new ArrayList<String>();
			
			invitees.add(invitee);
			event.setInvitees(invitees);
			
			mgr.persist(event);		
		} finally{
			mgr.close();
		}
	}
	
	

	private boolean containsEvent(Event event) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Event item = mgr.find(Event.class, event.getKey());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
