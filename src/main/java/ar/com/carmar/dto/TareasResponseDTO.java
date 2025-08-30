package ar.com.carmar.dto;

import ar.com.carmar.entity.Tareas;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TareasResponseDTO {
    private Long id;
    private Long ordenId;
    private Long operacionId;
    private String operacionNombre;
    private String operacionNombreCorto;
    private Integer nroMaquina;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinalizacion;
    private Integer cantidad;
    private String observaciones;
    private String username;
    private Integer ordCantidad;
    private List<OrdenesDocumentosDTO> documentos;

    public TareasResponseDTO(Tareas tareas) {
        this.id = tareas.getTarId();
        this.ordenId = tareas.getOrden().getOrdId();
        this.operacionId = tareas.getOperacion().getOpeId();
        this.operacionNombre = tareas.getOperacion().getOpeNombre();
        this.operacionNombreCorto = tareas.getOperacion().getOpeNombreCorto();
        this.nroMaquina = tareas.getTarNroMaquina();
        this.fechaInicio = tareas.getTarFechaInicio();
        this.fechaFinalizacion = tareas.getTarFechaFin();
        this.cantidad = tareas.getTarCantidad();
        this.observaciones = tareas.getTarObservaciones();
        this.username = tareas.getAudUsrIns();
        this.ordCantidad = tareas.getOrden().getCantidad();
        this.documentos = tareas.getOrden().getOrdenesDocumentos()
                .stream()
                .map(doc -> {
                    OrdenesDocumentosDTO d = new OrdenesDocumentosDTO();
                    d.setOdoId(doc.getOdoId());
                    d.setOdoNombre(doc.getOdoNombre());
                    d.setOdoDriveUrl(doc.getOdoDriveUrl());
                    return d;
                })
                .toList();
    }
}
