package natos.fit_zone_spring.service;

import natos.fit_zone_spring.model.Customer;
import natos.fit_zone_spring.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> listCustomer() {
        List<Customer> list = customerRepository.findAll();
        return list;
    }

    @Override
    public Customer seachCustomerByID(Integer customerID) {
        return customerRepository.findById(customerID).orElse(null);
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }
}
