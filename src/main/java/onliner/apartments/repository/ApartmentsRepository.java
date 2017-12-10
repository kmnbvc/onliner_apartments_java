package onliner.apartments.repository;

import onliner.apartments.model.Apartment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApartmentsRepository extends CrudRepository<Apartment, Long> {
    @Query("SELECT a FROM Apartment a ORDER BY a.updated DESC")
    List<Apartment> getAll();

    @Query("SELECT a FROM Apartment a WHERE a.active = TRUE AND a.ignored = FALSE ORDER BY a.updated DESC")
    List<Apartment> getActive();

    @Query("SELECT a FROM Apartment a WHERE a.favorite = TRUE ORDER BY a.updated DESC")
    List<Apartment> getFavorites();

    @Query("SELECT a FROM Apartment a WHERE a.ignored = TRUE ORDER BY a.updated DESC")
    List<Apartment> getIgnored();

    @Query("SELECT a FROM Apartment a, Filter f WHERE f.name = :filter " +
            "AND a.ignored = FALSE " +
            "AND (f.from IS NULL OR a.updated >= f.from) " +
            "AND (f.active IS NULL OR f.active = 'ALL' OR (f.active = 'ACTIVE_ONLY' AND a.active = TRUE) OR (f.active = 'INACTIVE_ONLY' AND a.active = FALSE)) " +
            "AND (f.source IS NULL OR a.source = f.source) " +
            "AND (f.owner IS NULL OR f.owner = 'ANY' OR (f.owner = 'OWNER' AND a.contact.owner = TRUE) OR (f.owner = 'NOT_OWNER' AND a.contact.owner = FALSE)) " +
            "ORDER BY a.updated DESC ")
    List<Apartment> find(@Param("filter") String filter);
}
