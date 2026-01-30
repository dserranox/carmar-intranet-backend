package ar.com.carmar.controller;

import ar.com.carmar.dto.ClienteResponseDTO;
import ar.com.carmar.service.ClientesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

    private final ClientesService clientesService;

    public ClientesController(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes() {
        return ResponseEntity.ok(clientesService.getAllClientes());
    }
}
