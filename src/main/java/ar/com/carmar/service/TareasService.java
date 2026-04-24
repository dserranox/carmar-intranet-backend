package ar.com.carmar.service;

import ar.com.carmar.dto.OperarioEstadoDTO;
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

    public TareasResponseDTO saveTareas(Long ordenId, Long operacionId, Integer nroMaquina, String usuarioOperario){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios usuarioLogueado = usuariosRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado: " + username));

        Usuarios usuarioTarea = usuarioLogueado;
        if (usuarioOperario != null && !usuarioOperario.equalsIgnoreCase(username)) {
            usuarioTarea = usuariosRepository.findByUsernameIgnoreCase(usuarioOperario)
                    .orElseThrow(() -> new IllegalStateException("Usuario operario no encontrado: " + usuarioOperario));
        }

        if (tareasRepository.existsByUsuarioAndTarFechaFinIsNull(usuarioTarea)) {
            throw new IllegalStateException("El operario tiene una tarea en curso. Finalizala antes de iniciar una nueva.");
        }

        Tareas tarea = new Tareas(new Ordenes(ordenId), new Operaciones(operacionId), nroMaquina, usuarioTarea, LocalDateTime.now());
        auditar(tarea, usuarioLogueado.getUsername());

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

        tarea.setTarFechaFin(tareaDto.getFechaFinalizacion() != null ? tareaDto.getFechaFinalizacion() : LocalDateTime.now());
        tarea.setTarCantidad(tareaDto.getCantidad());
        tarea.setTarObservaciones(tareaDto.getObservaciones());
        tarea.setTarNoConforme(tareaDto.getNoConforme());
        tarea.setTarPerdidaRendimiento(tareaDto.getPerdidaRendimiento());
        tarea.setTarPerdidaMantenimiento(tareaDto.getPerdidaMantenimiento());
        tarea.setTarPerdidaCalidad(tareaDto.getPerdidaCalidad());
        auditar(tarea, usuario.getUsername());
        return new TareasResponseDTO(tareasRepository.save(tarea));
    }

    public List<OperarioEstadoDTO> getOperariosActivos() {
        return usuariosRepository.findByRoles_NombreAndActivoTrue("OPERARIO").stream()
                .map(usuario -> {
                    OperarioEstadoDTO dto = new OperarioEstadoDTO();
                    dto.setUsername(usuario.getUsername());
                    tareasRepository.findFirstByUsuarioAndTarFechaFinIsNull(usuario).ifPresent(tarea -> {
                        dto.setFechaInicio(tarea.getTarFechaInicio());
                        dto.setNroMaquina(tarea.getTarNroMaquina());
                        dto.setOperacionNombre(tarea.getOperacion().getOpeNombre());
                        Ordenes orden = tarea.getOrden();
                        dto.setNroPlan(orden.getOrdNroPlan());
                        if (orden.getProducto() != null) {
                            dto.setProductoCodigo(orden.getProducto().getPrdCodigoProducto());
                            dto.setProductoDescripcion(orden.getProducto().getPrdDescripcion());
                        }
                    });
                    return dto;
                })
                .sorted(java.util.Comparator.comparing(OperarioEstadoDTO::getUsername))
                .collect(Collectors.toList());
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
