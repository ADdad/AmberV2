package amber_team.amber.service.implementation;


import amber_team.amber.dao.interfaces.*;
import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Comment;
import amber_team.amber.model.entities.Request;
import amber_team.amber.model.entities.User;
import amber_team.amber.service.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service(value = "requestService")
public class RequestServiceImpl implements RequestService {


    private static final int PAGINATION = 25;
    private static final String ADMIN_ROLE_NAME = "ROLE_ADMIN";
    private static final String KEEPER_ROLE_NAME = "ROLE_KEEPER";
    private final RequestDao requestDao;
    private final WarehouseDao warehouseDao;
    private final EquipmentDao equipmentDao;
    private final RequestTypeDao requestTypeDao;
    private final UserDao userDao;
    private final CommentDao commentDao;
    private final AttributesDao attributesDao;
    private final RoleDao roleDao;
    private final EmailServiceImpl emailService;

    @Autowired
    public RequestServiceImpl(RequestDao requestDao, WarehouseDao warehouseDao, EquipmentDao equipmentDao,
                              RequestTypeDao requestTypeDao, UserDao userDao, CommentDao commentDao, AttributesDao attributesDao,
                              RoleDao roleDao, EmailServiceImpl emailService) {
        this.requestDao = requestDao;
        this.warehouseDao = warehouseDao;
        this.equipmentDao = equipmentDao;
        this.requestTypeDao = requestTypeDao;
        this.userDao = userDao;
        this.commentDao = commentDao;
        this.attributesDao = attributesDao;
        this.roleDao = roleDao;
        this.emailService = emailService;
    }

