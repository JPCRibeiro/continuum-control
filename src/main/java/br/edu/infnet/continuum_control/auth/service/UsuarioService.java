package br.edu.infnet.continuum_control.auth.service;

import br.edu.infnet.continuum_control.auth.domain.Usuario;
import br.edu.infnet.continuum_control.auth.repository.UsuarioRepository;
import br.edu.infnet.continuum_control.shared.api.EntidadeNaoLocalizadaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoLocalizadaException("Usuário não localizado"));
    }
}