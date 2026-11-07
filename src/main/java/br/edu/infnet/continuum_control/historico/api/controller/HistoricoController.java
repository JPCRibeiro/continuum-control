package br.edu.infnet.continuum_control.historico.api.controller;

import br.edu.infnet.continuum_control.anomalia.service.AnomaliaService;
import br.edu.infnet.continuum_control.historico.api.dto.HistoricoResponse;
import br.edu.infnet.continuum_control.historico.service.HistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/anomalias/{anomaliaId}/historico")
@RequiredArgsConstructor
public class HistoricoController {
    private final HistoricoService historicoService;
    private final AnomaliaService anomaliaService;

    @GetMapping
    public List<HistoricoResponse> listarPorAnomalia(@PathVariable Long anomaliaId) {
        anomaliaService.buscarPorId(anomaliaId);

        return historicoService.listarPorAnomalia(anomaliaId)
                .stream()
                .map(HistoricoResponse::from)
                .toList();
    }
}
