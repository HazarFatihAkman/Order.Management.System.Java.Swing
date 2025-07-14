package repositories;

import domain.models.Product;
import java.util.List;
import java.util.UUID;

public interface ProductRepository {
    String prefix = "products";

    boolean save(Product product);
    boolean update(Product product);
    boolean markIsDeleted(UUID id);
    List<Product> getProducts();
    Product getProductById(UUID id);
    Product getProductByName(String name);
}
