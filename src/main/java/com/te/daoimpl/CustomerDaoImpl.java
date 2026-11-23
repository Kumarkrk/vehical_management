package com.te.daoimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.te.dao.CustomerDao;
import com.te.model.Customer;
@Repository
public class CustomerDaoImpl implements CustomerDao {
	@Autowired
    HibernateTemplate hibernateTemplate;
    
	@Override
	public void addCustomer(Customer customer) {
		hibernateTemplate.save(customer);
		
	}

	@Override
	public void deleteCustomer(int id) {
		Customer customer=hibernateTemplate.get(Customer.class, id);
		try {
		if(customer==null)
		{
			throw new Exception("customer not found with this id");
		}
		else
		{
			hibernateTemplate.delete(customer);
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
	}

	@Override
	public void updateCustomer(Customer customer) {
		 hibernateTemplate.update(customer);
		
	}

	@Override
	public List<Customer> getAllCustomers() {
		List<Customer> list=hibernateTemplate.loadAll(Customer.class);
		
		return list;
	}

	

	@Override
	public Customer getCustomerById(int id) {
	    return hibernateTemplate.get(Customer.class, id);
	}
    @Override
	public List<Customer> searchCustomer(String name, String address, String mobileNo) {
		
    	
    	
    	StringBuilder hql = new StringBuilder("FROM Customer c WHERE 1=1");

        List<String> paramNames = new ArrayList<>();
        List<Object> paramValues = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            hql.append(" AND c.name LIKE :name");
            paramNames.add("name");
            paramValues.add("%" + name.trim() + "%");
        }

        if (address != null && !address.isBlank()) {
            hql.append(" AND c.address LIKE :address");
            paramNames.add("address");
            paramValues.add("%" + address.trim() + "%");
        }

        if (mobileNo != null && !mobileNo.isBlank()) {
            hql.append(" AND c.mobileNo LIKE :mobileNo");
            paramNames.add("mobileNo");
            paramValues.add(mobileNo.trim());
        }
        if (paramNames.isEmpty()) {
            return hibernateTemplate.loadAll(Customer.class);
        }


        return (List<Customer>) hibernateTemplate.findByNamedParam(
            hql.toString(),
            paramNames.toArray(new String[0]),
            paramValues.toArray()
        );
		
		
	}

}
