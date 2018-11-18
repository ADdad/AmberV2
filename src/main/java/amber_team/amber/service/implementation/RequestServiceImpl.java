package amber_team.amber.service.implementation;


import amber_team.amber.dao.implementation.RequestDaoImpl;
import amber_team.amber.dao.interfaces.*;
import amber_team.amber.model.dto.*;
import amber_team.amber.model.entities.Equipment;
import amber_team.amber.model.entities.Request;
import amber_team.amber.model.dto.RequestSaveDto;
import amber_team.amber.model.dto.RequestStatusChangeDto;
import amber_team.amber.service.interfaces.RequestService;
import amber_team.amber.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Date;


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
    private RequestValuesDao requestValuesDao;
    @Autowired
    private RequestEquipmentDao requestEquipmentDao;


	@Override
    public ResponseEntity save(RequestSaveDto request) {
        Request newRequest = new Request();
        newRequest.setWarehouseId(request.getWarehouseId());
        newRequest.setDescription(request.getDescription());
        newRequest.setTitle(request.getTitle());
        newRequest.setCreatorId(request.getCreatorId());
        newRequest.setTypeId(requestTypeDao.getByName(request.getType()).getId());
        Request finalRequest = requestDao.save(newRequest);
		for (AttributeSaveDto attribute :
                request.getAttributes()) {
            requestValuesDao.save(attribute, finalRequest.getId());
        }

        for (EquipmentDto equipmentDto :
                request.getItems()) {
            requestEquipmentDao.save(equipmentDto, finalRequest.getId());
        }

        return new ResponseEntity<>(finalRequest,
				HttpStatus.OK);


//		if(request.getTitle().isEmpty()) {
//			return ResponseEntity.badRequest()
//					.body(ErrorMessages.EMPTY_TITLE);
//		} else {
//			Request newRequest = new Request();
//			newRequest.setTitle(request.getTitle());
//			newRequest.setDescription(request.getDescription());
//			newRequest.setTypeId(request.getTypeId());
//			//todo setAttrbutes
//			return requestDao.save(newRequest);
//		}
    }

	@Override
	public ResponseEntity open(RequestStatusChangeDto request) {
		if(!openValidation(request)){
			return ResponseEntity.badRequest()
					.body(ErrorMessages.STATUS_ERROR);
		} else {
			return requestDao.open(request);
		}
	}

    public ResponseEntity creationData(String type) {
        CreateOrderDto data = new CreateOrderDto();
        data.setAttributes(requestDao.attributes(type));
        data.setWarehouses(warehouseDao.getAll());
        data.setEquipment(equipmentDao.getLimited(25));
		return ResponseEntity.ok(data);
    }

    public ResponseEntity searchEquipment(String value){
        EquipmentSearchDto result = new EquipmentSearchDto();
        result.setEquipment(equipmentDao.search(value));
        return ResponseEntity.ok(result);
    }

	@Override
	public ResponseEntity cancel(RequestStatusChangeDto request) {
		if(!cancelValidation(request)){
			return ResponseEntity.badRequest()
					.body(ErrorMessages.STATUS_ERROR);
		} else {
			return requestDao.cancel(request);
		}
	}

	@Override
	public ResponseEntity reject(RequestStatusChangeDto request) {
		if(!rejectValidation(request)){
			return ResponseEntity.badRequest()
					.body(ErrorMessages.STATUS_ERROR);
		} else {
			return requestDao.reject(request);
		}
	}

	@Override
	public ResponseEntity review(RequestStatusChangeDto request) {
		if(!reviewValidation(request)){
			return ResponseEntity.badRequest()
					.body(ErrorMessages.STATUS_ERROR);
		} else {
			return requestDao.review(request);
		}
	}

	@Override
	public ResponseEntity progress(RequestStatusChangeDto request) {
		if(!progressValidation(request)){
			return ResponseEntity.badRequest()
					.body(ErrorMessages.STATUS_ERROR);
		} else {
			return requestDao.progress(request);
		}
	}

	@Override
	public ResponseEntity hold(RequestStatusChangeDto request) {
		if(!holdValidation(request)){
			return ResponseEntity.badRequest()
					.body(ErrorMessages.STATUS_ERROR);
		} else {
			return requestDao.hold(request);
		}
	}

	@Override
	public ResponseEntity deliver(RequestStatusChangeDto request) {
		if(!deliverValidation(request)){
			return ResponseEntity.badRequest()
					.body(ErrorMessages.STATUS_ERROR);
		} else {
			return requestDao.deliver(request);
		}
	}

	@Override
	public ResponseEntity complete(RequestStatusChangeDto request) {
		if(!completeValidation(request)){
			return ResponseEntity.badRequest()
					.body(ErrorMessages.STATUS_ERROR);
		} else {
			return requestDao.complete(request);
		}
	}

	@Override
	public void archiveOldRequests() {
		requestDao.archiveOldRequests();
	}

	@Override
    public ResponseEntity getRequestInfo(String id) {
        Request request = new Request();
        request.setId(id);
        //TODO Send string to method
        return requestDao.getRequestInfo(request);
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
