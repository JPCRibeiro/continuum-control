package br.edu.infnet.continuum_control.auth.service;

import br.edu.infnet.continuum_control.auth.api.dto.LoginRequest;
import br.edu.infnet.continuum_control.auth.api.dto.LoginResponse;
import br.edu.infnet.continuum_control.auth.domain.Usuario;
import br.edu.infnet.continuum_control.auth.repository.UsuarioRepository;
import br.edu.infnet.continuum_control.shared.api.CredenciaisInvalidasException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public LoginResponse login(LoginRequest req) {
        Usuario usuario = usuarioRepository.findByEmail(req.email().toLowerCase().trim())
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!passwordEncoder.matches(req.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException();
        }

        return new LoginResponse(tokenService.gerarAccessToken(usuario));
    }
}