package com.sashacorp.springmongojwtapi.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;

/**
 * Utility class to get {@link LocalDateTime} instances in a set timezone
 * ({@link ZoneId} = 'Europe/Rome')
 * 
 * @author matteo
 *
 */

public class TimeUtil {

	@Value("${sashacorp.timezone}")
	private static String timezone;
	private static final ZoneId zoneId = ZoneId.of(timezone);
	public static ZoneId getZoneId() {
		return zoneId;
	}

	/**
	 * Get the current time in the 'Europe/Rome' time-zone.
	 * 
	 * @return
	 */
	public static LocalDateTime now() {
		return ZonedDateTime.now(getZoneId()).toLocalDateTime();
	}
}
