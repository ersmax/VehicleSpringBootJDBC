package com.betacom.vehicles.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;
import com.betacom.vehicles.models.Vehicles;
import com.betacom.vehicles.singleton.VehicleRegistry;
import com.betacom.vehicles.VehiclesJdbCspringBootApplication;

@Slf4j
public class JsonTransfer {

	public static ObjectMapper jacksonMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
	
	private static List<Vehicles> registry = VehicleRegistry.getSingleton().getVehicleRegistry();
	
	/**
	 * Export the vehicle list into a JSON file
	 * @throws IOException
	 */
	public static void exportVehicles() throws IOException {
		
		// object TO json (writeFor keeps the @type of each vehicle, car, bike, bicycle)
		// writerFor(...) prepares a writer configured for List<Vehicles>.
		// writeValueAsString(registry) uses that writer on the data.
		String json = jacksonMapper.writerFor(new TypeReference<List<Vehicles>>() {})
								   .writeValueAsString(registry);
		
		FileManager.writeFile(VehiclesJdbCspringBootApplication.JSON_FILE, List.of(json), false);
		
		log.info("Exported {} vehicle(s) to {}", registry.size(), VehiclesJdbCspringBootApplication.JSON_FILE);
		log.debug("\n{}", json);		
	}
	
	/**
	 * Import vehicles from the JSON file into memory
	 * @throws IOException
	 */
	public static void importVehicles() throws IOException {
		
		String json = Files.readString(Path.of(VehiclesJdbCspringBootApplication.JSON_FILE));
		
		// json TO List: Jackson creates a list of Vehicles: Car, Bike, Bicycle from property = "vehicleType".
		// readValue(text, type) means: "read this JSON text and turn it into objects of this type." 
		// TypeReference carries the type
		// The {} creates an anonymous subclass of it. 
		// Java does keep generic types in a subclass's declaration, so Jackson can read the full type List<Vehicles> from it.
		// Without it, Jackson creates a list of generics
		List<Vehicles> imported = jacksonMapper.readValue(json, new TypeReference<List<Vehicles>>() {});
		
		VehicleRegistry.getSingleton().loadVehiclesFromJson(imported);
		
		log.info("Imported {} vehicle(s) from {}", imported.size(), VehiclesJdbCspringBootApplication.JSON_FILE);
		imported.forEach(vehicle -> log.debug("Imported: {}", vehicle));		
	}
	
}
