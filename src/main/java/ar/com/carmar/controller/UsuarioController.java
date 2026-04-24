package ar.com.carmar.controller;

import ar.com.carmar.dto.PerfilResponseDTO;
import ar.com.carmar.dto.PerfilUpdateDTO;
import ar.com.carmar.dto.UsuarioAdminDTO;
import ar.com.carmar.dto.UsuarioAdminUpdateDTO;
import ar.com.carmar.dto.UsuarioCreateDTO;
import ar.com.carmar.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /* ── Perfil del usuario autenticado ── */

    @GetMapping("/perfil")
    public ResponseEntity<PerfilResponseDTO> getPerfil() {
        return ResponseEntity.ok(usuarioService.getPerfil());
    }

    @PutMapping("/perfil")
    public ResponseEntity<PerfilResponseDTO> updatePerfil(@RequestBody PerfilUpdateDTO dto) {
        return ResponseEntity.ok(usuarioService.updatePerfil(dto));
    }

    /* ── Administración de usuarios (sólo ADMIN) ── */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<UsuarioAdminDTO>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioAdminDTO>> getUsuariosActivos() {
        return ResponseEntity.ok(usuarioService.getUsuariosActivos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<UsuarioAdminDTO> createUsuario(@RequestBody UsuarioCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.createUsuario(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<UsuarioAdminDTO> getUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<UsuarioAdminDTO> updateUsuario(@PathVariable Long id,
                                                          @RequestBody UsuarioAdminUpdateDTO dto) {
        return ResponseEntity.ok(usuarioService.updateUsuarioAdmin(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}/baja")
    public ResponseEntity<Void> darDeBaja(@PathVariable Long id) {
        usuarioService.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}/alta")
    public ResponseEntity<Void> darDeAlta(@PathVariable Long id) {
        usuarioService.darDeAlta(id);
        return ResponseEntity.noContent().build();
    }

    /* ── Manejo de errores ── */

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }
}
