package ar.com.carmar.repository;

import ar.com.carmar.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByNombreIgnoreCase(String nombre);
}
