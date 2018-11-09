package amber_team.amber.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public String renderErrorPage(HttpServletRequest httpRequest) {
        return "index.html";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
