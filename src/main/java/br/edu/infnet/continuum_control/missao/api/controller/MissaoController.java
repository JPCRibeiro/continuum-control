package br.edu.infnet.continuum_control.missao.api.controller;

import br.edu.infnet.continuum_control.missao.api.dto.*;
import br.edu.infnet.continuum_control.missao.domain.Missao;
import br.edu.infnet.continuum_control.missao.domain.SituacaoMissao;
import br.edu.infnet.continuum_control.missao.service.MissaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missoes")
@RequiredArgsConstructor
public class MissaoController {
    private final MissaoService missaoService;

    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR', 'ADMINISTRADOR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MissaoResponse registrar(@Valid @RequestBody RegistrarMissaoRequest req) {
        return MissaoResponse.from(missaoService.registrar(
                req.objetivo(), req.anomaliaId(), req.destinoTemporal(), req.prioridade())
        );
    }

    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR', 'ADMINISTRADOR')")
    @PostMapping("/{id}/agentes")
    public MissaoResponse designar(@PathVariable Long id, @Valid @RequestBody DesignarAgenteRequest req) {
        return MissaoResponse.from(missaoService.designar(id, req.agenteId()));
    }

    @PostMapping("/{id}/iniciar")
    public MissaoResponse iniciar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return MissaoResponse.from(missaoService.iniciar(id, jwt.getSubject()));
    }

    @PreAuthorize("hasAnyRole('SUPERVISOR', 'ADMINISTRADOR')")
    @PostMapping("/{id}/autorizar")
    public AutorizacaoResponse autorizar(@PathVariable Long id, @Valid @RequestBody AutorizarMissaoRequest req) {
        return AutorizacaoResponse.from(missaoService.autorizar(id, req.responsavel(), req.observacao()));
    }

    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR', 'ADMINISTRADOR')")
    @PostMapping("/{id}/concluir")
    @ResponseStatus(HttpStatus.CREATED)
    public EncerramentoResponse concluir(@PathVariable Long id, @Valid @RequestBody EncerrarMissaoRequest req) {
        return EncerramentoResponse.from(missaoService.concluir(id, req.resumo(), req.impacto(), req.observacao()));
    }

    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR', 'ADMINISTRADOR')")
    @PostMapping("/{id}/falhar")
    @ResponseStatus(HttpStatus.CREATED)
    public EncerramentoResponse falhar(@PathVariable Long id, @Valid @RequestBody EncerrarMissaoRequest req) {
        return EncerramentoResponse.from(missaoService.falhar(id, req.resumo(), req.impacto(), req.observacao()));
    }

    @PreAuthorize("hasAnyRole('OPERADOR', 'SUPERVISOR', 'ADMINISTRADOR')")
    @PostMapping("/{id}/cancelar")
    public MissaoResponse cancelar(@PathVariable Long id) {
        return MissaoResponse.from(missaoService.cancelar(id));
    }

    @GetMapping("/{id}")
    public MissaoResponse buscarPorId(@PathVariable Long id) {
        return MissaoResponse.from(missaoService.buscarPorId(id));
    }

    @GetMapping("/execucao")
    public List<MissaoResponse> listarEmExecucao() {
        return missaoService.listarEmExecucao()
                .stream()
                .map(MissaoResponse::from)
                .toList();
    }

    @GetMapping
        public List<MissaoResponse> listar(@RequestParam(required = false ) SituacaoMissao situacao,
                                           @RequestParam(required = false ) Long anomaliaId,
                                           @RequestParam(required = false) Long agenteId) {
        List<Missao> missoes;

        if (situacao != null) {
            missoes =  missaoService.listarPorSituacao(situacao);
        } else if (anomaliaId != null) {
            missoes = missaoService.listarPorAnomalia(anomaliaId);
        } else if (agenteId != null) {
            missoes = missaoService.listarPorAgente(agenteId);
        } else {
            missoes = missaoService.listar();
        }

        return missoes
                .stream()
                .map(MissaoResponse::from)
                .toList();
    }
}
