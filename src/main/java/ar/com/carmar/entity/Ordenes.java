package ar.com.carmar.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ordenes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ordenes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORD_ID")
    private Long ordId;

    @Column(name="ORD_ANIO")
    private Integer ordAnio;

    @Column(name="ORD_NRO_PLAN", nullable = false, length = 64)
    private String ordNroPlan;

    @Column(name="ORD_ORDEN_INTERNA")
    private String ordenInterna;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ORD_CLI_ID")
    private Clientes cliente;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ORD_PRD_ID")
    private Productos producto;

    @Column(name="ORD_FECHA_INICIO")
    private LocalDateTime fechaIncio;

    @Column(name="ORD_CANTIDAD")
    private Integer cantidad;

    @Column(name="ORD_HOJA")
    private String hoja;

    @Column(name="ORD_ETIQUETA")
    private String etiqueta;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "ORD_SIT_ID")
    private Situaciones situacion;

    @Column(name="ORD_FECHA_FINALIZACION")
    private LocalDateTime fechaFinalizacion;

    @Column(name="ORD_LOTE_PRODUCCION")
    private Integer loteProduccion;

    @Column(name="ORD_SERIES")
    private Integer series;

    @Column(name="ORD_OBSERVACION", length = 1024)
    private String ordObservacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ordenes")
    @JsonManagedReference
    private Set<OrdenesDocumentos> ordenesDocumentos = new HashSet<>();

    public Ordenes(Long ordId) {
        this.ordId = ordId;
    }
}
