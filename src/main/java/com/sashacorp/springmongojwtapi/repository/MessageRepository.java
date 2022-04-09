package com.sashacorp.springmongojwtapi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.util.TimeUtil;

/**
 * Repository interface for the {@link Message} entity. Query methods are named
 * according to this convention:
 * <ul>
 * <li>An Ongoing Message has start <= now < end AND has 'approved' set to
 * <b>true</b>, 'pending' set to <b>false</b>. MongoDB: start: $lte now, end:
 * $gt now</li>
 * <li>A Current Message has now < end. MongoDB: end: $gt now</li>
 * <li>An Outdated Message has end < now. MongoDB: end: $lte now</li>
 * 
 * @author Matteo Fordiani
 *
 */
public interface MessageRepository extends MongoRepository<Message, String> {

	/**
	 * Refer to {@link MessageRepository} for definitions
	 * 
	 * @param pending - <b>true</b> means the message has no responses yet
	 */
	@Query("{ 'pending' : ?0 }")
	public List<Message> findByPending(boolean pending);

	/**
	 * Refer to {@link MessageRepository} for definitions
	 * 
	 * @param approved - <b>true</b> means the request expressed in the message has
	 *                 been approved
	 */
	@Query("{ 'pending' : false , 'approved' : ?0 }")
	public List<Message> findByApproval(boolean approved);

	/**
	 * Find current messages. Refer to {@link MessageRepository} for definitions
	 * 
	 * @param now - used to pass the current time. Use {@link TimeUtil}'s now()
	 *            method to access the current time for the correct time-zone
	 *            (Europe/Rome).
	 */
	@Query("{ 'end' : { $gt : ?0 } }")
	public List<Message> findCurrent(LocalDateTime now);

	/**
	 * Find current messages by <b>pending</b>. Refer to {@link MessageRepository}
	 * for definitions
	 * 
	 * @param now     - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                method to access the current time for the correct time-zone
	 *                (Europe/Rome).
	 * @param pending - <b>true</b> means the message has no responses yet
	 */
	@Query("{ 'end' : { $gt : ?0 }, 'pending' : ?1 }")
	public List<Message> findCurrentByPending(LocalDateTime now, boolean pending);

	/**
	 * Find current messages by <b>approved</b>. Refer to {@link MessageRepository}
	 * for definitions
	 * 
	 * @param now      - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                 method to access the current time for the correct time-zone
	 *                 (Europe/Rome).
	 * @param approved - <b>true</b> means the request expressed in the message has
	 *                 been approved
	 */
	@Query("{ 'end' : { $gt : ?0 }, 'pending' : false, 'approved' : ?1 }")
	public List<Message> findCurrentByApproval(LocalDateTime now, boolean approved);

	/**
	 * Find ongoing messages. Refer to {@link MessageRepository} for definitions
	 * 
	 * @param now - used to pass the current time. Use {@link TimeUtil}'s now()
	 *            method to access the current time for the correct time-zone
	 *            (Europe/Rome).
	 */
	@Query("{ 'start' : { $lte : ?0 } , 'end' : { $gt : ?0 }, 'pending' : false, 'approved' : true }")
	public List<Message> findOngoing(LocalDateTime now);

	/**
	 * Find outdated messages. Refer to {@link MessageRepository} for definitions
	 * 
	 * @param now - used to pass the current time. Use {@link TimeUtil}'s now()
	 *            method to access the current time for the correct time-zone
	 *            (Europe/Rome).
	 */
	@Query("{ 'end' : { $lte : ?0 } }")
	public List<Message> findOutdated(LocalDateTime now);

	/**
	 * Find outdated messages by <b>pending</b>. Refer to {@link MessageRepository}
	 * for definitions
	 * 
	 * @param now     - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                method to access the current time for the correct time-zone
	 *                (Europe/Rome).
	 * @param pending - <b>true</b> means the message has no responses yet
	 */
	@Query("{ 'end' : { $lte : ?0 }, 'pending' : ?1 }")
	public List<Message> findOutdatedByPending(LocalDateTime now, boolean pending);

	/**
	 * Find outdated messages by <b>approved</b>. Refer to {@link MessageRepository}
	 * for definitions
	 * 
	 * @param now      - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                 method to access the current time for the correct time-zone
	 *                 (Europe/Rome).
	 * @param approved - <b>true</b> means the request expressed in the message has
	 *                 been approved
	 */
	@Query("{ 'end' : { $lte : ?0 }, 'pending' : false , 'approved' : ?1 }")
	public List<Message> findOutdatedByApproval(LocalDateTime now, boolean approved);

	/**
	 * Find by requester username. Refer to {@link MessageRepository} for definitions
	 * 
	 * @param username - username of the sender
	 */
	@Query("{ 'requester.username' : ?0 }")
	public List<Message> findByReqUsername(String username);

	/**
	 * Find by requester username and <b>pending</b>. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param username - username of the sender to access the current time for the
	 *                 correct time-zone (Europe/Rome).
	 * @param pending  - <b>true</b> means the message has no responses yet
	 */
	@Query("{ 'requester.username' : ?0 , 'pending' : ?1 }")
	public List<Message> findByReqUsernameAndPending(String username, boolean pending);

	/**
	 * Find by requester username and <b>approved</b>. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param username - username of the sender
	 * @param now      - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                 method to access the current time for the correct time-zone
	 *                 (Europe/Rome).
	 * @param approved - <b>true</b> means the request expressed in the message has
	 *                 been approved
	 */
	@Query("{ 'requester.username' : ?0 , 'pending' : false , 'approved' : ?1 }")
	public List<Message> findByReqUsernameAndApproval(String username, boolean approved);

