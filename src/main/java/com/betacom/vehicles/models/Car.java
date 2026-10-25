package com.betacom.vehicles.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString (callSuper = true)
public class Car extends Vehicles {

	private String plate;	// must be unique
	private Integer cc;			
	private Integer numberDoors;
}
