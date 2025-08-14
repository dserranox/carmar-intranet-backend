package ar.com.carmar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdenesExcelDTO {
    private String plan;
    private String ordenInterna;
    private String clienteId;
    private String productoSolicitado;
    private String codigoProducto;
    private String fechaIncio;
    private String cantidad;
    private String hoja;
    private String etiqueta;
    private String situacion;
    private String fechaFinalizacion;
    private String loteProduccion;
    private String series;
    private String observacion;
}
