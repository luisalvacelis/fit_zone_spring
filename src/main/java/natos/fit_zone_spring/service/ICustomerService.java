package natos.fit_zone_spring.service;

import natos.fit_zone_spring.model.Customer;

import java.util.List;

public interface ICustomerService {
    public List<Customer> listCustomer();

    public Customer seachCustomerByID(Integer customerID);

    public void saveCustomer(Customer customer);

    public void deleteCustomer(Customer customer);
}
