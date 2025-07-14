package services;

import domain.models.Product;
import domain.repositories.ProductRepository;
import java.util.List;
import java.util.UUID;

public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public Product create(Product product) {
        if (product == null) {
            System.out.println("Product cannot be null.");
            return null;
        }

        if (repo.getProductByName(product.getName()) != null) {
            System.out.println("Product already exists : " + product.getName());
            return product;
        }

        if (repo.save(product)) {
            System.out.println("Product saved.");
        }
        else {
            System.out.println("Product unsaved.");
        }

        return product;
    }

    public Product update(Product product) {
        if (product == null) {
            System.out.println("Product cannot be null.");
            return null;
        }

        if (repo.getProductById(product.getId()) != null) {
            if (repo.update(product)) {
                System.out.println("Product updated.");
            }
            else {
                System.out.println("Product not updated.");
            }
        }

        return product;
    }

    public boolean markIsDeleted(UUID id) {
        return repo.markIsDeleted(id);
    }

    public List<Product> getProducts() {
        return repo.getProducts();
    }

    public Product getProductById(UUID id) {
        return repo.getProductById(id);
    }

    public Product getProductByName(String name) {
        return repo.getProductByName(name);
    }
}
