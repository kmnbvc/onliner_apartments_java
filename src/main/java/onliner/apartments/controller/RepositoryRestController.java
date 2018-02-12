package onliner.apartments.controller;

import onliner.apartments.binding.CollectionWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;

public class RepositoryRestController<T, ID extends Serializable> {

    private CrudRepository<T, ID> repo;

    public RepositoryRestController(CrudRepository<T, ID> repo) {
        this.repo = repo;
    }

    @RequestMapping
    @ResponseBody
    public List<T> listAll() {
        List<T> result = new ArrayList<>();
        repo.findAll().forEach(result::add);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Map<String, Object> create(@RequestBody T json) {
        T created = this.repo.save(json);

        Map<String, Object> m = new HashMap<>();
        m.put("success", true);
        m.put("created", created);
        return m;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public T get(@PathVariable ID id) {
        return repo.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Map<String, Object> update(@PathVariable ID id, @RequestBody T json) {
        T entity = repo.findOne(id);
        BeanUtils.copyProperties(entity, json);
        T updated = repo.save(entity);

        Map<String, Object> m = new HashMap<>();
        m.put("success", true);
        m.put("id", id);
        m.put("updated", updated);
        return m;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/all")
    @ResponseBody
    public List<T> saveApartmentsList(@RequestBody CollectionWrapper<T> wrapper) {
        Iterable<T> updated = repo.save(wrapper.getItems());
        List<T> result = new ArrayList<>();
        updated.forEach(result::add);
        return result;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> delete(@PathVariable ID id) {
        repo.delete(id);
        Map<String, Object> m = new HashMap<>();
        m.put("success", true);
        return m;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/all")
    @ResponseBody
    public Map<String, Object> deleteAll() {
        repo.deleteAll();
        Map<String, Object> m = new HashMap<>();
        m.put("success", true);
        return m;
    }
}
