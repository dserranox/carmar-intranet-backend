package ar.com.carmar.repository;

import ar.com.carmar.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    Optional<Usuarios> findByUsernameIgnoreCase(String username);
}
