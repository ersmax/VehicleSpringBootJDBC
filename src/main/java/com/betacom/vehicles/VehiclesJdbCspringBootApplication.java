package com.betacom.vehicles;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.betacom.vehicles.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import com.betacom.vehicles.VehiclesJdbCspringBootApplication;

@Slf4j
//@SpringBootApplication
public class VehiclesJdbCspringBootApplication {

	public static final String INPUT_FILE 	= "data/readData.txt";
	public static final String OUTPUT_FILE 	= "data/writeData.txt";
	public static final String JSON_FILE	= "data/vehicles.json";

	// dynamic call: the compiler doesn't know this class is used. It's only found at runtime.
	private static final String PROCESS = "com.betacom.vehicles.process.StartVehicle";

	public static void main(String[] args) {
		log.info("Start Vehicles");

		try {
			Object process = loadProcess(PROCESS);
			executeOperation(process);
			log.info("End Main Vehicles");
		} catch (Exception e) {
			log.error("Error found: {}", e.getMessage());
		}
	}

	private static Object loadProcess(String className) throws Exception {
		try {
			Class<?> myClass = Class.forName(className);	// load class
			Object object = myClass.getDeclaredConstructor().newInstance();
			return object;
		} catch (ClassNotFoundException e) {
			throw new Exception("Process not found: " + className);
		}
	}

	private static void executeOperation(Object process) {
		try {
			// dynamic call: find the method by name, then invoke on the object
			Method method = process.getClass().getMethod("execute");
			method.invoke(process);

		} catch (SecurityException e) {			// a security policy blocks reflection
			throw new CustomException("Security error: " + e.getMessage());
		} catch (IllegalAccessException e) {	// execute exists but isn't public
			throw new CustomException("Illegal access: " + e.getMessage());
		} catch (IllegalArgumentException e) {	// wrong arguments passed to invoke
			throw new CustomException("Illegal argument: " + e.getMessage());
		} catch (NoSuchMethodException e) {		// no public execute() in the class
			throw new CustomException("execute method not found");

		} catch (InvocationTargetException e) {
			// execute() ran and threw an error itself; the real one is in getCause()
			throw new CustomException(e.getCause().getMessage());
		}
	}

}
