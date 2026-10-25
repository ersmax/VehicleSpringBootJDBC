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
public class Bicycle extends Vehicles {
	
	private Boolean suspensions;	// with or without
	private Boolean foldable;
}
