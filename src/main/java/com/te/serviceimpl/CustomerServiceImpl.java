package com.te.serviceimpl;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.te.daoimpl.CustomerDaoImpl;
import com.te.model.Customer;
import com.te.service.CustomerService;
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
	CustomerDaoImpl customerDao;
    private final Scanner sc = new Scanner(System.in);

	public CustomerServiceImpl() {
		
	}
	@Override
	@Transactional
	public void addCustomer(Customer customer) {
		System.out.println("you are choosed to enter the customer details");
		   System.out.println("You have chosen to enter customer details");

		    if (customer == null) {
		        throw new IllegalArgumentException("Customer cannot be null");
		    }

		    if (customer.getName() == null || customer.getName().isBlank()) {
		        throw new IllegalArgumentException("Name cannot be empty");
		    }

		    if (customer.getAddress() == null || customer.getAddress().isBlank()) {
		        throw new IllegalArgumentException("Address cannot be empty");
		    }

		    if (customer.getMobileNo() == null ||
		        !customer.getMobileNo().matches("[0-9]{10}")) {
		        throw new IllegalArgumentException("Enter a valid 10-digit mobile number");
		    }

		    customerDao.addCustomer(customer);
		
		
	}

	@Override
	@Transactional
	public void deleteCustomer(int id) {
		System.out.println("you are choosed to delete the customer");
		try
		{
			System.out.println("enter the customer id");
			int custId=id;
			
			
			customerDao.deleteCustomer(custId); 
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		
	}

	
		
		@Override
		@Transactional
		public void updateCustomer(Customer customerObj) throws Exception {

		    System.out.println("You have chosen to update customer details");

		    
	            
		        Customer customer = customerDao.getCustomerById(customerObj.getId());
		        if (customer == null) {
		            throw new Exception("Customer not found with ID: " + customerObj.getId());
		        }
		        
		        if(customerObj.getAddress()!=null && !customerObj.getAddress().isBlank())
		        {
		        	customer.setAddress(customerObj.getAddress());
		        }
		        if(customerObj.getMobileNo()!=null && !customerObj.getMobileNo().isBlank())
		        {
		        	customer.setMobileNo(customerObj.getMobileNo());
		        }
		        if(customerObj.getName()!=null && !customerObj.getMobileNo().isBlank())
		        {
		        	customer.setName(customerObj.getName());
		        }
		        
		        customerDao.updateCustomer(customer);
		        

		        
		        System.out.println("Customer updated successfully");

		    
		}
		
	

		@Override
		@Transactional(readOnly = true)
		public List<Customer> getCustomers() {

		    System.out.println("Customer Details:");

		    try {
		        List<Customer> customers = (List<Customer>) customerDao.getAllCustomers();

		        if (customers == null || customers.isEmpty()) {
		            System.out.println("No customer records found.");
		            return null;
		        }

		        return customers;

		    } catch (Exception e) {
		        System.out.println(e.getMessage());
		    }
		    return null;
		}
	@Override
	@Transactional(readOnly = true)
	public List<Customer> searchCustomer(String name, String address, String mobileNo) {

		System.out.println("You have chosen to search customer");

	    String custName = (name == null || name.isBlank())
	            ? null : name.trim();

	    String custAddress = (address == null || address.isBlank())
	            ? null : address.trim();

	    String custMobileNo = (mobileNo == null || mobileNo.isBlank())
	            ? null : mobileNo.trim();

	    if (custName == null &&
	        custAddress == null &&
	        custMobileNo == null) {

	        throw new IllegalArgumentException(
	                "Enter at least one search field");
	    }

	    List<Customer> customers = customerDao.searchCustomer(
	            custName, custAddress, custMobileNo);

	    return customers;
	}
	@Override
	@Transactional(readOnly=true)
	public Customer getCustomerById(int id) {
		 try {
		        System.out.println("Enter customer ID:");
		        Customer customer = customerDao.getCustomerById(id);
		       
		        if (customer == null) {
		            throw new Exception("Customer not found with ID: " + id);
		        }
		        return customer;
		 }
		 catch(Exception e)
		 {
			 System.out.println(e);
		 }
		return null;
	}

}
