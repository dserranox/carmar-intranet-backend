package ar.com.carmar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperarioEstadoDTO {
    private String username;
    private LocalDateTime fechaInicio;
    private String nroPlan;
    private String productoCodigo;
    private String productoDescripcion;
    private String operacionNombre;
    private Integer nroMaquina;
}
