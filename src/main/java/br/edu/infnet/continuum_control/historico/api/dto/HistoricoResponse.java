package br.edu.infnet.continuum_control.historico.api.dto;

import br.edu.infnet.continuum_control.historico.domain.Historico;
import br.edu.infnet.continuum_control.historico.domain.TipoHistorico;

import java.time.LocalDateTime;

public record HistoricoResponse(
        Long id,
        TipoHistorico tipo,
        String descricao,
        LocalDateTime ocorridoEm
) {
    public static HistoricoResponse from(Historico historico) {
        return new HistoricoResponse(
                historico.getId(),
                historico.getTipo(),
                historico.getDescricao(),
                historico.getOcorridoEm()
        );
    }
}
