package amber_team.amber.service.implementation;



import amber_team.amber.dao.interfaces.UserDao;
import amber_team.amber.model.entities.User;
import amber_team.amber.model.dto.UserDto;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.service.interfaces.UserService;
import amber_team.amber.util.ErrorMessages;
import amber_team.amber.util.RegExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.regex.Matcher;


@Service(value = "userService")
public class UserServiceImpl implements UserService {
	


	@Autowired
	private UserDao userDao;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;


	@Override
    public ResponseEntity save(UserDto user) {
		if(!checkConfirmPass(user.getPassword(),user.getConfirmPassword())) {
			return ResponseEntity.badRequest()
					.body(ErrorMessages.PASSWORD_AND_CONFIRMATION_NOT_EQUAL);
		} else if (!checkForNotNull(user.getEmail(),user.getPassword(),user.getFirstName(),user.getSecondName())) {
			return ResponseEntity.badRequest()
					.body(ErrorMessages.BLANK_INPUTS);
		} else if (!checkEmailReg(user.getEmail())) {
			return ResponseEntity.badRequest()
					.body(ErrorMessages.INVALID_EMAIL);
		} else {
			User newUser = new User();
			newUser.setEmail(user.getEmail());
			newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
			newUser.setfName(user.getFirstName());
			newUser.setSecondName(user.getSecondName());
			return userDao.save(newUser);
		}
    }

	@Override
	public ResponseEntity<UserInfoDto> getUserInfo(Principal principal) {
		return userDao.getUserInfo(principal);
	}

	private boolean checkForNotNull(String email, String password, String fname, String sname){
		return !(email.equals("") || password.equals("") || fname.equals("") || sname.equals(""));
	}

    private boolean checkConfirmPass(String password, String confirm){
		return password.equals(confirm);
	}

	private boolean checkEmailReg(String email) {
		Matcher matcher = RegExp.EMAIL.matcher(email);
		return matcher.find();
	}


}
