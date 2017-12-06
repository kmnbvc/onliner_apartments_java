package onliner.apartments.controller;

import onliner.apartments.binding.CollectionWrapper;
import onliner.apartments.model.Apartment;
import onliner.apartments.repository.ApartmentsRepository;
import onliner.apartments.service.ApartmentsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class ApartmentsRestController {

    private ApartmentsLoader apartmentsLoader;
    private ApartmentsRepository apartmentsRepository;

    @Autowired
    public ApartmentsRestController(ApartmentsLoader apartmentsLoader, ApartmentsRepository apartmentsRepository) {
        this.apartmentsLoader = apartmentsLoader;
        this.apartmentsRepository = apartmentsRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/apartments/new")
    @ResponseBody
    public ResponseEntity<?> getNewApartments() {
        List<Apartment> apartments = apartmentsLoader.loadNew();
        Resources<Apartment> resources = new Resources<>(apartments);
        resources.add(linkTo(methodOn(ApartmentsRestController.class).getNewApartments()).withSelfRel());
        return ResponseEntity.ok(resources);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/apartments/all")
    @ResponseBody
    public ResponseEntity<?> saveApartmentsList(@RequestBody CollectionWrapper<Apartment> wrapper, PersistentEntityResourceAssembler assembler) {
        Collection<Apartment> apartments = wrapper.getItems();
        Iterable<Apartment> result = apartmentsRepository.save(apartments);
        return ResponseEntity.ok(new Resources<>(result));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/apartments/all")
    public ResponseEntity<?> deleteAllApartments(PersistentEntityResourceAssembler assembler) {
        apartmentsRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

}
