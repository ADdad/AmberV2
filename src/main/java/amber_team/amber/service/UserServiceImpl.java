package amber_team.amber.service;



import amber_team.amber.dao.IUserDao;
import amber_team.amber.model.User;
import amber_team.amber.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;




@Service(value = "userService")
public class UserServiceImpl implements UserService {
	


	@Autowired
	private IUserDao userDao;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;


	@Override
    public ResponseEntity save(UserDto user) {
		if(!checkConfirmPass(user.getPassword(),user.getConfirmpassword())) {
			return ResponseEntity.badRequest()
					.body( "{ErrorMessage : 'Password and confirmation password are not equal!'}");
		} else if (!checkForNotNull(user.getEmail(),user.getPassword(),user.getFname(),user.getSname())) {
			return ResponseEntity.badRequest()
					.body( "{ErrorMessage : 'Some inputs are blank!'}");
		} else {
			User newUser = new User();
			newUser.setEmail(user.getEmail());
			newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
			newUser.setFname(user.getFname());
			newUser.setSname(user.getSname());
			return userDao.save(newUser);
		}
    }

    private boolean checkForNotNull (String email, String password, String fname, String sname){
		return !(email.equals("") || password.equals("") || fname.equals("") || sname.equals(""));
	}

    private boolean checkConfirmPass(String password, String confirm){
		return password.equals(confirm);
	}


}
