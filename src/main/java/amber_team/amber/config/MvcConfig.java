package amber_team.amber.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration
@Controller
public class MvcConfig extends WebMvcConfigurerAdapter {



    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/registration")
    public String registration() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login() { return "index.html"; }

    @GetMapping("/admin")
    @PreAuthorize("isAuthenticated()")
    public String admin() {
        return "index.html";
    }

    @GetMapping("/order")
    @PreAuthorize("isAuthenticated()")
    public String odrer() {
        return "index.html";
    }

}
