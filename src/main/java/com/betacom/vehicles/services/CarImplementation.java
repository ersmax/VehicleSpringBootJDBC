package com.betacom.vehicles.services;

import java.util.Map;

import com.betacom.vehicles.exception.CustomException;
import com.betacom.vehicles.models.Car;
import com.betacom.vehicles.singleton.VehicleRegistry;
import com.betacom.vehicles.utils.Validation;

public class CarImplementation extends VehicleAbstract {
	
	// formats: car AA123AA, motorbike AA12345
	public static final String CAR_PLATE = "[A-Z]{2}[0-9]{3}[A-Z]{2}";
	
	@Override
	public void add(Map<String, String> parameters) {
		Car car = new Car();
		VehicleAbstract.fillProperties(car, "car", parameters);
		car.setCc(Validation.getIntInRange(parameters, "cc", 50, 8000));
		car.setNumberDoors(Validation.getIntInRange(parameters, "doors", 2, 7));
		car.setPlate(Validation.getPlate(parameters, "plate", CAR_PLATE));
		
		if (car.getNumberWheels() < 3)
			throw new CustomException("A car must have at least 3 wheels");
		if (car.getFuelType().equalsIgnoreCase("manual"))
			throw new CustomException("A car cannot run without fuel");
		
		VehicleRegistry.getSingleton().addVehicle(car, car.getPlate());
	}
}
