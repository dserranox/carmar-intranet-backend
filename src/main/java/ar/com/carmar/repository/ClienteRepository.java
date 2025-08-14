package ar.com.carmar.repository;

import ar.com.carmar.entity.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Clientes, Long> {
    Optional<Clientes> findByCliCuit(String cuit);
}