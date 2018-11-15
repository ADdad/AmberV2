package amber_team.amber.controller;



import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Request;
import amber_team.amber.service.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/r_info/{id}", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> getRequestInfo(@PathVariable String id){
        return requestService.getRequestInfo(id);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/request/save", method = RequestMethod.POST)
    public ResponseEntity<Request> save(@RequestBody RequestSaveDto request){
        return requestService.save(request);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/request/create/{type}", method = RequestMethod.GET)
    public ResponseEntity<CreateOrderDto> creationData(@PathVariable String type){
        return requestService.creationData(type);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/request/equipment/find/{value}", method = RequestMethod.GET)
    public ResponseEntity<EquipmentSearchDto> findEquipment(@PathVariable String value){
        return requestService.searchEquipment(value);

    }


    @PreAuthorize("isAuthenticated() and #request.getUsername() == principal.username")
    @RequestMapping(value="/r_open", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> open(RequestStatusChangeDto request){
        return requestService.open(request);
    }

    @PreAuthorize("isAuthenticated() and #request.getUsername() == principal.username")
    @RequestMapping(value="/r_cancel", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> cancel(RequestStatusChangeDto request){
        return requestService.cancel(request);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/r_review", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> review(RequestStatusChangeDto request){
        return requestService.review(request);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/r_reject", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> reject(RequestStatusChangeDto request){
        return requestService.reject(request);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value="/r_progress", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> progress(RequestStatusChangeDto request){
        return requestService.progress(request);
    }

    @PreAuthorize("isAuthenticated() and #request.getUsername() == principal.username")
    @RequestMapping(value="/r_hold", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> hold(RequestStatusChangeDto request){
        return requestService.hold(request);
    }

    @PreAuthorize("isAuthenticated() and #request.getUsername() == principal.username")
    @RequestMapping(value="/r_deliver", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> deliver(RequestStatusChangeDto request){
        return requestService.deliver(request);
    }

    @PreAuthorize("isAuthenticated() and #request.getUsername() == principal.username")
    @RequestMapping(value="/r_completed", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> complete(RequestStatusChangeDto request){
        return requestService.complete(request);
    }
}
