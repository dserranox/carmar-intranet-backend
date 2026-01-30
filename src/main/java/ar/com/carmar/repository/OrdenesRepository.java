package ar.com.carmar.repository;

import ar.com.carmar.entity.Ordenes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrdenesRepository extends JpaRepository<Ordenes, Long> {
    Optional<Ordenes> findByOrdNroPlan(String plan);

    List<Ordenes> findByOrdAnioOrderByOrdNroPlanAsc(Integer anio);

    @Query("SELECT o FROM Ordenes o WHERE o.ordAnio = :anio ORDER BY o.ordNroPlan DESC LIMIT 1")
    Optional<Ordenes> findLastByAnio(@Param("anio") Integer anio);
}
