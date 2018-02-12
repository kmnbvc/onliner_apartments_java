package onliner.apartments.controller;

import onliner.apartments.model.Apartment;
import onliner.apartments.repository.ApartmentsRepository;
import onliner.apartments.service.ApartmentsLoader;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApartmentsRestController extends RepositoryRestController<Apartment,Long> {

    private ApartmentsLoader apartmentsLoader;

    public ApartmentsRestController(ApartmentsRepository apartmentsRepository, ApartmentsLoader apartmentsLoader) {
        super(apartmentsRepository);
        this.apartmentsLoader = apartmentsLoader;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apartments/new")
    @ResponseBody
    public List<Apartment> getNewApartments() {
        return apartmentsLoader.loadNew();
    }

}
