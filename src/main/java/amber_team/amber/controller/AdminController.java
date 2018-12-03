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
    @Autowired
    private AdminService adminService;

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping(value = "/admin")
//    public UserListDto getUserList() {
//        System.out.println("Controller: \n" + adminService.returnUsers().toString());
//        return adminService.returnUsers();
//    }
//
//    @PreAuthorize("isAuthenticated()")
//    @PostMapping(value = "/admin")
//    public UserListDto getRequestInfo(UserListDto request){
//        return adminService.update(request);
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/users/{pageNumber}")
    public AdminPageDto getUsers(@PathVariable int pageNumber) {
        return adminService.getUsers(pageNumber);
    }

//    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/users")
    public ResponseEntity update(@RequestBody UpdateRolesListDto userRoles) {
        return adminService.update(userRoles);
    }


}
