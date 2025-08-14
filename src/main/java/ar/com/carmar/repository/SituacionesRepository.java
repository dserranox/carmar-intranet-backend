package ar.com.carmar.repository;

import ar.com.carmar.entity.Situaciones;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SituacionesRepository extends JpaRepository<Situaciones, Long> {
    Optional<Situaciones> findBySitEstadoClaveIgnoreCase(String estadoClave);
}