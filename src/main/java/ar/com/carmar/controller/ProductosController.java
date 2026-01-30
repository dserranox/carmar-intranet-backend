package ar.com.carmar.controller;

import ar.com.carmar.dto.ProductoResponseDTO;
import ar.com.carmar.service.ProductosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductosController {

    private final ProductosService productosService;

    public ProductosController(ProductosService productosService) {
        this.productosService = productosService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> buscarProductos(@RequestParam String filtro) {
        return ResponseEntity.ok(productosService.buscarProductos(filtro));
    }
}
