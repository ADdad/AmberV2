package amber_team.amber.service.implementation;


import amber_team.amber.dao.interfaces.*;
import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Comment;
import amber_team.amber.model.entities.Request;
import amber_team.amber.model.dto.RequestSaveDto;
import amber_team.amber.model.dto.RequestStatusChangeDto;
import amber_team.amber.service.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;


@Service(value = "requestService")
public class RequestServiceImpl implements RequestService {


    @Autowired
    private RequestDao requestDao;
    @Autowired
    private WarehouseDao warehouseDao;
    @Autowired
    private EquipmentDao equipmentDao;
    @Autowired
    private RequestTypeDao requestTypeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private AttributesDao attributesDao;

    @Override
    public ResponseEntity save(RequestSaveDto request) {
        Request newRequest = new Request();
        newRequest.setWarehouseId(request.getWarehouseId());
        newRequest.setDescription(request.getDescription());
        newRequest.setTitle(request.getTitle());
        newRequest.setCreatorId(request.getCreatorId());
        newRequest.setTypeId(requestTypeDao.getByName(request.getType()).getId());
        Request finalRequest = requestDao.create(newRequest);
        attributesDao.addAttributeValueToRequest(filterNullAttributes(request.getAttributes()), finalRequest.getId());

        equipmentDao.addEquipmentToRequest(request.getItems(), finalRequest.getId());

        return new ResponseEntity<>(finalRequest,
                HttpStatus.OK);


//		if(request.getTitle().isEmpty()) {
//			return ResponseEntity.badRequest()
//					.body(ErrorMessages.EMPTY_TITLE);
//		} else {
//			RequestInfoDto newRequest = new RequestInfoDto();
//			newRequest.setTitle(request.getTitle());
//			newRequest.setDescription(request.getDescription());
//			newRequest.setTypeId(request.getTypeId());
//			//todo setAttrbutes
//			return requestDao.create(newRequest);
//		}
    }

    List<AttributeInfoDto> filterNullAttributes(List<AttributeInfoDto> attributeInfoDtos) {
        List<AttributeInfoDto> result = attributeInfoDtos.stream()
                .filter(attribute -> attribute.getValue() != null)
                .collect(Collectors.toList());
        return result;
    }


    public ResponseEntity creationData(String type) {
        CreateOrderDto data = new CreateOrderDto();
        data.setAttributes(attributesDao.getAttributesOfType(type));
        data.setWarehouses(warehouseDao.getAll());
        data.setEquipment(equipmentDao.getLimited(20));
        return ResponseEntity.ok(data);
    }

    public ResponseEntity searchEquipment(String value) {
        EquipmentSearchDto result = new EquipmentSearchDto();
        result.setEquipment(equipmentDao.search(value));
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<EquipmentListDto> unavailableEquipmentByRequestId(String requestId) {
        //TODO filter out negative quantity equipment
        EquipmentListDto equipmentListDto = new EquipmentListDto();
        equipmentListDto.setList(equipmentDao.getUnavailableEquipmentQuantity(requestId));
        return ResponseEntity.ok(equipmentListDto);
    }

    @Override
    public ResponseEntity<UserListDto> getWarehouseExecutors(String warehouseId) {
        UserListDto userListDto = new UserListDto();
        userListDto.setList(warehouseDao.getExecutors(warehouseId));
        return ResponseEntity.ok(userListDto);
    }

    @Override
    public ResponseEntity<Request> changeStatus(MyRequestStatusChangeDto request) {
        Request requestNew = new Request();
        requestNew.setId(request.getRequestId());
        requestNew.setExecutorId(request.getExecutorId());
        requestNew.setStatus(request.getStatus());
        requestNew = requestDao.update(requestNew);


        if (request.getCommentText() != null) {
            Comment newComment = new Comment();
            newComment.setText(request.getCommentText());
            newComment.setUser(userDao.getById(request.getUserId()));
            newComment.setRequest(requestDao.getById(requestDao.getById(requestNew)));
            commentDao.create(newComment);
        }

        return ResponseEntity.ok(requestNew);
    }


    @Override
    public void archiveOldRequests() {
        requestDao.archiveOldRequests();
    }

    @Override
    public ResponseEntity getRequestInfo(String id) {
        Request request = new Request();
        request.setId(id);
        request = requestDao.getById(request);
        RequestInfoDto requestInfoDto = new RequestInfoDto(request);
        requestInfoDto.setWarehouse(warehouseDao.getById(request.getWarehouseId()));
        requestInfoDto.setType(requestTypeDao.getById(request.getTypeId()));
        requestInfoDto.setCreator(userDao.getById(request.getCreatorId()));
        if (request.getExecutorId() != null && request.getExecutorId().isEmpty())
            requestInfoDto.setExecutor(userDao.getById(request.getExecutorId()));
        List<CommentDto> commentDtos = commentDao.getForRequest(request.getId());
        if (commentDtos != null && commentDtos.size() > 0) {
            List<Comment> comments = commentDtos.parallelStream().map(commentDto -> new Comment(commentDto, userDao.getById(commentDto.getUserId()))).collect(Collectors.toList());
            requestInfoDto.setComments(comments);
        } else requestInfoDto.setComments(new ArrayList<Comment>());

        List<AttributeInfoDto> attributesValuesOfRequest = attributesDao.getAttributesValuesOfRequest(id);
        if (attributesValuesOfRequest != null)
            requestInfoDto.setAttributes(attributesValuesOfRequest);
        else requestInfoDto.setAttributes(new ArrayList<>());
        requestInfoDto.setEquipment(equipmentDao.getRequestEquipment(id));
        return ResponseEntity.ok(requestInfoDto);
    }


    private boolean openValidation(RequestStatusChangeDto request) {
        return request.getStatus().equals("Rejected");
    }

    private boolean cancelValidation(RequestStatusChangeDto request) {
        return !request.getStatus().equals("On Reviewing") && !request.getStatus().equals("Delivering") && !request.getStatus().equals("Completed");
    }

    private boolean rejectValidation(RequestStatusChangeDto request) {
        return request.getStatus().equals("On Reviewing");
    }

    private boolean reviewValidation(RequestStatusChangeDto request) {
        return request.getStatus().equals("Open");
    }

    private boolean progressValidation(RequestStatusChangeDto request) {
        return request.getStatus().equals("On Reviewing") || request.getStatus().equals("On Hold");
    }

    private boolean holdValidation(RequestStatusChangeDto request) {
        return request.getStatus().equals("In Progress");
    }

    private boolean deliverValidation(RequestStatusChangeDto request) {
        return request.getStatus().equals("In Progress");
    }

    private boolean completeValidation(RequestStatusChangeDto request) {
        return request.getStatus().equals("Delivering");
    }
}
