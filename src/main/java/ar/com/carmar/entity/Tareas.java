package ar.com.carmar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "TAREAS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tareas implements IAuditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAR_ID")
    private Long tarId;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "TAR_ORD_ID")
    private Ordenes orden;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "TAR_OPE_ID")
    private Operaciones operacion;

    @Column(name = "TAR_NRO_MAQUINA")
    private Integer tarNroMaquina;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "TAR_USR_ID")
    private Usuarios usuario;

    @Column(name = "TAR_FECHA_INICIO")
    private LocalDateTime tarFechaInicio;

    @Column(name = "TAR_FECHA_FIN")
    private LocalDateTime tarFechaFin;

    @Column(name = "TAR_CANTIDAD")
    private Integer tarCantidad;

    @Column(name = "TAR_OBSERVACIONES")
    private String tarObservaciones;

    @Column(name = "AUD_USR_INS", nullable = false, length = 250)
    private String audUsrIns;
    @Column(name = "AUD_USR_UPD", length = 250)
    private String audUsrUpd;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AUD_FECHA_INS", nullable = false, length = 7)
    private Date audFechaIns;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "AUD_FECHA_UPD", length = 7)
    private Date audFechaUpd;

    public Tareas(Ordenes orden, Operaciones operacion, Integer tarNroMaquina, Usuarios usuario, LocalDateTime tarFechaInicio) {
        this.orden = orden;
        this.operacion = operacion;
        this.tarNroMaquina = tarNroMaquina;
        this.usuario = usuario;
        this.tarFechaInicio = tarFechaInicio;
    }
}
