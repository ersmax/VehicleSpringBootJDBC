package com.betacom.vehicles.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString (callSuper = true)
public class Bike extends Vehicles {

	private String plate;
	private Integer cc;
}
