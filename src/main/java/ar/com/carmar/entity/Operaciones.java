package ar.com.carmar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "OPERACIONES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Operaciones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OPE_ID")
    private Long opeId;

    @Column(name = "OPE_NOMBRE", nullable = false, length = 255)
    private String opeNombre;

    @Column(name = "OPE_NOMBRE_CORTO", nullable = false, length = 10)
    private String opeNombreCorto;

    @Column(name = "OPE_DESCRIPCION", length = 255)
    private String opeDescripcion;

    @Column(name = "OPE_GRUPO", length = 100)
    private String opeGrupo;

    @Column(name = "OPE_ORDEN")
    private Integer opeOrden;

    public Operaciones(Long opeId) {
        this.opeId = opeId;
    }
}
