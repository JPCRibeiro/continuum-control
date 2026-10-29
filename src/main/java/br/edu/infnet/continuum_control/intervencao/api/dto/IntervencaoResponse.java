package br.edu.infnet.continuum_control.intervencao.api.dto;

import br.edu.infnet.continuum_control.shared.Impacto;
import br.edu.infnet.continuum_control.intervencao.domain.Intervencao;

import java.time.LocalDateTime;

public record IntervencaoResponse(
        Long id,
        Long missaoId,
        Long agenteId,
        LocalDateTime ocorridaEm,
        String acao,
        String justificativa,
        Impacto impacto,
        String resultado
) {
    public static IntervencaoResponse from(Intervencao intervencao) {
        return new IntervencaoResponse(
                intervencao.getId(),
                intervencao.getMissao().getId(),
                intervencao.getAgente().getId(),
                intervencao.getOcorridaEm(),
                intervencao.getAcao(),
                intervencao.getJustificativa(),
                intervencao.getImpacto(),
                intervencao.getResultado()
        );
    }
}
