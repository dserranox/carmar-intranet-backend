package ar.com.carmar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ROLES")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROL_ID")
    private Long id;

    @Column(name = "ROL_NOMBRE", nullable = false, length = 64)
    private String nombre;

    @Column(name = "ROL_DESCRIPCION", length = 255)
    private String descripcion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "ROLES_PERMISOS",
            joinColumns = @JoinColumn(name = "RP_ROL_ID"),
            inverseJoinColumns = @JoinColumn(name = "RP_PRM_ID"))
    private Set<Permisos> permisos = new HashSet<>();
}
