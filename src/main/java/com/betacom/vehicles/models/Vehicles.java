package com.betacom.vehicles.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, 	// make Jackson write the type name of each object into the JSON as a type field
			  include = JsonTypeInfo.As.EXISTING_PROPERTY,
			  property = "vehicleType",
			  visible = true)		
@JsonSubTypes({
	@JsonSubTypes.Type(value = Car.class,     name = "car"),		// maps each name to its class, so on import type:car becomes new Car()
	@JsonSubTypes.Type(value = Bike.class,    name = "bike"),
	@JsonSubTypes.Type(value = Bicycle.class, name = "bicycle")
})
public class Vehicles {
	private Integer id;				// unique id of object
	private String 	vehicleType;	// car, bike, bicycle
	private Integer numberWheels;	// depends on the vehicle
	private String 	fuelType;		// petrol, diesel, lpg, methane, electric, hybrid, manual
	private String 	category;		// road, citycar, utility, sedan, suv, sports, offroad, racing
	private String 	color;
	private String 	brand;
	private Integer productionYear;
	private String 	model;
}
