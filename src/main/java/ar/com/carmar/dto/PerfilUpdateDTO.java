package ar.com.carmar.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PerfilUpdateDTO {
    private String email;
    private String passwordActual;
    private String passwordNueva;
}
