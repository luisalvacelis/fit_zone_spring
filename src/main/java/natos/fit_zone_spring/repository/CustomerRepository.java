package natos.fit_zone_spring.repository;

import natos.fit_zone_spring.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
