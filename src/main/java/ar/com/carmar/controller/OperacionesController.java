package ar.com.carmar.controller;

import ar.com.carmar.dto.OrdenResponseDTO;
import ar.com.carmar.entity.Operaciones;
import ar.com.carmar.service.ExcelOrderService;
import ar.com.carmar.service.OperacionesService;
import ar.com.carmar.service.OrdenesService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/operaciones")
@Validated
public class OperacionesController {

    private final OperacionesService operacionesService;

    public OperacionesController(OperacionesService operacionesService) {
        this.operacionesService = operacionesService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Operaciones>> getAllByOrden() {
        return ResponseEntity.ok(operacionesService.getAllOrderByOpeOrden());
    }

}
