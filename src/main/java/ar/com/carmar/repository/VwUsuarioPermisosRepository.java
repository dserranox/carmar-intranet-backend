package ar.com.carmar.repository;

import ar.com.carmar.entity.VwUsuarioPermisos;
import ar.com.carmar.entity.VwUsuarioPermisosId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VwUsuarioPermisosRepository extends JpaRepository<VwUsuarioPermisos, VwUsuarioPermisosId> {

    @Query("select distinct v.prmClave from VwUsuarioPermisos v where lower(v.usrUsername) = lower(:username)")
    List<String> findPermisosClavesByUsername(@Param("username") String username);
}