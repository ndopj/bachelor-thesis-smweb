package cz.sm.ng.clodwar.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Simple basic HTTP request controller
 * used to define home URL of clodwar web
 * interface.
 *
 * @author Norbert Dopjera
 */
@Controller
public class ClodWarHomeController {

    @RequestMapping(path = {"/clodwar", "/clodwar/"})
    public String index() {
        return "/clodwar/index"; // redirect
    }
}
