package com.betacom.vehicles.utils;

import java.util.List;
import java.util.Map;

import com.betacom.vehicles.exception.CustomException;

public class Validation {

	/**
	 * Search the whole attributes of vehicle, and return the correct one
	 * @param parameters : the list of vehicle's attribute and their value
	 * @param key		 : the attribute of interest of vehicle
	 * @return			 : the value corresponding to the key
	 */
	public static String getString(Map<String, String> parameters, String key) {
		String value = parameters.get(key);
		if (value == null || value.isEmpty()) {
			throw new CustomException("Missing parameter: " + key);
		}
		return value;
	}

	/**
	 * Return a boolean associated to the key value 
	 * @param parameters	: the list of vehicle's attribute and their value 
	 * @param key			: the attribute of interest of vehicle
	 * @return				: return true/false depending on the existence of attribute
	 */
	public static boolean getBoolean(Map<String, String> parameters, String key) {
		String value = getString(parameters, key).toLowerCase();
		switch (value) {
		case "yes", "true":
			return true;
		case "no", "false":
			return false;
		default:
			throw new CustomException("Parameter " + key + " must be yes/no. "
									+ "Found instead a value (" + value + ")");
		}
	}
	
	/**
	 * Check if the value associated to the key is included in a fixed list
	 * @param parameters	: the list of vehicle's attribute and their value
	 * @param key			: the attribute of interest of vehicle
	 * @param allowedValues : a fixed list of allowed values to check upon
	 * @return				: the value if it is included in the list
	 */
	public static String getOneOf(Map<String, String> parameters, String key, List<String> allowedValues) {
		String value = getString(parameters, key).toLowerCase();
		if (!allowedValues.contains(value))
			throw new CustomException("Invalid " + key + " (" + value + "). "
									+ "Allowed values: " + allowedValues);
		return value;
	}

	/**
	 * Return the Integer value associated with the key
	 * @param parameters	: the list of vehicle's attribute and their value
	 * @param key			: the attribute of interest of vehicle
	 * @return				: the number if key is present and is a number
	 */
	public static Integer getInt(Map<String, String> parameters, String key) {
		String value = getString(parameters, key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new CustomException("Parameter " + key + " must be a number. "
									+ "Found instead a value (" + value + ")");
		}
	}

	/**
	 * Returns the number if it is between min and max (included), otherwise throws
	 * @param parameters	: the list of vehicle's attribute and their value
	 * @param key			: the attribute of interest of vehicle
	 * @param min
	 * @param max
	 * @return				: the number if it is included in the limits
	 */
	public static Integer getIntInRange(Map<String, String> parameters, String key, int min, int max) {
		Integer value = getInt(parameters, key);
		if (value < min || value > max) {
			throw new CustomException("Parameter " + key + " must be between " + min + " and " + max
									+ ". Found instead a value (" + value + ")");
		}
		return value;
	}


	/**
	 * Check the value associated to key against the pattern
	 * @param parameters	: the list of vehicle's attribute and their value
	 * @param key			: the attribute of interest of vehicle
	 * @param pattern		: e.g. AA123AA for car against the regex [A-Z]{2}[0-9]{3}[A-Z]{2}
	 * @return				: the value associated to the key if it matches the regex
	 */
	public static String getPlate(Map<String, String> parameters, String key, String pattern) {
		String value = getString(parameters, key).toUpperCase();
		if (!value.matches(pattern)) {
			throw new CustomException("Invalid plate format (" + value + ")");
		}
		return value;
	}
	
	/**
	 * Return the name of the class (e.g. car, implementation = CarImplementation)
	 * @param name
	 * @return
	 */
	public static String buildClassName(String prefix, String suffix) {
		return 	  prefix.substring(0, 1).toUpperCase()
				+ prefix.substring(1).toLowerCase()
				+ suffix.substring(0, 1).toUpperCase()
				+ suffix.substring(1).toLowerCase();
	}

}
