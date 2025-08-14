package ar.com.carmar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CLIENTES", uniqueConstraints = @UniqueConstraint(columnNames = {"cliCuit"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Clientes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLI_ID")
    private Long cliId;

    @Column(name="CLI_RAZON_SOCIAL", length = 255)
    private String cliRazonSocial;

    @Column(name="CLI_CUIT", length = 20)
    private String cliCuit;
}
