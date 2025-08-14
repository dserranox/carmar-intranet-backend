package ar.com.carmar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SITUACIONES", uniqueConstraints = @UniqueConstraint(columnNames = {"sitEstadoClave"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Situaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SIT_ID")
    private Long sitId;

    @Column(name = "SIT_ESTADO_CLAVE", nullable = false, length = 64)
    private String sitEstadoClave;

    @Column(name = "SIR_DESCRIPCION", length = 255)
    private String sitDescripcion;
}
