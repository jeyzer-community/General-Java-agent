package com.hapiware.agent;

/*-
 * ---------------------------LICENSE_START---------------------------
 * Jeyzer Recorder
 * --
 * Copyright (C) 2020 - 2021 Jeyzer SAS
 * --
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 * ----------------------------LICENSE_END----------------------------
 */

public class BootLogger {
	
	public static final String PROPERTY_JEYZER_AGENT_BOOT_DEBUG = "jeyzer.agent.boot.debug";
	
	public static final String LOGGER_BOOT_DEBUG_PREFIX = "  Jeyzer agent boot debug - ";
	public static final String LOGGER_BOOT_WARNING_PREFIX = "  Jeyzer agent boot warning - ";
	public static final String LOGGER_BOOT_ERROR_PREFIX = "  Jeyzer agent boot error - ";
	
	private static boolean bootDebug = Boolean.parseBoolean(System.getProperty(PROPERTY_JEYZER_AGENT_BOOT_DEBUG));
	
	public static void debug(String message) {
		if (bootDebug)
			System.out.println(LOGGER_BOOT_DEBUG_PREFIX + message);
	}
	
	public static void warning(String message) {
		System.out.println(LOGGER_BOOT_WARNING_PREFIX + message);
	}
	
	public static void error(String message, Exception ex) {
		System.err.println(LOGGER_BOOT_ERROR_PREFIX + message);
		ex.printStackTrace();
	}
	
	public static void error(String message) {
		System.err.println(LOGGER_BOOT_ERROR_PREFIX + message);
	}

}
