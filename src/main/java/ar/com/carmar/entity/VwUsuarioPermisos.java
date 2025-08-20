package ar.com.carmar.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Getter @Setter @NoArgsConstructor
@Entity
@Immutable
@Table(name = "vw_usuario_permisos")
@IdClass(VwUsuarioPermisosId.class)
public class VwUsuarioPermisos {

    @Id
    @Column(name = "usr_id")
    private Long usrId;

    @Column(name = "usr_username")
    private String usrUsername;

    @Id
    @Column(name = "rol_nombre")
    private String rolNombre;

    @Id
    @Column(name = "prm_clave")
    private String prmClave;
}
