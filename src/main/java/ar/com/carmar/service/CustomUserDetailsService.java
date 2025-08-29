package ar.com.carmar.service;

import ar.com.carmar.entity.Usuarios;
import ar.com.carmar.repository.UsuariosRepository;
import ar.com.carmar.repository.VwUsuarioPermisosRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuariosRepository usuariosRepository;
    private final VwUsuarioPermisosRepository permisosRepository;

    public CustomUserDetailsService(UsuariosRepository usuariosRepository,
                                    VwUsuarioPermisosRepository permisosRepository) {
        this.usuariosRepository = usuariosRepository;
        this.permisosRepository = permisosRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuarios u = usuariosRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        if (Boolean.FALSE.equals(u.getActivo())) throw new UsernameNotFoundException("Usuario inactivo");

        List<GrantedAuthority> authorities = permisosRepository.findPermisosClavesByUsername(u.getUsername())
                .stream()
                .distinct()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(authorities)
                .accountExpired(false).accountLocked(false).credentialsExpired(false).disabled(false)
                .build();
    }
}
