package onliner.apartments.repository;

import onliner.apartments.model.Filter;
import org.springframework.data.repository.CrudRepository;

public interface FiltersRepository extends CrudRepository<Filter, String> {

}
