package ar.com.carmar.repository;

import ar.com.carmar.entity.Tareas;
import ar.com.carmar.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TareasRepository extends JpaRepository<Tareas, Long>, JpaSpecificationExecutor<Tareas> {

    List<Tareas> findByUsuarioOrderByTarFechaInicioDesc(Usuarios usuario);

    List<Tareas> findByUsuario_IdOrderByTarFechaInicioDesc(Long usuarioId);

    List<Tareas> findAllByOrderByTarFechaInicioDesc();
}
