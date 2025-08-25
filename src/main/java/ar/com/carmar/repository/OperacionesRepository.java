package ar.com.carmar.repository;

import ar.com.carmar.entity.Operaciones;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OperacionesRepository extends JpaRepository<Operaciones, Long> {
    List<Operaciones> findAllByOrderByOpeOrdenAsc();
}