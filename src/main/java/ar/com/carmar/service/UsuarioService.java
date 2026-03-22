package ar.com.carmar.service;

import ar.com.carmar.dto.PerfilResponseDTO;
import ar.com.carmar.dto.PerfilUpdateDTO;
import ar.com.carmar.dto.UsuarioAdminDTO;
import ar.com.carmar.dto.UsuarioAdminUpdateDTO;
import ar.com.carmar.dto.UsuarioCreateDTO;
import ar.com.carmar.entity.Roles;
import ar.com.carmar.entity.Usuarios;
import ar.com.carmar.repository.RolesRepository;
import ar.com.carmar.repository.UsuariosRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService extends BaseService {

    private final UsuariosRepository usuariosRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuariosRepository usuariosRepository,
                          RolesRepository rolesRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public PerfilResponseDTO getPerfil() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios usuario = usuariosRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + username));
        return new PerfilResponseDTO(usuario.getUsername(), usuario.getEmail());
    }

    @Transactional
    public PerfilResponseDTO updatePerfil(PerfilUpdateDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios usuario = usuariosRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + username));

        if (StringUtils.hasText(dto.getEmail())) {
            usuario.setEmail(dto.getEmail().trim());
        }

        if (StringUtils.hasText(dto.getPasswordNueva())) {
            if (!StringUtils.hasText(dto.getPasswordActual())) {
                throw new IllegalArgumentException("Debés ingresar tu contraseña actual para cambiarla.");
            }
            if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getPassword())) {
                throw new IllegalArgumentException("La contraseña actual es incorrecta.");
            }
            usuario.setPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        }

        auditar(usuario, username);
        usuariosRepository.save(usuario);
        return new PerfilResponseDTO(usuario.getUsername(), usuario.getEmail());
    }

    @Transactional(readOnly = true)
    public List<UsuarioAdminDTO> getUsuarios() {
        return usuariosRepository.findAll().stream()
                .sorted((a, b) -> a.getUsername().compareToIgnoreCase(b.getUsername()))
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioAdminDTO getUsuarioById(Long id) {
        Usuarios usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + id));
        return toDTO(usuario);
    }

    @Transactional
    public UsuarioAdminDTO createUsuario(UsuarioCreateDTO dto) {
        String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!StringUtils.hasText(dto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (usuariosRepository.findByUsernameIgnoreCase(dto.getUsername().trim()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }
        if (!StringUtils.hasText(dto.getEmail())) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        validateEmailDomain(dto.getEmail());
        if (!StringUtils.hasText(dto.getPassword())) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        Usuarios usuario = new Usuarios();
        usuario.setUsername(dto.getUsername().trim());
        usuario.setEmail(dto.getEmail().trim().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setActivo(true);

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            usuario.setRoles(resolveRoles(dto.getRoles()));
        }

        auditar(usuario, adminUsername);
        usuariosRepository.save(usuario);
        return toDTO(usuario);
    }

    @Transactional
    public UsuarioAdminDTO updateUsuarioAdmin(Long id, UsuarioAdminUpdateDTO dto) {
        String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + id));

        if (dto.getEmail() != null) {
            if (StringUtils.hasText(dto.getEmail())) {
                validateEmailDomain(dto.getEmail());
                usuario.setEmail(dto.getEmail().trim().toLowerCase());
            } else {
                usuario.setEmail(null);
            }
        }

        if (dto.getRoles() != null) {
            usuario.setRoles(resolveRoles(dto.getRoles()));
        }

        if (StringUtils.hasText(dto.getPasswordNueva())) {
            usuario.setPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        }

        auditar(usuario, adminUsername);
        usuariosRepository.save(usuario);
        return toDTO(usuario);
    }

    @Transactional
    public void darDeBaja(Long id) {
        String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + id));
        usuario.setActivo(false);
        auditar(usuario, adminUsername);
        usuariosRepository.save(usuario);
    }

    @Transactional
    public void darDeAlta(Long id) {
        String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuarios usuario = usuariosRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado: " + id));
        usuario.setActivo(true);
        auditar(usuario, adminUsername);
        usuariosRepository.save(usuario);
    }

    private UsuarioAdminDTO toDTO(Usuarios u) {
        return new UsuarioAdminDTO(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getRoles().stream().map(Roles::getNombre).sorted().toList(),
                u.getAudFechaIns(),
                u.getActivo()
        );
    }

    private void validateEmailDomain(String email) {
        if (!email.trim().toLowerCase().endsWith("@car-mar.com")) {
            throw new IllegalArgumentException("El email debe pertenecer al dominio @car-mar.com.");
        }
    }

    private Set<Roles> resolveRoles(List<String> nombres) {
        return nombres.stream()
                .map(nombre -> rolesRepository.findByNombreIgnoreCase(nombre)
                        .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + nombre)))
                .collect(Collectors.toSet());
    }
}
