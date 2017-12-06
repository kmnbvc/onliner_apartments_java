package onliner.apartments.repository;

import onliner.apartments.model.Apartment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApartmentsRepository extends CrudRepository<Apartment, Long> {
    @Query("SELECT a FROM Apartment a")
    List<Apartment> getAll();

    @Query("SELECT a FROM Apartment a WHERE a.favorite = TRUE")
    List<Apartment> getFavorites();
}
