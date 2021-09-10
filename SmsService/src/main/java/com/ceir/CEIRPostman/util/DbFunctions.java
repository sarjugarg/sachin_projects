package com.ceir.CEIRPostman.util;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DbFunctions {

	private static final Logger logger = LogManager.getLogger(DbFunctions.class);

	private static String dateFunction;
	private static String dateFormat;

	public static String getDate(String dialect) {
		logger.debug("Get date functions for DB : " + dialect);

		if (Objects.isNull(dialect)) {
			logger.warn("Dialect can't be null.");
			dateFunction = null;
		}

		if (Objects.isNull(dateFunction)) {
			if (dialect.toLowerCase().contains("oracle")) {
				// dateFunction = "TO_DATE";
				dateFunction = "TO_CHAR";
			} else if (dialect.toLowerCase().contains("mysql")) {
				dateFunction = "STR_TO_DATE";
			} else {
				dateFunction = null;
			}
		}

		logger.debug("Date functions for DB : " + dialect + " is " + dateFunction);

		return dateFunction;
	}

	public static String getDateFormat(String dialect) {
		logger.debug("Get dateFormat functions for DB : " + dialect);

		if (Objects.isNull(dialect)) {
			logger.warn("Dialect can't be null.");
			dateFunction = null;
		}

		if (Objects.isNull(dateFormat)) {
			if (dialect.toLowerCase().contains("oracle")) {
				dateFormat = "YYYY-MM-DD";
			} else if (dialect.toLowerCase().contains("mysql")) {
				dateFormat = "%Y-%m-%d";
			} else {
				dateFormat = null;
			}
		}

		logger.debug("DateFormat functions for DB : " + dialect + " is " + dateFormat);

		return dateFormat;
	}

}
