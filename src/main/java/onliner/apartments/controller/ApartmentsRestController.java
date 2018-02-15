package onliner.apartments.controller;

import onliner.apartments.model.Apartment;
import onliner.apartments.repository.ApartmentsRepository;
import onliner.apartments.service.ApartmentsLoader;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentsRestController extends RepositoryRestController<Apartment, Long> {

    private ApartmentsRepository apartmentsRepository;
    private ApartmentsLoader apartmentsLoader;
    private Map<String, Supplier<List<Apartment>>> predefinedFilters = new HashMap<>();

    public ApartmentsRestController(ApartmentsRepository apartmentsRepository, ApartmentsLoader apartmentsLoader) {
        super(apartmentsRepository);
        this.apartmentsLoader = apartmentsLoader;
        this.apartmentsRepository = apartmentsRepository;
        initPredefinedFilters();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/new")
    public List<Apartment> getNewApartments() {
        return apartmentsLoader.loadNew();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/search/{filter}")
    public List<Apartment> search(@PathVariable String filter) {
        if (predefinedFilters.containsKey(filter)) {
            return predefinedFilters.get(filter).get();
        }

        return apartmentsRepository.find(filter);
    }

    private void initPredefinedFilters() {
        predefinedFilters.put("all", apartmentsRepository::getAll);
        predefinedFilters.put("active", apartmentsRepository::getActive);
        predefinedFilters.put("ignored", apartmentsRepository::getIgnored);
        predefinedFilters.put("favorites", apartmentsRepository::getFavorites);
    }
}
