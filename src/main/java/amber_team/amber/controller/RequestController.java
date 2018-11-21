package amber_team.amber.controller;


import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Request;
import amber_team.amber.service.interfaces.AttachmentsService;
import amber_team.amber.service.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Arrays;


@CrossOrigin(origins = "*", maxAge = 3600)//TODO Read
@RestController
public class RequestController {


    @Autowired
    private RequestService requestService;

    @Autowired
    private AttachmentsService attachmentsService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/request/{id}", method = RequestMethod.GET)
    public ResponseEntity<RequestInfoDto> getRequestInfo(@PathVariable String id) {
        return requestService.getRequestInfo(id);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public ResponseEntity<Request> save(@RequestBody RequestSaveDto request) {
        return requestService.save(request);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/request/executors/{warehouseId}", method = RequestMethod.GET)
    public ResponseEntity<UserListDto> getExecutors(@PathVariable String warehouseId) {
        return requestService.getWarehouseExecutors(warehouseId);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/request/create/{type}", method = RequestMethod.GET)
    public ResponseEntity<CreateOrderDto> creationData(@PathVariable String type) {
        return requestService.creationData(type);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/request/equipment/find/{value}", method = RequestMethod.GET)
    public ResponseEntity<EquipmentSearchDto> findEquipment(@PathVariable String value) {
        return requestService.searchEquipment(value);

    }


    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/request", method = RequestMethod.PUT)
    public ResponseEntity<Request> changeRequest(@RequestBody MyRequestStatusChangeDto request) {
        return requestService.changeStatus(request);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/request/create/attachments/{requestId}", method = RequestMethod.POST)
    public ResponseEntity uploadNewFile(@NotNull @RequestParam("files") MultipartFile[] multipartFile, @PathVariable String requestId) throws IOException {
        if(multipartFile.length < 1) return (ResponseEntity) ResponseEntity.notFound();
        return attachmentsService.uploadAttachments(Arrays.asList(multipartFile), requestId);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/attachments/{requestId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable String requestId, @RequestParam("filename") String filename) throws IOException {
        return attachmentsService.downloadAttachment(requestId, filename);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/attachments/list/{requestId}", method = RequestMethod.GET)
    public ResponseEntity<ListFilesDto> getFilesList(@PathVariable String requestId) {
        return attachmentsService.listFiles(requestId);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/attachments/zip/{requestId}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getZipFiles(@PathVariable String requestId) {
        return attachmentsService.downloadAsZIP(requestId);
    }

}
