package onliner.apartments.controller;

import onliner.apartments.model.Source;
import onliner.apartments.repository.SourcesRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SourcesRestController extends RepositoryRestController<Source, String> {
    public SourcesRestController(SourcesRepository repo) {
        super(repo);
    }
}