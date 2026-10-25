package com.betacom.vehicles.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString (callSuper = true)
public class Car extends Vehicles {

	private String plate;	// must be unique
	private Integer cc;			
	private Integer numberDoors;
}