	/**
	 * Find current messages by requester username. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param username - username of the sender
	 * @param now      - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                 method to access the current time for the correct time-zone
	 *                 (Europe/Rome).
	 */
	@Query("{ 'requester.username' : ?0 , 'end' : { $gt : ?1 } }")
	public List<Message> findCurrentByReqUsername(String username, LocalDateTime now);

	/**
	 * Find current messages by requester username and <b>pending</b>. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param username - username of the sender
	 * @param now      - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                 method to access the current time for the correct time-zone
	 *                 (Europe/Rome).
	 * @param pending  - <b>true</b> means the message has no responses yet
	 */
	@Query("{ 'requester.username' : ?0 , 'end' : { $gt : ?1 }, 'pending' : ?2 }")
	public List<Message> findCurrentByReqUsernameAndPending(String username, LocalDateTime now,
			boolean pending);

	/**
	 * Find current messages by requester username and <b>approved</b>. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param username - username of the sender
	 * @param now      - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                 method to access the current time for the correct time-zone
	 *                 (Europe/Rome).
	 * @param approved - <b>true</b> means the request expressed in the message has
	 *                 been approved
	 */
	@Query("{ 'requester.username' : ?0 , 'end' : { $gt : ?1 }, 'pending' : false , 'approved' : ?2 }")
	public List<Message> findCurrentByReqUsernameAndApproval(String username,
			LocalDateTime now, boolean approved);

	/**
	 * Find ongoing messages by requester username. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param username - username of the sender
	 * @param now      - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                 method to access the current time for the correct time-zone
	 *                 (Europe/Rome).
	 */
	@Query("{ 'requester.username' : ?0 , 'start' : { $lte : ?1 } , 'end' : { $gt : ?1 }, 'pending' : false , 'approved' : true }")
	public List<Message> findOngoingByReqUsername(
			String username, LocalDateTime now);

	/**
	 * Find outdated messages by requester username. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param username - username of the sender
	 * @param now      - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                 method to access the current time for the correct time-zone
	 *                 (Europe/Rome).
	 */
	@Query("{ 'requester.username' : ?0 , 'end' : { $lte : ?1 } }")
	public List<Message> findOutdatedReqUsername(String username, LocalDateTime now);

	/**
	 * Find outdated messages by requester username and <b>pending</b>. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param username - username of the sender
	 * @param now      - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                 method to access the current time for the correct time-zone
	 *                 (Europe/Rome).
	 * @param pending  - <b>true</b> means the message has no responses yet
	 */
	@Query("{ 'requester.username' : ?0 , 'end' : { $lte : ?1 } , 'pending' : ?2 }")
	public List<Message> findOutdatedByReqUsernameAndPending(String username, LocalDateTime now,
			boolean pending);

	/**
	 * Find outdated messages by requester username and <b>approved</b>. Refer to
	 * {@link MessageRepository} for definitions
	 * 
	 * @param username - username of the sender
	 * @param now      - used to pass the current time. Use {@link TimeUtil}'s now()
	 *                 method to access the current time for the correct time-zone
	 *                 (Europe/Rome).
	 * @param approved - <b>true</b> means the request expressed in the message has
	 *                 been approved
	 */
	@Query("{ 'requester.username' : ?0 , 'end' : { $lte : ?1 } , 'pending' : false , 'approved' : ?2 }")
	public List<Message> findApprovalByReqUsernameAndApproval(String username,
			LocalDateTime now, boolean approved);

	/**
	 * Find all approved messages with start or end between <b>from</b> and
	 * <b>to</b>
	 * 
	 * @param username
	 * @param from
	 * @param to
	 * @return
	 */
	@Query("{ 'approved' : true , $or : [ { 'start' : { $gte : ?0 , $lt : ?1 } } , { 'end' : { $gt : ?0 , $lte : ?1 } } ] }")
	public List<Message> findApprovedBetween(LocalDateTime from, LocalDateTime to);

	/**
	 * Find all approved messages with start or end between <b>from</b> and
	 * <b>to</b> by requester username
	 * 
	 * @param username
	 * @param from
	 * @param to
	 * @return
	 */
	@Query("{ 'requester.username' : ?0 , 'approved' : true , $or : [ { 'start' : { $gte : ?1 , $lt : ?2 } } , { 'end' : { $gt : ?1 , $lte : ?2 } } ] }")
	public List<Message> findApprovedBetweenByReqUsername(String username,
			LocalDateTime from, LocalDateTime to);

	/**
	 * Find all (approved or not) messages with start or end between <b>from</b> and
	 * <b>to</b> by requester username
	 * 
	 * @param username
	 * @param from
	 * @param to
	 * @return
	 */
	@Query("{ 'requester.username' : ?0 , $or : [ { 'start' : { $gte : ?1 , $lt : ?2 } } , { 'end' : { $gt : ?1 , $lte : ?2 } } ] }")
	public List<Message> findBetweenByReqUsername(String username, LocalDateTime from,
			LocalDateTime to);

	/**
	 * Find all approved messages with start or end between <b>from</b> and
	 * <b>to</b> by requester team
	 * 
	 * @param team
	 * @param from
	 * @param to
	 * @return
	 */
	@Query("{ 'requester.team' : ?0 , 'approved' : true , $or : [ { 'start' : { $gte : ?1 , $lt : ?2 } } , { 'end' : { $gt : ?1 , $lte : ?2 } } ] }")
	public List<Message> findApprovedBetweenByReqTeam(String team,
			LocalDateTime from, LocalDateTime to);
}
