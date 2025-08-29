package ar.com.carmar.service;

import ar.com.carmar.dto.TareasResponseDTO;
import ar.com.carmar.entity.Operaciones;
import ar.com.carmar.entity.Ordenes;
import ar.com.carmar.entity.Tareas;
import ar.com.carmar.entity.Usuarios;
import ar.com.carmar.repository.TareasRepository;
import ar.com.carmar.repository.UsuariosRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TareasService extends BaseService {

    private final TareasRepository tareasRepository;
    private final UsuariosRepository usuariosRepository;

    public TareasService (TareasRepository tareasRepository, UsuariosRepository usuariosRepository){
        this.tareasRepository = tareasRepository;
        this.usuariosRepository = usuariosRepository;
    }

    public TareasResponseDTO saveTareas(Long ordenId, Long operacionId, Integer nroMaquina){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios usuario = usuariosRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado: " + username));

        Tareas tarea = new Tareas(new Ordenes(ordenId), new Operaciones(operacionId), nroMaquina, usuario, LocalDateTime.now());
        auditar(tarea, usuario.getUsername());
        return new TareasResponseDTO(tareasRepository.save(tarea));
    }
}
