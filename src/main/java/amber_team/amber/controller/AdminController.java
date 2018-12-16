package amber_team.amber.controller;

import amber_team.amber.model.dto.*;
import amber_team.amber.service.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users/data")
    public AdminPageUsersDataDto getUsersData() {
        return adminService.getUsersData();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/users/{pageNumber}")
    public UserListDto getUsers(@PathVariable int pageNumber) {
        return adminService.getUsers(pageNumber);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping(value = "/users")
    public ResponseEntity update(@RequestBody UpdateRolesListDto userRoles) {
        return adminService.update(userRoles);
    }


}
