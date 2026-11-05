package br.edu.infnet.continuum_control.auth.repository;

import br.edu.infnet.continuum_control.auth.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
