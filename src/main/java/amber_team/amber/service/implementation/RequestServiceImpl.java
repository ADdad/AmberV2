package amber_team.amber.service.implementation;


import amber_team.amber.dao.interfaces.*;
import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Comment;
import amber_team.amber.model.entities.Request;
import amber_team.amber.model.entities.User;
import amber_team.amber.service.interfaces.RequestService;
import amber_team.amber.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static amber_team.amber.util.Constants.*;


@Service(value = "requestService")
public class RequestServiceImpl implements RequestService {


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
                              RequestTypeDao requestTypeDao, UserDao userDao, CommentDao commentDao,
                              AttributesDao attributesDao,
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
        sendCreationEmail(request);
        return finalRequest;


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
    public UserListDto getWarehouseExecutors(String warehouseId) {
        UserListDto userListDto = new UserListDto();
        userListDto.setList(warehouseDao.getExecutors(warehouseId));
        return userListDto;
    }

    @Override
    public Request changeStatus(MyRequestStatusChangeDto request) {

        Status existedStatus = requestDao.getById(request.getRequestId()).getStatus();

        Request requestNew = new Request();
        requestNew.setId(request.getRequestId());
        requestNew.setExecutorId(request.getExecutorId());
        requestNew.setStatus(request.getStatus());

        String requestTypeName = requestTypeDao.getByRequestId(request.getRequestId()).getName();
        if ("replenishment".equals(requestTypeName) || "refund".equals(requestTypeName)) {
            if (Status.COMPLETED.equals(request.getStatus())) {
                equipmentDao.increaseEquipment(request.getRequestId());
            }
        } else {
            if (Status.DELIVERING.equals(request.getStatus())) {
                List<EquipmentDto> unavailableEquipment = equipmentDao.decreaseEquipment(request.getRequestId());
                if (!unavailableEquipment.isEmpty()) {
                    requestNew.setStatus(Status.ON_HOLD);
                    requestNew.setConnectedRequestId(createReplenishmentRequest(request, unavailableEquipment));
                }
            }
        }


        requestNew = requestDao.update(requestNew);


        if (request.getCommentText() != null) {
            Comment newComment = new Comment();
            newComment.setText(request.getCommentText());
            newComment.setUser(userDao.getById(request.getUserId()));
            newComment.setRequest(requestNew);
            commentDao.create(newComment);
        }

        sendChangeStatusEmail(requestNew, existedStatus);

        return requestNew;
    }

    @Override
    public void changeStatus(ListRequestChangeStatusDto requests) {
        for (String requestId :
                requests.getRequests()) {
            changeStatus(new MyRequestStatusChangeDto(requestId, requests.getStatus()));
        }
    }

    private void sendChangeStatusEmail(Request requestNew, Status existedStatus) {
        User userInfo = userDao.getById(requestNew.getCreatorId());
        emailService.sendRequestStatusChanged(userInfo.getEmail(), userInfo.getFirstName(), requestNew.getTitle(),
                existedStatus,
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
        newRequest.setStatus(Status.OPENED);
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
        if (userByEmail.getRoles().contains(roleDao.getByName(KEEPER_ROLE_NAME)))
            requestsList.addAll(requestDao.getKeeperRequestsPagination(userByEmail.getId(), page *
                    REQUESTS_PAGINATION_SIZE, REQUESTS_PAGINATION_SIZE));
        if (userByEmail.getRoles().contains(roleDao.getByName(ADMIN_ROLE_NAME)))
            requestsList.addAll(requestDao.getAdminRequestsPagination(userByEmail.getId(), page *
                    REQUESTS_PAGINATION_SIZE, REQUESTS_PAGINATION_SIZE));

        Collections.sort(requestsList, new Comparator<Request>() {
            @Override
            public int compare(Request request1, Request request2) {
                return request1.getModifiedDate().compareTo(request2.getModifiedDate());
            }
        });
        try {
            requestsList = requestsList.subList(0, REQUESTS_PAGINATION_SIZE);
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
        List<Request> listRequests = requestDao.getAllUsersRequestsPagination(userByEmail.getId(), page *
                REQUESTS_PAGINATION_SIZE, REQUESTS_PAGINATION_SIZE);
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
        if (request.getConnectedRequest() != null) newRequest.setConnectedRequestId(request.getConnectedRequest());
        return newRequest;
    }


    @Override
    public void archiveOldRequests() {
        requestDao.archiveOldRequests();
    }

    @Override
    public RequestInfoDto getRequestInfo(String id) {
        Request request = requestDao.getById(id);
        RequestInfoDto requestInfoDto = new RequestInfoDto(request);
        requestInfoDto.setWarehouse(warehouseDao.getById(request.getWarehouseId()));
        requestInfoDto.setType(requestTypeDao.getById(request.getTypeId()));
        requestInfoDto.setCreator(userDao.getById(request.getCreatorId()));
        if (request.getExecutorId() != null && !request.getExecutorId().isEmpty())
            requestInfoDto.setExecutor(userDao.getById(request.getExecutorId()));
        List<CommentDto> commentDtos = commentDao.getForRequest(request.getId());
        //Add converter
        if (commentDtos != null && commentDtos.size() > 0) {
            List<Comment> comments = commentDtos.stream().map(commentDto -> new Comment(commentDto,
                    userDao.getById(commentDto.getUserId()))).collect(Collectors.toList());
            requestInfoDto.setComments(comments);
        } else requestInfoDto.setComments(new ArrayList<>());

        List<AttributeInfoDto> attributesValuesOfRequest = attributesDao.getAttributesValuesOfRequest(id);
        if (attributesValuesOfRequest != null)
            requestInfoDto.setAttributes(attributesValuesOfRequest);
        else requestInfoDto.setAttributes(new ArrayList<>());
        requestInfoDto.setEquipment(equipmentDao.getRequestEquipment(id));
        return requestInfoDto;
    }
}
