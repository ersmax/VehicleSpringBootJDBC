package com.betacom.vehicles.services;

import java.util.Map;

import com.betacom.vehicles.exception.CustomException;
import com.betacom.vehicles.models.Bike;
import com.betacom.vehicles.singleton.VehicleRegistry;
import com.betacom.vehicles.utils.Validation;

public class BikeImplementation extends VehicleAbstract {

	// formats: car AA123AA, motorbike AA12345
	public static final String BIKE_PLATE = "[A-Z]{2}[0-9]{5}";
	
	@Override
	public void add(Map<String, String> parameters) {
		Bike bike = new Bike();
		VehicleAbstract.fillProperties(bike, "bike", parameters);
		bike.setPlate(Validation.getPlate(parameters, "plate", BIKE_PLATE));
		bike.setCc(Validation.getIntInRange(parameters, "cc", 50, 3000));		
		
		if (bike.getNumberWheels() != 2)
			throw new CustomException("A bike must have 2 wheels");
		if (bike.getFuelType().equals("manual"))
			throw new CustomException("A bike cannot run without fuel");
		
		VehicleRegistry.getSingleton().addVehicle(bike, bike.getPlate());
	}
}
