package amber_team.amber.service.implementation;


import amber_team.amber.dao.interfaces.RoleDao;
import amber_team.amber.dao.interfaces.UserDao;
import amber_team.amber.dao.interfaces.UserListDao;
import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Role;
import amber_team.amber.service.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static amber_team.amber.util.Constants.USERS_PAGINATION_SIZE;


@Service(value = "adminService")
public class AdminServiceImpl implements AdminService {


    private final UserListDao userListDao;
    private final RoleDao roleDao;
    private final UserDao userDao;
    private final EmailServiceImpl emailService;

    @Autowired
    public AdminServiceImpl(UserListDao userListDao, RoleDao roleDao, UserDao userDao, EmailServiceImpl emailService) {
        this.userListDao = userListDao;
        this.roleDao = roleDao;
        this.userDao = userDao;
        this.emailService = emailService;
    }


    @Override
    public ResponseEntity update(UpdateRolesListDto userDtos) {
        try {

            System.out.println(userDtos.getUsers());
            sendUpdateEmail(userDtos);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (userDtos.getUsers().isEmpty()) return ResponseEntity.badRequest().body("Empty list to update");
        else if (userDtos.getUsers().contains(null)) return ResponseEntity.badRequest().body("Null obj");

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
    public UserListDto getUsers(int pageNumber) {
        UserListDto userListDto = new UserListDto();
        List<UserInfoDto> users = userDao.getUsersPagination(USERS_PAGINATION_SIZE * pageNumber, USERS_PAGINATION_SIZE);
        users = users.stream().map(user -> {
            List<Role> roles = roleDao.getUserRoles(user.getId());
            user.setRoles(roles);
            return user;
        }).collect(Collectors.toList());
        userListDto.setList(users);
        return userListDto;
    }

    @Override
    public AdminPageUsersDataDto getUsersData() {
        AdminPageUsersDataDto adminPageUsersDataDto = new AdminPageUsersDataDto();
        adminPageUsersDataDto.setSystemRoles(roleDao.getAll());
        adminPageUsersDataDto.setUsersCount(userDao.getAllActive().size());
        return adminPageUsersDataDto;
    }

    private void sendUpdateEmail(UpdateRolesListDto users) {
        UserInfoDto userLocal;
        for (UpdateRolesDto user:
             users.getUsers()) {
            userLocal = userDao.getByIdWithRoles(user.getUserId());
            List<String> roles = userLocal.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            emailService.sendUserRolesChanged(userLocal.getEmail(), userLocal.getFirstName(), roles);
        }
    }


}
