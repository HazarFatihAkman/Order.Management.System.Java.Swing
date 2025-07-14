package domain.repositories;

import domain.models.Customer;
import java.util.List;
import java.util.UUID;

public interface CustomerRepository {
    String prefix = "customers";

    boolean save(Customer customer);
    boolean update(Customer customer);
    Customer getCustomerById(UUID id);
    List<Customer> getCustomers();
}
