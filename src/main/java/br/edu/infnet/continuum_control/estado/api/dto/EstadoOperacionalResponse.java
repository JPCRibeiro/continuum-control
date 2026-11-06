package br.edu.infnet.continuum_control.estado.api.dto;

import br.edu.infnet.continuum_control.estado.domain.EstadoLinhaTemporal;

public record EstadoOperacionalResponse(
        long agentesDisponiveis,
        long agentesEmMissao,
        long missoesEmExecucao,
        long anomaliasAtivas,
        long anomaliasCriticas,
        EstadoLinhaTemporal estadoLinhaTemporal,
        int indiceEstabilidade
) {}
