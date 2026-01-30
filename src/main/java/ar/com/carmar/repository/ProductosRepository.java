package ar.com.carmar.repository;

import ar.com.carmar.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductosRepository extends JpaRepository<Productos, Long> {
    Optional<Productos> findByPrdCodigoProducto(String codigoProducto);

    @Query("SELECT p FROM Productos p WHERE LOWER(p.prdCodigoProducto) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
           "OR LOWER(p.prdDescripcion) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Productos> buscarPorCodigoODescripcion(@Param("filtro") String filtro);
}