package ar.com.carmar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRODUCTOS", uniqueConstraints = @UniqueConstraint(columnNames = {"prdCodigoProducto"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Productos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRD_ID")
    private Long prdId;

    @Column(name = "PRD_CODIGO_PRODUCTO", length = 64)
    private String prdCodigoProducto;

    @Column(name = "PRD_DESCRIPCION", length = 255)
    private String prdDescripcion;
}
