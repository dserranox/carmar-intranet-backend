package ar.com.carmar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PERMISOS")
public class Permisos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRM_ID")
    private Long id;

    @Column(name = "PRM_CLAVE", nullable = false, length = 128)
    private String clave;

    @Column(name = "PRM_DESCRIPCION", length = 255)
    private String descripcion;
}
