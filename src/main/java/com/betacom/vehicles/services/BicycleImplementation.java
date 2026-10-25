package com.betacom.vehicles.services;

import java.util.List;
import java.util.Map;

import com.betacom.vehicles.exception.CustomException;
import com.betacom.vehicles.models.Bicycle;
import com.betacom.vehicles.singleton.VehicleRegistry;
import com.betacom.vehicles.utils.Validation;

public class BicycleImplementation extends VehicleAbstract {

	public static final List<String> SUSPENSIONS = List.of("with", "without");
	
	@Override
	public void add(Map<String, String> parameters) {
		Bicycle bicycle = new Bicycle();
		VehicleAbstract.fillProperties(bicycle, "bicycle", parameters);
		bicycle.setNumberGears(Validation.getIntInRange(parameters, "gears", 1, 100));
		bicycle.setTypeSuspensions(Validation.getOneOf(parameters, "suspension", SUSPENSIONS));
		bicycle.setFoldable(Validation.getBoolean(parameters, "foldable"));
		
		if (!bicycle.getFuelType().equals("manual") && !bicycle.getFuelType().equals("electric"))
			throw new CustomException("A bicycle can only be electric or manual");
		
		if (bicycle.getNumberWheels() != 2)
			throw new CustomException("A bicycle can have only 2 wheels");
		
		VehicleRegistry.getSingleton().addVehicle(bicycle, null);
	}
}
