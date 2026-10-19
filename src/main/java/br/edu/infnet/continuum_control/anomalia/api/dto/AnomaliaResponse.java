package br.edu.infnet.continuum_control.anomalia.api.dto;

import br.edu.infnet.continuum_control.anomalia.domain.Anomalia;
import br.edu.infnet.continuum_control.anomalia.domain.Risco;
import br.edu.infnet.continuum_control.anomalia.domain.SituacaoAnomalia;

import java.time.LocalDateTime;

public record AnomaliaResponse(
        Long id,
        Long eventoId,
        String divergencia,
        LocalDateTime detectadaEm,
        Risco risco,
        SituacaoAnomalia situacao,
        String origem,
        String observacao
) {
    public static AnomaliaResponse from(Anomalia anomalia) {
        return new AnomaliaResponse(
                anomalia.getId(),
                anomalia.getEvento().getId(),
                anomalia.getDivergencia(),
                anomalia.getDetectadaEm(),
                anomalia.getRisco(),
                anomalia.getSituacao(),
                anomalia.getOrigem(),
                anomalia.getObservacao()
        );
    }
}
