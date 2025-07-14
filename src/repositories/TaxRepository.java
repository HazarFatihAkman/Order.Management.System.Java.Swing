package repositories;

import domain.models.Tax;
import java.util.List;
import java.util.UUID;

public interface TaxRepository {
    String prefix = "taxes";

    boolean save(Tax tax);
    boolean update(Tax tax);
    Tax getTaxByName(String name);
    Tax getTaxById(UUID id);
    List<Tax> getTaxes();
}
