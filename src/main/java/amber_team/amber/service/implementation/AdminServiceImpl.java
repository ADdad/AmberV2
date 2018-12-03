package amber_team.amber.service.implementation;


import amber_team.amber.dao.interfaces.RoleDao;
import amber_team.amber.dao.interfaces.UserDao;
import amber_team.amber.dao.interfaces.UserListDao;
import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Role;
import amber_team.amber.service.interfaces.AdminService;
import amber_team.amber.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.xml.ws.Response;
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
    public ResponseEntity update(UpdateRolesListDto userDtos) {
        System.out.println("____________USERLISTDTOS___________"+userDtos);
        try {
            System.out.println(userDtos.getUsers());
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        if (userDtos.getUsers().isEmpty()) return ResponseEntity.badRequest().body("Empty list to update");
        else if(userDtos.getUsers().contains(null)) return ResponseEntity.badRequest().body("Null obj");
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
