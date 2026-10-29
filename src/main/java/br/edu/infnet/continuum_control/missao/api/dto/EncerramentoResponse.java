package br.edu.infnet.continuum_control.missao.api.dto;

import br.edu.infnet.continuum_control.anomalia.domain.SituacaoAnomalia;
import br.edu.infnet.continuum_control.missao.domain.Encerramento;
import br.edu.infnet.continuum_control.shared.Impacto;
import br.edu.infnet.continuum_control.missao.domain.ResultadoMissao;

public record EncerramentoResponse(
        Long id,
        Long missaoId,
        ResultadoMissao resultadoMissao,
        String resumo,
        Impacto impacto,
        SituacaoAnomalia situacaoFinalAnomalia,
        String observacao
) {
    public static EncerramentoResponse from(Encerramento encerramento) {
        return new EncerramentoResponse(
                encerramento.getId(),
                encerramento.getMissao().getId(),
                encerramento.getResultado(),
                encerramento.getResumo(),
                encerramento.getImpacto(),
                encerramento.getSituacaoFinalAnomalia(),
                encerramento.getObservacao()
        );
    }
}
