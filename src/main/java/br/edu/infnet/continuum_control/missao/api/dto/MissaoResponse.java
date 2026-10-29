package br.edu.infnet.continuum_control.missao.api.dto;

import br.edu.infnet.continuum_control.agente.api.dto.AgenteResponse;
import br.edu.infnet.continuum_control.missao.domain.Missao;
import br.edu.infnet.continuum_control.missao.domain.Prioridade;
import br.edu.infnet.continuum_control.missao.domain.SituacaoMissao;

import java.time.LocalDateTime;
import java.util.List;

public record MissaoResponse(
        Long id,
        String codigo,
        String objetivo,
        Long anomaliaId,
        LocalDateTime destinoTemporal,
        Prioridade prioridade,
        SituacaoMissao situacao,
        List<AgenteResponse> agentes
) {
    public static MissaoResponse from(Missao missao) {
        return new MissaoResponse(
                missao.getId(),
                missao.getCodigo(),
                missao.getObjetivo(),
                missao.getAnomalia().getId(),
                missao.getDestinoTemporal(),
                missao.getPrioridade(),
                missao.getSituacao(),
                missao.getAgentes().stream().map(AgenteResponse::from).toList()
        );
    }
}
