package amber_team.amber.service.impl;



import amber_team.amber.dao.IUserDao;
import amber_team.amber.model.User;
import amber_team.amber.model.UserDto;
import amber_team.amber.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;




@Service(value = "userService")
public class UserServiceImpl implements UserService {
	


	@Autowired
	private IUserDao userDao;



	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;







	@Override
    public User save(UserDto user) {
	    User newUser = new User();
	    newUser.setEmail(user.getEmail());
	    newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setFname(user.getFname());
		newUser.setSname(user.getSname());
        return userDao.save(newUser);
    }


}
