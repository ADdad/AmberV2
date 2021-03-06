package amber_team.amber.controller;

import amber_team.amber.model.dto.UserListDto;
import amber_team.amber.model.entities.User;
import amber_team.amber.model.dto.UserDto;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(value = "/register")
    public ResponseEntity<User> saveUser(@RequestBody UserDto user) {
        return userService.save(user);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/userinfo")
    public ResponseEntity<UserInfoDto> getUserInfo(Principal principal) {
        return userService.getUserInfo(principal);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @GetMapping(value = "/users")
    @ResponseStatus(HttpStatus.OK)
    public UserListDto searchUsers(@RequestParam("search") String search) {
        return userService.searchUsers(search);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDto getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

}
