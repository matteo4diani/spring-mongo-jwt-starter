package com.sashacorp.springmongojwtapi.util.log;

import com.sashacorp.springmongojwtapi.util.log.emoji.Emoji;

/**
 * Utility class to format logs
 * @author matteo
 *
 */
public class Log {
	
	/**
	 * If  Emoji is Emoji.ROBOT you get a message like "🤖 your message here"
	 * @param emoji
	 * @param success
	 * @param message
	 * @return
	 */
	public static String msg(Emoji emoji, String message) {
		return emoji.emj + " " + message;
	}
	/**
	 * If success is true and Emoji is Emoji.ROBOT you get a string like "🤖 ✔️ your message here"<br/>
	 * If success is false and Emoji is Emoji.ROBOT you get a string like "🤖 ❌ your message here"
	 * @param emoji
	 * @param success
	 * @param message
	 * @return
	 */
	public static String msg(Emoji emoji, boolean success, String message) {
		return emoji.emj + " " + (success ? Emoji.CHECK.emj : Emoji.CROSS.emj) + " " + message;
	}
}
