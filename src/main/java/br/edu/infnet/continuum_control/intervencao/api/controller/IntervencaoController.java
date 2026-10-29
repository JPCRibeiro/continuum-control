package br.edu.infnet.continuum_control.intervencao.api.controller;

import br.edu.infnet.continuum_control.intervencao.api.dto.IntervencaoResponse;
import br.edu.infnet.continuum_control.intervencao.api.dto.RegistrarIntervencaoRequest;
import br.edu.infnet.continuum_control.intervencao.service.IntervencaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missoes/{missaoId}/intervencoes")
@RequiredArgsConstructor
public class IntervencaoController {
    private final IntervencaoService intervencaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IntervencaoResponse registrar(@PathVariable Long missaoId,
                                         @Valid @RequestBody RegistrarIntervencaoRequest req) {
        return IntervencaoResponse.from(intervencaoService.registrar(
                missaoId, req.agenteId(), req.acao(), req.justificativa(), req.impacto(), req.resultado()
        ));
    }

    @GetMapping
    public List<IntervencaoResponse> listarPorMissao(@PathVariable Long missaoId) {
        return intervencaoService.listarPorMissao(missaoId)
                .stream()
                .map(IntervencaoResponse::from)
                .toList();
    }
}
