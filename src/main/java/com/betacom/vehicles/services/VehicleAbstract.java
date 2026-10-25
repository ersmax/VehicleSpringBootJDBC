package com.betacom.vehicles.services;

import java.time.Year;
import java.util.List;
import java.util.Map;

import com.betacom.vehicles.models.Vehicles;
import com.betacom.vehicles.utils.Validation;

public abstract class VehicleAbstract {

	public static final List<String> FUEL_TYPES = List.of("petrol", "diesel", "lpg", "methane", "electric", "hybrid", "manual");
	public static final List<String> CATEGORIES = List.of("road", "citycar", "utility", "sedan", "suv", "sports", "offroad", "racing");
	public static final List<String> COLORS     = List.of("white", "black", "grey", "silver", "red", "blue", "green", "yellow", "orange", "brown");

	/**
	 * Fill in common attributes for all vehicles type
	 * @param vehicle	 : the specific object of vehicle for which we add common attributes
	 * @param type		 : the type of vehicle
	 * @param parameters : the list of attributes for the object
	 */
	protected static void fillProperties(Vehicles vehicle, String type, Map<String, String> parameters) {

		vehicle.setVehicleType(type);
		vehicle.setBrand(Validation.getString(parameters, "brand"));
		vehicle.setCategory(Validation.getOneOf(parameters, "category", CATEGORIES));
		vehicle.setColor(Validation.getOneOf(parameters, "color", COLORS));
		vehicle.setFuelType(Validation.getOneOf(parameters, "fuel", FUEL_TYPES));
		vehicle.setModel(Validation.getString(parameters, "model"));
		vehicle.setNumberWheels(Validation.getInt(parameters, "wheels"));
		vehicle.setProductionYear(Validation.getIntInRange(parameters, "year", Year.now().getValue() - 50, Year.now().getValue()));
	}


	/**
	 * Public: operation called from outside through reflection
	 * Add custom parameters to the specific vehicle type.
	 * @param parameters	: the pair key value of parameters
	 */
	public abstract void add(Map<String, String> parameters);
}
