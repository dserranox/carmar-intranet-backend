package ar.com.carmar.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdenCreateDTO {

    // El año se calcula automáticamente (año en curso)
    private Integer anio;

    // El número de plan se genera automáticamente (YY-XXX)
    private String ordNroPlan;

    private String ordenInterna;

    private Long clienteId;

    private Long productoId;

    private String productoCodigo;

    private LocalDateTime fechaInicio;

    private Integer cantidad;

    private String hoja;

    private String etiqueta;

    private String situacionClave;

    private LocalDateTime fechaFinalizacion;

    private Integer loteProduccion;

    private Integer series;

    private String observacion;
}
