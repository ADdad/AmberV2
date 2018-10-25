package amber_team.amber.user;



import amber_team.amber.util.ErrorMesagges;
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
		if(!checkConfirmPass(user.getPassword(),user.getConfirmPassword())) {
			return ResponseEntity.badRequest()
					.body( ErrorMesagges.PASSWORD_AND_CONFIRMATION_NOT_EQUAL);
		} else if (!checkForNotNull(user.getEmail(),user.getPassword(),user.getFirstName(),user.getSecondName())) {
			return ResponseEntity.badRequest()
					.body(ErrorMesagges.BLANK_INPUTS);
		} else {
			User newUser = new User();
			newUser.setEmail(user.getEmail());
			newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
			newUser.setFirstName(user.getFirstName());
			newUser.setSecondName(user.getSecondName());
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
