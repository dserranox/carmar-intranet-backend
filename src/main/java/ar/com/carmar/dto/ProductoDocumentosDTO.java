package ar.com.carmar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDocumentosDTO {
    private Long pdoId;
    private String pdoNombre;
    private String pdoDriveUrl;
//    private OrdenResponseDTO orden;

}
