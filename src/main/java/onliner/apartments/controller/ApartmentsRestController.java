package onliner.apartments.controller;

import onliner.apartments.binding.CollectionWrapper;
import onliner.apartments.model.Apartment;
import onliner.apartments.model.Source;
import onliner.apartments.repository.ApartmentsRepository;
import onliner.apartments.service.ApartmentsLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.support.RepositoryLinkBuilder;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class ApartmentsRestController {

    @Autowired
    private ApartmentsLoader apartmentsLoader;
    @Autowired
    private ApartmentsRepository apartmentsRepository;
    @Autowired
    private RepositoryRestMvcConfiguration configuration;

    @RequestMapping(method = RequestMethod.GET, value = "/apartments/new")
    @ResponseBody
    public ResponseEntity<?> getNewApartments() {
        List<Apartment> apartments = apartmentsLoader.loadNew();
        List<Resource> resources = apartments.stream()
                .map(apartment -> new Resource<>(apartment, linkToSource(apartment)))
                .collect(Collectors.toList());
        Resources<Resource> result = new Resources<>(resources);
        result.add(linkTo(methodOn(ApartmentsRestController.class).getNewApartments()).withSelfRel());
        return ResponseEntity.ok(result);
    }

    private Link linkToSource(Apartment apartment) {
        ResourceMetadata metadata = configuration.resourceMappings().getMetadataFor(Source.class);
        RepositoryLinkBuilder linkBuilder = new RepositoryLinkBuilder(metadata, configuration.baseUri());
        return linkBuilder.slash(apartment.getSource()).withRel("source");
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
