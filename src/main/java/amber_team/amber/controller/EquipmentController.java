package amber_team.amber.controller;

import amber_team.amber.model.dto.EquipmentAddingDto;
import amber_team.amber.model.dto.EquipmentListDto;
import amber_team.amber.model.dto.EquipmentSearchDto;
import amber_team.amber.model.dto.WarehouseListDto;
import amber_team.amber.model.entities.Equipment;
import amber_team.amber.service.interfaces.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)//TODO Read
@RestController
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Autowired
    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/equipment/list")
    public ResponseEntity<String> addItems(@RequestBody EquipmentAddingDto equipmentAddingDto) {
        equipmentService.addEquipmentToWarehouse(equipmentAddingDto);
        return new ResponseEntity<>("Created", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/equipment")
    public ResponseEntity createEquipment(@RequestBody Equipment equipment) {
        equipmentService.addNewEquipment(equipment);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_KEEPER')")
    @GetMapping("/equipment/unavalible/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public EquipmentListDto getUnavalibleEquipmentOfRequest(@PathVariable String requestId) {
        return equipmentService.unavailableEquipmentByRequestId(requestId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/equipment")
    @ResponseStatus(HttpStatus.OK)
    public EquipmentSearchDto getItems() {
        return equipmentService.getLimitedEquipment();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/warehouse")
    @ResponseStatus(HttpStatus.OK)
    public WarehouseListDto getWarehouses() {
        WarehouseListDto warehouseListDto = new WarehouseListDto();
        warehouseListDto.setWarehouses(equipmentService.getWarehouses());
        return warehouseListDto;
    }


}
