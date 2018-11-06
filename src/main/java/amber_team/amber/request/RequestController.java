package amber_team.amber.request;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PreAuthorize("isAuthenticated() and (#request.getCreator_id() == principal.id or request.getExecutor_id() == principal.id)")
    @RequestMapping(value="/r_info", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> getRequestInfo(RequestStatusChangeDto request){
        return requestService.getRequestInfo(request);
    }

    @PreAuthorize("isAuthenticated() and #request.getCreator_id() == principal.id")
    @RequestMapping(value="/r_save", method = RequestMethod.POST)
    public ResponseEntity<Request> save(RequestSaveDto request){
        return requestService.save(request);
    }

    @PreAuthorize("isAuthenticated() and #request.getCreator_id() == principal.id")
    @RequestMapping(value="/r_open", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> open(RequestStatusChangeDto request){
        return requestService.open(request);
    }

    @PreAuthorize("isAuthenticated() and #request.getCreator_id() == principal.id")
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

    @PreAuthorize("isAuthenticated() and #request.getExecutor_id() == principal.id")
    @RequestMapping(value="/r_hold", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> hold(RequestStatusChangeDto request){
        return requestService.hold(request);
    }

    @PreAuthorize("isAuthenticated() and #request.getExecutor_id() == principal.id")
    @RequestMapping(value="/r_deliver", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> deliver(RequestStatusChangeDto request){
        return requestService.deliver(request);
    }

    @PreAuthorize("isAuthenticated() and #request.getCreator_id() == principal.id")
    @RequestMapping(value="/r_completed", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> complete(RequestStatusChangeDto request){
        return requestService.complete(request);
    }
}