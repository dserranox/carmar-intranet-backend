package ar.com.carmar.dto;

import ar.com.carmar.entity.Tareas;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TareasResponseDTO {
    private Long id;
    private Long ordenId;
    private Long operacionId;
    private String operacionNombre;
    private Integer nroMaquina;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinalizacion;
    private Integer cantidad;
    private String observaciones;

    public TareasResponseDTO(Tareas tareas) {
        this.id = tareas.getTarId();
        this.ordenId = tareas.getOrden().getOrdId();
        this.operacionId = tareas.getOperacion().getOpeId();
        this.operacionNombre = tareas.getOperacion().getOpeNombre();
        this.nroMaquina = tareas.getTarNroMaquina();
        this.fechaInicio = tareas.getTarFechaInicio();
        this.fechaFinalizacion = tareas.getTarFechaFin();
        this.cantidad = tareas.getTarCantidad();
        this.observaciones = tareas.getTarObservaciones();
    }
}
