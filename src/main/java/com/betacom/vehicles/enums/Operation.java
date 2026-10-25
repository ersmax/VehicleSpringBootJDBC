package com.betacom.vehicles.enums;


public enum Operation {
	ADD("addVehicle"),
	DELETE("deleteVehicle"),
	LIST("listVehicles"),
	EXPORT("exportVehicles"),
	IMPORT("importVehicles");
	
	private final String methodName;
	
	private Operation(String methodName) {
		this.methodName = methodName;
	}
	
	public String getMethodName() {
		return methodName;
	}
}
