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
    private String nroPlan;
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
    private List<ProductoDocumentosDTO> documentos;

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

        var prod = (tareas.getOrden() != null) ? tareas.getOrden().getProducto() : null;
        if (prod != null && prod.getProductoDocumentos() != null) {
            this.nroPlan = tareas.getOrden().getOrdNroPlan();
            this.documentos = prod.getProductoDocumentos()
                    .stream()
                    .map(doc -> {
                        ProductoDocumentosDTO d = new ProductoDocumentosDTO();
                        d.setPdoId(doc.getPdoId());
                        d.setPdoNombre(doc.getPdoNombre());
                        d.setPdoDriveUrl(doc.getPdoDriveUrl());
                        return d;
                    })
                    .toList();
        } else {
            this.documentos = java.util.Collections.emptyList();
        }
    }
}
