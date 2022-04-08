package com.sashacorp.springmongojwtapi.util;

import java.util.List;
import java.util.Map;

import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.models.persistence.user.Status;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.MessageRepository;

/**
 * Utility class to define status computation rules in a single place. Provides
 * an overloaded <i>setUserStatus</i> method fit for most use cases. Please keep
 * all status related logic in this class. Status computations depend on
 * {@link MessageRepository}'s definitions.
 * 
 * @author matteo
 *
 */
public class StatusUtil {

	/**
	 * Sets the user status based on his ongoing activity
	 * 
	 * @param messagesByUser
	 * @param user
	 */
	public static void setUserStatus(Map<String, List<Message>> messagesByUser, User user) {

		setUserStatus(null, false, messagesByUser, user);
	}

	public static void setUserStatus(MessageRepository messageRepository, User user) {

		setUserStatus(messageRepository, true, null, user);
	}

	/**
	 * Sets the user's status using a message source of choice.
	 * 
	 * @param messageRepository
	 * @param useRepository     - if <b>true</b> messageRepository will be used to
	 *                          fetch user messages
	 * @param messagesByUser
	 * @param user
	 */
	private static void setUserStatus(MessageRepository messageRepository, Boolean useRepository,
			Map<String, List<Message>> messagesByUser, User user) {
		/**
		 * Set only if user status is null or not hardcoded.
		 */
		if (user.getStatus() == null || (user.getStatus() != null && !user.getStatus().isHardcoded())) {

			List<Message> ongoingActivities = null;

			if (useRepository) {
				ongoingActivities = messageRepository
						.findOngoingByReqUsername(
								user.getUsername(), TimeUtil.now());
			} else {
				ongoingActivities = messagesByUser.get(user.getUsername());
			}

			StatusUtil.computeNonHardcodedUserStatus(ongoingActivities, user);

		}
	}

	public static void computeNonHardcodedUserStatus(List<Message> ongoingActivities, User user) {

		if (ongoingActivities == null || ongoingActivities.size() == 0) {
			/**
			 * user has no ongoing activities and no hardcoded status
			 */
			user.setStatus(new Status("ON", false));
		} else if (ongoingActivities.size() == 1) {
			/**
			 * user has an ongoing activity
			 */
			user.setStatus(new Status(ongoingActivities.get(0).getLeave(), false));
		} else {
			/**
			 * user has more than one ongoing activity. Their status is inconsistent.
			 */
			user.setStatus(new Status(Status.INCONSISTENT, false));
		}
	}

}
