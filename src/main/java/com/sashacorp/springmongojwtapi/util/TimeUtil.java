package com.sashacorp.springmongojwtapi.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Utility class to get {@link LocalDateTime} instances in a set timezone ({@link ZoneId} = 'Europe/Rome')
 * @author matteo
 *
 */
public class TimeUtil {
	
	private static final ZoneId zid = ZoneId.of("Europe/Rome");
	
	public static ZoneId getZoneId() {
		return zid;
	}
	/**
	 * Get the current time in the 'Europe/Rome' time-zone.
	 * @return
	 */
	public static LocalDateTime now() {
		return ZonedDateTime.now(zid).toLocalDateTime();
	}
}
