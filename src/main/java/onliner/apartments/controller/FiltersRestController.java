package onliner.apartments.controller;

import onliner.apartments.model.Filter;
import onliner.apartments.repository.FiltersRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FiltersRestController extends RepositoryRestController<Filter, String> {
    public FiltersRestController(FiltersRepository repo) {
        super(repo);
    }
}
