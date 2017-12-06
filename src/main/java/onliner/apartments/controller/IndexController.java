package onliner.apartments.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @RequestMapping(path = {"/", "/saved", "/saved/*", "/favorites", "/sources", "/filters"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

}
