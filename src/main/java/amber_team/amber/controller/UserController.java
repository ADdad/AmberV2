package amber_team.amber.controller;



import amber_team.amber.model.entities.User;
import amber_team.amber.model.dto.UserDto;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity<User> saveUser(@RequestBody UserDto user){
        return  userService.save(user);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/userinfo", method = RequestMethod.GET)
    public ResponseEntity<UserInfoDto> getUserInfo(Principal principal){
        return userService.getUserInfo(principal);
    }

}
