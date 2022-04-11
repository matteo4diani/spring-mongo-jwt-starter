package com.sashacorp.springmongojwtapi.util.log.test;

/**
 * Utility class to format test logs
 * @author matteo
 *
 */
public class Log {
	public static String msg(Emoji emoji, String message) {
		return emoji.emj + " " + message;
	}
	public static String msg(Emoji emoji, boolean success, String message) {
		return emoji.emj + " " + (success ? Emoji.CHECK.emj : Emoji.CROSS.emj) + " " + message;
	}
}
