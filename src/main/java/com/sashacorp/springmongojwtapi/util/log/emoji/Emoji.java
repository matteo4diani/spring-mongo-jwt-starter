package com.sashacorp.springmongojwtapi.util.log.emoji;

/**
 * Utility to centralize emojis and generation of emoji-decorated logs
 * @author matteo
 *
 */
public enum Emoji {
	ROBOT("🤖"), TRAFFICLIGHT("🚦"), CALENDAR("🗓️"), COFFEE("☕"), CHECK("✔️"), CROSS("❌");
	public final String emj;
	Emoji(String emj) {
		this.emj = emj;
	}
	
}
