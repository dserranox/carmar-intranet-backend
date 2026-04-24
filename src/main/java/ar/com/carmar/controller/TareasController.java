package ar.com.carmar.controller;

import ar.com.carmar.dto.OperarioEstadoDTO;
import ar.com.carmar.dto.TareasResponseDTO;
import ar.com.carmar.service.TareasService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tareas")
@Validated
public class TareasController {

    private final TareasService tareasService;
    public TareasController(TareasService tareasService) {
        this.tareasService = tareasService;
    }

    @GetMapping("/byUsername")
    public ResponseEntity<List<TareasResponseDTO>> getAll() throws IOException {
        return ResponseEntity.ok(tareasService.getTareasByUser());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/operarios-activos")
    public ResponseEntity<List<OperarioEstadoDTO>> getOperariosActivos() {
        return ResponseEntity.ok(tareasService.getOperariosActivos());
    }


    @PostMapping()
    public ResponseEntity<TareasResponseDTO> inciarTarea(
            @RequestBody TareasResponseDTO tareaDto
    ) throws IOException {
        return ResponseEntity.ok(tareasService.saveTareas(tareaDto.getOrdenId(), tareaDto.getOperacionId(), tareaDto.getNroMaquina(), tareaDto.getUsuarioOperario()));
    }

    @PostMapping("/finalizar-tarea")
    public ResponseEntity<TareasResponseDTO> finalizarTarea(
            @RequestBody TareasResponseDTO tareaDto
    ) throws IOException {
        return ResponseEntity.ok(tareasService.finalizarTareas(tareaDto));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
    }
}
