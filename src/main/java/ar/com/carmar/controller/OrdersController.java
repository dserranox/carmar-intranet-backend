package ar.com.carmar.controller;

import ar.com.carmar.dto.OrdenResponseDTO;
import ar.com.carmar.service.ExcelOrderService;
import ar.com.carmar.service.OrdenesService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Validated
public class OrdersController {

    private final ExcelOrderService excelOrderService;
    private final OrdenesService ordenesService;

    public OrdersController(ExcelOrderService excelOrderService, OrdenesService ordenesService) {
        this.excelOrderService = excelOrderService;
        this.ordenesService = ordenesService;
    }

    @GetMapping()
    public ResponseEntity<List<OrdenResponseDTO>> getAll(
            @RequestParam @NotNull Integer year
    ) throws IOException {
        return ResponseEntity.ok(ordenesService.getAllByYear(year));
    }

    /**
     * GET /api/orders?year=2025
     * GET /api/orders?year=2025&path=/ruta/al/archivo.xlsx
     */
    @GetMapping("/from-excel")
    public ResponseEntity<List<Map<String, Object>>> getOrders(
            @RequestParam @NotNull Integer year,
            @RequestParam(required = false) String path
    ) throws IOException {
        List<Map<String, Object>> rows = excelOrderService.readOrders(year, path);
        return ResponseEntity.ok(rows);
    }

    @PostMapping("/sync-from-excel")
    public ResponseEntity<Integer> syncFromExcel(
            @RequestParam @NotNull Integer year,
            @RequestParam(required = false) String path
    ) throws IOException {
        int count = ordenesService.bulkSaveFromDtos(year, path);
        return ResponseEntity.ok(count);
    }
}
