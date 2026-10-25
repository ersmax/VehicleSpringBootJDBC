package com.betacom.vehicles.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.betacom.vehicles.exception.CustomException;
import com.betacom.vehicles.models.Bicycle;
import com.betacom.vehicles.models.Bike;
import com.betacom.vehicles.models.Car;
import com.betacom.vehicles.models.Vehicles;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class VehiclesDAO {
	
	private final NamedParameterJdbcTemplate jdbcTemplate;
	
	
	private static final Map<String, String> FILTER_COLUMNS = Map.ofEntries(
			Map.entry("type",       "vt.description"),
			Map.entry("brand",      "bt.description"),
			Map.entry("model",      "v.model"),
			Map.entry("color",      "co.description"),
			Map.entry("fuel",       "ft.description"),
			Map.entry("category",   "ca.description"),
			Map.entry("plate",      "coalesce(c.plate, b.plate)"),
			Map.entry("year",       "v.production_year"),
			Map.entry("gears",      "v.number_gears"),
			Map.entry("cc",         "coalesce(c.cc, b.cc)"),
			Map.entry("doors",      "c.number_doors"),
			Map.entry("suspension", "bc.suspension"),
			Map.entry("foldable",   "bc.foldable"),
			Map.entry("wheels",     "v.number_wheels"));
	
	/*
	 * Filter name -> type of value: S = text (ignore case), I = integer, B = boolean (yes/no)
	 * public because it is also used by ListImplementation.validateFilters
	 */
	public static final Map<String, String> FILTERS = Map.ofEntries(
			Map.entry("type", "S"), Map.entry("brand", "S"), Map.entry("model", "S"),
			Map.entry("color", "S"), Map.entry("fuel", "S"), Map.entry("category", "S"),
			Map.entry("plate", "S"),
			Map.entry("year", "I"), Map.entry("gears", "I"), Map.entry("cc", "I"), 
			Map.entry("doors", "I"), Map.entry("wheels", "I"),
			Map.entry("suspension", "B"), Map.entry("foldable", "B"));	
	
	
	private static final String SELECT_VEHICLES = """
			select	v.id_vehicle, 
					vt.description as vehicle_type, 
					ft.description as fuel_type,
			       	ca.description as category, 
			       	co.description as color,
			       	bt.description as brand,
			       	v.number_wheels, v.number_gears, v.model, v.production_year,			       	
			       	c.plate as car_plate, 
			       	c.cc as car_cc, c.number_doors,
			       	b.plate as bike_plate, 
			       	b.cc as bike_cc,
			       	bc.suspension, bc.foldable
			from vehicles v
				join vehicle_types vt on vt.id_vehicle_type = v.id_vehicle_type
				join fuel_types ft    on ft.id_fuel_type    = v.id_fuel_type
				join categories ca    on ca.id_category     = v.id_category
				join colors co        on co.id_color        = v.id_color
				join brand_types bt   on bt.id_brand_type   = v.id_brand_type
					left join cars c      on c.id_vehicle  	= v.id_vehicle
					left join bikes b     on b.id_vehicle  	= v.id_vehicle
					left join bicycles bc on bc.id_vehicle 	= v.id_vehicle
			""";
	
	public List<Vehicles> findByFilters(Map<String, String> filters) {
		StringBuilder sql = new StringBuilder(SELECT_VEHICLES + " where 1 = 1");
		MapSqlParameterSource param = new MapSqlParameterSource();
		
		for (Map.Entry<String, String> entry : filters.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			String dbColumn = FILTER_COLUMNS.get(key);
			String type = FILTERS.get(key);	// String, Integer, Boolean
			
			if (dbColumn == null || type == null)
				throw new CustomException("Unknown filter: " + key);
			
			switch (type) {
				case "S" -> {
					sql.append(" and lower(").append(dbColumn).append(") = lower(:").append(key).append(")");
					param.addValue(key, value);
				}
				case "I" -> {
					sql.append(" and ").append(dbColumn).append(" = :").append(key);
					param.addValue(key, Integer.valueOf(value));
				}
				case "B" -> {
					sql.append(" and ").append(dbColumn).append(" = :").append(key);
					param.addValue(key, value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true"));
				}
			}
		}
		sql.append(" order by v.id_vehicle");
		log.debug("Query: {}", sql);
		
		return jdbcTemplate.query(sql.toString(), param, rs -> {
			List<Vehicles> vehicles = new ArrayList<Vehicles>();
			while (rs.next())
				vehicles.add(mapVehicle(rs));
			return vehicles;
		});
	}	
	
	private Vehicles mapVehicle(ResultSet rs) throws SQLException {
		String type = rs.getString("vehicle_type");
		
		Vehicles vehicle = switch (type) {
			case "car" -> Car.builder()
							 .plate(rs.getString("car_plate"))
							 .cc(rs.getInt("car_cc"))
							 .numberDoors(rs.getInt("number_doors"))
							 .build();
			case "bike" -> Bike.builder()
							   .plate(rs.getString("bike_plate"))
							   .cc(rs.getInt("bike_cc"))
							   .build();
			case "bicycle" -> Bicycle.builder()
									 .suspensions(rs.getBoolean("suspension"))
									 .foldable(rs.getBoolean("foldable"))
									 .build();
			default -> throw new CustomException("Unknown vehicle type in database: " + type);
		};
		
		// common attributes (parent table)
		vehicle.setId(rs.getInt("id_vehicle"));
		vehicle.setVehicleType(type);
		vehicle.setFuelType(rs.getString("fuel_type"));
		vehicle.setCategory(rs.getString("category"));
		vehicle.setColor(rs.getString("color"));
		vehicle.setNumberWheels(rs.getInt("number_wheels"));
		vehicle.setNumberGears(rs.getObject("number_gears", Integer.class));
		vehicle.setBrand(rs.getString("brand"));
		vehicle.setModel(rs.getString("model"));
		vehicle.setProductionYear(rs.getInt("production_year"));
		return vehicle;		
	}
	
}
