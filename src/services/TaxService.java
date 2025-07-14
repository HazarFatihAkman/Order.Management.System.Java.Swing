package services;

import domain.models.Tax;
import domain.repositories.TaxRepository;
import java.util.List;
import java.util.UUID;

public class TaxService {
    private final TaxRepository repo;

    public TaxService(TaxRepository repo) {
        this.repo = repo;
    }

    public Tax create(Tax tax) {
        if (tax == null) {
            System.out.println("Tax cannot be null.");
            return null;
        }

        if (repo.getTaxByName(tax.getName()) != null) {
            System.out.println("Tax already exists : " + tax.getName());
            return tax;
        }

        if (repo.save(tax)) {
            System.out.println("Tax saved.");
            return tax;
        }
        else {
            System.out.println("Tax unsaved.");
            return null;
        }
    }

    public Tax update(Tax tax) {
        if (tax == null) {
            System.out.println("Tax cannot be null.");
            return null;
        }

        if (repo.getTaxById(tax.getId()) != null) {
            if (repo.update(tax)) {
                System.out.println("Tax updated : " + tax.getName());
            }
            else {
                System.out.println("Tax not updated : " + tax.getName());
            }
        }

        return tax;
    }

    public Tax getTaxByName(String name) {
        return repo.getTaxByName(name);
    }

    public Tax getTaxById(UUID id) {
        return repo.getTaxById(id);
    }

    public List<Tax> getTaxes() {
        return repo.getTaxes();
    }
}
