package ar.com.carmar.repository;

import ar.com.carmar.entity.Ordenes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdenesRepository extends JpaRepository<Ordenes, Long> {
    Optional<Ordenes> findByOrdNroPlan(String plan);

    List<Ordenes> findByOrdAnioOrderByOrdNroPlanAsc(Integer anio);
}
