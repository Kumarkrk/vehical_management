package com.te.daoimpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.te.dao.VehicleDao;
import com.te.model.VehicalStatus;
import com.te.model.Vehicle;
@Repository
public class VehicleDaoImpl implements VehicleDao{

	  @Autowired
	  private HibernateTemplate hibernateTemplate;
	  
	@Override
	public void addVehicle(Vehicle v) {
		hibernateTemplate.save(v);
		
	}

	
	@Override
	public void updateVehicle(Vehicle vehicle) {
	    hibernateTemplate.update(vehicle);
	}

	@Override
	public void deleteVehicle(int id) {
		  Vehicle vehicle = hibernateTemplate.get(Vehicle.class, id);

	        if (vehicle != null) {
	            hibernateTemplate.delete(vehicle);
	        }
	}

	@Override
	public List<Vehicle> getAllVehicles() {
		
		return hibernateTemplate.loadAll(Vehicle.class);
	}

	@Override
	public Vehicle getVehicleById(int id) {
		
		return hibernateTemplate.get(Vehicle.class, id);
	}


	
	@Override
	public Map<String, Long> getInventoryReport() {

	    Map<String, Long> report = new LinkedHashMap<>();

	    String hql = "SELECT COUNT(v) FROM Vehicle v";

	    Long total = hibernateTemplate.execute(
	            (org.springframework.orm.hibernate5.HibernateCallback<Long>) session ->
	                    session.createQuery(hql, Long.class).uniqueResult()
	    );

	    String statusHql =
	            "SELECT COUNT(v) FROM Vehicle v WHERE v.vehicleStatus = :status";

	    Long available = hibernateTemplate.execute(
	            (org.springframework.orm.hibernate5.HibernateCallback<Long>) session ->
	                    session.createQuery(statusHql, Long.class)
	                            .setParameter("status", VehicalStatus.AVAILABLE)
	                            .uniqueResult()
	    );

	    Long reserved = hibernateTemplate.execute(
	            (org.springframework.orm.hibernate5.HibernateCallback<Long>) session ->
	                    session.createQuery(statusHql, Long.class)
	                            .setParameter("status", VehicalStatus.RESERVED)
	                            .uniqueResult()
	    );

	    Long sold = hibernateTemplate.execute(
	            (org.springframework.orm.hibernate5.HibernateCallback<Long>) session ->
	                    session.createQuery(statusHql, Long.class)
	                            .setParameter("status", VehicalStatus.SOLD)
	                            .uniqueResult()
	    );

	    report.put("Total Vehicles", total);
	    report.put("Available Vehicles", available);
	    report.put("Reserved Vehicles", reserved);
	    report.put("Sold Vehicles", sold);

	    return report;
	}


	@Override
	public List<Vehicle> searchVehicle(int id, String brand, String model) {
		StringBuilder statement=new StringBuilder("from Vehicle as v where 1=1");
		 List<String> paramNames = new ArrayList<>();
		    List<Object> paramValues = new ArrayList<>();
		if(id>0)
		{
			statement.append(" And v.id=:id");
			paramNames.add("id");
	        paramValues.add(id);
		}
		if(brand!=null && !brand.isBlank())
		{
			statement.append(" And Lower(v.brand) LIKE:brand");
			 paramNames.add("brand");
			 paramValues.add("%" + brand.trim().toLowerCase() + "%");  
		        
		}
		if(model!=null && !model.isBlank())	
		{
			statement.append(" And LOWER(v.model) LIKE:model");
			  paramNames.add("model");
			  paramValues.add("%" + model.trim().toLowerCase() + "%");
		        
		}
		
		return (List<Vehicle>) hibernateTemplate.findByNamedParam(statement.toString(), paramNames.toArray(new String[0]), paramValues.toArray());
		
		
		
	}
}
