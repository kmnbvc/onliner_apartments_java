package onliner.apartments.repository;

import onliner.apartments.model.Source;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SourcesRepository extends CrudRepository<Source, String> {
    @Query("SELECT s FROM Source s WHERE s.active = true")
    List<Source> getActiveSources();
}
