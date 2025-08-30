package ar.com.carmar.controller;

import ar.com.carmar.dto.TareasResponseDTO;
import ar.com.carmar.service.TareasService;
import org.springframework.http.ResponseEntity;
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


    @PostMapping()
    public ResponseEntity<TareasResponseDTO> inciarTarea(
            @RequestBody TareasResponseDTO tareaDto
    ) throws IOException {
        return ResponseEntity.ok(tareasService.saveTareas(tareaDto.getOrdenId(), tareaDto.getOperacionId(), tareaDto.getNroMaquina()));
    }

    @PostMapping("/finalizar-tarea")
    public ResponseEntity<TareasResponseDTO> finalizarTarea(
            @RequestBody TareasResponseDTO tareaDto
    ) throws IOException {
        return ResponseEntity.ok(tareasService.finalizarTareas(tareaDto));
    }
}
