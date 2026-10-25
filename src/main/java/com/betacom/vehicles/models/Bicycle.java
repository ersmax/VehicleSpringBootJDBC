package com.betacom.vehicles.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString (callSuper = true)
public class Bicycle extends Vehicles {
	
	private Integer numberGears;
	private String 	typeSuspensions;	// with or without
	private Boolean foldable;
}
