package amber_team.amber.service.implementation;


import amber_team.amber.dao.interfaces.UserListDao;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.model.dto.UserListDto;
import amber_team.amber.service.interfaces.AdminService;
import amber_team.amber.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;


@Service(value = "adminService")
public class AdminServiceImpl implements AdminService {


    @Autowired
    private UserListDao userListDao;

    @Override
    public UserListDto update(UserListDto userDtos) {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setId(ErrorMessages.EMPTY_LIST);
        userDtos.setList(Arrays.asList(userInfoDto));
        if (userDtos.getList().isEmpty()) return userDtos;
        return userListDao.update(userDtos);
    }

    @Override
    public UserListDto getAdminInfo(Principal principal) {
        return null;
    }

    @Override
    public UserListDto returnUsers() {
        return userListDao.returnUsers();
    }


}
