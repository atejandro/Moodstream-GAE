package com.moodstream.model;

import com.moodstream.util.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "checkinendpoint", namespace = @ApiNamespace(ownerDomain = "moodstream.com", ownerName = "moodstream.com", packagePath = "model"))
public class CheckinEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listCheckin")
	public CollectionResponse<Checkin> listCheckin(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<Checkin> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Checkin as Checkin");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<Checkin>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (Checkin obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Checkin> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getCheckin")
	public Checkin getCheckin(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Checkin checkin = null;
		try {
			checkin = mgr.find(Checkin.class, id);
		} finally {
			mgr.close();
		}
		return checkin;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param checkin the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertCheckin")
	public Checkin insertCheckin(Checkin checkin) {
		EntityManager mgr = getEntityManager();
		
			mgr.persist(checkin);
		
			mgr.close();
		
		return checkin;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param checkin the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateCheckin")
	public Checkin updateCheckin(Checkin checkin) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsCheckin(checkin)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(checkin);
		} finally {
			mgr.close();
		}
		return checkin;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeCheckin")
	public void removeCheckin(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Checkin checkin = mgr.find(Checkin.class, id);
			mgr.remove(checkin);
		} finally {
			mgr.close();
		}
	}

	private boolean containsCheckin(Checkin checkin) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			Checkin item = mgr.find(Checkin.class, checkin.getKey());
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