    @Override
    public Request save(RequestSaveDto request) {
        Request newRequest = mapSaveDto(request);
        Request finalRequest = requestDao.create(newRequest);
        attributesDao.addAttributeValueToRequest(filterNullAttributes(request.getAttributes()), finalRequest.getId());

        equipmentDao.addEquipmentToRequest(request.getItems(), finalRequest.getId());
       // sendCreationEmail(request);
        return finalRequest;


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

    private void sendCreationEmail(RequestSaveDto request) {
        User userInfo = userDao.getById(request.getCreatorId());
        emailService.sendRequestCreated(userInfo.getEmail(), userInfo.getFirstName(), request.getTitle());
    }

    private List<AttributeInfoDto> filterNullAttributes(List<AttributeInfoDto> attributeInfoDtos) {
        List<AttributeInfoDto> result = attributeInfoDtos.stream()
                .filter(attribute -> (attribute.getValue() != null && !attribute.getValue().isEmpty()))
                .collect(Collectors.toList());
        return result;
    }


    public CreateOrderDto creationData(String type) {
        CreateOrderDto data = new CreateOrderDto();
        data.setAttributes(attributesDao.getAttributesOfType(type));
        data.setWarehouses(warehouseDao.getAll());
        data.setEquipment(equipmentDao.getLimited(20));
        return data;
    }

    public EquipmentSearchDto searchEquipment(String value) {
        EquipmentSearchDto result = new EquipmentSearchDto();
        result.setEquipment(equipmentDao.search(value));
        return result;
    }

    @Override
    public EquipmentListDto unavailableEquipmentByRequestId(String requestId) {
        //TODO filter out negative quantity equipment
        EquipmentListDto equipmentListDto = new EquipmentListDto();
        equipmentListDto.setList(equipmentDao.getUnavailableEquipmentQuantity(requestId));
        return equipmentListDto;
    }

    @Override
    public UserListDto getWarehouseExecutors(String warehouseId) {
        UserListDto userListDto = new UserListDto();
        userListDto.setList(warehouseDao.getExecutors(warehouseId));
        return userListDto;
    }

    @Override
    public Request changeStatus(MyRequestStatusChangeDto request) {

        String oldStatus = requestDao.getById(request.getRequestId()).getStatus();

        Request requestNew = new Request();
        requestNew.setId(request.getRequestId());
        requestNew.setExecutorId(request.getExecutorId());
        requestNew.setStatus(request.getStatus());

        String requestTypeName = requestTypeDao.getByRequestId(request.getRequestId()).getName();
        if ("replenishment".equals(requestTypeName) || "refund".equals(requestTypeName)) {
            if ("Completed".equals(request.getStatus())) {
                equipmentDao.increaseEquipment(request.getRequestId());
            }
        } else {
            if ("Delivering".equals(request.getStatus())) {
                List<EquipmentDto> unavailableEquipment = equipmentDao.decreaseEquipment(request.getRequestId());
                if (!unavailableEquipment.isEmpty()) {
                    requestNew.setStatus("On Hold");
                    requestNew.setConnectedRequestId(createReplenishmentRequest(request, unavailableEquipment));
                }
            }
        }


        requestNew = requestDao.update(requestNew);


        if (request.getCommentText() != null) {
            Comment newComment = new Comment();
            newComment.setText(request.getCommentText());
            newComment.setUser(userDao.getById(request.getUserId()));
            newComment.setRequest(requestDao.getByRequest(requestDao.getByRequest(requestNew)));
            commentDao.create(newComment);
        }

        sendChangeStatusEmail(requestNew, oldStatus);

        return requestNew;
    }

    private void sendChangeStatusEmail(Request requestNew, String oldStatus) {
        User userInfo = userDao.getById(requestNew.getCreatorId());
        emailService.sendRequestStatusChanged(userInfo.getEmail(), userInfo.getFirstName(), requestNew.getTitle(), oldStatus,
                requestNew.getStatus());
    }

    private String createReplenishmentRequest(MyRequestStatusChangeDto request, List<EquipmentDto> unavailableEquipment) {
        RequestSaveDto replRequest = new RequestSaveDto();
        replRequest.setCreatorId(request.getUserId());
        replRequest.setItems(unavailableEquipment);
        replRequest.setTitle("Replenishment for request " + request.getRequestId());
        replRequest.setWarehouseId(requestDao.getById(request.getRequestId()).getWarehouseId());
        replRequest.setType("replenishment");
        return save(replRequest).getId();
    }

    @Override
    public Request editRequest(RequestSaveDto request) {
        Request newRequest = mapSaveDto(request);
        newRequest.setId(request.getRequestId());
        newRequest.setStatus("Opened");
        Request finalRequest = requestDao.update(newRequest);

        attributesDao.removeRequestValues(request.getRequestId());
        attributesDao.addAttributeValueToRequest(filterNullAttributes(request.getAttributes()), finalRequest.getId());


        equipmentDao.removeEquipmentFromRequest(request.getRequestId());
        equipmentDao.addEquipmentToRequest(request.getItems(), finalRequest.getId());

        return finalRequest;
    }

    @Override
    public RequestListDtoPagination getExecutingRequests(Principal principal, int page) {
        UserInfoDto userByEmail = userDao.getUserByEmail(principal);
        page--;
        List<Request> requestsList = new ArrayList<>();
        if (userByEmail.getRoles().contains(roleDao.getByName(ADMIN_ROLE_NAME)))
            requestsList.addAll(requestDao.getKeeperRequestsPagination(userByEmail.getId(), page * PAGINATION, PAGINATION));
        if (userByEmail.getRoles().contains(roleDao.getByName(KEEPER_ROLE_NAME)))
            requestsList.addAll(requestDao.getAdminRequestsPagination(userByEmail.getId(), page * PAGINATION, PAGINATION));

        Collections.sort(requestsList, new Comparator<Request>() {
            @Override
            public int compare(Request request1, Request request2) {
                return request1.getModifiedDate().compareTo(request2.getModifiedDate());
            }
        });
        try {
            requestsList = requestsList.subList(0, PAGINATION);
        } catch (IndexOutOfBoundsException e) {
            requestsList = requestsList.subList(0, requestsList.size());
        }
        for (Request req :
                requestsList) {
            req.setTypeId(requestTypeDao.getById(req.getTypeId()).getName());
        }
        RequestListDtoPagination requestListDtoPagination = new RequestListDtoPagination();
        requestListDtoPagination.setRequests(requestsList);
        requestListDtoPagination.setRequestsCount(requestDao.getCountOfAdminActiveRequests()
                + requestDao.getCountOfKeeperActiveRequests(userByEmail.getId()));
        return requestListDtoPagination;
    }

    @Override
    public RequestListDtoPagination getCreatedRequests(Principal userData, int page) {
        UserInfoDto userByEmail = userDao.getUserByEmail(userData);
        page--;
        List<Request> listRequests = requestDao.getAllUsersRequestsPagination(userByEmail.getId(), page * PAGINATION, PAGINATION);
        RequestListDtoPagination requestListDtoPagination = new RequestListDtoPagination();
        requestListDtoPagination.setRequests(listRequests);
        requestListDtoPagination.setRequestsCount(requestDao.getCountOfUsersActiveRequests(userByEmail.getId()));
        return requestListDtoPagination;
    }

    private Request mapSaveDto(RequestSaveDto request) {
        Request newRequest = new Request();

        newRequest.setWarehouseId(request.getWarehouseId());
        newRequest.setDescription(request.getDescription());
        newRequest.setTitle(request.getTitle());
        newRequest.setCreatorId(request.getCreatorId());
        newRequest.setTypeId(requestTypeDao.getByName(request.getType()).getId());
        if(request.getConnectedRequest() != null) newRequest.setConnectedRequestId(request.getConnectedRequest());
        return newRequest;
    }


    @Override
    public void archiveOldRequests() {
        requestDao.archiveOldRequests();
    }

    @Override
    public RequestInfoDto getRequestInfo(String id) {
        Request request = new Request();
        request.setId(id);
        request = requestDao.getByRequest(request);
        RequestInfoDto requestInfoDto = new RequestInfoDto(request);
        requestInfoDto.setWarehouse(warehouseDao.getById(request.getWarehouseId()));
        requestInfoDto.setType(requestTypeDao.getById(request.getTypeId()));
        requestInfoDto.setCreator(userDao.getById(request.getCreatorId()));
        if (request.getExecutorId() != null && !request.getExecutorId().isEmpty())
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
        return requestInfoDto;
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
