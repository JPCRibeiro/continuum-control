package br.edu.infnet.continuum_control.ocorrencia.api.controller;

import br.edu.infnet.continuum_control.ocorrencia.api.dto.OcorrenciaResponse;
import br.edu.infnet.continuum_control.ocorrencia.api.dto.RegistrarOcorrenciaRequest;
import br.edu.infnet.continuum_control.ocorrencia.service.OcorrenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missoes/{missaoId}/ocorrencias")
@RequiredArgsConstructor
public class OcorrenciaController {
    private final OcorrenciaService ocorrenciaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OcorrenciaResponse registrar(@PathVariable Long missaoId,
                                        @Valid @RequestBody RegistrarOcorrenciaRequest req,
                                        @AuthenticationPrincipal Jwt jwt) {
        return OcorrenciaResponse.from(ocorrenciaService.registrar(
                missaoId, req.agenteId(), req.tipo(), req.descricao(), req.distorcao(), req.evidencia(),
                jwt.getSubject())
        );
    }

    @GetMapping
    public List<OcorrenciaResponse> listarPorMissao(@PathVariable Long missaoId) {
        return ocorrenciaService.listarPorMissao(missaoId)
                .stream()
                .map(OcorrenciaResponse::from)
                .toList();
    }
}
