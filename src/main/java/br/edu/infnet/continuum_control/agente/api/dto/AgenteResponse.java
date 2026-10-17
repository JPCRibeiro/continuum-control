package br.edu.infnet.continuum_control.agente.api.dto;

import br.edu.infnet.continuum_control.agente.domain.Agente;
import br.edu.infnet.continuum_control.agente.domain.Situacao;

public record AgenteResponse(
    Long id,
    String codigo,
    String codinome,
    String especialidade,
    Situacao situacao
) {
    public static AgenteResponse from(Agente agente) {
        return new AgenteResponse(
                agente.getId(),
                agente.getCodigo(),
                agente.getCodinome(),
                agente.getEspecialidade(),
                agente.getSituacao()
        );
    }
}
