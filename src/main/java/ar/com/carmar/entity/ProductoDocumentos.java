package ar.com.carmar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PRODUCTO_DOCUMENTOS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDocumentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PDO_ID")
    private Long pdoId;

    @Column(name="PDO_NOMBRE")
    private String pdoNombre;

    @Column(name="PDO_DRIVE_URL", nullable = false)
    private String pdoDriveUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PDO_PRD_ID")
    @JsonBackReference
    private Productos producto;

}
