package ar.com.carmar.service;

import ar.com.carmar.dto.ProductoDocumentosDTO;
import ar.com.carmar.dto.TareasResponseDTO;
import ar.com.carmar.entity.*;
import ar.com.carmar.repository.OrdenesRepository;
import ar.com.carmar.repository.TareasRepository;
import ar.com.carmar.repository.UsuariosRepository;
import ar.com.carmar.repository.specifications.TareasSpecifications;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TareasService extends BaseService {

    private final TareasRepository tareasRepository;
    private final UsuariosRepository usuariosRepository;
    private final OrdenesRepository ordenesRepository;

    public TareasService (TareasRepository tareasRepository, UsuariosRepository usuariosRepository,
                          OrdenesRepository ordenesRepository){
        this.tareasRepository = tareasRepository;
        this.usuariosRepository = usuariosRepository;
        this.ordenesRepository = ordenesRepository;
    }

    public TareasResponseDTO saveTareas(Long ordenId, Long operacionId, Integer nroMaquina){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios usuario = usuariosRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado: " + username));

        Tareas tarea = new Tareas(new Ordenes(ordenId), new Operaciones(operacionId), nroMaquina, usuario, LocalDateTime.now());
        auditar(tarea, usuario.getUsername());

        TareasResponseDTO resposeDTO = new TareasResponseDTO(tareasRepository.save(tarea));

        Ordenes orden = ordenesRepository.findById(ordenId).orElseThrow(() -> new IllegalStateException("Orden no encontrada: " + ordenId));

        if(orden.getProducto() != null) {
            resposeDTO.setDocumentos(orden.getProducto().getProductoDocumentos()
                    .stream()
                    .map(doc -> {
                        ProductoDocumentosDTO d = new ProductoDocumentosDTO();
                        d.setPdoId(doc.getPdoId());
                        d.setPdoNombre(doc.getPdoNombre());
                        d.setPdoDriveUrl(doc.getPdoDriveUrl());
                        return d;
                    })
                    .toList());
        }
        return resposeDTO;
    }

    public TareasResponseDTO finalizarTareas(TareasResponseDTO tareaDto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios usuario = usuariosRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado: " + username));

        Tareas tarea = tareasRepository.findById(tareaDto.getId()).orElseThrow();

        tarea.setTarFechaFin(LocalDateTime.now());
        tarea.setTarCantidad(tareaDto.getCantidad());
        tarea.setTarObservaciones(tareaDto.getObservaciones());
        auditar(tarea, usuario.getUsername());
        return new TareasResponseDTO(tareasRepository.save(tarea));
    }

    public List<TareasResponseDTO> getTareasByUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();

        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a ->
                        "TAREAS:READ_ALL".equals(a.getAuthority())
        );

        String usernameFiltro = isAdmin ? null : currentUser;

        return tareasRepository.findAll(TareasSpecifications.byUserName(usernameFiltro), Sort.by("tarFechaInicio").descending()).stream().map(tarea -> {
            return new TareasResponseDTO(tarea);
        }).collect(Collectors.toList());
    }
}
