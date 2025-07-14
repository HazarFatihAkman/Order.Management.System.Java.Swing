package services;

import domain.models.Customer;
import domain.repositories.CustomerRepository;
import java.util.List;
import java.util.UUID;

public class CustomerService {
    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public Customer create(Customer customer) {
        if (customer == null) {
            System.out.println("Customer cannot be null.");
            return null;
        }

        if (repo.save(customer)) {
            System.out.println("Customer saved.");
        }
        else {
            System.out.println("Customer unsaved.");
        }

        return customer;
    }

    public Customer update(Customer customer) {
        if (customer == null) {
            System.out.println("Customer cannot be null.");
            return null;
        }

        if (repo.getCustomerById(customer.getId()) != null) {
            if (repo.update(customer)) {
                System.out.println("Customer updated.");
            }
            else {
                System.out.println("Customer not updated.");
            }
        }

        return customer;
    }

    public Customer getCustomerById(UUID id) {
        return repo.getCustomerById(id);
    }

    public List<Customer> getCustomers() {
        return repo.getCustomers();
    }
}
