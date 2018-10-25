package amber_team.amber.controller;


import amber_team.amber.model.User;
import amber_team.amber.model.UserDto;
import amber_team.amber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/signin")
    public void sgnin(){}


    @RequestMapping(value="/signup", method = RequestMethod.POST)
    public ResponseEntity<User> saveUser(@RequestBody UserDto user){
        return  userService.save(user);
    }



}
