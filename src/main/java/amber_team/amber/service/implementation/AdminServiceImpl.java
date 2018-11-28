package amber_team.amber.service.implementation;


import amber_team.amber.dao.interfaces.RoleDao;
import amber_team.amber.dao.interfaces.UserDao;
import amber_team.amber.dao.interfaces.UserListDao;
import amber_team.amber.model.dto.AdminPageDto;
import amber_team.amber.model.dto.UserInfoDto;
import amber_team.amber.model.dto.UserListDto;
import amber_team.amber.model.entities.Role;
import amber_team.amber.service.interfaces.AdminService;
import amber_team.amber.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service(value = "adminService")
public class AdminServiceImpl implements AdminService {
    private static final int PAGINATION_SIZE = 25;

    private final UserListDao userListDao;
    private final RoleDao roleDao;
    private final UserDao userDao;

    @Autowired
    public AdminServiceImpl(UserListDao userListDao, RoleDao roleDao, UserDao userDao) {
        this.userListDao = userListDao;
        this.roleDao = roleDao;
        this.userDao = userDao;
    }


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

    @Override
    public AdminPageDto getUsers(int pageNumber) {
        AdminPageDto adminPageDto = new AdminPageDto();
        List<UserInfoDto> users = userDao.getUsersPagination(PAGINATION_SIZE * pageNumber, PAGINATION_SIZE);
        users = users.stream().map(user -> {
            List<Role> roles = roleDao.getUserRoles(user.getId());
            user.setRoles(roles);
            return user;
        }).collect(Collectors.toList());
        adminPageDto.setUsers(users);
        adminPageDto.setSystemRoles(roleDao.getAll());
        return adminPageDto;
    }


}
