package amber_team.amber.controller;


import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Request;
import amber_team.amber.service.interfaces.AttachmentsService;
import amber_team.amber.service.interfaces.RequestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;


@CrossOrigin(origins = "*", maxAge = 3600)//TODO Read
@RestController
public class RequestController {

    private static Logger logger = LogManager.getLogger(RequestController.class);

    private final RequestService requestService;

    private final AttachmentsService attachmentsService;

    @Autowired
    public RequestController(RequestService requestService, AttachmentsService attachmentsService) {
        this.requestService = requestService;
        this.attachmentsService = attachmentsService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/request/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RequestInfoDto getRequestInfo(@PathVariable String id) {
        return requestService.getRequestInfo(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/request")
    @ResponseStatus(HttpStatus.CREATED)
    public Request save(@RequestBody RequestSaveDto request) {
        return requestService.save(request);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/request/executors/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    public UserListDto getExecutors(@PathVariable String warehouseId) {
        return requestService.getWarehouseExecutors(warehouseId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/requests/created/{page}")
    @ResponseStatus(HttpStatus.OK)
    public RequestListDtoPagination getCreatedRequests(Principal principal, @PathVariable int page,
                                                       @RequestParam("archive") boolean archive) {
        return requestService.getCreatedRequests(principal, page, archive);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/requests/executing/{page}")
    @ResponseStatus(HttpStatus.OK)
    public RequestListDtoPagination getExecutingRequests(Principal principal, @PathVariable int page,
                                                         @RequestParam("archive") boolean archive) {
        return requestService.getExecutingRequests(principal, page, archive);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/request/create/{type}")
    @ResponseStatus(HttpStatus.OK)
    public CreateOrderDto creationData(@PathVariable String type) {
        logger.debug("Getting creation data for type " + type);
        return requestService.creationData(type);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/request/equipment/find/{value}")
    public EquipmentSearchDto findEquipment(@PathVariable String value) {
        return requestService.searchEquipment(value);
    }


    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/request")
    public Request updateRequest(@RequestBody RequestStatusChangeDto request) {
        return requestService.changeStatus(request);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/request/list")
    public ResponseEntity updateRequests(@RequestBody ListRequestChangeStatusDto requests) {
        requestService.changeStatus(requests);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/request")
    public Request editRequest(@RequestBody RequestSaveDto request) {
        return requestService.editRequest(request);
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/request/create/attachments/{requestId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity uploadNewFile(@NotNull @RequestParam("files") MultipartFile[] multipartFile,
                                        @PathVariable String requestId) throws IOException {
        logger.debug("Upload attachments for id" + requestId + "size " + multipartFile.length);
        if (multipartFile.length < 1) {
            logger.debug("No attachments");
            attachmentsService.deleteRequestAttachments(requestId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return attachmentsService.uploadAttachments(Arrays.asList(multipartFile), requestId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/attachments/{requestId}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable String requestId,
                                                     @RequestParam("filename") String filename) throws IOException {
        return attachmentsService.downloadAttachment(requestId, filename);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/attachments/list/{requestId}")
    public ListFilesDto getFilesList(@PathVariable String requestId) {
        return attachmentsService.listFiles(requestId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/attachments/zip/{requestId}")
    public ResponseEntity<byte[]> getZipFiles(@PathVariable String requestId) {
        return attachmentsService.downloadAsZIP(requestId);
    }

}
