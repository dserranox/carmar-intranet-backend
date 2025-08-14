package ar.com.carmar.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ORDENES_DOCUMENTOS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdenesDocumentos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ODO_ID")
    private Long odoId;

    @Column(name="ODO_NOMBRE")
    private String odoNombre;

    @Column(name="ODO_DRIVE_URL", nullable = false, length = 64)
    private String odoDriveUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ODO_ORD_ID")
    @JsonBackReference
    private Ordenes ordenes;

}
