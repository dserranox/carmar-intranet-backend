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
public class UsuarioAdminDTO {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private LocalDateTime fechaCreacion;
    private Boolean activo;
}
