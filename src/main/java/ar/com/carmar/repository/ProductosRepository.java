package ar.com.carmar.repository;

import ar.com.carmar.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductosRepository extends JpaRepository<Productos, Long> {
    Optional<Productos> findByPrdCodigoProducto(String codigoProducto);
}