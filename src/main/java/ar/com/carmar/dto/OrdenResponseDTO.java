package ar.com.carmar.dto;

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
public class OrdenResponseDTO {
    private Long id;
    private String ordNroPlan;
    private Integer anio;
    private String observacion;
    private Long clienteId;
    private String productoCodigo;
    private String productoDescripcion;
    private String situacionClave;
    private String hoja;
    private String etiqueta;
    private Integer cantidad;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinalizacion;
    private String ordenInterna;
    private List<OrdenesDocumentosDTO> ordenesDocumentosDTOs;
}
