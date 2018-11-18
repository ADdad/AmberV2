package amber_team.amber.service.implementation;



import amber_team.amber.dao.interfaces.UserListDao;
import amber_team.amber.model.dto.UserDto;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.model.dto.UserListDto;
import amber_team.amber.model.entities.User;
import amber_team.amber.service.interfaces.AdminService;
import amber_team.amber.service.interfaces.UserService;
import amber_team.amber.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Service(value = "adminService")
public class AdminServiceImpl implements AdminService {


   @Autowired
   private UserListDao userListDao;

    @Override
    public ResponseEntity update(UserListDto userDtos) {
        if(userDtos.getList().isEmpty()) return ResponseEntity.badRequest()
        .body(ErrorMessages.EMPTY_LIST);
        List<UserInfoDto> list = new ArrayList<UserInfoDto>();
        for (UserInfoDto user:list) {

        }
        return null;
    }

    @Override
    public ResponseEntity getAdminInfo(Principal principal) {
        return null;
    }

    @Override
    public ResponseEntity returnUsers() {
        return userListDao.returnUsers();
    }


}
