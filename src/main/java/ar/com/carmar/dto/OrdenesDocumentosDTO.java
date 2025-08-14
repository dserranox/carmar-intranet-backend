package ar.com.carmar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdenesDocumentosDTO {
    private Long odoId;
    private String odoNombre;
    private String odoDriveUrl;
    private OrdenResponseDTO orden;

}
