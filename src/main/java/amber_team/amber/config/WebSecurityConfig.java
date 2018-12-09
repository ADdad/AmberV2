package amber_team.amber.config;


import amber_team.amber.util.SQLQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        SQLQueries.USER_BY_USERNAME_QUERY)
                .authoritiesByUsernameQuery(
                        SQLQueries.AUTHORITIES_BY_USERNAME).passwordEncoder(encoder());
    }

    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
        } catch (ServletException e) {
            System.out.println("Error while login " + e);
        }
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http

                .authorizeRequests()
                .antMatchers("/register", "/perform_login", "/*.js", "/*.ico", "/*.json", "/index*", "/static/**", "/registration", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/dashboard", true)
                .and()
                .logout().logoutSuccessUrl("/login")
                .and().csrf().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;

    }


    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
