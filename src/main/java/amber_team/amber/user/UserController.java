package amber_team.amber.user;



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


    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity<User> saveUser(@RequestBody UserDto user){
        return  userService.save(user);
    }



}
