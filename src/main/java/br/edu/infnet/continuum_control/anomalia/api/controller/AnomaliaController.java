package br.edu.infnet.continuum_control.anomalia.api.controller;

import br.edu.infnet.continuum_control.anomalia.api.dto.AnomaliaResponse;
import br.edu.infnet.continuum_control.anomalia.api.dto.AtualizarAnomaliaRequest;
import br.edu.infnet.continuum_control.anomalia.api.dto.ClassificarRiscoRequest;
import br.edu.infnet.continuum_control.anomalia.api.dto.RegistrarAnomaliaRequest;
import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import br.edu.infnet.continuum_control.anomalia.domain.SituacaoAnomalia;
import br.edu.infnet.continuum_control.anomalia.service.AnomaliaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anomalias")
@RequiredArgsConstructor
public class AnomaliaController {
    private final AnomaliaService anomaliaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnomaliaResponse registrar(@Valid @RequestBody RegistrarAnomaliaRequest req) {
        return AnomaliaResponse.from(anomaliaService.registrar(
                req.eventoId(), req.divergencia(), req.risco(), req.origem(), req.observacao())
        );
    }

    @GetMapping("/{id}")
    public AnomaliaResponse buscar(@PathVariable Long id) {
        return AnomaliaResponse.from(anomaliaService.buscarPorId(id));
    }

    @GetMapping("/ativas")
    public List<AnomaliaResponse> listarAtivas() {
        return anomaliaService.listarAtivas()
                .stream()
                .map(AnomaliaResponse::from)
                .toList();
    }

    @GetMapping("/criticas")
    public List<AnomaliaResponse> listarCriticas() {
        return anomaliaService.listarCriticas()
                .stream()
                .map(AnomaliaResponse::from)
                .toList();
    }

    @GetMapping
    public List<AnomaliaResponse> listar(@RequestParam(required = false) SituacaoAnomalia situacao,
                                         @RequestParam(required = false) Risco risco) {
        List<Anomalia> anomalias;

        if (situacao != null) {
            anomalias = anomaliaService.listarPorSituacao(situacao);
        } else if (risco != null) {
            anomalias = anomaliaService.listarPorRisco(risco);
        } else {
            anomalias = anomaliaService.listar();
        }

        return anomalias
                .stream()
                .map(AnomaliaResponse::from)
                .toList();
    }

    @PutMapping("/{id}")
    public AnomaliaResponse atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarAnomaliaRequest req) {
        return AnomaliaResponse.from(
                anomaliaService.atualizar(id, req.divergencia(), req.origem(), req.observacao())
        );
    }

    @PatchMapping("/{id}/risco")
    public AnomaliaResponse classificarRisco(@PathVariable Long id,
                                             @Valid @RequestBody ClassificarRiscoRequest req) {
        return AnomaliaResponse.from(anomaliaService.classificarRisco(id, req.risco()));
    }

    @PatchMapping("/{id}/analise")
    public AnomaliaResponse analisar(@PathVariable Long id) {
        return AnomaliaResponse.from(anomaliaService.analisar(id));
    }

    @PatchMapping("/{id}/irreversivel")
    public AnomaliaResponse tornarIrreversivel(@PathVariable Long id) {
        return AnomaliaResponse.from(anomaliaService.tornarIrreversivel(id));
    }
}
